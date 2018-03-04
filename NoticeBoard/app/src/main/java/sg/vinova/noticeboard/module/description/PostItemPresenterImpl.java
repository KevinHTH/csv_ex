package sg.vinova.noticeboard.module.description;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import sg.vinova.noticeboard.model.LoginResponse;
import sg.vinova.noticeboard.usecase.PostItemSaleUseCase;
import sg.vinova.noticeboard.usecase.PostItemUseCase;
import sg.vinova.noticeboard.utils.CacheUtils;
import sg.vinova.noticeboard.utils.LoaderUtils;
import sg.vinova.noticeboard.utils.Log;
import sg.vinova.noticeboard.utils.Utils;


/**
 * Created by Vinova on 28/4/17.
 */

public class PostItemPresenterImpl extends BaseAppPresenter<DescriptionPresenter.PostView>
        implements DescriptionPresenter.PostPresenter {

    PostItemUseCase postItemUseCase;
    PostItemSaleUseCase postItemSaleUseCase;
    public PostItemPresenterImpl(Context context) {
        super(context);
        postItemUseCase = new PostItemUseCase(context);
        postItemSaleUseCase = new PostItemSaleUseCase(context);
    }


    @Override
    public void postItem(String categoryId, String description, String type, String imgPath) {
        LoaderUtils.show(context);
        MultipartBody.Builder builder = new MultipartBody.Builder();

        builder.addFormDataPart("category_id", categoryId);
        if (description != null) {
            builder.addFormDataPart("description", description);
        }
        if (TextUtils.isEmpty(type)){
            type = "normal";
        }
        builder.addFormDataPart("type_mode", type);
        builder.setType(MultipartBody.FORM);

        MediaType MEDIA_TYPE_PNG = MediaType.parse("image/*");
        if (!TextUtils.isEmpty(imgPath)) {
            builder.addFormDataPart("photo", "image.jpg",
                    RequestBody.create(MEDIA_TYPE_PNG, new File(imgPath)));
        }
        requestAPI(postItemUseCase.request(builder.build()), new BaseResponseListener<BaseObjectResponse<LoginResponse>>() {
            @Override
            public void onSuccess(BaseObjectResponse<LoginResponse> data) {
                LoaderUtils.hide();
                CacheUtils.setFirstPostItem(context,false);
                Log.d("Post:","false");
                getMVPView().onPostItemSuccess(data.getData());
            }

            @Override
            public void onError(String message) {
                LoaderUtils.hide();
                getMVPView().onError(message);
            }
        });

    }

    @Override
    public void postItemSale(String categoryId, String description, String price, String imgPath) {
        LoaderUtils.show(context);
        MultipartBody.Builder builder = new MultipartBody.Builder();

        builder.addFormDataPart("category_id", categoryId);
        if (description != null) {
            builder.addFormDataPart("description", description);
        }
        builder.addFormDataPart("price", price);
        builder.setType(MultipartBody.FORM);
        MediaType MEDIA_TYPE_PNG = MediaType.parse("image/*");
        if (!TextUtils.isEmpty(imgPath)) {
            builder.addFormDataPart("photo", "image.jpg",
                    RequestBody.create(MEDIA_TYPE_PNG, new File(imgPath)));
        }
        requestAPI(postItemSaleUseCase.request(builder.build()), new BaseResponseListener<BaseObjectResponse<LoginResponse>>() {
            @Override
            public void onSuccess(BaseObjectResponse<LoginResponse> data) {
                LoaderUtils.hide();
                CacheUtils.setFirstPostItem(context,false);
                Log.d("Post:","false");
                getMVPView().onPostItemSuccess(data.getData());
            }

            @Override
            public void onError(String message) {
                LoaderUtils.hide();
                getMVPView().onError(message);
            }
        });
    }

    @Override
    public void pickPhoto() {
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
                    getMVPView().onPickPhotoSuccess(Utils.saveBitmap(context, Calendar.getInstance().getTimeInMillis() + "", bitmapRotate));
                }
            }
        });
    }
}
