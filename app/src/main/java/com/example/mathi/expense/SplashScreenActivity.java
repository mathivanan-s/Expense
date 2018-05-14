package com.example.mathi.expense;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

public class SplashScreenActivity extends AppCompatActivity {
    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.splash_screen_activity);

        databaseHelper = new DatabaseHelper(this);

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {

                if(databaseHelper.isAuthenticated()){
                    startActivity(new Intent(SplashScreenActivity.this, MainActivity.class));
                } else {
                    Intent intent = new Intent(SplashScreenActivity.this, LoginActivity.class);
                    intent.putExtra("silent", "true");
                    startActivity(intent);
                }
            }
        }, 1500);

    }
}
