package com.nahuo.quicksale.model.http;

import android.content.Context;
import android.text.TextUtils;

import com.nahuo.library.controls.LoadingDialog;
import com.nahuo.library.helper.FunctionHelper;
import com.nahuo.quicksale.ViewHub;
import com.nahuo.quicksale.api.HttpUtils;
import com.nahuo.quicksale.common.Utils;
import com.nahuo.quicksale.model.http.exception.ApiException;
import com.nahuo.quicksale.util.RxUtil;

import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.concurrent.TimeoutException;

import io.reactivex.subscribers.ResourceSubscriber;
import retrofit2.HttpException;

import static com.nahuo.quicksale.util.RxUtil.HttpErrorCode_401;
import static com.nahuo.quicksale.util.RxUtil.HttpErrorCode_403;
import static com.nahuo.quicksale.util.RxUtil.HttpErrorCode_404;
import static com.nahuo.quicksale.util.RxUtil.HttpErrorCode_405;
import static com.nahuo.quicksale.util.RxUtil.HttpErrorCode_500;
import static com.nahuo.quicksale.util.RxUtil.HttpErrorCode_504;
import static com.nahuo.quicksale.util.RxUtil.NOT_REGISTERED;

/**
 * Created by jame on 2018/5/3.
 */

public class CommonSubscriber<T> extends ResourceSubscriber<T> {
    private Context context;
    private String mErrorMsg;
    private boolean isShowErrorState = true;
    private LoadingDialog mLoadingDialog;
    private String loadMsg = "";
    private boolean isShowDialog;
    private int Type = -1;
    public static int IS_RESET_DIALOG = 1;

    @Override
    protected void onStart() {
        super.onStart();
        if (isShowDialog && mLoadingDialog != null && loadMsg != null)
            mLoadingDialog.start(loadMsg);
    }


    private void stopLoadDialog() {
        if (isShowDialog) {
            if (mLoadingDialog != null)
                mLoadingDialog.stop();
        }
    }

    protected CommonSubscriber(Context context, int Type, boolean isShowDialog, String loadMsg) {
        this.context = context;
        this.Type = Type;
        this.isShowDialog = isShowDialog;
        if (isShowDialog) {
            if (Type == IS_RESET_DIALOG) {
                this.mLoadingDialog = new LoadingDialog(this.context);
            } else {
                this.mLoadingDialog = LoadingDialog.getInstance(this.context);
            }
        }
        this.loadMsg = loadMsg;
    }

    protected CommonSubscriber(Context context, int Type, boolean isShowDialog, int loadMsg) {
        this.context = context;
        this.Type = Type;
        this.isShowDialog = isShowDialog;
        if (isShowDialog) {
            if (Type == IS_RESET_DIALOG) {
                this.mLoadingDialog = new LoadingDialog(this.context);
            } else {
                this.mLoadingDialog = LoadingDialog.getInstance(this.context);
            }
        }
        this.loadMsg = context.getResources().getString(loadMsg);
    }

    protected CommonSubscriber(Context context, boolean isShowDialog, String loadMsg) {
        this.context = context;
        if (isShowDialog)
            this.mLoadingDialog = LoadingDialog.getInstance(this.context);
        this.loadMsg = loadMsg;
        this.isShowDialog = isShowDialog;
    }

    protected CommonSubscriber(Context context, boolean isShowErrorState, boolean isShowDialog, String loadMsg) {
        this.context = context;
        this.isShowErrorState = isShowErrorState;
        if (isShowDialog)
            this.mLoadingDialog = LoadingDialog.getInstance(this.context);
        this.loadMsg = loadMsg;
        this.isShowDialog = isShowDialog;
    }

    protected CommonSubscriber(Context context, String errorMsg, boolean isShowErrorState, boolean isShowDialog, String loadMsg) {
        this.context = context;
        this.mErrorMsg = errorMsg;
        this.isShowErrorState = isShowErrorState;
        if (isShowDialog)
            this.mLoadingDialog = LoadingDialog.getInstance(this.context);
        this.loadMsg = loadMsg;
        this.isShowDialog = isShowDialog;
    }

    protected CommonSubscriber(Context context, boolean isShowDialog, int loadMsg) {
        this.context = context;
        if (isShowDialog)
            this.mLoadingDialog = LoadingDialog.getInstance(this.context);
        this.loadMsg = context.getResources().getString(loadMsg);
        this.isShowDialog = isShowDialog;
    }

    protected CommonSubscriber(Context context, boolean isShowErrorState, boolean isShowDialog, int loadMsg) {
        this.context = context;
        this.isShowErrorState = isShowErrorState;
        if (isShowDialog)
            this.mLoadingDialog = LoadingDialog.getInstance(this.context);
        this.loadMsg = context.getResources().getString(loadMsg);
        this.isShowDialog = isShowDialog;
    }

