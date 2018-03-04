package uel.vteam.belovedhostel.view;


import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.timessquare.CalendarPickerView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import uel.vteam.belovedhostel.MyInterface.IDateTimeDialogData;
import uel.vteam.belovedhostel.R;
import uel.vteam.belovedhostel.adapter.MyMethods;

/**
 * A simple {@link Fragment} subclass.
 */


public class DateTimeDialogFragment extends DialogFragment {
    public IDateTimeDialogData iDateTimeDialogData;

    View v;
    private CalendarPickerView calendar;
    TextView txtDayCheckIn, txtMonthCheckIn, txtMdayCheckIn;
    TextView txtDayCheckOut, txtMonthCheckOut, txtMdayCheckOut;
    TextView label_checkin,label_checkout;
    Button btnSetDate;
    Date dateCheckin = null;
    Date dateCheckout = null;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_date_time_dialog, container, false);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getBundleData();
        addControls();
        setUpCalenderPicker(dateCheckin, dateCheckout);
        addEvents();
        return v;
    }

    private void getBundleData() {
        Bundle date = getArguments();
        dateCheckin = (Date) date.getSerializable(SearchFragment.DATE_CHECKIN);
        dateCheckout = (Date) date.getSerializable(SearchFragment.DATE_CHECKOUT);
    }

    private void setUpCalenderPicker(Date dateCheckIn, Date dateCheckOut) {

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

    public void setiDateTimeDialogData(IDateTimeDialogData iDateTimeDialogData) {
        this.iDateTimeDialogData = iDateTimeDialogData;
    }

    private void addControls() {
        txtDayCheckIn = (TextView) v.findViewById(R.id.label_day_checkin2);
        txtDayCheckOut = (TextView) v.findViewById(R.id.label_day_checkout2);
        txtMonthCheckIn = (TextView) v.findViewById(R.id.label_month_checkin2);
        txtMonthCheckOut = (TextView) v.findViewById(R.id.label_month_checkout2);
        txtMdayCheckIn = (TextView) v.findViewById(R.id.label_monday_checkin2);
        txtMdayCheckOut = (TextView) v.findViewById(R.id.label_monday_checkout2);
        label_checkin= (TextView) v.findViewById(R.id.label_checkin2);
        label_checkout= (TextView) v.findViewById(R.id.label_checkout2);
        btnSetDate = (Button) v.findViewById(R.id.btnSet);
        calendar = (CalendarPickerView) v.findViewById(R.id.calendar_view);

        Calendar nextYear = Calendar.getInstance();
        nextYear.add(Calendar.YEAR, 1);
        calendar.init(dateCheckin, nextYear.getTime())
                .withSelectedDate(dateCheckin).withSelectedDate(dateCheckout)
                .inMode(CalendarPickerView.SelectionMode.RANGE);
                /*.withHighlightedDate(today);*/
    }

    private void addEvents() {
        calendar.setOnDateSelectedListener(new CalendarPickerView.OnDateSelectedListener() {
            @Override
            public void onDateSelected(Date date) {
                ArrayList<Date> selectedDates = (ArrayList<Date>) calendar
                        .getSelectedDates();
                dateCheckin = selectedDates.get(0);
                dateCheckout = selectedDates.get(selectedDates.size() - 1);
                setUpCalenderPicker(dateCheckin, dateCheckout);
            }

            @Override
            public void onDateUnselected(Date date) {

            }
        });


        btnSetDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (dateCheckout.getDate() == dateCheckin.getDate()) {
                    Toast.makeText(getContext(),getResources().getString(R.string.search_fragment_error_datetime),
                            Toast.LENGTH_SHORT).show();
                } else {
                    iDateTimeDialogData.getDateTimeData(SearchFragment.ACTION_UPDATE_DATE_FROMFRAGMENT, dateCheckin, dateCheckout);
                    getDialog().dismiss();
                }
            }
        });
    }


}
