package com.nahuo.quicksale;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.Html;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.baidu.mobstat.StatService;
import com.nahuo.quicksale.util.AKUtil;

/**
 * Description:显示text的弹窗
 * 2014-7-24 下午3:59:19
 * 
 * @author ZZB
 */
public class TextDlgFragment extends DialogFragment {

    private TextView mTextView;
    private View mContentView;
    private String mHtmlText;
    private float mTextSize;
    private View.OnClickListener mDefaultListener;
    private Button mBtnNegative;
    private Button mBtnPositive;
    private Btn mNegative;
    private Btn mPositive;
    
    public static final String ARG_TEXT = "ARG_TEXT";
    public static final String ARG_TEXT_SIZE = "ARG_TEXT_SIZE";

    public static TextDlgFragment newInstance(String htmlText) {
        TextDlgFragment f = new TextDlgFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TEXT, htmlText);
        f.setArguments(args);
        return f;
    }
    
    public static TextDlgFragment newInstance(Bundle args) {
        TextDlgFragment f = new TextDlgFragment();
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.MyTryUseDialogFragment);
    }

    public void onResume() {
        super.onResume();
        setWidth();
        StatService.onResume(this);
    }
    private void setWidth(){
        DisplayMetrics dm = new DisplayMetrics();
        //获取屏幕信息
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenWidth = dm.widthPixels;
        getDialog().getWindow().setLayout(screenWidth- AKUtil.convertDIP2PX(getActivity(),80), ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    public void onPause() {
        super.onPause();
        StatService.onPause(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContentView = inflater.inflate(R.layout.dlg_trade_log, container, false);
        
        Bundle args = getArguments();
        mHtmlText = args.getString(ARG_TEXT);
        mTextSize = args.getFloat(ARG_TEXT_SIZE);
        initView();
        return mContentView;
    }

    private void initView() {
        mTextView = (TextView) mContentView.findViewById(R.id.content);
        if(mTextSize > 0){
            mTextView.setTextSize(mTextSize);
        }
        setHtmlText(mHtmlText);
        
        mBtnNegative = (Button) mContentView.findViewById(R.id.btn_negative);
        mBtnPositive = (Button) mContentView.findViewById(R.id.btn_positive);
        mDefaultListener = new Listener(null);
        initNegativeBtn();
        initPositiveBtn();

    }

    private void setHtmlText(String text) {
        mTextView.setText(Html.fromHtml(text));
    }

    public void updateText(String htmlText) {
        setHtmlText(htmlText);
    }

    public TextDlgFragment setNegativeListener(String text, View.OnClickListener negativeListener) {
        mNegative = new Btn(text, negativeListener);
        return this;
    }

    public TextDlgFragment setPositiveListener(String text, View.OnClickListener positiveListener) {
        mPositive = new Btn(text, positiveListener);
        return this;
    }

    private void initNegativeBtn() {
        boolean show = (mNegative != null);
        mBtnNegative.setVisibility(show ? View.VISIBLE : View.GONE);
        mBtnNegative.setOnClickListener(show ? new Listener(mNegative.listener)  : mDefaultListener);
        mBtnNegative.setText(show ? mNegative.text : "");
    }

    private void initPositiveBtn() {
        boolean show = (mPositive != null);
        mBtnPositive.setVisibility(show ? View.VISIBLE : View.GONE);
        
        mBtnPositive.setOnClickListener(show ? new Listener(mPositive.listener) : mDefaultListener);
        mBtnPositive.setText(show ? mPositive.text : "");
    }

    private static class Btn {
        String text;
        View.OnClickListener listener;

        public Btn(String text, OnClickListener listener) {
            super();
            this.text = text;
            this.listener = listener;
        }
    }

    private class Listener implements View.OnClickListener{
        private View.OnClickListener mListener;
        
        public Listener(OnClickListener listener) {
            super();
            mListener = listener;
        }

        @Override
        public void onClick(View v) {
            if(mListener != null){
                mListener.onClick(v);
            }
            dismiss();
        }
        
    }
    

    
}
