package com.android.lab.androidattendancesystem;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.android.lab.androidattendancesystem.activity.ViewTeacherList;

public class MainActivity extends AppCompatActivity {


    Button mViewTeachers,mViewStudents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mViewTeachers = (Button)findViewById(R.id.btn_hod_teacher_list);
        mViewStudents = (Button)findViewById(R.id.btn_hod_student_list);



        mViewTeachers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent viewTeacher = new Intent(getApplicationContext(), ViewTeacherList.class);
                viewTeacher.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(viewTeacher);


            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.toolbar_project_logout) {
//            sessionManager.setLogin(false);
//            sessionManager.logoutUser();
//            databaseManager.dropUserTable();

            Intent logout = new Intent(getApplicationContext(), Home.class);
            logout.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(logout);
            finish();

        }

        return super.onOptionsItemSelected(item);
    }
}
