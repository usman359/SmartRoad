package com.ass2.smart_road;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class PersonalInformation extends AppCompatActivity {

    TextView nametext, mobilenumbertext, emailtext, gender, gendertext, dateofbirth, dateofbirthtext;

    String name, mobilenumber, email, GENDER, DATEOFBIRTH;

    String[] listItems;

    int year, month, day;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_information);

        nametext = findViewById(R.id.nametext);
        mobilenumbertext = findViewById(R.id.mobilenumbertext);
        emailtext = findViewById(R.id.emailtext);
//        gender = findViewById(R.id.gender);
//        gendertext = findViewById(R.id.gendertext);
//        dateofbirth = findViewById(R.id.dateofbirth);
//        dateofbirthtext = findViewById(R.id.dateofbirthtext);
        Calendar calendar = Calendar.getInstance();

        name = getIntent().getStringExtra("name");
        nametext.setText(name);

        mobilenumber = getIntent().getStringExtra("mobilenumber");
        if (mobilenumber != null)
            mobilenumbertext.setText(mobilenumber);

        else
            mobilenumbertext.setText("Logged in with gmail");


        email = getIntent().getStringExtra("email");
        if (email != null)
            emailtext.setText(email);

        else
            emailtext.setText("Logged in with phone number");


//        gender.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                listItems = new String[]{"Male", "Female", "Prefer not to sepecify"};
//                AlertDialog.Builder mBuilder = new AlertDialog.Builder(PersonalInformation.this);
//                mBuilder.setTitle("Choose your gender");
//                mBuilder.setSingleChoiceItems(listItems, -1, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        gendertext.setText(listItems[i]);
//                        GENDER = listItems[i];
//                        mBuilder.setCancelable(false);
//                        SharedPreferences sharedPreferences = getSharedPreferences("shared", MODE_PRIVATE);
//                        SharedPreferences.Editor editor = sharedPreferences.edit();
//                        editor.putString("text", GENDER);
//                        editor.apply();
//                        update();
////                        dialogInterface.dismiss();
//                    }
//                });
//
//
//                mBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//
//                    }
//                });
//
//                mBuilder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//
//                    }
//                });
////
//                // Show Alert Dialog
//                AlertDialog mDialog = mBuilder.create();
//                mDialog.show();
//            }
//        });
//
//
//        dateofbirth.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                    year = calendar.get(Calendar.YEAR);
//                    month = calendar.get(Calendar.MONTH);
//                    day = calendar.get(Calendar.DAY_OF_MONTH);
//                    DatePickerDialog datePickerDialog = new DatePickerDialog(PersonalInformation.this, new DatePickerDialog.OnDateSetListener() {
//                        @Override
//                        public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
////                            dateofbirthtext.setText(SimpleDateFormat.getDateInstance().format(calendar.getTime()));
//                            DATEOFBIRTH = i2 + "/" + (i1+1) + "/" + i;
//                            dateofbirthtext.setText(i2 + "/" + (i1+1) + "/" + i);
////                            SharedPreferences sharedPreferences = getSharedPreferences("shared", MODE_PRIVATE);
////                            SharedPreferences.Editor editor = sharedPreferences.edit();
////                            editor.putString("text", DATEOFBIRTH);
////                            editor.apply();
//                        }
//                    }, year, month, day);
//                    datePickerDialog.show();
//                }
//        });
    }

//    private void update() {
//        SharedPreferences sharedPreferences = getSharedPreferences("shared", MODE_PRIVATE);
//        String gen = sharedPreferences.getString("text", null);
//        gendertext.setText(gen);
//    }

}