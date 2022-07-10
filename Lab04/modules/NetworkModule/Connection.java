package modules.NetworkModule;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

import view.Chat;

public class Connection extends Thread {

    private Chat chatApp;

    public Chat getChatApp() {
        return chatApp;
    }

    public void setChatApp(Chat chatApp) {
        this.chatApp = chatApp;
    }


    @Override
    public void run() {

        ServerSocket server;
        try {
            server = new ServerSocket(4000);
            while (true) {
                Socket socket = null;
                try {
                    socket = server.accept();
                    Server s = new Server(socket);
                    s.setChatApp(this.getChatApp());

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
