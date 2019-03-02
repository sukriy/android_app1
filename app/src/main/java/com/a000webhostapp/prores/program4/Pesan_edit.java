package com.a000webhostapp.prores.program4;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Pesan_edit extends AppCompatActivity {
    static ArrayList<ListOrder> temp = new ArrayList<>();
    Pesan_listFragment pesan_listFragment = new Pesan_listFragment();
    CheckoutAddFragement checkoutFragment = new CheckoutAddFragement();
    FragmentManager fm = getSupportFragmentManager();
    Button submit;
    ProgressBar progressBar;
    private RequestQueue mRequestQueue;
    SharedPrefManager sharedPrefManager;
    static String jenis, pmbyrn, meja, link, id_pesan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pesan_edit);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        progressBar = (ProgressBar) findViewById(R.id.progressBar4);
        progressBar.bringToFront();
        sharedPrefManager = new SharedPrefManager(this);

        if (sharedPrefManager.getSPSudahLogin() == false){
            finish();
        }
        Intent intent = getIntent();
        id_pesan = intent.getStringExtra("id_pesan");
        if(id_pesan.equals("")){
            finish();
        }else{
            pesan_listFragment.cetak(id_pesan, "edit");
        }


        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.contain_frag, pesan_listFragment, "frag1");
        ft.commit();

        submit = (Button) findViewById(R.id.bnSubmit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(submit.getText().toString().equalsIgnoreCase("PESAN")){
                    temp = pesan_listFragment.tarik_data();

                    System.out.println(temp.size());
                    if(temp.size() > 0){
                        submit.setText("KONFIRM");
                        checkoutFragment.pass_var(temp);
                        FragmentTransaction ft = fm.beginTransaction();
                        ft.replace(R.id.contain_frag, checkoutFragment, "frag1");
                        ft.commit();

                    }else{
                        Toast.makeText(Pesan_edit.this, "Harap Pilih Pesanan", Toast.LENGTH_LONG).show();
                    }
                }else if(submit.getText().toString().equalsIgnoreCase("KONFIRM")){
                    Gson gson = new Gson();
                    final String newgson = gson.toJson(temp);
                    System.out.println(newgson);

                    progressBar.setVisibility(View.VISIBLE);
                    mRequestQueue = MySingleton.getInstance(Pesan_edit.this).getRequestQueue();

                    StringRequest stringRequest = new StringRequest(Request.Method.POST, MySingleton.pesan_edit,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    progressBar.setVisibility(View.INVISIBLE);
                                    System.out.println(response);
                                    try {
                                        JSONObject jsonObject = new JSONObject(response);
                                        Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                                        if (jsonObject.getString("error").equals("false")) {
                                            System.out.println("check0");
                                            finish();
                                        }else{
                                            System.out.println("check1");
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
                            params.put("id_pesan", id_pesan);
                            params.put("data", newgson);
                            return params;
                        }
                    };
                    MySingleton.getInstance(Pesan_edit.this).addToRequestQueue(stringRequest);
                }
            }
        });

    }
}
