package com.nahuo.quicksale;

import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.baidu.mobstat.StatService;
import com.nahuo.library.controls.LoadingDialog;
import com.nahuo.quicksale.api.ShopSetAPI;
import com.nahuo.quicksale.common.Const;
import com.nahuo.quicksale.common.SpManager;
import com.nahuo.quicksale.oldermodel.CreditJoinStatuModel;

/**
 * @description 服务承诺
 * @created 2015-3-4 下午4:28:08
 * @author ZZB
 */
public class SetPayPswFragment extends BaseSlideBackActivity implements View.OnClickListener {
    /**只查看协议*/
    public static final String EXTRA_READ_ONLY = "EXTRA_READ_ONLY";
    private Context              mContext = this;
    private LoadingDialog        mLoadingDialog;
    private CreditJoinStatuModel mJoinStatu;     // 7天退换货申请状态id
    private TextView             mTvContent;
    private Button               mBtnSubmit;
    private CheckBox             mCbAgree;
    private boolean mReadOnly;

    private static enum Step {
        LOAD_DATA, JOIN_CREDIT, QUIT_CREDIT
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_service_commitment);
        //getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.layout_titlebar_default);
        initExtras();
        initView();
        initData();
    }

    private void initExtras() {
        mReadOnly = getIntent().getBooleanExtra(EXTRA_READ_ONLY, false);
    }

    private void initData() {
        if(!mReadOnly){
            new Task(Step.LOAD_DATA).execute();
        }
        // if (!SpManager.isJoin7DaysDelivery(this)) {
        
        // }
    }

    private void initView() {
        initTitleBar();
        mLoadingDialog = new LoadingDialog(this);
        mTvContent = (TextView)findViewById(R.id.tv_commitment_content);
        mBtnSubmit = (Button)findViewById(R.id.btn_submit);
        mCbAgree = (CheckBox)findViewById(R.id.cb_agree);
        if(mReadOnly){
            mBtnSubmit.setVisibility(View.GONE);
            mCbAgree.setVisibility(View.GONE);
        }
    }

    private void initTitleBar() {
        TextView tvTitle = (TextView)findViewById(R.id.titlebar_tvTitle);
        tvTitle.setText("服务承诺");

        Button btnLeft = (Button)findViewById(R.id.titlebar_btnLeft);
        btnLeft.setText(R.string.titlebar_btnBack);
        btnLeft.setVisibility(View.VISIBLE);
        btnLeft.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_submit:// 加入、退出承诺
                onSubmitClicked(v);
                break;
            case R.id.btn_view_all:// 查看全部内容
                onViewAllClicked(v);
                break;
            case R.id.titlebar_btnLeft:
                finish();
                break;
        }
    }

    /**
     * @description 加入、退出承诺
     * @created 2015-3-5 上午11:21:58
     * @author ZZB
     */
    private void onSubmitClicked(View v) {
        switch (mJoinStatu.getId()) {
            case Const.CreditJoinStatuId.NOT_APPLY:
            case Const.CreditJoinStatuId.QUIT:
            case Const.CreditJoinStatuId.CHECK_NOT_PASSED:
                if (mCbAgree.isChecked()) {
                    ViewHub.showOkDialog(mContext, "提示", "加入后90天内不能退出", "确认", "取消",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    new Task(Step.JOIN_CREDIT).execute();
                                }
                            });
                } else {
                    ViewHub.shakeView(mCbAgree);
                }
                break;
            case Const.CreditJoinStatuId.CHECK_PASSED:// 审核通过
                new Task(Step.QUIT_CREDIT).execute();
                break;
        }
    }

    /**
     * @description 查看全部协议
     * @created 2015-3-5 上午10:16:00
     * @author ZZB
     */
    private void onViewAllClicked(View v) {
        TextView tv = (TextView)v;
        Object tag = v.getTag();
        if (tag == null) {
            mTvContent.setMaxLines(1000);
            tv.setText("收起>");
            v.setTag(v);
        } else {
            mTvContent.setMaxLines(6);
            tv.setText("全部协议内容>");
            v.setTag(null);
        }
    }

    /**
     * @description 数据加载完成
     * @created 2015-3-5 上午10:58:52
     * @author ZZB
     */
    public void onDataLoaded() {
        switch (mJoinStatu.getId()) {
            case Const.CreditJoinStatuId.NOT_APPLY:// 未加入
                restoreToDefaultStatu();
                setStatuResult("未加入");
                break;
            case Const.CreditJoinStatuId.CHECK_NOT_PASSED:// 审核未通过
                restoreToDefaultStatu();
                setStatuResult("审核未通过");
                break;
            case Const.CreditJoinStatuId.CHECK_PASSED:// 审核通过
                mBtnSubmit.setText("退出承诺");
                mCbAgree.setVisibility(View.GONE);
                SpManager.setJoin7DaysDelivery(mContext, true);
                setStatuResult("已加入");
                break;
            case Const.CreditJoinStatuId.QUIT:// 已退出
                restoreToDefaultStatu();
                SpManager.setJoin7DaysDelivery(mContext, false);
                setStatuResult("已退出");
                break;
            default:// 其他情况直接显示
                mCbAgree.setVisibility(View.GONE);
                mBtnSubmit.setClickable(false);
                mBtnSubmit.setText(mJoinStatu.getStatu());
                setStatuResult(mJoinStatu.getStatu());
                break;
        }
    }

    /**
     * @description 恢复到未加入的UI
     * @created 2015-3-5 下午2:07:00
     * @author ZZB
     */
    private void restoreToDefaultStatu() {
        mCbAgree.setVisibility(View.VISIBLE);
        mBtnSubmit.setText("加入承诺");
        mJoinStatu.setId(Const.CreditJoinStatuId.NOT_APPLY);
        mBtnSubmit.setClickable(true);
    }

    /**
     * @description 设置诚保状态值
     * @created 2015-3-5 下午1:55:08
     * @author ZZB
     */
    private void setStatuResult(String statu) {
        SpManager.set7DaysCreditStatu(mContext, statu);
        setResult(RESULT_OK);
    }

    private class Task extends AsyncTask<Object, Void, Object> {

        private Step mStep;

        public Task(Step step) {
            mStep = step;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            switch (mStep) {
                case LOAD_DATA:
                    mLoadingDialog.start("加载中...");
                    break;
                case JOIN_CREDIT:
                    mLoadingDialog.start("加入中...");
                    break;
                case QUIT_CREDIT:
                    mLoadingDialog.start("退出中...");
                    break;
            }
        }

        @Override
        protected Object doInBackground(Object... params) {
            try {
                switch (mStep) {
                    case LOAD_DATA:
                        mJoinStatu = ShopSetAPI.getCreditItemStatu(mContext, Const.CreditItemId._7_DAYS_DELIEVERY);
                        break;
                    case JOIN_CREDIT:
                        ShopSetAPI.joinCredit(mContext, Const.CreditItemId._7_DAYS_DELIEVERY);
                        break;
                    case QUIT_CREDIT:
                        ShopSetAPI.quitCredit(mContext, Const.CreditItemId._7_DAYS_DELIEVERY);
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
            super.onPostExecute(result);
            if (mLoadingDialog.isShowing()) {
                mLoadingDialog.stop();
            }
            if (result instanceof String && ((String)result).startsWith("error:")) {
                ViewHub.showLongToast(mContext, ((String)result).replace("error:", ""));
            } else {
                switch (mStep) {
                    case LOAD_DATA:
                        onDataLoaded();
                        break;
                    case JOIN_CREDIT:
                        mBtnSubmit.setText("已加入，退出承诺");
                        mCbAgree.setVisibility(View.GONE);
                        setStatuResult("已加入");
                        mJoinStatu.setId(Const.CreditJoinStatuId.CHECK_PASSED);
                        break;
                    case QUIT_CREDIT:
//                        restoreToDefaultStatu();
                        mBtnSubmit.setText("申请退出中");
                        mBtnSubmit.setClickable(false);
                        SpManager.setJoin7DaysDelivery(mContext, false);
                        setStatuResult("申请退出中");
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
}
