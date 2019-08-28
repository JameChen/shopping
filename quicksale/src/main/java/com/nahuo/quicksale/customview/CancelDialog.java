package com.nahuo.quicksale.customview;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nahuo.quicksale.R;

/****
 * 关闭dialog
 * created by Alan  2017-3-27  上午13:54:54
 */
public class CancelDialog extends Dialog implements View.OnClickListener {
    public CancelDialog(Context context){
        super(context , R.style.dialog) ;
        this.mContext=context;
    }
    private String msg;
    private Context mContext;
    private int imgRecource;

    private int showTime;

    public int getShowTime() {
        return showTime;
    }

    public void setShowTime(int showTime) {
        this.showTime = showTime;
    }

    public CancelDialog(Context context, String msg) {
        super(context,R.style.dialog);
        this.msg = msg;
        this.mContext=context;
    }

    public CancelDialog(Context context, String msg,int imgRecource) {
        super(context,R.style.dialog);
        this.msg = msg;
        this.mContext=context;
        this.imgRecource=imgRecource;
    }

    @Override
    public void show() {
        View view= LinearLayout.inflate(mContext,R.layout.dialog_cancel_agreement,null);
        setContentView(view) ;
        if(!TextUtils.isEmpty(msg)){
            setMsg(view,msg);
        }
        if(imgRecource!=0){
            setIMg(view,imgRecource);
        }
        WindowManager.LayoutParams params = getWindow().getAttributes() ;
        DisplayMetrics outMetrics = new DisplayMetrics() ;
        getWindow().getWindowManager().getDefaultDisplay().getMetrics(outMetrics) ;
        params.width = outMetrics.widthPixels*7/10;
        onWindowAttributesChanged(params) ;
        initView() ;
        super.show();
        afterTwoSecond(this);
    }

    private void afterTwoSecond(final CancelDialog dialog){
        new Handler().postDelayed(new Runnable(){
            public void run(){
                if(dialog.isShowing()){
                    dialog.dismiss();
                }
            }

        },getShowTime()==0?2000:getShowTime());
    }
    private void setMsg(View view,String msg){
        ((TextView)view.findViewById(R.id.dialog_cancel_msg)).setText(msg);
    }
    private void setIMg(View view,int imgRecource){
        ((ImageView)view.findViewById(R.id.dialog_cancel_img)).setImageResource(imgRecource);
    }

    private void initView() {
        findViewById(R.id.dialog_cancel_img).setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.dialog_cancel_img){
            dismiss() ;
            return ;
        }
    }
}

