package com.eventdee.travyplan;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.eventdee.travyplan.adapter.PlaceTypeIconAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PlaceTypeDialogFragment extends DialogFragment {

    public static final String TAG = "PlaceTypeDialog";
    private View mRootView;

    @BindView(R.id.recycler_transport_icons)
    RecyclerView mTransportIconsRecycler;

    private PlaceTypeIconAdapter mPlaceTypeIconAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.dialog_transport, container, false);
        ButterKnife.bind(this, mRootView);

        mPlaceTypeIconAdapter = new PlaceTypeIconAdapter(getContext());

        mTransportIconsRecycler.setAdapter(mPlaceTypeIconAdapter);
        GridLayoutManager mLinearLayoutManager = new GridLayoutManager(getContext(),3);
        mTransportIconsRecycler.setLayoutManager(mLinearLayoutManager);

        return mRootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        getDialog().getWindow().setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
    }
}
