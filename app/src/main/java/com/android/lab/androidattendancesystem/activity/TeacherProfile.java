package com.android.lab.androidattendancesystem.activity;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.android.lab.androidattendancesystem.R;
import com.android.lab.androidattendancesystem.app.AppConfig;

/**
 * Created by Shivichu on 10-03-2018.
 */

public class TeacherProfile extends AppCompatActivity {

    Activity activity;
    Context context;

    TextView tName, tEmail, tPh, tQualification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_teacher_profile);

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
            actionBar.setTitle("Student Profile");
        }

        tName = (TextView) findViewById(R.id.txt_teacher_profile_name);
        tEmail = (TextView) findViewById(R.id.txt_teacher_profile_email);
        tPh = (TextView) findViewById(R.id.txt_teacher_profile_ph);
        tQualification = (TextView) findViewById(R.id.txt_teacher_profile_course);

        setProfileDetails();

    }

    private void setProfileDetails() {


        tName.setText(AppConfig.TEACHER_NAME);
        tEmail.setText(AppConfig.TEACHER_EMAIL);
        tPh.setText(AppConfig.TEACHER_MOBILE);
        tQualification.setText(AppConfig.TEACHER_QUALIFICATION);

    }

}
