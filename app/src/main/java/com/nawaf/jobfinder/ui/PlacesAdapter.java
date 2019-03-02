package com.nawaf.jobfinder.ui;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.nawaf.jobfinder.R;
import com.nawaf.jobfinder.common.OnRecyclerItemClickLister;

import java.util.ArrayList;
import java.util.List;

/**
 * Adapter for google places AutoComplete to show the
 */
public class PlacesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private ArrayList<AutocompletePrediction> predictionArrayList = new ArrayList<>();

    private OnRecyclerItemClickLister<AutocompletePrediction> onRecyclerItemClickLister;

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new PlaceViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_place, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {

        if (viewHolder instanceof PlaceViewHolder) {
            ((PlaceViewHolder) viewHolder).setText(predictionArrayList.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return predictionArrayList.size();
    }

    /**
     * Add callback for recyclerView item click
     *
     * @param onRecyclerItemClickLister item call back
     */
    public void setOnRecyclerItemClickLister(OnRecyclerItemClickLister<AutocompletePrediction> onRecyclerItemClickLister) {
        this.onRecyclerItemClickLister = onRecyclerItemClickLister;
    }

    /***
     * refresh all list
     * @param predictionArrayList new array list to be replaced with
     */
    public void updateList(List<AutocompletePrediction> predictionArrayList) {

        this.predictionArrayList.clear();

        this.predictionArrayList.addAll(predictionArrayList);

        notifyDataSetChanged();

    }


    protected class PlaceViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvPlace;

        public PlaceViewHolder(@NonNull View itemView) {
            super(itemView);
            tvPlace = itemView.findViewById(R.id.tvPlace);

            itemView.setOnClickListener(this);
        }

        void setText(AutocompletePrediction prediction) {

            String location = String.format("%s, %s", prediction.getPrimaryText(null).toString(), prediction.getSecondaryText(null).toString());

            tvPlace.setText(location);
        }

        @Override
        public void onClick(View v) {

            if (onRecyclerItemClickLister != null) {
                onRecyclerItemClickLister.onItemClicked(predictionArrayList.get(getAdapterPosition()));
            }
        }
    }

}
