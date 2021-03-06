package com.smartcalendar.ilienkovkorovin.kalendar;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.smartcalendar.ilienkovkorovin.kalendar.DialodFragments.ConfirmLocationDialog;
import com.smartcalendar.ilienkovkorovin.kalendar.Utils.ParseXML;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class StartActivity extends AppCompatActivity implements LocationListener, ConfirmLocationDialog.ConfirmLocationDialogListener {

    private Toolbar toolbar;
    private ArrayList<String> allCity;
    private AutoCompleteTextView enterCityTextView;
    private Button nextButton;
    private EditText userNameEditText;

    private static final String SETTINGS = "settings";
    private static final String USER_NAME = "user_name";
    private static final String USER_CITY = "user_city";
    public static final String CITY_NAME_KEY = "city_name_key";


    private SharedPreferences userData;

    private LocationManager locationManager;
    private Location currentLocation;
    private String currentLocality;
    private String userLocation = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        userData = getSharedPreferences(SETTINGS, Context.MODE_PRIVATE);    // пробуем считать настройки
        String cityNameSetting = userData.getString(USER_CITY, null);   // получаем название города если он сохранён, или null если нет
        if (cityNameSetting == null) {    // если null:
            startLocation();              // - определяем местоположение
            initUI();                     // - показываем активность
        } else {
            startCalendarActivity();      // иначе - настройки сохранены - пропускаем это окно
        }
    }

    private void showConfirmCityDialog() {
        Bundle cityName = new Bundle();
        cityName.putString(CITY_NAME_KEY, userLocation);
        DialogFragment confirmCityDialog = new ConfirmLocationDialog();
        confirmCityDialog.setArguments(cityName);
        confirmCityDialog.show(getSupportFragmentManager(), "confirmCityDialog");
    }

    private void startCalendarActivity() {
        Intent intent = new Intent(this, CalendarActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    private void initUI() {
        
        enterCityTextView = findViewById(R.id.autoCompleteTextView);
        nextButton = findViewById(R.id.confirmCityAndNameButton);
        userNameEditText = findViewById(R.id.enterNameEditText);

        if (!userLocation.equals("")) enterCityTextView.setText(userLocation);
        allCity = ParseXML.getParsedXML(getApplicationContext()); // parse xml with city's
        enterCityTextView.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, allCity));
        enterCityTextView.setDropDownHeight(600);

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userCity = enterCityTextView.getText().toString();
                String userName = userNameEditText.getText().toString();

                if (userCity.equals("") || userName.equals("")) {
                    Toast.makeText(getApplicationContext(), R.string.enter_user_name_and_city, Toast.LENGTH_SHORT).show();
                } else {
                    saveUserData(userName, userCity);
                    startCalendarActivity();
                }
            }
        });
    }

    private void saveUserData(String userName, String userCity) {
        userData = getSharedPreferences(SETTINGS, Context.MODE_PRIVATE);
        SharedPreferences.Editor userDataEditor = userData.edit();
        userDataEditor.putString(USER_CITY, userCity);
        userDataEditor.putString(USER_NAME, userName);
        userDataEditor.apply();
    }

    private void startLocation() {
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if (!isGPSEnabled && !isNetworkEnabled) {
        } else {
            if (checkPermission()) {
                if (isNetworkEnabled) {
                    if (locationManager != null) {
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 1000, this);
                        currentLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (currentLocation != null) {
                            userLocation = getApplicationState(currentLocation);
                        }
                    }
                }
            }
        }

    }

    private String getApplicationState(Location location) {
        final Geocoder geocoder = new Geocoder(this);

        List<Address> addressList;
        try {
            addressList = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
        } catch (IOException e) {
            e.printStackTrace();
            return e.getLocalizedMessage();
        }
        if (addressList.isEmpty()) return "LOCATION NOT FOUND";
        Address currentAddress = addressList.get(0);
        currentLocality = currentAddress.getLocality();
        return currentLocality;
    }

    private boolean checkPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION}, 100);
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        boolean permissionsGranted = false;
        if (requestCode == 100) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (grantResults.length > 1 && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    permissionsGranted = true;
                } else {
                    permissionsGranted = false;
                }
            } else {
                permissionsGranted = false;
            }
        }
        if (permissionsGranted) {
            recreate();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        showConfirmCityDialog();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onDialogPositiveButtonClick(DialogFragment dialog) {
        enterCityTextView.setText(userLocation);
    }

    @Override
    public void onDialogNegativeButtonClick(DialogFragment dialog) {

    }
}

