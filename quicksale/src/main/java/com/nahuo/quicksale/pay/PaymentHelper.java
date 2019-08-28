package com.nahuo.quicksale.pay;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.text.Editable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.nahuo.library.controls.LightPopDialog;
import com.nahuo.library.controls.LightPopDialog.PopDialogListener;
import com.nahuo.library.controls.LoadingDialog;
import com.nahuo.quicksale.R;
import com.nahuo.quicksale.ViewHub;
import com.nahuo.quicksale.activity.FogetPayPassWordActivity;
import com.nahuo.quicksale.api.BuyOnlineAPI;
import com.nahuo.quicksale.api.PaymentAPI;
import com.nahuo.quicksale.app.BWApplication;
import com.nahuo.quicksale.pay.AlipayHelper.AlipayListener;
import com.nahuo.quicksale.pay.OrderPaidChecker.IOrderCheckListener;
import com.tencent.mm.opensdk.constants.Build;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

import de.greenrobot.event.EventBus;

public class PaymentHelper implements AlipayListener, IOrderCheckListener {

    private final static String TAG = "PaymentHelper";

    private Activity mActivity;
    private PaymentType mPaymentType;
    private String mOrderId;
    private PaymentListener mPaymentListener;

    public LoadingDialog mLoadingDialog;

    private OrderPaidChecker mOrderPaidChecker;


    private IWXAPI api;
    private double money;
    private boolean isRecharge;

    public enum PaymentType {
        YFT, ALIPAY, NETBANK, WECHAT, ALIPAY_RECHARGE, PAY_WITH_NO_PSW
    }

    public PaymentHelper(Activity activity, PaymentListener listener) {
        this.mActivity = activity;
        this.mPaymentListener = listener;
        mOrderPaidChecker = new OrderPaidChecker(mActivity, this);
        mLoadingDialog = new LoadingDialog(mActivity);

    }

