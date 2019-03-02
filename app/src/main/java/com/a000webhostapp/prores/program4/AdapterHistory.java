package com.a000webhostapp.prores.program4;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdapterHistory extends RecyclerView.Adapter<AdapterHistory.ViewHolder> {
    private List<ListHistory> listItems;
    private Context context;
    RequestQueue mRequestQueue;
    SharedPrefManager sharedPrefManager;

    public AdapterHistory(List<ListHistory> listItem, Context context) {
        this.listItems = listItem;
        this.context = context;
    }

    @Override
    public AdapterHistory.ViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_history, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {
        final ListHistory listItem = listItems.get(i);
        viewHolder.id_menu.setText(listItem.getId_order());
        viewHolder.tgl.setText(listItem.getTgl());
        viewHolder.total.setText(listItem.getTotal());
        viewHolder.nama_lengkap.setText(listItem.getNama_lengkap());

        viewHolder.constraintLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
//                System.out.println("Long press");
                Intent intent = new Intent(context, History_detail.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("id_pesan", listItem.getId_order());
                context.startActivity(intent);
                return true;
            }
        });
    }
    public String cetak(int i){
        final ListHistory listItem = listItems.get(i);
        return listItem.getId_order();
    }
    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public void setData() {
        mRequestQueue = MySingleton.getInstance(context).getRequestQueue();
        sharedPrefManager = new SharedPrefManager(context);

        StringRequest MyStringRequest = new StringRequest(Request.Method.POST, MySingleton.pesan_list,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println(response);
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
                        } catch (JSONException e) {
                            e.printStackTrace();
                            System.out.println(e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError e) {
                        System.out.println(e.getMessage());
                    }
                }){
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> MyData = new HashMap<String, String>();
                MyData.put("id_account", sharedPrefManager.getSPId()); //Add the data you'd like to send to the server.
                return MyData;
            }
        };
        MySingleton.getInstance(context).addToRequestQueue(MyStringRequest);
    }
    public void setData0() {
        mRequestQueue = MySingleton.getInstance(context).getRequestQueue();
        sharedPrefManager = new SharedPrefManager(context);

        StringRequest MyStringRequest = new StringRequest(Request.Method.POST, MySingleton.process,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println(response);
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
                        } catch (JSONException e) {
                            e.printStackTrace();
                            System.out.println(e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError e) {
                        System.out.println(e.getMessage());
                    }
                }){
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> MyData = new HashMap<String, String>();
                MyData.put("id_account", sharedPrefManager.getSPId()); //Add the data you'd like to send to the server.
                return MyData;
            }
        };
        MySingleton.getInstance(context).addToRequestQueue(MyStringRequest);
    }


    public class ViewHolder extends  RecyclerView.ViewHolder{
        public TextView id_menu, tgl, total, nama_lengkap;
        public ConstraintLayout constraintLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            id_menu = (TextView) itemView.findViewById(R.id.txtId);
            tgl = (TextView) itemView.findViewById(R.id.txtTgl);
            total = (TextView) itemView.findViewById(R.id.txtTotal);
            nama_lengkap = (TextView) itemView.findViewById(R.id.txtUser);
            constraintLayout = (ConstraintLayout) itemView.findViewById(R.id.contrain);
        }
    }
}