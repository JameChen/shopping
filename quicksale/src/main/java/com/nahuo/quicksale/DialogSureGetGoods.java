package com.nahuo.quicksale;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.TextView;

import com.nahuo.library.controls.LoadingDialog;
import com.nahuo.library.helper.GsonHelper;
import com.nahuo.library.helper.MD5Utils;
import com.nahuo.quicksale.api.HttpRequestHelper;
import com.nahuo.quicksale.api.HttpRequestHelper.HttpRequest;
import com.nahuo.quicksale.api.HttpRequestListener;
import com.nahuo.quicksale.common.SpManager;
import com.nahuo.quicksale.eventbus.BusEvent;
import com.nahuo.quicksale.eventbus.EventBusId;
import com.nahuo.quicksale.oldermodel.ResultData;
import com.nahuo.quicksale.provider.UserInfoProvider;
import com.nahuo.quicksale.yft.YFTActivity1;

import org.json.JSONException;
import org.json.JSONObject;

import de.greenrobot.event.EventBus;


/****
 * 确认收货对话框
 * created by 陈智勇   2015-4-28  上午9:15:54
 */
public class DialogSureGetGoods extends Dialog implements OnClickListener{

    private int orderID  ; 
    /***
     * 调用show显示对话框
     * @param orderID
     */
    public DialogSureGetGoods(Context context , int orderID ){
        super(context , R.style.dialog) ; 
        this.orderID = orderID; 
    }
    
    @Override
    public void show() {
        if(!UserInfoProvider.hasSetPayPsw(getContext(), SpManager.getUserId(getContext()))){ //未设置支付密码
            final Context context = getContext() ; 
            ViewHub.showOkDialog(context, context.getResources().getString(R.string.prompt)
                    , "你还没有设置支付密码，是否立即去设置?", context.getResources().getString(R.string.titlebar_btnOK)
                    , context.getResources().getString(R.string.titlebar_btnCancel), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(context , YFTActivity1.class) ;
                            context.startActivity(intent) ; 
                        }
                    });
            return ; 
        }
        setContentView(R.layout.dlg_sure_get_goods) ; 
        WindowManager.LayoutParams params = getWindow().getAttributes() ; 
        DisplayMetrics outMetrics = new DisplayMetrics() ; 
        getWindow().getWindowManager().getDefaultDisplay().getMetrics(outMetrics) ;
        params.width = outMetrics.widthPixels*4/5 ; 
        onWindowAttributesChanged(params) ;
        initView() ; 
        super.show();
    }

    private void initView() {
        
        findViewById(R.id.cancel).setOnClickListener(this) ; 
        findViewById(R.id.sure).setOnClickListener(this) ; 
        findViewById(R.id.txt_froget_pwd).setOnClickListener(this) ; 
    }
    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.txt_froget_pwd){
            ForgetPswActivity.toForgetPayPsw(v.getContext());
            return ; 
        }
        if(v.getId() == R.id.sure){
            TextView etPwd = (TextView)findViewById(R.id.et_pwd)  ; 
            String pwd = etPwd.getText().toString() ; 
            HttpRequestHelper  mRequestHelper      = new HttpRequestHelper();
            final Context context = v.getContext() ; 
            HttpRequest request = mRequestHelper.getRequest(v.getContext(), "shop/agent/order/ConfirmGoods", new HttpRequestListener() {
                LoadingDialog dialog = new LoadingDialog(context) ; 
                @Override
                public void onRequestSuccess(String method, Object object) {
                    dialog.stop() ; 
                    boolean success = false ; 
                    String jsonData = GsonHelper.objectToJson(object);
                    try {
                        JSONObject json = new JSONObject(jsonData) ;
                        success = json.getBoolean("Result") ; 
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } 
                    if(success){
                        dismiss() ; 
                        ViewHub.showLongToast(context, "已确认收货！") ; 
                        EventBus.getDefault().postSticky(BusEvent.getEvent(EventBusId.SURE_GET_GOOD)) ; 
                    }
                }
                @Override
                public void onRequestStart(String method) {
                    dialog.start("收货中...") ; 
                    
                }
                @Override
                public void onRequestFail(String method, int statusCode, String msg) {
                    dialog.stop() ; 
                    ViewHub.showLongToast(context, "收货失败！"+msg) ; 
                }
                @Override
                public void onRequestExp(String method, String msg, ResultData data) {
                    dialog.stop() ; 
                    ViewHub.showLongToast(context, "收货失败！"+msg) ; 
                }
            }) ;
            request.addParam("orderId", String.valueOf(orderID)).addParam("isEncode", "true").addParam("password", MD5Utils.encrypt32bit(pwd)).doPost() ;
            
        }
        else{
            dismiss() ; 
        }
    }
}
