package kr.co.waytech.peterparker.activity;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import android.content.SharedPreferences;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import java.io.IOException;

import kr.co.waytech.peterparker.R;
import kr.co.waytech.peterparker.fragment.ProfileFragment;

import static android.os.SystemClock.sleep;

public class LoginActivity extends AppCompatActivity {
    Button button_findID, button_findPW, button_signup, button_login;
    private EditText EdID, EdPW;
    private String VID, VPW;
    public static Activity loginActivity;
    private String loginToken;
    private String loginStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final PostClass Postc = new PostClass();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginActivity = LoginActivity.this;

        EdID = (EditText)findViewById(R.id.loginActivity_edittext_id);
        EdPW = (EditText)findViewById(R.id.loginActivity_edittext_password);

        getSupportActionBar().setTitle(Html.fromHtml("<font color='#000000'>로그인</font>"));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setElevation(1);

        //아이디 찾기
        button_findID = findViewById(R.id.btn_find_id);
        button_findID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2 = new Intent(LoginActivity.this, FineIDActivity.class);
                startActivity(intent2);
            }
        });

        //패스워드 찾기
        button_findPW = findViewById(R.id.btn_find_pw);
        button_findPW.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent3 = new Intent(LoginActivity.this, FinePWActivity.class);
                startActivity(intent3);
            }
        });

        //회원가입
        button_signup = findViewById(R.id.btn_signup);
        button_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent4 = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent4);
            }
        });

        //로그인
        button_login = findViewById(R.id.btn_login);
        button_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("로그인 버튼 클릭");
                VID = EdID.getText().toString();
                VPW = EdPW.getText().toString();
                System.out.println("get Text 완료");
                try {
                    Postc.send_login(VID, VPW);
                    System.out.println("send_login 완료");
                    System.out.println("loginStatus = "+ Postc.status);
                    while (Postc.status.equals("none")){ sleep(1);}
                    System.out.println("토큰 받아옴");
                    loginToken = Postc.realtoken;
                    loginStatus = Postc.status;
                    System.out.println("토큰 저장함");
                    System.out.println("loginStatus = "+loginStatus);
                    if (loginStatus.equals("success")) {
                        System.out.println("loginStatus success 뜸");
                        Toast.makeText(LoginActivity.this, "로그인 성공", Toast.LENGTH_SHORT).show();
                        //SharedPreferences에 값 저장.
                        SharedPreferences sharedPreferences = getSharedPreferences("login", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit(); //SP를 제어할 editor를 선언
                        editor.putString("token", loginToken);
                        editor.commit();
                        ProfileFragment.set_afterLoginView(loginToken); //프레그먼트 화면 새로고침
                        System.out.println("프레그먼트 화면 새로고침");

                    } else {
//                        Toast.makeText(LoginActivity.this, "로그인 실패", Toast.LENGTH_SHORT).show();
                        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                        builder.setTitle("로그인 실패")
                                .setMessage("아이디, 비밀번호를 확인해주세요.")
                                .setCancelable(false)
                                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        // 확인 눌렀을 떄 반응
                                        loginToken = "";
                                        loginStatus = "";
                                        Postc.status = "none";
                                    }
                                });
                        //Creating dialog box
                        AlertDialog dialog  = builder.create();
                        dialog.show();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    public void Success_Login() throws IOException, InterruptedException {

        //로그인 액티비티 종료
        loginActivity.finish();

    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

}
