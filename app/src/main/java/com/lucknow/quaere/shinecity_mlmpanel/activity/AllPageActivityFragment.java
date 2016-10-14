package com.lucknow.quaere.shinecity_mlmpanel.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.lucknow.quaere.shinecity_mlmpanel.LoginActivity;
import com.lucknow.quaere.shinecity_mlmpanel.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class AllPageActivityFragment extends Fragment {
    RelativeLayout rela_user_login;
    LinearLayout  Linear_aboutus;
    RelativeLayout webView_mission;
    RelativeLayout contact_us;
    RelativeLayout rela_project;
    RelativeLayout business_login;

    public AllPageActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_all_page, container, false);
        rela_user_login=(RelativeLayout)v.findViewById(R.id.rela_user_login);
        Linear_aboutus =(LinearLayout)v.findViewById(R.id.Linear_aboutus);
        webView_mission =(RelativeLayout)v.findViewById(R.id.webView_mission);
        contact_us =(RelativeLayout)v.findViewById(R.id.contact_us);
       // business_login =(RelativeLayout)v.findViewById(R.id.business_login);
        rela_project =(RelativeLayout)v.findViewById(R.id.rela_project);


        rela_user_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), LoginActivity.class));
            }
        });
        Linear_aboutus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), AboutusActivity.class));
            }
        });
        webView_mission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), WebMission99.class));
            }
        });
        contact_us.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), ContactUsActivity.class));
            }
        });

       /* business_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(),"Functionality will be added soon",Toast.LENGTH_LONG).show();
            }
        });*/
        rela_project.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), ProjectActivity.class));
              //  Toast.makeText(getActivity(),"Functionality will be added soon",Toast.LENGTH_LONG).show();
            }
        });

        return v;
    }
}
