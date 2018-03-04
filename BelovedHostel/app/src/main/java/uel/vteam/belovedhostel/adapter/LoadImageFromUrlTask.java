package uel.vteam.belovedhostel.adapter;

import android.content.Context;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

/**
 * Created by Hieu on 12/26/2016.
 */

public class LoadImageFromUrlTask extends AsyncTask<String,Void,Bitmap> {

    Context context;
    FolderImageAdapter.FolderViewHolder viewHolder;

    public LoadImageFromUrlTask(FolderImageAdapter.FolderViewHolder viewHolder) {
       // this.context = context;
        this.viewHolder = viewHolder;
    }


    @Override
    protected Bitmap doInBackground(String... params) {
        String urlImage=params[0];
        Bitmap bitmap = loadImageFormUrl(urlImage);
        return bitmap;
    }


    public Bitmap loadImageFormUrl(String url){
        Bitmap bmp   = null;
        int targetWidth = 50;
        int targetHeight = 50;
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

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);
        viewHolder.imgFirst.setImageBitmap(bitmap);
        viewHolder.imgFirst.setScaleType(ImageView.ScaleType.FIT_XY);
    }

/*viewHolder.imgFirst.setImageBitmap(bitmap);
    viewHolder.imgFirst.setScaleType(ImageView.ScaleType.FIT_XY);*/



}
