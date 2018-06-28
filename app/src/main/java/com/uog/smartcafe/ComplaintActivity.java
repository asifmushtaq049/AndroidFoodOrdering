package com.uog.smartcafe;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.uog.smartcafe.entities.LoginObject;
import com.uog.smartcafe.entities.SuccessObject;
import com.uog.smartcafe.network.GsonRequest;
import com.uog.smartcafe.network.VolleySingleton;
import com.uog.smartcafe.util.CustomApplication;
import com.uog.smartcafe.util.CustomSharedPreference;
import com.uog.smartcafe.util.Helper;

import java.util.HashMap;
import java.util.Map;

public class ComplaintActivity extends AppCompatActivity {

    EditText complaintMessage;
    EditText compalintTitle;
    Button sendComplaintBtn;
    private CustomSharedPreference shared;
    private LoginObject user;
    ProgressDialog alert;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complaint);

        compalintTitle = (EditText) findViewById(R.id.complainttitle);
        complaintMessage = (EditText) findViewById(R.id.complaintmessage);
        sendComplaintBtn = (Button) findViewById(R.id.sendcomplaint);

         alert = new ProgressDialog(ComplaintActivity.this);
         alert.setTitle("Complaints");
         alert.setMessage("Sending Complaints");
        Gson gson = ((CustomApplication)getApplication()).getGsonObject();

        shared = ((CustomApplication)getApplication()).getShared();
        user = gson.fromJson(shared.getUserData(), LoginObject.class);
        sendComplaintBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.v("COMPLAINT",Integer.toString(user.getId()));
                alert.show();
                String userID  = Integer.toString(user.getId());
                String title = compalintTitle.getText().toString();
                String message= complaintMessage.getText().toString();
                sendComplaints(userID,title,message);
            }
        });
    }


    private void sendComplaints(String userId, String title, String message){

        Map<String, String> params = new HashMap<String,String>();
        params.put("USER_ID", userId);
        params.put("TITLE", title);
        params.put("MESSAGE", message);


        GsonRequest<SuccessObject> serverRequest = new GsonRequest<SuccessObject>(
                Request.Method.POST,
                Helper.PATH_TO_SEND_COMPLAINT,
                SuccessObject.class,
                params,
                createRequestSuccessListener(),
                createRequestErrorListener());

        serverRequest.setRetryPolicy(new DefaultRetryPolicy(
                Helper.MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        VolleySingleton.getInstance(ComplaintActivity.this).addToRequestQueue(serverRequest);
    }

    private Response.Listener<SuccessObject> createRequestSuccessListener() {
        return new Response.Listener<SuccessObject>() {
            @Override
            public void onResponse(SuccessObject response) {
                //Log.d(TAG,"JSON response " + response.toString());
                try {

                    alert.dismiss();
                    Log.d("COMPLAINTRESPONSE", "Json Response " + response.getSuccess());
                    if(response.getSuccess() == 1){


                        Toast.makeText(ComplaintActivity.this, "Complaint Successfully Registered", Toast.LENGTH_SHORT).show();

                        compalintTitle.setText("");
                        complaintMessage.setText("");

                    }else{
                        Helper.displayErrorMessage(ComplaintActivity.this, "Failed to upload order to server");
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
}
