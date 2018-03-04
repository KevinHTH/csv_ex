package uel.vteam.belovedhostel.view;

import android.app.Dialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.r0adkll.slidr.model.SlidrPosition;

import uel.vteam.belovedhostel.MyInterface.SupportUI;
import uel.vteam.belovedhostel.MyInterface.TextValidator;
import uel.vteam.belovedhostel.R;
import uel.vteam.belovedhostel.adapter.MyMethods;
import uel.vteam.belovedhostel.model.Constant;

import static uel.vteam.belovedhostel.R.id.btnChangePass;
import static uel.vteam.belovedhostel.R.id.btnClearNewPass;
import static uel.vteam.belovedhostel.R.id.btnClearNewPass2;
import static uel.vteam.belovedhostel.R.id.btnClearOldPass;
import static uel.vteam.belovedhostel.R.id.btnShowNewPass;
import static uel.vteam.belovedhostel.R.id.btnShowNewPass2;
import static uel.vteam.belovedhostel.R.id.btnShowOldPass;

public class ChangePasswordActivity extends AppCompatActivity {

    TextView txtNotifyError;
    EditText editOldPass,editNewPass,editNewPass2;
    Button btnClear1,btnClear2,btnClear3;
    Button btnChange,btnHide1,btnHide2,btnHide3;

    Dialog dialog;
    boolean isHide1 = true;
    boolean isHide2 = true;
    boolean isHide3 = true;

    String oldPass="";
    String newPass="";
    String newPassAgain="";
    String email="";

