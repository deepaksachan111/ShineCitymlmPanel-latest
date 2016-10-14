package com.lucknow.quaere.shinecity_mlmpanel;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
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
import java.util.ArrayList;
import java.util.HashMap;

public class EPinRegistrationActivity extends AppCompatActivity {
    private  EditText epincode,referby,mobino, panno,name;
    private TextView tv_valid,tv_parentid ,parentname;
    private  RadioGroup radioGroup;
    private RadioButton radioButton;
    private Spinner spi_name,spi_gender;
    private Button btn_save,btn_reset;
    private String epin;
    private String checkvalue;
    private  String selected_gender, selected_name;
    private  String Parentcode = "";
    private Animation vanish;
    private  String memid;
private  void findviewbyid(){
    epincode =(EditText)findViewById(R.id.et_epincode);
    referby =(EditText)findViewById(R.id.et_epinreferby);
    tv_valid =(TextView)findViewById(R.id.tv_valid);
    radioGroup = (RadioGroup)findViewById(R.id.myRadioGroup_newRegi);
    tv_parentid =(TextView)findViewById(R.id.tv_parent_id_new_reg);
    parentname =(TextView)findViewById(R.id.tv_parent_name_new_reg);
    spi_name =(Spinner)findViewById(R.id.spi_name_new_regi);
    spi_gender =(Spinner)findViewById(R.id.spi_gender_new_regis);
    name =(EditText)findViewById(R.id.edt_name_newreg);
    panno =(EditText)findViewById(R.id.edt_panno_newreg);
    mobino  =(EditText)findViewById(R.id.edt_mobileno_newreg);
    btn_save=(Button)findViewById(R.id.btn_save_newreg);
    btn_reset=(Button)findViewById(R.id.btn_reset_newreg);
}
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_epin_registration);

        vanish = AnimationUtils.loadAnimation(this, R.anim.vanish);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
      //  getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);

        com.lucknow.quaere.shinecity_mlmpanel.SessionManager sessionManager= new com.lucknow.quaere.shinecity_mlmpanel.SessionManager(this);
          final HashMap<String,String >map = sessionManager.getUserDetails();
         memid = map.get(com.lucknow.quaere.shinecity_mlmpanel.SessionManager.KEY_MEMID);
        findviewbyid();
        Intent intent = getIntent();
         epin = intent.getStringExtra("EPIN");
        String s = intent.getStringExtra("OWNER");
        referby.setText(s);
        epincode.setText(epin);
        final int selected = radioGroup.getCheckedRadioButtonId();
        radioButton = (RadioButton) findViewById(selected);
        checkvalue = radioButton.getText().toString();
        if (checkvalue.equals("Left")) {
            checkvalue = "L";
        } else if (checkvalue.equals("Right")) {
            checkvalue = "R";
        }
       /* String url = "http://demo8.mlmsoftindia.com/ShinePanel.svc/MLM_GetMemberDetail/" + epin + "/" + checkvalue + "/" + memid;
        new EPinMemberDetailAsynctask().execute(url);*/

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup arg0, int selectedId) {
                //int selectedId = radioGroup.getCheckedRadioButtonId();
                radioButton = (RadioButton) findViewById(selectedId);
                checkvalue = radioButton.getText().toString();
                if (checkvalue.equals("Left")) {
                    checkvalue = "L";
                } else if (checkvalue.equals("Right")) {
                    checkvalue = "R";
                }
                String url = "http://demo8.mlmsoftindia.com/ShinePanel.svc/MLM_GetMemberDetail/" + epin + "/" + checkvalue + "/" + memid;
                new EPinMemberDetailAsynctask().execute(url);

            }
        });

        epincode.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {

                epin = epincode.getText().toString();
                if (!epin.equals("")) {
                    String url = "http://demo8.mlmsoftindia.com/ShinePanel.svc/ValidateEpinCode/" + epin;
                    new EPincheckketAsynctask().execute(url);
                }

                // epincode.setError("invalid ");
            /*    if(s.toString().length()>6){
                    et.setError("", null);
                }else{
                    et.setError("", errorIcon);
                }*/
            }
        });

        ArrayList<String> name_priflist = new ArrayList<>();
        name_priflist.add("Mr.");
        name_priflist.add("Mrs.");
        name_priflist.add("Ms.");
        name_priflist.add("M/s.");
        name_priflist.add("Dr.");
        name_priflist.add("Md.");

        spi_name.setAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item,
                name_priflist));
        spi_name.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selected_name =spi_name.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ArrayList<String> genderlist = new ArrayList<>();
        genderlist.add("Male");
        genderlist.add("Female");
        spi_gender.setAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item,
                genderlist));
         spi_gender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
             @Override
             public void onItemSelected(AdapterView<?> arg0, View view, int arg2, long arg3) {

                 selected_gender = spi_gender.getSelectedItem().toString();
                 if (selected_gender.equals("Female")) {
                     selected_gender = "F";
                 } else {
                     selected_gender = "M";
                 }

                /* Toast.makeText(getApplicationContext(), selected_val,
                         Toast.LENGTH_SHORT).show();*/
             }

             @Override
             public void onNothingSelected(AdapterView<?> arg0) {
                 // TODO Auto-generated method stub

             }
         });
        if(Parentcode.equals("")){
            if(!epincode.getText().equals("")) {
                String url = "http://demo8.mlmsoftindia.com/ShinePanel.svc/MLM_GetMemberDetail/" + epin + "/" + checkvalue + "/" + memid;
                new EPinMemberDetailAsynctask().execute(url);

            }
        }
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_save.setAnimation(vanish);


                String parentid = Parentcode.toString();
                String logiid = map.get(com.lucknow.quaere.shinecity_mlmpanel.SessionManager.KEY_LOGINID);
                String memeid = map.get(com.lucknow.quaere.shinecity_mlmpanel.SessionManager.KEY_MEMID);
                String epin = epincode.getText().toString();
                String leg = checkvalue.toString();
                String firstname = name.getText().toString().trim();
                String newUrl = firstname.replaceAll(" ", "_");
                String fulln = selected_name+" " + newUrl;
                String fullname = fulln.replace(" ","_");
                String gender = selected_gender;
                String mobile = mobino.getText().toString();
                String pan = panno.getText().toString();

                if (firstname.equals("")) {
                    name.setError("Enter Name");
                    return;
                } else if (mobile.length() != 10) {
                    mobino.setError("Enter only 10 digit Mobileno");
                    return;
                } else if (pan.equals("")) {
                    panno.setError("Enter Panno");
                    return;
                }
                String url = "http://demo8.mlmsoftindia.com/ShinePanel.svc/MLM_RegisterEpin/" + epin + "/" + leg + "/" + fullname + "/" + gender + "/" + mobile + "/" + pan + "/" + logiid + "/" + parentid + "/" + memeid;
                new EPinRegistrationSaveAsynctask().execute(url);
            }
        });
        btn_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name.setText("");
                mobino.setText("");
                panno.setText("");
            }
        });
    }

    private class EPinRegistrationSaveAsynctask extends AsyncTask<String, Void, String> {

        private static final int REGISTRATION_TIMEOUT = 10 * 1000;
        private static final int WAIT_TIMEOUT = 50 * 1000;
        private final HttpClient httpclient = new DefaultHttpClient();
        final HttpParams params = httpclient.getParams();
        HttpResponse response;
        private String content = null;
        private boolean error = false;
        private ProgressDialog dialog =
                new ProgressDialog(EPinRegistrationActivity.this,R.style.ThemeMyDialog);


        protected void onPreExecute() {
            dialog.setMessage("Getting data... Please wait...");
            dialog.setCancelable(true);
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
            Toast toast = Toast.makeText(EPinRegistrationActivity.this,"Connection Server Failed", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP, 25, 500);
            View view1 = toast.getView();
            view1.setBackgroundResource(R.drawable.toast_drawablecolor);
            toast.show();
            finish();
            startActivity(new Intent(EPinRegistrationActivity.this ,HomeActivity.class));
        }

        protected void onPostExecute(String content) {
            dialog.dismiss();
            try {
                JSONArray jArray = new JSONArray(content);
                int count =0;
                for (int i = 0; i < jArray.length(); i++) {
                    if (jArray != null) {
                        count++;
                    }
                    JSONObject jObj = jArray.getJSONObject(i);
                    String responsecode = jObj.getString("ResponseCode");
                    String  responsemessage = jObj.getString("ResponseMessage");
                    String  newagentname = jObj.getString("NewAgentName");
                    String  login_id = jObj.getString("NewLoginID");
                    String  login_pass = jObj.getString("NewPassword");
                    if(responsecode.equals("1")){
                        Toast toast = Toast.makeText(EPinRegistrationActivity.this, responsemessage, Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.TOP, 25, 500);
                        View view1 = toast.getView();
                        view1.setBackgroundResource(R.drawable.toast_drawablecolor);
                        toast.show();
                        epincode.setText("");
                         name.setText("");
                         mobino.setText("");
                         panno.setText("");
                        Intent in = new Intent(EPinRegistrationActivity.this, com.lucknow.quaere.shinecity_mlmpanel.EPinRegisteredSuccess.class);
                        in.putExtra("ANAME",newagentname);
                        in.putExtra("AID",login_id);
                        in.putExtra("APASS",login_pass);
                        startActivity(in);
                    }else if(responsecode.equals("0")){
                        Toast toast = Toast.makeText(EPinRegistrationActivity.this,responsemessage, Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.TOP, 25, 500);
                        View view1 = toast.getView();
                        view1.setBackgroundResource(R.drawable.toast_drawablecolor);
                        toast.show();
                    }else {
                        Toast toast = Toast.makeText(EPinRegistrationActivity.this,responsemessage, Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.TOP, 25, 500);
                        View view1 = toast.getView();
                        view1.setBackgroundResource(R.drawable.toast_drawablecolor);
                        toast.show();
                    }
                }



            } catch (JSONException e) {
                e.printStackTrace();
            }

        }


    }

    private class EPinMemberDetailAsynctask extends AsyncTask<String, Void, String> {

        private static final int REGISTRATION_TIMEOUT = 10 * 1000;
        private static final int WAIT_TIMEOUT = 50 * 1000;
        private final HttpClient httpclient = new DefaultHttpClient();
        final HttpParams params = httpclient.getParams();
        HttpResponse response;
        private String content = null;
        private boolean error = false;
        private ProgressDialog dialog =
                new ProgressDialog(EPinRegistrationActivity.this,R.style.ThemeMyDialog);

        protected void onPreExecute() {
            dialog.setMessage("Getting ... Please wait...");
            dialog.setCancelable(true);
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
            Toast toast = Toast.makeText(EPinRegistrationActivity.this,"Connection Server Failed", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP, 25, 500);
            View view1 = toast.getView();
            view1.setBackgroundResource(R.drawable.toast_drawablecolor);
            toast.show();
            finish();
        }

        protected void onPostExecute(String content) {
            dialog.dismiss();
            String s =content;

            if (content.equals("")) {
               /* Toast  toast2= Toast.makeText(EPinRegistrationActivity.this, "No Record Found", Toast.LENGTH_LONG);
                toast2.setGravity(Gravity.TOP, 25, 500);
                View view1 = toast2.getView();
                view1.setBackgroundResource(R.drawable.toast_drawablecolor);
                toast2.show();*/
            } else {
                try {
                    JSONArray jArray = new JSONArray(content);
                    int count =0;
                    for (int i = 0; i < jArray.length(); i++) {
                        if (jArray != null) {
                            count++;
                        }
                        JSONObject jObj = jArray.getJSONObject(i);
                        Parentcode = jObj.getString("ParentCode");
                        String  Parentid = jObj.getString("ParentID");
                        String ParentName = jObj.getString("ParentName");
                        tv_parentid.setText(Parentcode);
                        parentname.setText("("+ParentName+")");

                    }



                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            }

        }


    private class EPincheckketAsynctask extends AsyncTask<String, Void, String> {

        private static final int REGISTRATION_TIMEOUT = 10 * 1000;
        private static final int WAIT_TIMEOUT = 50 * 1000;
        private final HttpClient httpclient = new DefaultHttpClient();
        final HttpParams params = httpclient.getParams();
        HttpResponse response;
        private String content = null;
        private boolean error = false;
        private ProgressDialog dialog =
                new ProgressDialog(EPinRegistrationActivity.this,R.style.ThemeMyDialog);

        protected void onPreExecute() {
            dialog.setMessage("Getting data... Please wait...");
            dialog.setCancelable(true);
            //  dialog.show();
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
            Toast toast = Toast.makeText(EPinRegistrationActivity.this, "Connection Server Failed", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP, 25, 500);
            View view1 = toast.getView();
            view1.setBackgroundResource(R.drawable.toast_drawablecolor);
            toast.show();
        }

        protected void onPostExecute(String content) {
            dialog.dismiss();
            String s =content;

            if (content.equals("")) {
                Toast  toast2= Toast.makeText(EPinRegistrationActivity.this, "No Record Found", Toast.LENGTH_LONG);
                toast2.setGravity(Gravity.TOP, 25, 500);
                View view1 = toast2.getView();
                view1.setBackgroundResource(R.drawable.toast_drawablecolor);
                toast2.show();
            } else if (s.contains("invalid")) {
                epincode.setError(s);
                tv_valid.setVisibility(View.GONE);
            }else if(s.contains("valid pin")){
                tv_valid.setVisibility(View.VISIBLE);
                epincode.setError(null);
                tv_valid.setText("Valid");
                String url = "http://demo8.mlmsoftindia.com/ShinePanel.svc/MLM_GetMemberDetail/" + epin + "/" + checkvalue + "/" + memid;
                new EPinMemberDetailAsynctask().execute(url);
            }

        }


    }

/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_epin_registration, menu);
        return true;
    }
*/

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
            if(id == android.R.id.home){
                super.onBackPressed();
                return true;
            }


        return super.onOptionsItemSelected(item);
    }
}
