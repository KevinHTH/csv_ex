package sg.vinova.noticeboard.study;

import android.text.TextUtils;

import com.jakewharton.retrofit2.adapter.rxjava2.HttpException;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import sg.vinova.noticeboard.utils.ApiUtils;
import timber.log.Timber;

/**
 * Created by Ray on 3/6/17.
 */

public abstract class ApiErrorListener implements Consumer<Throwable> {
    @Override
    public void accept(@NonNull Throwable e) throws Exception {
        String errorMessage = "";
        retrofit2.Response response =null;
        try{
          response = ((HttpException) e).response();

            if (response != null && response.errorBody() != null) {
                if (response.code() >400){
                    String msg = ApiUtils.getErrorMessage(response.errorBody().string());
                    if (!TextUtils.isEmpty(msg)){
                        errorMessage = msg;
                    }
                    if (msg == null)
                        errorMessage = response.code() + " Error. Please try again.";
                    onApiErrorMessage(errorMessage);

                }
            }

        }catch (Exception ex){
            Timber.e(ex.getMessage());
            onApiErrorMessage(errorMessage);
        }

    }

    public abstract void onApiErrorMessage(String messageDetail);
}
