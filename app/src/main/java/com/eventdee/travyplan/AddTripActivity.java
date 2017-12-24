package com.eventdee.travyplan;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.ParseException;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.eventdee.travyplan.model.Trip;
import com.eventdee.travyplan.utils.Constants;
import com.eventdee.travyplan.utils.General;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddTripActivity extends AppCompatActivity {

    private static final String TAG = AddTripActivity.class.getSimpleName();

    @BindView(R.id.iv_trip_photo)
    ImageView ivTripPhoto;

    @BindView(R.id.et_trip_name)
    TextView etTripName;

    @BindView(R.id.tv_start_date)
    TextView tvStartDate;

    @BindView(R.id.tv_end_date)
    TextView tvEndDate;

    private int mYear, mMonth, mDay;
    private Calendar mCurrentDate = Calendar.getInstance();
    private Calendar mStartDate = Calendar.getInstance();
    private Calendar mEndDate = Calendar.getInstance();
    private Uri mPhotoUri;
    private String mTripTitle;
    private Trip mNewTrip;

    private FirebaseFirestore mFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_trip);
        ButterKnife.bind(this);

        // Firestore
        mFirestore = FirebaseFirestore.getInstance();

        etTripName.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_DONE) {
                    addNewTrip();
                    return true;
                }
                return false;
            }
        });

        mPhotoUri = General.getRandomDrawableUrl(this);

        Glide.with(ivTripPhoto.getContext())
                .load(mPhotoUri)
                .apply(new RequestOptions()
                        .centerCrop())
                .into(ivTripPhoto);

        tvStartDate.setText(General.dateFormat.format(mCurrentDate.getTimeInMillis()));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.RC_GET_IMAGE && resultCode == RESULT_OK && data != null) {
            mPhotoUri = data.getData();

            // Load image
            Glide.with(ivTripPhoto.getContext())
                    .load(mPhotoUri)
                    .apply(new RequestOptions()
                    .centerCrop())
                    .into(ivTripPhoto);
        }
    }

    public void selectDate(final View v) {
        if (v == tvStartDate) {
            mYear = mStartDate.get(Calendar.YEAR);
            mMonth = mStartDate.get(Calendar.MONTH);
            mDay = mStartDate.get(Calendar.DAY_OF_MONTH);
        } else {
            mYear = mEndDate.get(Calendar.YEAR);
            mMonth = mEndDate.get(Calendar.MONTH);
            mDay = mEndDate.get(Calendar.DAY_OF_MONTH);
        }
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        if (v == tvStartDate) {
                            mStartDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                            mStartDate.set(Calendar.MONTH, monthOfYear);
                            mStartDate.set(Calendar.YEAR, year);
                            tvStartDate.setText(General.dateFormat.format(mStartDate.getTimeInMillis()));
                        } else {
                            mEndDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                            mEndDate.set(Calendar.MONTH, monthOfYear);
                            mEndDate.set(Calendar.YEAR, year);
                            tvEndDate.setText(General.dateFormat.format(mEndDate.getTimeInMillis()));
                        }
                    }
                }, mYear, mMonth, mDay);
        if (v == tvStartDate && !tvEndDate.getText().equals("End Date")) {
            datePickerDialog.getDatePicker().setMaxDate(mEndDate.getTimeInMillis());
        } else if (v == tvEndDate){
            datePickerDialog.getDatePicker().setMinDate(mStartDate.getTimeInMillis());
        }
        datePickerDialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_trip, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_update) {
            addNewTrip();
        } else if (id == R.id.action_photo) {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(intent, Constants.RC_GET_IMAGE);
            }
        }

        return super.onOptionsItemSelected(item);
    }

    private void addNewTrip() {
        mTripTitle = etTripName.getText().toString().trim();
        mNewTrip = new Trip(mTripTitle, mStartDate.getTime(), mEndDate.getTime(), mPhotoUri.toString());

        try {
            mFirestore.collection("trips")
                    .add(mNewTrip)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            String tripId = documentReference.getId();
                            Log.d(TAG, "DocumentSnapshot written with ID: " + tripId);
                            // Go to the details page for adding details
                            Intent intent = new Intent(getApplicationContext(), TripDetailActivity.class);
                            intent.putExtra(TripDetailActivity.KEY_TRIP_ID, tripId);
                            startActivity(intent);
                            overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_to_left);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w(TAG, "Error adding document", e);
                        }
                    });
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
