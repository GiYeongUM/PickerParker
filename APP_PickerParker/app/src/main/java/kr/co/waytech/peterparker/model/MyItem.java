package kr.co.waytech.peterparker.model;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

import kr.co.waytech.peterparker.R;

public class MyItem implements ClusterItem {
    private final LatLng mPosition;
    public final String mTitle;
    private final int mPrice;
    public static String rID;
    private final BitmapDescriptor mbitmapDescriptor;
    public static BitmapDescriptor bitmapDescriptor;

    public MyItem(String slat, String slng, String title, String sprice) {

        bitmapDescriptor = BitmapDescriptorFactory.fromResource(R.drawable.googlemap_pin);
        double pLat = Double.parseDouble(slat);
        double pLng = Double.parseDouble(slng);
        int price = Integer.parseInt(sprice);
        mPosition = new LatLng(pLat, pLng);
        mTitle = title;
        mPrice = price;
        mbitmapDescriptor = bitmapDescriptor;
    }

    @NonNull
    @Override
    public LatLng getPosition() {
        return mPosition;
    }

    @Nullable
    @Override
    public String getTitle() {
        return mTitle;
    }

    public int getPrice(){
        return mPrice;
    }

    @Nullable
    @Override
    public String getSnippet() {
        return null;
    }

    public static String returnID(){
        return rID;
    }

    public BitmapDescriptor getIcon(BitmapDescriptor bitmapDescriptor) {
        return bitmapDescriptor;
    }


}