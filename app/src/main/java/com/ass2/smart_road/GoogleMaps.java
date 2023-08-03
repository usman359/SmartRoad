package com.ass2.smart_road;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class GoogleMaps extends AppCompatActivity {

    Button back;
    SupportMapFragment supportMapFragment;
    static GoogleMap map;
    FusedLocationProviderClient fusedLocationProviderClient;
    double currentLat = 0, currentLong = 0;
    String signBoard, sign;
    String result_sign = " ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_maps);
        // getting results from parent activity
        Intent i = getIntent();
        signBoard = i.getStringExtra("sign board");
        sign = i.getStringExtra("sign");
        if(!signBoard.equals(" ")){
            if(signBoard.equals("Left sign Board")) result_sign = "left";
            else if(signBoard.equals("Right sign Board")) result_sign = "right";
            else if(signBoard.equals("Uturn sign Board")) result_sign = "uturn";
        }
        if (!sign.equals(" ")){
            if(sign.equals("Left sign")) result_sign = "left";
            else if(sign.equals("Right sign")) result_sign = "right";
            else if(sign.equals("Uturn sign")) result_sign = "uturn";
        }
//        back = findViewById(R.id.back);


        supportMapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.google_map);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        if (ActivityCompat.checkSelfPermission(GoogleMaps.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            getCurrentLocation();
        }

        else {
            ActivityCompat.requestPermissions(GoogleMaps.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
        }

//        back.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(getApplicationContext(), Activity5.class));
//                finish();
//            }
//        });


    }

    private void getCurrentLocation() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    currentLat = location.getLatitude();
                    currentLong = location.getLongitude();
                    supportMapFragment.getMapAsync(new OnMapReadyCallback() {
                        @Override
                        public void onMapReady(@NonNull GoogleMap googleMap) {
                            map = googleMap;

//                            map.getUiSettings().setCompassEnabled(true);
//                            map.getUiSettings().setZoomControlsEnabled(true);
//                            map.getUiSettings().setZoomGesturesEnabled(true);
//                            map.setMapType(GoogleMap.MAP_TYPE_HYBRID);


                            map.animateCamera(CameraUpdateFactory.newLatLngZoom(
                                    new LatLng(currentLat, currentLong), 14
                            ));

                            Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                            List<Address> addresses = null;
                            try {
                                addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            if(result_sign.equals("left")) {
                                Context context = getApplicationContext();
                                String drawableName = "turn_left"; // name of your drawable resource
                                int drawableId = context.getResources().getIdentifier(drawableName, "drawable", context.getPackageName());
                                googleMap.addMarker(new MarkerOptions()
                                        .position(new LatLng(currentLat, currentLong))
                                        .title(addresses.get(0).getAddressLine(0))
                                        .icon(bitmapDescriptorFromVector(getApplicationContext(), drawableId))
                                );
                            }
                            else if (result_sign.equals("right")) {
                                Context context = getApplicationContext();
                                String drawableName = "turn_right"; // name of your drawable resource
                                int drawableId = context.getResources().getIdentifier(drawableName, "drawable", context.getPackageName());
                                googleMap.addMarker(new MarkerOptions()
                                        .position(new LatLng(currentLat, currentLong))
                                        .title(addresses.get(0).getAddressLine(0))
                                        .icon(bitmapDescriptorFromVector(getApplicationContext(), drawableId))
                                );
                            }
                            else if (result_sign.equals("uturn")){
                                Context context = getApplicationContext();
                                String drawableName = "u_turn"; // name of your drawable resource
                                int drawableId = context.getResources().getIdentifier(drawableName, "drawable", context.getPackageName());
                                googleMap.addMarker(new MarkerOptions()
                                        .position(new LatLng(currentLat, currentLong))
                                        .title(addresses.get(0).getAddressLine(0))
                                        .icon(bitmapDescriptorFromVector(getApplicationContext(), drawableId))
                                );
                            }
                        }
                    });
                }
            }
        });
    }

    private BitmapDescriptor bitmapDescriptorFromVector(Context applicationContext, int turn_left) {
        Log.d("bitmapDescriptor: ", Integer.toString(turn_left));
        Drawable vectorDrawable = ContextCompat.getDrawable(applicationContext, turn_left);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getMinimumWidth(), vectorDrawable.getMinimumHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 44) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation();
            }
        }
    }

    private class PlaceTask extends AsyncTask<String, Integer, String> {
        @Override
        protected String doInBackground(String... strings) {
            String data = null;
            try {
                data = downloadUrl(strings[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Log.d("Places_dat", data);
            return data;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            new ParserTask().execute(s);
        }
    }

    private String downloadUrl(String string) throws IOException {
        URL url = new URL(string);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.connect();
        InputStream stream = connection.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        StringBuilder builder = new StringBuilder();
        String line = "";
        while ((line = reader.readLine()) != null) {
            builder.append(line);
        }

        String data = builder.toString();
        reader.close();
        return data;
    }

    static class ParserTask extends AsyncTask<String, Integer, List<HashMap<String, String>>> {

        @Override
        protected List<HashMap<String, String>> doInBackground(String... strings) {
//            JsonParser jsonParser = new JsonParser();
            List<HashMap<String,String>> mapList = null;

            JSONObject object = null;
            try {
                object = new JSONObject(strings[0]);
//                mapList = jsonParser.parseResult(object);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return mapList;
        }

        @Override
        protected void onPostExecute(List<HashMap<String, String>> hashMaps) {
            for(int i =0; i<hashMaps.size(); i++){

                HashMap<String,String> hashMapList = hashMaps.get(i);
                double lat = Double.parseDouble(hashMapList.get("lat"));
                double lng = Double.parseDouble(hashMapList.get("lng"));
                String name = hashMapList.get("name");
                LatLng latLng = new LatLng(lat,lng);
                map.addMarker(new MarkerOptions()
                        .position(latLng)
                        .title(name));

            }

        }
    }
}