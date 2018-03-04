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
import sg.vinova.noticeboard.model.Photo;
import sg.vinova.noticeboard.usecase.admin.EditTransactionUseCase;
import sg.vinova.noticeboard.usecase.admin.PostEstateUseCase;
import sg.vinova.noticeboard.usecase.admin.PostTransactionUseCase;
import sg.vinova.noticeboard.utils.LoaderUtils;


/**
 * Created by Vinova on 28/4/17.
 */

public class TransactionPresenterImpl extends BaseAppPresenter<TransactionPresenter.TransactionView>
implements TransactionPresenter.PostPresenter, TransactionPresenter.EditPresenter {


    PostTransactionUseCase postTransactionUseCase;
    EditTransactionUseCase editTransactionUseCase;

    public TransactionPresenterImpl(Context context, String type) {
        super(context);
        postTransactionUseCase = new PostTransactionUseCase(context,type);
        editTransactionUseCase = new EditTransactionUseCase(context,type);
    }


    @Override
    public void postTransaction(String type, Description description) {
        LoaderUtils.show(context);
        postTransactionUseCase.setType(type);
        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);
        builder.addFormDataPart("category_id", String.valueOf(description.getCategory().getId()));
        builder.addFormDataPart("date", description.getDate());
        builder.addFormDataPart("size", String.valueOf(description.getSize()));

        if (type.equals(ConstantApp.TYPESUBMIT.SALE)){
            builder.addFormDataPart("price", String.valueOf(description.getSalePrice()));
            builder.addFormDataPart("block", String.valueOf(description.getBlock()));

        }else {
            builder.addFormDataPart("no_of_bedrooms", String.valueOf(description.getNoOfBedRooms()));
            builder.addFormDataPart("rental_per_month", String.valueOf(description.getRentalPerMonth()));
        }

        requestAPI(postTransactionUseCase.request(builder.build()), new BaseResponseListener<BaseObjectResponse>() {
            @Override
            public void onSuccess(BaseObjectResponse data) {
                LoaderUtils.hide();
                if (isAttached())
                    getMVPView().onPostTransactionSuccess(data);
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
    public void editTransaction(String type, Description description) {
        LoaderUtils.show(context);
        editTransactionUseCase.setType(type);
        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);
        builder.addFormDataPart("category_id", String.valueOf(description.getCategory().getId()));
        builder.addFormDataPart("date", description.getCreatedAt());
        builder.addFormDataPart("size", String.valueOf(description.getSize()));

        if (type.equals(ConstantApp.TYPESUBMIT.SALE)){
            builder.addFormDataPart("price", String.valueOf(description.getPrice()));
            builder.addFormDataPart("block", String.valueOf(description.getBlock()));

        }else {
            builder.addFormDataPart("no_of_bedrooms", String.valueOf(description.getNoOfBedRooms()));
            builder.addFormDataPart("rental_per_month", String.valueOf(description.getRentalPerMonth()));
        }

        HashMap<Integer, Object> map = new HashMap<>();
        map.put(0, description.getId());
        map.put(1, builder.build());

        requestAPI(editTransactionUseCase.request(map), new BaseResponseListener<BaseObjectResponse<MessagesApi>>() {
            @Override
            public void onSuccess(BaseObjectResponse<MessagesApi> data) {
                LoaderUtils.hide();
                if (isAttached())
                    getMVPView().onEditTransactionSuccess(data.getData().getMessage());
            }

            @Override
            public void onError(String message) {
                LoaderUtils.hide();
                if (isAttached())
                    getMVPView().onError(message);
            }
        });
    }
}
