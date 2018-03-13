package com.android.lab.androidattendancesystem.activity;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.lab.androidattendancesystem.R;
import com.android.lab.androidattendancesystem.StudentSignupActivity;
import com.android.lab.androidattendancesystem.TeacherSignupActivity;
import com.android.lab.androidattendancesystem.VolleySingleton;
import com.android.lab.androidattendancesystem.app.AppConfig;
import com.android.lab.androidattendancesystem.utils.SessionManager;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Shivichu on 02-03-2018.
 */

public class HODDashboardActivity extends AppCompatActivity {

    Activity activity;
    Context context;

    android.widget.Button mMarkAttendance, mViewTeacherList, mViewStudentList, mAddNewTeacher, mAddNewStudent;
    TextView tHODName;

    private static final String PREF_NAME = "SessionManager";

    private SessionManager sessionManager;
    private SharedPreferences sharedPreferences;

    private Spinner spinner;
    private Button mSubmit;
    String sCLASSID;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hod_homepage);

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
            actionBar.setTitle("HOD Dashboard");
        }


        progressDialog = new ProgressDialog(this);
        sessionManager = new SessionManager(this);

        tHODName = (TextView) findViewById(R.id.txt_hod_name);
        mMarkAttendance = (android.widget.Button) findViewById(R.id.btn_hod_mark_attendance);
        mViewTeacherList = (Button) findViewById(R.id.btn_hod_view_teacher_list);
        mViewStudentList = (Button) findViewById(R.id.btn_hod_view_student_list);
        mAddNewTeacher = (Button) findViewById(R.id.btn_hod_add_new_teacher);
        mAddNewStudent = (Button) findViewById(R.id.btn_hod_add_new_student);


        tHODName.setText("Welcome " + AppConfig.HOD_NAME);

        mMarkAttendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (AppConfig.HOD_ID != 0) {
                    markTodaysAttendance(AppConfig.HOD_ID);
                }
            }
        });


        mViewTeacherList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent viewTeacher = new Intent(getApplicationContext(), ViewTeacherList.class);
                viewTeacher.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(viewTeacher);
            }
        });


        mViewStudentList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                LayoutInflater inflater = LayoutInflater.from(HODDashboardActivity.this);
                final View alertLayout = inflater.inflate(R.layout.select_student_class, null);

                final Dialog dialog = new Dialog(HODDashboardActivity.this);
                dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(alertLayout);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                dialog.setCancelable(false);
                dialog.show();

                spinner = (Spinner) dialog.findViewById(R.id.spinner_select_class);
                mSubmit = (Button) dialog.findViewById(R.id.btn_submit_class);

                List<String> categories = new ArrayList<String>();
                categories.add("--Select From List--");
                categories.add("1");
                categories.add("2");
                categories.add("3");
                categories.add("4");
                categories.add("5");
                categories.add("6");

                final ArrayAdapter<String> dataadapter = new ArrayAdapter<String>(HODDashboardActivity.this, R.layout.spin_layout, R.id.textView, categories);

                spinner.setAdapter(dataadapter);

                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                        String value = (String) spinner.getItemAtPosition(i);

                        sCLASSID = value;

                        Toast.makeText(getApplicationContext(), "You selected" + value, Toast.LENGTH_LONG).show();

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });

                mSubmit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                        if (sCLASSID.equalsIgnoreCase("--Select From List--")) {
                            Toast.makeText(getApplicationContext(), "Please Select Class...", Toast.LENGTH_SHORT).show();
                        } else {

                            dialog.dismiss();

                            Intent viewStudent = new Intent(getApplicationContext(), ViewStudentList.class);
                            viewStudent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            viewStudent.putExtra("class_id", sCLASSID);
                            startActivity(viewStudent);
                        }
                    }
                });

            }
        });


        mAddNewStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent newStudent = new Intent(getApplicationContext(), StudentSignupActivity.class);
                newStudent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(newStudent);
            }
        });

        mAddNewTeacher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent newStudent = new Intent(getApplicationContext(), TeacherSignupActivity.class);
                newStudent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(newStudent);

            }
        });

    }

    private void markTodaysAttendance(final int hodId) {

        progressDialog.setMessage("Marking Your Attendance");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConfig.HOD_MARK_ATTENDANCE_URL,


                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("HOD Attendance",response);

                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }

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
                params.put("hod_id", String.valueOf(hodId));
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

            AppConfig.HOD_ID = 0;

            sessionManager.setLogin(false);
            sessionManager.logoutUser();

            Intent logout = new Intent(getApplicationContext(), SelectOption.class);
            logout.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(logout);
            finish();

        }

        return super.onOptionsItemSelected(item);
    }

}
