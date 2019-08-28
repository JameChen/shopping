package com.nahuo.quicksale;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.nahuo.library.controls.LoadingDialog;
import com.nahuo.quicksale.api.BuyOnlineAPI;
import com.nahuo.quicksale.api.PaymentAPI;
import com.nahuo.quicksale.eventbus.BusEvent;
import com.nahuo.quicksale.eventbus.EventBusId;
import com.nahuo.quicksale.oldermodel.OrderPayInfo;
import com.nahuo.quicksale.pay.OrderPaidChecker;
import com.nahuo.quicksale.pay.OrderPaidChecker.IOrderCheckListener;
import com.tencent.mm.opensdk.constants.Build;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.modelpay.PayResp;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;

import de.greenrobot.event.EventBus;

/**
 * @description wap支付
 * @created 2014-10-31 下午4:17:17
 * @author ZZB
 */
public class WapPayFragment extends Fragment implements IWXAPIEventHandler {

    private Context       mContext;
    private LoadingDialog mLoadingDialog;
    private Listener      mListener;
    private String        mOrderNum;
    private int           mWxAppSupportApi;
    public String         outTradeID;
    private PayReq        mPayReq;

    public static interface Listener {
        public void onPayReqGenerated(PayReq payReq);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
        mLoadingDialog = new LoadingDialog(mContext);

        if (mWxAppSupportApi < Build.PAY_SUPPORTED_SDK_INT) {
            ViewHub.showOkDialog(mContext, "提示", getString(R.string.wx_version_too_low), "OK", new OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    getActivity().finish();
                }
            });
        } else {
            if (mPayReq != null) {// 若已传预付单JSON
                Log.i(getClass().getSimpleName(), "mPayReq not null ");
                mListener.onPayReqGenerated(mPayReq);
            } else {
                new Task().execute();
            }
        }

    }

    public class Task extends AsyncTask<Object, Void, String> {

        @Override
        protected void onPreExecute() {
            mLoadingDialog.start("获取订单信息中");
        }

        @Override
        protected String doInBackground(Object... params) {
            String out_trade_id;
            try {
                OrderPayInfo info = BuyOnlineAPI.getOrderPayInfo(mContext, mOrderNum);
                JSONObject repObj = PaymentAPI.wapPay(mContext, info);
                PayReq req = new PayReq();
                req.appId = repObj.getString("appid");
                req.partnerId = repObj.getString("partnerid");// "1218435201";
                req.prepayId = repObj.getString("prepayid");
                req.nonceStr = repObj.getString("noncestr");
                req.timeStamp = repObj.getString("timestamp");
                req.packageValue = repObj.getString("package");
                req.sign = repObj.getString("sign");
                req.extData = repObj.getString("rechargecode"); // optional

                out_trade_id = repObj.getInt("outtradeid") + "";
                outTradeID = out_trade_id;
                mListener.onPayReqGenerated(req);
            } catch (Exception e) {
                e.printStackTrace();
                return "error:" + e.getMessage();
            }
            return out_trade_id;
        }

        @Override
        protected void onPostExecute(String out_trade_id) {
            if (mLoadingDialog.isShowing()) {
                mLoadingDialog.stop();
            }
            if (out_trade_id instanceof String && ((String)out_trade_id).startsWith("error:")) {
                ViewHub.showLongToast(mContext, ((String)out_trade_id).replace("error:", ""));
            }
        }
    }

    @Override
    public void onReq(BaseReq req) {

    }

    @Override
    public void onResp(BaseResp resp) {
        Log.i("WapPayFragment", "onResp resp.errCode: " + resp.errCode);
        switch (resp.errCode) {
            case 0:// 充值成功
                PayResp payResp = (PayResp)resp;
                String rechargeCode = payResp.extData;
                // ViewHub.showShortToast(getActivity(), "微信支付成功！");
                checkOrderStatus();
                break;
            case -1:// 充值失败
                // ViewHub.showOkDialog(mContext, "提示", "支付失败，请退出应用后再次尝试", "OK");
                ViewHub.showShortToast(getActivity(), "支付失败，请退出应用后再次尝试");
                getActivity().finish();

                break;
            case -2:// 取消充值
                ViewHub.showShortToast(getActivity(), "支付已取消");
                getActivity().finish();
                break;
            default:
                getActivity().finish();
                break;
        }
    }

    private void checkOrderStatus() {
        OrderPaidChecker checker = new OrderPaidChecker(getActivity(), new IOrderCheckListener() {
            @Override
            public void orderPaySuccess(String orderId , Object obj) {
                Intent intent = new Intent();
                intent.putExtra("out_trade_id", outTradeID);
                getActivity().setResult(Activity.RESULT_OK, intent);
                getActivity().finish();
                EventBus.getDefault().postSticky(BusEvent.getEvent(EventBusId.WECHAT_PAY_SUCCESS));
                // ViewHub.showShortToast(getActivity(), "订单检测状态成功！");
            }

            @Override
            public void orderPayFail(String orderId, String failMsg) {
                getActivity().finish();
                // ViewHub.showShortToast(getActivity(), "订单检测状态失败:" + failMsg);
            }
        });
        checker.checkOrderStatus(mOrderNum);
    }

    public void setListener(Listener listener) {
        mListener = listener;
    }

    public void setOrderNum(String orderNum) {
        mOrderNum = orderNum;
    }

    /** 微信预付单信息JSON格式 */
    public void setTrade(String tradeJson) {
        Log.i(getClass().getSimpleName(), "setTrade : " + tradeJson);
        String out_trade_id;
        try {
            JSONObject repObj = new JSONObject(tradeJson);
            mPayReq = new PayReq();
            mPayReq.appId = repObj.getString("appid");
            mPayReq.partnerId = repObj.getString("partnerid");// "1218435201";
            mPayReq.prepayId = repObj.getString("prepayid");
            mPayReq.nonceStr = repObj.getString("noncestr");
            mPayReq.timeStamp = repObj.getString("timestamp");
            mPayReq.packageValue = repObj.getString("package");
            mPayReq.sign = repObj.getString("sign");
            mPayReq.extData = repObj.getString("rechargecode"); // optional
            out_trade_id = repObj.getInt("outtradeid") + "";
            outTradeID = out_trade_id;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setWxAppSupportApi(int wxAppSupportApi) {
        mWxAppSupportApi = wxAppSupportApi;
    }
}
