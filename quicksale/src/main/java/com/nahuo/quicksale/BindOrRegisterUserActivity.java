package com.nahuo.quicksale;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.mobstat.StatService;
import com.nahuo.quicksale.common.Const;
import com.nahuo.quicksale.common.Const.LoginFrom;
import com.nahuo.quicksale.provider.UserInfoProvider;
import com.squareup.picasso.Picasso;

/**
 * @description 第三方绑定或者注册用户
 * @created 2015-1-5 下午4:09:33
 * @author ZZB
 */
public class BindOrRegisterUserActivity extends BaseSlideBackActivity implements View.OnClickListener {

    public static final String EXTRA_LOGIN_FROM = "EXTRA_LOGIN_FROM";
    public static final String EXTRA_ICON_URL = "EXTRA_ICON_URL";
    public static final String EXTRA_USER_NAME = "EXTRA_USER_NAME";
    private Context mContext = this;
    private Const.LoginFrom mLoginFrom;
    private String mUserName, mIconUrl;
    private ImageView mIvIcon;
    private TextView mTvGreeting, mTvUserName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_bind_or_register_user);
       // getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.layout_titlebar_default);
        initExtras();
        initView();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        UserInfoProvider.clearAllUserInfo(this);
    }
    private void initExtras() {
        Intent intent = getIntent();
        mLoginFrom = (LoginFrom) intent.getExtras().get(EXTRA_LOGIN_FROM);
        mUserName = intent.getStringExtra(EXTRA_USER_NAME);
        mIconUrl = intent.getStringExtra(EXTRA_ICON_URL);
    }

    private void initView() {
        initTitleBar();
    }

    private void initTitleBar() {
        TextView tvTitle = (TextView) findViewById(R.id.titlebar_tvTitle);
        tvTitle.setText("登录");

        Button btnLeft = (Button) findViewById(R.id.titlebar_btnLeft);
        btnLeft.setText(R.string.titlebar_btnBack);
        btnLeft.setVisibility(View.VISIBLE);
        btnLeft.setOnClickListener(this);
        
        mTvGreeting = (TextView) findViewById(R.id.tv_greeting);
        mTvUserName = (TextView) findViewById(R.id.tv_user_name);
        mIvIcon = (ImageView) findViewById(R.id.iv_icon);
        switch (mLoginFrom) {
        case QQ:
            mTvGreeting.setText("亲爱的QQ用户：");
            break;
        case WECHAT:
            mTvGreeting.setText("亲爱的微信用户：");
            break;
        }
        mTvUserName.setText(mUserName);
        Picasso.with(mContext).load(mIconUrl).into(mIvIcon);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.titlebar_btnLeft:
            UserInfoProvider.clearAllUserInfo(this);
            finish();
            break;
        case R.id.btn_bind:// 绑定
            Intent bindIntent = new Intent(mContext, BindThirdUserActivity.class);
            bindIntent.putExtra(BindThirdUserActivity.EXTRA_LOGIN_FROM, mLoginFrom);
            startActivity(bindIntent);
            break;
        case R.id.btn_register:// 注册
            Intent regIntent = new Intent(mContext, UserRegActivity.class);
            startActivity(regIntent);
            break;
        }
    }

    

    @Override
    public void onPause() {
        super.onPause();
        StatService.onPause(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        StatService.onResume(this);
    }
}
