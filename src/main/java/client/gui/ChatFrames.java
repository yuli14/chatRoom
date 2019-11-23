package client.gui;

import client.Listener.ChatListener;

import javax.swing.*;

public class ChatFrames extends JFrame {
    JFrame chatFrame;
    private JTextArea sendMessageArea = new JTextArea();
    public ChatFrames(){

    }

    public void setChat(String chatField, String sendMessage, ChatListener chatListener){
        //chat with group or private
        //set the titile to be different,
        //others are nearly same
        JFrame chatFrame = new JFrame();
        //chat field is either group chat or personal chat
        chatFrame.setTitle(chatField);
        chatFrame.setSize(600, 450);
        chatFrame.setDefaultCloseOperation(HIDE_ON_CLOSE);//should be reopen later
        chatFrame.setResizable(false);
        chatFrame.setLocationRelativeTo(null);
        chatFrame.setLayout(null);


        JLabel idLabelGroup = new JLabel();
        idLabelGroup.setText(chatField);
        idLabelGroup.setBounds(10,10,280,30);

        //scroll pane to view texts
        JTextArea showMessageArea = new JTextArea();
        showMessageArea.setEditable(false);
        //JTextArea sendMessageArea = new JTextArea();
        JScrollPane showPane  = new JScrollPane(showMessageArea);

        showPane.setBounds(10,50,580,200);
        JScrollPane sendPane = new JScrollPane(sendMessageArea);
        sendPane.setBounds(10,260,580,80);

        JButton sendGroupMessageButton = new JButton(sendMessage);
        sendGroupMessageButton.setBounds(400, 350, 150, 50);
        sendGroupMessageButton.addActionListener(chatListener);

        chatFrame.add(idLabelGroup);
        chatFrame.add(showPane);
        chatFrame.add(sendPane);
        chatFrame.add(sendGroupMessageButton);

        chatFrame.setVisible(true);


    }
    public String getMessage(){
        return sendMessageArea.getText();
    }
    //abstract this



}
