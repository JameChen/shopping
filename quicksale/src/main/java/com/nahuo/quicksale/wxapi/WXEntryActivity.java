package com.nahuo.quicksale.wxapi;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mobstat.StatService;
import com.luck.picture.lib.tools.ScreenUtils;
import com.nahuo.bean.InvitatioCodeBean;
import com.nahuo.bean.LoginBean;
import com.nahuo.library.controls.AutoCompleteTextViewEx;
import com.nahuo.library.controls.LoadingDialog;
import com.nahuo.library.helper.FunctionHelper;
import com.nahuo.library.helper.GsonHelper;
import com.nahuo.library.helper.MD5Utils;
import com.nahuo.quicksale.BindOrRegisterUserActivity;
import com.nahuo.quicksale.ForgotPwdActivity;
import com.nahuo.quicksale.QQAccessToken;
import com.nahuo.quicksale.R;
import com.nahuo.quicksale.SignUpActivity;
import com.nahuo.quicksale.ViewHub;
import com.nahuo.quicksale.WeChatAccessToken;
import com.nahuo.quicksale.api.AccountAPI;
import com.nahuo.quicksale.api.HttpUtils;
import com.nahuo.quicksale.api.WeChatAPI;
import com.nahuo.quicksale.app.BWApplication;
import com.nahuo.quicksale.base.BaseAppCompatActivity;
import com.nahuo.quicksale.common.BaiduStats;
import com.nahuo.quicksale.common.Const;
import com.nahuo.quicksale.common.Debug;
import com.nahuo.quicksale.common.DialogUtils;
import com.nahuo.quicksale.common.SpManager;
import com.nahuo.quicksale.common.Utils;
import com.nahuo.quicksale.di.module.HttpManager;
import com.nahuo.quicksale.eventbus.BusEvent;
import com.nahuo.quicksale.eventbus.EventBusId;
import com.nahuo.quicksale.model.http.CommonSubscriber;
import com.nahuo.quicksale.model.http.exception.ApiException;
import com.nahuo.quicksale.model.http.response.PinHuoResponse;
import com.nahuo.quicksale.oldermodel.PublicData;
import com.nahuo.quicksale.oldermodel.QQUserInfo;
import com.nahuo.quicksale.oldermodel.WeChatUserInfo;
import com.nahuo.quicksale.provider.QQAccessTokenKeeper;
import com.nahuo.quicksale.provider.WeChatAccessTokenKeeper;
import com.nahuo.quicksale.util.RxUtil;
import com.tencent.connect.common.Constants;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelbiz.WXLaunchMiniProgram;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import com.umeng.analytics.AnalyticsConfig;

import org.json.JSONObject;

import de.greenrobot.event.EventBus;

import static com.nahuo.constant.Constant.LoginRegisterFrom.LOGIN_REGISTER_FROM_ANDROID;

/**
 * @author ZZB
 * @description 微信回调
 * @created 2014-11-24 下午4:32:08
 */
public class WXEntryActivity extends BaseAppCompatActivity implements IWXAPIEventHandler, View.OnClickListener {
    public static final String EXTRA_TYPE = "EXTRA_TYPE";
    public static final String EXTRA_LOGIN_TYPE = "EXTRA_LOGIN_TYPE";
    public static final String EXTRA_LOGIN_LIVE = "EXTRA_LOGIN_LIVE";
    private boolean isLive=false;
    public static final int LOGIN_TYPE_RELOGIN = 1;
    private int login_Type = 0;
    private static boolean IS_LOGIN_FROM_WECHAT = false;
    private static final String TAG = WXEntryActivity.class.getSimpleName();
    public static final String PHONENO = "com.nahuo.bw.b.LoginActivity.phoneNo";
    private Context mContext = this;
    private AutoCompleteTextViewEx edtAccount;
    private EditText edtPassword, login_edtSms,et_invitation_code;
    private Button btnLogin, btnForgotPwd, btnReg;

    private LoadingDialog loadingDialog;
    private Tencent mTencent;
    private IUiListener mGetQQAccessTokenListener, mGetQQUserInfoListener;

    private IWXAPI mWxAPI;
    private WeChatAccessToken mWeChatToken;
    private QQUserInfo mQQUserInfo;
    private Const.LoginFrom mLoginFrom;
    public WeChatUserInfo mWechatUserInfo;
    private ScrollView sv;
    private Button login_btnSmSLogin, btn_sms_key;
    private View layout_password_login, layout_sms_login;
    private TextView tv_speech_verification;
    private int smstype = 1;
    private View tv_cancel,layout_invitation_code;
    private View rl_top;
    private ImageView img_see_pwd;
    private enum Step {
        LOGIN, WECHAT_LOGIN1, WECHAT_LOGIN2, QQ_LOGIN, THIRD_LOGIN, CHECK_BIND_STATUS,
        /**
         * 获取验证码
         */
        GET_VERIFY_CODE, LOGIN_VERIFY_CODE
    }

