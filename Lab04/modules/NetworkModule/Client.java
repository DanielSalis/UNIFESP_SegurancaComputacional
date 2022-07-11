package modules.NetworkModule;

import java.io.*;
import java.net.Socket;
import modules.KeyChanger.*;

public class Client {
    private Socket socket;
    private OutputStream outputStream;
    private Writer writer;
    private BufferedWriter bufferWriter;
    private final DH dh;

    public Client() {
        this.socket = null;
        this.dh = new DH(4, 0);
    }

    public Socket getSocket() {
        return this.socket;
    }

    public DH getDh()
    {
        return this.dh;
    }

    public void conectar(String ipAdress) throws IOException {
        socket = new Socket(ipAdress, 4000);
        outputStream = socket.getOutputStream();
        writer = new OutputStreamWriter(outputStream);
        bufferWriter = new BufferedWriter(writer);
        bufferWriter.flush();
        this.dh.setX(this.dh.generateX());
        System.out.println("Set x: "+this.dh.getX());
    }

    public void sendHeader(){
        this.dh.setX((int)(Math.random() * (this.dh.getQ())));
        this.dh.generateY();
        try {
            sendMessage("Header|Y:"+this.dh.getY()+"|Header");
        } catch (IOException ex) {
            System.out.println(ex);
        }
    }

    public void sendMessage(String msg) throws IOException {
        bufferWriter.write(msg + "\r\n");
        bufferWriter.flush();
    }
}
