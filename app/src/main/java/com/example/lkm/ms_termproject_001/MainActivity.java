package com.example.lkm.ms_termproject_001;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
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
}
