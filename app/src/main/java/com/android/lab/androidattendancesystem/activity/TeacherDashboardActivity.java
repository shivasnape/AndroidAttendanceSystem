package com.android.lab.androidattendancesystem.activity;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.lab.androidattendancesystem.R;
import com.android.lab.androidattendancesystem.StudentSignupActivity;
import com.android.lab.androidattendancesystem.VolleySingleton;
import com.android.lab.androidattendancesystem.app.AppConfig;
import com.android.lab.androidattendancesystem.utils.SessionManager;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by wesix on 2/3/18.
 */

public class TeacherDashboardActivity extends AppCompatActivity {

    Activity activity;
    Context context;

    Button mViewStudentList, mAddNewStudent, mMarkAttendance, mViewProfile,mViewTotalAttendance;
    TextView tTeacherName;

    private ProgressDialog progressDialog;
    SessionManager sessionManager;

    TextView tAttendance;
    Button mDismiss;

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

        progressDialog = new ProgressDialog(this);
        sessionManager = new SessionManager(this);

        tTeacherName = (TextView) findViewById(R.id.txt_teacher_name);
        mMarkAttendance = (Button) findViewById(R.id.btn_teacher_mark_attendance);
        mViewStudentList = (Button) findViewById(R.id.btn_teacher_view_student_list);
        mAddNewStudent = (Button) findViewById(R.id.btn_teacher_add_new_student);
        mViewProfile = (Button) findViewById(R.id.btn_teacher_profile);
        mViewTotalAttendance = (Button)findViewById(R.id.btn_teacher_view_attendance);

        tTeacherName.setText(AppConfig.TEACHER_NAME);

        mMarkAttendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (AppConfig.TEACHER_ID != 0) {

                    Calendar calendar = Calendar.getInstance();
                    int currentHour = calendar.get(Calendar.HOUR);

                    if (currentHour <= 10 && currentHour >= 9) {
                        markTodaysAttendance(AppConfig.TEACHER_ID);
                    } else {
                        Toast.makeText(getApplicationContext(), "You Cannot Mark Your Attendance as the Time is Over....Please Contact HOD", Toast.LENGTH_LONG).show();
                    }
                }


            }
        });

        mViewStudentList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent viewStudent = new Intent(getApplicationContext(), ViewStudentList.class);
                viewStudent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                viewStudent.putExtra("class_id", AppConfig.TEACHER_CLASS);
                startActivity(viewStudent);

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

        mViewProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent intent = new Intent(getApplicationContext(), TeacherProfile.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

            }
        });

        mViewTotalAttendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                LayoutInflater inflater = LayoutInflater.from(TeacherDashboardActivity.this);
                final View alertLayout = inflater.inflate(R.layout.view_student_total_attendance, null);

                final Dialog dialog = new Dialog(TeacherDashboardActivity.this);
                dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(alertLayout);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                dialog.setCancelable(false);
                dialog.show();

                tAttendance = (TextView) dialog.findViewById(R.id.txt_stud_total_attendance);
                mDismiss = (Button) dialog.findViewById(R.id.btn_std_at_dismiss);

                getTeacherAttendance();

                mDismiss.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (dialog.isShowing()) {
                            dialog.dismiss();
                        }
                    }
                });

            }
        });


    }

    private void getTeacherAttendance() {

        StringRequest request = new StringRequest(Request.Method.POST, AppConfig.TEACHER_VIEW_ATTENDANCE_URL,

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("Teacher RESPONSE", response);

                        JSONObject jsonObject = null;

                        try {
                            jsonObject = new JSONObject(response);

                            String errFlag = jsonObject.getString("err_flag").trim();
                            String dispMsg = jsonObject.getString("disp_msg").trim();

                            if (errFlag.equalsIgnoreCase("1")) {

                                JSONArray array = jsonObject.getJSONArray("data");

                                if (array.length() != 0) {

                                    for (int i = 0; i < array.length(); i++) {
                                        JSONObject object = array.getJSONObject(i);

                                        tAttendance.setText(object.getString("total"));
                                    }

                                } else {
                                    Log.d("TAG", "Array Empty");
                                }


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
                            Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }

        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("teacher_id", String.valueOf(AppConfig.TEACHER_ID));
                params.put("month", "3");
                params.put("year", "2018");
                Log.d("PARAMLIST", params.toString());
                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(request);

    }

    private void markTodaysAttendance(final int teacherId) {

        progressDialog.setMessage("Marking Today's Attendance");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConfig.TEACHER_MARK_ATTENDANCE_URL,

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("Teacher Attendance", response);

                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }

                        JSONObject jsonObject = null;

                        try {
                            jsonObject = new JSONObject(response);

                            String errFlag = jsonObject.getString("err_flag");
                            String dispMsg = jsonObject.getString("disp_msg");

                            if (errFlag.equalsIgnoreCase("1")) {
                                Toast toast = Toast.makeText(getApplicationContext(), dispMsg, Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.CENTER, 0, 0);
                                toast.show();
                            } else if (errFlag.equalsIgnoreCase("0")) {
                                Toast toast = Toast.makeText(getApplicationContext(), dispMsg, Toast.LENGTH_SHORT);
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

                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }

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
            sessionManager.setLogin(false);
            sessionManager.logoutUser();

            AppConfig.TEACHER_ID = 0;

            Intent logout = new Intent(getApplicationContext(), SelectOption.class);
            logout.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(logout);
            finish();

        }

        return super.onOptionsItemSelected(item);
    }
}

