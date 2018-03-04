package uel.vteam.belovedhostel.adapter;

import android.app.Dialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.TextView;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import uel.vteam.belovedhostel.R;


/**
 * Created by Hieu on 11/4/2016.
 */

public class MyMethods {
    private static MyMethods instance;


    public MyMethods() {
    }
    public static MyMethods getInstance(){
        if (instance==null)
            instance=new MyMethods();
        return instance;
    }

    public boolean checkNetwork(Context context) {
        boolean isConnected = false;
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            isConnected = true;
        } else {
            isConnected = false;
        }
        return isConnected;
    }
    public Map<String,Integer> getCurrentDateTime() {
        Map<String, Integer> mapDate = new HashMap<>();
        Calendar today = Calendar.getInstance();
        int gio = today.get(Calendar.HOUR);
        int phut = today.get(Calendar.MINUTE);
        int ngay = today.get(Calendar.DAY_OF_MONTH);
        int thang = today.get(Calendar.MONTH);
        int nam = today.get(Calendar.YEAR);
        mapDate.put("gio", gio);
        mapDate.put("phut", phut);
        mapDate.put("ngay", ngay);
        mapDate.put("thang", thang);
        mapDate.put("nam", nam);

        return mapDate;
    }


    public String convertDay(int thuCheckin) {

        String thu="";
        switch (thuCheckin){
            case 0: thu="Sunday";
                break;
            case 1: thu="Monday";
                break;
            case 2: thu="Tuesday";
                break;
            case 3: thu="Wednesday";
                break;
            case 4: thu="Thursday";
                break;
            case 5: thu="Friday";
                break;
            case 6: thu="Saturday";
                break;
        }
        return thu;
    }
    public String convertMonth(int month) {

        String thang="";
        switch (month){
            case 1: thang="Junary";
                break;
            case 2: thang="February";
                break;
            case 3: thang="March";
                break;
            case 4: thang="April";
                break;
            case 5: thang="May";
                break;
            case 6: thang="June";
                break;
            case 7: thang="July";
                break;
            case 8: thang="August";
                break;
            case 9: thang="September";
                break;
            case 10: thang="October";
                break;
            case 11: thang="November";
                break;
            case 12: thang="December";
                break;
        }
        return thang;
    }
    public String chuyenDoiThu(int thuCheckin) {

        String thu="";
        switch (thuCheckin){
            case 0: thu="Chủ Nhật";
                break;
            case 1: thu="Thứ Hai";
                break;
            case 2: thu="Thứ Ba";
                break;
            case 3: thu="Thứ Tư";
                break;
            case 4: thu="Thứ Năm";
                break;
            case 5: thu="Thứ Sáu";
                break;
            case 6: thu="Thứ Bảy";
                break;
        }
        return thu;
    }


    public void displayCustomProgress(Context context,Dialog customProgress, String customMsg) {

        customProgress.setContentView(R.layout.custom_progress);
        TextView msg = (TextView) customProgress.findViewById(R.id.txtNotifyProgress);
        if (customMsg.length() > 0) {
            msg.setVisibility(View.VISIBLE);
            msg.setText(customMsg);
        } else {
            msg.setVisibility(View.GONE);
        }
        customProgress.setCancelable(true);
        customProgress.show();
    }



    //Dismissing custom progress dialog
    public void dismissCustomProgress(Context context, final Dialog customProgress, int seconds) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                customProgress.dismiss();
            }
        }, seconds);

    }

    public String XuatNgayThangNam(Date date)
    {
        String strDateFormat = "dd/MM/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(strDateFormat);
        return sdf.format(date).toString();
    }

    public void replaceFragment (Fragment fragment,int replacedFrame,FragmentManager manager){
        String backStateName = fragment.getClass().getName();
        String fragmentTag = backStateName;
        boolean fragmentPopped = manager.popBackStackImmediate (backStateName, 0);

        if (!fragmentPopped && manager.findFragmentByTag(fragmentTag) == null){ //fragment not in back stack, create it.
            FragmentTransaction ft = manager.beginTransaction();
            ft.replace(replacedFrame, fragment, fragmentTag);
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            ft.addToBackStack(backStateName);
            ft.commit();
        }
    }
}
