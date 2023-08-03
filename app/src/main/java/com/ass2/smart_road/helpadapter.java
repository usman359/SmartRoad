package com.ass2.smart_road;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class helpadapter extends RecyclerView.Adapter<helpadapter.MyViewHolder> {
    List<myModel> ls;
    Context c;


    public helpadapter(List<String> ls, HelpFragment c) {
        ls = ls;
        c = c;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(c).inflate(R.layout.row,parent, false);
        return new MyViewHolder(row);
    }



    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.text.setText(ls.get(position).getText());
        holder.date_and_time.setText(ls.get(position).getDate_and_time());

    }

    @Override
    public int getItemCount() {
        return ls.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView text, date_and_time;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            text = itemView.findViewById(R.id.text);
            date_and_time = itemView.findViewById(R.id.date_and_time);
        }
    }
}

