package kr.co.waytech.peterparker.adapter;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import kr.co.waytech.peterparker.DownloadImageTask;
import kr.co.waytech.peterparker.R;
import kr.co.waytech.peterparker.activity.PostClass;


public class BookingImgAdapter  extends PagerAdapter {

    Context context;
    final PostClass Postc = new PostClass();
    public static int count_img = 0;
    Bitmap imgBitmap = null;
    HttpURLConnection conn = null;
    BufferedInputStream bis = null;
    public static Drawable d;
    private LayoutInflater inflater;
    //private int[] defaultimages = {R.drawable.img1, R.drawable.img2, R.drawable.img3};

    public BookingImgAdapter(Context context){
        this.context = context;

    }


    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        // TODO Auto-generated method stub
        inflater = (LayoutInflater)context.getSystemService
                (Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.slider, container, false);
        //Postc.send_Location(ListAdapter.ID);
        String drawableRes= Postc.Parking_img[position];
        System.out.println(Postc.Parking_img[position]);
        //TextView textView = (TextView)v.findViewById(R.id.booking_img_text);
        View viewItem = inflater.inflate(R.layout.activity_bookinglist, container, false);
        ImageView imageView = (ImageView) viewItem.findViewById(R.id.booking_image);
        if(drawableRes == "null") {
           // imageView.setImageResource(images[position]);
            //default 이미지 설정
        }
        else{
            new DownloadImageTask((ImageView)v.findViewById(R.id.booking_image)).execute(drawableRes);
        }
        System.out.println("addview-------------");
        container.addView(v);
        return v;
    }
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return Postc.Parking_img.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        // TODO Auto-generated method stub

        return view == ((View)object);
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        // TODO Auto-generated method stub
        ((ViewPager) container).removeView((View) object);
    }

}
