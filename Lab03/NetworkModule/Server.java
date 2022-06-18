package NetworkModule;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server extends Thread {
    private Socket socket;
    private InputStream inputStream;
    private InputStreamReader inputStreamReader;
    private BufferedReader buffer;

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
                        // descriptografar mensagem
                    }
                }
            } catch (IOException ex) {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
