package kr.co.waytech.peterparker.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Network;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.content.SharedPreferences;
import android.widget.ViewFlipper;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AlertDialog.Builder;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.BreakIterator;

import kr.co.waytech.peterparker.BeaconService;
import kr.co.waytech.peterparker.DownloadImageTask;
import kr.co.waytech.peterparker.activity.LoginActivity;
import kr.co.waytech.peterparker.activity.MainActivity;
import kr.co.waytech.peterparker.activity.PointActivity;
import kr.co.waytech.peterparker.activity.PostClass;
import kr.co.waytech.peterparker.R;
import kr.co.waytech.peterparker.activity.SignupActivity;

import static android.content.Context.MODE_PRIVATE;
import static java.lang.Thread.sleep;


public class ProfileFragment extends android.app.Fragment {

    private boolean Auto;
    private boolean auto = false;
    public boolean isChecked;
    private BeaconService bService;
    private boolean isBind;
    public static final String PREFS_NAME = "vibration";
    public static final String PREF_VIBRATION = "TicVib";

    private static SharedPreferences sharedPreferences;
    private static FragmentActivity activity;
    private File tempFile;
    private static final int PICK_FROM_ALBUM = 10;
    public static String str_Token;
    static final PostClass Postc = new PostClass();
    public static String user_id, nick_name, user_name, uuid, point, email, phone_number, car_number, profile_image, viewpoint;
    static ImageView iv_icon;
    public static TextView tv_nickname, tv_email, tv_point;
    public static View view_beforeLogin;
    public static View view_afterLogin;

    ServiceConnection sconn = new ServiceConnection() {
        @Override //서비스가 실행될 때 호출
        public void onServiceConnected(ComponentName name, IBinder service) {
            BeaconService.MyBinder myBinder = (BeaconService.MyBinder) service;
            bService = myBinder.getService();
            isBind = true;
        }

        @Override //서비스가 종료될 때 호출
        public void onServiceDisconnected(ComponentName name) {
            bService = null;
            isBind = false;
        }
    };

    public ProfileFragment()
    {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

    }


    Button LoginBtn, UploadBtn, btn_getToken, DeleteAccountBtn, btn_logout, test_btn, btn_goMypoint;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container,false);

        activity = (FragmentActivity)view.getContext();
        FragmentManager manager = activity.getSupportFragmentManager();

        tv_nickname = view.findViewById(R.id.tv_nickname);
        tv_email = view.findViewById(R.id.tv_email);
        tv_point = view.findViewById(R.id.tv_point);


        sharedPreferences = getActivity().getSharedPreferences("login", MODE_PRIVATE);
        str_Token = sharedPreferences.getString("token", "");

