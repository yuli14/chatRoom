package utils;

import java.util.HashSet;
import java.util.Set;

public class User {
    private String nickName;
    private char[] password;
    private Set<String> friends;
    public User(String nickName, char[] password) {
        this.nickName = nickName;
        this.password = password;
        friends = new HashSet<>();
    }
    public User(){

    }




    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public char[] getPassword() {
        return password;
    }

    public void setPassword(char[] password) {
        this.password = password;
    }

    public Set<String> getFriends() {
        return friends;
    }

    public boolean addFriends(String friendsName) {
        if(friends.contains(friendsName)){
            return false;
        }
        friends.add(friendsName);
        return true;
    }



}
