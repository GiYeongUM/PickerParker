package kr.co.waytech.peterparker.fragment;

import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import kr.co.waytech.peterparker.R;
import kr.co.waytech.peterparker.activity.LoginActivity;
import kr.co.waytech.peterparker.activity.MainActivity;
import kr.co.waytech.peterparker.activity.PostClass;
import kr.co.waytech.peterparker.adapter.BookingListAdapter;
import kr.co.waytech.peterparker.model.BookingList;
import kr.co.waytech.peterparker.model.ParkingList;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;
import static kr.co.waytech.peterparker.activity.PostClass.my_booking_list;
import static kr.co.waytech.peterparker.activity.PostClass.my_parking_lot;
import static kr.co.waytech.peterparker.fragment.ProfileFragment.str_Token;

public class BookingListFragment extends Fragment {

    View fragmentView;
    RecyclerView bookingListRecyclerView;
    BookingListAdapter bookingListAdapter;
    private Context bookinglistFragment;
    public static List<BookingList> bookingList;
    final PostClass Postc = new PostClass();
    public static int adding_booking_flag = 1;
    public BookingListFragment()
    {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("login", MODE_PRIVATE);
        str_Token = sharedPreferences.getString("token", "");
        bookinglistFragment = getActivity();
        if(bookingList == null) {
            bookingList = new ArrayList<>();
        }
        if(adding_booking_flag == 1) {
            Postc.get_my_booking_list(str_Token);
            Handler mHandler = new Handler();
            mHandler.postDelayed(new Runnable() {
                public void run() {
                    if (Postc.my_booking_list.length() < 15) {
                        Toast.makeText(bookinglistFragment, "로그인이 필요합니다." ,Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                        adding_booking_flag = 1;
                        startActivity(intent);
                    } else {
                        Postc.count_my_booking_list = Integer.parseInt(my_booking_list.split(",")[0].split(":")[1]);
                        for (int i = 2; i < Postc.count_my_booking_list + 2; i++) {
                            if (my_booking_list.split("\\{")[i].split(",")[15].split("\"")[3] == null) {
                                break;
                            } else if (my_booking_list.split("\\{")[i].split(",")[14].split(":")[1].equals("\"cancel\"")) {

                            } else {
                                Postc.my_booking_id = my_booking_list.split("\\{")[i].split(",")[0].split(":")[1];
                                Postc.my_booking_name = my_booking_list.split("\\{")[i].split(",")[15].split("\"")[3];
                                Postc.my_booking_address = my_booking_list.split("\\{")[i].split(",")[16].split("\"")[3];
                                Postc.my_booking_price = Integer.toString(Integer.parseInt(my_booking_list.split("\\{")[i].split(",")[1].split(":")[1]) +
                                        Integer.parseInt(my_booking_list.split("\\{")[i].split(",")[2].split(":")[1]));
                                Postc.my_booking_time = my_booking_list.split("\\{")[i].split(",")[3].split("\"")[3] + "년 "
                                        + my_booking_list.split("\\{")[i].split(",")[4].split("\"")[3] + "월 "
                                        + my_booking_list.split("\\{")[i].split(",")[5].split("\"")[3] + "일 "
                                        + my_booking_list.split("\\{")[i].split(",")[6].split("\"")[3] + "시 "
                                        + my_booking_list.split("\\{")[i].split(",")[7].split("\"")[3] + "분 ~ "
                                        + my_booking_list.split("\\{")[i].split(",")[8].split("\"")[3] + "년 "
                                        + my_booking_list.split("\\{")[i].split(",")[9].split("\"")[3] + "월 "
                                        + my_booking_list.split("\\{")[i].split(",")[10].split("\"")[3] + "일 "
                                        + my_booking_list.split("\\{")[i].split(",")[11].split("\"")[3] + "시 "
                                        + my_booking_list.split("\\{")[i].split(",")[12].split("\"")[3] + "분";
                                Postc.my_booking_img = my_booking_list.split("\\{")[i].split(",")[17].split("\"")[3].replace("\\", "");
                                System.out.println("splited result : " + Postc.count_my_booking_list + " 개, " + Postc.my_booking_address + ", " + Postc.my_booking_price + "원, " + Postc.my_booking_time + ", ");
                                if (i == 2) {
                                    bookingList.clear();
                                    bookingList = new ArrayList<>();
                                }
                                bookingList.add(new BookingList(Postc.my_booking_id, Postc.my_booking_name, "사용완료", Postc.my_booking_address, Postc.my_booking_price + "원", Postc.my_booking_time, Postc.my_booking_img));

                            }
                        }
                    }
                }
            }, 600);

            adding_booking_flag = 0;
        }
    }
    //Add dummy data in Booking class here
    //bookingList.add(new BookingList("산기대 종합관 뒤","사용완료","경기도 시흥시 정왕동 2121-1", "3,000원", "2020년 07월 20일(월) 12:00\n~2020년 07월 20일(월) 14:00", R.drawable.parkinglot4));
    //bookingList.add(new BookingList("산기대 E동 뒤","사용완료","경기도 시흥시 정왕동 2121-1", "2,000원", "2020년 07월 17일(금) 14:00\n~2020년 07월 17일(금) 15:00", R.drawable.parkinglot5));
    //bookingList.add(new BookingList("산기대 A동 뒤","사용완료","경기도 시흥시 정왕동 2121-1", "4,000원", "2020년 07월 15일(금) 14:00\n~2020년 07월 15일(금) 15:00", R.drawable.parkinglot1));
    //bookingList.add(new BookingList("산기대 B동 뒤","사용완료","경기도 시흥시 정왕동 2121-1", "1,000원", "2020년 07월 17일(금) 14:00\n~2020년 07월 17일(금) 15:00", R.drawable.parkinglot4));
    //bookingList.add(new BookingList("산기대 C동 뒤","사용완료","경기도 시흥시 정왕동 2121-1", "1,400원", "2020년 07월 17일(금) 14:00\n~2020년 07월 17일(금) 15:00", R.drawable.parkinglot2));
    //bookingList.add(new BookingList("산기대 D동 뒤","사용완료","경기도 시흥시 정왕동 2121-1", "2,500원", "2020년 07월 17일(금) 14:00\n~2020년 07월 17일(금) 15:00", R.drawable.parkinglot5));
    //bookingList.add(new BookingList("시흥비즈니스센터","사용완료","경기도 시흥시 정왕동 2121-1", "2,000원", "2020년 07월 17일(금) 14:00\n~2020년 07월 17일(금) 15:00", R.drawable.parkinglot3));





    Button DeleteBookingBtn;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.fragment_bookinglist, container,false);
        bookingListRecyclerView = fragmentView.findViewById(R.id.recent_recycler);
        bookingListRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        bookingListRecyclerView.setHasFixedSize(true);

        bookingListAdapter = new BookingListAdapter(getActivity(), bookingList);
        bookingListRecyclerView.setAdapter(bookingListAdapter);


        return fragmentView;

    }
    public static void addlist(String id, String name, String use,String address, String price, String time, String img){
        if(bookingList == null) {
            bookingList = new ArrayList<>();
        }
        bookingList.add(new BookingList(id, name,use,address, price, time, img));
    }


}
