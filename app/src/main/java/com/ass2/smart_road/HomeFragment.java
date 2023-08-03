package com.ass2.smart_road;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;



public class HomeFragment extends Fragment {

    RelativeLayout row1, row2, row3, row4, row5;

    ImageView titleImage;
    //    Button uploadphoto;
    ImageView image;
    ProgressDialog progressDialog;
    Button back;
    Uri imageUri;

    private String currentPhotoPath;
    String x;
    String name, email, phone;
    String firbaseImageUrl;
    String firebaseVideoUrl;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference mDatabaseRef;
    FirebaseAuth mAuth;
    SupportMapFragment supportMapFragment;
    GoogleMap map;
    FusedLocationProviderClient fusedLocationProviderClient;
    double currentLat = 0, currentLong = 0;
    Location currentLocation;
    CardView google_maps1;

    Boolean phoneNumberBoolean = false;
    Boolean emailBoolean = false;

    // Define ArrayLists to store latitude and longitude coordinates
    ArrayList<Double> latitudeArray = new ArrayList<>();
    ArrayList<Double> longitudeArray = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        row1 = view.findViewById(R.id.row1);
        row2 = view.findViewById(R.id.row2);
        row3 = view.findViewById(R.id.row3);
        //row4 = view.findViewById(R.id.row4);
        row5 = view.findViewById(R.id.row5);


        titleImage = view.findViewById(R.id.titleImage);


        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();


        Bundle data = this.getArguments();
        if (data != null) {
//            name = data.getString("name");
            email = data.getString("email");
            phone = data.getString("phone");
//            Log.d(name, "Name is:");
        }


        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());

        supportMapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.google_map);

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            getCurrentLocation();

        } else {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
        }


        google_maps1 = view.findViewById(R.id.google_maps1);
        google_maps1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(getContext(), "Hello", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), Activity3.class);
                startActivity(intent);
            }
        });

        titleImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), IntroToApp.class));
                getActivity().finish();
            }
        });

        // this listener is for taking image from camera
        row1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String fileName = "photo";
                File storageDirectory = getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);

                try {
                    File imageFile = File.createTempFile(fileName, ".jpg", storageDirectory);

                    currentPhotoPath = imageFile.getAbsolutePath();

                    imageUri = FileProvider.getUriForFile(getActivity(), "com.ass2.smart_road.fileprovider", imageFile);

                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                    startActivityForResult(intent, 1);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        // this listener is for selecting image from gallery
        row2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, 100);

            }
        });


        // this listener is for taking video from camera
        row3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LocationManager locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
                LocationListener locationListener = new LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {
                        double latitude = location.getLatitude();
                        double longitude = location.getLongitude();
                        // Add the latitude and longitude coordinates to the arrays
                        latitudeArray.add(latitude);
                        longitudeArray.add(longitude);
                    }

                    // Implement other methods of LocationListener as needed
                };

                // Step 2: Register the location listener to receive location updates
                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

                // Step 3: Start video capture
                Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                    startActivityForResult(intent, 2607);
                }
            }
        });


        // this listener is for uploading video from gallery
//        row4.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
//                startActivityForResult(intent, 2);
//            }
//        });


        //this listener is for viewing predicted results from recylcer view
        row5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), RecyclerViewForPredictedResults.class));
            }
        });

        return view;

    }

    private void getCurrentLocation() {

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
                            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                // TODO: Consider calling
                                //    ActivityCompat#requestPermissions
                                // here to request the missing permissions, and then overriding
                                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                //                                          int[] grantResults)
                                // to handle the case where the user grants the permission. See the documentation
                                // for ActivityCompat#requestPermissions for more details.
                                return;
                            }

                            map.setMyLocationEnabled(true);


                            map.getUiSettings().setCompassEnabled(true);
                            map.getUiSettings().setZoomControlsEnabled(false);
                            map.getUiSettings().setZoomGesturesEnabled(false);
                            map.getUiSettings().setMapToolbarEnabled(false);
                            map.getUiSettings().setMyLocationButtonEnabled(false);
                            map.getUiSettings().setScrollGesturesEnabled(false);
