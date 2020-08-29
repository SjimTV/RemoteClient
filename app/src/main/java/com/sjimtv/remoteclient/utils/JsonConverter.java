package com.sjimtv.remoteclient.utils;


import com.google.gson.Gson;
import com.sjimtv.remoteclient.showStructure.Show;
import com.sjimtv.remoteclient.showStructure.Shows;

public class JsonConverter {
    public static String convertShowsToJson(Shows shows){
        return new Gson().toJson(shows);
    }

    public static Shows convertShowsFromJson(String json) {
        return new Gson().fromJson(json, Shows.class);
    }

    public static String convertShowToJson(Show show){
        return new Gson().toJson(show);
    }

    public static Show convertShowFromJson(String json) {
        return new Gson().fromJson(json, Show.class);
    }
}
