package com.sjimtv.remoteclient.client;

import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client extends Thread{

    private static final String LOG_TAG = "CLIENTTAG";

    private final String ip;
    private final int port;
    private boolean isClientRunning;
    private PrintWriter bufferOut;
    private BufferedReader bufferIn;
    private Socket clientSocket;

    private ClientCommunicator clientCommunicator;

    public Client(String ip, int port){
        this.ip = ip;
        this.port = port;
    }

    @Override
    public void run() {
        Log.i(LOG_TAG, "Starting Client");
        try {
            runClient();
        } catch (UnknownHostException e) {
            Log.i(LOG_TAG, "Couldn't locate server IP...");
            e.printStackTrace();
        } catch (IOException e) {
            Log.i(LOG_TAG, "Couldn't connect to server...");
            e.printStackTrace();
        }

    }

    private void runClient() throws IOException {
        isClientRunning = true;
        openSocket();
        initializeBufferCommunication();

        clientCommunicator = new ClientCommunicator();
        listenForMessages();
    }

    private void openSocket() throws IOException {
        InetAddress serverIPAdress = InetAddress.getByName(ip);
        Log.i(LOG_TAG, "Connecting...");
        clientSocket = new Socket(serverIPAdress, port);
        Log.i(LOG_TAG, "Connection Accepted!");
    }

    private void initializeBufferCommunication() throws IOException {
        bufferOut = new PrintWriter(new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream())), true);
        bufferIn = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
    }

    private void listenForMessages() {
        while (isClientRunning) {
            String message = tryReadMessage();
            if (message != null) messageReceived(message);
            if (!clientSocket.isConnected()) close();
        }
    }

    private String tryReadMessage() {
        try {
            String message = bufferIn.readLine();
            if (isServerDead(message)) close();
            return message;
        } catch (IOException e) {
            Log.i(LOG_TAG, "Error reading message: " + e.getMessage());
            close();
            return null;
        }
    }

    private boolean isServerDead(String message){
        return message == null;
    }

    private void messageReceived(String message) {
        Log.i(LOG_TAG, message);
        clientCommunicator.getMessage(message);
    }

    public void sendMessage(final String message) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (bufferOut != null && !bufferOut.checkError()) {
                    bufferOut.println(message);
                    bufferOut.flush();
                }
            }
        }).start();
    }

    public void close() {
        Log.i(LOG_TAG, "Disconnecting Client");
        isClientRunning = false;
        cleanupBuffers();
    }

    private void cleanupBuffers() {
        if (bufferOut != null) {
            bufferOut.flush();
            bufferOut.close();
            bufferOut = null;
        }

        if (bufferIn != null) {
            bufferIn = null;
        }
    }


}
