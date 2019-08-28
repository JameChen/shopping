package com.nahuo.quicksale.api;

import com.nahuo.library.controls.LoadingDialog;
import com.nahuo.quicksale.oldermodel.ResultData;

/**
 * Created by ZZB on 2015/10/13.
 */
public abstract class SimpleHttpRequestListener implements HttpRequestListener {
    public void showDialog(LoadingDialog mLoadingDialog, String mess) {
        if (mLoadingDialog != null) {
            mLoadingDialog.setMessage(mess);
            mLoadingDialog.show();
        }
    }
    public void closeDialog(LoadingDialog mLoadingDialog) {
        if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
            mLoadingDialog.dismiss();
        }
    }
    @Override
    public void onRequestStart(String method) {

    }

    @Override
    public abstract void onRequestSuccess(String method, Object object);

    @Override
    public void onRequestFail(String method, int statusCode, String msg) {

    }

    @Override
    public void onRequestExp(String method, String msg, ResultData data) {

    }
}
