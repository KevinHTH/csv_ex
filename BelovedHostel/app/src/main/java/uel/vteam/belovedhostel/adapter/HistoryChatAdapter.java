package uel.vteam.belovedhostel.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.EnumSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import uel.vteam.belovedhostel.R;
import uel.vteam.belovedhostel.model.Account;
import uel.vteam.belovedhostel.model.ChattingItem;
import uel.vteam.belovedhostel.model.Messages;
import uel.vteam.belovedhostel.view.ChatActivity;


public class HistoryChatAdapter extends RecyclerView.Adapter<HistoryChatAdapter.ChatViewHolder>{
    Context context;

    DatabaseReference root;
    String myId;
    ArrayList<ChattingItem> arrItems=new ArrayList<>();

    public HistoryChatAdapter(Context context, ArrayList<ChattingItem> chattingItems) {
        this.context = context;
        this.arrItems =chattingItems;
    }

    @Override
    public ChatViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lv_list_chatting, parent, false);
        ChatViewHolder vh = new ChatViewHolder((LinearLayout)v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ChatViewHolder holder, int position) {
        ChattingItem item=arrItems.get(position);
        Account account=item.getFriendAcc();
        holder.txtNameAccount.setText(account.getUserName().toString());

        Glide.with(context).load(account.getUserAvatar()).asBitmap().centerCrop()
                .into(new BitmapImageViewTarget(holder.imgAvatar) {
            @Override
            protected void setResource(Bitmap resource) {
                RoundedBitmapDrawable circularBitmapDrawable =
                        RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                circularBitmapDrawable.setCircular(true);
                holder.imgAvatar .setImageDrawable(circularBitmapDrawable);
            }
        });
        Messages lastMessage=item.getLastMsg();

        Date date1=lastMessage.getTime();
        Date date2=new Date();
        long [] diff=getDiffTime(date1,date2);
        long ngay=diff[0];
        long gio=diff[1];
        long phut=diff[2];
        if (ngay>0){
            holder.txtTimeAccess.setText(ngay+" ngày");
        }else {
            if(gio>=1){
                holder.txtTimeAccess.setText(gio+" giờ");
            }else {
                if (phut>=1){
                    holder.txtTimeAccess.setText(phut+" phút");
                }else {
                    holder.txtTimeAccess.setText("1 phút");
                }

            }
        }
        holder.txtContentHistory.setText(lastMessage.getText());
    }

    private long[] getDiffTime(Date date1, Date date2) {
        long[] result=new long[5];
        Calendar cal=Calendar.getInstance();
        cal.setTimeZone(TimeZone.getDefault());

        cal.setTime(date1);


        long t1=cal.getTimeInMillis();
        cal.setTime(date2);

        long diff= Math.abs(cal.getTimeInMillis()-t1);
        final int ONE_DAY = 1000 * 60 * 60 * 24;
        final int ONE_HOUR = ONE_DAY / 24;
        final int ONE_MINUTE = ONE_HOUR / 60;
        final int ONE_SECOND = ONE_MINUTE / 60;

        long d = diff / ONE_DAY;
        diff %= ONE_DAY;

        long h = diff / ONE_HOUR;
        diff %= ONE_HOUR;

        long m = diff / ONE_MINUTE;
        diff %= ONE_MINUTE;

        long s = diff / ONE_SECOND;
        long ms = diff % ONE_SECOND;
        result[0] = d;
        result[1] = h;
        result[2] = m;
        result[3] = s;
        result[4] = ms;

        return result;
    }
    @Override
    public int getItemCount() {
        return arrItems.size();
    }

    public void clearListAccount() {
            this.arrItems.clear();
            notifyDataSetChanged();
    }
    public static Map<TimeUnit,Long> computeDiff(Date date1, Date date2) {

        long diffInMillies = date2.getTime() - date1.getTime();
        List<TimeUnit> units = new ArrayList<TimeUnit>(EnumSet.allOf(TimeUnit.class));
        Collections.reverse(units);

        Map<TimeUnit,Long> result = new LinkedHashMap<TimeUnit,Long>();
        long milliesRest = diffInMillies;
        for ( TimeUnit unit : units ) {
            long diff = unit.convert(milliesRest,TimeUnit.MILLISECONDS);
            long diffInMilliesForUnit = unit.toMillis(diff);
            milliesRest = milliesRest - diffInMilliesForUnit;
            result.put(unit,diff);
        }
        return result;
    }

    public class ChatViewHolder extends RecyclerView.ViewHolder {
        ImageView imgAvatar;
        TextView txtNameAccount;
        TextView txtContentHistory;
        TextView txtTimeAccess;
        public ChatViewHolder(LinearLayout v) {
            super(v);
            imgAvatar=(ImageView) v.findViewById(R.id.imgAvatarAccount);

            txtNameAccount= (TextView) v.findViewById(R.id.txtNamAccount);
            txtContentHistory= (TextView) v.findViewById(R.id.txtContentHistory);
            txtTimeAccess= (TextView) v.findViewById(R.id.txtTimeAccess);
            root = FirebaseDatabase.getInstance().getReference();
            myId=getCurrentAppId();
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    final Account acSelected = arrItems.get(getAdapterPosition()).getFriendAcc();
                    root.child("USER").child(myId).child("USER_INFO").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Account account = dataSnapshot.getValue(Account.class);

                            Bundle userData = new Bundle();
                            userData.putSerializable("myAccount", account);
                            userData.putSerializable("UserSelected", acSelected);
                            Intent i = new Intent(context, ChatActivity.class);
                            i.putExtras(userData);
                            context.startActivity(i);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            });

            v.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    final int position=getAdapterPosition();
                    AlertDialog.Builder builder=new AlertDialog.Builder(context);
                    builder.setIcon(R.drawable.ic_delete_black_48px);
                    builder.setMessage("Bạn có muốn xóa tin nhắn này?");
                    builder.setPositiveButton("Xóa!", new DialogInterface. OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {
                            deleteMessage(position);
                            notifyItemRemoved(position);
                        }});
                    builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {
                            dialog.cancel();
                        }
                    });
                    builder.create().show();
                    return true;
                }
            });
        }
    }

    private void deleteMessage(int position) {

        String idDelete= arrItems.get(position).getFriendAcc().getUserId();
        root.child("USER").child(myId).child("SessionChat").child(idDelete).setValue(null);
    }

    private String getCurrentAppId() {
        String strId = "";
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            strId = user.getUid();

        } else {
            strId = "";
        }
        return strId;
    }
}
