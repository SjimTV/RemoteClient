package com.sjimtv.remoteclient.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;

import com.sjimtv.remoteclient.R;
import com.sjimtv.remoteclient.media.Controller;
import com.sjimtv.remoteclient.showStructure.Episodes;
import com.sjimtv.remoteclient.showStructure.Shows;
import com.sjimtv.remoteclient.utils.EpisodeRecyclerDivider;

public class ShowsActivity extends AppCompatActivity implements StatusRefreshable{

    private Shows shows;
    private int displayedShow;
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

        HomeActivity.setNoLimits(this);

        int highlightedShow = getIntent().getIntExtra("highlightedShow", 0);
        showsLayoutManager.scrollToPositionWithOffset(highlightedShow, 0);
        changeDisplayedEpisodes(highlightedShow);

        int highlightedEpisode = getIntent().getIntExtra("highlightedEpisode", 1);
        episodeLayoutManager.scrollToPositionWithOffset(highlightedEpisode, 0);


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

        EpisodeRecyclerDivider dividerItemDecoration = new EpisodeRecyclerDivider(this, getDivider());
        episodeRecyclerView.addItemDecoration(dividerItemDecoration);

        episodeRecyclerAdapter = new EpisodeRecyclerAdapter(this, displayedEpisodes, displayedShow);
        episodeRecyclerView.setAdapter(episodeRecyclerAdapter);
    }

    private Drawable getDivider(){
        Drawable drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.recyclerview_episode_divider, null);
        if (drawable == null) Log.i("DIVIDER", "ERROR");
        return drawable;
    }

    private void changeDisplayedEpisodes(int showIndex){
        displayedEpisodes = shows.get(showIndex).getEpisodes();

        episodeRecyclerAdapter.setEpisodes(displayedEpisodes, showIndex);

        displayedShow = showIndex;
    }

    public void getShowClicked(int position){
        changeDisplayedEpisodes(position);
    }


    public void getEpisodeClicked(int episode){
        Log.i("DEBUG", "SHOW " + displayedShow + " EPISODE " + episode);
        Controller.playEpisode(this, displayedShow, episode);
    }

    @Override
    public void refresh() {
        episodeRecyclerAdapter.notifyDataSetChanged();
    }
}