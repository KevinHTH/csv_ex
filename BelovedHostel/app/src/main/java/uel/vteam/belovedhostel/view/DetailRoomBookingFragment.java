package uel.vteam.belovedhostel.view;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import uel.vteam.belovedhostel.R;
import uel.vteam.belovedhostel.adapter.RoomBookingAdapter;
import uel.vteam.belovedhostel.model.Constant;
import uel.vteam.belovedhostel.model.MyRoom;


public class DetailRoomBookingFragment extends DialogFragment {


    ListView lvListRoomBooking;
    ArrayList<MyRoom> arrRoomBooking;
    List<String> maPhong=new ArrayList<>();
    RoomBookingAdapter adapter;
    Button btnClose;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v=inflater.inflate(R.layout.fragment_detail_room_booking, container, false);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        Bundle data =this.getArguments();
        arrRoomBooking= (ArrayList<MyRoom>) data.getSerializable(Constant.BUNDLE_LIST_BOOKING);


        lvListRoomBooking= (ListView) v.findViewById(R.id.lvListRoomBooking);
        adapter=new RoomBookingAdapter(getContext(),R.layout.item_list_room,arrRoomBooking);
        lvListRoomBooking.setAdapter(adapter);
        btnClose= (Button) v.findViewById(R.id.btnCloseDetailBookingList);
        addEvents();

        return v;
    }

    private void addEvents() {
        lvListRoomBooking.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(final AdapterView<?> adapterView, View view, final int position, long l) {

                AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
                builder.setMessage(getResources().getString(R.string.detail_room_ask_delete));
                builder.setPositiveButton(getResources().getString(R.string.detail_room_yes), new DialogInterface. OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        // xoa phong va cap nhat lai danh sach phong da dat
                        arrRoomBooking.remove(position);
                        adapter.notifyDataSetChanged();
                        Intent i = new Intent(Constant.UPDATE_FROM_LISTBOOKING_DIALOG);
                        i.putExtra(Constant.BUNDLE_ACTION_UPDATE_LIST, Constant.BUNDLE_UPDATE_VALUE);
                        i.putExtra(Constant.BUNDLE_LIST_BOOKING,arrRoomBooking);
                        getContext().sendBroadcast(i);
                        if (arrRoomBooking.size()==0){
                            getDialog().dismiss();
                        }
                    }});
                builder.setNegativeButton(getResources().getString(R.string.detail_room_no), new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        dialog.cancel();
                    }
                });
                builder.create().show();
                return false;
            }
        });

        // close event
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDialog().dismiss();
            }
        });
    }


}
