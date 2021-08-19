package com.cops.challengers.view;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.WindowManager;


import com.cops.challengers.R;
import com.cops.challengers.utils.ContextWrapper;
import com.cops.challengers.view.fragments.BaseFragment;
import java.util.Locale;


public class MainActivity extends AppCompatActivity {


    public BaseFragment baseFragment;

    public void superBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onBackPressed() {
        baseFragment.onBack();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);


    }

    @Override
    protected void attachBaseContext(Context newBase) {

        //setting the language from ContextWrapper configuration to rape new locale to context

        SharedPreferences sharedPreferences = newBase.getSharedPreferences(
                "challengers", newBase.MODE_PRIVATE);
        Locale newLocale= Locale.getDefault();

        if (sharedPreferences.getString("appLang", null) !=null) {

            String lang=sharedPreferences.getString("appLang","en");
            newLocale = new Locale(lang);
        }


        Context context = ContextWrapper.wrap(newBase, newLocale);
        super.attachBaseContext(context);
    }

}
