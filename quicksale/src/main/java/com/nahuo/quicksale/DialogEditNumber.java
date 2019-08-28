package com.nahuo.quicksale;

import com.nahuo.library.controls.LoadingDialog;
import com.nahuo.quicksale.api.HttpRequestHelper;
import com.nahuo.quicksale.api.HttpRequestListener;
import com.nahuo.quicksale.api.HttpRequestHelper.HttpRequest;
import com.nahuo.quicksale.app.BWApplication;
import com.nahuo.quicksale.controls.WidgetPlusMinus;
import com.nahuo.quicksale.eventbus.BusEvent;
import com.nahuo.quicksale.eventbus.EventBusId;
import com.nahuo.quicksale.oldermodel.ResultData;

import de.greenrobot.event.EventBus;

import android.app.Dialog;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;


/****
 * 发货单修改价格对话框
 * created by 陈智勇   2015-4-28  上午9:15:54
 */
public class DialogEditNumber extends Dialog implements OnClickListener{

    protected String id  ; 
    private int number ; 
    private WidgetPlusMinus editNumber ; 
    /***
     * 修改商品数量对话框
     * @param orderID
     * @param orderPrice  订单商品金额
     * @param orderPost   订单商品邮费
     */
    public DialogEditNumber(Context context , String id ,int number ){
        super(context , R.style.dialog) ; 
        this.id = id ; 
        this.number = number ; 
    }
    protected void setContent(){
        setContentView(R.layout.dlg_edit_number) ; 
    }
    @Override
    public void show() {
        setContent() ;
        WindowManager.LayoutParams params = getWindow().getAttributes() ; 
        DisplayMetrics outMetrics = new DisplayMetrics() ; 
        getWindow().getWindowManager().getDefaultDisplay().getMetrics(outMetrics) ;
        params.width = outMetrics.widthPixels*4/5 ; 
        onWindowAttributesChanged(params) ;
        initView() ; 
        super.show();
    }

    protected void initView() {
        editNumber = (WidgetPlusMinus)findViewById(R.id.widget_plus_minus);  
        editNumber.setNum(number) ; 
        findViewById(R.id.sure).setOnClickListener(this) ; 
    }
    @Override
    public void onClick(final View v) {
        HttpRequestHelper requestHelper = new HttpRequestHelper() ; 
        HttpRequest request = requestHelper.getRequest(v.getContext()
                , "shop/agent/order/UpdateOrderItemQty", new HttpRequestListener() {
                    LoadingDialog dialog = new LoadingDialog(v.getContext());
                    @Override
                    public void onRequestSuccess(String method, Object object) {
                        ViewHub.showLongToast(BWApplication.getInstance(), "商品数量修改成功！") ; 
                        dialog.stop() ;
                        dismiss() ; 
                        EventBus.getDefault().postSticky(BusEvent.getEvent(EventBusId.CHANGE_NUMBER)) ;
                    }
                    @Override
                    public void onRequestStart(String method) {
                        dialog.start("修改中...") ; 
                    }
                    @Override
                    public void onRequestFail(String method, int statusCode, String msg) {
                        ViewHub.showLongToast(BWApplication.getInstance(), "商品数量修改失败！") ;
                        dialog.stop() ; 
                    }
                    @Override
                    public void onRequestExp(String method, String msg, ResultData data) {
                        dialog.stop() ; 
                        ViewHub.showLongToast(BWApplication.getInstance(), "商品数量修改失败！") ; 
                    }
                });
        
        request.addParam("qty", String.valueOf(editNumber.getNum())) ;
        request.addParam("id", id) ;
        request.doPost() ;
    }
}
