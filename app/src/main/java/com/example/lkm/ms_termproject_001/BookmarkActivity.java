package com.example.lkm.ms_termproject_001;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class BookmarkActivity extends AppCompatActivity {
    String name = "ERROR";    //카카오와 연동하기위해
    String mail = "ERROR";    //가장 위로 올림
    String profilePhotoURL = "";
    Bitmap bitmap;
    private SimpleSideDrawer mSlidingMenu;
    private ListView mListView;
    ArrayList<Integer> bookmarkArraylist = new ArrayList<>();
    ArrayList<Integer> bookmarkViewlist = new ArrayList<>();
    BookmarkMyAdapter mMyAdapter = new BookmarkMyAdapter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmark);

        mSlidingMenu = new SimpleSideDrawer(this);
        mSlidingMenu.setLeftBehindContentView(R.layout.activiry_left_menu);

        requestMe();  //카카오 정보 load

        mListView=(ListView)findViewById(R.id.bookmarkListView);
        temp();
        localBookmarkGet(); //저장된 리스트 불러오기
        dataSetting();  //리스트뷰에 뿌려주기

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                final CharSequence[] items = {"자세히 보기", "즐겨찾기 삭제 ㅜㅜ"};

                AlertDialog.Builder builder = new AlertDialog.Builder(BookmarkActivity.this);     // 여기서 this는 Activity의 this

                builder.setTitle(mMyAdapter.getItem(position).getName())        // 제목 설정
                        .setItems(items, new DialogInterface.OnClickListener(){    // 목록 클릭시 설정
                            public void onClick(DialogInterface dialog, int index){
                                switch (index){
                                    case 0:
                                        Toast.makeText(BookmarkActivity.this, "자세히보기로 이동", Toast.LENGTH_SHORT).show();
                                        break;
                                    case 1:
                                        localBookmarkRemove(position);
                                        bookmarkArraylist.clear();
                                        bookmarkViewlist.clear();
                                        mMyAdapter.removeAll();
                                        localBookmarkGet(); //저장된 리스트 불러오기
                                        dataSetting();  //리스트뷰에 뿌려주기
                                        break;
                                }
                            }
                        });

                AlertDialog dialog = builder.create();    // 알림창 객체 생성
                dialog.show();    // 알림창 띄우기


            }
        });


    }

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
                        startActivity(new Intent(BookmarkActivity.this, BookmarkActivity.class));
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        finish();
                    }
                });
                point_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(BookmarkActivity.this, PointActivity.class));
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        finish();
                    }
                });
                alert_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(BookmarkActivity.this, AlertActivity.class));
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        finish();
                    }
                });


                map_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(BookmarkActivity.this, GoogleMapActivity.class));
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        finish();
                    }
                });


                adjust_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(BookmarkActivity.this, FoodtrcukRegistActivity.class));
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        finish();
                    }
                });

                // -- 왼쪽 메뉴바 버튼 클릭 시 이벤트 end -- //

                break;
        }
    }
    // ------- 왼쪽 메뉴바 관련 코드 end ------- //

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
    // ------- 유저 프로필 설정 UI end ------- //

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
                global.uuid = result.getUUID();

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



    //파일 저장 코드 (즐겨찾기 목록)
    private void savePreferences(int index,int value){
        SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(String.valueOf(index), String.valueOf(value));
        editor.commit();
    }

    //파일 저장 코드 (즐겨찾기 목록)
    private void temp(){
        SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(String.valueOf(0), String.valueOf(566779173));
        editor.putString(String.valueOf(1), String.valueOf(566781065));
        editor.commit();
    }
    //로컬에 저장되어있는 파일을 가져옴
    private void localBookmarkGet(){
        int i=0;
        while(true){
            if(getPreferences(String.valueOf(i)).equals("")){
                break;
            }else{
                bookmarkArraylist.add(Integer.parseInt(getPreferences(String.valueOf(i))));
            }
            i++;
        }

    }
    //파일에서 값 가져오기 (즐겨찾기 목록)
    private String getPreferences(String key){
        SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
        return pref.getString(key, "");
    }

    private void localBookmarkRemove(int index){
        savePreferences(bookmarkArraylist.indexOf(bookmarkViewlist.get(index)),0);
    }


    // ------- 리스트 뷰 start ------- //

    String name_2="";
    String simpleExplain="기본글";
    String photo="";
    private void dataSetting(){
        FirebaseDatabase fd = FirebaseDatabase.getInstance();
        DatabaseReference myRef = fd.getReference().child("FoodTrucks");


        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Log.d("asdfasdf", "Value is: " + dataSnapshot);

                final Bitmap[] tempBitmap = new Bitmap[1];
                //String photo="";
                for(DataSnapshot child : dataSnapshot.getChildren()){
                    Log.e("key",child.getKey());

                    if(bookmarkArraylist.indexOf(Integer.parseInt(child.getKey()))==-1){

                        continue;
                    }
                    bookmarkViewlist.add(Integer.parseInt(child.getKey()));

                    for(DataSnapshot childchild : child.getChildren()){
                        if(childchild.getKey().equals("name")){
                            //      Log.e("we do",childchild.getValue().toString());
                            name_2 = childchild.getValue().toString();

                        }
                        if(childchild.getKey().equals("simpleExplain")){
                            simpleExplain = childchild.getValue().toString();
                        }
                        if(childchild.getKey().equals("1")){
                            photo = childchild.getValue().toString();

                        }

                    }
                    mMyAdapter.addItem(photo,name_2,simpleExplain,"100m");
                    mMyAdapter.notifyDataSetChanged();

                }

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("asdfasdf", "Failed to read value.", error.toException());
            }
        });


        mListView.setAdapter(mMyAdapter);



        /* 리스트뷰에 어댑터 등록 */


    }
}
