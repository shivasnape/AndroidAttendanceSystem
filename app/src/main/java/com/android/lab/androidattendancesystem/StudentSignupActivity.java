package com.android.lab.androidattendancesystem;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
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

import com.android.lab.androidattendancesystem.app.AppConfig;
import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.Manifest.permission.READ_CONTACTS;
import static com.android.volley.Request.Method;

/**
 * A login screen that offers login via email/password.
 */
public class StudentSignupActivity extends AppCompatActivity {

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
    private AutoCompleteTextView edemail, edName, edphone, edregno, edsem, edcourse, edpname, edpemail;
    private EditText mPasswordView, mCPasswordView;
    private View mProgressView;
    private View mLoginFormView;

    Activity activity;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

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
            actionBar.setTitle("Student Registration");
        }


        // Set up the login form.
        initialize();
        populateAutoComplete();

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

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }

    private void initialize() {
        edemail = (AutoCompleteTextView) findViewById(R.id.email);
        edName = (AutoCompleteTextView) findViewById(R.id.name);
        edphone = (AutoCompleteTextView) findViewById(R.id.mobile);
        edcourse = (AutoCompleteTextView) findViewById(R.id.course);
        edregno = (AutoCompleteTextView) findViewById(R.id.regno);
        edsem = (AutoCompleteTextView) findViewById(R.id.sem);
        edpname = (AutoCompleteTextView) findViewById(R.id.panme);
        edpemail = (AutoCompleteTextView) findViewById(R.id.pemail);
        edpname = (AutoCompleteTextView) findViewById(R.id.panme);
        mPasswordView = (EditText) findViewById(R.id.password);
        mCPasswordView = (EditText) findViewById(R.id.cpassword);


    }

    private void populateAutoComplete() {
        if (!mayRequestContacts()) {
            return;
        }

    }

    private boolean mayRequestContacts() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
            Snackbar.make(edemail, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
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
        edemail.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = edemail.getText().toString();
        String name = edName.getText().toString();
        String regno = edregno.getText().toString();
        String course = edcourse.getText().toString();
        String sem = edsem.getText().toString();
        String phone = edphone.getText().toString();
        String pname = edpname.getText().toString();
        String pemail = edpemail.getText().toString();
        String cpassword = mCPasswordView.getText().toString();
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
        if (TextUtils.isEmpty(name)) {
            edName.setError(getString(R.string.error_field_required));
            focusView = edName;
            cancel = true;
        }
        if (TextUtils.isEmpty(regno)) {
            edregno.setError(getString(R.string.error_field_required));
            focusView = edregno;
            cancel = true;
        }
        if (TextUtils.isEmpty(course)) {
            edcourse.setError(getString(R.string.error_field_required));
            focusView = edcourse;
            cancel = true;
        }
        if (!password.equals(cpassword)) {
            mPasswordView.setError("Password miss match");
            focusView = mPasswordView;
            cancel = true;
        }
        if (TextUtils.isEmpty(sem)) {
            edsem.setError(getString(R.string.error_field_required));
            focusView = edsem;
            cancel = true;
        }
        if (TextUtils.isEmpty(phone)) {
            edphone.setError(getString(R.string.error_field_required));
            focusView = edphone;
            cancel = true;
        }
        if (!isPhoneValid(phone)) {
            edphone.setError("This mobile number is invalid ");
            focusView = edphone;
            cancel = true;
        }
        if (TextUtils.isEmpty(email)) {
            edemail.setError(getString(R.string.error_field_required));
            focusView = edemail;
            cancel = true;
        } else if (!isEmailValid(email)) {
            edemail.setError(getString(R.string.error_invalid_email));
            focusView = edemail;
            cancel = true;
        }
        if (TextUtils.isEmpty(pemail)) {
            edpemail.setError(getString(R.string.error_field_required));
            focusView = edpemail;
            cancel = true;
        } else if (!isEmailValid(pemail)) {
            edpemail.setError(getString(R.string.error_invalid_email));
            focusView = edpemail;
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
            /*http://teamexplora.com/app/student_register.php?
            name=sdsa&regno=asdasddsasd&course=btech&semester=ist
            &email=fffff@a.com&password=sdfsdfsdf&parent_name=kanaram&parent_email=kanar@g.com&class_id=1*/
            TeacherSignUpTask(name, regno, course, sem, email, password, pname, pemail, phone);

        }
    }

    public void TeacherSignUpTask(final String name, final String regno, final String course, final String sem, final String email, final String password, final String pname, final String pemail, final String phone) {
        StringRequest stringRequest = new StringRequest(Method.POST, AppConfig.STUDENT_SIGNUP_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        showProgress(false);

                        Log.i("888880", response);

                        try {
                            //converting response to json object
                            JSONObject obj = new JSONObject(response);
                            Log.i("888880", response);
                            //if no error in response
                            String disp_msg = obj.getString("disp_msg");
                            if (obj.getString("err_flag").trim().equals("1")) {
                                Toast.makeText(getApplicationContext(), disp_msg, Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                            } else {
                                Toast.makeText(getApplicationContext(), disp_msg, Toast.LENGTH_SHORT).show();
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
                params.put("name", name);
                params.put("regno", regno);
                params.put("course", course);
                params.put("semester", sem);
                params.put("email", email);
                params.put("password", password);
                params.put("parent_name", pname);
                params.put("parent_email", pemail);
                params.put("class_id", "1");
                params.put("mobile", phone);
                return params;
            }
        };

        VolleySingleton.getInstance(StudentSignupActivity.this).addToRequestQueue(stringRequest);
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPhoneValid(String email) {
        return email.length() > 3;
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


    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(StudentSignupActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        edemail.setAdapter(adapter);
    }

    public void SudentSignUpTask(final String name, final String email, final String phone, final String password, final String dclass) {
        StringRequest stringRequest = new StringRequest(Method.GET, "http://teamexplora.com/app/register_teachers.php?" + "name=" + name + "&email=" + email + "&mobile=" + phone + "&password=" + password + "&class=" + dclass,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        showProgress(false);
                        try {
                            //converting response to json object
                            JSONObject obj = new JSONObject(response);
                            Log.i("888880", response);
                            //if no error in response
                            String disp_msg = obj.getString("disp_msg");
                            if (obj.getString("err_flag").trim().equals("1")) {
                                Toast.makeText(getApplicationContext(), disp_msg, Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            } else {
                                Toast.makeText(getApplicationContext(), disp_msg, Toast.LENGTH_SHORT).show();
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

        VolleySingleton.getInstance(StudentSignupActivity.this).addToRequestQueue(stringRequest);
    }
}

