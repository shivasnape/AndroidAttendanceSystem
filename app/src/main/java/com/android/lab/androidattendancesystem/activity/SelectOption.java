package com.android.lab.androidattendancesystem.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.android.lab.androidattendancesystem.Home;
import com.android.lab.androidattendancesystem.R;
import com.android.lab.androidattendancesystem.app.AppConfig;
import com.android.lab.androidattendancesystem.utils.SessionManager;

/**
 * Created by Shivichu on 03-03-2018.
 */

public class SelectOption extends AppCompatActivity {

    private Activity activity;
    private Context context;

    private Button mMarkAttendance, mLogin;

    private static final String PREF_NAME = "SessionManager";

    private SessionManager sessionManager;
    private SharedPreferences sharedPreferences;

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


        sessionManager = new SessionManager(this);
        sharedPreferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);

        if (sessionManager.isLoggedIn()) {

            // Reading from SharedPreferences
            String value = sharedPreferences.getString("user_type", "");

            switch (value) {

                case "hod":
                    int id = sharedPreferences.getInt("id", 0);
                    String name = sharedPreferences.getString("user_name", "");
                    String dept = sharedPreferences.getString("class_id", "");
                    AppConfig.HOD_ID = id;
                    AppConfig.HOD_NAME = name;
                    AppConfig.HOD_DEPARTMENT = dept;
                    finish();
                    startActivity(new Intent(getApplicationContext(), HODDashboardActivity.class));
                    break;
                case "teacher":
                    int id2 = sharedPreferences.getInt("id", 0);
                    String name2 = sharedPreferences.getString("user_name", "");
                    String classId = sharedPreferences.getString("class_id", "");
                    String qualification = sharedPreferences.getString("qualification","");
                    AppConfig.TEACHER_ID = id2;
                    AppConfig.TEACHER_NAME = name2;
                    AppConfig.TEACHER_CLASS = classId;
                    AppConfig.TEACHER_QUALIFICATION = qualification;
                    finish();
                    startActivity(new Intent(getApplicationContext(), TeacherDashboardActivity.class));
                    break;
                case "student":
                    int id3 = sharedPreferences.getInt("id", 0);
                    String name3 = sharedPreferences.getString("user_name", "");
                    AppConfig.STUDENT_ID = id3;
                    AppConfig.STUDENT_NAME = name3;
                    finish();
                    startActivity(new Intent(getApplicationContext(), StudentHomePage.class));
                    break;

            }
        } else {
            Log.d("TAG", "No Logged Users");
        }


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

