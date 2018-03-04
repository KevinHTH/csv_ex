package uel.vteam.belovedhostel.view;

import android.app.Dialog;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.r0adkll.slidr.model.SlidrPosition;


import java.util.ArrayList;

import uel.vteam.belovedhostel.MyInterface.SupportUI;
import uel.vteam.belovedhostel.MyInterface.TextValidator;
import uel.vteam.belovedhostel.R;
import uel.vteam.belovedhostel.adapter.LoadPhoneCodeTask;
import uel.vteam.belovedhostel.adapter.MyMethods;
import uel.vteam.belovedhostel.adapter.PhoneCodeAdapter;
import uel.vteam.belovedhostel.model.Constant;
import uel.vteam.belovedhostel.model.CountryPhoneCode;
import uel.vteam.belovedhostel.model.Account;

public class RegisterActivity extends AppCompatActivity {
    EditText editPhoneNumber, editPassWord, editName, editEmail;
    Button btnRegister, btnClearText1, btnClearText2, btnClearText3, btnShowPass;
    TextView txtNotify, txtUnderline1, txtUnderline2, txtUnderline3, txtUnderline4;

    Spinner spinnerPhoneCode = null;
    ArrayList<CountryPhoneCode> arrPhoneCode;
    PhoneCodeAdapter adapterPhoneCode;

    FirebaseAuth mAuth;
    DatabaseReference root;
    String email, password, phone, name, phoneCode;

    Dialog progress;
    boolean isHidePass = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth = FirebaseAuth.getInstance();
        root = FirebaseDatabase.getInstance().getReference();
        addControls();
        addEvents();
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btnClearText1:
                    editName.setText("");
                    editName.setFocusable(true);
                    break;
                case R.id.btnClearText2:
                    editEmail.setText("");
                    editEmail.setFocusable(true);
                    break;
                case R.id.btnClearText3:
                    editPassWord.setText("");
                    editPassWord.setFocusable(true);
                    break;

