package uel.vteam.belovedhostel.view;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import uel.vteam.belovedhostel.R;


public class TabSupportFragment extends Fragment {

    private View v;
    TextView txtTab1,txtTab2;
    Fragment fragment;
    FragmentManager fragmentManager;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_tab_support, null);
        fragmentManager = getFragmentManager();
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();

        addControls();
        setupTab();
        addEvents();

        return v;
    }

    private void addEvents() {
        txtTab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtTab1.setBackgroundColor(getResources().getColor(R.color.DeepskyBlue2));
                txtTab2.setBackgroundColor(getResources().getColor(R.color.DeepskyBlue));
                createTab1();
            }
        });
        txtTab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createTab2();
                txtTab1.setBackgroundColor(getResources().getColor(R.color.DeepskyBlue));
                txtTab2.setBackgroundColor(getResources().getColor(R.color.DeepskyBlue2));
            }
        });
    }

    private void setupTab() {
       /* int count = fragmentManager.getBackStackEntryCount();
        for (int i = 0; i < count - 1; ++i) {
            fragmentManager.popBackStack();
        }*/
        createTab1();

    }

    private void createTab2() {
        fragment=new HistoryChatFragment();
        fragmentManager.beginTransaction().replace(R.id.frame_content_tabview, fragment).commit();
    }

    private void createTab1() {
        fragment=new ListAdminFragment();
        fragmentManager.beginTransaction().replace(R.id.frame_content_tabview, fragment).commit();
    }

    private void addControls() {
        txtTab1= (TextView) v.findViewById(R.id.txtTab1);
        txtTab2= (TextView) v.findViewById(R.id.txtTab2);

    }

}
