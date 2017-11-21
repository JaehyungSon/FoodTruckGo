package com.example.lkm.ms_termproject_001;

import android.content.Context;
import android.location.LocationManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class FoodtrcukRegistActivity extends AppCompatActivity {
    EditText foodTruckName,FoodtruckSimpleExplain,FoodtruckExplain;
    FirebaseDatabase fd;    //데이터베이스
    DatabaseReference Ref;
    TextView tv;
    ToggleButton tb;



    LocationManager locationManager;    //경도 위도
    double longitudeBest, latitudeBest;
    double longitudeGPS, latitudeGPS;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foodtrcuk_regist);

        foodTruckName = (EditText)findViewById(R.id.FoodtruckName);
        FoodtruckSimpleExplain = (EditText)findViewById(R.id.FoodtruckSimpleExplain);
        FoodtruckExplain= (EditText)findViewById(R.id.FoodtruckExplain);

        locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);  //경도 위도 가져오기

    }
    public void SellStartClick(View v){
        fd = FirebaseDatabase.getInstance();
        Ref = fd.getReference();
        Ref = Ref.child("FoodTrucks");

        GlobalApplication global = (GlobalApplication)getApplicationContext();

        String key = "AAA";



        HashMap<String,String> data = new HashMap<String,String>();
        data.put("name",foodTruckName.getText().toString());
        data.put("simpleExplain",FoodtruckSimpleExplain.getText().toString());
        data.put("explain",FoodtruckExplain.getText().toString());
        data.put("openFlag","0");

        HashMap<String,Object> child = new HashMap<String,Object>();
        child.put(key,data);


        Ref.updateChildren(child);
    }




}

