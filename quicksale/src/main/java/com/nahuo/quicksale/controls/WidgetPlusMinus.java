package com.nahuo.quicksale.controls;

import com.nahuo.quicksale.R;
import com.nahuo.quicksale.ViewHub;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

public class WidgetPlusMinus extends LinearLayout implements View.OnClickListener, TextWatcher {

    private View             mRootView;
    private Button           mBtnPlus, mBtnMinus;
    private EditText         mEtCount;

    private int              mNum = 1;
    private ICountNumChanged mICountNumChanged;

    // private int mMaxNum = 1000;

    public WidgetPlusMinus(Context context) {
        super(context);
        initView();
    }

    public WidgetPlusMinus(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    // public void setMaxNum(int maxNum) {
    // this.mMaxNum = maxNum;
    // }

    private void initView() {
        mRootView = LayoutInflater.from(getContext()).inflate(R.layout.widget_plus_minus, this);
        mBtnMinus = (Button)mRootView.findViewById(R.id.btn_minus);
        mBtnMinus.setOnClickListener(this);
        mBtnPlus = (Button)mRootView.findViewById(R.id.btn_plus);
        mBtnPlus.setOnClickListener(this);
        mEtCount = (EditText)mRootView.findViewById(R.id.et_count_num);
        mEtCount.addTextChangedListener(this);
        mEtCount.setText(mNum + "");
    }

    public void setNum(int num) {
        mNum = num;
        mEtCount.setText(mNum + "");
    }

    public int getNum() {
        Editable e = mEtCount.getText();
        mNum = Integer.valueOf(TextUtils.isEmpty(e) ? (1 + "") : e.toString());
        return mNum;
    }

    @Override
    public void onClick(View v) {
        mNum = getNum();
        switch (v.getId()) {
            case R.id.btn_minus:// 减
                if (mNum > 1) {
                    mNum--;
                    mEtCount.setText(mNum + "");
                    // if (mICountNumChanged != null)
                    // mICountNumChanged.countNumChanged(mNum);
                } else {
                    ViewHub.showShortToast(getContext(), "不能再减了，亲～");
                }
                break;
            case R.id.btn_plus:// 加
                mNum++;
                mEtCount.setText(mNum + "");
                // if (mICountNumChanged != null)
                // mICountNumChanged.countNumChanged(mNum);
                break;
            default:
                break;
        }
    }

    public void setICountNumChanged(ICountNumChanged l) {
        this.mICountNumChanged = l;
    }

    public interface ICountNumChanged {
        public void countNumChanged(int num);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (mICountNumChanged != null)
            mICountNumChanged.countNumChanged(getNum());

        // int len = s.length();
        // Log.i(getClass().getSimpleName(), "onTextChanged s:" + s);
        //
        // long num = Long.valueOf(s.toString()).longValue();
        //
        // if (len > 10 || num > mMaxNum) {
        // int selEndIndex = Selection.getSelectionEnd(s);
        // String str = s.toString();
        // // 截取新字符串
        // String newStr = str.substring(0, len - 1);
        // mEtCount.setText(newStr);
        // s = mEtCount.getText();
        // // 新字符串的长度
        // int newLen = s.length();
        // // 旧光标位置超过字符串长度
        // if (selEndIndex > newLen) {
        // selEndIndex = s.length();
        // }
        // // 设置新光标所在的位置
        // Selection.setSelection(mEtCount.getText(), selEndIndex);
        // ViewHub.showShortToast(getContext(), "您输入的数量超过了限制额");
        // }
    }

    @Override
    public void afterTextChanged(Editable s) {
        String text = s.toString();
        int len = s.toString().length();
        if (len == 1 && text.equals("0")) {
            s.clear();
        }
    }
}
