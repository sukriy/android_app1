package com.a000webhostapp.prores.program4;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class History extends AppCompatActivity {
    SharedPrefManager sharedPrefManager;
    private RequestQueue mRequestQueue;
    private RecyclerView recyclerView;
    ProgressBar progressBar;
    ArrayList<ListHistory> listItems;
    private AdapterHistory adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView = (RecyclerView)findViewById(R.id.recylerView);
        sharedPrefManager = new SharedPrefManager(this);
        progressBar = (ProgressBar) findViewById(R.id.progressBar4);
        progressBar.bringToFront();

        if (sharedPrefManager.getSPSudahLogin() == false){
            finish();
        }
        loadRecyclerViewData();
    }
    private void loadRecyclerViewData(){
        listItems = new ArrayList<ListHistory>();

        mRequestQueue = MySingleton.getInstance(History.this).getRequestQueue();
        progressBar.setVisibility(View.VISIBLE);

        StringRequest MyStringRequest = new StringRequest(Request.Method.POST, MySingleton.pesan_list,
            new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    System.out.println(response);
                    progressBar.setVisibility(View.INVISIBLE);
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray array = jsonObject.getJSONArray("message");
                        listItems.clear();
                        for(int i=0; i<array.length(); i++) {
                            JSONObject o = array.getJSONObject(i);
                            ListHistory item = new ListHistory(
                                    o.getString("id_pesan"),
                                    o.getString("tgl_input"),
                                    o.getString("total"),
                                    o.getString("nama_lengkap")
                            );
                            listItems.add(item);
                        }
                        adapter = new AdapterHistory(listItems, History.this);
                        recyclerView.setAdapter(adapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

                        adapter.setData();
                        adapter.notifyDataSetChanged();

                    } catch (JSONException e) {
                        e.printStackTrace();
                        System.out.println(e.getMessage());
                        Toast.makeText(History.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError e) {
                    progressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }){
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> MyData = new HashMap<String, String>();
                MyData.put("id_account", sharedPrefManager.getSPId()); //Add the data you'd like to send to the server.
                return MyData;
            }
        };
        MySingleton.getInstance(History.this).addToRequestQueue(MyStringRequest);
    }
}
