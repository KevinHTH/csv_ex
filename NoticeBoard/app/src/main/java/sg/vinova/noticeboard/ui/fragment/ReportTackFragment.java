package sg.vinova.noticeboard.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import sg.vinova.noticeboard.R;
import sg.vinova.noticeboard.model.Description;
import sg.vinova.noticeboard.module.report.ReportTackPresenter;
import sg.vinova.noticeboard.module.report.ReportTackPresenterImpl;
import sg.vinova.noticeboard.utils.SnackBarUtils;
import sg.vinova.noticeboard.widgets.AppEditText;
import vn.eazy.architect.mvp.base.BasePresenter;

/**
 * Created by Ray on 7/17/17.
 */

public class ReportTackFragment extends BaseMainFragment implements ReportTackPresenter.View {

    @BindView(R.id.edtDescription)
    AppEditText edtDescription;
    @BindView(R.id.tvSend)
    TextView tvSend;

    Unbinder unbinder;
    private ReportTackPresenterImpl presenter;
    private Description description;

    public static ReportTackFragment newInstance(Description description) {
        ReportTackFragment fragment = new ReportTackFragment();
        fragment.description = description;
        return fragment;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_report_tack;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new ReportTackPresenterImpl(getContext());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, view);
        presenter.bind(this);
        getMainActivity().hideFooter(true);
        getMainActivity().getMainToolbarHelper().setRightToolbarButton(0, null);
        setupActionbar();
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @OnClick(R.id.tvSend)
    void onSend() {
        if (edtDescription.getText().toString().equals("")) {
            SnackBarUtils.getSnackInstance().showError(getView(), "Please fill in the reason.");
            return;
        }
        showProgressForView(true);
        presenter.report(description.getCategory().getId() + "", description.getId() + "", edtDescription.getText().toString());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        presenter.unbind();
    }

    @Override
    public void onError(String message) {
        showProgressForView(false);
        SnackBarUtils.getSnackInstance().showError(getView(), message);
    }

    @Override
    public void reportSuccess(String msg) {
        showProgressForView(false);
        SnackBarUtils.getSnackInstance().showError(getView(), msg);
        getMainActivity().onBackPressed();
    }

    private void setupActionbar() {
        setTitleToolbar("Report Tack");
        try {
            getBaseAppActivity().getToolbarHelper().setRightToolbarButton(null, null);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
