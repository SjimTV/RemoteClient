package com.sjimtv.remoteclient;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.sjimtv.remoteclient.showStructure.Shows;

public class ShowsRecyclerAdapter extends RecyclerView.Adapter<ShowsRecyclerAdapter.ShowViewHolder> {

    private Activity activity;
    private Shows shows;

    public ShowsRecyclerAdapter(Activity activity, Shows shows) {
        this.activity = activity;
        this.shows = shows;
    }

    @NonNull
    @Override
    public ShowViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_show, parent, false);
        return new ShowViewHolder(view, activity);
    }

    @Override
    public void onBindViewHolder(@NonNull ShowViewHolder holder, int position) {
        TextView showNameView = holder.showNameView;
        showNameView.setText(shows.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return shows.size();
    }

    public static class ShowViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView showNameView;
        ShowsActivity activity;

        public ShowViewHolder(@NonNull View itemView, Activity activity) {
            super(itemView);
            this.activity = (ShowsActivity) activity;

            showNameView = itemView.findViewById(R.id.showNameView);

            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View view) {
            activity.getShowClicked(getAdapterPosition());
        }
    }


}
