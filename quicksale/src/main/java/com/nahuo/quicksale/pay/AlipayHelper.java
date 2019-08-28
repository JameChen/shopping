package com.nahuo.quicksale.pay;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.alipay.sdk.app.PayTask;
import com.nahuo.library.controls.LightPopDialog.PopDialogListener;
import com.nahuo.library.controls.LoadingDialog;
import com.nahuo.library.helper.FunctionHelper;
import com.nahuo.quicksale.ViewHub;
import com.nahuo.quicksale.common.Utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
/**
 * @description 支付宝支付
 * @created 2015年4月28日 下午9:06:37
 * @author JorsonWong
 */
public class AlipayHelper {

    private Activity            mContext;
    private AlipayListener      mAlipayListener;
    private LoadingDialog       mLoadingDialog;
    private String              mRechareCode;

    // 支付宝公钥

    private static final int    SDK_PAY_FLAG   = 1;

    private static final int    SDK_CHECK_FLAG = 2;

    private static final String TAG            = "AlipayHelper";

    AlipayHelper(Activity context, AlipayListener listener) {
        this.mContext = context;
        this.mAlipayListener = listener;
        this.mLoadingDialog = new LoadingDialog(mContext);
    }
/*
    *//**
     * @description
     * @created 2015年4月29日 下午4:03:58
     * @author JorsonWong
     * @param rechareCode:预付单号
     * @param tradeName:交易名称
     * @param tradeDesc:交易描述
     * @param money:交易金额
     * @param notifyUrl:回调通知地址
     *//*
    public void pay(String rechareCode, String tradeName, String tradeDesc, double money, String notifyUrl,String partner,String seller_id,String sign) {
        if (!FunctionHelper.IsNetworkOnline(mContext)) {
            ViewHub.showLightPopDialog(mContext, "提示", "连接不到网络无法支付", mContext.getString(android.R.string.cancel),
                    "设置网络", new PopDialogListener() {
                        @Override
                        public void onPopDialogButtonClick(int which) {
                            Intent intent = new Intent("android.settings.WIFI_SETTINGS");
                            mContext.startActivity(intent);
                        }
                    });
            return;
        }
        mRechareCode = rechareCode;

        mLoadingDialog.start("正在支付，请耐心等待...");
        // 订单
        if (!TextUtils.isEmpty(tradeName) && tradeName.length() > 128) {// 限制最大长度128
            tradeName = tradeName.substring(0, 128);
        }

        if (!TextUtils.isEmpty(tradeDesc) && tradeDesc.length() > 512) {// 限制最大长度512
            tradeDesc = tradeDesc.substring(0, 512);
        }

        String orderInfo = getOrderInfo(rechareCode, tradeName, tradeDesc, Utils.moneyFormat(money), notifyUrl,partner,seller_id);

        // 对订单做RSA 签名
        //String sign2 = sign(orderInfo);

        try {
            // 仅需对sign 做URL编码
            sign = URLEncoder.encode(sign, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        // 完整的符合支付宝参数规范的订单信息
        final String payInfo = orderInfo + "&sign=\"" + sign + "\"&" + getSignType();

        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                // 构造PayTask 对象
                PayTask alipay = new PayTask(mContext);
                // 调用支付接口，获取支付结果
                String result = alipay.pay(payInfo,true);

                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };

        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }*/
    public void pay(String orderInfo,String rechareCode) {
        if (!FunctionHelper.IsNetworkOnline(mContext)) {
            ViewHub.showLightPopDialog(mContext, "提示", "连接不到网络无法支付", mContext.getString(android.R.string.cancel),
                    "设置网络", new PopDialogListener() {
                        @Override
                        public void onPopDialogButtonClick(int which) {
                            Intent intent = new Intent("android.settings.WIFI_SETTINGS");
                            mContext.startActivity(intent);
                        }
                    });
            return;
        }
        mRechareCode = rechareCode;
        if (mLoadingDialog!=null)
        mLoadingDialog.start("正在支付，请耐心等待...");
        // 完整的符合支付宝参数规范的订单信息+ "&sign=\"" + sign + "\"&" + getSignType()
        final String payInfo = orderInfo ;

        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                // 构造PayTask 对象
                PayTask alipay = new PayTask(mContext);
                // 调用支付接口，获取支付结果
                String result = alipay.pay(payInfo,true);

                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };

        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }
    /***
     * @description
     * @created 2015年4月29日 下午5:41:57
     * @author JorsonWong
     */
    private static String getOrderInfo(String orderId, String subject, String body, String price, String notify_url,String partner,String seller_id) {
        // 签约合作者身份ID
        String orderInfo = "partner=" + "\"" + partner + "\"";

        // 签约卖家支付宝账号
        orderInfo += "&seller_id=" + "\"" + seller_id + "\"";

        // 商户网站唯一订单号
        orderInfo += "&out_trade_no=" + "\"" + orderId + "\"";

        // 商品名称
        orderInfo += "&subject=" + "\"" + subject + "\"";

        // 商品详情
        orderInfo += "&body=" + "\"" + body + "\"";

        // 商品金额
        orderInfo += "&total_fee=" + "\"" + price + "\"";

        // 服务器异步通知页面路径
        orderInfo += "&notify_url=" + "\"" + notify_url + "\"";

        // 服务接口名称， 固定值
        orderInfo += "&service=\"mobile.securitypay.pay\"";

        // 支付类型， 固定值
        orderInfo += "&payment_type=\"1\"";

        // 参数编码， 固定值
        orderInfo += "&_input_charset=\"utf-8\"";

        // 设置未付款交易的超时时间
        // 默认30分钟，一旦超时，该笔交易就会自动被关闭。
        // 取值范围：1m～15d。
        // m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
        // 该参数数值不接受小数点，如1.5h，可转换为90m。
        orderInfo += "&it_b_pay=\"15d\"";

        // extern_token为经过快登授权获取到的alipay_open_id,带上此参数用户将使用授权的账户进行支付
        // orderInfo += "&extern_token=" + "\"" + extern_token + "\"";

        // 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
        orderInfo += "&return_url=\"m.alipay.com\"";

        // 调用银行卡支付，需配置此参数，参与签名， 固定值 （需要签约《无线银行卡快捷支付》才能使用）
        // orderInfo += "&paymethod=\"expressGateway\"";

        return orderInfo;
    }


    /**
     * get the sign type we use. 获取签名方式
     * 
     */
    private String getSignType() {
        return "sign_type=\"RSA\"";
    }

    private Handler mHandler = new Handler() {
                                 public void handleMessage(Message msg) {
                                     switch (msg.what) {
                                         case SDK_PAY_FLAG: {
                                             PayResult payResult = new PayResult((String)msg.obj);
                                             // 支付宝返回此次支付结果及加签，建议对支付宝签名信息拿签约时支付宝提供的公钥做验签
                                             String resultInfo = payResult.getResult();

                                             Log.i(TAG,
                                                     "PayResult resultInfo:" + resultInfo + " Memo:"
                                                             + payResult.getMemo());

                                             String resultStatus = payResult.getResultStatus();

                                             Log.i(TAG, "PayResult resultStatus:" + resultStatus);

                                             // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                                             if (TextUtils.equals(resultStatus, "9000")) {
                                                 mAlipayListener.alipaySuccess(mRechareCode);
                                                 mLoadingDialog.dismiss();
                                             } else {
                                                 // 判断resultStatus 为非“9000”则代表可能支付失败
                                                 // “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                                                 if (TextUtils.equals(resultStatus, "8000")) {
                                                     // Toast.makeText(mContext, "支付结果确认中", Toast.LENGTH_SHORT).show();
                                                     Log.i(TAG, "PayResult :等待支付结果确认");
                                                     mAlipayListener.alipayWaiting(mRechareCode);
                                                     mLoadingDialog.setMessage("正在等待支付确认...");
                                                 } else {
                                                     // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                                                     mAlipayListener.alipayFail(mRechareCode, payResult.getMemo());
                                                     mLoadingDialog.dismiss();
                                                 }
                                             }
                                             break;
                                         }
                                         // case SDK_CHECK_FLAG: {
                                         // Toast.makeText(mContext, "检查结果为：" + msg.obj, Toast.LENGTH_SHORT).show();
                                         // break;
                                         // }
                                         default:
                                             break;
                                     }
                                 };
                             };

    /***
     * @description 支付宝支付回调
     * @created 2015年4月29日 上午9:26:36
     * @author JorsonWong
     */
    public interface AlipayListener {

        /** 支付宝支付成功 */
        public void alipaySuccess(String rechareCode);

        /** 支付宝支付失败 */
        public void alipayFail(String rechareCode, String erro);

        /** 支付宝支付等待 */
        public void alipayWaiting(String rechareCode);
    }
}
