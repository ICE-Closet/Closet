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

class Mycloset():
    def __init__(self):
        # socket port
        self.PORT = 30000
        self.PORT2 = 65000
        self.HOST = ''
        self.MLHOST = '#####'

        # sensor pin
        GPIO.setmode(GPIO.BCM)
        GPIO.setwarnings(False)

        self.TRIG1 = 20
        self.ECHO1 = 21
        self.TRIG2 = 23
        self.ECHO2 = 24
        self.LIGHT = 4

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
            encode_param = [int(cv2.IMWRITE_JPEG_QUALITY), 90]
            result, imgencode = cv2.imencode('.jpg', frame, encode_param)
            data = numpy.array(imgencode)
            stringData = data.tostring()
            sendJson = {"token": token, "check": check, "img": base64.encodebytes(stringData).decode("utf-8")}
            msg = json.dumps(sendJson)
            client_socket.send(str(len(msg)).ljust(16).encode()) # send length
            client_socket.send(bytes(msg, encoding="utf-8")) # send json
        except:
            print('Disconnected by Server')
        client_socket.close()

    def ultra_sensing(self, q):
        pulse_start, pulse_end, pulse_duration = 0, 0, 0
        pulse_start2, pulse_end2, pulse_duration2 = 0, 0, 0
        current = datetime.datetime.now()
        now1, now2 = current - datetime.timedelta(seconds=40), current
        while True:
            if q.get():
                print("Distance measure")
                time.sleep(0.05)
                GPIO.output(self.TRIG1, True)
                time.sleep(0.00001)
                GPIO.output(self.TRIG1, False)
                while GPIO.input(self.ECHO1) == 0:
                    pulse_start = time.time()
                while GPIO.input(self.ECHO1) == 1:
                    pulse_end = time.time()
                pulse_duration = pulse_end - pulse_start
                dis1 = pulse_duration * 17150
                dis1 = round(dis1, 2)

                time.sleep(0.05)
                GPIO.output(self.TRIG2, True)
                time.sleep(0.00001)
                GPIO.output(self.TRIG2, False)
                while GPIO.input(self.ECHO2) == 0:
                    pulse_start2 = time.time()
                while GPIO.input(self.ECHO2) == 1:
                    pulse_end2 = time.time()
                pulse_duration2 = pulse_end2 - pulse_start2
                dis2 = pulse_duration2 * 17150
                dis2 = round(dis2, 2)

                if dis1 <= 20:
                    now1 = datetime.datetime.now()
                    time.sleep(0.2)
                if dis2 <= 20:
                    now2 = datetime.datetime.now()
                    time.sleep(0.2)

                print("distance1: ", dis1, "  distance2: ", dis2)

                if 1 <= now2.second - now1.second <= 2:
                    current = datetime.datetime.now()
                    now1, now2 = current - datetime.timedelta(seconds=40), current
                    print("\nClothes IN!!!!\n")
                    self.sendML(self.token, "IN")

                if 1 <= now1.second - now2.second <= 2:
                    current = datetime.datetime.now()
                    now1, now2 = current - datetime.timedelta(seconds=40), current
                    print("\nClothes Out!!!\n")
                    self.sendML(self.token, "OUT")

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
            if diff >= 0.9: q.put(0)
            else: q.put(1)
            time.sleep(0.4)

    def closet_main(self):
        self.token = self.receiveToken()
        if self.token:
            print("User info(token): ", self.token)

            q = Queue()

            GPIO.setup(self.TRIG1, GPIO.OUT)
            GPIO.setup(self.ECHO1, GPIO.IN)
            GPIO.setup(self.TRIG2, GPIO.OUT)
            GPIO.setup(self.ECHO2, GPIO.IN)
            GPIO.output(self.TRIG1, False)
            GPIO.output(self.TRIG2, False)

            proc1 = Process(target=self.light_sensing, args=(q,))
            proc2 = Process(target=self.ultra_sensing, args=(q,))

            proc1.start()
            proc2.start()

            q.close()
            q.join_thread()

            proc1.join()
            proc2.join()

if __name__ == "__main__":
    Closet = Mycloset()
    Closet.closet_main()
