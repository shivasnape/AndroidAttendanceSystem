package com.android.lab.androidattendancesystem;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

public class Home extends AppCompatActivity {

    Button hod, std, tech, di_but_submit;
    Dialog dialog;
    EditText di_edt_ip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


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
