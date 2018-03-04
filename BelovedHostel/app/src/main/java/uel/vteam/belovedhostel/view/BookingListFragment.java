package uel.vteam.belovedhostel.view;


import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import uel.vteam.belovedhostel.MyInterface.SupportUI;
import uel.vteam.belovedhostel.MyInterface.TextValidator;
import uel.vteam.belovedhostel.R;
import uel.vteam.belovedhostel.adapter.MyMethods;
import uel.vteam.belovedhostel.model.Bills;
import uel.vteam.belovedhostel.model.Constant;
import uel.vteam.belovedhostel.model.MyRoom;
import uel.vteam.belovedhostel.model.Voucher;


public class BookingListFragment extends Fragment {

    static ArrayList<MyRoom> listBooking = new ArrayList<>();
    static List<String> roomId = new ArrayList<>();
    static String identity = "";
    static String voucher = "";

    Date dateCheckIn, dateCheckOut;
    String strUserId, strUserPhone, strDateBooking, strCheckIn, strCheckOut;

    TextView txtDateBooking, txtCheckOut, txtCheckIn, txtIdRoom, txtTotal;
    TextView lbDateBooking, lbCheckout, lbCheckin, lbIdRoom, lbTotal, lbPeople, lbChild, lbAdult;
    EditText edUserPhone, edIdentity, edDiscount;
    Button btnAddMore, btnDetailBill, btnFinish, btnCheck;
    CheckBox cbDiscount;
    Spinner spAdult, spChild;

    String arrAdult[] = {"1","2","3","4","5","6","7","8","9","10"};
    String arrChild[] = {"0","1","2","3","4","5","6","7","8","9","10"};
    ArrayAdapter<String> adultAdapter;
    ArrayAdapter<String> childAdapter;

    FragmentManager fragmentManager = null;
    BroadcastReceiver updateReceiver = null;
    DatabaseReference root;
    DatabaseReference userRoot;
    Bills bill;
    MyRoom roomSelected = new MyRoom();
    Bundle bundle;
    View v;

    Dialog progress;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
         v = inflater.inflate(R.layout.fragment_booking, container, false);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(
                getResources().getString(R.string.booking_fragment_title_actionbar));
        fragmentManager = getActivity().getSupportFragmentManager();
        root = FirebaseDatabase.getInstance().getReference();
        bundle = getArguments();
        String control = bundle.getString(Constant.BUNDLE_ACTION_CONTROL);
        addControls(v);
        listenUpdateFromDialog();
        getDateBooking();
        getUserInfo();

        if (control.equalsIgnoreCase(Constant.BUNDLE_ACTION_CONTROL_CONTINUE)) {
            if (listBooking.size() == 0) {
                SupportUI.getInstance().customToast(getActivity(),
                        R.layout.fragment_booking,getResources().
                                getString(R.string.booking_fragment_toast_null_listroom));
                fragmentManager.popBackStack();
            } else {
                updateUI();
                addEvents();
            }
        } else {

            getRoomBooking();
            updateUI();
            addEvents();
        }

