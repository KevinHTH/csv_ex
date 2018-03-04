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
import sg.vinova.noticeboard.model.LoginResponse;
import sg.vinova.noticeboard.module.user.ProfilePresenter;
import sg.vinova.noticeboard.module.user.ProfilePresenterImpl;
import sg.vinova.noticeboard.utils.CacheUtils;
import sg.vinova.noticeboard.utils.DialogUtils;
import sg.vinova.noticeboard.widgets.AppButton;
import sg.vinova.noticeboard.widgets.AppEditText;

/**
 * Created by Jacky on 4/26/17.
 */

public class ChangePhoneFragment extends BaseMainFragment implements ProfilePresenter.View {


    @BindView(R.id.edPhoneNumber)
    AppEditText edPhoneNumber;
    @BindView(R.id.btnChangePhone)
    AppButton btnChangePhone;

    Unbinder unbinder;

    ProfilePresenterImpl presenter;
    private String username;
    private boolean isUpdateProfile;


    public static ChangePhoneFragment newInstance(String username, boolean isUpdateProfile) {
        ChangePhoneFragment fragment = new ChangePhoneFragment();
        fragment.username = username;
        fragment.isUpdateProfile = isUpdateProfile;
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new ProfilePresenterImpl(getContext());
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_change_number;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        presenter.bind(this);
        if (isUpdateProfile) {
            getMainActivity().getMainToolbarHelper().showBackButton(true, () -> getMainActivity().onBackPressed());
            getMainActivity().getMainToolbarHelper().setTitle(getString(R.string.change_phone));
        } else {
            getFirstScreenActivity().getMainToolbarHelper().setTitle(getString(R.string.change_phone));
        }

        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.btnChangePhone)
    public void onClickChangePhone() {
        if (TextUtils.isEmpty(edPhoneNumber.getText().toString())) {
            DialogUtils.showDialogMessage(getContext(), "Please fill your phone number!", null, null, "", "OK", false, false);
            return;
        }
        presenter.updateProfile(username, edPhoneNumber.getText().toString());
    }


    @Override
    public void onError(String message) {
        DialogUtils.showDialogMessage(getContext(), message, null, null, "", "OK", false, false);
    }

    @Override
    public void onGetUserSuccess(LoginResponse loginResponse) {

    }

    @Override
    public void onUpdateProfileSuccess(LoginResponse loginResponse) {
        CacheUtils.saveDataUser(loginResponse);

        if (isUpdateProfile) {
            getMainActivity().onBackPressed();
        } else {
            getFirstScreenActivity().onBackPressed();
        }
    }
}