    public static final int TYPE_PASSWORD_LOGIN = 1;
    public static final int TYPE_SMS_LOGIN = 2;

    public static enum Type {
        /**
         * 登录
         */
        LOGIN
    }

    int type = TYPE_SMS_LOGIN;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_login);
//        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.layout_titlebar_default);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            getWindow().setStatusBarColor(getResources().getColor(R.color.my_colorPrimaryDark));
//        }
        login_Type = getIntent().getIntExtra(EXTRA_LOGIN_TYPE, 0);
        isLive=getIntent().getBooleanExtra(EXTRA_LOGIN_LIVE,false);
        initView();
        initWeChatLogin();
        initTencentLogin();
    }

    /**
     * 刷新倒计时的线程
     */
    private class WaitTimer extends CountDownTimer {

        public WaitTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            btn_sms_key.setEnabled(false);
            tv_speech_verification.setEnabled(false);
            tv_speech_verification.setTextColor(getResources().getColor(R.color.gray_yuyin));
            btn_sms_key.setText("(" + millisUntilFinished / 1000 + ")重新获取");
        }

        @Override
        public void onFinish() {
            tv_speech_verification.setEnabled(true);
            tv_speech_verification.setTextColor(getResources().getColor(R.color.bule_overlay));
            btn_sms_key.setEnabled(true);
            btn_sms_key.setText(R.string.forgotpwd_btnGetSmsKey_text);
        }

    }

    WaitTimer waitTimer;
    String phoneNo = "", smsCode = "",invitation_code="";

    private class VTask extends AsyncTask<Void, Void, Object> {

        private Step mStep;

        public VTask(Step step) {
            mStep = step;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            String msg = "";
            switch (mStep) {
                case GET_VERIFY_CODE:
                    if (smstype == 2) {
                        msg = getString(R.string.forgotpwd_getSmsKey_yu_yin_loading);
                    } else {
                        msg = getString(R.string.forgotpwd_getSmsKey_loading);
                    }

                    break;
                case LOGIN_VERIFY_CODE:
                    msg = "验证码登录中...";
                    break;
            }
            loadingDialog.start(msg);
        }

        @Override
        protected Object doInBackground(Void... params) {
            try {
                String result = "";
                switch (mStep) {
                    case GET_VERIFY_CODE:

                        //  AccountAPI.getInstance().getMobileVerifyCode2(phoneNo, "", 1);
                        AccountAPI.getInstance().getSmsMobileVerifyCode(phoneNo, smstype);
                        result = "OK";
                        break;

                    case LOGIN_VERIFY_CODE:
                        String imei = Utils.GetAndroidImei(mContext);
                        //android版本号
                        String currentapiVersion = android.os.Build.VERSION.RELEASE;
                        //手机型号
                        String phoneName = android.os.Build.MANUFACTURER;
                        //网络
                        String netName = Utils.GetNetworkType(mContext);
                        return AccountAPI.getInstance().userSmsLogin(phoneNo, smsCode, imei, phoneName, "android " + currentapiVersion, netName);

                }
                return result;
            } catch (Exception ex) {
                ex.printStackTrace();
                return "error:" + ex.getMessage();
            }
        }

        @Override
        protected void onPostExecute(Object result) {

            if (loadingDialog.isShowing()) {
                loadingDialog.stop();
            }
            if (result instanceof String && ((String) result).startsWith("error:")) {
                String error = ((String) result).replace("error:", "");
                switch (mStep) {
                    case LOGIN_VERIFY_CODE:
                        ViewHub.showLongToast(mContext, error);
                        break;
                    default:
                        ViewHub.showLongToast(mContext, error);
                        break;
                }
            } else {
                switch (mStep) {
                    case GET_VERIFY_CODE:
                        // 启动定时器
                        if (smstype == 2) {
                            ViewHub.showOkDialog(mContext, "提示", getString(R.string.forgotpwd_getSmsKey_success_yuyin), "OK");
                        } else {
                            ViewHub.showOkDialog(mContext, "提示", "验证码已经发到"
                                    + edtAccount.getText().toString() + "的手机中", "OK");
                        }
                        waitTimer = new WaitTimer(60000, 1000);
                        waitTimer.start();

                        break;
                    case LOGIN_VERIFY_CODE:
                        if (!TextUtils.isEmpty(PublicData.getCookie(mContext))) {
                            SpManager.setCookie(mContext, PublicData.getCookie(mContext));
                        }
                        String msg = (String) result;
                        if ("user_no_exist".equals(msg)) { // 用户不存在
                            DialogUtils.showSureCancelDialog(WXEntryActivity.this, "登陆提示", "该账号还没注册，是否立即去注册？"
                                    , "注册账号", "换号登陆", new OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Dialog dialog = (Dialog) v.getTag();
                                            dialog.dismiss();
                                            //注册账号
                                            toReg();
                                        }
                                    }, new OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Dialog dialog = (Dialog) v.getTag();
                                            dialog.dismiss();
                                            edtAccount.setText(null);
                                            edtPassword.setText(null);
                                            edtAccount.requestFocus();
                                            edtAccount.setSelection(0);
                                        }
                                    });
                            break;
                        } else if ("password_error".equals(msg)) {//密码错误
                            DialogUtils.showToast(mContext, "你输入的密码错误，\n请重新填写。", 2000);
                            SpManager.setIs_Login(mContext, false);
                            break;
                        }


                        SpManager.setLoginAccount(mContext, edtAccount.getText().toString());
                        SpManager.setIs_Login(mContext, true);
                        // UMengTestUtls.onProfileSignIn(edtAccount.getText().toString());
                        EventBus.getDefault().post(BusEvent.getEvent(EventBusId.MAIN_DATA_RESH,isLive));
