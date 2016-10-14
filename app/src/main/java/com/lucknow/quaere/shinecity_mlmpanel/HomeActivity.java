package com.lucknow.quaere.shinecity_mlmpanel;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.internal.view.ContextThemeWrapper;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.navdrawer.SimpleSideDrawer;

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
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import yuku.ambilwarna.AmbilWarnaDialog;


public class HomeActivity extends AppCompatActivity implements View.OnClickListener {
    private DrawerLayout mDrawerLayout;
    private ExpandableListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private String name;

    TextView tv_toolbar_tittle;
    // nav drawer title
    private CharSequence mDrawerTitle;

    // used to store app title
    private CharSequence mTitle;
    // slide menu items
    private String[] navMenuTitles;
    private TypedArray navMenuIcons;
    LinearLayout mDrawerPane;
    private ArrayList<com.lucknow.quaere.shinecity_mlmpanel.NavDrawerItem> navDrawerItems;
    private com.lucknow.quaere.shinecity_mlmpanel.NavDrawerListAdapter adapter;
    private LinearLayout expand_layout;
    ListView myreports_explistview;
    boolean flag = true;
    private LinearLayout linearLayoutProfile, linearchangepasscode, linearreports, liner_logout_changetheme, linear_groupstrucure;
    com.lucknow.quaere.shinecity_mlmpanel.SessionManager session;
    private ImageView up_icon, down_icon;
    private TextView urlshinecity, pricardno, totalvalue, usedvalue, balancevalue;
    SimpleSideDrawer slide_me;
    LinearLayout themechange;
    RelativeLayout relativeLayoutprofileimage;


    private boolean isFlag = true;

