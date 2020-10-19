package kr.co.waytech.peterparker.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import androidx.cardview.widget.CardView;

import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import kr.co.waytech.peterparker.DownloadImageTask;
import kr.co.waytech.peterparker.model.ListData;
import kr.co.waytech.peterparker.R;
import kr.co.waytech.peterparker.activity.BookingActivity;
import kr.co.waytech.peterparker.activity.PostClass;

import static kr.co.waytech.peterparker.activity.CalendarActivity.mYear;
import static kr.co.waytech.peterparker.activity.CalendarActivity.sDay;
import static kr.co.waytech.peterparker.activity.CalendarActivity.sMonth;
import static kr.co.waytech.peterparker.activity.CalendarActivity.sYear;

public class ListAdapter extends BaseAdapter {

    private Context mContext;
    public static String address, price, distance, phone, ID, avaible_time, name;

    private ArrayList<ListData> array_parking_lot = new ArrayList<ListData>();
    long now = System.currentTimeMillis() - 1000;
    Date date_today = new Date(now);
    SimpleDateFormat sdfNow = new SimpleDateFormat("yyyy-MM-dd");
    String formatDate = sdfNow.format(date_today);
    final PostClass Postc = new PostClass();


    public ListAdapter() {

    }

    // position 위치의 아이템 타입 리턴.

    @Override
    public int getCount() {
        return array_parking_lot.size();
    }

    public Object getItem(int position) {
        return array_parking_lot.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final Context context = parent.getContext();
        int viewType = getItemViewType(position) ;

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) ;

        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        final ListData listViewItem = array_parking_lot.get(position);

        convertView = inflater.inflate(R.layout.map_list_item,
                parent, false);
        Postc.send_Location(listViewItem.getId());
        System.out.println("get id " + listViewItem.getId());
        TextView textView_address = (TextView)convertView.findViewById(R.id.list_text_address);
        TextView textView_price = (TextView) convertView.findViewById(R.id.list_text_price);

        TextView textView_Distance = (TextView) convertView.findViewById(R.id.text_distance);
        ImageView imageView = (ImageView) convertView.findViewById(R.id.list_image);
        textView_address.setText(listViewItem.getAddress());
        textView_price.setText(listViewItem.getContent_Price() + " 원");
        textView_Distance.setText(listViewItem.getDistance());
        Handler mHandler = new Handler();
        final View finalConvertView = convertView;
        mHandler.postDelayed(new Runnable()  {
            public void run() {
                new DownloadImageTask((ImageView) finalConvertView.findViewById(R.id.list_image)).execute(Postc.Parking_img[0]);
            }
        }, 800);
        final CardView cmdArea = (CardView)convertView.findViewById(R.id.List_cardview);
        cmdArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println(getItemId(position));
                address = listViewItem.getAddress();
                price = listViewItem.getContent_Price();
                distance = listViewItem.getDistance();
                ID = listViewItem.getId();
                System.out.println(address);
                Postc.send_Location(listViewItem.getId());
                if(mYear != 0){
                    Postc.send_booking_time_id(listViewItem.getId(), sYear, sMonth, sDay);
                }
                else{
                    Postc.send_booking_time_id(listViewItem.getId(), formatDate.split("-")[0], formatDate.split("-")[1], formatDate.split("-")[2]);
                }
                ID = listViewItem.getId();
                Handler mHandler = new Handler();
                mHandler.postDelayed(new Runnable()  {
                    public void run() {
                        name = Postc.one_parking_lot_name;
                        phone = Postc.Parking_phone[0];
                        avaible_time = Postc.Avaible_time;
                        // 시간 지난 후 실행할 코딩
                        Intent intent = new Intent(context.getApplicationContext(), BookingActivity.class);
                        context.startActivity(intent);
                    }
                }, 1000); // 0.5초후

            }
        });
        return convertView;
    }
    public void addItem(String Address, String price, String Distance, String id) {
        ListData item = new ListData() ;
        item.setAddress(Address);
        item.setContent_Price(price);
        item.setDistance(Distance);
        item.setId(id);
        array_parking_lot.add(item);
    }
}

