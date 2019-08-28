package com.nahuo.quicksale;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.baidu.mobstat.StatService;
import com.nahuo.library.controls.LoadingDialog;
import com.nahuo.library.utils.TimeUtils;
import com.nahuo.quicksale.api.PaymentAPI;
import com.nahuo.quicksale.common.SpManager;
import com.nahuo.quicksale.common.StringUtils;
import com.nahuo.quicksale.oldermodel.BalanceModel;
import com.nahuo.quicksale.oldermodel.WXPayment;
import com.nahuo.quicksale.oldermodel.json.JPayResult;
import com.nahuo.quicksale.pay.PaymentHelper;
import com.nahuo.quicksale.provider.UserInfoProvider;
import com.nahuo.quicksale.wxapi.WXPayEntryActivity;
import com.tencent.mm.opensdk.constants.Build;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.modelpay.PayResp;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @author ZZB
 * @description wx充值
 * @created 2014-10-31 下午4:17:30
 */
public class WxChargeFragment extends Fragment implements View.OnClickListener, IWXAPIEventHandler {
    private View mContentView;
    private Context mContext;
    /*** 检查支付结果超时时间 */
    public static final long CEHCK_PAY_RESULT_TIMEOUT = 20000;
    private static final String TAG = WXPayEntryActivity.class.getSimpleName();

    private EditText mEtMoney;
    private LoadingDialog mLoadingDlg;
    private Listener mListener;
    private View mWxCharge;                                                          // 充值wx
    private View mAlipayCharge;                                                          // 充值alipay
    private int mWxAppSupportApi;
    private PaymentHelper paymentHelper;

    public static interface Listener {
        public void onPayReqGenerated(PayReq payReq);
    }

    private static enum Step {
        CALL_WX_PAYMENT, CALL_ALIPAY_PAYMENT, CHECK_PAY_RESULT, LOAD_BASE_BALANCE
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = getActivity();
        mContentView = inflater.inflate(R.layout.activity_top_up, container, false);
        initView();
        paymentHelper = new PaymentHelper((Activity) mContext, new PaymentHelper.PaymentListener() {
            @Override
            public void paySuccess(String orderId, Object obj) {
                Log.i("OrderPayActivity", "paySuccess orderId:" + orderId);

                //这里的orderID实际上是rechareCode
                long startTime = System.currentTimeMillis();
                new Task(Step.CHECK_PAY_RESULT).execute(orderId, startTime);
            }

            @Override
            public void payFail(String orderId, String erroMsg) {
                Log.i("OrderPayActivity", "payFail orderId:" + orderId + " erroMsg:" + erroMsg);
            }
        });
        return mContentView;
    }

