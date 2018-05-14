package com.example.mathi.expense;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mathi.expense.category.CategoryListAddActivity;
import com.example.mathi.expense.income.Income;
import com.example.mathi.expense.income.IncomeListViewActivity;
import com.example.mathi.expense.income_category.IncomeCategoryListAddActivity;

/**
 * Created by MATHI on 8/1/2017.
 */

public class UserSettingsActivity extends AppCompatActivity implements View.OnClickListener {
    DatabaseHelper databaseHelper;
    TextView username,job,company,salary,email,edit_choose_language,edit_income,edit_expense;
    EditText user_name,job_title,company_name,t_salary,e_mail;
    int id;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_settings_activity);

        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimary));
        databaseHelper = new DatabaseHelper(this);
        username=(TextView)findViewById(R.id.user_text);
        job=(TextView)findViewById(R.id.job_text);
        company=(TextView)findViewById(R.id.company_text);
        salary=(TextView)findViewById(R.id.salary_text);
        email=(TextView)findViewById(R.id.email_text);

        edit_income=(TextView)findViewById(R.id.edit_income);
        edit_expense=(TextView)findViewById(R.id.edit_expense);
        edit_choose_language=(TextView)findViewById(R.id.edit_choose_language);

        TextView edit=(TextView)findViewById(R.id.edit_profile);
        edit.setOnClickListener(this);
        edit_income.setOnClickListener(this);
        edit_expense.setOnClickListener(this);
        edit_choose_language.setOnClickListener(this);

        Cursor cursor;
        cursor = databaseHelper.getuserdetail();
        if (cursor != null && cursor.moveToFirst()) {
            do {
                id=cursor.getInt(cursor.getColumnIndex("ID"));
                username.setText(cursor.getString(cursor.getColumnIndex("USER_NAME")));
                job.setText(cursor.getString(cursor.getColumnIndex("JOB")));
                company.setText(cursor.getString(cursor.getColumnIndex("COMPANY")));
                salary.setText(cursor.getString(cursor.getColumnIndex("SALARY")));
                email.setText(cursor.getString(cursor.getColumnIndex("EMAIL")));
            }
            while (cursor.moveToNext());
        }

    }

    @Override
    public void onClick(View view) {
        Intent intent;
        int id=view.getId();
        if(id==R.id.edit_profile){
            editDialogeset();
        }
        else if(id==R.id.edit_income){
            intent=new Intent(this, IncomeCategoryListAddActivity.class);
            startActivity(intent);
        }
        else if(id==R.id.edit_expense){
            intent=new Intent(this, CategoryListAddActivity.class);
            startActivity(intent);
        }
        else if(id==R.id.edit_choose_language){
            intent=new Intent(this, LanguageActivity.class);
            startActivity(intent);
        }

    }
    public void editDialogeset(){

        android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.
                Builder(UserSettingsActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        View convertView = (View) inflater.inflate(R.layout.edit_user_activity, null);
        alertDialog.setView(convertView);

        user_name=(EditText)convertView.findViewById(R.id.name);
        user_name.setText(username.getText().toString());
        job_title=(EditText)convertView.findViewById(R.id.job_title);
        job_title.setText(job.getText().toString());
        company_name=(EditText)convertView.findViewById(R.id.company_name);
        company_name.setText(company.getText().toString());
        t_salary=(EditText)convertView.findViewById(R.id.salary);
        t_salary.setText(salary.getText().toString());
        e_mail=(EditText)convertView.findViewById(R.id.email);
        e_mail.setText(email.getText().toString());
        alertDialog.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(user_name.length()==0){
                    Toast.makeText(UserSettingsActivity.this, "Please Enter Your Name..", Toast.LENGTH_SHORT).show();
                }
                else if(job_title.length()==0){
                    Toast.makeText(UserSettingsActivity.this, "Please Enter Your Job Title..", Toast.LENGTH_SHORT).show();
                }
                else if(company_name.length()==0){
                    Toast.makeText(UserSettingsActivity.this, "Please Enter Your Company..", Toast.LENGTH_SHORT).show();
                }
                else if(t_salary.length()==0){
                    Toast.makeText(UserSettingsActivity.this, "Please Enter Your Salary..", Toast.LENGTH_SHORT).show();
                }
                else if(e_mail.length()==0){
                    Toast.makeText(UserSettingsActivity.this, "Please Enter Your E-mail..", Toast.LENGTH_SHORT).show();
                }
                else {
                    insert_database();
                }
            }
        });
        alertDialog.setNegativeButton("Cancel",null);

        alertDialog.show();
    }
    public void insert_database(){
        boolean inserted = databaseHelper.updateUserDetail(user_name.getText().toString(),job_title.getText().toString(),
                company.getText().toString(),salary.getText().toString(),email.getText().toString(),id);
        if(inserted)
        {
            finish();
            overridePendingTransition( 0, 0);
            startActivity(getIntent());
            overridePendingTransition( 0, 0);
            Toast.makeText(UserSettingsActivity.this,"Data Saved", Toast.LENGTH_LONG).show();
        }
        else
        {
            Toast.makeText(UserSettingsActivity.this,"Data not Saved", Toast.LENGTH_LONG).show();
        }
    }


}
