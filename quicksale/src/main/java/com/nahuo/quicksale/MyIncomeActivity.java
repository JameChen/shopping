package com.nahuo.quicksale;

import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.baidu.mobstat.StatService;
import com.nahuo.bean.TradeBean;
import com.nahuo.constant.Constant;
import com.nahuo.constant.UmengClick;
import com.nahuo.library.controls.LightAlertDialog;
import com.nahuo.library.controls.LoadingDialog;
import com.nahuo.library.helper.FunctionHelper;
import com.nahuo.quicksale.adapter.TradeAdapter;
import com.nahuo.quicksale.api.PaymentAPI;
import com.nahuo.quicksale.base.BaseAppCompatActivity;
import com.nahuo.quicksale.common.Const;
import com.nahuo.quicksale.common.SpManager;
import com.nahuo.quicksale.common.Utils;
import com.nahuo.quicksale.di.module.HttpManager;
import com.nahuo.quicksale.model.http.CommonSubscriber;
import com.nahuo.quicksale.model.http.api.PinHuoApi;
import com.nahuo.quicksale.model.http.response.PinHuoResponse;
import com.nahuo.quicksale.oldermodel.BalanceModel;
import com.nahuo.quicksale.oldermodel.json.JPayUser;
import com.nahuo.quicksale.provider.UserInfoProvider;
import com.nahuo.quicksale.util.ActivityUtil;
import com.nahuo.quicksale.util.RxUtil;
import com.nahuo.quicksale.util.UMengTestUtls;
import com.nahuo.quicksale.wxapi.WXPayEntryActivity;
import com.nahuo.quicksale.yft.YFTActivity;

import java.io.Serializable;

public class MyIncomeActivity extends BaseAppCompatActivity implements View.OnClickListener, TradeAdapter.TradeListener {

