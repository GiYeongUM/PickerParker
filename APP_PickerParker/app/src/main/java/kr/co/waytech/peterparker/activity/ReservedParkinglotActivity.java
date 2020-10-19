package kr.co.waytech.peterparker.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import kr.co.waytech.peterparker.R;
import kr.co.waytech.peterparker.adapter.ReservedListAdapter;
import kr.co.waytech.peterparker.model.ReservedBookingList;

import static kr.co.waytech.peterparker.activity.PostClass.body_list_reserved_pl;
import static kr.co.waytech.peterparker.adapter.ParkingAdapter.manage_parking_lot_id;
import static kr.co.waytech.peterparker.adapter.ParkingAdapter.manage_parking_lot_img_url;
import static kr.co.waytech.peterparker.fragment.ParkingFragment.parking_adding_flag;
import static kr.co.waytech.peterparker.fragment.ProfileFragment.str_Token;

public class ReservedParkinglotActivity extends AppCompatActivity {
    RecyclerView Reserved_bookingListRecyclerView;
    ReservedListAdapter Reserved_bookingListAdapter;
    private Activity ReservedParkinglot;
    public static List<ReservedBookingList> reserved_bookingList = new ArrayList<>();
    final PostClass Postc = new PostClass();
    public static int adding_reserve_flag = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservedparkinglot);
        getSupportActionBar().setTitle(Html.fromHtml("<font color='#000000'>내 주차장 예약 조회</font>"));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setElevation(1);
        SharedPreferences sharedPreferences = getSharedPreferences("login", MODE_PRIVATE);
        str_Token = sharedPreferences.getString("token", "");
        ReservedParkinglot = ReservedParkinglotActivity.this;
        Reserved_bookingListRecyclerView = findViewById(R.id.reserved_recent_recycler);
        Reserved_bookingListRecyclerView.setLayoutManager(new LinearLayoutManager(ReservedParkinglot));
        Reserved_bookingListRecyclerView.setHasFixedSize(true);

        Reserved_bookingListAdapter = new ReservedListAdapter(ReservedParkinglot, reserved_bookingList);
        Reserved_bookingListRecyclerView.setAdapter(Reserved_bookingListAdapter);
        if(reserved_bookingList == null) {
            reserved_bookingList = new ArrayList<>();
        }
        if(adding_reserve_flag == 1) {
            Postc.post_list_reservation(manage_parking_lot_id, str_Token);
            Handler mHandler = new Handler();
            mHandler.postDelayed(new Runnable() {
                public void run() {
                    if (body_list_reserved_pl.length() < 15) {
                        Toast.makeText(ReservedParkinglot, "로그인이 필요합니다." ,Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(ReservedParkinglot, LoginActivity.class);
                        parking_adding_flag = 1;
                        startActivity(intent);
                    } else {
                        Postc.count_my_parking_lot_reservation = Integer.parseInt(body_list_reserved_pl.split(",")[0].split(":")[1]);
                        for (int i = 2; i < Postc.count_my_parking_lot_reservation + 2; i++) {
                                Postc.my_PL_reserved_id = body_list_reserved_pl.split("\\{")[i].split(",")[0].split("\"")[3];
                                Postc.my_PL_reserved_price = body_list_reserved_pl.split("\\{")[i].split(",")[1].split(":")[1];
                                Postc.my_PL_reserved_add_price = body_list_reserved_pl.split("\\{")[i].split(",")[2].split(":")[1];
                                Postc.my_PL_reserved_time = body_list_reserved_pl.split("\\{")[i].split(",")[3].split("\"")[3] + "년 "
                                        + body_list_reserved_pl.split("\\{")[i].split(",")[4].split("\"")[3] + "월 "
                                        + body_list_reserved_pl.split("\\{")[i].split(",")[5].split("\"")[3] + "일 "
                                        + body_list_reserved_pl.split("\\{")[i].split(",")[6].split("\"")[3] + "시 "
                                        + body_list_reserved_pl.split("\\{")[i].split(",")[7].split("\"")[3] + "분 ~ "
                                        + body_list_reserved_pl.split("\\{")[i].split(",")[8].split("\"")[3] + "년 "
                                        + body_list_reserved_pl.split("\\{")[i].split(",")[9].split("\"")[3] + "월 "
                                        + body_list_reserved_pl.split("\\{")[i].split(",")[10].split("\"")[3] + "일 "
                                        + body_list_reserved_pl.split("\\{")[i].split(",")[11].split("\"")[3] + "시 "
                                        + body_list_reserved_pl.split("\\{")[i].split(",")[12].split("\"")[3] + "분";
                                if(body_list_reserved_pl.split("\\{")[i].split(",")[13].split(":")[1].equals("null")){
                                    Postc.my_PL_reserved_in = "입차하지 않음";
                                }
                                else {
                                    Postc.my_PL_reserved_in = body_list_reserved_pl.split("\\{")[i].split(",")[13].split("\"")[3].split(",")[3] + "시" +
                                            body_list_reserved_pl.split("\\{")[i].split(",")[13].split("\"")[3].split(",")[4] + "분";
                                }
                                if(body_list_reserved_pl.split("\\{")[i].split(",")[14].split(":")[1].equals("null")){
                                    Postc.my_PL_reserved_out = "출차하지 않음";
                                }
                                else {
                                    Postc.my_PL_reserved_out = body_list_reserved_pl.split("\\{")[i].split(",")[14].split("\"")[3].split(",")[3] + "시" +
                                            body_list_reserved_pl.split("\\{")[i].split(",")[14].split("\"")[3].split(",")[4] + "분";
                                }
                                Postc.my_PL_reserved_nick = body_list_reserved_pl.split("\\{")[i].split(",")[15].split("\"")[3];
                                Postc.my_PL_reserved_phone = body_list_reserved_pl.split("\\{")[i].split(",")[16].split("\"")[3];
                                Postc.my_PL_reserved_car = body_list_reserved_pl.split("\\{")[i].split(",")[17].split("\"")[3];

                                 if (i == 2) {
                                    reserved_bookingList.clear();
                                    reserved_bookingList = new ArrayList<>();
                                }
                                reserved_bookingList.add(new ReservedBookingList(Postc.my_PL_reserved_id, Postc.my_PL_reserved_price, Postc.my_PL_reserved_add_price, Postc.my_PL_reserved_time
                                        , Postc.my_PL_reserved_in , Postc.my_PL_reserved_out, Postc.my_PL_reserved_nick, Postc.my_PL_reserved_phone, Postc.my_PL_reserved_car, manage_parking_lot_img_url));


                        }
                    }
                }
            }, 300);

            adding_reserve_flag = 0;
        }
    }
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
