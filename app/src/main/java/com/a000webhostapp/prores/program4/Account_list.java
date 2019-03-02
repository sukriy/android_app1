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
import android.widget.Button;
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

public class Account_list extends AppCompatActivity {
    private RequestQueue mRequestQueue;
    private RecyclerView recyclerView;
    private MyAdapter adapter;
    ProgressBar progressBar;
    ArrayList<ListItem> listItems;
    Button tambah;
    SwipeController swipeController;
    SwipeController0 swipeController0;
    SharedPrefManager sharedPrefManager;
    SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_list);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        recyclerView = (RecyclerView)findViewById(R.id.recylerView);
        sharedPrefManager = new SharedPrefManager(this);
        tambah = (Button)findViewById(R.id.bntambah);
        mSwipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swipeRefreshLayout);
        progressBar = (ProgressBar) findViewById(R.id.progressBar3);

        if (sharedPrefManager.getSPSudahLogin() == false){
            finish();
        }
        tambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Account_plus.class));
            }
        });
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Refresh items
                mSwipeRefreshLayout.setRefreshing(true);
                Toast.makeText(getApplicationContext(), "Refresh", Toast.LENGTH_LONG).show();
                adapter.setData();
                adapter.notifyDataSetChanged();
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
        loadRecyclerViewData();
    }
    private void loadRecyclerViewData(){
        listItems = new ArrayList<>();

        mRequestQueue = MySingleton.getInstance(Account_list.this).getRequestQueue();
        progressBar.setVisibility(View.VISIBLE);

        if(sharedPrefManager.getSpLevel().equalsIgnoreCase("staff")){
            StringRequest MyStringRequest = new StringRequest(Request.Method.POST, MySingleton.account,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            progressBar.setVisibility(View.INVISIBLE);
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                JSONArray array = jsonObject.getJSONArray("message");
                                listItems.clear();
                                for(int i=0; i<array.length(); i++) {
                                    JSONObject o = array.getJSONObject(i);
                                    ListItem item = new ListItem(
                                            o.getString("id_account"),
                                            o.getString("username"),
                                            o.getString("gambar"),
                                            o.getString("level")
                                    );
                                    listItems.add(item);
                                }
                                adapter = new MyAdapter(listItems, Account_list.this);
                                recyclerView.setAdapter(adapter);
                                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

                                adapter.setData();
                                adapter.notifyDataSetChanged();
                                swipeController0 = new SwipeController0(new SwipeControllerActions() {
                                    @Override
                                    public void onRightClicked(final int position) {
                                    }

                                    @Override
                                    public void onLeftClicked(int position) {
                                        Intent intent = new Intent(getApplicationContext(), Account_edit.class);
                                        intent.putExtra("id_account", adapter.cetak(position));
                                        startActivity(intent);
                                    }
                                });

                                ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeController0);
                                itemTouchhelper.attachToRecyclerView(recyclerView);

                                recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
                                    @Override
                                    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
                                        swipeController0.onDraw(c);
                                    }
                                });
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError e) {
                            progressBar.setVisibility(View.INVISIBLE);
                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
            );
            MySingleton.getInstance(Account_list.this).addToRequestQueue(MyStringRequest);
        }else{
            StringRequest MyStringRequest = new StringRequest(Request.Method.POST, MySingleton.account,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            progressBar.setVisibility(View.INVISIBLE);
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                JSONArray array = jsonObject.getJSONArray("message");
                                listItems.clear();
                                for(int i=0; i<array.length(); i++) {
                                    JSONObject o = array.getJSONObject(i);
                                    ListItem item = new ListItem(
                                            o.getString("id_account"),
                                            o.getString("username"),
                                            o.getString("gambar"),
                                            o.getString("level")
                                    );
                                    listItems.add(item);
                                }
                                adapter = new MyAdapter(listItems, Account_list.this);
                                recyclerView.setAdapter(adapter);
                                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

                                adapter.setData();
                                adapter.notifyDataSetChanged();

                                swipeController = new SwipeController(new SwipeControllerActions() {
                                    @Override
                                    public void onRightClicked(final int position) {
                                        progressBar.setVisibility(View.VISIBLE);
                                        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int choice) {
                                                switch (choice) {
                                                    case DialogInterface.BUTTON_POSITIVE:
                                                        Toast.makeText(getApplicationContext(), "klik kanan ok", Toast.LENGTH_LONG).show();
                                                        StringRequest stringRequest = new StringRequest(Request.Method.POST, MySingleton.account_delete,
                                                                new Response.Listener<String>() {
                                                                    @Override
                                                                    public void onResponse(String response) {
                                                                        progressBar.setVisibility(View.INVISIBLE);                                                                    try {
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
                                                                params.put("id_account", adapter.cetak(position));
                                                                return params;
                                                            }
                                                        };
                                                        MySingleton.getInstance(Account_list.this).addToRequestQueue(stringRequest);
                                                        break;
                                                    case DialogInterface.BUTTON_NEGATIVE:
                                                        adapter.notifyItemRangeChanged(position, adapter.getItemCount());
                                                        break;
                                                }
                                            }
                                        };

                                        AlertDialog.Builder builder = new AlertDialog.Builder(Account_list.this);
                                        builder.setMessage("Delete this image?")
                                                .setPositiveButton("Yes", dialogClickListener)
                                                .setNegativeButton("No", dialogClickListener).show();

                                    }

                                    @Override
                                    public void onLeftClicked(int position) {
                                        Intent intent = new Intent(getApplicationContext(), Account_edit.class);
                                        intent.putExtra("id_account", adapter.cetak(position));
                                        startActivity(intent);
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
                                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError e) {
                            progressBar.setVisibility(View.INVISIBLE);
                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
            );
            MySingleton.getInstance(Account_list.this).addToRequestQueue(MyStringRequest);
        }
    }
}