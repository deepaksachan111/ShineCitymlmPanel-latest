package com.lucknow.quaere.shinecity_mlmpanel;

import android.app.DatePickerDialog;
import android.app.Fragment;
import android.os.Bundle;
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

import com.lucknow.quaere.shinecity_mlmpanel.Model.BusinessData.BusinessOfData;
import com.lucknow.quaere.shinecity_mlmpanel.Model.BusinessData.PaymentProceedData;
import com.lucknow.quaere.shinecity_mlmpanel.Model.BusinessData.PaymentStatusData;
import com.lucknow.quaere.shinecity_mlmpanel.Model.BusinessData.PaymentTypeData;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;


public class BusinessReportFragment extends Fragment implements View.OnClickListener {
   private Spinner bussinessof,paymentstatus,paymenttype,payoutproceed;
    ArrayList<String> bussinessoflist = new ArrayList<String>();
    ArrayList<String> paymentstatuslist = new ArrayList<String>();
    ArrayList<String> paymenttypelist = new ArrayList<String>();
    ArrayList<String> payoutproceedlist = new ArrayList<String>();
    private EditText fromDateEtxt;
    private EditText toDateEtxt;
    private DatePickerDialog fromDatePickerDialog;
    private DatePickerDialog toDatePickerDialog;
    private SimpleDateFormat dateFormatter;
    private Button getreportsbutton;
    private ArrayList<BusinessOfData> businofdata = new ArrayList<BusinessOfData>();
    private ArrayList<PaymentStatusData> paymentStatusDatas = new ArrayList<PaymentStatusData>();
    private ArrayList<PaymentTypeData> paymentTypeDatas = new ArrayList<PaymentTypeData>();
    private ArrayList<PaymentProceedData> paymentProceedDatas = new ArrayList<PaymentProceedData>();
     private String busiofselectedtext1,paymenttypeselectedtext1,paymentstatusselectedtext1,paymentprocedselectedtext1;
    private  String url;
    private int mYear, mMonth, mDay;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.business_report_fragment, container, false);
        bussinessof=(Spinner)view.findViewById(R.id.business_of);
        paymentstatus=(Spinner)view.findViewById(R.id.busi_payment_status);
        paymenttype=(Spinner)view.findViewById(R.id.payment_type);
        payoutproceed=(Spinner)view.findViewById(R.id.payout_proceed);

        Calendar c = Calendar.getInstance();
      /*  mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);*/
        fromDateEtxt = (EditText)view.findViewById(R.id.edt_businessfromdate);
        // fromDateEtxt.setInputType(InputType.TYPE_NULL);
        toDateEtxt = (EditText)view. findViewById(R.id.edt_businesstodate);

        getreportsbutton =(Button)view.findViewById(R.id.btn_getbusinessreports);
        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        fromDateEtxt.setText(dateFormatter.format(c.getTime()));
        toDateEtxt.setText(dateFormatter.format(c.getTime()));
        setDateTimeField();

      /*  bussinessoflist.add("All");
        bussinessoflist.add("Self");
        bussinessoflist.add("Left Down");
        bussinessoflist.add("Right Down");*/

       /* paymentstatuslist.add("All");
        paymentstatuslist.add("Confirmed");
        paymentstatuslist.add("Pending");
        paymentstatuslist.add("Decline");*/

      /*  paymenttypelist.add("All");
        paymenttypelist.add("Booking");
        paymenttypelist.add("Allotment");
        paymenttypelist.add("Installment");*/

       /* payoutproceedlist.add("All");
        payoutproceedlist.add("Proceed");
        payoutproceedlist.add("Not Proceed");*/

        HashMap<String,String> mapbusinesslist =new LinkedHashMap<>();
        mapbusinesslist.put("Self", "Self");
        mapbusinesslist.put("All", "-1");
        mapbusinesslist.put("Left Down", "Left_Down");
        mapbusinesslist.put("Right Down", "Right_Down");


        for (Map.Entry<String, String> entry : mapbusinesslist.entrySet()) {
            System.out.println("Key : " + entry.getKey() + " Value : " + entry.getValue());
            bussinessoflist.add(entry.getKey());
            businofdata.add(new BusinessOfData(entry.getKey(), entry.getValue()));
        }
        HashMap<String,String> mappaymentstatuslist =new LinkedHashMap<>();
        mappaymentstatuslist.put("All", "-1");
        mappaymentstatuslist.put("Confirm", "Confirm");
        mappaymentstatuslist.put("Pending", "Pending");
        mappaymentstatuslist.put("Decline", "Decline");


        for (Map.Entry<String, String> entry : mappaymentstatuslist.entrySet()) {
            System.out.println("Key : " + entry.getKey() + " Value : " + entry.getValue());
            paymentstatuslist.add(entry.getKey());
            paymentStatusDatas.add(new PaymentStatusData(entry.getKey(), entry.getValue()));
        }
        HashMap<String,String> mappaymenttypelist =new LinkedHashMap<>();
        mappaymenttypelist.put("All", "-1");
        mappaymenttypelist.put("Booking", "Booking");
        mappaymenttypelist.put("Allotment", "Allotment");
        mappaymenttypelist.put("Installment", "Installment");


        for (Map.Entry<String, String> entry : mappaymenttypelist.entrySet()) {
            System.out.println("Key : " + entry.getKey() + " Value : " + entry.getValue());
            paymenttypelist.add(entry.getKey());
            paymentTypeDatas.add(new PaymentTypeData(entry.getKey(), entry.getValue()));
        }
        HashMap<String,String> mappaymentproceedlist =new LinkedHashMap<>();
        mappaymentproceedlist.put("All", "-1");
        mappaymentproceedlist.put("Proceed", "Proceed");
        mappaymentproceedlist.put("Not Proceed", "Not_Proceed");



        for (Map.Entry<String, String> entry : mappaymentproceedlist.entrySet()) {
            System.out.println("Key : " + entry.getKey() + " Value : " + entry.getValue());
            payoutproceedlist.add(entry.getKey());
            paymentProceedDatas.add(new PaymentProceedData(entry.getKey(), entry.getValue()));
        }

        bussinessof.setAdapter(new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_dropdown_item,
                bussinessoflist));
            bussinessof.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    BusinessOfData td = businofdata.get(position);
                    busiofselectedtext1 = td.getBusinessvalue();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }

            });
        paymentstatus.setAdapter(new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_dropdown_item,
                paymentstatuslist));
       paymentstatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
           @Override
           public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
               PaymentStatusData td = paymentStatusDatas.get(position);
               paymentstatusselectedtext1 = td.getPaymentstatusvalue();
           }

           @Override
           public void onNothingSelected(AdapterView<?> parent) {

           }


       });

        paymenttype.setAdapter(new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_dropdown_item,
                paymenttypelist));
       paymenttype.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
           @Override
           public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
               PaymentTypeData td = paymentTypeDatas.get(position);
               paymenttypeselectedtext1 = td.getPaymenttypevalue();
           }

           @Override
           public void onNothingSelected(AdapterView<?> parent) {

           }

       });
        payoutproceed.setAdapter(new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_dropdown_item,
                payoutproceedlist));
        payoutproceed.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                PaymentProceedData td = paymentProceedDatas.get(position);
                paymentprocedselectedtext1 = td.getPaymentproceedvalue();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });




      getreportsbutton.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {

              String fromdate = fromDateEtxt.getText().toString();
               String todate= toDateEtxt.getText().toString();
              if(fromdate.equals("")&& todate.equals("")){
                 Toast toast = Toast.makeText(getActivity(), "Select Date", Toast.LENGTH_LONG);
                  toast.setGravity(Gravity.TOP, 25, 500);
                  View view1 = toast.getView();
                  view1.setBackgroundResource(R.drawable.toast_drawablecolor);
                  toast.show();
              }else {
                  com.lucknow.quaere.shinecity_mlmpanel.BusinessReportDisplayFragment businessReportDisplayFragment = new com.lucknow.quaere.shinecity_mlmpanel.BusinessReportDisplayFragment();
                  Bundle args = new Bundle();
                  // args.putString("PARAM3", selectedtext);
                  args.putString("PARAM1", fromdate);
                  args.putString("PARAM2", todate);
                  args.putString("PARAM3", busiofselectedtext1);
                  args.putString("PARAM4", paymenttypeselectedtext1);
                  args.putString("PARAM5", paymentstatusselectedtext1);
                  args.putString("PARAM6", paymentprocedselectedtext1);
                  // String url = "http://demo8.mlmsoftindia.com/ShinePanel.svc/MLM_BusinessDetailsTable1/"+MemberID +"/"+fromdate+"/"+todate+"/"+busiofselectedtext1+"/"+paymenttypeselectedtext1+"/"+paymentstatusselectedtext1+"/"+paymentprocedselectedtext1+"/2000";

                  businessReportDisplayFragment.setArguments(args);
                  getFragmentManager().beginTransaction().replace(R.id.mainContent, businessReportDisplayFragment).addToBackStack(null).commit();

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
