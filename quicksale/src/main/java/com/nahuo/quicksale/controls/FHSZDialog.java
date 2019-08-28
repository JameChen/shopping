package com.nahuo.quicksale.controls;

import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import com.nahuo.quicksale.R;
import com.nahuo.quicksale.oldermodel.ApplyListModel;

public class FHSZDialog extends DialogFragment implements OnClickListener {

    public static FHSZDialog newInstance(ApplyListModel data) {
        FHSZDialog f = new FHSZDialog();
        Bundle args = new Bundle();
        args.putSerializable("data", data);
        f.setArguments(args);
        return f;
    }

    public Context mContext;
    private FHSZDialogListener dialog;
    private RadioButton menu_1, menu_2, menu_3,menu_4;
    private TextView menu2_et,menu3_et;
    private TextView detail;
    private TextView btn;
    private ApplyListModel info;

    private int select_type = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        info = (ApplyListModel)getArguments().getSerializable("data");
        setStyle(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Dialog);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_fhsz, container, false);
        menu_1 = (RadioButton)v.findViewById(R.id.menu_1);
        menu_1.setOnClickListener(this);
        menu_2 = (RadioButton)v.findViewById(R.id.menu_2);
        menu_2.setOnClickListener(this);
        menu_3 = (RadioButton)v.findViewById(R.id.menu_3);
        menu_3.setOnClickListener(this);
        menu_4 = (RadioButton)v.findViewById(R.id.menu_4);
        menu_4.setOnClickListener(this);

        detail = (TextView) v.findViewById(R.id.detail);

        menu2_et = (TextView)v.findViewById(R.id.menu2_et);
        menu2_et.setOnClickListener(this);
//        menu2_et.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                if (!hasFocus) {
//                    int vv = 0;
//                    try {
//                        vv = Integer.parseInt(menu2_et.getText().toString());
//                    } catch(Exception ex) {
//                        vv = 0;
//                    }
//                    int min = (int)menu2_et.getTag(R.id.tv_agent_num);
//                    int max = (int)menu2_et.getTag(R.id.tv_agent_price);
//                    int setv = -1;
//                    if (vv<min) {
//                        setv = min;
//                    }
//                    if (vv>max) {
//                        setv = max;
//                    }
//                    if (setv>=0) {
//                        menu2_et.setText(setv+"");
//                    }
//                }
//            }
//        });
        menu3_et = (TextView)v.findViewById(R.id.menu3_et);
        menu3_et.setOnClickListener(this);
//        menu3_et.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                if (!hasFocus) {
//                    int vv = 0;
//                    try {
//                        vv = Integer.parseInt(menu3_et.getText().toString());
//                    } catch(Exception ex) {
//                        vv = 0;
//                    }
//                    int min = (int)menu2_et.getTag(R.id.tv_agent_num);
//                    int max = (int)menu2_et.getTag(R.id.tv_agent_price);
//                    int setv = -1;
//                    if (vv<min) {
//                        setv = min;
//                    }
//                    if (vv>max) {
//                        setv = max;
//                    }
//                    if (setv>=0) {
//                        menu2_et.setText(setv+"");
//                    }
//                }
//            }
//        });

        for (ApplyListModel.ApplyTypeListBean b : info.getApplyTypeList()) {
            switch (b.getTypeID()) {
                case 0: {
                    break;
                }
                case 1: {
                    menu2_et.setTag(R.id.tv_agent_num,b.getMin());
                    menu2_et.setTag(R.id.tv_agent_price,b.getMax());
                    break;
                }
                case 2: {
                    menu3_et.setTag(R.id.tv_agent_num,b.getMin());
                    menu3_et.setTag(R.id.tv_agent_price,b.getMax());
                    break;
                }
                case 3: {
                    break;
                }
            }
        }
        if (info.getApplyInfo().getTypeID()==0) {
            menu_1.setChecked(true);
        }
        if (info.getApplyInfo().getTypeID()==1) {
            menu_2.setChecked(true);
            menu2_et.setText(info.getApplyInfo().getValue()+"");
        }
        if (info.getApplyInfo().getTypeID()==2) {
            menu_3.setChecked(true);
            menu3_et.setText(info.getApplyInfo().getValue()+"");
        }
        if (info.getApplyInfo().getTypeID()==3) {
            menu_4.setChecked(true);
        }

        detail.setText(info.getApplyInfo().getDesc());

        btn = (TextView)v.findViewById(R.id.btn);
        if (info.getApplyInfo().getTypeName().equals("仓库安排")) {
            btn.setText("申请");
            menu2_et.setEnabled(false);
            menu3_et.setEnabled(false);
        } else {
            btn.setText("重新设置");
            menu_1.setEnabled(false);
            menu_2.setEnabled(false);
            menu_3.setEnabled(false);
            menu_4.setEnabled(false);
            menu2_et.setEnabled(false);
            menu3_et.setEnabled(false);
        }
        btn.setOnClickListener(this);
        
        return v;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.menu2_et:
            case R.id.menu3_et:
            {
                final TextView tv = (TextView)v;
                int vv = 0;
                    try {
                        vv = Integer.parseInt(tv.getText().toString());
                    } catch(Exception ex) {
                        vv = 0;
                    }
                int selectedIndex = 0;
                int min = (int)tv.getTag(R.id.tv_agent_num);
                    int max = (int)tv.getTag(R.id.tv_agent_price);
                final String items[] = new String[max-min+1];
                int ii = 0;
                for (int i = min; i <= max; i++)
                {
                    items[ii] = i+"";
                    if (vv == i) {
                        selectedIndex = ii;
                    }
                    ii++;
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setTitle("选择");
                builder.setSingleChoiceItems(items, selectedIndex, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        tv.setText(items[which]);
                    }
                });
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        if (which>=0 && which<=items.length) {
                            tv.setText(items[which]);
                        }
                    }
                });
                builder.create().show();
                break;
            }
            case R.id.menu_1:
                select_type = 0;
                break;
            case R.id.menu_2:
                select_type = 1;
                menu2_et.setEnabled(true);
                menu3_et.setEnabled(false);
                break;
            case R.id.menu_3:
                select_type = 2;
                menu2_et.setEnabled(false);
                menu3_et.setEnabled(true);
                break;
            case R.id.menu_4:
                select_type = 3;
                break;
            case R.id.btn:
                if (dialog != null) {
int val = 0;
                    if (select_type == 1) {
                        val = Integer.valueOf(menu2_et.getText().toString());
                    }
                    if (select_type == 2) {
                        val = Integer.valueOf(menu3_et.getText().toString());
                    }
                    dialog.FHSZResult(select_type,val);
                }
                dismiss();
                break;
            default:
                break;
        }
    }

    public FHSZDialog setDialogListener(FHSZDialogListener callback) {
        this.dialog = callback;
        return this;
    }

    public interface FHSZDialogListener {
        public void FHSZResult(int typeid,int value);
    }

}
