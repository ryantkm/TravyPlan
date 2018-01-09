package com.eventdee.travyplan;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.eventdee.travyplan.model.TravyPlace;
import com.eventdee.travyplan.utils.Constants;
import com.eventdee.travyplan.utils.General;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceDetectionClient;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.eventdee.travyplan.TripDetailActivity.KEY_PLACE_ID;
import static com.eventdee.travyplan.TripDetailActivity.KEY_TRIP_ID;

public class AddPlaceActivity extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener{

    private Date mStartDate, mEndDate;
    private String mTripId, mPlaceId;
    private int mYear, mMonth, mDay, mHour, mMinute;
    private Calendar calendar = Calendar.getInstance();
    private String timeSelectedString;

    // google related variables;
    protected GeoDataClient mGeoDataClient;
    protected PlaceDetectionClient mPlaceDetectionClient;
    private GoogleApiClient mGoogleApiClient;

    // firestore variables;
    private FirebaseFirestore mFirestore;
    private DocumentReference mTripRef;

    private TravyPlace newTravyPlace;

    private String mSource;

    @BindView(R.id.date_picker)
    Button datePicker;
    @BindView(R.id.time_picker)
    Button timePicker;
    @BindView(R.id.place_picker)
    Button placePicker;
    @BindView(R.id.add)
    Button btnAdd;
    @BindView(R.id.et_add_notes)
    EditText editTextNotes;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_place);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        mSource = intent.getStringExtra("tag");
        mTripId = intent.getStringExtra(KEY_TRIP_ID);
        mStartDate = new Date(intent.getLongExtra("startDate",0));
        mEndDate = new Date(intent.getLongExtra("endDate",0));

        if (mSource.equalsIgnoreCase("PlaceDetailActivity")) {

            btnAdd.setText("Update");

            mTripId = intent.getStringExtra(KEY_TRIP_ID);
            mPlaceId = intent.getStringExtra(KEY_PLACE_ID);

            newTravyPlace = intent.getParcelableExtra("place");

            calendar.setTime(newTravyPlace.getDate());

            datePicker.setText(General.dateFormat.format(newTravyPlace.getDate()));
            datePicker.setBackgroundColor(getResources().getColor(R.color.colorPrimary));

            timePicker.setText(General.timeFormat.format(newTravyPlace.getDate()));
            timePicker.setBackgroundColor(getResources().getColor(R.color.colorPrimary));

            placePicker.setText(newTravyPlace.getName());
            placePicker.setBackgroundColor(getResources().getColor(R.color.colorPrimary));

            editTextNotes.setText(newTravyPlace.getNotes());
            editTextNotes.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        } else {
            btnAdd.setText("Add");
        }

        if (mTripId == null) {
            throw new IllegalArgumentException("Must pass extra " + KEY_TRIP_ID);
        }

        // Initialize Firestore
        mFirestore = FirebaseFirestore.getInstance();
        // Get reference to the trip
        mTripRef = mFirestore.collection("trips").document(mTripId);

        mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this, this)
                .build();

        // Construct a GeoDataClient.
        mGeoDataClient = Places.getGeoDataClient(this, null);

        // Construct a PlaceDetectionClient.
        mPlaceDetectionClient = Places.getPlaceDetectionClient(this, null);

        datePicker.setOnClickListener(this);
        timePicker.setOnClickListener(this);
        placePicker.setOnClickListener(this);
        btnAdd.setOnClickListener(this);

        // Get Current Date & Time
        mYear = calendar.get(Calendar.YEAR);
        mMonth = calendar.get(Calendar.MONTH);
        mDay = calendar.get(Calendar.DAY_OF_MONTH);
        mHour = calendar.get(Calendar.HOUR_OF_DAY);
        mMinute = calendar.get(Calendar.MINUTE);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        if (view == datePicker) {
            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {
                            calendar.set(year, monthOfYear, dayOfMonth);
                            datePicker.setText(String.format(General.dateFormat.format(calendar.getTime())));
                            datePicker.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                        }
                    }, mYear, mMonth, mDay);

            datePickerDialog.getDatePicker().setMaxDate(mEndDate.getTime());
            datePickerDialog.getDatePicker().setMinDate(mStartDate.getTime());

            datePickerDialog.show();
        }
        if (view == timePicker) {
            // Launch Time Picker Dialog
            TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                    new TimePickerDialog.OnTimeSetListener() {

                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay,
                                              int minute) {

                            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                            calendar.set(Calendar.MINUTE, minute);

                            String minuteSelectedString;
                            // adding "0" for single digit minute
                            if (minute < 10) {
                                minuteSelectedString = "0" + minute;
                            } else {
                                minuteSelectedString = String.valueOf(minute);
                            }
                            timeSelectedString = hourOfDay + ":" + minuteSelectedString;
                            timePicker.setText(timeSelectedString);
                            timePicker.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                        }
                    }, mHour, mMinute, true);
            timePickerDialog.show();
        }
        if (view == placePicker) {
            PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
            try {
                startActivityForResult(builder.build(this), Constants.RC_PLACE_PICKER);
            } catch (GooglePlayServicesRepairableException e) {
                e.printStackTrace();
            } catch (GooglePlayServicesNotAvailableException e) {
                e.printStackTrace();
            }
        }

        if (view == btnAdd) {
            if (mSource.equalsIgnoreCase("TripDetailActivity")) {
                if (datePicker.getText() == "") {
                    Toast.makeText(this, "Please select a date.", Toast.LENGTH_SHORT).show();
                } else if (newTravyPlace == null) {
                    Toast.makeText(this, "Please select a place.", Toast.LENGTH_SHORT).show();
                } else {
                    if (timePicker.getText() == "") {
                        calendar.set(Calendar.HOUR_OF_DAY, 0);
                        calendar.set(Calendar.MINUTE, 0);
                    }

                    newTravyPlace.setDate(calendar.getTime());
                    newTravyPlace.setNotes(editTextNotes.getText().toString().trim());

                    mTripRef.collection("places").add(newTravyPlace);
                    Toast.makeText(this, "location added: " + newTravyPlace.getName(), Toast.LENGTH_SHORT).show();
                    finish();
                }
            }else if (mSource.equalsIgnoreCase("PlaceDetailActivity")) {
                newTravyPlace.setDate(calendar.getTime());
                newTravyPlace.setNotes(editTextNotes.getText().toString().trim());

                mTripRef.collection("places").document(mPlaceId).set(newTravyPlace);
                Toast.makeText(this, "location updated: " + newTravyPlace.getName(), Toast.LENGTH_SHORT).show();

                Intent intent = new Intent();
                intent.putExtra("place", newTravyPlace);
                setResult(Activity.RESULT_OK, intent);

                finish();
            }
        }
    }

    private void updatePlace(Place place) {
        newTravyPlace = new TravyPlace();
        newTravyPlace.setId(place.getId());
        newTravyPlace.setPlaceTypes(place.getPlaceTypes());
        newTravyPlace.setAddress((place.getAddress() != null) ? place.getAddress().toString():null);
        newTravyPlace.setCountry((place.getLocale() != null) ? place.getLocale().getCountry():null);
        newTravyPlace.setCountryCode((place.getLocale() != null) ? place.getLocale().getISO3Country():null);
        newTravyPlace.setName((place.getName() != null) ? place.getName().toString():null);
        newTravyPlace.setLatitude(place.getLatLng().latitude);
        newTravyPlace.setLongtitude(place.getLatLng().longitude);
//                newTravyPlace.setGeoPoint(new GeoPoint(place.getLatLng().latitude, place.getLatLng().longitude));
        if (place.getViewport() != null) {

            newTravyPlace.setNorthEastViewportLatitude(place.getViewport().northeast.latitude);
            newTravyPlace.setNorthEastViewportLongtitude(place.getViewport().northeast.longitude);

            newTravyPlace.setSouthWestViewportLatitude(place.getViewport().southwest.latitude);
            newTravyPlace.setSouthWestViewportLongtitude(place.getViewport().southwest.longitude);

//                    newTravyPlace.setNorthEastLatLngBounds(new GeoPoint(place.getViewport().northeast.latitude, place.getViewport().northeast.longitude));
//                    newTravyPlace.setSouthWestLatLngBounds(new GeoPoint(place.getViewport().southwest.latitude, place.getViewport().southwest.longitude));
        }
        newTravyPlace.setWebsiteUri((place.getWebsiteUri() != null) ? place.getWebsiteUri().toString():null);
        newTravyPlace.setPhoneNumber((place.getPhoneNumber() != null) ? place.getPhoneNumber().toString():null);
        newTravyPlace.setRating(place.getRating());
        newTravyPlace.setPriceLevel(place.getPriceLevel());
        newTravyPlace.setAttributions((place.getAttributions() != null) ? place.getAttributions().toString():null);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.RC_PLACE_PICKER) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(this, data);
                placePicker.setText(place.getName());
                placePicker.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                updatePlace(place);
            }
        }
    }
}
