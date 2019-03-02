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
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Signup extends AppCompatActivity {
    Button button;
    EditText namalengkap, username, password, password0, alamat, notlpn, email;
    TextView signin;
    private long mLastClickTime = 0;
    SharedPrefManager sharedPrefManager;
    private RequestQueue mRequestQueue;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        progressBar = (ProgressBar) findViewById(R.id.progressBar2);
        signin = (TextView)findViewById(R.id.tvSignin);
        button = (Button)findViewById(R.id.bnTambah);
        namalengkap = (EditText)findViewById(R.id.etNamalengkap);
        username = (EditText)findViewById(R.id.etUsername);
        password = (EditText)findViewById(R.id.etPassword);
        password0 = (EditText)findViewById(R.id.etPassword0);
        alamat = (EditText)findViewById(R.id.etAlamat);
        notlpn = (EditText)findViewById(R.id.etNotlpn);
        email = (EditText)findViewById(R.id.etEmail);
        sharedPrefManager = new SharedPrefManager(this);

        if (sharedPrefManager.getSPSudahLogin()){
            startActivity(new Intent(Signup.this, Home.class));
            finish();
        }
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Signup.this, Home.class));
                finish();
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000){
                    return;
                }else{
                    mLastClickTime = SystemClock.elapsedRealtime();
                    signup_proses();
                }
            }
        });
    }
    public void signup_proses(){
        final String Namalengkap, Username, Password, Password0, Alamat, Notlpn, Email;
        Namalengkap = namalengkap.getText().toString().trim();
        Username = username.getText().toString().trim();
        Password = password.getText().toString().trim();
        Password0 = password0.getText().toString().trim();
        Alamat = alamat.getText().toString().trim();
        Notlpn = notlpn.getText().toString().trim();
        Email = email.getText().toString().trim();

        if(Namalengkap.equals("") || Username.equals("") || Password.equals("") || Email.equals("")) {
            Toast.makeText(Signup.this, "Harap isi Kolom", Toast.LENGTH_SHORT).show();
        }else if(!(Password.equals(Password0))) {
            Toast.makeText(Signup.this, "Password not match", Toast.LENGTH_SHORT).show();
        }else if(Namalengkap.length() > 255) {
            Toast.makeText(Signup.this, "Panjang Nama Lengkap max 255", Toast.LENGTH_SHORT).show();
        }else if(Username.length() > 255) {
            Toast.makeText(Signup.this, "Panjang Username max 255", Toast.LENGTH_SHORT).show();
        }else if(Password.length() > 255) {
            Toast.makeText(Signup.this, "Panjang Nama Lengkap max 255", Toast.LENGTH_SHORT).show();
        }else if(Alamat.length() > 255) {
            Toast.makeText(Signup.this, "Panjang Alamat max 255", Toast.LENGTH_SHORT).show();
        }else if(Notlpn.length() > 255) {
            Toast.makeText(Signup.this, "Panjang Notlpn max 255", Toast.LENGTH_SHORT).show();
        }else if(Email.length() > 255) {
            Toast.makeText(Signup.this, "Panjang Email max 255", Toast.LENGTH_SHORT).show();
        }else if(!(android.util.Patterns.EMAIL_ADDRESS.matcher(Email).matches())) {
            Toast.makeText(Signup.this, "Email not valid", Toast.LENGTH_SHORT).show();
        }else{
            mRequestQueue = MySingleton.getInstance(Signup.this).getRequestQueue();
            progressBar.setVisibility(View.VISIBLE);

            StringRequest MyStringRequest = new StringRequest(Request.Method.POST, MySingleton.regis, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    progressBar.setVisibility(View.INVISIBLE);
                    System.out.println(response);
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        if(jsonObject.getString("error").equalsIgnoreCase("false")){
                            Toast.makeText(getApplicationContext(), "Berhasil daftar", Toast.LENGTH_LONG).show();
                            startActivity(new Intent(Signup.this, Home.class));
                            finish();
                        }else{
                            Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_LONG).show();
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
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("nama_lengkap", Namalengkap);
                    params.put("username", Username);
                    params.put("password", Password);
                    params.put("password0", Password0);
                    params.put("alamat", Alamat);
                    params.put("notlpn", Notlpn);
                    params.put("email", Email);
                    return params;
                }
            };
            MySingleton.getInstance(Signup.this).addToRequestQueue(MyStringRequest);
        }
    }
}
