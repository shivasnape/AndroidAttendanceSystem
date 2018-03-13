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
import com.android.lab.androidattendancesystem.activity.EditTeacherActivity;
import com.android.lab.androidattendancesystem.app.AppConfig;
import com.android.lab.androidattendancesystem.model.TeacherDataList;
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
 * Created by wesix on 2/3/18.
 */

public class TeacherListRecyclerAdapter extends RecyclerView.Adapter<TeacherListRecyclerAdapter.MyViewHolder> {

    private Context mContext;
    private List<TeacherDataList> itemList;

    ProgressDialog progressDialog;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        CircleImageView circleImageView;
        TextView tTeacherShortCode, tProjectName;
        TextView tTeacherName, tTeacherAttendance, tTeacherClass;
        ImageView imageEdit, imageDelete;

        public MyViewHolder(View view) {
            super(view);

            circleImageView = (CircleImageView) view.findViewById(R.id.ci);
            tTeacherShortCode = (TextView) view.findViewById(R.id.recycler_teacher_short_code);

            tTeacherName = (TextView) view.findViewById(R.id.recycler_teacher_name);
            tTeacherAttendance = (TextView) view.findViewById(R.id.recycler_teacher_attendance);
            tTeacherClass = (TextView) view.findViewById(R.id.recycler_teacher_class);
            imageEdit = (ImageView) view.findViewById(R.id.recycler_teacher_img_edit);
            imageDelete = (ImageView) view.findViewById(R.id.recycler_teacher_img_delete);

        }
    }

    public TeacherListRecyclerAdapter(Context mContext, List<TeacherDataList> itemList) {
        this.mContext = mContext;
        this.itemList = itemList;
    }


    @Override
    public TeacherListRecyclerAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_project_layout, parent, false);

        progressDialog = new ProgressDialog(mContext);
        return new TeacherListRecyclerAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final TeacherListRecyclerAdapter.MyViewHolder holder, final int position) {

        final TeacherDataList data = itemList.get(position);


        holder.tTeacherName.setText(data.getsTeacherName());


        Random rnd = new Random();
        int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
        holder.circleImageView.setColorFilter(color);

        holder.tTeacherShortCode.setText(String.valueOf(upperCaseFirst(data.getsTeacherName())));

        holder.tTeacherAttendance.setText(data.getsTeacherTotalAttendance());

        holder.imageEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(mContext, EditTeacherActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("teacher_id", data.getiTeacherId());
                intent.putExtra("name", data.getsTeacherName());
                intent.putExtra("email", data.getsTeacherEmail());
                intent.putExtra("mobile", data.getsTeacherMobile());
                intent.putExtra("class", data.getsTeacherClass());
                intent.putExtra("qualification", data.getsQualification());
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
                dialog.setMessage("Are You Sure to Delete this Teacher ?");
                dialog.setNegativeButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        dialogInterface.dismiss();

                        deleteTeacher(data.getiTeacherId());

                        itemList.remove(position);
                        notifyDataSetChanged();
//                        Toast.makeText(mContext, "Delete Success", Toast.LENGTH_SHORT).show();

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
                dialog.setMessage("Are You Sure to Mark Special Attendance for this Teacher ?");
                dialog.setNegativeButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        dialogInterface.dismiss();

                        markSpecialAttendance(data.getiTeacherId());


                    }
                });


                dialog.setPositiveButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        dialogInterface.dismiss();

                    }
                });

                dialog.show();

            }
        });
    }

    private void markSpecialAttendance(final int i) {

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
                                Toast toast = Toast.makeText(mContext, dispMsg, Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.CENTER, 0, 0);
                                toast.show();
                            } else if (errFlag.equalsIgnoreCase("0")) {
                                Toast toast = Toast.makeText(mContext, dispMsg, Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.CENTER, 0, 0);
                                toast.show();
                            } else {
                                Toast toast = Toast.makeText(mContext, "Failed to Mark Your Attendance....plz Try Later...", Toast.LENGTH_SHORT);
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
                params.put("teacher_id", String.valueOf(i));
                Log.d("PARAMLIST", params.toString());
                return params;
            }
        };

        VolleySingleton.getInstance(mContext).addToRequestQueue(stringRequest);
    }

    private void deleteTeacher(final int i) {

        StringRequest request = new StringRequest(Request.Method.POST, AppConfig.TEACHER_DELETE_URL,

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("***TEACHER DELETE", response);

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
                            Toast.makeText(mContext, "Error : " + error.toString(), Toast.LENGTH_SHORT).show();
                        }

                    }
                }

        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("teacher_id", String.valueOf(i));
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


