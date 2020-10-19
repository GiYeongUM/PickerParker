package kr.co.waytech.peterparker.model;

public class ReservedBookingList {

    String parkinglotid;
    String price;
    String addprice;
    String time;
    String intime;
    String outtime;
    String nickname;
    String phone;
    String car;
    String img;

    public ReservedBookingList(String parkinglotid, String price, String addprice, String time, String intime,
                               String outtime, String nickname, String phone, String car, String img) {
        this.parkinglotid = parkinglotid;
        this.price = price;
        this.addprice = addprice;
        this.time = time;
        this.intime = intime;
        this.outtime = outtime;
        this.nickname = nickname;
        this.phone = phone;
        this.car = car;
        this.img = img;
    }

    public String getParkinglotid() {
        return parkinglotid;
    }

    public void setParkinglotid(String parkinglotName) {
        this.parkinglotid = parkinglotName;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getAddprice() {
        return addprice;
    }

    public void setAddprice(String addprice) { this.addprice = addprice; }

    public String getTime() {
        return time;
    }

    public void setTime(String time) { this.time = time; }

    public String getIntime() {
        return intime;
    }

    public void setIntime(String intime) { this.intime = intime; }

    public String getOuttime() {
        return outtime;
    }

    public void setOuttime(String outtime) {
        this.outtime = outtime;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPhone() { return phone; }

    public void setPhone(String phone) { this.phone = phone; }

    public String getCar() { return car;    }

    public void setCar(String car) { this.car = car;    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}