//                        Intent intent = new Intent(mContext, MainActivity.class);
//                        // intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
//                        startActivity(intent);
                        if (isLive){
                            setResult(RESULT_OK);
                        }
                        finish();

                        break;
                }
            }
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        mWxAPI.handleIntent(intent, this);
    }

//    @Override
//    public void onBackPressed() {
////        ViewHub.showExitDialog(this);
//        ViewHub.showExitLightPopDialog(this);
//    }

    /**
     * @description 初始化微信登录
     * @created 2014-12-18 上午11:56:41
     * @author ZZB
     */
    private void initWeChatLogin() {
        mWxAPI = WXAPIFactory.createWXAPI(mContext, Const.WeChatOpen.APP_ID_1);
        mWxAPI.registerApp(Const.WeChatOpen.APP_ID_1);
        mWxAPI.handleIntent(getIntent(), this);
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
                    BaiduStats.log(mContext, BaiduStats.EventId.QQ_LOGIN, mQQUserInfo.getNickName());
                    new Task(Step.CHECK_BIND_STATUS).execute(Const.TecentOpen.APP_ID, QQAccessTokenKeeper
                            .readAccessToken(mContext).getOpenId());
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
        layout_invitation_code=    findViewById(R.id.layout_invitation_code);
        layout_invitation_code.setVisibility(View.GONE);
        sv = (ScrollView) findViewById(R.id.scroll);
        img_see_pwd =(ImageView) findViewById(R.id.img_see_pwd);
        layout_password_login = findViewById(R.id.layout_password_login);
        tv_cancel = findViewById(R.id.tv_cancel);
        rl_top = findViewById(R.id.rl_top);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            rl_top.setPadding(0, ScreenUtils.getStatusBarHeight(this), 0, 0);
        }
        tv_cancel.setOnClickListener(this);
        layout_sms_login = findViewById(R.id.layout_sms_login);
        tv_speech_verification = (TextView) findViewById(R.id.tv_speech_verification);
        tv_speech_verification.setOnClickListener(this);
