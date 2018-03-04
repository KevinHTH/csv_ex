package uel.vteam.belovedhostel.view;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import uel.vteam.belovedhostel.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContactFragment extends Fragment {
    View v;

    public ContactFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

         v= inflater.inflate(R.layout.fragment_contact, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(getResources().getString(R.string.contact_fragment_title));
        return  v;
    }

}
