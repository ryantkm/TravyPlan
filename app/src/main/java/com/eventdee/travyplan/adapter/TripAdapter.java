package com.eventdee.travyplan.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.eventdee.travyplan.R;
import com.eventdee.travyplan.model.Trip;
import com.eventdee.travyplan.utils.General;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TripAdapter extends FirestoreAdapter<TripAdapter.ViewHolder> {

    public interface OnTripSelectedListener {

        void onTripSelected(DocumentSnapshot trip);

    }

    private OnTripSelectedListener mListener;

    public TripAdapter(Query query, OnTripSelectedListener listener) {
        super(query);
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_trip, parent, false);
        return new ViewHolder(item);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(getSnapshot(position), mListener);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iv_trip_photo_main)
        ImageView ivPhoto;
        @BindView(R.id.tv_trip_name_main)
        TextView tvTripName;
        @BindView(R.id.tv_trip_dates_main)
        TextView tvTripDates;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(final DocumentSnapshot snapshot, final OnTripSelectedListener listener) {
            Trip trip = snapshot.toObject(Trip.class);

            Glide.with(ivPhoto.getContext())
                    .load(trip.getCoverPhoto())
                    .apply(new RequestOptions()
                            .centerCrop())
                    .into(ivPhoto);

            tvTripName.setText(trip.getName());
            tvTripDates.setText(String.format("%s to %s", General.dateFormat.format(trip.getStartDate()), General.dateFormat.format(trip.getEndDate())));

            // Click listener
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        listener.onTripSelected(snapshot);
                    }
                }
            });
        }

    }
}
