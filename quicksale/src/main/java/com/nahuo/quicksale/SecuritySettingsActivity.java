package com.nahuo.quicksale;

import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.baidu.mobstat.StatService;
import com.nahuo.library.controls.LightAlertDialog;
import com.nahuo.library.controls.LoadingDialog;
import com.nahuo.library.controls.SingleLineItem;
import com.nahuo.quicksale.activity.BankAuthActivity;
import com.nahuo.quicksale.api.AccountAPI;
import com.nahuo.quicksale.base.BaseAppCompatActivity;
import com.nahuo.quicksale.common.Const;
import com.nahuo.quicksale.common.Const.PasswordExtra;
import com.nahuo.quicksale.common.Const.PasswordType;
import com.nahuo.quicksale.common.SpManager;
import com.nahuo.quicksale.eventbus.BusEvent;
import com.nahuo.quicksale.oldermodel.json.JAuthInfo;
import com.nahuo.quicksale.oldermodel.json.JBankInfo;
import com.nahuo.quicksale.provider.UserInfoProvider;

import de.greenrobot.event.EventBus;

import static com.nahuo.quicksale.eventbus.EventBusId.SECURITYSETTINGSACTIVITY_RESH;
import static com.nahuo.quicksale.provider.UserInfoProvider.getBankState;

public class SecuritySettingsActivity extends BaseAppCompatActivity implements View.OnClickListener {

    public static final String EXTRA_STATU = "EXTRA_STATU";
    private static final int REQUEST_BIND_PHONE = 1;
    private static final int REQUEST_SET_AUTH = 2;
    private static final int REQUEST_BIND_BANK = 3;
    private static final int REQUEST_SET_PSW = 4;
    private static final int REQUEST_SET_SAFEQ = 5;