                case R.id.btnShowPass:
                    switchShowHidePass();
                    break;
                case R.id.btnRegister:
                    if (MyMethods.getInstance().checkNetwork(getApplicationContext())){
                        txtNotify.setVisibility(View.INVISIBLE);
                        register();
                    }else {
                        txtNotify.setVisibility(View.VISIBLE);
                        txtNotify.setText(getResources().getString(R.string.error_no_network));
                    }
                    break;
            }
        }
    };
    View.OnKeyListener onKeyListener=new View.OnKeyListener() {
        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            if (keyCode == KeyEvent.KEYCODE_ENTER) {
                  register();
                return true;
            }
            return false;
        }
    };
    private void switchShowHidePass() {
        if (isHidePass == false) {
            // an pass
            btnShowPass.setText(getResources().getString(R.string.button_showpass));
            editPassWord.setTransformationMethod(new PasswordTransformationMethod());
            isHidePass = true;
        } else {
            // hien pass
            btnShowPass.setText(getResources().getString(R.string.button_hidepass));
            isHidePass = false;
            editPassWord.setTransformationMethod(null);
        }
    }

    private void goToMenu() {
        Intent i = new Intent(RegisterActivity.this, MenuActivity.class);
        startActivity(i);
        finish();
    }

    private void addEvents() {
        btnClearText1.setOnClickListener(onClickListener);
        btnClearText2.setOnClickListener(onClickListener);
        btnClearText3.setOnClickListener(onClickListener);
        btnShowPass.setOnClickListener(onClickListener);
        btnRegister.setOnClickListener(onClickListener);

        editName.addTextChangedListener(new GenericTextWatcher(editName));
        editEmail.addTextChangedListener(new GenericTextWatcher(editEmail));
        editPassWord.addTextChangedListener(new GenericTextWatcher(editPassWord));
        editPassWord.setOnKeyListener(onKeyListener);

        spinnerPhoneCode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (arrPhoneCode!=null){
                    phoneCode = arrPhoneCode.get(i).getDial_code();
                }else {
                   fakeData();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

                if (arrPhoneCode!=null){
                    phoneCode = arrPhoneCode.get(0).getDial_code();
                }else {
                   fakeData();
                    phoneCode=arrPhoneCode.get(0).getDial_code();
                }

            }
        });
    }
        public void fakeData(){
            CountryPhoneCode vn=new CountryPhoneCode("Viet Nam","+84","VN");
            CountryPhoneCode lao=new CountryPhoneCode("Lao","+856","LA");
            CountryPhoneCode cpc=new CountryPhoneCode("Cambodia","+855","KH");

            arrPhoneCode.add(vn);
            arrPhoneCode.add(lao);
            arrPhoneCode.add(cpc);
        }
    private void register() {
        if (!TextValidator.getInstance(this).validatePhone(editPhoneNumber)) {
            return;
        }
        if (!TextValidator.getInstance(this).validateText(editName)) {
            return;
        }
        if (!TextValidator.getInstance(this).validateEmail(editEmail)) {
            return;
        }
        if (!TextValidator.getInstance(this).checkLength(editPassWord, 6)) {
            return;
        } else {
            email = editEmail.getText().toString();
            password = editPassWord.getText().toString();
            name = editName.getText().toString();
            phone = editPhoneNumber.getText().toString();
            doCreateUserFirebase(email, password);
        }
    }


    private void doCreateUserFirebase(String email, String password) {
        MyMethods.getInstance().displayCustomProgress(this, progress,
                getResources().getString(R.string.waiting));
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this,
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (!task.isSuccessful()) {
                            MyMethods.getInstance().dismissCustomProgress(RegisterActivity.this,progress,500);
                            txtNotify.setVisibility(View.VISIBLE);
                            txtNotify.setText(getResources().getString(R.string.register_error_has_used));
                            editEmail.setError(getResources().getString(R.string.register_error_has_used));
                        } else {
                            txtNotify.setVisibility(View.INVISIBLE);
                            String userId = task.getResult().getUser().getUid();
                            createCustomerInfo(userId);
                            MyMethods.getInstance().dismissCustomProgress(RegisterActivity.this, progress,500);
                            goToMenu();
                        }
                    }
                });
    }

    private void addControls() {
        int secondColor = getResources().getColor(R.color.blue2);
        int primaryColor = getResources().getColor(R.color.xam);
        SupportUI.getInstance().setupSwipeWindow(this, primaryColor, secondColor, SlidrPosition.BOTTOM);
        progress = new Dialog(this, R.style.dialogStyle);
        editPhoneNumber = (EditText) findViewById(R.id.editRPhone);
        editPassWord = (EditText) findViewById(R.id.editRPassword);
        editName = (EditText) findViewById(R.id.editRName);
        editEmail = (EditText) findViewById(R.id.editREmail);

        btnRegister = (Button) findViewById(R.id.btnRegister);
        txtNotify = (TextView) findViewById(R.id.txtRNotify);
        txtUnderline1 = (TextView) findViewById(R.id.txtUnderline1);
        txtUnderline2 = (TextView) findViewById(R.id.txtUnderline2);
        txtUnderline3 = (TextView) findViewById(R.id.txtUnderline3);
        txtUnderline4 = (TextView) findViewById(R.id.txtUnderline4);

        btnClearText1 = (Button) findViewById(R.id.btnClearText1);
        btnClearText2 = (Button) findViewById(R.id.btnClearText2);
        btnClearText3 = (Button) findViewById(R.id.btnClearText3);
        btnShowPass = (Button) findViewById(R.id.btnShowPass);

        // load data to spinner phone code
        spinnerPhoneCode = (Spinner) findViewById(R.id.spinnerPhoneCode);
        arrPhoneCode = new ArrayList<>();
        adapterPhoneCode = new PhoneCodeAdapter(RegisterActivity.this,
                R.layout.item_custom_spinnerphonecode,
                arrPhoneCode);
        spinnerPhoneCode.setAdapter(adapterPhoneCode);
        adapterPhoneCode.setDropDownViewResource(android.R.layout.simple_spinner_item);
        String link = Constant.URL_PHONECODE;
        LoadPhoneCodeTask task = new LoadPhoneCodeTask(this, arrPhoneCode, adapterPhoneCode);
        task.execute(link);

    }

    private void createCustomerInfo(String userId) {
        DatabaseReference customerRoot = root.child("USER").child(userId);
        Account customerInfo = new Account();
        customerInfo.setUserId(userId);
        customerInfo.setUserName(name);
        customerInfo.setUserPhone(phone);
        customerInfo.setUserEmail(email);
        customerInfo.setUserPassword(password);
        customerInfo.setCountryPhoneCode(phoneCode);
        customerInfo.setPermission("user");
        customerInfo.setStatus("true");
        customerInfo.setUserAvatar(Constant.URL_DEFAULT_AVATAR);
        customerInfo.setIdentityNumber("");
        customerInfo.setMasterCardId("");
        customerInfo.setVisaId("");
        customerInfo.setPassportId("");

        customerRoot.child(Constant.FB_KEY_USER_INFO).setValue(customerInfo);
    }




    private class GenericTextWatcher implements TextWatcher {

        private View view;

        private GenericTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {

            String text = editable.toString();
            switch (view.getId()) {
                case R.id.editRName:
                    if (!editName.getEditableText().toString().equals("")){
                        btnClearText1.setVisibility(View.VISIBLE);
                        editName.setError(null);
                        txtNotify.setVisibility(View.INVISIBLE);
                    }else {
                        btnClearText1.setVisibility(View.INVISIBLE);
                        editName.setError(null);
                        txtNotify.setVisibility(View.INVISIBLE);
                    }
                    break;
                case R.id.editREmail:
                    if (!editEmail.getEditableText().toString().equals("")){
                        editEmail.setError(null);
                        btnClearText2.setVisibility(View.VISIBLE);
                        txtNotify.setVisibility(View.INVISIBLE);
                    }else {
                        editEmail.setError(null);
                        btnClearText2.setVisibility(View.INVISIBLE);
                        txtNotify.setVisibility(View.INVISIBLE);
                    }

                    break;
                case R.id.editRPassword:
                    if (!editPassWord.getEditableText().toString().equals("")){
                        editPassWord.setError(null);
                        btnClearText3.setVisibility(View.VISIBLE);
                        txtNotify.setVisibility(View.INVISIBLE);
                    }else {
                        editPassWord.setError(null);
                        btnClearText3.setVisibility(View.INVISIBLE);
                        txtNotify.setVisibility(View.INVISIBLE);
                    }

                    break;
            }
        }
    }
}
