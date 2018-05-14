package com.example.mathi.expense;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TabHost;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import org.json.JSONArray;
import org.json.JSONException;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by MATHI on 5/21/2017.
 */

public class ChartActivity extends AppCompatActivity {
    DatabaseHelper databaseHelper;
    String[] strArr,strArr1;
    JSONArray jsonArray,jsonArray1;
    ArrayList input1,input;
    PieChart pieChart ;
    ArrayList<Entry> entries ;
    ArrayList<String> PieEntryLabels ;
    ArrayList<BarEntry> BARENTRY;
    ArrayList<String> BarEntryLabels;
    PieDataSet pieDataSet ;
    PieData pieData ;
    Spinner month_spinner;
    ArrayAdapter<String> month_spinadapter;
    String state,months;
    String[] MONTHS = {"January", "February", "March","April","May","June","July",
            "August","September","October","November", "December"};
    private AdView mAdView;
    TabHost host;
    BarDataSet Bardataset;
    BarData BARDATA;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chart);

        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimary));
        databaseHelper = new DatabaseHelper(this);
        mAdView = (AdView) findViewById(R.id.adView);

        host = (TabHost) findViewById(R.id.tabHost);
        host.setup();

        //Tab 1
        TabHost.TabSpec spec = host.newTabSpec("Bar Chart");
        spec.setContent(R.id.bar_chart_view);
        spec.setIndicator("Bar Chart");
        host.addTab(spec);
        //tab 2

        spec = host.newTabSpec("Pie Chart");
        spec.setContent(R.id.pie_chart_view);
        spec.setIndicator("Pie Chart");
        host.addTab(spec);

        host.setOnTabChangedListener(new TabHost.OnTabChangeListener() {

            @Override
            public void onTabChanged(String tabId) {
                setTabColor(host);

            }
        });

        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .addTestDevice("AAB57E57F502361943BF3EDBAE4D0903")
                .build();
        mAdView.loadAd(adRequest);

        Calendar c = Calendar.getInstance();
        int mMonth = c.get(Calendar.MONTH);

        month_spinadapter = new ArrayAdapter<String>(ChartActivity.this, R.layout.month_spin_list, MONTHS);
        month_spinner = (Spinner) findViewById(R.id.month_spinner);
        month_spinner.setAdapter(month_spinadapter);
        month_spinner.setSelection(mMonth);
        month_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                months = (String)month_spinner.getSelectedItem();

                switch(months) {
                    case "January":
                        state="01";
                        break;
                    case "February":
                        state="02";
                        break;
                    case "March":
                        state="03";
                        break;
                    case "April":
                        state="04";
                        break;
                    case "May":
                        state="05";
                        break;
                    case "June":
                        state="06";
                        break;
                    case "July":
                        state="07";
                        break;
                    case "August":
                        state="08";
                        break;
                    case "September":
                        state="09";
                        break;
                    case "October":
                        state="10";
                        break;
                    case "November":
                        state="11";
                        break;
                    case "December":
                        state="12";
                        break;
                }
                input=databaseHelper.getentrydata(state);
                input1=databaseHelper.getLabledata(state);

                pieChart = (PieChart) findViewById(R.id.pie_chart);
                pieChart.setHoleRadius(22);
                pieChart.setTransparentCircleRadius(5);
                entries = new ArrayList<>();
                PieEntryLabels = new ArrayList<String>();
                AddValuesToPIEENTRY();
                AddValuesToPieEntryLabels();
                pieDataSet = new PieDataSet(entries, "");
                pieDataSet.setSliceSpace(4);
                pieDataSet.setValueTextSize(8);
                pieData = new PieData(PieEntryLabels, pieDataSet);
                pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                pieChart.setData(pieData);
                pieChart.animateY(2000);

                BarChart chart = (BarChart) findViewById(R.id.bar_chart);
                BARENTRY = new ArrayList<>();
                BarEntryLabels = new ArrayList<String>();
                AddValuesTochart();
                AddValuesToBarchart();
                Bardataset = new BarDataSet(BARENTRY, "Expense");
                BARDATA = new BarData(BarEntryLabels, Bardataset);
                Bardataset.setColors(ColorTemplate.COLORFUL_COLORS);
                chart.setData(BARDATA);
                chart.animateY(3000);
                chart.setPinchZoom(true);
                chart.setDescription("Expense");
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });
    }

    public void AddValuesToPIEENTRY(){
        jsonArray = new JSONArray(input);
        strArr = new String[jsonArray.length()];
        for (int j = 0; j < jsonArray.length(); j++) {
            try {
                strArr[j] = jsonArray.getString(j);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            entries.add(new BarEntry(Float.parseFloat(strArr[j]), j));
        }
    }
    public void AddValuesToPieEntryLabels(){
        jsonArray1 = new JSONArray(input1);
        strArr1 = new String[jsonArray1.length()];
        for (int j = 0; j < jsonArray1.length(); j++) {
            try {
                strArr1[j] = jsonArray1.getString(j);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            PieEntryLabels.add(strArr1[j]);
        }
    }
    public void AddValuesTochart(){
        jsonArray = new JSONArray(input);
        strArr = new String[jsonArray.length()];
        for (int j = 0; j < jsonArray.length(); j++) {
            try {
                strArr[j] = jsonArray.getString(j);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            BARENTRY.add(new BarEntry(Float.parseFloat(strArr[j]), j));
        }
    }
    public void AddValuesToBarchart(){
        jsonArray1 = new JSONArray(input1);
        strArr1 = new String[jsonArray1.length()];
        for (int j = 0; j < jsonArray1.length(); j++) {
            try {
                strArr1[j] = jsonArray1.getString(j);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            BarEntryLabels.add(strArr1[j]+"");
        }
    }
    public static void setTabColor(TabHost host) {
        for(int i=0;i<host.getTabWidget().getChildCount();i++)
        {
            host.getTabWidget().getChildAt(i).setBackgroundColor(Color.WHITE);  //unselected
        }
        host.getTabWidget().getChildAt(host.getCurrentTab()).setBackgroundResource(R.color.AntiqueWhite); // selected
    }
    @Override
    public void onPause() {
        if (mAdView != null) {
            mAdView.pause();
        }
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mAdView != null) {
            mAdView.resume();
        }
    }

    @Override
    public void onDestroy() {
        if (mAdView != null) {
            mAdView.destroy();
        }
        super.onDestroy();
    }

}