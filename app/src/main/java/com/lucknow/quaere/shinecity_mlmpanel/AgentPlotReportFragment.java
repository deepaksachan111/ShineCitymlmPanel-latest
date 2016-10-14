package com.lucknow.quaere.shinecity_mlmpanel;

import android.app.DatePickerDialog;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;


public class AgentPlotReportFragment extends Fragment implements View.OnClickListener {
    private  String val_paymenttype,val_paymentmode,val_epinno,val_chequq,val_fromdate,val_todate;
    private Spinner spi_paymenttype, spi_paymentmode;
   private EditText edt_epinno, edt_chequeno;

    private ArrayList<com.lucknow.quaere.shinecity_mlmpanel.Model.BusinessData.PaymentTypeData> paymentTypeDatas = new ArrayList<com.lucknow.quaere.shinecity_mlmpanel.Model.BusinessData.PaymentTypeData>();
    private ArrayList<com.lucknow.quaere.shinecity_mlmpanel.Model.BusinessData.PaymentStatusData> paymentmodedata = new ArrayList<com.lucknow.quaere.shinecity_mlmpanel.Model.BusinessData.PaymentStatusData>();
    private ArrayList<com.lucknow.quaere.shinecity_mlmpanel.Model.BusinessData.BusinessOfData> epinnodata = new ArrayList<com.lucknow.quaere.shinecity_mlmpanel.Model.BusinessData.BusinessOfData>();
    private ArrayList<com.lucknow.quaere.shinecity_mlmpanel.Model.BusinessData.PaymentProceedData> chequedata = new ArrayList<com.lucknow.quaere.shinecity_mlmpanel.Model.BusinessData.PaymentProceedData>();

    ArrayList<String> paymenttypelist = new ArrayList<String>();
    ArrayList<String> paymentmodelist = new ArrayList<String>();
    ArrayList<String> epinnolist = new ArrayList<String>();
    ArrayList<String> chequenolist = new ArrayList<String>();

    private EditText fromDateEtxt;
    private EditText toDateEtxt;
    private DatePickerDialog fromDatePickerDialog;
    private DatePickerDialog toDatePickerDialog;
    private SimpleDateFormat dateFormatter;
    private RadioGroup radioGroup;
    private RadioButton radioButton;
    private Button btn_getDetails;

    public AgentPlotReportFragment() {
        // Required empty public constructor
    }

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
    private void findviewbyID(View v){
        spi_paymenttype =(Spinner)v.findViewById(R.id.spi_paymenttype);
        spi_paymentmode =(Spinner)v.findViewById(R.id.spi_paymentmode);
        edt_chequeno =(EditText)v.findViewById(R.id.edt_chequeno);
        edt_epinno =(EditText)v.findViewById(R.id.edt_epinno);
        fromDateEtxt=(EditText)v.findViewById(R.id.edt_ajentplotfromdate);
        toDateEtxt=(EditText)v.findViewById(R.id.edt_ajentplottodate);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
      final View view = inflater.inflate(R.layout.fragment_agent_plot_report, container, false);

        radioGroup = (RadioGroup)view. findViewById(R.id.myRadioGroup);

        btn_getDetails =(Button)view.findViewById(R.id.btn_getagentdetails);

        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
          findviewbyID(view);
          setDateTimeField();

        HashMap<String,String> mappaymenttype =new LinkedHashMap<>();
        mappaymenttype.put("Booking", "B");
        mappaymenttype.put("All", "-1");
        mappaymenttype.put("Allotment", "A");
        mappaymenttype.put("Installment", "I");


        for (Map.Entry<String, String> entry : mappaymenttype.entrySet()) {
            System.out.println("Key : " + entry.getKey() + " Value : " + entry.getValue());
            paymenttypelist.add(entry.getKey());
            paymentTypeDatas.add(new com.lucknow.quaere.shinecity_mlmpanel.Model.BusinessData.PaymentTypeData(entry.getKey(), entry.getValue()));
        }
        spi_paymenttype.setAdapter(new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_dropdown_item,
                paymenttypelist));
        spi_paymenttype.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                com.lucknow.quaere.shinecity_mlmpanel.Model.BusinessData.PaymentTypeData td = paymentTypeDatas.get(position);
                val_paymenttype = td.getPaymenttypevalue();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });
        HashMap<String,String> mappaymentmode =new LinkedHashMap<>();
        mappaymentmode.put("All", "-1");
        mappaymentmode.put("Cash", "Cash");
        mappaymentmode.put("Cheque", "Cheque");
        mappaymentmode.put("Demand Draft", "Demand_Draft");
        mappaymentmode.put("Bankers Cheque", "Bankers_Cheque");
        mappaymentmode.put("NEFT", "NEFT");


        for (Map.Entry<String, String> entry : mappaymentmode.entrySet()) {
            System.out.println("Key : " + entry.getKey() + " Value : " + entry.getValue());
            paymentmodelist.add(entry.getKey());
            paymentmodedata.add(new com.lucknow.quaere.shinecity_mlmpanel.Model.BusinessData.PaymentStatusData(entry.getKey(), entry.getValue()));
        }
        spi_paymentmode.setAdapter(new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_dropdown_item,
                paymentmodelist));
        spi_paymentmode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                com.lucknow.quaere.shinecity_mlmpanel.Model.BusinessData.PaymentStatusData td = paymentmodedata.get(position);
                val_paymentmode = td.getPaymentstatusvalue();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });


        btn_getDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                val_fromdate = fromDateEtxt.getText().toString();
                val_todate = toDateEtxt.getText().toString();
                val_epinno = edt_epinno.getText().toString();
                val_chequq = edt_chequeno.getText().toString();
                if (val_chequq.equals("") || val_epinno.equals("") || val_fromdate.equals("") || val_todate.equals("")) {
                    val_chequq = "-1";
                    val_epinno = "-1";
                    val_todate = "-1";
                    val_fromdate = "-1";
                }
                int selectedId = radioGroup.getCheckedRadioButtonId();
                radioButton = (RadioButton) view.findViewById(selectedId);
                String checkvalue = radioButton.getText().toString();
                if (checkvalue.equals("Downline")) {
                    checkvalue = "1";
                } else if (checkvalue.equals("Self")) {
                    checkvalue = "2";
                }
                AgentPlotReportDisplayFragment age = new AgentPlotReportDisplayFragment();
                Bundle args = new Bundle();
                // args.putString("PARAM3", selectedtext);
                args.putString("PARAM1", val_fromdate);
                args.putString("PARAM2", val_todate);
                args.putString("PARAM3", val_paymenttype);
                args.putString("PARAM4", val_paymentmode);
                args.putString("PARAM5", val_epinno);
                args.putString("PARAM7", checkvalue);
                args.putString("PARAM6", val_chequq);
                // String url = "http://demo8.mlmsoftindia.com/ShinePanel.svc/MLM_BusinessDetailsTable1/"+MemberID +"/"+fromdate+"/"+todate+"/"+busiofselectedtext1+"/"+paymenttypeselectedtext1+"/"+paymentstatusselectedtext1+"/"+paymentprocedselectedtext1+"/2000";

                age.setArguments(args);
                getFragmentManager().beginTransaction().replace(R.id.mainContent, age).addToBackStack(null).commit();


            }
        });
        return view;
    }


}
