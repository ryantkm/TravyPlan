package com.eventdee.travyplan.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.eventdee.travyplan.R;
import com.eventdee.travyplan.model.TravyPlace;
import com.eventdee.travyplan.utils.General;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PlaceAdapter extends FirestoreAdapter<PlaceAdapter.ViewHolder> {

    public interface OnPlaceSelectedListener {

        void onPlaceSelected(DocumentSnapshot place);

    }

    private OnPlaceSelectedListener mListener;

    public PlaceAdapter(Query query, OnPlaceSelectedListener listener) {
        super(query);
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_place, parent, false);
        return new ViewHolder(item);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(getSnapshot(position), mListener);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_place_name)
        TextView tvPlaceName;
        @BindView(R.id.tv_place_type)
        TextView tvPlaceType;
        @BindView(R.id.tv_place_time)
        TextView tvPlaceTime;

        @BindView(R.id.connected_line)
        View connectedLine;
        @BindView(R.id.iv_transport_icon)
        ImageView ivTransportIcon;
        @BindView(R.id.iv_empty_transport)
        ImageButton ivEmptyTransport;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(final DocumentSnapshot snapshot,
                         final OnPlaceSelectedListener listener) {

            TravyPlace travyPlace = snapshot.toObject(TravyPlace.class);

            // Load image
//            Glide.with(ivPhoto.getContext())
//                    .load(trip.getCoverPhoto())
//                    .apply(new RequestOptions()
//                            .centerCrop())
//                    .into(ivPhoto);

            tvPlaceName.setText(travyPlace.getName());
            tvPlaceType.setText(travyPlace.getType());
            tvPlaceTime.setText(General.timeFormat.format(travyPlace.getDate()));

            if (travyPlace.getTransportMode() == null) {
                ivEmptyTransport.setVisibility(View.VISIBLE);
                ivTransportIcon.setVisibility(View.GONE);
                connectedLine.setVisibility(View.GONE);
            } else {
                ivEmptyTransport.setVisibility(View.GONE);
                ivTransportIcon.setVisibility(View.VISIBLE);
                connectedLine.setVisibility(View.VISIBLE);
            }

            if (getAdapterPosition() == 0) {
                ivEmptyTransport.setVisibility(View.GONE);
                ivTransportIcon.setVisibility(View.GONE);
                connectedLine.setVisibility(View.GONE);
                setIsRecyclable(false);
            }

            // Click listener
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        listener.onPlaceSelected(snapshot);
                    }
                }
            });

            ivTransportIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(ivTransportIcon.getContext(), "show transport dialog " + getAdapterPosition(), Toast.LENGTH_SHORT).show();
                }
            });

            ivEmptyTransport.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(ivEmptyTransport.getContext(), "add transport " + getAdapterPosition(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