    protected CommonSubscriber(Context context, String errorMsg, boolean isShowErrorState, boolean isShowDialog, int loadMsg) {
        this.context = context;
        this.mErrorMsg = errorMsg;
        this.isShowErrorState = isShowErrorState;
        if (isShowDialog)
            this.mLoadingDialog = LoadingDialog.getInstance(this.context);
        this.loadMsg = context.getResources().getString(loadMsg);
        this.isShowDialog = isShowDialog;
    }

    protected CommonSubscriber(Context context) {
        this.context = context;

    }

    protected CommonSubscriber(Context context, boolean isShowErrorState) {
        this.context = context;
        this.isShowErrorState = isShowErrorState;
    }

    protected CommonSubscriber(Context context, String errorMsg, boolean isShowErrorState) {
        this.context = context;
        this.mErrorMsg = errorMsg;
        this.isShowErrorState = isShowErrorState;
    }

    @Override
    public void onNext(T t) {
        stopLoadDialog();

    }

    @Override
    public void onError(Throwable e) {
        stopLoadDialog();
        if (context == null) {
            return;
        }
        if (!FunctionHelper.IsNetworkOnline(context)) {
            ViewHub.showShortToast(context, HttpUtils.SERVER_NOT_NET);
            return;
        }
        if (mErrorMsg != null && !TextUtils.isEmpty(mErrorMsg)) {
            ViewHub.showLongToast(context, mErrorMsg);
        } else if (e instanceof ApiException) {
            ApiException apiException = (ApiException) e;
            String code = apiException.getCode();
            if (!TextUtils.isEmpty(apiException.getCode())) {
                if (apiException.getCode().equals(NOT_REGISTERED) || apiException.getCode().equals(RxUtil.USER_NO_EXIST) || apiException.getCode().equals(RxUtil.PASSWORD_ERROR)) {
                    return;
                } else if (code.equals(String.valueOf(HttpErrorCode_401))) {
                    Utils.gotoLoginActivity(context);
                    ViewHub.showLongToast(context, apiException.getMessage());
                } else if (code.equals(String.valueOf(HttpErrorCode_403))) {
                    ViewHub.showLongToast(context, "状态码: " + code + "\n" + HttpUtils.SERVER_403);
                } else if (code.equals(String.valueOf(HttpErrorCode_404))) {
                    ViewHub.showLongToast(context, "状态码: " + code + "\n" + HttpUtils.SERVER_404);
                } else if (code.equals(String.valueOf(HttpErrorCode_500))) {
                    ViewHub.showLongToast(context, "状态码: " + code + "\n" + HttpUtils.SERVER_500);
                } else if (code.equals(String.valueOf(HttpErrorCode_405))) {
                    ViewHub.showLongToast(context, "状态码: " + code + "\n" + HttpUtils.SERVER_405);
                } else if (code.equals(String.valueOf(HttpErrorCode_504))) {
                    ViewHub.showLongToast(context, "状态码: " + code + "\n" + HttpUtils.SERVER_504);
                } else {
                    ViewHub.showLongToast(context, apiException.getMessage());
                }
            } else {
                ViewHub.showLongToast(context, apiException.getMessage());
            }
        } else if (e instanceof HttpException) {
            HttpException httpException = (HttpException) e;
            int code = httpException.code();
            if (code == HttpErrorCode_401) {
                Utils.gotoLoginActivity(context);
            } else if (code == HttpErrorCode_403) {
                ViewHub.showLongToast(context, "状态码: " + code + "\n" + HttpUtils.SERVER_403);
            } else if (code == HttpErrorCode_404) {
                ViewHub.showLongToast(context, "状态码: " + code + "\n" + HttpUtils.SERVER_404);
            } else if (code == HttpErrorCode_500) {
                ViewHub.showLongToast(context, "状态码: " + code + "\n" + HttpUtils.SERVER_500);
            } else if (code == HttpErrorCode_405) {
                ViewHub.showLongToast(context, "状态码: " + code + "\n" + HttpUtils.SERVER_405);
            } else if (code == HttpErrorCode_504) {
                ViewHub.showLongToast(context, "状态码: " + code + "\n" + HttpUtils.SERVER_504);
            } else {
                ViewHub.showLongToast(context, "状态码: " + code + "\n" + HttpUtils.SERVER_504);
            }
        } else if (e instanceof UnknownHostException) {
            ViewHub.showShortToast(context, HttpUtils.SERVER_NOT_FOUND_SERVER);
        } else if (e instanceof TimeoutException) {
            ViewHub.showShortToast(context, HttpUtils.CONNECT_TIME_OUT);
        } else if (e instanceof SocketException) {
            ViewHub.showShortToast(context, HttpUtils.CONNECT_TIME_OUT);
        } else if (e instanceof SocketTimeoutException) {
            ViewHub.showShortToast(context, HttpUtils.CONNECT_TIME_OUT);
        } else {
            ViewHub.showLongToast(context, e.getMessage());
        }
    }

    @Override
    public void onComplete() {
        stopLoadDialog();
    }
}
