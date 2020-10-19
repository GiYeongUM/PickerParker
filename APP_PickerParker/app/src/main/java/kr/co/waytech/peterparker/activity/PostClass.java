package kr.co.waytech.peterparker.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import com.google.android.gms.maps.model.Marker;

import kr.co.waytech.peterparker.fragment.ProfileFragment;
import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static java.lang.Thread.sleep;


public class PostClass {
    public static int responseCode = 0; //중복확인에서 리턴되는 코드 확인 위한
    public static int responseCode2 = 0; //중복확인에서 리턴되는 코드 확인 위한
    public static int responseCode3 = 0;
    public static int imgcount = 1;
    protected static String realtoken, bookingbody, body_profile;
    String status = "none";
    int Thread_Status = 3 ;
    int Login_Status = 3;
    public static int count, count_my_parking_lot, count_my_booking_list, count_my_parking_lot_reservation;
    public static Integer Count_Parkinglot;
    public static byte[] ImgByte1, ImgByte2, ImgByte3, ImgByte4;
    public static int[] ParkingPrice;
    public static double[] ParkingLat, ParkingLng;
    public static String[] Parking_img, Parking_phone;
    public static String[][] All_Parkinglot;
    public static String Avaible_time,  my_parking_lot_id, my_parking_lot_name,
            my_parking_lot_price, my_parking_lot_imageurl, my_parking_lot_address,
            my_booking_name, my_booking_address, my_booking_price, my_booking_time, my_booking_img,
            my_booking_id, one_parking_lot_name, my_PL_available_time,
            my_PL_reserved_id, my_PL_reserved_price, my_PL_reserved_time, my_PL_reserved_in, my_PL_reserved_out,
            my_PL_reserved_nick, my_PL_reserved_phone, my_PL_reserved_car, my_PL_reserved_add_price;
    public static String my_parking_lot = "A";
    public static String my_booking_list = "B";
    public static String status_add_parking_lot = "c";
    public static String body_update_parking_lot = "d";
    public static int b = 7;
    static File tempSelectFile;
    public static Marker ParkingMark;
    public static String body_parkinglot = "abc";
    public static String body_reserved_time_select, body_default_time_select, body_list_reserved_pl;
    LoginActivity LogA = new LoginActivity();
    SignupActivity SignupA = new SignupActivity();
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            Bundle bun = msg.getData();
            String serverHtml = bun.getString("Server_HTML");
        }
    };

    public void send_login(final String SID, final String SPassword) throws IOException {
        new Thread() {
            public void run() {
                Bundle bun = new Bundle();
                Message msg = handler.obtainMessage();
                msg.setData(bun);
                handler.sendMessage(msg);
                try {
                    Post_Login(SID, SPassword);
                    Improve_Status();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    Improve_Login();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    protected void send_signup(final String SEmail, final String SID, final String SName, final String SPassword,
                               final String SNick_Name, final String SPhone, final String SCar){
        new Thread() {
            public void run() {
                Bundle bun = new Bundle();
                Message msg = handler.obtainMessage();
                msg.setData(bun);
                handler.sendMessage(msg);
                try {
                    Post_Signup(SEmail, SID, SName, SPassword, SNick_Name, SPhone, SCar);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    protected void send_id_duplication (final String SID){
        new Thread() {
            public void run() {
                Bundle bun = new Bundle();
                Message msg = handler.obtainMessage();
                msg.setData(bun);
                handler.sendMessage(msg);
                try {
                    Post_id_duplication(SID);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    private void Post_id_duplication(String ID) throws IOException {
        OkHttpClient client = new OkHttpClient().newBuilder().build();
        MediaType mediaType = MediaType.parse("text/plain");
        RequestBody body = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("user_id", ID)
                .build();
        Request request = new Request.Builder()
                .url("http://blazingcode.asuscomm.com/api/check/id")
                .method("POST", body)
                .build();
        responseCode = 0;
        Response response = client.newCall(request).execute();
        String result = response.toString();
        System.out.println("Response Body is " + response);
        Log.d("Post_id_duplication", "Response Body is " + response);

        responseCode = response.code();

    }

    public void send_token(){
        new Thread() {
            public void run() {
                Bundle bun = new Bundle();
                Message msg = handler.obtainMessage();
                msg.setData(bun);
                handler.sendMessage(msg);
                try {
                    Post_token();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    public void Post_alllocation(){
        new Thread() {
            public void run() {
                Bundle bun = new Bundle();
                Message msg = handler.obtainMessage();
                msg.setData(bun);
                handler.sendMessage(msg);
                try {
                    getalllot();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    public void send_Img(){
        new Thread() {
            public void run() {
                Bundle bun = new Bundle();
                Message msg = handler.obtainMessage();
                msg.setData(bun);
                handler.sendMessage(msg);
                try {
                    Post_Img();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }


    public void send_Location(final String id){
        new Thread() {
            public void run() {
                Bundle bun = new Bundle();
                Message msg = handler.obtainMessage();
                msg.setData(bun);
                handler.sendMessage(msg);
                try {
                    Get_Location(id);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
    public void send_booking(final String syear, final String smonth, final String sday, final String shour, final String smin, final String eyear, final String emonth, final String eday
            , final String ehour, final String emin, final String ID, final String token){
        new Thread() {
            public void run() {
                Bundle bun = new Bundle();
                Message msg = handler.obtainMessage();
                msg.setData(bun);
                handler.sendMessage(msg);
                try {
                    post_booking(syear, smonth, sday, shour, smin, eyear, emonth, eday, ehour, emin, ID, token);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
    public void send_booking_time_id(final String ID, final String year, final String month, final String day){
        new Thread() {
            public void run() {
                Bundle bun = new Bundle();
                Message msg = handler.obtainMessage();
                msg.setData(bun);
                handler.sendMessage(msg);
                try {
                    post_avaible_booking_time(ID, year, month, day);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
    public void get_my_parking_lot(String token){
        new Thread() {
            public void run() {
                Bundle bun = new Bundle();
                Message msg = handler.obtainMessage();
                msg.setData(bun);
                handler.sendMessage(msg);
                try {
                    show_my_parking_lot(token);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
    public void get_my_booking_list(String token){
        new Thread() {
            public void run() {
                Bundle bun = new Bundle();
                Message msg = handler.obtainMessage();
                msg.setData(bun);
                handler.sendMessage(msg);
                try {
                    show_my_booking_list(token);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
    public void post_add_parking_lot(String id, String name, String address, String latitude, String longitude, String price, String token, Bitmap img1, Bitmap img2, Bitmap img3, Bitmap img4){
        new Thread() {
            public void run() {
                Bundle bun = new Bundle();
                Message msg = handler.obtainMessage();
                msg.setData(bun);
                handler.sendMessage(msg);
                try {
                    add_parking_lot(id, name, address, latitude, longitude, price, token, img1, img2, img3, img4);
                    System.out.println("do add_parking_lot");
                } catch (IOException e) {
                    e.printStackTrace();
                    System.out.println("do add_parking_lot");
                }
            }
        }.start();
    }
    public void post_cancel_booking(String bookingid, String token){
        new Thread() {
            public void run() {
                Bundle bun = new Bundle();
                Message msg = handler.obtainMessage();
                msg.setData(bun);
                handler.sendMessage(msg);
                try {
                    cancel_parking(bookingid, token);
                    System.out.println("cancel");
                } catch (IOException e) {
                    e.printStackTrace();
                    System.out.println("can't cancel");
                }
            }
        }.start();
    }
    public void send_avaible_time(String id, String token){
        new Thread() {
            public void run() {
                Bundle bun = new Bundle();
                Message msg = handler.obtainMessage();
                msg.setData(bun);
                handler.sendMessage(msg);
                try {
                    post_avaible_time(id, token);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
    public void post_set_reserved(String id, String year, String month, String day, String time, String token){
        new Thread() {
            public void run() {
                Bundle bun = new Bundle();
                Message msg = handler.obtainMessage();
                msg.setData(bun);
                handler.sendMessage(msg);
                try {
                    set_reserved(id, year, month, day, time, token);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    public void post_set_default_time(String id, String time, String token){
        new Thread() {
            public void run() {
                Bundle bun = new Bundle();
                Message msg = handler.obtainMessage();
                msg.setData(bun);
                handler.sendMessage(msg);
                try {
                    set_default_time(id, time, token);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    public void post_update_parking_lot(String id, String name, String address, Bitmap img1,
                                   Bitmap img2, Bitmap img3, Bitmap img4, String price, String token){
        new Thread() {
            public void run() {
                Bundle bun = new Bundle();
                Message msg = handler.obtainMessage();
                msg.setData(bun);
                handler.sendMessage(msg);
                try {
                    update_parking_lot(id, name, address,  img1, img2, img3, img4, price, token);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    public void Post_delete_PL(String id, String token){
        new Thread() {
            public void run() {
                Bundle bun = new Bundle();
                Message msg = handler.obtainMessage();
                msg.setData(bun);
                handler.sendMessage(msg);
                try {
                    delete_PL(id, token);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
    public void post_list_reservation(String id, String token){
        new Thread() {
            public void run() {
                Bundle bun = new Bundle();
                Message msg = handler.obtainMessage();
                msg.setData(bun);
                handler.sendMessage(msg);
                try {
                    get_list_reservation(id, token);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }


    private void Post_Login(String ID, String Password) throws IOException{
            OkHttpClient client = new OkHttpClient().newBuilder().build();
            MediaType mediaType = MediaType.parse("text/plain");
            RequestBody body = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("user_id", ID)
                    .addFormDataPart("password", Password)
                    .build();
            Request request = new Request.Builder()
                    .url("http://blazingcode.asuscomm.com/api/login")
                    .method("POST", body)
                    .build();
            Response response = client.newCall(request).execute();
            String body2 = response.body().string();
            String[] split_token = body2.split("\"");
            realtoken = split_token[7];
            status = split_token[3];
            System.out.println("Login status is.. " + status);
            System.out.println(realtoken);
    }

    public void send_getcash(String cash, String token) throws IOException {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        Request request = new Request.Builder()
                .url("http://blazingcode.asuscomm.com/api/point/"+cash)
                .method("GET", null)
                .addHeader("Authorization", "Bearer "+token)
                .build();
        Response response = client.newCall(request).execute();
    }

    private void Post_Signup(String Email, String ID, String Name, String Password, String Nick_Name, String Phone, String Car) throws IOException {
            OkHttpClient client = new OkHttpClient().newBuilder().build();
            MediaType mediaType = MediaType.parse("text/plain");
            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("email", Email)
                    .addFormDataPart("user_id", ID)
                    .addFormDataPart("name", Name)
                    .addFormDataPart("password", Password)
                    .addFormDataPart("nick_name", Nick_Name)
                    .addFormDataPart("phone_number", Phone)
                    .addFormDataPart("car_number", Car)
                    .build();
            Request request = new Request.Builder()
                    .url("http://blazingcode.asuscomm.com/api/register")
                    .post(requestBody)
                    .build();
            Call call = client.newCall(request);
            Response response = call.execute();
            System.out.println("Response Body is " + response.body().string());
    }

    private void Post_token() throws IOException{
        String token = "Bearer " + realtoken;

        OkHttpClient client = new OkHttpClient().newBuilder().build();
        Request request = new Request.Builder()
                .url("http://blazingcode.asuscomm.com/")
                .method("GET", null)
                .addHeader("Authorization", token)
                .build();
        Response response = client.newCall(request).execute();
        System.out.println("Response Body is " + response.body().string());
    }

    public synchronized void Improve_Status(){
        if(status.equals("error")) {
            Thread_Status = 0;
        }
        else if(status.equals("success")) {
            Thread_Status = 1;
        }
        else {
            //Toast
            Thread_Status = 2;
        }
    }
    public synchronized void Improve_Login() throws IOException, InterruptedException {
        if(Thread_Status == 1) {
           Login_Status = 1;
           LogA.Success_Login();
        }
        else {
            Login_Status = 0;
        }
    }
    private void Post_Img() throws IOException{
        String token = "Bearer " + realtoken;
        Bitmap orgImage = BitmapFactory.decodeFile("/sdcard/DCIM/Camera/20160528_115613.jpg");
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        orgImage.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] ImgByte = stream.toByteArray();

        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("text/plain");
        RequestBody body = null;
        switch (imgcount){
            case 1:
                body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                        .addFormDataPart("profile_image","20160528_115613.jpeg",
                                RequestBody.create(MediaType.parse("image/*"), ImgByte))
                        .build();
                break;
            case 2:
                body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                        .addFormDataPart("profile_image1","20160528_115613.jpeg",
                                RequestBody.create(MediaType.parse("image/*"), ImgByte))
                        .addFormDataPart("profile_image2","20160528_115613.jpeg",
                                RequestBody.create(MediaType.parse("image/*"), ImgByte))
                        .build();
                break;
            case 3:
                body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                        .addFormDataPart("profile_image1","20160528_115613.jpeg",
                                RequestBody.create(MediaType.parse("image/*"), ImgByte))
                        .addFormDataPart("profile_image2","20160528_115613.jpeg",
                                RequestBody.create(MediaType.parse("image/*"), ImgByte))
                        .addFormDataPart("profile_image3","20160528_115613.jpeg",
                                RequestBody.create(MediaType.parse("image/*"), ImgByte))
                        .build();
                break;
            case 4:
                body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                        .addFormDataPart("profile_image1","20160528_115613.jpeg",
                                RequestBody.create(MediaType.parse("image/*"), ImgByte))
                        .addFormDataPart("profile_image2","20160528_115613.jpeg",
                                RequestBody.create(MediaType.parse("image/*"), ImgByte))
                        .addFormDataPart("profile_image3","20160528_115613.jpeg",
                                RequestBody.create(MediaType.parse("image/*"), ImgByte))
                        .addFormDataPart("profile_image4","20160528_115613.jpeg",
                                RequestBody.create(MediaType.parse("image/*"), ImgByte))
                        .build();
                break;
            default:
        }
        Request request = new Request.Builder()
                .url("http://blazingcode.asuscomm.com/api/profile/image_upload")
                .method("POST", body)
                .addHeader("Authorization", token)
                .build();
        Response response = client.newCall(request).execute();
        System.out.println("Response Body is " + response.body().string());

    }

    public void Delete_account(String token) throws IOException, InterruptedException {

        System.out.println(token);
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("text/plain");
        RequestBody body = null;
        Request request = new Request.Builder()
                .url("http://blazingcode.asuscomm.com/api/withdrawal")
                .method("DELETE", body)
                .addHeader("Authorization", "Bearer " + token)
                .build();
        Response response = client.newCall(request).execute();
        System.out.println(response);
        responseCode2 = response.code();


    }

    public void Change_ProImg(String token, Bitmap proImg) throws IOException, InterruptedException {

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        proImg.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] ImgByte = stream.toByteArray();

        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("text/plain");
        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("profile_image","file",
                        RequestBody.create(MediaType.parse("application/octet-stream"),ImgByte))
                .build();
        Request request = new Request.Builder()
                .url("http://blazingcode.asuscomm.com/api/profile/image_upload")
                .method("POST", body)
                .addHeader("Authorization", "Bearer " + token)
                .build();
        Response response = client.newCall(request).execute();
        System.out.println(response);

        ProfileFragment.set_afterLoginView(token);

    }

    public void Get_profile(String token) throws IOException, InterruptedException {

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        while(token.length() < 10) { sleep(1);}
        System.out.println("Token is " + token);
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        Request request = new Request.Builder()
                .url("http://blazingcode.asuscomm.com/api/auth/user")
                .method("GET", null)
                .addHeader("Authorization", "Bearer "+token)
                .build();
        Response response = client.newCall(request).execute();
        body_profile = response.body().string();
        System.out.println("Response Body is " + body_profile);
        responseCode3 = response.code();
        System.out.println("Response Code is " + responseCode3);
        body_profile = body_profile.replace("{\"", "");
        body_profile = body_profile.replace("\"}", "");
        body_profile = body_profile.trim();
        String[] split = body_profile.split("\":\"|\",\"");

        if(!(responseCode3 == 401)){
            String user_id = split[1];
            String nick_name = split[3];
            String name = split[5];
            String uuid = split[7];
            String point = split[9];
            String email = split[11];
            String phone_number = split[13];
            String car_number = split[15];
            String profile_image = split[17];
            /*
            String user_id = split[7];
            String nick_name = split[9];
            String name = split[3];
            String uuid = split[1];
            String point = split[17];
            String email = split[5];
            String phone_number = split[11];
            String car_number = split[13];
            String profile_image = split[15];

             */

            ProfileFragment.user_id = user_id;
            ProfileFragment.nick_name = nick_name;
            ProfileFragment.user_name = name;
            ProfileFragment.uuid = uuid;
            ProfileFragment.point = point;
            ProfileFragment.email = email;
            ProfileFragment.phone_number = phone_number;
            ProfileFragment.car_number = car_number;
            ProfileFragment.profile_image = profile_image;


        }
    }

    public String unicodeToString(String uni) {

        String str = uni.split(" ")[0];
        str = str.replace("\\","");
        String[] arr = str.split("u");
        String text = "";
        for(int i = 1; i < arr.length; i++){
            int hexVal = Integer.parseInt(arr[i], 16);
            text += (char)hexVal;
        }
        return text;
    }

    public void Get_Location(String id) throws IOException {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        Request request = new Request.Builder()
                .url("http://blazingcode.asuscomm.com/api/parking-lot/"+ id)
                .method("GET", null)
                .build();
        Response response = client.newCall(request).execute();
        body_parkinglot = response.body().string();
        System.out.println("Body is" + body_parkinglot);
        String[] split_locationcount = body_parkinglot.split(",");
        String phone_number = split_locationcount[11].split("\"")[3];
        String PL_name = split_locationcount[2].split("\"")[3];
        System.out.println("phone : " + phone_number);
        String Image_1 = split_locationcount[5].split("\"")[3].replace("\\", "");
        String Image_2 = split_locationcount[6].split("\"")[3].replace("\\", "");
        String Image_3 = split_locationcount[7].split("\"")[3].replace("\\", "");
        String Image_4 = split_locationcount[8].split("\"")[3].replace("\\", "");
        Parking_phone = new String[1];
        Parking_img = new String[4];
        Parking_img[0] = Image_1;
        Parking_img[1] = Image_2;
        Parking_img[2] = Image_3;
        Parking_img[3] = Image_4;
        Parking_phone[0] = phone_number;
        one_parking_lot_name = PL_name;
        System.out.println(Parking_phone[0]);
        /*
        String[] split_onebody = body_parkinglot.split("\\{");
        System.out.println(split_onebody[0]);
        split_location = body_parkinglot.split("\"");
        String[] count_parkinglot = split_locationcount[0].split(":");
        Count_Parkinglot = new Integer(Integer.parseInt(count_parkinglot[1]));
        count = Count_Parkinglot.intValue();
        String[][] dataArray = new String[count][12];
        b = 7;
        for(int countbody = 2; countbody < count + 2; countbody++){
            String[] split_onedata = split_onebody[countbody].split(","); // split_onedata[0] = "parking_lot_id":"0"
            for(int amount = 0; amount < 11; amount++){
                String[] splitedData = split_onedata[amount].split(":"); // splitedData[1] = "0"
                dataArray[countbody-2][amount] = splitedData[1];
                System.out.println("dataArray : [" + (countbody - 2) + "]" + "[" + (amount) + "] = " + dataArray[countbody-2][amount]);
            }
            System.out.println(split_onedata[5].split(":")[1] + ":" + split_onedata[5].split(":")[2].replace("\\", ""));
            String[] splitedLng = dataArray[countbody-2][10].split("\\}");
            dataArray[countbody-2][10] = splitedLng[0];
            System.out.println(dataArray[countbody-2][10]);
            ParkingLat = new double[count];
            ParkingLat[countbody-2] = Double.parseDouble(dataArray[countbody-2][9]);
            ParkingLng = new double[count];
            ParkingLng[countbody-2] = Double.parseDouble(dataArray[countbody-2][10]);
            ParkingPrice = new int[count];
            ParkingPrice[countbody-2] = Integer.parseInt(dataArray[countbody-2][4]);
            Parking_phone = new String[count];
            Parking_phone[countbody-2] = dataArray[countbody-2][11];


      }

         */
        // ID : 7 , ownerID : 11, Name : 15, Address : 19, Price : 22, Image1 : 25, Image2 : 29, Image3 : 33, Image4 : 37, Lat : 40, Lng : 42
    }

    public void getalllot() throws IOException {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("text/plain");
        RequestBody body = RequestBody.create(mediaType, "");
        Request request = new Request.Builder()
                .url("http://blazingcode.asuscomm.com/api/parking-lot/show")
                .method("POST", body)
                .build();
        Response response = client.newCall(request).execute();
        String allbody = response.body().string();
        try {
            System.out.println("all body : " + allbody);
            String[] All_onePL = allbody.split("\\{");
            String[] count_parkinglot = All_onePL[1].split(",");
            Count_Parkinglot = new Integer(Integer.parseInt(count_parkinglot[0].substring(8)));
            count = Count_Parkinglot.intValue();
            System.out.println("count : " + count);

            All_Parkinglot = new String[count][4];
            for(int i = 0; i < count; i++) {
                String[] ALL_oneData = All_onePL[i+2].split(",");
                String[] ALL_oneIDraw = ALL_oneData[0].split(":");
                String[] ALL_oneID = ALL_oneIDraw[1].split("\"");
                String[] ALL_oneprice = ALL_oneData[1].split(":");
                String[] ALL_oneLat = ALL_oneData[2].split(":");
                String[] ALL_oneLngraw = ALL_oneData[3].split(":");
                String[] ALL_oneLng = ALL_oneLngraw[1].split("\\}");
                All_Parkinglot[i][0] = ALL_oneID[1];
                All_Parkinglot[i][1] = ALL_oneprice[1];
                All_Parkinglot[i][2] = ALL_oneLat[1];
                All_Parkinglot[i][3] = ALL_oneLng[0];
                System.out.println("Array Data is.. " + " " +  All_Parkinglot[i][0] + " " + All_Parkinglot[i][1] + " " + All_Parkinglot[i][2] + " " +All_Parkinglot[i][3]);
            }

        }

        catch (Exception e){
            System.out.println("전체 데이터 수신/스플릿 오류");
        }

    }
    public void post_booking(String syear, String smonth, String sday, String shour, String smin, String eyear, String emonth, String eday
    , String ehour, String emin, String ID, String token) throws IOException {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("text/plain");
        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("start_year", syear)
                .addFormDataPart("start_month", smonth)
                .addFormDataPart("start_day", sday)
                .addFormDataPart("start_hour", shour)
                .addFormDataPart("start_min", smin)
                .addFormDataPart("end_year", eyear)
                .addFormDataPart("end_month", emonth)
                .addFormDataPart("end_day", eday)
                .addFormDataPart("end_hour", ehour)
                .addFormDataPart("end_min", emin)
                .addFormDataPart("parking_lot_id", ID)
                .build();
        Request request = new Request.Builder()
                .url("http://blazingcode.asuscomm.com/api/booking")
                .method("POST", body)
                .addHeader("Authorization", "Bearer " + token)
                .build();
        Response response = client.newCall(request).execute();
        bookingbody = response.body().string();
        System.out.println(syear + " - " + smonth + " - " + sday + " - " + shour + " - " + smin + " - " +
                eyear + " - " + emonth + " - " + eday + " - " + ehour + " - " + emin + " - " + ID + " - " +  ": " + bookingbody);
    }
    public int getcountnumber(){
        return count;
    }
    /*
    public void AddMarker(int count, double x1, double x2, double y1, double y2){
        for(int i = 0; i < count; i++) {
            double pLat = Double.parseDouble(All_Parkinglot[i][2]);
            double pLng = Double.parseDouble(All_Parkinglot[i][3]);
            LatLng Parking = new LatLng(pLat, pLng);
            if (pLat < x1 && pLat > x2 && pLng > y1 && pLng < y2) {
                mMap.addMarker(new MarkerOptions().position(Parking).title(All_Parkinglot[i][0]));
            }
            else
                mMap.Marker.setVisible(boolean);
        }
    }

     */
    public void post_avaible_booking_time(String id, String year, String month, String day) throws IOException {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        Request request = new Request.Builder()
                .url("http://blazingcode.asuscomm.com/api/show/time/" + id + '-' + year + '-' + month + '-' + day)
                .method("GET", null)
                .build();
        Response response = client.newCall(request).execute();
        Avaible_time = response.body().string();
        System.out.println("time is .. "+ year + ", "+ month + ", " + day + ", " + Avaible_time);
    }

    public void post_avaible_time(String id, String token) throws IOException {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("text/plain");
        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("parking_lot_id", id)
                .build();
        Request request = new Request.Builder()
                .url("http://blazingcode.asuscomm.com/api/parking-lot/show/time")
                .method("POST", body)
                .addHeader("Authorization", "Bearer " +token)
                .build();
        Response response = client.newCall(request).execute();
        my_PL_available_time = response.body().string();
        System.out.println(my_PL_available_time);

    }
    public void cancel_parking(String bookingid, String token) throws IOException {

        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("text/plain");
        RequestBody body = RequestBody.create(mediaType, "");
        Request request = new Request.Builder()
                .url("http://blazingcode.asuscomm.com/api/booking/cancel/" + bookingid)
                .method("DELETE", body)
                .addHeader("Authorization", "Bearer " +token)
                .build();
        Response response = client.newCall(request).execute();
        String cancel_body = response.body().string();
        System.out.println(cancel_body);
    }
    public void show_my_booking_list(String token) throws IOException {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        Request request = new Request.Builder()
                .url("http://blazingcode.asuscomm.com/api/booking/show")
                .method("GET", null)
                .addHeader("Authorization", "Bearer " + token)
                .build();
        Response response = client.newCall(request).execute();
        my_booking_list = response.body().string();
        System.out.println(my_booking_list);
    }
    public void show_my_parking_lot(String token) throws IOException {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        Request request = new Request.Builder()
                .url("http://blazingcode.asuscomm.com/api/parking-lot/show/my_parking_lot")
                .method("GET", null)
                .addHeader("Authorization", "Bearer " + token)
                .build();
        Response response = client.newCall(request).execute();
        my_parking_lot = response.body().string();
        System.out.println("my parking lot : " + my_parking_lot);
    }
    public void add_parking_lot(String id, String name, String address, String latitude, String longitude, String price, String token, Bitmap img1, Bitmap img2, Bitmap img3, Bitmap img4) throws IOException {
        ByteArrayOutputStream stream1 = new ByteArrayOutputStream();
        ByteArrayOutputStream stream2 = new ByteArrayOutputStream();
        ByteArrayOutputStream stream3 = new ByteArrayOutputStream();
        ByteArrayOutputStream stream4 = new ByteArrayOutputStream();
        img1.compress(Bitmap.CompressFormat.JPEG, 100, stream1);
        img2.compress(Bitmap.CompressFormat.JPEG, 100, stream2);
        img3.compress(Bitmap.CompressFormat.JPEG, 100, stream3);
        img4.compress(Bitmap.CompressFormat.JPEG, 100, stream4);
        ImgByte1 = stream1.toByteArray();
        ImgByte2 = stream2.toByteArray();
        ImgByte3 = stream3.toByteArray();
        ImgByte4 = stream4.toByteArray();
        if(img1 != null && img2 != null && img3 != null && img4 != null) {
            img1.compress(Bitmap.CompressFormat.JPEG, 100, stream1);
            img2.compress(Bitmap.CompressFormat.JPEG, 100, stream2);
            img3.compress(Bitmap.CompressFormat.JPEG, 100, stream3);
            img4.compress(Bitmap.CompressFormat.JPEG, 100, stream4);
        }
        else if (img1 == null && img2 != null && img3 != null && img4 != null) {
            img2.compress(Bitmap.CompressFormat.JPEG, 100, stream2);
            img3.compress(Bitmap.CompressFormat.JPEG, 100, stream3);
            img4.compress(Bitmap.CompressFormat.JPEG, 100, stream4);
        }
        else if (img1 != null && img2 == null && img3 != null && img4 != null) {
            img1.compress(Bitmap.CompressFormat.JPEG, 100, stream1);
            img3.compress(Bitmap.CompressFormat.JPEG, 100, stream3);
            img4.compress(Bitmap.CompressFormat.JPEG, 100, stream4);
        }
        else if (img1 != null && img2 != null && img3 == null && img4 != null) {
            img1.compress(Bitmap.CompressFormat.JPEG, 100, stream1);
            img2.compress(Bitmap.CompressFormat.JPEG, 100, stream2);
            img4.compress(Bitmap.CompressFormat.JPEG, 100, stream4);
        }
        else if (img1 != null && img2 != null && img3 != null && img4 == null) {
            img1.compress(Bitmap.CompressFormat.JPEG, 100, stream1);
            img2.compress(Bitmap.CompressFormat.JPEG, 100, stream2);
            img3.compress(Bitmap.CompressFormat.JPEG, 100, stream3);
        }
        else{
            img1.compress(Bitmap.CompressFormat.JPEG, 100, stream1);
        }

        ImgByte1 = stream1.toByteArray();
        ImgByte2 = stream2.toByteArray();
        ImgByte3 = stream3.toByteArray();
        ImgByte4 = stream4.toByteArray();


        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("text/plain");
        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("parking_lot_id", id)
                .addFormDataPart("name", name)
                .addFormDataPart("address", address)
                .addFormDataPart("latitude", latitude)
                .addFormDataPart("longitude", longitude)
                .addFormDataPart("image1","img1.jpeg",
                        RequestBody.create(MediaType.parse("image/*"), ImgByte1))
                .addFormDataPart("image2","img2.jpeg",
                        RequestBody.create(MediaType.parse("image/*"), ImgByte2))
                .addFormDataPart("image3","img3.jpeg",
                        RequestBody.create(MediaType.parse("image/*"), ImgByte3))
                .addFormDataPart("image4","img4.jpeg",
                        RequestBody.create(MediaType.parse("image/*"), ImgByte4))
                .addFormDataPart("price", price)
                .addFormDataPart("time", "000000000000000000000000000000000000000000000000")
                .build();
        Request request = new Request.Builder()
                .url("http://blazingcode.asuscomm.com/api/parking-lot/create")
                .method("POST", body)
                .addHeader("Authorization", "Bearer " + token)
                .build();
        Response response = client.newCall(request).execute();
        status_add_parking_lot = response.body().string();
        System.out.println(status_add_parking_lot);
    }

    public void set_reserved(String id, String year, String month, String day, String time, String token) throws IOException {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("text/plain");
        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("parking_lot_id", id)
                .addFormDataPart("year", year)
                .addFormDataPart("month", month)
                .addFormDataPart("day", day)
                .addFormDataPart("time", time)
                .build();
        Request request = new Request.Builder()
                .url("http://blazingcode.asuscomm.com/api/parking-lot/update/available_time")
                .method("POST", body)
                .addHeader("Authorization", "Bearer " + token)
                .build();
        Response response = client.newCall(request).execute();
        body_reserved_time_select = response.body().string();
        System.out.println(body_reserved_time_select);
    }

    public void set_default_time(String id, String time, String token) throws IOException {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("text/plain");
        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("parking_lot_id", id)
                .addFormDataPart("time", time)
                .build();
        Request request = new Request.Builder()
                .url("http://blazingcode.asuscomm.com/api/parking-lot/update/default_time")
                .method("POST", body)
                .addHeader("Authorization", "Bearer " + token)
                .build();
        Response response = client.newCall(request).execute();
        body_default_time_select = response.body().string();
        System.out.println(body_default_time_select);
    }

    public void update_parking_lot(String id, String name, String address, Bitmap img1, Bitmap img2, Bitmap img3, Bitmap img4, String price, String token) throws IOException{
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        img1.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        img2.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        img3.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        img4.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] ImgByte = stream.toByteArray();
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("text/plain");
        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("parking_lot_id", id)
                .addFormDataPart("name", name)
                .addFormDataPart("address", address)
                .addFormDataPart("image1","img1.jpeg",
                        RequestBody.create(MediaType.parse("image/*"), ImgByte))
                .addFormDataPart("image2","img2.jpeg",
                        RequestBody.create(MediaType.parse("image/*"), ImgByte))
                .addFormDataPart("image3","img3.jpeg",
                        RequestBody.create(MediaType.parse("image/*"), ImgByte))
                .addFormDataPart("image4","img4.jpeg",
                        RequestBody.create(MediaType.parse("image/*"), ImgByte))
                .addFormDataPart("price", price)
                .build();
        Request request = new Request.Builder()
                .url("http://blazingcode.asuscomm.com/api/parking-lot/update/my_parking_lot")
                .method("POST", body)
                .addHeader("Authorization", "Bearer " + token)
                .build();
        Response response = client.newCall(request).execute();
        body_update_parking_lot = response.body().string();
        System.out.println(body_update_parking_lot);

    }

    public void delete_PL(String id, String token) throws IOException {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("text/plain");
        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("parking_lot_id", id)
                .build();
        Request request = new Request.Builder()
                .url("http://blazingcode.asuscomm.com/api/parking-lot/delete/my_parking_lot")
                .method("POST", body)
                .addHeader("Authorization", "Bearer " + token)
                .build();
        Response response = client.newCall(request).execute();
    }

    public void get_list_reservation(String id, String token) throws IOException {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("text/plain");
        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("parking_lot_id", id)
                .build();
        Request request = new Request.Builder()
                .url("http://blazingcode.asuscomm.com/api/booking/show/offer")
                .method("POST", body)
                .addHeader("Authorization", "Bearer " + token)
                .build();
        Response response = client.newCall(request).execute();
        body_list_reserved_pl = response.body().string();
    }
}
