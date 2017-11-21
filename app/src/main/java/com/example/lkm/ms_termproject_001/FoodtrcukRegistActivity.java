package com.example.lkm.ms_termproject_001;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class FoodtrcukRegistActivity extends AppCompatActivity {
    EditText foodTruckName,FoodtruckSimpleExplain,FoodtruckExplain;
    FirebaseDatabase fd;
    DatabaseReference Ref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foodtrcuk_regist);

        foodTruckName = (EditText)findViewById(R.id.FoodtruckName);
        FoodtruckSimpleExplain = (EditText)findViewById(R.id.FoodtruckSimpleExplain);
        FoodtruckExplain= (EditText)findViewById(R.id.FoodtruckExplain);


    }
    public void SellStartClick(View v){
        fd = FirebaseDatabase.getInstance();
        Ref = fd.getReference();
        Ref = Ref.child("FoodTrucks");

        String key = Ref.getKey();



        HashMap<String,String> data = new HashMap<String,String>();
        data.put("name",foodTruckName.getText().toString());
        data.put("simpleExplain",FoodtruckSimpleExplain.getText().toString());
        data.put("explain",FoodtruckExplain.getText().toString());

        HashMap<String,Object> child = new HashMap<String,Object>();
        child.put(key,data);


        Ref.updateChildren(child);
    }
}

