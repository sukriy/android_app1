package com.a000webhostapp.prores.program4;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
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

public class Process extends AppCompatActivity {
    SharedPrefManager sharedPrefManager;
    private RequestQueue mRequestQueue;
    private RecyclerView recyclerView;
    ProgressBar progressBar;
    ArrayList<ListHistory> listItems;
    private AdapterHistory adapter;
    SwipeController1 swipeController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_process);
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

        mRequestQueue = MySingleton.getInstance(Process.this).getRequestQueue();
        progressBar.setVisibility(View.VISIBLE);

        StringRequest MyStringRequest = new StringRequest(Request.Method.POST, MySingleton.process,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("Proses");
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
                            adapter = new AdapterHistory(listItems, Process.this);
                            recyclerView.setAdapter(adapter);
                            recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

                            adapter.setData0();
                            adapter.notifyDataSetChanged();

                            swipeController = new SwipeController1(new SwipeControllerActions() {
                                @Override
                                public void onRightClicked(final int position) {
                                    Toast.makeText(Process.this, "Right Cick", Toast.LENGTH_LONG).show();
                                    progressBar.setVisibility(View.VISIBLE);
                                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int choice) {
                                            switch (choice) {
                                                case DialogInterface.BUTTON_POSITIVE:
                                                    StringRequest stringRequest = new StringRequest(Request.Method.POST, MySingleton.pesan_delete,
                                                            new Response.Listener<String>() {
                                                                @Override
                                                                public void onResponse(String response) {
                                                                    System.out.println(response);
                                                                    progressBar.setVisibility(View.INVISIBLE);
                                                                    try {
                                                                        JSONObject jsonObject = new JSONObject(response);
                                                                        if(!(jsonObject.getString("message").equals(""))){
                                                                            if(jsonObject.getString("error").equals("false")){
                                                                                Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                                                                                listItems.remove(position);
                                                                                adapter.notifyItemRemoved(position);
                                                                                adapter.notifyItemRangeChanged(position, adapter.getItemCount());
                                                                            }else{
                                                                                Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                                                                                adapter.notifyItemRangeChanged(position, adapter.getItemCount());
                                                                            }
                                                                        }else{
                                                                            Toast.makeText(getApplicationContext(), "Bermasalah, Harap coba kembali", Toast.LENGTH_SHORT).show();
                                                                            adapter.notifyItemRangeChanged(position, adapter.getItemCount());
                                                                        }
                                                                    } catch (JSONException e) {
                                                                        e.printStackTrace();
                                                                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                                                                        adapter.notifyItemRangeChanged(position, adapter.getItemCount());
                                                                    }
                                                                }
                                                            },
                                                            new Response.ErrorListener() {
                                                                @Override
                                                                public void onErrorResponse(VolleyError error) {
                                                                    progressBar.setVisibility(View.INVISIBLE);
                                                                    Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                                                                    adapter.notifyItemRangeChanged(position, adapter.getItemCount());
                                                                }
                                                            }
                                                    ) {
                                                        protected Map<String, String> getParams() throws AuthFailureError {
                                                            Map<String, String> params = new HashMap<String, String>();
                                                            params.put("user_input", sharedPrefManager.getSPId());
                                                            params.put("id_order", adapter.cetak(position));
                                                            return params;
                                                        }
                                                    };
                                                    MySingleton.getInstance(Process.this).addToRequestQueue(stringRequest);
                                                    break;
                                                case DialogInterface.BUTTON_NEGATIVE:
                                                    adapter.notifyItemRangeChanged(position, adapter.getItemCount());
                                                    break;
                                            }
                                        }
                                    };

                                    AlertDialog.Builder builder = new AlertDialog.Builder(Process.this);
                                    builder.setMessage("Delete this Order?")
                                            .setPositiveButton("Yes", dialogClickListener)
                                            .setNegativeButton("No", dialogClickListener).show();
                                }
                                @Override
                                public void onLeftClicked(int position) {
                                    Toast.makeText(Process.this, "Left Cick", Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(getApplicationContext(), Order_add.class);
                                    intent.putExtra("id_pesan", adapter.cetak(position));
                                    startActivity(intent);
                                    finish();
                                }
                            });

                            ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeController);
                            itemTouchhelper.attachToRecyclerView(recyclerView);

                            recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
                                @Override
                                public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
                                    swipeController.onDraw(c);
                                }
                            });

                        } catch (JSONException e) {
                            e.printStackTrace();
                            System.out.println(e.getMessage());
                            Toast.makeText(Process.this, e.getMessage(), Toast.LENGTH_LONG).show();
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
        MySingleton.getInstance(Process.this).addToRequestQueue(MyStringRequest);
    }
}
