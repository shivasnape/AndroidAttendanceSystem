package com.android.lab.androidattendancesystem.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.android.lab.androidattendancesystem.Home;
import com.android.lab.androidattendancesystem.R;

/**
 * Created by Shivichu on 03-03-2018.
 */

public class SelectOption extends AppCompatActivity {

    private Activity activity;
    private Context context;

    private Button mMarkAttendance, mLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //making full screen
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_select_servie_option);

        activity = this;
        context = getApplicationContext();

        mMarkAttendance = (Button) findViewById(R.id.btn_mark_attendance);
        mLogin = (Button) findViewById(R.id.btn_login_option);


        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent login = new Intent(getApplicationContext(), Home.class);
                login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(login);
                finish();
            }
        });
    }
}
