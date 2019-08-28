package com.nahuo.quicksale;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.baidu.mobstat.StatService;
import com.nahuo.library.controls.LoadingDialog;
import com.nahuo.quicksale.activity.FogetPayPassWordActivity;
import com.nahuo.quicksale.api.AccountAPI;
import com.nahuo.quicksale.api.PaymentAPI;
import com.nahuo.quicksale.common.Const.PasswordExtra;
import com.nahuo.quicksale.common.Const.PasswordType;
import com.nahuo.quicksale.oldermodel.PublicData;

/**
 * Description：修改密码页面
 * 2014-7-4上午11:47:54
 */
public class ChangePswActivity extends BaseSlideBackActivity implements OnClickListener {

    private static final String TAG = ChangePswActivity.class.getSimpleName();
    private Context mContext = this;
    private LoadingDialog mDialog;
    private EditText mEtOldPsw;
    private EditText mEtFirstPsw;
    private EditText mEtSecondPsw;
    private TextView mTvForgetPsw;
    private PasswordType mType;
    private Listener mListener;

    public static interface Listener {

        public void onChangePswFinish(boolean success);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //  requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);// 设置自定义标题栏
        setContentView(R.layout.activity_change_psw);
//        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
//                R.layout.layout_titlebar_default);// 更换自定义标题栏布局
        mType = (PasswordType) getIntent().getSerializableExtra(PasswordExtra.EXTRA_PSW_TYPE);
        initView();
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
        mDialog = new LoadingDialog(this);
        initTitleBar();
        mEtOldPsw = (EditText) findViewById(R.id.old_psw);
        mEtFirstPsw = (EditText) findViewById(R.id.first_psw);
        mEtSecondPsw = (EditText) findViewById(R.id.second_psw);
        mTvForgetPsw = (TextView) findViewById(R.id.tv_forget_paypsw);

