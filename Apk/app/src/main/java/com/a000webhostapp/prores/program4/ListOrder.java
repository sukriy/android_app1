package com.a000webhostapp.prores.program4;

public class ListOrder {
    private String nama, harga, id_menu, jenis, jmlh, imageUrl;

    public ListOrder(String nama, String harga, String id_menu, String jenis, String jmlh, String imageUrl) {
        this.nama = nama;
        this.harga = harga;
        this.id_menu = id_menu;
        this.jenis = jenis;
        this.jmlh = jmlh;
        this.imageUrl = imageUrl;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getHarga() {
        return harga;
    }

    public void setHarga(String harga) {
        this.harga = harga;
    }

    public String getId_menu() {
        return id_menu;
    }

    public void setId_menu(String id_menu) {
        this.id_menu = id_menu;
    }

    public String getJenis() {
        return jenis;
    }

    public void setJenis(String jenis) {
        this.jenis = jenis;
    }

    public String getJmlh() {
        return jmlh;
    }

    public void setJmlh(String jmlh) {
        this.jmlh = jmlh;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
