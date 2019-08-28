package com.nahuo.quicksale.fragment;


import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.nahuo.library.helper.MD5Utils;
import com.nahuo.quicksale.R;
import com.nahuo.quicksale.ViewHub;
import com.nahuo.quicksale.app.BWApplication;
import com.nahuo.quicksale.base.BaseNewFragment;
import com.nahuo.quicksale.common.SafeUtils;
import com.nahuo.quicksale.common.SpManager;
import com.nahuo.quicksale.common.Utils;
import com.nahuo.quicksale.di.module.HttpManager;
import com.nahuo.quicksale.event.OnFragmentFinishListener;
import com.nahuo.quicksale.model.http.CommonSubscriber;
import com.nahuo.quicksale.model.http.response.PinHuoResponse;
import com.nahuo.quicksale.provider.UserInfoProvider;
import com.nahuo.quicksale.util.RxUtil;
import com.umeng.analytics.AnalyticsConfig;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import okhttp3.RequestBody;

/**
 * 设置密码
 */
public class SetPassWordFragment extends BaseNewFragment implements View.OnClickListener {
    public static final String ARGS_LISTENER = "ARGS_LISTENER";
    public static final String ARGS_NEED_OPEN_YFT = "ARGS_NEED_OPEN_YFT";
    private boolean NEED_OPEN_YFT = false;
    private OnFragmentFinishListener mListener;
    private EditText edtPassword;
    private Button btn_next;
    private ImageView img_see_pwd;
    private static String TAG = SetPassWordFragment.class.getSimpleName();

    public SetPassWordFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_set_pass_word, container, false);
        mListener = getArguments().getParcelable(ARGS_LISTENER);
        NEED_OPEN_YFT=getArguments().getBoolean(ARGS_NEED_OPEN_YFT,false);
        edtPassword = (EditText) view.findViewById(R.id.et_pass_word_num);
        btn_next = (Button) view.findViewById(R.id.btn_next);
        img_see_pwd = (ImageView) view.findViewById(R.id.img_see_pwd);
        img_see_pwd.setOnClickListener(this);
        btn_next.setOnClickListener(this);
        return view;
    }

    private void reSetPayPassWord() {
        String psw = edtPassword.getText().toString().trim();
        Map<String, String> params = new HashMap<String, String>();
        params.put("mobile", SpManager.getVerifyCodePhone(activity));
        params.put("password", MD5Utils.encrypt32bit(psw));
        params.put("isEncode", "true");
        params.put("code", SpManager.getVerifyCode(activity));
        params.put("deviceID", Utils.GetAndroidImei(BWApplication.getInstance()));
        params.put("channelCode", AnalyticsConfig.getChannel(BWApplication.getInstance()));
        addSubscribe(HttpManager.getInstance().getPinHuoNoCacheApi(TAG)
                .reSetPayPassword(params).compose(RxUtil.<PinHuoResponse<Object>>rxSchedulerHelper())
                .compose(RxUtil.handleResult())
                .subscribeWith(new CommonSubscriber<Object>(activity, true, R.string.yft_setting_paypsw) {
                    @Override
                    public void onNext(Object o) {
                        super.onNext(o);
                        ViewHub.showLongToast(activity, getString(R.string.yft_set_paypsw_suc));
                        UserInfoProvider.setHasSetPayPsw(activity, SpManager.getUserId(activity));
                        UserInfoProvider.setHasPayPassword(activity, 1);
                        if (mListener != null) {
                            mListener.onFinish(SetPassWordFragment.class);
                        }
                    }
                }));
    }
    private void setPayPassWord() {
        try {
            String psw = edtPassword.getText().toString().trim();
            TreeMap<String, String> params = new TreeMap<String, String>();
            params.put("pay_password", MD5Utils.encrypt32bit(psw));
            params.put("isEncode", "true");
            params.put("mobile", UserInfoProvider.getBindPhone(activity, SpManager.getUserId(activity)));
            SafeUtils.genPaySignParams(activity, params);
            JSONObject paramJson = new JSONObject();
            for (Map.Entry<String, String> keyValue : params.entrySet()) {
                String key = keyValue.getKey();
                String value = keyValue.getValue();
                paramJson.put(key, value);
            }
            RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json;charset=UTF-8"), paramJson.toString());
            addSubscribe(HttpManager.getInstance().getPinHuoNoCacheApi(TAG)
                    .setPayPassword(body).compose(RxUtil.<PinHuoResponse<Object>>rxSchedulerHelper())
                    .compose(RxUtil.handleResult())
                    .subscribeWith(new CommonSubscriber<Object>(activity, true, R.string.yft_setting_paypsw) {
                        @Override
                        public void onNext(Object o) {
                            super.onNext(o);
                            ViewHub.showLongToast(activity, getString(R.string.yft_set_paypsw_suc));
                            UserInfoProvider.setHasSetPayPsw(activity, SpManager.getUserId(activity));
                            UserInfoProvider.setHasPayPassword(activity, 1);
                            if (mListener != null) {
                                mListener.onFinish(SetPassWordFragment.class);
                            }
                        }
                    }));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_next:
                if (TextUtils.isEmpty(edtPassword.getText().toString().trim())) {
                    ViewHub.showShortToast(activity, "密码不能为空");
                    return;
                }
                if (NEED_OPEN_YFT){
                    setPayPassWord();
                }else {
                    reSetPayPassWord();
                }
                break;
            case R.id.img_see_pwd:
                int length = edtPassword.getText().length();
                if (length > 0) {
                    if (edtPassword.getInputType() == InputType.TYPE_TEXT_VARIATION_PASSWORD) {
                        edtPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        edtPassword.invalidate();
                        edtPassword.setSelection(length);
                    } else {
                        edtPassword.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        edtPassword.invalidate();
                        edtPassword.setSelection(edtPassword.getText().length());
                    }
                }
                break;
        }
    }
}
