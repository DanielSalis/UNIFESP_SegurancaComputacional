package modules.NetworkModule;

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
            if (msg.length() > 6){
                if(msg.substring(0, 6).equals("Header")) {
                    String header = msg.substring(7,msg.lastIndexOf("Header"));
                    String messageData[] = header.split("\\|");
                    for (String data: messageData) {
                        if (data.substring(0, 2).compareTo("Y:")==0) {
                            int k = this.chatApp
                                .getClient()
                                .getDh()
                                .generateK(Integer.parseInt(data.substring(2, data.length())))
                                .intValue();
                            int[] aux = new int[10];
                            String aux_k = Integer.toBinaryString(k);
                            for(int i = 9; i >= 0; i--){
                                if(aux_k.length() - 1 >= i){
                                     if(aux_k.charAt(i) == '1'){
                                        aux[i] = 1;
                                        continue;
                                    }
                                }
                                aux[i]=0;
                            }
                            this.chatApp.setKey(aux);
                            continue;
                        }
                    }
                }
            }


            try {
                if (buffer.ready()) {
                    msg = buffer.readLine();
                    if (msg.equals("Sair")) {
                        System.out.print("Servidor caiu! \r\n");
                    } else {
                        System.out.print("Server listened: " + msg + "\r\n\n");
                        this.chatApp.handleDecryption(msg);
                    }
                }
            } catch (IOException ex) {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
