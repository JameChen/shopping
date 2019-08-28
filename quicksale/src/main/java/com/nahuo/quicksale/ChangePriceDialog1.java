package com.nahuo.quicksale;

import java.text.DecimalFormat;

import com.nahuo.library.controls.LoadingDialog;
import com.nahuo.quicksale.api.HttpRequestHelper;
import com.nahuo.quicksale.api.HttpRequestListener;
import com.nahuo.quicksale.api.OrderAPI;
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
import android.widget.EditText;


/****
 * 发货单修改价格对话框
 * created by 陈智勇   2015-4-28  上午9:15:54
 */
public class ChangePriceDialog1 extends Dialog implements OnClickListener{

    protected int shipID  ; 
    protected float orderPrice , orderPost ;
    protected EditText txtPost;
    protected DecimalFormat df = new DecimalFormat("0.00") ;
    /***
     * 调用show显示对话框
     * @param orderID
     * @param orderPrice  订单商品金额
     * @param orderPost   订单商品邮费
     */
    public ChangePriceDialog1(Context context , int shipID 
            , float orderPrice , float orderPost, boolean isFreePost ){
        super(context , R.style.dialog) ; 
        this.shipID = shipID; 
        this.orderPrice = orderPrice ; 
        this.orderPost = isFreePost ? 0 : orderPost ; 
    }
    protected void setContent(){
        setContentView(R.layout.dlg_change_price_simple) ; 
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
        
        txtPost = (EditText)findViewById(R.id.et_dlg_change_price_post) ; 
        String postStr = df.format(orderPost) ; 
        txtPost.setText(postStr ) ; 
        txtPost.setSelection(postStr.length()) ; 
        findViewById(R.id.contact_msg_cancel).setOnClickListener(this) ; 
        findViewById(R.id.contact_msg_set).setOnClickListener(this) ; 
        
    }
    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.contact_msg_set){
            float post = 0 ; 
            try{
                post = Float.valueOf(txtPost.getText().toString()) ; 
            }catch(NumberFormatException e){
                e.printStackTrace() ;
            }
            if(post==orderPost){
                dismiss() ; 
                return ; 
            }
            final Context context = v.getContext() ; 
            HttpRequestHelper  mRequestHelper      = new HttpRequestHelper();
            
            OrderAPI.changePostPrice(v.getContext(), mRequestHelper, new HttpRequestListener() {
                LoadingDialog dialog = new LoadingDialog(context) ; 
                @Override
                public void onRequestSuccess(String method, Object object) {
                    dialog.stop() ;
                    dismiss() ;
                    ViewHub.showLongToast(context, "邮费修改成功！");
                    {
                        EventBus.getDefault().postSticky(BusEvent.getEvent(EventBusId.CHANGE_PRICE)) ;
                    }
                }
                @Override
                public void onRequestStart(String method) {
                    dialog.start("改邮费中...") ; 
                }
                @Override
                public void onRequestFail(String method, int statusCode, String msg) {
                    dialog.stop() ;
                    ViewHub.showLongToast(context, "邮费修改失败！");
                }
                @Override
                public void onRequestExp(String method, String msg, ResultData data) {
                    dialog.stop() ;
                    ViewHub.showLongToast(context, "邮费修改失败！");
                }
            }, shipID, post) ;
        }
        else{
            dismiss() ; 
        }
    }
}
