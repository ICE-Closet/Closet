import socket
import sys

HOST = '' #all available interfaces
PORT = 30000
s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
print ('Socket created')
try:
    s.bind((HOST, PORT))
except socket.error as msg:
    print ('Bind Failed. Error code: ' + str(msg[0]) + ' Message: ' + msg[1])
    sys.exit()

print ('Socket bind complete')

s.listen(10)
print ('Socket now listening')

while 1:
    conn, addr = s.accept()
    print ('Connected with ' + addr[0] + ':' + str(addr[1]))
    data = conn.recv(1024)
    if not data:
        break
    conn.sendall(data)
    print(data.decode())
conn.close()
s.close()
