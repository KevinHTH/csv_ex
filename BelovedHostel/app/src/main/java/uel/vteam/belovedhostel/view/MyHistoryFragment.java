package uel.vteam.belovedhostel.view;


import android.app.Dialog;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import uel.vteam.belovedhostel.R;
import uel.vteam.belovedhostel.adapter.MyHistoryAdapter;
import uel.vteam.belovedhostel.adapter.MyMethods;
import uel.vteam.belovedhostel.model.Bills;
import uel.vteam.belovedhostel.model.Constant;


public class MyHistoryFragment extends Fragment  {


    View v;
    ExpandableListView elvMyHistory;
    MyHistoryAdapter myHistoryAdapter;
    ArrayList<Bills> arrDataHeader=new ArrayList<>();


    DatabaseReference root;
    String myId;
    Dialog dialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_my_history, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(getResources().getString(R.string.myhistory_fragment_title));
        root = FirebaseDatabase.getInstance().getReference();
        myId = getCurrentAppId();
        addControls();
        loadDataFromFireBase();
        myHistoryAdapter=new MyHistoryAdapter(getContext(),arrDataHeader);
        elvMyHistory.setAdapter(myHistoryAdapter);
        return v;
    }

    private void addControls() {
        elvMyHistory= (ExpandableListView) v.findViewById(R.id.elvMyHistory);

    }

    private void loadDataFromFireBase() {
        dialog=new Dialog(getContext(),R.style.dialogStyle);
        MyMethods.getInstance().displayCustomProgress(getContext(),dialog,getResources().getString(R.string.loading));
        root.child(Constant.FB_KEY_USER_ROOT).child(myId).child(Constant.FB_KEY_USER_BILL)
                .child(Constant.FB_KEY_USER_ROOM).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                arrDataHeader.clear();
                for (DataSnapshot oneData: dataSnapshot.getChildren()){
                    Bills bills =  oneData.getValue(Bills.class);
                    arrDataHeader.add(bills);
                }
                myHistoryAdapter.notifyDataSetChanged();
                if (arrDataHeader.size()==0){
                    Snackbar snackbar = Snackbar
                            .make(v,getResources().getString(R.string.myhistory_fragment_toast_list_null),
                                    Snackbar.LENGTH_LONG);
                    snackbar.setDuration(4000).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Snackbar snackbar = Snackbar
                        .make(v,getResources().getString(R.string.system_error), Snackbar.LENGTH_LONG);
                snackbar.setDuration(4000).show();
            }
        });
        MyMethods.getInstance().dismissCustomProgress(getContext(),dialog,500);
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


}
