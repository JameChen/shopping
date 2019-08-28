package com.nahuo.quicksale.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.view.View;
import android.widget.TextView;

import com.nahuo.quicksale.BindPhoneActivity;
import com.nahuo.quicksale.FragmentTab;
import com.nahuo.quicksale.R;
import com.nahuo.quicksale.ViewHub;
import com.nahuo.quicksale.base.BaseAppCompatActivity;
import com.nahuo.quicksale.common.SafeUtils;
import com.nahuo.quicksale.common.SpManager;
import com.nahuo.quicksale.di.module.HttpManager;
import com.nahuo.quicksale.event.OnFragmentFinishListener;
import com.nahuo.quicksale.fragment.AuthenticationFragment;
import com.nahuo.quicksale.fragment.PhoneNumberFragment;
import com.nahuo.quicksale.fragment.SetPassWordFragment;
import com.nahuo.quicksale.fragment.VerificationCodeFragment;
import com.nahuo.quicksale.model.http.CommonSubscriber;
import com.nahuo.quicksale.model.http.response.PinHuoResponse;
import com.nahuo.quicksale.oldermodel.json.JPayUser;
import com.nahuo.quicksale.provider.UserInfoProvider;
import com.nahuo.quicksale.util.RxUtil;

import java.util.TreeMap;


