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
import com.android.lab.androidattendancesystem.model.TeacherDataList;

import java.util.List;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by wesix on 2/3/18.
 */

public class TeacherListRecyclerAdapter extends RecyclerView.Adapter<TeacherListRecyclerAdapter.MyViewHolder> {

    private Context mContext;
    private List<TeacherDataList> itemList;

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

        return new TeacherListRecyclerAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final TeacherListRecyclerAdapter.MyViewHolder holder, int position) {

        TeacherDataList data = itemList.get(position);


        holder.tTeacherName.setText(data.getsTeacherName());


        Random rnd = new Random();
        int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
        holder.circleImageView.setColorFilter(color);


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
                dialog.setMessage("Are You Sure to Delete this Teacher ?");
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


