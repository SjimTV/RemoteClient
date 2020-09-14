package com.sjimtv.remoteclient.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.google.gson.Gson;
import com.sjimtv.remoteclient.R;
import com.sjimtv.remoteclient.animations.CustomAnimation;
import com.sjimtv.remoteclient.client.Client;
import com.sjimtv.remoteclient.client.RestApiCommunicator;
import com.sjimtv.remoteclient.media.Controller;
import com.sjimtv.remoteclient.filemanager.FileManager;
import com.sjimtv.remoteclient.media.Status;
import com.sjimtv.remoteclient.showStructure.ConnectedDevice;
import com.sjimtv.remoteclient.showStructure.ConnectedDeviceList;
import com.sjimtv.remoteclient.showStructure.Episode;
import com.sjimtv.remoteclient.showStructure.Show;
import com.sjimtv.remoteclient.utils.TimeConverter;

import java.util.Objects;

public class HomeActivity extends AppCompatActivity implements StatusRefreshable, PositionListener {

    private final static String IP_LAPTOP = "192.168.0.200";
    private final static String IP_COMPUTER = "192.168.0.176";

    private final static String CONNECTED_DEVICE = IP_LAPTOP;

    public static FileManager fileManager;
    public static Client client;
    public static RestApiCommunicator restApiCommunicator;

    private ConnectedDeviceList connectedDeviceList;
    private SharedPreferences sharedPreferences;
    public static Status status;

    private ConstraintLayout menuLayover;
    private ConstraintLayout menuPlaceholder;
    private ConstraintLayout devicesPopupLayout;

    private RecyclerView deviceRecyclerView;
    private LinearLayoutManager deviceLayoutManager;
    private NetworkRecyclerAdapter deviceRecyclerAdapter;

    private SeekBar positionSeekBar;
    private TextView currentTimeView;
    private double totalTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initializeFileManager();
        status = Status.initialize();

        loadDeviceListFromBackup();
        initializeDeviceListView();
        initializeNetwork(connectedDeviceList.get(0).getIpAddress());
        pullData();

