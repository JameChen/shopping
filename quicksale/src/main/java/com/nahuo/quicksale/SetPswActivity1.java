package com.nahuo.quicksale;

import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.baidu.mobstat.StatService;
import com.nahuo.library.controls.LightAlertDialog;
import com.nahuo.library.controls.LoadingDialog;
import com.nahuo.quicksale.api.AccountAPI;
import com.nahuo.quicksale.api.PaymentAPI;
import com.nahuo.quicksale.common.Const.PasswordExtra;
import com.nahuo.quicksale.common.Const.PasswordType;
import com.nahuo.quicksale.common.SpManager;
import com.nahuo.quicksale.eventbus.BusEvent;
import com.nahuo.quicksale.eventbus.EventBusId;
import com.nahuo.quicksale.provider.UserInfoProvider;
import com.nahuo.quicksale.wxapi.WXEntryActivity;

import de.greenrobot.event.EventBus;

/**
 * Description:设置密码
 * 2014-7-29 上午10:44:45
 * @author ZZB
 */
public class SetPswActivity1 extends BaseSlideBackActivity implements View.OnClickListener {

    public static final String EXTRA_LISTENER = "EXTRA_LISTENER";
    public static final String EXTRA_VERIFYCODE = "EXTRA_VERIFYCODE";
    private EditText mEtFirstPsw;
    private EditText mEtSecondPsw;
    private PasswordType mPswType;
    private LoadingDialog mDialog;
    public Context mContext = this;
    private Listener mListener;
    private String mVerifyCode;
    public static interface Listener extends Parcelable {
        public void onPayPswSet();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);// 设置自定义标题栏
        setContentView(R.layout.activity_set_psw1);
//        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
//                R.layout.layout_titlebar_default);// 更换自定义标题栏布局
        initParams();
        initView();
    }

    private void initParams() {
        Intent intent = getIntent();
        mListener = (Listener) intent.getSerializableExtra(EXTRA_LISTENER);
        mPswType = (PasswordType) intent.getSerializableExtra(PasswordExtra.EXTRA_PSW_TYPE);
        mVerifyCode = intent.getStringExtra(EXTRA_VERIFYCODE);
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

    private void initView() {
        initTitleBar();
        mDialog = new LoadingDialog(this);
        mEtFirstPsw = (EditText) findViewById(R.id.first_psw);
        mEtSecondPsw = (EditText) findViewById(R.id.second_psw);
        mEtSecondPsw.setVisibility(View.GONE);
        mEtFirstPsw.setHint(mPswType != PasswordType.LOGIN ? "支付密码" : "新登录密码");
        mEtFirstPsw.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
       // mEtSecondPsw.setHint(mPswType != PasswordType.LOGIN ? "确认支付密码" : "确认登录密码");
    }

    private void initTitleBar() {
        TextView tvTitle = (TextView) findViewById(R.id.titlebar_tvTitle);
        tvTitle.setText(mPswType != PasswordType.LOGIN ? "设置支付密码" : "设置登录密码");

        Button btnLeft = (Button) findViewById(R.id.titlebar_btnLeft);
        btnLeft.setText(R.string.titlebar_btnBack);
        btnLeft.setVisibility(View.VISIBLE);
        btnLeft.setOnClickListener(this);

    }

    private boolean validateInput() {

        String firstPsw = mEtFirstPsw.getText().toString().trim();
        //String secondPsw = mEtSecondPsw.getText().toString().trim();
        if (TextUtils.isEmpty(firstPsw)) {
            ViewHub.setEditError(mEtFirstPsw, "新密码不能为空");
            return false;
        }
//        else if (TextUtils.isEmpty(secondPsw)) {
//            ViewHub.setEditError(mEtSecondPsw, "确认密码不能为空");
//            return false;
//        }
        else if (firstPsw.length() < 6 ) {
            ViewHub.setEditError(mEtFirstPsw, "密码长度最少为6位数");
            return false;
        }
// else if (!firstPsw.equals(secondPsw)) {
//            ViewHub.setEditError(mEtFirstPsw, "新密码与确认密码不一致");
//            return false;
//        }
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.titlebar_btnLeft:
            finish();
            break;
        case R.id.btn_confirm:
            setPayPsw();
            break;
        }

    }

    private void setPayPsw() {
        boolean validate = validateInput();
        String bindPhone = UserInfoProvider.getBindPhone(mContext, SpManager.getUserId(mContext));
        String psw = mEtFirstPsw.getText().toString().trim();
        boolean binded = !TextUtils.isEmpty(bindPhone);
        if (validate) {
            if (binded) {
                new Task().execute(psw, bindPhone);
            } else {
                ViewHub.showOkDialog(mContext, "提示" , "请先绑定手机？", "好" , new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface d, int which) {
                        Intent intent = new Intent(mContext, BindPhoneActivity.class);
                        startActivity(intent);
                    }
                });
//                LightAlertDialog.Builder.create(mContext).setTitle("提示").setMessage("请先绑定手机")
//                        .setPositiveButton("好", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                Intent intent = new Intent(mContext, BindPhoneActivity.class);
//                                startActivity(intent);
//                            }
//                        }).show();
            }
        }

    }

    @Override
    public void finish() {
        super.finish();
        ViewHub.hideKeyboard(this);
    }

    /**
     * @description 支付密码设置成功
     * @created 2014-9-18 下午9:28:48
     * @author ZZB
     */
    private void onPayPswSet() {
        int userId = SpManager.getUserId(mContext);
        // 缓存支付密码
        UserInfoProvider.setHasSetPayPsw(mContext, userId);

        Builder builder = LightAlertDialog.Builder.create(mContext);
        builder.setTitle("提示").setMessage("设置成功")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                        if (mListener != null) {
                            mListener.onPayPswSet();
                        }
                        EventBus.getDefault().post(BusEvent.getEvent(EventBusId.FINISH_PAY_PWD));
                    }
                });
        builder.show();
    }

    private class Task extends AsyncTask<Object, Void, Object> {

        @Override
        protected void onPreExecute() {
            mDialog.start("设置密码中...");
        }

        @Override
        protected Object doInBackground(Object... params) {
            try {
                String psw = (String) params[0];
                String phoneNo = (String) params[1];
                switch (mPswType) {
                case LOGIN:
                    AccountAPI.getInstance().resetPassword(phoneNo, psw, mVerifyCode);
                    break;
                case PAYMENT:
                    PaymentAPI.setPayPsw(mContext, psw, phoneNo);
                    break;
                case RESET_PAYMENT:
                    PaymentAPI.resetPayPsw(mContext, phoneNo, psw, mVerifyCode);
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
            if (mDialog.isShowing()) {
                mDialog.stop();
            }
            if (result instanceof String && ((String) result).startsWith("error:")) {
                ViewHub.showLongToast(mContext, ((String) result).replace("error:", ""));
            } else {
                switch (mPswType) {
                case LOGIN:
                    ViewHub.showOkDialog(mContext, "提示", "设置成功", "重新登录", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(mContext, WXEntryActivity.class);
                            intent.putExtra(WXEntryActivity.EXTRA_TYPE, WXEntryActivity.Type.LOGIN);
                            intent.putExtra(WXEntryActivity.EXTRA_LOGIN_TYPE,WXEntryActivity.LOGIN_TYPE_RELOGIN);
                            startActivity(intent);
                        }
                    });
                    
                    break;
                case PAYMENT:
                case RESET_PAYMENT:
                    onPayPswSet();
                    break;
                }

            }
        }

    }

}
