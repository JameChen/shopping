package com.nahuo.quicksale;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.baidu.mobstat.StatService;
import com.nahuo.library.controls.AutoCompleteTextViewEx;
import com.nahuo.library.controls.LoadingDialog;
import com.nahuo.library.helper.FunctionHelper;
import com.nahuo.library.helper.GsonHelper;
import com.nahuo.quicksale.api.AccountAPI;
import com.nahuo.quicksale.api.WeChatAPI;
import com.nahuo.quicksale.common.Const;
import com.nahuo.quicksale.common.SpManager;
import com.nahuo.quicksale.oldermodel.PublicData;
import com.nahuo.quicksale.oldermodel.QQUserInfo;
import com.nahuo.quicksale.oldermodel.WeChatUserInfo;
import com.nahuo.quicksale.provider.QQAccessTokenKeeper;
import com.nahuo.quicksale.provider.WeChatAccessTokenKeeper;
import com.nahuo.quicksale.util.ActivityUtil;
import com.tencent.connect.common.Constants;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.json.JSONObject;

public class LoginFragment extends Fragment implements IWXAPIEventHandler, OnClickListener {

    private static final String TAG = "LoginActivity";
    public static final String FINISH_LOGIN_ACTIVITY = "com.nahuo.bw.b.LoginActivity.finishActivity";
    public static final String PHONENO = "com.nahuo.bw.b.LoginActivity.phoneNo";
    private FragmentActivity mContext;
    private View mContentView;
    // private TextView tvTitle;
    private AutoCompleteTextViewEx edtAccount;
    private EditText edtPassword;
    private Button btnLogin, btnForgotPwd, btnReg;
    private ImageView imgLoginLogo;

    private LoadingDialog loadingDialog;
    private Tencent mTencent;
    private IUiListener mGetQQAccessTokenListener, mGetQQUserInfoListener;

    private IWXAPI mWxAPI;
    private WeChatAccessToken mWeChatToken;
    private QQUserInfo mQQUserInfo;

    private static enum Step {
        LOGIN, WECHAT_LOGIN1, WECHAT_LOGIN2, QQ_LOGIN
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = getActivity();
        mContentView = inflater.inflate(R.layout.activity_login, container, false);
        initView();
        initTencentLogin();
        initWeChatLogin();
        return mContentView;
    }

    /**
     * @description 初始化微信登录
     * @created 2014-12-18 上午11:56:41
     * @author ZZB
     */
    private void initWeChatLogin() {
        if (mWxAPI == null) {
            mWxAPI = WXAPIFactory.createWXAPI(mContext, Const.WeChatOpen.APP_ID_1);
            mWxAPI.registerApp(Const.WeChatOpen.APP_ID_1);
        }
    }

    /**
     * @description 初始化腾讯登录
     * @created 2014-12-18 上午11:29:46
     * @author ZZB
     */
    private void initTencentLogin() {
        mTencent = Tencent.createInstance(Const.TecentOpen.APP_ID, mContext);
        mGetQQAccessTokenListener = new IUiListener() {
            @Override
            public void onError(UiError e) {
                ViewHub.showShortToast(mContext, "onError");
            }

            @Override
            public void onComplete(Object response) {
                if (null == response) {
                    ViewHub.showShortToast(mContext, "登录失败");
                    return;
                }
                JSONObject jsonResponse = (JSONObject) response;
                if (null != jsonResponse && jsonResponse.length() == 0) {
                    ViewHub.showShortToast(mContext, "登录失败");
                    return;
                }
                try {
                    String tokenStr = jsonResponse.getString(Constants.PARAM_ACCESS_TOKEN);
                    long expires = jsonResponse.getLong(Constants.PARAM_EXPIRES_IN);
                    String openId = jsonResponse.getString(Constants.PARAM_OPEN_ID);
                    if (!TextUtils.isEmpty(tokenStr) && !TextUtils.isEmpty(openId)) {
                        mTencent.setAccessToken(tokenStr, expires + "");
                        mTencent.setOpenId(openId);
                        QQAccessToken token = new QQAccessToken(tokenStr, expires, openId);
                        QQAccessTokenKeeper.writeAccessToken(mContext, token);
                        com.tencent.connect.UserInfo userinfo = new com.tencent.connect.UserInfo(mContext,
                                mTencent.getQQToken());
                        userinfo.getUserInfo(mGetQQUserInfoListener);
                    }
                } catch (Exception e) {
                    Log.e(TAG, e.getMessage());
                }
            }

            @Override
            public void onCancel() {
            }
        };
        mGetQQUserInfoListener = new IUiListener() {

            @Override
            public void onError(UiError e) {
            }

            @Override
            public void onComplete(Object response) {
                String json = response.toString();
                try {
                    // QQ登录成功，拿到QQ登录用户信息
                    mQQUserInfo = GsonHelper.jsonToObject(json, QQUserInfo.class);
                } catch (Exception e) {
                    Log.e(TAG, e.getMessage());
                }
            }

            @Override
            public void onCancel() {
            }
        };
    }