    private SingleLineItem mBindPhone;
    private SingleLineItem mIdentityAuth;
    private SingleLineItem mBankCard;
    private SingleLineItem mPayPsw;
    private SingleLineItem mSafeQuestion;
    public Context mContext = this;
    public LoadingDialog mDialog;
    private JAuthInfo mAuthInfo;
    private JBankInfo mBankInfo;
    //加载用户信息的标记
    private boolean mLoadAuth = true;
    //加载银行信息的标记
    private boolean mLoadBank = true;
    private EventBus mEventBus = EventBus.getDefault();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_security_settings);
        //   getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.layout_titlebar_default);
        if (!mEventBus.isRegistered(this))
        mEventBus.registerSticky(this);
        initView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mEventBus != null) {
            if (mEventBus.isRegistered(this))
                mEventBus.unregister(this);
        }
    }

    public void onEventMainThread(BusEvent event) {
        switch (event.id) {
            case SECURITYSETTINGSACTIVITY_RESH:
                reReData();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        updateItem();
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
        mDialog = new LoadingDialog(mContext);
        initTitleBar();
        initItems();
    }

    private void initItems() {

        mIdentityAuth = (SingleLineItem) findViewById(R.id.security_authentication);
        mBankCard = (SingleLineItem) findViewById(R.id.security_withdraw_bank_card);
        mPayPsw = (SingleLineItem) findViewById(R.id.security_pay_psw);
        mSafeQuestion = (SingleLineItem) findViewById(R.id.security_safe_question);
        mBindPhone = (SingleLineItem) findViewById(R.id.security_bind_phone);

        updateItem();
        //身份验证状态
//        int userId = SpManager.getUserId(this);
//        boolean authed = UserInfoProvider.hasIdentityAuth(this, userId);
//        String authState = UserInfoProvider.getIdentityAuthState(this, userId);
//        authState = authed ? authState : "加载中";
        String authState = "加载中";
        mIdentityAuth.setRightText(authState);
//        JAuthInfo cacheAuthInfo = UserInfoProvider.getAuthInfo(mContext, userId);
//        if (!authed
//                || cacheAuthInfo == null || (cacheAuthInfo != null && !cacheAuthInfo.getStatu().equals(
//                        Const.IDAuthState.AUTH_PASSED))) {
//            mLoadAuth = true;
//        } else {// 认证过的，使用缓存
//            mAuthInfo = cacheAuthInfo;
//        }

        //银行验证状态
//        boolean bankAuthed = UserInfoProvider.getBankState(mContext, userId).equals(Const.BankState.AUTH_PASSED);
//        String bankState = UserInfoProvider.getBankState(this, userId);
//        bankState = bankAuthed ? bankState : "加载中";
        String bankState = "加载中";
        mBankCard.setRightText(bankState);
//        JBankInfo cacheBankInfo = UserInfoProvider.getBankInfo(mContext, userId);
//        if(!bankAuthed || cacheBankInfo == null ||(cacheBankInfo != null && !cacheBankInfo.getStatu().equals(Const.BankState.AUTH_PASSED))){
//            mLoadBank = true;
//        }else{
//            mBankInfo = cacheBankInfo;
//        }
//        if(mLoadAuth || mLoadBank){
        new Task().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
//        }

    }

    private void reReData() {
        updateItem();
        //身份验证状态
//        int userId = SpManager.getUserId(this);
//        boolean authed = UserInfoProvider.hasIdentityAuth(this, userId);
//        String authState = UserInfoProvider.getIdentityAuthState(this, userId);
//        authState = authed ? authState : "加载中";
        String authState = "加载中";
        mIdentityAuth.setRightText(authState);
//        JAuthInfo cacheAuthInfo = UserInfoProvider.getAuthInfo(mContext, userId);
//        if (!authed
//                || cacheAuthInfo == null || (cacheAuthInfo != null && !cacheAuthInfo.getStatu().equals(
//                        Const.IDAuthState.AUTH_PASSED))) {
//            mLoadAuth = true;
//        } else {// 认证过的，使用缓存
//            mAuthInfo = cacheAuthInfo;
//        }

        //银行验证状态
//        boolean bankAuthed = UserInfoProvider.getBankState(mContext, userId).equals(Const.BankState.AUTH_PASSED);
//        String bankState = UserInfoProvider.getBankState(this, userId);
//        bankState = bankAuthed ? bankState : "加载中";
        String bankState = "加载中";
        mBankCard.setRightText(bankState);
//        JBankInfo cacheBankInfo = UserInfoProvider.getBankInfo(mContext, userId);
//        if(!bankAuthed || cacheBankInfo == null ||(cacheBankInfo != null && !cacheBankInfo.getStatu().equals(Const.BankState.AUTH_PASSED))){
//            mLoadBank = true;
//        }else{
//            mBankInfo = cacheBankInfo;
//        }
//        if(mLoadAuth || mLoadBank){
        new Task().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private void updateItem() {
        int userId = SpManager.getUserId(mContext);
        mBindPhone.setRightText(UserInfoProvider.hasBindPhone(this, userId) ? "已绑定" : "未绑定");
        mBankCard.setRightText(getBankState(mContext, userId));
        mPayPsw.setRightText(UserInfoProvider.hasSetPayPsw(this, userId) ? "已设置" : "未设置");
        mSafeQuestion.setRightText(UserInfoProvider.hasSetSafeQuestion(this, userId) ? "已设置"
                : "未设置");
        String authState = UserInfoProvider.getIdentityAuthState(this, userId);
        mIdentityAuth.setRightText(authState);

    }

    private void initTitleBar() {
        TextView tvTitle = (TextView) findViewById(R.id.titlebar_tvTitle);
        Button btnLeft = (Button) findViewById(R.id.titlebar_btnLeft);

        tvTitle.setText("安全设置");
        btnLeft.setText(R.string.titlebar_btnBack);
        btnLeft.setVisibility(View.VISIBLE);
        btnLeft.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.security_pay_psw:// 支付密码
                onPayPswClick();
                break;
            case R.id.security_authentication:// 身份验证
                boolean bindPhone = UserInfoProvider.hasBindPhone(mContext, SpManager.getUserId(this));
                if (!bindPhone) {//没绑定手机
                    bindPhoneDialog();
                    return;
                }
                int userId = SpManager.getUserId(this);
                String authState = UserInfoProvider.getIdentityAuthState(this, userId);
                if (authState.equals(Const.IDAuthState.NOT_COMMIT)) {
                    Intent intent = new Intent(this, IdentityBankActivity.class);
                    startActivityForResult(intent, REQUEST_SET_AUTH);
                } else {
                    Intent intent = new Intent(this, IdentityAuthActivity.class);
                    startActivityForResult(intent, REQUEST_SET_AUTH);
                }
                break;
            case R.id.security_safe_question:// 安全问题
                onSetSafeQClick();

                break;
            case R.id.security_withdraw_bank_card:// 银行卡提现
                onWithdrawClick();
                break;
            case R.id.titlebar_btnLeft:
                finish();
                break;
            case R.id.security_bind_phone:// 绑定手机
                Intent bindPhoneIntent = new Intent(this, BindPhoneActivity.class);
                startActivityForResult(bindPhoneIntent, REQUEST_BIND_PHONE);
                break;
        }
    }

    /**
     * @description 设置安全问题
     * @created 2014-9-18 下午12:06:28
     * @author ZZB
     */
    private void onSetSafeQClick() {
        int userId = SpManager.getUserId(mContext);
        boolean setPayPsw = UserInfoProvider.hasSetPayPsw(mContext, userId);
        boolean bindPhone = UserInfoProvider.hasBindPhone(mContext, userId);
        if (!bindPhone) {//没绑定手机
            bindPhoneDialog();
        } else if (setPayPsw) {// 已设置支付密码才可以设置安全问题
            Intent intent = new Intent(mContext, SetSafeQuestionsActivity.class);
            startActivityForResult(intent, REQUEST_SET_PSW);
        } else {
            Builder builder = LightAlertDialog.Builder.create(mContext);
            builder.setTitle("提示").setMessage("您还未设置支付密码")
                    .setPositiveButton("去设置", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(mContext, SetPswActivity1.class);
                            intent.putExtra(PasswordExtra.EXTRA_PSW_TYPE, PasswordType.PAYMENT);
                            startActivityForResult(intent, REQUEST_SET_PSW);
                        }
                    }).setNegativeButton("取消", null);
            builder.show();
        }

    }

    /**
     * @description 银行卡绑定
     * @created 2014-9-15 下午4:32:27
     * @author ZZB
     */
    private void onWithdrawClick() {
        boolean bindPhone = UserInfoProvider.hasBindPhone(mContext, SpManager.getUserId(this));
        if (!bindPhone) {//没绑定手机
            bindPhoneDialog();
            return;
        }
        Builder builder = LightAlertDialog.Builder.create(this);
        builder.setTitle("提示").setNegativeButton("取消", null);// .setMessage(msg).setPositiveButton(btnText,
        int userId = SpManager.getUserId(this);
        String authState = UserInfoProvider.getIdentityAuthState(this, userId);
        String bankState = UserInfoProvider.getBankState(this, userId);
        if (authState.equals(Const.IDAuthState.AUTH_PASSED)) {
            if (bankState.equals(Const.IDAuthState.REJECT) || bankState.equals(Const.IDAuthState.NOT_COMMIT)
                    || bankState.equals(Const.IDAuthState.AUTH_PASSED)
                    || bankState.equals(Const.IDAuthState.RECHECK)) {
                //银行卡驳回没提交可以修改
                Intent intent = new Intent(mContext, BindCard2Acivity.class);
                startActivity(intent);
            } else {
                Intent intent = new Intent(mContext, BankAuthActivity.class);
                startActivity(intent);
            }
        } else if (authState.equals(Const.IDAuthState.NOT_COMMIT)) {

            builder.setMessage("你的身份验证未提交，验证完成即可绑定提现银行卡");
            builder.setPositiveButton("验证", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(mContext, IdentityBankActivity.class);
                    startActivityForResult(intent, REQUEST_SET_AUTH);
                }
            });
            builder.show();

        } else if (authState.equals(Const.IDAuthState.CHECKING)) {
            builder.setMessage("你的身份验证审核中，审核通过即可绑定提现银行卡");
            builder.setPositiveButton("查看验证", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(mContext, IdentityAuthActivity.class);
                    startActivityForResult(intent, REQUEST_SET_AUTH);
                }
            });
            builder.show();
        } else if (authState.equals(Const.IDAuthState.RECHECK)) {
            builder.setMessage("你的身份验证重审，重审通过即可绑定提现银行卡");
            builder.setPositiveButton("验证", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(mContext, IdentityAuthActivity.class);
                    startActivityForResult(intent, REQUEST_SET_AUTH);
                /*    Intent intent = new Intent(mContext, BindCard2Acivity.class);
                    startActivity(intent);*/
                }
            });
            builder.show();
        } else if (authState.equals(Const.IDAuthState.REJECT)) {
            builder.setMessage("你的身份验证驳回，重新验证通过即可绑定提现银行卡");
            builder.setPositiveButton("验证", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(mContext, IdentityAuthActivity.class);
                    startActivityForResult(intent, REQUEST_SET_AUTH);
              /*      Intent intent = new Intent(mContext, BindCard2Acivity.class);
                    startActivity(intent);*/
                }
            });
            builder.show();
        }

