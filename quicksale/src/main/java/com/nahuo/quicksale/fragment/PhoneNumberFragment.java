package com.nahuo.quicksale.fragment;


import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.nahuo.library.helper.FunctionHelper;
import com.nahuo.quicksale.R;
import com.nahuo.quicksale.ViewHub;
import com.nahuo.quicksale.base.BaseNewFragment;
import com.nahuo.quicksale.common.SpManager;
import com.nahuo.quicksale.di.module.HttpManager;
import com.nahuo.quicksale.event.OnFragmentFinishListener;
import com.nahuo.quicksale.model.http.CommonSubscriber;
import com.nahuo.quicksale.model.http.response.PinHuoResponse;
import com.nahuo.quicksale.util.RxUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * 手机号
 */
public class PhoneNumberFragment extends BaseNewFragment {
    public static final String ARGS_LISTENER = "ARGS_LISTENER";
    private OnFragmentFinishListener mListener;
    private EditText et_phone_num;
    private Button btn_next;
    private String phone;
    private static String TAG = PhoneNumberFragment.class.getSimpleName();
    private int smstype = 1;

    public PhoneNumberFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_phone_number, container, false);
        mListener = getArguments().getParcelable(ARGS_LISTENER);
        et_phone_num = (EditText) view.findViewById(R.id.et_phone_num);
        btn_next = (Button) view.findViewById(R.id.btn_next);
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phone = et_phone_num.getText().toString().trim();
                if (TextUtils.isEmpty(phone)) {
                    ViewHub.showShortToast(activity, "请输入手机号码");
                } else if (!FunctionHelper.isMobileNum(phone)) {
                    ViewHub.showShortToast(activity, "请输入正确手机号码");
                } else {
                    check();
                }
            }
        });
        return view;
    }
    private void check() {
        Map<String, Object> params = new HashMap<>();
        params.put("mobile", phone);
        params.put("usefor", "findpassword");
        params.put("username", "");
        params.put("smstype", smstype + "");
        params.put("messageFrom", "天天拼货团");
        addSubscribe(HttpManager.getInstance().getPinHuoNoCacheApi(TAG)
                .getMobileVerifyCode2(params).compose(RxUtil.<PinHuoResponse<Object>>rxSchedulerHelper())
                .compose(RxUtil.handleResult()).subscribeWith(new CommonSubscriber<Object>(activity,true,"获取验证码中") {
                    @Override
                    public void onNext(Object o) {
                        super.onNext(o);
                        SpManager.setVerifyCodePhone(activity,phone);
                        mListener.onFinish(PhoneNumberFragment.class);
                    }
                }));
    }
}
