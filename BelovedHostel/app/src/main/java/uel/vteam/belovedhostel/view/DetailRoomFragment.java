package uel.vteam.belovedhostel.view;


import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.DialogFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import uel.vteam.belovedhostel.R;
import uel.vteam.belovedhostel.model.Constant;
import uel.vteam.belovedhostel.model.MyImage;
import uel.vteam.belovedhostel.model.MyRoom;


public class DetailRoomFragment extends DialogFragment  {

    View view;
    private Button btnCloseDialog;
    ArrayList<MyImage> arrImage;
    ImageView imgRoom;
    TextView txtRoomId;
    LinearLayout layoutImage;
    MyRoom roomSelected;
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view= inflater.inflate(R.layout.fragment_detail_room, container, false);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        addControls();
        addEvents();
        getData();
        setupUI();
        return view;
    }

    private void getData() {
        Bundle data =this.getArguments();
        roomSelected = (MyRoom) data.getSerializable(Constant.BUNDLE_ROOM_SELECTED);
        arrImage = (ArrayList<MyImage>) roomSelected.getRoomImage();
    }

    private void setupUI() {
        txtRoomId.setText(roomSelected.getRoomId().toString());
        for (int i=0;i<arrImage.size();i++) {
            imgRoom = new ImageView(getContext());
            layoutImage.addView(imgRoom);
            imgRoom.setAdjustViewBounds(true);
           /* LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams
                    (ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);*/
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams
                    (500, 550);
            lp.setMargins(2, 0, 0, 0); // trai tren phai duoi
            imgRoom.setLayoutParams(lp);
         //    imgRoom.setScaleType(ImageView.ScaleType.MATRIX);
          //  imgRoom.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            Glide.with(getContext())
                    .load(arrImage.get(i).getImageLink()).centerCrop()
                    .into(imgRoom);
        }
    }

    private void addEvents() {
        btnCloseDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDialog().dismiss();
            }
        });
    }

    private void addControls() {

        txtRoomId= (TextView) view.findViewById(R.id.txtRoomId);
        btnCloseDialog= (Button) view.findViewById(R.id.btnCloseDialog);
        layoutImage= (LinearLayout) view.findViewById(R.id.layoutImage);

    }
}
