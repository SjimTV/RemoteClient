package com.sjimtv.remoteclient;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.ImageView;

import com.sjimtv.remoteclient.client.Client;
import com.sjimtv.remoteclient.client.RestApiCommunicator;
import com.sjimtv.remoteclient.control.Controller;
import com.sjimtv.remoteclient.filemanager.FileManager;
import com.sjimtv.remoteclient.showStructure.Shows;

import java.io.IOException;

public class HomeActivity extends AppCompatActivity {

    private final static String IP_LAPTOP = "192.168.0.200";
    private final static String IP_COMPUTER = "192.168.0.176";

    public static FileManager fileManager;
    public static Client client;
    public static RestApiCommunicator restApiCommunicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeFileManager();
        initializeClient();
        initializeRestApiRequester();

        fillFileManager();

    }

    // Initializers
    private void initializeFileManager() {
        fileManager = new FileManager(this);
    }

    private void initializeClient() {
        client = new Client(IP_COMPUTER, 4444);
        client.start();
    }

    private void initializeRestApiRequester() {
        restApiCommunicator = new RestApiCommunicator(IP_COMPUTER, 9000);
    }

    private void fillFileManager() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    Shows newShows = restApiCommunicator.pullShowsFromHost();
                    if (newShows == null) fileManager.loadShowsFromDeviceBackup();
                    else fileManager.setShows(newShows);
                } catch (IOException e){
                    e.printStackTrace();
                    fileManager.loadShowsFromDeviceBackup();
                }
            }
        }).start();

    }

    // Buttons
    public void openClicked(View view) {
        Intent openShowsIntent = new Intent(HomeActivity.this, ShowsActivity.class);
        startActivity(openShowsIntent);
    }

    public void playClicked(View view) {
        ImageView imageView = (ImageView) view;

        animateMiddleButton(imageView);
        try {
            if (Controller.pause())  imageView.setImageResource(R.drawable.ic_ffwd_button);
            else imageView.setImageResource(R.drawable.ic_play);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void volumeUpClicked(View view){
        animateSideButton(view);
    }

    public void volumeDownClicked(View view){
        animateSideButton(view);
    }

    public void bkwdClicked(View view){
        animateSideButton(view);
    }

    public void ffwdClicked(View view){
        animateSideButton(view);
    }

    public void animateMiddleButton(View view) {
        View parentLayout = (View) view.getParent();
        AnimatedVectorDrawable animatedButton = (AnimatedVectorDrawable) ContextCompat.getDrawable(this, R.drawable.button_middle_animated);
        parentLayout.setBackground(animatedButton);
        animatedButton.start();
    }

    public void animateSideButton(View view) {
        View parentLayout = (View) view.getParent();
        AnimatedVectorDrawable animatedButton = (AnimatedVectorDrawable) ContextCompat.getDrawable(this, R.drawable.button_side_animated);
        parentLayout.setBackground(animatedButton);
        animatedButton.start();
    }
}