package uel.vteam.belovedhostel.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import uel.vteam.belovedhostel.R;
import uel.vteam.belovedhostel.model.MyFolderImages;


public class FolderImageAdapter extends BaseAdapter {
    Context context;
    ArrayList<MyFolderImages> listFolderImg;
    MyFolderImages folder;


    public FolderImageAdapter(Context context, ArrayList<MyFolderImages> listFolderImg) {
        this.context = context;
        this.listFolderImg = listFolderImg;
    }

    static class FolderViewHolder{
        ImageView imgFirst;
        TextView txtNameFolder;
        TextView txtSize;
    }


    @Override
    public int getCount() {
        return listFolderImg.size();
    }

    @Override
    public Object getItem(int i) {
        return listFolderImg.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        LayoutInflater inflater= (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        FolderViewHolder holder=null;
        folder = listFolderImg.get(i);
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_spinner_folder_image, null);
            holder = new FolderViewHolder();
            holder.imgFirst= (ImageView) convertView.findViewById(R.id.imgFirstItem);
            holder.txtNameFolder= (TextView) convertView.findViewById(R.id.txtNameFolder);
            holder.txtSize= (TextView) convertView.findViewById(R.id.txtSize);

            convertView.setTag(holder);
        }else {
            holder= (FolderViewHolder) convertView.getTag();
        }

        String name="";
        if (folder==listFolderImg.get(0)){
           name="Tất cả";
        }else {
            name=folder.getFolderName();
        }
        holder.txtNameFolder.setText(name);
        holder.txtSize.setText(folder.getListImage().size() + "");
        String urlFirstImg = folder.getListImage().get(0).getImageLink();
        LoadImageFromUrlTask task=new LoadImageFromUrlTask(holder);
        task.execute(urlFirstImg);
        return convertView;
    }

    public Bitmap loadImageFormUrl(String url){
        Bitmap bmp   = null;
        int targetWidth = 100;
        int targetHeight = 100;
        BitmapFactory.Options bmpOptions = new BitmapFactory.Options();
        bmpOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(url, bmpOptions);
        int currHeight = bmpOptions.outHeight;
        int currWidth = bmpOptions.outWidth;
        int sampleSize = 1;
        if (currHeight > targetHeight || currWidth > targetWidth)
        {
            if (currWidth > currHeight)
                sampleSize = Math.round((float)currHeight
                        / (float)targetHeight);
            else
                sampleSize = Math.round((float)currWidth
                        / (float)targetWidth);
        }
        bmpOptions.inSampleSize = sampleSize;
        bmpOptions.inJustDecodeBounds = false;
        bmp = BitmapFactory.decodeFile(url, bmpOptions);
        return bmp;
    }
}
