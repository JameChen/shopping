package com.nahuo.quicksale.yft;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.nahuo.library.controls.LoadingDialog;
import com.nahuo.library.helper.FunctionHelper;
import com.nahuo.quicksale.R;
import com.nahuo.quicksale.ViewHub;
import com.nahuo.quicksale.api.PaymentAPI;
import com.nahuo.quicksale.event.OnFragmentFinishListener;
import com.nahuo.quicksale.task.GetSmsKeyTask;
import com.nahuo.quicksale.task.GetSmsKeyTask.CallBack;

/**
 * 2014-7-1下午3:34:45
 * Description 衣付通绑定手机号码页面
 */
public class BindPhoneFragment extends Fragment {

    private static final String TAG = BindPhoneFragment.class.getSimpleName();
    private Context mContext;
    private View mContentView;
    private Button mSmsKeyBtn;
    private EditText mEtPhoneNum;
    private EditText mEtSmsKey;
    private TextView mGetSmsKeyTips;
    private View mBtnNext;
    private LoadingDialog mLoadingDialog;
    private OnFragmentFinishListener mListener;
    public static final String ARGS_LISTENER = "ARGS_LISTENER";
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        mContext = getActivity();
        mContentView = inflater.inflate(R.layout.frgm_bind_phone, container, false);
        initView();
        mListener = getArguments().getParcelable(ARGS_LISTENER);
        return mContentView;
    }

    private void initView() {
        mLoadingDialog = new LoadingDialog(mContext);
        mGetSmsKeyTips = (TextView) mContentView.findViewById(R.id.sms_key_tips);
        initGetSmsBtn();
        initEtPhoneNum();
        initEtSmsKey();
        initNextBtn();

    }

    private void initNextBtn() {
        mBtnNext = mContentView.findViewById(R.id.btn_next);

        mBtnNext.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                final String verifyCode = mEtSmsKey.getText().toString();
                final String phoneNo = mEtPhoneNum.getText().toString();
                boolean validVerifyCode = FunctionHelper.isVerifyCode(verifyCode);

                if (!validatePhoneNo()) {
                    return;
                }
                if (validVerifyCode) {
                    new BindPhoneTask().execute(phoneNo, verifyCode);
                } else {
                    ViewHub.setEditError(mEtSmsKey, "请输入6位数字的验证码");
                }

            }
        });
    }

    /**
     * @description 绑定手机任务
     * @created 2014-9-2 上午11:47:25
     * @author ZZB
     */
    private class BindPhoneTask extends AsyncTask<Object, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (!FunctionHelper.CheckNetworkOnline(mContext)) {
                return;
            }
            mLoadingDialog.start("绑定手机中...");
        }

        @Override
        protected String doInBackground(Object... params) {
            String phoneNo = (String) params[0];
            String verifyCode = (String) params[1];

            try {
                PaymentAPI.bindPhone(mContext, phoneNo, verifyCode);
                return phoneNo;
            } catch (Exception e) {
                e.printStackTrace();
                return "error:" + e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String result) {
            if (result instanceof String && ((String) result).startsWith("error:")) {
                ViewHub.showShortToast(mContext, ((String) result).replace("error:", ""));
            } else {
//                UserInfoProvider.setBindPhone(mContext, SpManager.getUserId(mContext), result);
                mListener.onFinish(BindPhoneFragment.class);
            }

            if (mLoadingDialog.isShowing()) {
                mLoadingDialog.stop();
            }
        }
    }

    /**
     * Description:验证码发送成功，修改提示信息 2014-7-2上午11:13:41
     */
    private void handleGetSmsKeyTips(boolean success) {
        String phoneNum = mEtPhoneNum.getText().toString();
        if (FunctionHelper.isPhoneNo(phoneNum) && success) {
            String phoneTemp = phoneNum.substring(0, 3) + "****"
                    + phoneNum.substring(7);
            String desc = String.format(
                    getString(R.string.forgotpwd_tvSmsKeyDesc_text), phoneTemp);
            SpannableStringBuilder strBuilder = new SpannableStringBuilder(desc);
            // 设置字体颜色
            strBuilder.setSpan(
                    new ForegroundColorSpan(getResources().getColor(
                            R.color.pink)), 9, 9 + 11,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            mGetSmsKeyTips.setText(strBuilder);
        } else {
            mGetSmsKeyTips.setText("获取验证码失败，请重试");
        }

    }

    private void initEtSmsKey() {
        mEtSmsKey = (EditText) mContentView.findViewById(R.id.et_sms_key);
        mEtSmsKey.setInputType(EditorInfo.TYPE_CLASS_PHONE);
    }

    private void initEtPhoneNum() {
        mEtPhoneNum = (EditText) mContentView.findViewById(R.id.et_phone_num);

    }

    private void initGetSmsBtn() {
        mSmsKeyBtn = (Button) mContentView.findViewById(R.id.btn_sms_key);
        final CountDownTimer timer = new CountDownTimer(60000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mSmsKeyBtn.setText("(" + millisUntilFinished / 1000 + ")重新获取");
                mSmsKeyBtn.setClickable(false);
            }

            @Override
            public void onFinish() {
                mSmsKeyBtn.setText("重新获取");
                mSmsKeyBtn.setClickable(true);
            }
        };
        mSmsKeyBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean validate = validatePhoneNo();
                if(!validate){
                    return;
                }
                String phoneNo = mEtPhoneNum.getText().toString();
                final GetSmsKeyTask task = new GetSmsKeyTask(mContext, phoneNo, GetSmsKeyTask.TYPE_BIND_PHONE);
                task.setCallBack(new CallBack() {
                    @Override
                    public void beforeGettingSmsKey() {
                        mLoadingDialog.start(getString(R.string.userreg_getSmsKey_loading));
                    }

                    @Override
                    public void afterGettingSmsKey(Object result) {
                        mLoadingDialog.stop();
                        // 验证码获取成功
                        if (result.equals("OK")) {
                            timer.start();
                            ViewHub.showOkDialog(mContext, "提示", getString(R.string.forgotpwd_getSmsKey_success), "OK");
                            handleGetSmsKeyTips(true);
                        }else{
                            ViewHub.showOkDialog(mContext, "提示", result.toString(), "OK");
                        }
                    }
                });
                task.execute(phoneNo);
            }
        });
    }

    protected boolean validatePhoneNo() {
        String phone = mEtPhoneNum.getText().toString();
        boolean result = (phone.length()==11);//FunctionHelper.isPhoneNo(phone);
        if(!result){
            ViewHub.setEditError(mEtPhoneNum, "请输入正确的手机号码");
        }
        return result;
    }

    
}
