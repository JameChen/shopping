package com.nahuo.quicksale;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.nahuo.library.controls.LoadingDialog;
import com.nahuo.library.helper.FunctionHelper;
import com.nahuo.library.helper.GsonHelper;
import com.nahuo.quicksale.api.HttpUtils;
import com.nahuo.quicksale.api.PaymentAPI;
import com.nahuo.quicksale.common.SafeUtils;
import com.nahuo.quicksale.common.SpManager;
import com.nahuo.quicksale.common.Utils;
import com.nahuo.quicksale.eventbus.BusEvent;
import com.nahuo.quicksale.eventbus.EventBusId;
import com.nahuo.quicksale.oldermodel.PayForModel;
import com.nahuo.quicksale.oldermodel.PublicData;
import com.nahuo.quicksale.oldermodel.json.JPayUser;
import com.nahuo.quicksale.pay.OrderPaidChecker;
import com.nahuo.quicksale.pay.OrderPaidChecker.IOrderCheckListener;
import com.nahuo.quicksale.pay.PaymentHelper;
import com.nahuo.quicksale.pay.PaymentHelper.PaymentListener;
import com.nahuo.quicksale.pay.PaymentHelper.PaymentType;
import com.nahuo.quicksale.provider.UserInfoProvider;
import com.nahuo.quicksale.yft.YFTActivity;

import java.text.DecimalFormat;
import java.util.TreeMap;

import de.greenrobot.event.EventBus;

/***
 * 代付--支付，，，copy from OrderPayActivity
 * created by 陈智勇   2015-5-20  下午2:01:08
 */
public class PayForOtherActivity extends BaseActivity2 implements OnClickListener{

    private RadioButton        mRadiaYFT, mRadiaAlipay, mRadiaWechat, mRadiaBank;
    private Button             mBtnActive;
    private TextView           mTvAvMoney;

    // private HttpRequestHelper mRequestHelper = new HttpRequestHelper();
    private int                mOrderPayId;
    private double             mOrderMoney;

    /** 订单ID ,用于生产预付单 int 型 */
    public static final String INTENT_PAY_ORDER_ID        = "intent_order_id";

    /** 订单金额，用于显示 double 型 */
    public static final String INTENT_PAY_MONEY           = "intent_pay_money";

    /** 显示订单提交成功标识，用于购物车提交后直接跳转过来的 boolean 型 */
    public static final String INTENT_SHOW_SUBMIT_SUCCESS = "intent_show_submit_success";
    private static final int   CODE_RESULT_WECHAT_PAY     = 1200;

    private EventBus           mEventBus                  = EventBus.getDefault();