        return v;
    }



    private void getUserInfo() {
        MenuActivity menuActivity = (MenuActivity) getActivity();
        Bundle userData = menuActivity.getData();
        strUserId = userData.getString(Constant.BUNDLE_USER_ID);
        strUserPhone = userData.getString(Constant.BUNDLE_USER_PHONE);
    }

    private void getDateBooking() {
        dateCheckIn = (Date) bundle.getSerializable(SearchFragment.DATE_CHECKIN);
        dateCheckOut = (Date) bundle.getSerializable(SearchFragment.DATE_CHECKOUT);
        strCheckIn = MyMethods.getInstance().XuatNgayThangNam(dateCheckIn);
        strCheckOut = MyMethods.getInstance().XuatNgayThangNam(dateCheckOut);
    }

    private void getRoomBooking() {
        roomSelected = (MyRoom) bundle.getSerializable(Constant.BUNDLE_ROOM);
        boolean isAlready=false;
        if (listBooking.size() > 0) {
            for (int i = 0; i < listBooking.size(); i++) {
                String roomId = listBooking.get(i).getRoomId().toString();
                // khi có trùng thì ket thuc for, chưa có thi tìm cho co
                if (roomSelected.getRoomId().equalsIgnoreCase(roomId) == true) {
                    isAlready=true;
                    break;
                }
            }

            if (isAlready==true){
                SupportUI.getInstance().customToast(getActivity(),
                        R.layout.fragment_booking,
                        getResources().getString(R.string.booking_fragment_toast_already));
            }else {
                listBooking.add(roomSelected);
                BookingListFragment.roomId.add(roomSelected.getRoomId());
            }
        } else {
            listBooking.add(roomSelected);
            roomId.add(roomSelected.getRoomId());
        }

    }


    private void addEvents() {

        btnAddMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentManager.popBackStack();
            }
        });


        btnDetailBill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DetailRoomBookingFragment detailRoomDialog = new DetailRoomBookingFragment();
                Bundle dataBooking = new Bundle();
                dataBooking.putSerializable(Constant.BUNDLE_LIST_BOOKING, listBooking);
                detailRoomDialog.setArguments(dataBooking);
                detailRoomDialog.show(fragmentManager, "Detail Booking");
            }
        });
        edDiscount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                voucher=edDiscount.getText().toString();
            }
        });

        cbDiscount.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (cbDiscount.isChecked()) {
                    btnCheck.setEnabled(true);
                    edDiscount.setEnabled(true);
                    edDiscount.setText(voucher);
                } else {
                    btnCheck.setEnabled(false);
                    edDiscount.setError(null);
                    edDiscount.setEnabled(false);
                    edDiscount.setText("");
                    voucher = null;
                }
            }
        });
        edIdentity.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                identity = edIdentity.getText().toString();
            }
        });
        btnCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 long sum = totalAmount();
                String discountId=edDiscount.getText().toString().trim();
                checkDiscountId(sum,discountId);
            }
        });

        btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // kiem tra list room !=null
                if (listBooking.size() == 0) {
                    SupportUI.getInstance().customToast(getActivity(),
                            R.layout.fragment_booking,getResources().getString(R.string.booking_fragment_toast_null_listroom));
                } else {
                    if (!TextValidator.getInstance(getContext()).validatePhone(edUserPhone)) {
                        return;
                    } else if (!TextValidator.getInstance(getContext()).validateNumber(edIdentity)){
                        return;
                    } else if (!TextValidator.getInstance(getContext()).checkLength(edIdentity,9)){
                        return;
                    }
                    else if (cbDiscount.isChecked()) {
                        if (!TextValidator.getInstance(getContext()).validateNumber(edDiscount)){
                            return;
                        }else {
                            long sum = totalAmount();
                            String discountId=edDiscount.getText().toString().trim();
                            checkDiscountId(sum,discountId);
                            if (edDiscount.getError()==null){
                                booking();
                            }
                        }
                    } else {
                        booking();
                    }
                }
            }
        });
    }

    private void checkDiscountId(final long sum, final String discountId) {
        root.child(Constant.FB_KEY_MANAGEMENT_ROOT).child(Constant.FB_KEY_ROOT_MANAGEMENT_VOUCHER)
                .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                double percent;
                int tong = 0;
                boolean match = false;
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    Voucher v = data.getValue(Voucher.class);
                    String id = v.getIdVoucher();
                    if (discountId.equalsIgnoreCase(id) == true) {
                        percent = Double.parseDouble(v.getPercent().toString());
                        tong = (int) (sum - (percent * sum));
                        match = true;
                    }
                }
                if (match == true) {
                    Snackbar.make(v,getResources().getString(R.string.booking_fragment_toast_valid_voucher),5000).show();
                    txtTotal.setText(tong+"");
                }else {
                    edDiscount.setError(getResources().getString(R.string.booking_fragment_toast_invalid_voucher));
                    Snackbar.make(v,getResources().getString(R.string.booking_fragment_toast_invalid_voucher),5000).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Snackbar.make(v,getResources().getString(R.string.system_error),
                        4000).show();
            }
        });
    }

    private void booking() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(getResources().getString(R.string.booking_fragment_label_confirm));
        builder.setMessage(getResources().getString(R.string.booking_fragment_toast_confirm));
        builder.setPositiveButton((getResources().getString(R.string.booking_fragment_confirm_yes))
                , new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                doBookingRoom();
            }
        });
        builder.setNegativeButton((getResources().getString(R.string.booking_fragment_confirm_no))
                , new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });

        builder.create().show();
    }

    private void doBookingRoom() {
        boolean isCheckout = false;
        boolean isValid = false;
        String language= Locale.getDefault().getDisplayLanguage();


        String people="";
        if (language.equalsIgnoreCase("English")){
            people = spAdult.getSelectedItem().toString()
                    + Constant.KEY_ADULT+" - " + spChild.getSelectedItem().toString()+" "+Constant.KEY_CHILDREN;
        }else {
            people = spAdult.getSelectedItem().toString()
                    +Constant.KEY_NGUOILON +" - " + spChild.getSelectedItem().toString()+" "+ Constant.KEY_TREEM;
        }

        bill = new Bills();
        bill.setCustomerId(strUserId);
        bill.setIsCheckOut(isCheckout);
        bill.setIsValid(isValid);
        bill.setAmountPeople(people);
        bill.setListRoom(roomId);
        bill.setCheckIn(strCheckIn);
        bill.setCheckOut(strCheckOut);
        bill.setDateCreate(strDateBooking);
        bill.setContact(edUserPhone.getText().toString());
        bill.setVoucherNumber(edDiscount.getText().toString());
        bill.setTotalPrice(txtTotal.getText().toString());
        pushDataToFirebase();
    }

    private void pushDataToFirebase() {

        MyMethods.getInstance().displayCustomProgress(getContext(),progress, getResources().getString(R.string.waiting));
        userRoot = root.child(Constant.FB_KEY_USER_ROOT).child(strUserId).child(Constant.FB_KEY_USER_BILL).child(Constant.FB_KEY_USER_ROOM);
        root.child(Constant.FB_KEY_USER_ROOT).child(strUserId).child(Constant.FB_KEY_USER_INFO).child(Constant.FB_KEY_USER_IDENTITY_NUMBER).setValue(identity);
        String pushKey = userRoot.push().getKey();
        bill.setBillId(pushKey);
        userRoot.child(pushKey).setValue(bill).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()){

                    identity = "";
                    voucher = "";
                    roomId.clear();
                    listBooking.clear();
                    // back to menu
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            progress.dismiss();
                            Snackbar.make(v,getResources().getString(R.string.booking_fragment_result_successful),
                                    Snackbar.LENGTH_LONG).setDuration(10000).show();
                            int count = fragmentManager.getBackStackEntryCount();
                            for (int i = 0; i < count - 1; ++i) {
                                fragmentManager.popBackStackImmediate();
                            }
                        }
                    }, 2000);

                }else {
                    MyMethods.getInstance().dismissCustomProgress(getContext(),progress, 1000);
                    Toast.makeText(getContext(),(getResources()
                            .getString(R.string.booking_fragment_result_fault)), Toast.LENGTH_LONG).show();
                }
            }
        });

    }


    private long[] tinhNgay(Date date1, Date date2) {
        long[] result=new long[5];
        Calendar cal=Calendar.getInstance();
        cal.setTimeZone(TimeZone.getTimeZone("UTC"));
        cal.setTime(date1);

        long t1=cal.getTimeInMillis();
        cal.setTime(date2);

        long diff= Math.abs(cal.getTimeInMillis()-t1);
        final int ONE_DAY = 1000 * 60 * 60 * 24;

        long d = diff / ONE_DAY;
        result[0] = d;
        return result;
    }


    private long totalAmount() {
        long [] diff=tinhNgay(dateCheckIn,dateCheckOut);
        long songay=diff[0];
        if (songay==0){
            songay=1;
        }

        MyRoom myroom = new MyRoom();
        long sum = 0;
        if (listBooking.size() != 0) {
            for (int i = 0; i < listBooking.size(); i++) {
                myroom = listBooking.get(i);
                int gia = Integer.parseInt(myroom.getRoomPrice());
                sum += gia * songay;
            }
        } else {
            sum = 0;
        }
        return sum;
    }


    private void updateUI() {
        edUserPhone.setText(strUserPhone);
        edIdentity.setText(identity);

        Date today = new Date();
        strDateBooking = MyMethods.getInstance().XuatNgayThangNam(today);
        txtDateBooking.setText(strDateBooking);
        txtCheckIn.setText(strCheckIn);
        txtCheckOut.setText(strCheckOut);

        txtTotal.setText(totalAmount()+"");

        // set room id
        String strMaPhong = "";
        for (int i = 0; i < roomId.size(); i++) {
            if (i >= 1) {
                strMaPhong += "-";
            }
            strMaPhong += roomId.get(i);
        }
        txtIdRoom.setText(strMaPhong);


        // if list room diff null -> set enable button
        if (roomId.size()>=1){
            btnDetailBill.setEnabled(true);
            btnFinish.setEnabled(true);
        }

        cbDiscount.setChecked(false);
        btnCheck.setEnabled(false);
        if (voucher=="") {
            edDiscount.setEnabled(false);
            cbDiscount.setChecked(false);
        } else {
            btnCheck.setEnabled(true);
            cbDiscount.setChecked(true);
            edDiscount.setEnabled(true);
            edDiscount.setText(voucher);
            edDiscount.setError(getResources().getString(R.string.booking_fragment_error_voucher_again));
        }

        adultAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, arrAdult);
        adultAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spAdult.setAdapter(adultAdapter);

        childAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, arrChild);
        childAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spChild.setAdapter(childAdapter);

    }

    private void addControls(View v) {
        txtDateBooking = (TextView) v.findViewById(R.id.txtDateBooking);
        txtCheckIn = (TextView) v.findViewById(R.id.txtCheckIn);
        txtCheckOut = (TextView) v.findViewById(R.id.txtCheckOut);
        txtIdRoom = (TextView) v.findViewById(R.id.txtIdRoom);
        txtTotal = (TextView) v.findViewById(R.id.txtTotal);

        edUserPhone = (EditText) v.findViewById(R.id.editUPhone);
        edDiscount = (EditText) v.findViewById(R.id.editDiscount);
        edIdentity = (EditText) v.findViewById(R.id.editCMND);

        btnAddMore = (Button) v.findViewById(R.id.btnAddMore);
        btnDetailBill = (Button) v.findViewById(R.id.btnDetailListRoom);
        btnFinish = (Button) v.findViewById(R.id.btnFinish);
        cbDiscount = (CheckBox) v.findViewById(R.id.checkBoxDiscount);
        btnCheck = (Button) v.findViewById(R.id.btnCheckVoucher);

        spAdult = (Spinner) v.findViewById(R.id.spinnerAdult);
        spChild = (Spinner) v.findViewById(R.id.spinnerChildrent);


        lbDateBooking = (TextView) v.findViewById(R.id.label_datebook);
        lbCheckout = (TextView) v.findViewById(R.id.label_checkout_date);
        lbCheckin = (TextView) v.findViewById(R.id.label_checkin_date);
        lbIdRoom = (TextView) v.findViewById(R.id.label_room);
        lbTotal = (TextView) v.findViewById(R.id.label_total_price);
        lbPeople = (TextView) v.findViewById(R.id.label_people);
        lbChild = (TextView) v.findViewById(R.id.label_child);
        lbAdult = (TextView) v.findViewById(R.id.label_adult);
        progress=new Dialog(getContext(),R.style.dialogStyle);
    }

    private void listenUpdateFromDialog() {

        updateReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String actionFrom = intent.getStringExtra(Constant.BUNDLE_ACTION_UPDATE_LIST);
                if (actionFrom.equalsIgnoreCase(Constant.BUNDLE_UPDATE_VALUE)) {
                    listBooking = (ArrayList<MyRoom>) intent.getSerializableExtra(Constant.BUNDLE_LIST_BOOKING);
                    roomId.clear();
                    String strMaPhong = "";
                    if (listBooking.size() != 0) {
                        for (int i = 0; i < listBooking.size(); i++) {
                            roomId.add(listBooking.get(i).getRoomId());
                            if (i >= 1) {
                                strMaPhong += "-";
                            }
                            strMaPhong += listBooking.get(i).getRoomId();
                        }
                        txtIdRoom.setText(strMaPhong);
                        txtTotal.setText(totalAmount()+"");
                    } else {
                        btnDetailBill.setEnabled(false);
                        btnFinish.setEnabled(false);
                        txtIdRoom.setText(strMaPhong);
                        txtTotal.setText(totalAmount()+"");
                        SupportUI.getInstance().customToast(getActivity(),
                                R.layout.fragment_booking,getResources().getString(R.string.booking_fragment_toast_null_listroom));
                    }

                }

            }
        };
        getContext().registerReceiver(updateReceiver, new IntentFilter(Constant.UPDATE_FROM_LISTBOOKING_DIALOG));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getContext().unregisterReceiver(updateReceiver);
    }


}
