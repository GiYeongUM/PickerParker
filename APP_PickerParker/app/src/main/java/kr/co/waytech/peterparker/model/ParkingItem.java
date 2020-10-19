package kr.co.waytech.peterparker.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

public class ParkingItem implements ClusterItem {
    private LatLng location;
    private String address;

    public ParkingItem(LatLng location, String address) {
        this.location = location;
        this.address = address;
    }

    public void setLocation(LatLng location){
        this.location = location;
    }
    public String getAddress() {
        return address;
    }
    public void setAddress(String address){
        this.address = address;
    }

    @NonNull
    @Override
    public LatLng getPosition() {
        return location;
    }

    @Nullable
    @Override
    public String getTitle() {
        return null;
    }

    @Nullable
    @Override
    public String getSnippet() {
        return null;
    }
}
