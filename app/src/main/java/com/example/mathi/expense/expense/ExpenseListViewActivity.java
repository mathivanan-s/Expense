package com.example.mathi.expense.expense;

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
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
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
import com.example.mathi.expense.income.IncomeListViewActivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Calendar;

public class ExpenseListViewActivity extends AppCompatActivity implements View.OnClickListener {
    String price,category,description,date;
    int id;
    TextView ttotal,t_id,t_category,t_description,t_price,t_date;
    DatabaseHelper myDb;
    ListView listview,edit_del_listview;
    ImageView record;
    Spinner month_spinner;
    ArrayAdapter<String> month_spinadapter;
    String state,months;
    int a;
    EditText input;
    String[] MONTHS = {"January", "February", "March","April","May","June","July",
            "August","September","October","November", "December"};
    String[] OPTION={"EDIT","DELETE"};
    ImageButton imageButton;
    ExpenseCustomAdapter adapter;
    EditText add_expense,add_description;
    Button s_button,date_picker;
    DatePickerDialog datePickerDialog;
    Spinner category_spinner;
    String select_category,select_date;
    ArrayAdapter<String> spinadapter;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.expense_listview_activity);

        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimary));
        myDb = new DatabaseHelper(this);
        record=(ImageView)findViewById(R.id.empty_notification_text);
        record.setVisibility(View.INVISIBLE);

        ttotal=(TextView)findViewById(R.id.expense_total);

        imageButton=(ImageButton)findViewById(R.id.imageButton);
        imageButton.setOnClickListener(this);

        Calendar c = Calendar.getInstance();
        int mMonth = c.get(Calendar.MONTH);

        ArrayList<Expense> arrayOfItems = new ArrayList<Expense>();
        adapter = new ExpenseCustomAdapter(this, arrayOfItems);

        month_spinadapter = new ArrayAdapter<String>(ExpenseListViewActivity.this, R.layout.month_spin_list, MONTHS);
        month_spinner = (Spinner) findViewById(R.id.expense_month_spinner);
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
                listview = (ListView) findViewById(R.id.data_list_view);
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

                Cursor c = myDb.getTotalexpense(state);
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
        final android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.
                Builder(ExpenseListViewActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        View convertView = (View) inflater.inflate(R.layout.edit_delete_listview, null);
        alertDialog.setView(convertView);

        ArrayAdapter<String> arrayAdapter=new ArrayAdapter<String>(ExpenseListViewActivity.this,
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
        alertDialog.show();
    }
    public void editDialogeset(){

        android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.
                Builder(ExpenseListViewActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        View convertView = (View) inflater.inflate(R.layout.edit_expense_activity, null);
        alertDialog.setView(convertView);

        date_picker=(Button)convertView.findViewById(R.id.expense_date);
        s_button=(Button)convertView.findViewById(R.id.expense_save_btn);
        add_expense=(EditText)convertView.findViewById(R.id.expense_edit_price);
        add_description=(EditText)convertView.findViewById(R.id.expense_description_edit);
        add_expense.setText(""+t_price.getText().toString());

        date_picker.setText(t_date.getText().toString());
        add_description.setText(t_description.getText().toString());

        date_picker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);

                datePickerDialog = new DatePickerDialog(ExpenseListViewActivity.this,
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

        spinadapter = new ArrayAdapter<String>(ExpenseListViewActivity.this, R.layout.expense_spin_list, myDb.getcategorylist());
        category_spinner = (Spinner) convertView.findViewById(R.id.expense_category_list_spinner);
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
            if(add_expense.length()==0){
                Toast.makeText(ExpenseListViewActivity.this, "Please Enter Your Expense", Toast.LENGTH_SHORT).show();
            }

            else  if(date_picker.length()==0){
                Toast.makeText(ExpenseListViewActivity.this, "Please Enter the Date", Toast.LENGTH_SHORT).show();
            }
            else  if(add_description.length()==0){
                Toast.makeText(ExpenseListViewActivity.this, "Please Enter the Description", Toast.LENGTH_SHORT).show();
            }
            else{
                insert_database();
            }
                                                }
                                            });

    }
    public void insert_database(){
        boolean inserted = myDb.updateExpenseData(add_expense.getText().toString(),
                select_category,add_description.getText().toString(),date_picker.getText().toString(),t_id.getText().toString());
        if(inserted) {
            finish();
            overridePendingTransition( 0, 0);
            startActivity(getIntent());
            overridePendingTransition( 0, 0);
            Toast.makeText(ExpenseListViewActivity.this,"Data Saved", Toast.LENGTH_LONG).show();
        }
        else {
            Toast.makeText(ExpenseListViewActivity.this,"Data not Saved", Toast.LENGTH_LONG).show();
        }
    }
    protected void removeItemFromList() {
        android.app.AlertDialog.Builder alert = new android.app.AlertDialog.Builder(
                ExpenseListViewActivity.this);
        alert.setTitle("Delete");
        alert.setMessage("Do you want delete?");
        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialog, int which) {

                if (!adapter.isEmpty()) {
                    final ProgressDialog progressdialog = new ProgressDialog(ExpenseListViewActivity.this);
                    progressdialog.setMessage("Please wait..");
                    progressdialog.show();
                    myDb.deleteExpense(t_id.getText().toString());
                    new android.os.Handler().postDelayed(
                            new Runnable() {
                                public void run() {
                                    Toast.makeText(ExpenseListViewActivity.this, "Data deleted successfully", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(ExpenseListViewActivity.this, "No Data found", Toast.LENGTH_SHORT).show();
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



    public void fetchData(ExpenseCustomAdapter adapter){
        Cursor cursor;
        cursor = myDb.getexpenseData(state);
        if (cursor != null && cursor.moveToFirst()) {
            record.setVisibility(View.INVISIBLE);
            do {
                Expense cartItem = new Expense(price,category,description,date,id);
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
        if (id == R.id.imageButton) {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(ExpenseListViewActivity.this);
            alertDialog.setTitle("Export to CSV");
            alertDialog.setMessage("Enter File Name");

            input = new EditText(ExpenseListViewActivity.this);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT);
            input.setLayoutParams(lp);
            alertDialog.setView(input);
            input.setText(months + " expense.csv");
            alertDialog.setIcon(R.drawable.csvfileimage);
            alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    if (input.length() == 0) {
                        Toast.makeText(ExpenseListViewActivity.this, "Please Enter File Name..", Toast.LENGTH_SHORT).show();
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
            Cursor curCSV = db.rawQuery("SELECT * FROM EXPENSETABLE where strftime('%m', DATE) ='"+state+"'", null);
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
    private void exportDB(){
        File sd = Environment.getExternalStorageDirectory();
        File data = Environment.getDataDirectory();
        FileChannel source=null;
        FileChannel destination=null;
        String currentDBPath = "/data/"+ "com.example.mathi.expense" +"/databases/"+"Expense";
        String backupDBPath = "Expense";
        File currentDB = new File(data, currentDBPath);
        File backupDB = new File(sd, backupDBPath);
        try {
            source = new FileInputStream(currentDB).getChannel();
            destination = new FileOutputStream(backupDB).getChannel();
            destination.transferFrom(source, 0, source.size());
            source.close();
            destination.close();
            Toast.makeText(this, "DB Exported!", Toast.LENGTH_LONG).show();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
}
