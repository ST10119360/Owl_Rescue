package com.example.guidinglight_opsc_part_2_group_5;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FaveLandmarks extends AppCompatActivity {
    public ListView lsv;
    public ArrayAdapter<String> adapter;
    public ArrayList<String> list;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.favorites);

        getSupportActionBar().setTitle("Saved Places By Addresses");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        lsv = findViewById(R.id.lsv_LandMark);

        list=new ArrayList<String>();
        adapter= new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, list);
        lsv.setAdapter(adapter);


        DatabaseReference ref= FirebaseDatabase.getInstance().getReference();
        ref.child("save_data").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                String svd="";

                for(DataSnapshot s: snapshot.getChildren())
                {
                    svd=s.child("place").getValue(String.class);

                    list.add(svd);
                    adapter.notifyDataSetChanged();
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {

            }
        });

    }
    @Override
    public boolean onSupportNavigateUp() {

        Intent intent= new Intent(FaveLandmarks.this,HomePage.class);
        startActivity(intent);
        return true;
    }

}
