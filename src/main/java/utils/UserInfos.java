package utils;
import java.io.*;
import java.util.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import static client.ProtocolConstant.USER_INFO_PATH;


public class UserInfos {
    private Map<String, User> userMap;
    public UserInfos(){
        userMap = new HashMap<>();
    }

    public Map<String, User> getUserMap() {
        return userMap;
    }

    @SuppressWarnings("unchecked")
    public boolean createUser(User user){
        if(this.userMap.containsKey(user.getNickName())){
            return false;
        }
        this.userMap.put(user.getNickName(), user);
        return true;
    }


    public User getUser(String nickName){
        return this.userMap.get(nickName);

    }

    public void readAllFromFile(){
        ObjectMapper objectMapper = new ObjectMapper();
        File file = new File(USER_INFO_PATH);
        try {
            User[] users = objectMapper.readValue(file, User[].class);
            for(User user: users){
                userMap.put(user.getNickName(), user);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void writeAllToFile(){
        //call this method before closing the socket
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        List<User> users = new ArrayList<>(userMap.values());
        try{
            objectMapper.writeValue(new File(USER_INFO_PATH), users);
        }
        catch (IOException e){
            e.printStackTrace();
        }


    }





    public static void main( String[] args ) throws IOException{
        char[] arr= new char[]{'2','3'};
        User user = new User("10", arr);
        UserInfos userInfos = new UserInfos();
        userInfos.readAllFromFile();
        userInfos.createUser(user);
        userInfos.writeAllToFile();
//        new JsonObj().createUser(user);
//
//        ObjectMapper objectMapper = new ObjectMapper();
//        File file = new File(USER_INFO_PATH);
//        User[] users = objectMapper.readValue(file, User[].class);
//        System.out.println("");


    }


}

