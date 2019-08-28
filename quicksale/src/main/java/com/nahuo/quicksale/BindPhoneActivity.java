package com.nahuo.quicksale;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Parcel;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.baidu.mobstat.StatService;
import com.nahuo.library.controls.LightAlertDialog;
import com.nahuo.quicksale.activity.FogetPayPassWordActivity;
import com.nahuo.quicksale.common.SpManager;
import com.nahuo.quicksale.event.OnFragmentFinishListener;
import com.nahuo.quicksale.provider.UserInfoProvider;
import com.nahuo.quicksale.yft.BindPhoneFragment;

public class BindPhoneActivity extends BaseSlideBackActivity {

    private static final String TAG = BindPhoneActivity.class.getSimpleName();
    public static final String EXTRA_CHANGE_BIND_PHONE = "EXTRA_CHANGE_BIND_PHONE";
    protected Context mContext = this;
    private boolean mChangeBindPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_fragment);
//        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
//                R.layout.layout_titlebar_default);
        mChangeBindPhone = getIntent().getBooleanExtra(EXTRA_CHANGE_BIND_PHONE, false);
        initFragment();
    }

    private void initTitleBar() {
        TextView tvTitle = (TextView) findViewById(R.id.titlebar_tvTitle);
        tvTitle.setText("绑定手机");
        View btnLeft = findViewById(R.id.titlebar_btnLeft);
        btnLeft.setVisibility(View.VISIBLE);
        btnLeft.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initFragment() {
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);
        if (fragment == null) {
            fragment = createFragment();
            fm.beginTransaction().add(R.id.fragment_container, fragment).commit();
        } else {
            Log.d(TAG, fragment.getClass().getSimpleName());
        }
    }

    private Fragment createFragment() {
        initTitleBar();
        boolean hasBinded = isPhoneBinded();
        if (hasBinded && !mChangeBindPhone) {
            return new PhoneBindedFragment();
        } else {//没有绑定手机或者修改绑定手机
            BindPhoneFragment f = new BindPhoneFragment();
            Bundle args = new Bundle();

            args.putParcelable(BindPhoneFragment.ARGS_LISTENER, new OnFragmentFinishListener() {
                @Override
                public void onFinish(Class clz) {
                    LightAlertDialog.Builder.create(mContext).setTitle("提示").setMessage("绑定手机成功")
                            .setPositiveButton("好", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    setResult(FogetPayPassWordActivity.BindResultCode);
                                    finish();
                                }
                            }).show();
                }

                @Override
                public int describeContents() {
                    return 0;
                }

                @Override
                public void writeToParcel(Parcel dest, int flags) {
                }
            });
            f.setArguments(args);
            return f;
        }
    }

    /**
     * @description 检查是否绑定手机
     * @created 2014-9-3 上午10:45:02
     * @author ZZB
     */
    private boolean isPhoneBinded() {
        boolean result = UserInfoProvider.hasBindPhone(this, SpManager.getUserId(this));
        
        return result;
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
