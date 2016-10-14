package com.lucknow.quaere.shinecity_mlmpanel;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import java.util.HashMap;


public class LoginActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Boolean isInternetPresent = false;
        com.lucknow.quaere.shinecity_mlmpanel.NetworkConnectionchecker connectionchecker ;
      /*  requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);*/
        setContentView(R.layout.activity_login);
        if(savedInstanceState == null){
            connectionchecker = new com.lucknow.quaere.shinecity_mlmpanel.NetworkConnectionchecker(getApplicationContext());

            isInternetPresent = connectionchecker.isConnectingToInternet();

            // check for Internet status
            if (isInternetPresent) {
                // Internet Connection is Present
                // make HTTP requests
               /* showAlertDialog(LoginActivity.this, "Internet Connection",
                        "You have internet connection", true);*/
                getFragmentManager().beginTransaction().add(R.id.container,new com.lucknow.quaere.shinecity_mlmpanel.MLMLoginFragment()).commit();
                final com.lucknow.quaere.shinecity_mlmpanel.SessionManager session = new com.lucknow.quaere.shinecity_mlmpanel.SessionManager(this);
                HashMap<String, String> user = session.getUserDetails();
                // name
                String name = user.get(com.lucknow.quaere.shinecity_mlmpanel.SessionManager.KEY_USERNAME);
                if(name != null){
                    startActivity(new Intent(this, com.lucknow.quaere.shinecity_mlmpanel.HomeActivity.class));
                    finish();
                }else {

                    getFragmentManager().beginTransaction().replace(R.id.container, new com.lucknow.quaere.shinecity_mlmpanel.MLMLoginFragment()).addToBackStack(null).commit();
                }

            } else {
                // Internet connection is not present
                // Ask user to connect to Internet
               showAlertDialog(LoginActivity.this, "No Internet Connection", "You don't have internet connection.", false);

            }


        }
    }
    public void showAlertDialog(Context context, String title, String message, Boolean status) {
        Dialog dialog;

        final AlertDialog.Builder alertbuider = new AlertDialog.Builder(this);
       // AlertDialog alertDialog = AlertDialog.Builder(context).create();

        // Setting Dialog Title
        alertbuider.setTitle(title);

        // Setting Dialog Message
        alertbuider.setMessage(message);

        // Setting alert dialog icon
        alertbuider.setIcon((status) ? R.drawable.success : R.drawable.fail);

        // Setting OK Button
        alertbuider.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
       /* alertbuider.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
            }
        });*/
        AlertDialog alertDialog = alertbuider.create();
        dialog = alertDialog;
        // Showing Alert Message
        dialog.show();
    }
    @Override
    public void onBackPressed() {
       super.onBackPressed();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
// dont call **super**, if u want disable back button in current screen.
    }

}
