package com.nahuo.quicksale.controls;

import android.app.Activity;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.nahuo.quicksale.R;
import com.nahuo.quicksale.ViewHub;
import com.nahuo.quicksale.adapter.FhszAdapter;
import com.nahuo.quicksale.common.ListUtils;
import com.nahuo.quicksale.oldermodel.ApplyListModel;

public class NFHSZDialog extends DialogFragment implements OnClickListener {

    public static NFHSZDialog newInstance(ApplyListModel data) {
        NFHSZDialog f = new NFHSZDialog();
        Bundle args = new Bundle();
        args.putSerializable("data", data);
        f.setArguments(args);
        return f;
    }

    public Activity mContext;
    private FHSZDialogListener dialog;
    private RadioButton menu_1, menu_2, menu_3, menu_4;
    private TextView menu2_et, menu3_et;
    private TextView detail, title;
    private TextView btn;
    private ApplyListModel info;
    private ListView listView;
    private int select_type = 0;
    private FhszAdapter adapter;
    private ApplyListModel.WareHouseBean WareHouse;
    private ImageView iv_cancel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        info = (ApplyListModel) getArguments().getSerializable("data");
        setStyle(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Dialog);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_nfhsz, container, false);
//        menu_1 = (RadioButton)v.findViewById(R.id.menu_1);
//        menu_1.setOnClickListener(this);
//        menu_2 = (RadioButton)v.findViewById(R.id.menu_2);
//        menu_2.setOnClickListener(this);
//        menu_3 = (RadioButton)v.findViewById(R.id.menu_3);
//        menu_3.setOnClickListener(this);
//        menu_4 = (RadioButton)v.findViewById(R.id.menu_4);
//        menu_4.setOnClickListener(this);
        listView = (ListView) v.findViewById(R.id.list_view);
        iv_cancel = (ImageView) v.findViewById(R.id.iv_cancel);
        detail = (TextView) v.findViewById(R.id.detail);
        title = (TextView) v.findViewById(R.id.title);
        btn = (TextView) v.findViewById(R.id.btn);
        iv_cancel.setOnClickListener(this);
        if (info != null) {
            WareHouse = info.getWareHouse();
            if (WareHouse != null) {
                if (title != null)
                    title.setText(WareHouse.getName());
            }
            if (!ListUtils.isEmpty(info.getApplyTypeList())) {
                boolean is_reset = false;
                if (info.getApplyInfo().getTypeID() == info.getApplyTypeList().get(0).getTypeID()) {
                    btn.setText("设置");
                    is_reset = false;
                } else {
                    btn.setText("重新设置");
                    is_reset = true;
                }
                for (int i = 0; i < info.getApplyTypeList().size(); i++) {
                    if (info.getApplyInfo().getTypeID() == info.getApplyTypeList().get(i).getTypeID()) {
                        info.getApplyTypeList().get(i).setValue(info.getApplyInfo().getValue());
                        detail.setText(info.getApplyTypeList().get(i).getSummary());
                        break;
                    }
                }
                adapter = new FhszAdapter(getActivity(), info.getApplyInfo(), is_reset);
                adapter.setListener(new FhszAdapter.SummaryListener() {
                    @Override
                    public void SummaryResult(String Summary) {
                        if (detail != null)
                            detail.setText(Summary);
                    }
                });
                adapter.setData(info.getApplyTypeList());
                listView.setAdapter(adapter);
                btn.setOnClickListener(this);
            }
//            if (info.getApplyInfo()!=null){
//                detail.setText(info.getApplyInfo().getDesc()+"");
//            }
        }
        return v;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.menu2_et:
//            case R.id.menu3_et:
//            {
//                final TextView tv = (TextView)v;
//                int vv = 0;
//                    try {
//                        vv = Integer.parseInt(tv.getText().toString());
//                    } catch(Exception ex) {
//                        vv = 0;
//                    }
//                int selectedIndex = 0;
//                int min = (int)tv.getTag(R.id.tv_agent_num);
//                    int max = (int)tv.getTag(R.id.tv_agent_price);
//                final String items[] = new String[max-min+1];
//                int ii = 0;
//                for (int i = min; i <= max; i++)
//                {
//                    items[ii] = i+"";
//                    if (vv == i) {
//                        selectedIndex = ii;
//                    }
//                    ii++;
//                }
//                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
//                builder.setTitle("选择");
//                builder.setSingleChoiceItems(items, selectedIndex, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        tv.setText(items[which]);
//                    }
//                });
//                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                        if (which>=0 && which<=items.length) {
//                            tv.setText(items[which]);
//                        }
//                    }
//                });
//                builder.create().show();
//                break;
//            }
//            case R.id.menu_1:
//                select_type = 0;
//                break;
//            case R.id.menu_2:
//                select_type = 1;
//                menu2_et.setEnabled(true);
//                menu3_et.setEnabled(false);
//                break;
//            case R.id.menu_3:
//                select_type = 2;
//                menu2_et.setEnabled(false);
//                menu3_et.setEnabled(true);
//                break;
//            case R.id.menu_4:
//                select_type = 3;
//                break;
            case R.id.iv_cancel:
                this.dismiss();
                break;
            case R.id.btn:
                if (dialog != null) {
                    if (btn.getText().toString().equals("设置")) {
                        int val = 0;
                        boolean flag = false;
                        select_type = info.getApplyInfo().getTypeID();
                        val = info.getApplyInfo().getValue();
                        if (select_type == info.getApplyTypeList().get(0).getTypeID()) {
                            ViewHub.showLongToast(mContext, "默认仓库安排，无需申请");
                            return;
                        }
                        for (ApplyListModel.ApplyTypeListBean bean : info.getApplyTypeList()) {
                            if (select_type == bean.getTypeID() && val < bean.getMin()) {
                                ViewHub.showShortToast(getActivity(), "请选择" + bean.getMin() + "-" + bean.getMax() + "的数字!");
                                flag = true;
                                break;
                            }
                        }
                        if (!flag) {
                            dialog.FHSZResult(select_type, val);
                        }

                    } else {
                        dialog.FHSZResult(0, 0);
                    }
                }
                break;
            default:
                break;
        }
    }

    public NFHSZDialog setDialogListener(FHSZDialogListener callback) {
        this.dialog = callback;
        return this;
    }

    public interface FHSZDialogListener {
        void FHSZResult(int typeid, int value);
    }

}
