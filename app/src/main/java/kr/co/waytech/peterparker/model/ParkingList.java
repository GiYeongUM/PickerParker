package kr.co.waytech.peterparker.model;

public class ParkingList {

    String parkinglotName;
    String parkinglotAddress;
    String parkinglotPrice;
    String imageUrl;
    String PL_ID;

    public ParkingList(String parkinglotName, String parkinglotAddress, String parkinglotPrice, String imageUrl, String PL_ID) {
        this.parkinglotName = parkinglotName;
        this.parkinglotAddress = parkinglotAddress;
        this.parkinglotPrice = parkinglotPrice;
        this.imageUrl = imageUrl;
        this.PL_ID = PL_ID;
    }

    public String getPL_ID() {return PL_ID;}

    public void setPL_ID(String PL_ID) {
        this.PL_ID = PL_ID;
    }

    public String getPLName() {
        return parkinglotName;
    }

    public void setPLName(String parkinglotName) {
        this.parkinglotName = parkinglotName;
    }

    public String getPLAddress() {
        return parkinglotAddress;
    }

    public void setPLAddress(String parkinglotAddress) { this.parkinglotAddress = parkinglotAddress; }

    public String getPLPrice() {
        return parkinglotPrice;
    }

    public void setPLPrice(String parkinglotPrice) { this.parkinglotPrice = parkinglotPrice; }

    public String getPLImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
