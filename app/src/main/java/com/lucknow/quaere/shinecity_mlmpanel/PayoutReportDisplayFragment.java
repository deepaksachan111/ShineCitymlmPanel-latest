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
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
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


public class PayoutReportDisplayFragment extends Fragment {

    private ArrayList<com.lucknow.quaere.shinecity_mlmpanel.Model.PayoutDetailData> payoutDetailData;
    private ArrayAdapter<com.lucknow.quaere.shinecity_mlmpanel.Model.PayoutDetailData> payoutarrayAdapter;
    private ListView listView2;
    private TextView tv_total_noofrecords;


    public PayoutReportDisplayFragment() {
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
        View view = inflater.inflate(R.layout.fragment_payout_report_display, container, false);
        listView2 = (ListView) view.findViewById(R.id.listview_payoutdetail);
        tv_total_noofrecords = (TextView) view.findViewById(R.id.txt_payout_no_records);
       LinearLayout linear_total_noofrecords =(LinearLayout)view.findViewById(R.id.linear_total_noofrecords);
        linear_total_noofrecords.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
           /*     int index = listView2.getFirstVisiblePosition();
                View view = listView2.getChildAt(0);
                int top = (view == null) ? 0 : view.getTop();

// restore
                listView2.setSelectionFromTop(index, top);*/
                listView2.scrollBy(0, +200) ;

            }
        });

        SessionManager sessionManager = new SessionManager(getActivity());
        HashMap<String, String> user = sessionManager.getUserDetails();
        String MemberID = user.get(SessionManager.KEY_MEMID);
       /* String fromDate =getArguments().getString("PARAM1");
        String toDate =getArguments().getString("PARAM2");*/
        String url = "http://demo8.mlmsoftindia.com/ShinePanel.svc/MLM_PayoutDetails/" + MemberID + "/" + "-1" + "/" + "-1";

        new PayoutDetailsAsyncTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, url);
        return view;
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
            payoutDetailData = new ArrayList<com.lucknow.quaere.shinecity_mlmpanel.Model.PayoutDetailData>();
            long sum = 0;

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

                    long l = Long.parseLong(netamt);
                    sum = sum + l;
                    int itemCount = jArray.length();
                    long s = sum;
                    payoutDetailData.add(new com.lucknow.quaere.shinecity_mlmpanel.Model.PayoutDetailData(date, payoutdetail, harmonyallot, hormony, selfincentive, tds, grassamount, process, netamt));

                }
                long s = sum;
                int no = count;
                String aString = Integer.toString(no);
                String strI = String.valueOf(no);
                tv_total_noofrecords.setText(strI);

                payoutarrayAdapter = new PayoutDetailArrayadapter(getActivity(), R.layout.adpter_payoutdetail, payoutDetailData);

                listView2.setAdapter(payoutarrayAdapter);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    private class PayoutDetailArrayadapter extends ArrayAdapter<com.lucknow.quaere.shinecity_mlmpanel.Model.PayoutDetailData> {
        int deepColor = Color.parseColor("#fde9ec");
        int deepColor2 = Color.parseColor("#defde0");
        private int[] colors = new int[]{deepColor, deepColor2};
        ArrayList<com.lucknow.quaere.shinecity_mlmpanel.Model.PayoutDetailData> payoutDetailDatas2;
        Context context;

        public PayoutDetailArrayadapter(Context context, int resource, ArrayList<com.lucknow.quaere.shinecity_mlmpanel.Model.PayoutDetailData> payoutDetailDatas) {
            super(context, resource, payoutDetailDatas);
            this.context = context;
            this.payoutDetailDatas2 = payoutDetailDatas;
        }

        private class ViewHolder {
            TextView date;
            TextView payoutdetail;
            TextView harmonyallot;
            TextView harmony;
            TextView selfincentive;
            TextView tds;
            TextView grassamt;
            TextView processing;
            TextView netamount;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder = null;
            if (convertView == null) {
                LayoutInflater layoutInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = layoutInflater.inflate(R.layout.adpter_payoutdetail, null);
                viewHolder = new ViewHolder();
                int colorPos = position % colors.length;
                convertView.setBackgroundColor(colors[colorPos]);
                viewHolder.date = (TextView) convertView.findViewById(R.id.tv_payoutdeatail_date);
                //    viewHolder.payoutdetail = (TextView) convertView.findViewById(R.id.tv_payoutdeatail_paydetail);
                viewHolder.harmonyallot = (TextView) convertView.findViewById(R.id.tv_payoutdeatail_harnonyallot);
                viewHolder.harmony = (TextView) convertView.findViewById(R.id.tv_payoutdeatail_harmony);
                viewHolder.selfincentive = (TextView) convertView.findViewById(R.id.tv_payoutdeatail_selfincentive);
                //  viewHolder.tds = (TextView) convertView.findViewById(R.id.tv_payoutdeatail_tds);
                viewHolder.grassamt = (TextView) convertView.findViewById(R.id.tv_payoutdeatail_grassamt);
                //   viewHolder.processing = (TextView) convertView.findViewById(R.id.tv_payoutdeatail_processing);
                viewHolder.netamount = (TextView) convertView.findViewById(R.id.tv_payoutdeatail_netamt);

                //  holder.memberid = (TextView) convertView.findViewById(R.id.txt_memberid);

                convertView.setTag(viewHolder);
            } else {
                int colorPos = position % colors.length;
                convertView.setBackgroundColor(colors[colorPos]);
                viewHolder = (ViewHolder) convertView.getTag();
            }
            com.lucknow.quaere.shinecity_mlmpanel.Model.PayoutDetailData data = payoutDetailDatas2.get(position);
            viewHolder.date.setText(data.getDate());
            //   viewHolder.payoutdetail.setText(data.getPaymentdetail());
            viewHolder.harmonyallot.setText(data.getHarmonyallot());
            viewHolder.harmony.setText(data.getHarmony());
            viewHolder.selfincentive.setText(data.getSelfincentive());
            //     viewHolder.tds.setText(data.getTds());
            viewHolder.grassamt.setText(data.getGrassamt());
            //   viewHolder.processing.setText(data.getProcessing());
            viewHolder.netamount.setText(data.getNetamount());

            return convertView;
        }
    }


}
