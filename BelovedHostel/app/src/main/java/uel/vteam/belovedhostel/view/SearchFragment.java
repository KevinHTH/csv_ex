package uel.vteam.belovedhostel.view;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;

import uel.vteam.belovedhostel.MyInterface.IDateTimeDialogData;
import uel.vteam.belovedhostel.R;
import uel.vteam.belovedhostel.adapter.MyMethods;


public class SearchFragment extends Fragment implements IDateTimeDialogData {

    final static int ACTION_UPDATE_DATE_FROMFRAGMENT = 123;
    final static String DATE_CHECKIN="DATE_CHECKIN";
    final static String DATE_CHECKOUT="DATE_CHECKOUT";
    TextView txtDayCheckIn,txtMonthCheckIn,txtMdayCheckIn;
    TextView txtDayCheckOut,txtMonthCheckOut,txtMdayCheckOut;
    TextView label_checkin,label_checkout;
    Button btnContinue;

    LinearLayout layoutDateTime;
    Date dateCheckIn,dateCheckOut;
    DateTimeDialogFragment dateDialog;
    FragmentManager manager;

    DatabaseReference root;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_search, container,false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(
                getResources().getString(R.string.search_fragment_title_actionbar));
        manager =getActivity().getSupportFragmentManager();
        root= FirebaseDatabase.getInstance().getReference();


        addControls(v);
        setCurrentDate();
        addEvents();
        return v;
    }

    private void addEvents() {
            layoutDateTime.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dateDialog = new DateTimeDialogFragment();
                    Bundle data=new Bundle();
                    data.putSerializable(DATE_CHECKIN,dateCheckIn);
                    data.putSerializable(DATE_CHECKOUT,dateCheckOut);
                    dateDialog.setArguments(data);
                    dateDialog.show(manager, "Chon Ngay");
                    dateDialog.setiDateTimeDialogData(SearchFragment.this);
                }
            });


        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               Fragment fragmentChooseRoom=new ChooseRoomFragment();
                Bundle data=new Bundle();
                data.putSerializable(DATE_CHECKIN,dateCheckIn);
                data.putSerializable(DATE_CHECKOUT,dateCheckOut);
                fragmentChooseRoom.setArguments(data);
                MyMethods.getInstance().replaceFragment(fragmentChooseRoom,R.id.fragment_container,manager);
            }
        });
    }


    private void addControls(View v) {
        layoutDateTime= (LinearLayout) v.findViewById(R.id.layoutDateTime);
        txtDayCheckIn= (TextView) v.findViewById(R.id.label_day_checkin);
        txtDayCheckOut= (TextView) v.findViewById(R.id.label_day_checkout);
        txtMonthCheckIn= (TextView) v.findViewById(R.id.label_month_checkin);
        txtMonthCheckOut= (TextView) v.findViewById(R.id.label_month_checkout);
        txtMdayCheckIn= (TextView) v.findViewById(R.id.label_monday_checkin);
        txtMdayCheckOut= (TextView) v.findViewById(R.id.label_monday_checkout);
        btnContinue = (Button) v.findViewById(R.id.btnContinueToChooseRoom);

        label_checkin= (TextView) v.findViewById(R.id.label_checkin);
        label_checkout= (TextView) v.findViewById(R.id.label_checkout);

        label_checkin.setText(getResources().getString(R.string.search_fragment_label_checkin));
        label_checkout.setText(getResources().getString(R.string.search_fragment_label_checkout));

    }


    private void setCurrentDate() {
        Date today = new Date();
        // lay ngay hom sau
        int nday=today.getDate()+1;
        int month=today.getMonth();
        int year=today.getYear();
        Date nextDay=new Date(year,month,nday);
        dateCheckIn=today;
        dateCheckOut=nextDay;
        setUpDateTime(dateCheckIn,dateCheckOut);
    }

    @Override
    public void getDateTimeData(int code, Date checkIn, Date checkOut) {
        if (code==ACTION_UPDATE_DATE_FROMFRAGMENT){
            dateCheckIn = checkIn;
            dateCheckOut = checkOut;
            setUpDateTime(dateCheckIn,dateCheckOut);
        }
    }

    private void setUpDateTime(Date dateCheckIn, Date dateCheckOut) {

        if (label_checkin.getText().equals("Check in")){
            // set ngay tieng anh
            txtMdayCheckIn.setText(MyMethods.getInstance().convertDay(dateCheckIn.getDay()) + "");
            txtDayCheckIn.setText(" " + dateCheckIn.getDate());
            txtMonthCheckIn.setText(" " + MyMethods.getInstance().convertMonth(dateCheckIn.getMonth()+1));

            txtMdayCheckOut.setText(MyMethods.getInstance().convertDay(dateCheckOut.getDay()) + "");
            txtDayCheckOut.setText(" " + dateCheckOut.getDate());
            txtMonthCheckOut.setText(" " + MyMethods.getInstance().convertMonth(dateCheckOut.getMonth()+1));
        }else {
            txtMdayCheckIn.setText(MyMethods.getInstance().chuyenDoiThu(dateCheckIn.getDay()) + "");
            txtDayCheckIn.setText("Ngày " + dateCheckIn.getDate());
            txtMonthCheckIn.setText("Tháng " +(dateCheckIn.getMonth()+1));

            txtMdayCheckOut.setText(MyMethods.getInstance().chuyenDoiThu(dateCheckOut.getDay()) + "");
            txtDayCheckOut.setText("Ngày " + dateCheckOut.getDate());
            txtMonthCheckOut.setText("Tháng " +(dateCheckOut.getMonth()+1));
        }


    }
}
