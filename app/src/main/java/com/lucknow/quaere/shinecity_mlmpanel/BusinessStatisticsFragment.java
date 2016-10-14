package com.lucknow.quaere.shinecity_mlmpanel;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
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
import java.util.HashMap;


public class BusinessStatisticsFragment extends Fragment {
    private TextView id, name, sponsorid, sponsorname, panno, joiningdate, accono, bankname, branchname, ifsccode, PermanentTotalLeftall,
            PermanentTotalRightall, BinaryLeftmatched, BinaryRightmatched, BalanceLeft, BalanceRight, TotalLeft, TotalRight;
    private  TextView leftallold, rightallold, leftbalanceold,rightbalancedold,leftmatchedold,rightmatchedold;
     private TextView harmony,harmonyallotment,direct,total,tds,hs,totaldedction,netamount;
    private LinearLayout linearpayout;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    private void findviewbyid(View view) {

        id = (TextView) view.findViewById(R.id.tv_busistatics_id);
        name = (TextView) view.findViewById(R.id.tv_busistatics_name);
        sponsorid = (TextView) view.findViewById(R.id.tv_busistatics_sponsorid);
        sponsorname = (TextView) view.findViewById(R.id.tv_busistatics_sponsorname);
        panno = (TextView) view.findViewById(R.id.tv_busistatics_panno);
        joiningdate = (TextView) view.findViewById(R.id.tv_busistatics_joiningdate);
        accono = (TextView) view.findViewById(R.id.tv_busistatics_accountnumber);
        bankname = (TextView) view.findViewById(R.id.tv_busistatics_bankname);
        branchname = (TextView) view.findViewById(R.id.tv_busistatics_branchname);
        ifsccode = (TextView) view.findViewById(R.id.tv_busistatics_ifsccode);
        PermanentTotalLeftall = (TextView) view.findViewById(R.id.tv_busistatics_leftall);
        PermanentTotalRightall = (TextView) view.findViewById(R.id.tv_busistatics_rightall);
        BinaryLeftmatched = (TextView) view.findViewById(R.id.tv_busistatics_leftmatched);
        BinaryRightmatched = (TextView) view.findViewById(R.id.tv_busistatics_rightmatched);
        BalanceLeft = (TextView) view.findViewById(R.id.tv_busistatics_leftbalanced);
        BalanceRight = (TextView) view.findViewById(R.id.tv_busistatics_rightbalanced);
        TotalLeft = (TextView) view.findViewById(R.id.tv_busistatics_total_leftteam);
        TotalRight = (TextView) view.findViewById(R.id.tv_busistatics_total_rightteam);
        leftallold  = (TextView) view.findViewById(R.id.tv_busistatics_leftallold);
        rightallold  = (TextView) view.findViewById(R.id.tv_busistatics_rightallold);
        leftbalanceold  = (TextView) view.findViewById(R.id.tv_busistatics_leftbalancedold);
        rightbalancedold  = (TextView) view.findViewById(R.id.tv_busistatics_rightbalancedold);
        leftmatchedold  = (TextView) view.findViewById(R.id.tv_busistatics_leftmatchedold);
        rightmatchedold  = (TextView) view.findViewById(R.id.tv_busistatics_rightmatchedold);

        // textview id for business satistics payout detail
        linearpayout =(LinearLayout)view.findViewById(R.id.linear_business_satistics_payout);
        harmony = (TextView) view.findViewById(R.id.businesssatistics_pay_harmony);
        harmonyallotment = (TextView) view.findViewById(R.id.businesssatistics_pay_harmony_allot);
        direct = (TextView) view.findViewById(R.id.businesssatistics_pay_direct);
        total = (TextView) view.findViewById(R.id.businesssatistics_pay_total);
        tds = (TextView) view.findViewById(R.id.businesssatistics_pay_tds);
        hs = (TextView) view.findViewById(R.id.businesssatistics_pay_hs);
        totaldedction = (TextView) view.findViewById(R.id.businesssatistics_pay_totaldedction);
        netamount = (TextView) view.findViewById(R.id.businesssatistics_pay_netamount);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_business_statistics, container, false);
        findviewbyid(v);
        SessionManager sessionManager = new SessionManager(getActivity());
        HashMap<String, String> map = sessionManager.getUserDetails();
        String memid = map.get(SessionManager.KEY_MEMID);
        String url = "http://demo8.mlmsoftindia.com/ShinePanel.svc/MLM_BusinessStatstics/" + memid;
        //  {FromDate}/{ToDate}/{Status}/{EpinNo}/{PlotHolderID}/{PlotNo}/{DepositedMemId}
        new BusinessStatisticsAsynctask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, url);

        String url2="http://demo8.mlmsoftindia.com/ShinePanel.svc//MLM_BusinessStatsticsPart/"+ memid;
        new BusinessStatisticsPayoutDetailAsynctask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, url2);
        return v;
    }

    private class BusinessStatisticsAsynctask extends AsyncTask<String, Void, String> {

        private static final int REGISTRATION_TIMEOUT = 10 * 1000;
        private static final int WAIT_TIMEOUT = 50 * 1000;
        private final HttpClient httpclient = new DefaultHttpClient();
        final HttpParams params = httpclient.getParams();
        HttpResponse response;
        private String content = null;
        private boolean error = false;
        private ProgressDialog dialog = new ProgressDialog(getActivity(),R.style.ThemeMyDialog);

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

        protected void onPostExecute(String content) {
            dialog.dismiss();

            if (error) {

                Toast toast = Toast.makeText(getActivity(), content, Toast.LENGTH_LONG);
                toast.setGravity(Gravity.TOP, 25, 500);
                View view1 = toast.getView();
                view1.setBackgroundResource(R.drawable.toast_drawablecolor);
                toast.show();
            } else if (content.equals("[]")) {
                Toast toast2 = Toast.makeText(getActivity(), "No Record Found", Toast.LENGTH_LONG);
                toast2.setGravity(Gravity.TOP, 25, 500);
                View view1 = toast2.getView();
                view1.setBackgroundResource(R.drawable.toast_drawablecolor);
                toast2.show();
            } else if (content.equals("")) {
                Toast toast2 = Toast.makeText(getActivity(), "No Record Found", Toast.LENGTH_LONG);
                toast2.setGravity(Gravity.TOP, 25, 500);
                View view1 = toast2.getView();
                view1.setBackgroundResource(R.drawable.toast_drawablecolor);
                toast2.show();
            } else if (content != null) {
                displayagentrecord(content);


            }

        }

        private void displayagentrecord(String response) {


            try {
                JSONArray jArray = new JSONArray(response);
                int count = 0;
                for (int i = 0; i < jArray.length(); i++) {
                    if (jArray != null) {
                        count++;
                    }
                    JSONObject jObj = jArray.getJSONObject(i);
                    id.setText(jObj.getString("LoginId"));
                    name.setText(jObj.getString("FirstName"));
                    sponsorid.setText(jObj.getString("SponsorLoginId"));
                    sponsorname.setText(jObj.getString("SponsorFirstName"));
                    panno.setText(jObj.getString("PanCard"));
                    joiningdate.setText(jObj.getString("JoiningDate"));
                    accono.setText(jObj.getString("MemberAccNo"));
                    bankname.setText(jObj.getString("MemberBankName"));
                    branchname.setText(jObj.getString("MemberBranch"));
                    ifsccode.setText(jObj.getString("IFSCCode"));
                    PermanentTotalLeftall.setText(jObj.getString("PermanentTotalLeft"));
                    PermanentTotalRightall.setText(jObj.getString("PermanentTotalRight"));
                    BinaryLeftmatched.setText(jObj.getString("BinaryLeft"));
                    BinaryRightmatched.setText(jObj.getString("BinaryRight"));
                    BalanceLeft.setText(jObj.getString("BalanceLeft"));
                    BalanceRight.setText(jObj.getString("BalanceRight"));
                    TotalLeft.setText(jObj.getString("TotalLeft"));
                    TotalRight.setText(jObj.getString("TotalRight"));

                    leftallold.setText(jObj.getString("PermanentTotalLeftA"));
                    rightallold.setText(jObj.getString("PermanentTotalRightA"));
                    leftbalanceold.setText(jObj.getString("BalanceLeftA"));
                    rightbalancedold.setText(jObj.getString("BalanceRightA"));
                    leftmatchedold.setText(jObj.getString("BinaryLeftA"));
                    rightmatchedold.setText(jObj.getString("BinaryRightA"));


                    int itemCount = jArray.length();


                }


            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    private class BusinessStatisticsPayoutDetailAsynctask extends AsyncTask<String, Void, String> {

        private static final int REGISTRATION_TIMEOUT = 10 * 1000;
        private static final int WAIT_TIMEOUT = 50 * 1000;
        private final HttpClient httpclient = new DefaultHttpClient();
        final HttpParams params = httpclient.getParams();
        HttpResponse response;
        private String content = null;
        private boolean error = false;
        private ProgressDialog dialog = new ProgressDialog(getActivity(),R.style.ThemeMyDialog);

        protected void onPreExecute() {
            dialog.setMessage("Getting  data... Please wait...");
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

        protected void onPostExecute(String content) {
            dialog.dismiss();

            if (error) {

                Toast toast = Toast.makeText(getActivity(), content, Toast.LENGTH_LONG);
                toast.setGravity(Gravity.TOP, 25, 500);
                View view1 = toast.getView();
                view1.setBackgroundResource(R.drawable.toast_drawablecolor);
                toast.show();
            } else if (content.equals("[]")) {
                Toast toast2 = Toast.makeText(getActivity(), "No PayoutListRecord Found", Toast.LENGTH_LONG);
                toast2.setGravity(Gravity.TOP, 25, 500);
                View view1 = toast2.getView();
                view1.setBackgroundResource(R.drawable.toast_drawablecolor);
                toast2.show();
            } else if (content.equals("")) {
                Toast toast2 = Toast.makeText(getActivity(), "No PayoutList Record Found", Toast.LENGTH_LONG);
                toast2.setGravity(Gravity.TOP, 25, 500);
                View view1 = toast2.getView();
                view1.setBackgroundResource(R.drawable.toast_drawablecolor);
                toast2.show();
               // linearpayout.setVisibility(View.GONE);
            } else if (content != null) {
                displayagentrecord(content);


            }

        }

        private void displayagentrecord(String response) {


            try {
                JSONArray jArray = new JSONArray(response);
                int count = 0;
                for (int i = 0; i < jArray.length(); i++) {
                    if (jArray != null) {
                        count++;
                    }
                    JSONObject jObj = jArray.getJSONObject(i);
                    harmony.setText(jObj.getString("Harmony"));
                    harmonyallotment.setText(jObj.getString("HarmonyAllotment"));
                    direct.setText(jObj.getString("Direct"));
                    total.setText(jObj.getString("Total"));
                    tds.setText(jObj.getString("TDS"));
                    hs.setText(jObj.getString("HS"));
                    totaldedction.setText(jObj.getString("TotalDeduction"));
                    netamount.setText(jObj.getString("NatAmount"));


                   // harmony,harmonyallotment,direct,total,tds,hs,totaldedction,netamount
                    int itemCount = jArray.length();


                }


            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
}
