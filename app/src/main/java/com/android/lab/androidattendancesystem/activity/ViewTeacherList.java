package com.android.lab.androidattendancesystem.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.android.lab.androidattendancesystem.LoginActivity;
import com.android.lab.androidattendancesystem.MainActivity;
import com.android.lab.androidattendancesystem.R;
import com.android.lab.androidattendancesystem.VolleySingleton;
import com.android.lab.androidattendancesystem.adapter.TeacherListRecyclerAdapter;
import com.android.lab.androidattendancesystem.app.AppConfig;
import com.android.lab.androidattendancesystem.model.TeacherDataList;
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
 * Created by wesix on 2/3/18.
 */

public class ViewTeacherList extends AppCompatActivity {


    RecyclerView teacherRecyclerView;
    List<TeacherDataList> teacherDataList = new ArrayList<>();
    TeacherListRecyclerAdapter teacherListRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_teacher);


        teacherRecyclerView = (RecyclerView) findViewById(R.id.teacher_list_recycler_view);

        teacherListRecyclerAdapter = new TeacherListRecyclerAdapter(this, teacherDataList);

        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        teacherRecyclerView.setLayoutManager(linearLayoutManager1);
        teacherRecyclerView.setAdapter(teacherListRecyclerAdapter);


        initView();


    }

    private void initView() {


        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConfig.VIEW_TEACHER_LIST_URL,
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


                                JSONArray teacherArray = jsonObject.getJSONArray("data");

                                if (teacherArray.length() != 0) {

                                    for (int i = 0; i < teacherArray.length(); i++) {

                                        JSONObject object = teacherArray.getJSONObject(i);

                                        TeacherDataList data = new TeacherDataList();

                                        data.setiTeacherId(object.getInt("teacher_id"));
                                        data.setsTeacherName(object.getString("teacher_name"));
                                        data.setsTeacherMobile(object.getString("mobile"));
                                        data.setsTeacherEmail(object.getString("email"));

                                        teacherDataList.add(data);

                                    }

                                    teacherListRecyclerAdapter.notifyDataSetChanged();


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
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("function_name", "get_all_techers");
                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }


}

