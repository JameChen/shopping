package com.nahuo.quicksale.controls;

import com.nahuo.quicksale.R;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class NumPlusMinusDialog extends DialogFragment implements OnClickListener {

    public static NumPlusMinusDialog newInstance(int num) {
        NumPlusMinusDialog f = new NumPlusMinusDialog();
        Bundle args = new Bundle();
        args.putInt("num", num);
        f.setArguments(args);
        return f;
    }

    private NumPlusMinusDialogListener mNumPlusMinusDialog;
    private TextView                   mTvTitle, mTvCancel, mTvOK;
    private WidgetPlusMinus            mPlusMinus;
    private int                        mNum;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mNum = getArguments().getInt("num", 1);
        setStyle(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Dialog);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_plus_minus, container, false);
        mTvTitle = (TextView)v.findViewById(android.R.id.title);

        mTvCancel = (TextView)v.findViewById(android.R.id.text1);
        mTvCancel.setOnClickListener(this);
        mTvOK = (TextView)v.findViewById(android.R.id.text2);
        mTvOK.setOnClickListener(this);

        mPlusMinus = (WidgetPlusMinus)v.findViewById(R.id.wpm_count);

        mTvTitle.setText(R.string.update_buy_num);
        mPlusMinus.setNum(mNum);
        // mEtNum.setText(mNum + "");

        return v;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        // case android.R.id.button1:// 减
        // mNum = Integer.valueOf(mPlusMinus.getNum());
        // if (mNum > 1) {
        // mNum--;
        // mPlusMinus.setNum(mNum);
        // } else {
        // ViewHub.showShortToast(getActivity().getApplicationContext(), "亲，不能再减了哟");
        // }
        // break;
        // case android.R.id.button2:// 加
        // mNum = Integer.valueOf(mEtNum.getText().toString());
        // mNum++;
        // mEtNum.setText(mNum + "");
        // break;

            case android.R.id.text1:
                dismiss();
                break;
            case android.R.id.text2:
                if (mNumPlusMinusDialog != null) {
                    mNum = mPlusMinus.getNum();
                    mNumPlusMinusDialog.numChanged(mNum);
                }
                dismiss();
                break;
            default:
                break;
        }
    }

    public NumPlusMinusDialog setNumPlusMinusDialogListener(NumPlusMinusDialogListener callback) {
        this.mNumPlusMinusDialog = callback;
        return this;
    }

    public interface NumPlusMinusDialogListener {
        public void numChanged(int num);
    }

}
