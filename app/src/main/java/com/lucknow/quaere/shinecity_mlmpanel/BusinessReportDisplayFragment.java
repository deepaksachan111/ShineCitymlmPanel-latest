package com.lucknow.quaere.shinecity_mlmpanel;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.lucknow.quaere.shinecity_mlmpanel.Model.BusinessNewRecordData;
import com.lucknow.quaere.shinecity_mlmpanel.Model.BusinessOldReportData;

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


public class BusinessReportDisplayFragment extends Fragment {

    SessionManager sessionManager;
    private ArrayList<BusinessOldReportData> businessoldReportDatas;
    private ArrayList<BusinessNewRecordData> businessnewReportDatas;

    private ArrayAdapter<BusinessOldReportData> businessoldReportDataArrayAdapter;
    private ArrayAdapter<BusinessNewRecordData> businessnewReportDataArrayAdapter;
    private TextView tv_norecord;
    private ListView listViewold,listViewnew;
    private  boolean flag = true;
    private  boolean isFlagnew= true;
    TextView tvnew;
    TextView tvold;
    public BusinessReportDisplayFragment() {
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
        View view = inflater.inflate(R.layout.fragment_business_report_display, container, false);

         tvold = (TextView) view.findViewById(R.id.tv_oldtopupdetails);
       final Animation vanish = AnimationUtils.loadAnimation(getActivity(), R.anim.vanish);

        tvnew   = (TextView) view.findViewById(R.id.tv_newtopupdetails);

        tv_norecord = (TextView) view.findViewById(R.id.tv_business_norecordfound);
        final LinearLayout linearLayoutold = (LinearLayout) view.findViewById(R.id.linear_oldtopupdetails);
              final LinearLayout linearLayoutnew =(LinearLayout)view.findViewById(R.id.linear_newtopupdetails);
        listViewold = (ListView)view.findViewById(R.id.listview_old_popupdetails);
        listViewnew = (ListView)view.findViewById(R.id.listview_new_popupdetails);

        tvold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(flag == true ){
                    linearLayoutold.setVisibility(View.VISIBLE);

                    SessionManager sessionManager = new SessionManager(getActivity());
                    HashMap<String, String> user = sessionManager.getUserDetails();
                    String MemberID = user.get(SessionManager.KEY_MEMID);
                    String fromdate = getArguments().getString("PARAM1");
                    String todate = getArguments().getString("PARAM2");
                    String busiofselectedtext1 = getArguments().getString("PARAM3");
                    String paymenttypeselectedtext1 = getArguments().getString("PARAM4");
                    String paymentstatusselectedtext1 = getArguments().getString("PARAM5");
                    String paymentprocedselectedtext1 = getArguments().getString("PARAM6");

                    String url = "http://demo8.mlmsoftindia.com/ShinePanel.svc/MLM_BusinessDetailsTable1/" + MemberID + "/" + fromdate + "/" + todate + "/" + busiofselectedtext1 + "/" + paymenttypeselectedtext1 + "/" + paymentstatusselectedtext1 + "/" + paymentprocedselectedtext1 + "/2000";
                    new BusinessOldrecortDisplayAsyncTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, url);
                    flag = false;
                    tvold.startAnimation(vanish);
                }

