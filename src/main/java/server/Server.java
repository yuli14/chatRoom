package server;

import server.gui.ServerGui;
import utils.Msg;
import utils.User;
import utils.UserInfos;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

import static client.ProtocolConstant.*;

public class Server {
    private ServerGui serverGui;
    private Map<String, UserSocket> connectedClient;

    private ServerSocket serverSocket;
    private UserInfos userInfos;
    private Selector selector;
    private InetSocketAddress listenAddress;
    int port;
    Queue<Msg> msgs = new ConcurrentLinkedQueue<Msg>();
    Map<String, SelectionKey> nameToSocket;
    Map<SelectionKey, String> socketToName;

    //    public Server(){
//        serverGui = new ServerGui();
//        connectedClient = new HashMap<>();
//        try {
//            serverSocket = new ServerSocket();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
    public static void main(String[] args){

    }
    public Server(String address, int port){
        this.listenAddress = new InetSocketAddress(address, port);
    }

    public void launchServer() throws IOException {
        //create a new servergui, read infos from
        //the json file
        //start the serversocket and wait for connection
        serverGui = new ServerGui();
        serverGui.initServerFrame();
        connectedClient = new HashMap<>();
        userInfos = new UserInfos();
        userInfos.readAllFromFile();

        try {
            this.selector = Selector.open();
            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.bind(this.listenAddress);
            serverSocketChannel.register(this.selector, SelectionKey.OP_ACCEPT);

            System.out.println("server started");
        } catch (IOException e) {
            e.printStackTrace();
        }
        //init serversocket
//        try{
//            serverSocket = new ServerSocket(port);
//        }
//        catch (IOException e){
//            e.printStackTrace();
//        }

        while(true){
            serverGui.setClientCountArea("number of connected sockets are: " + connectedClient.size());

            //accept a new socket
            //if it's register, we add to our channel
            //wait for events
            this.selector.select();

            ////work on selected keys
            Iterator keys = this.selector.selectedKeys().iterator();
            boolean alreadyWrote = false;
            while(keys.hasNext()){
                //handle one by one
                SelectionKey selectedKey = (SelectionKey) keys.next();
                String NickNameAndPassWord = (String)selectedKey.attachment();
                //todo
                //remove to prevent iterate again
                keys.remove();

                if(!selectedKey.isValid()){
                    continue;
                }
                if(selectedKey.isAcceptable()){
                    this.accept(selectedKey);
                }
                else if(selectedKey.isReadable()){
                    this.read(selectedKey);
                }
                else if(selectedKey.isWritable()){
                    this.write(selectedKey);
                    alreadyWrote = true;

                }
            }
            if(alreadyWrote){
                msgs.clear();
            }

        }
    }
    public void accept(SelectionKey key) {
        ServerSocketChannel serverChannel = (ServerSocketChannel) key.channel();
        System.out.println("log: client connects...");
        try {
            SocketChannel socketChannel = serverChannel.accept();
            socketChannel.configureBlocking(false);
            socketChannel.register(selector, SelectionKey.OP_READ);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void read(SelectionKey key){
        //read from server
        SocketChannel socketChannel = (SocketChannel) key.channel();
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        StringBuffer sb = new StringBuffer();
        int c = 0;
        try {
            while((c = socketChannel.read(buffer)) > 0){
                buffer.flip();
                int size = buffer.remaining();
                byte[] bytes = new byte[size];
                buffer.get(bytes, 0, size);
                sb.append(new String(bytes));
            }
            if(c == -1){
                System.out.println("log: client closes");
                socketChannel.close();
                return;
            }else{
                System.out.println("log: msg-> " + sb);
                handleMsg(sb.toString(), key);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public void write(SelectionKey key){
        //write to server
        SocketChannel sc = (SocketChannel) key.channel();
        Iterator<Msg> itr = msgs.iterator();
        while(itr.hasNext()){
            Msg msg = itr.next();
            if(msg.getWho().equals(SEND_GROUP_MSG) || msg.getWho().equals(socketToName.get(key))){
                ByteBuffer buffer = ByteBuffer.allocate(1024);
                String m = msg.getMsg();
                buffer.put(m.getBytes());
                buffer.flip();
                try {
                    sc.write(buffer);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        try {
            sc.register(selector, SelectionKey.OP_READ);
        } catch (ClosedChannelException e) {
            e.printStackTrace();
        }

}

    public String[] decodeString(String message){
        //first string is the command
        //second string is the socket id
        //third string is the target socket id
        //last is the message itself
        return message.split(SEPARATOR);
    }

    //handle
    private void handleMsg(String message, SelectionKey key) {
        //handle message sent from client
        String[] decodedMsg = decodeString(message);
        String cmd = decodedMsg[0];
        String nickName = decodedMsg[1];
        if(cmd.equals(LOGIN)){
            String password = decodedMsg[2];
            Map<String, User> userMap = userInfos.getUserMap();
            //SEND BACK IF ALLOWED TO LOGIN OR NOT
            Msg msg = new Msg();
            if(userMap.containsKey(nickName)){
                msg.setWho(nickName);//
            }
            //return userMap.containsKey(nickName) && new String(userMap.get(nickName).getPassword()).equals(password);
        }
        else if(cmd.equals(SEND_PRIVATE_MSG)){
            Msg msg = new Msg();
            String targetNickName = decodedMsg[2];
            SelectionKey targetClientKey = nameToSocket.get(targetNickName);
            msg.setWho(targetNickName);
            msg.setMsg(decodedMsg[3]);
            msgs.add(msg);
            if(targetClientKey == null){
                //handle later
            }
            else {
                SocketChannel sc = (SocketChannel) targetClientKey.channel();
                try {
                    sc.register(selector, targetClientKey.interestOps() | SelectionKey.OP_WRITE);
                } catch (ClosedChannelException e) {
                    e.printStackTrace();
                }
            }

        }
        else if(cmd.equals(SEND_GROUP_MSG)){
            Msg msg = new Msg();
            msg.setMsg(decodedMsg[2]);
            msgs.add(msg);
            for(Map.Entry<String, SelectionKey> entry: nameToSocket.entrySet()){
                SelectionKey targetClientKey = entry.getValue();
                SocketChannel sc = (SocketChannel) targetClientKey.channel();
                try {
                    sc.register(selector, targetClientKey.interestOps() | SelectionKey.OP_WRITE);
                } catch (ClosedChannelException e) {
                    e.printStackTrace();
                }
            }

        }
        else{

        }
    }


    private void register(byte[] bytes, UserSocket userSocket){

    }
}
