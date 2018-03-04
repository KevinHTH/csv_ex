package uel.vteam.belovedhostel.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import uel.vteam.belovedhostel.R;
import uel.vteam.belovedhostel.model.MyRoom;

/**
 * Created by Hieu on 11/22/2016.
 */

public class RoomBookingAdapter extends ArrayAdapter<MyRoom> {

    Context context=null;
    int layoutId;
    ArrayList<MyRoom> arrRoom=null;

    MyRoom row_selected=null;

    public RoomBookingAdapter(Context context, int resource, ArrayList<MyRoom> arr) {
        super(context, resource, arr);
        this.context=context;
        this.layoutId=resource;
        this.arrRoom=arr;
    }

    @NonNull
    @Override
    public View getView(int position, View view, ViewGroup parent) {

        LayoutInflater inflater= (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view=inflater.inflate(layoutId,null);
        TextView roomId= (TextView) view.findViewById(R.id.txtRoomId);
        TextView roomArea= (TextView) view.findViewById(R.id.txtArea);
        TextView roomBedRoom= (TextView) view.findViewById(R.id.txtBedroom);
        TextView roomDiscount= (TextView) view.findViewById(R.id.txtDiscount);
        TextView roomFurniture= (TextView) view.findViewById(R.id.txtFurniture);
        ImageView roomImage= (ImageView) view.findViewById(R.id.imgRoom);
        TextView roomPrice= (TextView) view.findViewById(R.id.txtPrice);
        TextView status= (TextView) view.findViewById(R.id.txtStatus);
        Button btnBook= (Button) view.findViewById(R.id.btnBook);
        btnBook.setVisibility(View.GONE);

        row_selected = arrRoom.get(position);
        Glide.with(context)
                .load(row_selected.getRoomImage().get(1).getImageLink()).override(450,400)
                .into(roomImage);
        roomId.setText(row_selected.getRoomId());
        roomArea.setText(row_selected.getRoomAcreage());
        roomBedRoom.setText(row_selected.getBedRoom());
        roomFurniture.setText(row_selected.getRoomFurniture());
        roomDiscount.setText(row_selected.getDiscount());
        roomPrice.setText(row_selected.getRoomPrice());
        status.setText(row_selected.getIsBooked());

        return view;
    }
}
