package client.Listener;

import client.gui.InitFrame;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class UserListListener implements MouseListener {
    private String targetId = null;
    private InitFrame initFrame;
    private JList onlineUserList;
    public UserListListener(InitFrame initFrame, JList onlineUserList){
        this.initFrame = initFrame;
        this.onlineUserList = onlineUserList;
    }
    @Override
    public void mouseClicked(MouseEvent e) {
        if(e.getButton() == MouseEvent.BUTTON1 && e.getClickCount() == 2){
            targetId = (String)onlineUserList.getSelectedValue();
        }
        if(targetId == null || targetId.equals(initFrame.getChatListener().getCurNickName())){
            return;
            //talk to yourself is not allowed?
        }
        initFrame.startPrivateChat(targetId);

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
