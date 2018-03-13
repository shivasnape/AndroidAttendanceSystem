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
 * Created by Shivichu on 06-03-2018.
 */

public class EditTeacherActivity extends AppCompatActivity {

    private Activity activity;
    private Context context;

    EditText eName, eMobile, eEmail, eClass, eQualification, eAadharNo;
    int teacherId;
    String sName, sMobile, sEmail, sClass, sQualification, sAadhar;
    Button mSubmit;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_teacher);


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
            actionBar.setTitle("Edit Teacher");
        }

        progressDialog = new ProgressDialog(this);

        teacherId = getIntent().getExtras().getInt("teacher_id");
        sName = getIntent().getExtras().getString("name");
        sMobile = getIntent().getExtras().getString("mobile");
        sEmail = getIntent().getExtras().getString("email");
        sClass = getIntent().getExtras().getString("class");
        sQualification = getIntent().getExtras().getString("qualification");
        sAadhar = getIntent().getExtras().getString("aadhar");


        eName = (EditText) findViewById(R.id.edit_name);
        eMobile = (EditText) findViewById(R.id.edit_mobile);
        eEmail = (EditText) findViewById(R.id.edit_email);
        eClass = (EditText) findViewById(R.id.edit_dclass);
        eQualification = (EditText) findViewById(R.id.edit_qualification);
        mSubmit = (Button) findViewById(R.id.btn_edit_submit);
        eAadharNo = (EditText) findViewById(R.id.edit_t_aadhar);

        eName.setText(sName);
        eMobile.setText(sMobile);
        eEmail.setText(sEmail);
        eClass.setText(sClass);
        eQualification.setText(sQualification);
        eAadharNo.setText(sAadhar);


        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (eName.getText().length() != 0) {
                    if (eMobile.getText().length() != 0) {
                        if (eMobile.getText().length() == 10) {
                            if (eEmail.getText().length() != 0) {
                                if (eClass.getText().length() != 0) {
                                    if (eQualification.getText().length() != 0) {

                                        if (eAadharNo.getText().length() != 0) {

                                            String name = eName.getText().toString().trim();
                                            String mobile = eMobile.getText().toString().trim();
                                            String email = eEmail.getText().toString().trim();
                                            String sClass = eClass.getText().toString().trim();
                                            String qualification = eQualification.getText().toString().trim();
                                            String aadhar = eAadharNo.getText().toString().trim();

                                            updateTeacherDetails(name, mobile, email, sClass, qualification,aadhar);
                                        } else {
                                            Toast.makeText(getApplicationContext(), "Aadhar Field Empty", Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        Toast.makeText(getApplicationContext(), "Qualification Field Empty", Toast.LENGTH_SHORT).show();

                                    }
                                } else {
                                    Toast.makeText(getApplicationContext(), "Class Field Empty", Toast.LENGTH_SHORT).show();

                                }
                            } else {
                                Toast.makeText(getApplicationContext(), "Email Field Empty", Toast.LENGTH_SHORT).show();

                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Invalid Mobile Number", Toast.LENGTH_SHORT).show();

                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Mobile Field Empty", Toast.LENGTH_SHORT).show();

                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Name Field Empty", Toast.LENGTH_SHORT).show();

                }


            }
        });


    }

    private void updateTeacherDetails(final String name, final String mobile, final String email, final String sClass, final String qualification, final String aadharNo) {


        progressDialog.setMessage("Updating......");
        progressDialog.show();

        StringRequest request = new StringRequest(Request.Method.POST, AppConfig.TEACHER_DETAILS_EDIT_URL,


                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }

                        Log.d("Response : ", response);

                        JSONObject jsonObject = null;

                        try {
                            jsonObject = new JSONObject(response);

                            String errFlag = jsonObject.getString("err_flag").trim();
                            String dispMsg = jsonObject.getString("disp_msg").trim();

                            if (errFlag.equalsIgnoreCase("1")) {

                                Toast.makeText(getApplicationContext(), dispMsg, Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(getApplicationContext(), ViewTeacherList.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
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
                params.put("teacher_id", String.valueOf(teacherId));
                params.put("name", name);
                params.put("email", email);
                params.put("mobile", mobile);
                params.put("qualification", qualification);
                params.put("aadhar",aadharNo);
                params.put("class", sClass);
                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(request);
    }
}
