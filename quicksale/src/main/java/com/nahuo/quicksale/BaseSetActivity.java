package com.nahuo.quicksale;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

import com.baidu.mobstat.StatService;
import com.nahuo.library.controls.LightAlertDialog;
import com.nahuo.library.controls.LoadingDialog;
import com.nahuo.library.controls.SingleLineItem;
import com.nahuo.library.controls.SwitchButton;
import com.nahuo.quicksale.api.AccountAPI;
import com.nahuo.quicksale.api.BuyOnlineAPI;
import com.nahuo.quicksale.common.SpManager;
import com.nahuo.quicksale.oldermodel.BaseSettings;
import com.nahuo.quicksale.provider.ContactInfoProvider;

public class BaseSetActivity extends BaseActivity implements
        OnClickListener, OnCheckedChangeListener {

    private Context mContext = this;
    private Button btnLeft;
    private TextView tvTitle;
    private LoadingDialog mLoadingDialog;
    
    private SwitchButton mBtnChangeRetail, mBtnAllowShip, mBtnUseMyPhone;
    private SingleLineItem mItemAllowShip, mItemUseMyPhone;
    

    private static enum Step {
        LOAD_DATA, CHANGE_RETAIL_PRICE, ALLOW_AGENT_SHIP, USE_MY_PHONE
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);// 设置自定义标题栏
        setContentView(R.layout.activity_base_set);
/*        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
                R.layout.layout_titlebar_default);// 更换自定义标题栏布局*/

        initView();
        loadData();
    }

    private void loadData() {
        new Task(Step.LOAD_DATA).execute();
    }

    private void initView() {
        mLoadingDialog = new LoadingDialog(this);
        // 标题栏
        tvTitle = (TextView) findViewById(R.id.titlebar_tvTitle);

        tvTitle.setText("代理设置");
        btnLeft = (Button) findViewById(R.id.titlebar_btnLeft);
        btnLeft.setText(R.string.titlebar_btnBack);
        btnLeft.setVisibility(View.VISIBLE);
        btnLeft.setOnClickListener(this);

        mBtnChangeRetail = (SwitchButton) findViewById(R.id.btn_change_retail);
        mBtnAllowShip = (SwitchButton) findViewById(R.id.btn_allow_agent_ship);
        mBtnUseMyPhone = (SwitchButton) findViewById(R.id.btn_use_my_phone);

        mItemAllowShip = (SingleLineItem) findViewById(R.id.item_allow_agent_ship);
        mItemUseMyPhone = (SingleLineItem) findViewById(R.id.item_use_my_phone);
        
        mItemUseMyPhone.setLeftText(Html.fromHtml("收件人的电话用我的<br><font color='#C5C5C5'>快递先电话我，再由我联系客户收货</font>"));
        mItemAllowShip.setLeftText(Html.fromHtml("允许代理自行发货<br><font color='#C5C5C5'>开启时允许让代理自己来拿货、发货</font>"));

    }

    

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.titlebar_btnLeft:
            finish();
            break;
        }
    }

    private class Task extends AsyncTask<Object, Void, Object> {

        private Step mStep;

        public Task(Step step) {
            mStep = step;
        }

        @Override
        protected void onPreExecute() {
            switch (mStep) {
            case CHANGE_RETAIL_PRICE:
            case ALLOW_AGENT_SHIP:
            case USE_MY_PHONE:
                mLoadingDialog.start("正在保存...");
                break;
            case LOAD_DATA:
                mLoadingDialog.start("加载数据中...");
                break;
            }

        }

        @Override
        protected Object doInBackground(Object... params) {
            try {
                switch (mStep) {
                case CHANGE_RETAIL_PRICE:
                    AccountAPI.setIsRetailPriceUnified(mContext, !SpManager.getIsRetailPriceUnified(mContext));
                    break;
                case ALLOW_AGENT_SHIP:
                    BuyOnlineAPI.allowAgentShip(mContext, !SpManager.getAllowAgentShip(mContext));
                    break;
                case USE_MY_PHONE:
                    BuyOnlineAPI.consigneePhoneUseMine(mContext,
                            !SpManager.getConsigneeUseMyPhone(mContext));
                    break;
                case LOAD_DATA:
                    BaseSettings obj = AccountAPI.getAllBaseSettings(mContext);
                    return obj;
                }
            } catch (Exception e) {
                e.printStackTrace();
                return "error:" + e.getMessage();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Object result) {
            if (mLoadingDialog.isShowing()) {
                mLoadingDialog.stop();
            }
            if (result instanceof String && ((String) result).startsWith("error:")) {
                ViewHub.showLongToast(mContext, ((String) result).replace("error:", ""));
            } else {
                switch (mStep) {
                case CHANGE_RETAIL_PRICE:
                case ALLOW_AGENT_SHIP:
                    ViewHub.showShortToast(mContext, "设置成功");
                    break;
                case USE_MY_PHONE:
                    ViewHub.showShortToast(mContext, "设置成功");
                    if (!ContactInfoProvider.isPhoneSet(mContext)) {
                        LightAlertDialog.Builder.create(mContext).setTitle("提示")
                                .setMessage("设置成功，但您还未填写联系电话。联系电话未填写前，发货单依然会采用买家联系电话。")
                                .setNegativeButton("关闭", null)
                                .setPositiveButton("设置联系方式", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intent = new Intent(mContext, ContactActivity.class);
                                        startActivity(intent);
                                    }
                                }).show();
                    }
                    break;
                case LOAD_DATA:
                    mBtnAllowShip.setChecked(SpManager.getAllowAgentShip(mContext));
                    mBtnChangeRetail.setChecked(!SpManager.getIsRetailPriceUnified(mContext));
                    mBtnUseMyPhone.setChecked(SpManager.getConsigneeUseMyPhone(mContext));
                    
                    mBtnChangeRetail.setOnCheckedChangeListener(BaseSetActivity.this);
                    mBtnUseMyPhone.setOnCheckedChangeListener(BaseSetActivity.this);
                    mBtnAllowShip.setOnCheckedChangeListener(BaseSetActivity.this);
                    break;
                }

            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        StatService.onPause(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        StatService.onResume(this);
    }

    @Override
    public void finish() {
        super.finish();
        ViewHub.hideKeyboard(this);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
        case R.id.btn_change_retail:// 允许代理修改零售价
            new Task(Step.CHANGE_RETAIL_PRICE).execute(isChecked);
            break;
        case R.id.btn_allow_agent_ship:// 允许代理自行发货
            new Task(Step.ALLOW_AGENT_SHIP).execute(isChecked);
            break;
        case R.id.btn_use_my_phone:// 收件人的电话用我的
            new Task(Step.USE_MY_PHONE).execute(isChecked);
            break;
        }
    }

}
