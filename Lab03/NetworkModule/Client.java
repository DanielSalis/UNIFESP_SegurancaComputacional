package NetworkModule;

import java.io.*;
import java.net.Socket;

public class Client {
    private Socket socket;
    private OutputStream outputStream;
    private Writer writer;
    private BufferedWriter bufferWriter;

    public Client() {
        this.socket = null;
    }

    public Socket getSocket() {
        return this.socket;
    }

    public void conectar(String ipAdress) throws IOException {
        socket = new Socket(ipAdress, 4000);
        outputStream = socket.getOutputStream();
        writer = new OutputStreamWriter(outputStream);
        bufferWriter = new BufferedWriter(writer);
        bufferWriter.flush();
    }

    public void enviarMensagem(String msg) throws IOException {
        bufferWriter.write(msg + "\r\n");
        bufferWriter.flush();
    }
}