    private Toast toast;
    private long lastBackPressTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_navigationdrawer);

        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);

        slide_me = new SimpleSideDrawer(this);
        slide_me.setRightBehindContentView(R.layout.right_slide);
        urlshinecity = (TextView) findViewById(R.id.tv_mlmpanelshicity);
        pricardno = (TextView) findViewById(R.id.tv_carddetail_cardno);
        totalvalue = (TextView) findViewById(R.id.tv_carddetail_totalvalue);
        usedvalue = (TextView) findViewById(R.id.tv_carddetail_usedvalue);
        balancevalue = (TextView) findViewById(R.id.tv_carddetail_balancevalue);

        themechange = (LinearLayout) findViewById(R.id.linearlayoutfirst_navigation);
        relativeLayoutprofileimage = (RelativeLayout) findViewById(R.id.profileBox);
        urlshinecity.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);

        urlshinecity.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (isFlag == true) {
                    urlshinecity.setTextColor(getResources().getColorStateList(R.color.blue));
                    urlshinecity.setPaintFlags(Paint.HINTING_OFF);
                    isFlag = false;


                } else if (isFlag == false) {
                    urlshinecity.setTextColor(getResources().getColorStateList(R.color.red));
                    urlshinecity.setPaintFlags(Paint.HINTING_OFF);
                    isFlag = true;
                    urlshinecity.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
                }
             /*   Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://csw.shinecityinfra.com/mlogin/"));
                startActivity(browserIntent);*/
            }
        });
        Toolbar   toolbar = (Toolbar) findViewById(R.id.tool_bar);
        tv_toolbar_tittle =(TextView)toolbar.findViewById(R.id.toolbar_tittle);
        setSupportActionBar(toolbar);

       getSupportActionBar().setDisplayHomeAsUpEnabled(true);
       getSupportActionBar().setHomeButtonEnabled(true);


        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction().replace(R.id.mainContent, new com.lucknow.quaere.shinecity_mlmpanel.ProfileFragment()).commit();
           getSupportActionBar().setTitle("Dashboard");
        }

        linearLayoutProfile = (LinearLayout) findViewById(R.id.liner_profile);
        linearchangepasscode = (LinearLayout) findViewById(R.id.linear_changepasscode);
        linearreports = (LinearLayout) findViewById(R.id.linearmyreports_layout);
        myreports_explistview = (ListView) findViewById(R.id.ListViewreports);
        expand_layout = (LinearLayout) findViewById(R.id.expandlayout);
        liner_logout_changetheme = (LinearLayout) findViewById(R.id.liner_logout_changetheme);
        linear_groupstrucure = (LinearLayout) findViewById(R.id.linear_groupstructure);


        down_icon = (ImageView) findViewById(R.id.down_arrow);
        up_icon = (ImageView) findViewById(R.id.up_arrow);

        TextView profilename = (TextView) findViewById(R.id.userName);
        //profilename.setTextColor(Color.parseColor("#FFFFFF"));
        profilename.setTextColor(Color.WHITE);
        Typeface tf = Typeface.createFromAsset(getAssets(), "SEASRN__.ttf");

        profilename.setTypeface(tf);
        session = new com.lucknow.quaere.shinecity_mlmpanel.SessionManager(getApplicationContext());

        // get user data from session
        HashMap<String, String> user = session.getUserDetails();

        // name
        name = user.get(com.lucknow.quaere.shinecity_mlmpanel.SessionManager.KEY_USERNAME);

        // email
        // String email = user.get(SessionManager.KEY_MEMID);
        profilename.setText(name);

        linearLayoutProfile.setOnClickListener(this);
        linearchangepasscode.setOnClickListener(this);
        linearreports.setOnClickListener(this);
        liner_logout_changetheme.setOnClickListener(this);
        linear_groupstrucure.setOnClickListener(this);


        mDrawerPane = (LinearLayout) findViewById(R.id.drawerPane);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        //  mDrawerList = (ExpandableListView) findViewById(R.id.list_slidermenu);


        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                //nav menu toggle icon
                R.string.app_name, // nav drawer open - description for accessibility
                R.string.app_name // nav drawer close - description for accessibility
        ) {
            public void onDrawerClosed(View view) {
                //   getSupportActionBar().setTitle(mTitle);
                // calling onPrepareOptionsMenu() to show action bar icons
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                // getSupportActionBar().setTitle(mTitle);
                // calling onPrepareOptionsMenu() to hide action bar icons
                invalidateOptionsMenu();
            }
        };
      




    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // toggle nav drawer on selecting action bar app icon/title
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle action bar actions click
        switch (item.getItemId()) {
            case R.id.action_setting:
                alertdiaolog();
           /*     Intent intent = new Intent(android.provider.Settings.ACTION_SETTINGS);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);*/

                return true;
            case R.id.ic_menu_share:
                PackageManager pm = getPackageManager();
                String uri = null;
                for (ApplicationInfo app : pm.getInstalledApplications(0)) {
                    if (!((app.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) == 1))
                        if (!((app.flags & ApplicationInfo.FLAG_SYSTEM) == 1)) {
                            uri = app.sourceDir;
                            if (uri.contains("com.example.deepaksachan.shinecity_mlm"))
                                break;
                        }
                }

                Intent intent2 = new Intent();
                intent2.setAction(Intent.ACTION_SEND);
                intent2.setType("app/*");
                intent2.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(uri)));
                startActivity(intent2);
              /*  Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBody = "Here is the share content body";
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, "Share via"));*/
                return true;
            case R.id.slide_menu:


                slide_me.toggleRightDrawer();
                session = new com.lucknow.quaere.shinecity_mlmpanel.SessionManager(getApplicationContext());

                // get user data from session
                HashMap<String, String> user = session.getUserDetails();

                // name
                String memid = user.get(com.lucknow.quaere.shinecity_mlmpanel.SessionManager.KEY_MEMID);
                String url = "http://demo8.mlmsoftindia.com/ShinePanel.svc/GetCardDetail/" + memid;
                new CardDetailAsynctask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, url);

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public void setTitle(CharSequence title) {
       // mTitle = title;
        //getSupportActionBar().setTitle(mTitle);
    }

    /**
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     */

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onClick(View v) {

        if (v == linearLayoutProfile) {
            expand_layout.setVisibility(View.GONE);
            flag = true;
            // update.setVisibility(View.VISIBLE);
            Toast.makeText(getApplicationContext(), "Welcome  " + name, Toast.LENGTH_SHORT).show();
            getFragmentManager().beginTransaction().replace(R.id.mainContent, new com.lucknow.quaere.shinecity_mlmpanel.ProfileFragment()).commit();
            getSupportActionBar().setTitle("Dashboard");
            mDrawerLayout.closeDrawer(mDrawerPane);
            // mDrawerLayout.openDrawer(mDrawerPane);

        } else if (v == linearchangepasscode) {
            expand_layout.setVisibility(View.GONE);
            flag = true;
            getFragmentManager().beginTransaction().replace(R.id.mainContent, new com.lucknow.quaere.shinecity_mlmpanel.ChangePasswordFragment()).commit();
            getSupportActionBar().setTitle("Change Password");
            mDrawerLayout.closeDrawer(mDrawerPane);

        } else if (v == linearreports) {
            String[] subreports = new String[]{"Business Statistics", "E-PinList", "E-Pin Registration", "Business Report", "Harmony Incentive", "Self Incentive", "AgentPlotReport", "Payout Details", "Downline Reports"};
            // int[] icon = new int[]{R.drawable.profile,R.drawable.exit,R.drawable.success};
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                    HomeActivity.this, R.layout.my_subreports_list_row, subreports);
            myreports_explistview.setAdapter(adapter);
            com.lucknow.quaere.shinecity_mlmpanel.ListviewHelper.getListViewSize(myreports_explistview);
            myreports_explistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    sublistdata(position);
                }

            });

            if (flag == true) {
                expand_layout.setVisibility(View.VISIBLE);
                down_icon.setVisibility(View.GONE);
                up_icon.setVisibility(View.VISIBLE);
                flag = false;

            } else {
                // myAccount_explistview.setVisibility(View.GONE);
                expand_layout.setVisibility(View.GONE);
                up_icon.setVisibility(View.GONE);
                down_icon.setVisibility(View.VISIBLE);
                //down_arrow.setVisibility(View.VISIBLE);
                flag = true;
            }
        } else if (v == linear_groupstrucure) {
            getSupportActionBar().setTitle("Group Structure");
            expand_layout.setVisibility(View.GONE);
            flag = true;
            getFragmentManager().beginTransaction().replace(R.id.mainContent, new com.lucknow.quaere.shinecity_mlmpanel.GroupStructureFragment()).addToBackStack(null).commit();
            mDrawerLayout.closeDrawer(mDrawerPane);
        } else if (v == liner_logout_changetheme) {
            colorpicker();
            // getSupportActionBar().setTitle("Theme");
          /*  if (flag == true) {

                    getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.blueIndigo)));
                    // Drawable drawable = getResources().getDrawable(R.drawable.bg_material_design);
                    // relativeLayoutprofileimage.setBackground(drawable);
                    //relativeLayoutprofileimage .setBackgroundResource(R.drawable.whatappimg);
                    relativeLayoutprofileimage.setBackgroundColor(getResources().getColor(R.color.blueIndigo));
                    themechange.setBackgroundColor(getResources().getColor(R.color.blueIndigo));
                    flag = false;
                } else {
                    getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.actionbarthemecolor)));
                    themechange.setBackgroundColor(getResources().getColor(R.color.actionbarthemecolor));
                    relativeLayoutprofileimage.setBackgroundColor(getResources().getColor(R.color.actionbarthemecolor));
                    //  relativeLayoutprofileimage .setBackgroundResource(R.drawable.blue);
                    flag = true;
                }*/

        }
    }

    private void sublistdata(int position) {
        int id = position;
        switch (id) {
            case 0:


                getSupportActionBar().setTitle("Business Statistics");
                //  expand_layout.setVisibility(View.GONE);
                flag = true;
                mDrawerLayout.closeDrawer(mDrawerPane);
                getFragmentManager().beginTransaction().replace(R.id.mainContent, new com.lucknow.quaere.shinecity_mlmpanel.BusinessStatisticsFragment()).commit();

                break;
            case 1:

                getSupportActionBar().setTitle("E-PinList");
                //   expand_layout.setVisibility(View.GONE);
                flag = true;
                mDrawerLayout.closeDrawer(mDrawerPane);
                getFragmentManager().beginTransaction().replace(R.id.mainContent, new com.lucknow.quaere.shinecity_mlmpanel.EPinListFragment()).commit();
                break;
            case 2:

              //  getSupportActionBar().setTitle("E-Pin Registration");
                //   expand_layout.setVisibility(View.GONE);
                flag = true;
                mDrawerLayout.closeDrawer(mDrawerPane);
                // getFragmentManager().beginTransaction().replace(R.id.mainContent, new EPinListFragment()).commit();
                startActivity(new Intent(this, com.lucknow.quaere.shinecity_mlmpanel.EPinRegistrationActivity.class));
                break;
            case 3:

                getSupportActionBar().setTitle("Business Report");
                //  expand_layout.setVisibility(View.GONE);
                flag = true;
                mDrawerLayout.closeDrawer(mDrawerPane);
                getFragmentManager().beginTransaction().replace(R.id.mainContent, new com.lucknow.quaere.shinecity_mlmpanel.BusinessReportFragment()).commit();
                break;

            case 4:
                getSupportActionBar().setTitle("Harmony Incentive");
                //  expand_layout.setVisibility(View.GONE);
                flag = true;
                mDrawerLayout.closeDrawer(mDrawerPane);
                getFragmentManager().beginTransaction().replace(R.id.mainContent, new com.lucknow.quaere.shinecity_mlmpanel.HarmonyInsentiveDisplayFragment()).commit();
                break;

            case 5:
                getSupportActionBar().setTitle("Self Incentive");
                //  expand_layout.setVisibility(View.GONE);
                flag = true;
                mDrawerLayout.closeDrawer(mDrawerPane);
                getFragmentManager().beginTransaction().replace(R.id.mainContent, new com.lucknow.quaere.shinecity_mlmpanel.SelfIncentiveDisplayFragment()).commit();
                break;
            case 6:
                getSupportActionBar().setTitle("AgentPlotReports");
                // expand_layout.setVisibility(View.GONE);
                flag = true;
                mDrawerLayout.closeDrawer(mDrawerPane);
                getFragmentManager().beginTransaction().replace(R.id.mainContent, new com.lucknow.quaere.shinecity_mlmpanel.AgentPlotReportFragment()).commit();
                break;
            case 7:
                getSupportActionBar().setTitle("PayoutDetails");
                //  expand_layout.setVisibility(View.GONE);
                flag = true;
                mDrawerLayout.closeDrawer(mDrawerPane);
                getFragmentManager().beginTransaction().replace(R.id.mainContent, new com.lucknow.quaere.shinecity_mlmpanel.PayoutReportDisplayFragment()).commit();
                break;
            case 8:
                getSupportActionBar().setTitle("Downline Reports");
                //  expand_layout.setVisibility(View.GONE);
                flag = true;
                mDrawerLayout.closeDrawer(mDrawerPane);
                getFragmentManager().beginTransaction().replace(R.id.mainContent, new com.lucknow.quaere.shinecity_mlmpanel.DownlineReportFragment()).commit();
                break;

        }


    }

    public void alertdiaolog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.AlertDialogCustom));

        // set title
        alertDialogBuilder.setTitle("Would you like to logout?");

        // set dialog message
        alertDialogBuilder
                .setMessage("Click yes to logout!")
                .setCancelable(false)
                .setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int id) {
                                // if this button is clicked, close
                                // current activity
                                session.logoutUser();
                                startActivity(new Intent(HomeActivity.this, DefaultScreenActivity.class));
                                finish();

                            }
                        })
                .setNegativeButton("No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int id) {
                                // if this button is clicked, just close
                                // the dialog box and do nothing
                                dialog.cancel();
                            }
                        });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }


    @Override
    public void onBackPressed() {


// dont call **super**, if u want disable back button in current screen.
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();
            // super.onBackPressed();
          //  getFragmentManager().popBackStackImmediate();

        } else {
            alertdiaologbackbutton();
            // super.onBackPressed();
            // }
        }
    }

    public void alertdiaologbackbutton() {
     /*   new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setMessage("Are you sure you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {


                       finish();
                    }
                })
                .setNegativeButton("No", null)
                .show();*/


        if (this.lastBackPressTime < System.currentTimeMillis() - 4000) {
            toast = Toast.makeText(this, "Press back again to close this app", 4000);
            toast.show();
            this.lastBackPressTime = System.currentTimeMillis();
            mDrawerLayout.openDrawer(mDrawerPane);
        } else {
            if (toast != null) {
                toast.cancel();

            }


         /*   mDrawerLayout.closeDrawer(mDrawerPane);
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);*/

             finish();
        }
    }
    public void colorpicker()
    {
        // 	initialColor is the initially-selected color to be shown in the rectangle on the left of the arrow.
        // 	for example, 0xff000000 is black, 0xff0000ff is blue. Please be aware of the initial 0xff which is the alpha.

        AmbilWarnaDialog dialog = new AmbilWarnaDialog(this, 0xff0000ff, new AmbilWarnaDialog.OnAmbilWarnaListener()
        {

            // Executes, when user click Cancel button
            @Override
            public void onCancel(AmbilWarnaDialog dialog){

            }

            // Executes, when user click OK button
            @Override
            public void onOk(AmbilWarnaDialog dialog, int color) {
                Toast.makeText(getBaseContext(), "Theme changed  ", Toast.LENGTH_LONG).show();

                    ColorDrawable colorDrawable = new ColorDrawable();
                    colorDrawable.setColor(color);
                    getSupportActionBar().setBackgroundDrawable(colorDrawable);
                    relativeLayoutprofileimage.setBackgroundColor(color);
                    themechange.setBackgroundColor(color);


            }

        });

        dialog.show();
    }
    private class CardDetailAsynctask extends AsyncTask<String, Void, String> {

        private static final int REGISTRATION_TIMEOUT = 10 * 1000;
        private static final int WAIT_TIMEOUT = 50 * 1000;
        private final HttpClient httpclient = new DefaultHttpClient();
        final HttpParams params = httpclient.getParams();
        HttpResponse response;
        private String content = null;
        private boolean error = false;
        private ProgressDialog dialog =
                new ProgressDialog(HomeActivity.this, R.style.ThemeMyDialog);

        protected void onPreExecute() {
            dialog.setMessage("Getting your data... Please wait...");
            dialog.setCancelable(true);
            //  dialog.show();
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
            Toast toast = Toast.makeText(HomeActivity.this, "Connection Server Failed", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP, 25, 500);
            View view1 = toast.getView();
            view1.setBackgroundResource(R.drawable.toast_drawablecolor);
            toast.show();
            finish();
        }

        protected void onPostExecute(String content) {
            dialog.dismiss();

            if (error) {

                Toast toast = Toast.makeText(HomeActivity.this, content, Toast.LENGTH_LONG);
                toast.setGravity(Gravity.TOP, 25, 500);
                View view1 = toast.getView();
                view1.setBackgroundResource(R.drawable.toast_drawablecolor);
                toast.show();
            } else if (content.equals("[]")) {
                Toast toast2 = Toast.makeText(HomeActivity.this, "No Record Found", Toast.LENGTH_LONG);
                toast2.setGravity(Gravity.TOP, 25, 500);
                View view1 = toast2.getView();
                view1.setBackgroundResource(R.drawable.toast_drawablecolor);
                toast2.show();
            } else if (content.equals("")) {
                Toast toast2 = Toast.makeText(HomeActivity.this, "No Record Found", Toast.LENGTH_LONG);
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
            //  plotReportDatas = new ArrayList<DownlineRecordData>();
            try {
                JSONArray jArray = new JSONArray(response);
                int count = 0;
                for (int i = 0; i < jArray.length(); i++) {
                    if (jArray != null) {
                        count++;
                    }
                    JSONObject jObj = jArray.getJSONObject(i);
                    String PrivilegeCardNo = jObj.getString("PrivilegeCardNo");
                    String PrivilegeCardBalanceValue = jObj.getString("PrivilegeCardBalanceValue");
                    String PrivilegeCardTotalValue = jObj.getString("PrivilegeCardTotalValue");
                    String PrivilegeCardUsedValue = jObj.getString("PrivilegeCardUsedValue");

                    pricardno.setText(PrivilegeCardNo);
                    totalvalue.setText(PrivilegeCardTotalValue);
                    usedvalue.setText(PrivilegeCardUsedValue);
                    balancevalue.setText(PrivilegeCardBalanceValue);
                }

                int no = count;
                String aString = Integer.toString(no);
                String strI = String.valueOf(no);
                //tv_no.setText(strI);


            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
}
