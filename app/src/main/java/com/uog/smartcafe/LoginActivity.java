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
import android.widget.TextView;
import android.widget.Toast;

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

public class LoginActivity extends BaseCompatActivity {

    private static final String TAG = LoginActivity.class.getSimpleName();

    private TextView errorDisplay;
    private TextView signInformation;
    private EditText emailInput;
    private EditText passwordInput;
    ProgressDialog alert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        alert = new ProgressDialog(LoginActivity.this);
        alert.setTitle("LogIn");
        alert.setMessage("Loggin In");

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        ActionBar actionBar = getSupportActionBar();
        if(null != actionBar){
            actionBar.hide();
        }

        isUserLoggedIn();

        errorDisplay = (TextView)findViewById(R.id.login_error);
        signInformation = (TextView)findViewById(R.id.sign_in_notice);
        signInformation.setText(Helper.NEW_ACCOUNT);

        emailInput = (EditText)findViewById(R.id.email);
        passwordInput = (EditText)findViewById(R.id.password);

        Button signUpButton = (Button)findViewById(R.id.sign_up);
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signInIntent = new Intent(LoginActivity.this, RegistrationActivity.class);
                startActivity(signInIntent);
            }
        });

        Button loginButton = (Button)findViewById(R.id.login_button);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                alert.show();
                String enteredEmail = emailInput.getText().toString().trim();
                String enteredPassword = passwordInput.getText().toString().trim();
                if(TextUtils.isEmpty(enteredEmail) && TextUtils.isEmpty(enteredPassword))
                {
                    alert.dismiss();
                    errorDisplay.setText(R.string.must_fill_all_fields);
                }
                else
                {
                    if(TextUtils.isEmpty(enteredEmail) || TextUtils.isEmpty(enteredPassword)){
                        errorDisplay.setText(R.string.fill_all_fields);
                        alert.dismiss();
                        //Helper.displayErrorMessage(LoginActivity.this, getString(R.string.fill_all_fields));
                    }
                    else if(!Helper.isValidEmail(enteredEmail)){
                        errorDisplay.setText(R.string.invalid_email);
                        alert.dismiss();
                    }
                    else if(enteredPassword.length() < Helper.MINIMUM_LENGTH){
                        alert.dismiss();
                        errorDisplay.setText(R.string.maximum_pass_length);
                    }
                    else
                    {

                        errorDisplay.setText(" ");
                        //make server call for user authentication
                        authenticateUserInRemoteServer(enteredEmail, enteredPassword);
                    }
                }

            }
        });
    }

    private void authenticateUserInRemoteServer(String email, String password){
        Map<String, String> params = new HashMap<String,String>();
        params.put(Helper.EMAIL, email);
        params.put(Helper.PASSWORD, password);

        GsonRequest<LoginObject> serverRequest = new GsonRequest<LoginObject>(
                Request.Method.POST,
                Helper.PATH_TO_SERVER_LOGIN,
                LoginObject.class,
                params,
                createRequestSuccessListener(),
                createRequestErrorListener());

        serverRequest.setRetryPolicy(new DefaultRetryPolicy(
                Helper.MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        VolleySingleton.getInstance(LoginActivity.this).addToRequestQueue(serverRequest);
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


                        // navigate to restaurant home
                        Intent loginIntent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(loginIntent);
                        finish();
                    }
                    else if(response.getLoggedIn().equals("2")){
                        errorDisplay.setText("Your Account Need Admin Approval");
                        //Toast.makeText(LoginActivity.this, "Your Account Need Admin Approval", Toast.LENGTH_LONG).show();
                    }
                    else if(response.getLoggedIn().equals("0")){
                        errorDisplay.setText("Email or Password is Incorrect!");
                        //Toast.makeText(LoginActivity.this, "", Toast.LENGTH_LONG).show();
                    }
                    else{
                        errorDisplay.setText("Login Failed!! Try Again Later");
                        //Toast.makeText(LoginActivity.this, R.string.failed_login, Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
    }

    private Response.ErrorListener createRequestErrorListener() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                alert.dismiss();
                error.printStackTrace();
            }
        };
    }

    private boolean isUserLoggedIn(){
        Gson mGson = ((CustomApplication)getApplication()).getGsonObject();
        String storedUser = ((CustomApplication)getApplication()).getShared().getUserData();
        LoginObject userObject = mGson.fromJson(storedUser, LoginObject.class);
        if(userObject != null){
            alert.dismiss();
            Intent intentMain = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intentMain);
            finish();
            return true;
        }
        return false;
    }
}
