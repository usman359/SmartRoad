package com.ass2.smart_road;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;



import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class RecyclerViewForPredictedResults extends AppCompatActivity {

    RecyclerView rv;
    myAdaptor adaptor;
    List<myModel> ls;
    String text, date_and_time;
    Calendar calendar;
    SimpleDateFormat simpleDateFormat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_view_for_predicted_results);

        rv=findViewById(R.id.rv);
        ls=new ArrayList<>();
        calendar = Calendar.getInstance();
        simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");



        text = "View left traffic sign prediction";
        date_and_time = simpleDateFormat.format(calendar.getTime());
        ls.add(new myModel(text, date_and_time));
        text = "View right traffic sign prediction";
        ls.add(new myModel(text, date_and_time));
        text = "View u turn traffic sign prediction";
        ls.add(new myModel(text, date_and_time));

        adaptor = new myAdaptor(ls, RecyclerViewForPredictedResults.this);
        RecyclerView.LayoutManager lm = new LinearLayoutManager(RecyclerViewForPredictedResults.this);
        rv.setLayoutManager(lm);
        rv.setAdapter(adaptor);
    }
}