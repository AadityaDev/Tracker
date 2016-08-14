package com.skybee.tracker.ui.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

public class RoasterAdapter extends RecyclerView.Adapter<RoasterAdapter.RoasterViewHolder> {

    @Override
    public RoasterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(RoasterViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public static class RoasterViewHolder extends RecyclerView.ViewHolder {

        public RoasterViewHolder(View itemView) {
            super(itemView);
        }
    }
}
