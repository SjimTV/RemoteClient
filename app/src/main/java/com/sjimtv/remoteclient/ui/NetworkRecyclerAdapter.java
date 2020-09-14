package com.sjimtv.remoteclient.ui;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sjimtv.remoteclient.R;
import com.sjimtv.remoteclient.showStructure.ConnectedDeviceList;
import com.sjimtv.remoteclient.showStructure.Shows;

public class NetworkRecyclerAdapter extends RecyclerView.Adapter<NetworkRecyclerAdapter.DeviceViewHolder> {

    private Activity activity;
    private ConnectedDeviceList deviceList;

    public NetworkRecyclerAdapter(Activity activity, ConnectedDeviceList deviceList) {
        this.activity = activity;
        this.deviceList = deviceList;
    }

    @NonNull
    @Override
    public DeviceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_device, parent, false);
        return new DeviceViewHolder(view, activity);
    }

    @Override
    public void onBindViewHolder(@NonNull DeviceViewHolder holder, int position) {
        TextView nameTextView = holder.nameTextView;
        nameTextView.setText(deviceList.get(position).getName());

        TextView ipAddressTextView = holder.ipAddressTextView;
        ipAddressTextView.setText(deviceList.get(position).getIpAddress());
    }

    @Override
    public int getItemCount() {
        return deviceList.size();
    }

    public static class DeviceViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView nameTextView;
        private TextView ipAddressTextView;

        HomeActivity activity;

        public DeviceViewHolder(@NonNull View itemView, Activity activity) {
            super(itemView);
            this.activity = (HomeActivity) activity;

            nameTextView = itemView.findViewById(R.id.deviceNameTextView);
            ipAddressTextView = itemView.findViewById(R.id.deviceIpAddressTextView);

            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View view) {
            activity.getDeviceClicked(getAdapterPosition());
        }
    }


}
