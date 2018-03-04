package sg.vinova.noticeboard.module.photo;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

import java.util.ArrayList;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import sg.vinova.noticeboard.model.Photo;
import vn.eazy.architect.mvp.base.BasePresenter;

/**
 * Created by Ray on 3/14/17.
 */

public class PhotoPresenterImpl extends BasePresenter<PhotoPresenter.PhotoView> implements PhotoPresenter.Presenter {
    public PhotoPresenterImpl(Context context) {
        super(context);
    }

    @Override
    public void loadPhotos(Context context) {
        Observable.just(context)
                .subscribeOn(Schedulers.io())
                .map(new Func1<Context, ArrayList<Photo>>() {
                    @Override
                    public ArrayList<Photo> call(Context context) {
                        ArrayList<Photo> listOfAllImages = new ArrayList<>();
                        Boolean isSDPresent = android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
                        if (isSDPresent) {
                            final String[] columns = {MediaStore.Images.Media.DATA, MediaStore.Images.Media._ID};
                            final String orderBy = MediaStore.Images.Media.DATE_ADDED;
                            //Stores all the images from the gallery in Cursor
                            Cursor cursor = context.getContentResolver().query(
                                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, null,
                                    null, orderBy);
                            //Total number of images
                            int count = cursor.getCount();

                            //Create an array to store path to all the images
                            String[] arrPath = new String[count];

                            for (int i = 0; i < count; i++) {
                                cursor.moveToPosition(i);
                                int dataColumnIndex = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
                                //Store the path of the image
                                listOfAllImages.add(new Photo(cursor.getString(dataColumnIndex), false));
                            }
                        }
                        final String[] columns = {MediaStore.Images.Media.DATA, MediaStore.Images.Media._ID};
                        final String orderBy = MediaStore.Images.Media.DATE_ADDED;
                        //Stores all the images from the gallery in Cursor
                        Cursor cursor = context.getContentResolver().query(
                                MediaStore.Images.Media.INTERNAL_CONTENT_URI, columns, null,
                                null, orderBy);
                        //Total number of images
                        int count = cursor.getCount();

                        //Create an array to store path to all the images
                        String[] arrPath = new String[count];

                        for (int i = 0; i < count; i++) {
                            cursor.moveToPosition(i);
                            int dataColumnIndex = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
                            //Store the path of the image
                            listOfAllImages.add(new Photo(cursor.getString(dataColumnIndex), false));
                        }
                        return listOfAllImages;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<ArrayList<Photo>>() {
                    @Override
                    public void call(ArrayList<Photo> strings) {
                        if (isAttached())
                            getView().loadPhotosSuccess(strings);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        if (isAttached())
                            getView().error(throwable.getLocalizedMessage());
                    }
                });
    }
}
