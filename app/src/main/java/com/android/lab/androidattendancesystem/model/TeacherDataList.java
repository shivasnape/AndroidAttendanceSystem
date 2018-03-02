package com.android.lab.androidattendancesystem.model;

/**
 * Created by wesix on 2/3/18.
 */

public class TeacherDataList {

    int iTeacherId;
    String sTeacherName,sTeacherClass,sTeacherEmail,sTeacherMobile,sTeacherTotalAttendance;

    public TeacherDataList(){

    }

    public TeacherDataList(int teacherId, String name, String tClass, String email, String mobile, String totalAttendance){

        this.iTeacherId = teacherId;
        this.sTeacherName = name;
        this.sTeacherClass = tClass;
        this.sTeacherEmail = email;
        this.sTeacherMobile = mobile;
        this.sTeacherTotalAttendance = totalAttendance;
    }

    public int getiTeacherId() {
        return iTeacherId;
    }

    public void setiTeacherId(int iTeacherId) {
        this.iTeacherId = iTeacherId;
    }

    public String getsTeacherName() {
        return sTeacherName;
    }

    public void setsTeacherName(String sTeacherName) {
        this.sTeacherName = sTeacherName;
    }

    public String getsTeacherClass() {
        return sTeacherClass;
    }

    public void setsTeacherClass(String sTeacherClass) {
        this.sTeacherClass = sTeacherClass;
    }

    public String getsTeacherEmail() {
        return sTeacherEmail;
    }

    public void setsTeacherEmail(String sTeacherEmail) {
        this.sTeacherEmail = sTeacherEmail;
    }

    public String getsTeacherMobile() {
        return sTeacherMobile;
    }

    public void setsTeacherMobile(String sTeacherMobile) {
        this.sTeacherMobile = sTeacherMobile;
    }

    public String getsTeacherTotalAttendance() {
        return sTeacherTotalAttendance;
    }

    public void setsTeacherTotalAttendance(String sTeacherTotalAttendance) {
        this.sTeacherTotalAttendance = sTeacherTotalAttendance;
    }
}
