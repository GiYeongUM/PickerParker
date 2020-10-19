package kr.co.waytech.peterparker.adapter;

import android.bluetooth.le.AdvertiseCallback;
import android.bluetooth.le.AdvertiseSettings;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.BeaconTransmitter;

import java.util.Arrays;
import java.util.List;

import kr.co.waytech.peterparker.DownloadImageTask;
import kr.co.waytech.peterparker.R;
import kr.co.waytech.peterparker.activity.MainActivity;
import kr.co.waytech.peterparker.activity.PostClass;
import kr.co.waytech.peterparker.model.BookingList;

import static android.content.Context.MODE_PRIVATE;
import static kr.co.waytech.peterparker.fragment.BookingListFragment.adding_booking_flag;
import static kr.co.waytech.peterparker.fragment.ProfileFragment.str_Token;

public class BookingListAdapter extends RecyclerView.Adapter<BookingListAdapter.MyViewHolder> {

    Context mContext;
    List<BookingList> bookingList;
    PostClass Postc = new PostClass();

    public BookingListAdapter(Context mContext, List<BookingList> bookingList) {
        this.mContext = mContext;
        this.bookingList = bookingList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.row_bookinglist_item, parent, false);

        return new MyViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {

        View v = holder.itemView;
        holder.parkinglotName.setText(bookingList.get(position).getParkinglotName());
        holder.status.setText(bookingList.get(position).getStatus());
        holder.parkinglotAddress.setText(bookingList.get(position).getParkinglotAddress());
        holder.parkinglotPrice.setText(bookingList.get(position).getParkinglotPrice());
        holder.parkinglotSchedule.setText(bookingList.get(position).getParkinglotSchedule());
        new DownloadImageTask(holder.parkinglotImage).execute(bookingList.get(position).getImageUrl());


        //Dialog ini
        holder.checkin_btn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
                int pos = holder.getAdapterPosition();
                Toast.makeText(mContext,"Click Item"+ pos,Toast.LENGTH_SHORT).show();
//                Toast.makeText(mContext,"Click Item",Toast.LENGTH_SHORT).show();
                connectIBeacon();
            }
        });


        //Dialog ini
        holder.delete_booking_btn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
                final int pos = holder.getAdapterPosition();
                Toast.makeText(mContext,"Click Item"+ pos,Toast.LENGTH_SHORT).show();

                AlertDialog.Builder builder = new AlertDialog.Builder(holder.delete_booking_btn.getContext());
                builder.setTitle("예약취소")
                        .setMessage("정말로 예약을 취소하시겠습니까.")
                        .setCancelable(false)
                        .setPositiveButton("네", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Postc.post_cancel_booking(bookingList.get(pos).getBooking_id(), str_Token);
                                bookingList.remove(pos);
                                adding_booking_flag = 1;
                                notifyItemRemoved(pos);
                                notifyItemChanged(pos, bookingList.size());
                                Toast.makeText(holder.delete_booking_btn.getContext(),"position : "+ pos + " size : " + bookingList.size(),Toast.LENGTH_SHORT).show();
                                // 서버에 삭제한 거 전송
                            }
                        })
                        .setNegativeButton("아니요", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //No 눌렀을 때 반응 여기
                            }
                        });
                //Creating dialog box
                AlertDialog dialog  = builder.create();
                dialog.show();

            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void connectIBeacon() {
        Beacon beacon = new Beacon.Builder()
                .setId1("2f234454-cf6d-4a0f-adf2-f4911ba9ffa6") // UUID for beacon 변경 가능 => 사용자 uuid를 넣어줘야함
                .setId2("0206") // Major for beacon 고정
                .setId3("0406") // Minor for beacon 고정
                .setManufacturer(0x004C) // Radius Networks.0x0118  Change this for other beacon layouts//0x004C for iPhone
                .setTxPower(-56)// Power in dB
                .setDataFields(Arrays.asList(new Long[] {0l})) // Remove this for beacon layouts without d: fields
                .build();

        BeaconParser beaconParser = new BeaconParser()
                .setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24");

        BeaconTransmitter beaconTransmitter = new BeaconTransmitter(mContext.getApplicationContext(), beaconParser);

        beaconTransmitter.startAdvertising(beacon, new AdvertiseCallback() {
            @Override
            public void onStartFailure(int errorCode) {
                Toast.makeText(mContext, "Advertisement start failed with code:" +errorCode, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStartSuccess(AdvertiseSettings settingsInEffect) {
                Toast.makeText(mContext, "Advertisement start succeeded.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() { return bookingList.size(); }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        Button checkin_btn, delete_booking_btn;
        ImageView parkinglotImage;
        TextView parkinglotName, status, parkinglotAddress, parkinglotPrice, parkinglotSchedule;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            checkin_btn = itemView.findViewById(R.id.btn_check_in);
            delete_booking_btn = itemView.findViewById(R.id.delete_booking_Button);
            parkinglotImage = itemView.findViewById(R.id.booking_parkinglotImage);
            parkinglotName = itemView.findViewById(R.id.parkinglotName);
            status = itemView.findViewById(R.id.status);
            parkinglotAddress = itemView.findViewById(R.id.parkinglotAddress);
            parkinglotPrice = itemView.findViewById(R.id.parkinglotPrice);
            parkinglotSchedule = itemView.findViewById(R.id.parkinglotSchedule);

        }
    }
}

