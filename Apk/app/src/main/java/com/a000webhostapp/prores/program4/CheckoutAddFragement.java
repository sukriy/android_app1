package com.a000webhostapp.prores.program4;


import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.TimePicker;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static java.lang.Integer.parseInt;
import static java.lang.Integer.rotateLeft;


/**
 * A simple {@link Fragment} subclass.
 */
public class CheckoutAddFragement extends Fragment {
    static ArrayList<ListOrder> temp = new ArrayList<>();
    int total;
    SharedPrefManager sharedPrefManager;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_checkout_add_fragement, container, false);
        sharedPrefManager = new SharedPrefManager(getContext());
        NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.US);
        Toast.makeText(getContext(), "CheckoutAddFragment2", Toast.LENGTH_LONG).show();

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
    public void pass_var(ArrayList<ListOrder> temp) {
        this.temp = temp;
    }
}
