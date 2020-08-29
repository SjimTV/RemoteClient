package com.sjimtv.remoteclient.client;

import android.util.Log;
import com.sjimtv.remoteclient.showStructure.Shows;
import com.sjimtv.remoteclient.utils.JsonConverter;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class RestApiCommunicator {

    private String ip;
    private int port;

    private final OkHttpClient httpClient;

    public RestApiCommunicator(String ip, int port){
        this.ip = ip;
        this.port = port;
        httpClient = new OkHttpClient();
    }

    public boolean pause() throws IOException {
        String url ="http://" + ip + ":" + port + "/pause";

        String stringResponse = sendRequestForStringResponse(url);
        if (stringResponse.equals("playing")) return true;
        else if (stringResponse.equals("paused")) return false;

        throw new IOException("Invalid pause state returned " + stringResponse);
    }

    public Shows pullShowsFromHost() throws IOException {
        String url ="http://" + ip + ":" + port + "/pull_shows";
        Log.i("RESTAPI", url);

        String stringResponse = sendRequestForStringResponse(url);
        return JsonConverter.convertShowsFromJson(stringResponse);
    }

    private String sendRequestForStringResponse(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = httpClient.newCall(request).execute();
        if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
        if (response.body() == null)  throw new IOException("Empty response " + response);
        return response.body().string();
    }

}