//
//        if (authState.equals(Const.IDAuthState.AUTH_PASSED)&&bankState.equals(Const.IDAuthState.AUTH_PASSED)) {// 审核通过
//            Intent intent = new Intent(mContext, BindCard2Acivity.class);
//            startActivityForResult(intent, REQUEST_BIND_BANK);
//        } else if (authState.equals(Const.IDAuthState.CHECKING)) {// 审核中
//            builder.setMessage("你的身份验证正在审核中，验证完成即可绑定提现银行卡");
//            builder.setPositiveButton("查看验证", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    Intent intent = new Intent(mContext, IdentityAuthActivity.class);
//                    startActivity(intent);
//                }
//            });
//            builder.show();
//        } else {
//            Intent intent = new Intent(mContext, IdentityBankActivity.class);
//            startActivityForResult(intent, REQUEST_SET_AUTH);
//         //   builder.setMessage("是否去绑定银行卡？");
////            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
////                @Override
////                public void onClick(DialogInterface dialog, int which) {
////                    /*Intent intent = new Intent(mContext, BankAuthActivity.class);
////                    startActivityForResult(intent, REQUEST_SET_AUTH);*/
////                    Intent intent = new Intent(mContext, IdentityBankActivity.class);
////                    startActivityForResult(intent, REQUEST_SET_AUTH);
////                }
////            });
////            builder.show();
//        }
    }

    /**
     * Description:点击修改支付密码
     * 2014-7-29 上午10:42:15
     *
     * @author ZZB
     */
    private void onPayPswClick() {
        boolean result = UserInfoProvider.hasSetPayPsw(this, SpManager.getUserId(this));// 是否设置支付密码
        if (!result) {// 设置支付密码页面
            boolean bindPhone = UserInfoProvider.hasBindPhone(mContext, SpManager.getUserId(mContext));
            if (bindPhone) {
                Intent intent = new Intent(this, SetPswActivity1.class);
                intent.putExtra(PasswordExtra.EXTRA_PSW_TYPE, PasswordType.PAYMENT);
                startActivityForResult(intent, REQUEST_SET_PSW);
            } else {//没有绑定手机不能设置支付密码
                bindPhoneDialog();
            }

        } else {// 修改支付密码页面
            Intent intent = new Intent(this, ChangePswActivity.class);
            intent.putExtra(PasswordExtra.EXTRA_PSW_TYPE, PasswordType.RESET_PAYMENT);
            startActivityForResult(intent, REQUEST_SET_PSW);
        }

    }

    /**
     * @description 绑定手机弹窗
     * @created 2014-9-30 上午9:41:06
     * @author ZZB
     */
    private void bindPhoneDialog() {
        Builder builder = LightAlertDialog.Builder.create(this);
        builder.setTitle("提示").setMessage("您还没有绑定手机").setNegativeButton("取消", null)
                .setPositiveButton("去绑定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(mContext, BindPhoneActivity.class);
                        startActivity(intent);
                    }
                });
        builder.show();
    }


    private class Task extends AsyncTask<Object, Void, Object> {


        @Override
        protected void onPreExecute() {
            mDialog.start("加载数据中...");
        }

        @Override
        protected Object doInBackground(Object... params) {
            try {
                if (mLoadAuth) {
                    mAuthInfo = AccountAPI.getIDAuthInfo(mContext);
                }
                if (mLoadBank) {
                    mBankInfo = AccountAPI.getBankInfo(mContext);
                }

                return mAuthInfo;
            } catch (Exception e) {
                e.printStackTrace();
                return "error:" + e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(Object result) {
            if (mDialog.isShowing()) {
                mDialog.stop();
            }
            if (result instanceof String && ((String) result).startsWith("error:")) {
                ViewHub.showLongToast(mContext, ((String) result).replace("error:", ""));
            } else {
                mIdentityAuth.setRightText(mAuthInfo.getStatu());
                mBankCard.setRightText(mBankInfo.getStatu());
            }

        }

    }

}
