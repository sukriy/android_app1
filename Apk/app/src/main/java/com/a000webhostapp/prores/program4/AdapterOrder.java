package com.a000webhostapp.prores.program4;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static java.lang.Integer.parseInt;

public class AdapterOrder extends RecyclerView.Adapter<AdapterOrder.ViewHolder> {

    private List<ListOrder> listItems;
    private Context context;

    public AdapterOrder(List<ListOrder> listItem, Context context) {
        this.listItems = listItem;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_order, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {
        final ListOrder listItem = listItems.get(i);
        viewHolder.id_menu.setText(listItem.getId_menu());
        viewHolder.nama.setText(listItem.getNama());
        viewHolder.jenis.setText(listItem.getJenis());

        int number = Integer.parseInt(listItem.getHarga());
        NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.US);
        String numberAsString = numberFormat.format(number);
        viewHolder.harga.setText(numberAsString);
        viewHolder.jumlah.setText(listItem.getJmlh());
        viewHolder.jumlah.setFocusable(false);

        if(listItem.getImageUrl() != "") {
            Glide.with(context)
                    .load(MySingleton.base_url + "uploads/menu/" + listItem.getImageUrl())
                    .apply(new RequestOptions().override(600, 200))
                    .apply(RequestOptions.skipMemoryCacheOf(true))
                    .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
                    .into(viewHolder.imageView);
        }
        viewHolder.bn_tambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int byk0 = parseInt(viewHolder.jumlah.getText().toString());
                int byk = byk0 + 1;
                System.out.println(byk);

                viewHolder.jumlah.setText(String.valueOf(byk));
                listItem.setJmlh(String.valueOf(byk));
                System.out.println(listItem.getJmlh());
                Pesan_listFragment.hitung(listItem.getId_menu(), listItem.getHarga(), byk0, byk);
            }
        });
        viewHolder.bn_kurang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int byk0 = parseInt(viewHolder.jumlah.getText().toString());
                int byk = byk0 - 1;
                if(byk0 > 0){
                    viewHolder.jumlah.setText(String.valueOf(byk));
                    listItem.setJmlh(String.valueOf(byk));
                    Pesan_listFragment.hitung(listItem.getId_menu(), listItem.getHarga(), byk0, byk);
                }
            }
        });
    }
    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public ArrayList<ListOrder> tarik_data_adapter(){
        ArrayList<ListOrder> temp = new ArrayList<>();
        for(int i = 0; i < getItemCount(); i++){
            ListOrder listItem = listItems.get(i);
            if(parseInt(listItem.getJmlh()) > 0){
                temp.add(listItem);
            }
        }
        return temp;
    }

    public class ViewHolder extends  RecyclerView.ViewHolder{
        public TextView nama, harga, jenis, jumlah, id_menu;
        public Button bn_tambah, bn_kurang;
        public ImageView imageView;
        public ConstraintLayout constraintLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            nama = (TextView) itemView.findViewById(R.id.txtNama);
            id_menu = (TextView) itemView.findViewById(R.id.tvId);
            jumlah = (TextView) itemView.findViewById(R.id.etJmlh);
            harga = (TextView) itemView.findViewById(R.id.txtHarga);
            jenis = (TextView) itemView.findViewById(R.id.txtJenis);
            bn_tambah = (Button) itemView.findViewById(R.id.bn_tambah);
            bn_kurang = (Button) itemView.findViewById(R.id.bn_kurang);
            imageView = (ImageView) itemView.findViewById(R.id.imageView);
            constraintLayout = (ConstraintLayout) itemView.findViewById(R.id.constraintLayout);
        }
    }
}