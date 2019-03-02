package com.a000webhostapp.prores.program4;


import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.Calendar;
import java.util.Locale;

import static java.lang.Integer.parseInt;


/**
 * A simple {@link Fragment} subclass.
 */
public class CheckoutFragment extends Fragment {
    static ArrayList<ListOrder> temp = new ArrayList<>();
    Calendar myCalendar;
    DatePickerDialog.OnDateSetListener date;
    EditText txt_tmpt;
    Button btn_get_datetime;
    int total;
    Spinner jenis, pmbyrn;
    SharedPrefManager sharedPrefManager;
    Button meja_1, meja_2;
    ConstraintLayout meja;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_checkout, container, false);
        sharedPrefManager = new SharedPrefManager(getContext());
        NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.US);

        txt_tmpt = (EditText) view.findViewById(R.id.txt_tmpt);
        txt_tmpt.setFocusable(false);

        meja = (ConstraintLayout) view.findViewById(R.id.meja);
        meja_1 = (Button) view.findViewById(R.id.meja_1);
        meja_2 = (Button) view.findViewById(R.id.meja_2);

        load_meja();

        pmbyrn = (Spinner) view.findViewById(R.id.spinner2);
        if(sharedPrefManager.getSpLevel().equalsIgnoreCase("pelanggan")){
            String[] opsi0 = {"Saldo"};
            ArrayAdapter<String> adapter0 = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, opsi0);
            pmbyrn.setAdapter(adapter0);
        }else{
            String[] opsi0 = {"Cash", "Saldo"};
            ArrayAdapter<String> adapter0 = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, opsi0);
            pmbyrn.setAdapter(adapter0);
        }

        jenis = (Spinner) view.findViewById(R.id.spinner);
        String[] opsi = {"Dine in","Take Away"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, opsi);
        jenis.setAdapter(adapter);

        TableLayout tl = (TableLayout) view.findViewById(R.id.table);
        for(int i = 0; i < temp.size(); i++){
            ListOrder listItem = temp.get(i);
            System.out.println(listItem.getId_menu());
            TableRow tr = new TableRow(getContext());
            tr.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

            int no = i+1;
            TextView t0 = new TextView(getContext());
            t0.setText(String.valueOf(no));
            t0.setTextSize(18);
            t0.setGravity(Gravity.LEFT | Gravity.BOTTOM);
            t0.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
            tr.addView(t0);

            TextView t1 = new TextView(getContext());
            t1.setText(listItem.getNama());
            t1.setTextSize(18);
            t1.setGravity(Gravity.LEFT | Gravity.BOTTOM);
            t1.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
            tr.addView(t1);

            TextView t2 = new TextView(getContext());
            t2.setText(listItem.getJmlh());
            t2.setTextSize(18);
            t2.setGravity(Gravity.RIGHT | Gravity.BOTTOM);
            t2.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
            tr.addView(t2);

            int total0 = parseInt(listItem.getHarga())*parseInt(listItem.getJmlh());
            total += total0;

            TextView t3 = new TextView(getContext());
            t3.setText(String.valueOf(numberFormat.format(total0)));
            t3.setTextSize(18);
            t3.setGravity(Gravity.RIGHT | Gravity.BOTTOM);
            t3.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
            tr.addView(t3);

            tl.addView(tr, new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
        }

        TableRow tr = new TableRow(getContext());
        tr.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

        TextView t0 = new TextView(getContext());
        t0.setText("");
        t0.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
        tr.addView(t0);

        TextView t1 = new TextView(getContext());
        t1.setText("");
        t1.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
        tr.addView(t1);

        TextView t2 = new TextView(getContext());
        t2.setText("Total");
        t2.setTextSize(18);
        t2.setGravity(Gravity.LEFT | Gravity.BOTTOM);
        t2.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
        tr.addView(t2);

        TextView t3 = new TextView(getContext());
        t3.setText(String.valueOf(numberFormat.format(total)));
        t3.setTextSize(18);
        t3.setGravity(Gravity.RIGHT | Gravity.BOTTOM);
        t3.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
        tr.addView(t3);

        tl.addView(tr, new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

        return view;
    }
    public void load_meja(){
        RequestQueue mRequestQueue = MySingleton.getInstance(getContext()).getRequestQueue();
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
                                        meja_1.setEnabled(false);
                                        meja_1.setBackgroundColor(Color.RED);
                                    }else if(i == 1){
                                        meja_2.setEnabled(false);
                                        meja_2.setBackgroundColor(Color.RED);
                                    }
                                }else{
                                    if(i == 0){
                                        meja_1.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                meja_1.setBackgroundColor(Color.YELLOW);
                                                meja_2.setBackgroundColor(Color.GREEN);
                                                txt_tmpt.setText("Meja_1");
                                            }
                                        });
                                    }else if(i == 1){
                                        meja_2.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                meja_1.setBackgroundColor(Color.GREEN);
                                                meja_2.setBackgroundColor(Color.YELLOW);
                                                txt_tmpt.setText("Meja_2");
                                            }
                                        });
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
        MySingleton.getInstance(getContext()).addToRequestQueue(stringRequest);

    }
    public String get_jenis(){
        return jenis.getSelectedItem().toString().trim();
    }
    public String get_pmbyrn(){
        return pmbyrn.getSelectedItem().toString().trim();
    }
    public String get_meja(){
        return txt_tmpt.getText().toString().trim();
    }
    public void pass_var(ArrayList<ListOrder> temp) {
        this.temp = temp;
    }

}
