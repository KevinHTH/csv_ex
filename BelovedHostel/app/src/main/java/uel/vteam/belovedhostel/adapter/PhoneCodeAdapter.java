package uel.vteam.belovedhostel.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import uel.vteam.belovedhostel.R;
import uel.vteam.belovedhostel.model.CountryPhoneCode;

/**
 * Created by Hieu on 11/18/2016.
 */

public class PhoneCodeAdapter extends ArrayAdapter<CountryPhoneCode> {

    Activity context=null;
    int layoutId;
    ArrayList<CountryPhoneCode> arrPhoneCode=null;

    public PhoneCodeAdapter(Activity context, int layoutId,
                            ArrayList<CountryPhoneCode> arrPhoneCode) {
        super(context, layoutId, arrPhoneCode);
        this.context = context;
        this.layoutId = layoutId;
        this.arrPhoneCode = arrPhoneCode;
    }

    @NonNull
    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater= (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view=inflater.inflate(layoutId,null);
        TextView txtCountryName= (TextView) view.findViewById(R.id.txtCountryName);
        TextView txtCode= (TextView) view.findViewById(R.id.txtCode);
        CountryPhoneCode country= arrPhoneCode.get(position);


        txtCountryName.setText(country.getName());
        txtCode.setText(country.getDial_code());

        return view;
    }
}
