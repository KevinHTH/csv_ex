package sg.vinova.noticeboard.module.admin;

import android.content.Context;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import sg.vinova.noticeboard.base.BaseAppPresenter;
import sg.vinova.noticeboard.base.BaseObjectResponse;
import sg.vinova.noticeboard.base.BaseResponseListener;
import sg.vinova.noticeboard.base.MessagesApi;
import sg.vinova.noticeboard.factory.ConstantApp;
import sg.vinova.noticeboard.model.Description;
import sg.vinova.noticeboard.model.ImageObject;
import sg.vinova.noticeboard.model.Photo;
import sg.vinova.noticeboard.usecase.admin.EditEstateUseCase;
import sg.vinova.noticeboard.usecase.admin.PostEstateUseCase;
import sg.vinova.noticeboard.utils.LoaderUtils;


/**
 * Created by Vinova on 28/4/17.
 */

public class EstatePresenterImpl extends BaseAppPresenter<EstatePresenter.EstateView>
        implements EstatePresenter.PostPresenter, EstatePresenter.EditPresenter {

    PostEstateUseCase postEstateUseCase;
    EditEstateUseCase editEstateUseCase;



    public EstatePresenterImpl(Context context) {
        super(context);
        postEstateUseCase = new PostEstateUseCase(context);
        editEstateUseCase = new EditEstateUseCase(context);
    }


    @Override
    public void postEstate(String type, Description description, List<Photo> photoList) {
        LoaderUtils.show(context);
        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);
        builder.addFormDataPart("category_id", String.valueOf(description.getCategory().getId()));
        builder.addFormDataPart("description", description.getDescription());
        builder.addFormDataPart("no_of_bedrooms", String.valueOf(description.getNoOfBedRooms()));
        builder.addFormDataPart("type_property", type);
        if (type.equals(ConstantApp.TYPESUBMIT.SALE)){
            builder.addFormDataPart("sales_price", String.valueOf(description.getSalePrice()));
            builder.addFormDataPart("size", String.valueOf(description.getSize()));
        }else {
            builder.addFormDataPart("rental_per_month", String.valueOf(description.getRentalPerMonth()));
        }

        for (Photo photo : photoList) {
            if (photo.getPath().startsWith("http")) {
                continue;
            }
            File file = new File(photo.getPath());
            builder.addFormDataPart("photos[][photo]", file.getName(),
                    RequestBody.create(MediaType.parse("image/*"), file));
        }

        requestAPI(postEstateUseCase.request(builder.build()), new BaseResponseListener<BaseObjectResponse>() {
            @Override
            public void onSuccess(BaseObjectResponse data) {
                LoaderUtils.hide();
                if (isAttached())
                    getMVPView().onPostSuccess(data);
            }

            @Override
            public void onError(String message) {
                LoaderUtils.hide();
                if (isAttached())
                    getMVPView().onError(message);
            }
        });

    }

    @Override
    public void editEstate(String type, Description description, List<Photo> photoList) {
        LoaderUtils.show(context);
        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);
        builder.addFormDataPart("category_id", String.valueOf(description.getCategory().getId()));
        builder.addFormDataPart("description", description.getDescription());
        builder.addFormDataPart("no_of_bedrooms", String.valueOf(description.getNoOfBedRooms()));
        builder.addFormDataPart("type_property", type);
        if (type.equals(ConstantApp.TYPESUBMIT.SALE)){
            builder.addFormDataPart("sales_price", String.valueOf(description.getSalePrice()));
            builder.addFormDataPart("size", String.valueOf(description.getSize()));
        }else {
            builder.addFormDataPart("rental_per_month", String.valueOf(description.getRentalPerMonth()));
        }

        for (ImageObject imageObject : description.getImages()) {
            if (imageObject.getPhotoUrl().startsWith("http")) {
                builder.addFormDataPart("current_photo[][id]", String.valueOf(imageObject.getId()));
                builder.addFormDataPart("current_photo[][_destroy]", String.valueOf(imageObject.get_destroy()));

            }
        }

        for (Photo photo : photoList) {
            if (photo.getPath().startsWith("http")) {
                continue;
            }
            File file = new File(photo.getPath());
            builder.addFormDataPart("photos[][photo]", file.getName(),
                    RequestBody.create(MediaType.parse("image/*"), file));
        }

        HashMap<Integer, Object> map = new HashMap<>();
        map.put(0, description.getId());
        map.put(1, builder.build());

        requestAPI(editEstateUseCase.request(map), new BaseResponseListener<BaseObjectResponse<MessagesApi>>() {
            @Override
            public void onSuccess(BaseObjectResponse<MessagesApi> data) {
                LoaderUtils.hide();
                getMVPView().onEditSuccess("thanh cong");
            }

            @Override
            public void onError(String message) {
                LoaderUtils.hide();
                getMVPView().onError(message);
            }
        });

    }
}
