package com.example.lkm.ms_termproject_001;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.widget.ViewFlipper;

public class MainActivity extends AppCompatActivity {

    ViewFlipper flipper;
    ToggleButton toggle_Flipping;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
    }

    //onClick속성이 지정된 View가 클릭되었을 때 자동으로 호출되는 메소드.
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

}
