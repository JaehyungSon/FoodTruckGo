package com.example.lkm.ms_termproject_001;

import android.*;
import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class DetailFoodtruckActivity extends AppCompatActivity {


    double longitude = 0;  //경도
    double latitude = 0;   //위도
    double altitude = 0;   //고도
    String foodTruckId = "";

    TextView foodTruckName;
    TextView foodTruckDetailExample;
    ImageView detailFoodTruckPhoto;
    String photo = "";
    Bitmap bitmap;
    String phoneNumber;

    ImageView detailFoodTruckCallBtn,DetailBookmark;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_foodtruck);

        Intent intent = getIntent();
        longitude = Double.parseDouble(intent.getExtras().getString("longitude"));
        latitude = Double.parseDouble(intent.getExtras().getString("latitude"));
        altitude = Double.parseDouble(intent.getExtras().getString("altitude"));
        foodTruckId = intent.getExtras().getString("foodTruckId");

        foodTruckName = (TextView) findViewById(R.id.foodTruckName);
        foodTruckDetailExample = (TextView) findViewById(R.id.detailExplain);
        detailFoodTruckPhoto = (ImageView) findViewById(R.id.detailFoodTruckPhoto);
        detailFoodTruckCallBtn = (ImageView) findViewById(R.id.detailFoodTruckCallBtn);
        DetailBookmark = (ImageView)findViewById(R.id.DetailBookmark);

        Toast.makeText(this, foodTruckId + " " + longitude, Toast.LENGTH_SHORT).show();

        //파이어베이스 정보 가져오는 부분
        FirebaseDatabase fd = FirebaseDatabase.getInstance();
        DatabaseReference myRef = fd.getReference().child("FoodTrucks").child(foodTruckId);


        //푸드트럭 정보 가져오는부분
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    if (child.getKey().equals("explain")) {
                        //      Log.e("we do",childchild.getValue().toString());
                        foodTruckDetailExample.setText(child.getValue().toString());
                    }
                    if (child.getKey().equals("1")) {
                        //이미지 삽입
                        ImageLoader imageLoader = ImageLoader.getInstance();
                        DisplayImageOptions options = new DisplayImageOptions.Builder()
                                .showImageOnLoading(R.drawable.logo_img)
                                .showImageForEmptyUri(R.drawable.logo_img)
                                .showImageOnFail(R.drawable.logo_img)
                                .cacheInMemory(true)
                                .cacheOnDisk(true)
                                .considerExifParams(true)
                                .build();

                        ImageLoader.getInstance().displayImage(child.getValue().toString(), detailFoodTruckPhoto, options); //이미지 불러오는과정


                    }
                    if (child.getKey().equals("name")) {
                        foodTruckName.setText(child.getValue().toString());
                    }
                    if (child.getKey().equals("phoneNumber")) {
                        phoneNumber = child.getValue().toString();
                    }

                }


            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("asdfasdf", "Failed to read value.", error.toException());
            }
        });

        detailFoodTruckCallBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityCompat.requestPermissions(DetailFoodtruckActivity.this, new String[]{Manifest.permission.CALL_PHONE}, 1);


                if (ActivityCompat.checkSelfPermission(DetailFoodtruckActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNumber)));
            }
        });
        DetailBookmark.setOnClickListener(new View.OnClickListener() {
            int count;
            @Override
            public void onClick(View v) {
                if(getPreferences("count")!=""){
                    count=0;
                }
               count=Integer.parseInt( getPreferences("count"));
               count++;
               savePreferences("count",count);
               savePreferences(count,Integer.parseInt(foodTruckId));
                Toast.makeText(DetailFoodtruckActivity.this, "즐겨찾기에 추가되었습니다.", Toast.LENGTH_SHORT).show();
            }
        });


    }

    //파일 저장 코드 (즐겨찾기 목록)
    private void savePreferences(int index,int value){
        SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(String.valueOf(index), String.valueOf(value));
        editor.commit();
    }
    //파일 저장 코드 (즐겨찾기 목록)
    private void savePreferences(String index,int value){
        SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(index, String.valueOf(value));
        editor.commit();
    }
    //파일에서 값 가져오기 (즐겨찾기 목록)
    private String getPreferences(String key){
        SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
        return pref.getString(key, "");
    }
}
