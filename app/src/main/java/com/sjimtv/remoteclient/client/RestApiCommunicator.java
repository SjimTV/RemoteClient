package com.sjimtv.remoteclient.client;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.sjimtv.remoteclient.media.Status;
import com.sjimtv.remoteclient.utils.JsonConverter;

import org.json.JSONObject;

public class RestApiCommunicator {

    private static final String LOG_TAG = "API";

    private String ip;
    private int port;
    private RequestQueue queue;

    private Response.ErrorListener errorListener;


    public RestApiCommunicator(Context context, String ip, int port){
        this.ip = ip;
        this.port = port;
        queue = Volley.newRequestQueue(context);

        errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Log.i(LOG_TAG, "Error + " + error.getMessage());
            }
        };

        Log.i(LOG_TAG,String.format("Setup Rest API Communicator on IP %s and PORT %d", ip, port));
    }

    public void changeDevice(String ip){
        this.ip = ip;
        queue.cancelAll(this);
        Log.i(LOG_TAG,String.format("Setup Rest API Communicator on IP %s and PORT %d", ip, port));
    }


    public void pullShowsFromHost(Response.Listener<String> callback){
        String url ="http://" + ip + ":" + port + "/pull_shows";
        callGetRequest(url, callback);
    }

    public void updateStatus(Status status, Response.Listener<JSONObject> callback){
        String url ="http://" + ip + ":" + port + "/status/update";
        JSONObject jsonStatus = JsonConverter.convertStatusToJSONObject(status);
        callPostRequest(url, callback, jsonStatus);
    }

    public void getStatus(Response.Listener<String> callback) {
        String url ="http://" + ip + ":" + port + "/status/get";
        callGetRequest(url, callback);
    }

    public void adjustVolume(boolean adjustUp, Response.Listener<String> callback){
        String url ="http://" + ip + ":" + port + "/adjustVolume?adjustUp=";
        if (adjustUp) url += "true";
        else url += "false";

        callGetRequest(url, callback);
    }

    public void skipTime(boolean forward, Response.Listener<String> callback){
        String url ="http://" + ip + ":" + port + "/skipTime?forward=";
        if (forward) url += "true";
        else url += "false";

        callGetRequest(url, callback);
    }

    public void setPosition(float position, Response.Listener<String> callback){
        String url ="http://" + ip + ":" + port + "/position?position=";
        url += position;

        callGetRequest(url, callback);

    }

    public void getSubtitleDelay(Response.Listener<String> callback){
        String url ="http://" + ip + ":" + port + "/getSubtitleDelay";

        callGetRequest(url, callback);
    }

    public void setSubtitleDelay(float subtitleDelay, Response.Listener<String> callback){
        String url ="http://" + ip + ":" + port + "/setSubtitleDelay?subtitleDelaySeconds=";
        url += subtitleDelay;

        callGetRequest(url, callback);
    }

    public void subscribePositionlistener(boolean subscribe, Response.Listener<String> callback){
        String url ="http://" + ip + ":" + port + "/positionListener?subscribe=";
        if (subscribe) url += "true";
        else url += "false";

        callGetRequest(url, callback);
    }

    public void shutdown(Response.Listener<String> callback){
        String url ="http://" + ip + ":" + port + "/shutdown";

        callGetRequest(url, callback);
    }

    private void callGetRequest(String url, Response.Listener<String> callback){
        StringRequest stringRequest = new StringRequest(
                Request.Method.GET,
                url,
                callback, errorListener);
        queue.add(stringRequest);
        Log.i(LOG_TAG, "GET - " + url);
    }

    private void callPostRequest(String url, Response.Listener<JSONObject> callback, JSONObject postObject){
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                url,
                postObject,
                callback, errorListener);
        queue.add(jsonObjectRequest);
        Log.i(LOG_TAG, "POST - " + url);
    }


}
