package sg.vinova.noticeboard.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import sg.vinova.noticeboard.R;
import sg.vinova.noticeboard.module.user.ChangePasswordPresenter;
import sg.vinova.noticeboard.module.user.ChangePasswordPresenterImpl;
import sg.vinova.noticeboard.utils.DialogUtils;
import sg.vinova.noticeboard.utils.SnackBarUtils;
import sg.vinova.noticeboard.widgets.AppButton;
import sg.vinova.noticeboard.widgets.AppEditText;

/**
 * Created by cuong on 4/26/17.
 */

public class ChangePasswordFragment extends BaseMainFragment implements ChangePasswordPresenter.View {


    @BindView(R.id.edtOldPass)
    AppEditText edtOldPass;
    @BindView(R.id.edtNewPass)
    AppEditText edtNewPass;
    @BindView(R.id.edtNewPassConfirm)
    AppEditText edtNewPassConfirm;
    @BindView(R.id.btnConfirmChange)
    AppButton btnConfirmChange;
    Unbinder unbinder;

    ChangePasswordPresenterImpl presenter;

    public static ChangePasswordFragment newInstance() {
        return new ChangePasswordFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new ChangePasswordPresenterImpl(getContext());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        presenter.bind(this);
        setupActionbar();
        getMainActivity().hideFooter(true);
        return rootView;
    }

    private void setupActionbar() {
        setTitleToolbar(getString(R.string.change_password));
        try {
            getMainActivity().getToolbarHelper().showToolbar(true);
            getMainActivity().getToolbarHelper().setImageForLeftButton(R.mipmap.back);
            getMainActivity().getToolbarHelper().setRightToolbarButton(null, null);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

    }

    private boolean isValidData() {
        if (TextUtils.isEmpty(edtOldPass.getText().toString())) {
            edtOldPass.requestFocus();
            DialogUtils.showDialogMessage(getContext(), getString(R.string.please_fill_this_field, getString(R.string.old_password)), null, null, "", "Ok", false, false);
            return false;
        }

        if (TextUtils.isEmpty(edtNewPass.getText().toString())) {
            edtNewPass.requestFocus();
            DialogUtils.showDialogMessage(getContext(), getString(R.string.please_fill_this_field, getString(R.string.new_password)), null, null, "", "Ok", false, false);
            return false;
        }
        if (TextUtils.isEmpty(edtNewPassConfirm.getText().toString())) {
            edtNewPassConfirm.requestFocus();
            DialogUtils.showDialogMessage(getContext(), getString(R.string.please_fill_this_field, getString(R.string.confirm_new_password)), null, null, "", "Ok", false, false);
            return false;
        }

        return true;
    }

    @OnClick(R.id.btnConfirmChange)
    public void onClickConfirmChange() {
        if (isValidData()) {
            presenter.changePassword(edtOldPass.getText().toString(), edtNewPass.getText().toString(), edtNewPassConfirm.getText().toString());
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_change_password;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        presenter.unbind();
    }

    @Override
    public void onError(String message) {
        SnackBarUtils.getSnackInstance().showError(getView(), message);
    }

    @Override
    public void onChangePasswordSuccess(String message) {
        SnackBarUtils.getSnackInstance().showSuccess(getView(), message);
        getBaseAppActivity().onBackPressed();
    }
}
