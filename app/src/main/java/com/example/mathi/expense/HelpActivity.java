package com.example.mathi.expense;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

public class HelpActivity extends AppCompatActivity implements View.OnClickListener {

    TextView send_feedback,about_us,update;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        send_feedback=(TextView)findViewById(R.id.send_feedback);
        about_us=(TextView)findViewById(R.id.about_us);
        send_feedback.setOnClickListener(this);
        about_us.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        int id=view.getId();
        if(id==R.id.send_feedback){
            Intent intent=new Intent(this,FeedbackActivity.class);
            startActivity(intent);
        }
        else if(id==R.id.about_us){
            final AlertDialog.Builder alertDialog = new AlertDialog.Builder(HelpActivity.this);
            LayoutInflater inflater = getLayoutInflater();
            View convertView = (View) inflater.inflate(R.layout.about_us, null);
            alertDialog.setView(convertView);
            TextView title = new TextView(this);
            title.setText("About the application");
            title.setBackgroundColor(Color.DKGRAY);
            title.setPadding(10, 10, 10, 10);
            title.setGravity(Gravity.CENTER);
            title.setTextColor(Color.WHITE);
            title.setTextSize(20);
            alertDialog.setNegativeButton("Ok",null);


            alertDialog.setCustomTitle(title);

            alertDialog.show();
        }
    }



}
