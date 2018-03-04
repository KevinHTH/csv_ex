package uel.vteam.belovedhostel.adapter;

import android.content.Context;
import android.graphics.Bitmap;

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

import java.util.Date;
import java.util.List;

import uel.vteam.belovedhostel.R;
import uel.vteam.belovedhostel.model.Account;
import uel.vteam.belovedhostel.model.Messages;


/**
 * Created by Hieu on 12/8/2016.
 */
public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {


    private List<Messages> messageList;
    Messages message = new Messages();
    Context context;
    Account myAccount, friendAccount;

    public static final int SENDER = 0;
    public static final int RECEIVER = 1;

    public MessageAdapter(Context context, List<Messages> messages, Account myAccount, Account friendAccount) {
        this.context=context;
        this.messageList = messages;
        this.myAccount=myAccount;
        this.friendAccount=friendAccount;
    }
    public void remove(int pos) {
        int position = pos;
        messageList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, messageList.size());
    }
    public void updateList(List<Messages> data) {
        messageList = data;
        notifyDataSetChanged();


    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgChatAvatar;
        TextView txtTextContent;
        TextView txtTimeSend;

        public ViewHolder(LinearLayout v) {
            super(v);
            imgChatAvatar = (ImageView) v.findViewById(R.id.imgChatAvatar);
            txtTextContent= (TextView) v.findViewById(R.id.txtTextContent);
            txtTimeSend= (TextView) v.findViewById(R.id.txtTimeSend);

        }
    }


    @Override
    public MessageAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == 1) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_left, parent, false);
            ViewHolder vh = new ViewHolder((LinearLayout) v);
            return vh;
        } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_right, parent, false);
            ViewHolder vh = new ViewHolder((LinearLayout) v);
            return vh;
        }

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        message=messageList.get(position);
        holder.txtTextContent.setText(message.getText().toString());
        Date timeSend=message.getTime();
        holder.txtTimeSend.setText(timeSend.getHours()+":"+timeSend.getMinutes());

        String urlAvatar="";
        if (getItemViewType(position)!=1){
            urlAvatar=myAccount.getUserAvatar();
        }else {
            urlAvatar=friendAccount.getUserAvatar();
        }

        Glide.with(context).load(urlAvatar).asBitmap().centerCrop().into(new BitmapImageViewTarget(holder.imgChatAvatar) {
            @Override
            protected void setResource(Bitmap resource) {
                RoundedBitmapDrawable circularBitmapDrawable =
                        RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                circularBitmapDrawable.setCircular(true);
                holder.imgChatAvatar .setImageDrawable(circularBitmapDrawable);
            }
        });


    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    @Override
    public int getItemViewType(int position) {
        message = messageList.get(position);
        String sender=message.getSender();

        if (sender.equalsIgnoreCase(myAccount.getUserName())){
            return SENDER;
        }else {
           return RECEIVER;
        }
    }

}
