package com.a000webhostapp.prores.program4;

import android.content.Intent;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Signin extends AppCompatActivity {

    private RequestQueue mRequestQueue;
    SharedPrefManager sharedPrefManager;
    Button signin;
    TextView signup;
    EditText username, password;
    ProgressBar progressBar;
    long mLastClickTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        signin = (Button) findViewById(R.id.btnSignin);
        signup = (TextView) findViewById(R.id.tvSignup);
        username = (EditText) findViewById(R.id.etUsername);
        password = (EditText) findViewById(R.id.etPassword);
        sharedPrefManager = new SharedPrefManager(this);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        sharedPrefManager = new SharedPrefManager(this);

        if (sharedPrefManager.getSPSudahLogin()) {
            startActivity(new Intent(getApplicationContext(), Home.class));
            finish();
        }
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Signup.class));
                finish();
            }
        });
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000){
                    return;
                }else{
                    mLastClickTime = SystemClock.elapsedRealtime();
                    signin_proses();
                }
            }
        });
    }
    public void signin_proses(){
        final String Username, Password;
        Username = username.getText().toString().trim();
        Password = password.getText().toString().trim();

        if(Username.equals("") || Password.equals("")){
            Toast.makeText(getApplicationContext(), "Harap isi semua kolom", Toast.LENGTH_LONG).show();
        }else{
            mRequestQueue = MySingleton.getInstance(Signin.this).getRequestQueue();
            progressBar.setVisibility(View.VISIBLE);

            StringRequest MyStringRequest = new StringRequest(Request.Method.POST, MySingleton.login, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    progressBar.setVisibility(View.INVISIBLE);
                    System.out.println(response);
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        if (jsonObject.getString("error").equals("false")) {
                            Toast.makeText(getApplicationContext(), "Berhasil Login", Toast.LENGTH_SHORT).show();
                            JSONObject data = jsonObject.getJSONObject("message");

                            sharedPrefManager.saveSPString(SharedPrefManager.SP_ID, data.getString("id_account"));
                            sharedPrefManager.saveSPString(SharedPrefManager.SP_NAMA, data.getString("nama_lengkap"));
                            sharedPrefManager.saveSPString(SharedPrefManager.SP_LEVEL, data.getString("level"));
                            sharedPrefManager.saveSPString(SharedPrefManager.SP_GAMBAR, data.getString("gambar"));
                            sharedPrefManager.saveSPString(SharedPrefManager.SP_SALDO, data.getString("saldo"));
                            sharedPrefManager.saveSPBoolean(SharedPrefManager.SP_SUDAH_LOGIN, true);
                            startActivity(new Intent(getApplicationContext(), Home.class));
                            finish();
                        } else {
                            Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }, new Response.ErrorListener() { //Create an error listener to handle errors appropriately.
                @Override
                public void onErrorResponse(VolleyError error) {
                    progressBar.setVisibility(View.INVISIBLE);
                    System.out.println("ok1");
                    Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                }
            }) {
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> MyData = new HashMap<String, String>();
                    MyData.put("username", Username); //Add the data you'd like to send to the server.
                    MyData.put("password", Password); //Add the data you'd like to send to the server.
                    return MyData;
                }
            };
            MySingleton.getInstance(Signin.this).addToRequestQueue(MyStringRequest);
        }
    }
}
