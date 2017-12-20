package com.eventdee.travyplan.adapter;

import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.eventdee.travyplan.R;
import com.eventdee.travyplan.model.Trip;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;

import java.text.SimpleDateFormat;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TripAdapter extends FirestoreAdapter<TripAdapter.TripViewHolder> {

    public interface OnTripSelectedListener {

        void onTripSelected(DocumentSnapshot trip);

    }

    private OnTripSelectedListener mListener;

    public TripAdapter(Query query, OnTripSelectedListener listener) {
        super(query);
        mListener = listener;
    }

    @Override
    public TripViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_trip, parent, false);
        return new TripViewHolder(item);
    }

    @Override
    public void onBindViewHolder(TripViewHolder holder, int position) {
        holder.bind(getSnapshot(position), mListener);
    }

    static class TripViewHolder extends ViewHolder {

        private static final SimpleDateFormat dateFormat  = new SimpleDateFormat(
                "dd.MM.yyyy", Locale.US);

        @BindView(R.id.iv_trip_photo)
        ImageView ivPhoto;
        @BindView(R.id.tv_trip_name)
        TextView tvName;
        @BindView(R.id.tv_trip_date)
        TextView tvDate;

        public TripViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(final DocumentSnapshot snapshot, final OnTripSelectedListener listener) {
            Trip trip = snapshot.toObject(Trip.class);

            // Load image
            Glide.with(ivPhoto.getContext())
                    .load(trip.getCoverPhoto())
                    .into(ivPhoto);

            tvName.setText(trip.getName());
            tvDate.setText(String.format("%s to %s", dateFormat.format(trip.getStartDate()), dateFormat.format(trip.getEndDate())));

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
