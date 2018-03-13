package com.android.lab.androidattendancesystem.model;

/**
 * Created by Shivichu on 02-03-2018.
 */

public class StudentDataList {

    int iStudentId;
    String sRegNo, sStudentName, sCourse, sSemester, sEmail, sMobile, sParentName, sParenEmail,sAadharNo,sTotalAttendance;

    public StudentDataList() {

    }

    public StudentDataList(int id, String regNo, String name, String course, String semester, String email, String mobile, String parent_name, String parent_email,
                           String aadhar, String attendance) {

        this.iStudentId = id;
        this.sRegNo = regNo;
        this.sStudentName = name;
        this.sCourse = course;
        this.sSemester = semester;
        this.sEmail = email;
        this.sMobile = mobile;
        this.sParentName = parent_name;
        this.sParenEmail = parent_email;
        this.sAadharNo = aadhar;
        this.sTotalAttendance = attendance;
    }

    public int getiStudentId() {
        return iStudentId;
    }

    public void setiStudentId(int iStudentId) {
        this.iStudentId = iStudentId;
    }

    public String getsRegNo() {
        return sRegNo;
    }

    public void setsRegNo(String sRegNo) {
        this.sRegNo = sRegNo;
    }

    public String getsStudentName() {
        return sStudentName;
    }

    public void setsStudentName(String sStudentName) {
        this.sStudentName = sStudentName;
    }

    public String getsCourse() {
        return sCourse;
    }

    public void setsCourse(String sCourse) {
        this.sCourse = sCourse;
    }

    public String getsSemester() {
        return sSemester;
    }

    public void setsSemester(String sSemester) {
        this.sSemester = sSemester;
    }

    public String getsEmail() {
        return sEmail;
    }

    public void setsEmail(String sEmail) {
        this.sEmail = sEmail;
    }

    public String getsMobile() {
        return sMobile;
    }

    public void setsMobile(String sMobile) {
        this.sMobile = sMobile;
    }

    public String getsParentName() {
        return sParentName;
    }

    public void setsParentName(String sParentName) {
        this.sParentName = sParentName;
    }

    public String getsParenEmail() {
        return sParenEmail;
    }

    public void setsParenEmail(String sParenEmail) {
        this.sParenEmail = sParenEmail;
    }

    public String getsAadharNo() {
        return sAadharNo;
    }

    public void setsAadharNo(String sAadharNo) {
        this.sAadharNo = sAadharNo;
    }

    public String getsTotalAttendance() {
        return sTotalAttendance;
    }

    public void setsTotalAttendance(String sTotalAttendance) {
        this.sTotalAttendance = sTotalAttendance;
    }
}
