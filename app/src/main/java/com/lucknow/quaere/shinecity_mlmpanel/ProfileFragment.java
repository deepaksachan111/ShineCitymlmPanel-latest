package com.lucknow.quaere.shinecity_mlmpanel;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.lucknow.quaere.shinecity_mlmpanel.Model.PayoutDetailData;

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


public class ProfileFragment extends Fragment {
    private CoordinatorLayout snackbarCoordinatorLayout;
    private TextView tv_username, tv_useraddress, tv_dob,tv_panno, tv_email,tvuser_mobileno, tv_bankname,tv_bankacno,tv_bankaccounttype, tv_bankaddress, tv_branch, tv_ifsccode, tv_nomineename, tv_nomineeaddress, tv_nomineerelation, tv_nomineemobile;
    private ArrayList<PayoutDetailData> payoutDetailData;

    private ArrayAdapter<PayoutDetailData> payoutarrayAdapter;
    private ListView listView2;
    private TextView tv_total_noofrecords,tv_grassamt_total,tv_deduction_total,tv_netamt_total;
    private LinearLayout linearprofile_payout;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
      //  snackbarCoordinatorLayout = (CoordinatorLayout) view.findViewById(R.id.snackbarCoordinatorLayout);
        findviewbyId(view);
        SessionManager session = new SessionManager(getActivity());
        HashMap<String, String> user = session.getUserDetails();
        String MemberID = user.get(SessionManager.KEY_MEMID);

