package com.nahuo.Dialog;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.nahuo.quicksale.R;
import com.nahuo.quicksale.common.ListUtils;
import com.nahuo.quicksale.loopview.LoopView;
import com.nahuo.quicksale.loopview.OnItemSelectedListener;
import com.nahuo.quicksale.oldermodel.ApplyListModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jame on 2017/7/7.
 */

public class NfhszWheelDialog extends Dialog implements View.OnClickListener {
    static NfhszWheelDialog dialog = null;
    private Activity mActivity;
    View mRootView;
    TextView btn_ok, btn_cancle;
    int h, w;
    PopDialogListener mPopDialogListener;
    List<String> list;
    LoopView loopView;
    List<String> data = new ArrayList<>();
    List<String> subList = new ArrayList<>();
    int type;
    public NfhszWheelDialog(@NonNull Activity context) {
        super(context, R.style.popDialog);
        this.mActivity = context;
    }

    int choose_index = 0,choose_sub_index=0;
    String parentID;
    int subID;
    ApplyListModel.ApplyTypeListBean bean;
    public static NfhszWheelDialog getInstance(Activity activity) {
        if (dialog == null) {
            dialog = new NfhszWheelDialog(activity);
        }
        return dialog;
    }

    public NfhszWheelDialog setList(List<String> list) {
        this.list = list;
        return this;
    }

    public NfhszWheelDialog setValue(String parentID) {
        this.parentID=parentID;
        return this;
    }
    public NfhszWheelDialog setType(int type) {
        this.type = type;
        return this;
    }
    public NfhszWheelDialog setBean(ApplyListModel.ApplyTypeListBean bean) {
        this.bean = bean;
        return this;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViews();
    }

    private void initViews() {
        h = mActivity.getResources().getDisplayMetrics().heightPixels;
        w = mActivity.getResources().getDisplayMetrics().widthPixels;
        mRootView = LayoutInflater.from(mActivity).inflate(R.layout.layout_fhsz, null);
        loopView = (LoopView) mRootView.findViewById(R.id.loopView);
        loopView.setNotLoop();
        btn_ok = (TextView) mRootView.findViewById(R.id.tv_ok);
        btn_cancle = (TextView) mRootView.findViewById(R.id.tv_cancel);
        btn_ok.setOnClickListener(this);
        btn_cancle.setOnClickListener(this);
        setContentView(mRootView);
        setCanceledOnTouchOutside(true);
        setCancelable(true);
        Window window = dialog.getWindow();
        window.setGravity(Gravity.BOTTOM);
        WindowManager m = mActivity.getWindowManager();
        Display d = m.getDefaultDisplay(); //为获取屏幕宽、高
        WindowManager.LayoutParams p = dialog.getWindow().getAttributes(); //获取对话框当前的参数值
        p.width = d.getWidth(); //宽度设置为屏幕
        dialog.getWindow().setAttributes(p); //设置生效
        if (!ListUtils.isEmpty(list)) {
            data.clear();
            for (int i=0;i<list.size();i++) {
                if (parentID.equals(list.get(i))){
                    choose_index=i;
                }
                data.add(list.get(i));
            }
            }
            // 滚动监听
            loopView.setListener(new OnItemSelectedListener() {
                @Override
                public void onItemSelected(int index) {
                    choose_index = index;
                    if (loopView != null)
                        choose_index = loopView.getSelectedItem();

                }
            });
            // 设置原始数据
            loopView.setItems(data);
            loopView.setInitPosition(choose_index);
        }




    public void showDialog() {
//        if (!TextUtils.isEmpty(content)) {
//            if (tv_title != null)
//                tv_title.setText(content);
//        }
        this.show();
    }

    @Override
    public void dismiss() {
        super.dismiss();
        dialog = null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_cancel:
                dismiss();
                dialog = null;
                break;
            case R.id.tv_ok:
                if (loopView != null)
                    choose_index = loopView.getSelectedItem();
                if (choose_index >= 0 && !ListUtils.isEmpty(list)) {
                    parentID = list.get(choose_index);
                }
                if (mPopDialogListener != null) {
                    mPopDialogListener.onFhszDialogButtonClick(parentID,bean);
                }
                dismiss();
                dialog = null;
                break;

        }
    }

    public NfhszWheelDialog setPositive(PopDialogListener listener) {
        mPopDialogListener = listener;
        return this;
    }

    public interface PopDialogListener {
        void onFhszDialogButtonClick(String value,ApplyListModel.ApplyTypeListBean bean);
    }
}
