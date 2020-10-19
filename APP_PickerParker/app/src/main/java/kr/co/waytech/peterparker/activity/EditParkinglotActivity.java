package kr.co.waytech.peterparker.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Html;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import gun0912.tedbottompicker.TedBottomPicker;
import io.reactivex.disposables.Disposable;
import kr.co.waytech.peterparker.R;
import kr.co.waytech.peterparker.WebViewActivity;

import static java.lang.Thread.sleep;
import static kr.co.waytech.peterparker.fragment.MapFragment.mlatitude;
import static kr.co.waytech.peterparker.fragment.MapFragment.mlongitude;
import static kr.co.waytech.peterparker.fragment.ParkingFragment.parking_adding_flag;
import static kr.co.waytech.peterparker.fragment.ProfileFragment.str_Token;
import static kr.co.waytech.peterparker.adapter.ParkingAdapter.manage_parking_lot_id;

public class EditParkinglotActivity  extends AppCompatActivity {

    private Disposable multiImageDisposable;
    private ViewGroup edit_mSelectedImagesContainer;
    private RequestManager requestManager;
    private ImageView edit_iv_image;
    private List<Uri> edit_selectedUriList;
    private Uri selectedUri;
    private EditText edit_et_address, edit_et_address_detail, edit_et_price, edit_et_name;
    public static double plongitude = 0.0;
    public static double platitude = 0.0;
    public static Bitmap edit_PL_img1, edit_PL_img2, edit_PL_img3, edit_PL_img4;
    final PostClass Postc = new PostClass();

    private static final int SEARCH_ADDRESS_ACTIVITY = 10000;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_parkinglot);

        final LocationManager locationManagerm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        edit_iv_image = findViewById(R.id.edit_iv_image_);
        edit_mSelectedImagesContainer = findViewById(R.id.edit_selected_photos_container);
        requestManager = Glide.with(this);
        SharedPreferences sharedPreferences = getSharedPreferences("login", MODE_PRIVATE);
        str_Token = sharedPreferences.getString("token", "");

        getSupportActionBar().setTitle(Html.fromHtml("<font color='#000000'>내 주차장 수정</font>"));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setElevation(1);


        setMultiShowButton();


        edit_et_address = (EditText)findViewById(R.id.edit_et_address);
        edit_et_address_detail = (EditText)findViewById(R.id.edit_et_address_detail);
        edit_et_price = (EditText)findViewById(R.id.edit_et_price);
        edit_et_name = (EditText)findViewById(R.id.edit_et_name);
        Button edit_btn_search = findViewById(R.id.edit_btn_find_address);
        Button edit_btn_delete = findViewById(R.id.edit_btn_delete_PL);
        if (edit_btn_search != null)
            edit_btn_search.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(EditParkinglotActivity.this, WebViewActivity.class);
                    startActivityForResult(i, SEARCH_ADDRESS_ACTIVITY);
                }
            });
        edit_btn_delete.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Postc.Post_delete_PL(manage_parking_lot_id, str_Token);
                finish();
            }
        });

    }

    //actionbar menu listener
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection

        switch (item.getItemId()) {
            case R.id.menu_update:

                if(str_Token.length() > 10){
                    try {
                        edit_PL_img1 = MediaStore.Images.Media.getBitmap(getContentResolver(), edit_selectedUriList.get(0));
                        edit_PL_img2 = MediaStore.Images.Media.getBitmap(getContentResolver(), edit_selectedUriList.get(1));
                        edit_PL_img3 = MediaStore.Images.Media.getBitmap(getContentResolver(), edit_selectedUriList.get(2));
                        edit_PL_img4 = MediaStore.Images.Media.getBitmap(getContentResolver(), edit_selectedUriList.get(3));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    parking_adding_flag = 1;
                    Postc.post_update_parking_lot(manage_parking_lot_id, edit_et_name.getText().toString(),
                            edit_et_address.getText().toString().split(",")[1].replace(" ", "") +
                            edit_et_address_detail.getText().toString(), edit_PL_img1, edit_PL_img2, edit_PL_img3, edit_PL_img4,
                            edit_et_price.getText().toString(), str_Token);
                    set_aftereditParkingView();
                    Toast.makeText(EditParkinglotActivity.this,"정보 수정이 되었습니다." ,Toast.LENGTH_SHORT).show();
                }
                else if(str_Token.length() < 10){
                    Toast.makeText(EditParkinglotActivity.this,"로그인이 필요합니다." ,Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(EditParkinglotActivity.this,"오류 발생" ,Toast.LENGTH_SHORT).show();
                }
                //Add Register Button Listener Here
                //Send Data to Server Here
                EditParkinglotActivity.this.finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actionbar_menu2, menu);
        return true;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent){
        super.onActivityResult(requestCode, resultCode, intent);
        switch(requestCode){
            case SEARCH_ADDRESS_ACTIVITY:
                if(resultCode == RESULT_OK){
                    String data = intent.getExtras().getString("data");
                    if (data != null)
                        edit_et_address.setText(data);
                }
                break;
        }
    }

    private void setMultiShowButton() {

        Button btnMultiShow = findViewById(R.id.edit_btn_add_pic);
        btnMultiShow.setOnClickListener(view -> {

            PermissionListener permissionlistener = new PermissionListener() {
                @Override
                public void onPermissionGranted() {

                    TedBottomPicker.with(EditParkinglotActivity.this)
                            //.setPeekHeight(getResources().getDisplayMetrics().heightPixels/2)
                            .setPeekHeight(1600)
                            .showTitle(false)
                            .setCompleteButtonText("Done")
                            .setEmptySelectionText("No Select")
                            .setSelectedUriList(edit_selectedUriList)
                            .setSelectMaxCount(4)
                            .showMultiImage(uriList -> {
                                edit_selectedUriList = uriList;
                                showUriList(uriList);
                            });
                }
                @Override
                public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                    Toast.makeText(EditParkinglotActivity.this, "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
                }
            };
            checkPermission(permissionlistener);
        });
    }

    private void checkPermission(PermissionListener permissionlistener) {
        TedPermission.with(EditParkinglotActivity.this)
                .setPermissionListener(permissionlistener)
                .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .check();
    }

    private void showUriList(List<Uri> uriList) {
        // Remove all views before
        // adding the new ones.
        edit_mSelectedImagesContainer.removeAllViews();

        edit_iv_image.setVisibility(View.GONE);
        edit_mSelectedImagesContainer.setVisibility(View.VISIBLE);

        int widthPixel = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, getResources().getDisplayMetrics());
        int heightPixel = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, getResources().getDisplayMetrics());

        for (Uri uri : uriList) {

            View imageHolder = LayoutInflater.from(this).inflate(R.layout.image_item, null);
            ImageView thumbnail = imageHolder.findViewById(R.id.media_image);

            requestManager
                    .load(uri.toString())
                    .apply(new RequestOptions().fitCenter())
                    .into(thumbnail);

            edit_mSelectedImagesContainer.addView(imageHolder);

            thumbnail.setLayoutParams(new FrameLayout.LayoutParams(widthPixel, heightPixel));
        }
    }

    @Override
    protected void onDestroy() {
        if (multiImageDisposable != null && !multiImageDisposable.isDisposed()) {
            multiImageDisposable.dispose();
        }
        super.onDestroy();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
    public void set_aftereditParkingView()  {
        while (Postc.body_update_parking_lot.length() < 5) {
            try {
                sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}