package com.smartcalendar.ilienkovkorovin.kalendar;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.hussain_chachuliya.customsearch.SearchAdapterHolder;
import com.smartcalendar.ilienkovkorovin.kalendar.Utils.ParseXML;

import java.util.ArrayList;

public class StartActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ArrayList<String> allCity;
    private AutoCompleteTextView enterCityTextView;
    private Button nextButton;
    private EditText userNameEditTrext;

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

        enterCityTextView = findViewById(R.id.autoCompleteTextView);
        nextButton = findViewById(R.id.confirmCityAndNameButton);
        userNameEditTrext = findViewById(R.id.enterNameEditText);

        allCity = ParseXML.getParsedXML(getApplicationContext()); // parse xml with city's
        enterCityTextView.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, allCity));
        enterCityTextView.setDropDownHeight(600);

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (enterCityTextView.getText().toString().equals("") ||
                        userNameEditTrext.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), R.string.enter_user_name_and_city, Toast.LENGTH_SHORT).show();
                } else {
                    // сохраняем в SharedPreferences и переходим на новую активность
                }
            }
        });
    }
}
