package com.android.lab.androidattendancesystem.app;

/**
 * Created by wesix on 2/3/18.
 */

public class AppConfig {

    public static String VIEW_TEACHER_LIST_URL = "http://teamexplora.com/app/get_functions.php";

    public static String TEACHER_SIGNUP_URL = "http://teamexplora.com/app/register_teachers.php";
    public static String TEACHER_LOGIN_URL = "http://teamexplora.com/app/teachers_login.php";
    public static String TEACHER_MARK_ATTENDANCE_URL = "";

    public static String STUDENT_SIGNUP_URL = "http://teamexplora.com/app/student_register.php";
    public static String STUDENT_LOGIN_URL = "http://teamexplora.com/app/student_login.php";

    public static String HOD_MARK_ATTENDANCE_URL = "";
    public static String HOD_LOGIN_URL = "http://teamexplora.com/app/head_login.php";
    public static String HOD_STUDENT_LIST_URL = "http://teamexplora.com/app/get_students.php";


    //TEACHER DETAILS;
    public static int TEACHER_ID = 0;
    public static String TEACHER_NAME;
    public static String TEACHER_CLASS;

    //HOD DETAILS;
    public static int HOD_ID = 0;
    public static String HOD_NAME;
    public static String HOD_EMAIL;
    public static String HOD_MOBILE;
    public static String HOD_DEPARTMENT;


}
