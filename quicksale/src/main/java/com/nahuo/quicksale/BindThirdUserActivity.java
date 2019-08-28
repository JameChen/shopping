package com.nahuo.quicksale;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.baidu.mobstat.StatService;
import com.nahuo.library.controls.LoadingDialog;
import com.nahuo.quicksale.api.AccountAPI;
import com.nahuo.quicksale.common.Const;
import com.nahuo.quicksale.common.Const.LoginFrom;
import com.nahuo.quicksale.common.SpManager;
import com.nahuo.quicksale.oldermodel.BaseAccessToken;
import com.nahuo.quicksale.oldermodel.PublicData;
import com.nahuo.quicksale.provider.QQAccessTokenKeeper;
import com.nahuo.quicksale.provider.WeChatAccessTokenKeeper;
import com.nahuo.quicksale.util.ActivityUtil;

/**
 * @description 绑定第三方用户
 * @created 2015-1-5 下午5:50:40
 * @author ZZB
 */
public class BindThirdUserActivity extends BaseSlideBackActivity implements View.OnClickListener {

    public static final String EXTRA_LOGIN_FROM = "EXTRA_LOGIN_FROM";
    private Context mContext = this;
    private LoadingDialog mLoadingDialog;
    private BaseAccessToken mAccessToken;
    private EditText mEtUserName, mEtPassword;
    private Const.LoginFrom mLoginFrom;

    private static enum Step {
        BIND, LOGIN
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_bind_third_user);
       // getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.layout_titlebar_default);
        initData();
        initView();
    }

    private void initData() {
        mLoginFrom = (LoginFrom) getIntent().getSerializableExtra(EXTRA_LOGIN_FROM);
        
        switch (mLoginFrom) {
        case QQ:
            QQAccessToken qqToken = QQAccessTokenKeeper.readAccessToken(mContext);
            mAccessToken = new BaseAccessToken(Const.TecentOpen.APP_ID, qqToken.getOpenId(), qqToken.getAccessToken());
            break;
        case WECHAT:
            WeChatAccessToken wechatToken = WeChatAccessTokenKeeper.readAccessToken(mContext);
            mAccessToken = new BaseAccessToken(Const.WeChatOpen.APP_ID_1, wechatToken.getOpenId(), wechatToken.getAccessToken());
            break;
        }
    }

    private void initView() {
        initTitleBar();
        mLoadingDialog = new LoadingDialog(this);
        mEtUserName = (EditText) findViewById(R.id.et_user_name);
        mEtPassword = (EditText) findViewById(R.id.et_password);
    }

    private void initTitleBar() {
        TextView tvTitle = (TextView) findViewById(R.id.titlebar_tvTitle);
        tvTitle.setText("关联账号");

        Button btnLeft = (Button) findViewById(R.id.titlebar_btnLeft);
        btnLeft.setText(R.string.titlebar_btnBack);
        btnLeft.setVisibility(View.VISIBLE);
        btnLeft.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.titlebar_btnLeft:
            finish();
            break;
        case R.id.btn_bind:
            String userName = mEtUserName.getText().toString();
            String psw = mEtPassword.getText().toString();
            if(TextUtils.isEmpty(userName)){
                ViewHub.setEditError(mEtUserName, "请输入用户名");
            }else if(TextUtils.isEmpty(psw)){
                ViewHub.setEditError(mEtPassword, "请输入密码");
            }else{
                new Task(Step.BIND).execute();
            }
            break;
        }
    }

    private class Task extends AsyncTask<Object, Void, Object> {

        private Step mStep;

        public Task(Step step) {
            mStep = step;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            switch (mStep) {
            case BIND:
                mLoadingDialog.start("绑定中...");
                break;
            case LOGIN:
                mLoadingDialog.start("登录中...");
                break;

            }
        }

        @Override
        protected Object doInBackground(Object... params) {
            try {
                switch (mStep) {
                case BIND:
                    String bindUserName = mEtUserName.getText().toString().trim();
                    String bindPsw = mEtPassword.getText().toString().trim();

                    AccountAPI.bindThirdUser(mAccessToken.getAppId(), mAccessToken.getOpenId(),
                            mAccessToken.getAccessToken(), bindUserName, bindPsw);
                    break;
                case LOGIN:
                    String loginUserName = mEtUserName.getText().toString().trim();
                    String loginPsw = mEtPassword.getText().toString().trim();
                    AccountAPI.getInstance().userLogin(loginUserName, loginPsw,"","","","");
                    break;

                }
            } catch (Exception e) {
                e.printStackTrace();
                return "error:" + e.getMessage();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object result) {
            super.onPostExecute(result);
            if (mLoadingDialog.isShowing()) {
                mLoadingDialog.stop();
            }
            if (result instanceof String && ((String) result).startsWith("error:")) {
                ViewHub.showLongToast(mContext, ((String) result).replace("error:", ""));
            } else {
                switch (mStep) {
                case BIND:
                    new Task(Step.LOGIN).execute();
                    break;
                case LOGIN:
                    if (!TextUtils.isEmpty(PublicData.getCookie(mContext))) {
                        SpManager.setCookie(mContext, PublicData.getCookie(mContext));
                    }
                    SpManager.setLoginAccount(mContext, mEtUserName.getText().toString());
//                    Intent intent = new Intent(mContext, MainActivity.class);
//                    startActivity(intent);
                    ActivityUtil.goToMainActivity(mContext);
                    break;
                }
            }
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
