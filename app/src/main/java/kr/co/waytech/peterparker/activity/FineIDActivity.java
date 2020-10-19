package kr.co.waytech.peterparker.activity;

        import android.os.Bundle;
        import android.text.Html;

        import androidx.appcompat.app.AppCompatActivity;

        import kr.co.waytech.peterparker.R;

public class FineIDActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_findid);

        getSupportActionBar().setTitle(Html.fromHtml("<font color='#000000'>아이디 찾기</font>"));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setElevation(1);

    }


    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
