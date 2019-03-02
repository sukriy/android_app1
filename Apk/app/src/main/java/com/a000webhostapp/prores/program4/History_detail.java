package com.a000webhostapp.prores.program4;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static java.lang.Integer.parseInt;

public class History_detail extends AppCompatActivity {
    Calendar myCalendar;
    DatePickerDialog.OnDateSetListener date;
    TextView tvIdPesan,tvUserPesan, txt_tgl, txt_jam, txt_tmpt;
    Button btn_get_datetime;
    int total;
    Spinner jenis, pmbyrn;
    SharedPrefManager sharedPrefManager;
    String id_pesan;
    ArrayAdapter<String> adapter0, adapter;
    NumberFormat numberFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_detail);
        txt_tgl = (TextView) findViewById(R.id.txt_tgl);
        txt_jam = (TextView) findViewById(R.id.txt_jam);
        txt_tmpt = (TextView) findViewById(R.id.txt_tmpt);
        tvUserPesan = (TextView) findViewById(R.id.tvUserPesan);
        tvIdPesan = (TextView) findViewById(R.id.tvIdPesan);

        numberFormat = NumberFormat.getNumberInstance(Locale.US);

        sharedPrefManager = new SharedPrefManager(History_detail.this);
        if (sharedPrefManager.getSPSudahLogin() == false){
            finish();
        }

        pmbyrn = (Spinner) findViewById(R.id.spinner2);
            String[] opsi0 = {"Cash", "Saldo"};
            adapter0 = new ArrayAdapter<String>(History_detail.this, android.R.layout.simple_spinner_dropdown_item, opsi0);
            pmbyrn.setAdapter(adapter0);

        jenis = (Spinner) findViewById(R.id.spinner);
        String[] opsi = {"Dine in","Take Away"};
        adapter = new ArrayAdapter<String>(History_detail.this, android.R.layout.simple_spinner_dropdown_item, opsi);
        jenis.setAdapter(adapter);
        load_data();
    }
    public void load_data(){
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading Data");
        progressDialog.show();

        Intent intent = getIntent();
        id_pesan = intent.getStringExtra("id_pesan");
        Toast.makeText(History_detail.this, id_pesan, Toast.LENGTH_LONG).show();
        if(!id_pesan.equals("")){
            StringRequest stringRequest = new StringRequest(Request.Method.POST, MySingleton.pesan_list_detail,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            progressDialog.dismiss();
                            System.out.println(response);
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                if(jsonObject.getString("error").equals("false")){
                                    System.out.println( "ok3");
                                    JSONArray data = jsonObject.getJSONArray("message");
                                    System.out.println(data);
                                    System.out.println( "ok4");
                                    TableLayout tl = (TableLayout) findViewById(R.id.table);
                                    for(int i=0; i<data.length(); i++) {
                                        JSONObject o = data.getJSONObject(i);

                                        if(i == 0){
                                            for(int xx=0; xx<adapter0.getCount(); xx++){
                                                System.out.println(pmbyrn.getItemAtPosition(xx));
                                                if(pmbyrn.getItemAtPosition(xx).toString().equalsIgnoreCase(o.getString("pembayaran"))){
                                                    pmbyrn.setSelection(xx);
                                                }
                                            }
                                            System.out.println(adapter.getCount());
                                            System.out.println(pmbyrn);
                                            for(int xx=0; xx<adapter.getCount(); xx++){
                                                System.out.println(pmbyrn.getItemAtPosition(xx));
                                                if(jenis.getItemAtPosition(xx).toString().equalsIgnoreCase(o.getString("pembayaran"))){
                                                    jenis.setSelection(xx);
                                                }
                                            }
                                            String[] splited = o.getString("tgl_input").split(" ");
                                            txt_tgl.setText(splited[0]);
                                            txt_jam.setText(splited[1]);
                                            txt_tmpt.setText(o.getString("meja"));
                                            tvIdPesan.setText(o.getString("id_pesan"));
                                            tvUserPesan.setText(o.getString("nama_lengkap"));
                                        }

                                        TableRow tr = new TableRow(History_detail.this);
                                        tr.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

                                        int no = i+1;
                                        TextView t0 = new TextView(History_detail.this);
                                        t0.setText(String.valueOf(no));
                                        t0.setTextSize(18);
                                        t0.setGravity(Gravity.LEFT | Gravity.BOTTOM);
                                        t0.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                                        tr.addView(t0);

                                        TextView t1 = new TextView(History_detail.this);
                                        t1.setText(o.getString("nama"));
                                        t1.setTextSize(18);
                                        t1.setGravity(Gravity.LEFT | Gravity.BOTTOM);
                                        t1.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                                        tr.addView(t1);

                                        TextView t2 = new TextView(History_detail.this);
                                        t2.setText(o.getString("qty"));
                                        t2.setTextSize(18);
                                        t2.setGravity(Gravity.RIGHT | Gravity.BOTTOM);
                                        t2.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                                        tr.addView(t2);

                                        int total0 = parseInt(o.getString("harga"))*parseInt(o.getString("qty"));
                                        total += total0;
                                        TextView t3 = new TextView(History_detail.this);
                                        t3.setText(String.valueOf(numberFormat.format(total0)));
                                        t3.setTextSize(18);
                                        t3.setGravity(Gravity.RIGHT | Gravity.BOTTOM);
                                        t3.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                                        tr.addView(t3);

                                        tl.addView(tr, new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                                    }
                                    TableRow tr = new TableRow(History_detail.this);
                                    tr.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

                                    TextView t0 = new TextView(History_detail.this);
                                    t0.setText("");
                                    t0.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                                    tr.addView(t0);

                                    TextView t1 = new TextView(History_detail.this);
                                    t1.setText("");
                                    t1.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                                    tr.addView(t1);

                                    TextView t2 = new TextView(History_detail.this);
                                    t2.setText("Total");
                                    t2.setTextSize(18);
                                    t2.setGravity(Gravity.LEFT | Gravity.BOTTOM);
                                    t2.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                                    tr.addView(t2);

                                    TextView t3 = new TextView(History_detail.this);
                                    t3.setText(String.valueOf(numberFormat.format(total)));
                                    t3.setTextSize(18);
                                    t3.setGravity(Gravity.RIGHT | Gravity.BOTTOM);
                                    t3.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                                    tr.addView(t3);

                                    tl.addView(tr, new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

                                }else{
                                    System.out.println( "ok2");
                                    System.out.println(jsonObject.getString("message"));
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                                System.out.println( "ok1");
                                System.out.println( e.getMessage());
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            System.out.println("ok5");
                            System.out.println( error.getMessage());
                        }
                    }
            ) {
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("id_pesan", id_pesan);
                    return params;
                }
            };
            MySingleton.getInstance(History_detail.this).addToRequestQueue(stringRequest);

        }else{
            Toast.makeText(getApplicationContext(), "kosong", Toast.LENGTH_LONG).show();
            finish();
        }
    }
}
