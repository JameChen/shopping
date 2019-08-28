package com.nahuo.quicksale;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.Html;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.TextView;

import com.nahuo.Dialog.CDialog2;
import com.nahuo.Dialog.CommDialog;
import com.nahuo.Dialog.PayTimeDialog;
import com.nahuo.bean.PayTimeBean;
import com.nahuo.library.controls.LightPopDialog;
import com.nahuo.library.controls.LoadingDialog;
import com.nahuo.library.helper.FunctionHelper;
import com.nahuo.library.utils.TimeUtils;
import com.nahuo.quicksale.activity.PaySucessActivity;
import com.nahuo.quicksale.api.PaymentAPI;
import com.nahuo.quicksale.app.BWApplication;
import com.nahuo.quicksale.base.TitleAppCompatActivity;
import com.nahuo.quicksale.common.Const;
import com.nahuo.quicksale.common.SpManager;
import com.nahuo.quicksale.common.StringUtils;
import com.nahuo.quicksale.common.Utils;
import com.nahuo.quicksale.di.module.HttpManager;
import com.nahuo.quicksale.eventbus.BusEvent;
import com.nahuo.quicksale.eventbus.EventBusId;
import com.nahuo.quicksale.model.http.CommonSubscriber;
import com.nahuo.quicksale.model.http.api.PinHuoApi;
import com.nahuo.quicksale.model.http.response.PinHuoResponse;
import com.nahuo.quicksale.oldermodel.json.JPayResult;
import com.nahuo.quicksale.oldermodel.json.JPayUser;
import com.nahuo.quicksale.orderdetail.BaseOrderDetailActivity;
import com.nahuo.quicksale.orderdetail.GetBuyOrderActivity;
import com.nahuo.quicksale.pay.OrderPaidChecker;
import com.nahuo.quicksale.pay.OrderPaidChecker.IOrderCheckListener;
import com.nahuo.quicksale.pay.PaymentHelper;
import com.nahuo.quicksale.pay.PaymentHelper.PaymentListener;
import com.nahuo.quicksale.pay.PaymentHelper.PaymentType;
import com.nahuo.quicksale.provider.UserInfoProvider;
import com.nahuo.quicksale.util.RxUtil;
import com.nahuo.quicksale.wxapi.WXPayEntryActivity;
import com.nahuo.quicksale.yft.YFTActivity;
import com.nahuo.quicksale.yft.YFTActivity1;
import com.tencent.mm.opensdk.constants.Build;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.util.List;

import de.greenrobot.event.EventBus;

/***
 * @author JorsonWong
 * @description 支付界面 包含余额、支付宝、微信及网银支付
 * @created 2015年4月30日 上午10:18:43
 */
public class OrderPayActivity extends TitleAppCompatActivity implements OnClickListener, CommDialog.PopDialogListener {
    private static String TAG=OrderPayActivity.class.getSimpleName();
    private Context mContext = this;
    public static final int REQUEST_RECHARGE = 1;
    public static final int REQUEST_WX_RECHARGE = 1;
    private TextView mTvPayMoney;
    private TextView mNotEnoughMoney;
    private View mLine;
    private boolean mIsShowSuccess;

    private RadioButton mRadiaYFT, mRadiaAlipay, mRadiaWechat;
    private Button mBtnActive;
    private TextView mTvAvMoney;

    // private HttpRequestHelper mRequestHelper = new HttpRequestHelper();
    private String mOrderPayId="",code="";
    private double mOrderMoney;
    private View ll_balance;
    public static final String INTENT_PAY_ORDER = "intent_order";
    /**
     * 订单ID ,用于生产预付单 int 型
     */
    public static final String INTENT_PAY_ORDER_ID = "intent_order_id";

    /**
     * 订单金额，用于显示 double 型
     */
    public static final String INTENT_PAY_MONEY = "intent_pay_money";

    /**
     * 显示订单提交成功标识，用于购物车提交后直接跳转过来的 boolean 型
     */
    public static final String INTENT_SHOW_SUBMIT_SUCCESS = "intent_show_submit_success";
    private static final int CODE_RESULT_WECHAT_PAY = 1200;

    private EventBus mEventBus = EventBus.getDefault();
    private Button btnPay;
    private int HasPayPassword = -1;
    boolean isOpen = false;
    double balance = 0;
    private CommDialog commDialog;
    private TextView mTvHH, mTvH, mTvMM, mTvM, mTvSS, mTvS, mTvF, mTvDay, mTvDayDesc,tv_hour_desc,tv_m_desc;
    private View ll_time;
    private MyCountDownTimer myCountDownTimer;
    private PayTimeDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_pay);
        BWApplication.RECHARGECODE = "";
        mOrderPayId = getIntent().getStringExtra(INTENT_PAY_ORDER_ID);
        code = getIntent().getStringExtra(INTENT_PAY_ORDER);
        mOrderMoney = getIntent().getDoubleExtra(INTENT_PAY_MONEY, 0);
        mIsShowSuccess = getIntent().getBooleanExtra(INTENT_SHOW_SUBMIT_SUCCESS, false);
        BWApplication.addActivity(this);
        mEventBus.register(this);
        //commDialog = CommDialog.getInstance(this).setDialogType(CommDialog.DialogType.D_PAY).setContent("正进入微信支付中！如您已支付，可根据您支付的情况点击下面的按钮").setLeftStr("我已支付").setRightStr("取消").setPositive(this);
        //.setContent("是否退出编辑").setLeftStr("我已支付").setRightStr("取消").setPositive(this)
        initViews();
        balance = UserInfoProvider.getUserBalance(getApplicationContext(),
                SpManager.getUserId(getApplicationContext()));
