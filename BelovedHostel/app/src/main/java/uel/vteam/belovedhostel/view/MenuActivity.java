package uel.vteam.belovedhostel.view;

import android.app.Dialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import android.graphics.Bitmap;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Handler;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AlertDialog;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;


import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import uel.vteam.belovedhostel.MyInterface.SupportUI;
import uel.vteam.belovedhostel.R;
import uel.vteam.belovedhostel.adapter.MyMethods;
import uel.vteam.belovedhostel.model.Constant;

public class MenuActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    private static String phone = "";
    private static String name = "";
    String myId = "";
    FrameLayout mainLayout;
    Dialog progress;
    RelativeLayout loadingPanel;
    private Toolbar toolbar = null;
    private NavigationView navigationView = null;
    private Fragment fragment = null;
    private ImageView imgAvatar;
    private TextView txtCustomerName, txtCustomerPhone, txtLogout;
    private String customerPhone, customerName;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference root;
    private FragmentManager fragManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        myId = getCurrentAppId();
        if (myId != null) {
            fragManager = getSupportFragmentManager();
            root = FirebaseDatabase.getInstance().getReference();
            mAuth = FirebaseAuth.getInstance();
            addControls();
            addEvents();

            listenBooking();

            // listen broadcast to open fragment
            Intent intent = getIntent();
            String action = intent.getAction();
            if (action != null) {
                if (action.equals(Constant.BUNDLE_ACTION_CONTROL_OPEN)) {
                    fragment = new MyHistoryFragment();
                    replaceFragment(fragment, R.id.fragment_container);
                } else if (action.equals(Constant.BUNDLE_ACTION_NEW_MSG)) {
                    fragment = new TabSupportFragment();
                    replaceFragment(fragment, R.id.fragment_container);
                }
            }
        } else {
            Snackbar.make(mainLayout, getResources().getString(R.string.system_error), 5000).show();
        }


    }

    @Override
    protected void onResume() {
        super.onResume();
        listenLogin();
    }

    private void listenBooking() {

        root.child(Constant.FB_KEY_USER_ROOT).child(myId).child(Constant.FB_KEY_USER_BILL)
                .child(Constant.FB_KEY_USER_ROOM).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                NotificationCompat.Builder builder =
                        new NotificationCompat.Builder(MenuActivity.this)
                                .setSmallIcon(R.drawable.ic_event_note_white_48px)
                                .setContentTitle(getResources().getString(R.string.myhistory_fragment_notification_title))
                                .setContentText(getResources().getString(R.string.myhistory_fragment_notification_progress))
                                .setAutoCancel(true);
                // set action when click notification
                Intent resultIntent = new Intent(MenuActivity.this, MenuActivity.class);
                resultIntent.setAction(Constant.BUNDLE_ACTION_CONTROL_OPEN);
                PendingIntent pendingIntent = PendingIntent.getActivity(MenuActivity.this,
                        0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                builder.setContentIntent(pendingIntent);
                //set tone
                Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                builder.setSound(uri);
                // set vibrate
                Vibrator mVibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                boolean hasVibrator = mVibrator.hasVibrator();
                if(hasVibrator){
                    long[] v = {500, 500};
                    builder.setVibrate(v);
                }


                NotificationManager notificationManager = (NotificationManager)
                        MenuActivity.this.getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.notify(11, builder.build());
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

    private void listenLogin() {

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    loadingPanel.setVisibility(View.VISIBLE);
                    setOnline(myId);
                    DatabaseReference userRoot = root.child(Constant.FB_KEY_USER_ROOT)
                            .child(myId).child(Constant.FB_KEY_USER_INFO);
                    userRoot.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            customerName = dataSnapshot.child(Constant.FB_KEY_USER_NAME).getValue().toString();
                            customerPhone = dataSnapshot.child(Constant.FB_KEY_USER_PHONE).getValue().toString();

                            txtCustomerName.setText(customerName);
                            txtCustomerPhone.setText(customerPhone);
                            String imageUrl = dataSnapshot.child(Constant.FB_KEY_USER_AVATAR).getValue().toString();
                            if (imageUrl != null) {
                                Glide.with(getApplicationContext()).load(imageUrl).asBitmap().centerCrop()
                                        .into(new BitmapImageViewTarget(imgAvatar) {
                                            @Override
                                            protected void setResource(Bitmap resource) {
                                                RoundedBitmapDrawable circularBitmapDrawable =
                                                        RoundedBitmapDrawableFactory.create(getApplicationContext().getResources(), resource);
                                                circularBitmapDrawable.setCircular(true);
                                                imgAvatar.setImageDrawable(circularBitmapDrawable);
                                            }
                                        });

                            }
                            name = customerName;
                            phone = customerPhone;
                            loadingPanel.setVisibility(View.GONE);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            loadingPanel.setVisibility(View.GONE);
                        }
                    });
                } else {
                    finish();
                }
            }
        };
        mAuth.addAuthStateListener(mAuthListener);
    }

    private void addControls() {

        progress = new Dialog(this, R.style.dialogStyle);

        mainLayout = (FrameLayout) findViewById(R.id.fragment_container);
        // set toolbar. navigation
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white1));

        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // set info header navagation
        View v = navigationView.getHeaderView(0);
        imgAvatar = (ImageView) v.findViewById(R.id.imgAvatar);

        txtCustomerName = (TextView) v.findViewById(R.id.txtHeaderNameUser);
        txtCustomerPhone = (TextView) v.findViewById(R.id.txtHeaderPhoneUser);
        txtLogout = (TextView) v.findViewById(R.id.txtLogoutButton);
        loadingPanel = (RelativeLayout) v.findViewById(R.id.loadingPanel_avatar_menu);

        // main fragment load first
        fragment = new SearchFragment();
        replaceFragment(fragment, R.id.fragment_container);
    }

    private void addEvents() {
        txtLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(MenuActivity.this);
                builder.setMessage(getResources().getString(R.string.menu_activity_dialog_signout_question));
                builder.setPositiveButton(getResources().getString(R.string.menu_activity_dialog_signout_yes),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                MyMethods.getInstance().displayCustomProgress(
                                        MenuActivity.this, progress, getResources().getString(R.string.loading));
                                setOffline(myId);
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        mAuth.signOut();
                                        progress.dismiss();
                                    }
                                }, 2000);
                            }
                        });
                builder.setNegativeButton(getResources().getString(R.string.menu_activity_dialog_signout_no),
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                builder.create().show();
            }
        });
    }


    private void setOffline(String userId) {
        root.child(Constant.FB_KEY_USER_ROOT).child(userId).child(Constant.FB_KEY_USER_INFO)
                .child(Constant.FB_KEY_USER_STATUS).setValue("false");
    }

    private void setOnline(String userId) {
        root.child(Constant.FB_KEY_USER_ROOT).child(userId).child(Constant.FB_KEY_USER_INFO)
                .child(Constant.FB_KEY_USER_STATUS).setValue("true");
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        int count = fragManager.getBackStackEntryCount();
        switch (id) {
            case R.id.nav_booking:
                fragment = new SearchFragment();
                break;
            case R.id.nav_service:
                fragment = new MenuServiceFragment();
                break;
            case R.id.nav_map:
                fragment = new MapFragment();
                break;
            case R.id.nav_support:
                if (MyMethods.getInstance().checkNetwork(this)) {
                    fragment = new TabSupportFragment();
                } else {
                    Snackbar snackbar = Snackbar
                            .make(mainLayout, getResources().getString(R.string.error_no_network), Snackbar.LENGTH_LONG);
                    snackbar.setDuration(4000).show();
                }

                break;
            case R.id.nav_contact:
                fragment = new ContactFragment();
                break;
            case R.id.nav_history:
                if (MyMethods.getInstance().checkNetwork(this)) {
                    fragment = new MyHistoryFragment();
                } else {
                    Snackbar snackbar = Snackbar
                            .make(mainLayout, getResources().getString(R.string.error_no_network), Snackbar.LENGTH_LONG);
                    snackbar.setDuration(4000).show();
                }

                break;
            case R.id.nav_account:
                if (MyMethods.getInstance().checkNetwork(this)) {
                    Intent intent = new Intent(this, MyProfileActivity.class);
                    startActivityForResult(intent, 999);
                } else {
                    Snackbar snackbar = Snackbar
                            .make(mainLayout, getResources().getString(R.string.error_no_network), Snackbar.LENGTH_LONG);
                    snackbar.setDuration(4000).show();
                }
                break;
        }


        replaceFragment(fragment, R.id.fragment_container);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 999) {
            String result = data.getStringExtra(Constant.BUNDLE_RESULT_UPDATE_PROFILE);
            String message = "";
            if (result.equals("successful")) {
                message = getResources().getString(R.string.profile_fragment_toast_update_successful);
            } else if (result.equals("fault_timeout")) {
                message = getResources().getString(R.string.profile_fragment_toast_error_change_pass);
            } else if (result.equals("fault_data")) {
                message = getResources().getString(R.string.profile_fragment_toast_error_update);
            } else {
                message = getResources().getString(R.string.profile_fragment_toast_error_update);
            }
            SupportUI.getInstance().customToast(this, R.layout.activity_my_profile, message);
        }
    }

    public void replaceFragment(Fragment fragment, int replacedFrame) {
        String backStateName = fragment.getClass().getName();
        String fragmentTag = backStateName;
        boolean fragmentPopped = fragManager.popBackStackImmediate(backStateName, 0);
        if (!fragmentPopped && fragManager.findFragmentByTag(fragmentTag) == null) { //fragment not in back stack, create it.
            FragmentTransaction ft = fragManager.beginTransaction();
            ft.replace(replacedFrame, fragment, fragmentTag);
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            ft.addToBackStack(backStateName);
            ft.commit();
        }
    }

    public Bundle getData() {
        Bundle bundle = new Bundle();
        bundle.putString(Constant.BUNDLE_USER_ID, myId);
        bundle.putString(Constant.BUNDLE_USER_PHONE, phone);
        bundle.putString(Constant.BUNDLE_USER_NAME, name);
        return bundle;
    }

    @Override
    public void onBackPressed() {
        if (fragManager.getBackStackEntryCount() > 1) {
            fragManager.popBackStack();
            return;
        } else {

        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        name = null;
        phone = null;
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

   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
          case   R.id.item_AboutApp:
              Intent app=new Intent(this,InformationActivity.class);
              startActivity(app);
              break;
            case R.id.itemAboutAuthor:
                Intent author=new Intent(this,InformationActivity.class);
                startActivity(author);
                break;
        }
        return super.onOptionsItemSelected(item);
    }*/
}
