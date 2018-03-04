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
import sg.vinova.noticeboard.module.user.ForgotPasswordPresenter;
import sg.vinova.noticeboard.module.user.ForgotPasswordPresenterImpl;
import sg.vinova.noticeboard.utils.DialogUtils;
import sg.vinova.noticeboard.utils.Utils;
import sg.vinova.noticeboard.widgets.AppButton;
import sg.vinova.noticeboard.widgets.AppEditText;

/**
 * Created by Jacky on 4/26/17.
 */

public class ForgotPasswordFragment extends BaseAppFragment implements ForgotPasswordPresenter.View {
    @BindView(R.id.edtEmail)
    AppEditText edtEmail;
    Unbinder unbinder;

    @BindView(R.id.btnResetPassword)
    AppButton btnResetPassword;

    ForgotPasswordPresenterImpl presenter;

    public static ForgotPasswordFragment newInstance() {
        return new ForgotPasswordFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new ForgotPasswordPresenterImpl(getContext());
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_forgot_password;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        presenter.bind(this);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.btnResetPassword)
    public void onClickResetPassword() {
        if (!TextUtils.isEmpty(edtEmail.getText().toString())) {
            if (Utils.validateEmail(edtEmail.getText().toString())) {
                presenter.forgotPassword(edtEmail.getText().toString());
            } else {
                DialogUtils.showDialogMessage(getContext(), getString(R.string.invalid_email), null, null, "", "OK", false, false);
            }

        } else {
            edtEmail.requestFocus();
            DialogUtils.showDialogMessage(getContext(), getString(R.string.please_fill_this_field, getString(R.string.profile_email)), null, null, "", "OK", false, false);
        }
    }

    @Override
    public void onError(String message) {
        DialogUtils.showDialogMessage(getContext(), message, null, null, "", "OK", false, false);
    }

    @Override
    public void onForgotPasswordSuccess(String message) {
        DialogUtils.showDialogMessage(getContext(), message, null, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getBaseAppActivity().onBackPressed();
            }
        }, "", "OK", false, false);
    }
}