//        if (balance>0){
//            mRadiaYFT.setChecked(false);
//            mRadiaAlipay.setChecked(true);
//            mRadiaWechat.setChecked(false);
//        }else {
//            mRadiaYFT.setChecked(false);
//            mRadiaAlipay.setChecked(true);
//            mRadiaWechat.setChecked(false);
//        }
        mRadiaYFT.setChecked(false);
        mRadiaAlipay.setChecked(true);
        mRadiaWechat.setChecked(false);
        new Task().execute();
        if (!TextUtils.isEmpty(code))
        goPayTime(code);
    }
    private void  goPayTime(String code){
        PinHuoApi pinHuoApi = HttpManager.getInstance().getPinHuoNoCacheApi(TAG);
        addSubscribe(pinHuoApi.getPayViewInfo(code)
                .compose(RxUtil.<PinHuoResponse<PayTimeBean>>rxSchedulerHelper())
                .compose(RxUtil.<PayTimeBean>handleResult())
                .subscribeWith(new CommonSubscriber<PayTimeBean>(this, true, "获取时间...") {
                    @Override
                    public void onNext(PayTimeBean bean) {
                        super.onNext(bean);
                        if (bean!=null){
                            long end_time=bean.getEnd_time();
                            long start_time=bean.getStart_time();
                            if ((end_time-start_time)>0){
                                ll_time.setVisibility(View.VISIBLE);
                                myCountDownTimer=new MyCountDownTimer(end_time-start_time);
                                myCountDownTimer.start();
                            }else {
                                ll_time.setVisibility(View.GONE);
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        super.onComplete();
                    }
                }));
    }
    private class MyCountDownTimer extends CountDownTimer {
        public MyCountDownTimer(long millisInFuture) {
            super(millisInFuture, 1);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            // long millisUntilFinished_now = SpManager.getQuickSellEndTime(mContext) - System.currentTimeMillis();
            long millisUntilFinished_now = millisUntilFinished;
            int hour = (int) (millisUntilFinished_now / TimeUtils.HOUR_MILLIS);
            int min = (int) ((millisUntilFinished_now - hour * TimeUtils.HOUR_MILLIS) / TimeUtils.MINUTE_MILLIS);
            int sec = (int) ((millisUntilFinished_now - hour * TimeUtils.HOUR_MILLIS - min * TimeUtils.MINUTE_MILLIS) / TimeUtils.SECOND_MILLIS);
            int milli = (int) (millisUntilFinished_now - hour * TimeUtils.HOUR_MILLIS - min * TimeUtils.MINUTE_MILLIS - sec * TimeUtils.SECOND_MILLIS);
            updateCountDownTime(hour, min, sec, milli);
        }

        @Override
        public void onFinish() {
            try {
                ll_time.setVisibility(View.GONE);
                dialog = PayTimeDialog.getInstance(OrderPayActivity.this);
                dialog.setContent("支付时间已经截止，请返回或者跳转订单列表").setLeftStr("返回").setRightStr("订单列表").setPositive(new PayTimeDialog.PopDialogListener() {
                    @Override
                    public void onPopDialogButtonClick(int ok_cancel, PayTimeDialog.DialogType type) {
                        if (ok_cancel==PayTimeDialog.BUTTON_POSITIVIE)
                        BWApplication.reBackFirst();
                    }
                }).setNegative(new PayTimeDialog.PopDialogListener() {
                    @Override
                    public void onPopDialogButtonClick(int ok_cancel, PayTimeDialog.DialogType type) {
                        if (ok_cancel==PayTimeDialog.BUTTON_NEGATIVE) {
                            Intent it = new Intent(getApplicationContext(), OrderManageActivity.class);
                            it.putExtra(OrderManageActivity.EXTRA_ORDER_TYPE, Const.OrderStatus.WAIT_PAY);
                            it.putExtra(BaseOrderDetailActivity.EXTRA_ORDER_ID, Integer.parseInt(mOrderPayId));
                            startActivity(it);
                            finish();
                        }
                    }
                }).showDialog();

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
    private void updateCountDownTime(int hour, int min, int sec, int milli) {
        setHourTv(mTvHH, mTvH, mTvDay, mTvDayDesc, hour);
        setTv(mTvMM, mTvM, min);
        if (min>0){
            if (min<10){
                mTvMM.setVisibility(View.GONE);
            }else {
                mTvMM.setVisibility(View.VISIBLE);
            }
            mTvM.setVisibility(View.VISIBLE);
            tv_m_desc.setVisibility(View.VISIBLE);
        }else {
            mTvMM.setVisibility(View.GONE);
            mTvM.setVisibility(View.GONE);
            tv_m_desc.setVisibility(View.GONE);
        }
        setTv(mTvSS, mTvS, sec);
        if (sec<10&&min<=0){
            mTvSS.setVisibility(View.GONE);
        }else {
            mTvSS.setVisibility(View.VISIBLE);
        }
        mTvF.setText(milli / 100 + "");
    }

    private void setHourTv(TextView tvHH, TextView tvH, TextView tvDay, TextView tvDayDesc, int time) {
        int aa = time / 10;
        if (time < 24) {
            int a = time % 10;
            tvHH.setText(aa + "");
            tvH.setText(a + "");
            tvDay.setVisibility(View.GONE);
            tvDayDesc.setVisibility(View.GONE);
            if (time<=0){
                tvHH.setVisibility(View.GONE);
                tvH.setVisibility(View.GONE);
                tv_hour_desc.setVisibility(View.GONE);
            }else {
                if (time<10){
                    tvHH.setVisibility(View.GONE);
                }else {
                    tvHH.setVisibility(View.VISIBLE);
                }
                tvH.setVisibility(View.VISIBLE);
                tv_hour_desc.setVisibility(View.VISIBLE);
            }
        } else {
            tvDay.setVisibility(View.VISIBLE);
            tvDayDesc.setVisibility(View.VISIBLE);
            int hours = time % 24;
            setTv(tvHH, tvH, hours);
            tvDay.setText((time / 24) + "");
        }

    }

    private void setTv(TextView tvAA, TextView tvA, int time) {
        int aa = time / 10;
        int a = time % 10;
        tvAA.setText(aa + "");
        tvA.setText(a + "");
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mEventBus.unregister(this);
        BWApplication.RECHARGECODE = "";
        if (myCountDownTimer!=null)
            myCountDownTimer.cancel();
    }

    public boolean isWeixinAvilible(Context context) {
        final PackageManager packageManager = context.getPackageManager();// 获取packagemanager
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);// 获取所有已安装程序的包信息
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equals("com.tencent.mm")) {
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public void onResume() {
        super.onResume();
        initPay();
        showPayDialog();

    }

    private void showPayDialog() {
        try {
            if (mRadiaAlipay.isChecked()) {
                // commDialog.setContent("正进入支付宝支付中！如您已支付，可根据您支付的情况点击下面的按钮").setLeftStr("我已支付").setRightStr("取消").setPositive(this).showDialog();
            } else if (isWeixinAvilible(this) && mRadiaWechat.isChecked()) {
                if (api != null) {
                    commDialog = CommDialog.getInstance(OrderPayActivity.this).setDialogType(CommDialog.DialogType.D_PAY)
                            // .setContent("正进入微信支付中！如您已支付，可根据您支付的情况点击下面的按钮").setLeftStr("我已支付").setRightStr("取消")
                            .setPositive(this);
                    commDialog.showDialog();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            //System.out.println("按下了back键   onKeyDown()");
            goBackPay();
            return false;
        } else {
            return super.onKeyDown(keyCode, event);
        }

    }


    private void initViews() {
        ll_time=findViewById(R.id.ll_time);
        mTvHH = (TextView) findViewById(R.id.tv_hh);
        mTvH = (TextView) findViewById(R.id.tv_h);
        mTvMM = (TextView) findViewById(R.id.tv_mm);
        mTvM = (TextView) findViewById(R.id.tv_m);
        mTvSS = (TextView) findViewById(R.id.tv_ss);
        mTvS = (TextView) findViewById(R.id.tv_s);
        mTvF = (TextView) findViewById(R.id.tv_f);
        mTvDay = (TextView) findViewById(R.id.tv_day);
        mTvDayDesc = (TextView) findViewById(R.id.tv_day_desc);
        tv_hour_desc= (TextView) findViewById(R.id.tv_hour_desc);
        tv_m_desc= (TextView) findViewById(R.id.tv_m_desc);

        setTitle("订单支付");
        setLeftClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                goBackPay();
            }
        });
        ll_balance = findViewById(R.id.ll_balance);
        btnPay = (Button) findViewById(android.R.id.button1);
        mTvPayMoney = (TextView) findViewById(android.R.id.text1);
        mNotEnoughMoney = (TextView) findViewById(R.id.not_enough_money);
        mTvAvMoney = (TextView) findViewById(R.id.tv_money);
        mRadiaYFT = (RadioButton) findViewById(R.id.radio_yft);
        findViewById(R.id.ll_yft).setOnClickListener(this);
        mRadiaAlipay = (RadioButton) findViewById(R.id.radio_alipay);
        findViewById(R.id.ll_alipay).setOnClickListener(this);
        mRadiaWechat = (RadioButton) findViewById(R.id.radio_wechat);
        findViewById(R.id.ll_wechat).setOnClickListener(this);
        mBtnActive = (Button) findViewById(android.R.id.button2);
        mBtnActive.setOnClickListener(this);
        mTvPayMoney.setText(Html.fromHtml(getString(R.string.will_pay_x, Utils.moneyFormat(mOrderMoney))));
        btnPay.setText("立即付款");
        btnPay.setBackgroundResource(R.drawable.btn_blue);
        mNotEnoughMoney.setVisibility(View.GONE);
        // initcheck();
    }

    private void goBackPay() {
//        ViewHub.showOkDialog(mContext, "提示", "您还没有支付，是否放弃支付？", "确定", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                BWApplication.reBackFirst();
//                // finish();
//            }
//        });
        CDialog2 dialog = new CDialog2(this);
        dialog.setHasTittle(true).setTitle("确定要取消支付吗？").setMessage("未支付的订单将在专场结束后自动取消，确定要放弃支付吗？").setPositive("确定取消", new CDialog2.PopDialogListener() {
            @Override
            public void onPopDialogButtonClick(int which) {
                BWApplication.reBackFirst();
            }
        }).setNegative("我再想想", null).show();
    }

    public void cancelPayNotifyDialog() {
        if (commDialog != null) {
            commDialog.dismiss();
            commDialog = null;
        }

    }

    private void initcheck() {
        isOpen = UserInfoProvider.hasOpenedYFT(getApplicationContext(),
                SpManager.getUserId(getApplicationContext()));
        balance = UserInfoProvider.getUserBalance(getApplicationContext(),
                SpManager.getUserId(getApplicationContext()));
        if (balance > 0) {
            //有余额
            ll_balance.setVisibility(View.VISIBLE);
            mRadiaYFT.setChecked(true);
            mRadiaAlipay.setChecked(true);
            mRadiaWechat.setChecked(false);

        } else {
            //无余额
            ll_balance.setVisibility(View.GONE);
            mRadiaYFT.setChecked(false);
            mRadiaAlipay.setChecked(true);
            mRadiaWechat.setChecked(false);
        }

        if (!isOpen) {
            mRadiaYFT.setChecked(false);
            mRadiaAlipay.setChecked(true);
            mRadiaWechat.setChecked(false);
        } else {

            if (mOrderMoney > balance) {
                //充值
                btnPay.setText("立即付款");
              /*  btnPay.setBackgroundResource(R.drawable.btn_red_3);
                mNotEnoughMoney.setVisibility(View.GONE);*/
                btnPay.setBackgroundResource(R.drawable.btn_blue);
                mNotEnoughMoney.setVisibility(View.VISIBLE);
                mNotEnoughMoney.setText("你的余额不足，需补" + getSDouble(mOrderMoney - balance) + "元");
                mRadiaYFT.setChecked(false);
                mRadiaAlipay.setChecked(true);
                mRadiaWechat.setChecked(false);
            } else {
                btnPay.setText("立即付款");
                btnPay.setBackgroundResource(R.drawable.btn_blue);
                mNotEnoughMoney.setVisibility(View.GONE);
                mRadiaYFT.setChecked(true);
                mRadiaAlipay.setChecked(false);
                mRadiaWechat.setChecked(false);
            }

        }
    }

    @Override
    public void onClick(View v) {
        isOpen = UserInfoProvider.hasOpenedYFT(getApplicationContext(),
                SpManager.getUserId(getApplicationContext()));
        balance = UserInfoProvider.getUserBalance(getApplicationContext(),
                SpManager.getUserId(getApplicationContext()));
        HasPayPassword = UserInfoProvider.getHasPayPassword(this);
        switch (v.getId()) {
            case android.R.id.button2:// 激活衣付通
                Intent yftIntent = new Intent(getApplicationContext(), YFTActivity.class);
                startActivity(yftIntent);
                break;
            case android.R.id.button1:
                BWApplication.PayOrderId = mOrderPayId;
                BWApplication.PayMoney = mOrderMoney;
                if (mRadiaWechat.isChecked() && !mRadiaYFT.isChecked()) {
                    BWApplication.PayMode = "微信全额支付";
                    weixinCharge(mOrderMoney);
                } else if (mRadiaAlipay.isChecked() && !mRadiaYFT.isChecked()) {
                    BWApplication.PayMode = "支付宝全额支付";
                    alipayCharge(mOrderMoney);
                } else if (mRadiaWechat.isChecked() && mRadiaYFT.isChecked()) {
                    if (balance > 0) {
                        //有余额
                        if (mOrderMoney > balance) {
                            //余额不足
                            BWApplication.PayMode = "微信支付(使用余额)";
                            weixinCharge(getSDouble(mOrderMoney - balance));
                        } else {
                            //余额充足
                            BWApplication.PayMode = "余额支付";
                            judeYfTPay();

                        }

                    }

                } else if (mRadiaAlipay.isChecked() && mRadiaYFT.isChecked()) {

                    if (balance > 0) {
                        //有余额
                        if (mOrderMoney > balance) {
                            //余额不足
                            BWApplication.PayMode = "支付宝支付(使用余额)";
                            alipayCharge(getSDouble(mOrderMoney - balance));
                        } else {
                            //余额充足
                            BWApplication.PayMode = "余额支付";
                            judeYfTPay();
                        }

                    }
                }


                //确保第一次三方支付不用叫开支付通
//                if (mOrderMoney > balance) {
//                    //余额不足
//                    PayDialog dialog = new PayDialog(this, getSDouble(mOrderMoney - balance), balance);
//                    dialog.show();
//                } else {
//                    if (mRadiaWechat.isChecked()) {
//                        BWApplication.PayMode = "微信支付";
//                        weixinCharge(mOrderMoney);
//                    } else if (mRadiaAlipay.isChecked()) {
//                        BWApplication.PayMode = "支付宝支付";
//                        alipayCharge(mOrderMoney);
//                    } else {
//                        BWApplication.PayMode = "余额支付";
//                        if (!isOpen || HasPayPassword == 0) {
//                            ViewHub.showLongToast(getApplicationContext(), "现在激活衣付通");
//                            Intent yftIntent1 = new Intent(getApplicationContext(), YFTActivity.class);
//                            startActivity(yftIntent1);
//                            return;
//                        }
//                        PaymentHelper paymentHelper = new PaymentHelper(this, new PaymentListener() {
//                            @Override
//                            public void paySuccess(String orderId, Object obj) {
//                                Log.i("OrderPayActivity", "paySuccess orderId:" + orderId);
//                                gotoPayActivty();
//                            }
//
//                            @Override
//                            public void payFail(String orderId, String erroMsg) {
//                                Log.i("OrderPayActivity", "payFail orderId:" + orderId + " erroMsg:" + erroMsg);
//                            }
//                        });
//                        PaymentType type = null;
//                        type = PaymentType.YFT;
//                        paymentHelper.pay(type, mOrderPayId + "", 0, true);
//                    }
//                }

//                if (mRadiaWechat.isChecked()) {
//                    BWApplication.PayMode = "微信支付";
//                    weixinCharge(mOrderMoney);
//                } else if (mRadiaAlipay.isChecked()) {
//                    BWApplication.PayMode = "支付宝支付";
//                    alipayCharge(mOrderMoney);
//                } else {
//                    BWApplication.PayMode = "余额支付";
//                    if (!isOpen) {
//                        ViewHub.showLongToast(getApplicationContext(), "现在激活衣付通");
//                        Intent yftIntent1 = new Intent(getApplicationContext(), YFTActivity.class);
//                        startActivity(yftIntent1);
//                        return;
//                    }
//                    if (btnPay.getText().equals("充值")) {
//                        Intent intent = new Intent(this, WXPayEntryActivity.class);
//                        intent.putExtra(WXPayEntryActivity.EXTRA_TYPE, WXPayEntryActivity.Type.CHARGE);
//                        startActivity(intent);
//                        return;
//                    }
//
//                    PaymentHelper paymentHelper = new PaymentHelper(this, new PaymentListener() {
//                        @Override
//                        public void paySuccess(String orderId, Object obj) {
//                            Log.i("OrderPayActivity", "paySuccess orderId:" + orderId);
//                            gotoPayActivty();
////                            EventBus.getDefault().post(BusEvent.getEvent(EventBusId.CANCEL_ORDER));
////                            setResult(RESULT_OK);
////                            finish();
////                          Intent it = new Intent(getApplicationContext(), OrderManageActivity.class);
////                            it.putExtra(OrderManageActivity.EXTRA_ORDER_TYPE, Const.OrderStatus.WAIT_SHIP);
//////                                it.putExtra(BaseOrderDetailActivity.EXTRA_ORDER_ID, Integer.parseInt(mOrderPayId));
////                            startActivity(it);
//                        }
//
//                        @Override
//                        public void payFail(String orderId, String erroMsg) {
//                            Log.i("OrderPayActivity", "payFail orderId:" + orderId + " erroMsg:" + erroMsg);
//                        }
//                    });
//                    PaymentType type = null;
//                    type = PaymentType.YFT;
//                    paymentHelper.pay(type, mOrderPayId + "", 0, true);
//                }
                //}
                break;
            case R.id.ll_yft:
//                boolean isOpen1 = UserInfoProvider.hasOpenedYFT(getApplicationContext(),
//                        SpManager.getUserId(getApplicationContext()));
//                if (!isOpen1) {
//                    ViewHub.showShortToast(getApplicationContext(), "衣付通支付方式未激活，请您激活后再使用...");
//                    return;
//                }
//                if (mOrderMoney > balance) {
//                    btnPay.setText("立即付款");
//                    btnPay.setBackgroundResource(R.drawable.btn_blue);
//                    mNotEnoughMoney.setVisibility(View.VISIBLE);
//                    mNotEnoughMoney.setText("你的余额不足，需补" + getSDouble(mOrderMoney - balance) + "元");
////                    btnPay.setText("充值");
////                    btnPay.setBackgroundResource(R.drawable.btn_red_3);
////                    mNotEnoughMoney.setVisibility(View.VISIBLE);
////                    ViewHub.showShortToast(getApplicationContext(), "衣付通余额不足，请选择其他付款方式...");
////                    return;
//                } else {
//                    btnPay.setText("立即付款");
//                    btnPay.setBackgroundResource(R.drawable.btn_blue);
//                    mNotEnoughMoney.setVisibility(View.GONE);
//                }
//                mRadiaYFT.setChecked(true);
//                mRadiaAlipay.setChecked(false);
//                mRadiaWechat.setChecked(false);
                if (mRadiaYFT.isChecked()) {
                    mRadiaYFT.setChecked(false);
                } else {
                    mRadiaYFT.setChecked(true);
                }
                break;
            case R.id.ll_alipay:
                mRadiaAlipay.setChecked(true);
                mRadiaWechat.setChecked(false);
//                if (mOrderMoney > balance) {
//                    btnPay.setText("立即付款");
//                    btnPay.setBackgroundResource(R.drawable.btn_blue);
//                    mNotEnoughMoney.setVisibility(View.VISIBLE);
//                    mNotEnoughMoney.setText("你的余额不足，需补" + getSDouble(mOrderMoney - balance) + "元");
//                } else {
//                    btnPay.setText("立即付款");
//                    btnPay.setBackgroundResource(R.drawable.btn_blue);
//                    mNotEnoughMoney.setVisibility(View.GONE);
//                }
//              /*  btnPay.setText("立即付款");
//                btnPay.setBackgroundResource(R.drawable.btn_blue);
//                mNotEnoughMoney.setVisibility(View.GONE);*/
//                mRadiaYFT.setChecked(false);
//                mRadiaAlipay.setChecked(true);
//                mRadiaWechat.setChecked(false);
                break;
            case R.id.ll_wechat:
                mRadiaAlipay.setChecked(false);
                mRadiaWechat.setChecked(true);
//                if (mOrderMoney > balance) {
//                    btnPay.setText("立即付款");
//                    btnPay.setBackgroundResource(R.drawable.btn_blue);
//                    mNotEnoughMoney.setVisibility(View.VISIBLE);
//                    mNotEnoughMoney.setText("你的余额不足，需补" + getSDouble(mOrderMoney - balance) + "元");
//                } else {
//                    btnPay.setText("立即付款");
//                    btnPay.setBackgroundResource(R.drawable.btn_blue);
//                    mNotEnoughMoney.setVisibility(View.GONE);
//                }
//                mRadiaYFT.setChecked(false);
//                mRadiaAlipay.setChecked(false);
//                mRadiaWechat.setChecked(true);
                break;
        }
    }

    private void judeYfTPay() {
        if (!isOpen || HasPayPassword == 0) {
            //没开通或者没支付密码
            ViewHub.showTextDialog(mContext, "提示",
                    "为了您的资金安全，使用余额全额支付前需要先设置支付密码，是否现在开通衣付通余额付款功能？",
                    "确定", "取消",
                    new ViewHub.EditDialogListener() {
                        @Override
                        public void onOkClick(DialogInterface dialog, EditText editText) {

                        }

                        @Override
                        public void onOkClick(EditText editText) {
                            Intent yftIntent = new Intent(getApplicationContext(), YFTActivity1.class);
                            startActivity(yftIntent);

                        }

                        @Override
                        public void onNegativecClick() {//不需要

                        }
                    });
        } else {
            PaymentHelper paymentHelper = new PaymentHelper(this, new PaymentListener() {
                @Override
                public void paySuccess(String orderId, Object obj) {
                    Log.i("OrderPayActivity", "paySuccess orderId:" + orderId);
                    gotoPayActivty();
                }

                @Override
                public void payFail(String orderId, String erroMsg) {
                    Log.i("OrderPayActivity", "payFail orderId:" + orderId + " erroMsg:" + erroMsg);
                }
            });
            PaymentType type = null;
            type = PaymentType.YFT;
            paymentHelper.pay(type, mOrderPayId + "", 0, true);
        }
    }

    @Override
    public void onPopDialogButtonClick(int ok_cancel, CommDialog.DialogType type) {
        if (ok_cancel == CommDialog.BUTTON_POSITIVIE) {
            api = null;
            new ThridPayTask(Step.CHECK_PAY_RESULT, 0).execute(BWApplication.RECHARGECODE, System.currentTimeMillis());
        }
    }

    class PayDialog extends PopupWindow implements View.OnClickListener {
        private Activity mActivity;
        private View mRootView;
        private LinearLayout mContentViewBg;
        private Button btn_pay, btn_cancel;
        private TextView mTvTitle;
        private RadioButton rd_alipay, rd_wechat;
        private PayDialog dialog;
        private double differ_price, balance;

        public PayDialog(Activity activity, double differ_price, double balance) {
            super(activity);
            this.mActivity = activity;
            this.differ_price = differ_price;
            this.balance = balance;
            initView();
        }

        private void initView() {
            mRootView = mActivity.getLayoutInflater().inflate(R.layout.pay_popwindow_dialog, null);
            mContentViewBg = (LinearLayout) mRootView.findViewById(R.id.contentView);
            mTvTitle = (TextView) mRootView.findViewById(R.id.tv_title);
            btn_cancel = (Button) mRootView.findViewById(R.id.btn_cancel);
            btn_pay = (Button) mRootView.findViewById(R.id.btn_pay);
            rd_wechat = (RadioButton) mRootView.findViewById(R.id.rd_wechat);
            rd_alipay = (RadioButton) mRootView.findViewById(R.id.rd_alipay);
            mRootView.findViewById(R.id.ll_alipay).setOnClickListener(this);
            mRootView.findViewById(R.id.ll_wechat).setOnClickListener(this);
            btn_cancel.setOnClickListener(this);
            btn_pay.setOnClickListener(this);
            mTvTitle.setText("¥" + differ_price + "(已减余额¥" + balance + ")");
            this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
            this.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
            this.setContentView(mRootView);
            this.setFocusable(true);
            ColorDrawable dw = new ColorDrawable(0xb0000000);
            this.setBackgroundDrawable(dw);
            setAnimationStyle(R.style.LightPopDialogAnim);
        }

        public void show() {
            DisplayMetrics dm = new DisplayMetrics();
            mActivity.getWindowManager().getDefaultDisplay().getMetrics(dm);
            // int screenWidth = dm.widthPixels;
            int screenHeight = dm.heightPixels;
            int top = mContentViewBg.getTop();
            int bottom = mContentViewBg.getBottom();
            showAtLocation(mActivity.getWindow().getDecorView(), Gravity.CENTER, 0, screenHeight / 2 - (bottom - top) / 2);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_pay:
                    //付款
                    if (rd_wechat.isChecked()) {
                        BWApplication.PayMode = "微信支付";
                        weixinCharge(differ_price);
                    } else if (rd_alipay.isChecked()) {
                        BWApplication.PayMode = "支付宝支付";
                        alipayCharge(differ_price);
                    }
                    dismiss();
                    break;
                case R.id.btn_cancel:
                    dismiss();
                    break;
                case R.id.ll_wechat:
                    rd_alipay.setChecked(false);
                    rd_wechat.setChecked(true);
                    break;
                case R.id.ll_alipay:
                    rd_alipay.setChecked(true);
                    rd_wechat.setChecked(false);
                    break;
            }
        }
    }

    private void gotoPayActivty() {
        Intent intent = new Intent(getApplicationContext(), PaySucessActivity.class);
        // EventBus.getDefault().post(BusEvent.getEvent(EventBusId.CANCEL_ORDER));
        //setResult(RESULT_OK);
        startActivity(intent);
    }

    public void onEventMainThread(BusEvent event) {
        switch (event.id) {
            case EventBusId.WECHAT_PAY_SUCCESS:
                Intent it = new Intent(getApplicationContext(), GetBuyOrderActivity.class);
                it.putExtra(BaseOrderDetailActivity.EXTRA_ORDER_ID, mOrderPayId);
                startActivity(it);
                finish();
                break;

            case EventBusId.BANK_PAY_SUCCESS:
                OrderPaidChecker checker = new OrderPaidChecker(this, new IOrderCheckListener() {
                    @Override
                    public void orderPaySuccess(String orderId, Object obj) {
                        Intent it = new Intent(getApplicationContext(), GetBuyOrderActivity.class);
                        it.putExtra(BaseOrderDetailActivity.EXTRA_ORDER_ID, mOrderPayId);
                        startActivity(it);
                        finish();
                    }

                    @Override
                    public void orderPayFail(String orderId, String failMsg) {
                    }
                });
                checker.checkOrderStatus(mOrderPayId + "");
                break;
            case EventBusId.BANK_PAY_FAIL:
                ViewHub.showShortToast(getApplicationContext(), "支付失败");
                break;

        }
    }

    @Override
    protected void onActivityResult(int arg0, int arg1, Intent arg2) {
        if (arg1 == RESULT_OK) {
            switch (arg0) {
                case CODE_RESULT_WECHAT_PAY:// 微信支付成功
                    Intent it = new Intent(getApplicationContext(), GetBuyOrderActivity.class);
                    it.putExtra(BaseOrderDetailActivity.EXTRA_ORDER_ID, Integer.parseInt(mOrderPayId));
                    startActivity(it);
//                    Intent intent = new Intent(getApplicationContext(), OrderDetailsActivity.class);
//                    intent.putExtra(OrderDetailsActivity.EXTRA_ORDER_ID,Integer.parseInt(mOrderPayId));
//                    startActivity(intent);
                    finish();
                    break;
                case REQUEST_WX_RECHARGE:// 微信充值成功
                    cancelPayNotifyDialog();
                    String out_trade_id = arg2.getStringExtra("out_trade_id");
                    long startTime = System.currentTimeMillis();
                    check_count = 1;
                    api = null;
                    new ThridPayTask(Step.CHECK_PAY_RESULT, 0).execute(out_trade_id, startTime);
                    break;
                default:
                    break;
            }
            new Task().execute();
        } else if (arg1 == RESULT_CANCELED) {
            switch (arg0) {
                case REQUEST_WX_RECHARGE:// 微信充值成功
                    try {
                        cancelPayNotifyDialog();
                        if (arg2.hasExtra("msg") && arg2.getStringExtra("msg") != null) {
                            String msg = arg2.getStringExtra("msg");
                            if (!TextUtils.isEmpty(msg)) {
                                ViewHub.showLongToast(mContext, msg);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    break;
            }
        }
        super.onActivityResult(arg0, arg1, arg2);
    }

    private class Task extends AsyncTask<Object, Void, Object> {

        private JPayUser mPayUser;

        @Override
        protected void onPreExecute() {
            if (mLoadingDialog == null) {
                mLoadingDialog = new LoadingDialog(OrderPayActivity.this);
            }
            mLoadingDialog.setMessage("正在加载账户信息...");

            mLoadingDialog.show();
            super.onPreExecute();
        }

        @Override
        protected Object doInBackground(Object... params) {
            try {
                mPayUser = PaymentAPI.getUserInfo(getApplicationContext());
                UserInfoProvider.cachePayUserInfo(getApplicationContext(), mPayUser);
            } catch (Exception e) {
                e.printStackTrace();
                return "error:" + e.getMessage();
            }
            return mPayUser;
        }

        @Override
        protected void onPostExecute(Object result) {
            hideDialog();
            if (result instanceof String && ((String) result).startsWith("error:")) {
                ViewHub.showLongToast(getApplicationContext(), ((String) result).replace("error:", ""));
            } else {
                initPay();
            }
            super.onPostExecute(result);
        }
    }

    private void initPay() {
        isOpen = UserInfoProvider.hasOpenedYFT(getApplicationContext(),
                SpManager.getUserId(getApplicationContext()));
        balance = UserInfoProvider.getUserBalance(getApplicationContext(),
                SpManager.getUserId(getApplicationContext()));
        HasPayPassword = UserInfoProvider.getHasPayPassword(this);
        if (balance > 0) {
            //有余额
            ll_balance.setVisibility(View.VISIBLE);

            if (mOrderMoney > balance) {
                //余额不足
                mTvAvMoney.setText(Html.fromHtml("余额：" + "<font color='#FC3D39'><b>¥" + Utils.moneyFormat(balance) + "</b></font>" +
                        "(需再付" + "<font color='#FC3D39'><b>¥" + getSDouble(mOrderMoney - balance) + "</b></font>" + ")"));

            } else {
                //余额充足
//                mTvAvMoney.setText(Html.fromHtml("余额："+"<font color='#C60A1E'><b>¥"+Utils.moneyFormat(balance)+"</b></font>" +
//                        "(全额支付后剩余<font color='#C60A1E'><b>¥"+getSDouble(balance-mOrderMoney)+"</b></font>"+")"));
                mTvAvMoney.setText(Html.fromHtml("余额：" + "<font color='#FC3D39'><b>¥" + Utils.moneyFormat(balance) + "</b></font>"
                ));
            }

        } else {
            //无余额
            ll_balance.setVisibility(View.GONE);
            mRadiaYFT.setChecked(false);
        }

//        mTvAvMoney.setText(Html.fromHtml(getString(R.string.available_balance, Utils.moneyFormat(balance))));
//        mBtnActive.setVisibility(isOpen && HasPayPassword > 0 ? View.GONE : View.VISIBLE);
//        if (mOrderMoney > balance) {
//            btnPay.setText("立即付款");
//            btnPay.setBackgroundResource(R.drawable.btn_blue);
//            mNotEnoughMoney.setVisibility(View.VISIBLE);
//            mNotEnoughMoney.setText("你的余额不足，需补" + getSDouble(mOrderMoney - balance) + "元");
//        } else {
//            btnPay.setText("立即付款");
//            btnPay.setBackgroundResource(R.drawable.btn_blue);
//            mNotEnoughMoney.setVisibility(View.GONE);
//        }

    }

    /*
    * 四舍五入
    * */
    public Double getSDouble(double f) {
        return FunctionHelper.DoubleTwoFormat(f);
    }
    //---------------------------------------三方充值业务方法-------------------

    private Listener mListener;

    public static interface Listener {
        public void onPayReqGenerated(PayReq payReq);
    }

    private void topUp(Step mStep, double money) {
        boolean validate = validateMoney(money);
        if (!validate) {
            ViewHub.showLongToast(mContext, "请支付大于0的数字");
            return;
        }
        // 调起微信支付
        new ThridPayTask(mStep, money).execute();
    }

    private boolean validateMoney(double money) {
        return StringUtils.isPositiveNumber(money + "");
    }

    IWXAPI api;

    private void weixinCharge(double money) {
        api = WXAPIFactory.createWXAPI(mContext, BWApplication.getPayWXAPPId());
        boolean isPaySupported = api.getWXAppSupportAPI() >= Build.PAY_SUPPORTED_SDK_INT;
        if (!isPaySupported)
            ViewHub.showLightPopDialog((Activity) mContext, "提示", mContext.getString(R.string.wx_version_too_low),
                    mContext.getString(android.R.string.cancel), mContext.getString(android.R.string.ok),
                    new LightPopDialog.PopDialogListener() {
                        @Override
                        public void onPopDialogButtonClick(int which) {
                        }
                    });
        else {
            try {
                Intent intent = new Intent(this, WXPayEntryActivity.class);
                intent.putExtra(WXPayEntryActivity.EXTRA_TYPE, WXPayEntryActivity.Type.FAST_WX_CHARGE);
                intent.putExtra(WXPayEntryActivity.EXTRA_MONEY, money);
                startActivityForResult(intent, REQUEST_WX_RECHARGE);
            } catch (Exception e) {
                e.printStackTrace();
                ViewHub.showLongToast(mContext, e.getMessage());
            }
        }
    }

    PaymentHelper paymentHelper;

    private void alipayCharge(double money) {
        paymentHelper = new PaymentHelper((Activity) mContext, new PaymentHelper.PaymentListener() {
            @Override
            public void paySuccess(String orderId, Object obj) {
                Log.i("OrderPayActivity", "paySuccess orderId:" + orderId);
                cancelPayNotifyDialog();
                //这里的orderID实际上是rechareCode
                long startTime = System.currentTimeMillis();
                check_count = 1;
                new ThridPayTask(Step.CHECK_PAY_RESULT, 0).execute(orderId, startTime);
            }

            @Override
            public void payFail(String orderId, String erroMsg) {
                cancelPayNotifyDialog();
                Log.i("OrderPayActivity", "payFail orderId:" + orderId + " erroMsg:" + erroMsg);
            }
        });
        topUp(Step.CALL_ALIPAY_PAYMENT, money);

    }

    private static enum Step {
        CALL_ALIPAY_PAYMENT, CHECK_PAY_RESULT
    }

    private int check_count = 1;

    private class ThridPayTask extends AsyncTask<Object, Void, Object> {

        private Step mStep;
        private double payMoney;

        public ThridPayTask(Step step, double money) {
            mStep = step;
            payMoney = money;
        }

        @Override
        protected void onPreExecute() {
            switch (mStep) {
                case CALL_ALIPAY_PAYMENT:
                    mLoadingDialog.start("获取充值中...");
                    break;
                case CHECK_PAY_RESULT:
                    mLoadingDialog.start("第" + check_count + "次校验中...");
                    break;
            }
        }

        @Override
        protected Object doInBackground(Object... params) {
            try {
                switch (mStep) {
                    case CALL_ALIPAY_PAYMENT:// 调起支付宝支付
                        paymentHelper.pay(
                                PaymentHelper.PaymentType.ALIPAY_RECHARGE,
                                "",
                                payMoney, false);
                        break;
                    case CHECK_PAY_RESULT:// 确认支付结果
                        String rechargeCode = (String) params[0];
                        long startTime = (Long) params[1];
                        JPayResult obj = PaymentAPI.getRechargeInfo(mContext, rechargeCode);
                        obj.setStartTime(startTime);
                        return obj;
                }
            } catch (Exception e) {
                e.printStackTrace();
                return "error:" + e.getMessage();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object result) {

            if (result instanceof String && ((String) result).startsWith("error:")) {
                ViewHub.showLongToast(mContext, ((String) result).replace("error:", ""));
            } else {
                switch (mStep) {
                    case CALL_ALIPAY_PAYMENT:
                        if (mLoadingDialog.isShowing()) {
                            mLoadingDialog.stop();
                        }
                        break;
                    case CHECK_PAY_RESULT:
                        final JPayResult payResult = (JPayResult) result;
                        final long startTime = payResult.getStartTime();
                        int status = payResult.getStatus();
                        boolean suc = (status == 3);
                        boolean failed = (status == 4);
                        boolean unpay = (status == 1);
                        if (suc) {
                            if (mLoadingDialog.isShowing()) {
                                mLoadingDialog.stop();
                            }
                            updateBalance(payResult.getMoney());
                            //调免密支付
                            payNoPsw();
                        } else {
                            if (check_count == 5) {
                                if (mLoadingDialog.isShowing()) {
                                    mLoadingDialog.stop();
                                }
                                payNoPsw();
                                ViewHub.showLightPopDialog(OrderPayActivity.this, "提示", "系统正在同步中，如果订单显示未支付，请等待5分钟后重新刷新订单。", getString(android.R.string.cancel),
                                        "确定", new LightPopDialog.PopDialogListener() {
                                            @Override
                                            public void onPopDialogButtonClick(int which) {
                                                Intent it = new Intent(getApplicationContext(), OrderManageActivity.class);
                                                it.putExtra(OrderManageActivity.EXTRA_ORDER_TYPE, Const.OrderStatus.ALL_PAY);
                                                it.putExtra(BaseOrderDetailActivity.EXTRA_ORDER_ID, Integer.parseInt(mOrderPayId));
                                                startActivity(it);
                                                finish();
                                            }
                                        });
                            } else {
                                check_count++;
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        new ThridPayTask(Step.CHECK_PAY_RESULT, 0).execute(payResult.getRecharge_code(), startTime);
                                    }
                                }, 3000);
                            }
                        }
//                        if (suc || failed) {
//                            if (suc) {
//                                updateBalance(payResult.getMoney());
//                                //调免密支付
//                                payNoPsw();
//                            } else {
//                                ViewHub.showLongToast(mContext, "充值失败");
//                            }
//
//                        } else if (System.currentTimeMillis() - startTime < WxChargeFragment.CEHCK_PAY_RESULT_TIMEOUT) {
//                            // 如果小于5秒，仍没支付成功继续查询结果
//                            new ThridPayTask(Step.CHECK_PAY_RESULT, 0).execute(payResult.getRecharge_code(), startTime);
//                        } else {// 5秒查询之后仍然没有结果，手动添加结果
//                            updateBalance(payResult.getMoney());
//                            ViewHub.showLongToast(mContext, "充值成功,但未确定是否对账成功,请点余额支付进行尝试");
//                        }

                        break;
                }
            }

        }

        private void updateBalance(double balance) {
            double oldBalance = UserInfoProvider.getUserBalance(mContext, SpManager.getUserId(mContext));
            double newBalance = oldBalance + balance;
            UserInfoProvider.setUserBalance(mContext, SpManager.getUserId(mContext), newBalance);
        }

    }

    private void payNoPsw() {
        PaymentHelper paymentHelper = new PaymentHelper(this, new PaymentListener() {
            @Override
            public void paySuccess(String orderId, Object obj) {
                Log.i("OrderPayActivity", "paySuccess orderId:" + orderId);
//                EventBus.getDefault().post(BusEvent.getEvent(EventBusId.CANCEL_ORDER));
//                setResult(RESULT_OK);
//                finish();
//                Intent it = new Intent(getApplicationContext(), OrderManageActivity.class);
//                it.putExtra(OrderManageActivity.EXTRA_ORDER_TYPE, Const.OrderStatus.WAIT_SHIP);
////                                it.putExtra(BaseOrderDetailActivity.EXTRA_ORDER_ID, Integer.parseInt(mOrderPayId));
//                startActivity(it);
                gotoPayActivty();
            }

            @Override
            public void payFail(String orderId, String erroMsg) {
                Log.i("OrderPayActivity", "payFail orderId:" + orderId + " erroMsg:" + erroMsg);
            }
        });
        PaymentType type = null;
        type = PaymentType.PAY_WITH_NO_PSW;
        paymentHelper.pay(type, mOrderPayId + "", 0, true);
    }
}
