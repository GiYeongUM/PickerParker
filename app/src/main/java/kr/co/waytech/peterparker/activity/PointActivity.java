package kr.co.waytech.peterparker.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

import kr.co.waytech.peterparker.R;
import kr.co.waytech.peterparker.fragment.ProfileFragment;

import static java.lang.Thread.sleep;

public class PointActivity extends AppCompatActivity {

    public static String point = "";
    public static String viewpoint;
    public static TextView tv_mypoint;
    Button btn_getCoin, btn_getCoins, btn_getCoinss, btn_getCoinsss;
    private String str_Token;
    final PostClass Postc = new PostClass();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_point);

        SharedPreferences sharedPreferences = this.getSharedPreferences("login", MODE_PRIVATE);
        str_Token = sharedPreferences.getString("token", "");

        getSupportActionBar().setTitle(Html.fromHtml("<font color='#000000'>포인트</font>"));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setElevation(1);

        tv_mypoint = findViewById(R.id.tv_mypoint);
        System.out.println(point);

        int origin_point = Integer.parseInt(point);
        viewpoint = String.format("%,d", origin_point);

        tv_mypoint.setText(viewpoint);


        btn_getCoin = findViewById(R.id.btn_buy_coin);
        btn_getCoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(PointActivity.this);
                builder.setTitle("포인트 충전")
                        .setMessage("5,000 포인트를 충전합니다.")
                        .setCancelable(false)
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                try {
                                    Postc.send_getcash("5000", str_Token);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                                try {
                                    ProfileFragment.set_afterLoginView(str_Token);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                                int origin_point = Integer.parseInt(point);
                                int new_point = origin_point + 5000;
                                point = Integer.toString(new_point);
                                viewpoint = String.format("%,d", new_point);
                                tv_mypoint.setText(viewpoint);

                            }
                        })
                        .setNegativeButton("아니요", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });

                //Creating dialog box
                AlertDialog dialog  = builder.create();
                dialog.show();


            }
        });

        btn_getCoins = findViewById(R.id.btn_buy_coins);
        btn_getCoins.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(PointActivity.this);
                builder.setTitle("포인트 충전")
                        .setMessage("10,000 포인트를 충전합니다.")
                        .setCancelable(false)
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                try {
                                    Postc.send_getcash("10000", str_Token);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                                try {
                                    ProfileFragment.set_afterLoginView(str_Token);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                                int origin_point = Integer.parseInt(point);
                                int new_point = origin_point + 10000;
                                point = Integer.toString(new_point);
                                viewpoint = String.format("%,d", new_point);
                                tv_mypoint.setText(viewpoint);

                            }
                        })
                        .setNegativeButton("아니요", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });

                //Creating dialog box
                AlertDialog dialog  = builder.create();
                dialog.show();

            }
        });

        //회원가입
        btn_getCoinss = findViewById(R.id.btn_buy_coinss);
        btn_getCoinss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(PointActivity.this);
                builder.setTitle("포인트 충전")
                        .setMessage("30,000 포인트를 충전합니다.")
                        .setCancelable(false)
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                try {
                                    Postc.send_getcash("30000", str_Token);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                                try {
                                    ProfileFragment.set_afterLoginView(str_Token);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                                int origin_point = Integer.parseInt(point);
                                int new_point = origin_point + 30000;
                                point = Integer.toString(new_point);
                                viewpoint = String.format("%,d", new_point);
                                tv_mypoint.setText(viewpoint);

                            }
                        })
                        .setNegativeButton("아니요", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });

                //Creating dialog box
                AlertDialog dialog  = builder.create();
                dialog.show();

            }
        });

        //회원가입
        btn_getCoinsss = findViewById(R.id.btn_buy_coinsss);
        btn_getCoinsss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(PointActivity.this);
                builder.setTitle("포인트 충전")
                        .setMessage("50,000 포인트를 충전합니다.")
                        .setCancelable(false)
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                try {
                                    Postc.send_getcash("50000", str_Token);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                                try {
                                    ProfileFragment.set_afterLoginView(str_Token);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                                int origin_point = Integer.parseInt(point);
                                int new_point = origin_point + 50000;
                                point = Integer.toString(new_point);
                                viewpoint = String.format("%,d", new_point);
                                tv_mypoint.setText(viewpoint);

                            }
                        })
                        .setNegativeButton("아니요", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });

                //Creating dialog box
                AlertDialog dialog  = builder.create();
                dialog.show();

            }
        });



    }



    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
