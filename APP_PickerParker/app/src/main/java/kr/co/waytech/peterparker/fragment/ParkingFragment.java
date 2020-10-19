package kr.co.waytech.peterparker.fragment;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import kr.co.waytech.peterparker.R;
import kr.co.waytech.peterparker.activity.AddParkinglotActivity;
import kr.co.waytech.peterparker.activity.LoginActivity;
import kr.co.waytech.peterparker.activity.PostClass;
import kr.co.waytech.peterparker.adapter.ParkingAdapter;
import kr.co.waytech.peterparker.model.BookingList;
import kr.co.waytech.peterparker.model.ParkingList;

import static android.content.Context.MODE_PRIVATE;
import static java.lang.Thread.sleep;
import static kr.co.waytech.peterparker.activity.PostClass.my_parking_lot;
import static kr.co.waytech.peterparker.fragment.ProfileFragment.str_Token;

public class ParkingFragment extends Fragment {

    View fragmentView;
    RecyclerView parkingListRecyclerView;
    ParkingAdapter parkingListAdapter;
    public static List<ParkingList> parkingList;
    final PostClass Postc = new PostClass();
    public static int parking_adding_flag = 1;
    private Context parkingFragment;

    public ParkingFragment()
    {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        parkingFragment = getContext();
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("login", MODE_PRIVATE);
        str_Token = sharedPreferences.getString("token", "");
        //Add dummy data in Booking class here
        if(parkingList == null) {
            parkingList = new ArrayList<>();
        }
        if(parking_adding_flag == 1) {
            //Postc.get_my_parking_lot(str_Token);

            set_afterParkingView(str_Token);

            if (Postc.my_parking_lot.length() < 15) {
                        Toast.makeText(parkingFragment, "로그인이 필요합니다.", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                        parking_adding_flag = 1;
                        startActivity(intent);
                    } else {
                        Postc.count_my_parking_lot = Integer.parseInt(my_parking_lot.split(",")[0].split(":")[1]);
                        if (Postc.count_my_parking_lot == 0) {
                            System.out.println("주차장 없음");
                            Toast.makeText(parkingFragment, "주차장이 없습니다." ,Toast.LENGTH_LONG).show();

                        } else {
                            for (int i = 0; i < Postc.count_my_parking_lot; i++) {
                                Postc.my_parking_lot_id = my_parking_lot.split("\\{")[i + 2].split(",")[0].split("\"")[3];
                                Postc.my_parking_lot_name = my_parking_lot.split("\\{")[i + 2].split(",")[2].split("\"")[3];
                                Postc.my_parking_lot_address = my_parking_lot.split("\\{")[i + 2].split(",")[3].split("\"")[3];
                                Postc.my_parking_lot_price = my_parking_lot.split("\\{")[i + 2].split(",")[4].split(":")[1];
                                Postc.my_parking_lot_imageurl = my_parking_lot.split("\\{")[i + 2].split(",")[5].split("\"")[3].replace("\\", "");
                                System.out.println("splited result : " + Postc.count_my_parking_lot + " 개, " + Postc.my_parking_lot_id + ", " + Postc.my_parking_lot_address + ", " + "30분당 " + Postc.my_parking_lot_price + ", " + Postc.my_parking_lot_imageurl + ", ");
                                if (i == 0) {
                                    parkingList.clear();
                                    parkingList = new ArrayList<>();
                                }
                                parkingList.add(new ParkingList(Postc.my_parking_lot_name, Postc.my_parking_lot_address, "30분당 " + Postc.my_parking_lot_price, Postc.my_parking_lot_imageurl, Postc.my_parking_lot_id));
                            }
                        }
                    }
            parking_adding_flag = 0;
        }
    }

    ImageButton btn_addParking;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.fragment_parking, container,false);
        parkingListRecyclerView = fragmentView.findViewById(R.id.parking_recycler);
        parkingListRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        parkingListRecyclerView.setHasFixedSize(true);

        parkingListAdapter = new ParkingAdapter(getActivity(), parkingList);
        parkingListRecyclerView.setAdapter(parkingListAdapter);

        btn_addParking = (ImageButton)fragmentView.findViewById(R.id.btn_Add_Parkinglot);

        btn_addParking.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                Intent intent_addParkinglot = new Intent(getActivity(), AddParkinglotActivity.class);
                startActivity(intent_addParkinglot);
            }
        });

        return fragmentView;
    }
    public static void addlist(String name, String address, String price, String img, String ID){
        if(parkingList == null) {
            parkingList = new ArrayList<>();
        }
        parkingList.add(new ParkingList(name, address, price,  img, ID));
    }
    public void set_afterParkingView(String token)  {
        Postc.get_my_parking_lot(token);
        while (Postc.my_parking_lot.length() < 5) {
                try {
                    sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

    }
}