//                            map.setMapType(GoogleMap.MAP_TYPE_HYBRID);


                            Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
                            List<Address> addresses = null;
                            try {
                                addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

//                            googleMap.addMarker(new MarkerOptions()
//                                    .position(new LatLng(currentLat, currentLong))
//                                    .title(addresses.get(0).getAddressLine(0)));

                            map.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                    new LatLng(currentLat, currentLong), 14
                            ));

                            map.animateCamera(CameraUpdateFactory.newLatLngZoom(
                                    new LatLng(currentLat, currentLong), 14
                            ));

                            FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
                            LocationRequest locationRequest = new LocationRequest();
                            locationRequest.setInterval(1000);
                            locationRequest.setFastestInterval(500);
                            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);


                            LocationCallback locationCallback = new LocationCallback() {
                                @Override
                                public void onLocationResult(LocationResult locationResult) {
                                    if (locationResult == null) {
                                        return;
                                    }
                                    for (Location location : locationResult.getLocations()) {
                                        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                                        googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                                    }
                                }
                            };

// ...

                            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);

                        }
                    });
                }
            }
        });
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


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // if image is choosen from camera
        if (requestCode == 1 && resultCode == RESULT_OK) {

            Bitmap bitmap = BitmapFactory.decodeFile(currentPhotoPath);
//            image.setImageBitmap(bitmap);

//            image.setVisibility(View.VISIBLE);

//            uploadphoto.setVisibility(View.VISIBLE);
//            uploadphoto.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.CANADA);
//                    Date now = new Date();
//                    String filename = formatter.format(now);
//
//                    FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
//                    StorageReference storageReference = firebaseStorage.getReference().child("images/" + filename + ".jpg");
//
//                    storageReference.putFile(imageUri)
//                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                                @Override
//                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                                    storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                                        @Override
//                                        public void onSuccess(Uri uri) {
//                                            firbaseImageUrl = uri.toString();
//                                            Log.d("url", firbaseImageUrl);
//                                        }
//                                    });
//
////                                    image.setImageURI(null);
////                                    takephotofromcamera.setText("Take Another Photo");
////                                    uploadphoto.setVisibility(View.INVISIBLE);
//
//                                    Toast.makeText(getActivity(), "Image stored in firebase", Toast.LENGTH_LONG).show();
////                                    startActivity(new Intent(getActivity(), Activity4.class));
////                                    getActivity().finish();
//
//                                    FirebaseUser user = mAuth.getCurrentUser();
//
//                                    // for creating a users folder entry in the real time database
//                                    mDatabaseRef = FirebaseDatabase.getInstance().getReference("users");
////                            String uploadId = mDatabaseRef.push().getKey();
////                            Log.d(mAuth.getCurrentUser().getUid(), "UID");
//
//                                    SignInDetails signInDetails = null;
//                                    if (user.getPhoneNumber()!=null) {
//                                        phoneNumberBoolean = true;
//                                        signInDetails = new SignInDetails(user.getDisplayName(), "", phone , firbaseImageUrl, "");
//                                    }
//
//                                    if (user.getEmail()!=null) {
//                                        emailBoolean = true;
//                                        signInDetails = new SignInDetails(user.getDisplayName(), user.getEmail(), "" , firbaseImageUrl, "");
//                                    }
//
//                                    mDatabaseRef.child(mAuth.getCurrentUser().getUid()).setValue(signInDetails);
//
//
////                            Intent intent = new Intent(getApplicationContext(), FourImages.class);
////                            intent.putExtra("img", imageUri);
////                            startActivity(intent);
//
            Intent intent = new Intent(getActivity(), FourImages.class);
            intent.putExtra("img", imageUri);
            intent.putExtra("source", "camera");
            startActivity(intent);
//
//                                }
//                            })
//
//
//                            .addOnFailureListener(new OnFailureListener() {
//                                @Override
//                                public void onFailure(@NonNull Exception e) {
//
//
//                                    Toast.makeText(getActivity(), "Failed save image to firebase", Toast.LENGTH_LONG).show();
//                                }
//                            });
//
//
//
////                    image.setVisibility(View.INVISIBLE);
////                    uploadphoto.setVisibility(View.INVISIBLE);
//
//
////            Uri uri = data.getData();


        }
