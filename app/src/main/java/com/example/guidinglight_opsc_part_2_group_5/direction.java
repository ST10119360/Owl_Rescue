package com.example.guidinglight_opsc_part_2_group_5;


import android.graphics.Color;
import android.os.AsyncTask;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.PolyUtil;


import java.io.IOException;
import java.util.HashMap;


    public class direction extends AsyncTask<Object,String,String> {

        private GoogleMap googleMaps;
        private String link;
        private String direct;
        private String duration, distance;
        private LatLng latAndLong;
        @Override
        protected String doInBackground(Object... objects)
        {
            googleMaps = (GoogleMap)objects[0];
            link = (String)objects[1];
            latAndLong = (LatLng)objects[2];



            Link dwn = new Link();
            try
            {
                direct = dwn.readUrl(link);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }

            return direct;
        }

        @Override
        protected void onPostExecute(String string) {

            googleMaps.clear();
            HashMap<String, String> dlist = null;
            send_data phase = new send_data();
            dlist = phase.director(string);
            distance=dlist.get("distance");
            duration=dlist.get("duration");

            MarkerOptions options= new MarkerOptions();
            options.position(latAndLong);
            options.title("Destination");
            options.snippet("Time="+duration+"."+"Calculated Distance="+distance);
            googleMaps.addMarker(options);

            String[] drlist;
            send_data ph = new send_data();
            drlist = ph.direct(string);
            displayDirection(drlist);


        }

        public void displayDirection(String[] drli)
        {

            int count = drli.length;
            for(int i = 0;i<count;i++)
            {
                PolylineOptions options = new PolylineOptions();
                options.color(Color.RED);
                options.width(10);

                options.addAll(PolyUtil.decode(drli[i]));

                googleMaps.addPolyline(options);
            }
        }
    }


