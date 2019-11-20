package client.gui;

import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {

    JFrame loginFrame = new JFrame();

    JFrame registerFrame = new JFrame();
//    JFrame launchFrmae = new JFrame();


    public void setLoginFrame(){
//        loginFrame.setVisible();
        loginFrame.setTitle("Login or Register");
        loginFrame.setSize(400, 300);
        loginFrame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        loginFrame.setResizable(false);
        loginFrame.setLocationRelativeTo(null);
        loginFrame.setLayout(null);
        //avoid components overlapping

        Dimension dim = new Dimension(320, 30);
        JLabel account = new JLabel("Account:");
        account.setBounds(50,50,80,30);
        account.setPreferredSize(dim);

        JLabel passWord = new JLabel("Password:");
        passWord.setBounds(50,100,80,30);
        passWord.setPreferredSize(dim);

        JLabel nickName= new JLabel("Nick name:");
        nickName.setBounds(50,150,80,30);
        nickName.setPreferredSize(dim);

        JTextField accountText = new JTextField(20);
        accountText.setBounds(130,50,250,30);
        JPasswordField passwordText = new JPasswordField(20);
        passwordText.setBounds(130,100,250,30);
        JTextField nickNameText = new JTextField(20);
        nickNameText.setBounds(130,150,250,30);

        JLabel warn = new JLabel();
        warn.setBounds(80,120,250,60);
        JButton registerButton = new JButton(ProtocolConstant.REGISTER);
        registerButton.setBounds(80, 200, 130, 40);
        //registerButton.addActionListener(listen);
        JButton loginButton = new JButton(ProtocolConstant.LOGIN);
        loginButton.setBounds(220, 200, 130, 40);
        //loginButton.addActionListener(listen);

        loginFrame.add(account);
        loginFrame.add(accountText);
        loginFrame.add(passWord);
        loginFrame.add(passwordText);
        loginFrame.add(nickName);
        loginFrame.add(nickNameText);
        loginFrame.add(loginButton);
        loginFrame.add(registerButton);

        loginFrame.setVisible(true);


    }

}
