package com.a000webhostapp.prores.program4;

import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.smarteist.autoimageslider.SliderLayout;
import com.smarteist.autoimageslider.SliderView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static java.lang.Integer.parseInt;

public class Home extends AppCompatActivity {
    SliderLayout sliderLayout;
    ImageView profpic;
    TextView nama, saldo;
    SharedPrefManager sharedPrefManager;
    private RequestQueue mRequestQueue;
    FragmentManager fm = getSupportFragmentManager();
    Button meja_1, meja_2;
    static int pesan=0;
    static Thread thread;
    TextView notif;
    SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        profpic = (ImageView)findViewById(R.id.prof_pic);
        notif = (TextView)findViewById(R.id.notif);
        nama = (TextView)findViewById(R.id.tvNama);
        saldo = (TextView)findViewById(R.id.tvSaldo);

        sharedPrefManager = new SharedPrefManager(this);

        load_meja();

        if (sharedPrefManager.getSPSudahLogin() == false){
            startActivity(new Intent(getApplicationContext(), Signin.class));
            finish();
        }

        mSwipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swipeRefreshLayout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSwipeRefreshLayout.setRefreshing(true);
                Toast.makeText(getApplicationContext(), "Refresh", Toast.LENGTH_LONG).show();
                load_data();
                load_meja();
                notif();
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });

        load_data();
        sliderLayout = findViewById(R.id.imageSlider);
        sliderLayout.setIndicatorAnimation(SliderLayout.Animations.FILL); //set indicator animation by using SliderLayout.Animations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        sliderLayout.setScrollTimeInSec(4); //set scroll delay in seconds :
        setSliderViews();

        FragmentTransaction ft = fm.beginTransaction();
        if(sharedPrefManager.getSpLevel().equalsIgnoreCase("pelanggan")){
            ft.add(R.id.contain_frag, new UserFragment(), "frag1");
        }else{
            ft.add(R.id.contain_frag, new StaffFragment(), "frag1");
        }
        ft.commit();

        thread = new Thread() {
            @Override
            public void run() {
                try {
                    while(true) {
                        sleep(3500);
                        System.out.println("coba");
                        load_meja();
                        sleep(3500);
                        notif();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start();
    }
    public void load_meja(){
        meja_1 = (Button)findViewById(R.id.meja_1);
        meja_2 = (Button)findViewById(R.id.meja_2);
        meja_1.setEnabled(false);
        meja_2.setEnabled(false);
        mRequestQueue = MySingleton.getInstance(Home.this).getRequestQueue();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, MySingleton.load_meja,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println(response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("message");
                            for(int i=0; i<jsonArray.length(); i++) {
                                JSONObject data = jsonArray.getJSONObject(i);
                                if(data.getString("posisi").toString().equals("1")){
                                    if(i == 0){
                                        meja_1.setBackgroundColor(Color.RED);
                                    }else if(i == 1){
                                        meja_2.setBackgroundColor(Color.RED);
                                    }
                                }else{
                                    if(i == 0){
                                        meja_1.setBackgroundColor(Color.GREEN);
                                    }else if(i == 1){
                                        meja_2.setBackgroundColor(Color.GREEN);
                                    }
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            System.out.println(e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println(error.getMessage());
                    }
                }
        );
        MySingleton.getInstance(Home.this).addToRequestQueue(stringRequest);
    }
    public void notif(){
        RequestQueue mRequestQueue = MySingleton.getInstance(Home.this).getRequestQueue();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, MySingleton.notif,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            System.out.println("notif");
                            System.out.println(response);
                            JSONObject jsonObject = new JSONObject(response);
                            if (!(jsonObject.getString("message").equals(""))) {
                                System.out.println("notif1");

                                if (jsonObject.getString("error").equals("false")) {
                                    String temp1 = sharedPrefManager.getSpNotif().trim();
                                    String temp2 = jsonObject.getString("message").trim();
                                    if((!temp1.equalsIgnoreCase(temp2)) || !temp2.equalsIgnoreCase("")){
                                        sharedPrefManager.saveSPString(SharedPrefManager.SP_NOTIF, temp2);
                                        Toast.makeText(getApplicationContext(), temp2, Toast.LENGTH_SHORT).show();
                                    }
                                    if(!notif.getText().equals(temp2)){
                                        notif.setText(temp2);
                                    }
                                } else {
//                                    Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                                }
                            } else {
//                                Toast.makeText(getApplicationContext(), jsonObject.getString("Bermasalah, Harap coba kembali"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            System.out.println("notif");
                            System.out.println(e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("notif");
                        System.out.println(error.getMessage());
                    }
                }
        ) {
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("id_account", sharedPrefManager.getSPId());
                return params;
            }
        };
        MySingleton.getInstance(Home.this).addToRequestQueue(stringRequest);
    }
    public void load_data(){
        mRequestQueue = MySingleton.getInstance(Home.this).getRequestQueue();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, MySingleton.load_data,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("load_data");
                        System.out.println(response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (!(jsonObject.getString("message").equals(""))) {
                                if (jsonObject.getString("error").equals("false")) {
//                                    Toast.makeText(getApplicationContext(), "Berhasil Login", Toast.LENGTH_SHORT).show();
                                    JSONObject data = jsonObject.getJSONObject("message");
                                    sharedPrefManager.saveSPString(SharedPrefManager.SP_NAMA, data.getString("nama_lengkap"));
                                    sharedPrefManager.saveSPString(SharedPrefManager.SP_LEVEL, data.getString("level"));
                                    sharedPrefManager.saveSPString(SharedPrefManager.SP_GAMBAR, data.getString("gambar"));

                                    NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.US);
                                    sharedPrefManager.saveSPString(SharedPrefManager.SP_SALDO, numberFormat.format(parseInt(data.getString("saldo"))));
                                    pesan = parseInt(data.getString("pesanan"));
                                    cetak();

                                } else {
//                                    Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                                    cetak();
                                }
                            } else {
//                                Toast.makeText(getApplicationContext(), jsonObject.getString("Bermasalah, Harap coba kembali"), Toast.LENGTH_SHORT).show();
                                cetak();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            System.out.println("load_data");
                            System.out.println(e.getMessage());
                            cetak();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println(error.getMessage());
                        cetak();
                    }
                }
        ) {
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("id_account", sharedPrefManager.getSPId());
                return params;
            }
        };
        MySingleton.getInstance(Home.this).addToRequestQueue(stringRequest);
    }
    public void cetak(){
        if(!sharedPrefManager.getSpGambar().toString().trim().equals("")){
            Glide.with(getApplicationContext())
                .load("http://prores.000webhostapp.com/uploads/account/" + sharedPrefManager.getSpGambar().toString().trim())
                .apply(RequestOptions.circleCropTransform())
                .into(profpic);
        }
        nama.setText(sharedPrefManager.getSPNama());
        saldo.setText(sharedPrefManager.getSpSaldo());
    }
    private void setSliderViews() {
        for (int i = 0; i <= 2; i++) {
            SliderView sliderView = new SliderView(this);
            switch (i) {
                case 0:
                    sliderView.setImageUrl("http://prores.000webhostapp.com/uploads/image1.jpeg");
                    break;
                case 1:
                    sliderView.setImageUrl("http://prores.000webhostapp.com/uploads/image2.jpeg");
                    break;
                case 2:
                    sliderView.setImageUrl("http://prores.000webhostapp.com/uploads/image3.jpeg");
                    break;
            }

            sliderView.setImageScaleType(ImageView.ScaleType.CENTER_CROP);
//            sliderView.setDescription("setDescription " + (i + 1));
//            final int finalI = i;
//            sliderView.setOnSliderClickListener(new SliderView.OnSliderClickListener() {
//                @Override
//                public void onSliderClick(SliderView sliderView) {
//                    Toast.makeText(Home.this, "This is slider " + (finalI + 1), Toast.LENGTH_SHORT).show();
//                }
//            });

            //at last add this view in your layout :
            sliderLayout.addSliderView(sliderView);
        }
    }
    boolean doubleBackToExitPressedOnce = false;
    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            System.out.println("if0");
            Home.thread.interrupt();
            super.onBackPressed();
            return;
        }
        System.out.println("if2");
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                System.out.println("if1");
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }
}