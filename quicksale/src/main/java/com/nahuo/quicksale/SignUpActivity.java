package com.nahuo.quicksale;

import android.animation.LayoutTransition;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.nahuo.bean.InvitatioCodeBean;
import com.nahuo.library.controls.LoadingDialog;
import com.nahuo.library.helper.FunctionHelper;
import com.nahuo.library.utils.TimeUtils;
import com.nahuo.quicksale.activity.MainNewActivity;
import com.nahuo.quicksale.api.RequestMethod;
import com.nahuo.quicksale.common.AnimUtils;
import com.nahuo.quicksale.common.InputFilterHelper;
import com.nahuo.quicksale.common.SpManager;
import com.nahuo.quicksale.customview.CancelDialog;
import com.nahuo.quicksale.di.module.HttpManager;
import com.nahuo.quicksale.model.http.CommonSubscriber;
import com.nahuo.quicksale.model.http.response.PinHuoResponse;
import com.nahuo.quicksale.mvp.BaseMVPActivity;
import com.nahuo.quicksale.mvp.RequestData;
import com.nahuo.quicksale.mvp.RequestError;
import com.nahuo.quicksale.mvp.presenter.SignUpPresenter;
import com.nahuo.quicksale.mvp.view.SignUpView;
import com.nahuo.quicksale.util.ActivityUtil;
import com.nahuo.quicksale.util.RxUtil;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class SignUpActivity extends BaseMVPActivity implements View.OnClickListener, SignUpView {
    private static final String TAG = SignUpActivity.class.getSimpleName();
    private EditText mEtPhoneNum, mEtPsw, mEtVerifyCode, mEtCardID,et_invitation_code;
    private ViewGroup mViewStep1, mViewStep2;
    private TextView mTvError, mTvStep2Error, mTvAgree, tv_speech_verification;
    private LoadingDialog mLoadingDialog;
    private SignUpPresenter mPresenter;
    private Button mBtnReGetVerifyCode;
    private CheckBox mCheckboxAgree;
    private ImageView mImage;
    private WaitTimer mWaitTimer = new WaitTimer();
    private int smstype=1;
    private View layout_invitation_code;
    private SignUpActivity Vthis=this;

    @Override
    protected void onResume() {
        super.onResume();
        Vthis=this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        Vthis=this;
        initViews();
        //加载参数
        String phone = getIntent().getStringExtra("phone");
        if (phone != null) {
            mEtPhoneNum.setText(phone);
            mEtPhoneNum.setSelection(phone.length());
        }
        showlayout_invitation_code();
    }
    private void showlayout_invitation_code(){
        addSubscribe(HttpManager.getInstance().getPinHuoNoCacheApi(TAG).getConfigIndex()
                .compose(RxUtil.<PinHuoResponse<InvitatioCodeBean>>rxSchedulerHelper())
                .compose(RxUtil.<InvitatioCodeBean>handleResult())
                .subscribeWith(new CommonSubscriber<InvitatioCodeBean>(Vthis,true,R.string.loading) {
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
    @Override
    public void onBackPressed() {
        if (mViewStep1.getVisibility() == View.VISIBLE) {
            super.onBackPressed();
        } else if (mViewStep2.getVisibility() == View.VISIBLE) {
            backToStep1();
        }

    }

    private void backToStep1() {
        mViewStep1.setVisibility(View.VISIBLE);
        mViewStep2.setVisibility(View.GONE);
        mEtVerifyCode.setText("");
        mTvStep2Error.setVisibility(View.GONE);
//        ViewHub.setTextInputLayoutError(mTilVerifyCode, "");
    }

    @Override
    protected void onStop() {
        super.onStop();
        FunctionHelper.hideSoftInput(this);
    }


    private void initViews() {
        initToolbar();
        mLoadingDialog = new LoadingDialog(this);
        layout_invitation_code=$(R.id.layout_invitation_code);
        layout_invitation_code.setVisibility(View.GONE);
        et_invitation_code=$(R.id.et_invitation_code);
        mViewStep1 = $(R.id.layout_step1);
        initLayoutTransition(mViewStep1);
        mViewStep2 = $(R.id.layout_step2);
        initLayoutTransition(mViewStep2);
        mViewStep1.setVisibility(View.VISIBLE);
        mEtPhoneNum = $(mViewStep1, R.id.et_phone_num);
        mEtPsw = $(mViewStep1, R.id.et_psw);
        mEtPsw.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        mEtPsw.invalidate();
        mEtVerifyCode = $(mViewStep2, R.id.et_sms_code);
        mTvStep2Error = $(mViewStep2, R.id.tv_error);
        mTvError = $(mViewStep1, R.id.tv_error);
        mEtCardID = $(R.id.et_cardid);
        mBtnReGetVerifyCode = $(mViewStep2, R.id.btn_reget_verify_code);
        mBtnReGetVerifyCode.setOnClickListener(this);
        mCheckboxAgree = $(R.id.cx_agreement);
        mTvAgree = $(R.id.tv_agreement);
        mTvAgree.setOnClickListener(this);
        mImage = $(R.id.im_psw);
        mImage.setImageResource(R.drawable.close_eye);
        mImage.setOnClickListener(this);
        mEtPsw.setFilters(new InputFilter[]{InputFilterHelper.noWhiteSpaceFilter()});
        mEtPhoneNum.setFilters(new InputFilter[]{InputFilterHelper.maxLengthFilter(11)});
        mEtVerifyCode.setFilters(new InputFilter[]{InputFilterHelper.maxLengthFilter(6)});
        tv_speech_verification = $(R.id.tv_speech_verification);
        tv_speech_verification.setOnClickListener(this);

    }

    /**
     * 刷新倒计时的线程
     */
    private class WaitTimer extends CountDownTimer {
        public WaitTimer() {
            super(TimeUnit.MINUTES.toMillis(1), TimeUnit.SECONDS.toMillis(1));
        }

        @Override
        public void onTick(long millisUntilFinished) {
            mBtnReGetVerifyCode.setEnabled(false);
            tv_speech_verification.setEnabled(false);
            tv_speech_verification.setTextColor(getResources().getColor(R.color.gray_yuyin));
            mBtnReGetVerifyCode.setTextColor(Color.WHITE);
            mBtnReGetVerifyCode.setText("重新获取(" + (millisUntilFinished / 1000) + ")");
        }

        @Override
        public void onFinish() {
            mBtnReGetVerifyCode.setEnabled(true);
            mBtnReGetVerifyCode.setText("重新获取");
            tv_speech_verification.setEnabled(true);
            tv_speech_verification.setTextColor(getResources().getColor(R.color.bule_overlay));
        }
    }

    private void initLayoutTransition(ViewGroup viewGroup) {
        int startDelay = getResources().getInteger(android.R.integer.config_shortAnimTime);
        LayoutTransition transition = new LayoutTransition();
//        transition.enableTransitionType(LayoutTransition.CHANGING);
        transition.setStartDelay(LayoutTransition.APPEARING, startDelay);
        transition.setStartDelay(LayoutTransition.CHANGE_APPEARING, startDelay);
        viewGroup.setLayoutTransition(transition);

    }

    private void initToolbar() {

        ((TextView) findViewById(R.id.tv_title)).setText("注册");
        Button backBtn = (Button) findViewById(R.id.titlebar_btnLeft);
        backBtn.setVisibility(View.VISIBLE);
        backBtn.setOnClickListener(this);

    }


    @Override
    protected void initPresenters() {
        super.initPresenters();
        mPresenter = new SignUpPresenter(this);
        mPresenters.add(mPresenter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_speech_verification:
                //语音验证
                smstype=2;
                mWaitTimer.cancel();
                mWaitTimer.start();
                //   mPresenter.getVerifyCode2(mEtPhoneNum.getText().toString(), mEtCardID.getText().toString());
                mPresenter.getVerifyCode3(mEtPhoneNum.getText().toString(), mEtCardID.getText().toString(),smstype);
                break;
            case R.id.titlebar_btnLeft:
                onBackPressed();
                break;
            case R.id.btn_get_sms://获取验证码
                onGoToStep2Clicked(v);
                break;
            case R.id.btn_confirm://提交验证码
                onSubmitVerifyCodeClicked(v);
                break;
            case R.id.btn_reget_verify_code://重新获取验证码
                mWaitTimer.cancel();
                mWaitTimer.start();
             //   mPresenter.getVerifyCode2(mEtPhoneNum.getText().toString(), mEtCardID.getText().toString());
                mPresenter.getVerifyCode3(mEtPhoneNum.getText().toString(), mEtCardID.getText().toString(),smstype);
                break;
            case R.id.tv_agreement://用户协议
                Intent intent = new Intent(this, WPAgreeUseActivity.class);
                startActivity(intent);
                break;
            case R.id.im_psw:
                setHideOrShow();
                break;
        }
    }

    private void setHideOrShow() {
        int length = mEtPsw.getText().length();
      //  if (length > 0) {
            if (mEtPsw.getInputType() == InputType.TYPE_TEXT_VARIATION_PASSWORD) {
                mEtPsw.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                mEtPsw.invalidate();
                mEtPsw.setSelection(length);
                mImage.setImageResource(R.drawable.close_eye);
            } else {
                mEtPsw.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                mEtPsw.invalidate();
                mEtPsw.setSelection(mEtPsw.getText().length());
                mImage.setImageResource(R.drawable.see_eye);
            }
       // }
    }

    private void onGoToStep2Clicked(View v) {
        boolean valid = validateStep1Input();
        if (valid) {
            mPresenter.getVerifyCode2(mEtPhoneNum.getText().toString(), mEtCardID.getText().toString());
        }
    }

    /**
     * 提交验证码
     *
     * @author ZZB
     * created at 2015/8/10 10:32
     */
    private void onSubmitVerifyCodeClicked(View v) {
        boolean valid = validateStep2Input();
        if (valid) {
//            mPresenter.submitVerifyCode(mEtPhoneNum.getText().toString(), mEtVerifyCode.getText().toString());
            mPresenter.signUpUser(this,mEtPhoneNum.getText().toString(), mEtPsw.getText().toString(), mEtVerifyCode.getText().toString(), mEtCardID.getText().toString(),et_invitation_code.getText().toString());
        }
    }

    private boolean validateStep1Input() {
        String phoneNo = mEtPhoneNum.getText().toString();
        String psw = mEtPsw.getText().toString();
        mTvError.setVisibility(View.GONE);
        if (isEtEmpty(mEtPhoneNum)) {
//            showError("请输入手机号码");
        } else if (!FunctionHelper.isPhoneNo(phoneNo)) {
            setTilError(mEtPhoneNum, "请输入正确的手机号码");
        } else if (isEtEmpty(mEtPsw)) {
//            showError("请输入密码");
        } else if (psw.length() < 6) {
            setTilError(mEtPsw, "密码长度不小于6位");
        } else if (!mCheckboxAgree.isChecked()) {
            //setTilError("请同意用户协议");
            new CancelDialog(this).show();
        } else {
//            mTvError.setVisibility(View.GONE);
            return true;
        }
        return false;
    }

    private boolean validateStep2Input() {
        String verifyCode = mEtVerifyCode.getText().toString();
        if (!TextUtils.isEmpty(verifyCode) && FunctionHelper.isVerifyCode(verifyCode)) {
            return true;
        } else {
            setStep2Error("验证码小于4位数字");
//            setTilError(mEtVerifyCode, "验证码为6位数字");
            return false;
        }

    }

    private void setStep2Error(String error) {
        mTvStep2Error.setVisibility(View.VISIBLE);
        mTvStep2Error.setText(error);
    }

    private void setTilError(EditText et, String error) {
//        ViewHub.setEditError(et, error);
        et.setError(error);
        AnimUtils.shake(et);
    }

    private void showError(String error) {
        mTvError.setVisibility(View.VISIBLE);
        mTvError.setText(error);
    }

    private void clearErrorView() {
        mTvError.setVisibility(View.GONE);
        mTvError.setText("");
    }

    private boolean isEtEmpty(EditText et) {
        if (TextUtils.isEmpty(et.getText())) {
            AnimUtils.shake(et);
            return true;
        } else {
            return false;
        }
    }


    @Override
    public void showLoading(RequestData requestData) {
        if (requestData != null) {
            String method = requestData.method;
            if (RequestMethod.UserMethod.NEW_GET_SIGN_UP_VERIFY_CODE.equals(method)) {
                mLoadingDialog.start("获取验证码中...");
            } else if (RequestMethod.UserMethod.USER_REGISTER2.equals(method)) {
                mLoadingDialog.start("注册用户中...");
            } else if (RequestMethod.ShopMethod.REGISTER_SHOP.equals(method)) {
                mLoadingDialog.start("注册店铺中...");
            }
        }

    }

    @Override
    public void hideLoading(RequestData requestData) {
        mLoadingDialog.stop();
    }

    @Override
    public void onGetVerifyCodeSuccess() {
        mViewStep1.setVisibility(View.GONE);
        mEtVerifyCode.requestFocus();
        mViewStep2.setVisibility(View.VISIBLE);
        mWaitTimer.cancel();
        mWaitTimer.start();
        if (smstype==2){
            ViewHub.showOkDialog(this, "提示", getString(R.string.forgotpwd_getSmsKey_success_yuyin), "OK");

        }else {
            ViewHub.showOkDialog(this, "提示", getString(R.string.forgotpwd_getSmsKey_success), "OK");

        }
    }

    @Override
    public void onGetVerifyCodeFailed(RequestError requestError) {
        showError(requestError.msg);
    }

    @Override
    public void onValidateVerifyCodeSuccess() {

    }

    @Override
    public void onValidateVerifyCodeFailed(RequestError requestError) {

    }

    @Override
    public void onSignUpUserSuccess() {
        String date = TimeUtils.dateToTimeStamp(new Date(), "yyyyMMdd");
        String shopName = "shop" + date;
        mPresenter.signUpShop(this,mEtPhoneNum.getText().toString(), shopName);
    }

    @Override
    public void onSignUpUserFailed(RequestError requestError) {
        Log.v(TAG, requestError.msg);
        if (!requestError.isServerExp) {
            String error = requestError.msg;
            setStep2Error(error);
        }
    }

    @Override
    public void onSignUpShopSuccess() {
        //注册成功
        SpManager.setIsFirstUseApp(this, FunctionHelper.GetAppVersionCode(this),false);
        SpManager.setFirstUseTime(this, TimeUtils.dateToTimeStamp(new Date(), "yyyy-MM-dd HH:mm:ss"));
        SpManager.setIs_Login(this,true);
        String codeInvite = mEtCardID.getText().toString().trim();// 邀请码
//        Intent intent = new Intent(this, MainActivity.class);
//        intent.putExtra(MainActivity.TAG_CARDID, codeInvite);
//        startActivity(intent);
        Map<String,Object> map=new HashMap<>();
        map.put(MainNewActivity.TAG_CARDID, codeInvite);
        ActivityUtil.goToMainActivity(this,map);
        finish();
    }

    @Override
    public void onSignUpShopFailed(RequestError requestError) {
        if (!requestError.isServerExp) {
            String error = requestError.msg;
            setStep2Error(error);
        }
    }
}
