package com.sjimtv.remoteclient.control;

import com.sjimtv.remoteclient.HomeActivity;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class Controller {

    public static boolean pause() throws ExecutionException, InterruptedException {

        CompletableFuture<Boolean> completableFuture
                = CompletableFuture.supplyAsync(() -> {
            try {
                return HomeActivity.restApiCommunicator.pause();
            } catch (IOException e) {
                e.printStackTrace();
                return true;
            }
        });

        return completableFuture.get(); //gives you the value
    }

    public static void stop(){

    }

    public static void open(String path){

    }
}
