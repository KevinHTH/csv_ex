package sg.vinova.noticeboard.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.google.firebase.iid.FirebaseInstanceId;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import sg.vinova.noticeboard.R;
import sg.vinova.noticeboard.di.module.AppModule;
import sg.vinova.noticeboard.factory.ConstantApp;
import sg.vinova.noticeboard.model.LoginResponse;
import sg.vinova.noticeboard.module.user.SignUpPresenter;
import sg.vinova.noticeboard.module.user.SignUpPresenterImpl;
import sg.vinova.noticeboard.utils.CacheUtils;
import sg.vinova.noticeboard.utils.Constant;
import sg.vinova.noticeboard.utils.DialogUtils;
import sg.vinova.noticeboard.utils.Utils;
import sg.vinova.noticeboard.widgets.AppButton;
import sg.vinova.noticeboard.widgets.AppEditText;
import sg.vinova.noticeboard.widgets.AppTextView;
import vn.eazy.core.utils.PreferencesUtils;

/**
 * Created by Jacky on 4/26/17.
 */

public class SignUpFragment extends BaseMainFragment implements SignUpPresenter.View {


    @BindView(R.id.edtName)
    AppEditText edtName;

    @BindView(R.id.edtEmail)
    AppEditText edtEmail;

    @BindView(R.id.edtPhone)
    AppEditText edtPhone;

    @BindView(R.id.edtPassword)
    AppEditText edtPassword;

    @BindView(R.id.edtRetypePassword)
    AppEditText edtRetypePassword;

    @BindView(R.id.btnSignUp)
    AppButton btnSignUp;

    @BindView(R.id.cbOne)
    CheckBox cbOne;

    @BindView(R.id.cbTwo)
    CheckBox cbTwo;

    @BindView(R.id.cbThree)
    CheckBox cbThree;

    @BindView(R.id.edtConfirmEmail)
    AppEditText edtConfirmEmail;

    @BindView(R.id.tvCheckbox1)
    AppTextView tvCheckbox1;

    @BindView(R.id.tvCheckbox2)
    AppTextView tvCheckbox2;

    @BindView(R.id.tvCheckbox3)
    TextView tvCheckbox3;

    Unbinder unbinder;

    SignUpPresenterImpl signUpPresenter;
    String deviceToken;


    public static SignUpFragment newInstance() {
        return new SignUpFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        signUpPresenter = new SignUpPresenterImpl(getContext());
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_signup;
    }

    @OnClick(R.id.btnSignUp)
    void onClickSignUp() {


        if (FirebaseInstanceId.getInstance().getToken() != null || TextUtils.isEmpty(FirebaseInstanceId.getInstance().getToken())) {
            deviceToken = FirebaseInstanceId.getInstance().getToken();
            CacheUtils.setDeviceToken(getContext(), deviceToken);
        } else {
            deviceToken = null;
        }
        if (isValidData()) {
            if (!cbOne.isChecked()) {
                DialogUtils.showDialogMessage(getContext(), getString(R.string.please_agree), null, null, "", "Ok", false, false);
                return;
            }
            if (!cbTwo.isChecked()) {
                DialogUtils.showDialogMessage(getContext(), getString(R.string.please_agree), null, null, "", "Ok", false, false);
                return;
            }
            if (!cbThree.isChecked()) {
                DialogUtils.showDialogMessage(getContext(), getString(R.string.please_agree), null, null, "", "Ok", false, false);
            } else {
                signUpPresenter.signUp(edtEmail.getText().toString(),
                        edtPassword.getText().toString(),
                        edtRetypePassword.getText().toString(),
                        edtName.getText().toString(),
                        edtPhone.getText().toString(), deviceToken, "android");
            }
        }
    }

