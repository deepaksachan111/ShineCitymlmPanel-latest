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
import android.widget.ImageView;
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


public class AgentPlotReportDisplayFragment extends Fragment {

     private ArrayList<com.lucknow.quaere.shinecity_mlmpanel.Model.AgentPlotReportData> ajentplotreportdata;
     private ArrayAdapter<com.lucknow.quaere.shinecity_mlmpanel.Model.AgentPlotReportData> agentPlotReportDataArrayAdapter;
     private ListView listView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_agent_plot_report_display, container, false);
        listView = (ListView)view.findViewById(R.id.listview_agentplotreport);
        com.lucknow.quaere.shinecity_mlmpanel.SessionManager sessionManager = new com.lucknow.quaere.shinecity_mlmpanel.SessionManager(getActivity());
        HashMap<String, String> user = sessionManager.getUserDetails();
        String MemberID = user.get(com.lucknow.quaere.shinecity_mlmpanel.SessionManager.KEY_MEMID);
        String fromdate = getArguments().getString("PARAM1");
        String todate = getArguments().getString("PARAM2");
        String paymenttype = getArguments().getString("PARAM3");
        String paymentmode = getArguments().getString("PARAM4");
        String epinno = getArguments().getString("PARAM5");
        String cheque = getArguments().getString("PARAM6");
        String checkvalue = getArguments().getString("PARAM7");

       String url = "http://demo8.mlmsoftindia.com/ShinePanel.svc/AgentCollectionReport/"+paymenttype+"/"+fromdate+"/"+todate+"/"+epinno+"/-1/-1/"+MemberID+"/-1/"+paymentmode+"/-1/-1/M/"+checkvalue;
    //    AgentCollectionReport/{PaymentType}/{FromDate}/{ToDate}/{EpinNo}/{PlotHolderID}/{PlotNo}/{MemberID}/{SiteID}/{PaymentMode}/{BranchID}/{ChequeNo}/{UserType}/{CollectionType}
       new AgentPlotRecordDisplayAsyncTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,url);
        return view;
    }

    private class AgentPlotRecordDisplayAsyncTask extends AsyncTask<String, Void, String> {

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
            } else if (content.equals("null")) {
                Toast toast2 = Toast.makeText(getActivity(), "No Record Found", Toast.LENGTH_LONG);
                toast2.setGravity(Gravity.TOP, 25, 500);
                View view1 = toast2.getView();
                view1.setBackgroundResource(R.drawable.toast_drawablecolor);
                toast2.show();

            }

        }

        private void displayagentrecord(String response) {
            ajentplotreportdata = new ArrayList<com.lucknow.quaere.shinecity_mlmpanel.Model.AgentPlotReportData>();
            try {
                JSONArray jArray = new JSONArray(response);
                int count = 0;
                for (int i = 0; i < jArray.length(); i++) {
                    if (jArray != null) {
                        count++;
                    }
                    JSONObject jObj = jArray.getJSONObject(i);
                    String plotholder = jObj.getString("PloHolderName");
                    String member = jObj.getString("MemberName");
                    String depoby = jObj.getString("DepositedBy");
                    String plotno = jObj.getString("PlotNo");
                    String receipt = jObj.getString("RecieptNo");
                    String mode = jObj.getString("PaymentMode");
                    String pmttype = jObj.getString("PaymentType");
                    String status = jObj.getString("PaymentStatus");
                    String amount = jObj.getString("PaidAmount");

                    int itemCount = jArray.length();
                    ajentplotreportdata.add(new com.lucknow.quaere.shinecity_mlmpanel.Model.AgentPlotReportData(plotholder, member, depoby, plotno, receipt, mode, pmttype,amount,status));


                }
                int no = count;
                String aString = Integer.toString(no);
                String strI = String.valueOf(no);
                //  tv_no.setText(strI);

                agentPlotReportDataArrayAdapter = new AgentPlotReportArrayAdapter(getActivity(), R.layout.adapter_agentplotreportdat, ajentplotreportdata);

                listView.setAdapter(agentPlotReportDataArrayAdapter);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    private class AgentPlotReportArrayAdapter extends ArrayAdapter<com.lucknow.quaere.shinecity_mlmpanel.Model.AgentPlotReportData> {
        int deepColor = Color.parseColor("#fde9ec");
        int deepColor2 = Color.parseColor("#defde0");
        private int[] colors = new int[] { deepColor, deepColor2 };
        private ArrayList<com.lucknow.quaere.shinecity_mlmpanel.Model.AgentPlotReportData> agentPlotReportDatas;
        private Context context;
        // private TextView Date,Month,Year,Amount;


        public AgentPlotReportArrayAdapter(Context context, int resource, ArrayList<com.lucknow.quaere.shinecity_mlmpanel.Model.AgentPlotReportData> agentPlotReportDatas) {
            super(context, resource, agentPlotReportDatas);
            this.agentPlotReportDatas = agentPlotReportDatas;
            this.context = context;
        }

        private class ViewHolder {
            TextView plotholder;
            TextView member;
            TextView depoby;
            TextView  plotno;
            TextView recepit;
            TextView mode;
            TextView pmttype;
            ImageView status;
            TextView amount;


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
                convertView = vi.inflate(R.layout.adapter_agentplotreportdat, null);
                int colorPos = position % colors.length;
                convertView.setBackgroundColor(colors[colorPos]);
                holder = new ViewHolder();
                holder.plotholder = (TextView) convertView.findViewById(R.id.tv_agent_plotholder);
                //  holder.memberid = (TextView) convertView.findViewById(R.id.txt_memberid);
                holder.member = (TextView) convertView.findViewById(R.id.tv_agent_member);
                //   holder.pareientid = (TextView) convertView.findViewById(R.id.txt_parenid);
              //  holder.depoby = (TextView) convertView.findViewById(R.id.tv_agent_depositedby);
                holder.plotno = (TextView) convertView.findViewById(R.id.tv_agent_plotno);
            //    holder.recepit = (TextView) convertView.findViewById(R.id.tv_agent_receipt);
              //  holder.pmtdate = (TextView) convertView.findViewById(R.id.tv_agent_pmtdate);
             //   holder.mode = (TextView) convertView.findViewById(R.id.tv_agent_mode);
                holder.pmttype = (TextView) convertView.findViewById(R.id.tv_agent_pmttype);
                holder.status = (ImageView) convertView.findViewById(R.id.tv_agent_status);
                holder.amount = (TextView) convertView.findViewById(R.id.tv_agent_amt);


                com.lucknow.quaere.shinecity_mlmpanel.Model.AgentPlotReportData data = agentPlotReportDatas.get(position);
                holder.plotholder.setText(data.getPlotholder());
                holder.member.setText(data.getMember());
               // holder.depoby.setText(data.getDepositedby());
                holder.plotno.setText(data.getPlotno());
              //  holder.recepit.setText(data.getRecepit());
             //   holder.mode.setText(data.getMode());
                holder.pmttype.setText(data.getPmttype());
                holder.amount.setText(data.getAmount());
                // holder.status.setText(data.getStatus());
                String s = (data.getStatus().toString());
                if(s.contains("P")) {
                    //  holder.status.setTextColor(Color.parseColor("#4B0082"));
                    //  holder.status.setText("P");
                    holder.status.setBackgroundResource(R.drawable.pending);

                }else if(s.contains("B")){
                    holder.status.setBackgroundResource(R.drawable.blocked);
                    // holder.status.setTextColor(Color.parseColor("#FF0000"));
                    // holder.status.setText("B");
                }else if(s.contains("C")){
                    holder.status.setBackgroundResource(R.drawable.confirm);
                    // holder.status.setTextColor(Color.parseColor("#368200"));
                    //  holder.status.setText("C");
                }
                convertView.setTag(holder);

            } else {

                holder = (ViewHolder) convertView.getTag();

            }
            com.lucknow.quaere.shinecity_mlmpanel.Model.AgentPlotReportData data = agentPlotReportDatas.get(position);
            holder.plotholder.setText(data.getPlotholder());
            holder.member.setText(data.getMember());
           // holder.depoby.setText(data.getDepositedby());
            holder.plotno.setText(data.getPlotno());
          //  holder.recepit.setText(data.getRecepit());
         //   holder.mode.setText(data.getMode());
            holder.pmttype.setText(data.getPmttype());
            holder.amount.setText(data.getAmount());
           // holder.status.setText(data.getStatus());

            String s = (data.getStatus().toString());
            if(s.contains("P")) {
                //  holder.status.setTextColor(Color.parseColor("#4B0082"));
                //  holder.status.setText("P");
                holder.status.setBackgroundResource(R.drawable.pending);

            }else if(s.contains("B")){
                holder.status.setBackgroundResource(R.drawable.blocked);
                // holder.status.setTextColor(Color.parseColor("#FF0000"));
                // holder.status.setText("B");
            }else if(s.contains("C")){
                holder.status.setBackgroundResource(R.drawable.confirm);
                // holder.status.setTextColor(Color.parseColor("#368200"));
                //  holder.status.setText("C");
            }

            return convertView;

        }


    }

}