    String myId;
    DatabaseReference root;
    FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        addControls();
        addEvents();
    }
    View.OnClickListener onClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case btnClearOldPass:
                    editOldPass.setText("");
                    btnClear1.setVisibility(View.INVISIBLE);
                    txtNotifyError.setText("");
                    break;
                case btnClearNewPass:
                    editNewPass.setText("");
                    btnClear2.setVisibility(View.INVISIBLE);
                    txtNotifyError.setText("");
                    break;
                case btnClearNewPass2:
                    editNewPass2.setText("");
                    btnClear3.setVisibility(View.INVISIBLE);
                    txtNotifyError.setText("");
                    break;
                case btnShowOldPass:
                    if (isHide1==false){
                        btnHide1.setText(getResources().getString(R.string.changepass_activity_visible));
                        editOldPass.setTransformationMethod(new PasswordTransformationMethod());
                        isHide1 = true;
                    }else {
                        btnHide1.setText(getResources().getString(R.string.changepass_activity_invisible));
                        editOldPass.setTransformationMethod(null);
                        isHide1=false;
                    }
                    break;
                case btnShowNewPass:
                    if (isHide2==false){
                        btnHide2.setText(getResources().getString(R.string.changepass_activity_visible));
                        editNewPass.setTransformationMethod(new PasswordTransformationMethod());
                        isHide2 = true;
                    }else {
                        btnHide2.setText(getResources().getString(R.string.changepass_activity_invisible));
                        editNewPass.setTransformationMethod(null);
                        isHide2=false;
                    }
                    break;
                case btnShowNewPass2:
                    if (isHide3==false){
                        btnHide3.setText(getResources().getString(R.string.changepass_activity_visible));
                        editNewPass2.setTransformationMethod(new PasswordTransformationMethod());
                        isHide3 = true;
                    }else {
                        btnHide3.setText(getResources().getString(R.string.changepass_activity_invisible));
                        editNewPass2.setTransformationMethod(null);
                        isHide3=false;
                    }
                    break;

                case btnChangePass:
                    changePassword();
                    break;
            }
        }
    };



    private void changePassword() {

        if (!TextValidator.getInstance(this).checkLength(editOldPass,6) ) {
            return;
        }
        if (!TextValidator.getInstance(this).checkLength(editNewPass,6) ) {
            return;
        }
        if (!TextValidator.getInstance(this).checkLength(editNewPass2,6) ) {
            return;
        }
        else {
            MyMethods.getInstance().displayCustomProgress(ChangePasswordActivity.this,dialog,
                    getResources().getString(R.string.loading));
            oldPass= editOldPass.getText().toString().trim();
            root.child(Constant.FB_KEY_USER_ROOT).child(myId).child(Constant.FB_KEY_USER_INFO)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String pass= (String) dataSnapshot.child(Constant.FB_KEY_USER_PASSWORD).getValue();
                    email= (String) dataSnapshot.child(Constant.FB_KEY_USER_EMAIL).getValue();
                    if (!pass.equalsIgnoreCase(oldPass)){
                        MyMethods.getInstance().dismissCustomProgress(ChangePasswordActivity.this,dialog,100);
                        txtNotifyError.setText(getResources().getString(R.string.changepass_activity_error_not_correct));
                    }else {
                        newPass=editNewPass.getText().toString().trim();
                        newPassAgain=editNewPass2.getText().toString().trim();
                        if (!newPass.equalsIgnoreCase(newPassAgain)){
                            MyMethods.getInstance().dismissCustomProgress(ChangePasswordActivity.this,dialog,100);
                            txtNotifyError.setText(getResources().getString(R.string.changepass_activity_error_not_match));
                        }else {
                            updatePassword();
                        }
                    }


                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }

    }

    private void updatePassword() {
        user.updatePassword(newPass)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            root.child(Constant.FB_KEY_USER_ROOT).child(myId).child(Constant.FB_KEY_USER_INFO)
                                    .child(Constant.FB_KEY_USER_PASSWORD).setValue(newPass);
                            MyMethods.getInstance().dismissCustomProgress(ChangePasswordActivity.this,dialog,100);
                            SupportUI.getInstance().customToast(ChangePasswordActivity.this, R.layout.activity_change_password,
                                    getResources().getString(R.string.profile_fragment_toast_update_successful));
                            finish();
                        }else {
                            reLogin(email,oldPass);
                        }
                    }
                });

    }


    private void reLogin(String email,String oldPass) {
        AuthCredential credential = EmailAuthProvider
                .getCredential(email, oldPass);
        user.reauthenticate(credential)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                       updatePassword();
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
    private void addEvents() {
        btnClear1.setOnClickListener(onClickListener);
        btnClear2.setOnClickListener(onClickListener);
        btnClear3.setOnClickListener(onClickListener);
        btnHide1.setOnClickListener(onClickListener);
        btnHide2.setOnClickListener(onClickListener);
        btnHide3.setOnClickListener(onClickListener);
        btnChange.setOnClickListener(onClickListener);

        editOldPass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.toString().length()>0){
                    btnClear1.setVisibility(View.VISIBLE);
                }else {
                    btnClear1.setVisibility(View.GONE);
                }
                txtNotifyError.setText("");
            }
        });
        editNewPass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.toString().length()>0){
                    btnClear2.setVisibility(View.VISIBLE);
                }else {
                    btnClear2.setVisibility(View.GONE);
                }
                txtNotifyError.setText("");
            }
        });
        editNewPass2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.toString().length()>0){
                    btnClear3.setVisibility(View.VISIBLE);
                }else {
                    btnClear3.setVisibility(View.GONE);
                }
                txtNotifyError.setText("");
            }
        });
    }

    private void addControls() {
        int secondColor = getResources().getColor(R.color.blue2);
        int primaryColor = getResources().getColor(R.color.xam);
        SupportUI.getInstance().setupSwipeWindow(this, primaryColor, secondColor, SlidrPosition.LEFT);
            txtNotifyError= (TextView) findViewById(R.id.txtNotifyError);

            btnClear1= (Button) findViewById(R.id.btnClearOldPass);
            btnClear2= (Button) findViewById(R.id.btnClearNewPass);
            btnClear3= (Button) findViewById(R.id.btnClearNewPass2);

            btnHide1= (Button) findViewById(R.id.btnShowOldPass);
            btnHide2= (Button) findViewById(R.id.btnShowNewPass);
            btnHide3= (Button) findViewById(R.id.btnShowNewPass2);
            btnChange= (Button) findViewById(R.id.btnChangePass);

            editOldPass= (EditText) findViewById(R.id.editOldPass);
            editNewPass= (EditText) findViewById(R.id.editNewPass);
            editNewPass2= (EditText) findViewById(R.id.editNewPass2);
        dialog=new Dialog(this,R.style.dialogStyle);
        root = FirebaseDatabase.getInstance().getReference();
        user = FirebaseAuth.getInstance().getCurrentUser();
        myId=getCurrentAppId();

    }
}