//        findViewById(R.id.tv_test).setVisibility(Debug.CONST_DEBUG ? View.VISIBLE : View.GONE);
      //  findViewById(R.id.tv_test).setVisibility(HttpsUtils.IS_LOCAL ? View.VISIBLE : View.GONE);
        TextView tvTitle = (TextView) findViewById(R.id.titlebar_tvTitle);
        login_btnSmSLogin = (Button) findViewById(R.id.login_btnSmSLogin);
        btn_sms_key = (Button) findViewById(R.id.btn_sms_key);
        btn_sms_key.setOnClickListener(this);
        login_btnSmSLogin.setOnClickListener(this);
        tvTitle.setText("登录");
        loadingDialog = new LoadingDialog(mContext);
        edtAccount = (AutoCompleteTextViewEx) findViewById(R.id.login_edtAccount);
        edtPassword = (EditText) findViewById(R.id.login_edtPassword);
        login_edtSms = (EditText) findViewById(R.id.login_edtSms);
        et_invitation_code= (EditText) findViewById(R.id.et_invitation_code);
        edtPassword.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                changeScrollView();
                return false;
            }
        });
        btnLogin = (Button) findViewById(R.id.login_btnLogin);
        btnForgotPwd = (Button) findViewById(R.id.login_btnForgotPwd);
        btnReg = (Button) findViewById(R.id.login_btnReg);
        edtAccount.setOnSearchLogDeleteListener(new AutoCompleteTextViewEx.OnSearchLogDeleteListener() {
            @Override
            public void onSearchLogDeleted(String text) {
                String newChar = SpManager.deleteLoginAccounts(getApplicationContext(), text);
                Log.i(getClass().getSimpleName(), "deleteSearchItemHistories:" + newChar);
                edtAccount.populateData(newChar, ",");
                edtAccount.getFilter().filter(edtAccount.getText());
            }
        });
        edtAccount.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                changeScrollView();
                return false;
            }
        });


        String username = SpManager.getLoginAccounts(mContext);
        edtAccount.populateData(username, ",");

        btnLogin.setOnClickListener(this);
        btnForgotPwd.setOnClickListener(this);
        btnReg.setOnClickListener(this);
        findViewById(R.id.btn_qq_login).setOnClickListener(this);
        findViewById(R.id.btn_wx_login).setOnClickListener(this);
        img_see_pwd.setOnClickListener(this);
        findViewById(R.id.test).setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                ViewHub.showLongToast(mContext, (Debug.CONST_DEBUG ? "测试：" : "正式：") + Debug.BUILD_VERSION_DATE + " " + HttpUtils.SERVERURL);
                return false;
            }
        });
        judeType();
    }

    private void judeType() {
        switch (type) {
            case TYPE_PASSWORD_LOGIN:
                layout_invitation_code.setVisibility(View.GONE);
                layout_password_login.setVisibility(View.VISIBLE);
                layout_sms_login.setVisibility(View.GONE);
                btnForgotPwd.setVisibility(View.VISIBLE);
                login_btnSmSLogin.setText(R.string.login_btnSmsLogin_text);
                edtAccount.setHint("手机号/用户名");
                edtAccount.setInputType(InputType.TYPE_CLASS_TEXT);
                edtAccount.setText(SpManager.getLoginAccount(mContext));
                break;
            case TYPE_SMS_LOGIN:
                showlayout_invitation_code();
                //layout_invitation_code.setVisibility(View.VISIBLE);
                edtAccount.setHint("手机号");
                edtAccount.setInputType(InputType.TYPE_CLASS_PHONE);
                layout_password_login.setVisibility(View.GONE);
                layout_sms_login.setVisibility(View.VISIBLE);
                btnForgotPwd.setVisibility(View.GONE);
                login_btnSmSLogin.setText(R.string.login_btnPassLogin_text);
                if (FunctionHelper.isMobileNum(SpManager.getLoginAccount(mContext)))
                edtAccount.setText(SpManager.getLoginAccount(mContext));
                else
                    edtAccount.setText("");
                break;
        }
    }
    private void showlayout_invitation_code(){
        addSubscribe(HttpManager.getInstance().getPinHuoNoCacheApi(TAG).getConfigIndex()
                .compose(RxUtil.<PinHuoResponse<InvitatioCodeBean>>rxSchedulerHelper())
                .compose(RxUtil.<InvitatioCodeBean>handleResult())
                .subscribeWith(new CommonSubscriber<InvitatioCodeBean>(mContext,true,R.string.loading) {
                    @Override
                    public void onNext(InvitatioCodeBean bean) {
                        super.onNext(bean);
                       if (bean!=null){
                           if (bean.IsInvitationCode){
                               layout_invitation_code.setVisibility(View.VISIBLE);
                               if (et_invitation_code!=null)
                                   et_invitation_code.setHint(bean.getPlaceholder());
                           }else {
                               layout_invitation_code.setVisibility(View.GONE);
                           }
                       }else {
                           layout_invitation_code.setVisibility(View.GONE);
                       }
                    }
                }));
    }

    private void changeScrollView() {
        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                sv.scrollTo(0, sv.getHeight());
            }
        }, 300);
    }

    Handler h = new Handler();

    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        EventBus.getDefault().post(BusEvent.getEvent(EventBusId.REFRESH_COMPLETEd1));
