package com.sjimtv.remoteclient.filemanager;

import android.content.SharedPreferences;
import android.util.Log;

import com.sjimtv.remoteclient.HomeActivity;
import com.sjimtv.remoteclient.showStructure.Shows;
import com.sjimtv.remoteclient.utils.JsonConverter;

public class FileManager {

    private SharedPreferences sharedPreferences;
    private Shows shows;

    public FileManager(HomeActivity homeActivity){
        sharedPreferences = homeActivity.getSharedPreferences("com.sjimtv.remoteclient", 0);
    }

    public void loadShowsFromDeviceBackup(){
        String showsBackupJson = sharedPreferences.getString("showsBackup", "");
        if (showsBackupJson != null && !showsBackupJson.isEmpty()) setShowsFromJSON(showsBackupJson);
        else Log.i("FILEMANAGER", "No backup found");
    }

    public void setShowsFromJSON(String jsonShows) {
        sharedPreferences.edit().putString("showsBackup", jsonShows).apply();
        this.shows = JsonConverter.convertShowsFromJson(jsonShows);
    }

    public Shows getShows() {
        return shows;
    }

    public void setShows(Shows shows) {
        this.shows = shows;
    }
}
