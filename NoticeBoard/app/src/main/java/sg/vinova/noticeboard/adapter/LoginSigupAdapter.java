package sg.vinova.noticeboard.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import sg.vinova.noticeboard.ui.fragment.LoginFragment;
import sg.vinova.noticeboard.ui.fragment.SignUpFragment;

public class LoginSigupAdapter extends FragmentPagerAdapter {
    public LoginSigupAdapter(FragmentManager fm) {
        super(fm); // super tracks this
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return LoginFragment.newInstance();
        }
        return SignUpFragment.newInstance();
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 0)
            return "Login";
        return "Sign Up";
    }
}