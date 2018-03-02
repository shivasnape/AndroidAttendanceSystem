package com.android.lab.androidattendancesystem;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

/**
 * Created by HP on 4/4/2017.
 */
public class Route {
    public static String BASE_URL = null;
    public static String SUB_URL = ":8080/student_proshare/";
    public static String uploadfilepath=null;
    public static String usertype="";


    public static void nextActivity(Activity activity, Context context, Class nextclass){
        Intent intent = new Intent(context,nextclass);
        context.startActivity(intent);
        activity.finish();
    }
}
