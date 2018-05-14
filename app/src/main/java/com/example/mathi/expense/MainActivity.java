package com.example.mathi.expense;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mathi.expense.expense.Expense;
import com.example.mathi.expense.expense.ExpenseActivity;
import com.example.mathi.expense.expense.ExpenseCustomAdapter;
import com.example.mathi.expense.expense.ExpenseListViewActivity;
import com.example.mathi.expense.income.IncomeActivity;
import com.example.mathi.expense.income.IncomeListViewActivity;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.Highlight;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.File;
import java.io.FileWriter;
import java.sql.Time;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import info.hoang8f.widget.FButton;
import me.tankery.lib.circularseekbar.CircularSeekBar;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    DatabaseHelper myDb;
    ListView listview;
    String price, category, description, date, mnth1;
    int id, mnth, a, b, d;
    TextView show_listview, left_days, t_id, title_month, text_left, text_income,
            text_expense, texpence, tincome, tbalance;
    ExpenseCustomAdapter adapter;
    boolean doubleBackToExitPressedOnce = false;
    private Boolean isFabOpen = false;
    private FloatingActionButton fab, fab1, fab2;
    private Animation fab_open, fab_close, rotate_forward, rotate_backward;
    String[] strArr, strArr1,strArr2,strArr3;
    JSONArray jsonArray, jsonArray1,jsonArray2,jsonArray3;
    ArrayList input1, input,input2,input3;
    ArrayList<BarEntry> BARENTRY,BARENTRY1;
    ArrayList<String> BarEntryLabels,BarEntryLabels1;
    BarDataSet Bardataset,Bardataset1;
    BarData BARDATA,BARDATA1;
    Button add_expense, add_income;
    NavigationView navigationView;
    private Locale myLocale;
    int total_days, month;
    String month_lan_types;


    private static final int PERMISSION_CALLBACK_CONSTANT = 100;
    private static final int REQUEST_PERMISSION_SETTING = 101;
    String[] permissionsRequired = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private SharedPreferences permissionStatus;
    private boolean sentToSettings = false;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimary));

        AdView mAdView = (AdView) findViewById(R.id.adView1);

        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .addTestDevice("AAB57E57F502361943BF3EDBAE4D0903")
                .build();
        mAdView.loadAd(adRequest);

        myDb = new DatabaseHelper(this);

        show_listview = (TextView) findViewById(R.id.last_10_records);
        add_income = (Button) findViewById(R.id.income_icon);
        add_expense = (Button) findViewById(R.id.expense_icon);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        title_month = (TextView) findViewById(R.id.t_title);
        text_left = (TextView) findViewById(R.id.tleft);
        text_income = (TextView) findViewById(R.id.tincome);
        text_expense = (TextView) findViewById(R.id.texpense);
        texpence = (TextView) findViewById(R.id.expense);
        tincome = (TextView) findViewById(R.id.income);
        tbalance = (TextView) findViewById(R.id.balance);
        left_days = (TextView) findViewById(R.id.left_days);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header = navigationView.getHeaderView(0);
        TextView username = (TextView) header.findViewById(R.id.username);
        username.setText(myDb.getusername());
        TextView usermail = (TextView) header.findViewById(R.id.usermail);
        usermail.setText(myDb.getusermail());
        TextView userimage = (TextView) header.findViewById(R.id.user_image);
        userimage.setText(myDb.getusername().substring(0, 1));

        add_expense.setOnClickListener(this);
        add_income.setOnClickListener(this);
        show_listview.setOnClickListener(this);
        float_button();

        loadLocaleLanguage();
        //permission
        requestPermission();
        //monthly balance+remaining
        monthly_balance_and_remaining();
    }
    @Override
    public void onResume(){
        super.onResume();
        // put your code here...
        loadLocaleLanguage();
        //permission
        requestPermission();
        //monthly balance+remaining
        monthly_balance_and_remaining();

        float_button();

    }
    public void float_button(){
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab1 = (FloatingActionButton) findViewById(R.id.fab1);
        fab2 = (FloatingActionButton) findViewById(R.id.fab2);
        fab_open = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_close);
        rotate_forward = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_forward);
        rotate_backward = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_backward);
        fab.setOnClickListener(this);
        fab1.setOnClickListener(this);
        fab2.setOnClickListener(this);
    }

    public void requestPermission() {
        permissionStatus = getSharedPreferences("permissionStatus", MODE_PRIVATE);


        if (ActivityCompat.checkSelfPermission(this, permissionsRequired[0]) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, permissionsRequired[0])) {
                //Show Information about why you need the permission
                android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
                builder.setTitle("Need Multiple Permissions");
                builder.setMessage("This app needs Write External Storage...");
                builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        ActivityCompat.requestPermissions(MainActivity.this, permissionsRequired, PERMISSION_CALLBACK_CONSTANT);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            } else if (permissionStatus.getBoolean(permissionsRequired[0], false)) {
                //Previously Permission Request was cancelled with 'Dont Ask Again',
                // Redirect to Settings after showing Information about why you need the permission
                android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
                builder.setTitle("Need Multiple Permissions");
                builder.setMessage("This app needs Write External Storage...");
                builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        sentToSettings = true;
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                        intent.setData(uri);
                        startActivityForResult(intent, REQUEST_PERMISSION_SETTING);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            } else {
                //just request the permission
                ActivityCompat.requestPermissions(MainActivity.this, permissionsRequired, PERMISSION_CALLBACK_CONSTANT);
            }

            SharedPreferences.Editor editor = permissionStatus.edit();
            editor.putBoolean(permissionsRequired[0], true);
            editor.commit();
        } else {
            //You already have the permission, just go ahead.
            proceedAfterPermission();
        }
    }

    private void monthly_balance_and_remaining() {
        Calendar calendar = Calendar.getInstance();
        int monthMaxDays = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        int mDay = calendar.get(Calendar.DAY_OF_MONTH);
        total_days = monthMaxDays - mDay;
        month = calendar.get(Calendar.MONTH);


        SimpleDateFormat df = new SimpleDateFormat("HH:mm");
        String formattedDate = df.format(calendar.getTime());
        if (formattedDate.equals("17:14")) {
            CustomNotification();
        }
        mnth = month + 1;
        if (month < 10) {
            mnth1 = "0" + mnth;
        } else {
            mnth1 = "" + mnth;
        }

        Cursor c = myDb.getTotalexpense(mnth1);
        if (c != null && c.moveToFirst()) {
            a = c.getInt(0);
            texpence.setText("RS:" + a + "");
        }
        c = myDb.getTotalincome(mnth1);
        if (c != null && c.moveToFirst()) {
            b = c.getInt(0);
            tincome.setText("RS:" + b + "");
        }
        d = b - a;
        tbalance.setText("RS:" + d + "");
        CircularSeekBar balance_seekbar = (CircularSeekBar) findViewById(R.id.balance_seekbar);
        balance_seekbar.setMax(Float.valueOf(b));
        balance_seekbar.setProgress(Float.valueOf(d));

        CircularSeekBar income_seekbar = (CircularSeekBar) findViewById(R.id.income_seekbar);
        income_seekbar.setMax(Float.valueOf(b));
        income_seekbar.setProgress(Float.valueOf(d));

        CircularSeekBar expense_seekbar = (CircularSeekBar) findViewById(R.id.expense_seekbar);
        expense_seekbar.setMax(Float.valueOf(a));
        expense_seekbar.setProgress(Float.valueOf(d));

        chartaction();
    }

    public void chartaction() {
        //expense data
        input = myDb.getentrydata(mnth1);
        input1 = myDb.getLabledata(mnth1);
        //income data
        input2 = myDb.getentrydata1(mnth1);
        input3 = myDb.getLabledata1(mnth1);

        BarChart chart = (BarChart) findViewById(R.id.expense_bar_chart);
        BARENTRY = new ArrayList<>();
        BarEntryLabels = new ArrayList<String>();
        AddValuesToPIEENTRY();
        AddValuesToPieEntryLabels();
        Bardataset = new BarDataSet(BARENTRY, "Expense");
        BARDATA = new BarData(BarEntryLabels, Bardataset);
        Bardataset.setColors(ColorTemplate.COLORFUL_COLORS);
        chart.setData(BARDATA);
        chart.animateY(3000);
        chart.setPinchZoom(true);
        chart.setDescription("Expense");

        BarChart chart1 = (BarChart) findViewById(R.id.income_bar_chart);
        BARENTRY1 = new ArrayList<>();
        BarEntryLabels1 = new ArrayList<String>();
        AddValuesTochart();
        AddValuesToBarchart();
        Bardataset1 = new BarDataSet(BARENTRY1, "Income");
        BARDATA1 = new BarData(BarEntryLabels1, Bardataset1);
        Bardataset1.setColors(ColorTemplate.COLORFUL_COLORS);
        chart1.setData(BARDATA1);
        chart1.animateY(3000);
        chart1.setPinchZoom(true);
        chart1.setDescription("Income");
    }

    public void AddValuesToPIEENTRY() {
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

    public void AddValuesToPieEntryLabels() {
        jsonArray1 = new JSONArray(input1);
        strArr1 = new String[jsonArray1.length()];
        for (int j = 0; j < jsonArray1.length(); j++) {
            try {
                strArr1[j] = jsonArray1.getString(j);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            BarEntryLabels.add(strArr1[j]);
        }
    }
    public void AddValuesTochart(){
        jsonArray2 = new JSONArray(input2);
        strArr2 = new String[jsonArray2.length()];
        for (int j = 0; j < jsonArray2.length(); j++) {
            try {
                strArr2[j] = jsonArray2.getString(j);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            BARENTRY1.add(new BarEntry(Float.parseFloat(strArr2[j]), j));
        }
    }
    public void AddValuesToBarchart(){
        jsonArray3 = new JSONArray(input3);
        strArr3 = new String[jsonArray3.length()];
        for (int j = 0; j < jsonArray3.length(); j++) {
            try {
                strArr3[j] = jsonArray3.getString(j);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            BarEntryLabels1.add(strArr3[j]+"");
        }
    }

    public void loadLocaleLanguage() {
        changeLang(Utils.getNetworkMode(this));

        String lan_type = Utils.getNetworkMode(this);
        switch (lan_type) {
            case "en":
                getMonthForEnglish(month);
                month_lan_types = getMonthForEnglish(month);
                break;
            case "ta":
                getMonthForTamil(month);
                month_lan_types = getMonthForTamil(month);
                break;
            case "hi":
                getMonthForEnglish(month);
                month_lan_types = getMonthForHindi(month);
                break;
        }
        String monthly_balance_remaing = getString(R.string.monthly_balance_remaing);
        title_month.setText(month_lan_types + " " + monthly_balance_remaing);
    }

    public void changeLang(String lang) {
        if (lang.equalsIgnoreCase(""))
            return;
        myLocale = new Locale(lang);
        Locale.setDefault(myLocale);
        android.content.res.Configuration config = new android.content.res.Configuration();
        config.locale = myLocale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
        updateTexts();
    }

    private void updateTexts() {
        add_income.setText(R.string.income);
        add_expense.setText(R.string.expense);
        show_listview.setText(R.string.last_10_expense_record);
        text_left.setText(R.string.remaining_balance);
        text_expense.setText(R.string.expense);
        text_income.setText(R.string.income);
        String days_left = getString(R.string.days_left);
        left_days.setText(total_days + " " + days_left);

        Menu menu = navigationView.getMenu();


        menu.findItem(R.id.nav_expense_list).setTitle(R.string.nav_expense_list);
        menu.findItem(R.id.nav_income_list).setTitle(R.string.nav_income_list);
        menu.findItem(R.id.nav_chart).setTitle(R.string.nav_chart);
        menu.findItem(R.id.nav_category_wise_expense_list).setTitle(R.string.nav_category_wise_expense_list);
        menu.findItem(R.id.nav_total_report).setTitle(R.string.nav_total_report);
        menu.findItem(R.id.nav_settings).setTitle(R.string.nav_settings);
        menu.findItem(R.id.nav_language).setTitle(R.string.nav_language);
        menu.findItem(R.id.nav_about_us).setTitle(R.string.nav_about_us);
        menu.findItem(R.id.nav_share).setTitle(R.string.nav_share);
        menu.findItem(R.id.nav_rate_us).setTitle(R.string.nav_rate_us);
        menu.findItem(R.id.nav_allinone).setTitle(R.string.nav_allinone);
    }

    public static String getMonthForEnglish(int num) {
        String monthname = "wrong";
        String[] MONTHS = {"January", "February", "March", "April", "May", "June", "July",
                "August", "September", "October", "November", "December"};
        if (num >= 0 && num <= 11) {
            monthname = MONTHS[num];
        }
        return monthname;
    }

    public static String getMonthForTamil(int num) {
        String monthname = "wrong";
        String[] MONTHS = {"ஜனவரி", "பிப்ரவரி", "மார்ச்", "ஏப்ரல்", "மே", "ஜூன்", "ஜூலை",
                "ஆகஸ்ட்", "செப்டம்பர்", "அக்டோபர்", "நவம்பர்", "டிசம்பர்"};
        if (num >= 0 && num <= 11) {
            monthname = MONTHS[num];
        }
        return monthname;
    }

    public static String getMonthForHindi(int num) {
        String monthname = "wrong";
        String[] MONTHS = {"जनवरी", "फरवरी", "मार्च", "अप्रैल", "मई", "जून", "जुलाई",
                "अगस्त", "सितंबर", "अक्टूबर", "नवंबर", "दिसंबर"};
        if (num >= 0 && num <= 11) {
            monthname = MONTHS[num];
        }
        return monthname;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.expense_icon) {
            Intent intent = new Intent(this, ExpenseActivity.class);
            startActivity(intent);
        } else if (id == R.id.income_icon) {
            Intent intent = new Intent(this, IncomeActivity.class);
            startActivity(intent);
        } else if (id == R.id.last_10_records) {

            AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
            LayoutInflater inflater = getLayoutInflater();
            View convertView = (View) inflater.inflate(R.layout.last10records_dialog, null);
            alertDialog.setView(convertView);
            TextView title = new TextView(this);
            title.setText("Records");
            title.setBackgroundColor(Color.DKGRAY);
            title.setPadding(10, 10, 10, 10);
            title.setGravity(Gravity.CENTER);
            title.setTextColor(Color.WHITE);
            title.setTextSize(20);

            alertDialog.setCustomTitle(title);
            //alertDialog.setTitle("Last 10 Records");
            ArrayList<Expense> arrayOfItems = new ArrayList<Expense>();
            adapter = new ExpenseCustomAdapter(this, arrayOfItems);
            listview = (ListView) convertView.findViewById(R.id.listView1);
            listview.setAdapter(adapter);
            fetchData(adapter);

            listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position,
                                        long id) {
                    t_id = (TextView) view.findViewById(R.id.get_id);
                    removeItemFromList(position);

                }
            });
            alertDialog.show();
        }

        switch (id) {
            case R.id.fab:

                animateFAB();
                break;
            case R.id.fab1:

                Intent intent = new Intent(this, ExpenseActivity.class);
                startActivity(intent);
                break;
            case R.id.fab2:
                intent = new Intent(this, IncomeActivity.class);
                startActivity(intent);
                break;
        }
    }

    public void fetchData(ExpenseCustomAdapter adapter) {
        Cursor cursor;
        cursor = myDb.getLast10Data();
        if (cursor != null && cursor.moveToFirst()) {
            do {
                Expense cartItem = new Expense(price, category, description, date, id);
                cartItem.setPrice(cursor.getString(1));
                cartItem.setCategory(cursor.getString(2));
                cartItem.setDescription(cursor.getString(3));
                cartItem.setDate(cursor.getString(4));
                cartItem.setId(cursor.getInt(0));
                adapter.add(cartItem);
            }
            while (cursor.moveToNext());
        } else {
            Toast.makeText(MainActivity.this, "No Data found", Toast.LENGTH_SHORT).show();
        }
    }

    protected void removeItemFromList(final int position) {
        int deletePosition = position;
        AlertDialog.Builder alert = new AlertDialog.Builder(
                MainActivity.this);
        alert.setTitle("Delete");
        alert.setMessage("Do you want delete?");
        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (!adapter.isEmpty()) {
                    final ProgressDialog progressdialog = new ProgressDialog(MainActivity.this);
                    progressdialog.setMessage("Please wait..");
                    progressdialog.show();
                    adapter.clear();
                    myDb.deleteExpense(t_id.getText().toString());
                    new android.os.Handler().postDelayed(
                            new Runnable() {
                                public void run() {
                                    Toast.makeText(MainActivity.this, "data deleted successfully", Toast.LENGTH_SHORT).show();
                                    listview.setAdapter(adapter);
                                    fetchData(adapter);
                                    progressdialog.dismiss();

                                }
                            }, 100);
                } else {
                    Toast.makeText(MainActivity.this, "No data found", Toast.LENGTH_SHORT).show();
                }


            }
        });
        alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                dialog.dismiss();
            }
        });
        alert.show();
    }

    public void animateFAB() {

        if (isFabOpen) {

            fab.startAnimation(rotate_backward);
            fab1.startAnimation(fab_close);
            fab2.startAnimation(fab_close);
            fab1.setClickable(false);
            fab2.setClickable(false);
            isFabOpen = false;

        } else {

            fab.startAnimation(rotate_forward);
            fab1.startAnimation(fab_open);
            fab2.startAnimation(fab_open);
            fab1.setClickable(true);
            fab2.setClickable(true);
            isFabOpen = true;

        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);

        } else {
            if (doubleBackToExitPressedOnce) {
                ActivityCompat.finishAffinity(MainActivity.this);
                return;
            }

            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);
        }
    }

    public void CustomNotification() {
        RemoteViews remoteViews = new RemoteViews(getPackageName(),
                R.layout.notificationview);
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setTicker("expense")
                .setAutoCancel(true)
                .setContentIntent(pIntent)
                .setContent(remoteViews);
        remoteViews.setImageViewResource(R.id.imagenotileft, R.mipmap.ic_launcher);
        remoteViews.setTextViewText(R.id.title, "Expense");
        remoteViews.setTextViewText(R.id.text, "Please Enter Your Expense Today");
        NotificationManager notificationmanager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationmanager.notify(0, builder.build());

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        Intent intent;
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_expense_list) {
            intent = new Intent(this, ExpenseListViewActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_income_list) {
            intent = new Intent(this, IncomeListViewActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_chart) {
            intent = new Intent(this, ChartActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_category_wise_expense_list) {
            intent = new Intent(this, MonthAndCategoryWiseListViewActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_total_report) {
            intent = new Intent(this, SeekbarWiseListViewActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_settings) {
            intent = new Intent(this, UserSettingsActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_language) {
            intent = new Intent(this, LanguageActivity.class);
            startActivity(intent);
        }else if (id == R.id.nav_allinone) {
            intent = new Intent(this, AllInOneReportActivity.class);
            startActivity(intent);
        }else if (id == R.id.nav_about_us) {
            intent = new Intent(this, HelpActivity.class);
            startActivity(intent);
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_CALLBACK_CONSTANT) {
            //check if all permissions are granted
            boolean allgranted = false;
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    allgranted = true;
                } else {
                    allgranted = false;
                    break;
                }
            }

            if (allgranted) {
                proceedAfterPermission();
            } else if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, permissionsRequired[0])) {
                android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Need Multiple Permissions");
                builder.setMessage("This app needs Write External Storage...");
                builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        ActivityCompat.requestPermissions(MainActivity.this, permissionsRequired, PERMISSION_CALLBACK_CONSTANT);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            } else {
                Toast.makeText(getBaseContext(), "Unable to get Permission", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_PERMISSION_SETTING) {
            if (ActivityCompat.checkSelfPermission(MainActivity.this, permissionsRequired[0]) == PackageManager.PERMISSION_GRANTED) {
                //Got Permission
                proceedAfterPermission();
            }
        }
    }

    private void proceedAfterPermission() {
        //Toast.makeText(getBaseContext(), "We got All Permissions", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        if (sentToSettings) {
            if (ActivityCompat.checkSelfPermission(MainActivity.this, permissionsRequired[0]) == PackageManager.PERMISSION_GRANTED) {
                //Got Permission
                proceedAfterPermission();
            }
        }
    }

    @Override
    public void onConfigurationChanged(android.content.res.Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (myLocale != null) {
            newConfig.locale = myLocale;
            Locale.setDefault(myLocale);
            getBaseContext().getResources().updateConfiguration(newConfig, getBaseContext().getResources().getDisplayMetrics());
        }
    }
}
