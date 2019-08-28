package com.nahuo.quicksale;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.nahuo.library.controls.LoadingDialog;
import com.nahuo.quicksale.api.AccountAPI;
import com.nahuo.quicksale.api.HttpRequestHelper;
import com.nahuo.quicksale.api.HttpRequestListener;
import com.nahuo.quicksale.oldermodel.ResultData;
import com.nahuo.quicksale.mvp.BaseMVPActivity;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * Created by ZZB on 2015/8/26.
 */
public class ScanLoginActivity extends BaseMVPActivity implements View.OnClickListener {
    public static final String EXTRA_UID = "EXTRA_UID";
    public static final String EXTRA_DEVICE = "EXTRA_DEVICE";
    private String mUid, mDeviceName;
    private HttpRequestHelper mRequestHelper = new HttpRequestHelper();
    private LoadingDialog mLoadingDialog;
    private Context mContext = this;
    private TextView mTvContent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_login);
        initViews();
        initExtras();
    }

    private void initExtras(){
        Intent intent = getIntent();
        mUid = intent.getStringExtra(EXTRA_UID);
        mDeviceName = intent.getStringExtra(EXTRA_DEVICE);
        try {
            mDeviceName = URLDecoder.decode(mDeviceName, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        mTvContent.setText(getString(R.string.scan_login_device, mDeviceName));
    }

    private void initViews() {
        mTvContent = $(R.id.tv_content);
        mLoadingDialog = new LoadingDialog(this);
        setTitle("扫码登录");
        enableNavigationIcon();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_confirm:
                scanLogin();
                break;
        }
    }

    private void scanLogin() {
        AccountAPI.scanLogin(this, mRequestHelper, new HttpRequestListener() {
            @Override
            public void onRequestStart(String method) {
                mLoadingDialog.start("登录中");
            }
            @Override
            public void onRequestSuccess(String method, Object object) {
                mLoadingDialog.stop();
                //
                ViewHub.showShortToast(mContext, "登录成功");
            }

            @Override
            public void onRequestFail(String method, int statusCode, String msg) {
                mLoadingDialog.stop();
                ViewHub.showShortToast(mContext, "登录失败");
            }

            @Override
            public void onRequestExp(String method, String msg, ResultData data) {
                mLoadingDialog.stop();
                ViewHub.showShortToast(mContext, "登录失败");
            }
        }, mUid);
    }
}
