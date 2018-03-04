package uel.vteam.belovedhostel.adapter;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;


import java.util.ArrayList;

import uel.vteam.belovedhostel.MyInterface.SupportUI;
import uel.vteam.belovedhostel.R;
import uel.vteam.belovedhostel.model.MyRoom;
import uel.vteam.belovedhostel.view.BookingListFragment;

/**
 * Created by Hieu on 11/15/2016.
 */

public class ListRoomAdapter extends ArrayAdapter<MyRoom> implements Filterable {

    RelativeLayout loadingPanel;
    Context context=null;
    int layoutId;
    ArrayList<MyRoom> arrRoom=null;
    ArrayList<MyRoom> arrFilter=null;
    Bundle data=null; // ChooseRoom.DATE_BOOKING

    private ItemFilter itemFilter=new ItemFilter();


    MyRoom row_selected=new MyRoom();
    FragmentManager fragmentManager;



    public ListRoomAdapter(Context context, int resource, ArrayList<MyRoom> objects,Bundle data,FragmentManager fragmentManager) {
        super(context, resource, objects);
        this.context=context;
        this.layoutId=resource;
        this.arrRoom=objects;
        this.data=data;
        this.fragmentManager=fragmentManager;
        this.arrFilter=objects;
    }

    @Override
    public int getCount() {
        return arrFilter.size();
    }

    @Nullable
    @Override
    public MyRoom getItem(int position) {
        return arrFilter.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public View getView(final int position, View view, ViewGroup parent) {
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
        loadingPanel = (RelativeLayout)view. findViewById(R.id.loadingPanel_listRoom);

        Button book= (Button) view.findViewById(R.id.btnBook);
        loadingPanel.setVisibility(View.VISIBLE);
        row_selected = arrRoom.get(position);
        // set image picasso
        /*Picasso.with(context).load(row_selected.getRoomImage().
                get(1).getImageLink()).into(roomImage);*/
        // load anh bang glide
        String urlImage=row_selected.getRoomImage().get(1).getImageLink();


        Glide.with(context)
                .load(urlImage).fitCenter()
                .into(roomImage);

        roomId.setText(row_selected.getRoomId());
        roomArea.setText(row_selected.getRoomAcreage());
        roomBedRoom.setText(row_selected.getBedRoom());
        roomFurniture.setText(row_selected.getRoomFurniture());
        roomDiscount.setText(row_selected.getDiscount());
        roomPrice.setText(row_selected.getRoomPrice());

        String tinhtrang=row_selected.getIsBooked().toString();
        if (tinhtrang.equalsIgnoreCase("true")){
            tinhtrang=context.getResources().getString(R.string.itemroom_layout_value_status_true);
        }else {
            tinhtrang=context.getResources().getString(R.string.itemroom_layout_value_status_false);
        }
        status.setText(tinhtrang);





        book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                row_selected=arrRoom.get(position);
                String isBooked=row_selected.getIsBooked().toString();
                if (isBooked.equalsIgnoreCase("true")){
                    SupportUI.getInstance().customToast((Activity) context,layoutId,context.
                            getResources().getString(R.string.listroom_fragment_toast_occupied));
                }else {
                    addRoomToListBooking();
                }

            }
            private void addRoomToListBooking() {
                BookingListFragment fragBooking = new BookingListFragment();
                data.putSerializable("ROOM",row_selected);
                data.putString("control","book");
                fragBooking.setArguments(data);

                FragmentTransaction transaction=fragmentManager.beginTransaction();
                transaction.addToBackStack("STEP3");
                transaction.replace(R.id.fragment_container, fragBooking).commit();
            }
        });

        loadingPanel.setVisibility(View.GONE);
        return view;
    }

    private class ItemFilter extends Filter{

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results=new FilterResults();
            String filterString=constraint.toString().toLowerCase().trim();
            int price= Integer.parseInt(filterString);
            if (!TextUtils.isEmpty(filterString)){
                ArrayList<MyRoom> listResult=new ArrayList<>();
                for (MyRoom item: arrRoom){
                    int gia= Integer.parseInt(item.getRoomPrice());
                    if (item.getRoomPrice().equals(filterString)){
                        listResult.add(item);
                    }
                }
                results.values=listResult;
                results.count=listResult.size();
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            if (results!=null && results.count>0){

                arrFilter= (ArrayList<MyRoom>) results.values;
                notifyDataSetChanged();
            }else {

                arrFilter=arrRoom;
                notifyDataSetChanged();
            }
        }
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return itemFilter;
    }
}
