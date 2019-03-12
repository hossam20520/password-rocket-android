package com.example.passwordrocket;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Sign_Up_Activity extends AppCompatActivity {
    private static String Url = "https://tomorz.com/develop/sign_up.php";
    private StringRequest stringRequest;
    private RequestQueue request;
    ProgressBar progressBar;
    Intent loginc;
    public static final String PREFS = "myprefs";
    static String email_pref = null;
    Button signup , loginn;
    EditText fname , lname , email , password , confirm;
    String fname_txt , lname_txt , email_txt , Password_txt , confirm_txt;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.sign_up_activity);
            progressBar = (ProgressBar) findViewById(R.id.progressBar_sign);
            progressBar.setVisibility(View.GONE);
            fname = (EditText) findViewById(R.id.fname);
            lname = (EditText) findViewById(R.id.lname);
            email = (EditText) findViewById(R.id.email);
            password = (EditText) findViewById(R.id.password_upp);
            confirm = (EditText) findViewById(R.id.confirm_f);
            signup = (Button) findViewById(R.id.Sign_up);
            loginn = (Button) findViewById(R.id.login_nn);

            request  = Volley.newRequestQueue(this);

            signup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    progressBar.setVisibility(View.VISIBLE);
                    fname_txt = fname.getText().toString();
                    lname_txt = lname.getText().toString();
                    email_txt = email.getText().toString();
                    Password_txt = password.getText().toString();
                    confirm_txt = confirm.getText().toString();


                    stringRequest = new StringRequest(Request.Method.POST, Url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {


                            try {
                                JSONObject jsonObject = new JSONObject(response);

                                if (jsonObject.names().get(0).equals("success")) {
                                    email_pref = email_txt;
                                    progressBar.setVisibility(View.GONE);
                                    Intent intentez = new Intent(getApplicationContext(), Display.class);
                                    intentez.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intentez);
                                } else if(jsonObject.names().get(0).equals("found")) {
                                    progressBar.setVisibility(View.GONE);
                                    Toast.makeText(getApplicationContext(), "found", Toast.LENGTH_LONG).show();
                                }else{

                                    progressBar.setVisibility(View.GONE);

                                    Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_LONG).show();
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            HashMap<String, String> hasmap = new HashMap<String, String>();
                            hasmap.put("fname", fname_txt);
                            hasmap.put("lname", lname_txt);
                            hasmap.put("email", email_txt);
                            hasmap.put("password", Password_txt);

                            return hasmap;
                        }
                    };
                    request.add(stringRequest);


                }
            });

loginn.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        loginc = new Intent(getApplicationContext(), MainActivity.class);
        loginc.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(loginc);
    }
});


        }


    @Override
    protected void onResume(){
        super.onResume();
        SharedPreferences sp = this.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        email_pref = sp.getString("email" , null);
        if(email_pref != null) {

            loginc = new Intent(getApplicationContext(), MainActivity.class);
            loginc.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(loginc);
        }
    }

    @Override
    protected  void  onPause(){
        super.onPause();
        if(email_pref != null){
            SharedPreferences sp = this.getSharedPreferences(PREFS , Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            editor.putString("email" , email_pref);
            editor.commit();

        }

        System.exit(1);
    }
}
