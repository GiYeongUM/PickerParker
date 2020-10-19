package kr.co.waytech.peterparker.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import kr.co.waytech.peterparker.adapter.BookingImgAdapter;
import kr.co.waytech.peterparker.R;

import static kr.co.waytech.peterparker.activity.CalendarActivity.mDay;
import static kr.co.waytech.peterparker.activity.CalendarActivity.mMonth;
import static kr.co.waytech.peterparker.activity.CalendarActivity.mYear;
import static kr.co.waytech.peterparker.adapter.ListAdapter.ID;
import static kr.co.waytech.peterparker.adapter.ListAdapter.address;
import static kr.co.waytech.peterparker.adapter.ListAdapter.avaible_time;
import static kr.co.waytech.peterparker.adapter.ListAdapter.phone;
import static kr.co.waytech.peterparker.adapter.ListAdapter.price;
import static kr.co.waytech.peterparker.adapter.ListAdapter.distance;
import static kr.co.waytech.peterparker.adapter.ListAdapter.name;

public class BookingActivity extends AppCompatActivity {
    public static int count;
    long now = System.currentTimeMillis() - 1000;
    Date date_today = new Date(now);
    SimpleDateFormat sdfNow = new SimpleDateFormat("yyyy년 MM월 dd일");
    String formatDate = sdfNow.format(date_today);
    public static double selectcount = 0;
    public static Activity bookingActivity;
    public static ArrayList<CheckBox> checkBoxArrayList;
    public static ArrayList<TextView> textviewArrayList;
    public static double checkedcount;
    public static TextView booking_parking_lot_name, booking_parking_lot_address, booking_parking_lot_phone, booking_parking_lot_price, booking_parking_lot_distance;
    public static String textprice, texttime;
    public static String starttime, endtime, parking_ID;
    public static Button btnbooking, CalendarBtn;
    public static int totalprice;
    ViewPager viewPager;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookinglist);
        bookingActivity = BookingActivity.this;
        checkBoxArrayList= new ArrayList<>();
        textviewArrayList= new ArrayList<>();
        btnbooking = (Button)findViewById(R.id.booking_btn);
        CalendarBtn = (Button)findViewById(R.id.booking_Calendar_btn);
        booking_parking_lot_address = findViewById(R.id.booking_parking_lot_address);
        booking_parking_lot_phone = findViewById(R.id.booking_parking_lot_phone);
        booking_parking_lot_price = findViewById(R.id.booking_parking_lot_price);
        booking_parking_lot_distance = findViewById(R.id.booking_parking_lot_distance);
        booking_parking_lot_name = findViewById(R.id.booking_parking_lot_name);
        ImageButton booking_act_back_btn = findViewById(R.id.booking_act_back_btn);
        viewPager = (ViewPager) findViewById(R.id.view);
        PagerAdapter adapter = new BookingImgAdapter(BookingActivity.this);
        viewPager.setAdapter(adapter);
        parking_ID = ID;
        booking_parking_lot_name.setText(name);
        booking_parking_lot_address.setText(address);
        booking_parking_lot_price.setText("30분당 " + price + "원");
        booking_parking_lot_distance.setText(distance);
        booking_parking_lot_phone.setText(phone);
        if(mYear == 0) {
            CalendarBtn.setText(formatDate);
        }
        else{
            CalendarBtn.setText(mYear + "년 " + mMonth + "월 " + mDay + "일 ");
        }
        CalendarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BookingActivity.this, CalendarActivity.class);
                startActivity(intent);
            }
        });

        for(count = 0; count < 48; count++) {
            System.out.println("splited .. " + count + "번째 : "  + avaible_time.charAt(count));//.substring(count));
                final String checkboxid = "checkbox_time_" + (count + 1);
                String textviewid = "checkbox_text_" + (count + 1);
                int cresID = getResources().getIdentifier(checkboxid, "id", getPackageName());
                int tresID = getResources().getIdentifier(textviewid, "id", getPackageName());
                checkBoxArrayList.add((CheckBox) findViewById(cresID));
                textviewArrayList.add((TextView)findViewById(tresID));
                if(avaible_time.charAt(count) == '0' || avaible_time.charAt(count) == '2') {
                    checkBoxArrayList.get(count).setChecked(false);
                    checkBoxArrayList.get(count).setButtonDrawable(R.drawable.ic_booking_time_cannot);
                    textviewArrayList.get(count).setTextColor(Color.parseColor("#ffffff"));
                    checkBoxArrayList.get(count).setVisibility(View.INVISIBLE);
                    textviewArrayList.get(count).setVisibility(View.INVISIBLE);
                    checkBoxArrayList.get(count).setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener() {
                        int c = count;
                        public void onCheckedChanged(CompoundButton compoundButton, boolean bool) {
                            /*
                            checkBoxArrayList.get(c).setChecked(false);
                            checkBoxArrayList.get(c).setButtonDrawable(R.drawable.ic_booking_time_cannot);
                            textviewArrayList.get(c).setTextColor(Color.parseColor("#ffffff"));

                             */

                        }
                    });
                }
                else if (avaible_time.charAt(count) == '1'){
                checkBoxArrayList.get(count).setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener() {
                    int a = count;

                    @Override

                    public void onCheckedChanged(CompoundButton compoundButton, boolean bool) {
                        if (bool == true) {
                            compoundButton.setButtonDrawable(R.drawable.ic_booking_time_enable);
                            textviewArrayList.get(a).setTextColor(Color.parseColor("#ffffff"));
                            for (int b = 0; b < checkBoxArrayList.size(); b++) {
                                if (checkBoxArrayList.get(a).isChecked() && checkBoxArrayList.get(b).isChecked()) {
                                    if (b > a) {
                                        for (int c = a + 1; c < b; c++) {
                                            if (!checkBoxArrayList.get(c).isChecked()) {
                                                if(checkBoxArrayList.get(c).getVisibility() == View.INVISIBLE){
                                                    Toast.makeText(getApplicationContext(), "예약 불가능한 시간입니다." + textviewArrayList.get(c).getText(), Toast.LENGTH_SHORT).show();
                                                    for (int d = 0; d < 48; d++) {
                                                            checkBoxArrayList.get(d).setChecked(false);
                                                            checkBoxArrayList.get(d).setButtonDrawable(R.drawable.ic_booking_time_disable);
                                                            textviewArrayList.get(d).setTextColor(Color.parseColor("#000000"));

                                                    }
                                                    break;
                                                }
                                                else {
                                                    checkBoxArrayList.get(c).setChecked(true);
                                                    checkBoxArrayList.get(c).setButtonDrawable(R.drawable.ic_booking_time_enable);
                                                    textviewArrayList.get(c).setTextColor(Color.parseColor("#ffffff"));
                                                }

                                                System.out.println("선택 : " + a + ", c = " + c);
                                            }
                                        }
                                    } else if (a > b) {
                                        for (int c = b; c < a; c++) {
                                            if (!checkBoxArrayList.get(c).isChecked()) {
                                                if(checkBoxArrayList.get(c).getVisibility() == View.INVISIBLE){
                                                    Toast.makeText(getApplicationContext(), "예약 불가능한 시간입니다." + textviewArrayList.get(c).getText(), Toast.LENGTH_SHORT).show();
                                                    for (int d = 0; d < 48; d++) {
                                                        checkBoxArrayList.get(d).setChecked(false);
                                                        checkBoxArrayList.get(d).setButtonDrawable(R.drawable.ic_booking_time_disable);
                                                        textviewArrayList.get(d).setTextColor(Color.parseColor("#000000"));

                                                    }
                                                    break;
                                                }
                                                else {
                                                    checkBoxArrayList.get(c).setChecked(true);
                                                    checkBoxArrayList.get(c).setButtonDrawable(R.drawable.ic_booking_time_enable);
                                                    textviewArrayList.get(c).setTextColor(Color.parseColor("#ffffff"));
                                                    System.out.println("선택 : " + a + ", c = " + c);
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        } else {
                            compoundButton.setButtonDrawable(R.drawable.ic_booking_time_disable);
                            textviewArrayList.get(a).setTextColor(Color.parseColor("#000000"));
                            selectcount++;
                            if (selectcount > 1) {
                                for (int b = 0; b < checkBoxArrayList.size(); b++) {
                                    if (checkBoxArrayList.get(b).isChecked()) {
                                        if (b < a) {
                                            if (checkBoxArrayList.get(b).isChecked()) {
                                                checkBoxArrayList.get(a).setChecked(false);
                                                checkBoxArrayList.get(a).setButtonDrawable(R.drawable.ic_booking_time_disable);
                                                textviewArrayList.get(a).setTextColor(Color.parseColor("#000000"));
                                                checkBoxArrayList.get(b).setChecked(false);
                                                checkBoxArrayList.get(b).setButtonDrawable(R.drawable.ic_booking_time_disable);
                                                textviewArrayList.get(b).setTextColor(Color.parseColor("#000000"));
                                                System.out.println("선택 : " + a + ", b 제거 " + b);
                                            }
                                        } else if (a < b) {
                                            if (checkBoxArrayList.get(b).isChecked()) {
                                                checkBoxArrayList.get(b).setChecked(false);
                                                checkBoxArrayList.get(b).setButtonDrawable(R.drawable.ic_booking_time_disable);
                                                textviewArrayList.get(b).setTextColor(Color.parseColor("#000000"));
                                                checkBoxArrayList.get(a).setChecked(false);
                                                checkBoxArrayList.get(a).setButtonDrawable(R.drawable.ic_booking_time_disable);
                                                textviewArrayList.get(a).setTextColor(Color.parseColor("#000000"));
                                                System.out.println("선택 : " + a + ", b 제거 " + b);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                });
            }
            btnbooking.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    checkedcount = 0;
                    int g = 0;
                    int b = 0;
                    while(g < checkBoxArrayList.size()){
                        if (checkBoxArrayList.get(g).isChecked()) {
                            if(checkedcount == 0){
                                starttime = (String)textviewArrayList.get(g).getText();
                                b = g;
                            }
                            checkedcount++;
                        }
                        g++;
                    }
                    if((int)checkedcount == 48){
                        endtime = "24:00";
                    }
                    else {
                       endtime = (String)textviewArrayList.get(b + (int) checkedcount).getText();
                    }
                    System.out.println(starttime);
                    System.out.println(endtime);
                    System.out.println(checkedcount);
                    totalprice = (int)checkedcount * Integer.parseInt(price);
                    if(checkedcount != 0) {
                        Intent intent = new Intent(getApplicationContext(), ConfirmActivity.class);
                        startActivity(intent);
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "예약 시간을 선택해 주세요.", Toast.LENGTH_LONG).show();
                        checkedcount = 0;
                    }
                }
            });

        }
        booking_act_back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }



}
