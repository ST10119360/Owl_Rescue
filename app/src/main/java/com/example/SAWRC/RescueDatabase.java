package com.example.SAWRC;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.guidinglight_opsc_part_2_group_5.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class RescueDatabase extends AppCompatActivity {
    public ListView lsv;
    public ArrayAdapter<String> adapter;
    public ArrayList<String> list;
    private DatabaseReference mDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rescue_locations);

        getSupportActionBar().setTitle("Saved Places By Addresses");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        lsv = findViewById(R.id.lstRescue);

        list=new ArrayList<String>();
        adapter= new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, list);
        lsv.setAdapter(adapter);

        DatabaseReference ref= FirebaseDatabase.getInstance().getReference();
    }
    public class Animal {

        public int Code;
        public String genus;

        public Animal() {
            // Default constructor required for calls to DataSnapshot.getValue(User.class)
        }

        public Animal(int code, String email) {
            this.Code = code;
            this.genus = email;
        }

        public void writeNewAnimal( int code, String genus) {
            Animal user = new Animal(code, genus);

            mDatabase.child("Animals").setValue(user);
        }

    }
}
