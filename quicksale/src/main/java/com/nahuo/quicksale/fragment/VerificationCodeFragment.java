package com.nahuo.quicksale.fragment;


import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.nahuo.library.helper.FunctionHelper;
import com.nahuo.quicksale.R;
import com.nahuo.quicksale.ViewHub;
import com.nahuo.quicksale.base.BaseNewFragment;
import com.nahuo.quicksale.common.SpManager;
import com.nahuo.quicksale.di.module.HttpManager;
import com.nahuo.quicksale.event.OnFragmentFinishListener;
import com.nahuo.quicksale.model.http.CommonSubscriber;
import com.nahuo.quicksale.model.http.response.PinHuoResponse;
import com.nahuo.quicksale.util.RxUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * 验证码
 */
public class VerificationCodeFragment extends BaseNewFragment implements View.OnClickListener {
    public static final String ARGS_LISTENER = "ARGS_LISTENER";
    private OnFragmentFinishListener mListener;
    private TextView tvSmsKeyDesc, tv_speech_verification;
    private EditText edtSmsKey;
    private Button btnGetSmsKey, btnNext2;
    private static String TAG = VerificationCodeFragment.class.getSimpleName();
    private int smstype = 1;

    public VerificationCodeFragment() {
        // Required empty public constructor
    }

    private WaitTimer waitTimer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_verification_code, container, false);
        tvSmsKeyDesc = (TextView) view.findViewById(R.id.forgotpwd_tvSmsKeyDesc);
        edtSmsKey = (EditText) view.findViewById(R.id.forgotpwd_edtSmsKey);
        btnGetSmsKey = (Button) view.findViewById(R.id.forgotpwd_btnGetSmsKey);
        btnNext2 = (Button) view.findViewById(R.id.forgotpwd_btnNext2);
        btnNext2.setOnClickListener(this);
        btnGetSmsKey.setOnClickListener(this);
        tv_speech_verification = (TextView) view.findViewById(R.id.tv_speech_verification);
        tv_speech_verification.setOnClickListener(this);
        mListener = getArguments().getParcelable(ARGS_LISTENER);
        String phone = SpManager.getVerifyCodePhone(activity);
        String phone_temp = phone.substring(0, 3)
                + "****" + phone.substring(7);
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
        waitTimer = new WaitTimer(60000, 1000);
        waitTimer.start();
        return view;
    }

    private class WaitTimer extends CountDownTimer {

        public WaitTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            if (activity != null) {
                if (btnGetSmsKey != null) {
                    btnGetSmsKey.setEnabled(false);
                    btnGetSmsKey.setText("(" + millisUntilFinished / 1000 + ")重新获取");
                }
                if (tv_speech_verification != null) {
                    tv_speech_verification.setEnabled(false);
                    tv_speech_verification.setTextColor(getResources().getColor(R.color.gray_yuyin));
                }

            }
        }

        @Override
        public void onFinish() {
            if (activity != null) {
                if (btnGetSmsKey != null) {
                    btnGetSmsKey.setEnabled(true);
                    btnGetSmsKey.setText(R.string.forgotpwd_btnGetSmsKey_text);
                }
                if (tv_speech_verification != null) {
                    tv_speech_verification.setEnabled(true);
                    tv_speech_verification.setTextColor(getResources().getColor(R.color.bule_overlay));
                }

            }
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        if (waitTimer!=null)
            waitTimer.cancel();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (waitTimer!=null)
            waitTimer.cancel();
    }

    private void vetifyCode() {
        Map<String, Object> params = new HashMap<>();
        params.put("mobile", SpManager.getVerifyCodePhone(activity));
        params.put("code", edtSmsKey.getText().toString().trim());
        addSubscribe(HttpManager.getInstance().getPinHuoNoCacheApi(TAG)
                .checkMobileVerifyCode(params).compose(RxUtil.<PinHuoResponse<Object>>rxSchedulerHelper())
                .compose(RxUtil.handleResult()).subscribeWith(new CommonSubscriber<Object>(activity, true, R.string.forgotpwd_validate_loading) {
                    @Override
                    public void onNext(Object o) {
                        super.onNext(o);
                        SpManager.setVerifyCode(activity, edtSmsKey.getText().toString().trim());
                        if (mListener != null)
                            mListener.onFinish(VerificationCodeFragment.class);
                        if (waitTimer!=null)
                            waitTimer.cancel();
                    }
                }));
    }

    private void check() {
        int msg;
        if (smstype == 2) {
            msg = R.string.forgotpwd_getSmsKey_yu_yin_loading;

        } else {
            msg = R.string.forgotpwd_getSmsKey_loading;
        }
        Map<String, Object> params = new HashMap<>();
        params.put("mobile", SpManager.getVerifyCodePhone(activity));
        params.put("usefor", "findpassword");
        params.put("username", "");
        params.put("smstype", smstype + "");
        params.put("messageFrom", "天天拼货团");
        addSubscribe(HttpManager.getInstance().getPinHuoNoCacheApi(TAG)
                .getMobileVerifyCode2(params).compose(RxUtil.<PinHuoResponse<Object>>rxSchedulerHelper())
                .compose(RxUtil.handleResult()).subscribeWith(new CommonSubscriber<Object>(activity, true, msg) {
                    @Override
                    public void onNext(Object o) {
                        super.onNext(o);
                        waitTimer = new WaitTimer(60000, 1000);
                        waitTimer.start();
                        if (smstype == 2) {
                            ViewHub.showOkDialog(activity, "提示", getString(R.string.forgotpwd_getSmsKey_success_yuyin), "OK");

                        } else {
                            ViewHub.showOkDialog(activity, "提示", getString(R.string.forgotpwd_getSmsKey_success), "OK");

                        }
                    }
                }));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.forgotpwd_btnGetSmsKey:
                // 验证网络
                if (!FunctionHelper.CheckNetworkOnline(activity)) {
                    ViewHub.showShortToast(activity, "没有网络，请联网再尝试");
                    return;
                }
                check();
                break;
            case R.id.tv_speech_verification:
                //语音验证
                smstype = 2;
                // 验证网络
                if (!FunctionHelper.CheckNetworkOnline(activity)) {
                    ViewHub.showShortToast(activity, "没有网络，请联网再尝试");
                    return;
                }
                check();
                break;
            case R.id.forgotpwd_btnNext2:
                String smsKey = edtSmsKey.getText().toString().trim();
                if (smsKey.length() < 4) {
                    ViewHub.showShortToast(activity, R.string.forgotpwd_edtSmsKey_empty);
                    edtSmsKey.requestFocus();
                    return;
                }
                // 验证网络
                if (!FunctionHelper.CheckNetworkOnline(activity)) {
                    ViewHub.showShortToast(activity, "没有网络，请联网再尝试");
                    return;
                }
                vetifyCode();
                break;
        }
    }
}
