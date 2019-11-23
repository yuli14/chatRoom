package client;

import java.security.SecureRandom;

public class ProtocolConstant {
    public static String REGISTER = "register";
    public static String LOGIN = "login";
    public static String CONFIRM = "confirm";
    public static String GROUP_CHAT = "group";
    public static String SEND_GROUP_MSG = "send group message";
    public static String PRIVATE_CHAT = "privacy";
    public static String SEND_PRIVATE_MSG = "send_private";

    public static String REFRESH_FRIENDS_ID = "refresh";
    //public static String ADD_FRIEND = "add";
    //public static String ADD_FRIEND_CONFIRM = "add_confirm";
    public static String USER_INFO_PATH = "userInfo.json";
    public static String SEPARATOR;
    public static String CLOSE = "close";
    public static String DUPLICATE = "duplicate";
    public static String NOT_VALID = "notvalid";
    private static final String SYMBOLS =
            "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static SecureRandom rnd = new SecureRandom();

    public void generateRandomProtocol(){
        //generate random protocol for every instance
        //and write it to file
        int len = 25;
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            sb.append(SYMBOLS.charAt(rnd.nextInt(SYMBOLS.length())));
        }


    }




}
