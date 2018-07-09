package com.smartcalendar.ilienkovkorovin.kalendar;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.hussain_chachuliya.customsearch.SearchAdapterHolder;
import com.smartcalendar.ilienkovkorovin.kalendar.Utils.ParseXML;

import java.util.ArrayList;

public class StartActivity extends AppCompatActivity {

    Toolbar toolbar;
    ArrayList<String> allCity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        InitUI();
    }

    private void InitUI() {
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.hair_time);
        setSupportActionBar(toolbar);
        allCity = ParseXML.getParsedXML(getApplicationContext());
    }
}
