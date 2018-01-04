package com.eventdee.travyplan;

import android.content.Intent;
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

import butterknife.BindView;
import butterknife.ButterKnife;

public class PlaceDetailActivity extends AppCompatActivity {

    @BindView(R.id.toolbar_layout)
    CollapsingToolbarLayout mCollapsingToolbar;
    @BindView(R.id.tv_place_type)
    TextView mTvPlaceType;

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
        mTvPlaceType.setText(General.setAndroidType(mPlace.getPlaceTypes().get(0)));
    }
}
