package com.android.lab.androidattendancesystem.activity;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.android.lab.androidattendancesystem.R;
import com.android.lab.androidattendancesystem.VolleySingleton;
import com.android.lab.androidattendancesystem.adapter.StudentListRecyclerAdapter;
import com.android.lab.androidattendancesystem.app.AppConfig;
import com.android.lab.androidattendancesystem.model.StudentDataList;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Shivichu on 02-03-2018.
 */

public class ViewStudentList extends AppCompatActivity {

    Activity activity;
    Context context;

    RecyclerView studentRecyclerView;
    List<StudentDataList> studentDataList = new ArrayList<>();
    StudentListRecyclerAdapter studentListRecyclerAdapter;

    public static String sClassId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_student);

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
            actionBar.setTitle("Student's List");
        }

        sClassId = getIntent().getExtras().getString("class_id");


        studentRecyclerView = (RecyclerView) findViewById(R.id.student_list_recycler_view);

        studentListRecyclerAdapter = new StudentListRecyclerAdapter(this, studentDataList);

        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        studentRecyclerView.setLayoutManager(linearLayoutManager1);
        studentRecyclerView.setAdapter(studentListRecyclerAdapter);


        initView();

    }

    private void initView() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConfig.HOD_STUDENT_LIST_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("RESPONSE : ", response);

                        JSONObject jsonObject = null;

                        try {
                            jsonObject = new JSONObject(response);

                            String errFlag = jsonObject.getString("err_flag").trim();
                            String dispMsg = jsonObject.getString("disp_msg").trim();

                            if (errFlag.equalsIgnoreCase("1")) {


                                JSONArray studeentArray = jsonObject.getJSONArray("data");

                                if (studeentArray.length() != 0) {

                                    for (int i = 0; i < studeentArray.length(); i++) {


                                        JSONObject studentObject = studeentArray.getJSONObject(i);

                                        StudentDataList data = new StudentDataList();

                                        data.setiStudentId(studentObject.getInt("student_id"));
                                        data.setsStudentName("Shiva");
                                        data.setsRegNo(studentObject.getString("reg_no"));
                                        data.setsCourse(studentObject.getString("course"));
                                        data.setsSemester(studentObject.getString("semester"));
                                        data.setsEmail(studentObject.getString("email"));
                                        data.setsMobile(studentObject.getString("mobile"));
                                        data.setsParentName(studentObject.getString("parent_name"));
                                        data.setsParenEmail(studentObject.getString("parent_email"));
                                        data.setsAadharNo(studentObject.getString("aadhar_number"));
                                        data.setsTotalAttendance(studentObject.getString("total_attendance"));

                                        studentDataList.add(data);

                                    }

                                    studentListRecyclerAdapter.notifyDataSetChanged();

                                } else {
                                    Toast.makeText(getApplicationContext(), "Student List Empty.....", Toast.LENGTH_SHORT).show();
                                }

                            } else {
                                Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();

                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
//                params.put("class_id", sClassId);
                params.put("class_id", "2");
                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }
}
