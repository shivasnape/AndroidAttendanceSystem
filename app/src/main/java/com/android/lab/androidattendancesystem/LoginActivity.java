package com.android.lab.androidattendancesystem;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.lab.androidattendancesystem.activity.HODDashboardActivity;
import com.android.lab.androidattendancesystem.activity.StudentHomePage;
import com.android.lab.androidattendancesystem.activity.TeacherDashboardActivity;
import com.android.lab.androidattendancesystem.app.AppConfig;
import com.android.lab.androidattendancesystem.utils.SessionManager;
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

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements LoaderCallbacks<Cursor> {

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;

    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */
    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "foo@example.com:hello", "bar@example.com:world"
    };
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private TextView signup;

    private View mLoginFormView;

    Activity activity;
    Context context;

    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        activity = this;
        context = getApplicationContext();

        Window window = activity.getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(ContextCompat.getColor(activity, R.color.window_bar));
        }

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            //setting action bar with custom color defined in colors
            actionBar.setDisplayHomeAsUpEnabled(false);
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(context, R.color.action_bar)));
            actionBar.setTitle("Enter Login Credentials");
        }

        sessionManager = new SessionManager(this);


        // Set up the login form.
        mEmailView = findViewById(R.id.email);
        signup = findViewById(R.id.signup);

        populateAutoComplete();

        if (Route.usertype.trim().equals("hod")) {
            signup.setVisibility(View.GONE);
        }

        signup.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (Route.usertype) {

                    case "std":
                        Intent i = new Intent(getApplicationContext(), StudentSignupActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(i);
                        break;
                    /*case "tech":
                        Intent in = new Intent(getApplicationContext(), TeacherSignupActivity.class);
                        in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(in);
                        break;*/

                    default:
                        Toast.makeText(getApplicationContext(), "Please Contact HOD", Toast.LENGTH_SHORT).show();
                }
            }
        });

        mPasswordView = findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mEmailSignInButton = findViewById(R.id.email_sign_in_button);

        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }

    private void populateAutoComplete() {
        if (!mayRequestContacts()) {
            return;
        }

        getLoaderManager().initLoader(0, null, this);
    }

    private boolean mayRequestContacts() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
            Snackbar.make(mEmailView, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new OnClickListener() {
                        @Override
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v) {
                            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
                        }
                    });
        } else {
            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
        }
        return false;
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                populateAutoComplete();
            }
        }
    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {


        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }
        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }
        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            switch (Route.usertype) {
                case "hod":
                    hodLogin(email, password);
//                    finish();
//                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    break;
                case "std":
                    studentLogin(email, password);
//                    finish();
//                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    break;
                case "tech":
                    teacherLogin(email, password);
                    break;
            }
        }
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

        addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(LoginActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        mEmailView.setAdapter(adapter);
    }


    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }


    public void teacherLogin(final String email, final String password) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConfig.TEACHER_LOGIN_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        showProgress(false);

                        Log.i("888880", response);

                        JSONObject jsonObject = null;

                        try {

                            jsonObject = new JSONObject(response);

                            String errFlag = jsonObject.getString("err_flag");
                            String dispMsg = jsonObject.getString("disp_msg");

                            if (errFlag.equalsIgnoreCase("1")) {

                                JSONArray jsonArray = jsonObject.getJSONArray("data");


                                if (jsonArray.length() != 0) {

                                    for (int i = 0; i < jsonArray.length(); i++) {

                                        JSONObject object = jsonArray.getJSONObject(i);

                                        String teacherId = object.getString("teacher_id");
                                        String teacherName = object.getString("teacher_name");
                                        String teacherEmail = object.getString("email");
                                        String teacherMobile = object.getString("mobile");
                                        String teacherClass = object.getString("class");
                                        String aadharNo = object.getString("aadhar_number");
                                        String qualification  = object.getString("qualification");

                                        AppConfig.TEACHER_ID = Integer.parseInt(teacherId);
                                        AppConfig.TEACHER_NAME = teacherName;
                                        AppConfig.TEACHER_CLASS = teacherClass;
                                        AppConfig.TEACHER_EMAIL = teacherEmail;
                                        AppConfig.TEACHER_MOBILE = teacherMobile;

                                        sessionManager.setLogin(true);
                                        sessionManager.setLoginData(Integer.parseInt(teacherId), teacherName, "teacher", teacherClass,teacherEmail,teacherMobile,qualification);


                                        finish();
                                        startActivity(new Intent(getApplicationContext(), TeacherDashboardActivity.class));

                                    }
                                } else {
                                    Toast.makeText(getApplicationContext(), "Empty Data.....", Toast.LENGTH_SHORT).show();

                                }


                            } else {
                                Toast.makeText(getApplicationContext(), "Error......", Toast.LENGTH_SHORT).show();

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
                params.put("email", email);
                params.put("password", password);
                Log.d("PARAMLIST", params.toString());
                return params;
            }
        };

        VolleySingleton.getInstance(LoginActivity.this).addToRequestQueue(stringRequest);
    }

    public void hodLogin(final String email, final String password) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConfig.HOD_LOGIN_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("***HID DRESPONSE : ", response);

                        showProgress(false);
                        try {

                            JSONObject jsonObject = null;

                            jsonObject = new JSONObject(response);

                            String errFlag = jsonObject.getString("err_flag");
                            String dispMsg = jsonObject.getString("disp_msg");

                            if (errFlag.equalsIgnoreCase("1")) {

                                JSONArray jsonArray = jsonObject.getJSONArray("data");


                                if (jsonArray.length() != 0) {

                                    for (int i = 0; i < jsonArray.length(); i++) {

                                        JSONObject object = jsonArray.getJSONObject(i);

                                        String hodId = object.getString("id");
                                        String name = object.getString("name");
                                        String email = object.getString("email");
                                        String mobile = object.getString("mobile");
                                        String dept = object.getString("department");

                                        AppConfig.HOD_ID = Integer.parseInt(hodId);
                                        AppConfig.HOD_NAME = name;
                                        AppConfig.HOD_EMAIL = email;
                                        AppConfig.HOD_MOBILE = mobile;
                                        AppConfig.HOD_DEPARTMENT = dept;

                                        sessionManager.setLogin(true);
                                        sessionManager.setLoginData(Integer.parseInt(hodId), name, "hod", dept,email,mobile,"");

                                        finish();
                                        startActivity(new Intent(getApplicationContext(), HODDashboardActivity.class));

                                    }
                                } else {
                                    Toast.makeText(getApplicationContext(), "Empty Data.....", Toast.LENGTH_SHORT).show();

                                }


                            } else {
                                Toast.makeText(getApplicationContext(), "Error......", Toast.LENGTH_SHORT).show();

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
                params.put("email", email);
                params.put("password", password);
                return params;
            }
        };

        VolleySingleton.getInstance(LoginActivity.this).addToRequestQueue(stringRequest);
    }

    public void studentLogin(final String email, final String password) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConfig.STUDENT_LOGIN_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("***RESPOSNSE : ", response);

                        showProgress(false);

                        JSONObject jsonObject = null;

                        try {
                            jsonObject = new JSONObject(response);

                            String errFlag = jsonObject.getString("err_flag");
                            String dispMsg = jsonObject.getString("disp_msg");

                            if (errFlag.equalsIgnoreCase("1")) {


                                JSONArray studentArray = jsonObject.getJSONArray("data");

                                if (studentArray.length() != 0) {

                                    for (int i = 0; i < studentArray.length(); i++) {

                                        JSONObject object = studentArray.getJSONObject(i);

                                        String studentId = object.getString("student_id");
                                        String name = object.getString("name");
                                        String email = object.getString("email");
                                        String mobile = object.getString("mobile");
                                        String course = object.getString("course");
                                        String aadharNo = object.getString("aadhar_number");

                                        AppConfig.STUDENT_ID = Integer.parseInt(studentId);
                                        AppConfig.STUDENT_NAME = name;
                                        AppConfig.STUDENT_EMAIL = email;
                                        AppConfig.STUDENT_MOBILE = mobile;
                                        AppConfig.STUDENT_COURSE = course;

                                        sessionManager.setLogin(true);
                                        sessionManager.setLoginData(Integer.parseInt(studentId), name, "student", course,email,mobile,"");

                                        finish();
                                        startActivity(new Intent(getApplicationContext(), StudentHomePage.class));

                                    }

                                } else {
                                    Toast.makeText(getApplicationContext(), "Student Details Empty", Toast.LENGTH_SHORT).show();
                                }


                            } else {
                                Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
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
                params.put("email", email);
                params.put("password", password);
                Log.d("Param List", String.valueOf(params));
                return params;
            }
        };

        VolleySingleton.getInstance(LoginActivity.this).addToRequestQueue(stringRequest);
    }

}

