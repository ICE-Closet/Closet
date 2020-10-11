import socket
import threading
import time
import cv2
import numpy as np
import os

import pickle
import base64
import requests

#from classify2 import classifyimg
port = 65000
HEADERSIZE = 80000
def recvall(sock):
	while True:
		full_msg = b''
		new_msg = True
		while True:
			msg = sock.recv(16)
			if new_msg:
				print("new msg len:",msg[:HEADERSIZE])
				msglen = int(msg[:HEADERSIZE])
				new_msg = False

			print(f"full message length: {msglen}")

			full_msg += msg

			print(len(full_msg))

			if len(full_msg)-HEADERSIZE == msglen:
				print("full msg recvd")
				print(full_msg[HEADERSIZE:])
				print(pickle.loads(full_msg[HEADERSIZE:]))
				new_msg = True
				full_msg = b""

def handle_client(client_socket, addr):
    #recvData = client_socket.recv(1024)
    
    stringData = recvall(client_socket)
    #print(stringData)
    #print(json.loads(client_sock)
    #if :
    #    check="right.jpg"
    #elif stringData[:5].decode('utf-8') == 'leftt':
    #    check="left.jpg"
    #else:
    #    check=""
    #data = np.frombuffer(stringData[5:], dtype='uint8')
    #print(recvData.decode('utf-8'))
    #decimg = cv2.imdecode(data, 1)
    #cv2.imshow('Image', decimg)
    #cv2.imwrite(check, decimg)
    
    #os.system("python classify.py --model fashion6.model --labelbin mlb6.pickle --image /home/dasan/keras-multi-label/"+check)
    #classifying("/home/dasan/keras-multi-label/"+check)
    time.sleep(2)
    #db_store("/home/dasan/keras-multi-label/"+check)
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

def db_store(data):
    URL = "http://220.67.124.193:8000/accounts/clothes_info/"
    token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c2VyIjoyfQ.mP_IOdB4LEsJIeeUVaxLpG0k4NlnMesaMhU13J6gQ8M" 
    headers = { 'Authorizations' : token }
    files = {
        'image': open(data, 'rb'),
    }
    data = {
        "top": "short",
        "color": "red",
        "bottom": "None",
        "outer": "None",
        "pattern": "check",
    }
    try:
        response = requests.post(url=URL, headers=headers, data=data, files=files)
        print(response) 

    except Exception as e:
        print(e)

if __name__ == '__main__':
    accept_func()
    
    #db_store()

#https://dgkim5360.tistory.com/entry/python-requests

#https://developers.naver.com/forum/posts/12059



