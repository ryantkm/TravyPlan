package com.eventdee.travyplan.adapter;

import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.eventdee.travyplan.R;
import com.eventdee.travyplan.model.Transport;
import com.eventdee.travyplan.model.TravyPlace;
import com.eventdee.travyplan.utils.General;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * RecyclerView adapter for a list of Restaurants.
 */
public class TripItemAdapter extends FirestoreAdapter<RecyclerView.ViewHolder> {

    private final int PLACE = 0, TRANSPORT = 1;

    public interface OnTripItemSelectedListener {

        void onTripItemSelected(DocumentSnapshot tripItem);

    }

    private OnTripItemSelectedListener mListener;

    public TripItemAdapter(Query query, OnTripItemSelectedListener listener) {
        super(query);
        mListener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch (viewType) {
            case PLACE:
                View placeView = inflater.inflate(R.layout.item_place, parent, false);
                viewHolder = new PlaceViewHolder(placeView);
                break;
            case TRANSPORT:
                View transportView = inflater.inflate(R.layout.item_transport, parent, false);
                viewHolder = new TransportViewHolder(transportView);
                break;
            default:
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case PLACE:
                PlaceViewHolder vh1 = (PlaceViewHolder) holder;
                vh1.bind(getSnapshot(position), mListener);
                break;
            case TRANSPORT:
                TransportViewHolder vh2 = (TransportViewHolder) holder;
                vh2.bind(getSnapshot(position), mListener);
                break;
            default:
                break;
        }
    }

    static class PlaceViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_place_name)
        TextView tvPlaceName;
        @BindView(R.id.tv_place_type)
        TextView tvPlaceType;
        @BindView(R.id.tv_place_time)
        TextView tvPlaceTime;

        public PlaceViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(final DocumentSnapshot snapshot,
                         final OnTripItemSelectedListener listener) {

            TravyPlace travyPlace = snapshot.toObject(TravyPlace.class);
            Resources resources = itemView.getResources();

            // Load image
//            Glide.with(ivPhoto.getContext())
//                    .load(trip.getCoverPhoto())
//                    .apply(new RequestOptions()
//                            .centerCrop())
//                    .into(ivPhoto);

            tvPlaceName.setText(travyPlace.getName());
            tvPlaceType.setText(travyPlace.getType());
            tvPlaceTime.setText(General.timeFormat.format(travyPlace.getDate()));

            // Click listener
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        listener.onTripItemSelected(snapshot);
                    }
                }
            });
        }
    }

    static class TransportViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.connected_line)
        View connectedLine;
        @BindView(R.id.iv_transport_icon)
        ImageView ivTransportIcon;

        public TransportViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(final DocumentSnapshot snapshot,
                         final OnTripItemSelectedListener listener) {

            Transport transport = snapshot.toObject(Transport.class);
            Resources resources = itemView.getResources();

            ivTransportIcon.setImageResource(R.drawable.ic_menu_camera);

            // Click listener
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        listener.onTripItemSelected(snapshot);
                    }
                }
            });
        }
    }

    @Override
    public int getItemViewType(int position) {

        String itemType = (String) getSnapshot(position).get("itemType");

        if (itemType.equalsIgnoreCase("transport")) {
            return TRANSPORT;
        } else if (itemType.equalsIgnoreCase("place")){
            return PLACE;
        } else {
            return -1;
        }
    }
}
