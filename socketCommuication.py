import socket
import threading
import time
import cv2
import json
import os
import datetime
import base64

import requests
import MLclassify

class Communication():
    def __init__(self):
        self.PORT = 65000
        self.device = ''
        self.URL = "http://13.124.208.47:8000/accounts/clothes_info/"


    def DBStore(self, img, result, token):
        headers = {'Authorizations': token}
        files = {
            'image': open(img, 'rb'),
        }
        data = {
            "classify": result,
        }
        try:
            response = requests.post(url=self.URL, headers=headers, data=data, files=files)
            print(response)
        except Exception as e:
            print(e)

    def recvall(self, sock, count):
        buf = b''
        while count:
            newbuf = sock.recv(count)
            if not newbuf: return None
            buf += newbuf
            count -= len(newbuf)
        return buf

    def handle_client(self, client_socket, addr):
        length = self.recvall(client_socket, 16)
        print(length)
        stringData = self.recvall(client_socket, int(length))

        data = json.loads(stringData)

        # image classify
        print(data["token"])
        imgdata = base64.b64decode(data["img"])
        now = datetime.datetime.now()
        filename = now.strftime('%Y-%m-%d-%H-%M-%S') + ".jpg"
        with open(filename, 'wb') as f:
            f.write(imgdata)
          
        cf = MLclassify.ClothesML()
        cloth_result = cf.classify(filename)

        if data["check"] == "IN" or data["check"] == 'OUT':
            self.device = 'MyCloset_Raspberrypi'
            if data["check"] == 'IN':
                cloth_result = cloth_result + "_IN"
            else:
                cloth_result = cloth_result + "_OUT"
        else:
            self.device = 'MyCloset_Android'
        print('Device: ',self.device, ', cloth classify result : ', cloth_result)

        self.DBStore(filename, cloth_result, data["token"])

        client_socket.sendall('[200]'.encode())

        # delete picture code
        # os.system("rm -rf /home/dasan/keras-multi-label/" + filename)
        time.sleep(2)

        client_socket.close()

    def accept_func(self):
        global server_socket
        server_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        server_socket.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
        server_socket.bind(('', self.PORT))
        server_socket.listen(True)
        while 1:
            try:
                client_socket, addr = server_socket.accept()
            except KeyboardInterrupt:
                server_socket.close()
                print("Keyboard interrupt")
            t = threading.Thread(target=self.handle_client, args=(client_socket, addr))
            t.daemon = True
            t.start()



if __name__ == '__main__':
    Commu = Communication()
    Commu.accept_func()
