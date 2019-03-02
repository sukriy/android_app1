package com.a000webhostapp.prores.program4;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 */
public class StaffFragment extends Fragment {
    CardView account, logout, menu, pesan, topup, history;
    SharedPrefManager sharedPrefManager;

    public StaffFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_staff, container, false);
        account = (CardView) view.findViewById(R.id.Account_card);
        logout = (CardView) view.findViewById(R.id.Logout_card);
        menu = (CardView) view.findViewById(R.id.Menu_card);
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
                startActivity(new Intent(getContext(), HistoryAdmin.class));
            }
        });
        account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), Account_list.class));
            }
        });
        pesan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), Pesan.class));
            }
        });
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), Menu_list.class));
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
