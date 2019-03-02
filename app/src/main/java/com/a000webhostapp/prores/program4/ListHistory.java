package com.a000webhostapp.prores.program4;

public class ListHistory {
    private String id_order, tgl, total, nama_lengkap;

    public String getId_order() {
        return id_order;
    }

    public String getTgl() {
        return tgl;
    }

    public String getTotal() {
        return total;
    }

    public String getNama_lengkap() {
        return nama_lengkap;
    }

    public ListHistory(String id_order, String tgl, String total, String nama_lengkap) {

        this.id_order = id_order;
        this.tgl = tgl;
        this.total = total;
        this.nama_lengkap = nama_lengkap;
    }
}
