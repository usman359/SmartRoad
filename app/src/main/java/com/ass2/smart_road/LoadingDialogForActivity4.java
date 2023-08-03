package com.ass2.smart_road;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.widget.TextView;



public class LoadingDialogForActivity4 {
    Context context;
    Dialog dialog;
    TextView textview;

    public LoadingDialogForActivity4(Context context) {
        this.context = context;
    }

    public void ShowDialog(String title) {
        dialog=new Dialog(context);
        dialog.setContentView(R.layout.activity_4);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        textview = dialog.findViewById(R.id.textview);

        textview.setText(title);
        dialog.create();
        dialog.show();
    }

    public void HideDialog()
    {
        dialog.dismiss();
    }
}