    public void pay(PaymentType type, String orderId, double _money, boolean isRecharge) {
        this.money = _money;
        this.mPaymentType = type;
        this.mOrderId = orderId;
        this.isRecharge = isRecharge;
        if (mPaymentType == PaymentType.YFT) {// 输入密码框
            LightPopDialog dialog = new LightPopDialog(mActivity);
            View vInput = LayoutInflater.from(mActivity).inflate(R.layout.layout_yft_pwd_input, null);
            final EditText mEdtPass = (EditText) vInput.findViewById(R.id.edt_yftpass);
            vInput.findViewById(R.id.tv_forgot).setOnClickListener(listenerForgotPwd);
            dialog.setTitle("衣付通支付");
            dialog.addContentView(vInput);
            dialog.setPositive(android.R.string.ok, new PopDialogListener() {
                @Override
                public void onPopDialogButtonClick(int which) {
                    Editable pwd = mEdtPass.getText();
                    if (!TextUtils.isEmpty(pwd)) {
                        new TaskPay().execute(pwd.toString());
                    } else {
                        ViewHub.showShortToast(mActivity.getApplicationContext(), "请输入支付密码");
                    }
                }
            });
            dialog.show();
            mEdtPass.setFocusableInTouchMode(true);
            mEdtPass.requestFocus();
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                               public void run() {
                                   InputMethodManager inputManager =
                                           (InputMethodManager) mEdtPass.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                                   inputManager.showSoftInput(mEdtPass, 0);
                               }
                           },
                    500);
        } else if (mPaymentType == PaymentType.WECHAT) {// wechat pay
            api = WXAPIFactory.createWXAPI(mActivity, BWApplication.getPayWXAPPId());
            boolean isPaySupported = api.getWXAppSupportAPI() >= Build.PAY_SUPPORTED_SDK_INT;
            if (!isPaySupported)
                ViewHub.showLightPopDialog(mActivity, "提示", mActivity.getString(R.string.wx_version_too_low),
                        mActivity.getString(android.R.string.cancel), mActivity.getString(android.R.string.ok),
                        new PopDialogListener() {
                            @Override
                            public void onPopDialogButtonClick(int which) {
                                mActivity.finish();
                            }
                        });
            new TaskPay().execute();

        } else {
            new TaskPay().execute();
        }
    }

    private class TaskPay extends AsyncTask<Object, Object, Object> {

        //        private List<OrderPayInfo> mOrderPayInfo;
        private EventBus mEventBus;

        @Override
        protected void onPreExecute() {
            if (mPaymentType != PaymentType.ALIPAY_RECHARGE) {
                mLoadingDialog.start("正在获取订单信息...");
            }
            super.onPreExecute();
        }

        @Override
        protected Object doInBackground(Object... params) {
            String json = null;
            try {
                if (mPaymentType == PaymentType.ALIPAY_RECHARGE) {
                    //支付宝
                    json = PaymentAPI.aliPay_Recharge(mActivity.getApplicationContext(), money, isRecharge);
                    Log.i("OrderPayActivity", "PaymentAPI aliPay_Recharge ： " + json.toString());
                } else {
                    String payInfoJson = BuyOnlineAPI.getOrderPayInfoForPinHuo(mActivity, mOrderId);
                    JSONObject jo = new JSONObject(payInfoJson);
                    String payGuid = jo.getString("PayGuid");
                    if (mPaymentType == PaymentType.YFT) {
                        json = PaymentAPI.YFTPay(mActivity, payGuid, mOrderId, params[0].toString());
                        Log.i("OrderPayActivity", "PaymentAPI YFT ： " + json.toString());

                    } else if (mPaymentType == PaymentType.PAY_WITH_NO_PSW) {
                        json = PaymentAPI.NoPswPay(mActivity, payGuid, mOrderId);
                        Log.i("OrderPayActivity", "PaymentAPI NO PSW ： " + json.toString());
                    }
//                    else if (mPaymentType == PaymentType.ALIPAY) {
//                        json = PaymentAPI.aliPay(mActivity.getApplicationContext(), mOrderPayInfo);
//                        Log.i("OrderPayActivity", "PaymentAPI aliPay ： " + json.toString());
//                    } else if (mPaymentType == PaymentType.WECHAT) {
//                        JSONObject repObj = PaymentAPI.wapPay(mActivity.getApplicationContext(), mOrderPayInfo);
//
//                        Log.i("OrderPayActivity", "PaymentAPI wapPay ： " + repObj.toString());
//
//                        json = repObj.toString();
//
//                    } else if (mPaymentType == PaymentType.NETBANK) {
//                        json = PaymentAPI.bankPay(mActivity.getApplicationContext(), mOrderPayInfo);
//                        Log.i("OrderPayActivity", "PaymentAPI NETBANK ： " + json.toString());
//                    }
                }
            } catch (Exception e) {
                Log.e(TAG, "Exception : " + e.getMessage());
                return "error:" + e.getMessage();
            }
            return json;
        }

        @Override
        protected void onPostExecute(Object result) {
            if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
                mLoadingDialog.stop();
            }
            if (result instanceof String && ((String) result).startsWith("error:")) {
                String erroMsg = ((String) result).replace("error:", "");
                showPayFail(erroMsg);

            } else {
                if (mPaymentType == PaymentType.YFT) {
                    mOrderPaidChecker.checkOrderStatus(mOrderId);
                } else if (mPaymentType == PaymentType.ALIPAY_RECHARGE) {
                    //支付宝
                    try {
                        if (result != null) {
                            JSONObject jsonObject = new JSONObject(result.toString());
                            AlipayHelper alipayHelper = new AlipayHelper(mActivity, PaymentHelper.this);
//                            String rechare_code = jsonObject.getString("rechare_code");
//                            String notify_url = jsonObject.getString("notify_url");// "1218435201";
//                            String alipaysign = jsonObject.getString("alipaysign");
//                            String alipay_partner = jsonObject.getString("alipay_partner");
//                            String alipay_seller_id = jsonObject.getString("alipay_seller_id");
                  /*          String rechare_code = jsonObject.getString("rechare_code");
                            String notify_url = "";// "1218435201";
                            String alipaysign = jsonObject.getString("alipaysign");
                            String alipay_partner = jsonObject.getString("alipay_partner");
                            String alipay_seller_id = jsonObject.getString("alipay_seller_id");
                            alipayHelper.pay(rechare_code, "天天拼货团充值", "天天拼货团充值",
                                    money, notify_url, alipay_partner, alipay_seller_id, alipaysign);*/
                            String orderInfo=jsonObject.getString("order_info");
                            String rechare_code=jsonObject.getString("rechare_code");
                            alipayHelper.pay(orderInfo,rechare_code);
                        } else {
                            showPayFail("支付失败：数据异常");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        showPayFail("支付失败：数据异常");
                    }
                } else if (mPaymentType == PaymentType.PAY_WITH_NO_PSW) {
                    mPaymentListener.paySuccess("0", null);
                }
//                else if (mPaymentType == PaymentType.ALIPAY) {
//
//                    try {
//                        if (result != null) {
//                            JSONObject jsonObject = new JSONObject(result.toString());
//                            String rechare_code = jsonObject.getString("rechare_code");
//                            String notify_url = jsonObject.getString("notify_url");// "1218435201";
//                            AlipayHelper alipayHelper = new AlipayHelper(mActivity, PaymentHelper.this);
//                            alipayHelper.pay(rechare_code, mOrderPayInfo.getTradeName(), mOrderPayInfo.getDesc(),
//                                    mOrderPayInfo.getMoney(), notify_url);
//                        } else {
//                            showPayFail("支付失败：数据异常");
//                        }
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                        showPayFail("支付失败：数据异常");
//                    }
//                } else if (mPaymentType == PaymentType.NETBANK) {
//                    try {
//                        if (result != null) {
//                            JSONObject jsonObject = new JSONObject(result.toString());
//                            String pay_url = jsonObject.getString("pay_url");
//                            Log.i(TAG, "pay_url:" + pay_url);
//                            Intent it = new Intent(mActivity, ItemPreviewActivity.class);
//                            it.putExtra("url", pay_url);
//                            it.putExtra("name", "订单支付");
//                            mActivity.startActivity(it);
//                        } else {
//                            showPayFail("支付失败：数据异常");
//                        }
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                        showPayFail("支付失败：数据异常");
//                    }
//                } else if (mPaymentType == PaymentType.WECHAT) {
//                    if (result != null) {
//                        try {
//                            JSONObject repObj = new JSONObject(result.toString());
//                            Intent intent = new Intent(mActivity, WXPayEntryActivity.class);
//                            intent.putExtra(WXPayEntryActivity.EXTRA_ORDER_NUM, mOrderId + "");
//                            intent.putExtra(WXPayEntryActivity.EXTRA_TYPE, WXPayEntryActivity.Type.WAP_PAY);
//                            intent.putExtra(WXPayEntryActivity.EXTRA_WECHAT_TRAND, result.toString());
//                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                            mActivity.startActivity(intent);
//                            // PayReq req = new PayReq();
//                            // req.appId = repObj.getString("appid");
//                            // req.partnerId = repObj.getString("partnerid");// "1218435201";
//                            // req.prepayId = repObj.getString("prepayid");
//                            // req.nonceStr = repObj.getString("noncestr");
//                            // req.timeStamp = repObj.getString("timestamp");
//                            // req.packageValue = repObj.getString("package");
//                            // req.sign = repObj.getString("sign");
//                            // req.extData = repObj.getString("rechargecode"); // optional
//                            // // out_trade_id = repObj.getInt("outtradeid") + "";
//                            // // 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
//                            // api.sendReq(req);
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                            showPayFail("支付失败：数据异常");
//                        }
//                    }
//
//                }
            }
            super.onPostExecute(result);
        }
    }

    public interface PaymentListener {

        public void paySuccess(String orderId, Object obj);

        public void payFail(String orderId, String erroMsg);
    }

    @Override
    public void alipaySuccess(String rechareCode) {
        if (mPaymentType == PaymentType.ALIPAY_RECHARGE) {
            mPaymentListener.paySuccess(rechareCode, null);
        } else {
            mOrderPaidChecker.checkOrderStatus(mOrderId);
        }
    }

    @Override
    public void alipayFail(String rechareCode, String erro) {
        showPayFail(erro);
    }

    private void showPayFail(String failMsg) {
        ViewHub.showLongToast(mActivity, failMsg);
        if (mPaymentListener != null) {
            mPaymentListener.payFail(mOrderId, failMsg);
        }
    }

    @Override
    public void alipayWaiting(String rechareCode) {

    }

    private OnClickListener listenerForgotPwd = new OnClickListener() {
        @Override
        public void onClick(View v) {
          //  ForgetPswActivity.toForgetPayPsw(mActivity);
            Intent intent = new Intent(mActivity, FogetPayPassWordActivity.class);
            mActivity.startActivity(intent);
        }
    };

    @Override
    public void orderPaySuccess(String orderId, Object obj) {
        mPaymentListener.paySuccess(mOrderId, obj);
    }

    @Override
    public void orderPayFail(String orderId, String failMsg) {
        mPaymentListener.payFail(mOrderId, failMsg);
    }

}
