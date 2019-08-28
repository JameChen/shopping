package com.nahuo.quicksale;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.nahuo.library.controls.LoadingDialog;
import com.nahuo.quicksale.api.HttpRequestHelper;
import com.nahuo.quicksale.api.HttpRequestHelper.HttpRequest;
import com.nahuo.quicksale.api.HttpRequestListener;
import com.nahuo.quicksale.eventbus.BusEvent;
import com.nahuo.quicksale.eventbus.EventBusId;
import com.nahuo.quicksale.oldermodel.ResultData;

import de.greenrobot.event.EventBus;

/***
 * 添加备忘对话框 created by 陈智勇 2015-4-28 下午4:27:23
 */
public class DialogMemo extends Dialog implements android.view.View.OnClickListener {

    private int     id;
    private int     userID;
    private String  memo;
    private Context mContext;

    public DialogMemo(Context context, int id, String memo) {
        super(context, R.style.dialog);
        this.id = id;
        this.memo = memo;
        mContext = context;
    }

    /***
     * 有用户id是留言
     * 
     * @param context
     * @param id
     * @param userID
     */
    public DialogMemo(Context context, int id, int userID) {
        super(context, R.style.dialog);
        this.id = id;
        this.userID = userID;
    }

    @Override
    public void show() {
        setContentView(R.layout.dlg_add_memory);
        WindowManager.LayoutParams params = getWindow().getAttributes();
        DisplayMetrics outMetrics = new DisplayMetrics();
        getWindow().getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
        params.width = outMetrics.widthPixels * 4 / 5;
        onWindowAttributesChanged(params);
        initView();
        super.show();
    }

    private void initView() {
        TextView btn = (TextView)findViewById(R.id.btn_sure_add_memo);
        btn.setOnClickListener(this);
        findViewById(R.id.btn_cancel).setOnClickListener(this);
        if (!TextUtils.isEmpty(memo)) {
            EditText memoTxt = (EditText)findViewById(R.id.et_dlg_memo);
            memoTxt.setText(memo);
            btn.setText("修改");
            memoTxt.setSelection(memo.length());
        }
    }

    @Override
    public void onClick(final View v) {
        switch (v.getId()) {
            case R.id.btn_sure_add_memo:
                submitData(v);
                break;
            case R.id.btn_cancel:
                dismiss();
                break;
        }
        
    }

    private void submitData(final View v) {
        TextView memoTxt = (TextView)findViewById(R.id.et_dlg_memo);
        String memo = memoTxt.getText().toString();
        {
            if (userID == 0) {
                final LoadingDialog dialog = new LoadingDialog(v.getContext());
                dialog.start("备注添加中...");
                HttpRequestHelper mRequestHelper = new HttpRequestHelper();
                HttpRequest request = mRequestHelper.getRequest(v.getContext(), "shop/agent/order/SaveOrderMemo",
                        new HttpRequestListener() {
                            @Override
                            public void onRequestSuccess(String method, Object object) {
                                dialog.stop();
                                dismiss();
                                ViewHub.showLongToast(v.getContext(), "订单备注添加成功!");
                                EventBus.getDefault().postSticky(BusEvent.getEvent(EventBusId.ADD_MEMO));
                            }

                            @Override
                            public void onRequestStart(String method) {}

                            @Override
                            public void onRequestFail(String method, int statusCode, String msg) {
                                dialog.stop();
                                ViewHub.showLongToast(v.getContext(), "订单备注添加失败!" + msg);
                            }

                            @Override
                            public void onRequestExp(String method, String msg, ResultData data) {
                                dialog.stop();
                                ViewHub.showLongToast(v.getContext(), "订单备注添加失败!" + msg);
                            }
                        });
                request.addParam("orderId", String.valueOf(id));
                request.addParam("memo", memo);
                request.doPost();
            }
        }
    }
}
