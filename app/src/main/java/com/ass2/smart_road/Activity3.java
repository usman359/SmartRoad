package com.ass2.smart_road;

import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;


import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class Activity3 extends AppCompatActivity {

    ImageView enable_gps_image;
    TextView enable_gps_text1, enable_gps_text2;
    BottomNavigationView bottomNavigationView;
    GoogleMap googleMap;
    private LocationRequest locationRequest;
    SupportMapFragment supportMapFragment;
    RelativeLayout gps_color_changer;
    String name, email, phone;
    boolean flagForGPS = true;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_3);



        HomeFragment homeFragment = new HomeFragment();
        HelpFragment helpFragment = new HelpFragment();
        ProfileFragment profileFragment = new ProfileFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager()
                .beginTransaction();

//        name = getIntent().getStringExtra("name");
        email = getIntent().getStringExtra("email");
        phone = getIntent().getStringExtra("phone");

//        ProfileFragment myFrag = new ProfileFragment();
//
//
//
        Bundle data = new Bundle();
//        data.putString("name", name);
        data.putString("email", email);
        data.putString("phone", phone);
//        Log.d("phone1", phone);
//
//        myFrag.setArguments(data);


        bottomNavigationView = findViewById(R.id.bottom_navigation);



//        enable_gps_image = findViewById(R.id.enable_gps_image);
//        enable_gps_text1 = findViewById(R.id.enable_gps_text1);
//        enable_gps_text2 = findViewById(R.id.enable_gps_text2);
//
//        gps_color_changer = findViewById(R.id.gps_color_changer);


        CheckGPS();


//        if (Build.VERSION.SDK_INT >= 21 && flagForGPS == true) {
//            Window window = this.getWindow();
//            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            window.setStatusBarColor(this.getResources().getColor(R.color.grey));
//            gps_color_changer.setBackground(this.getDrawable(R.color.white));
//
//
//        }

        getSupportFragmentManager().beginTransaction().replace(R.id.container, homeFragment).commit();

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, homeFragment).commit();
                        return true;

                    case R.id.help:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, helpFragment).commit();
                        return true;

                    case R.id.profile:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, profileFragment).commit();
                        return true;

                }
                return false;
            }
        });


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1001) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(this, "GPS Enabled", Toast.LENGTH_SHORT).show();
//                getSupportFragmentManager().beginTransaction().replace(R.id.container, homeFragment).commit();
//                enable_gps_image.setVisibility(View.GONE);
//                enable_gps_text1.setVisibility(View.GONE);
//                enable_gps_text2.setVisibility(View.GONE);
//                if (Build.VERSION.SDK_INT >= 21) {
//                    Window window = this.getWindow();
//                    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//                    window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//                    window.setStatusBarColor(this.getResources().getColor(R.color.grey));
//                }
//
//
//                gps_color_changer.setBackground(this.getDrawable(R.color.white));

//                getSupportFragmentManager().beginTransaction().replace(R.id.container, new HomeFragment()).commit();


            }
            if (resultCode == RESULT_CANCELED) {
//                Toast.makeText(this, "Denied Gps enable", Toast.LENGTH_SHORT).show();
//                enable_gps_image.setVisibility(View.VISIBLE);
//                enable_gps_text1.setVisibility(View.VISIBLE);
//                enable_gps_text2.setVisibility(View.VISIBLE);

//                getSupportFragmentManager().beginTransaction().hide(new HomeFragment()).commit();
                CheckGPS();
            }

        }
    }



    private void CheckGPS() {
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(3000);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest)
                .setAlwaysShow(true);

        Task<LocationSettingsResponse> locationSettingsResponseTask = LocationServices.getSettingsClient(getApplicationContext())
                .checkLocationSettings(builder.build());

        locationSettingsResponseTask.addOnCompleteListener(new OnCompleteListener<LocationSettingsResponse>() {
            @Override
            public void onComplete(@NonNull Task<LocationSettingsResponse> task) {
                try {
                    LocationSettingsResponse response = task.getResult(ApiException.class);
//                    enable_gps_image.setVisibility(View.GONE);
//                    enable_gps_text1.setVisibility(View.GONE);
//                    enable_gps_text2.setVisibility(View.GONE);


//                    Toast.makeText(Activity3.this, "Gps is already enable", Toast.LENGTH_SHORT).show();

                } catch (ApiException e) {
                    if (e.getStatusCode() == LocationSettingsStatusCodes.RESOLUTION_REQUIRED) {
//                        getSupportFragmentManager().beginTransaction().hide(homeFragment).commit();
//                        flagForGPS = false;
//
//                        Window window = getWindow();
//                        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//                        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//                        window.setStatusBarColor(getResources().getColor(R.color.goldon));
//                        gps_color_changer.setBackground(getDrawable(R.color.goldon));


//                        enable_gps_text1.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View view) {
                                ResolvableApiException resolvableApiException = (ResolvableApiException) e;
                                try {

//                                    enable_gps_text.setVisibility(View.INVISIBLE);
                                    resolvableApiException.startResolutionForResult(Activity3.this, 1001);
                                } catch (IntentSender.SendIntentException sendIntentException) {
//                                    enable_gps_text.setVisibility(View.VISIBLE);
                                    sendIntentException.printStackTrace();
                                }
//                                enable_gps_text.setVisibility(View.INVISIBLE);
//                            }
//                        });

//                        enable_gps_text2.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View view) {
//                                ResolvableApiException resolvableApiException = (ResolvableApiException) e;
//                                try {
////                                    enable_gps_text.setVisibility(View.INVISIBLE);
//                                    resolvableApiException.startResolutionForResult(Activity3.this, 1001);
//                                } catch (IntentSender.SendIntentException sendIntentException) {
////                                    enable_gps_text.setVisibility(View.VISIBLE);
//                                    sendIntentException.printStackTrace();
//                                }
////                                enable_gps_text.setVisibility(View.INVISIBLE);
//                            }
//                        });

//                        enable_gps_image.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View view) {
//                                ResolvableApiException resolvableApiException = (ResolvableApiException) e;
//                                try {
////                                    enable_gps_text.setVisibility(View.INVISIBLE);
//                                    resolvableApiException.startResolutionForResult(Activity3.this, 1001);
//                                } catch (IntentSender.SendIntentException sendIntentException) {
////                                    enable_gps_text.setVisibility(View.VISIBLE);
//                                    sendIntentException.printStackTrace();
//                                }
////                                enable_gps_text.setVisibility(View.INVISIBLE);
//                            }
//                        });

                    }
                    if (e.getStatusCode() == LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE) {
                        Toast.makeText(Activity3.this, "Settings not avaliable", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }


}
