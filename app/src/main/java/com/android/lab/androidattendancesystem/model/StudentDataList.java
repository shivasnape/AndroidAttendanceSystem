package com.android.lab.androidattendancesystem.model;

/**
 * Created by Shivichu on 02-03-2018.
 */

public class StudentDataList {

    int iStudentId;

    public StudentDataList(){

    }

    public StudentDataList(int id){

        this.iStudentId = id;
    }

    public int getiStudentId() {
        return iStudentId;
    }

    public void setiStudentId(int iStudentId) {
        this.iStudentId = iStudentId;
    }
}
