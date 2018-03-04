package uel.vteam.belovedhostel.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import uel.vteam.belovedhostel.R;
import uel.vteam.belovedhostel.model.Bills;

/**
 * Created by Hieu on 12/19/2016.
 */

public class MyHistoryAdapter extends BaseExpandableListAdapter {

    Context context;
    ArrayList<Bills> arrDataHeader = new ArrayList<>();

    ViewHolder holder;

    public MyHistoryAdapter(Context context, ArrayList<Bills> arrDataHeader) {
        this.context = context;
        this.arrDataHeader = arrDataHeader;
    }

    static class ViewHolder {
        TextView txtDateCreateBill, txtStatusBill, txtCheckIn,
                txtCheckOut, txtIdRoom, txtPeople, txtPhone, txtTotal;

        public ViewHolder(View convertView ) {
            txtDateCreateBill = (TextView) convertView.findViewById(R.id.txtDateCreateBill);
            txtStatusBill = (TextView) convertView.findViewById(R.id.txtStatusBill);
            txtCheckIn = (TextView) convertView.findViewById(R.id.txtCheckInHistory);
            txtCheckOut = (TextView) convertView.findViewById(R.id.txtCheckOutHistory);
            txtIdRoom = (TextView) convertView.findViewById(R.id.txtIdRoomHistory);
            txtPeople = (TextView) convertView.findViewById(R.id.txtPeopleHistory);
            txtPhone = (TextView) convertView.findViewById(R.id.txtPhoneHistory);
            txtTotal = (TextView) convertView.findViewById(R.id.txtTotalHistory);

        }
    }

    @Override
    public int getGroupCount() {
        return arrDataHeader.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return arrDataHeader.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        Bills b=arrDataHeader.get(groupPosition);
        return b;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.item_elv_group_myhistory, null);
        }

         holder=new ViewHolder(convertView);

            Bills b = (Bills) getGroup(groupPosition);
            holder.txtDateCreateBill.setText(b.getDateCreate().toString());
            if (b.getIsValid() == true) {
                convertView.setBackgroundColor(context.getResources().getColor(R.color.DeepskyBlue));
                holder.txtStatusBill.setText(context.getResources().getString(R.string.myhistory_fragment_label_status_valid));
            } else {
                convertView.setBackgroundColor(context.getResources().getColor(R.color.Wheat1));
                holder.txtStatusBill.setText(context.getResources().getString(R.string.myhistory_fragment_label_status_invalid));
            }

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.item_elv_child_myhistory, null);
        }
         holder=new ViewHolder(convertView);

            Bills child = (Bills) getChild(groupPosition, childPosition);
            holder.txtCheckIn.setText(child.getCheckIn());
            holder.txtCheckOut.setText(child.getCheckOut());
            String roomId = "";
            for (int i = 0; i < child.getListRoom().size(); i++) {
                roomId += child.getListRoom().get(i) + " ";
            }

            holder.txtIdRoom.setText(roomId);
            holder.txtPeople.setText(child.getAmountPeople());
            holder.txtPhone.setText(child.getContact());
            holder.txtTotal.setText(child.getTotalPrice() + " VND");


        return convertView;
    }


    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}
