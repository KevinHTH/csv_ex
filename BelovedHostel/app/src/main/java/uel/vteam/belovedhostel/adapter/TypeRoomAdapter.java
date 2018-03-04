package uel.vteam.belovedhostel.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import uel.vteam.belovedhostel.R;
import uel.vteam.belovedhostel.model.RowTypeRoom;

/**
 * Created by Hieu on 11/12/2016.
 */

public class TypeRoomAdapter extends BaseAdapter {

    Context context;
    List<RowTypeRoom> rowItem;

    public  TypeRoomAdapter(Context context,
                            List<RowTypeRoom> rowItem) {
        this.context = context;
        this.rowItem = rowItem;
    }
    public class ViewHolder{
        ImageView icon;
        TextView title;
        TextView detail;
    }


    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder=null;
        LayoutInflater inflater= (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if(convertView==null){
            convertView=inflater.inflate(R.layout.item_list_typeroom,null);
            holder=new ViewHolder();

            holder.icon= (ImageView) convertView.findViewById(R.id.imgIconRoom);
            holder.title= (TextView) convertView.findViewById(R.id.txtRoomName);
            holder.detail= (TextView) convertView.findViewById(R.id.txtInfoRoom);

            RowTypeRoom row_selected=rowItem.get(position);

            holder.icon.setImageResource(row_selected.getIcon());
            holder.title.setText(row_selected.getTitle());
            holder.detail.setText(row_selected.getDetail());
            convertView.setTag(holder);
        }else {
            holder= (ViewHolder) convertView.getTag();
        }
        return  convertView;
    }
    @Override
    public int getCount() {
        return rowItem.size();
    }


    @Override
    public Object getItem(int position) {
        return rowItem.get(position);
    }

    @Override
    public long getItemId(int position) {
        return rowItem.indexOf(getItem(position));
    }
}
