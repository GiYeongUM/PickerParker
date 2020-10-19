package kr.co.waytech.peterparker.activity;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import java.util.regex.Pattern;

import kr.co.waytech.peterparker.R;

public class SignupActivity2 extends AppCompatActivity {
    protected static int Data_Type_Flag = 1;
    Button SignupBtn;

    private static EditText EdEM, EdPW, EdID, EdNM, EdNN, EdPN, EdCN;
    private static String VID, VPW, VNM, VEM, VNN, VPN, VCN;
    public static Activity SignupActivity2;
    public static boolean SignupableID, SignupablePW, SignupableNM, SignupableEM, SignupableNN, SignupablePN, SignupableCN = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final PostClass Postc = new PostClass();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup2);
        SignupActivity2 = SignupActivity2.this;
        EdEM = (EditText)findViewById(R.id.signupActiviy_edittext_email);
        EdID = (EditText)findViewById(R.id.signupActiviy_edittext_ID);
        EdNN = (EditText)findViewById(R.id.signupActiviy_edittext_nickname);
        EdPN = (EditText)findViewById(R.id.signupActiviy_edittext_phonenumber);
        EdCN = (EditText)findViewById(R.id.signupActiviy_edittext_carnumber);
        EdNM = (EditText)findViewById(R.id.signupActiviy_edittext_name);
        EdPW = (EditText)findViewById(R.id.signupActiviy_edittext_password);
        regularTextID();
        regularTextEmail();
        regularTextCar();
        regularTextNick();
        regularTextPhone();
        regularTextPW();
        regularTextName();

        SignupBtn =(Button)findViewById(R.id.signupActiviy_button_signup);
        SignupBtn.setOnClickListener(new View.OnClickListener()
        {
            public void onClick (View v) {
                if(SignupableID == true && SignupablePW == true && SignupableNM == true && SignupableEM == true &&
                        SignupableNN == true && SignupablePN == true && SignupableCN == true) {
                    VID = EdID.getText().toString();
                    VPW = EdPW.getText().toString();
                    VNM = EdNM.getText().toString();
                    VEM = EdEM.getText().toString();
                    VNN = EdNN.getText().toString();
                    VPN = EdPN.getText().toString();
                    VCN = EdCN.getText().toString();
                    Postc.send_signup(VEM, VID, VNM, VPW, VNN, VPN, VCN);
                    Success_Signup();
                }
                else
                    System.out.println("error");
            }
        });
    }
    public void Success_Signup(){
        SignupActivity2.finish();
    } //String strID, String strPW, String strEM, String strPN, String strCN
    public static void regularTextID(){
        EdID.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String textID = s.toString();
                if (Pattern.matches("^[a-zA-Z0-9]*$", textID) && textID.length() >=6 && textID.length() <= 10) {
                    Data_Type_Flag = 1;
                }
                else {
                    Data_Type_Flag = 0;
                    EdID.setError("영문/숫자 6~10자를 입력해주세요.");
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {
                if (Data_Type_Flag == 1) {
                    SignupableID = true;
                    System.out.println("ID true");
                } else {
                    SignupableID = false;
                    System.out.println("ID false");
                }
            }
        });
    }

    public static void regularTextNick(){
        EdNN.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String textNick = s.toString();
                if(Pattern.matches("^[a-zA-Z0-9가-힣]*$", textNick) && textNick.length() <= 6){
                    Data_Type_Flag = 1;
                }
                else {
                    EdNN.setError("6자까지 입력가능합니다.");
                    Data_Type_Flag = 0;
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(Data_Type_Flag == 1) {
                    SignupableNN = true;
                    System.out.println("NN true");
                }
                else {
                    SignupableNN = false;
                    System.out.println("NN false");
                }
            }
        });
    }

    public static void regularTextPW(){
        EdPW.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {   }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String textPW = s.toString();
                if(Pattern.matches("^(?=.*[0-9])(?=.*[a-zA-Z]).{8,12}$", textPW)){
                    Data_Type_Flag = 1;
                }
                else {
                    EdPW.setError("영문/숫자 8~12자를 입력해주세요.");
                    Data_Type_Flag = 0;
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(Data_Type_Flag == 1) {
                    SignupablePW = true;
                    System.out.println("PW true");
                }

                else {
                    SignupablePW = false;
                    System.out.println("PW false");
                }
            }
        });
    }

    public static void regularTextEmail() {
        EdEM.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String textEmail = s.toString();
                if (Pattern.matches("(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])", textEmail)) {
                    Data_Type_Flag = 1;
                } else {
                    EdEM.setError("올바른 이메일 형식을 입력해주세요.");
                    Data_Type_Flag = 0;
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (Data_Type_Flag == 1) {
                    SignupableEM = true;
                    System.out.println("EM true");
                } else {
                    SignupableEM = false;
                    System.out.println("EM false");
                }
            }
        }
    );
    }



    public static void regularTextPhone(){
        EdPN.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {      }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String textPhone = s.toString();
                if(Pattern.matches("^01(?:0|1|[6-9])(?:\\d{3}|\\d{4})\\d{4}+$", textPhone) && textPhone.length() <= 11 && textPhone.length() >= 3){
                    Data_Type_Flag = 1;
                }
                else {
                    EdPN.setError("올바른 전화번호 형식을 입력해주세요.(- 제외)");
                    Data_Type_Flag = 0;
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(Data_Type_Flag == 1){
                    SignupablePN = true;
                System.out.println("PN true");
            }
                else{
                    SignupablePN = false;
                    System.out.println("PN false");
                }
            }
        });

    }

    public static void regularTextCar(){
        EdCN.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String textCar = s.toString();
                if(Pattern.matches("([0-9]{2,3})+([가-힣]{1,1})+([0-9]{4})", textCar)){
                    Data_Type_Flag = 1;
                }
                else {
                    EdCN.setError("올바른 차번호 형식을 입력해주세요.");
                    Data_Type_Flag = 0;
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(Data_Type_Flag == 1){
                    SignupableCN = true;
                    System.out.println("CN true");
                }
                else{
                    SignupableCN = false;
                System.out.println("CN false");
            }
            }
        });
    }

    public static void regularTextName(){
        EdNM.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                VNM = EdNM.getText().toString();
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable editable) {

                if(VNM.length() > 0){
                    Data_Type_Flag = 1;
                }
                else {
                    EdNM.setError("이름을 입력해주세요.");
                    Data_Type_Flag = 0;
                }
                if(Data_Type_Flag == 1){
                    SignupableNM = true;
                System.out.println("NM true");
            }
                else{
                    SignupableNM = false;
                System.out.println("NM false");
            }
            }
        });
    }
}


