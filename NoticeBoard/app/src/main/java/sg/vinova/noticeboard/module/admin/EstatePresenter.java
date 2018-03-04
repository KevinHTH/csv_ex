package sg.vinova.noticeboard.module.admin;

import java.util.List;

import sg.vinova.noticeboard.base.BaseObjectResponse;
import sg.vinova.noticeboard.base.MVPView;
import sg.vinova.noticeboard.model.Description;
import sg.vinova.noticeboard.model.ImageObject;
import sg.vinova.noticeboard.model.Photo;

/**
 * Created by Vinova on 28/4/17.
 */

public interface EstatePresenter {

    interface PostPresenter{
        void postEstate(String type, Description description, List<Photo> photoList);
    }

    interface EstateView extends MVPView{
        void onTakePhotoSuccess(Photo photo);
        void onPostSuccess(BaseObjectResponse baseObjectResponse);
        void onEditSuccess(String message);
    }

    interface EditPresenter{
        void editEstate(String type, Description description, List<Photo> photoList);
    }


}
