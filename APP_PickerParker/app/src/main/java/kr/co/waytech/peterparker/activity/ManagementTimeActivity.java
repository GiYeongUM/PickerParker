package kr.co.waytech.peterparker.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import kr.co.waytech.peterparker.DownloadImageTask;
import kr.co.waytech.peterparker.R;

import static kr.co.waytech.peterparker.activity.ManageCalendarActivity.mYear;
import static kr.co.waytech.peterparker.activity.ManageCalendarActivity.sDay;
import static kr.co.waytech.peterparker.activity.ManageCalendarActivity.sMonth;
import static kr.co.waytech.peterparker.activity.ManageCalendarActivity.sYear;
import static kr.co.waytech.peterparker.activity.PostClass.count;
import static kr.co.waytech.peterparker.activity.PostClass.my_PL_available_time;
import static kr.co.waytech.peterparker.adapter.ListAdapter.avaible_time;
import static kr.co.waytech.peterparker.adapter.ParkingAdapter.manage_parking_lot_address;
import static kr.co.waytech.peterparker.adapter.ParkingAdapter.manage_parking_lot_id;
import static kr.co.waytech.peterparker.adapter.ParkingAdapter.manage_parking_lot_img_url;
import static kr.co.waytech.peterparker.adapter.ParkingAdapter.manage_parking_lot_name;
import static kr.co.waytech.peterparker.adapter.ParkingAdapter.manage_parking_lot_price;
import static kr.co.waytech.peterparker.fragment.ProfileFragment.str_Token;

public class ManagementTimeActivity extends AppCompatActivity {

