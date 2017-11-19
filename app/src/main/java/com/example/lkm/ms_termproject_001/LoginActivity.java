package com.example.lkm.ms_termproject_001;

import android.app.ActionBar;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.kakao.auth.ErrorCode;
import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeResponseCallback;
import com.kakao.usermgmt.response.model.UserProfile;
import com.kakao.util.exception.KakaoException;

public class LoginActivity extends AppCompatActivity {
    SessionCallback callback;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        callback = new SessionCallback();
        Session.getCurrentSession().addCallback(callback);

        findViewById(R.id.login_start_btn).setOnClickListener(login_start_btn);
//        requestMe();
    }

    Button.OnClickListener login_start_btn = new View.OnClickListener() {
        public void onClick(View v) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            finish();
        }
    };

//    public void requestMe() {
//        //유저의 정보를 받아오는 함수
//
//        UserManagement.requestMe(new MeResponseCallback() {
//            @Override
//            public void onFailure(ErrorResult errorResult) {
//            //    Log.e(TAG, "error message=" + errorResult);
////                super.onFailure(errorResult);
//                Toast.makeText(LoginActivity.this, "onFailure", Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onSessionClosed(ErrorResult errorResult) {
//                Toast.makeText(LoginActivity.this, "onSessionClosed", Toast.LENGTH_SHORT).show();
//            //    Log.d(TAG, "onSessionClosed1 =" + errorResult);
//            }
//
//            @Override
//            public void onNotSignedUp() {
//                //카카오톡 회원이 아닐시
//            //    Log.d(TAG, "onNotSignedUp ");
//                Toast.makeText(LoginActivity.this, "onNotSignedUp", Toast.LENGTH_SHORT).show();
//
//            }
//
//            @Override
//            public void onSuccess(UserProfile result) {
//                Toast.makeText(LoginActivity.this, "onSuccess", Toast.LENGTH_SHORT).show();
//          //      Log.e("UserProfile", result.toString());
//          //      Log.e("UserProfile", result.getId() + "");
//            }
//        });
//    }
private class SessionCallback implements ISessionCallback {

    @Override
    public void onSessionOpened() {

        UserManagement.requestMe(new MeResponseCallback() {

            @Override
            public void onFailure(ErrorResult errorResult) {
                String message = "failed to get user info. msg=" + errorResult;

                ErrorCode result = ErrorCode.valueOf(errorResult.getErrorCode());
                if (result == ErrorCode.CLIENT_ERROR_CODE) {
                    //에러로 인한 로그인 실패
//                        finish();
                } else {
                    //redirectMainActivity();
                }
            }

            @Override
            public void onSessionClosed(ErrorResult errorResult) {
            }

            @Override
            public void onNotSignedUp() {

            }

            @Override
            public void onSuccess(UserProfile userProfile) {
                //로그인에 성공하면 로그인한 사용자의 일련번호, 닉네임, 이미지url등을 리턴합니다.
                //사용자 ID는 보안상의 문제로 제공하지 않고 일련번호는 제공합니다.

                Log.e("UserProfile", userProfile.toString());
                Log.e("UserProfile", userProfile.getId() + "");

                long number = userProfile.getId();


            }
        });

    }

    @Override
    public void onSessionOpenFailed(KakaoException exception) {
        // 세션 연결이 실패했을때
        // 어쩔때 실패되는지는 테스트를 안해보았음 ㅜㅜ

    }
}

}

