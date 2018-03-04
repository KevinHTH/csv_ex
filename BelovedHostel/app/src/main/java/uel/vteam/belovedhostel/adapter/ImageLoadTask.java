package uel.vteam.belovedhostel.adapter;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

public class ImageLoadTask extends AsyncTask<Void, Void, Bitmap> {

    private String url;
    private GoogleMap map;
    private Context context;
    private boolean isCompleted = false;
    private Marker currentMarker;


    public ImageLoadTask(String url, GoogleMap map, Context context,
                         Marker currentMarker) {
        super();
        this.url = url;
        this.map = map;
        this.context = context;
        this.currentMarker = currentMarker;
    }


    public boolean isCompleted() {
        return isCompleted;
    }


    public void setCompleted(boolean isCompleted) {
        this.isCompleted = isCompleted;
    }


    @Override
    protected Bitmap doInBackground(Void... params) {
        try {
            URL urlConnection = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) urlConnection.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            if (myBitmap == null)
                return null;
            return myBitmap;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Bitmap result) {
        // TODO Auto-generated method stub
        super.onPostExecute(result);
        /*map.setInfoWindowAdapter(new MyInfoWindowAdapter((Activity) context,result));
		currentMarker.showInfoWindow();*/

    }

}
