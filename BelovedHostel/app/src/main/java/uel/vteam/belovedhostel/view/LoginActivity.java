package uel.vteam.belovedhostel.view;


import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.r0adkll.slidr.model.SlidrPosition;

import uel.vteam.belovedhostel.MyInterface.SupportUI;
import uel.vteam.belovedhostel.MyInterface.TextValidator;
import uel.vteam.belovedhostel.R;
import uel.vteam.belovedhostel.adapter.MyMethods;

public class LoginActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    String email, password;
    EditText editPassword, editEmail;
    Button btnLogin, btnShowPass, btnClearText1, btnClearText2;
    TextView txtNotify;

    Dialog customProgress;
    boolean isHidePass = true;

    CheckBox checkBoxSavePass;
    String loginPreferenceName="LoginPreference";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
        addControls();
        addEvents();

    }

    @Override
    protected void onPause() {
        super.onPause();
        // luu preference
        savePreference();
    }

    private void savePreference() {
        SharedPreferences preferences=getSharedPreferences(loginPreferenceName,MODE_PRIVATE);
        SharedPreferences.Editor editor= preferences.edit();
        String userName=editEmail.getText().toString();
        String password=editPassword.getText().toString();
        boolean isSave=checkBoxSavePass.isChecked();
        if (!isSave){
            editor.clear();
        }else {
            editor.putString("email",userName);
            editor.putString("password",password);
            editor.putBoolean("checked",isSave);
        }
        editor.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // khoi phuc preference
        restorePreference();
    }

    private void restorePreference() {
        SharedPreferences preferences=getSharedPreferences(loginPreferenceName,MODE_PRIVATE);
        boolean isSave= preferences.getBoolean("checked",false);
        if (isSave){
            String userName=preferences.getString("email","");
            String password=preferences.getString("password","");
            editEmail.setText(userName);
            editPassword.setText(password);
        }
        checkBoxSavePass.setChecked(isSave);
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btnClearText1:
                    editEmail.setText("");
                    btnClearText1.setVisibility(View.INVISIBLE);
                    break;
                case R.id.btnClearText2:
                    editPassword.setText("");
                    btnClearText2.setVisibility(View.INVISIBLE);
                    break;
                case R.id.btnShowPass:
                    switchShowHidePass();
                    break;
                case R.id.btnLogin:
                    if (MyMethods.getInstance().checkNetwork(getApplicationContext())){
                        txtNotify.setVisibility(View.INVISIBLE);
                        login();
                    }else {
                        txtNotify.setVisibility(View.VISIBLE);
                        txtNotify.setText(getResources().getString(R.string.error_no_network));
                    }


                    break;
            }
        }
    };

    private void login() {
        email = editEmail.getText().toString();
        password = editPassword.getText().toString();
        if (!TextValidator.getInstance(this).validateEmail(editEmail)) {
            return;
        } else if (!TextValidator.getInstance(this).validateText(editPassword)) {
            return;
        } else {
            doLoginFirebase();
        }
    }

    View.OnKeyListener onKeyListener=new View.OnKeyListener() {
        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            if (keyCode == KeyEvent.KEYCODE_ENTER) {
                login();
                return true;
            }
            return false;
        }
    };
    private void addEvents() {

        editEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                btnClearText1.setVisibility(View.VISIBLE);
                txtNotify.setVisibility(View.INVISIBLE);
            }
        });
        editPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                btnClearText2.setVisibility(View.VISIBLE);
                txtNotify.setVisibility(View.INVISIBLE);
            }
        });

        btnClearText1.setOnClickListener(onClickListener);
        btnClearText2.setOnClickListener(onClickListener);
        btnShowPass.setOnClickListener(onClickListener);
        btnLogin.setOnClickListener(onClickListener);
        editPassword.setOnKeyListener(onKeyListener);
    }

    private void switchShowHidePass() {
        if (isHidePass == false) {
            // an pass
            btnShowPass.setText(getResources().getString(R.string.button_showpass));
            editPassword.setTransformationMethod(new PasswordTransformationMethod());
            isHidePass = true;
        } else {
            // hien pass
            btnShowPass.setText(getResources().getString(R.string.button_hidepass));
            isHidePass = false;
            editPassword.setTransformationMethod(null);
        }
    }


    private void doLoginFirebase() {
        MyMethods.getInstance().displayCustomProgress(this,customProgress,
                getResources().getString(R.string.loading));
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this,
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (!task.isSuccessful()) {
                            MyMethods.getInstance().dismissCustomProgress(LoginActivity.this,customProgress,500);
                            txtNotify.setVisibility(View.VISIBLE);
                            txtNotify.setText(getResources().getString(R.string.login_msg_error_wrong));
                        } else {

                                    MyMethods.getInstance().dismissCustomProgress(LoginActivity.this,customProgress,500);
                                    goToMenu();
                        }

                    }
                });
    }

    private void goToMenu() {
        Intent i = new Intent(LoginActivity.this, MenuActivity.class);
        startActivity(i);
        finish();
    }

    private void addControls() {
        int secondColor = getResources().getColor(R.color.blue2);
        int primaryColor = getResources().getColor(R.color.xam);
        SupportUI.getInstance().setupSwipeWindow(this, primaryColor,
                secondColor, SlidrPosition.BOTTOM);
        editEmail = (EditText) findViewById(R.id.editEmailAddress);
        editPassword = (EditText) findViewById(R.id.editPassWord);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnShowPass = (Button) findViewById(R.id.btnShowPass);
        btnClearText1 = (Button) findViewById(R.id.btnClearText1);
        btnClearText2 = (Button) findViewById(R.id.btnClearText2);
        txtNotify = (TextView) findViewById(R.id.txtNotify);
        customProgress = new Dialog(this, R.style.dialogStyle);

        checkBoxSavePass= (CheckBox) findViewById(R.id.checkBoxSavePass);

    }


}

