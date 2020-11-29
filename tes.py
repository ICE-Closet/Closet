from multiprocessing import Process, Queue
import RPi.GPIO as GPIO
import time
import datetime
import cv2
import socket
import numpy
import json
import sys
import base64
from apds9960.const import *
from apds9960 import APDS9960
import smbus
from tensorflow.keras.preprocessing.image import img_to_array
from tensorflow.keras.models import load_model
import tensorflow as tf

class Mycloset():
    def __init__(self):
        # socket port
        self.PORT = 40000
        self.PORT2 = 65000
        self.HOST = ''
        self.MLHOST = '220.67.124.120'

        # sensor pin
        GPIO.setmode(GPIO.BCM)
        GPIO.setwarnings(False)

        # light setup
        self.LIGHT = 26
        
        # gesture setup
        self.bus = smbus.SMBus(1)
        self.apds = APDS9960(self.bus)
        self.GESTURE = 7
        self.direction = {
            APDS9960_DIR_NONE: "none",
            APDS9960_DIR_LEFT: "left",
            APDS9960_DIR_RIGHT: "right",
            APDS9960_DIR_NEAR: "near",
            APDS9960_DIR_FAR: "far",
        }

        # ultrasonic distance
        self.dis1, self.dis2 = 0, 0

        # token
        self.token = ''

    def receiveToken(self): #receive user token from DB
        s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        print('Socket created')
        try:
            s.bind((self.HOST, self.PORT))
        except socket.error as msg:
            print('Bind Failed. Error code: ' + str(msg[0]) + ' Message: ' + msg[1])
            sys.exit()
        print('Socket bind complete')
        s.listen(10)
        print('Socket now listening')
        conn, addr = s.accept()
        print('Connected with ' + addr[0] + ':' + str(addr[1]))
        data = conn.recv(1024)
        conn.sendall(data)
        print(data.decode())
        tok = data.decode()
        conn.close()
        s.close()
        return tok

    def sendML(self, userToken, check): #send clothes info to ML Server
        client_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        client_socket.connect((self.MLHOST, self.PORT2))
        token = userToken
        try:
            capture = cv2.VideoCapture(0)
            ret, frame = capture.read()
            cv2.imwrite("dd.jpg",frame)
            encode_param = [int(cv2.IMWRITE_JPEG_QUALITY), 90]
            result, imgencode = cv2.imencode('.jpg', frame, encode_param)
            data = numpy.array(imgencode)
            stringData = data.tostring()
            sendJson = {"token": token, "check": check, "img": base64.encodebytes(stringData).decode("utf-8")}
            sendJson = {"token": token, "check": check, "img": base64.encodebytes('asdasdwqe').decode("utf-8")}
            msg = json.dumps(sendJson)
            client_socket.send(str(len(msg)).ljust(16).encode()) # send length
            client_socket.send(bytes(msg, encoding="utf-8")) # send json
        except:
            print('Disconnected by Server')
        client_socket.close()
        
    def gesture_sensing(self, q):
        while True:
            if q.get():
                self.apds.setProximityIntLowThreshold(50)
                self.apds.enableGestureSensor()
                    
                try:
                    while True:
                        time.sleep(0.2)
                        if self.apds.isGestureAvailable():
                            motion = self.apds.readGesture()
                            motion_box = self.direction.get(motion, "unknown")
                            if motion_box == "right":
                                print("motion : ",motion_box)
                                #self.sendML(self.token, "IN")
                                time.sleep(0.5)
                                self.test()
                            if motion_box == "left":
                                print("motion : ",motion_box)
                                time.sleep(0.5)
                                self.test2()
                                #self.sendML(self.token, "OUT")
                        else:
                            break
                finally:
                    GPIO.cleanup()

    def light_sensing(self, q):
        while True:
            print("Light measure")
            GPIO.setup(self.LIGHT, GPIO.OUT)
            GPIO.output(self.LIGHT, GPIO.LOW)
            time.sleep(0.1)

            GPIO.setup(self.LIGHT, GPIO.IN)
            currentTime = time.time()
            diff = 0

            while GPIO.input(self.LIGHT) == GPIO.LOW:
                diff = time.time() - currentTime
            diff = diff * 100000
            print("light: ", diff)
            if diff >= 0.6: q.put(0)
            else: q.put(1)
            time.sleep(1)

    def closet_main(self):
        #self.token = self.receiveToken()
        #if self.token:
        #    print("User info(token): ", self.token)

        #    q = Queue()

        #    GPIO.setup(self.GESTURE, GPIO.IN)

         #   proc1 = Process(target=self.light_sensing, args=(q,))
         #   proc2 = Process(target=self.gesture_sensing, args=(q,))

         #   proc1.start()
         #   proc2.start()

          #  q.close()
         #   q.join_thread()

          #  proc1.join()
          #  proc2.join()
        interpreter = tf.lite.Interpreter(model_path='./model.tflite')
        interpreter.allocate_tensors()
        input_details = interpreter.get_input_details()
        output_details = interpreter.get_output_details()
        mlb = pickle.loads(open("./mlb.pickle", "rb").read())
        # check the type of the input tensor
        floating_model = input_details[0]['dtype'] == np.float32
        
        q = Queue()

        GPIO.setup(self.GESTURE, GPIO.IN)

        proc1 = Process(target=self.light_sensing, args=(q,))
        proc2 = Process(target=self.gesture_sensing, args=(q,))

        proc1.start()
        proc2.start()

        q.close()
        q.join_thread()

        proc1.join()
        proc2.join()
        

if __name__ == "__main__":
    Closet = Mycloset()
    Closet.closet_main()



