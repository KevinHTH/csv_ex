package uel.vteam.belovedhostel.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import uel.vteam.belovedhostel.R;

/**
 * Created by Hieu on 11/24/2016.
 */

public class MenuServiceAdapter extends BaseExpandableListAdapter {
    Context context;
    ArrayList<String> arrDataHeader;
    HashMap<String, List<String>> arrDataChild;
    ArrayList<Integer> arrImageHeader;

    public MenuServiceAdapter(Context context, ArrayList<String>
            arrDataHeader, HashMap<String, List<String>> arrDataChild, ArrayList<Integer> arrImageHeader) {
        this.context = context;
        this.arrDataHeader = arrDataHeader;
        this.arrDataChild = arrDataChild;
        this.arrImageHeader=arrImageHeader;
    }

    @Override
    public int getGroupCount() {
        return this.arrDataHeader.size();
    }

    @Override
    public int getChildrenCount(int groPosition) {
        return this.arrDataChild.get(this.arrDataHeader.get(groPosition)).size();
    }

    @Override
    public Object getGroup(int groPosition) {
        return this.arrDataHeader.get(groPosition);
    }

    @Override
    public Object getChild(int groPosition, int childPosition) {
        return this.arrDataChild.get(this.arrDataHeader.get(groPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groPosition) {
        return groPosition;
    }

    @Override
    public long getChildId(int groPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groPosition, boolean b, View view, ViewGroup viewGroup) {
        String headerTitle= (String) getGroup(groPosition);
        if (view==null){
            LayoutInflater layoutInflater= (LayoutInflater) this.context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view=layoutInflater.inflate(R.layout.item_elv_group_services,null);
        }

        TextView txtTitleHeader= (TextView) view.findViewById(R.id.txtTitleHeader);
        ImageView imgHeader= (ImageView) view.findViewById(R.id.imgHeader);

        txtTitleHeader.setTypeface(null, Typeface.BOLD);
        txtTitleHeader.setText(headerTitle);
        imgHeader.setImageResource(arrImageHeader.get(groPosition));

        return view;
    }

    @Override
    public View getChildView(int groPosition, int childPosition,
          boolean b, View view, ViewGroup viewGroup) {
        final String  childText = (String) getChild(groPosition,childPosition);
        if (view==null){
            LayoutInflater layoutInflater= (LayoutInflater) this.context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view=layoutInflater.inflate(R.layout.item_elv_child_services,null);
        }

        TextView txtListChild= (TextView) view.findViewById(R.id.lblListItem);
        txtListChild.setText(childText);
        return view;
    }

    @Override
    public boolean isChildSelectable(int groPosition, int childPosition) {
        return true;
    }
}
