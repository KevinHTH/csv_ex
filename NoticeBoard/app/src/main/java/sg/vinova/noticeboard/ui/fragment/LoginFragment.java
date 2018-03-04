package sg.vinova.noticeboard.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.iid.FirebaseInstanceId;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import sg.vinova.noticeboard.R;
import sg.vinova.noticeboard.di.module.AppModule;
import sg.vinova.noticeboard.model.LoginResponse;
import sg.vinova.noticeboard.module.user.LoginPresenter;
import sg.vinova.noticeboard.module.user.LoginPresenterImpl;
import sg.vinova.noticeboard.ui.activity.MainActivity;
import sg.vinova.noticeboard.utils.CacheUtils;
import sg.vinova.noticeboard.utils.DialogUtils;
import sg.vinova.noticeboard.utils.Utils;
import sg.vinova.noticeboard.widgets.AppButton;
import sg.vinova.noticeboard.widgets.AppEditText;
import sg.vinova.noticeboard.widgets.AppTextView;

/**
 * Created by Jacky on 4/26/17.
 */

public class LoginFragment extends BaseMainFragment implements LoginPresenter.View {
    @BindView(R.id.edtEmail)
    AppEditText edtEmail;
    @BindView(R.id.edtPassword)
    AppEditText edtPassword;
    @BindView(R.id.btnLogin)
    AppButton btnLogin;
    @BindView(R.id.tvForgotPassword)
    AppTextView tvForgotPassword;
    Unbinder unbinder;

    private String deviceToken;
    private LoginPresenterImpl presenter;

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new LoginPresenterImpl(getContext());
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_login;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        presenter.bind(this);
        getFirstScreenActivity().hideFooter(true);
        getFirstScreenActivity().getMainToolbarHelper().showBackButton(true, () -> {
            if (getFirstScreenActivity() != null) {
                getFirstScreenActivity().getMainToolbarHelper().showBackButton(false);
                getFirstScreenActivity().onBackPressed();
            }
        });
        getFirstScreenActivity().getMainToolbarHelper().setImageForLeftButton(R.mipmap.back);
        if (!TextUtils.isEmpty(CacheUtils.getFooterPhoto(getContext()))) {
            getFirstScreenActivity().setPhotoFooter(CacheUtils.getFooterPhoto(getContext()));
        }
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        presenter.unbind();
    }

    @OnClick(R.id.btnLogin)
    void onClickLogin() {
        if (FirebaseInstanceId.getInstance().getToken() != null || TextUtils.isEmpty(FirebaseInstanceId.getInstance().getToken())) {
            deviceToken = FirebaseInstanceId.getInstance().getToken();
            CacheUtils.setDeviceToken(getContext(), deviceToken);
        } else {
            deviceToken = null;
        }

        if (isValidData()) {
            AppModule.setIsNeedAuthToken(false);
            presenter.login(edtEmail.getText().toString(),
                    edtPassword.getText().toString(), deviceToken, "android");
        }
    }


    @OnClick(R.id.tvForgotPassword)
    void onClickForgotPassword() {
        getFirstScreenActivity().changeFragment(new ForgotPasswordFragment(), true);
    }


    private boolean isValidData() {
        if (TextUtils.isEmpty(edtEmail.getText().toString())) {
            edtEmail.requestFocus();
            DialogUtils.showDialogMessage(getContext(), getString(R.string.please_fill_this_field, "Email"), null, null, "", "Ok", false, false);
            return false;
        } else {
            if (!Utils.validateEmail(edtEmail.getText().toString())) {
                edtEmail.requestFocus();
                DialogUtils.showDialogMessage(getContext(), getString(R.string.invalid_email), null, null, "", "Ok", false, false);
                return false;
            }
        }
        if (TextUtils.isEmpty(edtPassword.getText().toString())) {
            edtPassword.requestFocus();
            DialogUtils.showDialogMessage(getContext(), getString(R.string.please_fill_this_field, "Password"), null, null, "", "Ok", false, false);
            return false;
        }
        return true;
    }

    @Override
    public void onError(String message) {
        DialogUtils.showDialogMessage(getFirstScreenActivity(), message, null, null, "", "Ok", false, false);
    }

    @Override
    public void onLoginSuccess(LoginResponse loginResponse) {
        CacheUtils.setAuthToken(getContext(), loginResponse.getApi_token());
        CacheUtils.saveDataUser(loginResponse);
        CacheUtils.setFirstPostItem(getContext(), true);

        if (loginResponse.getBulk_status().equals("approved")) {
            CacheUtils.setVerifyPhone(getContext(), true);

            getFirstScreenActivity().overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            startActivity(new Intent(getContext(), MainActivity.class));
            getFirstScreenActivity().finish();
        } else {
            CacheUtils.setVerifyPhone(getContext(), false);
            boolean isUpdateProfile = false;
            getFirstScreenActivity().changeFragment(VerifyPasscodeFragment.newInstance(loginResponse.getUsername(), isUpdateProfile, true), true);
        }
    }
}
