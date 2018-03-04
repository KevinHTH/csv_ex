package uel.vteam.belovedhostel.view;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.r0adkll.slidr.model.SlidrPosition;

import java.io.File;

import uel.vteam.belovedhostel.MyInterface.SupportUI;
import uel.vteam.belovedhostel.MyInterface.TextValidator;
import uel.vteam.belovedhostel.R;
import uel.vteam.belovedhostel.adapter.MyMethods;
import uel.vteam.belovedhostel.model.Account;
import uel.vteam.belovedhostel.model.Constant;


public class MyProfileActivity extends AppCompatActivity
        implements OnCompleteListener, OnFailureListener {

    EditText editName, editPhone;
    ImageView imgEditName, imgEditPhone, imgAvatarProfile;
    FrameLayout layoutAvatarProfile;
    RelativeLayout loadingPanel;
    Button btnUpdateProfile;
    TextView linkChangePass;

  //  Dialog customProgress;
    String myId;
    DatabaseReference root;
    Account myAccount;
    FirebaseStorage storage;
    StorageReference storageRef;
    String urlDownload = "";

    boolean nameClick = false;
    boolean phoneClick = false;


    Uri url;
    Cursor c = null;
    Dialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
        addControls();
        getDataFromFirebase();
        addEvents();
    }

    private void addEvents() {
        linkChangePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(MyProfileActivity.this,ChangePasswordActivity.class);
                startActivity(i);
                finish();
            }
        });
        imgEditName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (nameClick == false) {
                    editName.setEnabled(true);
                    nameClick = true;
                } else {
                    editName.setEnabled(false);
                    nameClick = false;
                }

            }
        });
        imgEditPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (phoneClick == false) {
                    editPhone.setEnabled(true);
                    phoneClick = true;
                } else {
                    editPhone.setEnabled(false);
                    phoneClick = false;
                }
            }
        });

        btnUpdateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!TextValidator.getInstance(getApplicationContext()).validateText(editName)) {
                    return;
                }
                if (!TextValidator.getInstance(getApplicationContext()).validatePhone(editPhone)) {
                    return;
                }
               else {
                    if (nameClick == true) {
                        if (phoneClick==true){
                            MyMethods.getInstance().displayCustomProgress(MyProfileActivity.this,dialog,
                                    getResources().getString(R.string.loading));
                            root.child(Constant.FB_KEY_USER_ROOT).child(myId).child(Constant.FB_KEY_USER_INFO)
                                    .child(Constant.FB_KEY_USER_NAME).setValue(editName.getText().toString());
                            root.child(Constant.FB_KEY_USER_ROOT).child(myId).child(Constant.FB_KEY_USER_INFO)
                                    .child(Constant.FB_KEY_USER_PHONE)
                                    .setValue(editPhone.getText().toString())
                                    .addOnCompleteListener(MyProfileActivity.this)
                                    .addOnFailureListener(MyProfileActivity.this);
                        }else {
                            MyMethods.getInstance().displayCustomProgress(MyProfileActivity.this,
                                    dialog,getResources().getString(R.string.loading));
                            root.child(Constant.FB_KEY_USER_ROOT).child(myId).child(Constant.FB_KEY_USER_INFO)
                                    .child(Constant.FB_KEY_USER_NAME)
                                    .setValue(editName.getText().toString())
                                    .addOnCompleteListener(MyProfileActivity.this)
                                    .addOnFailureListener(MyProfileActivity.this);
                        }
                    } else if (phoneClick==true){
                        root.child(Constant.FB_KEY_USER_ROOT).child(myId).child(Constant.FB_KEY_USER_INFO)
                                .child(Constant.FB_KEY_USER_PHONE)
                                .setValue(editPhone.getText().toString())
                                .addOnCompleteListener(MyProfileActivity.this)
                                .addOnFailureListener(MyProfileActivity.this);
                    }




                }


            }

        });


        layoutAvatarProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkDataAvailable()){
                    Intent i = new Intent(MyProfileActivity.this, GalleryPictureActivity.class);
                    startActivityForResult(i, Constant.REQUEST_UPDATE_AVATAR);
                }else {
                    SupportUI.getInstance().customToast(MyProfileActivity.this,
                            R.layout.activity_my_profile,
                            getResources().getString(R.string.error_no_image));
                }

            }
        });

    }
    private boolean checkDataAvailable() {
        boolean isAvailable=false;
        File sdCard= Environment.getExternalStorageDirectory();
        if (sdCard.exists()){
            url = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        }else {
            url= MediaStore.Images.Media.INTERNAL_CONTENT_URI;
        }
        String[] projection = {MediaStore.Images.ImageColumns.DATA};

        if (url != null) {
            c = managedQuery(url, projection, null, null, null);
        }
        if ((c != null) && (c.moveToFirst())) {
            isAvailable=true;
        }
        return isAvailable;
    }
    @Override
    public void onComplete(@NonNull Task task) {
        Intent ketqua=new Intent();

        if (task.isSuccessful()) {
            editPhone.setEnabled(false);
            editName.setEnabled(false);
            ketqua.putExtra(Constant.BUNDLE_RESULT_UPDATE_PROFILE,"successful");
        } else {
            ketqua.putExtra(Constant.BUNDLE_RESULT_UPDATE_PROFILE,"fault_data");
        }
        setResult(999,ketqua);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                dialog.dismiss();
                finish();
            }
        }, 1000);

    }

    @Override
    public void onFailure(@NonNull Exception e) {

        Intent ketqua=new Intent();
        ketqua.putExtra(Constant.BUNDLE_RESULT_UPDATE_PROFILE,"fault_system");
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                dialog.dismiss();
                finish();
            }
        }, 1000);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Constant.RESULT_UPDATE_AVATAR) {
            // load lai hinh avatar
            String imagePath = data.getStringExtra("Image_Path");
            loadingPanel.setVisibility(View.VISIBLE);
            uploadImageToFireBase(imagePath);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    getDataFromFirebase();
                    loadingPanel.setVisibility(View.GONE);
                }
            }, 3000);

        }
    }

    private void uploadImageToFireBase(String imagePath) {
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReferenceFromUrl(Constant.URL_STORAGE);
        Uri file = Uri.fromFile(new File(imagePath));
        StorageReference riversRef = storageRef.child("avatar/" + myId + "_avatar");

        UploadTask uploadTask = riversRef.putFile(file);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                SupportUI.getInstance().customToast(MyProfileActivity.this,
                        R.layout.activity_my_profile, getResources().getString(R.string.profile_fragment_toast_error_upload));
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                Uri downloadUrl = taskSnapshot.getDownloadUrl();
                urlDownload = downloadUrl.toString();
                root.child(Constant.FB_KEY_USER_ROOT).child(myId)
                        .child(Constant.FB_KEY_USER_INFO).child(Constant.FB_KEY_USER_AVATAR)
                        .setValue(urlDownload);
            }
        });
    }

    private void getDataFromFirebase() {
        root = FirebaseDatabase.getInstance().getReference();
        root.child(Constant.FB_KEY_USER_ROOT).child(myId)
                .child(Constant.FB_KEY_USER_INFO)
                .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                myAccount = dataSnapshot.getValue(Account.class);
                editName.setText(myAccount.getUserName());
                editPhone.setText(myAccount.getUserPhone());
                Glide.with(MyProfileActivity.this)
                        .load(myAccount.getUserAvatar())
                        .override(250, 250).fitCenter()
                        .into(imgAvatarProfile);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                SupportUI.getInstance().customToast(MyProfileActivity.this,
                        R.layout.activity_my_profile,
                        getResources().getString(R.string.system_error));
                finish();
            }
        });


    }

    private void addControls() {
        int secondColor = getResources().getColor(R.color.blue2);
        int primaryColor = getResources().getColor(R.color.xam);
        SupportUI.getInstance().setupSwipeWindow(this, primaryColor, secondColor, SlidrPosition.LEFT);

        editName = (EditText) findViewById(R.id.editMyProfileName);
        editPhone = (EditText) findViewById(R.id.editMyProfilePhone);
        linkChangePass= (TextView) findViewById(R.id.linkChangePass);

        imgAvatarProfile = (ImageView) findViewById(R.id.imgAvatarProfile);
        imgEditName = (ImageView) findViewById(R.id.imgEditName);
        imgEditPhone = (ImageView) findViewById(R.id.imgEditPhone);

        btnUpdateProfile = (Button) findViewById(R.id.btnUpdateProfile);

        layoutAvatarProfile = (FrameLayout) findViewById(R.id.layoutAvatarProfile);
        loadingPanel = (RelativeLayout) findViewById(R.id.loadingPanel_profile);
        dialog=new Dialog(this,R.style.dialogStyle);
        myId = getCurrentAppId();

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
