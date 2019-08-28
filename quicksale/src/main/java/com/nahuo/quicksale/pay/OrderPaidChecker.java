package com.nahuo.quicksale.pay;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.nahuo.library.controls.LoadingDialog;
import com.nahuo.quicksale.ViewHub;
import com.nahuo.quicksale.api.PaymentAPI;
import com.nahuo.quicksale.eventbus.BusEvent;
import com.nahuo.quicksale.eventbus.EventBusId;

import de.greenrobot.event.EventBus;

/***
 * @description 支付后循环检测订单状态
 * @created 2015年4月30日 上午9:41:18
 * @author JorsonWong
 */
public class OrderPaidChecker {

    //    private int                 mCheckTimes;
    private LoadingDialog mLoadingDialog;
    private Activity mActivity;
    private String mOrderId;
    private IOrderCheckListener mIOrderCheckListener;

    /** 最大检验次数 */
//    private static final int    MAX_CHECK_TIMES      = 8;
    /**
     * 检验间隔 单位毫秒
     */
    private static final long INTERVAL = 9500;

    /**
     * pay success
     */
    private static final int MESSAGE_WHAT_SUCCESS = 10;
    /**
     * pay fail
     */
    private static final int MESSAGE_WHAT_FAIL = 4;

    private static final String TAG = "OrderPaidChecker";

    public OrderPaidChecker(Activity activity, IOrderCheckListener listener) {
        this.mActivity = activity;
        this.mIOrderCheckListener = listener;
        mLoadingDialog = new LoadingDialog(mActivity);
    }

    public void checkOrderStatus(String orderId) {
        ViewHub.showShortToast(mActivity.getApplicationContext(), "支付成功！");
        EventBus.getDefault().post(
                BusEvent.getEvent(EventBusId.ORDER_PAY_SUCCESS, mOrderId));
        mIOrderCheckListener.orderPaySuccess(mOrderId, "订单支付成功");
    }

    public void checkOrderStatus1(String orderId) {
        this.mOrderId = orderId;
//        mCheckTimes = 0;
        mLoadingDialog.start("正在校验支付...");
        Runnable payRunnable = new Runnable() {
            @Override
            public void run() {

//                while (mCheckTimes <= MAX_CHECK_TIMES) {
//                    Log.i(TAG, "checkOrderIsPaid:" + " mCheckTimes:" + mCheckTimes);
                //   try {
                try {
                    Thread.sleep(INTERVAL);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                String reslut = null;
                try {
                    reslut = PaymentAPI.checkOrderIsPaid(mActivity, mOrderId + "");
                    if (reslut.equals("ok")) {// 支付成功
                        Message msg = mHandler.obtainMessage(MESSAGE_WHAT_SUCCESS);
                        msg.obj = "订单支付成功";
                        msg.sendToTarget();
//                            break;
                    } else {
                        Message msg = new Message();
                        msg.what = MESSAGE_WHAT_FAIL;
                        msg.obj = reslut;
                        mHandler.sendMessage(msg);
//                                break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e(TAG, "checkOrderIsPaid Exception:" + e.toString());
                    Message msg = new Message();
                    msg.what = MESSAGE_WHAT_FAIL;
                    msg.obj = "数据异常";
                    mHandler.sendMessage(msg);
                }


            }
//                catch (Exception e) {
//                    Log.e(TAG, "checkOrderIsPaid Exception:" + e.toString());
////                        if (mCheckTimes >= MAX_CHECK_TIMES) {// 正在同步
//                    Message msg = new Message();
//                    msg.what = MESSAGE_WHAT_FAIL;
//                    msg.obj = "数据异常";
//                    mHandler.sendMessage(msg);
////                        }
//                }

//                    mCheckTimes++;
//                }
            //  }
        };
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
                mLoadingDialog.stop();
            }
            switch (msg.what) {
                case MESSAGE_WHAT_SUCCESS:
                    ViewHub.showShortToast(mActivity.getApplicationContext(), "支付成功！");
                    EventBus.getDefault().post(
                            BusEvent.getEvent(EventBusId.ORDER_PAY_SUCCESS, mOrderId));
                    mIOrderCheckListener.orderPaySuccess(mOrderId, msg.obj);
                    break;
                case MESSAGE_WHAT_FAIL:
                    if (msg.obj == null) {
                        msg.obj = "数据异常";
                    }
                    ViewHub.showShortToast(mActivity.getApplicationContext(), "支付失败："
                            + msg.obj.toString());
                    mIOrderCheckListener.orderPayFail(mOrderId, "支付失败：" + msg.obj.toString());
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }

    };

    public interface IOrderCheckListener {

        public void orderPaySuccess(String orderId, Object obj);

        public void orderPayFail(String orderId, String failMsg);

    }
}
