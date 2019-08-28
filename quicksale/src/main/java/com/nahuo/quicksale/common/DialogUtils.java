package com.nahuo.quicksale.common;

import com.nahuo.library.helper.FunctionHelper;
import com.nahuo.quicksale.R;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.TextView;


public class DialogUtils {

    public static void showSureCancelDialog(Context context , String msg){
        showSureCancelDialog(context ,"提示", msg , context.getString(R.string.camera_enter) 
                , context.getString(R.string.camera_Cancel) , null , null) ;
    }
    public static void showSureCancelDialog(Context context ,String title , String msg){
        showSureCancelDialog(context ,title , msg , context.getString(R.string.camera_enter) 
                , context.getString(R.string.camera_Cancel) , null , null) ;
    }
    public static void showSureCancelDialog(Context context , String msg , OnClickListener sureListener){
        showSureCancelDialog(context , "提示" , msg , context.getString(R.string.camera_enter) 
                , context.getString(R.string.camera_Cancel) , sureListener , null) ;
    }
    public static void showSureCancelDialog(Context context , String title , String msg , OnClickListener sureListener){
        showSureCancelDialog(context , title , msg , context.getString(R.string.camera_enter) 
                , context.getString(R.string.camera_Cancel) , sureListener , null) ;
    }
    public static void showSureCancelDialog(Context context , String title , String msg ,String sure, OnClickListener sureListener){
        showSureCancelDialog(context , title , msg , sure , context.getString(R.string.camera_Cancel) , sureListener , null) ;
    }
    public static void showSureCancelDialog(Context context , String title , String msg ,String sure , String cancel , OnClickListener sureListener){
        showSureCancelDialog(context , title , msg , sure , cancel , sureListener , null) ; 
    }
    public static void showSureCancelDialog(Context context , String title , String msg ,String sure
            , String cancel , OnClickListener sureListener , OnClickListener cancelListener){
        showSureCancelDialog(context, title, msg, sure, cancel, sureListener, cancelListener, 0, 0) ;
    }

    public static void dismissDiaog(View v){
        Dialog d = (Dialog)v.getTag() ;
        if(d!=null)
            d.dismiss() ;
    }
    /****
     * Dialog的对象以setTag的方式绑定在view上。在onClick(View v)中获取dialog对象并dismiss();
     * created by 陈智勇   2015-5-19  下午3:11:43
     * @param context
     * @param msg
     * @param sure
     * @param cancel
     * @param sureListener
     * @param cancelListener
     */
    public static void showSureCancelDialog(Context context , String title , String msg ,String sure
            , String cancel , OnClickListener sureListener , OnClickListener cancelListener , int sureColor , int cancelColor){
        final Dialog dialog = new Dialog(context , R.style.dialog) ; 
        dialog.setContentView(R.layout.dialog_sure_cancel) ; 
        dialog.show() ; 
        TextView titleView = (TextView)dialog.findViewById(R.id.dialog_title) ; 
        if(TextUtils.isEmpty(title))
            titleView.setVisibility(View.GONE) ; 
        else
            titleView.setText(title) ; 
        TextView msgView = (TextView)dialog.findViewById(R.id.dialog_message) ; 
        TextView cancelView = (TextView)dialog.findViewById(R.id.dialog_cancel) ; 
        msgView.setText(msg);  
        TextView sureView = (TextView)dialog.findViewById(R.id.dialog_sure) ; 
        sureView.setText(sure);  
        cancelView.setText(cancel) ; 
        if(cancelListener == null){
            cancelListener = new OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss() ; 
                }
            };
        }
        else{
            cancelView.setTag(dialog) ; 
        }
        cancelView.setOnClickListener(cancelListener) ; 
        
        if(sureListener == null)
        {
            sureListener = cancelListener ; 
        }
        else{
            sureView.setTag(dialog) ; 
        }
        sureView.setOnClickListener(sureListener) ; 
    }
    public static void showToast(Context context , String toast){
        showToast(context , toast , 0) ; 
    }
    /***
     * 显示自定义toast，已dialog实现
     * created by 陈智勇   2015-5-27  上午10:51:21
     * @param context
     * @param toast
     * @param timeMiss   提示消失的时间，毫秒计算，0=不消失
     */
    public static void showToast(Context context , String toast , final long timeMiss){
        final Dialog dialog = new Dialog(context , R.style.dialog) ; 
        final TextView text = new TextView(context) ; 
        text.setText(toast) ; 
        text.setTextSize(16) ; 
        int pad = FunctionHelper.dip2px(context.getResources(), 24) ;
        text.setPadding(pad, pad, pad, pad) ; 
        text.setTextColor(Color.WHITE) ; 
        text.setGravity(Gravity.CENTER) ; 
        text.setBackgroundResource(R.drawable.draw_toast_bg) ; 
        dialog.setContentView(text) ; 
        dialog.show() ; 
        WindowManager.LayoutParams params = dialog.getWindow().getAttributes() ; 
        params.width = context.getResources().getDisplayMetrics().widthPixels*3/4 ; 
        dialog.onWindowAttributesChanged(params) ; 
        if(timeMiss > 0 ){
            new Thread(){
                public void run(){
                    try {
                        Thread.sleep(timeMiss) ;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } 
                    text.post(new Runnable(){
                        @Override
                        public void run() {
                            if(dialog!=null&&dialog.isShowing())
                                dialog.dismiss() ; 
                        }
                    }) ; 
                }
            }.start() ;
        }
    }
}
