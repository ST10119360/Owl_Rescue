package com.example.guidinglight_opsc_part_2_group_5;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.firebase.auth.FirebaseAuth;

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




    } @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;}

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.navSettings:
                startActivity(new Intent(getApplicationContext(), SettingsPage.class));
                finish();
                return true;
            case R.id.navLogout:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(), Login.class));
                finish();
                return true;
            case R.id.navFavorites:
                startActivity(new Intent(getApplicationContext(), FaveLandmarks.class));
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }
}

