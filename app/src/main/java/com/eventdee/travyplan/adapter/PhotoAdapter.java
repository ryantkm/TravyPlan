package com.eventdee.travyplan.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.eventdee.travyplan.R;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PhotoAdapter extends FirestoreAdapter<PhotoAdapter.ViewHolder> {

    public interface OnPhotoSelectedListener {
        void onPhotoSelected(DocumentSnapshot trip);
    }

    private OnPhotoSelectedListener mListener;

    public PhotoAdapter(Query query, OnPhotoSelectedListener listener) {
        super(query);
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_photo, parent, false);
        return new ViewHolder(item);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(getSnapshot(position), mListener);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iv_place_photo)
        ImageView ivPhoto;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(final DocumentSnapshot snapshot, final OnPhotoSelectedListener listener) {
            String placePhoto = (String) snapshot.get("name");

            Glide.with(ivPhoto.getContext())
                    .load(placePhoto)
                    .apply(new RequestOptions()
                            .centerCrop())
                    .into(ivPhoto);

            // Click listener
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        listener.onPhotoSelected(snapshot);
                    }
                }
            });
        }

    }
}
