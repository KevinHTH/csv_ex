package sg.vinova.noticeboard.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import sg.vinova.noticeboard.R;
import sg.vinova.noticeboard.di.module.AppModule;
import sg.vinova.noticeboard.model.LoginResponse;
import sg.vinova.noticeboard.module.user.PhoneVerifyPresenter;
import sg.vinova.noticeboard.module.user.PhoneVerifyPresenterImpl;
import sg.vinova.noticeboard.ui.activity.FirstScreenActivity;
import sg.vinova.noticeboard.ui.activity.MainActivity;
import sg.vinova.noticeboard.utils.CacheUtils;
import sg.vinova.noticeboard.utils.DialogUtils;
import sg.vinova.noticeboard.utils.Log;
import sg.vinova.noticeboard.utils.SnackBarUtils;
import sg.vinova.noticeboard.widgets.AppButton;
import sg.vinova.noticeboard.widgets.AppEditText;
import sg.vinova.noticeboard.widgets.AppTextView;
import vn.eazy.core.toolbar.OnCallBackToolbarAction;

/**
 * Created by Jacky on 4/26/17.
 */

public class VerifyPasscodeFragment extends BaseMainFragment implements TextWatcher, View.OnKeyListener,
        PhoneVerifyPresenter.View {
    @BindView(R.id.edBox1)
    AppEditText edBox1;
    @BindView(R.id.edBox2)
    AppEditText edBox2;
    @BindView(R.id.edBox3)
    AppEditText edBox3;
    @BindView(R.id.edBox4)
    AppEditText edBox4;
    @BindView(R.id.edBox5)
    AppEditText edBox5;
    @BindView(R.id.tvNotifyError)
    AppTextView tvNotifyError;
    @BindView(R.id.btnSendPassCode)
    AppButton btnSendPassCode;
    View rootView;
    @BindView(R.id.linearFrame)
    LinearLayout linearFrame;
    @BindView(R.id.tvChangeNumber)
    AppTextView tvChangeNumber;

    private int whoHasFocus = 0;
    private final int TIME_COUNTDOWN = 10;

    private PhoneVerifyPresenterImpl phonePresenter;
    private String passCode, username;
    private boolean isUpdateProfile;
    private boolean needResend;
    private int resend;
    private boolean isLogin;


    Unbinder unbinder;
    private Disposable countObserable;

    public static VerifyPasscodeFragment newInstance(String username, boolean isUpdateProfile, boolean isLogin) {
        VerifyPasscodeFragment fragment = new VerifyPasscodeFragment();
        fragment.isUpdateProfile = isUpdateProfile;
        fragment.username = username;
        fragment.isLogin = isLogin;
        return fragment;
    }

    public static VerifyPasscodeFragment newInstance() {
        VerifyPasscodeFragment fragment = new VerifyPasscodeFragment();
        fragment.isLogin = false;
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppModule.setIsNeedAuthToken(true);
        resend = 0;
        phonePresenter = new PhoneVerifyPresenterImpl(getBaseAppActivity());
        CacheUtils.setVerifyPhone(getContext(), false);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_verify_passcode;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        phonePresenter.bind(this);
        needResend = false;

        edBox1.addTextChangedListener(this);
        edBox2.addTextChangedListener(this);
        edBox3.addTextChangedListener(this);
        edBox4.addTextChangedListener(this);
        edBox5.addTextChangedListener(this);

        edBox1.setOnKeyListener(this);
        edBox2.setOnKeyListener(this);
        edBox3.setOnKeyListener(this);
        edBox4.setOnKeyListener(this);
        edBox5.setOnKeyListener(this);

        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        phonePresenter.unbind();
        if (countObserable != null && !countObserable.isDisposed()) {
            countObserable.dispose();
        }
        if (isUpdateProfile) {
            getMainActivity().getMainToolbarHelper().showBackButton(false);
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (isUpdateProfile) {
            getMainActivity().getMainToolbarHelper().setTitle("Verify Pass Code");
            getMainActivity().getMainToolbarHelper().setImageForLeftButton(R.mipmap.back);
            getMainActivity().getMainToolbarHelper().showBackButton(true, new OnCallBackToolbarAction() {
                @Override
                public void onCallBackToolbar() {
                    getMainActivity().overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    startActivity(new Intent(getContext(), FirstScreenActivity.class));
                    getMainActivity().finish();
                }
            });
            getMainActivity().getMainToolbarHelper().setRightToolbarButton(R.mipmap.tackit_logo_small, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //
                }
            });

            if (CacheUtils.getDataUser().getPhoneNumber() != null) {
                DialogUtils.showDialogMessage(getContext(), "Your pass code will be send to " + CacheUtils.getDataUser().getPhoneNumber()
                        , null, null, "", "Ok", false, false);
            }

        } else {
            if (isLogin) {
                phonePresenter.resendCode();

            }

            getFirstScreenActivity().getMainToolbarHelper().setTitle("Verify Pass Code");
            getFirstScreenActivity().getMainToolbarHelper().setImageForLeftButton(R.mipmap.back);
            getFirstScreenActivity().getMainToolbarHelper().showBackButton(true, () -> getFirstScreenActivity().onBackPressed());
            getFirstScreenActivity().getMainToolbarHelper().setRightToolbarButton(R.mipmap.tackit_logo_small, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //
                }
            });
        }

        countDownTime(TIME_COUNTDOWN);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (rootView != null) {
            int v;
            if (linearFrame.getFocusedChild() != null) {
                v = linearFrame.getFocusedChild().getId();
            } else {
                v = edBox1.getId();
            }

            if (v == edBox1.getId()) {
                requestFocus(edBox1, edBox2);
            } else if (v == edBox2.getId()) {
                requestFocus(edBox2, edBox3);
            } else if (v == edBox3.getId()) {
                requestFocus(edBox3, edBox4);
            } else if (v == edBox4.getId()) {
                requestFocus(edBox4, edBox5);
            } else {
                if (verifyTextBox()) {
                    passCode = edBox1.getText().toString() + edBox2.getText().toString() + edBox3.getText().toString()
                            + edBox4.getText().toString() + edBox5.getText().toString();

                    phonePresenter.verifyPhone(passCode);
                }

            }
        }
    }

    private void requestFocus(AppEditText editText, AppEditText nextEditText) {
        if (editText.getText().length() == 1) {
            nextEditText.requestFocus();
        }
    }

    private void countDownTime(int timeCount) {

        countObserable = Observable.interval(1, TimeUnit.SECONDS).take(timeCount)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aLong -> {
                    Log.i("Count", String.valueOf(60 - aLong.intValue()));
                    btnSendPassCode.setClickable(false);
                    btnSendPassCode.setAlpha(0.3f);
                    btnSendPassCode.setText("Resend (" + String.valueOf(timeCount - aLong.intValue()) + ")");
                }, throwable -> {

                }, () -> {
                    btnSendPassCode.setClickable(true);
                    btnSendPassCode.setAlpha(1);
                    needResend = true;
                    btnSendPassCode.setText("Resend");
                });

    }

    @OnClick(R.id.btnSendPassCode)
    public void onClickSendPasscode() {
        phonePresenter.resendCode();
    }

    private boolean verifyTextBox() {
        if (TextUtils.isEmpty(edBox1.getText().toString())) {
            edBox1.requestFocus();
            DialogUtils.showDialogMessage(getContext(), "Please fill pass code", null, null, "", "Ok", false, false);
            return false;
        }
        if (TextUtils.isEmpty(edBox2.getText().toString())) {
            edBox2.requestFocus();
            DialogUtils.showDialogMessage(getContext(), "Please fill pass code", null, null, "", "Ok", false, false);
            return false;
        }
        if (TextUtils.isEmpty(edBox3.getText().toString())) {
            edBox3.requestFocus();
            DialogUtils.showDialogMessage(getContext(), "Please fill pass code", null, null, "", "Ok", false, false);
            return false;
        }
        if (TextUtils.isEmpty(edBox4.getText().toString())) {
            edBox4.requestFocus();
            DialogUtils.showDialogMessage(getContext(), "Please fill pass code", null, null, "", "Ok", false, false);
            return false;
        }
        if (TextUtils.isEmpty(edBox5.getText().toString())) {
            edBox5.requestFocus();
            DialogUtils.showDialogMessage(getContext(), "Please fill pass code", null, null, "", "Ok", false, false);
            return false;
        }
        return true;
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        int currentFocus = linearFrame.getFocusedChild().getId();
        if (keyCode == KeyEvent.KEYCODE_DEL) {
            if (currentFocus == edBox5.getId()) {
                edBox4.requestFocus();
            } else if (currentFocus == edBox4.getId()) {
                edBox3.requestFocus();
            } else if (currentFocus == edBox3.getId()) {
                edBox2.requestFocus();
            } else {
                edBox1.requestFocus();
            }
        }

        return false;
    }

    @Override
    public void onError(String message) {
        tvNotifyError.setVisibility(View.VISIBLE);
        tvNotifyError.setText(message);
        needResend = true;
        resend++;
        CacheUtils.setVerifyPhone(getContext(), false);

    }

    @Override
    public void onPhoneConfirmSuccess(LoginResponse loginResponse) {
        tvNotifyError.setVisibility(View.GONE);
        SnackBarUtils.getSnackInstance().showSuccess(rootView, "verify successfully!");
        CacheUtils.saveDataUser(loginResponse);
        CacheUtils.setFirstPostItem(getContext(), true);
        CacheUtils.setVerifyPhone(getContext(), true);
        if (isUpdateProfile) {
            getMainActivity().changeFragment(HomeFragment.newInstance(), false);
        } else {
            getFirstScreenActivity().overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            startActivity(new Intent(getContext(), MainActivity.class));
            getFirstScreenActivity().finish();
        }
    }

    @Override
    public void onResendCodeSuccess(String msg) {
        if (CacheUtils.getDataUser().getPhoneNumber() != null) {
            DialogUtils.showDialogMessage(getContext(), "Your pass code will be send to "
                            + CacheUtils.getDataUser().getPhoneNumber()
                    , null, null, "", "Ok", false, false);
        }

        edBox1.setText("");
        edBox2.setText("");
        edBox3.setText("");
        edBox4.setText("");
        edBox5.setText("");
        edBox1.requestFocus();
    }

    @OnClick(R.id.tvChangeNumber)
    public void changeNumber() {
        if (isUpdateProfile) {
            getMainActivity().changeFragment(ChangePhoneFragment.newInstance(username, isUpdateProfile), true);
        } else {
            getFirstScreenActivity().changeFragment(ChangePhoneFragment.newInstance(username, isUpdateProfile), true);
        }

    }

}
