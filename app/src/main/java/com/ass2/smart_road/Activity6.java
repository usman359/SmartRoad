package com.ass2.smart_road;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class Activity6 extends AppCompatActivity {


    LoadingDialogForUploadVideoFromGallery loadingDialogForUploadVideoFromGallery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_6);

        loadingDialogForUploadVideoFromGallery = new LoadingDialogForUploadVideoFromGallery(this);
        loadingDialogForUploadVideoFromGallery.ShowDialog("Getting results...");

    }
}