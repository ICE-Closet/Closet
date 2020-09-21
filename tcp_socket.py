import cv2 
import socket 
import numpy 
import pickle 
import json 
port = 65000 
token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c2VyIjoyfQ.mP_IOdB4LEsJIeeUVaxLpG0k4NlnMesaMhU13J6gQ8M"

def opendoor(check):
    client_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    client_socket.connect(("220.67.124.120", port))
    try:
        #capture = cv2.VideoCapture(0)
        #ret, frame = capture.read()
        frame = cv2.imread('test2.jpg', cv2.IMREAD_COLOR)
        
        encode_param = [int(cv2.IMWRITE_JPEG_QUALITY), 90]
        #result, imgencode = cv2.imencode('.jpg', frame, encode_param)
        result, imgencode = cv2.imencode('.jpg', frame, encode_param)
        data = numpy.array(imgencode)
        stringData = data.tostring() #tobytes()
        x = json.loads(stringData.decode("utf-8"))
        
        print(type(x))
        d = {"token":token, "check":check, "img":stringData}
        #dd = d.decode()
        msg = json.dumps(str(d))
        #print(msg)
        #l1 = len(msg) + 7 + len(stringData)
        #msg = json.dump(d)
        print(len(msg))
        client_socket.send(str(len(msg)).ljust(16).encode())
        
        client_socket.send(bytes(msg,encoding="utf-8"))
          
    except:
        print('Disconnected by Server')

    client_socket.close()

opendoor("leftkk")
