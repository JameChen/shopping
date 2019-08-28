package com.nahuo.quicksale;

import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.baidu.mobstat.StatService;
import com.nahuo.library.controls.LightAlertDialog;
import com.nahuo.library.controls.LoadingDialog;
import com.nahuo.quicksale.PayPswDlgFragment.Listener;
import com.nahuo.quicksale.api.PaymentAPI;
import com.nahuo.quicksale.common.Const;
import com.nahuo.quicksale.common.Const.PasswordExtra;
import com.nahuo.quicksale.common.Const.PasswordType;
import com.nahuo.quicksale.common.SpManager;
import com.nahuo.quicksale.common.Utils;
import com.nahuo.quicksale.provider.UserInfoProvider;

/**
 * Description:提现页面
 * 2014-7-31 上午10:20:51
 * 
 * @author ZZB
 */
public class WithdrawActivity extends BaseSlideBackActivity implements OnClickListener {

    private Context mContext = this;
    private View mLayoutWithdraw;
    private View mLayoutWithdrawSuc;
    private TextView mTvForgetPsw;
    private EditText mEdtMoney;
    private PayPswDlgFragment payFragment;
    private WithdrawActivity vthis=this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

      //  requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_withdraw);
      //  getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.layout_titlebar_default);

        payFragment = PayPswDlgFragment.newInstance();
        payFragment.setListener(new Listener() {
            @Override
            public void onPswValidated() {
                new Task().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }
        });

