package sg.vinova.noticeboard.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import sg.vinova.noticeboard.R;
import sg.vinova.noticeboard.model.LoginResponse;
import sg.vinova.noticeboard.module.user.ProfilePresenter;
import sg.vinova.noticeboard.module.user.ProfilePresenterImpl;
import sg.vinova.noticeboard.utils.CacheUtils;
import sg.vinova.noticeboard.utils.Constant;
import sg.vinova.noticeboard.utils.DialogUtils;
import sg.vinova.noticeboard.utils.SnackBarUtils;
import sg.vinova.noticeboard.utils.Utils;
import sg.vinova.noticeboard.widgets.AppEditText;
import vn.eazy.core.utils.PreferencesUtils;


/**
 * Created by Jacky on 4/26/17.
 */

public class ProfileFragment extends BaseMainFragment implements ProfilePresenter.View {
    @BindView(R.id.edtName)
    AppEditText edtName;
    @BindView(R.id.edtEmail)
    AppEditText edtEmail;
    @BindView(R.id.edtPhone)
    AppEditText edtPhone;
    Unbinder unbinder;

    ProfilePresenterImpl presenter;


    public static ProfileFragment newInstance() {
        ProfileFragment fragment = new ProfileFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new ProfilePresenterImpl(getContext());
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_profile;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        presenter.bind(this);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupActionbar();
        getMainActivity().hideFooter(false);
        getMainActivity().setFooterText(PreferencesUtils.getString(getContext(), Constant.FOOTER_TEXT));
        //showProgressPage(true);
        presenter.getUser();
    }

    private void setUpUi(LoginResponse user) {
        edtName.setText(user.getUsername());
        edtEmail.setText(user.getEmail());
        edtPhone.setText(user.getPhoneNumber());
    }

    private void setupActionbar() {
        setTitleToolbar(getString(R.string.my_profile));
        getMainActivity().getMainToolbarHelper().showToolbar(true);
        getMainActivity().getMainToolbarHelper().setImageForLeftButton(R.mipmap.back);
        getMainActivity().getMainToolbarHelper().setRightToolbarButton("SAVE", v -> {
            if (isValid()) {
                presenter.updateProfile(edtName.getText().toString(), edtPhone.getText().toString());
            }
        });
    }

    private boolean isValid() {
        if (Utils.isEmpty(edtName.getText().toString())) {
            edtName.requestFocus();
            DialogUtils.showDialogMessage(getContext(), getString(R.string.please_fill_this_field, "Name"), null, null, "", "Ok", false, false);
            return false;
        }
        if (Utils.isEmpty(edtPhone.getText().toString())) {
            edtPhone.requestFocus();
            DialogUtils.showDialogMessage(getContext(), getString(R.string.please_fill_this_field, "Phone Number"), null, null, "", "Ok", false, false);
            return false;
        }
        return true;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        presenter.unbind();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getMainActivity().getMainToolbarHelper().setRightToolbarButton(R.mipmap.tackit_logo_small, null);
    }

    @Override
    public void onError(String message) {
        //showProgressPage(false);
        SnackBarUtils.getSnackInstance().showSuccess(rootView, message);
    }

    @Override
    public void onGetUserSuccess(LoginResponse loginResponse) {
        //showProgressPage(false);
        setUpUi(loginResponse);
    }

    @Override
    public void onUpdateProfileSuccess(LoginResponse loginResponse) {
        CacheUtils.saveDataUser(loginResponse);
        if (!loginResponse.getBulk_status().equals("approved")) {
            CacheUtils.setClusterToken(getContext(), "");
            boolean isUpdateProfile = true;
            getMainActivity().changeFragment(VerifyPasscodeFragment.newInstance(loginResponse.getUsername(), isUpdateProfile, false), false);
        } else {
            SnackBarUtils.getSnackInstance().showSuccess(rootView, getString(R.string.upload_profile_success));
            getMainActivity().onBackPressed();
        }
    }
}