package com.sjimtv.remoteclient.client;

import android.util.Log;

import com.sjimtv.remoteclient.ui.PositionListener;


public class ClientCommunicator {
    private final static String LOG_TAG = "ClientCom";
    private final static String POS_TAG = "POS:";

    private PositionListener positionListener;

    public ClientCommunicator(PositionListener positionListener) {
        this.positionListener = positionListener;
    }

    public void getMessage(String message) {
        if (message.contains(POS_TAG)) positionListener(message);
        else Log.i(LOG_TAG, message);
    }

    private void positionListener(String positionMessage) {
        try {
            float position = Float.parseFloat(positionMessage.replaceFirst(POS_TAG, ""));
            positionListener.positionListen(position);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Unexpected position message " + positionMessage);
        }


    }

}
