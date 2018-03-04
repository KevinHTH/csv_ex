package uel.vteam.belovedhostel.view;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import uel.vteam.belovedhostel.R;
import uel.vteam.belovedhostel.adapter.MyMethods;
import uel.vteam.belovedhostel.adapter.SlidingImage_Adapter;

public class MainActivity extends AppCompatActivity {

    TextView txtEnterRegister,txtError,txtVietNamese,txtEnglish;
    Button btnEnterLogin;

    private Handler mHandler = new Handler();
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    DatabaseReference root;

    boolean doubleBackToExitPressedOnce = false;
    String preferenceName="LanguagesPreference";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth= FirebaseAuth.getInstance();
        addControls();

    }



    private void savePreference() {
        SharedPreferences preferences=getSharedPreferences(preferenceName,MODE_PRIVATE);
        SharedPreferences.Editor editor= preferences.edit();
        String language=getCurrentLanguagesApp();
        editor.putString("language",language);
        editor.commit();
    }

    private void restorePreference() {
        SharedPreferences preferences=getSharedPreferences(preferenceName,MODE_PRIVATE);
        String language=preferences.getString("language","");

            if (language.equalsIgnoreCase("English")) {
               // Toast.makeText(this, "chuyen sang tieng viet", Toast.LENGTH_SHORT).show();
                setLanguage("vi-rvn");
            } else {
                setLanguage("en");
               // Toast.makeText(this, "chueyn sang tieng anh", Toast.LENGTH_SHORT).show();
            }
    }
    private void addControls() {
        txtVietNamese= (TextView) findViewById(R.id.txtVietNamese);
        txtEnglish= (TextView) findViewById(R.id.txtEnglish);

        txtError= (TextView) findViewById(R.id.txtError);
        txtEnterRegister = (TextView) findViewById(R.id.btnEnterRegister);
        btnEnterLogin= (Button) findViewById(R.id.btnEnterLogin);
        txtError.setVisibility(View.INVISIBLE);


    }

    private void loginListen() {
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Intent i = new Intent(MainActivity.this, MenuActivity.class);
                    startActivity(i);
                }
            }
        };
        mAuth.addAuthStateListener(mAuthListener);

    }



    BroadcastReceiver internetReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            ConnectivityManager connectivityManager= (ConnectivityManager)
                    getSystemService(CONNECTIVITY_SERVICE);
            if (connectivityManager.getActiveNetworkInfo()==null){
                txtError.setVisibility(View.VISIBLE);
                txtError.setText(getResources().getString(R.string.error_internet));
            }else {
                    txtError.setVisibility(View.INVISIBLE);
                    loginListen();
                    addEvents();
            }

        }
    };


    @Override
    protected void onResume() {
        super.onResume();

        IntentFilter filterInternet=new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(internetReceiver,filterInternet);

    }

    @Override
    protected void onPause() {
        super.onPause();
        //savePreference();
        if (internetReceiver!=null){
            unregisterReceiver(internetReceiver);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mHandler != null) {
            mHandler.removeCallbacks(mRunnable);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (mAuthListener!=null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
    private void addEvents() {
        txtEnterRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MyMethods.getInstance().checkNetwork(getApplicationContext())){
                    Intent iRegister = new Intent(MainActivity.this, RegisterActivity.class);
                    startActivity(iRegister);
                    overridePendingTransition( R.anim.slide_in_up, R.anim.slide_out_up );
                }else {
                    txtError.setVisibility(View.VISIBLE);
                    txtError.setText(getResources().getString(R.string.error_internet));
                }

            }
        });

        btnEnterLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MyMethods.getInstance().checkNetwork(getApplicationContext())){
                    Intent iLogin = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(iLogin);
                    overridePendingTransition( R.anim.slide_in_up, R.anim.slide_out_up );
                }else {
                    txtError.setVisibility(View.VISIBLE);
                    txtError.setText(getResources().getString(R.string.error_internet));
                }
            }
        });

        txtVietNamese.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

             if (getCurrentLanguagesApp().equalsIgnoreCase("English")){
                 changeLanguages("vi-rvn");
             }

            }
        });

        txtEnglish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!getCurrentLanguagesApp().equalsIgnoreCase("English")){
                    changeLanguages("en");
                }
            }
        });
    }

    private String getCurrentLanguagesApp() {
        return  Locale.getDefault().getDisplayLanguage();
    }

    private void changeLanguages(String language) {

        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());
        Intent refresh = new Intent(this, MainActivity.class);
        startActivity(refresh);
        finish();
    }
public void setLanguage(String language){
    Locale locale = new Locale(language);
    Locale.setDefault(locale);
    Configuration config = new Configuration();
    config.locale = locale;
    getBaseContext().getResources().updateConfiguration(config,
            getBaseContext().getResources().getDisplayMetrics());
}

    // click back 2 times in order to exit
    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, getResources().getString(R.string.back2time), Toast.LENGTH_SHORT).show();
        mHandler.postDelayed(mRunnable, 2000);
    }
    private final Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            doubleBackToExitPressedOnce = false;
        }
    };


}
