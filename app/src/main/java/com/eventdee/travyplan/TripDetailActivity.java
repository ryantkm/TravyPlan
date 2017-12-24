package com.eventdee.travyplan;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.eventdee.travyplan.model.Trip;
import com.eventdee.travyplan.utils.General;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TripDetailActivity extends AppCompatActivity implements EventListener<DocumentSnapshot> {

    private static final String TAG = TripDetailActivity.class.getSimpleName();

    public static final String KEY_TRIP_ID = "key_trip_id";

    @BindView(R.id.iv_trip_photo)
    ImageView ivTripPhoto;

    @BindView(R.id.tv_trip_dates)
    TextView tvTripDates;

    @BindView(R.id.recycler_trip_items)
    RecyclerView mTripItemsRecycler;

    @BindView(R.id.view_empty_trip_items)
    ViewGroup mEmptyView;

    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout mCollapsingToolbar;

    private FirebaseFirestore mFirestore;
    private DocumentReference mRestaurantRef;
    private ListenerRegistration mTripRegistration;

//    private TripItemAdapter mTripItemAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_detail);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Get restaurant ID from extras
        String tripId = getIntent().getExtras().getString(KEY_TRIP_ID);
        if (tripId == null) {
            throw new IllegalArgumentException("Must pass extra " + KEY_TRIP_ID);
        }

        // Initialize Firestore
        mFirestore = FirebaseFirestore.getInstance();

        // Get reference to the restaurant
        mRestaurantRef = mFirestore.collection("trips").document(tripId);

//        // Get trip items
//        Query itemsQuery = mRestaurantRef
//                .collection("ratings")
//                .orderBy("date", Query.Direction.DESCENDING)
//                .limit(50);
//
//        // RecyclerView
//        mTripItemAdapter = new TripItemAdapter(itemsQuery) {
//            @Override
//            protected void onDataChanged() {
//                if (getItemCount() == 0) {
//                    mTripItemsRecycler.setVisibility(View.GONE);
//                    mEmptyView.setVisibility(View.VISIBLE);
//                } else {
//                    mTripItemsRecycler.setVisibility(View.VISIBLE);
//                    mEmptyView.setVisibility(View.GONE);
//                }
//            }
//        };
//        mTripItemsRecycler.setLayoutManager(new LinearLayoutManager(this));
//        mTripItemsRecycler.setAdapter(mTripItemAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                Intent addTripToMain = new Intent(this, MainActivity.class);
                startActivity(addTripToMain);
                Toast.makeText(this, "back from Detail to Main", Toast.LENGTH_SHORT).show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStart() {
        super.onStart();

//        mTripItemAdapter.startListening();
        mTripRegistration = mRestaurantRef.addSnapshotListener(this);
    }

    @Override
    public void onStop() {
        super.onStop();

//        mTripItemAdapter.stopListening();

        if (mTripRegistration != null) {
            mTripRegistration.remove();
            mTripRegistration = null;
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_from_left, R.anim.slide_out_to_right);
    }

    /**
     * Listener for the Restaurant document ({@link #mRestaurantRef}).
     */
    @Override
    public void onEvent(DocumentSnapshot snapshot, FirebaseFirestoreException e) {
        if (e != null) {
            Log.w(TAG, "trip:onEvent", e);
            return;
        }

        onTripLoaded(snapshot.toObject(Trip.class));
    }

    private void onTripLoaded(Trip trip) {
        // Load image
        Glide.with(ivTripPhoto.getContext())
                .load(trip.getCoverPhoto())
                .apply(new RequestOptions()
                        .centerCrop())
                .into(ivTripPhoto);

        tvTripDates.setText(String.format("%s to %s", General.dateFormat.format(trip.getStartDate()), General.dateFormat.format(trip.getEndDate())));
        mCollapsingToolbar.setTitle(trip.getName());

    }

}
