package com.nahuo.quicksale;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.nahuo.library.controls.LoadingDialog;
import com.nahuo.library.helper.FunctionHelper;
import com.nahuo.library.helper.GsonHelper;
import com.nahuo.quicksale.api.HttpRequestHelper;
import com.nahuo.quicksale.api.HttpRequestHelper.HttpRequest;
import com.nahuo.quicksale.api.HttpRequestListener;
import com.nahuo.quicksale.api.OrderAPI;
import com.nahuo.quicksale.common.FileUtils;
import com.nahuo.quicksale.eventbus.BusEvent;
import com.nahuo.quicksale.eventbus.EventBusId;
import com.nahuo.quicksale.oldermodel.ResultData;
import com.nahuo.quicksale.orderdetail.model.ExpressModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import de.greenrobot.event.EventBus;

public class DialogChooseExpress extends Dialog implements android.view.View.OnClickListener {

    private int                shipID;
    /***
     * 选择的 快递公司名
     */
    private ExpressModel chooseExpress;
    private Spinner            spinner;
    private boolean            state;
    private TextView           txtExpressName, txtExpressCode;
    private File               cacheFile;
    private Adapter            adapter;
    private List<ExpressModel> models;
    private HttpRequestHelper  mRequestHelper;
    private boolean isUpdateExpress ; 
    public DialogChooseExpress(Context context, int shipID) {
        
        this(context , shipID , false) ; 
    }
    /***
     * 
     * @param context
     * @param shipID
     * @param updateShip   true:更改发货信息
     */
    public DialogChooseExpress(Context context, int shipID , boolean updateShip) {
        super(context, R.style.dialog);
        this.shipID = shipID;
        isUpdateExpress = updateShip ;
        cacheFile = context.getExternalCacheDir();
        if (cacheFile != null) {
            cacheFile = new File(cacheFile, "express");
        } else {
            cacheFile = new File(context.getCacheDir(), "express");
        }
        String json = "";
        if (cacheFile.exists()) {
            json = FileUtils.readFile(cacheFile);
        } else {
            try {
                InputStream in = context.getAssets().open("express.txt");
                byte[] buf = new byte[in.available()];
                in.read(buf);
                in.close();
                json = new String(buf);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        getModels(json);
        initData(context);
    }
    private void getModels(String json) {
        try {
            JSONObject obj = new JSONObject(json);
            json = obj.getString("Data");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        models = GsonHelper.jsonToObject(json, new TypeToken<List<ExpressModel>>() {});
    }

    private void initData(Context context) {

        mRequestHelper = new HttpRequestHelper();
        mRequestHelper.getRequest(context, "shop/express/GetMyShopExpress", new HttpRequestListener() {
            @Override
            public void onRequestSuccess(String method, Object object) {
                String json = (String)object;
                try {
                    FileOutputStream out = new FileOutputStream(cacheFile);
                    out.write(json.getBytes());
                    out.flush();
                    out.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                getModels(json);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onRequestStart(String method) {}

            @Override
            public void onRequestFail(String method, int statusCode, String msg) {}

            @Override
            public void onRequestExp(String method, String msg, ResultData data) {}
        }).doPost();

    }

    @Override
    public void show() {
        setContentView(R.layout.dlg_fahuo);
        WindowManager.LayoutParams params = getWindow().getAttributes();
        DisplayMetrics outMetrics = new DisplayMetrics();
        getWindow().getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
        params.width = outMetrics.widthPixels * 4 / 5;
        onWindowAttributesChanged(params);
        initView();
        super.show();
    }

    private void initView() {
        findViewById(R.id.btn_dlg_fahuo_add).setOnClickListener(this);
        findViewById(R.id.contact_msg_cancel).setOnClickListener(this);
        findViewById(R.id.contact_msg_set).setOnClickListener(this);
        spinner = (Spinner)findViewById(R.id.spinner_express_company);
        adapter = new Adapter();
        spinner.setPrompt("选择发货的快递公司");
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                chooseExpress = models.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
        txtExpressName = (TextView)findViewById(R.id.et_dlg_fahuo_post_name);
        txtExpressCode = (TextView)findViewById(R.id.et_dlg_fahuo_post_code);
    }

    @Override
    public void onClick(final View v) {
        int id = v.getId();
        if (id == R.id.btn_dlg_fahuo_add) {
            if (state) {
                state = false;
                v.setBackgroundResource(R.drawable.add_blue);
                txtExpressName.setVisibility(View.INVISIBLE);
                spinner.setVisibility(View.VISIBLE);
            } else {
                state = true;
                v.setBackgroundResource(R.drawable.back);
                spinner.setVisibility(View.INVISIBLE);
                txtExpressName.setVisibility(View.VISIBLE);
            }
        } else {
            if (id == R.id.contact_msg_cancel) {
                dismiss();
            } else if (id == R.id.contact_msg_set) {

                String express = null;
                if (state) { // 手动输入
                    express = txtExpressName.getText().toString();
                } else if (chooseExpress == null) {
                    ViewHub.showLongToast(v.getContext(), "你还没选择快递公司!");
                    return;
                } else {
                    express = String.valueOf(chooseExpress.ID);
                }
                if (TextUtils.isEmpty(express)) {
                    ViewHub.showLongToast(v.getContext(), "你还没选择快递公司!");
                } else {
                    String code = txtExpressCode.getText().toString();
                    if (TextUtils.isEmpty(code)) {
                        ViewHub.showLongToast(v.getContext(), "你还没输入快递单号!");
                        return;
                    }
                    final Context context = v.getContext();
                    if(isUpdateExpress){
                        OrderAPI.updateShipExpress(context, mRequestHelper, express , shipID, code , new HttpRequestListener() {
                            LoadingDialog dialog = new LoadingDialog(context);

                            @Override
                            public void onRequestSuccess(String method, Object object) {
                                EventBus.getDefault().postSticky(BusEvent.getEvent(EventBusId.SEND_GOOD));
                                dialog.stop();
                                dismiss() ; 
                                ViewHub.showLongToast(context, "修改成功！");
                            }

                            @Override
                            public void onRequestStart(String method) {
                                dialog.start("修改中...");
                            }

                            @Override
                            public void onRequestFail(String method, int statusCode, String msg) {
                                dialog.stop();
                                ViewHub.showLongToast(context, "修改失败！");
                            }

                            @Override
                            public void onRequestExp(String method, String msg, ResultData data) {
                                dialog.stop();
                                ViewHub.showLongToast(context, "修改失败！");
                            }
                         }) ;
                    }
                    else{
                        HttpRequest request = mRequestHelper.getRequest(v.getContext(), "shop/agent/order/Ship",
                                new HttpRequestListener() {
                                    LoadingDialog dialog = new LoadingDialog(context);

                                    @Override
                                    public void onRequestSuccess(String method, Object object) {
                                        dialog.dismiss();
                                        ViewHub.showLongToast(context, "发货成功!");
                                        dismiss();
                                        EventBus.getDefault().post(BusEvent.getEvent(EventBusId.SEND_GOOD));
                                    }

                                    @Override
                                    public void onRequestStart(String method) {
                                        dialog.start("发货中...");
                                    }

                                    @Override
                                    public void onRequestFail(String method, int statusCode, String msg) {
                                        dialog.dismiss();
                                        ViewHub.showLongToast(context, "发货失败!" + msg);
                                    }

                                    @Override
                                    public void onRequestExp(String method, String msg, ResultData data) {
                                        dialog.dismiss();
                                        ViewHub.showLongToast(context, "发货失败!" + msg);
                                    }
                                });
                        request.addParam("shipId", String.valueOf(shipID));
                        request.addParam("expressId", express);
                        request.addParam("trackingNum", code);
                        request.addParam("expressName", express);
                        request.doPost();
                    }
                }
            }
        }
    }

    class Adapter extends BaseAdapter {
        @Override
        public int getCount() {
            return models == null ? 0 : models.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                TextView text = new TextView(parent.getContext());
                int padding = FunctionHelper.dip2px(parent.getResources(), 12);
                text.setPadding(padding, padding, padding, padding);
                text.setTextColor(Color.BLACK);
                text.setTextSize(18);
                convertView = text;
            }
            ((TextView)convertView).setText(models.get(position).Name);
            return convertView;
        }
    }
}
