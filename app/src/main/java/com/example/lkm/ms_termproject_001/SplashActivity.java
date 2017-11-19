package com.example.lkm.ms_termproject_001;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(new SplashView(this));

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
            }
        }, 4500);
    }

    class SplashView extends View {
        int width, height;                                    // 화면의 폭과 높이
        int x, y;                                              // 캐릭터의 현재 좌표
        int dx, dy;                                            // 캐릭터가 이동할 방향과 거리
        int cw, ch;                                            // 캐릭터의 폭과 높이
        int counter=0;                                         // 루프 카운터
        Bitmap character[] = new Bitmap[2];
        Bitmap bg_img[] = new Bitmap[1]; // 캐릭터의 비트맵 이미지

        public SplashView(Context context) {
            super(context);

            Display display = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE))
                    .getDefaultDisplay();
            width = display.getWidth();           // 화면의 가로폭
            height = display.getHeight();         // 화면의 세로폭
            x = 0;                               // 캐릭터의 현재 x위치
            y = (height/2)-180;                               // 캐릭터의 현재 y위치
            dx = 10;                               // 캐릭터가 x축으로 이동할 거리
            dy = 10;                               // 캐릭터가 y축으로 이동할 거리

            // 캐릭터의 비트맵 읽기
            character[0] = BitmapFactory.decodeResource(getResources(), R.drawable.logo_img);
            character[1] = BitmapFactory.decodeResource(getResources(), R.drawable.logo_img);
            bg_img[0] = BitmapFactory.decodeResource(getResources(), R.drawable.splash_bg);
            character[0] = Bitmap.createScaledBitmap(character[0],width/3+50, (height/5)-50, true);
            character[1] = Bitmap.createScaledBitmap(character[1],width/3+50, (height/5)-50, true);
            bg_img[0] = Bitmap.createScaledBitmap(bg_img[0],width, height, true);

            cw = character[0].getWidth() / 2;          // 캐릭터의 폭/2
            ch = character[0].getHeight() / 2;          // 캐릭터의 높이/2

            mHandler.sendEmptyMessageDelayed(0, 10);
        }

        //-----------------------------------
        //       실제 그림을 그리는 부분
        //-----------------------------------

        public void onDraw(Canvas canvas) {
            x += dx;                                               // 가로 방향으로 이동
            counter++;
            int n = counter %2; // 바퀴 굴리는 부분
            canvas.drawBitmap(bg_img[0],0,0, null);
            canvas.drawBitmap(character[n], x - cw, y - ch, null);
        } // onDraw 끝

        //------------------------------------
        //      Timer Handler
        //------------------------------------

        Handler mHandler = new Handler() {              // 타이머로 사용할 Handler
            public void handleMessage(Message msg) {
                invalidate();                              // onDraw() 다시 실행
                mHandler.sendEmptyMessageDelayed(0, 4);
            }
        }; // Handler
    } // SplashView 끝
}
