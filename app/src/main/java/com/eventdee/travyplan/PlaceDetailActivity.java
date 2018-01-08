package com.eventdee.travyplan;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.eventdee.travyplan.model.TravyPlace;
import com.eventdee.travyplan.utils.Constants;
import com.eventdee.travyplan.utils.General;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.zhanghai.android.materialratingbar.MaterialRatingBar;

import static com.eventdee.travyplan.TripDetailActivity.KEY_PLACE_ID;
import static com.eventdee.travyplan.TripDetailActivity.KEY_TRIP_ID;

public class PlaceDetailActivity extends AppCompatActivity implements OnMapReadyCallback, NotesDialogFragment.NotesListener {

    public static final String TAG = PlaceDetailActivity.class.getSimpleName();

    @BindView(R.id.toolbar_layout)
    CollapsingToolbarLayout mCollapsingToolbar;
    @BindView(R.id.tv_place_type)
    TextView mTvPlaceType;
    @BindView(R.id.tv_place_rating)
    TextView mTvPlaceRating;
    @BindView(R.id.place_rating_bar)
    MaterialRatingBar mRatingIndicator;
    @BindView(R.id.tv_place_address)
    TextView mTvPlaceAddress;
    @BindView(R.id.tv_place_website)
    TextView mTvPlaceWebsite;
    @BindView(R.id.tv_place_phone)
    TextView mTvPlacePhone;
    @BindView(R.id.tv_notes)
    TextView mTvNotes;

    private TravyPlace mPlace;
    private String mTripId, mPlaceId;
    private Date mStartDate, mEndDate;
    private NotesDialogFragment mNotesDialog;

    private FirebaseFirestore mFirestore;
    private DocumentReference mPlaceRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_detail);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent editPlace = new Intent(getApplicationContext(), AddPlaceActivity.class);
                editPlace.putExtra("tag", TAG);
                editPlace.putExtra("place", mPlace);
                editPlace.putExtra(KEY_PLACE_ID, mPlaceId);
                editPlace.putExtra(KEY_TRIP_ID, mTripId);
                editPlace.putExtra("startDate", mStartDate.getTime());
                editPlace.putExtra("endDate", mEndDate.getTime());
                startActivityForResult(editPlace, Constants.RC_UPDATE_PLACE);

//                Snackbar.make(view, "Future Action: Display Edit Activity", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        mPlace = intent.getParcelableExtra("place");
        mTripId = intent.getStringExtra(KEY_TRIP_ID);
        mPlaceId = intent.getStringExtra(KEY_PLACE_ID);
        mStartDate = new Date(intent.getLongExtra("startDate",0));
        mEndDate = new Date(intent.getLongExtra("endDate",0));

        // Initialize Firestore
        mFirestore = FirebaseFirestore.getInstance();

        // Get reference to the place
        mPlaceRef = mFirestore.collection("trips").document(mTripId).collection("places").document(mPlaceId);

        updateUI();

        // Get the SupportMapFragment and request notification
        // when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);
    }

    private void updateUI() {
        mCollapsingToolbar.setTitle(mPlace.getName());
        mTvPlaceType.setText(General.setAndroidType(mPlace.getPlaceTypes()));
        mRatingIndicator.setRating(mPlace.getRating());
        mTvPlaceRating.setText(String.valueOf(mPlace.getRating()));
        mTvPlaceAddress.setText(mPlace.getAddress());
        mTvPlaceWebsite.setText(mPlace.getWebsiteUri());
        mTvPlacePhone.setText(mPlace.getPhoneNumber());
        mTvNotes.setText(mPlace.getNotes());
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        // Add a marker and move the map's camera to the location.
        LatLng placeCoordinates = new LatLng(mPlace.getLatitude(), mPlace.getLongtitude());
        googleMap.addMarker(new MarkerOptions().position(placeCoordinates)
                .title(mPlace.getName()));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(placeCoordinates));
    }

    public void callPlace(View view) {
        String phoneNumber = mPlace.getPhoneNumber();
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + phoneNumber));
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    public void goToWebsite(View view) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_BROWSABLE);
        intent.setData(Uri.parse(mPlace.getWebsiteUri()));
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    public void goToMap(View view) {
        String mapUri = "https://www.google.com/maps/search/?api=1&query="
                + mPlace.getLatitude()
                + ","
                + mPlace.getLongtitude()
                +"&query_place_id="
                + mPlace.getId();
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(mapUri));
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    public void editNotes(View view) {
//        mNotesDialog.show(getSupportFragmentManager(), NotesDialogFragment.TAG);

        FragmentManager fm = getSupportFragmentManager();
        mNotesDialog = NotesDialogFragment.newInstance(mPlace.getNotes());
        mNotesDialog.show(fm, NotesDialogFragment.TAG);

    }


    @Override
    public void onClick(final String notes) {
        mPlaceRef.update("notes", notes)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully updated!");
                        mTvNotes.setText(notes);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error updating document", e);
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == Constants.RC_UPDATE_PLACE) {

            if (resultCode == Activity.RESULT_OK) {
                mPlace = data.getParcelableExtra("place");
                updateUI();

            } else if (resultCode == Activity.RESULT_CANCELED) {
                // some stuff that will happen if there's no result
            }
        }
    }
}
