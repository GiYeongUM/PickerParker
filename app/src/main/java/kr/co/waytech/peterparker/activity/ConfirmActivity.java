package kr.co.waytech.peterparker.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;

import kr.co.waytech.peterparker.DownloadImageTask;
import kr.co.waytech.peterparker.R;

import static kr.co.waytech.peterparker.activity.CalendarActivity.sDay;
import static kr.co.waytech.peterparker.activity.CalendarActivity.sMonth;
import static kr.co.waytech.peterparker.activity.CalendarActivity.sYear;
import static kr.co.waytech.peterparker.adapter.ListAdapter.address;
import static kr.co.waytech.peterparker.adapter.ListAdapter.price;
import static kr.co.waytech.peterparker.activity.BookingActivity.bookingActivity;
import static kr.co.waytech.peterparker.activity.BookingActivity.endtime;
import static kr.co.waytech.peterparker.activity.BookingActivity.totalprice;
import static kr.co.waytech.peterparker.activity.BookingActivity.parking_ID;
import static kr.co.waytech.peterparker.activity.BookingActivity.starttime;
import static kr.co.waytech.peterparker.activity.CalendarActivity.mDay;
import static kr.co.waytech.peterparker.activity.CalendarActivity.mMonth;
import static kr.co.waytech.peterparker.activity.CalendarActivity.mYear;
import static kr.co.waytech.peterparker.fragment.ProfileFragment.str_Token;

public class ConfirmActivity extends AppCompatActivity {
    PostClass Postc = new PostClass();
    public static TextView oneprice, total_price, oneAddress, oneday, onetime;
    public static String startYear, startMonth, startDay, endYear, endMonth, endDay;
    long now = System.currentTimeMillis() - 1000;
    Date date_today = new Date(now);
    SimpleDateFormat sdfNow = new SimpleDateFormat("yyyy년-MM월-dd일");
    String formatDate = sdfNow.format(date_today);
    public static String success_day, success_total_price;
    public static Activity confirmActivity;
    public static String Imgurl;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_one);
        confirmActivity = ConfirmActivity.this;

        getSupportActionBar().setTitle(Html.fromHtml("<font color='#000000'>주차장 예약</font>"));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setElevation(1);

        final AlertDialog.Builder builder = new AlertDialog.Builder(ConfirmActivity.this);
        Button booking_one_confirm_button = findViewById(R.id.booking_one_confirm_button);
        oneprice = findViewById(R.id.booking_one_parking_lot_price);
        total_price = findViewById(R.id.booking_one_parking_lot_total_price);
        oneAddress = findViewById(R.id.booking_one_parking_lot_address);
        oneday = findViewById(R.id.booking_one_parking_lot_day);
        onetime = findViewById(R.id.booking_one_parking_lot_time);
        new DownloadImageTask((ImageView)findViewById(R.id.booking_one_parking_lot_img)).execute(Postc.Parking_img[0]);
        onetime.setText(starttime + " ~ " + endtime);
        oneAddress.setText(address);
        oneday.setText(mYear + "년-" + mMonth + "월-" + mDay + "일");
        startYear =  sYear;
        startMonth = sMonth;
        startDay = sDay;
        Imgurl = Postc.Parking_img[0];
        if(mYear == 0){
            oneday.setText(formatDate);
            startYear =  formatDate.split("년")[0];
            startMonth = formatDate.split("년")[1].split("월")[0].replace("-","");
            startDay = formatDate.split("년")[1].split("월")[1].split("일")[0].replace("-","");
        }

        oneprice.setText("30분당 " + price + "원");
        total_price.setText(totalprice + "원");
        success_day = (String) oneday.getText();
        success_total_price = (String) total_price.getText();
        booking_one_confirm_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                builder.setTitle("주차장 예약을 하시겠습니까?").setMessage(oneday.getText() + "\n" + starttime + " ~ " + endtime + "\n" + totalprice + "원").setIcon(R.drawable.booking_icon_enable);
                builder.setPositiveButton("예약", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        System.out.println("예약함");
                        Postc.send_booking(startYear, startMonth, startDay, starttime.split(":")[0], starttime.split(":")[1],
                                startYear, startMonth, startDay, endtime.split(":")[0], endtime.split(":")[1], parking_ID, str_Token);
                        Handler mHandler = new Handler();
                        mHandler.postDelayed(new Runnable(){
                            public void run() {
                                if(Postc.bookingbody.equals("success")){
                                    System.out.println(Postc.bookingbody);
                                    Toast.makeText(getApplicationContext(), "예약되었습니다.", Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(getApplicationContext(), BookingSuccessActivity.class);
                                    startActivity(intent);
                                }
                                else{
                                    Toast.makeText(getApplicationContext(), "예약에 실패하였습니다.", Toast.LENGTH_LONG).show();
                                }
                            }
                        }, 500);

                    }
                });
                builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        System.out.println("취소함");
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });


    }
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

}
