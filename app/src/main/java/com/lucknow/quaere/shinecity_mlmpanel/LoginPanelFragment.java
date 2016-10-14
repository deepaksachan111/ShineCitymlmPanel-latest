package com.lucknow.quaere.shinecity_mlmpanel;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import java.util.HashMap;


public class LoginPanelFragment extends Fragment {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.frament_loginpanel, container, false);
        final FrameLayout frameLayoutmlm =(FrameLayout)view.findViewById(R.id.mlmpanellogin);

       final SessionManager session = new SessionManager(getActivity());

        frameLayoutmlm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String, String> user = session.getUserDetails();
                // name
                String name = user.get(SessionManager.KEY_USERNAME);
                if(name != null){
                   startActivity(new Intent(getActivity(),HomeActivity.class));
                    getActivity().finish();
                }else {

                    getFragmentManager().beginTransaction().replace(R.id.container, new MLMLoginFragment()).addToBackStack(null).commit();
                }
            }
        });
                return  view;
    }


}
