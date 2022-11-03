package com.example.SAWRC;

import android.os.AsyncTask;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class navigate_near_places extends AsyncTask<Object, String, String> {

    private String places;
    private GoogleMap googleMaps;
    private String link;

    @Override
    protected String doInBackground(Object... objects)
    {
        googleMaps = (GoogleMap)objects[0];
        link = (String)objects[1];

        Link downloadUrl = new Link();
        try {
            places = downloadUrl.readUrl(link);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return places;
    }

    @Override
    protected void onPostExecute(String s)
    {
        List<HashMap<String, String>> nearbyPlaceList = null;
        send_data parser = new send_data();
        nearbyPlaceList = parser.parse(s);
        showNearbyPlaces(nearbyPlaceList);
    }

    private void showNearbyPlaces(List<HashMap<String,String>> nearbyPlaceList)
    {
        for(int i = 0;i<nearbyPlaceList.size() ; i++)
        {
            MarkerOptions markerOptions = new MarkerOptions();
            HashMap<String , String> googlePlace = nearbyPlaceList.get(i);

            String placeName = googlePlace.get("place_name");
            String vicinity = googlePlace.get("vicinity");
            double lat = Double.parseDouble( googlePlace.get("lat") );
            double lng = Double.parseDouble( googlePlace.get("lng"));

            LatLng latLng = new LatLng(lat, lng);
            markerOptions.position(latLng);
            markerOptions.title(placeName +" : "+ vicinity);
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
            googleMaps.addMarker(markerOptions);
            googleMaps.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            googleMaps.animateCamera(CameraUpdateFactory.zoomTo(10));



        }


    }

}

