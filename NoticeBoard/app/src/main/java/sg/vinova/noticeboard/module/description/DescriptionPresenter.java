package sg.vinova.noticeboard.module.description;

import java.util.List;

import sg.vinova.noticeboard.base.MVPView;
import sg.vinova.noticeboard.model.Description;
import sg.vinova.noticeboard.model.LoginResponse;
import sg.vinova.noticeboard.model.Photo;

/**
 * Created by Vinova on 28/4/17.
 */

public interface DescriptionPresenter {
    interface Presenter{
        void getItemsById(String categoryId, String page, String pagePer);
        void getListItem(String categoryId, String page, String pagePer, String type);

    }

    interface View extends MVPView{
        void onGetItemsSuccess(List<Description> list);

    }

    interface PostPresenter{
        void postItem(String categoryId, String description,String type, String imgPath);
        void postItemSale(String categoryId, String description,String price, String imgPath);
        void pickPhoto();
    }

    interface PostView extends MVPView{
        void onTakePhotoSuccess(Photo photo);
        void onPickPhotoSuccess(String path);
        void onPostItemSuccess(LoginResponse loginResponse);

    }

    interface DeleteItemPresenter{
        void deleteItem(String id, String idItem);
    }

    interface DeleteView extends MVPView{
        void onDeleteItemSuccess(String message);
    }


}
