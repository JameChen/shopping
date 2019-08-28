package com.nahuo.quicksale.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.nahuo.quicksale.R;
import com.nahuo.quicksale.adapter.ShopItemmAdapter;
import com.nahuo.quicksale.base.BaseAppCompatActivity;
import com.nahuo.quicksale.common.ListUtils;
import com.nahuo.quicksale.oldermodel.TempOrder;

public class ShopItemActivity extends BaseAppCompatActivity implements View.OnClickListener {
    TextView tvTitle, titlebar_right;
    public static String TempOrder_Extra = "TempOrder_Extra";
    ListView lv_order;
    TempOrder tempOrder;
    ShopItemmAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_item);
        Button btnLeft = (Button) findViewById(R.id.titlebar_btnLeft);
        btnLeft.setVisibility(View.VISIBLE);
        btnLeft.setOnClickListener(this);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        titlebar_right = (TextView) findViewById(R.id.titlebar_right);
        lv_order = (ListView) findViewById(R.id.lv_order);
        tvTitle.setText("商品清单");
        tempOrder = (TempOrder) getIntent().getSerializableExtra(TempOrder_Extra);
        if (tempOrder != null) {
            titlebar_right.setVisibility(View.VISIBLE);
            titlebar_right.setText("共" + tempOrder.getTotalQty() + "件");
            adapter = new ShopItemmAdapter(this);
            if (!ListUtils.isEmpty(tempOrder.getItems())) {
                adapter.setData(tempOrder.getItems());
            }
            lv_order.setAdapter(adapter);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.titlebar_btnLeft:
                finish();
                break;
        }
    }
}
