package sg.vinova.noticeboard.module.user;

import sg.vinova.noticeboard.base.MVPView;
import sg.vinova.noticeboard.model.LoginResponse;
import sg.vinova.noticeboard.model.Photo;

/**
 * Created by cuong on 4/27/17.
 */

public interface FeedbackPresenter {
    interface Presenter{
        void attachPhoto();
        void sendFeedback(String descrition, String typeMode, String imagePath);
    }

    interface View extends MVPView {
        void onAttachPhotoSuccess(String path);
        void onTakePhotoSuccess(Photo photo);
        void onSendFeedbackSuccess(String message);
    }
}
