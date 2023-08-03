package com.ass2.smart_road;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class FourImages extends AppCompatActivity {

    ImageView image1, image2, image3, image4;
    String currentPhotoPath;
    Uri imageUri;

    String firbaseImageUrl;

    Boolean img2Clicked = false, img3Clicked = false, img4Clicked = false;

    FirebaseUser user;

    DatabaseReference mDatabaseRef;
    // String array to store 4 encoded images
    ArrayList<Uri> Imgs_Uri_arr = new ArrayList<Uri>();

    FirebaseAuth mAuth;
    String src = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_four_images);

        mAuth = FirebaseAuth.getInstance();

        image1 = findViewById(R.id.image1);
        image2 = findViewById(R.id.image2);
        image3 = findViewById(R.id.image3);
        image4 = findViewById(R.id.image4);


        // getting uri of image, the image we capture in the HomeFragment Activity
        Uri uri = (Uri) getIntent().getParcelableExtra("img");
        image1.setImageURI(uri);
        // this src variable decide that from where we will take image
        // either from Gallery or from Camera
        src = getIntent().getStringExtra("source");
        // storing URI in ArrayList
        // it would be the 1st image's Uri
        Imgs_Uri_arr.add(uri);

        image2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                img2Clicked = true;
                Intent intent = null;
                if (src.equals("camera")) {
                    imageUri = Capture_from_Camera(); // method to capture image
                    intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                    startActivityForResult(intent, 1);
                }
                else if (src.equals("gallery")) {
                    intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(intent, 100);
                }
            }

        });

        image3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                img3Clicked = true;
                Intent intent = null;
                if (src.equals("camera")) {
                    imageUri = Capture_from_Camera(); // method to capture image
                    intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                    startActivityForResult(intent, 1);
                }
                else if (src.equals("gallery")) {
                    intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(intent, 100);
                }
            }
        });

        image4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                img4Clicked = true;
                Intent intent = null;
                if (src.equals("camera")) {
                    imageUri = Capture_from_Camera(); // method to capture image
                    intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                    startActivityForResult(intent, 1);
                }
                else if (src.equals("gallery")) {
                    intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(intent, 100);
                }
            }
        });


    }
    private Uri Capture_from_Camera() {
        String fileName = "photo";
        File storageDirectory = getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        try {
            File imageFile = File.createTempFile(fileName, ".jpg", storageDirectory);

            currentPhotoPath = imageFile.getAbsolutePath();

            return FileProvider.getUriForFile(getApplicationContext(), "com.ass2.smart_road.fileprovider", imageFile);

        } catch (IOException e) {
            e.printStackTrace();
        }
        // in case if in capturing image from camera failed then
        return null;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK) {

            Bitmap bitmap = BitmapFactory.decodeFile(currentPhotoPath);
            // storing the images URI in ArrayList
            // it would be 2nd, 3rd, and 4th image's URI
            Imgs_Uri_arr.add(imageUri);

            if (img2Clicked) {
                image2.setImageURI(imageUri);
                img2Clicked = false;
                image2.setScaleType(ImageView.ScaleType.CENTER_CROP);
                image3.setVisibility(View.VISIBLE);
            }

            if (img3Clicked) {
                image3.setImageURI(imageUri);
                img3Clicked = false;
                image3.setScaleType(ImageView.ScaleType.CENTER_CROP);
                image4.setVisibility(View.VISIBLE);
            }

            if (img4Clicked) {
                image4.setImageURI(imageUri);
                img4Clicked = false;
                image4.setScaleType(ImageView.ScaleType.CENTER_CROP);
                // moving to next activity
                Intent intent = new Intent(getApplicationContext(), Activity4.class);
                intent.putExtra("Img-1", Imgs_Uri_arr.get(0));
                intent.putExtra("Img-2", Imgs_Uri_arr.get(1));
                intent.putExtra("Img-3", Imgs_Uri_arr.get(2));
                intent.putExtra("Img-4", Imgs_Uri_arr.get(3));
                startActivity(intent);
                finish();
            }

//                            FirebaseUser user = mAuth.getCurrentUser();
//
//                            // for creating a users folder entry in the real time database
//                            mDatabaseRef = FirebaseDatabase.getInstance().getReference("users");
////                            String uploadId = mDatabaseRef.push().getKey();
////                            Log.d(mAuth.getCurrentUser().getUid(), "UID");
//
//                            SignInDetails signInDetails = new SignInDetails(user.getDisplayName(), user.getEmail(), "", firbaseImageUrl, "");
//                            mDatabaseRef.child(mAuth.getCurrentUser().getUid()).setValue(signInDetails);
////                            Intent intent = new Intent(getApplicationContext(), FourImages.class);
////                            intent.putExtra("img", imageUri);
////                            startActivity(intent);

//                        }
//                    })
//
//
//                    .addOnFailureListener(new OnFailureListener() {
//                        @Override
//                        public void onFailure(@NonNull Exception e) {
//
//
//                            Toast.makeText(getApplicationContext(), "Failed save image to firebase", Toast.LENGTH_LONG).show();
//                        }
//                    });



//                    image.setVisibility(View.INVISIBLE);
//                    uploadphoto.setVisibility(View.INVISIBLE);


//            Uri uri = data.getData();


        }
        // Image from Gallery
        else if (requestCode == 100 && resultCode == RESULT_OK) {
            if (data.getData() != null) {
                imageUri = data.getData();
                Imgs_Uri_arr.add(imageUri);
                if (img2Clicked) {
                    image2.setImageURI(imageUri);
                    img2Clicked = false;
                    image2.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    image3.setVisibility(View.VISIBLE);
                }

                if (img3Clicked) {
                    image3.setImageURI(imageUri);
                    img3Clicked = false;
                    image3.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    image4.setVisibility(View.VISIBLE);
                }

                if (img4Clicked) {
                    image4.setImageURI(imageUri);
                    img4Clicked = false;
                    image4.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    // moving to next activity
                    Intent intent = new Intent(getApplicationContext(), Activity4.class);
                    intent.putExtra("Img-1", Imgs_Uri_arr.get(0));
                    intent.putExtra("Img-2", Imgs_Uri_arr.get(1));
                    intent.putExtra("Img-3", Imgs_Uri_arr.get(2));
                    intent.putExtra("Img-4", Imgs_Uri_arr.get(3));
                    startActivity(intent);
                    finish();
                }
            }
        }
    }
}