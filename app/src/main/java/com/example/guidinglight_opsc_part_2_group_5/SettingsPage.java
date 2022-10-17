package com.example.guidinglight_opsc_part_2_group_5;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SettingsPage extends AppCompatActivity {
    SwitchCompat kilometer, miles;

    boolean switchKm, switchMile;
    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        getSupportActionBar().setTitle("Settings");

        preferences = getSharedPreferences("PREFS", 0);
        switchKm = preferences.getBoolean("switch1", false);
        switchMile = preferences.getBoolean("switch2", false);

        kilometer = (SwitchCompat) findViewById(R.id.switcKm);
        miles = (SwitchCompat) findViewById(R.id.switcMiles);

        kilometer.setChecked(switchKm);
        miles.setChecked(switchMile);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        ref.child("Settings").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot snap : snapshot.getChildren()) {

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        kilometer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchKm = !switchKm;
                kilometer.setChecked(switchKm);
                miles.setChecked(false);
                kilometer.setChecked(true);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putBoolean("switch1", switchKm);
                editor.apply();
                String Kilometer = "Kilometer";

                SettingClass set = new SettingClass(Kilometer);
                FirebaseDatabase.getInstance().getReference("Settings").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(set)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(SettingsPage.this, "Kilometers", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(SettingsPage.this, "Settings Failed To Change", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


            }
        });

        miles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchMile = !switchMile;
                miles.setChecked(switchMile);
                kilometer.setChecked(false);
                miles.setChecked(true);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putBoolean("switch2", switchMile);
                editor.apply();

                String mile = "Miles";

                SettingClass set = new SettingClass(mile);
                FirebaseDatabase.getInstance().getReference("Settings").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(set)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(SettingsPage.this, "Miles", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(SettingsPage.this, "Settings Failed To Change", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

            }
        });
    }
    @Override
    public boolean onSupportNavigateUp() {

        Intent intent= new Intent(SettingsPage.this,HomePage.class);
        startActivity(intent);
        return true;
    }
}
