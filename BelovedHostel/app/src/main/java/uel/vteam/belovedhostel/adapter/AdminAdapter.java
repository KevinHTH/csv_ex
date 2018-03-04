package uel.vteam.belovedhostel.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import java.util.ArrayList;

import uel.vteam.belovedhostel.R;
import uel.vteam.belovedhostel.model.Account;

/**
 * Created by Hieu on 12/7/2016.
 */

public class AdminAdapter extends BaseAdapter  {

    ArrayList<Account> arrAdmin;
    Context context;
    LayoutInflater inflater;
    Account admin=new Account();

    public AdminAdapter(Context context, ArrayList<Account> arrAdmin) {
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.arrAdmin = arrAdmin;
    }

    static class ViewHolder{
        ImageView imgAvatar;
        ImageView imgStatus;
        TextView txtName;
    }

    @Override
    public int getCount() {
        return arrAdmin.size();
    }

    @Override
    public Object getItem(int position) {
        return arrAdmin.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public View getView(int position, View row, ViewGroup parent) {
        final ViewHolder holder;
        LayoutInflater inflater= (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            row=inflater.inflate(R.layout.item_lv_account_admin,null);
            holder=new ViewHolder();
            admin=arrAdmin.get(position);
            holder.imgAvatar=(ImageView) row.findViewById(R.id.imgItemLvAccount);
            holder.imgStatus= (ImageView) row.findViewById(R.id.imgStatus);
            holder.txtName= (TextView) row.findViewById(R.id.txtItemNameLvAccount);

            holder.txtName.setText(admin.getUserName().toString());
            boolean online= Boolean.parseBoolean(admin.getStatus().toString());
            if (online==true){
                holder.imgStatus.setImageResource(R.drawable.ic_online);
            }else {
                holder.imgStatus.setImageResource(R.drawable.ic_offline);
            }

        Glide.with(context).load(admin.getUserAvatar()).asBitmap().centerCrop()
                .into(new BitmapImageViewTarget(holder.imgAvatar) {
            @Override
            protected void setResource(Bitmap resource) {
                RoundedBitmapDrawable circularBitmapDrawable =
                        RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                circularBitmapDrawable.setCircular(true);
                holder.imgAvatar.setImageDrawable(circularBitmapDrawable);
            }
        });

        return row;
    }
}
