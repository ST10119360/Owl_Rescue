package com.example.SAWRC;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class Location extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener,
        GoogleMap.OnMarkerClickListener,
        GoogleMap.OnMarkerDragListener
{

    private GoogleMap googleMap;
    private double longLatitude;
    private double longLongitude;
    private double currentLatitude;
    private double currentlongitude;
    private GoogleApiClient client;
    private android.location.Location location;
    private Marker mark;
    private LocationRequest requestLocationUpdates;
    private int PROXIMITY_RADIUS = 10000;
    private String fastFood;
    private String hos;
    private String sch;
    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 110;
    private String pass;
    private AlertDialog.Builder startBuild;
    private double onLongLatitude;
    private double onLongLongitude;
    private String link;
    TextView name1,name2,name3,name4,name5;
    private String name6,name7,name9,name10,name11;
    private  String place;
    public String addresssssss;
    TextView text1,text2;
    EditText enteredPlace ;
    SupportMapFragment mapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_maps);


        enteredPlace = (EditText) findViewById(R.id.edt_search);
        Places.initialize(getApplicationContext(), "AIzaSyCFEmm4byf82ebpvEgSk0lzSQChvGdS7TA");

        enteredPlace.setFocusable(false);
        enteredPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Place.Field> fieldList = Arrays.asList(Place.Field.ADDRESS,
                        Place.Field.LAT_LNG, Place.Field.NAME);

                Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fieldList)
                        .build(Location.this);
                startActivityForResult(intent,100);

            }
        });



        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            permission();
        }

         mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.getActivity);
       mapFragment.getMapAsync(this);



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==100 && resultCode==RESULT_OK)
        {
            Place place= Autocomplete.getPlaceFromIntent(data);

            enteredPlace.setText(place.getAddress());
        }
        else if(resultCode== AutocompleteActivity.RESULT_ERROR)
        {
            Status sta=Autocomplete.getStatusFromIntent(data);
            Toast.makeText(Location.this, ""+sta.getStatusMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onMapReady(GoogleMap Map)
    {
        googleMap = Map;
        googleMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                googleMap.setMyLocationEnabled(true);
            }
        }
        else
        {
            buildGoogleApiClient();
            googleMap.setMyLocationEnabled(true);
        }
        googleMap.setOnMarkerDragListener(this);
        googleMap.setOnMarkerClickListener(this);

        googleMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(@NonNull LatLng getLatAndLong)
            {
                MarkerOptions opts = new MarkerOptions();

                startBuild=new AlertDialog.Builder(Location.this);
                startBuild.setTitle("Select What To Do:");

                CharSequence[] cha={"Display Direction","Add Favorite","Display Place","Cancel"};

                startBuild.setItems(cha, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {

                        switch (which)
                        {
                            case 0:

                                Geocoder onLong= new Geocoder(Location.this, Locale.getDefault());
                                List<Address> longMap=null;
                                try
                                {
                                    longMap=onLong.getFromLocation(getLatAndLong.latitude, getLatAndLong.longitude,1);
                                }
                                catch (IOException exception)
                                {
                                    exception.printStackTrace();
                                }


                                Object dataTransfer[] = new Object[2];
                                MarkerOptions mark = new MarkerOptions();

                                if (longMap != null)
                                {
                                    Address theAddress = longMap.get(0);


                                    if (theAddress.hasLatitude() && theAddress.hasLongitude())
                                    {

                                        googleMap.clear();
                                        onLongLatitude=theAddress.getLatitude();
                                        onLongLongitude=theAddress.getLongitude();

                                        LatLng latiAndLong = new LatLng(onLongLatitude, onLongLongitude);

                                        dataTransfer = new Object[3];
                                        link = onLongMap();
                                        direction direct = new direction();
                                        dataTransfer[0] = googleMap;
                                        dataTransfer[1] = link;
                                        dataTransfer[2] = new LatLng(onLongLatitude, onLongLongitude);

                                        direct.execute(dataTransfer);

                                        mark.position(latiAndLong);
                                        googleMap.addMarker(mark);
                                        googleMap.animateCamera(CameraUpdateFactory.newLatLng(latiAndLong));
                                        googleMap.animateCamera(CameraUpdateFactory.zoomTo(12));
                                        dialog.cancel();

                                    }
                                }
                                break;


                            case 1:

                                try
                                {

                                    Geocoder add= new Geocoder(Location.this, Locale.getDefault());
                                    List<Address> lst=null;


                                    lst=add.getFromLocation(getLatAndLong.latitude, getLatAndLong.longitude,1);
                                    Address myAddress = lst.get(0);

                                    addresssssss=myAddress.getAddressLine(0);



                                }
                                catch (IOException e)
                                {
                                    e.printStackTrace();
                                }


                                if(addresssssss=="" || addresssssss==null)
                                {
                                    Toast.makeText(Location.this, "No Place", Toast.LENGTH_SHORT).show();
                                }
                                else {

                                    save plac = new save(addresssssss);
                                    FirebaseDatabase.getInstance().getReference("save_data").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).push()
                                            .setValue(plac).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task)
                                                {
                                                    if (task.isSuccessful()) {
                                                        Toast.makeText(Location.this, "Saved", Toast.LENGTH_SHORT).show();
                                                    }
                                                    else
                                                    {
                                                        Toast.makeText(Location.this, "Not Saved", Toast.LENGTH_SHORT).show();

                                                    }
                                                }
                                            });

                                }
                                break;
                            case 2:

                                try
                                {

                                    Geocoder geocoder= new Geocoder(Location.this, Locale.getDefault());
                                    List<Address> lst=null;


                                    lst=geocoder.getFromLocation(getLatAndLong.latitude, getLatAndLong.longitude,1);

                                    googleMap.clear();
                                    LatLng getLatAndlongt= new LatLng(getLatAndLong.latitude, getLatAndLong.longitude);
                                    MarkerOptions mrk= new MarkerOptions();
                                    mrk.position(getLatAndlongt);
                                    googleMap.addMarker(mrk);
                                    googleMap.moveCamera(CameraUpdateFactory.newLatLng(getLatAndlongt));
                                    googleMap.animateCamera(CameraUpdateFactory.zoomTo(11));

                                }
                                catch (IOException e)
                                {
                                    e.printStackTrace();
                                }


                                break;

                            default:
                                dialog.dismiss();
                                break;
                        }

                    }

                });

                startBuild.show();

            }
        });
    }



    protected synchronized void buildGoogleApiClient()
    {
        client = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        client.connect();
    }


    public void onClick(View v)
    {
        Object dataTransfer[] = new Object[2];
        navigate_near_places navigate = new navigate_near_places();

        switch(v.getId())
        {
            case R.id.btn_Place:
            {



                place = enteredPlace.getText().toString();

                if(place.equals(""))
                {
                    enteredPlace.setError("Please enter place");
                    return;
                }
                else
                {
                    MarkerOptions opts = new MarkerOptions();

                    startBuild = new AlertDialog.Builder(Location.this);
                    startBuild.setTitle("Select What To Do:");

                    CharSequence[] cha = {"Display Direction", "Add Favorite", "Display Place", "Cancel"};

                    startBuild.setItems(cha, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            List<Address> seachedAddress = null;
                            Object dataTransfer[] = new Object[2];
                            navigate_near_places navigate = new navigate_near_places();

                            switch (which) {
                                case 0:
                                    if (!place.equals("")) {
                                        Geocoder geocoder = new Geocoder(Location.this);
                                        try
                                        {
                                            seachedAddress = geocoder.getFromLocationName(place, 1);

                                        } catch (IOException e)
                                        {
                                            e.printStackTrace();
                                        }

                                        if (seachedAddress != null)
                                        {
                                            Address addLongitudeAndLatitude = seachedAddress.get(0);

                                            if (addLongitudeAndLatitude.hasLatitude() && addLongitudeAndLatitude.hasLongitude()) {
                                                googleMap.clear();

                                                longLatitude = addLongitudeAndLatitude.getLatitude();
                                                longLongitude = addLongitudeAndLatitude.getLongitude();

                                                LatLng extractLat = new LatLng(longLatitude, longLongitude);

                                                dataTransfer = new Object[3];
                                                pass = place();
                                                direction dr = new direction();
                                                dataTransfer[0] = googleMap;
                                                dataTransfer[1] = pass;
                                                dataTransfer[2] = new LatLng(longLatitude, longLongitude);

                                                dr.execute(dataTransfer);

                                                opts.position(extractLat);
                                                googleMap.addMarker(opts);
                                                googleMap.animateCamera(CameraUpdateFactory.newLatLng(extractLat));
                                                googleMap.animateCamera(CameraUpdateFactory.zoomTo(12));
                                                enteredPlace.setText("");

                                            }

                                        }

                                    }
                                    else
                                    {
                                        Toast.makeText(Location.this, "Invalid Place", Toast.LENGTH_SHORT).show();
                                    }

                                    break;

                                case 1:
                                    if (place.equals(""))
                                    {
                                        Toast.makeText(Location.this, "No Place", Toast.LENGTH_SHORT).show();
                                    } else
                                    {

                                        save plac = new save(place);
                                        FirebaseDatabase.getInstance().getReference("save_data").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                .push().setValue(plac).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful())
                                                        {
                                                            Toast.makeText(Location.this, "Saved", Toast.LENGTH_SHORT).show();
                                                            enteredPlace.setText("");
                                                        } else
                                                        {
                                                            Toast.makeText(Location.this, "Not Saved", Toast.LENGTH_SHORT).show();

                                                        }
                                                    }
                                                });

                                    }
                                    break;
                                case 2:

                                    if (!place.equals("")) {
                                        Geocoder geocoder = new Geocoder(Location.this);
                                        List<Address> l = null;
                                        try {
                                            l = geocoder.getFromLocationName(place, 1);

                                            if (l != null) {
                                                for (int i = 0; i < l.size(); i++) {
                                                    googleMap.clear();
                                                    LatLng latLng = new LatLng(l.get(i).getLatitude(), l.get(i).getLongitude());
                                                    MarkerOptions markerOptions = new MarkerOptions();
                                                    markerOptions.position(latLng);
                                                    markerOptions.title(place);
                                                    googleMap.addMarker(markerOptions);
                                                    googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                                                    googleMap.animateCamera(CameraUpdateFactory.zoomTo(12));
                                                    dialog.cancel();
                                                    enteredPlace.setText("");
                                                }
                                            }
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                    else
                                    {
                                        Toast.makeText(Location.this, "Please enter Place to Search", Toast.LENGTH_SHORT).show();
                                    }


                                    break;

                                default:
                                    dialog.dismiss();
                                    break;


                            }

                        }

                    });
                    startBuild.show();

                }
            }
            break;









            case R.id.btnExit:

                System.exit(1);
                break;

            case R.id.btn_fav:

                Intent intent= new Intent(Location.this, FaveLandmarks.class);
                startActivity(intent);
                break;

            case R.id.btn_Menu:

                googleMap.clear();
                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

                MarkerOptions m = new MarkerOptions();
                m.position(latLng);
                m.title("Live");
                m.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                mark = googleMap.addMarker(m);

                googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                googleMap.animateCamera(CameraUpdateFactory.zoomTo(10));
                break;

            case R.id.btnRefresh:

                googleMap.clear();

                break;

        }
    }

    @Override
    public void onConnected(Bundle bundle)
    {
        requestLocationUpdates = new LocationRequest();
        requestLocationUpdates.setInterval(1000);
        requestLocationUpdates.setFastestInterval(1000);

        requestLocationUpdates.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED)
        {
            LocationServices.FusedLocationApi.requestLocationUpdates(client, requestLocationUpdates, this);
        }
    }
    @Override
    public void onConnectionSuspended(int i)
    {

    }

    @Override
    public void onLocationChanged(android.location.Location locate)
    {
        location = locate;

        if (mark != null)
        {
            mark.remove();
        }

        currentLatitude = location.getLatitude();
        currentlongitude = location.getLongitude();

        googleMap.clear();
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

        MarkerOptions m = new MarkerOptions();
        m.position(latLng);
        m.title("Live");
        m.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
        mark = googleMap.addMarker(m);

        googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(10));

        if (client != null)
        {
            LocationServices.FusedLocationApi.removeLocationUpdates(client, this);
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult)
    {
    }


    public boolean permission()
    {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED)
        {


            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION))
            {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);


            }
            else
            {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int code, String permissions[], int[] grantResults)
    {
        switch (code)
        {
            case MY_PERMISSIONS_REQUEST_LOCATION:
            {

                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {


                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        if (client == null)
                        {
                            buildGoogleApiClient();
                        }
                        googleMap.setMyLocationEnabled(true);
                    }

                }
                else
                {


                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
        super.onRequestPermissionsResult(code, permissions, grantResults);
    }


    @Override
    public boolean onMarkerClick(Marker marker)
    {
        Dialog dialog= new Dialog(Location.this);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.onmarkclick);

        name1=dialog.findViewById(R.id.name1);
        name2=dialog.findViewById(R.id.name2);
        name3=dialog.findViewById(R.id.name3);
        name4=dialog.findViewById(R.id.name4);
        name5=dialog.findViewById(R.id.name5);
        Button btn;
        btn=dialog.findViewById(R.id.btnCa);

        btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                dialog.cancel();
            }
        });

        Geocoder geocoder= new Geocoder(Location.this, Locale.getDefault());
        try
        {
            List<Address> lst= null;
            LatLng latPosition=marker.getPosition();

            lst=geocoder.getFromLocation(latPosition.latitude,latPosition.longitude,1);

            name6=lst.get(0).getAddressLine(0);
            name7=lst.get(0).getCountryName();
            name9=lst.get(0).getLocality();
            name10=lst.get(0).getPostalCode();
            name11=lst.get(0).getAdminArea();

            name1.setText(String.valueOf(name6.toString()));
            name2.setText(String.valueOf(name7.toString()));
            name3.setText(String.valueOf(name9.toString()));
            name4.setText(String.valueOf(name10.toString()));
            name5.setText(String.valueOf(name11.toString()));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }


        dialog.show();
        return false;
    }

    @Override
    public void onMarkerDragStart(Marker marker) {

    }

    @Override
    public void onMarkerDrag(Marker marker) {

    }

    @Override
    public void onMarkerDragEnd(Marker m) {

        longLatitude = m.getPosition().latitude;
        longLongitude=  m.getPosition().longitude;

    }
    private String place()
    {
        StringBuilder app = new StringBuilder("https://maps.googleapis.com/maps/api/directions/json?");
        app.append("origin="+currentLatitude+","+currentlongitude);
        app.append("&destination="+longLatitude+","+longLongitude);
        app.append("&key="+"AIzaSyAKL9DGUB_Fn1JMyNUDg7NanKGSnYi2Ne4");

        return app.toString();
    }

    private String retriveLink(double latitude, double longitude, String place)
    {
        StringBuilder app = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        app.append("location=" + latitude + "," + longitude);
        app.append("&radius=" + PROXIMITY_RADIUS);
        app.append("&type=" + place);
        app.append("&sensor=true");
        app.append("&key=" + "AIzaSyA-D3W5kPvRCz9T2duMCFag7MGHUpUtc-g");


        return (app.toString());
    }
    private String onLongMap()
    {
        StringBuilder app = new StringBuilder("https://maps.googleapis.com/maps/api/directions/json?");
        app.append("origin="+currentLatitude+","+currentlongitude);
        app.append("&destination="+onLongLatitude+","+onLongLongitude);
        app.append("&key="+"AIzaSyAKL9DGUB_Fn1JMyNUDg7NanKGSnYi2Ne4");

        return app.toString();
    }
}









