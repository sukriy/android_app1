package com.a000webhostapp.prores.program4;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefManager {
    public static final String SP_MAHASISWA_APP = "spMahasiswaApp";

    public static final String SP_ID = "spId";
    public static final String SP_NAMA = "spNama";
    public static final String SP_LEVEL = "spLevel";
    public static final String SP_GAMBAR = "spGambar";
    public static final String SP_SALDO = "spSaldo";
    public static final String SP_LINK = "spLink";
    public static final String SP_NOTIF = "";

    public static final String SP_SUDAH_LOGIN = "spSudahLogin";

    SharedPreferences sp;
    SharedPreferences.Editor spEditor;

    public SharedPrefManager(Context context){
        sp = context.getSharedPreferences(SP_MAHASISWA_APP, Context.MODE_PRIVATE);
        spEditor = sp.edit();
    }

    public void saveSPString(String keySP, String value){
        spEditor.putString(keySP, value);
        spEditor.commit();
    }

    public void saveSPInt(String keySP, int value){
        spEditor.putInt(keySP, value);
        spEditor.commit();
    }

    public void saveSPBoolean(String keySP, boolean value){
        spEditor.putBoolean(keySP, value);
        spEditor.commit();
    }

    public String getSPId(){
        return sp.getString(SP_ID, "");
    }

    public String getSPNama(){
        return sp.getString(SP_NAMA, "");
    }

    public String getSpLevel(){
        return sp.getString(SP_LEVEL, "");
    }

    public String getSpGambar(){
        return sp.getString(SP_GAMBAR, "");
    }

    public String getSpSaldo(){
        return sp.getString(SP_SALDO, "");
    }

    public String getSpLink(){
        return sp.getString(SP_LINK, "");
    }

    public String getSpNotif(){
        return sp.getString(SP_NOTIF, "");
    }

    public Boolean getSPSudahLogin(){
        return sp.getBoolean(SP_SUDAH_LOGIN, false);
    }
}

