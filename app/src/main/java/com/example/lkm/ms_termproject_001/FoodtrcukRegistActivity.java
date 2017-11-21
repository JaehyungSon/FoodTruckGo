package com.example.lkm.ms_termproject_001;

import android.*;
import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;

public class FoodtrcukRegistActivity extends AppCompatActivity {
    EditText foodTruckName,FoodtruckSimpleExplain,FoodtruckExplain;
    FirebaseDatabase fd;    //데이터베이스
    DatabaseReference Ref;
    ImageButton profileImg01,profileImg02,profileImg03;
    TextView tv;
    final int REQ_CODE_SELECT_IMAGE=100;
    int imgFlag=0;
    Uri TruckImg1,TruckImg2,TruckImg3;

    //  ToggleButton tb;

    double longitude=0;  //경도
    double latitude=0;   //위도
    double altitude=0;   //고도

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foodtrcuk_regist);

        foodTruckName = (EditText)findViewById(R.id.FoodtruckName);
        FoodtruckSimpleExplain = (EditText)findViewById(R.id.FoodtruckSimpleExplain);
        FoodtruckExplain= (EditText)findViewById(R.id.FoodtruckExplain);
        profileImg01 =(ImageButton)findViewById(R.id.profileImg01);
        profileImg02 =(ImageButton)findViewById(R.id.profileImg02);
        profileImg03 =(ImageButton)findViewById(R.id.profileImg03);
        tv = (TextView) findViewById(R.id.textView2); //위도경도 표시

        profileImg01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgFlag=1;
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
                intent.setData(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, REQ_CODE_SELECT_IMAGE);
            }
        });
        profileImg02.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgFlag=2;
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
                intent.setData(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, REQ_CODE_SELECT_IMAGE);
            }
        });
        profileImg03.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgFlag=3;
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
                intent.setData(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, REQ_CODE_SELECT_IMAGE);
            }
        });


        //사용자에게 권한 물어봄
        ActivityCompat.requestPermissions(FoodtrcukRegistActivity.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);





        final LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);


        try{

            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, // 등록할 위치제공자
                    100, // 통지사이의 최소 시간간격 (miliSecond)
                    1, // 통지사이의 최소 변경거리 (m)
                    mLocationListener);
            lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, // 등록할 위치제공자
                    100, // 통지사이의 최소 시간간격 (miliSecond)
                    1, // 통지사이의 최소 변경거리 (m)
                    mLocationListener);
    //                lm.removeUpdates(mLocationListener);  //  미수신할때는 반드시 자원해체를 해주어야 한다.

        }catch(SecurityException ex){
        }




    }
    public void SellStartClick(View v){
        fd = FirebaseDatabase.getInstance();
        Ref = fd.getReference();
        Ref = Ref.child("FoodTrucks");

        GlobalApplication global = (GlobalApplication)getApplicationContext();

        FirebaseStorage fs = FirebaseStorage.getInstance();
        StorageReference storageRef = fs.getReference();


        StorageReference riversRef = storageRef.child("images/0.jpg");
        riversRef.putFile(TruckImg1)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Uri downloadUrl = taskSnapshot.getDownloadUrl();
                        Log.e("URL",downloadUrl+"");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        // ...
                    }
                });




        String key = "AAA";

        HashMap<String,String> data = new HashMap<String,String>();
        data.put("name",foodTruckName.getText().toString());
        data.put("simpleExplain",FoodtruckSimpleExplain.getText().toString());
        data.put("explain",FoodtruckExplain.getText().toString());
        data.put("openFlag","0");
        data.put("경도",longitude+"");
        data.put("위도",latitude+"");
        data.put("고도",altitude+"");

        HashMap<String,Object> child = new HashMap<String,Object>();
        child.put(key,data);


        Ref.updateChildren(child);







    }
    private final LocationListener mLocationListener = new LocationListener() {
        public void onLocationChanged(Location location) {
            //여기서 위치값이 갱신되면 이벤트가 발생한다.
            //값은 Location 형태로 리턴되며 좌표 출력 방법은 다음과 같다.

            Log.d("test", "onLocationChanged, location:" + location);
            longitude = location.getLongitude(); //경도
            latitude = location.getLatitude();   //위도
            altitude = location.getAltitude();   //고도
            float accuracy = location.getAccuracy();    //정확도
            String provider = location.getProvider();   //위치제공자



            //Gps 위치제공자에 의한 위치변화. 오차범위가 좁다.
            //Network 위치제공자에 의한 위치변화
            //Network 위치는 Gps에 비해 정확도가 많이 떨어진다.
            tv.setText("위도 : " + longitude + "\n경도 : " + latitude);
        }
        public void onProviderDisabled(String provider) {
            // Disabled시
            Log.d("test", "onProviderDisabled, provider:" + provider);
        }

        public void onProviderEnabled(String provider) {
            // Enabled시
            Log.d("test", "onProviderEnabled, provider:" + provider);
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
            // 변경시
            Log.d("test", "onStatusChanged, provider:" + provider + ", status:" + status + " ,Bundle:" + extras);
        }
    };


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Toast.makeText(getBaseContext(), "resultCode : "+resultCode,Toast.LENGTH_SHORT).show();
        if(requestCode == REQ_CODE_SELECT_IMAGE)
        {
            if(resultCode== Activity.RESULT_OK)
            {
                try {

                    Bitmap image_bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
                    ImageButton image;
                    //Uri에서 이미지 이름을 얻어온다.
                    if(imgFlag==1){
                        TruckImg1 = data.getData();
                        image = (ImageButton)findViewById(R.id.profileImg01);
                    }else if(imgFlag==2){
                        TruckImg2=data.getData();
                         image = (ImageButton)findViewById(R.id.profileImg02);
                    }else{
                        TruckImg3=data.getData();
                         image = (ImageButton)findViewById(R.id.profileImg03);
                    }
                    image.setImageBitmap(image_bitmap);
                    //String name_Str = getImageNameToUri(data.getData());
                    //이미지 데이터를 비트맵으로 받아온다.
                 //   Bitmap image_bitmap 	= MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
                 //   ImageView image = (ImageView)findViewById(R.id.profile_img);
                    //배치해놓은 ImageView에 set
                  //  image.setImageBitmap(image_bitmap);
                    //Toast.makeText(getBaseContext(), "name_Str : "+name_Str , Toast.LENGTH_SHORT).show();
                } catch (FileNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }
    }
}