    private boolean isValidData() {
        String error = "";
        if (TextUtils.isEmpty(edtName.getText().toString())) {
            edtName.requestFocus();
            error = getString(R.string.please_fill_this_field, "Name");
            DialogUtils.showDialogMessage(getContext(), error, null, null, "", "Ok", false, false);
            return false;
        }
        if (TextUtils.isEmpty(edtEmail.getText().toString())) {
            edtEmail.requestFocus();
            error = getString(R.string.please_fill_this_field, "Email");
            DialogUtils.showDialogMessage(getContext(), error, null, null, "", "Ok", false, false);
            return false;
        } else {
            if (!Utils.validateEmail(edtEmail.getText().toString())) {
                edtEmail.requestFocus();
                error = getString(R.string.invalid_email);
                DialogUtils.showDialogMessage(getContext(), error, null, null, "", "Ok", false, false);
                return false;
            }
        }

        if (!Utils.compareString(edtEmail.getText().toString(), edtConfirmEmail.getText().toString())) {
            edtConfirmEmail.requestFocus();
            error = getString(R.string.email_not_match);
            DialogUtils.showDialogMessage(getContext(), error, null, null, "", "Ok", false, false);
            return false;
        }
        if (TextUtils.isEmpty(edtPhone.getText().toString())) {
            edtPhone.requestFocus();
            error = getString(R.string.please_fill_this_field, "Phone");
            DialogUtils.showDialogMessage(getContext(), error, null, null, "", "Ok", false, false);
            return false;
        }

        if (TextUtils.isEmpty(edtPassword.getText().toString())) {
            edtPassword.requestFocus();
            error = getString(R.string.please_fill_this_field, "Password");
            DialogUtils.showDialogMessage(getContext(), error, null, null, "", "Ok", false, false);
            return false;
        }
        if (TextUtils.isEmpty(edtRetypePassword.getText().toString())) {
            edtRetypePassword.requestFocus();
            error = getString(R.string.please_fill_this_field, "Retype Password");
            DialogUtils.showDialogMessage(getContext(), error, null, null, "", "Ok", false, false);
            return false;
        }
        if (!Utils.compareString(edtPassword.getText().toString(), edtRetypePassword.getText().toString())) {
            edtRetypePassword.requestFocus();
            error = getString(R.string.password_not_match);
            DialogUtils.showDialogMessage(getContext(), error, null, null, "", "Ok", false, false);
            return false;
        }

        return true;
    }

    @Override
    public void onSignUpSuccess(LoginResponse loginResponse) {
        CacheUtils.saveDataUser(loginResponse);
        CacheUtils.setAuthToken(loginResponse.getApi_token());
        CacheUtils.setFirstPostItem(getContext(), true);
        CacheUtils.setVerifyPhone(getContext(), false);
        if (!loginResponse.getBulk_status().equals("approved")) {
            boolean isUpdateProfile = false;
            getFirstScreenActivity().changeFragment(VerifyPasscodeFragment.newInstance(loginResponse.getUsername(), isUpdateProfile, false), true);
        }
    }

    @Override
    public void onError(String message) {
        DialogUtils.showDialogMessage(getFirstScreenActivity(), message, null, null, "", "Ok", false, false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        signUpPresenter.bind(this);

        AppModule.setIsNeedAuthToken(false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getFirstScreenActivity().hideFooter(true);
        String clusterlName = PreferencesUtils.getString(getContext(), Constant.CLUSTER_NAME);
        String postalCode = PreferencesUtils.getString(getContext(), Constant.POSTAL_CODE);
        tvCheckbox1.setText(Html.fromHtml(getString(R.string.text_checkbox1, postalCode, clusterlName)));
        tvCheckbox2.setText(getString(R.string.text_checkbox2));
        tvCheckbox3.setText(Html.fromHtml("I agree to the " + "<b><font color=#FF0000> <a href=\"" + ConstantApp.TERM_URL + "\">Terms of Use</a></b>" + " of the App."));
        tvCheckbox3.setMovementMethod(LinkMovementMethod.getInstance());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        signUpPresenter.unbind();
    }
}
