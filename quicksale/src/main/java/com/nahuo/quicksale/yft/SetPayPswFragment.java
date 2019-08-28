package com.nahuo.quicksale.yft;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;

import com.nahuo.library.controls.LoadingDialog;
import com.nahuo.quicksale.R;
import com.nahuo.quicksale.ViewHub;
import com.nahuo.quicksale.api.PaymentAPI;
import com.nahuo.quicksale.common.SpManager;
import com.nahuo.quicksale.event.OnFragmentFinishListener;
import com.nahuo.quicksale.provider.UserInfoProvider;

/**
 * 2014-7-1下午3:35:01
 * Description 衣付通设置支付密码页面
 */
public class SetPayPswFragment extends Fragment {

    private View mContentView;
    private Context mContext;
    private View mConfirmBtn;
    private EditText mFirstEt;
    private EditText mSecondEt;
    private LoadingDialog loadingDialog;

    private static final String RESULT_SAME_AS_LOGIN_PSW = "RESULT_SAME_AS_LOGIN_PSW";
    private static final String RESULT_OK = "RESULT_OK";
    public static final String ARGS_LISTENER = "ARGS_LISTENER";
    private OnFragmentFinishListener mListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        mContentView = inflater.inflate(R.layout.frgm_set_pay_psw, container, false);
        mContext = getActivity();
        initViews();
        mListener = getArguments().getParcelable(ARGS_LISTENER);
        return mContentView;
    }

    private void initViews() {
        loadingDialog = new LoadingDialog(mContext);
        mFirstEt = (EditText) mContentView.findViewById(R.id.yft_et_paypsw);
        mSecondEt = (EditText) mContentView.findViewById(R.id.yft_et_confirm_paypsw);
        mConfirmBtn = mContentView.findViewById(R.id.yft_btn_confirm);
        mFirstEt.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        mConfirmBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String firstPsw = mFirstEt.getText().toString();
               // String secondPsw = mSecondEt.getText().toString();
                if (validateFirstPsw(firstPsw)){
                    new SetPayPswTask().execute(firstPsw);
                }
//                if (validatePsw(firstPsw, secondPsw)) {
//                    new SetPayPswTask().execute(firstPsw);
//                } else {
//                    return;
//                }
            }
        });
    }
    private boolean validateFirstPsw(String firstPsw) {
        if (TextUtils.isEmpty(firstPsw)) {
            ViewHub.setEditError(mFirstEt, "密码不能为空");
            return false;
        } else if (firstPsw.length() < 6) {
            ViewHub.setEditError(mFirstEt, "密码长度至少为6位数");
            return false;
        }
        return true;
    }
    private boolean validatePsw(String firstPsw, String secondPsw) {
        if (TextUtils.isEmpty(firstPsw)) {
            ViewHub.setEditError(mFirstEt, "密码不能为空");
            return false;
        } else if (firstPsw.length() < 6) {
            ViewHub.setEditError(mFirstEt, "密码长度至少为6位数");
            return false;
        }

        if (TextUtils.isEmpty(secondPsw)) {
            ViewHub.setEditError(mSecondEt, "密码不能为空");
            return false;
        } else if (!firstPsw.equals(secondPsw)) {
            ViewHub.setEditError(mSecondEt, "两次输入密码不一致");
            return false;
        }
        return true;
    }

    private class SetPayPswTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loadingDialog.start(getString(R.string.yft_setting_paypsw));
        }

        @Override
        protected String doInBackground(String... params) {

            try {
                boolean sameAsLoginPsw = false;
                if (sameAsLoginPsw) {
                    return RESULT_SAME_AS_LOGIN_PSW;
                } else {

                    PaymentAPI.setPayPsw(mContext, mFirstEt.getText().toString(),
                            UserInfoProvider.getBindPhone(mContext, SpManager.getUserId(mContext)));

                    return RESULT_OK;
                }
            } catch (Exception e) {
                e.printStackTrace();
                return "error:" + e.getMessage();
            }
        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (loadingDialog.isShowing()) {
                loadingDialog.stop();
            }
            if (result instanceof String && ((String) result).startsWith("error:")) {
                ViewHub.showLongToast(mContext, ((String) result).replace("error:", ""));
            } else {
                if (RESULT_OK.equals(result)) {// 设置支付密码成功
                    ViewHub.showLongToast(mContext, getString(R.string.yft_set_paypsw_suc));
                    UserInfoProvider.setHasSetPayPsw(mContext, SpManager.getUserId(mContext));
                    UserInfoProvider.setHasPayPassword(mContext,1);
                    if (mListener != null) {
                        mListener.onFinish(SetPayPswFragment.class);
                    }
                } else if (RESULT_SAME_AS_LOGIN_PSW.equals(result)) {
                    ViewHub.showOkDialog(mContext, "提示", getString(R.string.yft_paypsw_same_as_loginpsw), "OK");
                }
            }

        }

    }

}
