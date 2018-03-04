package uel.vteam.belovedhostel.model;

import java.io.Serializable;

/**
 * Created by Hieu on 12/13/2016.
 */

public class ChattingItem implements Serializable {
   Account friendAcc;
    Messages lastMsg;

    public ChattingItem() {
    }

    public ChattingItem(Account friendAcc, Messages lastMsg) {
        this.friendAcc = friendAcc;
        this.lastMsg = lastMsg;
    }

    public Account getFriendAcc() {
        return friendAcc;
    }

    public void setFriendAcc(Account friendAcc) {
        this.friendAcc = friendAcc;
    }

    public Messages getLastMsg() {
        return lastMsg;
    }

    public void setLastMsg(Messages lastMsg) {
        this.lastMsg = lastMsg;
    }
}
