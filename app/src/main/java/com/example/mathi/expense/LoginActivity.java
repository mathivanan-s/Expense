package com.example.mathi.expense;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mathi.expense.expense.ExpenseActivity;

/**
 * Created by MATHI on 8/28/2017.
 */

public class LoginActivity  extends AppCompatActivity implements View.OnClickListener {
    DatabaseHelper databaseHelper;
    Button save_btn,cancel_btn;
    EditText user_name,job_title,company,salary,email;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimary));

        databaseHelper = new DatabaseHelper(this);

        save_btn=(Button)findViewById(R.id.save);
        save_btn.setOnClickListener(this);
        cancel_btn=(Button)findViewById(R.id.cancel);
        cancel_btn.setOnClickListener(this);
        user_name=(EditText)findViewById(R.id.name);
        job_title=(EditText)findViewById(R.id.job_title);
        company=(EditText)findViewById(R.id.company_name);
        salary=(EditText)findViewById(R.id.salary);
        email=(EditText)findViewById(R.id.email);
    }

    @Override
    public void onClick(View view) {
        int id=view.getId();
        if(id==R.id.save){
            if(user_name.length()==0){
                Toast.makeText(this, "Please Enter Your Name..", Toast.LENGTH_SHORT).show();
            }
            else if(job_title.length()==0){
                Toast.makeText(this, "Please Enter Your Job Title..", Toast.LENGTH_SHORT).show();
            }
            else if(company.length()==0){
                Toast.makeText(this, "Please Enter Your Company..", Toast.LENGTH_SHORT).show();
            }
            else if(salary.length()==0){
                Toast.makeText(this, "Please Enter Your Salary..", Toast.LENGTH_SHORT).show();
            }
            else if(email.length()==0){
                Toast.makeText(this, "Please Enter Your E-mail..", Toast.LENGTH_SHORT).show();
            }
            else {
                databaseHelper.updateUserDetails(DatabaseHelper.USER_TABLE_COL_2,true);
                Intent intent=new Intent(this,MainActivity.class);
                startActivity(intent);
                insert_database();
            }

        }
        if(id==R.id.cancel){
            finish();
        }
    }
    public void insert_database(){
        boolean inserted = databaseHelper.insertUserDetail(user_name.getText().toString(),job_title.getText().toString(),
                company.getText().toString(),salary.getText().toString(),email.getText().toString());
        if(inserted)
        {
            Toast.makeText(LoginActivity.this,"Data Saved", Toast.LENGTH_LONG).show();
        }
        else
        {
            Toast.makeText(LoginActivity.this,"Data not Saved", Toast.LENGTH_LONG).show();
        }
    }
}
