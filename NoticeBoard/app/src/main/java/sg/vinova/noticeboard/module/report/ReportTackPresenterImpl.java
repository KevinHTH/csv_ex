package sg.vinova.noticeboard.module.report;

import android.content.Context;

import sg.vinova.noticeboard.base.BaseAppPresenter;
import sg.vinova.noticeboard.base.BaseObjectResponse;
import sg.vinova.noticeboard.base.BaseResponseListener;
import sg.vinova.noticeboard.model.MessageResponse;
import sg.vinova.noticeboard.usecase.ReportTackUseCase;

/**
 * Created by Ray on 7/17/17.
 */

public class ReportTackPresenterImpl extends BaseAppPresenter<ReportTackPresenter.View>
        implements ReportTackPresenter.Presenter {

    private ReportTackUseCase reportTackUseCase;

    public ReportTackPresenterImpl(Context context) {
        super(context);
        reportTackUseCase = new ReportTackUseCase(context);
    }

    @Override
    public void report(String cateId, String itemId, String des) {
        requestAPI(reportTackUseCase.request(cateId, itemId, des), new BaseResponseListener<BaseObjectResponse<MessageResponse>>() {
            @Override
            public void onSuccess(BaseObjectResponse<MessageResponse> data) {
                if (!isAttached())
                    return;
                getMVPView().reportSuccess(data.getData().getMessage());
            }

            @Override
            public void onError(String message) {
                if (!isAttached())
                    return;
                getMVPView().onError(message);
            }
        });
    }
}
