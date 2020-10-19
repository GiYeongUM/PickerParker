package kr.co.waytech.peterparker.adapter;

import android.content.Context;
import android.content.Intent;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import kr.co.waytech.peterparker.DownloadImageTask;
import kr.co.waytech.peterparker.R;
import kr.co.waytech.peterparker.activity.BookingActivity;
import kr.co.waytech.peterparker.activity.PostClass;
import kr.co.waytech.peterparker.model.Data;

import static kr.co.waytech.peterparker.activity.CalendarActivity.mYear;
import static kr.co.waytech.peterparker.activity.CalendarActivity.sDay;
import static kr.co.waytech.peterparker.activity.CalendarActivity.sMonth;
import static kr.co.waytech.peterparker.activity.CalendarActivity.sYear;
import static kr.co.waytech.peterparker.adapter.ListAdapter.address;
import static kr.co.waytech.peterparker.adapter.ListAdapter.phone;
import static kr.co.waytech.peterparker.adapter.ListAdapter.price;
import static kr.co.waytech.peterparker.adapter.ListAdapter.distance;
import static kr.co.waytech.peterparker.adapter.ListAdapter.avaible_time;
import static kr.co.waytech.peterparker.adapter.ListAdapter.ID;
import static kr.co.waytech.peterparker.adapter.ListAdapter.name;

public class RecyclerAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<Data> picked_parking_lot = new ArrayList<Data>();
    long now = System.currentTimeMillis() - 1000;
    Date date_today = new Date(now);
    SimpleDateFormat sdfNow = new SimpleDateFormat("yyyy-MM-dd");
    String formatDate = sdfNow.format(date_today);
    final PostClass Postc = new PostClass();


    public RecyclerAdapter() {

    }

    // position 위치의 아이템 타입 리턴.

    @Override
    public int getCount() {
        return picked_parking_lot.size();
    }

    public Object getItem(int position) {
        return picked_parking_lot.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final Context context = parent.getContext();
        int viewType = getItemViewType(position) ;

        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) ;

        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        final Data data = picked_parking_lot.get(position);
        convertView = inflater.inflate(R.layout.map_picked_item,
                parent, false);
        Postc.send_Location(data.getId());
        System.out.println("get id " + data.getId());
        TextView textView_address = (TextView)convertView.findViewById(R.id.textView_Address);
        TextView textView_price = (TextView) convertView.findViewById(R.id.textView_price);
        TextView textView_Distance = (TextView) convertView.findViewById(R.id.textview_distance);
        TextView textView_time = (TextView) convertView.findViewById(R.id.textView_time);
        textView_address.setText(data.getAddress());
        textView_price.setText(data.getContent_Price() + " 원");
        textView_Distance.setText(data.getDistance());
        Handler mHandler = new Handler();
        final View finalConvertView = convertView;
        mHandler.postDelayed(new Runnable()  {
            public void run() {
                new DownloadImageTask((ImageView) finalConvertView.findViewById(R.id.picked_imageView)).execute(Postc.Parking_img[0]);
            }
        }, 400);
        Button picked_btn = (Button)convertView.findViewById(R.id.picked_Btn);
        picked_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Postc.send_Location(data.getId());
                System.out.println(getItemId(position));
                address = data.getAddress();
                price = data.getContent_Price();
                distance = data.getDistance();
                ID = data.getId();
                if(mYear != 0){
                    Postc.send_booking_time_id(data.getId(), sYear, sMonth, sDay);
                }
                else{
                    Postc.send_booking_time_id(data.getId(), formatDate.split("-")[0], formatDate.split("-")[1], formatDate.split("-")[2]);
                }
                System.out.println(address);
                Handler mHandler = new Handler();
                mHandler.postDelayed(new Runnable()  {
                    public void run() {
                        phone = Postc.Parking_phone[0];
                        name = Postc.one_parking_lot_name;
                        // 시간 지난 후 실행할 코딩
                        avaible_time = Postc.Avaible_time;
                        Intent intent = new Intent(context.getApplicationContext(), BookingActivity.class);
                        context.startActivity(intent);
                    }
                }, 1000); // 0.5초후

            }
        });
        return convertView;
    }
    public void addItem(String Address, String price, String Distance, String id) {
        Data item = new Data() ;
        item.setAddress(Address);
        item.setContent_Price(price);
        item.setDistance(Distance);
        item.setId(id);
        picked_parking_lot.add(item);
    }
}