        String url = "http://demo8.mlmsoftindia.com/ShinePanel.svc/MLM_ViewProfile/" + MemberID;
        new ProfileFragmentAsynktask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, url);

        String url2 = "http://demo8.mlmsoftindia.com/ShinePanel.svc/MLM_PayoutDetails/" + MemberID + "/" + "-1" + "/" + "-1";

        new PayoutDetailsAsyncTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, url2);
        return view;
    }

    private void findviewbyId(View view) {
        tv_username = (TextView)view.findViewById(R.id.username_tv);
        tv_useraddress = (TextView)view.findViewById(R.id.user_address_tv);
        tv_dob = (TextView)view.findViewById(R.id.user_dob_tv);
        tv_email= (TextView)view.findViewById(R.id.user_email_tv);
        tv_panno = (TextView)view.findViewById(R.id.user_panno_tv);
        tvuser_mobileno= (TextView)view.findViewById(R.id.user_mobile_tv);

        tv_bankname = (TextView)view.findViewById(R.id.bankname_tv);
        tv_bankacno = (TextView)view.findViewById(R.id.bank_accountno_tv);
        tv_bankaccounttype  = (TextView)view.findViewById(R.id.bankaccounttype_tv);
        tv_bankaddress  = (TextView)view.findViewById(R.id.bank_address_tv);
        tv_branch =  (TextView)view.findViewById(R.id.bank_branch_tv);
        tv_ifsccode  = (TextView)view.findViewById(R.id.ifsc_tv);

        tv_nomineename = (TextView)view.findViewById(R.id.nominee_name_tv);
        tv_nomineeaddress  = (TextView)view.findViewById(R.id.nominee_address_tv);
        tv_nomineerelation  = (TextView)view.findViewById(R.id.nominee_relation_tv);
        tv_nomineemobile  = (TextView)view.findViewById(R.id.nominee_mobile_tv);

        listView2 =(ListView)view.findViewById(R.id.listview_payoutdetail_profile);
         linearprofile_payout =(LinearLayout)view.findViewById(R.id.linear_profile_payout);
        tv_netamt_total = (TextView)view.findViewById(R.id.tv_profile_netamt_total);
        tv_deduction_total = (TextView)view.findViewById(R.id.tv_profile_deducation_total);
        tv_grassamt_total = (TextView)view.findViewById(R.id.tv_profile_grassamt_total);


    }

    private class ProfileFragmentAsynktask extends AsyncTask<String, Void, String> {

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
           Toast toast2= Toast.makeText(getActivity(), "connection server failed", Toast.LENGTH_LONG);
            toast2.setGravity(Gravity.TOP, 25, 500);
            View view1 = toast2.getView();
            view1.setBackgroundResource(R.drawable.toast_drawablecolor);
            toast2.show();
           /*Snackbar snackbar = Snackbar
                    .make(snackbarCoordinatorLayout, "Error connecting to Server", Snackbar.LENGTH_LONG);
            ViewGroup group = (ViewGroup) snackbar.getView();
            TextView tv = (TextView) group.findViewById(android.support.design.R.id.snackbar_text);
            tv.setTextColor(Color.WHITE);
            group.setBackgroundColor(getResources().getColor(R.color.blue));
            snackbar.show();*/
            getActivity().finish();
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
            } else if (content.equals("[]")) {
                Toast  toast2= Toast.makeText(getActivity(),  "invalid user name or password", Toast.LENGTH_LONG);
                toast2.setGravity(Gravity.TOP, 25, 500);
                View view1 = toast2.getView();
                view1.setBackgroundResource(R.drawable.toast_drawablecolor);
                toast2.show();
            } else if (content.equals("")) {
                Toast  toast2= Toast.makeText(getActivity(), "Server not responding", Toast.LENGTH_LONG);
                toast2.setGravity(Gravity.TOP, 25, 500);
                View view1 = toast2.getView();
                view1.setBackgroundResource(R.drawable.toast_drawablecolor);
                toast2.show();
            } else if (content != null) {
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

        private void displayCountryList(String response) {

            try {
                JSONArray jArray = new JSONArray(response);

                for (int i = 0; i < jArray.length(); i++) {

                   JSONObject jObj = jArray.getJSONObject(i);
                    String  username = jObj.getString("FirstName");
                    tv_username.setText(username);
                    String address = jObj.getString("Address");
                    tv_useraddress.setText(address);
                    String dob = jObj.getString("DOB");
                    if(dob.contains("")){
                        tv_dob.setText("not available");
                    }else {
                        tv_dob.setText(dob);
                    }
                    String email = jObj.getString("EmailId");
                    tv_email.setText(email);
                    String usermobileno = jObj.getString("Mobile");
                    tvuser_mobileno.setText(usermobileno);
                    String panNo = jObj.getString("PANNo");
                    tv_panno.setText(panNo);

                    String bankname = jObj.getString("BankName");
                    tv_bankname.setText(bankname);
                    String bankAccounNo = jObj.getString("BankAccounNo");
                    tv_bankacno.setText(bankAccounNo);
                    String bankactype = jObj.getString("AccountType");
                    tv_bankaccounttype.setText(bankactype);
                    String bankaddress = jObj.getString("City");
                    tv_bankaddress.setText(bankaddress);
                    String branchname = jObj.getString("BankBranchName");
                    tv_branch.setText(branchname);
                    String ifsccode = jObj.getString("IFSCCode");
                    tv_ifsccode.setText(ifsccode);

                    String  nomineeName = jObj.getString("NomineeName");
                    if (nomineeName.contains("")) {
                        tv_nomineename.setText("not available");
                    }else {
                        tv_nomineename.setText(nomineeName);
                    }

                    String  nomineeAddress = jObj.getString("NomineeAddress");
                    if (nomineeAddress.contains(" ")) {
                        tv_nomineeaddress.setText("not available");
                    }else {
                        tv_nomineeaddress.setText(nomineeAddress);
                    }
                    String  nomineeRelation = jObj.getString("NomineeRelation");
                    if (nomineeRelation.contains("")) {
                        tv_nomineerelation.setText("not available");
                    }else {
                        tv_nomineerelation.setText(nomineeRelation);
                    }
                    String  nonineeMobile = jObj.getString("NonineeMobile");
                    if (nonineeMobile.contains("[]")) {
                        tv_nomineemobile.setText("not available");
                    }else {
                        tv_nomineemobile.setText(nonineeMobile);
                    }


                }


            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    private class PayoutDetailsAsyncTask extends AsyncTask<String, Void, String> {

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
                Toast toast2 = Toast.makeText(getActivity(), "No PayoutRecord Found", Toast.LENGTH_LONG);
                toast2.setGravity(Gravity.TOP, 25, 500);
                View view1 = toast2.getView();
                view1.setBackgroundResource(R.drawable.toast_drawablecolor);
                toast2.show();
            } else if (content.equals("")) {
                Toast toast2 = Toast.makeText(getActivity(), "No PayoutRecord Found", Toast.LENGTH_LONG);
                toast2.setGravity(Gravity.TOP, 25, 500);
                View view1 = toast2.getView();
                view1.setBackgroundResource(R.drawable.toast_drawablecolor);
                toast2.show();
            } else if (content != null) {
                displayagentrecord(content);


            }

        }

        private void displayagentrecord(String response) {
            payoutDetailData = new ArrayList<PayoutDetailData>();
            long sum = 0;

            double amttotal = 0;
            double namt = 0;
            double ded=0;

            try {
                JSONArray jArray = new JSONArray(response);
                int count = 0;
                for (int i = 0; i < jArray.length(); i++) {
                    if (jArray != null) {
                        count++;
                    }
                    JSONObject jObj = jArray.getJSONObject(i);
                    String date = jObj.getString("ClosingDate");
                    String payoutdetail = jObj.getString("PaymentDetails");
                    String harmonyallot = jObj.getString("BinaryAllotment");
                    String hormony = jObj.getString("BinaryBooking");
                    String selfincentive = jObj.getString("Spill");
                    String tds = jObj.getString("TDSAmount");
                    String grassamount = jObj.getString("GrossAmount");
                    String process = jObj.getString("ProcessingFee");
                    String netamt = jObj.getString("NetAmount");

                    long longprocessing = Long.parseLong(process);
                    long longtds = Long.parseLong(tds);
                    sum =longprocessing + longtds;
                    String deduction = String.valueOf(sum);


                    double amtto = Double.parseDouble(grassamount);
                    double nam = Double.parseDouble(netamt);
                    double d = Double.parseDouble(deduction);

                    amttotal = amttotal + amtto;
                    namt = namt + nam;
                    ded = ded + d;


                   // String deduction =
                            payoutDetailData.add(new PayoutDetailData(date, grassamount, deduction, netamt));

                }
                 String amt = String.valueOf(amttotal);
                String net = String.valueOf(namt);
                String d = String.valueOf(ded);

                  tv_grassamt_total.setText( amt);
                tv_netamt_total.setText(net);
                tv_deduction_total.setText(d);

                payoutarrayAdapter = new PayoutDetailArrayadapter(getActivity(), R.layout.adapter_payoutdetail_profile, payoutDetailData);

                listView2.setAdapter(payoutarrayAdapter);
                com.lucknow.quaere.shinecity_mlmpanel.ListviewHelper.getListViewSize(listView2);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    private class PayoutDetailArrayadapter extends ArrayAdapter<PayoutDetailData> {
        int deepColor = Color.parseColor("#E9E9E9");
        int deepColor2 = Color.parseColor("#FCF9F9");
        private int[] colors = new int[]{deepColor, deepColor2};
        ArrayList<PayoutDetailData> payoutDetailDatas2;
        Context context;

        public PayoutDetailArrayadapter(Context context, int resource, ArrayList<PayoutDetailData> payoutDetailDatas) {
            super(context, resource, payoutDetailDatas);
            this.context = context;
            this.payoutDetailDatas2 = payoutDetailDatas;
        }

        private class ViewHolder {
            TextView date;

            TextView grassamt;
            TextView deduction;
            TextView netamount;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder = null;
            if (convertView == null) {
                LayoutInflater layoutInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = layoutInflater.inflate(R.layout.adapter_payoutdetail_profile, null);
                viewHolder = new ViewHolder();
                int colorPos = position % colors.length;
                convertView.setBackgroundColor(colors[colorPos]);
                viewHolder.date = (TextView) convertView.findViewById(R.id.tv_payoutdeatail_date_profile);

                viewHolder.grassamt = (TextView) convertView.findViewById(R.id.tv_payoutdeatail_grassamt_profile);

                viewHolder .deduction = (TextView) convertView.findViewById(R.id.tv_payoutdeatail_deduction_profile);

                viewHolder.netamount = (TextView) convertView.findViewById(R.id.tv_payoutdeatail_netamt_profile);


                convertView.setTag(viewHolder);
            } else {
                int colorPos = position % colors.length;
                convertView.setBackgroundColor(colors[colorPos]);
                viewHolder = (ViewHolder) convertView.getTag();
            }
            PayoutDetailData data = payoutDetailDatas2.get(position);
            viewHolder.date.setText(data.getDate());
            viewHolder.grassamt.setText(data.getGrassamt());
            viewHolder.deduction.setText(data.getDeduction());
            viewHolder.netamount.setText(data.getNetamount());

            return convertView;
        }
    }

}