    private CheckBox surePayBox ;
    private TextView txtPayForInfo; 
    private Button btnPay ;
    public PayForModel model;
    private TextView txtPaySuccessSyn; 
    private boolean canPayNow ; 
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_for_other);
        mOrderPayId = getIntent().getIntExtra(INTENT_PAY_ORDER_ID, 0);

        initViews();
        initData();

        mEventBus.register(this);

        new Task().execute();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mEventBus.unregister(this);
    }

    @Override
    public void onResume() {
        initPay();
        super.onResume();
    }

    private void initData() {
    }

    private void initViews() {
        setTitle("代付款");

        mTvAvMoney = (TextView)findViewById(R.id.tv_money);

        mRadiaYFT = (RadioButton)findViewById(R.id.radio_yft);
        findViewById(R.id.ll_yft).setOnClickListener(this);

        mRadiaAlipay = (RadioButton)findViewById(R.id.radio_alipay);
        findViewById(R.id.ll_alipay).setOnClickListener(this);

        mRadiaWechat = (RadioButton)findViewById(R.id.radio_wechat);
        findViewById(R.id.ll_wechat).setOnClickListener(this);

        mRadiaBank = (RadioButton)findViewById(R.id.radio_bank);
        findViewById(R.id.ll_netbank).setOnClickListener(this);
        mBtnActive = (Button)findViewById(android.R.id.button2);
        mBtnActive.setOnClickListener(this);
        surePayBox = (CheckBox)findViewById(R.id.box_sure_pay)    ; 
        txtPayForInfo = (TextView)findViewById(R.id.txt_pay_for_info) ; 
        btnPay = (Button)findViewById(android.R.id.button1) ; 
        surePayBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setPayCanClick(isChecked) ; 
            }
        }) ;
        findViewById(R.id.txt_pay_for_info).setOnClickListener(this) ;
        setPayCanClick(false) ; 
    }
    private void setPayCanClick(boolean can){
//        if(can){
//            btnPay.setClickable(true) ; 
//            btnPay.setAlpha(1) ; 
//        }
//        else{
//            btnPay.setClickable(false) ; 
//            btnPay.setAlpha(0.3f) ; 
//        }
        canPayNow = can ; 
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txt_pay_for_info:
                if(model!=null){
                    Intent intent = new Intent(Intent.ACTION_DIAL) ; 
                    intent.setData(Uri.parse("tel:"+model.phone)) ; 
                    startActivity(intent); 
                }
                break ; 
            case android.R.id.button2:// 激活衣付通
                Intent yftIntent = new Intent(getApplicationContext(), YFTActivity.class);
                startActivity(yftIntent);
                break;
            case android.R.id.button1:
                if(!canPayNow){
                    ViewHub.showLongToast(v.getContext(), "请先勾选上面提示！") ; 
                    return ; 
                }
                if (mRadiaWechat.isChecked() || mRadiaAlipay.isChecked() || mRadiaYFT.isChecked()
                        || mRadiaBank.isChecked()) {
                    PaymentHelper paymentHelper = new PaymentHelper(this, new PaymentListener() {
                        @Override
                        public void paySuccess(String orderId , Object obj) {
                            Log.i("OrderPayActivity", "paySuccess orderId:" + orderId);
                            paySuccessSyned() ; 
                        }
                        @Override
                        public void payFail(String orderId, String erroMsg) {
                            Log.i("OrderPayActivity", "payFail orderId:" + orderId + " erroMsg:" + erroMsg);
                        }
                    });
                    
                    PaymentType type = null;
                    if (mRadiaYFT.isChecked()) {// 衣付通
                        type = PaymentType.YFT;
                    } else if (mRadiaAlipay.isChecked()) {// 支付宝支付
                        type = PaymentType.ALIPAY;
                    } else if (mRadiaBank.isChecked()) {// 网银支付
                        type = PaymentType.NETBANK;
                    } else if (mRadiaWechat.isChecked()) {
                        type = PaymentType.WECHAT;
                    }
                    paymentHelper.pay(type, mOrderPayId + "",0,true);
                } else {
                    ViewHub.showShortToast(getApplicationContext(), "请先选择付款方式");
                }
                break;
            case R.id.ll_yft:
                boolean isOpen = UserInfoProvider.hasOpenedYFT(getApplicationContext(),
                        SpManager.getUserId(getApplicationContext()));
                if (!isOpen) {
                    ViewHub.showShortToast(getApplicationContext(), "衣付通支付方式未激活，请您激活后再使用...");
                    return;
                }
                double balance = UserInfoProvider.getUserBalance(getApplicationContext(),
                        SpManager.getUserId(getApplicationContext()));
                if (mOrderMoney > balance) {
                    ViewHub.showShortToast(getApplicationContext(), "衣付通余额不足，请选择其他付款方式...");
                    return;
                }
                mRadiaYFT.setChecked(true);
                mRadiaAlipay.setChecked(false);
                mRadiaWechat.setChecked(false);
                mRadiaBank.setChecked(false);

                break;
            case R.id.ll_alipay:
                mRadiaYFT.setChecked(false);
                mRadiaAlipay.setChecked(true);
                mRadiaWechat.setChecked(false);
                mRadiaBank.setChecked(false);
                break;
            case R.id.ll_wechat:
                mRadiaYFT.setChecked(false);
                mRadiaAlipay.setChecked(false);
                mRadiaWechat.setChecked(true);
                mRadiaBank.setChecked(false);
                break;
            case R.id.ll_netbank:
                mRadiaYFT.setChecked(false);
                mRadiaAlipay.setChecked(false);
                mRadiaWechat.setChecked(false);
                mRadiaBank.setChecked(true);
                break;
        }
    }
    public void onEventMainThread(BusEvent event) {
        switch (event.id) {
            case EventBusId.WECHAT_PAY_SUCCESS:
                paySuccessSyned() ;
                break;

            case EventBusId.BANK_PAY_SUCCESS:
                OrderPaidChecker checker = new OrderPaidChecker(this, new IOrderCheckListener() {
                    @Override
                    public void orderPaySuccess(String orderId , Object obj) {
                        paySuccessSyned() ; 
                    }

                    @Override
                    public void orderPayFail(String orderId, String failMsg) {}
                });
                checker.checkOrderStatus(mOrderPayId + "");
                break;
            case EventBusId.BANK_PAY_FAIL:
                ViewHub.showShortToast(getApplicationContext(), "支付失败");
                paySuccessSyning() ; 
                break;
        }
    }

    @Override
    protected void onActivityResult(int arg0, int arg1, Intent arg2) {
        if (arg1 == RESULT_OK) {
            switch (arg0) {
                case CODE_RESULT_WECHAT_PAY:// 微信支付成功
                    paySuccessSyned() ;
                    break;
                default:
                    break;
            }
        }
        super.onActivityResult(arg0, arg1, arg2);
    }

    private class Task extends AsyncTask<Object, Object, Object> {

        private JPayUser mPayUser;

        @Override
        protected void onPreExecute() {
            if (mLoadingDialog == null) {
                mLoadingDialog = new LoadingDialog(PayForOtherActivity.this);
            }
            mLoadingDialog.setMessage("正在加载账户信息...");

            mLoadingDialog.show();
            super.onPreExecute();
        }

        @Override
        protected Object doInBackground(Object... params) {
            TreeMap<String, String> p = new TreeMap<String, String>();
            p.put("orderID", String.valueOf(mOrderPayId));
            SafeUtils.genCommonSinParams(getApplicationContext(), p);
//            Map<String , String> p = new HashMap<String , String> ()  ; 
//            p.put("orderID", String.valueOf(mOrderPayId)) ; 
            PayForModel model = null ; 
            try {
                String json = HttpUtils.httpPost("shop/agent/order/GetOrderReplacePayInfo", p , PublicData.getCookie(getApplicationContext())) ;
                model = GsonHelper.jsonToObject(json, PayForModel.class) ; 
            } catch (Exception e1) {
                e1.printStackTrace();
            }
            if(model!=null){
                publishProgress(model) ; 
                
            }
            else{
                finish() ;
                return "error:代付链接已失效！"; 
            }
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
        protected void onProgressUpdate(Object... values) {
            DecimalFormat df = new DecimalFormat("0.00") ; 
            model = (PayForModel)values[0] ; 
            StringBuilder builder = new StringBuilder() ; 
            builder.append("好友");
            builder.append(model.buyer) ; 
            if(!TextUtils.isEmpty(model.phone)){
                builder.append("(手机:") ; 
                builder.append(model.phone) ; 
                builder.append(")") ; 
            }
            builder.append("请求您帮忙付款") ; 
            builder.append("<font color=\"#fa2a1a\">");
            builder.append(df.format(model.money)) ; 
            builder.append("</font>元<br/><br/>所购商品:") ;
            builder.append(model.product) ; 
            txtPayForInfo.setText(Html.fromHtml(builder.toString())) ; 
        }

        @Override
        protected void onPostExecute(Object result) {
            hideDialog();
            if (result instanceof String && ((String)result).startsWith("error:")) {
                ViewHub.showLongToast(getApplicationContext(), ((String)result).replace("error:", ""));
            } else {
                initPay();
            }
            super.onPostExecute(result);
        }
    }
    private void paySuccessSyning(String prompt){
        LinearLayout parent = new LinearLayout(this) ; 
        parent.setOrientation(LinearLayout.VERTICAL) ; 
        txtPaySuccessSyn = new TextView(this) ; 
        txtPaySuccessSyn.setTextColor(getResources().getColor(R.color.green_price)) ; 
        txtPaySuccessSyn.setText(prompt) ; 
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams
                (LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT) ; 
        params.gravity = Gravity.CENTER_HORIZONTAL ; 
        int topMargin = FunctionHelper.dip2px(getResources(), 36) ; 
        int topMargin1 = FunctionHelper.dip2px(getResources(), 10) ; 
        params.topMargin = topMargin ; 
        parent.addView(txtPaySuccessSyn , params) ; 
        
        TextView toUser = new TextView(this) ; 
        toUser.setPadding(0, topMargin1, 0, topMargin1) ; 
        toUser.setGravity(Gravity.CENTER_VERTICAL) ; 
        String tell = "告诉好友："+model.buyer ;
        SpannableString span = new SpannableString(tell) ; 
        span.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.blue))
            , 5, tell.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE) ; 
        toUser.setText(span) ; 
        LinearLayout.LayoutParams param1  = new LinearLayout.LayoutParams
                (LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT) ; 
        param1.gravity = Gravity.CENTER_HORIZONTAL ; 
        param1.topMargin = topMargin1 ; 
        parent.addView(toUser , param1) ; 
        toUser.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(v.getContext() , UserInfoActivity.class) ;
