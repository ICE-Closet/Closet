import socket
import threading
import time
import cv2
import json
import os
import datetime
import base64

import requests
import classify2 as cf

# import classify as cf

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
    print("len", length)
    stringData = recvall(client_socket, int(length))

    data = json.loads(stringData)

    if data["check"] == "right" or data["check"] == 'left':
        imgdata = base64.b64decode(data["img"])
        now = datetime.datetime.now()
        filename = now.strftime('%Y-%m-%d-%H-%M-%S') + ".jpg"
        with open(filename, 'wb') as f:
            f.write(imgdata)
        # dt = np.frombuffer(data["img"], dtype='uint8')
        # decimg = cv2.imdecode(dt, 1)
        # # cv2.imshow('Image', decimg)
        # now = datetime.datetime.now()
        # filename = now.strftime('%Y-%m-%d-%H-%M-%S') + ".jpg"
        # cv2.imwrite(filename, decimg)
        cloth_result = cf.classifyimg(filename)

        if data["check"] == 'right':
            cloth_result = cloth_result + "_IN"
        else:
            cloth_result = cloth_result + "_OUT"

    else:
        imgdata = base64.b64decode(data["img"])
        now = datetime.datetime.now()
        filename = now.strftime('%Y-%m-%d-%H-%M-%S') + ".jpg"
        with open(filename, 'wb') as f:
            f.write(imgdata)
        cloth_result = cf.classifyimg(filename)
    print(cloth_result)
    # db_store(filename, cloth_result, data["token"])
    client_socket.sendall('[200]'.encode())
    # delete picture code
    os.system("rm -rf /home/dasan/keras-multi-label/"+filename)
    time.sleep(2)

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