        initView();
    }

    private void initView() {
        initTitleBar();
        ((TextView)findViewById(R.id.withdraw_tips)).setText(SpManager.getBALANCE_CASHOUT_TIPS(mContext));
        mTvForgetPsw = (TextView) findViewById(R.id.tv_forget_paypsw);
        mEdtMoney = (EditText) findViewById(R.id.et_money);
        
        double canWithdrawalsMoney = SpManager.getBALANCE_enablecashoutmoney(mContext);
//        canWithdrawalsMoney = UserInfoProvider.getUserBalance(mContext, SpManager.getUserId(mContext));
//        canWithdrawalsMoney -= SpManager.getBALANCE_FEEZE_MONEY(mContext);
//        canWithdrawalsMoney -= 2;//手续费
//if (canWithdrawalsMoney<0){
//	canWithdrawalsMoney = 0.00;
//}

        String money_tips = "充值不可提现金额:¥"+ Utils.moneyFormat(SpManager.getBALANCE_FEEZE_MONEY(mContext))+",手续费每笔:¥"+Utils.moneyFormat(SpManager.getBALANCE_CASHOUT_FEE_MONEY(mContext))+"元";
        ((TextView)findViewById(R.id.withdraw_money_tips)).setText(money_tips);

        mEdtMoney.setHint("可提现金额"+Utils.moneyFormat(canWithdrawalsMoney));
        mEdtMoney.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String number = s.toString();
                String[] arr = number.split("\\.");
                int len = arr.length;
                if (len != 2) {
                    return;
                } else if (arr[1].length() > 2) {
                    number = number.substring(0, number.indexOf(".") + 3);
                    mEdtMoney.setText(number);
                    mEdtMoney.setSelection(number.length());
                }
            }
        });

        // 下划线加抗锯齿
        mTvForgetPsw.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
        mLayoutWithdraw = findViewById(R.id.layout_withdraw);
        mLayoutWithdrawSuc = findViewById(R.id.layout_withdraw_suc);
    }

    private void initTitleBar() {
        TextView tvTitle = (TextView) findViewById(R.id.titlebar_tvTitle);
        tvTitle.setText("提现");

        Button btnLeft = (Button) findViewById(R.id.titlebar_btnLeft);
        btnLeft.setText(R.string.titlebar_btnBack);
        btnLeft.setVisibility(View.VISIBLE);
        btnLeft.setOnClickListener(this);

    }

    /**
     * @description 提现
     * @time 2014-7-31 上午10:23:15
     * @author ZZB
     */
    private void onSubmitClick() {
        String moneyStr = mEdtMoney.getText().toString();
        int userId = SpManager.getUserId(mContext);
        if (TextUtils.isEmpty(moneyStr)) {
            ViewHub.setEditError(mEdtMoney, "请输入提现金额");
            return;
        }
        boolean authed = UserInfoProvider.hasIdentityAuth(mContext, userId);
        if (!authed) {
            Builder builder = LightAlertDialog.Builder.create(this);
            builder.setTitle("提示").setMessage("你的身份验证未验证或者审核中，不能提现");
            builder.setPositiveButton("提交身份验证", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    int userId = SpManager.getUserId(vthis);
                    String authState = UserInfoProvider.getIdentityAuthState(vthis, userId);
                    if(authState.equals(Const.IDAuthState.NOT_COMMIT)){
                        Intent intent = new Intent(mContext, IdentityAuthActivity.class);
                        startActivity(intent);
                    }else {
                        Intent intent2 = new Intent(mContext, IdentityBankActivity.class);
                        startActivity(intent2);
                    }
                  /*  Intent intent = new Intent(mContext, BankAuthActivity.class);
                    startActivity(intent);*/
                }
            }).setNegativeButton("取消", null);
            builder.show();
            return;
        }
        double money = Double.valueOf(mEdtMoney.getText().toString());
        double balance = UserInfoProvider.getUserBalance(mContext, userId);
        if (money > balance) {
            ViewHub.setEditError(mEdtMoney, "提现余额不足,当前余额为" + balance + "元");
        } else {
            //payFragment.show(getSupportFragmentManager(), "PayPswDlgFragment");
            new OutTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.tv_forget_paypsw:
            forgetPsw();
            break;
        case R.id.titlebar_btnLeft:
            ViewHub.hideKeyboard(this);
            finish();
            break;
        case R.id.btn_submit:
            onSubmitClick();
            break;
        }
    }

    private void forgetPsw() {
        boolean set = UserInfoProvider.hasSetSafeQuestion(mContext, SpManager.getUserId(mContext));
        if (set) {
            Intent intent = new Intent(mContext, ForgetPswActivity.class);
            intent.putExtra(PasswordExtra.EXTRA_PSW_TYPE, PasswordType.RESET_PAYMENT);
            startActivity(intent);
        }else if (!set) {
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

    }

    public void onResume() {
        super.onResume();
        StatService.onResume(this);
    }

    public void onPause() {
        super.onPause();
        StatService.onPause(this);
    }

    @Override
    public void finish() {
        super.finish();
        ViewHub.hideKeyboard(this);
    }
    private class OutTask extends AsyncTask<Void, Void, Object> {

        private LoadingDialog mDialog;

        public OutTask() {
            mDialog = new LoadingDialog(mContext);
        }

        @Override
        protected void onPreExecute() {
            mDialog.start("正在处理...");
        }

        @Override
        protected Object doInBackground(Void... params) {
            try {
                PaymentAPI.cashOut(mContext, mEdtMoney.getText().toString(), "");
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
                String error = result.toString().replace("error:", "");
                ViewHub.showOkDialog(mContext, "错误", error, "OK");
            } else {
                mLayoutWithdraw.setVisibility(View.GONE);
                mLayoutWithdrawSuc.setVisibility(View.VISIBLE);
                ViewHub.showOkDialog(mContext, "提示", "提现申请成功", "OK");
            }

        }

    }

    private class Task extends AsyncTask<Void, Void, Object> {

        private LoadingDialog mDialog;

        public Task() {
            mDialog = new LoadingDialog(mContext);
        }

        @Override
        protected void onPreExecute() {
            mDialog.start("正在处理...");
        }

        @Override
        protected Object doInBackground(Void... params) {
            try {
                PaymentAPI.cashOut(mContext, mEdtMoney.getText().toString(), payFragment.mEtPayPsw
                        .getText().toString());
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
                String error = result.toString().replace("error:", "");
                ViewHub.showOkDialog(mContext, "错误", error, "OK");
            } else {
                mLayoutWithdraw.setVisibility(View.GONE);
                mLayoutWithdrawSuc.setVisibility(View.VISIBLE);
                ViewHub.showOkDialog(mContext, "提示", "提现申请成功", "OK");
            }

        }

    }
}
