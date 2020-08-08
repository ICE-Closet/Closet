import cv2
import socket
import numpy
import pickle
port = 65000
token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c2VyIjoyfQ.mP_IOdB4LEsJIeeUVaxLpG0k4NlnMesaMhU13J6gQ8M" 

def opendoor(check):
    client_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    client_socket.connect(("220.67.124.120", port))
    try:
        #capture = cv2.VideoCapture(0)
        #ret, frame = capture.read()
        frame = cv2.imread('right.jpg', cv2.IMREAD_COLOR)
        
        encode_param = [int(cv2.IMWRITE_JPEG_QUALITY), 90]
        #result, imgencode = cv2.imencode('.jpg', frame, encode_param)
        result, imgencode = cv2.imencode('.jpg', frame, encode_param)
        data = numpy.array(imgencode)
        stringData = data.tobytes()
        
        d = {"token":token, "check":check, "img":stringData}
        msg = pickle.dumps(d)
        client_socket.send(str(len(msg)).ljust(16).encode())
        client_socket.send(msg)
          
    except:
        print('Disconnected by Server')

    client_socket.close()

opendoor("leftkk")
