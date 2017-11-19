package com.example.lkm.ms_termproject_001;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.widget.ViewFlipper;

import com.navdrawer.SimpleSideDrawer;

public class MainActivity extends AppCompatActivity {

    ViewFlipper flipper;
    ToggleButton toggle_Flipping;

    private SimpleSideDrawer mSlidingMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // ------- 이미지 슬라이드 관련 코드 start ------- //
        mSlidingMenu = new SimpleSideDrawer(this);
        mSlidingMenu.setLeftBehindContentView(R.layout.activiry_left_menu);

        flipper= (ViewFlipper)findViewById(R.id.flipper);

        for(int i=0;i<3;i++){
            ImageView img= new ImageView(this);
            img.setImageResource(R.drawable.test_img_01+i);
            flipper.addView(img);
        }
        Animation showIn= AnimationUtils.loadAnimation(this, android.R.anim.slide_in_left);

        flipper.setInAnimation(showIn);
        flipper.setOutAnimation(this, android.R.anim.slide_out_right);

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

    }


    public void testClick(View v){
        startActivity(new Intent(MainActivity.this, TestActivity.class));
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        finish();
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

    public void topMenuClick(View v){
        switch( v.getId() ){
            // ------- 왼쪽 메뉴바 관련 코드 start ------- //
            case R.id.main_top_menu_left_btn:
                mSlidingMenu.toggleLeftDrawer();

                boolean setUserProfile = setUserProfile();

                if(setUserProfile){

                }else{
                    Toast.makeText(this, "ERROR", Toast.LENGTH_SHORT).show();
                }








                //Button btn_left = (Button)findViewById(R.id.test_btn);
                //btn_left.setOnClickListener(new View.OnClickListener() {
                    //@Override
                    //public void onClick(View view) {
                    //    Toast.makeText(MainActivity.this, "test ok", Toast.LENGTH_SHORT).show();
                    //}
                //});

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
        String name = "이강민";
        String mail = "hopefuler@naver.com";
        int point = 10100;

        imageview.setImageResource(R.drawable.profile_test_01); // 바꾸는 코드

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
}
