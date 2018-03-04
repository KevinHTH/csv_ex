package uel.vteam.belovedhostel.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrConfig;
import com.r0adkll.slidr.model.SlidrPosition;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import uel.vteam.belovedhostel.R;
import uel.vteam.belovedhostel.adapter.MessageAdapter;
import uel.vteam.belovedhostel.model.Account;
import uel.vteam.belovedhostel.model.Constant;
import uel.vteam.belovedhostel.model.Messages;

public class ChatActivity extends AppCompatActivity {

    static int color = 0;
    EditText edNhap;
    Button imbSend;
    String myId, friendId;
    DatabaseReference root;
    DatabaseReference chatRoot;
    Account myAccount, acSelected;
    SlidrConfig mConfig;
    private RecyclerView mRecyclerView;
    private MessageAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<Messages> messageList = new ArrayList();

    Toolbar toolbarChat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        root = FirebaseDatabase.getInstance().getReference();
        getData();
        color = getResources().getColor(R.color.LightGray);

        myId = getCurrentAppId();
        friendId = getFriendId();

        addControls();
        listenMessage();
        addEvents();

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public Messages getMessages() {
        // kiem tra sessionId co roi thi khoi tao
        String text = edNhap.getText().toString();
        Date timeSend = new Date();

        String sender = myAccount.getUserName();
        Messages message = new Messages(sender, text, timeSend);
        return message;
    }

    private void sendMessage() {
        root.child(Constant.FB_KEY_USER_ROOT).child(myId)
                .child(Constant.FB_KEY_USER_SESSION_CHAT)
                .child(friendId).child(Constant.FB_KEY_USER_FRIEND_INFO).setValue(acSelected);
        chatRoot = root.child(Constant.FB_KEY_USER_ROOT)
                .child(myId).child(Constant.FB_KEY_USER_SESSION_CHAT)
                .child(friendId).child(Constant.FB_KEY_USER_CHAT_BOX);
        chatRoot.push().setValue(getMessages());

        root.child(Constant.FB_KEY_USER_ROOT).child(friendId)
                .child(Constant.FB_KEY_USER_SESSION_CHAT)
                .child(myId).child(Constant.FB_KEY_USER_FRIEND_INFO).setValue(myAccount);
        chatRoot = root.child(Constant.FB_KEY_USER_ROOT)
                .child(friendId).child(Constant.FB_KEY_USER_SESSION_CHAT)
                .child(myId).child(Constant.FB_KEY_USER_CHAT_BOX);
        chatRoot.push().setValue(getMessages());

    }


    private void listenMessage() {
        chatRoot = root.child(Constant.FB_KEY_USER_ROOT).child(myId)
                .child(Constant.FB_KEY_USER_SESSION_CHAT).child(friendId).child(Constant.FB_KEY_USER_CHAT_BOX);
        chatRoot.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Messages m = dataSnapshot.getValue(Messages.class);
                messageList.add(m);
                mAdapter.updateList(messageList);

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private String getCurrentAppId() {
        String strId = "";
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            strId = user.getUid();
        } else {
        }
        return strId;

    }

    public String getFriendId() {
        String frId = "";
        frId = acSelected.getUserId().toString();
        return frId;
    }


    private void addEvents() {
        mAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                mRecyclerView.scrollToPosition(mAdapter.getItemCount() - 1);
            }
        });
        imbSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if (!edNhap.getText().toString().isEmpty()){
                   sendMessage();
                   edNhap.setText("");
               }

            }
        });

           mRecyclerView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
               @Override
               public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                   mRecyclerView.scrollToPosition(mAdapter.getItemCount() - 1);
               }
           });

    }

    private void getData() {
        Intent i = getIntent();
        myAccount = (Account) i.getSerializableExtra(Constant.BUNDLE_MY_ACCOUNT);
        acSelected = (Account) i.getSerializableExtra(Constant.BUNDLE_SELECTED_ACCOUNT);
    }



    private void addControls() {

        edNhap = (EditText) findViewById(R.id.edNhap);
        imbSend = (Button) findViewById(R.id.btnSend);

        toolbarChat= (Toolbar) findViewById(R.id.toolbarChat);
        if (acSelected.getStatus().equalsIgnoreCase("true")) {
            toolbarChat.setSubtitle(getResources().getString(R.string.chat_activity_status_online));
        } else {
            toolbarChat.setSubtitle(getResources().getString(R.string.chat_activity_status_offline));
        }
        toolbarChat.setTitle(acSelected.getUserName());
        toolbarChat.setTitleTextColor(getResources().getColor(R.color.white1));
        toolbarChat.setSubtitleTextColor(getResources().getColor(R.color.white2));
        setSupportActionBar(toolbarChat);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(getResources().getDrawable(R.drawable.ic_arrow_back_white_18px));
        toolbarChat.setBackgroundColor(getResources().getColor(R.color.DeepskyBlue));

        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new MessageAdapter(ChatActivity.this, messageList, myAccount, acSelected);
        mRecyclerView.setBackgroundColor(color);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setVerticalScrollBarEnabled(false);


        mConfig = new SlidrConfig.Builder()
                .position(SlidrPosition.LEFT)
                .velocityThreshold(2400)
                .distanceThreshold(.25f)
                .edge(true)
                .build();

        Slidr.attach(this, mConfig);


    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
