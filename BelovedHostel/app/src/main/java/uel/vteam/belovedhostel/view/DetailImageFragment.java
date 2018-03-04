package uel.vteam.belovedhostel.view;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import uel.vteam.belovedhostel.MyInterface.ICommunicator;
import uel.vteam.belovedhostel.R;


public class DetailImageFragment extends DialogFragment {

    public ICommunicator iCommunicator;
    Button btnClose, btnSetAvatar;
    ImageView imgThumbnail;
    View v;
    Bitmap bitmap;
    String imgPath;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_detail_image, container, false);

        addControls();
        getData();
        addEvents();
        return v;
    }

    public void setiCommunicator(ICommunicator iCommunicator) {
        this.iCommunicator = iCommunicator;
    }

    private void addEvents() {
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });
        btnSetAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle data=new Bundle();
                data.putString("Image_Path",imgPath);
                iCommunicator.getInfoFromFragment(1,data);
                getDialog().dismiss();
            }
        });
    }


    private void getData() {
        getDialog().setTitle("Avatar");
        Bundle data = this.getArguments();
         imgPath = data.getString("image_path");

        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bitmap = BitmapFactory.decodeFile(imgPath, bmOptions);
      //  bitmap = Bitmap.createScaledBitmap(bitmap, 450, 700, true); // rong/cao/filter

        imgThumbnail.setImageBitmap(bitmap);
        imgThumbnail.setScaleType(ImageView.ScaleType.FIT_CENTER);
    }

    private void addControls() {
        btnClose = (Button) v.findViewById(R.id.btnCloseDetailImage);
        btnSetAvatar = (Button) v.findViewById(R.id.btnSetAvatar);
        imgThumbnail = (ImageView) v.findViewById(R.id.imgThumbnail);
    }


}
