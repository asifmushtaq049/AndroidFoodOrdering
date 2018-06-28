package com.uog.smartcafe;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.google.gson.Gson;
import com.uog.smartcafe.entities.LoginObject;
import com.uog.smartcafe.util.CustomApplication;

public class LoginOptionActivity extends BaseCompatActivity {

    private static final String TAG = LoginOptionActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        isUserLoggedIn();
        setContentView(R.layout.activity_login_option);


        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        ActionBar actionBar = getSupportActionBar();
        if(null != actionBar){
            actionBar.hide();
        }
        Button signInButton = (Button)findViewById(R.id.sign_in);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent loginIntent = new Intent(LoginOptionActivity.this, LoginActivity.class);
                startActivity(loginIntent);
            }
        });

        Button signUpButton = (Button)findViewById(R.id.sign_up);
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signInIntent = new Intent(LoginOptionActivity.this, RegistrationActivity.class);
                startActivity(signInIntent);
            }
        });

    }

    private boolean isUserLoggedIn(){
        Gson mGson = ((CustomApplication)getApplication()).getGsonObject();
        String storedUser = ((CustomApplication)getApplication()).getShared().getUserData();
        LoginObject userObject = mGson.fromJson(storedUser, LoginObject.class);
        if(userObject != null){
            Intent intentMain = new Intent(LoginOptionActivity.this, MainActivity.class);
            startActivity(intentMain);
            finish();
            return true;
        }
        return false;
    }
}
