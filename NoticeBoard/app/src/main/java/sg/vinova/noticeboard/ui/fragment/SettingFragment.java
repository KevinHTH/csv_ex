package sg.vinova.noticeboard.ui.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import sg.vinova.noticeboard.R;
import sg.vinova.noticeboard.di.module.AppModule;
import sg.vinova.noticeboard.factory.ConstantApp;
import sg.vinova.noticeboard.model.BlockedUsers;
import sg.vinova.noticeboard.ui.activity.FirstScreenActivity;
import sg.vinova.noticeboard.utils.CacheUtils;
import sg.vinova.noticeboard.utils.DialogUtils;
import sg.vinova.noticeboard.widgets.AppDialog;
import sg.vinova.noticeboard.widgets.AppTextView;

/**
 * Created by Jacky on 4/26/17.
 */

public class SettingFragment extends BaseMainFragment implements AppDialog.OnClickAppDialog {

    @BindView(R.id.lnEditProfile)
    LinearLayout lnEditProfile;
    @BindView(R.id.lnChangePass)
    LinearLayout lnChangePass;
    @BindView(R.id.lnGiveFeedback)
    LinearLayout lnGiveFeedback;
    @BindView(R.id.lnTerm)
    LinearLayout lnTerm;
    @BindView(R.id.lnLogout)
    LinearLayout lnLogout;
    @BindView(R.id.tvVersion)
    AppTextView tvVersion;
    Unbinder unbinder;
    @BindView(R.id.lnAboutUs)
    LinearLayout lnAboutUs;
    @BindView(R.id.lnBlockedUsers)
    LinearLayout lnBlockedUsers;

    public static SettingFragment newInstance() {
        return new SettingFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_setting;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupActionbar();
        getMainActivity().hideFooter(false);
        // getMainActivity().setFooterText(PreferencesUtils.getString(getContext(), Constant.FOOTER_TEXT));
    }

    private void setupActionbar() {
        setTitleToolbar(getString(R.string.setting));
        try {
            getBaseAppActivity().getToolbarHelper().setRightToolbarButton(null, null);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.lnEditProfile)
    public void onClickEditProfile() {
        getBaseAppActivity().changeFragment(ProfileFragment.newInstance(), true);
    }

    @OnClick(R.id.lnChangePass)
    public void onClickChangePass() {
        getBaseAppActivity().changeFragment(ChangePasswordFragment.newInstance(), true);
    }

    @OnClick(R.id.lnGiveFeedback)
    public void onClickGiveFeedback() {
        getBaseAppActivity().changeFragment(NewFeedbackFragment.newInstance(ConstantApp.FEEDBACK.FEEDBACK_APP), true);
    }

    @OnClick(R.id.lnAboutUs)
    public void onClickAboutUs() {
        getBaseAppActivity().changeFragment(AboutUsFragment.newInstance(), true);
    }

    @OnClick(R.id.lnTerm)
    public void onClickTerm() {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(ConstantApp.TERM_URL));
        startActivity(browserIntent);
    }

    @OnClick(R.id.lnLogout)
    public void onClickLogout() {
        DialogUtils.showDialogMessage(getContext(), getString(R.string.do_you_want_to_logout), null, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppModule.setIsNeedAuthToken(false);
                CacheUtils.clearLoggedInData();
                CacheUtils.setAuthToken(getContext(), "");
                CacheUtils.setFirstPostItem(getContext(), true);
              /*  Intent intent = new Intent(getContext(), EnterMyPostcodeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                getBaseAppActivity().overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                getBaseAppActivity().finish();
                startActivity(intent);*/

                getBaseAppActivity().overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                startActivity(new Intent(getContext(), FirstScreenActivity.class));
                getBaseAppActivity().finish();
            }
        }, "No", "Yes", false, true);
    }

    @OnClick(R.id.lnBlockedUsers)
    void onBlockedUsers(){
        getBaseAppActivity().changeFragment(BlockedUsersFragment.newInstance(), true);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onClickLeftButton() {
        Toast.makeText(getContext(), "left", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClickRightButton(boolean isChecked) {
        Toast.makeText(getContext(), "right", Toast.LENGTH_SHORT).show();
    }
}
