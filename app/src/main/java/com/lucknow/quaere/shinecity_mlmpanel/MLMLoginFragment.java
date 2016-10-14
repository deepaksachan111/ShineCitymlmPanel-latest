package com.lucknow.quaere.shinecity_mlmpanel;


import android.app.Dialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class MLMLoginFragment extends Fragment {
    @InjectView(R.id.input_email)
    EditText edt_username;
    @InjectView(R.id.input_password)
    EditText edtpassword;
    @InjectView(R.id.btn_login)
    Button _loginButton;
    SessionManager session;
    private LinearLayout lin_forgetpassword;
    private  String username,loginid,memid,password;

    private SharedPreferences loginPreferences;
    private SharedPreferences.Editor loginPrefsEditor;

    private Boolean saveLogin;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_mlmlogin, container, false);
        ButterKnife.inject(getActivity());

        session = new SessionManager(getActivity());
        lin_forgetpassword =(LinearLayout)view.findViewById(R.id.tv_forget_password);

        TextView tv_user_user=(TextView)view.findViewById(R.id.tv_useruser);
        Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), "Android Insomnia Regular.ttf");
        tv_user_user.setTypeface(tf);
        edt_username = (EditText) view.findViewById(R.id.input_email);
        edtpassword = (EditText) view.findViewById(R.id.input_password);
        edt_username.clearComposingText();
        edt_username.requestFocus();


        InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);

       final CheckBox ch=(CheckBox)view.findViewById(R.id.ch_rememberme);
        loginPreferences = getActivity().getSharedPreferences("loginPrefs", Context.MODE_PRIVATE);
        loginPrefsEditor = loginPreferences.edit();
        saveLogin = loginPreferences.getBoolean("saveLogin", false);
        if (saveLogin == true) {
            edt_username.setText(loginPreferences.getString("username", ""));
            edtpassword.setText(loginPreferences.getString("password", ""));
            ch.setChecked(true);
        }



        _loginButton = (Button) view.findViewById(R.id.btn_login);

        _loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                validate();




                username = edt_username.getText().toString();
                password = edtpassword.getText().toString();

                if (ch.isChecked()) {
                    loginPrefsEditor.putBoolean("saveLogin", true);
                    loginPrefsEditor.putString("username", username);
                    loginPrefsEditor.putString("password", password);
                    loginPrefsEditor.commit();
                    InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(edt_username.getWindowToken(), 0);
                } else {
                    loginPrefsEditor.clear();
                    loginPrefsEditor.commit();
                }
            }

        });
        lin_forgetpassword.setOnClickListener(new View.OnClickListener() {
       @Override
       public void onClick(View v) {
           forgetPasswordDialog();
       }
   });

        return view;

    }


    public void validate() {
        boolean valid = true;

        String email = edt_username.getText().toString();
      password = edtpassword.getText().toString();

        if (email.isEmpty() || email.length() < 4) {
            edt_username.setError("enter a valid User Name");

        } else if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
                edtpassword.setError("between 4 and 10 alphanumeric characters");

       }else {

            String url = "http://demo8.mlmsoftindia.com/ShinePanel.svc/shinecitymlm/"+email + "/"+password;
            new LoginFragmentAsynktask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,url);

        }

    }


    private class LoginFragmentAsynktask extends AsyncTask<String,Void,String>{

        private static final int REGISTRATION_TIMEOUT = 10 * 1000;
        private static final int WAIT_TIMEOUT = 50 * 1000;
        private final HttpClient httpclient = new DefaultHttpClient();
        final HttpParams params = httpclient.getParams();
        HttpResponse response;
        private String content =  null;
        private boolean error = false;
        private ProgressDialog dialog =
                new ProgressDialog(getActivity(),R.style.ThemeMyDialog);

        protected void onPreExecute() {
            dialog.setMessage("Getting your data... Please wait...");
            dialog.show();
        }

        protected String doInBackground(String... urls) {

            String URL = null;

            try {

                URL = urls[0];
                HttpConnectionParams.setConnectionTimeout(params, REGISTRATION_TIMEOUT);
                HttpConnectionParams.setSoTimeout(params, WAIT_TIMEOUT);
                ConnManagerParams.setTimeout(params, WAIT_TIMEOUT);

                HttpGet httpGet = new HttpGet(URL);

             /*   //add name value pair for the country code
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
                nameValuePairs.add(new BasicNameValuePair("start",String.valueOf(start)));
                nameValuePairs.add(new BasicNameValuePair("limit",String.valueOf(limit)));
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));*/
                response = httpclient.execute(httpGet);

                StatusLine statusLine = response.getStatusLine();
                if(statusLine.getStatusCode() == HttpStatus.SC_OK){
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    response.getEntity().writeTo(out);
                    out.close();
                    content = out.toString();
                } else{
                    //Closes the connection.
                    Log.w("HTTP1:", statusLine.getReasonPhrase());
                    response.getEntity().getContent().close();
                    throw new IOException(statusLine.getReasonPhrase());
                }
            } catch (ClientProtocolException e) {
                Log.w("HTTP2:",e );
                content = e.getMessage();
                error = true;
                cancel(true);
            } catch (IOException e) {
                Log.w("HTTP3:",e );
                content = e.getMessage();
                error = true;
                cancel(true);
            }catch (Exception e) {
                Log.w("HTTP4:",e );
                content = e.getMessage();
                error = true;
                cancel(true);
            }

            return content;
        }

        protected void onCancelled() {
            dialog.dismiss();
            Toast  toast2= Toast.makeText(getActivity(), "Error connecting server", Toast.LENGTH_LONG);
            toast2.setGravity(Gravity.TOP, 25, 500);
            View view1 = toast2.getView();
            view1.setBackgroundResource(R.drawable.toast_drawablecolor);
            toast2.show();


        }

        protected void onPostExecute(String content) {
            dialog.dismiss();
            Toast toast;
            if (error) {

                Toast  toast2= Toast.makeText(getActivity(), content, Toast.LENGTH_LONG);
                toast2.setGravity(Gravity.TOP, 25, 500);
                View view1 = toast2.getView();
                view1.setBackgroundResource(R.drawable.toast_drawablecolor);
                toast2.show();

            }   else if (content.equals("[]")) {
                Toast  toast2= Toast.makeText(getActivity(),  "Server not Responding", Toast.LENGTH_LONG);
                toast2.setGravity(Gravity.TOP, 25, 500);
                View view1 = toast2.getView();
                view1.setBackgroundResource(R.drawable.toast_drawablecolor);
                toast2.show();

            }else if(content.equals("")) {
                Toast  toast2= Toast.makeText(getActivity(),  "Server not Responding", Toast.LENGTH_LONG);
                toast2.setGravity(Gravity.TOP, 25, 500);
                View view1 = toast2.getView();
                view1.setBackgroundResource(R.drawable.toast_drawablecolor);
                toast2.show();
            }
            else if(content !=null) {
               displayCountryList(content);
            /*    Intent intent = new Intent(getActivity(), CardLogin.class);
               *//**//* intent.putExtra("DISPLAYNAME",Displayname);
                intent.putExtra("MEMBERID", MemberID);*//**//*
                startActivity(intent);
                Toast toast2 =Toast.makeText(getActivity().getApplicationContext(), "Welcome  "+ Displayname, Toast.LENGTH_SHORT);
                View tosvie= toast2.getView();
                tosvie.setBackgroundResource(R.drawable.toast_drawablecolor);
                toast2.setGravity(Gravity.TOP, 25, 500);
                toast2.show();
                getActivity().finish();*/


            }

        }
        private void displayCountryList(String response){
            String responsecode ="";
            try {
                JSONArray jArray = new JSONArray(response);

                for (int i = 0; i < jArray.length(); i++)
                {

                    JSONObject jObj = jArray.getJSONObject(i);
                    username = jObj.getString("FirstName");
                    memid = jObj.getString("MemID");
                    loginid = jObj.getString("LoginID");
                     responsecode = jObj.getString("ResponseCode");
                    String responsemessage = jObj.getString("ResponseMessage");
                    String usertype = jObj.getString("UserType");

                    session.createLoginSession(username, memid,loginid,password);
                  //  DbHandler.dbHandler.saveCardProfile(new CardProfile(cardno,Displayname, totalbalance,redemac,balance,MemberID));
                }

             if(responsecode.contains("1")){


                 Toast  toast2= Toast.makeText(getActivity(),"Welcome "+ username,Toast.LENGTH_LONG);
                 toast2.setGravity(Gravity.TOP, 25, 500);
                 View view1 = toast2.getView();
                 view1.setBackgroundResource(R.drawable.toast_drawablecolor);
                 toast2.show();

                 getActivity().startActivity(new Intent(getActivity(),HomeActivity.class));
                 getActivity().finish();

             }
                if(responsecode.contains("0")){

                    Toast  toast2= Toast.makeText(getActivity(),  "invalid user name or password", Toast.LENGTH_LONG);
                    toast2.setGravity(Gravity.TOP, 25, 500);
                    View view1 = toast2.getView();
                    view1.setBackgroundResource(R.drawable.toast_drawablecolor);
                    toast2.show();


                }

            }
            catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
    public void forgetPasswordDialog() {
//       final String url = "http://demo8.mlmsoftindia.com/ShinePanel.svc/MemberPasswordChange/"+id;

        final EditText loinid, mobileno;
        Button okbtn;
        final Dialog dialog = new Dialog(getActivity());
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_box);
        dialog.requestWindowFeature(Window.FEATURE_LEFT_ICON);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);



        dialog.setCancelable(true);
       dialog.setTitle(Html.fromHtml("<font color='#FF7F27'>Forget Password</font>"));
        // dialog.getWindow().setBackgroundDrawableResource(R.color.blue);
        dialog.setContentView(R.layout.forgetpassworddialog);

        // dialog.setTitle("Forget Password");


        loinid = (EditText) dialog.findViewById(R.id.editTxt_forgetpassloginid_dialog);
        mobileno = (EditText) dialog.findViewById(R.id.editTxt_forgetpassmobileno_dialog);
        okbtn = (Button) dialog.findViewById(R.id.forgetsetPasswordBtn);
        ImageView iv = (ImageView)dialog.findViewById(R.id.close_dialog);

        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        okbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginid = loinid.getText().toString();

                // forgetlogin.setText("");
                String mobilenono = mobileno.getText().toString();


             /*    SessionManager sessionManager = new SessionManager(getActivity());
                HashMap<String, String > map = sessionManager.getUserDetails();
                loginid = map.get(SessionManager.KEY_LOGINID);*/
                    if (loginid.length() > 3 && mobileno.length()==10) {
                        String url = "http://demo8.mlmsoftindia.com/ShinePanel.svc/CommanRequestPassword/"+loginid+"/"+mobilenono;


                        new ForgetpassdialogAsyncTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,url);
                      /*  ForgetPasswordActivity forgetPasswordActivity = new ForgetPasswordActivity();
                        forgetPasswordActivity.new ForgetAsyncTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,loginid,mobilenono);*/
                        // new ForgetAsyncTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, loginid, mobileno);
                        dialog.dismiss();
                    } /*else if (loginid.length() == 16 && mobileno.length()==10)
                    {

                    }*/
                    else {
                        Toast toast = Toast.makeText(getActivity(), "enter correct loginid or mobileno.", Toast.LENGTH_LONG);
                        View tostview = toast.getView();
                        tostview.setBackgroundResource(R.drawable.toast_drawablecolor);
                        toast.setGravity(Gravity.TOP, 25, 500);
                        toast.show();
                    }
                }



        });


        dialog.show();

    }

    class ForgetpassdialogAsyncTask extends AsyncTask<String, Void, String> {

          //  String Url = "http://demo8.mlmsoftindia.com/ShinePanel.svc/ForgetPassword/" + params[0] + "/" + params[1];
            private static final int REGISTRATION_TIMEOUT = 10 * 1000;
            private static final int WAIT_TIMEOUT = 50 * 1000;
            private final HttpClient httpclient = new DefaultHttpClient();
            final HttpParams params = httpclient.getParams();
            HttpResponse response;
            private String content = null;
            private boolean error = false;
            private ProgressDialog dialog =
                    new ProgressDialog(getActivity(),R.style.ThemeMyDialog);



        protected void onPreExecute() {
            dialog.setMessage("Getting data... Please wait...");
            dialog.setCancelable(false);
            dialog.show();



        }

        protected String doInBackground(String... urls) {

            String URL = null;

            try {

                URL = urls[0];
                HttpConnectionParams.setConnectionTimeout(params, REGISTRATION_TIMEOUT);
                HttpConnectionParams.setSoTimeout(params, WAIT_TIMEOUT);
                ConnManagerParams.setTimeout(params, WAIT_TIMEOUT);

                HttpPost httpPost = new HttpPost(URL);

             /*   //add name value pair for the country code
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
                nameValuePairs.add(new BasicNameValuePair("start",String.valueOf(start)));
                nameValuePairs.add(new BasicNameValuePair("limit",String.valueOf(limit)));
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));*/
                response = httpclient.execute(httpPost);

                StatusLine statusLine = response.getStatusLine();
                if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    response.getEntity().writeTo(out);
                    out.close();
                    content = out.toString();
                } else {
                    //Closes the connection.
                    Log.w("HTTP1:", statusLine.getReasonPhrase());
                    response.getEntity().getContent().close();
                    throw new IOException(statusLine.getReasonPhrase());
                }
            } catch (ClientProtocolException e) {
                Log.w("HTTP2:", e);
                content = e.getMessage();
                error = true;
                cancel(true);
            } catch (IOException e) {
                Log.w("HTTP3:", e);
                content = e.getMessage();
                error = true;
                cancel(true);
            } catch (Exception e) {
                Log.w("HTTP4:", e);
                content = e.getMessage();
                error = true;
                cancel(true);
            }

            return content;
        }

        protected void onCancelled() {
            dialog.dismiss();
            Toast toast = Toast.makeText(getActivity(), "Connection Server Failed", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP, 25, 500);
            View view1 = toast.getView();
            view1.setBackgroundResource(R.drawable.toast_drawablecolor);
            toast.show();
            getActivity().finish();
        }

        @Override
        protected void onPostExecute(String response) {
            super.onPostExecute(response);
            dialog.dismiss();
            if (response != null) {

                try {
                    JSONArray jsonArray = new JSONArray(response);
                    JSONObject jsonObject1 = (JSONObject) jsonArray.get(0);
                    String code = jsonObject1.getString("MessageCode");
                    String Mobile = jsonObject1.getString("Mobile");
                    String OTP = jsonObject1.getString("OTP");
                    String LoginID = jsonObject1.getString("LoginID");
                  //  [{"LoginID":"axl419","MessageCode":"1","MessageText":"Valid Details","Mobile":"7388870003","OTP":"1844","Password":null}]
                    if (code.equals("1")) {
                        Intent intent = new Intent(getActivity(),OTPPasswordChangeActivity.class);
                        intent.putExtra("loginname", loginid);
                        intent.putExtra("mobile", Mobile);
                        intent.putExtra("otp", OTP);
                        startActivity(intent);
                        Toast toast = Toast.makeText(getActivity(), "Send OTP on your mobile.", Toast.LENGTH_LONG);

                        View tostview = toast.getView();
                        tostview.setBackgroundResource(R.drawable.toast_drawablecolor);
                        toast.setGravity(Gravity.TOP, 25, 500);
                        toast.show();
                    } else {
                        Toast toast = Toast.makeText(getActivity(), "invalid loginid or mobileno.", Toast.LENGTH_LONG);
                        View tostview = toast.getView();
                        tostview.setBackgroundResource(R.drawable.toast_drawablecolor);
                        toast.setGravity(Gravity.TOP, 25, 500);
                        toast.show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else {

                Toast toast= Toast.makeText(getActivity(), "invalid loginid or mobileno.", Toast.LENGTH_LONG);
                View tostview = toast.getView();
                tostview.setBackgroundResource(R.drawable.toast_drawablecolor);
                toast.setGravity(Gravity.TOP, 25, 500);
                toast.show();
            }
        }
    }

}