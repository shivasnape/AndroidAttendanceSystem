package com.android.lab.androidattendancesystem.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.lab.androidattendancesystem.R;
import com.android.lab.androidattendancesystem.model.StudentDataList;
import com.android.lab.androidattendancesystem.model.TeacherDataList;

import java.util.List;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by wesix on 3/3/18.
 */

public class StudentListRecyclerAdapter extends RecyclerView.Adapter<StudentListRecyclerAdapter.MyViewHolder> {

    private Context mContext;
    private List<StudentDataList> itemList;

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

        return new StudentListRecyclerAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final StudentListRecyclerAdapter.MyViewHolder holder, int position) {

        StudentDataList data = itemList.get(position);


        holder.tStudentName.setText(data.getsStudentName());

        holder.tStudentMobile.setText(data.getsRegNo());


        Random rnd = new Random();
        int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
        holder.circleImageView.setColorFilter(color);

        holder.tStudentShortCode.setText(String.valueOf(upperCaseFirst(data.getsStudentName())));


        holder.imageEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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

                    }
                });


                dialog.setPositiveButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        dialogInterface.dismiss();

                        Toast.makeText(mContext, "Not Deleted", Toast.LENGTH_SHORT).show();

                    }
                });

                dialog.show();


            }
        });
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


