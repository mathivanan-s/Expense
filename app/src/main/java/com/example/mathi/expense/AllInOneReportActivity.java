package com.example.mathi.expense;

import android.database.Cursor;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import java.util.ArrayList;

public class AllInOneReportActivity extends AppCompatActivity {
    DatabaseHelper myDb;
    int a,b,c;
    ListView all_in_one_listview;
    AllinOneCustomAdapter adapter;
    String[] strArr, strArr1,strArr2;
    JSONArray jsonArray, jsonArray1,jsonArray2;
    ArrayList input, input1,input2;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_in_one_report);

        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimary));
        myDb = new DatabaseHelper(this);

        input= new ArrayList<String>();
        input1= new ArrayList<String>();
        input2= new ArrayList<String>();
        all_in_one_listview=(ListView)findViewById(R.id.all_in_one_listview);

        TextView ttotal_income=(TextView)findViewById(R.id.ttotal_income);
        TextView ttotal_expense=(TextView)findViewById(R.id.ttotal_expense);
        TextView ttotal_balance=(TextView)findViewById(R.id.ttotal_balance);
        Cursor cursor = myDb.getallExpense();
        if( cursor != null && cursor.moveToFirst()){
            a=cursor.getInt(0);
            ttotal_expense.setText("Expense :Rs."+a+".00");
        }
        cursor = myDb.getallIncome();
        if( cursor != null && cursor.moveToFirst()){
            b=cursor.getInt(0);
            ttotal_income.setText("Income :Rs."+b+".00");
        }
        c=b-a;
        ttotal_balance.setText("Total Balance :Rs."+c+".00");
        String monthname,monthnamestate;
        String[] STATES = {"01", "02", "03", "04", "05", "06", "07",
                "08", "09", "10", "11", "12"};
        String[] MONTH = {"January", "February", "March","April","May","June","July",
                "August","September","October","November", "December"};
        for(int i=0;i<STATES.length;i++){
            monthnamestate = STATES[i];
            monthname=MONTH[i];
            cursor = myDb.getTotalincome(monthnamestate);
            if (cursor != null && cursor.moveToFirst()) {
                input.add(cursor.getInt(0));
            }
            cursor = myDb.getTotalexpense(monthnamestate);
            if (cursor != null && cursor.moveToFirst()) {
                input1.add(cursor.getInt(0));
            }
            input2.add(monthname);
            fetchData();
        }
    }
    public void fetchData(){
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
        jsonArray2 = new JSONArray(input2);
        strArr2 = new String[jsonArray2.length()];
        for (int j = 0; j < jsonArray2.length(); j++) {
            try {
                strArr2[j] = jsonArray2.getString(j);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            adapter = new AllinOneCustomAdapter(AllInOneReportActivity.this,strArr, strArr1,strArr2);
            all_in_one_listview.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
    }
}
