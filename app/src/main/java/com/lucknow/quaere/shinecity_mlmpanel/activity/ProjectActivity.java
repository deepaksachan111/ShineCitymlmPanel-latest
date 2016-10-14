package com.lucknow.quaere.shinecity_mlmpanel.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.lucknow.quaere.shinecity_mlmpanel.R;


public class ProjectActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project);

        if(savedInstanceState == null){
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_projectactivity,new com.lucknow.quaere.shinecity_mlmpanel.activity.ProjectActivityFragment()).commit();
        }


    }





}
