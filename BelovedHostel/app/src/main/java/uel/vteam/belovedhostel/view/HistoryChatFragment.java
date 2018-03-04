package uel.vteam.belovedhostel.view;


import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import uel.vteam.belovedhostel.R;
import uel.vteam.belovedhostel.adapter.HistoryChatAdapter;
import uel.vteam.belovedhostel.adapter.MyMethods;
import uel.vteam.belovedhostel.model.Account;
import uel.vteam.belovedhostel.model.ChattingItem;
import uel.vteam.belovedhostel.model.Constant;
import uel.vteam.belovedhostel.model.Messages;


public class HistoryChatFragment extends Fragment {

    RecyclerView rvChatting;
    HistoryChatAdapter chattingAdapter;
    RecyclerView.LayoutManager mLayoutManager;


    String myId;
    ArrayList<Messages> listLastMsg=new ArrayList<>();
    ArrayList<Account> arrFriendAccount;

    ArrayList<ChattingItem> arrChatItem;
    DatabaseReference root;
    View v;
    Dialog dialog;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v= inflater.inflate(R.layout.fragment_history_chat, container, false);
        addControls();
        root = FirebaseDatabase.getInstance().getReference();
        myId = getCurrentAppId();
        getDataFromFireBase();
        return  v;
    }
    private void getDataFromFireBase() {
        dialog=new Dialog(getContext(),R.style.dialogStyle);
        MyMethods.getInstance().displayCustomProgress(getContext(),dialog, getResources().getString(R.string.loading));
        root.child(Constant.FB_KEY_USER_ROOT).child(myId).child(Constant.FB_KEY_USER_SESSION_CHAT)
                .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listLastMsg.clear();
                arrFriendAccount.clear();
                arrChatItem.clear();
                chattingAdapter.clearListAccount();
                for (DataSnapshot oneFriend : dataSnapshot.getChildren()) {
                    String friendId = oneFriend.getKey();
                    final ChattingItem chatItem = new ChattingItem();

                    int size = (int) oneFriend.child(Constant.FB_KEY_USER_CHAT_BOX).getChildrenCount();
                    int i = 1;
                    for (DataSnapshot oneMsg : oneFriend.child(Constant.FB_KEY_USER_CHAT_BOX).getChildren()) {
                        if (i++ == size) {
                            Messages msg = oneMsg.getValue(Messages.class);
                            chatItem.setLastMsg(msg);
                        }
                    }
                    Account friendAcc = oneFriend.child(Constant.FB_KEY_USER_FRIEND_INFO).getValue(Account.class);
                    chatItem.setFriendAcc(friendAcc);
                    arrChatItem.add(chatItem);
                }  chattingAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Snackbar snackbar = Snackbar
                        .make(v,getResources().getString(R.string.system_error), Snackbar.LENGTH_LONG);
                snackbar.setDuration(4000).show();
            }
        });
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                dialog.dismiss();
            }
        },500);
    }
    @Override
    public void onResume() {
        super.onResume();

    }
    private String getCurrentAppId() {
        String strId = "";
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            strId = user.getUid();

        } else {
            strId = "";
        }
        return strId;
    }

    private void addControls() {
        rvChatting= (RecyclerView) v.findViewById(R.id.rvChatting);
        rvChatting.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext());
        rvChatting.setLayoutManager(mLayoutManager);

        listLastMsg=new ArrayList<>();
        arrFriendAccount=new ArrayList<>();
        arrChatItem=new ArrayList<>();
        chattingAdapter=new HistoryChatAdapter(getContext(),arrChatItem);
        rvChatting.setAdapter(chattingAdapter);
        rvChatting.setItemAnimator(new DefaultItemAnimator());

    }

}
