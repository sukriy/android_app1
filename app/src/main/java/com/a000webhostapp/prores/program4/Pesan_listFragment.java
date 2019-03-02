package com.a000webhostapp.prores.program4;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
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

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static java.lang.Integer.parseInt;


/**
 * A simple {@link Fragment} subclass.
 */
public class Pesan_listFragment extends Fragment {
    ProgressBar progressBar;
    ArrayList<ListOrder> listItems;
    RecyclerView recyclerView;
    static TextView total;
    private RequestQueue mRequestQueue;
    private AdapterOrder adapter;
    String id_pesan, action;

    public Pesan_listFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pesan_list, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recylerView);
        total = (TextView) view.findViewById(R.id.tvTotal);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar) ;
        progressBar.bringToFront();
        loadRecyclerViewData();
        return  view;
    }
    public ArrayList<ListOrder> tarik_data(){
        return adapter.tarik_data_adapter();
    }
    private void loadRecyclerViewData(){
        listItems = new ArrayList<>();
        progressBar.setVisibility(View.VISIBLE);
        mRequestQueue = MySingleton.getInstance(getContext()).getRequestQueue();

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                MySingleton.menu_pesan,
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
                                ListOrder item = new ListOrder(
                                        o.getString("nama"),
                                        o.getString("harga"),
                                        o.getString("id_menu"),
                                        o.getString("jenis"),
                                        o.getString("jumlah"),
                                        o.getString("gambar")
                                );
                                hitung(o.getString("id_menu"),o.getString("harga"),0,parseInt(o.getString("jumlah")));
                                listItems.add(item);
                            }
                            adapter = new AdapterOrder(listItems, getContext());
                            recyclerView.setAdapter(adapter);
                            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                        } catch (JSONException e) {
                            progressBar.setVisibility(View.INVISIBLE);
                            e.printStackTrace();
                            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError e) {
                        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            ) {
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("id_pesan", id_pesan);
                params.put("action", action);
                return params;
            }
        };
        MySingleton.getInstance(getContext()).addToRequestQueue(stringRequest);
    }
    public static void hitung(String id_menu, String harga, int byk0, int byk) {
        int number = parseInt(total.getText().toString().replace(",",""))+((byk * parseInt(harga)) - (byk0 * parseInt(harga)));
        NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.US);
        String numberAsString = numberFormat.format(number);
        total.setText(numberAsString);
    }
    public void cetak(String id_pesan, String action) {
        this.id_pesan = id_pesan;
        this.action = action;
    }
}
