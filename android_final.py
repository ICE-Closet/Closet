import socket
import threading
import time
import cv2
import numpy as np
import json
import io
import os
import datetime
import base64
import classify2 as cf
import requests
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
    length = recvall(client_socket, 16)
    stringData = recvall(client_socket, int(length)) 
    #print(type(stringData))
    
    data = json.loads(stringData)
    print(data["token"])    
    imgdata = base64.b64decode(data["img"])
    now = datetime.datetime.now()
    filename = now.strftime('%Y-%m-%d-%H-%M-%S') + ".jpg"
    
    with open(filename, 'wb') as f:
        f.write(imgdata)
    
    
    cloth_result = cf.classifyimg(filename)
    print(cloth_result)
    db_store(filename, cloth_result, data["token"])
    msg = int(123)
    #msg.encode()
    client_socket.send(msg.to_bytes(4, byteorder='little'))
    
    time.sleep(2)
    
    os.remove('/home/dasan/keras-multi-label/' + filename)
    
    client_socket.close()

def db_store(img, result, token):  # img,result, token
    URL = "http://13.124.208.47:8000/accounts/clothes_info/"
    headers = {'Authorizations': token}
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

if __name__ == '__main__':
    accept_func()
