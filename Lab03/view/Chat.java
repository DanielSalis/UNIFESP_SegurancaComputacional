package view;
import javax.swing.*;

import CryptoModule.CBC;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

import NetworkModule.Client;
import NetworkModule.Connection;

public class Chat extends JFrame {
    private Client client;

    public JFrame frame;
    public JTextArea textArea;
    public JPanel topPanel;
    public JPanel bottomPanel;

    public JPanel message__panel;
    public JTextField message__textField;
    public JLabel message__label;
    public JButton message__button_send;

    public JPanel ip__pannel;
    public JLabel ip__label;
    public JTextField ip__textField;
    public JButton ip__button_send;

    public JPanel encryptor__pannel;
    public JLabel encryptor__label;
    public JComboBox<String> encryptor__combobox;

    public JPanel cypher__pannel;
    public JLabel cypher__label;
    public JComboBox<String> cypher__combobox;

    public Chat() {
        drawScreen();
        this.client = new Client();
    }

    private void addMessage(String mes, String who) {
        textArea.append(who + ":\n" + mes + "\n\n");
    }

    private String handleEncryption(String text){
        String cipheredText = text;

        if (((String) encryptor__combobox.getSelectedItem()).compareTo("SDES") == 0) {
            switch (((String) cypher__combobox.getSelectedItem())) {
                case "CBC":
                    int[] key = { 0, 1, 0, 1, 0, 0, 1, 1, 1, 0 };
                    int[] IV = { 0, 1, 1, 1, 0, 1, 0, 1, 1, 0 };
                    CBC cbc = new CBC(key, IV);
                    cipheredText = cbc.encrypt(message__textField.getText());
                    break;

                case "ECB":
                    
                    break;
            
                default:
                    break;
            }
        }

        return cipheredText;
    }

    public void handleDecryption(String text) {
        String plainText = text;

        if (((String) encryptor__combobox.getSelectedItem()).compareTo("SDES") == 0) {
            switch (((String) cypher__combobox.getSelectedItem())) {
                case "CBC":
                    int[] key = { 0, 1, 0, 1, 0, 0, 1, 1, 1, 0 };
                    int[] IV = { 0, 1, 1, 1, 0, 1, 0, 1, 1, 0 };
                    CBC cbc = new CBC(key, IV);
                    plainText = cbc.decrypt(text);
                    break;

                case "ECB":
                    
                    break;
            
                default:
                    break;
            }
        }

        this.addMessage(plainText, "Servidor?");

    }

    private void sendMessage(ActionEvent evt) {
        System.out.println(cypher__combobox.getSelectedItem().toString());
        System.out.println(encryptor__combobox.getSelectedItem().toString());
        System.out.println(ip__textField.getText());
        System.out.println(message__textField.getText());

        if (this.client.getSocket() == null || this.ip__textField.getText() == "") {
            JOptionPane.showMessageDialog(null, "Make sure you filled in the ip input or connected");
            return;
        }

        String cipheredText = handleEncryption(message__textField.getText());

        this.addMessage(message__textField.getText(), "Eu");
        message__textField.setText("");

        try{
            System.out.println(cipheredText);
            this.client.enviarMensagem(cipheredText);
        }catch (IOException e){
            System.out.println(e);
        }
    }

    private void connectToIp(ActionEvent evt) {
        try {
            if (this.ip__textField.getText().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Make sure you filled in the ip input");
                return;
            }
            this.client.conectar(ip__textField.getText());
            textArea.setText("");
        } catch (IOException ex) {
            System.out.println(ex);
        }
    }

    private void handleChangeEncrypt(ActionEvent evt){
        System.out.println(encryptor__combobox.getSelectedItem().toString());
    }

    private void handleChangeCypher(ActionEvent evt){
        System.out.println(cypher__combobox.getSelectedItem().toString());
    }

    private void drawScreen() {

        // Creating the Frame
        frame = new JFrame("--- SAFE CHAT v.0 ---");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));
        frame.setSize(1000, 800);

        // Text Area at the Center
        textArea = new JTextArea();
        textArea.setFont(new Font("Serif", Font.BOLD, 20));
        textArea.setEditable(false);
        textArea.setBorder(BorderFactory.createCompoundBorder(
                textArea.getBorder(),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        textArea.setBorder(BorderFactory.createTitledBorder("Conversa"));

        // Creating ip__panel
        ip__pannel = new JPanel();
        ip__label = new JLabel("Enter ip address");
        ip__textField = new JTextField(12);
        ip__button_send = new JButton("Connect");
        ip__button_send.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                connectToIp(evt);
            }
        });
        ip__pannel.add(ip__label);
        ip__pannel.add(ip__textField);
        ip__pannel.add(ip__button_send);
        ip__pannel.setBackground(Color.green);

        // Creating encryptor__pannel
        encryptor__pannel = new JPanel();
        encryptor__label = new JLabel("Encrypt Mode");
        encryptor__combobox = new JComboBox<>();
        encryptor__combobox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "SDES", "RC4"}));
        encryptor__combobox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                handleChangeEncrypt(evt);
            }
        });
        encryptor__pannel.add(encryptor__label);
        encryptor__pannel.add(encryptor__combobox);
        encryptor__pannel.setBackground(Color.PINK);

        cypher__pannel = new JPanel();
        cypher__label = new JLabel("Cypher Mode");
        cypher__combobox = new JComboBox<>();
        cypher__combobox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "ECB", "CBC"}));
        cypher__combobox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                handleChangeCypher(evt);
            }
        });
        cypher__pannel.add(cypher__label);
        cypher__pannel.add(cypher__combobox);
        cypher__pannel.setBackground(Color.YELLOW);

        // Creating the message__panel at bottom and adding components
        message__panel = new JPanel();
        message__label = new JLabel("Enter Text");
        message__textField = new JTextField(12);
        message__button_send = new JButton("Send");
        message__button_send.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                sendMessage(evt);
            }
        });
        message__panel.add(message__label);
        message__panel.add(message__textField);
        message__panel.add(message__button_send);
        message__panel.setBackground(Color.RED);

        topPanel = new JPanel();
        topPanel.add(ip__pannel);
        topPanel.add(encryptor__pannel);
        topPanel.add(cypher__pannel);
        topPanel.setBackground(Color.GRAY);

        bottomPanel = new JPanel();
        bottomPanel.add(message__panel);
        bottomPanel.setBackground(Color.GRAY);

        // Adding Components to the frame.
        frame.getContentPane().add(topPanel);
        frame.getContentPane().add(textArea);
        frame.getContentPane().add(bottomPanel);
    }

    public static void main(String args[]) {
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {

                Chat a = new Chat();
                Connection con = new Connection();
                con.setChatApp(a);
                con.start();
                a.frame.setVisible(true);
            }
        });
    }
}