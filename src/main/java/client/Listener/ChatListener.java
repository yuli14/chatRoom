package client.Listener;

import client.gui.ChatFrames;
import client.gui.InitFrame;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

import static client.ProtocolConstant.*;
import static utils.Msg.encodeString;

public class ChatListener implements ActionListener {
    InitFrame initFrame = null;
    JLabel idLabel = null;
    private Selector selector;
    SocketChannel socketChannel;
    private String curNickName;
    private static String DEFAULT_HOST = "localhost";
    private static int DEFAULT_PORT = 8090;
    ByteBuffer outBuffer;
    ByteBuffer inBuffer;
    //need a controller chain?
    Queue<String> messageQueue = new LinkedList<String>();
    volatile boolean allowLogin = false;
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
            initFrame.getLoginFrame().setVisible(true);
            initFrame.getRegisterFrame().setVisible(false);
            //todo check whether duplicate
        }
        else if(e.getActionCommand().equals(LOGIN)){
            String loginNickName = initFrame.getNickName();
            String passWord = initFrame.getPassword();
            //handle input validation
            if(loginNickName == null || loginNickName.equals("")){
                initFrame.showWarnMessage("please enter a nick name");
                return;
            }
            if(passWord == null || passWord.length() == 0){
                initFrame.showWarnMessage("please enter a pass word");
                return;
            }
            //start a new socket to send information to login
            //
            String msg = encodeString(LOGIN, initFrame.getNickName(), new String(initFrame.getPassword()));
            System.out.println("message is " + msg);
            writeMessageToBuffer(msg);
            startSocket();

//

            String response = receiveMessage();
            System.out.println("get response " + response);

            //todo if response start with, close connection
            //after validation
            try {
                Thread.sleep(2000);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
            if(allowLogin){
                System.out.println("successfully logged in");
                initFrame.getLoginFrame().setVisible(false);
                initFrame.startMainFrame();

            }
            else{
                System.out.println("socket closed");
                initFrame.showWarnMessage("bad");
                closeSocket();
            }

            //verify response from server and updated here, if login
            //show new messages

        }
        else if(e.getActionCommand().equals(GROUP_CHAT)){
            initFrame.startGroupChat();
        }
        else if(e.getActionCommand().equals(SEND_PRIVATE_MSG)){
            //todo
            System.out.println("private msg");
        }
        else if(e.getActionCommand().equals(SEND_GROUP_MSG)){
            //todo
            System.out.println("group msg");

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
            selector = Selector.open();
            socketChannel = SocketChannel.open();
            socketChannel.configureBlocking(false);
            socketChannel.register(selector, SelectionKey.OP_CONNECT);
            socketChannel.connect(new InetSocketAddress(DEFAULT_HOST, DEFAULT_PORT));

            boolean open = true;
            while(open){
                selector.select();
                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                while(iterator.hasNext()){
                    SelectionKey key = iterator.next();
                    iterator.remove();
                    if(key.isConnectable()){
                        socketChannel.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);
                        socketChannel.finishConnect();
                        System.out.println("finished connected to server");
                    }
                    else if(key.isWritable()){
                        //todo
//                        sendMessage("tried to write");
//                        System.out.println("?writable lo lolo");
                        //handle write here
                        try{
                            while(!messageQueue.isEmpty()){
                                String s = messageQueue.poll();

                                socketChannel.write(ByteBuffer.wrap(s.getBytes()));
                                System.out.println("wrote " + s + " to server");
                                socketChannel.register(selector, SelectionKey.OP_READ);

                            }
                        }
                        catch (IOException e){
                            e.printStackTrace();
                            open = false;
                        }

//                        byte[] bytes = new byte[]{'1','2'};
//                        outBuffer.put(bytes);
//                        if(outBuffer.remaining() == 0){
//                            System.out.println("empty");
//                        }
//                        else{
//                            outBuffer.flip();
//                            socketChannel.write(outBuffer);
//                        }
//                        if(outBuffer.remaining() == 0){
//                            System.out.println("empty");
//                            open = false;
//                            break;
//                        }
//                        else{
//                            System.out.println("sent message to server");
//                            socketChannel.write(outBuffer);
//                            outBuffer.clear();
//                        }

                    }
                    else if(key.isReadable()){
                        receiveMessage();
                        System.out.println("readable now");
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendMessage(String msg){
        //open a thread and write to buffer?
//
//        outBuffer.wrap(msg.getBytes());

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
                System.out.println("received message from server " + msg);
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

    public void writeMessageToBuffer(String msg){
//        if(outBuffer == null){
//            outBuffer = ByteBuffer.allocate(4096);
//        }
        System.out.println("wrote message");
//        outBuffer.put(msg.getBytes(StandardCharsets.UTF_8));
//        outBuffer.flip();
        messageQueue.add(msg);

    }

    public void handleResponse(String response){
        //todo
        //handle response from server
        //and update the infos to our board
        //including show messages
        //??not nessessarily has friend info
        System.out.println("handled response");
        String[] responseArr = response.split(SEPARATOR);
        if(responseArr[0].equals(VALID)){
            System.out.println("allow to login");
            allowLogin = true;
        }
    }
    public String getCurNickName() {
        return curNickName;
    }

    public void setCurNickName(String curNickName) {
        this.curNickName = curNickName;
    }
}
