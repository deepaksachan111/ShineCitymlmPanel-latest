package com.lucknow.quaere.shinecity_mlmpanel;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
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
import java.util.HashMap;


public class ChangePasswordFragment extends Fragment {
    private EditText oldpass, newpass, confirmpass;
    private Button submit;
    private SessionManager sessionManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_changepassword, container, false);
        oldpass = (EditText) view.findViewById(R.id.edt_oldpass);
        newpass = (EditText) view.findViewById(R.id.edt_changenewpass);
        confirmpass = (EditText) view.findViewById(R.id.edt_changeconfirmpass);
        submit = (Button) view.findViewById(R.id.btn_change_submit);

        submit.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                String oldp = oldpass.getText().toString();
                String newp = newpass.getText().toString();
                String confirmp = confirmpass.getText().toString();
                if (oldp.length() < 4) {
                    oldpass.setError("Enter old password");
                    oldpass.requestFocus();
                  /*  Toast toast = Toast.makeText(getActivity(), "enter OldPassword", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.TOP, 25, 600);
                    View view1 = toast.getView();
                    view1.setBackgroundResource(R.drawable.toast_drawablecolor);
                    toast.show();*/
                } else if (newp.length() < 4) {
                    newpass.setError("enter newpassword atleast 4 digit");
                    oldpass.setError(null);
                    newpass.requestFocus();
                  /*  Toast toast2 = Toast.makeText(getActivity(), "enter newpassword atleast 4 digit", Toast.LENGTH_LONG);
                    View view1 = toast2.getView();
                    view1.setBackgroundResource(R.drawable.toast_drawablecolor);
                    toast2.setGravity(Gravity.TOP, 25, 600);
                    toast2.show();*/
                } else if (!newp.equals(confirmp)) {
                    confirmpass.setError("newpass & confirmpass not match");
                    oldpass.setError(null);
                    confirmpass.requestFocus();
                   /* Toast toast3 = Toast.makeText(getActivity(), "newpass & confirmpass not match", Toast.LENGTH_LONG);
                    View view1 = toast3.getView();
                    view1.setBackgroundResource(R.drawable.toast_drawablecolor);
                    toast3.setGravity(Gravity.TOP, 25, 600);
                    toast3.show();*/
                } else {
                    sessionManager = new SessionManager(getActivity());
                    HashMap<String, String> user = sessionManager.getUserDetails();

                    String loginid = user.get(SessionManager.KEY_LOGINID);
                    String url = "http://demo8.mlmsoftindia.com/ShinePanel.svc/MLM_MemberPasswordChange/" + loginid + "/" + oldp + "/" + newp;
                    new ChangePasswordAsyncTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, url);
                }
            }
        });
        return view;
    }

    private class ChangePasswordAsyncTask extends AsyncTask<String, Void, String> {
        private static final int REGISTRATION_TIMEOUT = 3 * 1000;
        private static final int WAIT_TIMEOUT = 30 * 1000;
        private final HttpClient httpclient = new DefaultHttpClient();
        final HttpParams params = httpclient.getParams();
        HttpResponse response;
        private String content = null;
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
            Toast toast = Toast.makeText(getActivity(),
                    "Error connecting to Server", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP, 25, 600);
            View view1 = toast.getView();
            view1.setBackgroundResource(R.drawable.toast_drawablecolor);
            toast.show();

        }

        protected void onPostExecute(String content) {
            dialog.dismiss();
            Toast toast;
            if (error) {
                toast = Toast.makeText(getActivity(),
                        content, Toast.LENGTH_LONG);
                toast.setGravity(Gravity.TOP, 25, 600);
                toast.show();
            } else {
                displayCountryList(content);


            }
        }


    }

    private void displayCountryList(String response) {
        if (response != null) {
            try {
                JSONArray jsonArray = new JSONArray(response);
                JSONObject jsonObject = (JSONObject) jsonArray.get(0);
                String rs_status = jsonObject.getString("ResponceStatus");
                String rs_message =jsonObject.getString("ResponceMessage");
                if (rs_status.equals("1")) {

                    sessionManager.logoutUser();
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                    getActivity().finish();
                    Toast toast = Toast.makeText(getActivity(),
                            rs_message, Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.TOP, 25, 600);
                    View view1 = toast.getView();
                    view1.setBackgroundResource(R.drawable.toast_drawablecolor);
                    toast.show();

                } else {
                    Toast toast = Toast.makeText(getActivity(),
                            "Incorrect data", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.TOP, 25, 600);
                    View view1 = toast.getView();
                    view1.setBackgroundResource(R.drawable.toast_drawablecolor);
                    toast.show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            Toast toast = Toast.makeText(getActivity(),
                    "Incorrect Old Password", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP, 25, 600);
            View view1 = toast.getView();
            view1.setBackgroundResource(R.drawable.toast_drawablecolor);
            toast.show();
        }
    }

}
