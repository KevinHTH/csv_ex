package uel.vteam.belovedhostel.view;


import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import uel.vteam.belovedhostel.R;
import uel.vteam.belovedhostel.adapter.MyMethods;
import uel.vteam.belovedhostel.adapter.TypeRoomAdapter;
import uel.vteam.belovedhostel.model.Constant;
import uel.vteam.belovedhostel.model.RowTypeRoom;


public class ChooseRoomFragment extends Fragment {




    String[] menuTitles;
    String[] menuDetails;
    TypedArray menuIcons;
    ListView lvTypeRoom;
    List<RowTypeRoom> rowItems;
    TypeRoomAdapter typeRoomAdapter;
    FragmentManager fragmentManager;
    Bundle dateBooking =null;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(
                getResources().getString(R.string.choose_room_title_actionbar));
        View v = inflater.inflate(R.layout.fragment_choose_room, container, false);
        dateBooking = this.getArguments();
        fragmentManager = getActivity().getSupportFragmentManager();
        addControls(v);
        addEvents();
        return v;
    }


    private void addEvents() {

        lvTypeRoom.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (MyMethods.getInstance().checkNetwork(getContext())){
                    String typeRoom = rowItems.get(i).getTitle();
                    dateBooking.putString(Constant.TYPE_ROOM, typeRoom);
                    Fragment fragment = new ListRoomFragment();
                    fragment.setArguments(dateBooking);
                    MyMethods.getInstance().replaceFragment(fragment,R.id.fragment_container, fragmentManager);
                }
                else {
                    Snackbar snackbar = Snackbar
                            .make(view,getResources().getString(R.string.error_no_network), Snackbar.LENGTH_LONG);
                    snackbar.setDuration(4000).show();

                }
            }
        });
    }

    private void addControls(View v) {
        menuTitles = getResources().getStringArray(R.array.nameTypeRoom);
        menuDetails = getResources().getStringArray(R.array.detailRoom);
        menuIcons = getResources().obtainTypedArray(R.array.iconTypeRoom);

        lvTypeRoom = (ListView) v.findViewById(R.id.lvTypeRoom);
        rowItems = new ArrayList<RowTypeRoom>();
        for (int i = 0; i < menuTitles.length; i++) {
            RowTypeRoom item =
                    new RowTypeRoom
                            (menuTitles[i],
                                    menuIcons.getResourceId(i, -1),
                                    menuDetails[i]);
            rowItems.add(item);
        }
        menuIcons.recycle();
        typeRoomAdapter = new TypeRoomAdapter(getContext(), rowItems);
        lvTypeRoom.setAdapter(typeRoomAdapter);
    }

}
