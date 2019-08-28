package com.nahuo.quicksale;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.baidu.mobstat.StatService;
import com.nahuo.library.controls.LoadingDialog;
import com.nahuo.quicksale.api.ShopSetAPI;
import com.nahuo.quicksale.common.Const;
import com.nahuo.quicksale.oldermodel.json.JPostFee;

public class SetExpressFeeActivity extends BaseActivity implements
        OnClickListener {

    private Context mContext = this;
    private Button btnLeft;
    private TextView tvTitle;
    private EditText mEtPostFee, mEtFreePostAmount;
    private RadioGroup radiogrp;
    private LinearLayout showlayout;
    private int mCurFeeTypeId;
    private LoadingDialog mLoadingDialog;

    private static enum Step {
        LOAD_FEE, SET_FEE
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);// 设置自定义标题栏
        setContentView(R.layout.activity_setexpressfee);
//        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
//                R.layout.layout_titlebar_default);// 更换自定义标题栏布局

        initView();
        initData();
    }

    private void initData() {
        mCurFeeTypeId = Const.PostFeeType.VENDOR_TOTAL;
        new Task(Step.LOAD_FEE).execute();
    }

    private void initView() {
        mLoadingDialog = new LoadingDialog(this);
        // 标题栏
        tvTitle = (TextView) findViewById(R.id.titlebar_tvTitle);

        tvTitle.setText(R.string.me_my_expfee);
        btnLeft = (Button) findViewById(R.id.titlebar_btnLeft);
        btnLeft.setText(R.string.titlebar_btnBack);
        btnLeft.setVisibility(View.VISIBLE);
        btnLeft.setOnClickListener(this);

        mEtPostFee = (EditText) findViewById(R.id.et_post_fee);
        //多少起免运费
        mEtFreePostAmount = (EditText) findViewById(R.id.et_free_post_amount);
        radiogrp = (RadioGroup) findViewById(R.id.express_setexpress_radgrp);

        showlayout = (LinearLayout) findViewById(R.id.express_setexpress_layout);

        radiogrp.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int radioButtonId = group.getCheckedRadioButtonId();
                if (radioButtonId == R.id.rd_custom_fee) {
                    showlayout.setVisibility(View.VISIBLE);
                    mCurFeeTypeId = Const.PostFeeType.UNIFICATION;
                } else {
                    showlayout.setVisibility(View.GONE);
                    mCurFeeTypeId = Const.PostFeeType.VENDOR_TOTAL;
                }
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.titlebar_btnLeft:
            finish();
            break;
        case R.id.btn_submit:
            onSubmitClick();
            break;
        }
    }

    private void onSubmitClick() {
        JPostFee fee = null;
        if(mCurFeeTypeId == Const.PostFeeType.UNIFICATION){
            if(TextUtils.isEmpty(mEtPostFee.getText())){
                ViewHub.setEditError(mEtPostFee, "请设置运费");
                return;
            }else if(TextUtils.isEmpty(mEtFreePostAmount.getText())){
                ViewHub.setEditError(mEtFreePostAmount, "请设置金额");
                return;
            }
            double postFee = Double.valueOf(mEtPostFee.getText().toString());
            double freePostAmount = Double.valueOf(mEtFreePostAmount.getText().toString());
            fee = new JPostFee(mCurFeeTypeId, postFee, freePostAmount);
        }else{
            fee = new JPostFee(mCurFeeTypeId);
        }
        new Task(Step.SET_FEE).execute(fee);
    }

    private class Task extends AsyncTask<Object, Void, Object> {

        private Step mStep;

        public Task(Step step) {
            mStep = step;
        }

        @Override
        protected void onPreExecute() {
            switch (mStep) {
            case LOAD_FEE:
                mLoadingDialog.start("加载运费中...");
                break;
            case SET_FEE:
                mLoadingDialog.start("设置运费中...");
                break;
            }

        }

        @Override
        protected Object doInBackground(Object... params) {
            try {
                switch (mStep) {
                case LOAD_FEE:
                    JPostFee obj = ShopSetAPI.getPostFee(mContext);
                    return obj;
                case SET_FEE:
                    JPostFee fee =(JPostFee) params[0];
                    ShopSetAPI.setPostFee(mContext, fee);
                    break;
                }
            } catch (Exception e) {
                e.printStackTrace();
                return "error:" + e.getMessage();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Object result) {
            if (mLoadingDialog.isShowing()) {
                mLoadingDialog.stop();
            }
            if (result instanceof String && ((String) result).startsWith("error:")) {
                ViewHub.showLongToast(mContext, ((String) result).replace("error:", ""));
            } else {
                switch (mStep) {
                case LOAD_FEE:
                    JPostFee fee = (JPostFee) result;
                    if(fee.getPostFeeTypeID() == 2){
                        mEtFreePostAmount.setText(fee.getFreePostFeeAmount() + "");
                        mEtPostFee.setText(fee.getDefaultPostFee() + "");
                        radiogrp.check(R.id.rd_custom_fee);
                    }
                    
                    break;
                case SET_FEE:
                    ViewHub.showOkDialog(mContext, "提示", "设置成功", "OK");
                    break;
                }
                
            }
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        StatService.onPause(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        StatService.onResume(this);
    }

    @Override
    public void finish() {
        super.finish();
        ViewHub.hideKeyboard(this);
    }
}
