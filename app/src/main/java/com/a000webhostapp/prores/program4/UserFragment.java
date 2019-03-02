package com.a000webhostapp.prores.program4;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static java.lang.Integer.parseInt;

public class UserFragment extends Fragment {
    CardView account, logout, menu, pesan, topup, history;
    SharedPrefManager sharedPrefManager;

    public UserFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user, container, false);
        logout = (CardView) view.findViewById(R.id.Logout_card);
        pesan = (CardView) view.findViewById(R.id.Pesan_card);
        topup = (CardView) view.findViewById(R.id.Topup_card);
        history = (CardView) view.findViewById(R.id.History_card);
        sharedPrefManager = new SharedPrefManager(getContext());

        topup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), Topup.class));
            }
        });
        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), History.class));
            }
        });
        pesan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RequestQueue mRequestQueue = MySingleton.getInstance(getContext()).getRequestQueue();

                StringRequest stringRequest = new StringRequest(Request.Method.POST, MySingleton.load_data,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    if (!(jsonObject.getString("message").equals(""))) {
                                        if (jsonObject.getString("error").equals("false")) {
                                            JSONObject data = jsonObject.getJSONObject("message");
                                            sharedPrefManager.saveSPString(SharedPrefManager.SP_NAMA, data.getString("nama_lengkap"));
                                            sharedPrefManager.saveSPString(SharedPrefManager.SP_LEVEL, data.getString("level"));
                                            sharedPrefManager.saveSPString(SharedPrefManager.SP_GAMBAR, data.getString("gambar"));

                                            NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.US);
                                            sharedPrefManager.saveSPString(SharedPrefManager.SP_SALDO, numberFormat.format(parseInt(data.getString("saldo"))));
                                            Home.pesan = parseInt(data.getString("pesanan"));

                                            System.out.println(Home.pesan);
                                            if(Home.pesan == 1){
                                                startActivity(new Intent(getContext(), Process.class));
                                            }else{
                                                startActivity(new Intent(getContext(), Pesan.class));
                                            }
                                        } else {
//                                    Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
//                                Toast.makeText(getApplicationContext(), jsonObject.getString("Bermasalah, Harap coba kembali"), Toast.LENGTH_SHORT).show();
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
                ) {
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("id_account", sharedPrefManager.getSPId());
                        return params;
                    }
                };
                MySingleton.getInstance(getContext()).addToRequestQueue(stringRequest);
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharedPrefManager.saveSPBoolean(SharedPrefManager.SP_SUDAH_LOGIN, false);
                startActivity(new Intent(getContext(), Signin.class));
                getActivity().finish();
                Home.thread.interrupt();
            }
        });
        return view;
    }

}
