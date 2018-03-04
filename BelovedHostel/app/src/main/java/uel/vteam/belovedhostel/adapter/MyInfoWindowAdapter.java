package uel.vteam.belovedhostel.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import uel.vteam.belovedhostel.R;

public class MyInfoWindowAdapter implements InfoWindowAdapter {


    private Context context;
    private int iconId;
    private String address;

    public MyInfoWindowAdapter(Context context, int iconId, String address) {
        super();
        this.context = context;
        this.iconId = iconId;
        this.address = address;

    }

    @Override
    public View getInfoContents(Marker marker) {

        return null;

    }

    @Override
    public View getInfoWindow(Marker arg0) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.info_windowmap_layout, null);

        TextView txtDiachi = (TextView) view.findViewById(R.id.txtDiachi);
        TextView txtToaDo = (TextView) view.findViewById(R.id.txtChuthich);
        ImageView image = (ImageView) view.findViewById(R.id.imageView1);


        LatLng latLng = arg0.getPosition();
        if (address==null){
            txtDiachi.setText("");
        }else {
            txtDiachi.setText(address);
        }

        txtToaDo.setText(latLng.latitude+"\n"+ latLng.longitude);
        image.setImageResource(iconId);

        return view;
    }

}
