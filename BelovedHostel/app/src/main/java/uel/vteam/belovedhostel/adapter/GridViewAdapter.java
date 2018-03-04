package uel.vteam.belovedhostel.adapter;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import android.widget.ImageView;


import java.util.ArrayList;

import uel.vteam.belovedhostel.R;
import uel.vteam.belovedhostel.model.MyImage;

/**
 * Created by Hieu on 12/25/2016.
 */

public class GridViewAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<MyImage> listImage;

    public GridViewAdapter(Context context, ArrayList<MyImage> listImage) {
        this.context = context;
        this.listImage = listImage;

    }

    public int getCount() {
        return listImage.size();
    }

    public Object getItem(int position) {
        return listImage.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    static class GridViewHolder {
        ImageView image;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        GridViewHolder holder=null;
        LayoutInflater inflater= (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_gridview_image, null);
            holder = new GridViewHolder();
            holder.image = (ImageView) convertView.findViewById(R.id.thumbImage);
           convertView.setTag(holder);
        }else {
            holder= (GridViewHolder) convertView.getTag();
        }

            holder.image.setImageBitmap(loadImageFormUrl(listImage.get(position).getImageLink()));
            holder.image.setScaleType(ImageView.ScaleType.CENTER_CROP);
        return convertView;
    }
    public Bitmap loadImageFormUrl(String url){
        Bitmap bmp = null;
        int targetWidth = 80;
        int targetHeight = 80;
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
