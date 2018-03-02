package com.android.lab.androidattendancesystem;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

public class Home extends AppCompatActivity {

    Button hod, std, tech, di_but_submit;
    Dialog dialog;
    EditText di_edt_ip;

    Activity activity;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        activity = this;
        context = getApplicationContext();

        Window window = activity.getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(ContextCompat.getColor(activity, R.color.window_bar));
        }

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            //setting action bar with custom color defined in colors
            actionBar.setDisplayHomeAsUpEnabled(false);
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(context, R.color.action_bar)));
            actionBar.setTitle("Select User Type");
        }


        hod = (Button) findViewById(R.id.hod);
        std = (Button) findViewById(R.id.std);
        tech = (Button) findViewById(R.id.teach);
        if (Route.BASE_URL == null) {

            dialog = new Dialog(this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialog_port_ip);
            dialog.setCancelable(false);
            dialog.show();
            di_but_submit = (Button) dialog.findViewById(R.id.l_but_click);
            di_edt_ip = (EditText) dialog.findViewById(R.id.l_edt_ip);

            di_but_submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Route.BASE_URL = di_edt_ip.getText().toString();
//                    WebClient.BASE_URL = di_edt_ip.getText().toString();
                    dialog.dismiss();
                }
            });

            hod.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Route.usertype = "hod";
                    Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                }
            });
            std.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Route.usertype = "std";
                    Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                }
            });
            tech.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Route.usertype = "tech";
                    Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                }
            });


        }
    }
}
