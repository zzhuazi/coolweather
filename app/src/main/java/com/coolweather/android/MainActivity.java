package com.coolweather.android;

import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.text.UnicodeSetSpanner;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.coolweather.android.util.LBS;

public class MainActivity extends AppCompatActivity implements LBS.LocationCallBack{
    private static final String TAG = "MainActivity";
    private LBS lbs;
    private String city;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initLocation();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        if (preferences.getString("weather",null) != null){ //已经请求过数据
            Intent intent= new Intent(this, WeatherActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private void initLocation(){
        final TextView locationText = (TextView)findViewById(R.id.location_text);
        Button locationButton = (Button)findViewById(R.id.location_button);
        startLocation();
        if (city != null){   //定位城市不为空
            locationText.setText("当前城市：" + city);
            SharedPreferences.Editor editor= PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit();
            editor.putString("LocationCity", city);
            editor.apply();
            locationText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getApplicationContext(),"当前城市:" + city,Toast.LENGTH_SHORT).show();
                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                    if (preferences.getString("LocationCity",null) != null){ //有城市信息
                        Intent intent= new Intent(MainActivity.this, WeatherActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            });
        }else{
            locationText.setText("未获取定位");
        }
        locationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "获取定位", Toast.LENGTH_SHORT).show();
                locationText.setText("当前定位：" + city);
                startLocation();
                initLocation();
            }
        });
    }

    public void startLocation(){
        lbs = LBS.getInstance(getApplicationContext());
        lbs.setCallBack(this);
        lbs.start();
    }

    @Override
    public void callBack(String city) {
        this.city = city;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        lbs.stop();
    }
}
