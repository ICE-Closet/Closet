# send token to raspberry pi server

from socket import socket, AF_INET, SOCK_STREAM
from .models import RaspberryPi

# views.py 에서 해당 사용자의 raspberrypi id, 토큰 받아오기 -> raspberry port, ip db에서 찾기

def sendToken(id, token):
    id = id
    data = token

    rasp = RaspberryPi.objects.get(pk=id)
    ip = rasp.ip
    port = rasp.port

    sock = socket(AF_INET, SOCK_STREAM)
    socket.connect((ip, port))
    socket.send(data.encode('utf-8'))

    sock.close()
    
    return "send to raspberry pi success"