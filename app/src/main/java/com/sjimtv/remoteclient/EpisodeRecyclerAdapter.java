package com.sjimtv.remoteclient;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sjimtv.remoteclient.showStructure.Episodes;
import com.sjimtv.remoteclient.showStructure.Shows;

public class EpisodeRecyclerAdapter extends RecyclerView.Adapter<EpisodeRecyclerAdapter.EpisodeViewHolder> {

    private Activity activity;
    private Episodes episodes;

    public EpisodeRecyclerAdapter(Activity activity, Episodes episodes) {
        this.activity = activity;
        this.episodes = episodes;
    }

    public void setEpisodes(Episodes episodes) {
        this.episodes = episodes;
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
        TextView episodeViewName = holder.episodeViewName;
        episodeViewName.setText(episodes.get(position + 1).getName());
    }

    @Override
    public int getItemCount() {
        return episodes.size();
    }

    public static class EpisodeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView episodeViewName;
        ShowsActivity activity;

        public EpisodeViewHolder(@NonNull View itemView, Activity activity) {
            super(itemView);
            this.activity = (ShowsActivity) activity;
            this.episodeViewName = itemView.findViewById(R.id.episodeNameView);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            activity.getEpisodeClicked(getAdapterPosition());
        }
    }


}