        placeMenuLayoutInPlaceholder();
        setupPositionListener();
    }

    // Initializers
    private void initializeFileManager() {
        fileManager = new FileManager(this);
    }

    private void initializeNetwork(String ipServer){
        initializeClient(ipServer);
        initializeRestApiRequester(ipServer);
    }


    private void initializeClient(String ipServer) {
        client = new Client(this, ipServer, 4444);
        client.start();
    }

    private void initializeRestApiRequester(String ipServer) {
        restApiCommunicator = new RestApiCommunicator(this, ipServer, 9000);
    }

    private void connectDevice(ConnectedDevice connectedDevice){
        restApiCommunicator.changeDevice(connectedDevice.getIpAddress());
        client.changeDevice(connectedDevice.getIpAddress());
        pullData();
    }

    private void pullData(){
        pullStatus();
        pullShows();
        subscribePositionListener(true);
    }

    private void pullStatus() {
        restApiCommunicator.getStatus(new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                status = new Gson().fromJson(response, Status.class);
                refresh();
            }
        });

    }

    private void pullShows() {
        restApiCommunicator.pullShowsFromHost(response -> fileManager.setShowsFromJSON(response));
    }

    private void placeMenuLayoutInPlaceholder() {
        menuLayover = findViewById(R.id.menuLayover);
        menuPlaceholder = findViewById(R.id.menuPlaceholder);
        ConstraintLayout menuLayout = (ConstraintLayout) getLayoutInflater().inflate(R.layout.menu_layout, menuPlaceholder, false);
        menuPlaceholder.addView(menuLayout);
    }

    private void loadDeviceListFromBackup() {
        sharedPreferences = getSharedPreferences("com.sjimtv.remotehost", 0);
        String cDevicesJson = sharedPreferences.getString("connectedDevices", "empty");
        if (cDevicesJson != null && cDevicesJson.equals("empty")) {
            connectedDeviceList = new ConnectedDeviceList();
            connectedDeviceList.add(new ConnectedDevice("Laptop", "192.168.0.200"));
            connectedDeviceList.add(new ConnectedDevice("Computer", "192.168.0.176"));
            sharedPreferences.edit().putString("connectedDevices", new Gson().toJson(connectedDeviceList)).apply();
        } else {
            connectedDeviceList = new Gson().fromJson(cDevicesJson, ConnectedDeviceList.class);
        }
    }

    private void initializeDeviceListView(){
        devicesPopupLayout = findViewById(R.id.devicesPopupLayout);
        deviceRecyclerView = findViewById(R.id.devicesRecyclerView);

        deviceLayoutManager = new LinearLayoutManager(this);
        deviceLayoutManager.setOrientation(RecyclerView.VERTICAL);
        deviceRecyclerView.setLayoutManager(deviceLayoutManager);
        deviceRecyclerView.setItemAnimator(new DefaultItemAnimator());

        deviceRecyclerAdapter = new NetworkRecyclerAdapter(this, connectedDeviceList);
        deviceRecyclerView.setAdapter(deviceRecyclerAdapter);
    }


    // Toolbar Buttons
    public void openMenuClicked(View view) {
        openMenu();
    }

    private void openMenu() {
        menuLayover.setVisibility(View.VISIBLE);
        menuPlaceholder.setVisibility(View.VISIBLE);
        menuLayover.startAnimation(CustomAnimation.fadeMenuIn());
        menuPlaceholder.startAnimation(CustomAnimation.slideMenuInLeft());
    }

    public void closeMenuClicked(View view) {
        closeMenu();
    }

    private void closeMenu() {
        if (menuPlaceholder.getVisibility() == View.VISIBLE){
            menuPlaceholder.startAnimation(CustomAnimation.slideMenuOutLeft());
            menuPlaceholder.setVisibility(View.GONE);
        }

        menuLayover.startAnimation(CustomAnimation.fadeMenuOut());
        menuLayover.setVisibility(View.GONE);
        devicesPopupLayout.setVisibility(View.GONE);
    }

    public void openShowsClicked(View view) {
        Intent openShowsIntent = new Intent(HomeActivity.this, ShowsActivity.class);

        if (status.getShowIndex() == -1) openShowsIntent.putExtra("highlightedShow", 0);
        else openShowsIntent.putExtra("highlightedShow", status.getShowIndex());

        if (status.getEpisodeNumber() == -1) openShowsIntent.putExtra("highlightedEpisode", 1);
        else openShowsIntent.putExtra("highlightedEpisode", status.getEpisodeNumber());

        startActivity(openShowsIntent);
        closeMenu();
    }

    public void openDevicesClicked(View view) {
        devicesPopupLayout.setVisibility(View.VISIBLE);
        menuPlaceholder.startAnimation(CustomAnimation.slideMenuOutLeft());
        menuPlaceholder.setVisibility(View.GONE);
    }

    public void getDeviceClicked(int position){
        ConnectedDevice connectedDevice = connectedDeviceList.get(position);
        connectedDeviceList.remove(connectedDevice);
        connectedDeviceList.add(0, connectedDevice);
        sharedPreferences.edit().putString("connectedDevices", new Gson().toJson(connectedDeviceList)).apply();
        deviceRecyclerAdapter.notifyDataSetChanged();

        new Thread(){
            @Override
            public void run() {
                connectDevice(connectedDevice);
                super.run();
            }
        }.start();

        closeMenu();

    }

    public void shutdownClicked(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogShutdown = getLayoutInflater().inflate(R.layout.dialog_shutdown, null);
        TextView positiveButton = dialogShutdown.findViewById(R.id.positiveButton);
        TextView negativeButton = dialogShutdown.findViewById(R.id.negativeButton);
        builder.setView(dialogShutdown);

        final AlertDialog shutdownDialog = builder.create();
        Objects.requireNonNull(shutdownDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        shutdownDialog.show();

        positiveButton.setOnClickListener(posAction -> {
            Toast.makeText(getApplicationContext(), "Shutting down!", Toast.LENGTH_SHORT).show();
            Controller.shutdown();
            shutdownDialog.dismiss();
            closeMenu();
        });

        negativeButton.setOnClickListener(negAction -> {
            Toast.makeText(getApplicationContext(), "Not shutting down..", Toast.LENGTH_SHORT).show();
            shutdownDialog.dismiss();
            closeMenu();
        });
    }

    // Control Buttons
    public void playClicked(View view) {
        ImageView imageView = (ImageView) view;
        animateMiddleButton(imageView);

        Controller.pause(this, !status.isPaused());
    }

    public void volumeUpClicked(View view) {
        animateSideButton(view);
        Controller.adjustVolume(true);
    }

    public void volumeDownClicked(View view) {
        animateSideButton(view);
        Controller.adjustVolume(false);
    }

    public void bkwdClicked(View view) {
        animateSideButton(view);
        Controller.skipTime(false);
    }

    public void ffwdClicked(View view) {
        animateSideButton(view);
        Controller.skipTime(true);
    }

    public void nextEpisodeClicked(View view){
        if (status.getShowIndex() != -1) Controller.playEpisode(this, status.getShowIndex(), status.getEpisodeNumber() + 1);
        else nextEpisodeFromBackup();
    }

    public void nextEpisodeFromBackup(){
        int lastWatchedShow = sharedPreferences.getInt("lastWatchedShowIndex", 0);
        int lastWatchedEpisode = sharedPreferences.getInt("lastWatchedEpisodeNr", 1);
        Controller.playEpisode(this, lastWatchedShow, lastWatchedEpisode + 1);
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

    // Position listener
    private void setupPositionListener() {
        positionSeekBar = findViewById(R.id.positionSeekBar);
        currentTimeView = findViewById(R.id.currentTimeView);

        positionSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                Controller.pause(null, true);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Controller.setPosition((float) positionSeekBar.getProgress() / 1000);
                Controller.pause(null, false);
            }
        });

    }

    private void subscribePositionListener(boolean subscribe) {
        restApiCommunicator.subscribePositionlistener(subscribe, response -> Log.i("Position", "Subscribed"));
    }

    @Override
    public void positionListen(float position) {
        positionSeekBar.setProgress((int) (position * 1000));
        double currentTime = totalTime * position;

        runOnUiThread(() -> currentTimeView.setText(TimeConverter.doubleToHourMinSec(currentTime)));
    }

    private boolean isMedia() {
        return status.getEpisodeNumber() <= 0;
    }

    @Override
    public void refresh() {
        saveLastWatched(status.getShowIndex(), status.getEpisodeNumber());
        runOnUiThread(this::refreshUI);
    }

    private void saveLastWatched(int showIndex, int episodeNr){
        if (status.getShowIndex() == -1) return;
        sharedPreferences.edit().putInt("lastWatchedShowIndex", showIndex).apply();
        sharedPreferences.edit().putInt("lastWatchedEpisodeNr", episodeNr).apply();
    }

    private void refreshUI(){
        setNoLimits(this);
        refreshPlayButton();
        refreshNowPlayingEpisode();
        refreshConnectionState();
    }

    private void refreshPlayButton() {
        ImageView playButton = findViewById(R.id.playButton);

        if (status.isPaused()) playButton.setImageResource(R.drawable.ic_play);
        else playButton.setImageResource(R.drawable.ic_pause);
    }

    private void refreshNowPlayingEpisode() {
        if (isMedia()) setupNoMediaView();
        else setupMediaView();
    }

    private void setupNoMediaView() {
        TextView showPlayingView = findViewById(R.id.showPlayingView);
        TextView episodePlayingView = findViewById(R.id.episodePlayingView);
        TextView totalTimeView = findViewById(R.id.totalTimeView);


        showPlayingView.setText("No Media Playing");
        episodePlayingView.setText(" ");

        totalTimeView.setText(" ");
        currentTimeView.setVisibility(View.INVISIBLE);
        positionSeekBar.setVisibility(View.INVISIBLE);

    }

    private void setupMediaView() {
        TextView showPlayingView = findViewById(R.id.showPlayingView);
        TextView episodePlayingView = findViewById(R.id.episodePlayingView);
        TextView totalTimeView = findViewById(R.id.totalTimeView);

        Show nowPlayingShow = fileManager.getShows().get(status.getShowIndex());
        Episode nowPlayingEpisode = nowPlayingShow.getEpisodes().get(status.getEpisodeNumber());

        showPlayingView.setText(nowPlayingShow.getName());
        episodePlayingView.setText(nowPlayingEpisode.getName());

        totalTimeView.setText(TimeConverter.doubleToHourMinSec(nowPlayingEpisode.getDuration()));
        totalTime = nowPlayingEpisode.getDuration();
        currentTimeView.setVisibility(View.VISIBLE);
        positionSeekBar.setVisibility(View.VISIBLE);
    }

    private void refreshConnectionState(){
        ImageView connectionIcon = findViewById(R.id.connectionIcon);
        if (client.isClientConnected()) connectionIcon.setImageResource(R.drawable.ic_wifi);
        else connectionIcon.setImageResource(R.drawable.ic_wifi_off);

    }

    public static void setNoLimits(Activity activity){
        Window window = activity.getWindow();
        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
    }


    @Override
    protected void onStart() {
        super.onStart();
        refresh();
        subscribePositionListener(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        refresh();
        subscribePositionListener(true);
    }

    @Override
    protected void onPause() {
        super.onPause();
        subscribePositionListener(false);
    }

    @Override
    public void onBackPressed() {
        if (menuPlaceholder.isShown() || devicesPopupLayout.isShown()) closeMenu();
        else super.onBackPressed();
    }


}