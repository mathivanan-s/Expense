package com.example.mathi.expense.income;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mathi.expense.CSVWriter;
import com.example.mathi.expense.DatabaseHelper;
import com.example.mathi.expense.MainActivity;
import com.example.mathi.expense.R;
import com.example.mathi.expense.category.CategoryListAddActivity;
import com.example.mathi.expense.expense.ExpenseListViewActivity;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Calendar;

public class IncomeListViewActivity extends AppCompatActivity implements View.OnClickListener {
    android.app.AlertDialog.Builder alertDialog,edit_del_alertDialog;
    String price,category,date,description;
    int id,a;
    TextView t_id,t_category,t_description,t_price,t_date,ttotal;
    DatabaseHelper myDb;
    ListView listview,edit_del_listview;
    IncomeCustomAdapter adapter;
    Spinner month_spinner;
    ImageView record;
    ArrayAdapter<String> month_spinadapter;
    String state,months;
    String[] MONTHS = {"January", "February", "March","April","May","June","July",
            "August","September","October","November", "December"};
    String[] OPTION={"EDIT","DELETE"};

    EditText input,add_income,add_description;
    Button s_button,date_picker;
    DatePickerDialog datePickerDialog;
    Spinner category_spinner;
    String select_category,select_date;
    ArrayAdapter<String> spinadapter;
    ImageButton imageButton;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.income_listview_activity);

        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimary));
        myDb = new DatabaseHelper(this);


        record=(ImageView) findViewById(R.id.income_empty_data_text);
        record.setVisibility(View.INVISIBLE);

        ttotal=(TextView)findViewById(R.id.income_total);
        imageButton=(ImageButton)findViewById(R.id.exportcsv);
        imageButton.setOnClickListener(this);

        ArrayList<Income> arrayOfItems = new ArrayList<Income>();
        adapter = new IncomeCustomAdapter(this, arrayOfItems);

        Calendar c = Calendar.getInstance();
        int mMonth = c.get(Calendar.MONTH);


        month_spinadapter = new ArrayAdapter<String>(IncomeListViewActivity.this, R.layout.month_spin_list, MONTHS);
        month_spinner = (Spinner) findViewById(R.id.income_month_spinner);
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
                //clear or refresh adapter
                listview = (ListView) findViewById(R.id.income_data_list_view);
                adapter.clear();
                listview.setAdapter(adapter);
                fetchData(adapter);

                listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position,
                                            long id) {
                        t_category = (TextView) view.findViewById(R.id.get_category);
                        t_id = (TextView) view.findViewById(R.id.get_id);
                        t_date= (TextView) view.findViewById(R.id.get_date);
                        t_price= (TextView) view.findViewById(R.id.get_price);
                        t_description= (TextView) view.findViewById(R.id.get_description);

                        editdeletedialog();

                    }
                });
                Cursor c = myDb.getTotalincome(state);
                if( c != null && c.moveToFirst()){
                    a=c.getInt(0);
                    ttotal.setText("Total :Rs."+a+"");
                }


            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });

    }
    public void editdeletedialog(){
         edit_del_alertDialog = new android.app.AlertDialog.
                Builder(IncomeListViewActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        View convertView = (View) inflater.inflate(R.layout.edit_delete_listview, null);
        edit_del_alertDialog.setView(convertView);

        ArrayAdapter<String> arrayAdapter=new ArrayAdapter<String>(IncomeListViewActivity.this,
                R.layout.edit_del_list,OPTION);
        edit_del_listview =  (ListView) convertView.findViewById(R.id.edit_del_lv);
        edit_del_listview.setAdapter(arrayAdapter);

        edit_del_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                TextView t_get_text = (TextView) view.findViewById(R.id.del_edit_text);
                String option=t_get_text.getText().toString();
                switch(option) {
                    case "EDIT":
                       editDialogeset();
                        break;
                    case "DELETE":
                        removeItemFromList();
                        break;
                }

            }
        });
        edit_del_alertDialog.show();
    }

    public void editDialogeset(){

        alertDialog = new android.app.AlertDialog.
                Builder(IncomeListViewActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        View convertView = (View) inflater.inflate(R.layout.edit_income_activity, null);
        alertDialog.setView(convertView);

        date_picker=(Button)convertView.findViewById(R.id.income_date);
        s_button=(Button)convertView.findViewById(R.id.income_save_btn);
        add_income=(EditText)convertView.findViewById(R.id.income_edit_price);
        add_description=(EditText)convertView.findViewById(R.id.income_description_edit);
        add_income.setText(""+t_price.getText().toString());

        date_picker.setText(t_date.getText().toString());
        add_description.setText(t_description.getText().toString());

        date_picker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);

                datePickerDialog = new DatePickerDialog(IncomeListViewActivity.this,
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

        spinadapter = new ArrayAdapter<String>(IncomeListViewActivity.this, R.layout.income_spin_list, myDb.getincomecategorylist());
        category_spinner = (Spinner) convertView.findViewById(R.id.income_category_list_spinner);
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

        alertDialog.show();
    }
    public void addData() {
        s_button.setOnClickListener(new
            View.OnClickListener() {
                @Override
                public void onClick(View view) {
            if(add_income.length()==0){
                Toast.makeText(IncomeListViewActivity.this, "Please Enter Your Expense", Toast.LENGTH_SHORT).show();
            }

            else  if(date_picker.length()==0){
                Toast.makeText(IncomeListViewActivity.this, "Please Enter the Date", Toast.LENGTH_SHORT).show();
            }
            else  if(add_description.length()==0){
                Toast.makeText(IncomeListViewActivity.this, "Please Enter the Description", Toast.LENGTH_SHORT).show();
            }
            else{
                insert_database();
            }
        }
    });

    }
    public void insert_database(){
        boolean inserted = myDb.updateIncomeData(add_income.getText().toString(),
                select_category,add_description.getText().toString(),date_picker.getText().toString(),t_id.getText().toString());
        if(inserted) {
            Intent intent = getIntent();
            finish();
            startActivity(intent);
            Toast.makeText(IncomeListViewActivity.this,"Data Saved", Toast.LENGTH_LONG).show();
        }
        else {
            Toast.makeText(IncomeListViewActivity.this,"Data not Saved", Toast.LENGTH_LONG).show();
        }
    }

    protected void removeItemFromList() {
        AlertDialog.Builder alert = new AlertDialog.Builder(
                IncomeListViewActivity.this);
        alert.setTitle("Delete");
        alert.setMessage("Do you want delete?");
        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (!adapter.isEmpty()) {
                    final ProgressDialog progressdialog = new ProgressDialog(IncomeListViewActivity.this);
                    progressdialog.setMessage("Please wait..");
                    progressdialog.show();
                    adapter.clear();
                    myDb.deleteIncome(t_id.getText().toString());
                    new android.os.Handler().postDelayed(
                            new Runnable() {
                                public void run() {
                                    Toast.makeText(IncomeListViewActivity.this, "Data deleted successfully", Toast.LENGTH_SHORT).show();
                                    finish();
                                    overridePendingTransition( 0, 0);
                                    startActivity(getIntent());
                                    overridePendingTransition( 0, 0);
                                    progressdialog.dismiss();

                                }
                            }, 100);
                }
                else
                {
                    Toast.makeText(IncomeListViewActivity.this, "No Data found", Toast.LENGTH_SHORT).show();
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

    public void fetchData(IncomeCustomAdapter adapter){
        Cursor cursor;
        cursor = myDb.getincomeData(state);
        if (cursor != null && cursor.moveToFirst()) {
            record.setVisibility(View.INVISIBLE);
            do {
                Income cartItem = new Income(price,category,description,date,id);
                cartItem.setPrice(cursor.getString(1));
                cartItem.setCategory(cursor.getString(2));
                cartItem.setDescription(cursor.getString(3));
                cartItem.setDate(cursor.getString(4));
                cartItem.setId(cursor.getInt(0));
                adapter.add(cartItem);
            }
            while (cursor.moveToNext());
        }
        else {
            record.setVisibility(View.VISIBLE);
        }
    }
    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.exportcsv) {
            android.support.v7.app.AlertDialog.Builder alertDialog = new android.support.v7.app.AlertDialog.Builder(IncomeListViewActivity.this);
            alertDialog.setTitle("Export to CSV");
            alertDialog.setMessage("Enter File Name");

            input = new EditText(IncomeListViewActivity.this);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT);
            input.setLayoutParams(lp);
            alertDialog.setView(input);
            input.setText(months + " income.csv");
            alertDialog.setIcon(R.drawable.csvfileimage);
            alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    if (input.length() == 0) {
                        Toast.makeText(IncomeListViewActivity.this, "Please Enter File Name..", Toast.LENGTH_SHORT).show();
                    } else {
                        exportCSV();
                    }
                }
            });

            alertDialog.setNegativeButton("NO",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
            alertDialog.show();
        }

    }
    private void exportCSV() {

        // File dbFile = getDatabasePath("Expense.db");
        File exportDir = new File(Environment.getExternalStorageDirectory(), "Expense");
        if (!exportDir.exists()) {
            exportDir.mkdirs();
        }

        File file = new File(exportDir, input.getText().toString());
        try {
            file.createNewFile();
            CSVWriter csvWrite = new CSVWriter(new FileWriter(file));
            SQLiteDatabase db = myDb.getReadableDatabase();
            Cursor curCSV = db.rawQuery("SELECT * FROM INCOMETABLE where strftime('%m', DATE) ='"+state+"'", null);
            csvWrite.writeNext(curCSV.getColumnNames());
            while (curCSV.moveToNext()) {
                //Which column you want to exprort
                String arrStr[] = {curCSV.getString(0), curCSV.getString(1),curCSV.getString(2),
                        curCSV.getString(3),curCSV.getString(4)};
                csvWrite.writeNext(arrStr);

            }
            Toast.makeText(this, "Successfully saved to csv file....", Toast.LENGTH_SHORT).show();
            csvWrite.close();
            curCSV.close();
        } catch (Exception sqlEx) {
            Toast.makeText(this, "Failed....", Toast.LENGTH_SHORT).show();
        }
    }

}
