package com.lucknow.quaere.shinecity_mlmpanel;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.inputmethod.InputMethodManager;

import com.lucknow.quaere.shinecity_mlmpanel.activity.AllPageActivity;

public class DefaultScreenActivity extends Activity {
    private TransparentProgressDialog pd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
     /* requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);*/
        setContentView(R.layout.activity_defaultscreen);
        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
        pd = new TransparentProgressDialog(this, R.drawable.loading_spinner_icon);

     //  pd.show();

        Thread timerthread = new Thread(){
            public  void run(){
                try {
                    sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    startActivity(new Intent(DefaultScreenActivity.this,AllPageActivity.class));
                    finish();
                   // pd.dismiss();
                }
            }

        };timerthread.start();


    }
    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        finish();
    }


}