//
//            });


        // if image is chosen for gallery
        if (requestCode == 100 && resultCode == RESULT_OK) {

//            imageUri = data.getData();
//            image.setImageURI(imageUri);

            if (data.getData() != null) {

                Uri imageUri = data.getData();
                Intent intent = new Intent(getActivity(), FourImages.class);
                intent.putExtra("img", imageUri);
                intent.putExtra("source", "gallery");
                startActivity(intent);


//                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.CANADA);
//                    Date now = new Date();
//                    String filename = formatter.format(now);
//
//                    FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
//                    StorageReference storageReference = firebaseStorage.getReference().child("images/" + filename + ".jpg");
//
//                    storageReference.putFile(imageUri)
//                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                                @Override
//                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                                    storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                                        @Override
//                                        public void onSuccess(Uri uri) {
//                                            firbaseImageUrl = uri.toString();
//                                        }
//                                    });
////                                        image.setImageURI(null);
////                                        selectimagefromgallery.setText("Select Another Image From Gallery");
////                                        uploadphoto.setVisibility(View.INVISIBLE);
//
//                                    Toast.makeText(getActivity(), "Image stored in firebase", Toast.LENGTH_LONG).show();
//                                    FirebaseUser user = mAuth.getCurrentUser();
//
//                                    // for creating a users folder entry in the real time database
//                                    mDatabaseRef = FirebaseDatabase.getInstance().getReference("users");
////                            String uploadId = mDatabaseRef.push().getKey();
////                            Log.d(mAuth.getCurrentUser().getUid(), "UID");
//
//                                    SignInDetails signInDetails = new SignInDetails(user.getDisplayName(), user.getEmail(), "", firbaseImageUrl, "");
//                                    mDatabaseRef.child(mAuth.getCurrentUser().getUid()).setValue(signInDetails);
////                                    startActivity(new Intent(getActivity(), Activity4.class));
////                                        getActivity().finish();
//
//                                }
//                            })
//
//
//                            .addOnFailureListener(new OnFailureListener() {
//                                @Override
//                                public void onFailure(@NonNull Exception e) {
//
//
//                                    Toast.makeText(getActivity(), "Failed save image to firebase", Toast.LENGTH_LONG).show();
//                                }
//                            });


//                        image.setVisibility(View.INVISIBLE);
//                        uploadphoto.setVisibility(View.INVISIBLE);


                //  startActivity(new Intent(getActivity(), Activity4.class));
            }


//            if (data.getClipData() != null) {
//
//                int count = data.getClipData().getItemCount();
//                Uri[] imageUris = new Uri[count];

//                for (int i = 0; i < 4; i++) {
//                    imageUris[i] = data.getClipData().getItemAt(i).getUri();
//
////                    Log.d("length", String.valueOf(imageUris.length));
////                    Log.d("count", String.valueOf(count));
//                }
////                image.setVisibility(View.VISIBLE);
//                Bitmap bitmap = BitmapFactory.decodeFile(currentPhotoPath);
//                image.setImageBitmap(bitmap);


//                image.setVisibility(View.VISIBLE);

//                uploadphoto.setVisibility(View.VISIBLE);
//                uploadphoto.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {


//                for (int i = 0; i < count; i++) {
////                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.CANADA);
////                    Date now = new Date();
////                    String filename = formatter.format(now);
////
////                    FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
////                    StorageReference storageReference = firebaseStorage.getReference().child("images/" + filename + "(" + i + ")" + ".jpg");
////
////                    storageReference.putFile(imageUris[i])
////                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
////                                @Override
////                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
////                                    storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
////                                        @Override
////                                        public void onSuccess(Uri uri) {
////                                            firbaseImageUrl = uri.toString();
////                                        }
////                                    });
//////                                        image.setImageURI(null);
//////                                        selectimagefromgallery.setText("Select Another Image From Gallery");
//////                                        uploadphoto.setVisibility(View.INVISIBLE);
////
////                                    Toast.makeText(getActivity(), "Image stored in firebase", Toast.LENGTH_LONG).show();
////                                    FirebaseUser user = mAuth.getCurrentUser();
////
////                                    // for creating a users folder entry in the real time database
////                                    mDatabaseRef = FirebaseDatabase.getInstance().getReference("users");
//////                            String uploadId = mDatabaseRef.push().getKey();
//////                            Log.d(mAuth.getCurrentUser().getUid(), "UID");
////
////                                    SignInDetails signInDetails = new SignInDetails(user.getDisplayName(), user.getEmail(), "", firbaseImageUrl, "");
////                                    mDatabaseRef.child(mAuth.getCurrentUser().getUid()).setValue(signInDetails);
//////                                    startActivity(new Intent(getActivity(), Activity4.class));
//////                                        getActivity().finish();
////
////                                }
////                            })
////
////
////                            .addOnFailureListener(new OnFailureListener() {
////                                @Override
////                                public void onFailure(@NonNull Exception e) {
////
////
////                                    Toast.makeText(getActivity(), "Failed save image to firebase", Toast.LENGTH_LONG).show();
////                                }
////                            });
//
//
////                        image.setVisibility(View.INVISIBLE);
////                        uploadphoto.setVisibility(View.INVISIBLE);
//
//                }
//                startActivity(new Intent(getActivity(), Activity4.class));
//            }
////
//                });


        }

        // if video is choosen from camera
        if (requestCode == 2607 && resultCode == RESULT_OK) {
            Uri videoUri = data.getData();

            if (data != null) {

//                image.setVisibility(View.VISIBLE);
                Bitmap bitmap = BitmapFactory.decodeFile(videoUri.getPath());
//                image.setImageBitmap(bitmap);


//                image.setVisibility(View.VISIBLE);

//                uploadphoto.setVisibility(View.VISIBLE);
//                uploadphoto.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.CANADA);
//                Date now = new Date();
//                String filename = formatter.format(now);
//
//                FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
//                StorageReference storageReference = firebaseStorage.getReference().child("videos/" + filename + ".mp4");
//
//                storageReference.putFile(videoUri)
//                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//
//                            @Override
//                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                                storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                                    @Override
//                                    public void onSuccess(Uri uri) {
//                                        firebaseVideoUrl = uri.toString();
//                                    }
//                                });
////                                        image.setImageURI(null);
////                                        selectimagefromgallery.setText("Select Another Image From Gallery");
////                                        uploadphoto.setVisibility(View.INVISIBLE);
//
//                                Toast.makeText(getActivity(), "Video stored in firebase", Toast.LENGTH_LONG).show();
//                                FirebaseUser user = mAuth.getCurrentUser();
//
//                                // for creating a users folder entry in the real time database
//                                mDatabaseRef = FirebaseDatabase.getInstance().getReference("users");
//                            String uploadId = mDatabaseRef.push().getKey();
//                            Log.d(mAuth.getCurrentUser().getUid(), "UID");
//
//                                SignInDetails signInDetails = new SignInDetails(user.getDisplayName(), user.getEmail(), "", "", firebaseVideoUrl);
//                                mDatabaseRef.child(mAuth.getCurrentUser().getUid()).setValue(signInDetails);


                String[] projection = {MediaStore.Video.Media.DATA};
                Cursor cursor = getContext().getContentResolver().query(videoUri, projection, null, null, null);
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
                cursor.moveToFirst();
                String videoPath = cursor.getString(column_index);
                cursor.close();

                process_video result = new process_video(videoPath, getContext(),latitudeArray, longitudeArray);
                result.get_recommendation();

//                MediaMetadataRetriever retriever = new MediaMetadataRetriever();
//                retriever.setDataSource(videoPath);
//
//                // Get the duration of the video
//                String durationStr = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
//                long durationMs = Long.parseLong(durationStr);
//
//                // Calculate the number of frames to extract
//                int frameRate = 30;
//                int numFrames = (int) (durationMs / 1000.0 * frameRate);
//                Log.d("frames", String.valueOf(numFrames));
//
//                // Extract the frames from the video
//                Bitmap frame = null;
//                // Release the MediaMetadataRetriever
//                retriever.release();
//
//
//                Log.d("Latitude size", String.valueOf(latitudeArray.size()));
//                Log.d("Longitude size", String.valueOf(longitudeArray.size()));
//                // Step 6: Print the coordinates in a loop
//                for (int i = 0; i < latitudeArray.size(); i++) {
//                    double latitude = latitudeArray.get(i);
//                    double longitude = longitudeArray.get(i);
//                    Log.d("Coordinates", "Latitude: " + latitude + ", Longitude: " + longitude);
//                }

               // startActivity(new Intent(getActivity(), Activity4.class));
//                                        getActivity().finish();

            }
//                        })
//
//
//                        .addOnFailureListener(new OnFailureListener() {
//                            @Override
//                            public void onFailure(@NonNull Exception e) {
//
//
//                                Toast.makeText(getActivity(), "Failed save video to firebase", Toast.LENGTH_LONG).show();
//                            }
//                        });


//                        image.setVisibility(View.INVISIBLE);
//                        uploadphoto.setVisibility(View.INVISIBLE);


            // if video is taken from gallery
            if (requestCode == 2 && resultCode == RESULT_OK) {
                Uri selectedVideo = data.getData();
                String[] filePathColumn = {MediaStore.Video.Media.DATA};
                Cursor cursor = getActivity().getContentResolver().query(selectedVideo, filePathColumn, null, null, null);
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String filePath = cursor.getString(columnIndex);
                cursor.close();

                startActivity(new Intent(getActivity(), Activity6.class));
            }


        }

    }
}
