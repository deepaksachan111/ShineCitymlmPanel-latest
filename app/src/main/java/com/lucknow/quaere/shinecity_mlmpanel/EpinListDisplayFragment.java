package com.lucknow.quaere.shinecity_mlmpanel;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.lucknow.quaere.shinecity_mlmpanel.Model.EPinListData;

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


public class EpinListDisplayFragment extends Fragment {
    private ArrayAdapter<EPinListData> ePinListDataArrayAdapter;
    private ArrayList<EPinListData>  ePinListData;
    ListView listView;
    private TextView tv_no ;

    private Animation vanish;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_epin_list_display, container, false);
        listView =(ListView)v.findViewById(R.id.listview_epinlist);
        tv_no=(TextView)v.findViewById(R.id.txt_epin_no_records);
       vanish = AnimationUtils.loadAnimation(getActivity(), R.anim.vanish);

           SessionManager sessionManager = new SessionManager(getActivity());
            HashMap<String,String> map = sessionManager.getUserDetails();
            String loginid = map.get(SessionManager.KEY_LOGINID);
             String fromdate = getArguments().getString("PARAM1");
             String todate = getArguments().getString("PARAM2");
             String status =getArguments().getString("PARAM3");
             String epino =getArguments().getString("PARAM4");
         String url ="http://demo8.mlmsoftindia.com/ShinePanel.svc/MLM_EPinReport/"+fromdate+"/"+todate+"/"+status+"/"+"-1/-1/-1/"+loginid;
      //  {FromDate}/{ToDate}/{Status}/{EpinNo}/{PlotHolderID}/{PlotNo}/{DepositedMemId}
        new EPinlistAsynctask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,url);
        return v;
    }

    private class EPinlistAsynctask extends AsyncTask<String, Void, String> {

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
            dialog.setMessage("Getting your data... Please wait...");
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
            ePinListData = new ArrayList<EPinListData>();


            try {
                JSONArray jArray = new JSONArray(response);
                int count = 0;
                for (int i = 0; i < jArray.length(); i++) {
                    if (jArray != null) {
                        count++;
                    }
                    JSONObject jObj = jArray.getJSONObject(i);
                    String cdate = jObj.getString("CreatedDate");
                    String epin = jObj.getString("ePinNo");
                    String owner = jObj.getString("tOwner");
                    String status = jObj.getString("PinStatus");


                    int itemCount = jArray.length();

                     ePinListData.add(new EPinListData(cdate,epin,owner,status) );

                }

                String aString = Integer.toString(count);
                String strI = String.valueOf(count);
                tv_no.setText(strI);

                ePinListDataArrayAdapter  = new EPinListArrayAdapter(getActivity(), R.layout.adpter_epinlistdisplay, ePinListData);
                 listView.setAdapter(ePinListDataArrayAdapter);

            /*    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        EPinListData s = ePinListDataArrayAdapter.getItem(position);
                       String status = s.getStatus();
                        if(status.equals("Unused")){
                         *//*   EPinRegistrationFragment ePinRegistrationFragment = new EPinRegistrationFragment();
                            Bundle bundle = new Bundle();
                            bundle.putString("EPIN", s.getEnpinno());
                            bundle.putString("OWNER",s.getOwner());
                            ePinRegistrationFragment.setArguments(bundle);
                            getFragmentManager().beginTransaction().replace(R.id.mainContent,ePinRegistrationFragment).commit();*//*

                            Intent intent = new Intent(getActivity(), EPinRegistrationActivity.class);
                            intent.putExtra("EPIN", s.getEnpinno());
                            intent.putExtra("OWNER",s.getOwner());
                            startActivity(intent);
                        }

                    }
                });*/

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    private class EPinListArrayAdapter extends ArrayAdapter<EPinListData> {
        private ArrayList<EPinListData> epinlistarraydata;
        private Context context;
        int deepColor = Color.parseColor("#fde9ec");
        int deepColor2 = Color.parseColor("#defde0");
        private int[] colors = new int[] {deepColor,deepColor2 };
        // private TextView Date,Month,Year,Amount;


        public EPinListArrayAdapter(Context context, int resource, ArrayList<EPinListData> epinlistarraydata) {
            super(context, resource, epinlistarraydata);
            this.epinlistarraydata = epinlistarraydata;
            this.context = context;
        }

        private class  ViewHolder{
            TextView date;
            TextView epin;
            TextView owner;
            TextView  status;
            TextView tv_newregistration;
            int colorPos;

        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final ViewHolder holder;
            if (convertView == null) {
                LayoutInflater vi = (LayoutInflater) getActivity().getSystemService(
                        Context.LAYOUT_INFLATER_SERVICE);
                convertView = vi.inflate(R.layout.adpter_epinlistdisplay, null);
                holder = new ViewHolder();
                final Animation vanish = AnimationUtils.loadAnimation(getActivity(), R.anim.vanish);

                int colorPos = position % colors.length;
                convertView.setBackgroundColor(colors[colorPos]);

                holder.date = (TextView) convertView.findViewById(R.id.tv_epin_createdby);
                holder.epin = (TextView) convertView.findViewById(R.id.tv_epin_epinno);
                holder.owner = (TextView) convertView.findViewById(R.id.tv_epin_owner);
                holder.status = (TextView) convertView.findViewById(R.id.tv_epin_status);
                holder.tv_newregistration = (TextView) convertView.findViewById(R.id.tv_newregistration);
                holder.tv_newregistration.setAnimation(vanish);
                holder.tv_newregistration .setPaintFlags(holder.tv_newregistration.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);


                convertView.setTag(holder);

            } else {
                int colorPos = position % colors.length;
                convertView.setBackgroundColor(colors[colorPos]);
                holder = (ViewHolder) convertView.getTag();

            }
            final EPinListData data = epinlistarraydata.get(position);
            if(data !=null){


            holder.date.setText(data.getCreateddate());
            holder.epin.setText(data.getEnpinno());
            holder.owner.setText(data.getOwner());
            holder.status.setText(data.getStatus());

                if(data.getStatus().equals("Used")){
                    holder.tv_newregistration.setText("Registered");
                    holder.tv_newregistration .setPaintFlags(0);

                }
            holder.tv_newregistration.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String status = data.getStatus();
                    holder.tv_newregistration.startAnimation(vanish);
                    if (status.equals("Unused")) {
                         /*   EPinRegistrationFragment ePinRegistrationFragment = new EPinRegistrationFragment();
                            Bundle bundle = new Bundle();
                            bundle.putString("EPIN", s.getEnpinno());
                            bundle.putString("OWNER",s.getOwner());
                            ePinRegistrationFragment.setArguments(bundle);
                            getFragmentManager().beginTransaction().replace(R.id.mainContent,ePinRegistrationFragment).commit();*/

                        Intent intent = new Intent(getActivity(), EPinRegistrationActivity.class);
                        intent.putExtra("EPIN", data.getEnpinno());
                        intent.putExtra("OWNER", data.getOwner());
                        startActivity(intent);
                    }

                }
            });

            }


            return convertView;

        }


    }
}