        mEtOldPsw.setHint(mType != PasswordType.LOGIN ? "旧支付密码" : "旧登录密码");
        mEtFirstPsw.setHint(mType != PasswordType.LOGIN ? "新支付密码" : "新登录密码");
        mEtSecondPsw.setHint(mType != PasswordType.LOGIN ? "确认新支付密码" : "确认新登录密码");
        mTvForgetPsw.setText(mType != PasswordType.LOGIN ? "忘记支付密码?" : "忘记登录密码?");
        // 下划线加抗锯齿
        mTvForgetPsw.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);

    }

    private void initTitleBar() {
        TextView title = (TextView) findViewById(R.id.titlebar_tvTitle);
        title.setText(mType != PasswordType.LOGIN ? "修改支付密码" : "修改登录密码");
        Button btnLeft = (Button) findViewById(R.id.titlebar_btnLeft);
        btnLeft.setText(R.string.titlebar_btnBack);
        btnLeft.setVisibility(View.VISIBLE);
        btnLeft.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_confirm:
                changePsw();
                break;
            case R.id.tv_forget_paypsw:
                forgetPayPsw();
                break;
            case R.id.titlebar_btnLeft:
                ViewHub.hideKeyboard(this);
                finish();
                break;
        }
    }

    /**
     * Description:提交修改密码
     * 2014-7-4下午1:52:31
     */
    private void changePsw() {
        boolean validate = validateInput();
        if (validate) {
            new Task().execute();
        }
    }

    /**
     * Description:校验密码输入
     * 2014-7-4下午1:56:22
     */
    private boolean validateInput() {
        String result = "";
        String oldPsw = mEtOldPsw.getText().toString().trim();
        String firstPsw = mEtFirstPsw.getText().toString().trim();
        String secondPsw = mEtSecondPsw.getText().toString().trim();
        if (TextUtils.isEmpty(oldPsw)) {
            result = "旧密码不能为空";
            setError(mEtOldPsw, result);
        } else if (TextUtils.isEmpty(firstPsw)) {
            result = "新密码不能为空";
            setError(mEtFirstPsw, result);
        } else if (TextUtils.isEmpty(secondPsw)) {
            result = "确认密码不能为空";
            setError(mEtSecondPsw, result);
        } else if (oldPsw.length() < 6) {
            result = "密码长度最少为6位数";
            setError(mEtOldPsw, result);
        } else if (firstPsw.length() < 6) {
            result = "密码长度最少为6位数";
            setError(mEtFirstPsw, result);
        } else if (secondPsw.length() < 6) {
            result = "密码长度最少为6位数";
            setError(mEtSecondPsw, result);
        } else if (!firstPsw.equals(secondPsw)) {
            result = "新密码与确认密码不一致";
            setError(mEtFirstPsw, result);
        } else if (oldPsw.equals(firstPsw)) {
            result = "旧密码与新密码一样";
            setError(mEtFirstPsw, result);
        } else {
            return true;
        }

        return false;
    }

    /**
     * Description:忘记支付密码
     * 2014-7-4下午1:53:07
     */
    private void forgetPayPsw() {
//        Intent intent = new Intent(mContext, FogetPayPassWordActivity.class);
//        startActivity(intent);
        Intent intent = null;
        if (mType == PasswordType.LOGIN) {
            intent = new Intent(this, ForgotPwdActivity.class);
            intent.putExtra(PasswordExtra.EXTRA_PSW_TYPE, mType);
        } else {
            intent = new Intent(this, FogetPayPassWordActivity.class);
        }
            /*
             * 注释下面这行的原因：如果应用退出到后台，会返回到ChangePswActivity而不是ForgetPswActivity
             * 之前加上这行的原因：ChangePswActivity->ForgetPswActivity->SetPswActivity
             * 如果有下面这行，SetPswActivity返回直接到ChangePswActivity，不会退回到找验证码页面
             */
        // intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);// 不加入回退栈

        startActivity(intent);
//        boolean set = UserInfoProvider.hasSetSafeQuestion(mContext, SpManager.getUserId(mContext));
//        if (set) {
//            Intent intent = null;
//            if (mType == PasswordType.LOGIN) {
//                intent = new Intent(this, ForgotPwdActivity.class);
//            } else {
//                intent = new Intent(this, ForgetPswActivity.class);
//            }
//            intent.putExtra(PasswordExtra.EXTRA_PSW_TYPE, mType);
//            /*
//             * 注释下面这行的原因：如果应用退出到后台，会返回到ChangePswActivity而不是ForgetPswActivity
//             * 之前加上这行的原因：ChangePswActivity->ForgetPswActivity->SetPswActivity
//             * 如果有下面这行，SetPswActivity返回直接到ChangePswActivity，不会退回到找验证码页面
//             */
//            // intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);// 不加入回退栈
//
//            startActivity(intent);
//        }else if(!set){
//            Builder builder = LightAlertDialog.Builder.create(this);
//            builder.setTitle("提示").setMessage("您还没有设置密码问题").setNegativeButton("取消", null)
//                    .setPositiveButton("去设置", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            Intent intent = new Intent(mContext, SetSafeQuestionsActivity.class);
//                            startActivity(intent);
//                        }
//                    });
//            builder.show();
//        }

    }

    private void setError(EditText et, String error) {
        et.setError(Html.fromHtml("<font color='black'>" + error + "</font>"));
        et.requestFocus();
    }

    public void setListener(Listener listener) {
        mListener = listener;
    }

    private class Task extends AsyncTask<Void, Void, Object> {

        String oldPsw, newPsw;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mDialog.start("修改中...");
            oldPsw = mEtOldPsw.getText().toString();
            newPsw = mEtFirstPsw.getText().toString();
        }

        @Override
        protected Object doInBackground(Void... params) {
            try {
                switch (mType) {
                    case RESET_PAYMENT:
                        PaymentAPI.changePayPsw(mContext, oldPsw, newPsw);
                        break;
                    case LOGIN:
                        AccountAPI.changeLoginPassword(oldPsw, newPsw, PublicData.getCookie(mContext));

                        break;
                    default:
                        break;
                }
            } catch (Exception e) {
                Log.d(TAG, e.getMessage());
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
                String error = result.toString().replace("error:", "");
                ViewHub.showOkDialog(mContext, "提示", error, "OK");
            } else {
                switch (mType) {
                    case RESET_PAYMENT:
                    case LOGIN:
                        ViewHub.showOkDialog(mContext, "提示", "修改密码成功", "OK",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        finish();
                                        if (mListener != null) {
                                            mListener.onChangePswFinish(true);
                                        }
                                    }
                                });
                        break;
                    default:
                        break;

                }
            }
        }

    }
}