//        EventBus.getDefault().post(BusEvent.getEvent(EventBusId.REFRESH_GROUP_DETAIL_NEW));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_cancel:
                // EventBus.getDefault().post(BusEvent.getEvent(EventBusId.REFRESH_COMPLETEd1));
                // EventBus.getDefault().post(BusEvent.getEvent(EventBusId.REFRESH_GROUP_DETAIL_NEW));
                //EventBus.getDefault().post(BusEvent.getEvent(EventBusId.PINHUO_AD_REFRESH));
                finish();
                break;
            case R.id.login_btnSmSLogin:
                //短信快捷登录
                if (type == TYPE_PASSWORD_LOGIN) {
                    type = TYPE_SMS_LOGIN;
                } else {
                    type = TYPE_PASSWORD_LOGIN;
                }
                judeType();
                smstype = 1;
                // new VTask(Step.GET_VERIFY_CODE).execute();
                break;
            case R.id.tv_speech_verification:
                //语音验证
                smstype = 2;
                phoneNo = edtAccount.getText().toString().trim();
                new VTask(Step.GET_VERIFY_CODE).execute();
                break;
            case R.id.btn_sms_key:
                //验证码
                phoneNo = edtAccount.getText().toString().trim();
                new VTask(Step.GET_VERIFY_CODE).execute();
                break;
            case R.id.img_see_pwd:
                int length = edtPassword.getText().length();
                    if (edtPassword.getInputType() == InputType.TYPE_TEXT_VARIATION_PASSWORD) {
                        edtPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        edtPassword.invalidate();
                        edtPassword.setSelection(length);
                        img_see_pwd.setImageResource(R.drawable.close_eye);
                    } else {
                        edtPassword.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        edtPassword.invalidate();
                        edtPassword.setSelection(edtPassword.getText().length());
                        img_see_pwd.setImageResource(R.drawable.see_eye);

                    }
                break;
            case R.id.login_btnLogin:
                phoneNo = edtAccount.getText().toString().trim();
                String password = edtPassword.getText().toString().trim();
                String encode = login_edtSms.getText().toString().trim();
                switch (type) {
                    case TYPE_PASSWORD_LOGIN:
                        login(phoneNo, password);
                        break;
                    case TYPE_SMS_LOGIN:
                        if (TextUtils.isEmpty(phoneNo)) {
                            Toast.makeText(mContext, R.string.login_edtAccount_empty, Toast.LENGTH_SHORT).show();
                            edtAccount.requestFocus();
                        } else if (TextUtils.isEmpty(encode)) {
                            ViewHub.showShortToast(mContext, "请输入验证码");
                        } else {
                            phoneNo = edtAccount.getText().toString().trim();
                            smsCode = login_edtSms.getText().toString().trim();
                            invitation_code=et_invitation_code.getText().toString().trim();
                            //  new VTask(Step.LOGIN_VERIFY_CODE).execute();
                            gotoSmSLogin(phoneNo, smsCode,invitation_code);
                        }
                        break;
                }

