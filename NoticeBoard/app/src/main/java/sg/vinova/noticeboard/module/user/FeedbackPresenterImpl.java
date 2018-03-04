package sg.vinova.noticeboard.module.user;

import android.content.Context;
import android.content.Intent;
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
import sg.vinova.noticeboard.usecase.SendFeedbackUseCase;
import sg.vinova.noticeboard.utils.LoaderUtils;
import sg.vinova.noticeboard.utils.Utils;

/**
 * Created by cuong on 4/27/17.
 */

public class FeedbackPresenterImpl extends BaseAppPresenter<FeedbackPresenter.View> implements FeedbackPresenter.Presenter {

    SendFeedbackUseCase sendFeedbackUseCase;

    public FeedbackPresenterImpl(Context context) {
        super(context);
        sendFeedbackUseCase = new SendFeedbackUseCase(context);
    }

    @Override
    public void attachPhoto() {
        RxImagePicker.with(context).requestImage(Sources.GALLERY)
                .flatMap(new Function<Uri, ObservableSource<Bitmap>>() {
                    @Override
                    public ObservableSource<Bitmap> apply(@NonNull Uri uri) throws Exception {
                        return RxImageConverters.uriToBitmap(context, uri);
                    }
                }).subscribe(new Consumer<Bitmap>() {
            @Override
            public void accept(@NonNull Bitmap bitmap) throws Exception {
                if (isAttached()) {
                    Bitmap bitmapRotate =  Utils.getRotatedBitmap(Utils.saveBitmap(context, Calendar.getInstance().getTimeInMillis() + "", bitmap), bitmap);
                    getMVPView().onAttachPhotoSuccess(Utils.saveBitmap(context, Calendar.getInstance().getTimeInMillis() + "", bitmapRotate));
                }
            }
        });
    }

    @Override
    public void sendFeedback(String description, String typeMode, String imagePath) {
        LoaderUtils.show(context);
        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.addFormDataPart("description",description);
        builder.addFormDataPart("type_mode",typeMode);
        if (imagePath!=null || !TextUtils.isEmpty(imagePath)){
            MediaType MEDIA_TYPE_PNG = MediaType.parse("image/*");
            builder.addFormDataPart("image","image.jpg",
                    RequestBody.create(MEDIA_TYPE_PNG,new File(imagePath)) );
        }
        requestAPI(sendFeedbackUseCase.request(builder.build()), new BaseResponseListener<BaseObjectResponse<MessagesApi>>() {
            @Override
            public void onSuccess(BaseObjectResponse<MessagesApi> data) {
                LoaderUtils.hide();
                getMVPView().onSendFeedbackSuccess(data.getData().getMessage());
            }

            @Override
            public void onError(String message) {
                LoaderUtils.hide();
                getMVPView().onError(message);
            }
        });
    }
}
