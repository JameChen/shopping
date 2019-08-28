//package com.nahuo.quicksale;
//
//import com.nahuo.quicksale.adapter.SubmitOrderResultAdapter;
//import com.nahuo.quicksale.adapter.SubmitOrderResultAdapter.PayOnClickListener;
//import com.nahuo.quicksale.oldermodel.SubmitOrderResult;
//import com.nahuo.quicksale.oldermodel.SubmitOrderResult.OrderPay;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.text.Html;
//import android.view.View;
//import android.widget.ListView;
//import android.widget.TextView;
//
///**
// * @description 订单提交结果
// * @created 2015年4月27日 上午11:46:02
// * @author JorsonWong
// */
//public class SubmitOrderResultActivity extends BaseActivity2 implements PayOnClickListener {
//
//    private ListView                 mListView;
//    private SubmitOrderResult        mOrderResult;
//    private View                     mHeader;
//    private TextView                 mTvSuccess, mTvFail;
//    private SubmitOrderResultAdapter mAdapter;
//    public static final String       INTENT_SUBMIT_RESULT = "intent_submit_result";
//    private static final int         CODE_PAY_ORDER       = 0;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_submit_order_result);
//        setTitle("订单提交结果");
//        mOrderResult = (SubmitOrderResult)getIntent().getSerializableExtra(INTENT_SUBMIT_RESULT);
//
//        initView();
//        init();
//    }
//
//    private void initView() {
//        mListView = (ListView)findViewById(android.R.id.list);
//        mHeader = View.inflate(this, R.layout.layout_submit_order_result_header, null);
//        mTvSuccess = (TextView)mHeader.findViewById(R.id.tv_success);
//        mTvFail = (TextView)mHeader.findViewById(R.id.tv_fail);
//        mListView.addHeaderView(mHeader);
//    }
//
//    private void init() {
//        mTvSuccess.setText(Html.fromHtml(getString(R.string.submit_order_success_x)));
//        mTvFail.setText(Html.fromHtml(getString(R.string.submit_order_fail_x, 0)));
//        mTvFail.setVisibility(0 > 0 ? View.VISIBLE : View.GONE);
//
//        mAdapter = new SubmitOrderResultAdapter(this);
//        mAdapter.setPayOnClickListener(this);
////        mAdapter.setData(mOrderResult.OrderList);
//        mListView.setAdapter(mAdapter);
//    }
//
//    @Override
//    public void payOrder(OrderPay orderPay) {
//        Intent it = new Intent(getApplicationContext(), OrderPayActivity.class);
//        it.putExtra(OrderPayActivity.INTENT_PAY_ORDER_ID, orderPay.OrderID);
//        it.putExtra(OrderPayActivity.INTENT_PAY_MONEY, orderPay.PayableAmount);
//        startActivityForResult(it, CODE_PAY_ORDER);
//
//    }
//
//    @Override
//    protected void onActivityResult(int arg0, int arg1, Intent arg2) {
//        if (arg1 == RESULT_OK) {
//            switch (arg0) {
//                case CODE_PAY_ORDER:
//
//                    break;
//                default:
//                    break;
//            }
//        }
//        super.onActivityResult(arg0, arg1, arg2);
//    }
//
//}
