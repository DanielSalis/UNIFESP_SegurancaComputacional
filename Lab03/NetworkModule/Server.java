package NetworkModule;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

import view.Chat;

public class Server extends Thread {
    private Socket socket;
    private InputStream inputStream;
    private InputStreamReader inputStreamReader;
    private BufferedReader buffer;
    private Chat chatApp;

    public Chat getChatApp() {
        return chatApp;
    }

    public void setChatApp(Chat chatApp) {
        this.chatApp = chatApp;
    }


    public Server(Socket s) {
        this.socket = s;
        try {
            inputStream = socket.getInputStream();
            inputStreamReader = new InputStreamReader(inputStream);
            buffer = new BufferedReader(inputStreamReader);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        String msg = "";

        while (!"Sair".equalsIgnoreCase(msg)) {
            try {
                if (buffer.ready()) {
                    msg = buffer.readLine();
                    if (msg.equals("Sair")) {
                        System.out.print("Servidor caiu! \r\n");
                    } else {
                        System.out.print("Escutou: " + msg + "\r\n");
                        this.chatApp.handleDecryption(msg);
                    }
                }
            } catch (IOException ex) {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
