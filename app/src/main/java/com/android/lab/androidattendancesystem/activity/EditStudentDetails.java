package com.android.lab.androidattendancesystem.activity;

import android.app.Activity;
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
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.lab.androidattendancesystem.R;
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
 * Created by Shivichu on 07-03-2018.
 */

public class EditStudentDetails extends AppCompatActivity {

    private Activity activity;
    private Context context;

    EditText eStudentName, eStudentRgNo, eCourse, eSemester, eEmail, ePName, ePEmail, eMobile, eAadhar;
    int studentId;
    String sName, sRgNo, sCourse, sSemester, sEmail, sPName, sPEmail, sMobile, sAadhar;
    Button mSubmit;

    ProgressDialog progressDialog;

    String sClassId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_student);

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
            actionBar.setTitle("Edit Student Details");
        }

        progressDialog = new ProgressDialog(this);

        sClassId = getIntent().getExtras().getString("class_id");

        studentId = getIntent().getExtras().getInt("student_id");
        sName = getIntent().getExtras().getString("name");
        sRgNo = getIntent().getExtras().getString("rg_no");
        sCourse = getIntent().getExtras().getString("course");
        sSemester = getIntent().getExtras().getString("semester");
        sEmail = getIntent().getExtras().getString("email");
        sMobile = getIntent().getExtras().getString("mobile");
        sPName = getIntent().getExtras().getString("pname");
        sPEmail = getIntent().getExtras().getString("pemail");
        sAadhar = getIntent().getExtras().getString("aadhar");


        eStudentName = (EditText) findViewById(R.id.edt_edit_student_name);
        eStudentRgNo = (EditText) findViewById(R.id.edt_edit_student_rg_no);
        eCourse = (EditText) findViewById(R.id.edt_edit_student_course);
        eSemester = (EditText) findViewById(R.id.edt_edit_student_semester);
        eEmail = (EditText) findViewById(R.id.edt_edit_student_email);
        eMobile = (EditText) findViewById(R.id.edt_edit_student_mobile);
        ePName = (EditText) findViewById(R.id.edt_edit_student_pname);
        ePEmail = (EditText) findViewById(R.id.edt_edit_student_pemail);
        mSubmit = (Button) findViewById(R.id.btn_edit_student_submit);
        eAadhar = (EditText) findViewById(R.id.edt_edit_student_aadhar);

        eStudentName.setText(sName);
        eStudentRgNo.setText(sRgNo);
        eCourse.setText(sCourse);
        eSemester.setText(sSemester);
        eEmail.setText(sEmail);
        eMobile.setText(sMobile);
        ePName.setText(sPName);
        ePEmail.setText(sPEmail);
        eAadhar.setText(sAadhar);


        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String name = eStudentName.getText().toString().trim();
                String rgNo = eStudentRgNo.getText().toString().trim();
                String course = eCourse.getText().toString().trim();
                String semester = eSemester.getText().toString().trim();
                String email = eEmail.getText().toString().trim();
                String mobile = eMobile.getText().toString().trim();
                String pname = ePName.getText().toString().trim();
                String pemail = ePEmail.getText().toString().trim();
                String aadhar = eAadhar.getText().toString().trim();

                updateStudent(AppConfig.STUDENT_ID, name, rgNo, course, semester, email, mobile, pname, pemail, aadhar);


            }
        });

    }

    private void updateStudent(final int studentId, final String name, final String rgNo, final String course, final String semester, final String email, final String mobile, final String pname, final String pemail, final String aadharNo) {

        progressDialog.setMessage("Updating.....");
        progressDialog.show();

        StringRequest request = new StringRequest(Request.Method.POST, AppConfig.STUDENT_DETAILS_EDIT_URL,

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }

                        Log.d("Update Student ", response);

                        JSONObject jsonObject = null;

                        try {
                            jsonObject = new JSONObject(response);

                            String errFlag = jsonObject.getString("err_flag").trim();
                            String dispMsg = jsonObject.getString("disp_msg").trim();

                            if (errFlag.equalsIgnoreCase("1")) {

                                Toast.makeText(getApplicationContext(), dispMsg, Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(getApplicationContext(), ViewStudentList.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                intent.putExtra("class_id", sClassId);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(getApplicationContext(), dispMsg, Toast.LENGTH_SHORT).show();
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
//                params.put("class_id", sClassId);
                params.put("student_id", String.valueOf(studentId));
                params.put("name", name);
                params.put("regno", rgNo);
                params.put("course", course);
                params.put("semester", semester);
                params.put("email", email);
                params.put("parent_name", pname);
                params.put("parent_email", pemail);
                params.put("class_id", "1");
                params.put("mobile", mobile);
                params.put("aadhar", aadharNo);
                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(request);
    }
}
