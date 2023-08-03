package com.ass2.smart_road;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class HelpFragment extends Fragment {


    ImageView safetyandsecurity;
    TextView safetyandsecuritytext;
    ImageView arrow1;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



        View view = inflater.inflate(R.layout.fragment_help, container, false);

        safetyandsecurity = view.findViewById(R.id.safetyandsecurity);

        safetyandsecuritytext = view.findViewById(R.id.safetyandsecuritytext);

        arrow1 = view.findViewById(R.id.arrow1);


        safetyandsecurity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), HelpQuestions.class));
            }
        });

        safetyandsecuritytext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), HelpQuestions.class));
            }
        });

        return view;
    }


}