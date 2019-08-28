package com.nahuo.quicksale;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nahuo.library.controls.LoadingDialog;
import com.nahuo.library.helper.FunctionHelper;
import com.nahuo.quicksale.api.AccountAPI;
import com.nahuo.quicksale.oldermodel.FindPwdModel;
import com.nahuo.quicksale.wxapi.WXEntryActivity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ForgotPwdActivity extends BaseSlideBackActivity implements OnClickListener {

    private ForgotPwdActivity vThis = this;

    private LoadingDialog loadingDialog;
    private Button btnLeft, btnRight, btnNext1, btnNext2, btnGetSmsKey,
            btnResetPwd, btnGotoLogin;
    private TextView tvTitle, tvSmsKeyDesc, tvStep1, tvStep2, tv_speech_verification;
    private ImageView iconStep1, iconStep2, toStep2;
    private View stepIconView, firstView, secondView, thirdView, finishView;
    private EditText edtPhoneNo, edtSmsKey, edtPwd, edtSecondPwd;
    private Step mCurrentStep;
    private FindPwdModel mFindPwdModel = new FindPwdModel();

    // 找回密码步骤
    private enum Step {
        FIRST, SECOND, THIRD, FINISH
    }

    private WaitTimer waitTimer;
    private GetSmsKeyTask getSmsKeyTask;
    private FindPwdTask findPwdTask;
    private int smstype = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      //  requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);// 设置自定义标题栏
        setContentView(R.layout.activity_forgotpwd);
//        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
//                R.layout.layout_titlebar_default);// 更换自定义标题栏布局
        initView();
    }


    // 判断手机格式是否正确
    public boolean isMobileNO(String mobiles) {
        if (TextUtils.isEmpty(mobiles)) {
            return false;
        }
        Pattern p = Pattern
                .compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }

    /**
     * 初始化视图
     */
    private void initView() {
        loadingDialog = new LoadingDialog(vThis);
        // 标题栏
        btnLeft = (Button) findViewById(R.id.titlebar_btnLeft);
        btnRight = (Button) findViewById(R.id.titlebar_btnRight);
        tvTitle = (TextView) findViewById(R.id.titlebar_tvTitle);
        tv_speech_verification = (TextView) findViewById(R.id.tv_speech_verification);
        tv_speech_verification.setOnClickListener(this);
        btnLeft.setVisibility(View.VISIBLE);
        btnRight.setVisibility(View.GONE);
        tvTitle.setText(R.string.title_activity_forgotpwd);
        // 步骤导航视图
        stepIconView = findViewById(R.id.forgotpwd_stepIconView);
        iconStep1 = (ImageView) findViewById(R.id.forgotpwd_icon_step1);
        iconStep2 = (ImageView) findViewById(R.id.forgotpwd_icon_step2);
        toStep2 = (ImageView) findViewById(R.id.forgotpwd_next_toStep2);
        tvStep1 = (TextView) findViewById(R.id.forgotpwd_tvStep1);
        tvStep2 = (TextView) findViewById(R.id.forgotpwd_tvStep2);
        // 功能操作视图
        firstView = findViewById(R.id.forgotpwd_firstView);
        secondView = findViewById(R.id.forgotpwd_secondView);
        thirdView = findViewById(R.id.forgotpwd_thirdView);
        finishView = findViewById(R.id.forgotpwd_finishView);
        tvSmsKeyDesc = (TextView) findViewById(R.id.forgotpwd_tvSmsKeyDesc);
        edtPhoneNo = (EditText) findViewById(R.id.forgotpwd_edtPhoneNo);
        edtSmsKey = (EditText) findViewById(R.id.forgotpwd_edtSmsKey);
        edtPwd = (EditText) findViewById(R.id.forgotpwd_edtPwd);
        edtSecondPwd = (EditText) findViewById(R.id.forgotpwd_edtSecondPwd);
        btnNext1 = (Button) findViewById(R.id.forgotpwd_btnNext1);
        btnNext2 = (Button) findViewById(R.id.forgotpwd_btnNext2);
        btnGetSmsKey = (Button) findViewById(R.id.forgotpwd_btnGetSmsKey);
        btnResetPwd = (Button) findViewById(R.id.forgotpwd_btnResetPwd);
        btnGotoLogin = (Button) findViewById(R.id.forgotpwd_btnGotoLogin);

        // 获取窗体传值(手机号)
        Intent intent = getIntent();
        String phoneNo = intent.getStringExtra(LoginFragment.PHONENO);
        // 如果不是手机的话
        if (isMobileNO(phoneNo)) {
            edtPhoneNo.setText(phoneNo);
        }

        btnLeft.setOnClickListener(this);
        btnNext1.setOnClickListener(this);
        btnNext2.setOnClickListener(this);
        btnGetSmsKey.setOnClickListener(this);
        btnResetPwd.setOnClickListener(this);
        btnGotoLogin.setOnClickListener(this);

        mCurrentStep = Step.FIRST;
        changeView(mCurrentStep);
    }

    /**
     * 改变界面视图
     */
    private void changeView(Step step) {
        switch (step) {
            case FIRST:
                btnLeft.setText(R.string.titlebar_btnBack);
                stepIconView.setVisibility(View.VISIBLE);
                iconStep1.setImageResource(R.drawable.step1);
                iconStep2.setImageResource(R.drawable.step2_unok);
                toStep2.setImageResource(R.drawable.next_unok);
                tvStep1.setTextColor(getResources().getColor(R.color.lightblack));
                tvStep2.setTextColor(getResources().getColor(R.color.gray));
                break;
            case SECOND:
                btnLeft.setText(R.string.titlebar_btnBack);
                stepIconView.setVisibility(View.VISIBLE);
                iconStep1.setImageResource(R.drawable.step1);
                iconStep2.setImageResource(R.drawable.step2_unok);
                toStep2.setImageResource(R.drawable.next_ok);
                tvStep1.setTextColor(getResources().getColor(R.color.pink));
                tvStep2.setTextColor(getResources().getColor(R.color.gray));
                break;
            case THIRD:
                btnLeft.setText(R.string.titlebar_btnBack);
                stepIconView.setVisibility(View.VISIBLE);
                iconStep1.setImageResource(R.drawable.step1);
                iconStep2.setImageResource(R.drawable.step2);
                toStep2.setImageResource(R.drawable.next_ok);
                tvStep1.setTextColor(getResources().getColor(R.color.pink));
                tvStep2.setTextColor(getResources().getColor(R.color.lightblack));
                break;
            case FINISH:
                btnLeft.setText(R.string.titlebar_btnBack);
                stepIconView.setVisibility(View.GONE);
                break;
        }

        changeContentView(step);
    }

    private void changeContentView(Step step) {
        if (step == Step.SECOND) {
            String phone_temp = mFindPwdModel.getPhoneNo().substring(0, 3)
                    + "****" + mFindPwdModel.getPhoneNo().substring(7);
            String desc = String
                    .format(getString(R.string.forgotpwd_tvSmsKeyDesc_text),
                            phone_temp);
            SpannableStringBuilder strBuilder = new SpannableStringBuilder(desc);
            // 设置字体颜色
            strBuilder.setSpan(
                    new ForegroundColorSpan(getResources().getColor(
                            R.color.pink)), 9, 9 + 11,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            tvSmsKeyDesc.setText(strBuilder);
        }
        firstView.setVisibility(step == Step.FIRST ? View.VISIBLE : View.GONE);
        secondView
                .setVisibility(step == Step.SECOND ? View.VISIBLE : View.GONE);
        thirdView.setVisibility(step == Step.THIRD ? View.VISIBLE : View.GONE);
        finishView
                .setVisibility(step == Step.FINISH ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_speech_verification:
                //语音验证
                smstype=2;
                // 验证网络
                if (!FunctionHelper.CheckNetworkOnline(vThis))
                    return;
                // 执行操作
                getSmsKeyTask = new GetSmsKeyTask();
                getSmsKeyTask.execute((Void) null);
                break;
            case R.id.titlebar_btnLeft:
                finish();
                break;
            case R.id.forgotpwd_btnGetSmsKey:
                // 验证网络
                if (!FunctionHelper.CheckNetworkOnline(vThis))
                    return;
                // 执行操作
                getSmsKeyTask = new GetSmsKeyTask();
                getSmsKeyTask.execute((Void) null);
                break;
            case R.id.forgotpwd_btnNext1:
            case R.id.forgotpwd_btnNext2:
            case R.id.forgotpwd_btnResetPwd:
                // 验证用户录入
                if (!validateInput(mCurrentStep))
                    return;
                // 验证网络
                if (!FunctionHelper.CheckNetworkOnline(vThis))
                    return;
                // 执行操作
                findPwdTask = new FindPwdTask();
                findPwdTask.execute((Void) null);
                break;
            case R.id.forgotpwd_btnGotoLogin:
                Intent intent = new Intent(this, WXEntryActivity.class);
                intent.putExtra(WXEntryActivity.EXTRA_TYPE, WXEntryActivity.Type.LOGIN);
                startActivity(intent);
                break;
        }
    }

    /**
     * 验证用户录入
     */
    private boolean validateInput(Step step) {
        if (step == Step.FIRST) {
            String phoneNo = edtPhoneNo.getText().toString().trim();
            if (TextUtils.isEmpty(phoneNo)) {
                Toast.makeText(vThis, R.string.forgotpwd_edtPhoneNo_empty,
                        Toast.LENGTH_SHORT).show();
                edtPhoneNo.requestFocus();
                return false;
            } else if (!FunctionHelper.isPhoneNo(phoneNo)) {
                Toast.makeText(vThis, R.string.forgotpwd_edtPhoneNo_error,
                        Toast.LENGTH_SHORT).show();
                edtPhoneNo.requestFocus();
                return false;
            }
            mFindPwdModel.setPhoneNo(phoneNo);
        } else if (step == Step.SECOND) {
            String smsKey = edtSmsKey.getText().toString().trim();
            if (smsKey.length() < 4) {
                Toast.makeText(vThis, R.string.forgotpwd_edtSmsKey_empty,
                        Toast.LENGTH_SHORT).show();
                edtSmsKey.requestFocus();
                return false;
            }
            mFindPwdModel.setSmsKey(smsKey);
        } else if (step == Step.THIRD) {
            String pwd = edtPwd.getText().toString().trim();
            if (TextUtils.isEmpty(pwd)) {
                Toast.makeText(vThis, R.string.forgotpwd_edtPwd_empty,
                        Toast.LENGTH_SHORT).show();
                edtPwd.requestFocus();
                return false;
            } else if (pwd.length() < 4) {
                Toast.makeText(vThis, R.string.forgotpwd_edtPwd_error,
                        Toast.LENGTH_SHORT).show();
                edtPwd.requestFocus();
                return false;
            }

            String secondPwd = edtSecondPwd.getText().toString().trim();
            if (TextUtils.isEmpty(secondPwd)) {
                Toast.makeText(vThis, R.string.forgotpwd_edtSecondPwd_empty,
                        Toast.LENGTH_SHORT).show();
                edtSecondPwd.requestFocus();
                return false;
            } else if (!pwd.equals(secondPwd)) {
                Toast.makeText(vThis, R.string.forgotpwd_pwd_compare,
                        Toast.LENGTH_SHORT).show();
                edtSecondPwd.requestFocus();
                return false;
            }
            mFindPwdModel.setPwd(pwd);
        }
        return true;
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
            btnGetSmsKey.setEnabled(false);
            tv_speech_verification.setEnabled(false);
            tv_speech_verification.setTextColor(getResources().getColor(R.color.gray_yuyin));
            btnGetSmsKey.setText("(" + millisUntilFinished / 1000 + ")重新获取");
        }

        @Override
        public void onFinish() {
            btnGetSmsKey.setEnabled(true);
            tv_speech_verification.setEnabled(true);
            tv_speech_verification.setTextColor(getResources().getColor(R.color.bule_overlay));
            btnGetSmsKey.setText(R.string.forgotpwd_btnGetSmsKey_text);
        }

    }

    /**
     * 获取语音和短信验证码的异步任务
     */
    private class GetSmsKeyTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            try {
                AccountAPI.getInstance().getSmsMobileVerifyCode(
                        mFindPwdModel.getPhoneNo(), smstype);
                return "OK";
            } catch (Exception ex) {
                ex.printStackTrace();
                return ex.getMessage() == null ? "获取验证码发生异常" : ex.getMessage();
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (smstype==2){
                loadingDialog
                        .start(getString(R.string.forgotpwd_getSmsKey_yu_yin_loading));

            }else {
                loadingDialog
                        .start(getString(R.string.forgotpwd_getSmsKey_loading));
            }

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            loadingDialog.stop();
            getSmsKeyTask = null;

            // 验证码获取成功
            if (result.equals("OK")) {
                // 启动定时器
                waitTimer = new WaitTimer(60000, 1000);
                waitTimer.start();
                if (smstype==2){
                    ViewHub.showOkDialog(vThis, "提示", getString(R.string.forgotpwd_getSmsKey_success_yuyin), "OK");

                }else {
                    ViewHub.showOkDialog(vThis, "提示", getString(R.string.forgotpwd_getSmsKey_success), "OK");

                }
            } else {
                Toast.makeText(vThis, result, Toast.LENGTH_LONG).show();
            }
        }

    }

    /**
     * 找回密码：1.获取短信验证码 2.判断验证码有效性 3.重置密码
     */
    private class FindPwdTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            try {
                String result = "";
                switch (mCurrentStep) {
                    case FIRST:
                        AccountAPI.getInstance().getMobileVerifyCode2(
                                mFindPwdModel.getPhoneNo(), "", 2);
                        result = "OK";
                        break;
                    case SECOND:
                        AccountAPI.getInstance().checkMobileVerifyCode(
                                mFindPwdModel.getPhoneNo(),
                                mFindPwdModel.getSmsKey());
                        result = "OK";
                        break;
                    case THIRD:
                        AccountAPI.getInstance().resetPassword(
                                mFindPwdModel.getPhoneNo(), mFindPwdModel.getPwd(),
                                mFindPwdModel.getSmsKey());
                        result = "OK";
                        break;
                    default:
                        result = "无效操作";
                        break;
                }
                return result;
            } catch (Exception ex) {
                ex.printStackTrace();
                return ex.getMessage() == null ? "操作异常" : ex.getMessage();
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            String msg = "";
            switch (mCurrentStep) {
                case FIRST:
                    msg = getString(R.string.forgotpwd_getSmsKey_loading);
                    loadingDialog.start(msg);
                    break;
                case SECOND:
                    msg = getString(R.string.forgotpwd_validate_loading);
                    loadingDialog.start(msg);
                    break;
                case THIRD:
                    msg = getString(R.string.forgotpwd_savePwd_loading);
                    loadingDialog.start(msg);
                    break;
                default:
                    break;
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (loadingDialog.isShowing())
                loadingDialog.stop();
            findPwdTask = null;

            if (result.equals("OK")) {
                switch (mCurrentStep) {
                    case FIRST:
                        mCurrentStep = Step.SECOND;
                        changeView(mCurrentStep);
                        // 启动定时器
                        waitTimer = new WaitTimer(60000, 1000);
                        waitTimer.start();
                        break;
                    case SECOND:
                        mCurrentStep = Step.THIRD;
                        changeView(mCurrentStep);
                        break;
                    case THIRD:
                        mCurrentStep = Step.FINISH;
                        changeView(mCurrentStep);
                        break;
                    default:
                        break;
                }
            } else {
                Toast.makeText(vThis, result, Toast.LENGTH_LONG).show();
            }
        }
    }
}
