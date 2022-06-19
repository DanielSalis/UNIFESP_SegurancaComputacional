import javax.swing.*;
import java.awt.*;
import NetworkModule.Connection;

public class Chat extends JFrame {
    public JFrame frame;
    public JTextArea textArea;
    public JPanel topPanel;

    public JPanel message__panel;
    public JTextField messa__textField;
    public JLabel message__label;
    public JButton message__button_send;

    public JPanel ip__pannel;
    public JLabel ip__label;
    public JTextField ip__textField;
    public JButton ip__button_send;

    public JPanel encryptor__pannel;
    public JLabel encryptor__label;
    public JComboBox<String> encryptor__combobox;

    public Chat() {
        initComponents();
    }

    private void addMessage(String mes, String who) {
        textArea.setText(textArea.getText().concat(who + ":\n" + mes + "\n\n"));
    }

    private void sendMessage(java.awt.event.ActionEvent evt) {
        this.addMessage(messa__textField.getText(), "Eu");
        messa__textField.setText("");
    }

    private void initComponents() {

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
        ip__pannel.add(ip__label);
        ip__pannel.add(ip__textField);
        ip__pannel.add(ip__button_send);
        ip__pannel.setBackground(Color.green);

        // Creating encryptor__pannel
        encryptor__pannel = new JPanel();
        encryptor__label = new JLabel("Encrypt Mode");
        encryptor__combobox = new JComboBox<>();
        encryptor__combobox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "ECB", "CBC", "Nenhum" }));
        encryptor__pannel.add(encryptor__label);
        encryptor__pannel.add(encryptor__combobox);
        encryptor__pannel.setBackground(Color.PINK);

        // Creating the message__panel at bottom and adding components
        message__panel = new JPanel();
        message__label = new JLabel("Enter Text");
        messa__textField = new JTextField(12);
        message__button_send = new JButton("Send");
        message__button_send.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sendMessage(evt);
            }
        });
        message__panel.add(message__label);
        message__panel.add(messa__textField);
        message__panel.add(message__button_send);
        message__panel.setBackground(Color.RED);

        topPanel = new JPanel();
        topPanel.add(ip__pannel);
        topPanel.add(encryptor__pannel);
        topPanel.add(message__panel);
        topPanel.setBackground(Color.GRAY);

        // Adding Components to the frame.
        frame.getContentPane().add(topPanel);
        frame.getContentPane().add(textArea);
    }

    public static void main(String args[]) {
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {

                Chat a = new Chat();
                Connection con = new Connection();
                con.start();
                a.frame.setVisible(true);
            }
        });
    }
}