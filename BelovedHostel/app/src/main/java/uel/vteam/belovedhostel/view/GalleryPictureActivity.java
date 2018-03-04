package uel.vteam.belovedhostel.view;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.SortedSet;
import java.util.TreeSet;

import uel.vteam.belovedhostel.MyInterface.ICommunicator;
import uel.vteam.belovedhostel.R;
import uel.vteam.belovedhostel.adapter.FolderImageAdapter;
import uel.vteam.belovedhostel.adapter.GridViewAdapter;
import uel.vteam.belovedhostel.model.Constant;
import uel.vteam.belovedhostel.model.MyFolderImages;
import uel.vteam.belovedhostel.model.MyImage;


public class GalleryPictureActivity extends AppCompatActivity implements ICommunicator {

    SortedSet<String> dirList;
    String[] directories=null;
    Uri url;
    Cursor c = null;

    ArrayList<MyFolderImages> listFolderImg;
    Spinner spinFolderImage;
    FolderImageAdapter spinAdapter=null;

    ArrayList<MyImage> listImageSelected;
    GridView gridViewImg;
    GridViewAdapter gridAdapter=null;
    StorageReference storageRef;
    FirebaseStorage storage=FirebaseStorage.getInstance();
    @Override
    protected void onCreate (Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery_picture);

        storageRef = storage.getReferenceFromUrl(Constant.URL_STORAGE);
        addControls();
        addEvents();

    }

    private void addEvents() {
      spinFolderImage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
          @Override
          public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

              listImageSelected.clear();
              listImageSelected.addAll(listFolderImg.get(position).getListImage());
              gridAdapter.notifyDataSetChanged();
          }

          @Override
          public void onNothingSelected(AdapterView<?> parent) {

          }
      });

        gridViewImg.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               FragmentManager fragmentManager = getSupportFragmentManager();
                DetailImageFragment detail = new DetailImageFragment();
                Bundle dataBooking = new Bundle();
                dataBooking.putString("image_path",listImageSelected.get(position).getImageLink());
                detail.setArguments(dataBooking);
                detail.show(fragmentManager, "Set Avatar");
                detail.setiCommunicator(GalleryPictureActivity.this);

            }

        });
    }


    private void addControls() {
        listFolderImg=new ArrayList<>();

        spinFolderImage= (Spinner) findViewById(R.id.spFolderImage);
        gridViewImg = (GridView)findViewById(R.id.gridImages);

        spinAdapter =new FolderImageAdapter(this,listFolderImg);
        spinFolderImage.setAdapter(spinAdapter);
        getData();

        listImageSelected=new ArrayList<>();

        gridAdapter = new GridViewAdapter(this, listImageSelected);
        gridViewImg.setAdapter(gridAdapter);

            listImageSelected.addAll(listFolderImg.get(0).getListImage());
            gridAdapter.notifyDataSetChanged();

    }

    private void getData() {

        File sdCard= Environment.getExternalStorageDirectory();
        if (sdCard.exists()){
           url = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        }else {
           url= MediaStore.Images.Media.INTERNAL_CONTENT_URI;
        }

        String[] projection = {MediaStore.Images.ImageColumns.DATA};

        dirList = new TreeSet<String>();
        // get cursor null do ko co the nho
        if (url != null) {
            c = managedQuery(url, projection, null, null, null);
        }
        if ((c != null) && (c.moveToFirst())) {
            // neu co hinh
            do {
                String tempDir = c.getString(0);
                tempDir = tempDir.substring(0, tempDir.lastIndexOf("/"));
                try {
                    dirList.add(tempDir);
                } catch (Exception e) {
                }
            }
            while (c.moveToNext());
            directories = new String[dirList.size()];
            dirList.toArray(directories);
            loadImage();

        }

    }



    private void loadImage() {
        listFolderImg.clear();
        for (int i = 0; i < dirList.size(); i++) {
            // lay danh sach folder chua image
            File allImageInFolder = new File(directories[i]);
            File[] imageList = allImageInFolder.listFiles(); // danh sach hinh co trong 1 thu muc allImageInFolder

            MyFolderImages folderImages=new MyFolderImages();
            folderImages.setFolderName(allImageInFolder.getName());
            folderImages.setFolderPath(allImageInFolder.getAbsolutePath());
            ArrayList<MyImage>  listImage=new ArrayList<>();

            if (imageList == null)
                continue;
            for (File imagePath : imageList) {// vao doc thu muc hinh, chi lay hinh dung dinh dang
                // lay danh sach image cua 1 folder chua image
                try {
                    if (imagePath.isDirectory()) {
                        imageList = imagePath.listFiles();
                    }
                    if (imagePath.getName().contains(".jpg") || imagePath.getName().contains(".JPG")
                            || imagePath.getName().contains(".jpeg") || imagePath.getName().contains(".JPEG")
                            || imagePath.getName().contains(".png") || imagePath.getName().contains(".PNG")
                            ) {
                        MyImage image = new MyImage();
                        image.setImageName(imagePath.getName());
                        image.setImageLink(imagePath.getAbsolutePath());
                        listImage.add(image);
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
            folderImages.setListImage(listImage);
            listFolderImg.add(folderImages);
        }  spinAdapter.notifyDataSetChanged();
    }


    @Override
    public void getInfoFromFragment(int fragmentId, Bundle data) {
        if (fragmentId==1){
            String imgPath=data.getString("Image_Path");
            Intent intent=getIntent();
            intent.putExtra("Image_Path", imgPath);
            setResult(Constant.RESULT_UPDATE_AVATAR,intent);
            this.finish();
        }
    }
}
