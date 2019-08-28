package com.nahuo.quicksale.adapter;

import android.app.Activity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import com.nahuo.Dialog.NfhszWheelDialog;
import com.nahuo.quicksale.R;
import com.nahuo.quicksale.common.ListUtils;
import com.nahuo.quicksale.oldermodel.ApplyListModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jame on 2017/9/12.
 */

public class FhszAdapter extends MyBaseAdapter<ApplyListModel.ApplyTypeListBean> implements NfhszWheelDialog.PopDialogListener {
    ApplyListModel.ApplyInfoBean applyInfoBean;
    Activity activity;
    int sel_value = 0;
    public static final int TYPE_SINGLE_TV = 0;
    public static final int TYPE_SINGLE_ET = 1;
    boolean is_reset;
    SummaryListener listener;

    public void setListener(SummaryListener listener) {
        this.listener = listener;
    }

    public FhszAdapter(Activity context, ApplyListModel.ApplyInfoBean applyInfoBean, boolean is_reset) {
        super(context);
        this.applyInfoBean = applyInfoBean;
        this.activity = context;
        this.is_reset = is_reset;
    }

    @Override
    public int getItemViewType(int position) {
        if (mdata.get(position).getDesc().contains("#")) {
            return TYPE_SINGLE_ET;
        } else {
            return TYPE_SINGLE_TV;
        }
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolderEt holder = null;
        ViewHolderTv holderTv = null;
        ApplyListModel.ApplyTypeListBean bean = null;
        int type = getItemViewType(position);
        bean = mdata.get(position);
        if (convertView == null) {
            switch (type) {
                case TYPE_SINGLE_ET:
                    holder = new ViewHolderEt();
                    convertView = LayoutInflater.from(mContext).inflate(R.layout.item_fhsz_et, parent, false);
                    holder.double_rb = (RadioButton) convertView.findViewById(R.id.double_rb);
                    holder.menu_et = (TextView) convertView.findViewById(R.id.menu_et);
                    holder.tv_right = (TextView) convertView.findViewById(R.id.tv_right);
                    holder.tv_left = (TextView) convertView.findViewById(R.id.tv_left);
                    convertView.setTag(holder);
                    break;
                case TYPE_SINGLE_TV:
                    holderTv = new ViewHolderTv();
                    convertView = LayoutInflater.from(mContext).inflate(R.layout.item_fhsz_tv, parent, false);
                    holderTv.single_rb = (RadioButton) convertView.findViewById(R.id.single_rb);
                    convertView.setTag(holderTv);
                    break;
            }

        } else {
            switch (type) {
                case TYPE_SINGLE_ET:
                    holder = (ViewHolderEt) convertView.getTag();
                    break;
                case TYPE_SINGLE_TV:
                    holderTv = (ViewHolderTv) convertView.getTag();
                    break;
            }

        }
        switch (type) {
            case TYPE_SINGLE_ET:
                if (bean.isIsSelected()) {
                    holder.double_rb.setChecked(true);
                    holder.menu_et.setEnabled(true);
                    holder.menu_et.setBackgroundResource(R.drawable.bg_re_gray_white1);

                } else {
                    holder.double_rb.setChecked(false);
                    holder.menu_et.setEnabled(false);
                    holder.menu_et.setBackgroundResource(R.drawable.bg_re_gray_white2);

                }
                holder.menu_et.setText(bean.getValue() + "");
                if (!TextUtils.isEmpty(bean.getDesc())) {
                    String[] ss = bean.getDesc().split("#");
                    if (ss.length > 1) {
                        holder.tv_left.setText(ss[0] + "");
                        holder.tv_right.setText(ss[1] + "");
                    }
                }
                holder.double_rb.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ApplyListModel.ApplyTypeListBean bean = mdata.get(position);
                        setIsSelect(bean);
                        String summer = "";
                        if (bean != null) {
                            summer = bean.getSummary();
                        }
                        if (listener != null)
                            listener.SummaryResult(summer);
                    }
                });
                holder.menu_et.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ApplyListModel.ApplyTypeListBean bean = mdata.get(position);
                        if (bean.getMax() > 0) {
                            List<String> list = new ArrayList<>();
                            for (int i = bean.getMin(); i <= bean.getMax(); i++) {
                                list.add(i + "");
                            }
                            int aPvalue = bean.getValue();
                            applyInfoBean.setValue(sel_value);
                            NfhszWheelDialog.getInstance(activity).setValue(aPvalue + "").setBean(bean).setList(list).setPositive(FhszAdapter.this).showDialog();
                        }
                    }
                });
                if (is_reset) {
                    holder.double_rb.setEnabled(false);
                    holder.menu_et.setEnabled(false);
                }

                break;
            case TYPE_SINGLE_TV:
                if (bean.isIsSelected()) {
                    holderTv.single_rb.setChecked(true);
                } else {
                    holderTv.single_rb.setChecked(false);
                }
                holderTv.single_rb.setText(bean.getDesc() + "");
                holderTv.single_rb.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ApplyListModel.ApplyTypeListBean bean = mdata.get(position);
                        setIsSelect(bean);
                        String summer = "";
                        if (bean != null) {
                            summer = bean.getSummary();
                        }
                        if (listener != null)
                            listener.SummaryResult(summer);
                    }
                });
                if (is_reset) {
                    holderTv.single_rb.setEnabled(false);
                }
                break;
        }


        return convertView;
    }

    public interface SummaryListener {
        void SummaryResult(String Summary);
    }

    public void setIsSelect(ApplyListModel.ApplyTypeListBean bean) {
        if (!ListUtils.isEmpty(mdata)) {
            for (ApplyListModel.ApplyTypeListBean xbean : mdata) {
                if (xbean.getTypeID() == bean.getTypeID()) {
                    xbean.setIsSelected(true);
                    applyInfoBean.setTypeID(xbean.getTypeID());
                    applyInfoBean.setValue(xbean.getValue());
                } else {
                    xbean.setIsSelected(false);
                }
            }
            notifyDataSetChanged();
        }
    }

    @Override
    public void onFhszDialogButtonClick(String value, ApplyListModel.ApplyTypeListBean bean) {
        try {
            int mValue = Integer.parseInt(value);
            if (bean != null)
                bean.setValue(mValue);
            if (applyInfoBean != null)
                applyInfoBean.setValue(mValue);
            notifyDataSetChanged();
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

    }

    public static class ViewHolderTv {
        RadioButton single_rb;
    }

    public static class ViewHolderEt {
        RadioButton double_rb;
        TextView menu_et, tv_right, tv_left;
    }

}
