package com.uog.smartcafe;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.uog.smartcafe.entities.LoginObject;
import com.uog.smartcafe.network.GsonRequest;
import com.uog.smartcafe.network.VolleySingleton;
import com.uog.smartcafe.util.CustomApplication;
import com.uog.smartcafe.util.Helper;

import java.util.HashMap;
import java.util.Map;

public class RegistrationActivity extends BaseCompatActivity {

    private static final String TAG = RegistrationActivity.class.getSimpleName();

    private TextView displayError;

    private EditText username;
    private EditText email;
    private EditText password;
    private EditText department;
    private Spinner semester;
    private Spinner customerTypeSpinner;
    private EditText phoneNumber;
    String enteredSemester;
    String enteredAddress;
    String customerType;

    ProgressDialog alert ;

    String[] semesterTypes = {"1st","2nd","3rd","4th","5th","6th","7th","8th","9th","10th","Repeater","Not Applicable"};
    String[] customerTypes = {"Student","Teacher"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        alert = new ProgressDialog(RegistrationActivity.this);
        alert.setTitle("Sign Up");
        alert.setMessage("Registering");

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        ActionBar actionBar = getSupportActionBar();
        if(null != actionBar){
            actionBar.hide();
        }

        isUserLoggedIn();

        displayError = (TextView)findViewById(R.id.login_error);

        username = (EditText)findViewById(R.id.username);
        email = (EditText)findViewById(R.id.email);
        password = (EditText)findViewById(R.id.password);
        department = (EditText)findViewById(R.id.department);
        semester = (Spinner) findViewById(R.id.semester);
        customerTypeSpinner = (Spinner) findViewById(R.id.customertype);
        phoneNumber = (EditText)findViewById(R.id.phone_number);


        ArrayAdapter<String> spinnerArrayAdapter3 = new ArrayAdapter<String>(getApplicationContext(),R.layout.spinner_item,semesterTypes);
        spinnerArrayAdapter3.setDropDownViewResource(R.layout.spinner_item);
        semester.setAdapter(spinnerArrayAdapter3);

        semester.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                enteredSemester = semesterTypes[position];


                Log.v("RegistrationActivity", "Ëntered Semester is " +enteredSemester);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        ArrayAdapter<String> customerArrayAdapter = new ArrayAdapter<String>(getApplicationContext(),R.layout.spinner_item,customerTypes);
        customerArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
        customerTypeSpinner.setAdapter(customerArrayAdapter);

        customerTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                  customerType  = customerTypes[position];


                Log.v("RegistrationActivity", "Ëntered Customer  is " +customerType);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Button createAccountButton = (Button)findViewById(R.id.create_account_button);
        createAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                alert.show();
                String enteredUsername = username.getText().toString().trim();
                String enteredEmail = email.getText().toString().trim();
                String enteredPassword = password.getText().toString().trim();
                String enteredDepartment = department.getText().toString().trim();

                enteredAddress = enteredDepartment + ", " + enteredSemester ;

                String enteredPhoneNumber = phoneNumber.getText().toString();





                if(TextUtils.isEmpty(enteredUsername) && TextUtils.isEmpty(enteredEmail) && TextUtils.isEmpty(enteredPassword)
                        && TextUtils.isEmpty(enteredDepartment) && TextUtils.isEmpty(enteredPhoneNumber)){
                    displayError.setText(R.string.must_fill_all_fields);
                    alert.dismiss();
//                    Helper.displayErrorMessage(RegistrationActivity.this, getString(R.string.fill_all_fields));
                }
                else
                {
                    if(TextUtils.isEmpty(enteredUsername) || TextUtils.isEmpty(enteredEmail) || TextUtils.isEmpty(enteredPassword)
                            || TextUtils.isEmpty(enteredDepartment) || TextUtils.isEmpty(enteredPhoneNumber)){
                        displayError.setText(R.string.fill_all_fields);
                    }

                    else if(!Helper.isValidEmail(enteredEmail)){
                        displayError.setText(R.string.invalid_email);
                        alert.dismiss();
                    }

                    else if(enteredUsername.length() < Helper.MINIMUM_LENGTH){
                        displayError.setText(R.string.maximum_length);
                        alert.dismiss();
                    }
                    else if(enteredPassword.length() < Helper.MINIMUM_LENGTH){
                        displayError.setText(R.string.maximum_pass_length);
                        alert.dismiss();
                    }
                    else if(enteredPhoneNumber.length() != Helper.MINIMUM_PHONE ){
                        displayError.setText(R.string.invalid_phone);
                        alert.dismiss();
                    }
                    else if(!Helper.isValidPhone(enteredPhoneNumber)){
                        displayError.setText(R.string.invalid_phone);
                        alert.dismiss();
                    }
                    else
                    {

                        displayError.setText(" ");
                        Log.d(TAG, enteredUsername + enteredEmail + enteredPassword + enteredAddress + enteredPhoneNumber);
                        //Add new user to the server
                        addNewUserToRemoteServer(enteredUsername, enteredEmail, enteredPassword, enteredAddress, enteredPhoneNumber);
                    }
                }

            }
        });
    }

    private void addNewUserToRemoteServer(String username, String email, String password, String address, String phoneNumber){
        Map<String, String> params = new HashMap<String,String>();
        params.put(Helper.USERNAME, username);
        params.put(Helper.EMAIL, email);
        params.put(Helper.PASSWORD, password);
        params.put(Helper.ADDRESS, address);
        params.put(Helper.PHONE_NUMBER, phoneNumber);
        params.put(Helper.CUSTOMER_TYPE, customerType);

        GsonRequest<LoginObject> serverRequest = new GsonRequest<LoginObject>(
                Request.Method.POST,
                Helper.PATH_TO_SERVER_REGISTRATION,
                LoginObject.class,
                params,
                createRequestSuccessListener(),
                createRequestErrorListener());

        serverRequest.setRetryPolicy(new DefaultRetryPolicy(
                Helper.MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        VolleySingleton.getInstance(RegistrationActivity.this).addToRequestQueue(serverRequest);
    }

    private Response.Listener<LoginObject> createRequestSuccessListener() {
        return new Response.Listener<LoginObject>() {
            @Override
            public void onResponse(LoginObject response) {
                try {

                    alert.dismiss();
                    Log.d(TAG, "Json Response " + response.getLoggedIn());
                    if(response.getLoggedIn().equals("1")){
                        //save login data to a shared preference
                        String userData = ((CustomApplication)getApplication()).getGsonObject().toJson(response);
                        ((CustomApplication)getApplication()).getShared().setUserData(userData);

                        Log.v("USERDATA",userData);
                        // navigate to restaurant home

                             Intent loginIntent = new Intent(RegistrationActivity.this, MainActivity.class);
                             loginIntent.putExtra("customertype",customerType);
                             startActivity(loginIntent);




                    }else if(response.getLoggedIn().equals("0")){
                        Helper.displayErrorMessage(RegistrationActivity.this, "User registration failed - Email address already exist");
                    }else{
                        Toast.makeText(RegistrationActivity.this, R.string.failed_registration, Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
    }

    private Response.ErrorListener createRequestErrorListener() {
        alert.dismiss();
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Invalid Retur", "eRR", error);
            }
        };
    }

    private void isUserLoggedIn(){

        alert.dismiss();
        Gson mGson = ((CustomApplication)getApplication()).getGsonObject();
        String storedUser = ((CustomApplication)getApplication()).getShared().getUserData();
        LoginObject userObject = mGson.fromJson(storedUser, LoginObject.class);
        if(userObject != null){
            Intent intentMain = new Intent(RegistrationActivity.this, LoginActivity.class);
            startActivity(intentMain);
            Toast.makeText(this, "Your Account need Admin Approval...", Toast.LENGTH_SHORT).show();
        }
    }
}
