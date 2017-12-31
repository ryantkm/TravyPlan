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

public class TransportIconAdapter extends RecyclerView.Adapter<TransportIconAdapter.IconViewHolder> {

    private Context mContext;
    private TypedArray mNames;
    private TypedArray mIcons;

    public TransportIconAdapter(Context mContext) {
        this.mContext = mContext;
        mNames = mContext.getResources().obtainTypedArray(R.array.transport_modes_array);
        mIcons = mContext.getResources().obtainTypedArray(R.array.transport_icons_array);
    }

    @Override
    public IconViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(mContext).inflate(R.layout.item_transport_icon, parent, false);
        return new IconViewHolder(item);
    }

    @Override
    public void onBindViewHolder(IconViewHolder holder, int position) {
        holder.mIvTransportIcon.setImageDrawable(mIcons.getDrawable(position));
        holder.mTvTransportName.setText(mNames.getString(position));
    }

    @Override
    public int getItemCount() {
        return mIcons.length();
    }

    class IconViewHolder extends RecyclerView.ViewHolder {

        ImageView mIvTransportIcon;
        TextView mTvTransportName;

        IconViewHolder(View itemView) {
            super(itemView);
            mIvTransportIcon = itemView.findViewById(R.id.iv_transport_icon);
            mTvTransportName = itemView.findViewById(R.id.tv_transport_name);
        }
    }
}
