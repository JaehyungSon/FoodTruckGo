package com.example.lkm.ms_termproject_001;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.widget.ViewFlipper;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeResponseCallback;
import com.kakao.usermgmt.response.model.UserProfile;
import com.navdrawer.SimpleSideDrawer;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.microedition.khronos.opengles.GL;

public class MainActivity extends AppCompatActivity {

    ViewFlipper flipper;
    ToggleButton toggle_Flipping;

    String name = "ERROR";    //카카오와 연동하기위해
    String mail = "ERROR";    //가장 위로 올림
    String profilePhotoURL = "";
    Bitmap bitmap;

    private SimpleSideDrawer mSlidingMenu;

    private ListView mListView;

    final int REQ_CODE_SELECT_IMAGE=100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* 위젯과 멤버변수 참조 획득 */
        mListView = (ListView)findViewById(R.id.listView);

        /* 아이템 추가 및 어댑터 등록 */
        dataSetting();


        mSlidingMenu = new SimpleSideDrawer(this);
        mSlidingMenu.setLeftBehindContentView(R.layout.activiry_left_menu);

        // ------- 이미지 슬라이드 관련 코드 start ------- //
        flipper= (ViewFlipper)findViewById(R.id.flipper);

        for(int i=0;i<3;i++){
            ImageView img= new ImageView(this);
            img.setImageResource(R.drawable.test_img_01+i);
            flipper.addView(img);
        }
        Animation showIn= AnimationUtils.loadAnimation(this, android.R.anim.fade_in);

        flipper.setInAnimation(showIn);
        flipper.setOutAnimation(this, android.R.anim.fade_out);

        flipper.setFlipInterval(3000);
        flipper.startFlipping();

