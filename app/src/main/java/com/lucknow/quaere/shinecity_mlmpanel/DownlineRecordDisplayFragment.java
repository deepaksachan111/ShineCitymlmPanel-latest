package com.lucknow.quaere.shinecity_mlmpanel;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
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


import com.lucknow.quaere.shinecity_mlmpanel.Model.DownlineRecordData;

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


public class DownlineRecordDisplayFragment extends Fragment {

   private DownlineReportdisplayArrayadapter downlineReportdisplayArrayadapter;
    private ArrayList<DownlineRecordData>  plotReportDatas;
    private ListView listView;
    private TextView tv_no;



    public DownlineRecordDisplayFragment() {
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
        View view= inflater.inflate(R.layout.fragment_downline_report_display, container, false);
        listView =(ListView)view.findViewById(R.id.listview_plotreportdisplay);
        tv_no=(TextView)view.findViewById(R.id.txt_no_records);



                //listView.smoothScrollToPosition(0);




        SessionManager session = new SessionManager(getActivity());
        HashMap<String, String> user = session.getUserDetails();
        String loginid = user.get(SessionManager.KEY_LOGINID);

        String fromdate = getArguments().getString("PARAM1");
        String todate = getArguments().getString("PARAM2");
        String leg = getArguments().getString("PARAM3");
        String   url = "http://demo8.mlmsoftindia.com/ShinePanel.svc/MLM_PlotReport/"+loginid+"/"+fromdate+"/"+todate+"/"+leg;
        new DownlineRecordDisplayAsyncTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, url);
        return view;
    }

    private class DownlineRecordDisplayAsyncTask extends AsyncTask<String, Void, String> {

        private static final int REGISTRATION_TIMEOUT = 10 * 1000;
        private static final int WAIT_TIMEOUT = 50 * 1000;
        private final HttpClient httpclient = new DefaultHttpClient();
        final HttpParams params = httpclient.getParams();
        HttpResponse response;
        private String content = null;
        private boolean error = false;
        private ProgressDialog dialog = new ProgressDialog(getActivity(),R.style.ThemeMyDialog);

        protected void onPreExecute() {
            dialog.setMessage("Getting your data... Please wait...");
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
            Toast   toast = Toast.makeText(getActivity(),"Connection Server Failed", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP, 25, 500);
            View view1 = toast.getView();
            view1.setBackgroundResource(R.drawable.toast_drawablecolor);
            toast.show();
            getActivity().finish();
        }

        protected void onPostExecute(String content) {
            dialog.dismiss();

            if (error) {

                Toast   toast = Toast.makeText(getActivity(),content, Toast.LENGTH_LONG);
                toast.setGravity(Gravity.TOP, 25, 500);
                View view1 = toast.getView();
                view1.setBackgroundResource(R.drawable.toast_drawablecolor);
                toast.show();
            } else if (content.equals("[]")) {
             Toast  toast2= Toast.makeText(getActivity(), "No Record Found", Toast.LENGTH_LONG);
                toast2.setGravity(Gravity.TOP, 25, 500);
                View view1 = toast2.getView();
                view1.setBackgroundResource(R.drawable.toast_drawablecolor);
                toast2.show();
            } else if (content.equals("")) {
                Toast  toast2= Toast.makeText(getActivity(), "No Record Found", Toast.LENGTH_LONG);
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
                plotReportDatas = new ArrayList<DownlineRecordData>();
            try {
                JSONArray jArray = new JSONArray(response);
                int count =0;
                for (int i = 0; i < jArray.length(); i++) {
                    if (jArray != null) {
                        count++;
                    }
                    JSONObject jObj = jArray.getJSONObject(i);
                    String  membername = jObj.getString("DisplayName");
                    String memid = jObj.getString("LoginID");
                    String parentname = jObj.getString("ParentDisplayName");
                    String parentid = jObj.getString("ParentLoginID");
                    String totalAmount = jObj.getString("TotalAmount");
                    String status = jObj.getString("Status");
                    String date   = jObj.getString("JoiningDate");
                    int itemCount = jArray.length();
                     plotReportDatas.add(new DownlineRecordData(date, membername, memid, parentname, parentid, totalAmount, status));
                    }
                  int no= count;
                String aString = Integer.toString(no);
                String strI = String.valueOf(no);
                   tv_no.setText(strI);

             downlineReportdisplayArrayadapter = new DownlineReportdisplayArrayadapter(getActivity(),R.layout.adapter_downlinereport_display,plotReportDatas);

                listView.setAdapter(downlineReportdisplayArrayadapter);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
    public class DownlineReportdisplayArrayadapter extends ArrayAdapter<DownlineRecordData> {
        int deepColor = Color.parseColor("#fde9ec");
        int deepColor2 = Color.parseColor("#defde0");
        private int[] colors = new int[] {deepColor, deepColor2 };
        private ArrayList<DownlineRecordData> plotreportdata;
        private Context context;
        // private TextView Date,Month,Year,Amount;


        public DownlineReportdisplayArrayadapter(Context context, int resource, ArrayList<DownlineRecordData> datewisedatas) {
            super(context, resource, datewisedatas);
            this.plotreportdata = datewisedatas;
            this.context = context;
        }

        private class ViewHolder {
            TextView membername;
            TextView memberid;
            TextView parentname;
            TextView pareientid;
            TextView tatalamt;
            ImageView status;
            TextView date;
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
                convertView = vi.inflate(R.layout.adapter_downlinereport_display, null);
                int colorPos = position % colors.length;
                convertView.setBackgroundColor(colors[colorPos]);
                holder = new ViewHolder();
                holder.membername = (TextView) convertView.findViewById(R.id.txt_membername);
              //  holder.memberid = (TextView) convertView.findViewById(R.id.txt_memberid);
                holder.parentname = (TextView) convertView.findViewById(R.id.txt_parentname);
             //   holder.pareientid = (TextView) convertView.findViewById(R.id.txt_parenid);
                holder.tatalamt = (TextView) convertView.findViewById(R.id.txt_totalamt);
                holder.status = (ImageView) convertView.findViewById(R.id.txt_status);
                holder.date = (TextView) convertView.findViewById(R.id.txt_joiningdate);
                //  holder.type = (TextView) convertView.findViewById(R.id.txt_transhistorytype);
               // String s = holder.status.getText().toString();

                DownlineRecordData data = plotreportdata.get(position);

                holder.membername.setText(data.getMembername());
                //  holder.memberid.setText(data.getMemberid());
                holder.parentname.setText(data.getParentname());
                //  holder.pareientid.setText(data.getPareientid());
                holder.tatalamt.setText(data.getTatalamt());
                holder.date.setText(data.getDate());

                String s = (data.getStatus().toString());
                if(s.contains("Pending")) {
                  //  holder.status.setTextColor(Color.parseColor("#4B0082"));
                   // holder.status.setText(data.getStatus());
                    holder.status.setBackgroundResource(R.drawable.pending);
                }else if(s.contains("Blocked")){
                    //holder.status.setTextColor(Color.parseColor("#FF0000"));
                   // holder.status.setText(data.getStatus());
                    holder.status.setBackgroundResource(R.drawable.blocked);
                }else if(s.contains("Confirmed")){
                   // holder.status.setTextColor(Color.parseColor("#368200"));
                  //  holder.status.setText(data.getStatus());
                    holder.status.setBackgroundResource(R.drawable.confirm);
                }
                convertView.setTag(holder);

            } else {
                holder = (ViewHolder) convertView.getTag();
                int colorPos = position % colors.length;
                convertView.setBackgroundColor(colors[colorPos]);
                DownlineRecordData data = plotreportdata.get(position);

                holder.membername.setText(data.getMembername());
                //  holder.memberid.setText(data.getMemberid());
                holder.parentname.setText(data.getParentname());
                //  holder.pareientid.setText(data.getPareientid());
                holder.tatalamt.setText(data.getTatalamt());
                holder.date.setText(data.getDate());
                String s = (data.getStatus().toString());
                if(s.contains("Pending")) {
                    //  holder.status.setTextColor(Color.parseColor("#4B0082"));
                    // holder.status.setText(data.getStatus());
                    holder.status.setBackgroundResource(R.drawable.pending);
                }else if(s.contains("Blocked")){
                    //holder.status.setTextColor(Color.parseColor("#FF0000"));
                    // holder.status.setText(data.getStatus());
                    holder.status.setBackgroundResource(R.drawable.blocked);
                }else if(s.contains("Confirmed")){
                    // holder.status.setTextColor(Color.parseColor("#368200"));
                    //  holder.status.setText(data.getStatus());
                    holder.status.setBackgroundResource(R.drawable.confirm);
                }

            }


           // holder.status.setText(data.getStatus());

            return convertView;

        }



    }

}
