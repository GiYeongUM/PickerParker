package kr.co.waytech.peterparker.model;

import android.graphics.drawable.Drawable;

public class Data {

    private String Address;
    private String content_Price;
    private String id;
    private String Distance;
    private int resId;
    public void setAddress(String Address) {
        this.Address = Address;
    }
    public String getAddress() { return Address; }
    public void setId(String id) { this.id = id; }
    public String getId() { return id; }
    public void setDistance(String Distance) { this.Distance = Distance; }
    public String getDistance() { return Distance; }
    public void setContent_Price(String content_Price) {
        this.content_Price = content_Price;
    }
    public String getContent_Price() { return content_Price; }
}