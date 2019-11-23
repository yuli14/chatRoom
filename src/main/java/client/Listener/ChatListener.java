package client.Listener;

import client.gui.ChatFrames;
import client.gui.InitFrame;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import static client.ProtocolConstant.*;

public class ChatListener implements ActionListener {
    //REGISTER = "register";
    //LOGIN = "login";
    //CONFIRM = "confirm";
    //GROUP_CHAT = "group";
    //PRIVATE_CHAT = "privacy";
    //REFRESH_FRIENDS_ID = "refresh";
    //String ADD_FRIEND = "add";
    //ADD_FRIEND_CONFIRM = "add_confirm";
    InitFrame initFrame = null;
    JLabel idLabel = null;
    //ChatFrames groupChatFrame = null;
    //ChatFrames privateChatFrame = null;

    SocketChannel socketChannel;
    private String curNickName;
    public ChatListener(InitFrame initFrame, JLabel idLabel){
        this.initFrame = initFrame;
        this.idLabel = idLabel;
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        //login
        if(e.getActionCommand().equals(REGISTER)){
            //loginFrame.setLoginFrame();
            initFrame.getLoginFrame().setVisible(false);
            initFrame.registerFrame();

        }
        else if(e.getActionCommand().equals(CONFIRM)){
            String regNickName = initFrame.getRegNickName();
            char[] regPassWord = initFrame.getRegPassword();
            //todo check whether duplicate
            //handle register from server and updated here



        }
        else if(e.getActionCommand().equals(LOGIN)){
            String loginNickName = initFrame.getNickName();
            char[] passWord = initFrame.getPassword();
            //handle input validation
            if(loginNickName == null || loginNickName.equals("")){
                initFrame.showWarnMessage("please enter a nick name");
                return;
            }
            if(passWord == null || passWord.length == 0){
                initFrame.showWarnMessage("please enter a pass word");
                return;
            }
            //start a new socket to send information to login
            startSocket();
//            try{
            String msg = encodeString(LOGIN, initFrame.getNickName(), new String(initFrame.getPassword()));
            sendMessage(msg);
            String response = receiveMessage();

            //todo if response start with, close connection
            //after validation
            initFrame.startMainFrame();

            //verify response from server and updated here, if login
            //show new messages

        }
        else if(e.getActionCommand().equals(GROUP_CHAT)){
            initFrame.startGroupChat();
        }
        else if(e.getActionCommand().equals(SEND_PRIVATE_MSG)){
            //todo
        }
        else if(e.getActionCommand().equals(SEND_GROUP_MSG)){
            //todo
        }


    }
//    public static ClientService getInstance(){
//        synchronized (lock) {
//            if(service == null){
//                try {
//                    service = new ClientService();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//            return service;
//        }
//    }


    private void startSocket() {
        try {
            socketChannel = SocketChannel.open();
            socketChannel.connect(new InetSocketAddress("localhost", 8090));
            socketChannel.configureBlocking(false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendMessage(String msg){
//
        try {
            socketChannel.write(ByteBuffer.wrap(msg.getBytes()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String receiveMessage(){

        ByteBuffer buffer = ByteBuffer.allocate(1024);
        buffer.clear();
        StringBuilder sb = new StringBuilder();
        int count = 0;
        String msg = null;
        try{
            while((count = socketChannel.read(buffer)) > 0){
                sb.append(new String(buffer.array(), 0, count));
            }
            if(sb.length() > 0){
                msg = sb.toString();
                if(msg.contains(DUPLICATE)){
                    //must be initFrame here
                    initFrame.showWarnMessage("Duplicate login");
                    closeSocket();
                }
                else if(msg.equals(CLOSE)){
                    closeSocket();
                }
                else if(msg.equals(NOT_VALID)){
                    initFrame.showWarnMessage("Not valid credential");
                    closeSocket();
                }
                else{
                    //should we send the online lists here?

                    handleResponse(msg);
                }
                msg = null;
            }
        }
        catch (IOException e){
            e.printStackTrace();
        }
        return msg;
    }
    public void closeSocket(){
        //msg = null;
        initFrame.getMainFrame().setVisible(false);
        try {
            socketChannel.close();
            socketChannel.socket().close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public String encodeString(String command, String clientNickName, String message){
        return command + SEPARATOR + clientNickName + SEPARATOR + message;
    }

    public void sendRequest(){
        //send request to server
        //we must the server port
        //wrap up message
    }

    public void handleResponse(String response){
        //todo
        //handle response from server
        //and update the infos to our board
        //including show messages
        //??not nessessarily has friend info
    }
    public String getCurNickName() {
        return curNickName;
    }

    public void setCurNickName(String curNickName) {
        this.curNickName = curNickName;
    }
}
