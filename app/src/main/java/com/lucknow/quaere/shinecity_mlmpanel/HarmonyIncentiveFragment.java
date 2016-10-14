package com.lucknow.deepaksachan.shinecity_mlmpanel;

import android.app.DatePickerDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.lucknow.quaere.shinecity_mlmpanel.Model.SelfincentiveData;
import com.lucknow.quaere.shinecity_mlmpanel.R;

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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;


public class HarmonyIncentiveFragment extends Fragment implements View.OnClickListener {
    private EditText fromDateEtxt;
    private EditText toDateEtxt;
    private DatePickerDialog fromDatePickerDialog;
    private DatePickerDialog toDatePickerDialog;
    private Spinner spinner_payoutno;
    private Button getdetails;
    private SimpleDateFormat dateFormatter;
    private List<String> dropdownlist;
    private ArrayList<SelfincentiveData> selfincentiveDatas;
    private String payoutno;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    private void setDateTimeField() {
        fromDateEtxt.setOnClickListener(this);
        toDateEtxt.setOnClickListener(this);

        Calendar newCalendar = Calendar.getInstance();
        fromDatePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                fromDateEtxt.setText(dateFormatter.format(newDate.getTime()));
            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        toDatePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                toDateEtxt.setText(dateFormatter.format(newDate.getTime()));
            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
    }

    @Override
    public void onClick(View v) {
        if (v == fromDateEtxt) {
            fromDatePickerDialog.show();
        } else if (v == toDateEtxt) {
            toDatePickerDialog.show();
        }
    }

    private void findviewbyid(View view) {

        fromDateEtxt = (EditText) view.findViewById(R.id.edt_hormonyincentive_fromdate);
        toDateEtxt = (EditText) view.findViewById(R.id.edt_hormonyincentive_todate);
        spinner_payoutno = (Spinner) view.findViewById(R.id.my_spinner_hormonyincentive);
        getdetails = (Button) view.findViewById(R.id.btn_getDetails_hormonyincentive);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_harmony_incentive, container, false);
        findviewbyid(view);
        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        setDateTimeField();
        String url = "http://demo8.mlmsoftindia.com/ShinePanel.svc/PayoutDropDown";
        //  {FromDate}/{ToDate}/{Status}/{EpinNo}/{PlotHolderID}/{PlotNo}/{DepositedMemId}
        new HarmonyIncentiveAsynctask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, url);
        getdetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fromdate = fromDateEtxt.getText().toString();
                String todate = toDateEtxt.getText().toString();
                String payn = payoutno;
                if (fromdate.equals("") && todate.equals("")) {
                    fromdate = "-1";
                    todate = "-1";
                }
                if (payoutno.equals("Select")) {
                    payoutno = "-1";
                }
                com.lucknow.quaere.shinecity_mlmpanel.HarmonyInsentiveDisplayFragment harmonyInsentiveDisplayFragment = new com.lucknow.quaere.shinecity_mlmpanel.HarmonyInsentiveDisplayFragment();
                Bundle bundle = new Bundle();
                bundle.putString("FROM", fromdate);
                bundle.putString("TO", todate);
                bundle.putString("PAYNO", payoutno);
                harmonyInsentiveDisplayFragment.setArguments(bundle);

                getFragmentManager().beginTransaction().replace(R.id.mainContent, harmonyInsentiveDisplayFragment).addToBackStack(null).commit();

            }
        });
        return view;
    }

    private class HarmonyIncentiveAsynctask extends AsyncTask<String, Void, String> {

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

        protected void onPostExecute(String content) {
             dialog.dismiss();

            if (error) {

                Toast toast = Toast.makeText(getActivity(), content, Toast.LENGTH_LONG);
                toast.setGravity(Gravity.TOP, 25, 500);
                View view1 = toast.getView();
                view1.setBackgroundResource(R.drawable.toast_drawablecolor);
                toast.show();
            } else if (content.equals("[]")) {
                Toast toast2 = Toast.makeText(getActivity(), "Server response failed", Toast.LENGTH_LONG);
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
            dropdownlist = new ArrayList<>();
            selfincentiveDatas = new ArrayList<>();
            try {
                JSONArray jArray = new JSONArray(response);


                for (int i = 0; i < jArray.length(); i++) {
                    JSONObject jObj = jArray.getJSONObject(i);
                    String payoutdate = jObj.getString("PayoutDate");
                    String payoutno = jObj.getString("PayoutNo");

                    dropdownlist.add(payoutdate);
                    int itemCount = jArray.length();
                    selfincentiveDatas.add(new SelfincentiveData(payoutdate, payoutno));
                }
                //Collections.reverse(dropdownlist);
                spinner_payoutno.setAdapter(new ArrayAdapter<String>(getActivity(),
                        android.R.layout.simple_spinner_dropdown_item,
                        dropdownlist));
                spinner_payoutno.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        SelfincentiveData td = selfincentiveDatas.get(position);

                        payoutno = td.getPayoutValue();
                        String s = payoutno;

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
}
