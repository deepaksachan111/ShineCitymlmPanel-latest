package com.lucknow.quaere.shinecity_mlmpanel;

import android.app.Dialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

import java.io.ByteArrayOutputStream;
import java.io.IOException;


public class OtpSetPasswordFragment extends Fragment {
    private EditText newpass, confirmpass;
    private Button btn_submit;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_otp_set_password, container, false);
        newpass =(EditText)view.findViewById(R.id.edt_newpass);
        confirmpass=(EditText)view.findViewById(R.id.edt_confirmpass);
        btn_submit =(Button)view.findViewById(R.id.otp_set_pass);

        btn_submit.setOnClickListener(new View.OnClickListener() {

            String otp = getArguments().getString("otp");
            String id = getArguments().getString("id");
            String mobile = getArguments().getString("mobile");


            @Override
            public void onClick(View v) {

                if(newpass.getText().toString().length()<4){
                    newpass.setError("password length short");

                }else if(!newpass.getText().toString().equals(confirmpass.getText().toString())){
                    Toast toast = Toast.makeText(getActivity(), "new password or confirmpass not matches", Toast.LENGTH_LONG);

                    toast.setGravity(Gravity.TOP, 25, 500);
                    View view1 = toast.getView();
                    view1.setBackgroundResource(R.drawable.toast_drawablecolor);
                    toast.show();
                }else {

                String url = "http://demo8.mlmsoftindia.com/ShinePanel.svc/CommanPasswordChanged/" + id + "/" + mobile + "/" + otp + "/"+newpass.getText().toString();
                new SetPassword().execute(url);
            }
            }
        });


        return view;
    }

    class SetPassword extends AsyncTask<String, Void, String> {
        private static final int REGISTRATION_TIMEOUT = 10 * 1000;
        private static final int WAIT_TIMEOUT = 50 * 1000;
        private final HttpClient httpclient = new DefaultHttpClient();
        final HttpParams params = httpclient.getParams();
        HttpResponse response;
        private String content = null;
        private boolean error = false;
        private ProgressDialog dialog =
                new ProgressDialog(getActivity());

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
        protected void onPostExecute(String s) {
            dialog.dismiss();
            super.onPostExecute(s);
            if (s != null) {
                Toast toast = Toast.makeText(getActivity(), "Successfully Password Changed", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.TOP, 25, 500);
                View view1 = toast.getView();
                view1.setBackgroundResource(R.drawable.toast_drawablecolor);
                toast.show();
               // forgetPasswordDialog();
                startActivity(new Intent(getActivity(), DefaultScreenActivity.class));
                getActivity().finish();
            }
        }
    }

    public void forgetPasswordDialog() {
//       final String url = "http://demo8.mlmsoftindia.com/ShinePanel.svc/MemberPasswordChange/"+id;
        Button okbtn;
        final Dialog mydialog = new Dialog(getActivity());
        mydialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_box);
        mydialog.requestWindowFeature(Window.FEATURE_LEFT_ICON);
        mydialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mydialog.setCancelable(true);
        mydialog.setTitle(Html.fromHtml("<font color='#FF7F27'>Set IP Address</font>"));
        // mydialog.getWindow().setBackgroundDrawableResource(R.color.blue);
        mydialog.setContentView(R.layout.dialogsuccess);


        okbtn = (Button) mydialog.findViewById(R.id.set_pass_finish);
        ImageView iv = (ImageView) mydialog.findViewById(R.id.close_dialog);

        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mydialog.dismiss();
            }
        });
        okbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }


        });

        mydialog.show();

    }
}
