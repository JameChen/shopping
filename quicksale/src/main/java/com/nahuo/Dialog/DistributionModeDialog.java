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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.nahuo.quicksale.R;
import com.nahuo.quicksale.adapter.DistributionModeAdapter;
import com.nahuo.quicksale.common.ListUtils;
import com.nahuo.quicksale.oldermodel.ShipSettingModel;

import java.util.List;

/**
 * Created by jame on 2017/7/7.
 */

public class DistributionModeDialog extends Dialog implements View.OnClickListener {
    static DistributionModeDialog dialog = null;
    private Activity mActivity;
    View mRootView;
    TextView btn_ok;
    ImageView btn_cancle;
    int h, w;
    PopDialogListener mPopDialogListener;
    List<ShipSettingModel> list;
    ListView listView;
    DistributionModeAdapter adapter;
    int type;

    public DistributionModeDialog(@NonNull Activity context) {
        super(context, R.style.popDialog);
        this.mActivity = context;
    }

    int choose_index = 0, choose_sub_index = 0;
    int parentID;
    int subID;

    public static DistributionModeDialog getInstance(Activity activity) {
        if (dialog == null) {
            dialog = new DistributionModeDialog(activity);
        }
        return dialog;
    }

    public DistributionModeDialog setList(List<ShipSettingModel> list) {
        this.list = list;
        return this;
    }

    public DistributionModeDialog setID(int parentID) {
        this.parentID = parentID;
        return this;
    }

    public DistributionModeDialog setType(int type) {
        this.type = type;
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
        mRootView = LayoutInflater.from(mActivity).inflate(R.layout.layout_distribution_mode, null);
        listView = (ListView) mRootView.findViewById(R.id.listview);
        btn_ok = (TextView) mRootView.findViewById(R.id.tv_ok);
        btn_cancle = (ImageView) mRootView.findViewById(R.id.iv_cancel);
        btn_ok.setOnClickListener(this);
        btn_cancle.setOnClickListener(this);
        setContentView(mRootView);
        setCanceledOnTouchOutside(false);
        setCancelable(false);
        Window window = dialog.getWindow();
        window.setGravity(Gravity.BOTTOM);
        WindowManager m = mActivity.getWindowManager();
        Display d = m.getDefaultDisplay(); //为获取屏幕宽、高
        WindowManager.LayoutParams p = dialog.getWindow().getAttributes(); //获取对话框当前的参数值
        p.width = d.getWidth(); //宽度设置为屏幕
        dialog.getWindow().setAttributes(p); //设置生效
        adapter = new DistributionModeAdapter(mActivity);
        listView.setAdapter(adapter);
        if (!ListUtils.isEmpty(list)){
            adapter.setData(list);
            adapter.notifyDataSetChanged();
        }
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
            case R.id.iv_cancel:
                dismiss();
                dialog = null;
                break;
            case R.id.tv_ok:
                ShipSettingModel bean=null;
                if (mPopDialogListener != null) {
                    if (adapter!=null)
                        bean=adapter.getCheck();
                    mPopDialogListener.onDistriDialogButtonClick(bean);
                }
                dismiss();
                dialog = null;
                break;

        }
    }

    public DistributionModeDialog setPositive(PopDialogListener listener) {
        mPopDialogListener = listener;
        return this;
    }

    public interface PopDialogListener {
        void onDistriDialogButtonClick(ShipSettingModel bean);
    }
}
