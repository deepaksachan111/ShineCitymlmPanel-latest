package com.lucknow.quaere.shinecity_mlmpanel;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;


public class EPinListFragment extends Fragment implements View.OnClickListener {

    private String seletedvalue;
    private EditText fromDateEtxt;
    private EditText toDateEtxt;
    private EditText edt_epinno;
    private Spinner spi_status;
    private Button btn_getepindetails;
    private DatePickerDialog fromDatePickerDialog;
    private DatePickerDialog toDatePickerDialog;
    private SimpleDateFormat dateFormatter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    private void findviewbyid(View view) {
        fromDateEtxt =(EditText)view.findViewById(R.id.edt_epinlist_fromdate);
        toDateEtxt =(EditText)view.findViewById(R.id.edt_epinlist_todate);
        edt_epinno =(EditText)view.findViewById(R.id.edt_epinlist_epinno);
        spi_status =(Spinner)view.findViewById(R.id.spi_epinlist_status);
        btn_getepindetails=(Button)view.findViewById(R.id.btn_get_pin_details);
        edt_epinno =(EditText)view.findViewById(R.id.edt_epinlist_epinno);
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
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_epin_list, container, false);
        findviewbyid(view);
        setDateTimeField();
        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        ArrayList<String> statuslist = new ArrayList<String>();
        statuslist.add("Unused");
        statuslist.add("Used");
        spi_status.setAdapter(new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_dropdown_item,
                statuslist));
        spi_status.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                seletedvalue = spi_status.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }


        });
        btn_getepindetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String epino = edt_epinno.getText().toString();
                String fromdate = fromDateEtxt.getText().toString();
                String todate = toDateEtxt.getText().toString();
                if (fromdate.equals("") || todate.equals("")||epino.equals("")) {
                    fromdate = "-1";
                    todate = "-1";
                    epino = "-1";
                }
                String s = seletedvalue;
                String from = fromdate;
                String to = todate;
                EpinListDisplayFragment epinListDisplayFragment = new EpinListDisplayFragment();
                Bundle bundle = new Bundle();
                bundle.putString("PARAM1",fromdate);
                bundle.putString("PARAM2",todate);
                bundle.putString("PARAM3",seletedvalue);
                bundle.putString("PARAM4",epino);
                epinListDisplayFragment.setArguments(bundle);
                getFragmentManager().beginTransaction().replace(R.id.mainContent,epinListDisplayFragment).addToBackStack(null).commit();

            }
        });
        return view;
    }






}
