package uel.vteam.belovedhostel.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import uel.vteam.belovedhostel.R;
import uel.vteam.belovedhostel.adapter.MenuServiceAdapter;


public class MenuServiceFragment extends Fragment {


    MenuServiceAdapter menuServiceAdapter;
    ExpandableListView expandlvMenuService;
    ArrayList<String> listDataHeader;
    ArrayList<Integer> arrImageHeader;
    HashMap<String, List<String>> listDataChild;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_menu_service, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();
        ((AppCompatActivity) getActivity()).getSupportActionBar()
                .setTitle(getResources().getString(R.string.services_fragment_title));
        expandlvMenuService = (ExpandableListView) view.findViewById(R.id.exlistviewMenuService);
        prepareListData();
        menuServiceAdapter=new MenuServiceAdapter(getContext(),listDataHeader,listDataChild, arrImageHeader);
        expandlvMenuService.setAdapter(menuServiceAdapter);

        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int width = metrics.widthPixels;
        expandlvMenuService.setIndicatorBounds(width - GetPixelFromDips(50), width - GetPixelFromDips(10));

        addEvents();
        return view;
    }

    private void addEvents() {
        expandlvMenuService.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int parentPo, int childPo, long l) {

                Toast.makeText(getContext(), ""
                                +listDataHeader.get(parentPo)+":"+listDataChild.get(
                        listDataHeader.get(parentPo)).get(childPo),
                        Toast.LENGTH_SHORT).show();
                return false;
            }
        });
    }

    public int GetPixelFromDips(float pixels) {
        // Get the screen's density scale
        final float scale = getResources().getDisplayMetrics().density;
        // Convert the dps to pixels, based on density scale
        return (int) (pixels * scale + 0.5f);
    }
    private void prepareListData() {
        arrImageHeader =new ArrayList<>();
        arrImageHeader.add(R.drawable.icon_orderfood);
        arrImageHeader.add(R.drawable.icon_vehicles);
        arrImageHeader.add(R.drawable.icon_otherservice);
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        // Adding child data
        listDataHeader.add(getResources().getString(R.string.services_fragment_group_food));
        listDataHeader.add(getResources().getString(R.string.services_fragment_group_transport));
        listDataHeader.add(getResources().getString(R.string.services_fragment_group_other));

        // Adding child data
        List<String> food = new ArrayList<String>();
        food.add(getResources().getString(R.string.services_fragment_child_breakfast));
        food.add(getResources().getString(R.string.services_fragment_child_lunch));
        food.add(getResources().getString(R.string.services_fragment_child_dinner));
        food.add(getResources().getString(R.string.services_fragment_child_party));


        List<String> transport = new ArrayList<String>();
        transport.add(getResources().getString(R.string.services_fragment_child_renmoto));
        transport.add(getResources().getString(R.string.services_fragment_child_rencar));
        transport.add(getResources().getString(R.string.services_fragment_child_shuttle));


        List<String> other = new ArrayList<String>();
        other.add(getResources().getString(R.string.services_fragment_child_wash));
        other.add(getResources().getString(R.string.services_fragment_child_tourguide));

        listDataChild.put(listDataHeader.get(0), food); // Header, Child data
        listDataChild.put(listDataHeader.get(1), transport);
        listDataChild.put(listDataHeader.get(2), other);

    }



}
