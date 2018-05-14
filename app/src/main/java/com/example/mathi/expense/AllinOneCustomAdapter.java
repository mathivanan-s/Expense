package com.example.mathi.expense;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


public class AllinOneCustomAdapter extends ArrayAdapter<String>  {

    private final Activity context;
    private final String[] strArr;
    private final String[] strArr1;
    private final String[] strArr2;
    TextView month,expense,income,balance;


    public AllinOneCustomAdapter(Activity context, String[] strArr, String[] strArr1,String[] strArr2)
    {

        super(context, R.layout.all_in_one_report_list, strArr);

        this.context=context;
        this.strArr=strArr;
        this.strArr1=strArr1;
        this.strArr2=strArr2;

    }
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.all_in_one_report_list, null,true);
        int a,b,c;
        a= Integer.parseInt(strArr[position]);
        b= Integer.parseInt(strArr1[position]);
        c=a-b;
        month = (TextView) rowView.findViewById(R.id.month);
        expense = (TextView) rowView.findViewById(R.id.expense_total);
        income=(TextView)rowView.findViewById(R.id.income_total);
        balance=(TextView)rowView.findViewById(R.id.balance_total);
        month.setText(strArr2[position]);
        income.setText(strArr[position]+".00");
        expense.setText(strArr1[position]+".00");
        balance.setText(String.valueOf(c)+".00");


        return rowView;

    };
}
