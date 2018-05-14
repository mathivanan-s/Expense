package com.example.mathi.expense;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.TextView;
import java.util.Locale;

public class LanguageActivity extends AppCompatActivity implements View.OnClickListener {
    private Locale myLocale;
    Context context;
    CheckBox checkedText_english,checkedText_tamil,checkedText_hindi;
    TextView choose_lan;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language);

        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimary));
        context = this;
        checkedText_english=(CheckBox)findViewById(R.id.checkedText_english);
        checkedText_tamil=(CheckBox)findViewById(R.id.checkedText_tamil);
        checkedText_hindi=(CheckBox)findViewById(R.id.checkedText_hindi);
        choose_lan=(TextView)findViewById(R.id.choose_language);
        checkedText_english.setOnClickListener(this);
        checkedText_tamil.setOnClickListener(this);
        checkedText_hindi.setOnClickListener(this);
        setInitialCheckboxState();
        loadLocale();

    }
    private void setInitialCheckboxState() {
        String network_mode = Utils.getNetworkMode(LanguageActivity.this);
        if (network_mode.equals("en")) {
            checkedText_english.setChecked(true);
        } else if (network_mode.equals("ta")) {
            checkedText_tamil.setChecked(true);
        }
        else if (network_mode.equals("hi")) {
            checkedText_hindi.setChecked(true);
        }
        else {
            checkedText_english.setChecked(true);
        }
    }

    public void loadLocale()
    {
        String language = Utils.getNetworkMode(this);
        changeLang(language);
    }
    @Override
    public void onClick(View view) {
        Integer id = view.getId();
        String lan="";
        if(id == R.id.checkedText_english){
            checkedText_tamil.setChecked(false);
            checkedText_hindi.setChecked(false);
            Utils.storeNetworkMode("en", LanguageActivity.this);
            lan="en";
        }
        else if (id == R.id.checkedText_tamil){
            checkedText_hindi.setChecked(false);
            checkedText_english.setChecked(false);
            Utils.storeNetworkMode("ta", LanguageActivity.this);
            lan="ta";
        }
        else if(id == R.id.checkedText_hindi){
            checkedText_english.setChecked(false);
            checkedText_tamil.setChecked(false);
            Utils.storeNetworkMode("hi", LanguageActivity.this);
            lan="hi";
        }
        changeLang(lan);
    }

    public void changeLang(String lang)
    {
        if (lang.equalsIgnoreCase(""))
            return;
        myLocale = new Locale(lang);
        Utils.storeNetworkMode(lang,this);
        Locale.setDefault(myLocale);
        android.content.res.Configuration config = new android.content.res.Configuration();
        config.locale = myLocale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
        updateTexts();
    }
    public void updateTexts(){
        choose_lan.setText(R.string.language_title);
    }

    @Override
    public void onConfigurationChanged(android.content.res.Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (myLocale != null){
            newConfig.locale = myLocale;
            Locale.setDefault(myLocale);
            getBaseContext().getResources().updateConfiguration(newConfig, getBaseContext().getResources().getDisplayMetrics());
        }
    }

}