public class FogetPayPassWordActivity extends BaseAppCompatActivity {
    public static String EXTRA_NEED_OPEN_YFT = "EXTRA_NEED_OPEN_YFT";
    private TextView mTitle;
    private FragmentTab.TabItem[] mFragmentTabs;
    private FragmentTab mFragmentTab;
    private int mCurrentrPos = -1;
    private static final int POS_GO_AUTHENTICAT = 0;
    private static final int POS_GO_PHONENUMBER = 1;
    private static final int POS_GO_VERIFICAT = 2;
    private static final int POS_GO_SETPASSWORD = 3;
    private static String TAG = FogetPayPassWordActivity.class.getSimpleName();
    private FogetPayPassWordActivity vThis = this;
    private boolean NEED_OPEN_YFT = false;
    private boolean NEED_PASS_SET = false;
    public static int BindRequestCode = 1;
    public static int BindResultCode = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foget_pay_pass_word);
        initTitleBar();
        getAccountBaseInfo();


    }

    private void init() {
        initFragmentTabs();
        if (mFragmentTab == null) {
            mFragmentTab = new FragmentTab(this, R.id.fragment_container, mFragmentTabs);
        }
        mCurrentrPos = 0;
        int userId = SpManager.getUserId(this);
        int HasPayPassword = UserInfoProvider.getHasPayPassword(this);
        boolean authed = UserInfoProvider.hasIdentityAuth(this, userId);
        boolean bindPhone = UserInfoProvider.hasBindPhone(this, userId);
        if (HasPayPassword > 0) {
            if (authed) {
                //toFragment(POS_GO_AUTHENTICAT);
                mTitle.setText("验证身份证");
                mCurrentrPos = POS_GO_AUTHENTICAT;
                mFragmentTab.setDefaultFragment(mCurrentrPos);
            } else {
                if (!bindPhone) {//没绑定手机
                    Intent intent = new Intent(this, BindPhoneActivity.class);
                    startActivityForResult(intent, BindRequestCode);
                    //  handleBackPressed();
                } else {
                    //toFragment(POS_GO_PHONENUMBER);
                    mTitle.setText("获取手机验证码");
                    mCurrentrPos = POS_GO_PHONENUMBER;
                    mFragmentTab.setDefaultFragment(mCurrentrPos);
                }
            }
        } else {
            if (!bindPhone) {//没绑定手机
                NEED_PASS_SET=true;
                Intent intent = new Intent(this, BindPhoneActivity.class);
                startActivityForResult(intent, BindRequestCode);
                //  handleBackPressed();
            }else {
                mCurrentrPos = POS_GO_SETPASSWORD;
                mTitle.setText("设置支付密码");
                mFragmentTab.setDefaultFragment(mCurrentrPos);
            }
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == BindRequestCode) {
            if (resultCode == BindResultCode) {
                if (NEED_PASS_SET){
                    mCurrentrPos = POS_GO_SETPASSWORD;
                    mFragmentTab.setDefaultFragment(mCurrentrPos);
                    toFragment(mCurrentrPos);
                }else {
                    mCurrentrPos = POS_GO_PHONENUMBER;
                    mFragmentTab.setDefaultFragment(mCurrentrPos);
                    toFragment(mCurrentrPos);
                }
            }
        }
    }

    /**
     * 获取支付用户信息
     *
     * @author James Chen
     * @create time in 2018/5/11 16:21
     */
    private void getAccountBaseInfo() {
        TreeMap<String, String> params = new TreeMap<String, String>();
        addSubscribe(HttpManager.getInstance().getPinHuoNoCacheApi(TAG
        ).getAccountBaseInfo(SafeUtils.genPaySignParams(vThis, params)
        )
                .compose(RxUtil.<PinHuoResponse<JPayUser>>rxSchedulerHelper())
                .compose(RxUtil.<JPayUser>handleResult())
                .subscribeWith(new CommonSubscriber<JPayUser>(vThis, true, R.string.loading) {
                    @Override
                    public void onNext(JPayUser jPayUser) {
                        super.onNext(jPayUser);
                        if (jPayUser != null) {
                            UserInfoProvider.setBankState(vThis, SpManager.getUserId(vThis), jPayUser.getBankInfoStatu());// 缓存银行状态
                            UserInfoProvider.cachePayUserInfo(vThis, jPayUser);
                            init();
                        }
                    }

                }));
    }

    /**
     * 开通衣付通
     *
     * @author James Chen
     * @create time in 2018/5/11 16:21
     */
    private void setSettlement() {
        TreeMap<String, String> params = new TreeMap<String, String>();
        params.put("type", "open");
        addSubscribe(HttpManager.getInstance().getPinHuoNoCacheApi(TAG
        ).setSettlement(SafeUtils.genPaySignParams(vThis, params)
        )
                .compose(RxUtil.<PinHuoResponse<Object>>rxSchedulerHelper())
                .compose(RxUtil.<Object>handleResult())
                .subscribeWith(new CommonSubscriber<Object>(vThis, true, "开通衣付通....") {
                    @Override
                    public void onNext(Object jPayUser) {
                        super.onNext(jPayUser);
                        UserInfoProvider.setHasOpenedYFT(vThis, SpManager.getUserId(vThis));
                        ViewHub.showOkDialog(vThis, "提示", "在线结算已开通", "OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                handleBackPressed();
                            }
                        });
                    }

                }));
    }

    private void initTitleBar() {
        NEED_OPEN_YFT = getIntent().getBooleanExtra(FogetPayPassWordActivity.EXTRA_NEED_OPEN_YFT, false);
        mTitle = (TextView) findViewById(R.id.titlebar_tvTitle);
        //mTitle.setText(R.string.yft_titlebar_text);
        View btnRight = findViewById(R.id.titlebar_btnRight);
        btnRight.setVisibility(View.GONE);
        View btnLeft = findViewById(R.id.titlebar_btnLeft);
        btnLeft.setVisibility(View.VISIBLE);
        btnLeft.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                handleBackPressed();
            }
        });
    }

    private void initFragmentTabs() {
        mFragmentTabs = new FragmentTab.TabItem[]{
                new FragmentTab.TabItem(AuthenticationFragment.class,//身份验证
                        genFragmentArgs(AuthenticationFragment.ARGS_LISTENER, SetPassWordFragment.ARGS_NEED_OPEN_YFT)),
                new FragmentTab.TabItem(PhoneNumberFragment.class,// 绑定手机
                        genFragmentArgs(PhoneNumberFragment.ARGS_LISTENER, SetPassWordFragment.ARGS_NEED_OPEN_YFT)),
                new FragmentTab.TabItem(VerificationCodeFragment.class,// 验证码
                        genFragmentArgs(VerificationCodeFragment.ARGS_LISTENER, SetPassWordFragment.ARGS_NEED_OPEN_YFT)),
                new FragmentTab.TabItem(SetPassWordFragment.class,// 设置密码
                        genFragmentArgs(SetPassWordFragment.ARGS_LISTENER, SetPassWordFragment.ARGS_NEED_OPEN_YFT))};
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

    private Bundle genFragmentArgs(String argKey, String argkey2) {
        Bundle args = new Bundle();
        args.putBoolean(argkey2, NEED_OPEN_YFT);
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

    public void switchFragment(Class clz) {
        String clzName = clz.getName();
        // int userId = SpManager.getUserId(this);
//        if (clzName.equals(YFTInitFragment.class.getName())) {
//            toFragment(POS_AGREEMENT);
//        } else {
//            jumpLogic(userId);
//        }
        int userId = SpManager.getUserId(this);
        //  boolean authed = UserInfoProvider.hasIdentityAuth(this, userId);
        boolean bindPhone = UserInfoProvider.hasBindPhone(this, userId);
        if (clzName.equals(AuthenticationFragment.class.getName())) {
            if (!bindPhone) {//没绑定手机
                Intent intent = new Intent(this, BindPhoneActivity.class);
                startActivityForResult(intent, BindRequestCode);
                // startActivity(intent);
                // finish();
            } else {
                toFragment(POS_GO_PHONENUMBER);
            }
        } else if (clzName.equals(PhoneNumberFragment.class.getName())) {
            toFragment(POS_GO_VERIFICAT);
        } else if (clzName.equals(VerificationCodeFragment.class.getName())) {
            toFragment(POS_GO_SETPASSWORD);
        } else if (clzName.equals(SetPassWordFragment.class.getName())) {
            if (NEED_OPEN_YFT) {
                setSettlement();
            } else {
                handleBackPressed();
            }
        }

    }

    private void toFragment(int pos) {
        switch (pos) {
            case POS_GO_AUTHENTICAT:
                mCurrentrPos = POS_GO_AUTHENTICAT;
                mTitle.setText("验证身份证");
                break;
            case POS_GO_PHONENUMBER:
                mCurrentrPos = POS_GO_PHONENUMBER;
                mTitle.setText("获取手机验证码");
                break;
            case POS_GO_VERIFICAT:
                mCurrentrPos = POS_GO_VERIFICAT;
                mTitle.setText("输入验证码");
                break;
            case POS_GO_SETPASSWORD:
                mCurrentrPos = POS_GO_SETPASSWORD;
                mTitle.setText("设置支付密码");
                break;

        }
        mFragmentTab.onSwitch(mCurrentrPos);
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

    @Override
    public void onBackPressed() {

        handleBackPressed();
    }
}
