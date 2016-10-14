package com.lucknow.quaere.shinecity_mlmpanel;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.app.Fragment;
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

import com.lucknow.quaere.shinecity_mlmpanel.Model.ModelData;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;


public class DownlineReportFragment extends Fragment implements View.OnClickListener{
    private ArrayList<ModelData> modelData = new ArrayList<ModelData>();
    private String selectedtext;
    private EditText fromDateEtxt;
    private EditText toDateEtxt;
    private Spinner mySpinner;
    private ArrayList<String> dropdwonlist;
    private Button getreportsbutton;
    private  String FROM;
    private String TO;
    private String TYPE;
    private  String VENDERID;
    private String TOTAL;
    private DatePickerDialog fromDatePickerDialog;
    private DatePickerDialog toDatePickerDialog;
    private SimpleDateFormat dateFormatter;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view=inflater.inflate(R.layout.fragment_downline_report, container, false);
        fromDateEtxt = (EditText)view.findViewById(R.id.edt_fromdate);
        // fromDateEtxt.setInputType(InputType.TYPE_NULL);
        toDateEtxt = (EditText)view. findViewById(R.id.edt_todate);
        // toDateEtxt.setInputType(InputType.TYPE_NULL);
        getreportsbutton =(Button)view.findViewById(R.id.btn_getreports);
        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
      /*  Calendar c = Calendar.getInstance();
        fromDateEtxt.setText(dateFormatter.format(c.getTime()));
        toDateEtxt.setText(dateFormatter.format(c.getTime()));*/
        setDateTimeField();
        mySpinner=(Spinner)view.findViewById(R.id.my_spinner);
          dropdwonlist = new ArrayList<String>();
      /*  dropdwonlist.add("All Downline");
        dropdwonlist.add("Left Downline");
        dropdwonlist.add("Right Downline");*/

        HashMap<String,String> map =new LinkedHashMap<>();
            map.put("All Downline","0");
            map.put("Left Downline","1");
            map.put("Right Downline", "2");


             for (Map.Entry<String, String> entry : map.entrySet()) {
                 System.out.println("Key : " + entry.getKey() + " Value : " + entry.getValue());
                       dropdwonlist.add(entry.getKey());
                     modelData.add(new ModelData(entry.getKey(),entry.getValue()));
             }


        mySpinner.setAdapter(new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_dropdown_item,
                dropdwonlist));
        String dataitext = mySpinner.getSelectedItem().toString();

        mySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ModelData td = modelData.get(position);
                selectedtext = td.getDisplayname();

               // selectedtext = mySpinner.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        getreportsbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fromDateEtxt.getText().toString().equals("") || toDateEtxt.getText().toString().equals("")) {
                    Toast toast = Toast.makeText(getActivity(), "Select  Date ", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.TOP, 25, 500);
                    View view1 = toast.getView();
                    view1.setBackgroundResource(R.drawable.toast_drawablecolor);
                    toast.show();

                } else {

                    FROM = fromDateEtxt.getText().toString();
                    TO = toDateEtxt.getText().toString();
                    DownlineRecordDisplayFragment downlineRecordDisplayFragment = new DownlineRecordDisplayFragment();
                    Bundle args = new Bundle();

                    args.putString("PARAM3", selectedtext);
                    args.putString("PARAM1", FROM);
                    args.putString("PARAM2", TO);
                    downlineRecordDisplayFragment.setArguments(args);
                     getFragmentManager().beginTransaction().replace(R.id.mainContent, downlineRecordDisplayFragment).addToBackStack(null).commit();

/*
                    //  new DatewiseTotalTransactionAsyncTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,VENDERID,FROM,TO,TYPE);
                    Intent intent = new Intent(DatewiseTransactionActivity.this, DatewiseTransactionsShowDataActivity.class);
                    intent.putExtra("FROMDATE", FROM);
                    intent.putExtra("TODATE", TO);
                    intent.putExtra("TYPE", TYPE);
                    intent.putExtra("TOTAL", TOTAL);
                    startActivity(intent);*/
                }
            }
        });
        return view;
    }

    @Override
    public void onClick(View v) {
        if(v == fromDateEtxt) {
            fromDatePickerDialog.show();
        } else if(v == toDateEtxt) {
            toDatePickerDialog.show();
        }
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

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        toDatePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                toDateEtxt.setText(dateFormatter.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
    }




}