    private void initView() {
        ((TextView) mContentView.findViewById(R.id.btn_recharge_tips)).setText(SpManager.getBALANCE_RECHARGE_TIPS(mContext));
        mWxCharge = mContentView.findViewById(R.id.btn_pay_wx);
        mWxCharge.setOnClickListener(this);
        mAlipayCharge = mContentView.findViewById(R.id.btn_pay_alipay);
        mAlipayCharge.setOnClickListener(this);
        mEtMoney = (EditText) mContentView.findViewById(R.id.et_money);
        mEtMoney.addTextChangedListener(new TextWatcher() {
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
                    mEtMoney.setText(number);
                    mEtMoney.setSelection(number.length());
                }
            }
        });

        mEtMoney.setFocusableInTouchMode(true);
        mEtMoney.requestFocus();
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
                           public void run() {
                               InputMethodManager inputManager =
                                       (InputMethodManager) mEtMoney.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                               inputManager.showSoftInput(mEtMoney, 0);
                           }
                       },
                998);
        mLoadingDlg = new LoadingDialog(mContext);
    }

    /**
     * @description 充值
     * @time 2014-8-12 下午5:30:24
     * @author ZZB
     */
    private void topUp(Step mStep) {
        boolean validate = validateMoney();
        boolean isPaySupported = mWxAppSupportApi >= Build.PAY_SUPPORTED_SDK_INT;
        if (!validate) {
            ViewHub.setEditError(mEtMoney, "请输入大于0的数字");
            return;
        } else if (!isPaySupported) {
            if (mStep == Step.CALL_WX_PAYMENT) {
                ViewHub.showShortToast(getActivity(), "对不起，您的设备不支持微信支付，请安装5.0版本以上的微信。");
                return;
            }
        }
        // 调起微信支付
        new Task(mStep).execute();
    }

    private boolean validateMoney() {
        String money = mEtMoney.getText().toString();
        return StringUtils.isPositiveNumber(money);
    }

    private class Task extends AsyncTask<Object, Void, Object> {

        private Step mStep;

        public Task(Step step) {
            mStep = step;
        }

        @Override
        protected void onPreExecute() {
            switch (mStep) {
                case CALL_WX_PAYMENT:
                    mLoadingDlg.start("获取充值中...");
                    break;
                case CALL_ALIPAY_PAYMENT:
                    mLoadingDlg.start("获取充值中...");
                    break;
                case CHECK_PAY_RESULT:
                    mLoadingDlg.start("充值结果确认中...");
                    break;
                case LOAD_BASE_BALANCE:
                    break;
            }
        }

        @Override
        protected Object doInBackground(Object... params) {
            try {
                switch (mStep) {
                    case CALL_WX_PAYMENT:// 调起微信支付
                        WXPayment model = new WXPayment();
                        model.setTimeStamp(TimeUtils.dateToTimeStamp(new Date(), "yyyyMMddHHmmss"));
                        model.setMoney(Double.valueOf(mEtMoney.getText().toString()));
                        PayReq req = PaymentAPI.getPayReq(mContext, model, true);
                        mListener.onPayReqGenerated(req);
                        // api.sendReq(req);
                        break;
                    case CALL_ALIPAY_PAYMENT:// 调起支付宝支付
                        paymentHelper.pay(
                                PaymentHelper.PaymentType.ALIPAY_RECHARGE,
                                "",
                                Double.valueOf(mEtMoney.getText().toString()), true);
                        break;
                    case CHECK_PAY_RESULT:// 确认支付结果
                        String rechargeCode = (String) params[0];
                        long startTime = (Long) params[1];
                        JPayResult obj = PaymentAPI.getRechargeInfo(mContext, rechargeCode);
                        obj.setStartTime(startTime);
                        return obj;
                    case LOAD_BASE_BALANCE:
                        BalanceModel balanceData = PaymentAPI.getBalance(mContext);
                        SpManager.setBALANCE_RECHARGE_TIPS(mContext, balanceData.getRechargetips());
                        SpManager.setBALANCE_CASHOUT_TIPS(mContext, balanceData.getCashouttips());
                        SpManager.setBALANCE_FEEZE_MONEY(mContext, balanceData.getFreeze_money());
                        SpManager.setBALANCE_enablecashoutmoney(mContext, balanceData.getEnablecashoutmoney());
                        SpManager.setBALANCE_CASHOUT_FEE_MONEY(mContext, balanceData.getCashoutfee());
                        UserInfoProvider.setUserBalance(mContext, SpManager.getUserId(mContext), balanceData.getBalance());
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
                return "error:" + e.getMessage();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object result) {
            if (mLoadingDlg.isShowing()) {
                mLoadingDlg.stop();
            }
            if (result instanceof String && ((String) result).startsWith("error:")) {
                ViewHub.showLongToast(mContext, ((String) result).replace("error:", ""));
            } else {
                switch (mStep) {
                    case CALL_WX_PAYMENT:
                        mLoadingDlg.stop();
                        break;
                    case CALL_ALIPAY_PAYMENT:
                        mLoadingDlg.stop();
                        break;
                    case LOAD_BASE_BALANCE:
                        break;
                    case CHECK_PAY_RESULT:
                        JPayResult payResult = (JPayResult) result;
                        long startTime = payResult.getStartTime();
                        int status = payResult.getStatus();
                        boolean suc = (status == 3);
                        boolean failed = (status == 4);
                        Log.d(TAG, "CHECK_PAY_RESULT = " + suc + " , " + failed + "");
                        if (suc || failed) {
                            if (suc) {
                                updateBalance(payResult.getMoney());
                            }
                            String msg = suc ? "充值成功！" : "充值失败！";
                            ViewHub.showOkDialog(mContext, "提示", msg, "OK", new OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Activity aty = getActivity();
                                    if (aty != null && !aty.isFinishing()) {
                                        aty.finish();
                                    }
                                }
                            });

                        } else if (System.currentTimeMillis() - startTime < CEHCK_PAY_RESULT_TIMEOUT) {
                            // 如果小于5秒，仍没支付成功继续查询结果
                            new Task(Step.CHECK_PAY_RESULT).execute(payResult.getRecharge_code(), startTime);
                        } else {// 5秒查询之后仍然没有结果，手动添加结果
                            updateBalance(payResult.getMoney());
                            ViewHub.showOkDialog(mContext, "提示", "充值结果:" + payResult.getStatus_name(), "OK",
                                    new OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            getActivity().finish();
                                        }
                                    });
                        }

                        break;
                }
            }

        }

    }

    private void updateBalance(double balance) {
        double oldBalance = UserInfoProvider.getUserBalance(mContext, SpManager.getUserId(mContext));
        double newBalance = oldBalance + balance;
        UserInfoProvider.setUserBalance(mContext, SpManager.getUserId(mContext), newBalance);

        new Task(Step.LOAD_BASE_BALANCE).execute();
    }

    @Override
    public void onPause() {
        super.onPause();
        StatService.onPause(this);
        if (mLoadingDlg != null && mLoadingDlg.isShowing()) {
            mLoadingDlg.stop();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        StatService.onResume(this);
    }

    public void setListener(Listener listener) {
        mListener = listener;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_pay_wx:
                topUp(Step.CALL_WX_PAYMENT);
                break;
            case R.id.btn_pay_alipay:
                topUp(Step.CALL_ALIPAY_PAYMENT);
                break;
        }
    }

    @Override
    public void onReq(BaseReq req) {

    }

    @Override
    public void onResp(BaseResp resp) {
        Log.d(TAG, "onPayFinish, errCode = " + resp.errCode);
        switch (resp.errCode) {
            case 0:// 充值成功
                PayResp payResp = (PayResp) resp;
                String rechargeCode = payResp.extData;
                long startTime = System.currentTimeMillis();
                new Task(Step.CHECK_PAY_RESULT).execute(rechargeCode, startTime);
                break;
            case -1:// 充值失败
                // ViewHub.showOkDialog(mContext, "提示", "充值失败，请退出应用后再次尝试", "OK");
                ViewHub.showShortToast(mContext, "充值失败，请退出应用后再次尝试");
                break;
            case -2:// 取消充值
                break;
        }
    }

    public void setWxAppSupportApi(int wxAppSupportApi) {
        mWxAppSupportApi = wxAppSupportApi;
    }

}
