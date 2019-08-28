package com.nahuo.quicksale.yft;

import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcel;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.baidu.mobstat.StatService;
import com.nahuo.library.controls.LoadingDialog;
import com.nahuo.quicksale.BaseSlideBackActivity;
import com.nahuo.quicksale.FragmentTab;
import com.nahuo.quicksale.FragmentTab.TabItem;
import com.nahuo.quicksale.R;
import com.nahuo.quicksale.ViewHub;
import com.nahuo.quicksale.api.PaymentAPI;
import com.nahuo.quicksale.common.SpManager;
import com.nahuo.quicksale.event.OnFragmentFinishListener;
import com.nahuo.quicksale.oldermodel.json.JPayUser;
import com.nahuo.quicksale.provider.UserInfoProvider;

public class YFTActivity extends BaseSlideBackActivity {

    private static final String TAG = YFTActivity.class.getSimpleName();
    private LoadingDialog mDialog;
    private static final int POS_SET_SAFEQ = 3;
    private static final int POS_PAY_PSW = 2;
    private static final int POS_BINDPHONE = 1;
    private static final int POS_AGREEMENT = 11;
    private static final int POS_INIT_FRAGMENT = 0;

    /** 标志是否来自登录 */
    // public static final String EXTRA_FROM_LOGIN = "EXTRA_FROM_LOGIN";
    private TabItem[] mFragmentTabs;
    private FragmentTab mFragmentTab;
    
    private Button showAllContentBtn;
    private View showAllContent;
    
    private int mCurrentrPos = -1;
    /** 是否来自登录 */
    // private boolean mFromLogin;

    private TextView mTitle;
    protected Context mContext = this;

    private static enum Step {
        LOAD_USER_INFO, OPEN_YFT
    }
    private int HasPayPassword=-1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      //  requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);// 设置自定义标题栏
        // mFromLogin = getIntent().getBooleanExtra(EXTRA_FROM_LOGIN, false);
        HasPayPassword=UserInfoProvider.getHasPayPassword(this);

