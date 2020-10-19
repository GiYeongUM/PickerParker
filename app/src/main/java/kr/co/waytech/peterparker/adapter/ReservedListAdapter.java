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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import kr.co.waytech.peterparker.DownloadImageTask;
import kr.co.waytech.peterparker.R;
import kr.co.waytech.peterparker.activity.MainActivity;
import kr.co.waytech.peterparker.activity.PostClass;
import kr.co.waytech.peterparker.model.BookingList;
import kr.co.waytech.peterparker.model.ReservedBookingList;

import static android.content.Context.MODE_PRIVATE;
import static kr.co.waytech.peterparker.fragment.BookingListFragment.adding_booking_flag;
import static kr.co.waytech.peterparker.fragment.ProfileFragment.str_Token;

public class ReservedListAdapter extends RecyclerView.Adapter<ReservedListAdapter.MyViewHolder> {

    Context mContext;
    List<ReservedBookingList> ReservedBookingList;
    PostClass Postc = new PostClass();

    public ReservedListAdapter(Context mContext, List<ReservedBookingList> ReservedBookingList) {
        this.mContext = mContext;
        this.ReservedBookingList = ReservedBookingList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.row_bookinglistitem_parkinglot, parent, false);

        return new MyViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {

        View v = holder.itemView;
        holder.reserved_parkinglotID.setText(ReservedBookingList.get(position).getParkinglotid());
        holder.reserved_parkinglot_in_time.setText(ReservedBookingList.get(position).getIntime());
        holder.reserved_parkinglotCar.setText(ReservedBookingList.get(position).getCar());
        holder.reserved_parkinglotPrice.setText((Integer.parseInt(ReservedBookingList.get(position).getPrice())
                + Integer.parseInt(ReservedBookingList.get(position).getAddprice())) + "원");
        holder.reserved_parkinglotSchedule.setText(ReservedBookingList.get(position).getTime());
        holder.reserved_parkinglot_out_time.setText(ReservedBookingList.get(position).getOuttime());
        new DownloadImageTask(holder.parkinglotImage).execute(ReservedBookingList.get(position).getImg());
    }

    public int getItemCount() { return ReservedBookingList.size(); }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView parkinglotImage;
        TextView reserved_parkinglotID, reserved_parkinglot_in_time,reserved_parkinglot_out_time , reserved_parkinglotCar, reserved_parkinglotPrice, reserved_parkinglotSchedule;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            parkinglotImage = itemView.findViewById(R.id.reserved_booking_parkinglotImage);
            reserved_parkinglotID = itemView.findViewById(R.id.reserved_parkinglotID);
            reserved_parkinglot_in_time = itemView.findViewById(R.id.reserved_parkinglot_in_time);
            reserved_parkinglotCar = itemView.findViewById(R.id.reserved_parkinglotCar);
            reserved_parkinglotPrice = itemView.findViewById(R.id.reserved_parkinglotPrice);
            reserved_parkinglotSchedule = itemView.findViewById(R.id.reserved_parkinglotSchedule);
            reserved_parkinglot_out_time = itemView.findViewById(R.id.reserved_parkinglot_out_time);

        }
    }
}

