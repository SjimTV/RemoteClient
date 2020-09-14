package com.sjimtv.remoteclient.media;


import android.content.Context;
import android.util.Log;

import com.android.volley.Response;
import com.google.gson.Gson;
import com.sjimtv.remoteclient.R;
import com.sjimtv.remoteclient.ui.HomeActivity;
import com.sjimtv.remoteclient.ui.StatusRefreshable;

import org.json.JSONObject;


public class Controller {

    public static void pause(Context context,  boolean isPaused) {
        Status status = HomeActivity.status;
        status.setNewMedia(false);
        status.setIsPaused(isPaused);
        sendUpdateStatus(context, status);
    }

    public static void playEpisode(Context context, int showIndex, int episodeNumber){
        Status status = new Status();
        status.setNewMedia(true);
        status.setShowIndex(showIndex);
        status.setEpisodeNumber(episodeNumber);
        status.setIsPaused(false);

        sendUpdateStatus(context, status);
    }

    public static void shutdown(){
        HomeActivity.restApiCommunicator.shutdown(new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("Controller",response);
            }
        });
    }

    private static void sendUpdateStatus(Context context, Status status){
        HomeActivity.restApiCommunicator.updateStatus(status, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.i("Controller",response.toString());
                HomeActivity.status = new Gson().fromJson(String.valueOf(response), Status.class);
                try {
                    StatusRefreshable refreshable = (StatusRefreshable) context;
                    refreshable.refresh();
                } catch (Exception e) {
                    System.out.println("Context cannot refresh");
                }

            }
        });
    }



    public static void adjustVolume(boolean adjustUp){
        HomeActivity.restApiCommunicator.adjustVolume(adjustUp, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("Controller", "New Volume = " + response);
            }
        });
    }

    public static void skipTime(boolean forward){
        HomeActivity.restApiCommunicator.skipTime(forward, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("Controller", "Current position = " + response);
            }
        });
    }

    public static void setPosition(float position) {
        HomeActivity.restApiCommunicator.setPosition(position, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("Controller", "Current position = " + response);
            }
        });
    }

}