    /**
     * 初始化视图
     */
    private void initView() {

        loadingDialog = new LoadingDialog(mContext);
        edtAccount = (AutoCompleteTextViewEx) mContentView.findViewById(R.id.login_edtAccount);
        edtPassword = (EditText) mContentView.findViewById(R.id.login_edtPassword);

//        imgLoginLogo = (ImageView)mContentView.findViewById(R.id.login_imgLogo);
//        imgLoginLogo.setOnLongClickListener(new OnLongClickListener() {
//            // 长按提示一下内部版本号
//            @Override
//            public boolean onLongClick(View v) {
//                ViewHub.showLongToast(getActivity(), com.nahuo.wp.common.Debug.BUILD_VERSION_DATE);
//                return false;
//            }
//        });
        btnLogin = (Button) mContentView.findViewById(R.id.login_btnLogin);
        btnForgotPwd = (Button) mContentView.findViewById(R.id.login_btnForgotPwd);
        btnReg = (Button) mContentView.findViewById(R.id.login_btnReg);

        edtAccount.setOnSearchLogDeleteListener(new AutoCompleteTextViewEx.OnSearchLogDeleteListener() {
            @Override
            public void onSearchLogDeleted(String text) {
                String newChar = SpManager.deleteLoginAccounts(getActivity().getApplicationContext(), text);
                Log.i(getClass().getSimpleName(), "deleteSearchItemHistories:" + newChar);
                edtAccount.populateData(newChar, ",");
                edtAccount.getFilter().filter(edtAccount.getText());
            }
        });


        String username = SpManager.getLoginAccounts(mContext);
        edtAccount.populateData(username, ",");
        // edtAccount.setText(username);

        btnLogin.setOnClickListener(this);
        btnForgotPwd.setOnClickListener(this);
        btnReg.setOnClickListener(this);
        mContentView.findViewById(R.id.btn_qq_login).setOnClickListener(this);
        mContentView.findViewById(R.id.btn_wx_login).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_btnLogin:
                login();
                break;
            case R.id.login_btnReg:
                Intent regIntent = new Intent(mContext, UserRegActivity.class);
                startActivity(regIntent);
                break;
            case R.id.login_btnForgotPwd:
                Intent findPwdIntent = new Intent(mContext, ForgotPwdActivity.class);
                findPwdIntent.putExtra(PHONENO, edtAccount.getText().toString().trim());
                startActivity(findPwdIntent);
                break;
            case R.id.btn_qq_login:// QQ登录
                qqLogin();
                break;
            case R.id.btn_wx_login:// 微信登录
                wechatLogin();
                break;
        }
    }

    /**
     * @description QQ登录
     * @created 2014-12-19 下午6:07:10
     * @author ZZB
     */
    private void qqLogin() {
        QQAccessToken token = QQAccessTokenKeeper.readAccessToken(mContext);
        long expiresIn = token.getExpiresTime() - System.currentTimeMillis();
        mTencent.setAccessToken(token.getAccessToken(), expiresIn + "");
        mTencent.setOpenId(token.getOpenId());
        if (!mTencent.isSessionValid()) {
            mTencent.login(this, "all", mGetQQAccessTokenListener);
        } else {
            mTencent.logout(mContext);
            QQAccessTokenKeeper.clear(mContext);
        }
    }

    /**
     * @description 微信登录
     * @created 2014-12-19 下午5:11:08
     * @author ZZB
     */
    private void wechatLogin() {
        boolean isWxSupport = mWxAPI.isWXAppInstalled();
        if (!isWxSupport) {
            ViewHub.showLongToast(mContext, "您未安装微信或者微信版本过低");
            return;
        }
        mWeChatToken = WeChatAccessTokenKeeper.readAccessToken(mContext);
        if (!mWeChatToken.isSessionValid()) {
            ViewHub.showShortToast(mContext, "无token或者token过期，重新获取");
            SendAuth.Req req = new SendAuth.Req();
            req.scope = "snsapi_userinfo";
            req.state = "state";
            mWxAPI.sendReq(req);
        } else {
            ViewHub.showShortToast(mContext, "已经有token无需获取");
            new Task(Step.WECHAT_LOGIN2).execute();
        }

    }

    /**
     * 登录
     */
    private void login() {
        // 验证用户录入
        if (!validateInput())
            return;
        // 验证网络
        if (!FunctionHelper.CheckNetworkOnline(mContext))
            return;
        // 执行登录操作
        new Task(Step.LOGIN).execute();
    }

    /**
     * 验证用户输入
     */
    private boolean validateInput() {
        String phoneNo = edtAccount.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();
        // 验证手机号
        if (TextUtils.isEmpty(phoneNo)) {
            Toast.makeText(mContext, R.string.login_edtAccount_empty, Toast.LENGTH_SHORT).show();
            edtAccount.requestFocus();
            return false;
        }
        // 验证密码
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(mContext, R.string.login_edtPassword_empty, Toast.LENGTH_SHORT).show();
            edtPassword.requestFocus();
            return false;
        }
        return true;
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
                case LOGIN:
                    loadingDialog.start(getString(R.string.login_doLogin_loading));
                    break;
                case QQ_LOGIN:
                    loadingDialog.start("登录中...");
                    break;
                case WECHAT_LOGIN1:
                case WECHAT_LOGIN2:
                    loadingDialog.start("登录中...");
                    break;
            }

        }

        @Override
        protected Object doInBackground(Object... params) {
            try {
                switch (mStep) {
                    case LOGIN:
                        String phoneNo = edtAccount.getText().toString().trim();
                        String password = edtPassword.getText().toString().trim();
                        AccountAPI.getInstance().userLogin(phoneNo, password,"","","","");

                        break;
                    case QQ_LOGIN:
                        break;
                    case WECHAT_LOGIN1:// 获取access token
                        String code = params[0].toString();
                        mWeChatToken = WeChatAPI.getLoginAccessToken(mContext, code);
                        // 保存access token到本地
                        WeChatAccessTokenKeeper.writeAccessToken(mContext, mWeChatToken);

                    case WECHAT_LOGIN2:// 获取微信用户信息
                        WeChatUserInfo userInfo = WeChatAPI.getUserInfo(mContext, mWeChatToken);
                        return userInfo;
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
            if (loadingDialog.isShowing()) {
                loadingDialog.stop();
            }
            if (result instanceof String && ((String) result).startsWith("error:")) {
                ViewHub.showLongToast(mContext, ((String) result).replace("error:", ""));
            } else {
                switch (mStep) {
                    case LOGIN:
                        if (!TextUtils.isEmpty(PublicData.getCookie(mContext))) {
                            SpManager.setCookie(mContext, PublicData.getCookie(mContext));
                        }
                        SpManager.setLoginAccount(mContext, edtAccount.getText().toString());

//                        Intent intent = new Intent(mContext, MainActivity.class);
//                        startActivity(intent);
                        ActivityUtil.goToMainActivity(mContext);
                        break;
                    case QQ_LOGIN:
                        break;
                    case WECHAT_LOGIN1:
                    case WECHAT_LOGIN2:
                        WeChatUserInfo userinfo = (WeChatUserInfo) result;
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

    @Override
    public void onReq(BaseReq req) {
    }

    @Override
    public void onResp(BaseResp resp) {
        try {
            switch (resp.errCode) {
                case 0:// 用户同意
                    SendAuth.Resp r = (SendAuth.Resp) resp;
                    new Task(Step.WECHAT_LOGIN1).execute(r.code);
                    break;
                case -4:// 用户拒绝授权
                    break;
                case -2:// 用户取消
                    break;
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }

    }
}
