package com.nahuo.quicksale;

import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.baidu.mobstat.StatService;
import com.nahuo.library.controls.LightAlertDialog;
import com.nahuo.library.controls.LoadingDialog;
import com.nahuo.quicksale.api.PaymentAPI;
import com.nahuo.quicksale.common.Const.PasswordExtra;
import com.nahuo.quicksale.common.Const.PasswordType;
import com.nahuo.quicksale.common.SpManager;
import com.nahuo.quicksale.provider.UserInfoProvider;

/**
 * Description:填写支付密码弹窗
 * 2014-7-31 上午9:39:43
 * 
 * @author ZZB
 */
public class PayPswDlgFragment extends DialogFragment implements View.OnClickListener {

    private Context mContext;
    private TextView mTvForgetPsw;
    public EditText mEtPayPsw;
    private View mBtnCancle, mBtnConfirm;

    private Listener mListener;
    protected LoadingDialog mDialog;

    public static interface Listener {
        /** 提现成功 */
        public void onPswValidated();
    }

    public static PayPswDlgFragment newInstance() {
        PayPswDlgFragment f = new PayPswDlgFragment();
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Dialog);
        mContext = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dlg_pay_psw, container, false);
        initView(view);
        return view;
    }

    private void initView(View contentView) {
        mDialog = new LoadingDialog(mContext);
        mTvForgetPsw = (TextView) contentView.findViewById(R.id.tv_forget_paypsw);
        mTvForgetPsw.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
        mTvForgetPsw.setOnClickListener(this);
        mEtPayPsw = (EditText) contentView.findViewById(R.id.et_pay_psw);
        mEtPayPsw.requestFocus();
        mBtnCancle = contentView.findViewById(R.id.btn_cancle);
        mBtnConfirm = contentView.findViewById(R.id.btn_confirm);
        mBtnCancle.setOnClickListener(this);
        mBtnConfirm.setOnClickListener(this);
    }

    /**
     * Description:提交支付密码
     * 2014-7-31 上午10:17:42
     * 
     * @author ZZB
     */
    private void onConfirmClick() {
        String psw = mEtPayPsw.getText().toString();
        if (TextUtils.isEmpty(psw)) {
            mEtPayPsw.setError(getError("密码不能为空"));
            return;
        } else if (psw.length() < 6) {
            mEtPayPsw.setError(getError("密码不能小于6位数"));
            return;
        }
        new AsyncTask<Void, Void, Object>() {

            @Override
            protected void onPreExecute() {
                mDialog.start("验证支付密码中...");
            }

            @Override
            protected Object doInBackground(Void... params) {
                try {
                    PaymentAPI.validatePayPsw(mContext, mEtPayPsw.getText().toString());
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
                }else{
                    if (mListener != null) {
                        mListener.onPswValidated();
                    }
                }
                dismiss();

            }

        }.execute();
    }

    private Spanned getError(String error) {
        return Html.fromHtml("<font color='black'>" + error + "</font>");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.tv_forget_paypsw://重置支付密码
            boolean set = UserInfoProvider.hasSetSafeQuestion(mContext, SpManager.getUserId(mContext));
            if (set) {
                Intent intent = new Intent(mContext, ForgetPswActivity.class);
                intent.putExtra(PasswordExtra.EXTRA_PSW_TYPE, PasswordType.RESET_PAYMENT);
                startActivity(intent);
            }else if(!set){
                Builder builder = LightAlertDialog.Builder.create(mContext);
                builder.setTitle("提示").setMessage("您还没有设置密码问题").setNegativeButton("取消", null)
                        .setPositiveButton("去设置", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(mContext, SetSafeQuestionsActivity.class);
                                startActivity(intent);
                            }
                        });
                builder.show();
            }
           
            break;
        case R.id.btn_cancle:
            dismiss();
            break;
        case R.id.btn_confirm:
            onConfirmClick();
            break;
        }
    }

    public void setListener(Listener listener) {
        mListener = listener;
    }

    public void onResume() {
        super.onResume();
        StatService.onResume(this);
    }

    public void onPause() {
        super.onPause();
        StatService.onPause(this);
    }
}