        if (isYftOpened()) {
            createOpenedView();
        } else {
            createNotOpenedView();
        }
    }

    /**
     * @description 已经开通的View
     * @created 2014-8-20 下午4:36:33
     * @author ZZB
     */
    private void createOpenedView() {
        setContentView(R.layout.activity_yft_opened);
//        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
//                R.layout.layout_titlebar_default);
        initTitleBar();

		 showAllContentBtn = (Button) findViewById(R.id.yft_agreement_full_content_btn);
		showAllContent = findViewById(R.id.yft_agreement_full_content);
		showAllContentBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
			    v.setTag(showAllContent.getVisibility());
			    boolean isContentVisible = ((Integer)v.getTag()) == View.VISIBLE ? true : false;
			    showAllContentBtn.setText(isContentVisible ?  "在线结算协议>" : "收起");
				showAllContent.setVisibility(isContentVisible ? View.GONE : View.VISIBLE);
				
			}

		});
    }

    /**
     * @description 末开通的View
     * @created 2014-8-20 下午4:36:43
     * @author ZZB
     */
    private void createNotOpenedView() {
        setContentView(R.layout.activity_fragment);
//        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
//                R.layout.layout_titlebar_default);
        mDialog = new LoadingDialog(this);
        initTitleBar();
        initFragmentTabs();
        if (mFragmentTab == null) {
            mFragmentTab = new FragmentTab(this, R.id.fragment_container, mFragmentTabs);
        }
        mCurrentrPos = 0;
        mFragmentTab.setDefaultFragment(mCurrentrPos);
        //加载支付用户基本信息
        new Task(Step.LOAD_USER_INFO).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }


    private void initFragmentTabs() {

        mFragmentTabs = new TabItem[] {
                new TabItem(YFTInitFragment.class, genFragmentArgs(YFTInitFragment.ARGS_LISTENER)),// 初始化
//                new TabItem(AgreementFragment.class,// 协议
//                        genFragmentArgs(AgreementFragment.ARGS_LISTENER)),
                new TabItem(BindPhoneFragment.class,// 绑定手机
                        genFragmentArgs(BindPhoneFragment.ARGS_LISTENER)),
                new TabItem(SetPayPswFragment.class,// 设置支付密码
                        genFragmentArgs(SetPayPswFragment.ARGS_LISTENER)),
                new TabItem(SetSafeQuestionsFragment.class,// 设置安全问题
                        genFragmentArgs(SetSafeQuestionsFragment.ARGS_LISTENER)) };
    }

    private Bundle genFragmentArgs(String argKey) {
        Bundle args = new Bundle();
        args.putParcelable(argKey, new OnFragmentFinishListener() {

            @Override
            public void writeToParcel(Parcel dest, int flags) {
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void onFinish(Class clz) {
                switchFragment(clz);
            }
        });
        return args;
    }

    public void onClick(View view) {

//        switch (view.getId()) {
//        case R.id.yft_btn_back:
//            toMainActivity();
//            break;
//        }
    }

    /**
     * Description:切换fragment 2014-7-1下午4:48:13
     */
    public void switchFragment(Class clz) {
        String clzName = clz.getName();
        int userId = SpManager.getUserId(this);
//        if (clzName.equals(YFTInitFragment.class.getName())) {
//            toFragment(POS_AGREEMENT);
//        } else {
//            jumpLogic(userId);
//        }
        jumpLogic(userId);

    }

    /**
     * @description 跳转逻辑
     * @created 2014-9-10 下午2:11:36
     * @author ZZB
     */
    private void jumpLogic(int userId) {
        boolean binded = UserInfoProvider.hasBindPhone(this, userId);
        if (binded) {
            boolean setPayPsw = UserInfoProvider.hasSetPayPsw(this, userId);
            if (setPayPsw) {
                boolean setSafeQ = UserInfoProvider.hasSetSafeQuestion(this, userId);
                if (setSafeQ) {
                    openYft();
                } else {
                    toFragment(POS_SET_SAFEQ);
                }
            } else {
                toFragment(POS_PAY_PSW);
            }
        } else {
            toFragment(POS_BINDPHONE);
        }
    }

    /**
     * @description 开通衣付通
     * @created 2014-9-10 下午3:14:35
     * @author ZZB
     */
    private void openYft() {
        new Task(Step.OPEN_YFT).execute();
    }

    private void toFragment(int pos) {
        switch (pos) {
        case POS_INIT_FRAGMENT:
            mCurrentrPos = POS_INIT_FRAGMENT;
            mTitle.setText("在线结算");
            break;
        case POS_AGREEMENT:
            mCurrentrPos = POS_AGREEMENT;
            mTitle.setText("在线结算");
            break;
        case POS_BINDPHONE:
            mCurrentrPos = POS_BINDPHONE;
            mTitle.setText("绑定手机");
            break;
        case POS_PAY_PSW:
            mCurrentrPos = POS_PAY_PSW;
            mTitle.setText("设置支付密码");
            break;
        case POS_SET_SAFEQ:
            mCurrentrPos = POS_SET_SAFEQ;
            mTitle.setText("设置安全问题");
            break;

        }
        mFragmentTab.onSwitch(mCurrentrPos);
    }

    private void initTitleBar() {
        mTitle = (TextView) findViewById(R.id.titlebar_tvTitle);
        mTitle.setText(R.string.yft_titlebar_text);
        View btnRight = findViewById(R.id.titlebar_btnRight);
        btnRight.setVisibility(View.GONE);
        View btnLeft = findViewById(R.id.titlebar_btnLeft);
        btnLeft.setVisibility(View.VISIBLE);
        btnLeft.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                handleBackPressed();
            }
        });
    }


    /**
     * @description 处理后退
     * @created 2014-8-14 下午1:56:12
     * @author ZZB
     */
    private void handleBackPressed() {
        ViewHub.hideKeyboard(this);
        finish();
    }

    /**
     * 2014-7-1上午9:56:50 检查是否开通衣付通
     */
    private boolean isYftOpened() {
        return UserInfoProvider.hasOpenedYFT(this, SpManager.getUserId(this));
    }

    @Override
    public void onBackPressed() {
    	
        handleBackPressed();
    }

    private class Task extends AsyncTask<Void, Void, Object> {
        private Step mStep;

        public Task(Step step) {
            mStep = step;
        }

        @Override
        protected void onPreExecute() {
            switch (mStep) {
            case OPEN_YFT:
                mDialog.start("开通衣付通中...");
                break;
            case LOAD_USER_INFO:
                mDialog.start("加载数据中...");
                break;
            }
            
        }

        @Override
        protected Object doInBackground(Void... params) {
            try {
                switch (mStep) {
                case OPEN_YFT:
                    PaymentAPI.openYFT(mContext);
                    break;
                case LOAD_USER_INFO:
                    JPayUser payuser = PaymentAPI.getUserInfo(mContext);
                    UserInfoProvider.cachePayUserInfo(mContext, payuser);
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
            if (mDialog.isShowing()) {
                mDialog.stop();
            }
            if (result instanceof String && ((String) result).startsWith("error:")) {
                ViewHub.showLongToast(mContext, ((String) result).replace("error:", ""));
            }else{
                switch(mStep){
                case OPEN_YFT:
                    UserInfoProvider.setHasOpenedYFT(mContext, SpManager.getUserId(mContext));
                    ViewHub.showOkDialog(mContext, "提示", "在线结算已开通", "OK", new DialogInterface.OnClickListener(){
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        
                            finish();
                        }});
                    break;
                case LOAD_USER_INFO:
                    
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
