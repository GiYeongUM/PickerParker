package kr.co.waytech.peterparker.activity;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


import kr.co.waytech.peterparker.DownloadImageTask;
import kr.co.waytech.peterparker.R;
import kr.co.waytech.peterparker.fragment.BookingListFragment;

import static kr.co.waytech.peterparker.activity.CalendarActivity.mDay;
import static kr.co.waytech.peterparker.activity.CalendarActivity.mMonth;
import static kr.co.waytech.peterparker.activity.CalendarActivity.mYear;
import static kr.co.waytech.peterparker.activity.ConfirmActivity.Imgurl;
import static kr.co.waytech.peterparker.activity.ConfirmActivity.confirmActivity;
import static kr.co.waytech.peterparker.activity.BookingActivity.bookingActivity;
import static kr.co.waytech.peterparker.activity.BookingActivity.endtime;
import static kr.co.waytech.peterparker.activity.BookingActivity.starttime;
import static kr.co.waytech.peterparker.activity.ConfirmActivity.success_total_price;
import static kr.co.waytech.peterparker.activity.ConfirmActivity.success_day;
import static kr.co.waytech.peterparker.adapter.ListAdapter.address;
import static kr.co.waytech.peterparker.fragment.BookingListFragment.adding_booking_flag;


public class BookingSuccessActivity extends AppCompatActivity {

    TextView success_list_text, success_address, success_time, success_price;
    ImageView success_img;
    public static int bookinglist_flag = 0;
    int drawable_img;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success);
        bookinglist_flag = 0;
        getSupportActionBar().setTitle(Html.fromHtml("<font color='#000000'>주차장 예약</font>"));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setElevation(1);
        mYear = 0; mMonth = 0; mDay = 0;
        success_list_text = findViewById(R.id.success_list_text);
        success_address = findViewById(R.id.success_address);
        success_time = findViewById(R.id.success_time);
        success_price = findViewById(R.id.success_price);
        success_img = findViewById(R.id.success_img);
        success_address.setText(address);
        success_price.setText(success_total_price);
        success_time.setText(success_day + " / " + starttime + " ~ " + endtime);
        new DownloadImageTask((ImageView)findViewById(R.id.success_img)).execute(Imgurl);
        SpannableString content = new SpannableString("내역");
        content.setSpan(new UnderlineSpan(), 0, content.length(),0);
        success_list_text.setText(content);
        bookingActivity.finish();
        confirmActivity.finish();
        success_list_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bookinglist_flag = 1;
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                adding_booking_flag = 1;
                startActivity(intent);
                finish();
            }
        });

    }
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
