package com.sjimtv.remoteclient.ui;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sjimtv.remoteclient.R;
import com.sjimtv.remoteclient.showStructure.Episode;
import com.sjimtv.remoteclient.showStructure.Episodes;
import com.sjimtv.remoteclient.utils.TimeConverter;

import java.util.Locale;

public class EpisodeRecyclerAdapter extends RecyclerView.Adapter<EpisodeRecyclerAdapter.EpisodeViewHolder> {

    private Activity activity;
    private Episodes episodes;

    private int showIndex;

    public EpisodeRecyclerAdapter(Activity activity, Episodes episodes, int showIndex) {
        this.activity = activity;
        this.episodes = episodes;

        this.showIndex = showIndex;
    }

    public void setEpisodes(Episodes episodes, int showIndex) {
        this.episodes = episodes;
        this.showIndex = showIndex;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public EpisodeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_episode, parent, false);
        return new EpisodeViewHolder(view, activity);
    }

    @Override
    public void onBindViewHolder(@NonNull EpisodeViewHolder holder, int position) {
        int episodeNumber = position + 1;
        setupEpisodeInfo(holder, episodes.get(episodeNumber), episodeNumber);

        int playingShow = HomeActivity.status.getShowIndex();
        int playingEpisode = HomeActivity.status.getEpisodeNumber();
        if (showIndex == playingShow && episodeNumber == playingEpisode) {
            Log.i("Debug", episodes.get(episodeNumber).getName() + " PAUSE");
            holder.setPauseButton();
        }
        else {
            Log.i("Debug", "PS=" + playingShow + " PE=" + playingEpisode + " thisShow=" + showIndex + " thisEp=" + episodeNumber);
            Log.i("Debug", episodes.get(episodeNumber).getName() + " PLAY");
            holder.setPlayButton();
        }
    }

    private void setupEpisodeInfo(EpisodeViewHolder holder, Episode episode, int episodeNumber){
        setEpisodeNumber(holder.episodeNumberView, episodeNumber);
        setEpisodeName(holder.episodeViewName, episode);
        setEpisodeDuration(holder.episodeDurationView, episode);
    }

    private void setEpisodeNumber(TextView episodeNumberView, int episodeNumber){
        episodeNumberView.setText(String.format(Locale.ENGLISH, "%d", episodeNumber));
    }

    private void setEpisodeName(TextView episodeViewName, Episode episode){
        episodeViewName.setText(episode.getName());
    }

    private void setEpisodeDuration(TextView episodeDurationView, Episode episode){
        episodeDurationView.setText(TimeConverter.doubleToHourMinSec(episode.getDuration()));
    }


    @Override
    public int getItemCount() {
        return episodes.size();
    }

    public static class EpisodeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ShowsActivity activity;

        TextView episodeNumberView;
        TextView episodeViewName;
        TextView episodeDurationView;

        ImageView playButton;

        public EpisodeViewHolder(@NonNull View itemView, Activity activity) {
            super(itemView);
            this.activity = (ShowsActivity) activity;

            episodeNumberView = itemView.findViewById(R.id.episodeNumberView);
            episodeViewName = itemView.findViewById(R.id.episodeNameView);
            episodeDurationView = itemView.findViewById(R.id.episodeDuration);

            playButton = itemView.findViewById(R.id.playButton);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            int episode = getAdapterPosition() + 1;
            activity.getEpisodeClicked(episode);
        }

        public void setPlayButton(){
            playButton.setImageResource(R.drawable.ic_play);
        }

        public void setPauseButton(){
            playButton.setImageResource(R.drawable.ic_pause);
        }
    }


}
