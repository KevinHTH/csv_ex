package sg.vinova.noticeboard.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ogaclejapan.smarttablayout.SmartTabLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import sg.vinova.noticeboard.R;
import sg.vinova.noticeboard.adapter.LoginSigupAdapter;

/**
 * Created by Jacky on 4/26/17.
 */

public class LoginSignUpFragment extends BaseMainFragment {
    @BindView(R.id.vpLoginSign)
    ViewPager vpLoginSign;

    @BindView(R.id.viewpagertab)
    SmartTabLayout viewpagertab;

    Unbinder unbinder;
    LoginSigupAdapter adapter;


    public static LoginSignUpFragment newInstance() {
        LoginSignUpFragment fragment = new LoginSignUpFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new LoginSigupAdapter(getChildFragmentManager());

    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_login_sign;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        getFirstScreenActivity().hideFooter(true);
        setTitleToolbar(getString(R.string.title_app2));
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        vpLoginSign.setAdapter(adapter);
        viewpagertab.setViewPager(vpLoginSign);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