        toggle_Flipping= (ToggleButton)findViewById(R.id.toggle_auto);
        toggle_Flipping.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // TODO Auto-generated method stub
                if(isChecked){
                    flipper.stopFlipping();
                }else{
                    flipper.setFlipInterval(3000);
                    flipper.startFlipping();
                }
            }
        });
        // ------- 이미지 슬라이드 관련 코드 end ------- //

        requestMe();  //카카오 정보 load





    }

    // ------- 이미지 슬라이드 관련 코드 start ------- //
    public void mOnClick(View v){
        switch( v.getId() ){
            case R.id.btn_previous:
                flipper.showPrevious();//이전 View로 교체
                flipper.showPrevious();
                break;
            case R.id.btn_next:
                flipper.showNext();//다음 View로 교체
                break;
        }
    }
    // ------- 이미지 슬라이드 관련 코드 end ------- //

    // ------- 왼쪽 메뉴바 관련 코드 start ------- //
    public void topMenuClick(View v){
        switch( v.getId() ){


            case R.id.main_top_menu_left_btn:
                mSlidingMenu.toggleLeftDrawer();

                boolean setUserProfile = setUserProfile();

                if(setUserProfile){

                }else{
                    Toast.makeText(this, "ERROR", Toast.LENGTH_SHORT).show();
                }

                // -- 왼쪽 메뉴바 버튼 클릭 시 이벤트 start -- //


                ImageButton bookmark = (ImageButton)findViewById(R.id.bookmark_btn); // 즐겨찾기
                ImageButton point_btn = (ImageButton)findViewById(R.id.point_btn); // 적립내역
                ImageButton alert_btn = (ImageButton)findViewById(R.id.alert_btn); // 알림
                ImageButton map_btn = (ImageButton)findViewById(R.id.map_btn); // 구글 맵

                ImageButton adjust_btn = (ImageButton)findViewById(R.id.adjust_btn); // 등록 수정
                // 로그아웃

                bookmark.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        finish();
                        startActivity(new Intent(MainActivity.this, MainActivity.class));
                        startActivity(new Intent(MainActivity.this, BookmarkActivity.class));
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    }
                });
                point_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        finish();
                        startActivity(new Intent(MainActivity.this, MainActivity.class));
                        startActivity(new Intent(MainActivity.this, PointActivity.class));
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    }
                });
                alert_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        finish();
                        startActivity(new Intent(MainActivity.this, MainActivity.class));
                        startActivity(new Intent(MainActivity.this, AlertActivity.class));
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    }
                });
                map_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        finish();
                        startActivity(new Intent(MainActivity.this, MainActivity.class));
                        startActivity(new Intent(MainActivity.this, GoogleMapActivity.class));
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    }
                });


                adjust_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        finish();
                        startActivity(new Intent(MainActivity.this, MainActivity.class));
                        startActivity(new Intent(MainActivity.this, FoodtrcukRegistActivity.class));
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    }
                });



                // -- 왼쪽 메뉴바 버튼 클릭 시 이벤트 end -- //

                break;
            // ------- 왼쪽 메뉴바 관련 코드 end ------- //

            // ------- 필터링 관련 코드 start ------- //
            case R.id.main_top_menu_filter_btn:


                //...//


                break;
            // ------- 필터링 관련 코드 end ------- //
        }
    }

    // ------- 유저 프로필 설정 UI start ------- //
    public boolean setUserProfile(){

        // -- DB로부터 데이터 입력받음 -- //
        ImageView imageview = (ImageView)findViewById(R.id.profile_img);

        int point = 10100;
        if(profilePhotoURL.equals("")){
            imageview.setImageResource(R.drawable.profile_test_01); // 바꾸는 코드
        }else{

            imageview.setImageBitmap(bitmap);
        }

        TextView txt_name = (TextView)findViewById(R.id.profile_name);
        txt_name.setText(name);
        txt_name.setTextColor(Color.BLACK);

        TextView txt_mail = (TextView)findViewById(R.id.profile_mail);
        txt_mail.setText(mail);
        txt_mail.setTextColor(Color.BLACK);

        TextView txt_point = (TextView)findViewById(R.id.profile_point);
        txt_point.setText("포인트 : "+point+" P"); // 1000원 이상시 쉼표 추가 하는 함수 만들 것.  ex) 10000  ->  10,000
        txt_point.setTextColor(Color.BLACK);


        return true;
    }

    // ------- 카카오 유저정보 가져오기 start ------- //
    public void requestMe() {
        //유저의 정보를 받아오는 함수
        UserManagement.requestMe(new MeResponseCallback() {
            @Override
            public void onFailure(ErrorResult errorResult) {
            }

            @Override
            public void onSessionClosed(ErrorResult errorResult) {
            }

            @Override
            public void onNotSignedUp() {
                //카카오톡 회원이 아닐시
                //    Log.d(TAG, "onNotSignedUp ");
            }

            @Override
            public void onSuccess(UserProfile result) {
                GlobalApplication global = (GlobalApplication)getApplicationContext();
                global.uuid = result.getId()+"";

                name = result.getNickname();
                profilePhotoURL = result.getProfileImagePath();

                Thread mThread = new Thread(){
                    @Override
                    public void run(){
                        try{
                            URL url = new URL(profilePhotoURL);
                            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                            conn.connect();
                            InputStream is = conn.getInputStream();
                            bitmap = BitmapFactory.decodeStream(is);

                        }catch (IOException ex){
                        }
                    }
                };
                mThread.start();
                try{
                    mThread.join();
                    //profile_img.setImageBitmap(bitmap);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    // ------- 카카오 유저정보 가져오기 end ------- //

    // ------- 리스트 뷰 start ------- //
    private void dataSetting(){
        FirebaseDatabase fd = FirebaseDatabase.getInstance();
        DatabaseReference myRef = fd.getReference().child("FoodTrucks");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
               // Log.d("asdfasdf", "Value is: " + dataSnapshot);
                String name;
                String simpleExplain;
                String photo;
                for(DataSnapshot child : dataSnapshot.getChildren()){
                    Log.e("key",child.getKey());
                    for(DataSnapshot childchild : child.getChildren()){
                        if(childchild.getKey().equals("name")){
                      //      Log.e("we do",childchild.getValue().toString());
                            name = childchild.getValue().toString();

                        }
                        if(childchild.getKey().equals("name")){
                            simpleExplain = childchild.getValue().toString();
                        }
                        if(childchild.getKey().equals("1")){
                            photo = childchild.getValue().toString();
                        }

                    }
                    Log.e("AAAA",child+"");

                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("asdfasdf", "Failed to read value.", error.toException());
            }
        });

        MyAdapter mMyAdapter = new MyAdapter();

        Bitmap img_1 = null;
        String name_1 = "LeeKangMin";
        String content_1 = "안녕하세여 ㅎㅎㅎㅎㅎ";
        String distance_1 = "100m";

        Bitmap img_2 = null;
        String name_2 = "Son";
        String content_2 = "꺼져 ㅋ";
        String distance_2 = "100m";

        Bitmap img_3 = null;
        String name_3 = "Jung";
        String content_3 = "하하하하하하하하ㅏ핳하하하하하하";
        String distance_3 = "100m";

        // 사진 가능하면 "profile_null" 자리에 추가하면 됨.

        mMyAdapter.addItem(img_1, name_1, content_1, distance_1);
        mMyAdapter.addItem(img_2, name_2, content_2, distance_2);
        mMyAdapter.addItem(img_3, name_3, content_3, distance_3);

        /* 리스트뷰에 어댑터 등록 */
        mListView.setAdapter(mMyAdapter);

    }



    // ------- 리스트 뷰 end ------- //

    // ------- 갤러리 - 이미지 start ------- //
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Toast.makeText(getBaseContext(), "resultCode : "+resultCode,Toast.LENGTH_SHORT).show();
        if(requestCode == REQ_CODE_SELECT_IMAGE)
        {
            if(resultCode== Activity.RESULT_OK)
            {
                try {
                    //Uri에서 이미지 이름을 얻어온다.
                    //String name_Str = getImageNameToUri(data.getData());
                    //이미지 데이터를 비트맵으로 받아온다.
                    Bitmap image_bitmap 	= MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
                    ImageView image = (ImageView)findViewById(R.id.profile_img);
                    //배치해놓은 ImageView에 set
                    image.setImageBitmap(image_bitmap);
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

    public String getImageNameToUri(Uri data)
    {
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(data, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String imgPath = cursor.getString(column_index);
        String imgName = imgPath.substring(imgPath.lastIndexOf("/")+1);
        Toast.makeText(getBaseContext(), "imgPath : "+imgPath +" //  imgName: "+imgName , Toast.LENGTH_SHORT).show();
        return imgName;
    }
    // ------- 갤러리 - 이미지 end ------- //

}
