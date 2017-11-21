package com.example.lkm.ms_termproject_001;

import android.*;
import android.app.FragmentManager;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class GoogleMapActivity extends AppCompatActivity implements OnMapReadyCallback
    {
        double longitude=0;  //경도
        double latitude=0;   //위도
        double altitude=0;   //고도
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_google_map);

                            FragmentManager fragmentManager = getFragmentManager();
            MapFragment mapFragment = (MapFragment)fragmentManager
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);





        }

        @Override
        public void onMapReady(final GoogleMap map) {


//아래부분은 지피에스

            ActivityCompat.requestPermissions(GoogleMapActivity.this,new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},1);





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








            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    LatLng SEOUL = new LatLng(latitude, longitude);

                    MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.position(SEOUL);
                    markerOptions.title("서울");
                    markerOptions.snippet("한국의 수도");
                    map.addMarker(markerOptions);

                    map.moveCamera(CameraUpdateFactory.newLatLng(SEOUL));
                    map.animateCamera(CameraUpdateFactory.zoomTo(13));
                }
            }, 3000);


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
                //tv.setText("위도 : " + longitude + "\n경도 : " + latitude);
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

}
