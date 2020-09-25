from multiprocessing import Process, Queue
import RPi.GPIO as GPIO
import time
import datetime
import cv2
import socket
import numpy
import json
import base64
import sys
port = 30000
port2 = 65000
def opendoor(atoken, check):
    client_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    client_socket.connect(("220.67.124.120", port2))
    #token="eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c2VyIjoyfQ.mP_IOdB4LEsJIeeUVaxLpG0k4NlnMesaMhU13J6gQ8M"
    token = atoken
    try:
        capture = cv2.VideoCapture(0, cv2.CAP_V4L)
        ret, frame = capture.read()

        encode_param = [int(cv2.IMWRITE_JPEG_QUALITY), 90]
        result, imgencode = cv2.imencode('.jpg', frame, encode_param)
        data = numpy.array(imgencode)
        stringData = data.tostring()

        d = {"token":token, "check":check, "img":base64.encodebytes(stringData).decode("utf-8")}
        msg = json.dumps(d)
        client_socket.send(str(len(msg)).ljust(16).encode())
        client_socket.send(msg)
    except:
        print('Disconnected by Server')

    client_socket.close()
    
opendoor('"eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c2VyIjoyfQ.mP_IOdB4LEsJIeeUVaxLpG0k4NlnMesaMhU13J6gQ8M"','right')