//        SharedPreferences.Editor editor = sharedPreferences.edit(); //SP를 제어할 editor를 선언
//        editor.clear();
//        editor.commit();
//        Toast.makeText(getContext(), "토큰을 삭제하였습니다", Toast.LENGTH_SHORT).show();

        SharedPreferences preferences = getActivity().getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        Auto = preferences.getBoolean(PREF_VIBRATION, true);

        Switch sw = (Switch) view.findViewById(R.id.switch1);

        sw.setChecked(Auto);
        sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    getActivity().startService(new Intent(getActivity(), BeaconService.class)); // 서비스 시작
                    SharedPreferences.Editor editor=getActivity().getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit();
                    editor.putBoolean(PREF_VIBRATION, isChecked);
                    editor.apply();

                } else {
                    getActivity().stopService(new Intent(getActivity(), BeaconService.class)); // 서비스 시작
                    SharedPreferences.Editor editor=getActivity().getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit();
                    editor.putBoolean(PREF_VIBRATION, isChecked);
                    editor.apply();
                }
            }
        });

        view_beforeLogin = view.findViewById(R.id.view_beforeLogin);
        view_afterLogin = view.findViewById(R.id.view_afterLogin);

        iv_icon = view.findViewById(R.id.img_profile);
        iv_icon.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK); //ACTION_PICK 사진 가져오는 것
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                startActivityForResult(intent, PICK_FROM_ALBUM); // 리퀘스트코드값을 넘겨줌, 내가 원하는 부분으로 이벤트 이동
                //결과값은 onActivityResult로

            }
        });

        if (str_Token.length() > 20){
//            Toast.makeText(getContext(), "토큰이 존재", Toast.LENGTH_SHORT).show();
            try {
                set_afterLoginView(str_Token);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {
//            Toast.makeText(getContext(), "토큰이 존재하지 않습니다.", Toast.LENGTH_SHORT).show();
            view_beforeLogin.setVisibility(View.VISIBLE);
            view_afterLogin.setVisibility(View.GONE);
        }


        LoginBtn = view.findViewById(R.id.LoginButton);
        LoginBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                Intent intentLoadNewActivity = new Intent(getActivity(), LoginActivity.class);
                startActivity(intentLoadNewActivity);
            }
        });


        btn_goMypoint = view.findViewById(R.id.btn_goMypoint);
        btn_goMypoint.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                PointActivity.point = point;
                Intent intentPointActivity = new Intent(getActivity(), PointActivity.class);
                System.out.println("인텐트 OK");
                startActivity(intentPointActivity);
            }
        });


        test_btn = view.findViewById(R.id.test_btn);
        test_btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                try {
                    Postc.Get_profile(str_Token);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });


        btn_logout = view.findViewById(R.id.btn_logout);
        btn_logout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("로그아웃")
                        .setMessage("로그아웃 하시겠습니까?")
                        .setCancelable(false)
                        .setPositiveButton("네", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("login", MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit(); //SP를 제어할 editor를 선언
                                editor.clear();
                                editor.commit();
//                                Toast.makeText(getContext(), "토큰을 삭제하였습니다", Toast.LENGTH_SHORT).show();
                                Success_logout();
                                set_afterLogoutView();
                            }
                        })
                        .setNegativeButton("아니요", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //select No
                            }
                        });
                //Creating dialog box
                AlertDialog dialog  = builder.create();
                dialog.show();

            }
        });


        DeleteAccountBtn = view.findViewById(R.id.delete_account_Button);
        DeleteAccountBtn.setOnClickListener(new View.OnClickListener()
        {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("회원탈퇴")
                        .setMessage("정말로 탈퇴 하시겠습니까.")
                        .setCancelable(false)
                        .setPositiveButton("네", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                try {
                                    Postc.Delete_account(str_Token);
                                    if(PostClass.responseCode == 401) {
//                        Toast.makeText(SignupActivity.this, "실패", Toast.LENGTH_SHORT).show();
                                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                        builder.setTitle("회원탈퇴")
                                                .setMessage("소유 주차장에 예약된 내역이 존재하여 탈퇴할 수 없습니다.")
                                                .setCancelable(false)
                                                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        // 확인 눌렀을 떄 반응
                                                    }
                                                });
                                        //Creating dialog box
                                        AlertDialog dialog2  = builder.create();
                                        dialog2.show();
                                    }
                                    else set_afterLogoutView();

                                } catch (IOException e) {
                                    e.printStackTrace();

                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        })
                        .setNegativeButton("아니요", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(getContext(),"Selected Option: No",Toast.LENGTH_SHORT).show();
                            }
                        });
                //Creating dialog box
                AlertDialog dialog  = builder.create();
                dialog.show();
            }
        });

        return view;
    }

    //화면 로그인 이후 화면으로 셋팅
    public static void set_afterLoginView(String token) throws IOException{

        try {
            Postc.Get_profile(token);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        if (PostClass.responseCode3 == 401) {
            SharedPreferences.Editor editor = sharedPreferences.edit(); //SP를 제어할 editor를 선언
            editor.clear();
            editor.commit();

            view_beforeLogin.setVisibility(View.VISIBLE);
            view_afterLogin.setVisibility(View.GONE);
        } else {
            view_beforeLogin.setVisibility(View.GONE);
            view_afterLogin.setVisibility(View.VISIBLE);
            while (profile_image.length() < 5) {
                try {
                    sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if (profile_image.length() > 5) {
                tv_nickname.setText(nick_name);
                System.out.println("tv_nickname 변경 완료");
                profile_image = profile_image.replace("\\/", "/");
                System.out.println("profile_image 은 " + profile_image);
                int origin_point = Integer.parseInt(point);
                viewpoint = String.format("%,d", origin_point);
                tv_email.setText(email);
                System.out.println(email + ", " + viewpoint);
                tv_point.setText(viewpoint);

                URL url = new URL(profile_image);
                URLConnection conn = url.openConnection();
                conn.connect();
                BufferedInputStream bis = new BufferedInputStream(conn.getInputStream());
                Bitmap bm = BitmapFactory.decodeStream(bis);
                bis.close();
                iv_icon.setImageBitmap(bm);
                System.out.println("이미지 변경 완료");



                activity.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {

                        view_beforeLogin.setVisibility(View.GONE);
                        view_afterLogin.setVisibility(View.VISIBLE);

                    }
                });


            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == PICK_FROM_ALBUM) {

            Uri photoUri = data.getData();

            Cursor cursor = null;

            try {

                /*
                 *  Uri 스키마를
                 *  content:/// 에서 file:/// 로  변경한다.
                 */
                String[] proj = {MediaStore.Images.Media.DATA};

                assert photoUri != null;
                cursor = getActivity().getContentResolver().query(photoUri, proj, null, null, null);

                assert cursor != null;
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

                cursor.moveToFirst();

                tempFile = new File(cursor.getString(column_index));

            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }

            try {
                setImage();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }

    private void setImage() throws IOException, InterruptedException {

        BitmapFactory.Options options = new BitmapFactory.Options();
        Bitmap originalBm = BitmapFactory.decodeFile(tempFile.getAbsolutePath(), options);

        while(originalBm == null) {sleep(1);}
        System.out.println(originalBm);

        Postc.Change_ProImg(str_Token, originalBm);


    }

        //화면 로그아웃 이후 화면으로 셋팅
    public void set_afterLogoutView() {

        user_id = null;
        nick_name = null;
        user_name = null;
        uuid = null;
        point = null;
        email = null;
        phone_number = null;
        car_number = null;
        profile_image = null;

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("login", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit(); //SP를 제어할 editor를 선언
        editor.clear();
        editor.commit();

        view_beforeLogin.setVisibility(View.VISIBLE);
        view_afterLogin.setVisibility(View.GONE);


    }

    public void Success_logout(){
        profile_image = null;
    }

    private void refresh(){
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.detach(this).attach(this).commit();
    }


}

