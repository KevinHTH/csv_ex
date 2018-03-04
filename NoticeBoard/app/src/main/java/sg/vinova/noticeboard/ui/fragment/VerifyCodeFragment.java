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
import sg.vinova.noticeboard.model.ClusterResponse;
import sg.vinova.noticeboard.module.postcode.PostCodePresenter;
import sg.vinova.noticeboard.module.postcode.PostCodePresenterImpl;
import sg.vinova.noticeboard.utils.CacheUtils;
import sg.vinova.noticeboard.utils.Constant;
import sg.vinova.noticeboard.utils.SnackBarUtils;
import sg.vinova.noticeboard.widgets.AppButton;
import sg.vinova.noticeboard.widgets.AppEditText;
import vn.eazy.core.utils.PreferencesUtils;

/**
 * Created by Jacky on 4/26/17.
 */

public class VerifyCodeFragment extends BaseMainFragment implements PostCodePresenter.View {
    @BindView(R.id.edtPostcode)
    AppEditText edtPostcode;
    @BindView(R.id.edtPassword)
    AppEditText edtPassword;
    @BindView(R.id.btnContinue)
    AppButton btnContinue;
    Unbinder unbinder;
    private PostCodePresenterImpl presenter;

    public static VerifyCodeFragment newInstance() {
        VerifyCodeFragment fragment = new VerifyCodeFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new PostCodePresenterImpl(getContext());
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_verivy_code;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        presenter.bind(this);
        getFirstScreenActivity().hideFooter(true);
        getFirstScreenActivity().hideToolbar(false);
        setTitleToolbar(getString(R.string.title_app2));
        getFirstScreenActivity().getMainToolbarHelper()
                .showBackButton(true, () -> getFirstScreenActivity().onBackPressed());
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.btnContinue)
    void onClickContinue() {
        if (isValidData()) {
            // AppModule.setIsNeedAuthToken(false);
            presenter.postCode(edtPostcode.getText().toString(), edtPassword.getText().toString());
        }
    }

    @Override
    public void onError(String message) {
        SnackBarUtils.getSnackInstance().showError(getView(), message);

    }

    @Override
    public void onPostcodeSuccess(ClusterResponse clusterResponse) {
        CacheUtils.setClusterToken(getContext(), clusterResponse.getApiToken());

        PreferencesUtils.putString(getContext(), Constant.CLUSTER_NAME, clusterResponse.getCluster().getName());
        PreferencesUtils.putString(getContext(), Constant.POSTAL_CODE, clusterResponse.getCode());

        String footerText = getString(R.string.footer_text);
        if (clusterResponse.getCluster().getClusterAdmin() != null) {
            String clusterAdminName = clusterResponse.getCluster().getClusterAdmin().getUsername();
            String clusterAdminPhone = clusterResponse.getCluster().getClusterAdmin().getPhoneNumber();
            String clusterCompanyName = clusterResponse.getCluster().getClusterAdmin().getCompany();
            String ceaNo = clusterResponse.getCluster().getClusterAdmin().getCea_license_number();
            String ceaReg = clusterResponse.getCluster().getClusterAdmin().getCea_registration_number();
            footerText = getString(R.string.footer_text) + " " + clusterAdminName + " " + clusterAdminPhone
                    + " " + clusterCompanyName + "(CEA No.:" + ceaNo + "/" + ceaReg + ")";
            String urlPhoto = clusterResponse.getCluster().getClusterAdmin().getPhoto_url();
            CacheUtils.setFooterPhoto(getContext(), urlPhoto);
        }

        PreferencesUtils.putString(getContext(), Constant.FOOTER_TEXT, footerText);

        getFirstScreenActivity().setFooterText(footerText);
        getFirstScreenActivity().changeFragment(LoginSignUpFragment.newInstance(), true);
    }

    private boolean isValidData() {
        if (TextUtils.isEmpty(edtPostcode.getText().toString())) {
            SnackBarUtils.getSnackInstance().showSuccess(btnContinue, getString(R.string.please_fill_this_field, "The Postcode"));
            return false;
        }

        if (TextUtils.isEmpty(edtPassword.getText().toString())) {
            SnackBarUtils.getSnackInstance().showSuccess(getView(), getString(R.string.please_fill_this_field, "The Password"));
            return false;
        }

        return true;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.unbind();
    }
}
