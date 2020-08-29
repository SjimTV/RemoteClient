package com.sjimtv.remoteclient.client;

import android.util.Log;


public class ClientCommunicator {
    private final static String LOG_TAG = "ClientCom";

    public void getMessage(String message) {
       Log.i(LOG_TAG, message);
    }

}
