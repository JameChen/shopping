package com.nahuo.quicksale.orderdetail;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.nahuo.quicksale.BaseActivity2;
import com.nahuo.quicksale.R;
import com.nahuo.quicksale.util.ChatUtl;

public class BaseOrderActivity extends BaseActivity2 {

    public Button btnWeiXun;

    void initView() {
        btnWeiXun = (Button) findViewById(R.id.btn_weixun);
        btnWeiXun.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                toWeiXun(0, null);
            }
        });
    }

    void viewBindData() {
//        if (btnWeiXun != null && btnWeiXun.getVisibility() != View.VISIBLE) {
//            btnWeiXun.setVisibility(View.VISIBLE);
//        }
    }

    void toWeiXun(int userID, String nickName) {
//        Intent intent = new Intent(this, ChatActivity.class);
//        intent.putExtra("userId", SpManager.getECC_USER_ID(this));
//        intent.putExtra("nick", SpManager.getECC_USER_NICK_NAME(this));
////        intent.putExtra("userId", "861619");
////        intent.putExtra("nick", "售后小格调");
//        Log.e("name id", SpManager.getECC_USER_NAME(this) + " --" + SpManager.getECC_USER_NAME(this) + "--" + SpManager.getECC_USER_ID(this));
//        startActivity(intent);
        ChatUtl.goToChatActivity(this);
    }
}
