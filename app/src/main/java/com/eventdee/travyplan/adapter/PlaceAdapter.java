package com.eventdee.travyplan.adapter;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.eventdee.travyplan.R;
import com.eventdee.travyplan.model.TravyPlace;
import com.eventdee.travyplan.utils.General;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PlaceAdapter extends FirestoreAdapter<PlaceAdapter.ViewHolder> {

    private Context mContext;
//    private String mTripId;

    private String[] transportModeArray;
    private ArrayList<String> transportModeArrayList;
    private TypedArray transportIconArray;

    public interface OnPlaceSelectedListener {

        void onPlaceSelected(DocumentSnapshot place, int position);

        void onTransportSelected(DocumentSnapshot place);

        void onOptionSelected (DocumentSnapshot place, MenuItem item, int position);
    }

    private OnPlaceSelectedListener mListener;

    public PlaceAdapter(Context mContext, Query query, OnPlaceSelectedListener listener) {
        super(query);
        this.mContext = mContext;
        mListener = listener;
        // retrieving these arrays so as to know which icon to use for the returned transport mode
        transportModeArray = mContext.getResources().getStringArray(R.array.transport_modes_array);
        transportModeArrayList = new ArrayList<String>(Arrays.asList(transportModeArray));
        transportIconArray = mContext.getResources().obtainTypedArray(R.array.transport_icons_array);
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

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_place_name)
        TextView tvPlaceName;
        @BindView(R.id.tv_place_type)
        TextView tvPlaceType;
        @BindView(R.id.tv_place_time)
        TextView tvPlaceTime;
        @BindView(R.id.tv_place_date)
        TextView tvPlaceDate;

        @BindView(R.id.iv_more)
        ImageView ivMoreOptions;

        @BindView(R.id.iv_transport_icon)
        ImageView ivTransportIcon;

        @BindView(R.id.photos_slider)
        RelativeLayout mPhotosSlider;
//        BannerSlider mBannerSlider;

        @BindView(R.id.view_pager)
        ViewPager mViewPager;
        @BindView(R.id.tab_layout)
        TabLayout mTabLayout ;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(final DocumentSnapshot snapshot,
                         final OnPlaceSelectedListener listener) {

            // seems that without this line of code, the recycled itemview of the first one will be used and icon will be gone
            ivTransportIcon.setVisibility(View.VISIBLE);

            final TravyPlace travyPlace = snapshot.toObject(TravyPlace.class);

//            FirebaseFirestore mFirestore = FirebaseFirestore.getInstance();
//            mFirestore.collection("visibleTrips").document("E7LyBl61OcxNLRiXd3U4").collection("places")
//                    .add(travyPlace)
//                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//                        @Override
//                        public void onSuccess(DocumentReference documentReference) {
//                            String tripId = documentReference.getId();
//                            Log.d("Ryan", "DocumentSnapshot written with ID: " + tripId + " placename: " + travyPlace.getName());
//                        }
//                    })
//                    .addOnFailureListener(new OnFailureListener() {
//                        @Override
//                        public void onFailure(@NonNull Exception e) {
//                            Log.w("Ryan", "Error adding document", e);
//                        }
//                    });

            String transportMode = travyPlace.getTransportMode();
            int indexPosition = transportModeArrayList.indexOf(transportMode);

            if (indexPosition == -1) {
                indexPosition = transportModeArrayList.indexOf("others");
            }

            tvPlaceName.setText(travyPlace.getName());
            tvPlaceType.setText(General.setAndroidType(travyPlace.getPlaceTypes()));
            tvPlaceTime.setText(General.timeFormat.format(travyPlace.getDate()));
            tvPlaceDate.setText(General.dateFormatPlace.format(travyPlace.getDate()));

            if (travyPlace.getPhotos() != null) {
                mPhotosSlider.setVisibility(View.VISIBLE);
                PhotoPagerAdapter myCustomPagerAdapter = new PhotoPagerAdapter(mContext, travyPlace.getPhotos());
                mViewPager.setAdapter(myCustomPagerAdapter);

                mTabLayout.setupWithViewPager(mViewPager, true);
            } else {
                mPhotosSlider.setVisibility(View.GONE);
            }

            if (transportMode == null) {
                ivTransportIcon.setImageResource(R.drawable.ic_crop_free_36dp);
            } else {
                ivTransportIcon.setVisibility(View.VISIBLE);
                Drawable selectedTransportDrawable = transportIconArray.getDrawable(indexPosition);
                ivTransportIcon.setImageDrawable(selectedTransportDrawable);
            }
            if (getAdapterPosition() == 0) {
                ivTransportIcon.setVisibility(View.GONE);
//                setIsRecyclable(false);
            }

            // Click listener
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        listener.onPlaceSelected(snapshot, getAdapterPosition());
                    }
                }
            });

            ivTransportIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        listener.onTransportSelected(snapshot);
                    }
                }
            });

            ivMoreOptions.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        //Creating the instance of PopupMenu
                        PopupMenu popup = new PopupMenu(ivMoreOptions.getContext(),ivMoreOptions);
                        //Inflating the Popup using xml file
                        popup.getMenuInflater()
                                .inflate(R.menu.menu_pop_up_more, popup.getMenu());

                        //registering popup with OnMenuItemClickListener
                        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            public boolean onMenuItemClick(MenuItem item) {
                                listener.onOptionSelected(snapshot, item, getAdapterPosition());
                                return true;
                            }
                        });
                        popup.show(); //showing popup menu
                    }
                }
            });
        }
    }
}
