package uel.vteam.belovedhostel.MyInterface;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrConfig;
import com.r0adkll.slidr.model.SlidrPosition;

import uel.vteam.belovedhostel.R;

/**
 * Created by Hieu on 12/31/2016.
 */

public class SupportUI {
    private static SupportUI instance;
    public static SupportUI getInstance() {
        if (instance == null)
            instance = new SupportUI();
        return instance;
    }
    public void setupSwipeWindow(Context context,int primaryColor,int secondColor, SlidrPosition slidrPosition) {
        SlidrConfig mConfig = new SlidrConfig.Builder()

                .position(slidrPosition)
                .velocityThreshold(2400)
                .distanceThreshold(.25f)
                .edge(true)
                .build();

        Slidr.attach((Activity) context, mConfig);
    }
    public void customToast(Activity context, int layoutUse, String message){
        LayoutInflater inflater = context.getLayoutInflater();
        View layout = inflater.inflate(R.layout.custom_toast_layout,
                (ViewGroup) context.findViewById(layoutUse));

        TextView text = (TextView) layout.findViewById(R.id.txtMsgToast);
        text.setText(message);

        Toast toast = new Toast(context.getApplicationContext());
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
    }


}
