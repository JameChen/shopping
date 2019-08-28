package com.nahuo.quicksale.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import com.nahuo.quicksale.OrderManageActivity;
import com.nahuo.quicksale.PHQDActivity;
import com.nahuo.quicksale.R;
import com.nahuo.quicksale.app.BWApplication;
import com.nahuo.quicksale.base.BaseAppCompatActivity;
import com.nahuo.quicksale.common.Const;
import com.nahuo.quicksale.eventbus.BusEvent;
import com.nahuo.quicksale.eventbus.EventBusId;

import de.greenrobot.event.EventBus;

public class PaySucessActivity extends BaseAppCompatActivity implements View.OnClickListener {
    TextView tv_money, tv_mode, tv_look_order, tv_pinhuo, tv_title, tv_deliver_goods;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_sucess);
        initView();
    }

    private void initView() {
        BWApplication.addActivity(this);
        tv_money = (TextView) findViewById(R.id.tv_money);
        tv_mode = (TextView) findViewById(R.id.tv_mode);
        tv_look_order = (TextView) findViewById(R.id.tv_look_order);
        tv_pinhuo = (TextView) findViewById(R.id.tv_pinhuo);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_deliver_goods = (TextView) findViewById(R.id.tv_deliver_goods);
        tv_deliver_goods.setOnClickListener(this);
        tv_look_order.setOnClickListener(this);
        tv_pinhuo.setOnClickListener(this);
        tv_title.setText("订单支付成功");
        tv_money.setText(Html.fromHtml("<font color='#FF252323'>支付金额: </font>" + "<font color='#ff0000' >" + "¥" + BWApplication.PayMoney + "</font>"));
        tv_mode.setText(Html.fromHtml("<font color='#FF252323'>支付方式: </font>" + "<font color='#ff0000' >" + BWApplication.PayMode + "</font>"));

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_look_order:
                BWApplication.reBackFirst();
                //EventBus.getDefault().post(BusEvent.getEvent(EventBusId.CANCEL_ORDER));
                Intent it = new Intent(getApplicationContext(), OrderManageActivity.class);
                it.putExtra(OrderManageActivity.EXTRA_ORDER_TYPE, Const.OrderStatus.ALL_PAY);
//                                it.putExtra(BaseOrderDetailActivity.EXTRA_ORDER_ID, Integer.parseInt(mOrderPayId));
                startActivity(it);
                break;
            case R.id.tv_pinhuo:
                BWApplication.reBackFirst();
                EventBus.getDefault().post(BusEvent.getEvent(EventBusId.MAIN_CURRENT_TAB));
                break;
            case R.id.tv_deliver_goods:
                BWApplication.reBackFirst();
                Intent phqd = new Intent(getApplicationContext(), PHQDActivity.class);
                startActivity(phqd);
                break;
        }
    }
}