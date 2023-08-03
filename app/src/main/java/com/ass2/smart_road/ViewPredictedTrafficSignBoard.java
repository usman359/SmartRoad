package com.ass2.smart_road;

import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ViewPredictedTrafficSignBoard extends AppCompatActivity {

    ImageView image1, image2, image3, image4, image5;

    String signBoard, sign;
    TextView status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_predicted_traffic_sign_board);


        image1 = findViewById(R.id.image1);
        image2 = findViewById(R.id.image2);
        image3 = findViewById(R.id.image3);
        image4 = findViewById(R.id.image4);
        image5 = findViewById(R.id.image5);
        status = findViewById(R.id.result);

        // getting uri of image, the image we capture in the HomeFragment Activity
        Uri uri1 = getIntent().getParcelableExtra("Img-1");
        image1.setImageURI(uri1);

//        Uri uri2 = getIntent().getParcelableExtra("Img-2");
        image2.setImageURI(uri1);
//
//        Uri uri3 =  getIntent().getParcelableExtra("Img-3");
        image3.setImageURI(uri1);
//
//        Uri uri4 = getIntent().getParcelableExtra("Img-4");
        image4.setImageURI(uri1);

        signBoard = getIntent().getStringExtra("sign board");
        sign = getIntent().getStringExtra("sign");


        if (signBoard.equals("Left sign Board") || sign.equals("Left sign")) {
            image5.setImageResource(R.drawable.turn_left);
        }

        else if (signBoard.equals("Right sign Board") || sign.equals("Right sign")) {
            image5.setImageResource(R.drawable.turn_right);
        }

        else if (signBoard.equals("Uturn sign Board") || sign.equals("Uturn sign")) {
            image5.setImageResource(R.drawable.u_turn);
        }

        if(signBoard.equals("Left sign Board")) status.setText("Sign Board Present");
        else if (signBoard.equals("Right sign Board")) status.setText("Sign Board Present");
        else if (signBoard.equals("Uturn sign Board")) status.setText("Sign Board Present");
        else status.setText("No sign Board Present");
    }
}