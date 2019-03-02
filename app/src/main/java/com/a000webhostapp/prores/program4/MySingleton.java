package com.a000webhostapp.prores.program4;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.LruCache;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

public class MySingleton {
    public static String base_url = "http://prores.000webhostapp.com/";
    public static String coba = base_url + "coba";
    public static String login = base_url + "login";
    public static String regis = base_url + "regis";
    public static String load_data = base_url + "load_data";
    public static String load_meja = base_url + "load_meja";

    public static String account = base_url + "account";
    public static String account_detail = base_url + "account_detail";
    public static String account_plus = base_url + "account_plus";
    public static String account_delete = base_url + "account_delete";
    public static String account_edit = base_url + "account_edit";

    public static String menu = base_url + "menu";
    public static String menu_detail = base_url + "menu_detail";
    public static String menu_plus = base_url + "menu_plus";
    public static String menu_delete = base_url + "menu_delete";
    public static String menu_edit = base_url + "menu_edit";

    public static String topup = base_url + "topup";
    public static String pesan = base_url + "pesan";
    public static String pesan_list = base_url + "pesan_list";
    public static String pesan_list_detail = base_url + "pesan_list_detail";
    public static String pesan_delete = base_url + "pesan_delete";
    public static String process = base_url + "process";
    public static String pesan_add = base_url + "pesan_add";
    public static String pesan_edit = base_url + "pesan_edit";
    public static String menu_pesan = base_url + "menu_pesan";
    public static String notif = base_url + "notif";


    private static MySingleton mInstance;
    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;
    private static Context mCtx;

    private MySingleton(Context context) {
        mCtx = context;
        mRequestQueue = getRequestQueue();

        mImageLoader = new ImageLoader(mRequestQueue,
                new ImageLoader.ImageCache() {
                    private final LruCache<String, Bitmap>
                            cache = new LruCache<String, Bitmap>(20);

                    @Override
                    public Bitmap getBitmap(String url) {
                        return cache.get(url);
                    }

                    @Override
                    public void putBitmap(String url, Bitmap bitmap) {
                        cache.put(url, bitmap);
                    }
                });
    }

    public static synchronized MySingleton getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new MySingleton(context);
        }
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            mRequestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }

    public ImageLoader getImageLoader() {
        return mImageLoader;
    }
}