    public static final int REQUEST_RECHARGE = 1;
    public static final String TAG = MyIncomeActivity.class.getSimpleName();
    protected Context mContext = this;
    protected LoadingDialog mDialog;
    private TextView mTvBalance, mTvText;
    private BalanceModel balanceData;
    private RecyclerView recycler_view;
    private TradeAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_income);
        initView();
        getButtom4APPMyBag();
    }

    private void getButtom4APPMyBag() {
        PinHuoApi pinHuoApi = HttpManager.getInstance().getPinHuoNoCacheApi(TAG);
        addSubscribe(pinHuoApi.getButtom4APPMyBag()
                .compose(RxUtil.<PinHuoResponse<TradeBean>>rxSchedulerHelper())
                .compose(RxUtil.<TradeBean>handleResult())
                .subscribeWith(new CommonSubscriber<TradeBean>(this, true, R.string.loading) {
                    @Override
                    public void onNext(TradeBean tempOrderV2) {
                        super.onNext(tempOrderV2);
                        if (tempOrderV2 != null) {
                            if (adapter != null) {
                                adapter.setData(tempOrderV2.getButtons());
                                adapter.notifyDataSetChanged();
                            }

                        }
                    }


                }));


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_RECHARGE) {
            updateBalance();
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
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
        initData();
    }

    private void initView() {
        mDialog = new LoadingDialog(this);
        mTvBalance = (TextView) findViewById(R.id.available_money);
        mTvText = (TextView) findViewById(R.id.available_text);
        recycler_view = (RecyclerView) findViewById(R.id.recycler_view);
        recycler_view.setNestedScrollingEnabled(false);
        recycler_view.setLayoutManager(new LinearLayoutManager(this));
        adapter = new TradeAdapter(this);
        adapter.setmLister(this);
        recycler_view.setAdapter(adapter);
        initTitleBar();
    }

    private void initTitleBar() {
        TextView tvTitle = (TextView) findViewById(R.id.titlebar_tvTitle);
        Button btnLeft = (Button) findViewById(R.id.titlebar_btnLeft);

        tvTitle.setText("我的收入");
        btnLeft.setText(R.string.titlebar_btnBack);
        btnLeft.setVisibility(View.VISIBLE);
        btnLeft.setOnClickListener(this);
    }

    /**
     * Description:设置“可用余额”与“不可用余额” 2014-7-4上午9:41:42
     */
    private void initData() {


        new AsyncTask<Void, Void, Object>() {

            @Override
            protected void onPreExecute() {
                mDialog.start("加载数据中");
            }

            @Override
            protected Object doInBackground(Void... params) {
                try {
                    JPayUser user = PaymentAPI.getUserInfo(mContext);
                    balanceData = PaymentAPI.getBalance(mContext);
                    SpManager.setBALANCE_enablecashoutmoney(MyIncomeActivity.this, balanceData.getEnablecashoutmoney());
                    UserInfoProvider.cachePayUserInfo(mContext, user);

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
                    updateBalance();
                }
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private void updateBalance() {
        double balance = UserInfoProvider.getUserBalance(mContext, SpManager.getUserId(mContext));
        mTvBalance.setText("" + Utils.moneyFormat(balance));
        mTvText.setText(Html.fromHtml(balanceData.getCreditinfo()));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.titlebar_btnLeft:
                finish();
                break;
            case R.id.trade_log:// 交易记录
                toAnotherActivity(TradeLogActivity.class);
                break;
            case R.id.withdraw_log:// 提现记录
                int userId = SpManager.getUserId(getApplicationContext());
                boolean isOpen = UserInfoProvider.hasOpenedYFT(getApplicationContext(), userId);
                if (!isOpen) {
                    ViewHub.showLongToast(getApplicationContext(), "现在激活衣付通");
                    Intent yftIntent1 = new Intent(getApplicationContext(), YFTActivity.class);
                    startActivity(yftIntent1);
                    return;
                }
                toAnotherActivity(WithdrawLogActivity.class);
                break;
            case R.id.security_settings:// 安全设置
                toAnotherActivity(SecuritySettingsActivity.class);
                break;
            case R.id.online_statement:// 在线结算
                toAnotherActivity(YFTActivity.class);
                break;
            case R.id.btn_withdraw:// 提现
                UMengTestUtls.UmengOnClickEvent(this, UmengClick.Click35);
                onWithdrawClick();
                break;
            case R.id.btn_top_up:// 充值
                UMengTestUtls.UmengOnClickEvent(this, UmengClick.Click34);
                Intent intent = new Intent(this, WXPayEntryActivity.class);
                intent.putExtra(WXPayEntryActivity.EXTRA_TYPE, WXPayEntryActivity.Type.CHARGE);
                startActivityForResult(intent, REQUEST_RECHARGE);
                break;
        }
    }

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

    /**
     * Description:提现
     * 2014-7-30 下午5:23:09
     *
     * @author ZZB
     */
    private void onWithdrawClick() {
        final int userId = SpManager.getUserId(mContext);
        boolean setPswed = UserInfoProvider.hasSetPayPsw(mContext, userId);
        boolean bindCard = UserInfoProvider.hasBindBank(mContext, userId);
        boolean authed = UserInfoProvider.hasIdentityAuth(mContext, userId);
        if (FunctionHelper.isFastClick()) {
            ViewHub.showShortToast(this, "点击频繁");
            return;
        }
        if (!authed) {//没身份验证
            String authState = UserInfoProvider.getIdentityAuthState(mContext, userId);
            if (authState.equals(Const.IDAuthState.NOT_COMMIT)) {

                //startActivityForResult(intent, REQUEST_SET_AUTH);
                Builder builder = LightAlertDialog.Builder.create(this);
                builder.setTitle("提示").setMessage("你的身份验证未验证，不能提现");
                builder.setPositiveButton("提交身份验证", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(mContext, IdentityBankActivity.class);
                        startActivity(intent);
                    }
                }).setNegativeButton("取消", null);
                builder.show();
            } else {
                String mess = "";
                if (authState.equals(Const.IDAuthState.CHECKING)) {
                    mess = "你的身份验证审核中，不能提现";
                }
                if (authState.equals(Const.IDAuthState.REJECT)) {
                    mess = "你的身份验证驳回，不能提现";
                }
                if (authState.equals(Const.IDAuthState.RECHECK)) {
                    mess = "你的身份验证重审，不能提现";
                }
                Builder builder = LightAlertDialog.Builder.create(this);
                builder.setTitle("提示").setMessage(mess);
                builder.setPositiveButton("查看身份验证", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(mContext, IdentityAuthActivity.class);
                        startActivity(intent);
                    }
                }).setNegativeButton("取消", null);
                builder.show();

                // startActivityForResult(intent, REQUEST_SET_AUTH);
            }

            return;
        } else if (!bindCard) {//没绑定银行卡
            String bankState = UserInfoProvider.getBankState(this, userId);
            if (bankState.equals(Const.BankState.UNCHECK) || bankState.equals(Const.BankState.CHECKING)) {
                String mess = "";
                if (bankState.equals(Const.BankState.UNCHECK) || bankState.equals(Const.BankState.CHECKING)) {
                    mess = "您绑定的银行卡还在审核中!";
                }
                Builder builder = LightAlertDialog.Builder.create(this);
                builder.setTitle("提示").setMessage(mess);
                builder.setNegativeButton("取消", null);
                builder.show();
            } else {
                String mess = "";
                if (bankState.equals(Const.BankState.RECHECK)) {
                    mess = "您绑定的银行卡还在重审";
                }
                if (bankState.equals(Const.BankState.REJECT)) {
                    mess = "您绑定银行卡处于驳回状态，不能提现";
                }
                if (bankState.equals(Const.BankState.NOT_COMMIT)) {
                    mess = "您还未绑定银行卡，不能提现";
                }
                Builder builder = LightAlertDialog.Builder.create(this);
                builder.setTitle("提示").setMessage(mess);
                builder.setPositiveButton("去绑定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(mContext, BindCard2Acivity.class);
                        startActivity(intent);
                    }
                }).setNegativeButton("取消", null);
                builder.show();
            }
        } else if (!setPswed) {//没设置支付密码
//            Builder builder = LightAlertDialog.Builder.create(this);
//            builder.setTitle("提示").setMessage("您还未设置支付密码");
//            builder.setPositiveButton("设置支付密码", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    boolean bindPhone = UserInfoProvider.hasBindPhone(mContext, SpManager.getUserId(mContext));
//                    if (bindPhone) {
//                        Intent intent = new Intent(mContext, SetPswActivity1.class);
//                        intent.putExtra(Const.PasswordExtra.EXTRA_PSW_TYPE, Const.PasswordType.PAYMENT);
//                        startActivity(intent);
//                    } else {//没有绑定手机不能设置支付密码
//                        bindPhoneDialog();
//                    }
//                }
//            }).setNegativeButton("放弃", null);
//            builder.show();
            boolean bindPhone = UserInfoProvider.hasBindPhone(mContext, SpManager.getUserId(mContext));
            if (bindPhone) {
                toAnotherActivity(WithdrawActivity.class);
            } else {//没有绑定手机不能设置支付密码
                bindPhoneDialog();
            }
        } else {
            toAnotherActivity(WithdrawActivity.class);
        }
    }

    private void toAnotherActivity(Class<?> cls) {
        Intent intent = new Intent(this, cls);
        startActivity(intent);
    }

    @Override
    public void gotoDoWath(TradeBean.ButtonsBean item) {
        if (item != null) {
            switch (item.getAction()) {
                case Constant.TtrdeAction.PRESENT_RECORD:
                    int userId = SpManager.getUserId(getApplicationContext());
                    boolean isOpen = UserInfoProvider.hasOpenedYFT(getApplicationContext(), userId);
                    if (!isOpen) {
                        ViewHub.showLongToast(getApplicationContext(), "现在激活衣付通");
                        Intent yftIntent1 = new Intent(getApplicationContext(), YFTActivity.class);
                        startActivity(yftIntent1);
                        return;
                    }
                    toAnotherActivity(WithdrawLogActivity.class);
                    break;
                case Constant.TtrdeAction.SECURITY_SETTING:
                    ActivityUtil.switchTo(mContext, SecuritySettingsActivity.class);
                    break;
                case Constant.TtrdeAction.AFTER_SALE_SUBSIDY:
                case Constant.TtrdeAction.FREIGHT_SETTLEMENT:
                case Constant.TtrdeAction.ORDER_REFUND:
                case Constant.TtrdeAction.ORDER_TRANSACTION:
                case Constant.TtrdeAction.RECHARGE_RECORD:
                case Constant.TtrdeAction.TRADING_FLOW:
                    Intent intent = new Intent(this, TradeLogActivity.class);
                    intent.putExtra(TradeLogActivity.EXTRA_TRADE_NAME,item.getName());
                    intent.putExtra(TradeLogActivity.EXTRA_TRADE_LIST, (Serializable) item.getValue());
                    startActivity(intent);
                    break;

            }
        }
    }
}
