package com.nahuo.quicksale.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.nahuo.Dialog.CDialog;
import com.nahuo.bean.PackageReceivedBean;
import com.nahuo.quicksale.R;
import com.nahuo.quicksale.ViewHub;
import com.nahuo.quicksale.adapter.PackageReceivedAdapter;
import com.nahuo.quicksale.base.BaseAppCompatActivity;
import com.nahuo.quicksale.di.module.HttpManager;
import com.nahuo.quicksale.model.http.CommonSubscriber;
import com.nahuo.quicksale.model.http.api.PinHuoApi;
import com.nahuo.quicksale.model.http.response.PinHuoResponse;
import com.nahuo.quicksale.util.RxUtil;

public class PackageReceivedActivity extends BaseAppCompatActivity implements View.OnClickListener ,PackageReceivedAdapter.Color_Size_Click{
    private PackageReceivedActivity vThis;
    private TextView tvTitleCenter,tv_received;
    private int  ship_id;
    public static String EXTRA_SHIPID = "SHIPID";
    public static int REQUEST_CODE = 1;
    private RecyclerView mRecyclerView;
    public static String TAG=PackageReceivedActivity.class.getSimpleName();
    private int NoReceivedQty;
    private View layout_package_received;
    private PackageReceivedAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_package_received);
        vThis = this;
        findViewById(R.id.tvTLeft).setOnClickListener(this);
        tvTitleCenter = (TextView) findViewById(R.id.tvTitleCenter);
        tv_received=(TextView) findViewById(R.id.tv_received);
        layout_package_received=findViewById(R.id.layout_package_received);
        layout_package_received.setOnClickListener(this);
        tvTitleCenter.setText(R.string.package_received_tittle);
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_list);
        adapter=new PackageReceivedAdapter(vThis);
        adapter.setColorSizeClick(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(vThis));
        mRecyclerView.setAdapter(adapter);
        if (getIntent() != null) {
            ship_id =  getIntent().getIntExtra(vThis.EXTRA_SHIPID,-1);
        }
        getPackageProductList();
    }
    private void  getPackageProductList(){
        PinHuoApi pinHuoApi = HttpManager.getInstance().getPinHuoNoCacheApi(TAG);
        addSubscribe(pinHuoApi.GetPackageProductList(ship_id)
                .compose(RxUtil.<PinHuoResponse<PackageReceivedBean>>rxSchedulerHelper())
                .compose(RxUtil.<PackageReceivedBean>handleResult())
                .subscribeWith(new CommonSubscriber<PackageReceivedBean>(vThis, true, R.string.loading) {
                    @Override
                    public void onNext(PackageReceivedBean bean) {
                        super.onNext(bean);
                        if (bean!=null){
                            NoReceivedQty=bean.getNoReceivedQty();
                            if (NoReceivedQty>0){
                                layout_package_received.setVisibility(View.VISIBLE);
                            }else {
                                layout_package_received.setVisibility(View.GONE);
                            }
                            tv_received.setText("全部确认收货("+NoReceivedQty+"件)");
                            if (adapter!=null){
                                adapter.setMyData(bean.getList());
                                adapter.notifyDataSetChanged();
                            }
                        }
                    }


                }));
    }
    private void  goToReceivedProduct(String orderProductID){
        PinHuoApi pinHuoApi = HttpManager.getInstance().getPinHuoNoCacheApi(TAG);
        addSubscribe(pinHuoApi.goToReceivedProduct(ship_id,orderProductID)
                .compose(RxUtil.<PinHuoResponse<Object>>rxSchedulerHelper())
                .compose(RxUtil.<Object>handleResult())
                .subscribeWith(new CommonSubscriber<Object>(vThis, true, "点货中....") {
                    @Override
                    public void onNext(Object bean) {
                        super.onNext(bean);
                        if (bean instanceof String) {
                            ViewHub.showShortToast(vThis, bean.toString());
                        }
                        getPackageProductList();
                    }


                }));
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvTLeft:
                finish();
                break;
            case R.id.layout_package_received:
                CDialog dialog = new CDialog(vThis);
                dialog.setHasTittle(true).setTitle("全部确认收货").setMessage("请确认您已收到该包裹的"+NoReceivedQty+"件货物").setPositive("确定", new CDialog.PopDialogListener() {
                    @Override
                    public void onPopDialogButtonClick(int which) {
                        goToReceivedProduct("");
                    }
                }).setNegative("取消", null).show();

                break;
        }
    }

    @Override
    public void onClick(String OrderProductID) {
        goToReceivedProduct(OrderProductID);
    }
}
