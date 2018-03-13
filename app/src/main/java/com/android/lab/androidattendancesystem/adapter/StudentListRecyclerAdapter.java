package com.android.lab.androidattendancesystem.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.lab.androidattendancesystem.R;
import com.android.lab.androidattendancesystem.VolleySingleton;
import com.android.lab.androidattendancesystem.activity.EditStudentDetails;
import com.android.lab.androidattendancesystem.activity.ViewStudentList;
import com.android.lab.androidattendancesystem.app.AppConfig;
import com.android.lab.androidattendancesystem.model.StudentDataList;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by wesix on 3/3/18.
 */

public class StudentListRecyclerAdapter extends RecyclerView.Adapter<StudentListRecyclerAdapter.MyViewHolder> {

    private Context mContext;
    private List<StudentDataList> itemList;

    ProgressDialog progressDialog;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        CircleImageView circleImageView;
        TextView tStudentShortCode, tProjectName;
        TextView tStudentName, tStudentAttendance, tStudentMobile;
        ImageView imageEdit, imageDelete;

        public MyViewHolder(View view) {
            super(view);

            circleImageView = (CircleImageView) view.findViewById(R.id.cis);
            tStudentShortCode = (TextView) view.findViewById(R.id.recycler_student_short_code);

            tStudentName = (TextView) view.findViewById(R.id.recycler_student_name);
            tStudentAttendance = (TextView) view.findViewById(R.id.recycler_student_attendance);
            tStudentMobile = (TextView) view.findViewById(R.id.recycler_student_mobile);
            imageEdit = (ImageView) view.findViewById(R.id.recycler_student_img_edit);
            imageDelete = (ImageView) view.findViewById(R.id.recycler_student_img_delete);

        }
    }

    public StudentListRecyclerAdapter(Context mContext, List<StudentDataList> itemList) {
        this.mContext = mContext;
        this.itemList = itemList;
    }


    @Override
    public StudentListRecyclerAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_student_list_layout, parent, false);

        progressDialog = new ProgressDialog(mContext);
        return new StudentListRecyclerAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final StudentListRecyclerAdapter.MyViewHolder holder, final int position) {

        final StudentDataList data = itemList.get(position);


        holder.tStudentName.setText(data.getsStudentName());

        holder.tStudentMobile.setText(data.getsRegNo());


        Random rnd = new Random();
        int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
        holder.circleImageView.setColorFilter(color);

        holder.tStudentShortCode.setText(String.valueOf(upperCaseFirst(data.getsStudentName())));

        holder.tStudentAttendance.setText(data.getsTotalAttendance());

        holder.imageEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent intent = new Intent(mContext, EditStudentDetails.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("class_id", ViewStudentList.sClassId);
                intent.putExtra("student_id",data.getiStudentId());
                intent.putExtra("name",data.getsStudentName());
                intent.putExtra("rg_no",data.getsRegNo());
                intent.putExtra("course",data.getsCourse());
                intent.putExtra("semester",data.getsSemester());
                intent.putExtra("email",data.getsEmail());
                intent.putExtra("mobile",data.getsMobile());
                intent.putExtra("pname",data.getsParentName());
                intent.putExtra("pemail",data.getsParenEmail());
                intent.putExtra("aadhar",data.getsAadharNo());
                mContext.startActivity(intent);


            }
        });

        holder.imageDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
                dialog.setCancelable(false);
                dialog.setTitle("Warning");
                dialog.setMessage("Are You Sure to Delete this Student ?");
                dialog.setNegativeButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        dialogInterface.dismiss();

                        Toast.makeText(mContext, "Delete Success", Toast.LENGTH_SHORT).show();

                        deleteStudent(data.getiStudentId());

                        itemList.remove(position);
                        notifyDataSetChanged();

                    }
                });


                dialog.setPositiveButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        dialogInterface.dismiss();

//                        Toast.makeText(mContext, "Not Deleted", Toast.LENGTH_SHORT).show();

                    }
                });

                dialog.show();


            }
        });

        holder.circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
                dialog.setCancelable(false);
                dialog.setTitle("Warning");
                dialog.setMessage("Are You Sure to Mark Special Attendance for this Student ?");
                dialog.setNegativeButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        dialogInterface.dismiss();

                        markSpecialAttendance(data.getiStudentId());

                    }
                });


                dialog.setPositiveButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        dialogInterface.dismiss();

//                        Toast.makeText(mContext, "Not Deleted", Toast.LENGTH_SHORT).show();

                    }
                });

                dialog.show();


            }
        });
    }

    private void markSpecialAttendance(final int i) {

        progressDialog.setMessage("Marking Special Attendance......");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConfig.STUDENT_MARK_ATTENDANCE_URL,

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("Student Attendance", response);

                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }

                        JSONObject jsonObject = null;

                        try {
                            jsonObject = new JSONObject(response);

                            String errFlag = jsonObject.getString("err_flag");
                            String dispMsg = jsonObject.getString("disp_msg");

                            if (errFlag.equalsIgnoreCase("1")) {
                                Toast toast = Toast.makeText(mContext, "Your Attendance Has been Marked", Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.CENTER, 0, 0);
                                toast.show();
                            } else {
                                Toast toast = Toast.makeText(mContext, dispMsg, Toast.LENGTH_SHORT);
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
                            Toast.makeText(mContext, "Error : " + error.toString(), Toast.LENGTH_SHORT).show();
                        }

                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("student_id", String.valueOf(i));
                Log.d("PARAMLIST", params.toString());
                return params;
            }
        };

        VolleySingleton.getInstance(mContext).addToRequestQueue(stringRequest);
    }

    private void deleteStudent(final int i) {

        StringRequest request = new StringRequest(Request.Method.POST, AppConfig.STUDENT_DETAILS_DELETE_URL,

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("***RESPONSE : ", response);

                        JSONObject jsonObject = null;

                        try {
                            jsonObject = new JSONObject(response);

                            String errFlag = jsonObject.getString("err_flag");
                            String dispMsg = jsonObject.getString("disp_msg");

                            if (errFlag.equalsIgnoreCase("1")) {
                                Toast.makeText(mContext, dispMsg, Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(mContext, dispMsg, Toast.LENGTH_SHORT).show();

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
                            Toast.makeText(mContext, "Error :" + error.toString(), Toast.LENGTH_SHORT).show();
                        }

                    }
                }

        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("student_id", String.valueOf(i));
                Log.d("PARAMLIST", params.toString());
                return params;
            }
        };

        VolleySingleton.getInstance(mContext).addToRequestQueue(request);

    }

    private String upperCaseFirst(String value) {

        // Convert String to char array.
        char[] array = value.toCharArray();
        // Modify first element in array.
        array[0] = Character.toUpperCase(array[0]);
        // Return string.
        String string = String.valueOf(array[0]);
        return string;
    }


    @Override
    public int getItemCount() {
        return itemList.size();
    }

}


