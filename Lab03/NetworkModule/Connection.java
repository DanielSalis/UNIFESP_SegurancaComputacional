package NetworkModule;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Connection extends Thread {

    @Override
    public void run() {

        ServerSocket server;
        try {
            server = new ServerSocket(3000);
            while (true) {
                Socket socket = null;
                try {
                    socket = server.accept();
                    Server s = new Server(socket);

                    s.start();
                    System.out.println("Connected");
                } catch (Exception ex) {
                    socket.close();
                    ex.printStackTrace();
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(Connection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
