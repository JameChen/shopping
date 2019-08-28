package com.nahuo.quicksale.wxapi;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.mobstat.StatService;
import com.nahuo.library.controls.LoadingDialog;
import com.nahuo.library.utils.TimeUtils;
import com.nahuo.quicksale.R;
import com.nahuo.quicksale.ViewHub;
import com.nahuo.quicksale.WapPayFragment;
import com.nahuo.quicksale.WxChargeFragment;
import com.nahuo.quicksale.api.PaymentAPI;
import com.nahuo.quicksale.app.BWApplication;
import com.nahuo.quicksale.oldermodel.WXPayment;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.modelpay.PayResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.util.Date;

/**
 * @author ZZB
 * @description 微信支付
 * @created 2014-9-23 下午5:31:29
 */
public class WXPayEntryActivity extends FragmentActivity implements IWXAPIEventHandler, View.OnClickListener {

    public static final String EXTRA_TYPE = "EXTRA_TYPE";
    public static final String EXTRA_MONEY = "EXTRA_MONEY";
    public static final String EXTRA_ORDER_NUM = "EXTRA_ORDER_NUM";
    /**
     * 预付单JSON
     */
    public static final String EXTRA_WECHAT_TRAND = "extra_wechat_trade";

    private Context mContext = this;
    private IWXAPI api;
    private Type mType;
    private Fragment mCurFragment;
    private IWXAPIEventHandler mWXAPIEventHandler;
    private String mOrderNum;
    private WapPayFragment wpf;
    private String mJsonTrade;
    protected LoadingDialog mLoadingDialog;
    private double money;
    private RelativeLayout rl_wx, rl_content;

    public  enum Type {
        /**
         * 充值
         */
        CHARGE,
        /**
         * 微信快速充值
         */
        FAST_WX_CHARGE,
        /**
         * wap支付
         */
        WAP_PAY
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_fragment);
        try {
          //  getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.layout_titlebar_default1);
            api = WXAPIFactory.createWXAPI(mContext, BWApplication.getPayWXAPPId());
            api.registerApp(BWApplication.getPayWXAPPId());
            api.handleIntent(getIntent(), this);
            initExtras();
            initView();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initExtras() {
        Intent intent = getIntent();
        mType = (Type) intent.getSerializableExtra(EXTRA_TYPE);
        mOrderNum = intent.getStringExtra(EXTRA_ORDER_NUM);
        money = intent.getDoubleExtra(EXTRA_MONEY, 0);
        mJsonTrade = intent.getStringExtra(EXTRA_WECHAT_TRAND);

    }

    private void initView() {
        initTitleBar();
        switch (mType) {
            case FAST_WX_CHARGE:// 微信快速充值
                new Task().execute();
                break;
            case CHARGE:// 充值
                WxChargeFragment wxf = new WxChargeFragment();
                wxf.setListener(new WxChargeFragment.Listener() {
                    @Override
                    public void onPayReqGenerated(PayReq payReq) {
                        api.sendReq(payReq);
                    }
                });
                wxf.setWxAppSupportApi(api.getWXAppSupportAPI());
                mWXAPIEventHandler = wxf;
                mCurFragment = wxf;
                getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, mCurFragment).commit();
                break;
            case WAP_PAY:// wap 支付

                wpf = new WapPayFragment();
                wpf.setListener(new WapPayFragment.Listener() {
                    @Override
                    public void onPayReqGenerated(PayReq payReq) {
                        api.sendReq(payReq);
                        // onResp(new PayResp());
                    }
                });
                wpf.setOrderNum(mOrderNum);
                wpf.setTrade(mJsonTrade);

                wpf.setWxAppSupportApi(api.getWXAppSupportAPI());
                mWXAPIEventHandler = wpf;
                mCurFragment = wpf;
                getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, mCurFragment).commit();
                break;
        }

        // Intent intent = new Intent();
        // intent.putExtra("out_trade_id", wpf.outTradeID);
        // setResult(RESULT_OK, intent);
    }

    private class Task extends AsyncTask<Object, Void, Object> {

        @Override
        protected void onPreExecute() {
            if (mLoadingDialog == null) {
                mLoadingDialog = new LoadingDialog(mContext);
            }
            mLoadingDialog.start("获取充值中...");

        }

        @Override
        protected Object doInBackground(Object... params) {
            try {

                WXPayment model = new WXPayment();
                model.setTimeStamp(TimeUtils.dateToTimeStamp(new Date(), "yyyyMMddHHmmss"));
                model.setMoney(money);
                PayReq req = PaymentAPI.getPayReq(mContext, model, false);
                BWApplication.RECHARGECODE=req.extData;
                api.sendReq(req);
                return "OK";
            } catch (Exception ex) {
                return "error:" + ex.getMessage();

            }
        }


        @Override
        protected void onPostExecute(Object result) {
            if (mLoadingDialog.isShowing()) {
                mLoadingDialog.stop();
            }
            if (result instanceof String && ((String) result).startsWith("error:")) {
                ViewHub.showLongToast(mContext, ((String) result).replace("error:", ""));
                finish();
            } else {
            }
        }

    }

    private void initTitleBar() {
        TextView tvTitle = (TextView) findViewById(R.id.titlebar_tvTitle);
        rl_wx = (RelativeLayout) findViewById(R.id.rl_wx);
        rl_content = (RelativeLayout) findViewById(R.id.rl_content);
        rl_wx.setVisibility(View.GONE);
        switch (mType) {
            case CHARGE:
                rl_wx.setVisibility(View.VISIBLE);
                tvTitle.setText("充值");
                break;
            case WAP_PAY:
                rl_wx.setVisibility(View.VISIBLE);
                tvTitle.setText("微信支付");
                break;
        }

        Button btnLeft = (Button) findViewById(R.id.titlebar_btnLeft);
        btnLeft.setText(R.string.titlebar_btnBack);
        btnLeft.setVisibility(View.VISIBLE);
        btnLeft.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.titlebar_btnLeft:
                setResult(RESULT_CANCELED);
                ViewHub.hideKeyboard(this);
                finish();
                break;
        }

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {

    }

    @Override
    public void onResp(BaseResp resp) {
        if (mWXAPIEventHandler != null)
            mWXAPIEventHandler.onResp(resp);// 相应fragmet的回调
        switch (mType) {
            case CHARGE:
            case FAST_WX_CHARGE:
                PayResp payResp = (PayResp) resp;
                switch (payResp.errCode) {
                    case BaseResp.ErrCode.ERR_OK: {
                        String rechargeCode = payResp.extData;
                        Intent intent1 = new Intent();
                        intent1.putExtra("out_trade_id", rechargeCode);
                        setResult(RESULT_OK, intent1);
                        break;
                    }

                    default: {
                        Intent intent1 = new Intent();
                        intent1.putExtra("msg", payResp.errStr);
                        setResult(RESULT_CANCELED, intent1);
                        break;
                    }
                }
                finish();
                break;
            case WAP_PAY:
                Intent intent = new Intent();
                intent.putExtra("out_trade_id", wpf.outTradeID);
                setResult(RESULT_OK, intent);
                break;
            default:
                finish();
                break;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        StatService.onPause(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        StatService.onResume(this);
    }

    @Override
    public void finish() {
        super.finish();
        ViewHub.hideKeyboard(this);
    }

    @Override
    protected void onActivityResult(int arg0, int arg1, Intent arg2) {
        super.onActivityResult(arg0, arg1, arg2);
    }

}
