package com.nahuo.quicksale;

import android.app.Dialog;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.nahuo.library.controls.LoadingDialog;
import com.nahuo.quicksale.api.HttpRequestHelper;
import com.nahuo.quicksale.api.HttpRequestListener;
import com.nahuo.quicksale.api.OrderAPI;
import com.nahuo.quicksale.oldermodel.ResultData;

public class DialogReplenishment extends Dialog implements View.OnClickListener {

    private String msg;
    private String itemID;
    private TextView           txtCount, txtRemark,txtMsg;
    private HttpRequestHelper  mRequestHelper;

    public DialogReplenishment(Context context, String _msg,String _itemID) {
        super(context, R.style.dialog);
        this.msg = _msg;
        this.itemID = _itemID;

        mRequestHelper = new HttpRequestHelper();
    }

    private void initData() {
        txtCount.setText("1");
        txtMsg.setText(msg);
    }

    @Override
    public void show() {
        setContentView(R.layout.dlg_replenishment);
        WindowManager.LayoutParams params = getWindow().getAttributes();
        DisplayMetrics outMetrics = new DisplayMetrics();
        getWindow().getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
        params.width = outMetrics.widthPixels * 4 / 5;
        onWindowAttributesChanged(params);
        initView();
        initData();
        super.show();
    }

    private void initView() {
        findViewById(R.id.contact_msg_cancel).setOnClickListener(this);
        findViewById(R.id.contact_msg_set).setOnClickListener(this);
        txtCount = (TextView)findViewById(R.id.et_dlg_replenishment_count);
        txtRemark = (TextView)findViewById(R.id.et_dlg_replenishment_remark);
        txtMsg = (TextView)findViewById(R.id.et_dlg_replenishment_msg);
    }

    @Override
    public void onClick(final View v) {
        int id = v.getId();
            if (id == R.id.contact_msg_cancel) {
                dismiss();
            } else if (id == R.id.contact_msg_set) {

                final Context context = v.getContext();

                int count = 1;
                try {
                    count = Integer.parseInt(txtCount.getText().toString());
                } catch(Exception ex) {}

                OrderAPI.SaveReplenishmentRecord(context, mRequestHelper, new HttpRequestListener() {
                    LoadingDialog dialog = new LoadingDialog(context);

                    @Override
                    public void onRequestSuccess(String method, Object object) {
                        dialog.stop();
                        dismiss() ;
                        ViewHub.showLongToast(context, "已成功提交！");
                    }

                    @Override
                    public void onRequestStart(String method) {
                        dialog.start("补货中...");
                    }

                    @Override
                    public void onRequestFail(String method, int statusCode, String msg) {
                        dialog.stop();
                        ViewHub.showLongToast(context, msg);
                    }

                    @Override
                    public void onRequestExp(String method, String msg, ResultData data) {
                        dialog.stop();
                        ViewHub.showLongToast(context, msg);
                    }
                }, itemID,count,txtRemark.getText().toString());
            }
    }

}
