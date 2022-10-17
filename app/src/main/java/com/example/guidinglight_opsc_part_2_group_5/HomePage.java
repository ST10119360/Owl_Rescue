package com.example.guidinglight_opsc_part_2_group_5;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class HomePage extends AppCompatActivity {
    ActionBar act;
    Intent intent;
    private CardView viewMap, viewSettings, viewFavorites;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homepage);

        act = getSupportActionBar();
        act.setTitle("Main Page");

        viewMap = (CardView) findViewById(R.id.cardViewMap);
        viewSettings = (CardView) findViewById(R.id.carViewSettings);
        viewFavorites = (CardView) findViewById(R.id.cardViewFavorites);

        viewMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomePage.this, MapsActivity.class);
                startActivity(intent);
            }
        });

        viewSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(HomePage.this, SettingsPage.class);
                startActivity(intent);
            }
        });
        viewFavorites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent= new Intent(HomePage.this, FaveLandmarks.class);
                startActivity(intent);

            }
        });
    }


}

