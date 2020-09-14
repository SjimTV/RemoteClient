package com.sjimtv.remoteclient.utils;


import com.google.gson.Gson;
import com.sjimtv.remoteclient.showStructure.Episode;
import com.sjimtv.remoteclient.showStructure.Show;
import com.sjimtv.remoteclient.showStructure.Shows;
import com.sjimtv.remoteclient.media.Status;

import org.json.JSONException;
import org.json.JSONObject;

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

    public static JSONObject convertEpisodeToJSONObject(Episode episode){
        try {
            return new JSONObject(new Gson().toJson(episode));
        } catch (JSONException e){
            e.printStackTrace();
            return new JSONObject();
        }
    }

    public static JSONObject convertStatusToJSONObject(Status status){
        try {
            return new JSONObject(new Gson().toJson(status));
        } catch (JSONException e){
            e.printStackTrace();
            return new JSONObject();
        }
    }
}
