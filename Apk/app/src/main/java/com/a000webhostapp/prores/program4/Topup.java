package com.a000webhostapp.prores.program4;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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

public class Topup extends AppCompatActivity {
    SharedPrefManager sharedPrefManager;
    TextView nominal;
    Button submit;
    ProgressBar progressBar;
    private RequestQueue mRequestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topup);

        submit = (Button)findViewById(R.id.bnTopup);
        nominal = (TextView)findViewById(R.id.etNominal);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        sharedPrefManager = new SharedPrefManager(this);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.bringToFront();
        if (sharedPrefManager.getSPSudahLogin() == false){
            finish();
        }

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String Nominal;
                Nominal = nominal.getText().toString().trim();

                if(Nominal.equals("")){
                    Toast.makeText(getApplicationContext(), "Harap isi nominal", Toast.LENGTH_LONG).show();
                }else{
                    progressBar.setVisibility(View.VISIBLE);
                    mRequestQueue = MySingleton.getInstance(Topup.this).getRequestQueue();

                    StringRequest stringRequest = new StringRequest(Request.Method.POST, MySingleton.topup,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    System.out.println(response);
                                    progressBar.setVisibility(View.INVISIBLE);
                                    try {
                                        JSONObject jsonObject = new JSONObject(response);
                                        if(!(jsonObject.getString("message").equals(""))){
                                            if(jsonObject.getString("error").equals("false")){
                                                Toast.makeText(getApplicationContext(), "Berhasil TopUp", Toast.LENGTH_LONG).show();
                                                finish();
                                            }else{
                                                Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                                            }
                                        }else{
                                            Toast.makeText(getApplicationContext(), jsonObject.getString("Bermasalah, Harap coba kembali"), Toast.LENGTH_SHORT).show();
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    progressBar.setVisibility(View.INVISIBLE);
                                    Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            }
                    ) {
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> params = new HashMap<String, String>();
                            params.put("id_account", sharedPrefManager.getSPId());
                            params.put("nominal", Nominal);
                            return params;
                        }
                    };
                    MySingleton.getInstance(Topup.this).addToRequestQueue(stringRequest);
                }
            }
        });
    }
}
