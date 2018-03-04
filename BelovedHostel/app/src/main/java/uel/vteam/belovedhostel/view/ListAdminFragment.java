package uel.vteam.belovedhostel.view;


import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.r0adkll.slidr.model.SlidrConfig;
import java.util.ArrayList;

import uel.vteam.belovedhostel.R;
import uel.vteam.belovedhostel.adapter.AdminAdapter;
import uel.vteam.belovedhostel.adapter.MyMethods;
import uel.vteam.belovedhostel.model.Account;
import uel.vteam.belovedhostel.model.Constant;

/**
 * A simple {@link Fragment} subclass.
 */
public class ListAdminFragment extends Fragment {

    View view;
    DatabaseReference root;

    ListView lvAdmin;
    ArrayList<Account> arrAdmin;
    AdminAdapter adminAdapter;

    Account myAccount;
    String myId;
    SlidrConfig mConfig;
    Dialog dialog;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_list_admin, container, false);
        root= FirebaseDatabase.getInstance().getReference();
        myId=getCurrentAppId();
        addControls();
        getDataFromFireBase();
        addEvents();
        return view;
    }

    private void addEvents() {
        lvAdmin.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
                final Account acSelected = arrAdmin.get(position);
                root.child(Constant.FB_KEY_USER_ROOT).child(myId)
                        .child(Constant.FB_KEY_USER_INFO).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        myAccount=dataSnapshot.getValue(Account.class);
                        Bundle userData=new Bundle();
                        userData.putSerializable(Constant.BUNDLE_MY_ACCOUNT,myAccount);
                        userData.putSerializable(Constant.BUNDLE_SELECTED_ACCOUNT, acSelected);
                        Intent i=new Intent(getActivity(),ChatActivity.class);
                        i.putExtras(userData);
                        startActivity(i);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Snackbar snackbar = Snackbar
                                .make(view,getResources().getString(R.string.system_error), Snackbar.LENGTH_LONG);
                        snackbar.setDuration(4000).show();
                    }
                });
            }
        });
    }

    private void getDataFromFireBase() {
        dialog=new Dialog(getContext(),R.style.dialogStyle);
        MyMethods.getInstance().displayCustomProgress(getContext(),dialog,
                getResources().getString(R.string.loading));

        root.child("USER").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                arrAdmin.clear();
                for (DataSnapshot oneData: dataSnapshot.getChildren()) {

                    String permission= (String) oneData.child(Constant.FB_KEY_USER_INFO)
                            .child(Constant.FB_KEY_USER_PERMISSION).getValue();
                    if (permission.equalsIgnoreCase("admin")){
                        Account admin=oneData.child(Constant.FB_KEY_USER_INFO)
                                .getValue(Account.class);
                        arrAdmin.add(admin);
                    }
                    if (oneData.getKey().equalsIgnoreCase(myId)){
                        myAccount=oneData.child(Constant.FB_KEY_USER_INFO)
                                .getValue(Account.class);
                    }
                }  adminAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Snackbar snackbar = Snackbar
                        .make(view,getResources().getString(R.string.system_error),
                                Snackbar.LENGTH_LONG);
                snackbar.setDuration(4000).show();
            }
        });
        MyMethods.getInstance().dismissCustomProgress(getContext(),dialog,1000);
    }



    private void addControls() {
        lvAdmin= (ListView) view.findViewById(R.id.lvListAdmin);
        arrAdmin=new ArrayList<>();
        adminAdapter=new AdminAdapter(getContext(),arrAdmin);
        lvAdmin.setAdapter(adminAdapter);
    }

    private String getCurrentAppId() {
        String strId="";
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            strId = user.getUid();
        } else {
            strId="";
        }
        return strId;
    }

}
