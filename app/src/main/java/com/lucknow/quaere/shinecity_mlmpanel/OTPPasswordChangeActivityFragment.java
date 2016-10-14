package com.lucknow.quaere.shinecity_mlmpanel;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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

/**
 * Created by deepak sachan on 12/7/2015.
 */
public  class OTPPasswordChangeActivityFragment extends Fragment {
    private EditText otp;
    private Button send;
    private   String  loginid ;
    private String getotp;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_otppassword_change, container, false);


        otp = (EditText) v.findViewById(R.id.edt_otp);

        send = (Button) v.findViewById(R.id.otp_send);
        final OTPPasswordChangeActivity otpPasswordChangeActivity = new OTPPasswordChangeActivity();


        send.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                // String loginid = new OTPPasswordChangeActivity.loginData();
                String id = getActivity().getIntent().getStringExtra("loginname");
                getotp = otp.getText().toString();

                //http://demo8.mlmsoftindia.com/ShinePanel.svc/CommanValidateOTP/{LoginId}/{OTP}

                String url = "http://demo8.mlmsoftindia.com/ShinePanel.svc/CommanValidateOTP/" +id + "/" + getotp +"/"+  getActivity().getIntent().getStringExtra("mobile");
                new OTPChangePassAsyncTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, url);
            }
        });
        return v;
    }

    class  OTPChangePassAsyncTask extends AsyncTask<String,Void,String> {
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
            Toast toast = Toast.makeText(getActivity(), "Connection Server Failed", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP, 25, 500);
            View view1 = toast.getView();
            view1.setBackgroundResource(R.drawable.toast_drawablecolor);
            toast.show();
           getActivity(). finish();
        }

        @Override
        protected void onPostExecute(String response) {
            dialog.dismiss();
            super.onPostExecute(response);
            if(response!= null){
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    JSONObject jsonObject = (JSONObject)jsonArray.get(0);
                    String rs_code = jsonObject.getString("MessageCode");
                    if(rs_code.equals("1")){

                        com.lucknow.quaere.shinecity_mlmpanel.OtpSetPasswordFragment otpSetPasswordFragment = new com.lucknow.quaere.shinecity_mlmpanel.OtpSetPasswordFragment();
                        Bundle b = new Bundle();
                        b.putString("id",getActivity().getIntent().getStringExtra("loginname"));
                        b.putString("mobile",getActivity().getIntent().getStringExtra("mobile"));
                        b.putString("otp", getActivity().getIntent().getStringExtra("otp"));
                        otpSetPasswordFragment.setArguments(b);
                        getFragmentManager().beginTransaction().replace(R.id.fragment, otpSetPasswordFragment).commit();
                       // getActivity().finish();

                    }else {
                        otp.setText("");
                        otp.setFocusableInTouchMode(true);
                        otp.requestFocus();
                        Toast toast = Toast.makeText(getActivity(), "Invalid OTP code", Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.TOP, 25, 500);
                        View view1 = toast.getView();
                        view1.setBackgroundResource(R.drawable.toast_drawablecolor);
                        toast.show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }


    }
}