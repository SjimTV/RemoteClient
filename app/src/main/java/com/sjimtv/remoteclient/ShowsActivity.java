package com.sjimtv.remoteclient;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.sjimtv.remoteclient.showStructure.Episodes;
import com.sjimtv.remoteclient.showStructure.Shows;

public class ShowsActivity extends AppCompatActivity {

    private Shows shows;
    private Episodes displayedEpisodes;

    private RecyclerView showsRecyclerView;
    private LinearLayoutManager showsLayoutManager;
    private ShowsRecyclerAdapter showsRecyclerAdapter;

    private RecyclerView episodeRecyclerView;
    private LinearLayoutManager episodeLayoutManager;
    private EpisodeRecyclerAdapter episodeRecyclerAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shows);
        shows = HomeActivity.fileManager.getShows();
        displayedEpisodes = shows.get(0).getEpisodes();

        setupShowRecyclerView();
        setupEpisodesRecyclerView();
    }

    private void setupShowRecyclerView(){
        showsRecyclerView = findViewById(R.id.showsRecyclerView);

        showsLayoutManager = new LinearLayoutManager(this);
        showsLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        showsRecyclerView.setLayoutManager(showsLayoutManager);
        showsRecyclerView.setItemAnimator(new DefaultItemAnimator());

        showsRecyclerAdapter = new ShowsRecyclerAdapter(this, shows);
        showsRecyclerView.setAdapter(showsRecyclerAdapter);

    }

    private void setupEpisodesRecyclerView(){
        episodeRecyclerView = findViewById(R.id.episodeRecyclerView);

        episodeLayoutManager = new LinearLayoutManager(this);
        episodeRecyclerView.setLayoutManager(episodeLayoutManager);
        episodeRecyclerView.setItemAnimator(new DefaultItemAnimator());

        episodeRecyclerAdapter = new EpisodeRecyclerAdapter(this, displayedEpisodes);
        episodeRecyclerView.setAdapter(episodeRecyclerAdapter);
    }

    private void changeDisplayedEpisodes(Episodes episodes){
        displayedEpisodes = episodes;
        episodeRecyclerAdapter.setEpisodes(displayedEpisodes);
    }

    public void getShowClicked(int position){
        Log.i("DEBUG", position + "");
        changeDisplayedEpisodes(shows.get(position).getEpisodes());
    }


    public void getEpisodeClicked(int position){
        Log.i("DEBUG", "EPISODE " + position);
    }


}