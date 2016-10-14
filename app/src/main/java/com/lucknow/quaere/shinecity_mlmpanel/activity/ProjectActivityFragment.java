package com.lucknow.quaere.shinecity_mlmpanel.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.lucknow.quaere.shinecity_mlmpanel.R;


/**
 * A placeholder fragment containing a simple view.
 */
public class ProjectActivityFragment extends Fragment implements View.OnClickListener {
private     LinearLayout lko_paradise_garden;
   private LinearLayout lko ,kanpur_linear,linear_kanpur_visibility ,linear_varashi,linear_varanashi_visibility,linear_allahabad,linear_allahabad_visibility;
 private    boolean flag = true;
    private ImageView iv_lucknow_down,iv_lucknow_up ,iv_kanpur_down,iv_kanpur_up,  iv_varanashi_down,iv_varanashi_up,iv_allahabad_down,iv_allahabad_up  ;
    public ProjectActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.fragment_project, container, false);

        lko_paradise_garden=(LinearLayout)v.findViewById(R.id.lko_paradise_garden);

        iv_lucknow_down=(ImageView)v.findViewById(R.id.iv_lucknow_down);
        iv_lucknow_up=(ImageView)v.findViewById(R.id.iv_lucknow_up);
        iv_kanpur_down=(ImageView)v.findViewById(R.id.iv_kanpur_down);
        iv_kanpur_up=(ImageView)v.findViewById(R.id.iv_kanpur_up);
        iv_varanashi_down=(ImageView)v.findViewById(R.id.iv_varanashi_down);
        iv_varanashi_up=(ImageView)v.findViewById(R.id.iv_varanashi_up);
        iv_allahabad_down=(ImageView)v.findViewById(R.id.iv_allahabad_down);
        iv_allahabad_up=(ImageView)v.findViewById(R.id.iv_allahabad_up);

        linear_kanpur_visibility=(LinearLayout)v.findViewById(R.id.linear_kanpur_visibility);
        linear_varanashi_visibility=(LinearLayout)v.findViewById(R.id.linear_varanashi_visibility);
        linear_allahabad_visibility=(LinearLayout)v.findViewById(R.id.linear_allahabad_visibility);



        lko=(LinearLayout)v.findViewById(R.id.lko);
        kanpur_linear=(LinearLayout)v.findViewById(R.id.kanpur_linear);
        linear_varashi=(LinearLayout)v.findViewById(R.id.varanashi_linear);
        linear_allahabad =(LinearLayout)v.findViewById(R.id.allahabad_linear);

        lko.setOnClickListener(this);
        kanpur_linear.setOnClickListener(this);
        linear_varashi.setOnClickListener(this);
        linear_allahabad.setOnClickListener(this);

        LinearLayout back = (LinearLayout)v.findViewById(R.id.back_iv);
        back .setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View v) {
                       getActivity().finish();
                   }
               });

        return v;
    }

    @Override
    public void onClick(View v) {
        if(v == lko){
            if(flag){
                iv_lucknow_down.setVisibility(View.GONE);
                iv_lucknow_up.setVisibility(View.VISIBLE);
                lko_paradise_garden.setVisibility(View.VISIBLE);
                flag= false;
            }else{
                iv_lucknow_up.setVisibility(View.GONE);
                iv_lucknow_down.setVisibility(View.VISIBLE);
                lko_paradise_garden.setVisibility(View.GONE);
                flag= true;
            }


        }

        if(v == kanpur_linear){
            if(flag){
                iv_kanpur_down.setVisibility(View.GONE);
                iv_kanpur_up.setVisibility(View.VISIBLE);
                linear_kanpur_visibility.setVisibility(View.VISIBLE);
                flag= false;
            }else{
                iv_kanpur_up.setVisibility(View.GONE);
                iv_kanpur_down.setVisibility(View.VISIBLE);
                linear_kanpur_visibility.setVisibility(View.GONE);
                flag= true;
            }


        }

        if(v == linear_varashi){
            if(flag){
                iv_varanashi_down.setVisibility(View.GONE);
                iv_varanashi_up.setVisibility(View.VISIBLE);
                linear_varanashi_visibility.setVisibility(View.VISIBLE);
                flag= false;
            }else{
                iv_varanashi_up.setVisibility(View.GONE);
                iv_varanashi_down.setVisibility(View.VISIBLE);
                linear_varanashi_visibility.setVisibility(View.GONE);
                flag= true;
            }


        }

        if(v == linear_allahabad){
            if(flag){
                iv_allahabad_down.setVisibility(View.GONE);
                iv_allahabad_up.setVisibility(View.VISIBLE);
                linear_allahabad_visibility.setVisibility(View.VISIBLE);
                flag= false;
            }else{
                iv_allahabad_up.setVisibility(View.GONE);
                iv_allahabad_down.setVisibility(View.VISIBLE);
                linear_allahabad_visibility.setVisibility(View.GONE);
                flag= true;
            }


        }
    }
}