//                intent.putExtra(UserInfoActivity.EXTRA_USER_ID, model.buyerUserID) ;
//                v.getContext().startActivity(intent) ;
            }
        }) ;
        setTitle("代付结果") ; 
        setContentView(parent) ; 
    }
    public void paySuccessSyning(){
        paySuccessSyning("您已成功完成支付，订单正在同步状态中,请你稍候再查看！") ; 
    }
    private void paySuccessSyned(){
        if(txtPaySuccessSyn!=null){
            txtPaySuccessSyn.setText("你已成功完成支付！") ; 
        }
        else{
            paySuccessSyning("你已成功完成支付！") ; 
        }
    }
    private void initPay() {
        int userId = SpManager.getUserId(getApplicationContext());
        boolean isOpen = UserInfoProvider.hasOpenedYFT(getApplicationContext(), userId);
        double balance = UserInfoProvider.getUserBalance(getApplicationContext(), userId);
        mRadiaYFT.setEnabled(balance >= mOrderMoney);
        mTvAvMoney.setText(Html.fromHtml(getString(R.string.available_balance, Utils.moneyFormat(balance))));
        mBtnActive.setVisibility(isOpen ? View.GONE : View.VISIBLE);
    }

    @Override
    public void onBackClick(View v) {
        super.onBackClick(v);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