    int count_time_all, count_day, count_time_day;
    long now = System.currentTimeMillis() - 1000;
    Date date_today = new Date(now);
    SimpleDateFormat sdfNow = new SimpleDateFormat("yyyy년 MM월 dd일");
    SimpleDateFormat sssNow = new SimpleDateFormat("yyyy-MM-dd");
    String secondDate = sssNow.format(date_today);
    String formatDate = sdfNow.format(date_today);
    public static int tabbtn_tool = 1;
    public static int check_day;
    public static int[][] timechecker;
    public static ArrayList<CheckBox> Manage_checkBoxArrayList_day_time;
    public static ArrayList<CheckBox> Manage_checkBoxArrayList_all_time;
    public static ArrayList<TextView> Manage_textviewArrayList_day_text;
    public static ArrayList<TextView> Manage_textviewArrayList_all_time;
    String default_time = "000000000000000000000000000000000000000000000000";
    String reserved_count, temp_time_data;
    public static int count_data_format;
    public static String[] reserved_count_month, reserved_count_day, reserved_count_time;
    public String[][] manage_Day;
    public static Button show_all_btn, show_day_btn;
    public static int selected_flag = 0;
    TextView manage_PL_name, manage_PL_address, manage_PL_price;
    ImageView PLImg;
    PostClass Postc = new PostClass();

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_time_setting);
        getSupportActionBar().setTitle(Html.fromHtml("<font color='#000000'>공유 시간 설정</font>"));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setElevation(1);
        SharedPreferences sharedPreferences = getSharedPreferences("login", MODE_PRIVATE);
        str_Token = sharedPreferences.getString("token", "");
        show_all_btn = findViewById(R.id.show_check_all_btn);
        show_day_btn = findViewById(R.id.show_check_day_btn);
        reserved_count_month = new String[8];
        reserved_count_day = new String[8];
        reserved_count_time = new String[8];
        Manage_checkBoxArrayList_all_time= new ArrayList<>();
        Manage_textviewArrayList_all_time= new ArrayList<>();
        Manage_checkBoxArrayList_day_time= new ArrayList<>();
        Manage_textviewArrayList_day_text= new ArrayList<>();
        Postc.send_avaible_time(manage_parking_lot_id, str_Token);
        final CheckBox checkall = findViewById(R.id.checkbox_check_all_1);
        final CheckBox checkday = findViewById(R.id.checkbox_check_all_2);
        final LinearLayout checking_all = findViewById(R.id.checking_all);
        final LinearLayout checking_day = findViewById(R.id.checking_day);
        PLImg = findViewById(R.id.manage_parkinglotImage);
        new DownloadImageTask(PLImg).execute(manage_parking_lot_img_url);
        manage_PL_name = findViewById(R.id.manage_parkinglotName);
        manage_PL_address = findViewById(R.id.manage_parkinglotAddress);
        manage_PL_price = findViewById(R.id.manage_parkinglotPrice);
        manage_PL_name.setText(manage_parking_lot_name);
        manage_PL_address.setText(manage_parking_lot_address);
        manage_PL_price.setText(manage_parking_lot_price + "원");
        Button management_confirm_btn = findViewById(R.id.management_confirm_btn);
        for (count_time_all = 0; count_time_all < 48; count_time_all++) {
            final String checkboxid_time = "management_checkbox_all_time_" + (count_time_all + 1);
            String textviewid_time = "management_checkbox_all_text_" + (count_time_all + 1);
            int cresID_time = getResources().getIdentifier(checkboxid_time, "id", getPackageName());
            int tresID_time = getResources().getIdentifier(textviewid_time, "id", getPackageName());
            Manage_checkBoxArrayList_all_time.add((CheckBox)findViewById(cresID_time));
            Manage_textviewArrayList_all_time.add((TextView)findViewById(tresID_time));
            Manage_checkBoxArrayList_all_time.get(count_time_all).setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener() {
                int a = count_time_all;
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean bool) {
                    if (bool == true) {
                        compoundButton.setButtonDrawable(R.drawable.ic_booking_time_enable);
                        Manage_textviewArrayList_all_time.get(a).setTextColor(Color.parseColor("#ffffff"));
                        StringBuilder change_time = new StringBuilder(default_time);
                        change_time.setCharAt(a, 'A');
                        default_time = change_time.toString();
                        System.out.println(change_time.toString());
                    }
                    else{
                        compoundButton.setButtonDrawable(R.drawable.ic_booking_time_disable);
                        Manage_textviewArrayList_all_time.get(a).setTextColor(Color.parseColor("#000000"));
                        tabbtn_tool = 0;
                        checkall.setChecked(false);
                        tabbtn_tool = 1;
                        StringBuilder change_time = new StringBuilder(default_time);
                        change_time.setCharAt(a, 'B');
                        default_time = change_time.toString();
                        System.out.println(change_time.toString());
                    }
                }
            });

        }
        show_all_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                show_all_btn.setBackgroundResource(R.drawable.btn_white);
                show_all_btn.setTextSize(18);
                show_all_btn.setTypeface(show_all_btn.getTypeface(), Typeface.BOLD);
                show_day_btn.setBackgroundResource(R.drawable.btn_gray);
                show_day_btn.setTextSize(15);
                show_day_btn.setTypeface(show_day_btn.getTypeface(), Typeface.NORMAL);
                checking_all.setVisibility(View.VISIBLE);
                checking_day.setVisibility(View.GONE);
                checkall.setVisibility(View.VISIBLE);
                checkday.setVisibility(View.GONE);
                selected_flag = 0;
            }
        });
        show_day_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                show_day_btn.setBackgroundResource(R.drawable.btn_white);
                show_day_btn.setTextSize(18);
                show_day_btn.setTypeface(show_day_btn.getTypeface(), Typeface.BOLD);
                show_all_btn.setBackgroundResource(R.drawable.btn_gray);
                show_all_btn.setTextSize(15);
                show_all_btn.setTypeface(show_all_btn.getTypeface(), Typeface.NORMAL);
                checking_day.setVisibility(View.VISIBLE);
                checking_all.setVisibility(View.GONE);
                checkall.setVisibility(View.GONE);
                checkday.setVisibility(View.VISIBLE);
                selected_flag = 1;
                default_time = my_PL_available_time.split("\\{")[2].split(",")[0].split("\"")[3];
                reserved_count = my_PL_available_time.split("\\{")[2].split(",")[1].split(":")[1];
                for(int i = 0; i < Integer.parseInt(reserved_count); i++) {
                    if(my_PL_available_time.split("\\{")[i + 3].split(",")[2].split("\"")[3] == null){
                        break;
                    }
                    reserved_count_month[i] = my_PL_available_time.split("\\{")[i + 3].split(",")[2].split("\"")[3];
                    reserved_count_day[i] = my_PL_available_time.split("\\{")[i + 3].split(",")[3].split("\"")[3];
                    reserved_count_time[i] = my_PL_available_time.split("\\{")[i + 3].split(",")[4].split("\"")[3];
                    System.out.println(reserved_count_month[i] + " " + reserved_count_day[i] + " " + reserved_count_time[i]);
                }



                if (Integer.parseInt(reserved_count) == 0) {
                    Toast.makeText(ManagementTimeActivity.this,"예약된 날짜가 없으므로" + "\n" + "기본 시간 설정으로 이동합니다.", Toast.LENGTH_LONG).show();
                    show_all_btn.setBackgroundResource(R.drawable.btn_white);
                    show_all_btn.setTextSize(18);
                    show_all_btn.setTypeface(show_all_btn.getTypeface(), Typeface.BOLD);
                    show_day_btn.setBackgroundResource(R.drawable.btn_gray);
                    show_day_btn.setTextSize(15);
                    show_day_btn.setTypeface(show_day_btn.getTypeface(), Typeface.NORMAL);
                    checking_all.setVisibility(View.VISIBLE);
                    checking_day.setVisibility(View.GONE);
                    checkall.setVisibility(View.VISIBLE);
                    checkday.setVisibility(View.GONE);
                }
                else {
                    Intent intent = new Intent(getApplicationContext(), ManageCalendarActivity.class);
                    startActivity(intent);
                    for (count_time_day = 0; count_time_day < 48; count_time_day++) {
                        final String checkboxid_time = "management_checkbox_day_time_" + (count_time_day + 1);
                        String textviewid_time = "management_checkbox_day_text_" + (count_time_day + 1);
                        int cresID_time = getResources().getIdentifier(checkboxid_time, "id", getPackageName());
                        int tresID_time = getResources().getIdentifier(textviewid_time, "id", getPackageName());
                        Manage_checkBoxArrayList_day_time.add((CheckBox) findViewById(cresID_time));
                        Manage_textviewArrayList_day_text.add((TextView) findViewById(tresID_time));
                        for (int diff = 0; diff < Integer.parseInt(reserved_count); diff++) {
                            if (mYear == 0) {
                                //2020년 8월 27일
                                System.out.println(" 날짜 비교 : " + reserved_count_month[diff] + ", " + formatDate.split("년")[1].split("월")[0].replace(" ", "") +
                                        ", " + reserved_count_day[diff] + ", " + formatDate.split("월")[1].replace(" ", ""));
                                if (reserved_count_month[diff].equals(formatDate.split("년")[1].split("월")[0].replace(" ", ""))
                                        && reserved_count_day[diff].equals(formatDate.split("월")[1].replace(" ", ""))) {
                                    count_data_format = diff;
                                }
                            } else {
                                System.out.println(" 날짜 비교 : " + reserved_count_month[diff] + ", " + sMonth + ", " + reserved_count_day[diff] + ", " + sDay);
                                if (reserved_count_day[diff] == sMonth && reserved_count_day[diff] == sDay) {
                                    count_data_format = diff;
                                }
                            }

                        }
                        if (reserved_count_time[count_data_format].charAt(count_time_day) == '0' || reserved_count_time[count_data_format].charAt(count_time_day) == '1') {
                            Manage_checkBoxArrayList_day_time.get(count_time_day).setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener() {
                                int b = count_time_day;

                                @Override
                                public void onCheckedChanged(CompoundButton compoundButton, boolean bool) {
                                    if (bool == true) {
                                        compoundButton.setButtonDrawable(R.drawable.ic_booking_time_enable);
                                        Manage_textviewArrayList_day_text.get(b).setTextColor(Color.parseColor("#ffffff"));
                                        StringBuilder change_time = new StringBuilder(reserved_count_time[count_data_format]);
                                        change_time.setCharAt(b, 'A');
                                        System.out.println(b);
                                        reserved_count_time[count_data_format] = change_time.toString();
                                        System.out.println(change_time.toString());
                                    } else {
                                        compoundButton.setButtonDrawable(R.drawable.ic_booking_time_disable);
                                        Manage_textviewArrayList_day_text.get(b).setTextColor(Color.parseColor("#000000"));
                                        StringBuilder change_time = new StringBuilder(reserved_count_time[count_data_format]);
                                        change_time.setCharAt(b, 'B');
                                        reserved_count_time[count_data_format] = change_time.toString();
                                        tabbtn_tool = 0;
                                        checkday.setChecked(false);
                                        System.out.println(b);
                                        tabbtn_tool = 1;
                                        System.out.println(change_time.toString());
                                    }
                                    System.out.println(reserved_count_time[count_data_format]);
                                }
                            });

                        } else {
                            Manage_checkBoxArrayList_day_time.get(count_time_day).setChecked(false);
                            Manage_checkBoxArrayList_day_time.get(count_time_day).setButtonDrawable(R.drawable.ic_booking_time_cannot);
                            Manage_textviewArrayList_day_text.get(count_time_day).setTextColor(Color.parseColor("#ffffff"));
                            Manage_checkBoxArrayList_day_time.get(count_time_day).setVisibility(View.INVISIBLE);
                            Manage_textviewArrayList_day_text.get(count_time_day).setVisibility(View.INVISIBLE);
                        }

                    }
                }
            }
        });
        Button manage_calendar = findViewById(R.id.manage_btn_calendar);
        manage_calendar.setOnClickListener(new CheckBox.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ManageCalendarActivity.class);
                startActivity(intent);
            }
        });


        checkall.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton compoundButton, boolean bool) {
                if(checkall.isChecked() && tabbtn_tool == 1){
                    for(int i = 0; i < 48; i++) {
                        Manage_checkBoxArrayList_all_time.get(i).setChecked(true);
                        Manage_checkBoxArrayList_all_time.get(i).setButtonDrawable(R.drawable.ic_booking_time_enable);
                        Manage_checkBoxArrayList_all_time.get(i).setTextColor(Color.parseColor("#ffffff"));
                        checkall.setChecked(true);
                    }
                }

                else if(!checkall.isChecked() && tabbtn_tool == 1){
                    for(int i = 0; i < 48; i++) {
                        Manage_checkBoxArrayList_all_time.get(i).setChecked(false);
                        Manage_checkBoxArrayList_all_time.get(i).setButtonDrawable(R.drawable.ic_booking_time_disable);
                        Manage_checkBoxArrayList_all_time.get(i).setTextColor(Color.parseColor("#000000"));
                        checkall.setChecked(false);
                    }
                }

            }
        });
        checkday.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton compoundButton, boolean bool) {
                if(checkday.isChecked() && tabbtn_tool == 1){
                    for(int i = 0; i < 48; i++) {
                        Manage_checkBoxArrayList_day_time.get(i).setChecked(true);
                        Manage_checkBoxArrayList_day_time.get(i).setButtonDrawable(R.drawable.ic_booking_time_enable);
                        Manage_textviewArrayList_day_text.get(i).setTextColor(Color.parseColor("#ffffff"));
                        checkday.setChecked(true);

                    }
                }
                else if(!checkday.isChecked() && tabbtn_tool == 1){
                    for(int i = 0; i < 48; i++) {
                        Manage_checkBoxArrayList_day_time.get(i).setChecked(false);
                        Manage_checkBoxArrayList_day_time.get(i).setButtonDrawable(R.drawable.ic_booking_time_disable);
                        Manage_textviewArrayList_day_text.get(i).setTextColor(Color.parseColor("#000000"));
                        checkday.setChecked(false);
                    }
                }

            }
        });

