package client.gui;

import javax.swing.*;

public class ChatFrames extends JFrame {

    public void setChat(String chatField, JButton sendMessage){
        JFrame chatFrame = new JFrame();
        chatFrame.setTitle(chatField + " Chat");
        chatFrame.setSize(600, 500);
        chatFrame.setDefaultCloseOperation(HIDE_ON_CLOSE);
        chatFrame.setResizable(false);
        chatFrame.setLocationRelativeTo(null);
        chatFrame.setLayout(null);


        JLabel idLabelGroup = new JLabel();
        idLabelGroup.setText(chatField + " chat");
        idLabelGroup.setBounds(10,10,280,30);

        //scroll pane to view texts
        JTextArea showMessageArea = new JTextArea();
        showMessageArea.setEditable(false);
        JTextArea sendMessageArea = new JTextArea();
        JScrollPane showPane  = new JScrollPane(showMessageArea);

        showPane.setBounds(10,50,580,200);
        JScrollPane sendPane = new JScrollPane(sendMessageArea);
        sendPane.setBounds(10,260,580,80);

        JButton sendGroupMessageButton = new JButton("send " + chatField +  "message");
        sendGroupMessageButton.setBounds(400, 350, 150, 50);
        //button.addActionListener(listen);

        chatFrame.add(idLabelGroup);
        chatFrame.add(showPane);
        chatFrame.add(sendPane);
        chatFrame.add(sendGroupMessageButton);

        chatFrame.setVisible(true);


    }
    //abstract this

}
