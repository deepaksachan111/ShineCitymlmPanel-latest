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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.lucknow.quaere.shinecity_mlmpanel.Model.BusinessData.SelfIncentiveAdapterdata;

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


public class SelfIncentiveDisplayFragment extends Fragment {
    private ArrayAdapter selfIncentiveAdapterdataArrayAdapter;
    private  ArrayList<SelfIncentiveAdapterdata> selfIncentiveAdapterdatas;
    private ListView listView;
   private  TextView tv_norecord ;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_self_incentive_display, container, false);
        listView  = (ListView)view.findViewById(R.id.listview_selfincentive);
        tv_norecord =(TextView)view.findViewById(R.id.tv_selfincentive_norecord);
    /*     String formdate = getArguments().getString("FROM");
        String todate = getArguments().getString("TO");
        String payno = getArguments().getString("PAYNO");
        if(payno.equals("1")){
            payno = "-1";
        }*/
       // MLM_DirectIncome/{FK_MemID}/{FromDate}/{ToDate}/{PayoutNo}
        SessionManager sessionManager = new SessionManager(getActivity());
        HashMap<String,String> map = sessionManager.getUserDetails();
        String memid = map.get(SessionManager.KEY_MEMID);
        String url = "http://demo8.mlmsoftindia.com/ShinePanel.svc/MLM_DirectIncome/"+memid+"/-1/-1/-1";
        new SelfIncentiveDisplayAsyncTask().execute(url);
        return view;
    }
    private class SelfIncentiveDisplayAsyncTask extends AsyncTask<String, Void, String> {

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

        protected void onPostExecute(String content) {
            dialog.dismiss();

            if (error) {

                Toast toast = Toast.makeText(getActivity(), content, Toast.LENGTH_LONG);
                toast.setGravity(Gravity.TOP, 25, 500);
                View view1 = toast.getView();
                view1.setBackgroundResource(R.drawable.toast_drawablecolor);
                toast.show();
            } else if (content.equals("[]")) {
                tv_norecord.setVisibility(View.VISIBLE);
                tv_norecord.setText("No Record Found");
                /*Toast toast2 = Toast.makeText(getActivity(), "No Record Found", Toast.LENGTH_LONG);
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
               displayagentrecord(content);


            }

        }

       private void displayagentrecord(String response) {
     selfIncentiveAdapterdatas = new ArrayList<SelfIncentiveAdapterdata>();
            try {
                JSONArray jArray = new JSONArray(response);


                for (int i = 0; i < jArray.length(); i++) {
                    JSONObject jObj = jArray.getJSONObject(i);
                    String id = jObj.getString("LoginID");
                    String name = jObj.getString("Name");
                    String currentdate = jObj.getString("currentdate");
                    String payoutNo = jObj.getString("PayoutNo");
                    String closingDate = jObj.getString("ClosingDate");
                    String amount = jObj.getString("Amount");
                    selfIncentiveAdapterdatas.add(new SelfIncentiveAdapterdata(id,name,currentdate,payoutNo,closingDate,amount));
                }

              selfIncentiveAdapterdataArrayAdapter = new SelfIncentiveDisplayAdapter(getActivity(),R.layout.adapterselfincentivedisplay,selfIncentiveAdapterdatas);
                listView.setAdapter(selfIncentiveAdapterdataArrayAdapter);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
    private class SelfIncentiveDisplayAdapter extends ArrayAdapter<SelfIncentiveAdapterdata> {
        int graycolor = Color.parseColor("#EEEEEE");
        int whitecolor = Color.parseColor("#FAF9F9");
        private int[] colors = new int[] { graycolor, whitecolor };
        ArrayList<SelfIncentiveAdapterdata> selfincentivedata;
        Context context;
        public SelfIncentiveDisplayAdapter(Context context, int resource,ArrayList<SelfIncentiveAdapterdata> selfincentivedata) {
            super(context, resource, selfincentivedata);
            this.context = context;
            this.selfincentivedata = selfincentivedata;
        }
        private class  ViewHolder{
            private TextView fromid;
            private TextView membername;
            private TextView currentdate;
            private TextView payoutno;
            private TextView closingdate;
            private TextView amount;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder = null;
            if(convertView == null){
                LayoutInflater layoutInflater =(LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = layoutInflater.inflate(R.layout.adapterselfincentivedisplay,null);
                viewHolder = new ViewHolder();
                int colorPos = position % colors.length;
                convertView.setBackgroundColor(colors[colorPos]);
                viewHolder.fromid = (TextView) convertView.findViewById(R.id.tv_selfincentive_fromid);
                viewHolder.membername = (TextView) convertView.findViewById(R.id.tv_selfincentive_membername);
                viewHolder.currentdate = (TextView) convertView.findViewById(R.id.tv_selfincentive_currentdate);
                viewHolder.payoutno = (TextView) convertView.findViewById(R.id.tv_selfincentive_payoutno);
                viewHolder.closingdate = (TextView) convertView.findViewById(R.id.tv_selfincentive_closedate);
                viewHolder.amount = (TextView) convertView.findViewById(R.id.tv_selfincentive_amount);
               ;

                //  holder.memberid = (TextView) convertView.findViewById(R.id.txt_memberid);

                convertView.setTag(viewHolder);
            }else {
                int colorPos = position % colors.length;
                convertView.setBackgroundColor(colors[colorPos]);
                viewHolder = (ViewHolder)convertView.getTag();
            }
            SelfIncentiveAdapterdata data = selfincentivedata.get(position);
            viewHolder.fromid.setText(data.getFromid());
            viewHolder.membername.setText(data.getMembername());
            viewHolder.currentdate.setText(data.getCurrentdate());
            viewHolder.payoutno.setText(data.getPayoutno());
            viewHolder.closingdate.setText(data.getClosingdate());
            viewHolder.amount.setText(data.getAmount());


            return  convertView;
        }
    }

}
