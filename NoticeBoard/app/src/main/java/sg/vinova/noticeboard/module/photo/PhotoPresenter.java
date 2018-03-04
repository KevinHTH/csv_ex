package sg.vinova.noticeboard.module.photo;

import android.content.Context;

import java.util.List;

import sg.vinova.noticeboard.model.Photo;
import vn.eazy.architect.mvp.base.BasePresenter;

/**
 * Created by Ray on 3/14/17.
 */

public interface PhotoPresenter {

    interface PhotoView extends BasePresenter.View{
        void loadPhotosSuccess(List<Photo> photos);
        void error(String msg);
    }
    interface Presenter{
        void loadPhotos(Context context);
    }
}
