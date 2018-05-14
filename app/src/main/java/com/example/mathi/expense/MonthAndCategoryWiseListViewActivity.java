package com.example.mathi.expense;

import android.app.Activity;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.example.mathi.expense.expense.Expense;
import com.example.mathi.expense.expense.ExpenseCustomAdapter;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by MATHI on 8/10/2017.
 */

public class MonthAndCategoryWiseListViewActivity extends Activity  {
    Spinner category_spinner,month_spinner;
    ArrayAdapter<String> category_spinadapter,month_spinadapter;
    TextView category_text;
    ExpenseCustomAdapter adapter;
    ArrayList<Expense> arrayOfItems;
    DatabaseHelper myDb;
    ListView listview;
    String[] MONTHS = {"January", "February", "March","April","May","June","July",
            "August","September","October","November", "December"};
    String price,category,description,date,months,state;
    int id;
    ImageView no_data_image;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.category_listview_activity);

        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimary));
        listview = (ListView) findViewById(R.id.category_listview);
        myDb = new DatabaseHelper(this);

        no_data_image=(ImageView)findViewById(R.id.empty_notification_text);
        no_data_image.setVisibility(View.INVISIBLE);


        arrayOfItems = new ArrayList<Expense>();
        adapter = new ExpenseCustomAdapter(this, arrayOfItems);

        category_spinadapter = new ArrayAdapter<String>(MonthAndCategoryWiseListViewActivity.this, R.layout.category_spin_list, myDb.getcategorylist());
        category_spinner = (Spinner) findViewById(R.id.category_listview_spinner);
        category_spinner.setAdapter(category_spinadapter);
        category_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                category_text=(TextView)view.findViewById(R.id.select_category_text);
                //clear or refresh adapter
                adapter.clear();
                listview.setAdapter(adapter);
                fetchData(adapter);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });

        Calendar c = Calendar.getInstance();
        int mMonth = c.get(Calendar.MONTH);

        month_spinadapter = new ArrayAdapter<String>(MonthAndCategoryWiseListViewActivity.this, R.layout.month_spin_list, MONTHS);
        month_spinner = (Spinner) findViewById(R.id.month_listview_spinner);
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
                adapter.clear();
                listview.setAdapter(adapter);
                fetchData(adapter);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });
    }

    public void fetchData(ExpenseCustomAdapter adapter){
        Cursor cursor;
        cursor = myDb.getcategorywisedata(category_text.getText().toString(),state);
        if (cursor != null && cursor.moveToFirst()) {
            no_data_image.setVisibility(View.INVISIBLE);
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
            no_data_image.setVisibility(View.VISIBLE);
        }
    }
}
