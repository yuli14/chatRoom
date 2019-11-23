package server.gui;

import javax.swing.*;

public class ServerGui extends JFrame {

    private static JTextArea showMessageArea = new JTextArea();
    private static JTextArea idArea = new JTextArea();
    private static JTextArea clientCountArea = new JTextArea();
    private static JTextArea idAndPasswordArea = new JTextArea();
    static String id = "";
    static String password = "";
    //static final String REG = "(register)";

    public void initServerFrame(){
        this.setTitle("Server UI");
        this.setSize(400,700);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setLayout(null);

        clientCountArea.setEditable(false);
        clientCountArea.setBounds(5,5,380,35);
        idArea.setEnabled(false);
        JScrollPane idPane = new JScrollPane(idArea);
        idPane.setBounds(5, 50, 380, 90);
        showMessageArea.setEnabled(false);
        showMessageArea.setVerifyInputWhenFocusTarget(true);
        JScrollPane showMessagePane = new JScrollPane(showMessageArea);
        showMessagePane.setBounds(5,150,380,400);
        idAndPasswordArea.setEnabled(false);
        JScrollPane idAndPassPane = new JScrollPane(idAndPasswordArea);
        idAndPassPane.setBounds(5,560,380,100);
        idAndPasswordArea.setText(" id ----------  password");

        this.add(clientCountArea);
        this.add(idPane);
        this.add(showMessagePane);
        this.add(idAndPassPane);
        this.setVisible(true);
    }

    public void setClientCountArea(String str){
        clientCountArea.setText(str);
    }
}
