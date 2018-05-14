package com.example.mathi.expense.income;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.mathi.expense.DatabaseHelper;
import com.example.mathi.expense.MainActivity;
import com.example.mathi.expense.R;
import com.example.mathi.expense.income_category.IncomeCategoryListAddActivity;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.Calendar;

/**
 * Created by MATHI on 8/1/2017.
 */

public class IncomeActivity extends AppCompatActivity {

    EditText add_income,add_description;
    Button s_button,date_picker;
    DatePickerDialog datePickerDialog;
    Spinner category_spinner;
    String select_category,current_date,select_date;
    ArrayAdapter<String> spinadapter;
    DatabaseHelper databaseHelper;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.income_activity);

        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimary));
        databaseHelper = new DatabaseHelper(this);
        date_picker=(Button) findViewById(R.id.income_date);
        s_button=(Button)findViewById(R.id.income_save_btn);
        add_income=(EditText)findViewById(R.id.income_edit_price);

        add_description=(EditText)findViewById(R.id.income_description_edit);


        AdView mAdView = (AdView) findViewById(R.id.adView2);

        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .addTestDevice("AAB57E57F502361943BF3EDBAE4D0903")
                .build();
        mAdView.loadAd(adRequest);


          call_date_picker_and_spinner();

    }

    @Override
    protected void onResume() {
        call_date_picker_and_spinner();
        super.onResume();
    }

    public void call_date_picker_and_spinner(){
        Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);

        int mnth=mMonth+1;
        String mnth1;
        if (mnth < 10) {
            mnth1="0"+mnth;
        }
        else{
            mnth1=""+mnth;
        }
        int date=mDay;
        String day1;
        if (date< 10) {
            day1="0"+date;
        }
        else{
            day1=""+date;
        }
        current_date=String.valueOf(mYear+"-"+mnth1+"-"+day1);
        date_picker.setText(current_date);

        date_picker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);

                datePickerDialog = new DatePickerDialog(IncomeActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                int mnth=monthOfYear+1;
                                String mnth1;
                                if (mnth < 10) {
                                    mnth1="0"+mnth;
                                }
                                else{
                                    mnth1=""+mnth;
                                }
                                int date=dayOfMonth;
                                String day1;
                                if (date< 10) {
                                    day1="0"+date;
                                }
                                else{
                                    day1=""+date;
                                }
                                select_date=(year + "-" + mnth1 + "-" + day1);
                                date_picker.setText(select_date);
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        spinadapter = new ArrayAdapter<String>(IncomeActivity.this, R.layout.income_spin_list, databaseHelper.getincomecategorylist());
        category_spinner = (Spinner) findViewById(R.id.income_category_list_spinner);
        category_spinner.setAdapter(spinadapter);
        category_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                TextView category_text=(TextView)view.findViewById(R.id.income_category);
                select_category = category_text.getText().toString();
            }@Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });
        addData();
    }
    public void addData() {
        s_button.setOnClickListener(new
               View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(add_income.length()==0){
                    Toast.makeText(IncomeActivity.this, "Please Enter Your Expense", Toast.LENGTH_SHORT).show();
                }

                else  if(date_picker.length()==0){
                    Toast.makeText(IncomeActivity.this, "Please Enter the Date", Toast.LENGTH_SHORT).show();
                }
                else  if(add_description.length()==0){
                    Toast.makeText(IncomeActivity.this, "Please Enter Description", Toast.LENGTH_SHORT).show();
                }
                else{
                    insert_database();
                }
            }
        });

    }
    public void insert_database(){
        boolean inserted = databaseHelper.insertIncomeData(add_income.getText().toString(),select_category,add_description.getText().toString(),date_picker.getText().toString());
        if(inserted) {
            finish();
            overridePendingTransition( 0, 0);
            startActivity(getIntent());
            overridePendingTransition( 0, 0);
            Toast.makeText(IncomeActivity.this,"Data Saved", Toast.LENGTH_LONG).show();
        }
        else {
            Toast.makeText(IncomeActivity.this,"Data not Saved", Toast.LENGTH_LONG).show();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.income_bar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_category) {
            Intent intent=new Intent(this,IncomeCategoryListAddActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
