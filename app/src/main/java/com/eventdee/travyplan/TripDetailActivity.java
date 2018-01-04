package com.eventdee.travyplan;

import android.content.ClipData;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.eventdee.travyplan.adapter.PlaceAdapter;
import com.eventdee.travyplan.model.TravyPlace;
import com.eventdee.travyplan.model.Trip;
import com.eventdee.travyplan.utils.Constants;
import com.eventdee.travyplan.utils.General;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TripDetailActivity extends AppCompatActivity implements EventListener<DocumentSnapshot>, PlaceAdapter.OnPlaceSelectedListener {

    public static final String TAG = TripDetailActivity.class.getSimpleName();
    public static final String KEY_TRIP_ID = "key_trip_id";
    public static final String KEY_PLACE_ID = "key_place_id";
    private String mTripId;
    private String mPlaceId;
    private Trip mTrip;

    @BindView(R.id.iv_trip_photo)
    ImageView ivTripPhoto;
    @BindView(R.id.tv_trip_dates)
    TextView tvTripDates;
    @BindView(R.id.recycler_trip_items)
    RecyclerView mPlaceRecycler;
    @BindView(R.id.view_empty_trip_items)
    ViewGroup mEmptyView;
    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout mCollapsingToolbar;

    private FirebaseFirestore mFirestore;
    private DocumentReference mTripRef;
    private ListenerRegistration mTripRegistration;

    private PlaceAdapter mPlaceAdapter;

    private TransportDialogFragment mTransportDialogFragment;

    private Date mStartDate, mEndDate;

    private ArrayList<String> mPhotosArray = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_detail);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addPlace = new Intent(getApplicationContext(), AddPlaceActivity.class);
                addPlace.putExtra("startDate", mStartDate.getTime());
                addPlace.putExtra("endDate", mEndDate.getTime());
                addPlace.putExtra(KEY_TRIP_ID, mTripId);
                startActivityForResult(addPlace, Constants.RC_ADD_TRIP_ITEM);
            }
        });

        // Get trip ID from extras
        mTripId = getIntent().getExtras().getString(KEY_TRIP_ID);
        if (mTripId == null) {
            throw new IllegalArgumentException("Must pass extra " + KEY_TRIP_ID);
        }

        // Initialize Firestore
        mFirestore = FirebaseFirestore.getInstance();

        // Get reference to the trip
        mTripRef = mFirestore.collection("trips").document(mTripId);

        // Get trip items
        Query itemsQuery = mTripRef
                .collection("places")
                .orderBy("date", Query.Direction.ASCENDING);

        // RecyclerView
        mPlaceAdapter = new PlaceAdapter(this, itemsQuery, this) {
            @Override
            protected void onDataChanged() {
                if (getItemCount() == 0) {
                    mPlaceRecycler.setVisibility(View.GONE);
                    mEmptyView.setVisibility(View.VISIBLE);
                } else {
                    mPlaceRecycler.setVisibility(View.VISIBLE);
                    mEmptyView.setVisibility(View.GONE);
                }
            }
        };
        mPlaceRecycler.setLayoutManager(new LinearLayoutManager(this));
        mPlaceRecycler.setAdapter(mPlaceAdapter);

        mTransportDialogFragment = new TransportDialogFragment();
    }

    @Override
    public void onStart() {
        super.onStart();

        mPlaceAdapter.startListening();
        mTripRegistration = mTripRef.addSnapshotListener(this);
    }

    @Override
    public void onStop() {
        super.onStop();

        mPlaceAdapter.stopListening();

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_trip_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case R.id.action_edit_cover:
                Intent editIntent = new Intent(this, AddTripActivity.class);
                editIntent.putExtra("trip", mTrip);
                editIntent.putExtra(KEY_TRIP_ID, mTripId);
                editIntent.putExtra("tag", TAG);
                startActivity(editIntent);
                return true;
            case R.id.action_delete_trip:
                mTripRef
                        .delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d(TAG, "Trip successfully deleted!");
                                Toast.makeText(TripDetailActivity.this, "Trip deleted!", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Error deleting document", e);
                            }
                        });
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Listener for the Trip document ({@link #mTripRef}).
     */
    @Override
    public void onEvent(DocumentSnapshot snapshot, FirebaseFirestoreException e) {
        if (e != null) {
            Log.w(TAG, "trip:onEvent", e);
            return;
        }
        if (snapshot.exists()) {
            onTripLoaded(snapshot.toObject(Trip.class));
        }
    }

    private void onTripLoaded(Trip trip) {
        mTrip = trip;
        // Load image
        Glide.with(ivTripPhoto.getContext())
                .load(trip.getCoverPhoto())
                .apply(new RequestOptions()
                        .centerCrop())
                .into(ivTripPhoto);

        mStartDate = trip.getStartDate();
        mEndDate = trip.getEndDate();
        tvTripDates.setText(String.format("%s to %s", General.dateFormat.format(mStartDate), General.dateFormat.format(mEndDate)));
        mCollapsingToolbar.setTitle(trip.getName());
    }

    @Override
    public void onPlaceSelected(DocumentSnapshot place) {
        mPlaceId = place.getId();
        TravyPlace travyPlace = place.toObject(TravyPlace.class);
        Intent placeIntent = new Intent(this, PlaceDetailActivity.class);
        placeIntent.putExtra(KEY_TRIP_ID, mTripId);
        placeIntent.putExtra(KEY_PLACE_ID, mPlaceId);
        placeIntent.putExtra("place", travyPlace);
        startActivity(placeIntent);
    }

    @Override
    public void onTransportSelected(DocumentSnapshot place) {
        mPlaceId = place.getId();
        mTransportDialogFragment.show(getSupportFragmentManager(), TransportDialogFragment.TAG);
    }

    @Override
    public void onOptionSelected(final DocumentSnapshot place, MenuItem item, final int position) {
        mPlaceId = place.getId();
        TravyPlace travyPlace = place.toObject(TravyPlace.class);
        if (travyPlace.getPhotos() != null) {
            mPhotosArray = travyPlace.getPhotos();
        }
        switch (item.getItemId()) {
            case R.id.action_delete_place:
                mTripRef.collection("places").document(mPlaceId)
                        .delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d(TAG, "Trip successfully deleted!");
                                Toast.makeText(TripDetailActivity.this, place.getString("name") + " deleted!", Toast.LENGTH_SHORT).show();
                                if (position == 0) {
                                    mPlaceRecycler.setAdapter(mPlaceAdapter);
//                                    mPlaceAdapter.notifyItemChanged(0);
                                    //TODO: need to refactor as removal animation is not smooth
                                }
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Error deleting document", e);
                            }
                        });
                break;
            case R.id.action_add_photos:
                mPlaceId = place.getId();
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(intent, Constants.RC_GET_IMAGE);
                }
                break;
        }
    }

    public void update(View view) {
        View childView = mTransportDialogFragment.mTransportIconsRecycler.findContainingItemView(view);
        int imagePosition = mTransportDialogFragment.mTransportIconsRecycler.getChildAdapterPosition(childView);
        String transportName = getResources().obtainTypedArray(R.array.transport_modes_array).getString(imagePosition);
        if (transportName.equalsIgnoreCase("none")) {
            transportName = null;
        }
        DocumentReference placeRef = mTripRef.collection("places").document(mPlaceId);
        placeRef
                .update("transportMode", transportName)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "TransportMode successfully updated!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error updating document", e);
                    }
                });
        mTransportDialogFragment.dismiss();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.RC_GET_IMAGE && resultCode == RESULT_OK && data != null) {
            if (data.getClipData() != null) {
                ClipData clipData = data.getClipData();
                for (int i = 0; i < clipData.getItemCount(); i++) {
                    ClipData.Item item = clipData.getItemAt(i);
                    String photoUri = item.getUri().toString();
                    mPhotosArray.add(photoUri);
                }
            } else if (data.getData() != null) {
                String photoUri = data.getData().toString();
                mPhotosArray.add(photoUri);
            }
            mTripRef.collection("places").document(mPlaceId).update("photos", mPhotosArray)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d(TAG, "DocumentSnapshot successfully updated!");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w(TAG, "Error updating document", e);
                        }
                    });
        }
    }
}
