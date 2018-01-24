package com.eventdee.travyplan.adapter;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.eventdee.travyplan.R;
import com.eventdee.travyplan.model.TravyPlace;
import com.eventdee.travyplan.utils.General;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ss.com.bannerslider.banners.Banner;
import ss.com.bannerslider.banners.RemoteBanner;
import ss.com.bannerslider.views.BannerSlider;

public class PlaceAdapter extends FirestoreAdapter<PlaceAdapter.ViewHolder> {

    private Context mContext;

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

        @BindView(R.id.banner_slider)
        BannerSlider mBannerSlider;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(final DocumentSnapshot snapshot,
                         final OnPlaceSelectedListener listener) {

            // seems that without this line of code, the recycled itemview of the first one will be used and icon will be gone
            ivTransportIcon.setVisibility(View.VISIBLE);

            TravyPlace travyPlace = snapshot.toObject(TravyPlace.class);
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
                mBannerSlider.setVisibility(View.VISIBLE);
                List<Banner> banners = new ArrayList<>();
                for (int i = 0; i < travyPlace.getPhotos().size(); i++) {
                    banners.add(new RemoteBanner(travyPlace.getPhotos().get(i)));
                }
                mBannerSlider.setBanners(banners);
            } else {
                mBannerSlider.setVisibility(View.GONE);
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
