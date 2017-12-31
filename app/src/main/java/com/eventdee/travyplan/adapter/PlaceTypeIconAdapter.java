package com.eventdee.travyplan.adapter;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.eventdee.travyplan.R;

public class PlaceTypeIconAdapter extends RecyclerView.Adapter<PlaceTypeIconAdapter.IconViewHolder> {

    private Context mContext;
    private TypedArray mNames;
    private TypedArray mIcons;

    public PlaceTypeIconAdapter(Context mContext) {
        this.mContext = mContext;
        mNames = mContext.getResources().obtainTypedArray(R.array.place_types_array);
        mIcons = mContext.getResources().obtainTypedArray(R.array.place_type_icons_array);
    }

    @Override
    public IconViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(mContext).inflate(R.layout.item_icon, parent, false);
        return new IconViewHolder(item);
    }

    @Override
    public void onBindViewHolder(IconViewHolder holder, int position) {
        holder.mIvPlaceTypeIcon.setImageDrawable(mIcons.getDrawable(position));
        holder.mTvPlaceTypeName.setText(mNames.getString(position));
    }

    @Override
    public int getItemCount() {
        return mIcons.length();
    }

    class IconViewHolder extends RecyclerView.ViewHolder {

        ImageView mIvPlaceTypeIcon;
        TextView mTvPlaceTypeName;

        IconViewHolder(View itemView) {
            super(itemView);
            mIvPlaceTypeIcon = itemView.findViewById(R.id.iv_transport_icon);
            mTvPlaceTypeName = itemView.findViewById(R.id.tv_transport_name);
        }
    }
}
