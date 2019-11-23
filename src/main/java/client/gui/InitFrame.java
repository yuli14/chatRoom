package client.gui;

import client.Listener.ChatListener;
import client.Listener.UserListListener;
import client.ProtocolConstant;

import javax.swing.*;
import java.awt.*;

import static client.ProtocolConstant.*;

public class InitFrame extends JFrame {

    private JFrame loginFrame = new JFrame();
    private JFrame registerFrame = new JFrame();
    private ChatFrames groupChatFrame;
    private ChatFrames privateChatFrame;
    private JFrame mainFrame;
//    JFrame launchFrmae = new JFrame();

    private JTextField accountText = new JTextField(20);
    private JPasswordField passwordText = new JPasswordField(20);
    private JTextField nickNameText = new JTextField(20);
    JLabel warn = new JLabel();
    JLabel idLabel = new JLabel(); // current user id
    ChatListener chatListener = new ChatListener(this, new JLabel());

    private JTextField regText = new JTextField(20);

    private JPasswordField regPassword = new JPasswordField(20);
    public void initFrame(){
//        loginFrame.setVisible();
        loginFrame.setTitle("Login or Register");
        loginFrame.setSize(400, 300);
        loginFrame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        loginFrame.setResizable(false);
        loginFrame.setLocationRelativeTo(null);
        loginFrame.setLayout(null);
        //avoid components overlapping

        Dimension dim = new Dimension(320, 30);
        JLabel account = new JLabel("Nick Name: ");
        account.setBounds(50,50,80,30);
        account.setPreferredSize(dim);

        JLabel passWord = new JLabel("Password: ");
        passWord.setBounds(50,100,80,30);
        passWord.setPreferredSize(dim);



        accountText.setBounds(130,50,250,30);
        passwordText.setBounds(130,100,250,30);
        //nickNameText.setBounds(130,150,250,30);


        warn.setBounds(80,120,250,60);
        JButton registerButton = new JButton(ProtocolConstant.REGISTER);
        registerButton.setBounds(80, 200, 130, 40);
        registerButton.addActionListener(chatListener);
        JButton loginButton = new JButton(ProtocolConstant.LOGIN);
        loginButton.setBounds(220, 200, 130, 40);
        loginButton.addActionListener(chatListener);

        loginFrame.add(account);
        loginFrame.add(accountText);
        loginFrame.add(passWord);
        loginFrame.add(passwordText);
        //loginFrame.add(nickName);
        loginFrame.add(nickNameText);
        loginFrame.add(loginButton);
        loginFrame.add(registerButton);

        loginFrame.setVisible(true);


    }


    public void registerFrame(){
        JFrame regFrame = new JFrame();
        regFrame.setTitle(ProtocolConstant.REGISTER);
        regFrame.setSize(400,300);
        regFrame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        regFrame.setResizable(false);
        regFrame.setLocationRelativeTo(null);
        regFrame.setLayout(null);

        Dimension dim = new Dimension(320,30);
        JLabel label1 = new JLabel();
        JLabel label2 = new JLabel();
        label1.setBounds(50,50,50,30);
        label2.setBounds(50,100,50,30);
        label1.setText("Nick Name: ");
        label2.setText("password: ");



        regText.setBounds(100,50,250,30);
        regText.setPreferredSize(dim);



        regPassword.setPreferredSize(dim);
        regPassword.setBounds(100,100,250,30);

        JLabel warn = new JLabel();
        warn.setBounds(80,120,250,60);
        JButton button = new JButton(ProtocolConstant.CONFIRM);
        button.setBounds(150, 170, 130, 40);

        button.addActionListener(chatListener);

        regFrame.add(label1);
        regFrame.add(regText);
        regFrame.add(label2);
        regFrame.add(regPassword);
        regFrame.add(warn);
        regFrame.add(button);

        regFrame.setVisible(true);
    }

    public void startMainFrame(){
        mainFrame = new JFrame();
        mainFrame.setTitle("main frame");
        mainFrame.setSize(400,600);
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setDefaultCloseOperation(3);
        mainFrame.setResizable(false);
        mainFrame.setLayout(null);

        idLabel.setBounds(10,10,180,30);

        JLabel label1 = new JLabel("Friend Client Nick Name:");
        label1.setBounds(5,50,180,30);
        JList friendList = new JList();
        friendList.setBounds(5,90,380,400);
        friendList.addMouseListener(new UserListListener(this, friendList));

        JButton button1 = new JButton(GROUP_CHAT);
        button1.setBounds(10,510,140,30);
        button1.addActionListener(chatListener);

        JButton button2 = new JButton(REFRESH_FRIENDS_ID);
        button2.setBounds(270,510,140,30);
        button2.addActionListener(chatListener);

        mainFrame.add(idLabel);
        mainFrame.add(label1);//
        mainFrame.add(friendList);//friend nick name list
        mainFrame.add(button1);

        mainFrame.add(button2);
        mainFrame.setVisible(true);
    }
    public void startGroupChat(){
        groupChatFrame.setChat("group chat",SEND_GROUP_MSG, chatListener);
    }
    public void startPrivateChat(String targetNickName){
        privateChatFrame.setChat("private chat with " + targetNickName, SEND_PRIVATE_MSG, chatListener);
    }


    public String getNickName(){
        return accountText.getText();
    }

    public char[] getPassword(){
        return passwordText.getPassword();
    }

    public String getRegNickName(){
        return regText.getText();
    }

    public char[] getRegPassword(){
        return regPassword.getPassword();
    }
    public void showWarnMessage(String message){
        this.warn.setText(message);
    }

    public JFrame getLoginFrame(){
        return this.loginFrame;
    }
    public JFrame getRegisterFrame(){
        return this.registerFrame;
    }
    public JFrame getMainFrame(){return this.mainFrame;}
    public ChatFrames getGroupChatFrame(){return this.groupChatFrame;}
    public ChatFrames getPrivateChatFrame() {
        return privateChatFrame;
    }
    public ChatListener getChatListener(){
        return this.chatListener;
    }
//    public String getNickName(){
//        return accountText.getText();
//    }


}