                else {
                    linearLayoutold.setVisibility(View.GONE);
                    tvold.startAnimation(vanish);
                    flag =true;
                }


            }
        });

        tvnew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isFlagnew == true){
                    linearLayoutnew.setVisibility(View.VISIBLE);
                    SessionManager sessionManager = new SessionManager(getActivity());
                    HashMap<String, String> user = sessionManager.getUserDetails();
                    String MemberID = user.get(SessionManager.KEY_MEMID);
                    String fromdate = getArguments().getString("PARAM1");
                    String todate = getArguments().getString("PARAM2");
                    String busiofselectedtext1 = getArguments().getString("PARAM3");
                    String paymenttypeselectedtext1 = getArguments().getString("PARAM4");
                    String paymentstatusselectedtext1 = getArguments().getString("PARAM5");
                    String paymentprocedselectedtext1 = getArguments().getString("PARAM6");

                    String url = "http://demo8.mlmsoftindia.com/ShinePanel.svc/MLM_BusinessDetailsTable2/" + MemberID + "/" + fromdate + "/" + todate + "/" + busiofselectedtext1 + "/" + paymenttypeselectedtext1 + "/" + paymentstatusselectedtext1 + "/" + paymentprocedselectedtext1 + "/2000";
                    new BusinessNewrecortDisplayAsyncTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, url);
                    isFlagnew = false;
                    tvnew.setAnimation(vanish);
                }else {
                    tvnew.setAnimation(vanish);
                    linearLayoutnew.setVisibility(View.GONE);
                    isFlagnew = true;
                }
            }
        });


        return view;
    }

    private class BusinessOldrecortDisplayAsyncTask extends AsyncTask<String, Void, String> {

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
                 displayOldbusinessRecord(content);


            }

        }

        private void displayOldbusinessRecord(String response) {
            businessoldReportDatas = new ArrayList<BusinessOldReportData>();
            try {
                JSONArray jArray = new JSONArray(response);
                int count = 0;
                for (int i = 0; i < jArray.length(); i++) {
                    if (jArray != null) {
                        count++;
                    }
                    JSONObject jObj = jArray.getJSONObject(i);
                    String date = jObj.getString("request_on");
                    String membername = jObj.getString("PHName");
                    String memid = jObj.getString("PHID");
                    String paymenttype = jObj.getString("PaymentType");
                    String mode = jObj.getString("Payment_Mode");
                    String amount = jObj.getString("amount");
                    String status = jObj.getString("status");

                    int itemCount = jArray.length();
                    businessoldReportDatas.add(new BusinessOldReportData(date, membername, memid, paymenttype, mode, amount, status));
                }
                int no = count;
                String aString = Integer.toString(no);
                String strI = String.valueOf(no);
              //  tv_no.setText(strI);

                businessoldReportDataArrayAdapter = new BusinessOldReportdisplayArrayadapter(getActivity(), R.layout.adapter_old_businessreport_display, businessoldReportDatas);

                listViewold.setAdapter(businessoldReportDataArrayAdapter);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    private class BusinessOldReportdisplayArrayadapter extends ArrayAdapter<BusinessOldReportData> {
        private ArrayList<BusinessOldReportData> businessdataadapter;
        private Context context;
        int deepColor = Color.parseColor("#fde9ec");
        int deepColor2 = Color.parseColor("#defde0");
        private int[] colors = new int[] {deepColor,deepColor2 };
        // private TextView Date,Month,Year,Amount;


        public BusinessOldReportdisplayArrayadapter(Context context, int resource, ArrayList<BusinessOldReportData> businessdata) {
            super(context, resource, businessdata);
            this.businessdataadapter = businessdata;
            this.context = context;
        }

        private class ViewHolder {
            TextView date;
            TextView membername;
            TextView memberid;
            TextView paymenttype;
            TextView mode;
            TextView amount;
            ImageView status;

        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
          /*  ViewHolder holder = null;
            if (convertView == null) {
                LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View rowView = layoutInflater.inflate(R.layout.debittranscation_arryadapter, parent, false);*/

            ViewHolder holder = null;
            Log.v("ConvertView", String.valueOf(position));
            if (convertView == null) {

                LayoutInflater vi = (LayoutInflater) getActivity().getSystemService(
                        Context.LAYOUT_INFLATER_SERVICE);
                convertView = vi.inflate(R.layout.adapter_old_businessreport_display, null);
                int colorPos = position % colors.length;
                convertView.setBackgroundColor(colors[colorPos]);
                holder = new ViewHolder();
                holder.date = (TextView) convertView.findViewById(R.id.tv_businessdate);
                //  holder.memberid = (TextView) convertView.findViewById(R.id.txt_memberid);
                holder.membername = (TextView) convertView.findViewById(R.id.tv_businessmembername);
                //   holder.pareientid = (TextView) convertView.findViewById(R.id.txt_parenid);
                holder.memberid = (TextView) convertView.findViewById(R.id.tv_businessmemberid);
                holder.paymenttype = (TextView) convertView.findViewById(R.id.tv_businesspaymenttype);
                holder.mode = (TextView) convertView.findViewById(R.id.tv_businessmode);
                holder.amount = (TextView) convertView.findViewById(R.id.tv_businessamt);
                holder.status = (ImageView) convertView.findViewById(R.id.tv_businesstatus);

                BusinessOldReportData data = businessdataadapter.get(position);
                holder.date.setText(data.getDate());
                holder.membername.setText(data.getMembername());
                holder.memberid.setText(data.getMemberid());
                holder.paymenttype.setText(data.getPaymenttype());
                holder.mode.setText(data.getMode());
                holder.amount.setText(data.getAmount());
               // holder.status.setText(data.getStatus());
                String s = (data.getStatus().toString());
                if(s.contains("Pending")) {
                  //  holder.status.setTextColor(Color.parseColor("#4B0082"));
                  //  holder.status.setText("P");
                    holder.status.setBackgroundResource(R.drawable.pending);

                }else if(s.contains("Blocked")){
                    holder.status.setBackgroundResource(R.drawable.blocked);
                   // holder.status.setTextColor(Color.parseColor("#FF0000"));
                   // holder.status.setText("B");
                }else if(s.contains("Confirmed")){
                    holder.status.setBackgroundResource(R.drawable.confirm);
                   // holder.status.setTextColor(Color.parseColor("#368200"));
                  //  holder.status.setText("C");
                }


                convertView.setTag(holder);

            } else {
                int colorPos = position % colors.length;
                convertView.setBackgroundColor(colors[colorPos]);
                holder = (ViewHolder) convertView.getTag();

            }
            BusinessOldReportData data = businessdataadapter.get(position);
            holder.date.setText(data.getDate());
            holder.membername.setText(data.getMembername());
            holder.memberid.setText(data.getMemberid());
            holder.paymenttype.setText(data.getPaymenttype());
            holder.mode.setText(data.getMode());
            holder.amount.setText(data.getAmount());
          //  holder.status.setText(data.getStatus());
            String s = (data.getStatus().toString());
            if(s.contains("Pending")) {
            /*    holder.status.setTextColor(Color.parseColor("#4B0082"));
                holder.status.setText("P");*/
                holder.status.setBackgroundResource(R.drawable.pending);
            }else if(s.contains("Blocked")){
            /*    holder.status.setTextColor(Color.parseColor("#FF0000"));
                holder.status.setText("B");*/
                holder.status.setBackgroundResource(R.drawable.blocked);
            }else if(s.contains("Confirmed")){
              /*  holder.status.setTextColor(Color.parseColor("#368200"));
                holder.status.setText("C");*/
                holder.status.setBackgroundResource(R.drawable.confirm);
            }

            return convertView;

        }


    }


    private class BusinessNewrecortDisplayAsyncTask extends AsyncTask<String, Void, String> {

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
                tv_norecord.setText("No Record found");
               /* Toast toast2 = Toast.makeText(getActivity(), "Server response failed", Toast.LENGTH_LONG);
                toast2.setGravity(Gravity.TOP, 25, 500);
                View view1 = toast2.getView();
                view1.setBackgroundResource(R.drawable.toast_drawablecolor);
                toast2.show();*/
            } else if (content.equals("")) {
                Toast toast2 = Toast.makeText(getActivity(), "No Record Found", Toast.LENGTH_LONG);
                toast2.setGravity(Gravity.TOP, 25, 500);
                View view1 = toast2.getView();
                view1.setBackgroundResource(R.drawable.toast_drawablecolor);
                toast2.show();
            } else if (content != null) {
                displayNewbusinessRecord(content);


            }

        }

        private void displayNewbusinessRecord(String response) {
            businessnewReportDatas = new ArrayList<BusinessNewRecordData>();
            try {
                JSONArray jArray = new JSONArray(response);
                int count = 0;
                for (int i = 0; i < jArray.length(); i++) {
                    if (jArray != null) {
                        count++;
                    }
                    JSONObject jObj = jArray.getJSONObject(i);
                    String date = jObj.getString("InstallmentPaidDate");
                    String custid = jObj.getString("PHID");
                    String agentid = jObj.getString("AgentID");
                    String paymenttype = jObj.getString("PaymentType");
                    String mode = jObj.getString("PaymentMode");
                    String site = jObj.getString("SiteName");
                    String plot = jObj.getString("PlotNo");
                    String amount = jObj.getString("InstallmentAmount");
                    String status = jObj.getString("PaymentStatus");




                    int itemCount = jArray.length();

                    businessnewReportDatas.add(new BusinessNewRecordData(date, custid, agentid, paymenttype, mode,site,plot, amount, status));
                }
                int no = count;
                String aString = Integer.toString(no);
                String strI = String.valueOf(no);
                //  tv_no.setText(strI);

                businessnewReportDataArrayAdapter = new BusinessNewReportdisplayArrayadapter(getActivity(), R.layout.adapter_new_businessreport_display, businessnewReportDatas);

                listViewnew.setAdapter(businessnewReportDataArrayAdapter);


                } catch (JSONException e) {
                e.printStackTrace();
            }


        }
        }

        private class BusinessNewReportdisplayArrayadapter extends ArrayAdapter<BusinessNewRecordData> {
            private ArrayList<BusinessNewRecordData> businessnewdataadapter;
            private Context context;
            // private TextView Date,Month,Year,Amount;


            public BusinessNewReportdisplayArrayadapter(Context context, int resource, ArrayList<BusinessNewRecordData> businesnewsdata) {
                super(context, resource, businesnewsdata);
                this.businessnewdataadapter = businesnewsdata;
                this.context = context;
            }

            private class ViewHolder {
                TextView date;
                TextView custid;
                TextView agentid;
                TextView paymenttype;
                TextView mode;
                TextView site;
                TextView plot;
                TextView amount;
                ImageView status;

            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
          /*  ViewHolder holder = null;
            if (convertView == null) {
                LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View rowView = layoutInflater.inflate(R.layout.debittranscation_arryadapter, parent, false);*/

                ViewHolder holder = null;
                Log.v("ConvertView", String.valueOf(position));
                if (convertView == null) {

                    LayoutInflater vi = (LayoutInflater) getActivity().getSystemService(
                            Context.LAYOUT_INFLATER_SERVICE);
                    convertView = vi.inflate(R.layout.adapter_new_businessreport_display, null);

                    holder = new ViewHolder();
                    holder.date = (TextView) convertView.findViewById(R.id.tv_businewdate);
                    //  holder.memberid = (TextView) convertView.findViewById(R.id.txt_memberid);
                    holder.custid = (TextView) convertView.findViewById(R.id.tv_businewcustid);
                    //   holder.pareientid = (TextView) convertView.findViewById(R.id.txt_parenid);
                    holder.agentid = (TextView) convertView.findViewById(R.id.tv_businewagentid);
                    holder.paymenttype = (TextView) convertView.findViewById(R.id.tv_businewpaymenttype);
                    holder.mode = (TextView) convertView.findViewById(R.id.tv_businewmode);

                    holder.site = (TextView) convertView.findViewById(R.id.tv_businewsite);
                    holder.plot = (TextView) convertView.findViewById(R.id.tv_businewplot);

                    holder.amount = (TextView) convertView.findViewById(R.id.tv_businewamount);
                    holder.status = (ImageView) convertView.findViewById(R.id.tv_businewstatus);

                    BusinessNewRecordData data = businessnewdataadapter.get(position);
                    holder.date.setText(data.getDate());
                    holder.custid.setText(data.getCustid());
                    holder.agentid.setText(data.getAgentid());
                    holder.paymenttype.setText(data.getPaymenttype());
                    holder.mode.setText(data.getMode());
                    holder.site.setText(data.getSite());
                    holder.plot.setText(data.getPlot());

                    holder.amount.setText(data.getAmount());
                    //holder.status.setText(data.getStatus());
                   String s = (data.getStatus().toString());

                    if(s.contains("Pending")) {
            /*    holder.status.setTextColor(Color.parseColor("#4B0082"));
                holder.status.setText("P");*/
                        holder.status.setBackgroundResource(R.drawable.pending);
                    }else if(s.contains("Blocked")){
            /*    holder.status.setTextColor(Color.parseColor("#FF0000"));
                holder.status.setText("B");*/
                        holder.status.setBackgroundResource(R.drawable.blocked);

                    }else if(s.contains("Confirmed")){
              /*  holder.status.setTextColor(Color.parseColor("#368200"));
                holder.status.setText("C");*/
                        holder.status.setBackgroundResource(R.drawable.confirm);
                    }

                    convertView.setTag(holder);

                } else {

                    holder = (ViewHolder) convertView.getTag();

                }
                BusinessNewRecordData data = businessnewdataadapter.get(position);
                holder.date.setText(data.getDate());
                holder.custid.setText(data.getCustid());
                holder.agentid.setText(data.getAgentid());
                holder.paymenttype.setText(data.getPaymenttype());
                holder.mode.setText(data.getMode());
                holder.site.setText(data.getSite());
                holder.plot.setText(data.getPlot());

                holder.amount.setText(data.getAmount());
               // holder.status.setText(data.getStatus());

                String s = (data.getStatus().toString());
                if(s.contains("Pending")) {
            /*    holder.status.setTextColor(Color.parseColor("#4B0082"));
                holder.status.setText("P");*/
                    holder.status.setBackgroundResource(R.drawable.pending);
                }else if(s.contains("Blocked")){
            /*    holder.status.setTextColor(Color.parseColor("#FF0000"));
                holder.status.setText("B");*/
                    holder.status.setBackgroundResource(R.drawable.blocked);
                }else if(s.contains("Confirmed")){
              /*  holder.status.setTextColor(Color.parseColor("#368200"));
                holder.status.setText("C");*/
                    holder.status.setBackgroundResource(R.drawable.confirm);
                }
                return convertView;

            }


        }


    }

