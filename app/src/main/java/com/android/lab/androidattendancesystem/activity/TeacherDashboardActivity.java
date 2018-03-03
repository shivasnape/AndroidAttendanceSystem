package com.android.lab.androidattendancesystem.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.lab.androidattendancesystem.Home;
import com.android.lab.androidattendancesystem.R;
import com.android.lab.androidattendancesystem.StudentSignupActivity;
import com.android.lab.androidattendancesystem.VolleySingleton;
import com.android.lab.androidattendancesystem.app.AppConfig;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by wesix on 2/3/18.
 */

public class TeacherDashboardActivity extends AppCompatActivity {


    Activity activity;
    Context context;

    Button mViewStudentList, mAddNewStudent, mMarkAttendance;
    TextView tTeacherName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_home_page);

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
            actionBar.setTitle("Teacher Dashboard");
        }

        tTeacherName = (TextView)findViewById(R.id.txt_teacher_name);
        mMarkAttendance = (Button) findViewById(R.id.btn_teacher_mark_attendance);
        mViewStudentList = (Button) findViewById(R.id.btn_teacher_view_student_list);
        mAddNewStudent = (Button) findViewById(R.id.btn_teacher_add_new_student);

        tTeacherName.setText(AppConfig.TEACHER_NAME);

        mMarkAttendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (AppConfig.TEACHER_ID != 0) {
                    markTodaysAttendance(AppConfig.TEACHER_ID);
                }


            }
        });

        mViewStudentList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



            }
        });


        mAddNewStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent newStudent = new Intent(getApplicationContext(), StudentSignupActivity.class);
                newStudent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(newStudent);

            }
        });


    }

    private void markTodaysAttendance(final int teacherId) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConfig.TEACHER_MARK_ATTENDANCE_URL,

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        JSONObject jsonObject = null;

                        try {
                            jsonObject = new JSONObject(response);

                            String errFlag = jsonObject.getString("err_flag");
                            String dispMsg = jsonObject.getString("disp_msg");

                            if (errFlag.equalsIgnoreCase("1")) {
                                Toast toast = Toast.makeText(getApplicationContext(), "Your Attendance Has been Marked", Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.CENTER, 0, 0);
                                toast.show();
                            } else {
                                Toast toast = Toast.makeText(getApplicationContext(), "Failed to Mark Your Attendance....plz Try Later...", Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.CENTER, 0, 0);
                                toast.show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },


                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        if (error.toString() != null) {
                            Toast.makeText(getApplicationContext(), "Error : " + error.toString(), Toast.LENGTH_SHORT).show();
                        }

                    }
                }


        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("teacher_id", String.valueOf(teacherId));
                params.put("function_name", "");
                Log.d("PARAMLIST", params.toString());
                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);

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
//            databaseManager.dropTeacherTable();

            AppConfig.TEACHER_ID = 0;

            Intent logout = new Intent(getApplicationContext(), Home.class);
            logout.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(logout);
            finish();

        }

        return super.onOptionsItemSelected(item);
    }
}

