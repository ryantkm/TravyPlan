package com.eventdee.travyplan;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.eventdee.travyplan.model.TravyPlace;
import com.eventdee.travyplan.utils.General;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.zhanghai.android.materialratingbar.MaterialRatingBar;

public class PlaceDetailActivity extends AppCompatActivity implements OnMapReadyCallback {

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
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        mPlace = intent.getParcelableExtra("place");

        mCollapsingToolbar.setTitle(mPlace.getName());
        mTvPlaceType.setText(General.setAndroidType(mPlace.getPlaceTypes()));
        mRatingIndicator.setRating(mPlace.getRating());
        mTvPlaceRating.setText(String.valueOf(mPlace.getRating()));
        mTvPlaceAddress.setText(mPlace.getAddress());
        mTvPlaceWebsite.setText(mPlace.getWebsiteUri());
        mTvPlacePhone.setText(mPlace.getPhoneNumber());
        mTvNotes.setText(mPlace.getNotes());

        // Get the SupportMapFragment and request notification
        // when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);
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
}
