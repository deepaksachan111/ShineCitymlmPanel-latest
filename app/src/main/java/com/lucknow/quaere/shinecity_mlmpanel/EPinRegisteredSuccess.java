package com.lucknow.quaere.shinecity_mlmpanel;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class EPinRegisteredSuccess extends AppCompatActivity {
   private TextView tv_name, tv_loginid, tv_pass;
    private Button btn_finish,btn_viewDetail;
    private LinearLayout linearLayout_viewDeatail;
    private  boolean flag = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_epin_registered_success);
   /*     getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);*/
        tv_name =(TextView)findViewById(R.id.tv_registername);
        tv_loginid =(TextView)findViewById(R.id.tv_registerid);
        tv_pass =(TextView)findViewById(R.id.tv_registerpass);
        linearLayout_viewDeatail=(LinearLayout)findViewById(R.id.linear_view_detail);
        btn_finish =(Button)findViewById(R.id.btn_view_finish);
        btn_viewDetail =(Button)findViewById(R.id.btn_view_detail);
        Intent intent = getIntent();
        String name = intent.getStringExtra("ANAME");
        String id = intent.getStringExtra("AID");
        String pass =intent.getStringExtra("APASS");

        tv_name.setText(name);
        tv_loginid.setText(id);
        tv_pass.setText(pass);

        btn_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EPinRegisteredSuccess.this,HomeActivity.class));
            }
        });

        btn_viewDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(flag == true) {
                    linearLayout_viewDeatail.setVisibility(View.VISIBLE);
                    flag = false;
                }else {
                    linearLayout_viewDeatail.setVisibility(View.GONE);
                    flag = true;
                }
            }
        });
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_epin_registered_success, menu);
        return true;
    }*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
      /*   if(id == android.R.id.home){
             startActivity(new Intent(EPinRegisteredSuccess.this,HomeActivity.class));
             return  true;
         }*/
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(EPinRegisteredSuccess.this,HomeActivity.class));
    }
}
