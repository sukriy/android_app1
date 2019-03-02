package com.a000webhostapp.prores.program4;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    RequestQueue mRequestQueue;
    private List<ListItem> listItems;
    private Context context;

    public MyAdapter(List<ListItem> listItem, Context context) {
        this.listItems = listItem;
        this.context = context;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);
        return new ViewHolder(v);
    }

    public String cetak(int i){
        final ListItem listItem = listItems.get(i);
        return listItem.getHead();
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        final ListItem listItem = listItems.get(i);
        viewHolder.textViewHead.setText(listItem.getHead());
        viewHolder.textViewDesc.setText(listItem.getDesc());
        viewHolder.textViewLevel.setText(listItem.getLevel());

        if(listItem.getHead().trim().substring(0,1).equalsIgnoreCase("m")){
            if(listItem.getImageUrl() != "") {
                Glide.with(context)
                        .load(MySingleton.base_url + "uploads/menu/" + listItem.getImageUrl())
                        .apply(RequestOptions.skipMemoryCacheOf(true))
                        .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
                        .into(viewHolder.imageView);
            }
        }else if(listItem.getHead().trim().substring(0,1).equalsIgnoreCase("a")){
            if(listItem.getImageUrl() != "") {
                Glide.with(context)
                        .load(MySingleton.base_url + "uploads/account/" + listItem.getImageUrl())
                        .apply(RequestOptions.skipMemoryCacheOf(true))
                        .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
                        .into(viewHolder.imageView);
            }
        }
    }
    public void setData0(){
        mRequestQueue = MySingleton.getInstance(context).getRequestQueue();
        StringRequest MyStringRequest = new StringRequest(Request.Method.POST, MySingleton.menu, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(!response.toString().trim().equals("")){
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray array = jsonObject.getJSONArray("message");
                        listItems.clear();
                        for(int i=0; i<array.length(); i++) {
                            JSONObject o = array.getJSONObject(i);
                            ListItem item = new ListItem(
                                    o.getString("id_menu"),
                                    o.getString("nama"),
                                    o.getString("gambar"),
                                    o.getString("jenis")
                            );
                            listItems.add(item);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(context, "coba lagi", Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() { //Create an error listener to handle errors appropriately.
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("ok1");
                Toast.makeText(context, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
        MySingleton.getInstance(context).addToRequestQueue(MyStringRequest);
    }
    public void setData() {
        mRequestQueue = MySingleton.getInstance(context).getRequestQueue();

        StringRequest MyStringRequest = new StringRequest(Request.Method.POST, MySingleton.account, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(!response.toString().trim().equals("")){
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
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(context, "coba lagi", Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() { //Create an error listener to handle errors appropriately.
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("ok1");
                Toast.makeText(context, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
        MySingleton.getInstance(context).addToRequestQueue(MyStringRequest);
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public String getnilai(int i){
        final ListItem listItem = listItems.get(i);
        return listItem.getHead();
    }

    public class ViewHolder extends  RecyclerView.ViewHolder{
        public TextView textViewHead;
        public TextView textViewDesc;
        public TextView textViewLevel;
        public ImageView imageView;
        public ConstraintLayout constraintLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewHead = (TextView) itemView.findViewById(R.id.textViewHead);
            textViewDesc = (TextView) itemView.findViewById(R.id.txtNama);
            textViewLevel = (TextView) itemView.findViewById(R.id.textViewLevel);
            imageView = (ImageView) itemView.findViewById(R.id.imageView);
            constraintLayout = (ConstraintLayout) itemView.findViewById(R.id.constraintLayout);
        }
    }
}