package kr.co.waytech.peterparker.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import kr.co.waytech.peterparker.OneDayDecorator;
import kr.co.waytech.peterparker.R;
import kr.co.waytech.peterparker.SaturdayDecorator;
import kr.co.waytech.peterparker.SundayDecorator;

import static kr.co.waytech.peterparker.activity.BookingActivity.bookingActivity;
import static kr.co.waytech.peterparker.activity.BookingActivity.parking_ID;
import static kr.co.waytech.peterparker.activity.ManagementTimeActivity.show_day_btn;
import static kr.co.waytech.peterparker.adapter.ListAdapter.avaible_time;
import static kr.co.waytech.peterparker.adapter.ListAdapter.phone;


public class ManageCalendarActivity extends AppCompatActivity  {

    MaterialCalendarView materialCalendarView;
    long now = System.currentTimeMillis() - 1000;
    long MaximumDate = now + (1000*24*60*60*7);
    public static int mYear, mMonth, mDay = 0;
    public static String sYear, sMonth, sDay;
    Date date_today = new Date(now);
    public Drawable drawable;
    Date date_max = new Date(MaximumDate);
    final List<CalendarDay> days = new ArrayList<>();
    Color color = new Color();
    TextView calendar_text1, calendar_text2;
    Button select_btn, booking_day_text;
    final PostClass Postc = new PostClass();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_booking);
        getSupportActionBar().setTitle(Html.fromHtml("<font color='#000000'>공유시간 설정</font>"));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setElevation(1);
        calendar_text1 = findViewById(R.id.calendar_text1);
        materialCalendarView = findViewById(R.id.calendarView);
        select_btn = findViewById(R.id.calendar_btn);
        booking_day_text = findViewById(R.id.booking_Calendar_btn);
        materialCalendarView.state().edit()
                .setFirstDayOfWeek(Calendar.SUNDAY)
                .setMinimumDate(now)
                .setMaximumDate(MaximumDate)
                .setCalendarDisplayMode(CalendarMode.MONTHS)
                .commit();

        materialCalendarView.addDecorators(
                new SundayDecorator(),
                new SaturdayDecorator(),
                new OneDayDecorator());

        materialCalendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                int Year = date.getYear();
                int Month = date.getMonth() + 1;
                int Day = date.getDay();
                mYear = Year;
                mMonth = Month;
                mDay = Day;
                calendar_text1.setText(Year + "년 " + Month + "월 " + Day + "일 ");
                sYear = Integer.toString(mYear);
                if (mMonth < 10) {
                    sMonth = '0' + Integer.toString(mMonth);
                } else{
                    sMonth = Integer.toString(mMonth);
                }
                if (mDay < 10) {
                    sDay = '0' + Integer.toString(mDay);
                } else{
                    sDay = Integer.toString(mDay);
                }

            }
        });
        select_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                show_day_btn.setText(sYear + "년 " + sMonth + "월 " + sDay + "일");
                finish();

            }
        });

    }
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

}
