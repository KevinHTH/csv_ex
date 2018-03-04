package sg.vinova.noticeboard.module.description;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.text.TextUtils;

import com.mlsdev.rximagepicker.RxImageConverters;
import com.mlsdev.rximagepicker.RxImagePicker;
import com.mlsdev.rximagepicker.Sources;

import java.io.File;
import java.util.Calendar;

import io.reactivex.ObservableSource;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import sg.vinova.noticeboard.base.BaseAppPresenter;
import sg.vinova.noticeboard.base.BaseObjectResponse;
import sg.vinova.noticeboard.base.BaseResponseListener;
import sg.vinova.noticeboard.base.MessagesApi;
import sg.vinova.noticeboard.model.LoginResponse;
import sg.vinova.noticeboard.usecase.DeleteItemUseCase;
import sg.vinova.noticeboard.usecase.PostItemSaleUseCase;
import sg.vinova.noticeboard.usecase.PostItemUseCase;
import sg.vinova.noticeboard.utils.LoaderUtils;
import sg.vinova.noticeboard.utils.Utils;


/**
 * Created by Vinova on 28/4/17.
 */

public class DeleteItemPresenterImpl extends BaseAppPresenter<DescriptionPresenter.DeleteView>
        implements DescriptionPresenter.DeleteItemPresenter {

    DeleteItemUseCase deleteItemUseCase;
    public DeleteItemPresenterImpl(Context context) {
        super(context);
        deleteItemUseCase = new DeleteItemUseCase(context);
    }


    @Override
    public void deleteItem(String id, String idItem) {
        LoaderUtils.show(context);
        requestAPI(deleteItemUseCase.request(id, idItem), new BaseResponseListener<BaseObjectResponse<MessagesApi>>() {
            @Override
            public void onSuccess(BaseObjectResponse<MessagesApi> data) {
                LoaderUtils.hide();
                getMVPView().onDeleteItemSuccess(data.getData().getMessage());
            }

            @Override
            public void onError(String message) {
                LoaderUtils.hide();
                getMVPView().onError(message);
            }
        });
    }
}