//        Handler mHandler = new Handler();
//        mHandler.postDelayed(new Runnable(){
//            public void run() {
//
//            }
//        }, 1500);
        System.out.println(" mYear : " + mYear);
        management_confirm_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(selected_flag == 0){
                    Toast.makeText(ManagementTimeActivity.this,"설정되었습니다.", Toast.LENGTH_SHORT).show();
                    Postc.post_set_default_time(manage_parking_lot_id, "111111111111111111111111111111111111111111111111", str_Token);
                    finish();
                }
                else{
                    String reserv_time = reserved_count_time[count_data_format].replace("A", "1").replace("B", "0");
                    System.out.println(reserv_time);
                    if(mYear == 0){
                        Toast.makeText(ManagementTimeActivity.this,"설정되었습니다.", Toast.LENGTH_SHORT).show();
                        Postc.post_set_reserved(manage_parking_lot_id, secondDate.split("-")[0], secondDate.split("-")[1],
                                secondDate.split("-")[2], reserv_time, str_Token);
                        finish();
                    }
                    else {
                        Toast.makeText(ManagementTimeActivity.this,"설정되었습니다.", Toast.LENGTH_SHORT).show();
                        Postc.post_set_reserved(manage_parking_lot_id, sYear, sMonth, sDay, reserv_time, str_Token);
                        finish();
                    }
                }
            }
        });

        if(mYear == 0){
            show_day_btn.setText(formatDate);
        }
        else {
            show_day_btn.setText(sYear + "년 " + sMonth + "월 " + sDay + "일");
        }
    }

}