//                login();
                break;
            case R.id.login_btnReg:
                toReg();
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

    private void toReg() {
        String phone = edtAccount.getText().toString();
        if (!FunctionHelper.isPhoneNo(phone)) {
            phone = null;
        }
//        Intent regIntent = new Intent(mContext, UserRegActivity.class);
//        regIntent.putExtra("phone", phone) ;
//        startActivity(regIntent);
        Intent intent = new Intent(this, SignUpActivity.class);
        intent.putExtra("phone", phone);
        startActivity(intent);
    }

    /**
     * @description QQ登录
     * @created 2014-12-19 下午6:07:10
     * @author ZZB
     */
    private void qqLogin() {
        mLoginFrom = Const.LoginFrom.QQ;
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
        IS_LOGIN_FROM_WECHAT = true;
        mLoginFrom = Const.LoginFrom.WECHAT;
        boolean isWxSupport = mWxAPI.isWXAppInstalled();
        if (!isWxSupport) {
            ViewHub.showLongToast(mContext, "您未安装微信或者微信版本过低");
            return;
        }
        mWeChatToken = WeChatAccessTokenKeeper.readAccessToken(mContext);
        if (!mWeChatToken.isSessionValid()) {
            SendAuth.Req req = new SendAuth.Req();
            req.scope = "snsapi_userinfo";
            req.state = "state";
            mWxAPI.sendReq(req);
        } else {
            new Task(Step.WECHAT_LOGIN2).execute();
        }

    }

    /**
     * 登录
     */
    private void login(String phoneNo, String password) {


        // 验证用户录入
        if (!validateInput())
            return;
        // 验证网络
        if (!FunctionHelper.CheckNetworkOnline(mContext))
            return;
        // 执行登录操作
//        new Task(Step.LOGIN).execute();
        // new Task(Step.LOGIN, phoneNo, password).execute();
        gotoLogin(phoneNo, password);
    }

    private void gotoLogin(String phoneNo, String password) {
        String imei = Utils.GetAndroidImei(mContext);
        //android版本号
        String currentapiVersion = android.os.Build.VERSION.RELEASE;
        //手机型号
        String phoneName = android.os.Build.MANUFACTURER;
        //网络
        String netName = Utils.GetNetworkType(mContext);
        addSubscribe(HttpManager.getInstance().getPinHuoNoCacheApi(TAG)
                        .getLoginData(phoneNo, MD5Utils.encrypt32bit(password),
                                true, LOGIN_REGISTER_FROM_ANDROID, imei, phoneName, "android " + currentapiVersion, netName, imei, AnalyticsConfig.getChannel(BWApplication.getInstance()))
                        .compose(RxUtil.<PinHuoResponse<LoginBean>>rxSchedulerHelper())
                        .compose(RxUtil.<LoginBean>handleResult())
                        .subscribeWith(new CommonSubscriber<LoginBean>(mContext, true, R.string.login_doLogin_loading) {
                            @Override
                            public void onNext(LoginBean loginBean) {
                                super.onNext(loginBean);
                                if (!TextUtils.isEmpty(PublicData.getCookie(mContext))) {
                                    SpManager.setCookie(mContext, PublicData.getCookie(mContext));
                                }
                                //设置登录状态
                                SpManager.setIs_Login(mContext, true);
                                SpManager.setLoginAccount(mContext, edtAccount.getText().toString());
                                if (loginBean != null) {
                                    SpManager.setUserId(mContext, loginBean.getUserID());
                                    SpManager.setUserName(mContext, loginBean.getUserName());
                                }

//                        Intent intent = new Intent(mContext, MainActivity.class);
//                        // intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
//                        startActivity(intent);
                                //友盟统计账号
                                // UMengTestUtls.onProfileSignIn(edtAccount.getText().toString());
                                EventBus.getDefault().post(BusEvent.getEvent(EventBusId.MAIN_DATA_RESH,isLive));
                                if (isLive){
                                    setResult(RESULT_OK);
                                }
                                EventBus.getDefault().post(BusEvent.getEvent(EventBusId.ITEM_DETAIL_REFESH));
                                judeReLogin();
                                finish();
                            }

                            @Override
                            public void onError(Throwable e) {
                                super.onError(e);
                                SpManager.setIs_Login(mContext, false);
                                if (e instanceof ApiException) {
                                    ApiException apiException = (ApiException) e;
                                    if (!TextUtils.isEmpty(apiException.getCode())) {
                                        if (apiException.getCode().equals(RxUtil.USER_NO_EXIST)) {
                                            //设置登录状态
                                            DialogUtils.showSureCancelDialog(WXEntryActivity.this, "登陆提示", "该账号还没注册，是否立即去注册？"
                                                    , "注册账号", "换号登陆", new OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {
                                                            Dialog dialog = (Dialog) v.getTag();
                                                            dialog.dismiss();
                                                            //注册账号
                                                            toReg();
                                                        }
                                                    }, new OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {
                                                            Dialog dialog = (Dialog) v.getTag();
                                                            dialog.dismiss();
                                                            edtAccount.setText(null);
                                                            edtPassword.setText(null);
                                                            edtAccount.requestFocus();
                                                            edtAccount.setSelection(0);
                                                        }
                                                    });
                                        } else if (apiException.getCode().equals(RxUtil.PASSWORD_ERROR)) {
                                            DialogUtils.showToast(mContext, apiException.getMessage() + "\n请重新填写", 2000);
                                        }
                                    }
                                }

                            }
                        })
        );
    }

    private void judeReLogin() {
        if (login_Type == LOGIN_TYPE_RELOGIN) {
            BWApplication.reBackFirst();
            EventBus.getDefault().post(BusEvent.getEvent(EventBusId.MAIN_CHANGE_SHOPCAT));
        }
    }

    private void gotoSmSLogin(String phoneNo, String smsCode,String invitation_code) {
        String imei = Utils.GetAndroidImei(mContext);
        //android版本号
        String currentapiVersion = android.os.Build.VERSION.RELEASE;
        //手机型号
        String phoneName = android.os.Build.MANUFACTURER;
        //网络
        String netName = Utils.GetNetworkType(mContext);
        addSubscribe(HttpManager.getInstance().getPinHuoNoCacheApi(TAG)
                        .getSMSLoginData(phoneNo, smsCode,
                                true, LOGIN_REGISTER_FROM_ANDROID, imei, phoneName, "android " + currentapiVersion, netName, imei, AnalyticsConfig.getChannel(BWApplication.getInstance()),invitation_code)
                        .compose(RxUtil.<PinHuoResponse<LoginBean>>rxSchedulerHelper())
                        .compose(RxUtil.<LoginBean>handleResult())
                        .subscribeWith(new CommonSubscriber<LoginBean>(mContext, true, R.string.login_sms_doLogin_loading) {
                            @Override
                            public void onNext(LoginBean loginBean) {
                                super.onNext(loginBean);
                                if (!TextUtils.isEmpty(PublicData.getCookie(mContext))) {
                                    SpManager.setCookie(mContext, PublicData.getCookie(mContext));
                                }
                                //设置登录状态
                                SpManager.setIs_Login(mContext, true);
                                SpManager.setLoginAccount(mContext, edtAccount.getText().toString());
                                if (loginBean != null) {
                                    SpManager.setUserId(mContext, loginBean.getUserID());
                                    SpManager.setUserName(mContext, loginBean.getUserName());
                                }
//                        Intent intent = new Intent(mContext, MainActivity.class);
//                        // intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
//                        startActivity(intent);
                                //友盟统计账号
                                // UMengTestUtls.onProfileSignIn(edtAccount.getText().toString());
                                EventBus.getDefault().post(BusEvent.getEvent(EventBusId.MAIN_DATA_RESH,isLive));
                                if (isLive){
                                    setResult(RESULT_OK);
                                }
                                EventBus.getDefault().post(BusEvent.getEvent(EventBusId.ITEM_DETAIL_REFESH));
                                judeReLogin();
                                finish();
                            }

                            @Override
                            public void onError(Throwable e) {
                                super.onError(e);
                                SpManager.setIs_Login(mContext, false);
                                if (e instanceof ApiException) {
                                    ApiException apiException = (ApiException) e;
                                    if (!TextUtils.isEmpty(apiException.getCode())) {
                                        if (apiException.getCode().equals(RxUtil.USER_NO_EXIST)) {
                                            //设置登录状态
                                            DialogUtils.showSureCancelDialog(WXEntryActivity.this, "登陆提示", "该账号还没注册，是否立即去注册？"
                                                    , "注册账号", "换号登陆", new OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {
                                                            Dialog dialog = (Dialog) v.getTag();
                                                            dialog.dismiss();
                                                            //注册账号
                                                            toReg();
                                                        }
                                                    }, new OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {
                                                            Dialog dialog = (Dialog) v.getTag();
                                                            dialog.dismiss();
                                                            edtAccount.setText(null);
                                                            edtPassword.setText(null);
                                                            edtAccount.requestFocus();
                                                            edtAccount.setSelection(0);
                                                        }
                                                    });
                                        } else if (apiException.getCode().equals(RxUtil.PASSWORD_ERROR)) {
                                            DialogUtils.showToast(mContext, apiException.getMessage() + "\n请重新填写", 2000);
                                        }
                                    }
                                }

                            }
                        })
        );
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
        private String phoneNo;
        private String password;

        public Task(Step step) {
            mStep = step;
        }

        public Task(Step step, String name, String pwd) {
            mStep = step;
            this.phoneNo = name;
            this.password = pwd;
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
                case CHECK_BIND_STATUS:
                    loadingDialog.start("检查绑定状态中...");
                    break;
                case THIRD_LOGIN:
                    loadingDialog.start("登录中...");
                    break;
            }

        }

        @Override
        protected Object doInBackground(Object... params) {
            try {
                switch (mStep) {
                    case LOGIN:
//                        String phoneNo = edtAccount.getText().toString().trim();
//                        String password = edtPassword.getText().toString().trim();
                        //提交登录记录操作
                        //imei
                        String imei = Utils.GetAndroidImei(mContext);
                        //android版本号
                        String currentapiVersion = android.os.Build.VERSION.RELEASE;
                        //手机型号
                        String phoneName = android.os.Build.MANUFACTURER;
                        //网络
                        String netName = Utils.GetNetworkType(mContext);
                        return AccountAPI.getInstance().userLogin(phoneNo, password, imei, phoneName, "android " + currentapiVersion, netName);


                    case QQ_LOGIN:
                    case WECHAT_LOGIN1:// 获取access token
                        String code = params[0].toString();
                        mWeChatToken = WeChatAPI.getLoginAccessToken(mContext, code);
                        // 保存access token到本地
                        WeChatAccessTokenKeeper.writeAccessToken(mContext, mWeChatToken);

                    case WECHAT_LOGIN2:// 获取微信用户信息
                        WeChatUserInfo userInfo = WeChatAPI.getUserInfo(mContext, mWeChatToken);
                        BaiduStats.log(mContext, BaiduStats.EventId.WECHAT_LOGIN, userInfo.getNickName());
                        return userInfo;
                    case CHECK_BIND_STATUS:// 检查绑定状态
                        String appId = params[0].toString();
                        String openId = params[1].toString();
                        boolean isBinded = AccountAPI.isThirdLoginBinded(appId, openId);
                        return isBinded;
                    case THIRD_LOGIN:// 第三方登录
                        switch (mLoginFrom) {
                            case QQ:
                                QQAccessToken token = QQAccessTokenKeeper.readAccessToken(mContext);
                                AccountAPI.thirdLogin(Const.TecentOpen.APP_ID, token.getOpenId(),
                                        token.getAccessToken());
                                break;
                            case WECHAT:
                                WeChatAccessToken wechatToken = WeChatAccessTokenKeeper.readAccessToken(mContext);
                                AccountAPI.thirdLogin(Const.WeChatOpen.APP_ID_1, wechatToken.getOpenId(),
                                        wechatToken.getAccessToken());
                                break;
                        }

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
            if (loadingDialog.isShowing()) {
                loadingDialog.stop();
            }
            if (result instanceof String && ((String) result).startsWith("error:")) {
                ViewHub.showLongToast(mContext, mStep.toString() + ":" + result.toString().replace("error:", ""));
                //设置登录状态
                SpManager.setIs_Login(mContext, false);
            } else {
                switch (mStep) {
                    case LOGIN:
                        if (!TextUtils.isEmpty(PublicData.getCookie(mContext))) {
                            SpManager.setCookie(mContext, PublicData.getCookie(mContext));
                        }
                        String msg = (String) result;
                        if ("user_no_exist".equals(msg)) { // 用户不存在
                            //设置登录状态
                            SpManager.setIs_Login(mContext, false);
                            DialogUtils.showSureCancelDialog(WXEntryActivity.this, "登陆提示", "该账号还没注册，是否立即去注册？"
                                    , "注册账号", "换号登陆", new OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Dialog dialog = (Dialog) v.getTag();
                                            dialog.dismiss();
                                            //注册账号
                                            toReg();
                                        }
                                    }, new OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Dialog dialog = (Dialog) v.getTag();
                                            dialog.dismiss();
                                            edtAccount.setText(null);
                                            edtPassword.setText(null);
                                            edtAccount.requestFocus();
                                            edtAccount.setSelection(0);
                                        }
                                    });
                            break;
                        } else if ("password_error".equals(msg)) {//密码错误
                            //设置登录状态
                            SpManager.setIs_Login(mContext, false);
                            DialogUtils.showToast(mContext, "你输入的密码错误，\n请重新填写。", 2000);
                            break;
                        }
                        //设置登录状态
                        SpManager.setIs_Login(mContext, true);
                        SpManager.setLoginAccount(mContext, edtAccount.getText().toString());
//                        Intent intent = new Intent(mContext, MainActivity.class);
//                        // intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
//                        startActivity(intent);
                        //友盟统计账号
                        // UMengTestUtls.onProfileSignIn(edtAccount.getText().toString());
                        EventBus.getDefault().post(BusEvent.getEvent(EventBusId.MAIN_DATA_RESH,isLive));
                        if (isLive){
                            setResult(RESULT_OK);
                        }
                        finish();
                        break;
                    case QQ_LOGIN:
                        break;
                    case WECHAT_LOGIN1:
                    case WECHAT_LOGIN2:
                        mWechatUserInfo = (WeChatUserInfo) result;
                        new Task(Step.CHECK_BIND_STATUS).execute(Const.WeChatOpen.APP_ID_1, WeChatAccessTokenKeeper
                                .readAccessToken(mContext).getOpenId());
                        break;
                    case CHECK_BIND_STATUS:
                        boolean isBinded = (Boolean) result;
                        if (isBinded) {// 如果已经绑定，执行第三方登录
                            new Task(Step.THIRD_LOGIN).execute();
                        } else {
                            Intent registerIntent = new Intent(mContext, BindOrRegisterUserActivity.class);
                            registerIntent.putExtra(BindOrRegisterUserActivity.EXTRA_LOGIN_FROM, mLoginFrom);
                            String iconUrl = "";
                            String userName = "";
                            switch (mLoginFrom) {
                                case QQ:
                                    iconUrl = mQQUserInfo.getIconUrl100();
                                    userName = mQQUserInfo.getNickName();
                                    break;
                                case WECHAT:
                                    iconUrl = mWechatUserInfo.getImgUrl();
                                    userName = mWechatUserInfo.getNickName();
                                    break;
                            }
                            registerIntent.putExtra(BindOrRegisterUserActivity.EXTRA_ICON_URL, iconUrl);
                            registerIntent.putExtra(BindOrRegisterUserActivity.EXTRA_USER_NAME, userName);
                            startActivity(registerIntent);
                        }
                        break;
                    case THIRD_LOGIN:
                        if (!TextUtils.isEmpty(PublicData.getCookie(mContext))) {
                            SpManager.setCookie(mContext, PublicData.getCookie(mContext));
                        }
                        SpManager.setIs_Login(mContext, true);
                        // UMengTestUtls.onProfileSignIn(edtAccount.getText().toString());
                        EventBus.getDefault().post(BusEvent.getEvent(EventBusId.MAIN_DATA_RESH,isLive));
                        if (isLive){
                            setResult(RESULT_OK);
                        }
//                        Intent thirdIntent = new Intent(mContext, MainActivity.class);
//                        // thirdIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
//                        startActivity(thirdIntent);
                        finish();
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
        if (resp.getType() == ConstantsAPI.COMMAND_LAUNCH_WX_MINIPROGRAM) {
            WXLaunchMiniProgram.Resp launchMiniProResp = (WXLaunchMiniProgram.Resp) resp;
            String extraData = launchMiniProResp.extMsg; // 对应JsApi navigateBackApplication中的extraData字段数据
        } else {
            if (IS_LOGIN_FROM_WECHAT) {
                IS_LOGIN_FROM_WECHAT = false;
            } else {
                finish();
                return;
            }

            mLoginFrom = Const.LoginFrom.WECHAT;
            try {
                switch (resp.errCode) {
                    case 0:// 用户同意
                        SendAuth.Resp r = (SendAuth.Resp) resp;
                        new Task(Step.WECHAT_LOGIN1).execute(r.code);
                        break;
                    case -4:// 用户拒绝授权
                        ViewHub.showShortToast(mContext, "用户拒绝授权");
                        break;
                    case -2:// 用户取消
                        ViewHub.showShortToast(mContext, "用户取消");
                        break;
                }
            } catch (Exception e) {
                if (e != null) {
                    Log.e(TAG, e.toString());
                }

            }
        }
    }

}
