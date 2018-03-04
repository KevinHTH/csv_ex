package uel.vteam.belovedhostel.view;



import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;



import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import uel.vteam.belovedhostel.R;
import uel.vteam.belovedhostel.adapter.ListRoomAdapter;
import uel.vteam.belovedhostel.model.Constant;
import uel.vteam.belovedhostel.model.MyImage;
import uel.vteam.belovedhostel.model.MyRoom;


public class ListRoomFragment extends Fragment {

    String typeRoom;
    Button btnContinue,btnBack;
    DatabaseReference root;
    DatabaseReference rootRoom;

    List<MyImage> arrImage;
    ArrayList<MyRoom> arrMyRoom;
    ListView lvRoom2;
    ListRoomAdapter adapterRoom = null;
    FragmentManager fragmentManager;
    final String TAG = "Room";
    MyRoom roomSelected;
    Bundle data;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_list_room, container, false);
        fragmentManager = getActivity().getSupportFragmentManager();
        root = FirebaseDatabase.getInstance().getReference();
        addControls(v);
        loadData();
        addEvents();
        return v;
    }

    private void addControls(View v) {
        btnContinue = (Button) v.findViewById(R.id.btnContinue);
        btnBack= (Button) v.findViewById(R.id.btnBack2);

        data = this.getArguments();
        typeRoom = data.getString(Constant.TYPE_ROOM);
    //    data1 = data.getBundle("data1");

        arrMyRoom = new ArrayList<>();
        lvRoom2 = (ListView) v.findViewById(R.id.lvListRoom);
        adapterRoom = new ListRoomAdapter(getContext(), R.layout.item_list_room,
                arrMyRoom, data, fragmentManager);
        lvRoom2.setAdapter(adapterRoom);
    }


    private void addEvents() {
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                fm.popBackStack();
            }
        });

        // xem hinh anh cua phong
        lvRoom2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                roomSelected = arrMyRoom.get(position);
                Bundle data3 = new Bundle();
                data3.putSerializable(Constant.BUNDLE_ROOM_SELECTED,roomSelected);
                DetailRoomFragment detailFrag=new DetailRoomFragment();
                detailFrag.setArguments(data3);
                detailFrag.show(fragmentManager,"Detail ROOM");

            }
        });

        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                data.putString(Constant.BUNDLE_ACTION_CONTROL,Constant.BUNDLE_ACTION_CONTROL_CONTINUE);

                BookingListFragment fragBooking = new BookingListFragment();
                fragBooking.setArguments(data);

                FragmentTransaction transaction=fragmentManager.beginTransaction();
                transaction.addToBackStack("STEP4");
                transaction.replace(R.id.fragment_container, fragBooking).commit();
				
            }
        });

    }

    private void loadData() {
        if (typeRoom.equalsIgnoreCase("Single Room") ||typeRoom.equalsIgnoreCase("Phòng đơn") ){
            rootRoom=root.child(Constant.FB_KEY_MANAGEMENT_ROOT)
                    .child(Constant.FB_KEY_USER_ROOM).child(Constant.FB_ROOM_SINGLE);
        }else {
            rootRoom=root.child(Constant.FB_KEY_MANAGEMENT_ROOT)
                    .child(Constant.FB_KEY_USER_ROOM).child(Constant.FB_ROOM_SHARED);
        }
        rootRoom.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot oneRoom : dataSnapshot.getChildren()) {
                    String keyRoom = oneRoom.getKey().toString();
                    MyRoom myRoom = new MyRoom();
                    myRoom.setRoomId(keyRoom);
                    myRoom.setRoomAcreage(oneRoom.child(Constant.ROOM_AREA).getValue().toString());
                    myRoom.setRoomFurniture(oneRoom.child(Constant.ROOM_FURNITURE).getValue().toString());
                    myRoom.setBedRoom(oneRoom.child(Constant.ROOM_BEDROOM).getValue().toString());
                    myRoom.setDiscount(oneRoom.child(Constant.ROOM_DISCOUNT).getValue().toString());
                    myRoom.setRoomPrice(oneRoom.child(Constant.ROOM_PRICE).getValue().toString());
                    myRoom.setIsBooked(oneRoom.child(Constant.ROOM_STATUS).getValue().toString());

                    arrImage = new ArrayList<MyImage>();
                    for (DataSnapshot oneImage : oneRoom.child(Constant.ROOM_IMAGE).getChildren()) {
                        MyImage image=new MyImage();
                        image.setImageName(oneImage.child(Constant.ROOM_IMAGE_NAME).getValue().toString());
                        image.setImageLink(oneImage.child(Constant.ROOM_IMAGE_LINK).getValue().toString());
                        arrImage.add(image);
                    }
                    myRoom.setRoomImage(arrImage);
                   arrMyRoom.add(myRoom);
                }
                adapterRoom.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
		

		
    }
}