package com.example.mathi.expense;

import android.app.Activity;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mathi.expense.expense.Expense;
import com.example.mathi.expense.expense.ExpenseCustomAdapter;
import com.example.mathi.expense.income.Income;
import com.example.mathi.expense.income.IncomeCustomAdapter;
import com.example.mathi.expense.income.IncomeListViewActivity;
import com.github.mikephil.charting.data.BarEntry;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by MATHI on 8/10/2017.
 */

public class SeekbarWiseListViewActivity extends AppCompatActivity implements View.OnClickListener {
    Spinner category_spinner, month_spinner;
    ArrayAdapter<String> category_spinadapter, month_spinadapter;
    String[] strArr, strArr1;
    JSONArray jsonArray, jsonArray1;
    ArrayList input, input1;
    DatabaseHelper myDb;
    ListView listview,individual_listview;
    String[] TABLE_NAME = {"Expense", "Income"};
    String[] MONTHS = {"January", "February", "March", "April", "May", "June", "July",
            "August", "September", "October", "November", "December"};
    String months, state, get_category, state1,price,category,description,date;
    int id;
    SeekbarCustomListAdapter adapter;
    TextView t_category,t_id,t_price;
    ExpenseCustomAdapter customadapter;
    Button click_view;
    ImageView no_data_image;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.seekbar_listview_activity);

        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimary));

        click_view=(Button)findViewById(R.id.view);
        no_data_image=(ImageView)findViewById(R.id.no_data_image);
        no_data_image.setVisibility(View.INVISIBLE);

        myDb = new DatabaseHelper(this);

        listview = (ListView) findViewById(R.id.seekbar_listview);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                t_category = (TextView) view.findViewById(R.id.get_category);
                t_id = (TextView) view.findViewById(R.id.get_id);
                t_price = (TextView) view.findViewById(R.id.get_price);

                editdeletedialog();

            }
        });
        category_spinadapter = new ArrayAdapter<String>(SeekbarWiseListViewActivity.this, R.layout.category_spin_list, TABLE_NAME);
        category_spinner = (Spinner) findViewById(R.id.seekbar_category_listview_spinner);
        category_spinner.setAdapter(category_spinadapter);
        category_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                TextView cat=(TextView)view.findViewById(R.id.select_category_text);
                get_category = cat.getText().toString();

                switch (get_category) {
                    case "Expense":
                        state1 = "EXPENSETABLE";
                        break;
                    case "Income":
                        state1 = "INCOMETABLE";
                        break;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });
        Calendar c = Calendar.getInstance();
        int mMonth = c.get(Calendar.MONTH);

        month_spinadapter = new ArrayAdapter<String>(SeekbarWiseListViewActivity.this, R.layout.month_spin_list, MONTHS);
        month_spinner = (Spinner) findViewById(R.id.month_month_listview_spinner);
        month_spinner.setAdapter(month_spinadapter);
        month_spinner.setSelection(mMonth);
        month_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                TextView mon=(TextView)view.findViewById(R.id.select_month_text);
                months = mon.getText().toString();

               // months = (String) month_spinner.getSelectedItem();
                switch (months) {
                    case "January":
                        state = "01";
                        break;
                    case "February":
                        state = "02";
                        break;
                    case "March":
                        state = "03";
                        break;
                    case "April":
                        state = "04";
                        break;
                    case "May":
                        state = "05";
                        break;
                    case "June":
                        state = "06";
                        break;
                    case "July":
                        state = "07";
                        break;
                    case "August":
                        state = "08";
                        break;
                    case "September":
                        state = "09";
                        break;
                    case "October":
                        state = "10";
                        break;
                    case "November":
                        state = "11";
                        break;
                    case "December":
                        state = "12";
                        break;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });
        click_view.setOnClickListener(this);
    }
    public void editdeletedialog(){
        android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.
                Builder(SeekbarWiseListViewActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        View convertView = (View) inflater.inflate(R.layout.categorywise_individual_list_dialog, null);
        alertDialog.setView(convertView);
        TextView title = new TextView(this);

        title.setText("Details");
        title.setBackgroundColor(Color.DKGRAY);
        title.setPadding(10, 10, 10, 10);
        title.setGravity(Gravity.CENTER);
        title.setTextColor(Color.WHITE);
        title.setTextSize(20);

        alertDialog.setCustomTitle(title);

        ArrayList<Expense> arrayOfItems = new ArrayList<Expense>();
        customadapter = new ExpenseCustomAdapter(this, arrayOfItems);
        individual_listview =  (ListView) convertView.findViewById(R.id.indvidual_listView);
        TextView total=(TextView)convertView.findViewById(R.id.individual_toal);
        total.setText("Total: "+t_price.getText().toString());
        individual_listview.setAdapter(customadapter);
        fetchData(customadapter);

        individual_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {


            }
        });
        alertDialog.show();
    }
    @Override
    public void onClick(View view) {
        int id=view.getId();
        if(id==R.id.view){
            input = myDb.getgroupofsumofprice(state1, state);
            input1 = myDb.getgroupofcategory(state1, state);
            AddValuesToPIEENTRY();

        }

    }
    public void fetchData(ExpenseCustomAdapter adapter){
        Cursor cursor;
        cursor = myDb.getindividualData(state1,state,t_category.getText().toString());
        if (cursor != null && cursor.moveToFirst()) {
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
            Toast.makeText(SeekbarWiseListViewActivity.this, "No Data found", Toast.LENGTH_SHORT).show();
        }
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
        }

        jsonArray1 = new JSONArray(input1);
        strArr1 = new String[jsonArray1.length()];
        for (int j = 0; j < jsonArray1.length(); j++) {
            try {
                strArr1[j] = jsonArray1.getString(j);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        adapter = new SeekbarCustomListAdapter(SeekbarWiseListViewActivity.this,strArr, strArr1);
        if(!adapter.isEmpty()){
            listview.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            no_data_image.setVisibility(View.INVISIBLE);
        }
        else{
            no_data_image.setVisibility(View.VISIBLE);
        }
    }


}