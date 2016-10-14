package com.lucknow.quaere.shinecity_mlmpanel;

import android.app.DatePickerDialog;
import android.app.Fragment;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;


public class PayoutDetailsFragment extends Fragment implements View.OnClickListener {

    private EditText fromDateEtxt;
    private EditText toDateEtxt;
    private DatePickerDialog fromDatePickerDialog;
    private DatePickerDialog toDatePickerDialog;
    private  Button getreportsbutton;
    private SimpleDateFormat dateFormatter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View view = inflater.inflate(R.layout.fragment_payout_details, container, false);
        fromDateEtxt = (EditText)view.findViewById(R.id.edt_fromdatepayoutdetails);
        // fromDateEtxt.setInputType(InputType.TYPE_NULL);
        toDateEtxt = (EditText)view. findViewById(R.id.edt_todatepayoutdetails);
        // toDateEtxt.setInputType(InputType.TYPE_NULL);
        getreportsbutton =(Button)view.findViewById(R.id.btn_getdetails_payoutdetails);
        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        Calendar c = Calendar.getInstance();
        fromDateEtxt.setText(dateFormatter.format(c.getTime()));
        toDateEtxt.setText(dateFormatter.format(c.getTime()));
        setDateTimeField();
        getreportsbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fromdate,todate;
                fromdate =fromDateEtxt.getText().toString();
                todate =toDateEtxt.getText().toString();
                if(fromdate.equals("")|| todate.equals("")){
                    Toast toast2 = Toast.makeText(getActivity(), "Please select Date", Toast.LENGTH_LONG);
                    toast2.setGravity(Gravity.TOP, 25, 500);
                    View view1 = toast2.getView();
                    view1.setBackgroundResource(R.drawable.toast_drawablecolor);
                    toast2.show();
                }else {
                    com.lucknow.quaere.shinecity_mlmpanel.PayoutReportDisplayFragment payoutReportDisplayFragment = new com.lucknow.quaere.shinecity_mlmpanel.PayoutReportDisplayFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("PARAM1", fromdate);
                    bundle.putString("PARAM2", todate);
                    payoutReportDisplayFragment.setArguments(bundle);
                    getFragmentManager().beginTransaction().replace(R.id.mainContent, payoutReportDisplayFragment).addToBackStack(null).commit();
                }
            }
        });
        return view;
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

    @Override
    public void onClick(View v) {
        if(v == fromDateEtxt) {
            fromDatePickerDialog.show();
        } else if(v == toDateEtxt) {
            toDatePickerDialog.show();
        }
    }
}
