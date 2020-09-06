from multiprocessing import Process, Queue
import RPi.GPIO as GPIO
import time
import datetime
import cv2
import socket
import numpy
import json

port = 65000
token="eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c2VyIjoyfQ.mP_IOdB4LEsJIeeUVaxLpG0k4NlnMesaMhU13J6gQ8M"
GPIO.setmode(GPIO.BCM)
GPIO.setwarnings(False)

TRIG = 20
ECHO = 21
TRIG1 = 23
ECHO1 = 24
resistorPin = 4

dis1, dis2= 0, 0

def opendoor(check):
    client_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    client_socket.connect(("220.67.124.120", port))
    try:
        capture = cv2.VideoCapture(0, cv2.CAP_V4L)
        ret, frame = capture.read()

        encode_param = [int(cv2.IMWRITE_JPEG_QUALITY), 90]
        result, imgencode = cv2.imencode('.jpg', frame, encode_param)
        data = numpy.array(imgencode)
        stringData = data.tobytes()

        d = {"token":token, "check":check, "img":stringData}
        msg = json.dumps(d)
        client_socket.send(str(len(msg)).ljust(16).encode())
        client_socket.send(bytes(msg, encoding="utf-8"))
    except:
        print('Disconnected by Server')

    client_socket.close()


def ultra_sensing(q):
    now1,now2 = datetime.time(12, 23, 10), datetime.time(12, 23, 50)
    while True:
        if q.get():
            print("Distance measure")
            time.sleep(0.05)
            GPIO.output(TRIG, True)
            time.sleep(0.00001)
            GPIO.output(TRIG, False)
            #print("reading sensor 1")
            while GPIO.input(ECHO) == 0:
                pulse_start = time.time()
            while GPIO.input(ECHO) == 1:
                pulse_end = time.time()
            pulse_duration = pulse_end - pulse_start
            dis1 = pulse_duration * 17150
            dis1 = round(dis1, 2)
            print("distance sensor1: ", dis1, "cm")
            time.sleep(0.05)
            GPIO.output(TRIG1, True)
            time.sleep(0.00001)
            GPIO.output(TRIG1, False)
            while GPIO.input(ECHO1) == 0:
                pulse_start2 = time.time()
            while GPIO.input(ECHO1) == 1:
                pulse_end2 = time.time()
            pulse_duration2 = pulse_end2 - pulse_start2
            dis2 = pulse_duration2 * 17150
            dis2 = round(dis2, 2)
            print("distance sensor2: ", dis2, "cm")

            if dis1 <= 10:
                now1 = datetime.datetime.now()
                time.sleep(1)
            if dis2 <= 10:
                now2 = datetime.datetime.now()
                time.sleep(1)
            if 1 <= now2.second - now1.second <= 3:
                now1,now2 = datetime.time(12, 23, 10), datetime.time(12, 23, 50)
                print("right\n")
                opendoor("right")

            if 1 <= now1.second - now2.second <= 3:
                now1,now2 = datetime.time(12, 23, 10), datetime.time(12, 23, 50)
                print("left\n")
                opendoor("left")

        else: pass

def light_sensing(q):
    while True:
        print("Light measure")
        GPIO.setup(resistorPin, GPIO.OUT)
        GPIO.output(resistorPin, GPIO.LOW)
        time.sleep(0.1)

        GPIO.setup(resistorPin, GPIO.IN)
        currentTime = time.time()
        diff = 0

        while (GPIO.input(resistorPin) == GPIO.LOW):
            diff = time.time() - currentTime
        diff = diff*100000
        print("light:",diff)
        if diff >= 0.9:
            q.put(0)
        else:
            q.put(1)
        time.sleep(0.4)

if __name__ == "__main__":
    q = Queue()

    GPIO.setup(TRIG, GPIO.OUT)
    GPIO.setup(ECHO, GPIO.IN)
    GPIO.setup(TRIG1, GPIO.OUT)
    GPIO.setup(ECHO1, GPIO.IN)
    GPIO.output(TRIG, False)
    GPIO.output(TRIG1, False)

    proc1 = Process(target=light_sensing, args=(q,))
    proc2 = Process(target=ultra_sensing, args=(q,))

    proc1.start()
    proc2.start()

    q.close()
    q.join_thread()

    proc1.join()
    proc2.join()


