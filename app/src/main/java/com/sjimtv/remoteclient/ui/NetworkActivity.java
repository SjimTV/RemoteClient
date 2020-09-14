package com.sjimtv.remoteclient.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.sjimtv.remoteclient.R;
import com.sjimtv.remoteclient.showStructure.ConnectedDevice;
import com.sjimtv.remoteclient.showStructure.ConnectedDeviceList;

import java.util.ArrayList;

public class NetworkActivity extends AppCompatActivity {

    private HomeActivity homeActivity;

    private ConnectedDeviceList connectedDeviceList;

    private RecyclerView deviceRecyclerView;
    private LinearLayoutManager deviceLayoutManager;
    private NetworkRecyclerAdapter deviceRecyclerAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_network);
        loadDeviceListFromBackup();
        initializeDeviceListView();

        HomeActivity.setNoLimits(this);
    }

    private void loadDeviceListFromBackup() {
        connectedDeviceList = new ConnectedDeviceList();
        connectedDeviceList.add(new ConnectedDevice("Laptop", "192.168.0.200"));
        connectedDeviceList.add(new ConnectedDevice("Computer", "192.168.0.176"));
    }

    private void initializeDeviceListView(){
        deviceRecyclerView = findViewById(R.id.devicesRecyclerView);

        deviceLayoutManager = new LinearLayoutManager(this);
        deviceLayoutManager.setOrientation(RecyclerView.VERTICAL);
        deviceRecyclerView.setLayoutManager(deviceLayoutManager);
        deviceRecyclerView.setItemAnimator(new DefaultItemAnimator());

        deviceRecyclerAdapter = new NetworkRecyclerAdapter(this, connectedDeviceList);
        deviceRecyclerView.setAdapter(deviceRecyclerAdapter);
    }

    public void getDeviceClicked(int position){

    }


}