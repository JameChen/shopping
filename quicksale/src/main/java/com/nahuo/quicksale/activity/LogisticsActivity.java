package com.nahuo.quicksale.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.nahuo.bean.LogisticsBean;
import com.nahuo.library.controls.LoadingDialog;
import com.nahuo.quicksale.R;
import com.nahuo.quicksale.ViewHub;
import com.nahuo.quicksale.adapter.ExpressInfoAdapter;
import com.nahuo.quicksale.adapter.LogisticsAdpater;
import com.nahuo.quicksale.api.HttpRequestHelper;
import com.nahuo.quicksale.api.HttpRequestListener;
import com.nahuo.quicksale.api.RequestMethod;
import com.nahuo.quicksale.base.BaseAppCompatActivity;
import com.nahuo.quicksale.common.ListUtils;
import com.nahuo.quicksale.common.Utils;
import com.nahuo.quicksale.oldermodel.ResultData;

import java.util.ArrayList;
import java.util.List;

public class LogisticsActivity extends BaseAppCompatActivity implements View.OnClickListener, HttpRequestListener {
    LogisticsActivity vThis;
    TextView tvTitleCenter;
    public static String ETRA_SHIPID = "ETRA_SHIPID";
    public static String ETRA_ORDER_ID = "ETRA_ORDER_ID";
    int shipId, orderId;
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    HttpRequestHelper httpRequestHelper = new HttpRequestHelper();
    private LoadingDialog mLoadingDialog;
    private LogisticsAdpater adapter;
    private TextView tv_order_tittle, tv_look_logistics;
    private View layout_look_logistics,layout_received;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logistics);
        initView();
        initData();
        getHttpOderData();
    }

    private void getHttpOderData() {
        HttpRequestHelper.HttpRequest request = httpRequestHelper.getRequest(getApplicationContext(),
                RequestMethod.OrderDetailMethod.GET_PACKAGE_INFOV2, vThis);
        request.setConvert2Class(LogisticsBean.class);
        request.addParam("shipid", String.valueOf(shipId));
        request.addParam("phOrderId", String.valueOf(orderId));
//        request.addParam("type", "2");
        request.doPost();
    }

    private void initData() {
        shipId = getIntent().getIntExtra(ETRA_SHIPID, 0);
        orderId = getIntent().getIntExtra(ETRA_ORDER_ID, 0);
    }

    private void initView() {
        vThis = this;
        mLoadingDialog = new LoadingDialog(this);
        findViewById(R.id.tvTLeft).setOnClickListener(this);
        tvTitleCenter = (TextView) findViewById(R.id.tvTitleCenter);
        tvTitleCenter.setText(R.string.logistics_detail_tittle);
        layout_received = findViewById(R.id.layout_received);
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_list);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeLayout);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorAccent, R.color.lightcolorAccent, android.R.color.holo_blue_dark, android.R.color.holo_blue_light);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        initAdapter();
        initRefreshLayout();
        mSwipeRefreshLayout.setRefreshing(true);
    }

    private void initRefreshLayout() {
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                onRefreshData();
            }
        });
    }

    private void onRefreshData() {
        getHttpOderData();
    }

    /**
     * 添加頭部
     *
     * @author James Chen
     * @create time in 2018/3/7 11:04
     */
    TextView tv_logistics_code, tv_logistics_company, tv_logistics_count_weight, tv_logistics_time, tv_copy_code;

    private void addConsigneeInfoHeadView(final LogisticsBean logisticsBean) {
        View headView = getLayoutInflater().inflate(R.layout.order_detail_logistics_head, (ViewGroup) mRecyclerView.getParent(), false);
        tv_order_tittle = (TextView) headView.findViewById(R.id.tv_order_tittle);
        layout_look_logistics = headView.findViewById(R.id.layout_look_logistics);
        tv_look_logistics = (TextView) headView.findViewById(R.id.tv_look_logistics);
        tv_look_logistics.setOnClickListener(this);
        tv_logistics_code = (TextView) headView.findViewById(R.id.tv_logistics_code);
        tv_logistics_company = (TextView) headView.findViewById(R.id.tv_logistics_company);
        tv_logistics_count_weight = (TextView) headView.findViewById(R.id.tv_logistics_count_weight);
        tv_logistics_time = (TextView) headView.findViewById(R.id.tv_logistics_time);
        tv_copy_code = (TextView) headView.findViewById(R.id.tv_copy_code);

        tv_copy_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (logisticsBean != null) {
                    if (TextUtils.isEmpty(logisticsBean.getInfo().getCode()))
                        ViewHub.showShortToast(vThis, "单号为空");
                    else {
                        Utils.addNewToClipboard(vThis, logisticsBean.getInfo().getCode());
                    }
                } else {
                    ViewHub.showShortToast(vThis, "单号为空");
                }

            }
        });
        if (orderId <= 0) {
            layout_look_logistics.setVisibility(View.GONE);
            tv_order_tittle.setText("当前包裹所有已发商品");
        } else {
            if (logisticsBean.getInfo() != null) {
                tv_order_tittle.setText("当前订单(" + logisticsBean.getInfo().getPhOrderCode() + ")已发商品");
            }
            layout_look_logistics.setVisibility(View.VISIBLE);
        }
        if (logisticsBean.getInfo() != null) {
            if (TextUtils.isEmpty(logisticsBean.getInfo().getCode()))
                tv_logistics_code.setText("");
            else
                tv_logistics_code.setText(logisticsBean.getInfo().getCode());
            tv_logistics_company.setText(logisticsBean.getInfo().getName());
            if (orderId <= 0) {
                tv_logistics_count_weight.setTextSize(15);
                tv_logistics_count_weight.setText(logisticsBean.getInfo().getSummary());
            } else {
                if (TextUtils.isEmpty(logisticsBean.getInfo().getSummary())) {
                    tv_logistics_count_weight.setText("");
                } else {
                    tv_logistics_count_weight.setText(Html.fromHtml(
                            "<font color= '#d52831'>当前订单 </font>" + "<font color= '#ee000000'>" + "<big>" + logisticsBean.getInfo().getSummary() + "</big></font> " + "<br/><font color= '#929292'>(仅含当前订单商品，查看整个包裹请点击顶部按钮)</font>"));
                }
            }

            tv_logistics_time.setText(logisticsBean.getInfo().getShipTime());

        }
        adapter.addHeaderView(headView);
    }

    /**
     * 添加物流頭部
     *
     * @author James Chen
     * @create time in 2018/3/7 11:04
     */
    RecyclerView expressView;
    ExpressInfoAdapter expressInfoAdapter;

    private void addPackageHeadView(LogisticsBean logisticsBean) {
        View headView = getLayoutInflater().inflate(R.layout.order_detail_logistics_head_expressinfo, (ViewGroup) mRecyclerView.getParent(), false);
        expressView = (RecyclerView) headView.findViewById(R.id.express_list);
        expressView.setLayoutManager(new LinearLayoutManager(this));
        ArrayList<MultiItemEntity> res = new ArrayList<>();
        LogisticsBean.ExpressInfoParentListBean parent = logisticsBean.getExpressInfoParentListBean();
        int size = 0;
        if (!ListUtils.isEmpty(logisticsBean.getExpressInfoList())) {
            size = logisticsBean.getExpressInfoList().size();
            List<LogisticsBean.ExpressInfoListBean> list = new ArrayList<>();
            for (int i = 0; i < logisticsBean.getExpressInfoList().size(); i++) {
                LogisticsBean.ExpressInfoListBean ordersBean = logisticsBean.getExpressInfoList().get(i);
                if (i == 0) {
                    parent.setFirstInfoListBean(ordersBean);
                } else {
                    list.add(ordersBean);
                }
            }
            parent.setSubItems(list);
        }
        parent.setExpressInfoListSize(size);
        res.add(parent);
        expressInfoAdapter = new ExpressInfoAdapter(res, vThis);
        expressView.setAdapter(expressInfoAdapter);
        //expressInfoAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_LEFT);
        // expressInfoAdapter.expandAll();
        adapter.addHeaderView(headView);
    }

    /**
     * 添加底部
     *
     * @author James Chen
     * @create time in 2018/3/7 11:04
     */
    TextView txt_order_info_money, txt_order_info_buyway, txt_order_info_buyway_weight,
            tv_bottom_line1, tv_bottom_line2;
    View layout_fee;

    private void addBottomFootView(LogisticsBean logisticsBean) {
        View headView = getLayoutInflater().inflate(R.layout.order_detail_logistics_foot, (ViewGroup) mRecyclerView.getParent(), false);
        txt_order_info_money = (TextView) headView.findViewById(R.id.txt_order_info_money);
        txt_order_info_buyway = (TextView) headView.findViewById(R.id.txt_order_info_buyway);
        txt_order_info_buyway_weight = (TextView) headView.findViewById(R.id.txt_order_info_buyway_weight);
        tv_bottom_line1 = (TextView) headView.findViewById(R.id.tv_bottom_line1);
        tv_bottom_line2 = (TextView) headView.findViewById(R.id.tv_bottom_line2);
        layout_fee = headView.findViewById(R.id.layout_fee);
        if (logisticsBean.getInfo() != null) {
            LogisticsBean.InfoBean orderDetailBean = logisticsBean.getInfo();
            txt_order_info_money.setText("¥" + orderDetailBean.getProductAmount());
            if (TextUtils.isEmpty(orderDetailBean.getPostFee()) || orderDetailBean.getPostFee().equals("0.00") || orderDetailBean.getPostFee().equals("0") || orderDetailBean.getPostFee().equals("0.0")) {
                layout_fee.setVisibility(View.GONE);
            } else {
                layout_fee.setVisibility(View.VISIBLE);
            }
            txt_order_info_buyway.setText("¥" + orderDetailBean.getPostFee());
            txt_order_info_buyway_weight.setText(orderDetailBean.getWeight());
            tv_bottom_line1.setText("总款式：" + orderDetailBean.getIemCount() + "款，" + "总件数：" + orderDetailBean.getShipQty() + "件");
            tv_bottom_line2.setText(Html.fromHtml("<font color='#ee000000'>本次合计：</font>" + "<font color='#d52831'>¥" + orderDetailBean.getTotalShipAmount() + "</font>"));
        }
        adapter.addFooterView(headView);
    }

    private void addReceivedBottomFootView(final LogisticsBean logisticsBean) {
        if (logisticsBean!=null){
            if (logisticsBean.isReceivedButton())
                layout_received.setVisibility(View.VISIBLE);
            else
                layout_received.setVisibility(View.GONE);
        }else {
            layout_received.setVisibility(View.GONE);
        }
        layout_received.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(vThis, PackageReceivedActivity.class);
                intent.putExtra(PackageReceivedActivity.EXTRA_SHIPID, shipId);
                vThis.startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getHttpOderData();
    }



    private void initAdapter() {
        adapter = new LogisticsAdpater(null, this);
        //adapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_LEFT);
//        mAdapter.setPreLoadNumber(3);
        mRecyclerView.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvTLeft:
                finish();
                break;
            case R.id.tv_look_logistics:
                Intent intent = new Intent(vThis, LogisticsActivity.class);
                intent.putExtra(LogisticsActivity.ETRA_SHIPID, shipId);
                intent.putExtra(LogisticsActivity.ETRA_ORDER_ID, 0);
                vThis.startActivity(intent);
                break;
        }

    }

    @Override
    public void onRequestStart(String method) {
        if (RequestMethod.OrderDetailMethod.GET_PACKAGE_INFOV2.equals(method)) {
            if (!isDialogShowing())
                mLoadingDialog.start("获取物流详情信息...");
        }
    }

    @Override
    public void onRequestSuccess(String method, Object object) {
        hideDialog();
        if (RequestMethod.OrderDetailMethod.GET_PACKAGE_INFOV2.equals(method)) {
            mSwipeRefreshLayout.setRefreshing(false);
            try {
                LogisticsBean bean = (LogisticsBean) object;
                adapter.setNewData(null);
                //adapter.setOrderDetailBean(bean);
                adapter.removeAllHeaderView();
                adapter.removeAllFooterView();
                addReceivedBottomFootView(bean);
                if (bean != null) {
                    if (bean.getInfo() != null) {
                        addConsigneeInfoHeadView(bean);
                    }
                    addPackageHeadView(bean);
                    ArrayList<MultiItemEntity> res = new ArrayList<>();
                    if (!ListUtils.isEmpty(bean.getOrders())) {
                        bean.getOrdersParentBean().setSubItems(bean.getOrders());
                        for (int i = 0; i < bean.getOrders().size(); i++) {
                            LogisticsBean.OrdersBean ordersBean = bean.getOrders().get(i);
                            if (i == 0) {
                                ordersBean.isShowTop = false;
                            } else {
                                ordersBean.isShowTop = true;
                            }
                        }
                        res.add(bean.getOrdersParentBean());
                    }
                    adapter.setNewData(res);
                    adapter.expandAll();
                    if (bean.getInfo() != null) {
                        addBottomFootView(bean);
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onRequestFail(String method, int statusCode, String msg) {
        hideDialog();
        if (RequestMethod.OrderDetailMethod.GET_PACKAGE_INFOV2.equals(method)) {
            mSwipeRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void onRequestExp(String method, String msg, ResultData data) {
        hideDialog();
        if (RequestMethod.OrderDetailMethod.GET_PACKAGE_INFOV2.equals(method)) {
            mSwipeRefreshLayout.setRefreshing(false);
        }
    }

    protected boolean isDialogShowing() {
        return (mLoadingDialog != null && mLoadingDialog.isShowing());
    }

    protected void hideDialog() {
        if (isDialogShowing()) {
            mLoadingDialog.stop();
        }
    }

}
