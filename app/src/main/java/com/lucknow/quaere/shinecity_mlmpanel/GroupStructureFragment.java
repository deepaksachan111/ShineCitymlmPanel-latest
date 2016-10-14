package com.lucknow.quaere.shinecity_mlmpanel;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.lucknow.quaere.shinecity_mlmpanel.Model.ModelData;

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


public class GroupStructureFragment extends Fragment {

    ArrayList<ModelData> GroupData;
    GroupdataArraydapter groupdataArraydapter;
    ListView listView;
    private  ProgressDialog dialog;
    String id ;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_group_structure, container, false);
       // ListView listView = (ListView)view. findViewById(R.id.list_viewgroupdata);

        WebView webView = (WebView)view.findViewById(R.id.myWebView);

        webView.setWebViewClient(new MyWebViewClient());

        SessionManager sessionManager = new SessionManager(getActivity());
        HashMap<String,String> map = sessionManager.getUserDetails();
        String id= map.get(SessionManager.KEY_MEMID);
        String password = map.get(SessionManager.KEY_PASSWORD);

        dialog =
                new ProgressDialog(getActivity(),R.style.ThemeMyDialog);
        dialog.setMessage("Getting data... Please wait...");
        dialog.setCancelable(true);
        dialog.show();
       String url = "http://csw.shinecityinfra.com/mlmpanel/groupstructuremobapp.aspx?MemID="+id ;
     //  String url = "http://google.com";
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(url);

       //String url = "http://demo8.mlmsoftindia.com/ShinePanel.svc/MLM_GroupStructure/4/2";
      //  new  GroupStructureAsyncTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,url);
        return view;

    }

    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            dialog.dismiss();
            return true;
        }
        @Override
        public void onPageFinished(WebView view, String url) {
            // TODO Auto-generated method stub
            super.onPageFinished(view, url);

           dialog.dismiss();
        }
    }

    private class GroupStructureAsyncTask extends AsyncTask<String, Void, String> {
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
                View view1 = toast.getView();
                view1.setBackgroundResource(R.drawable.toast_drawablecolor);
                toast.show();
            } else {
                displayCountryList(content);
            }
        }

    }

    private void displayCountryList(String response) {

        try {

           // JSONObject jsonObj = new JSONObject(response);
            JSONArray jArray = new JSONArray(response);
            GroupData = new ArrayList<ModelData>();
            for (int i = 0; i < jArray.length(); i++) {
                String id;
                String name;
                JSONObject jObj = jArray.getJSONObject(i);
                name = jObj.getString("DayText");
                id = jObj.getString("MonthText");

              GroupData.add(new ModelData(id,name));
            }

            groupdataArraydapter = new GroupdataArraydapter(getActivity(), R.layout.groupdataarrayadapter, GroupData);

            listView.setAdapter(groupdataArraydapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private class GroupdataArraydapter extends ArrayAdapter<ModelData> {
        Context context;
        ArrayList<ModelData> data;
       // String datass[] ;

        public GroupdataArraydapter(Context context, int groupdataarrayadapter, ArrayList<ModelData> groupData) {
            super(context, groupdataarrayadapter, groupData);
            this.context = context;
            data = groupData;
        }

        private class ViewHolder {
            TextView textview1;
            TextView textview2;

        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView= inflater.inflate(R.layout.groupdataarrayadapter,null);

                holder.textview1 = (TextView)convertView.findViewById(R.id.tv_name1);
                holder.textview2 = (TextView)convertView.findViewById(R.id.tv_name2);
                convertView.setTag(holder);
            } else {
            holder = (ViewHolder)convertView.getTag();

            }
            ModelData datas = data.get(position);
           // String dat = datass[position];
                holder.textview1.setText(datas.getUserid());
              holder.textview2.setText(datas.getDisplayname());
            return convertView;
        }
    }


}
