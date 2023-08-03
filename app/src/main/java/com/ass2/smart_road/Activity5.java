package com.ass2.smart_road;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;


public class Activity5 extends AppCompatActivity {

    RelativeLayout row1, row2;
    String signBoard, sign;
    Uri image1, image2, image3, image4;
//    Button back;

    Button save;
    String firbaseImageUrl;
    String firebaseVideoUrl;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference mDatabaseRef;
    FirebaseAuth mAuth;
    Uri imageUri;
    ArrayList<Uri> imgs_Uri = new ArrayList<Uri>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_5);
        save = findViewById(R.id.saveResults);

        Intent i = getIntent();
        signBoard = i.getStringExtra("Board");
        sign = i.getStringExtra("Sign");


        image1 = i.getParcelableExtra("Img-1");
        image2 = i.getParcelableExtra("Img-2");
        image3 = i.getParcelableExtra("Img-3");
        image4 = i.getParcelableExtra("Img-4");

        if(signBoard.equals(" ") && sign.equals(" "))
        {
            startActivity(new Intent(getApplicationContext(), Activity3.class));
            finish();
        }

        Log.d( "Sign Board : ", signBoard);
        Log.d("Sign: ", sign);

        row1 = findViewById(R.id.row1);
        row2 = findViewById(R.id.row2);

        row1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Activity5.this, GoogleMaps.class);
                i.putExtra("sign board", signBoard);
                i.putExtra("sign", sign);
                startActivity(i);
            }
        });
//        back = findViewById(R.id.back);

        row2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Activity5.this, ViewPredictedTrafficSignBoard.class);
                i.putExtra("Img-1", image1);
                i.putExtra("Img-2", image2);
                i.putExtra("Img-3", image3);
                i.putExtra("Img-4", image4);
                i.putExtra("sign board", signBoard);
                i.putExtra("sign", sign);
                startActivity(i);
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // img 1
                Uri imageUri = getIntent().getParcelableExtra("Img-1");
                imgs_Uri.add(imageUri);

                // img 2
                imageUri = getIntent().getParcelableExtra("Img-2");
                imgs_Uri.add(imageUri);

                // img 3
                imageUri = getIntent().getParcelableExtra("Img-3");
                imgs_Uri.add(imageUri);

                // img 4
                imageUri = getIntent().getParcelableExtra("Img-4");
                imgs_Uri.add(imageUri);


                SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.CANADA);
                Date now = new Date();
                String filename = formatter.format(now);

                FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();

                Intent i = getIntent();
                String path;
                for (int j = 1; j <= 4; j++) {
                    imageUri = imgs_Uri.get(0);
                    path = "images/" + mAuth.getUid().toString() + "/" + filename + "/" + Integer.toString(j) + ".jpg";
                    store_in_firebase(firebaseStorage, path, filename, imageUri);
                }
            }
        });
    }

    void store_in_firebase(FirebaseStorage firebaseStorage, String path, String filename, Uri imageUri){
        StorageReference storageReference = firebaseStorage.getReference().child(path);

        storageReference.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                firbaseImageUrl = uri.toString();
                            }
                        });

                        Toast.makeText(getApplicationContext(), "Image stored in firebase", Toast.LENGTH_LONG).show();
                        FirebaseUser user = mAuth.getCurrentUser();

//                                    // for creating a users folder entry in the real time database
                        mDatabaseRef = FirebaseDatabase.getInstance().getReference("users");
                        imgs_record_firbase irf = new imgs_record_firbase(firbaseImageUrl, signBoard, sign);
                        mDatabaseRef.child(mAuth.getCurrentUser().getUid()).child("Imgs_Results").child(filename).setValue(irf);

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Failed save image to firebase", Toast.LENGTH_LONG).show();
                    }
                });
    }

}