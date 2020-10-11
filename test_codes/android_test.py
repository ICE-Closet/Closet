import socket
import threading
import time
import cv2
import numpy as np
import os

import pickle
import base64
import requests

import classify2 as cf
import datetime
port = 65000


def recvall(sock, count):
    buf = b''
    while count:
        newbuf = sock.recv(count)
        if not newbuf: return None
        buf += newbuf
        count -= len(newbuf)
    return buf

def handle_client(client_socket, addr):
    #recvData = client_socket.recv(1024)
    length = recvall(client_socket, 16)
    stringData = recvall(client_socket, int(length))

    print(pickle.loads(stringData)) 

    data = pickle.loads(stringData)
    print(data["img"])
    dt = np.frombuffer(data["img"],dtype = 'uint8')
    decimg = cv2.imdecode(dt, 1)
    #cv2.imshow('Image', decimg)
    now = datetime.datetime.now()
    t = now.strftime('%Y-%m-%d-%H-%M-%S')+".jpg"
    cv2.imwrite(t, decimg)
    
   
    cloth_result = cf.classifyimg(t)
    if data["check"]=="right":
    	cloth_result=cloth_result+"_IN"
  
    elif data["check"] == 'left':
    	cloth_result=cloth_result+"_OUT"
    else: pass

    print(cloth_result)
    db_store(t, cloth_result, data["token"])    
    time.sleep(2)

    client_socket.close()

def accept_func():
    global server_socket
    server_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    server_socket.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
    server_socket.bind(('', port))
    server_socket.listen(True)
    while 1:
        try:
            client_socket, addr = server_socket.accept()
        except KeyboardInterrupt:
            server_socket.close()
            print("Keyboard interrupt")
        t = threading.Thread(target=handle_client, args=(client_socket, addr))
        t.daemon = True
        t.start()

def db_store(img, result, token):
    URL = "http://13.124.208.47:8000/accounts/clothes_info/"
    #token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c2VyIjoyfQ.mP_IOdB4LEsJIeeUVaxLpG0k4NlnMesaMhU13J6gQ8M" 
    headers = { 'Authorizations' : token }
    files = {
        'image': open(img, 'rb'),
    }
    data = {
        "classify": result,
    }
    try:
        response = requests.post(url=URL, headers=headers, data=data, files=files)
        print(response) 

    except Exception as e:
        print(e)

if __name__ == '__main__':
    accept_func()

#https://dgkim5360.tistory.com/entry/python-requests

#https://developers.naver.com/forum/posts/12059


