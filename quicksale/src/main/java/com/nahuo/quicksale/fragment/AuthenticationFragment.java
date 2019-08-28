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
import com.nahuo.quicksale.di.module.HttpManager;
import com.nahuo.quicksale.event.OnFragmentFinishListener;
import com.nahuo.quicksale.model.http.CommonSubscriber;
import com.nahuo.quicksale.model.http.response.PinHuoResponse;
import com.nahuo.quicksale.util.RxUtil;

/**
 * 身份验证
 */
public class AuthenticationFragment extends BaseNewFragment {
    public static final String ARGS_LISTENER = "ARGS_LISTENER";
    private OnFragmentFinishListener mListener;
    private EditText et_authentication_num;
    private Button btn_next;
    private String cardId;
    private static String TAG = AuthenticationFragment.class.getSimpleName();

    public AuthenticationFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_authentication, container, false);
        mListener = getArguments().getParcelable(ARGS_LISTENER);
        et_authentication_num = (EditText) view.findViewById(R.id.et_authentication_num);
        btn_next = (Button) view.findViewById(R.id.btn_next);
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cardId = et_authentication_num.getText().toString().trim();
                if (TextUtils.isEmpty(cardId)) {
                    ViewHub.showShortToast(activity, "请输入身份证号码");
                } else if (!FunctionHelper.isIDCard(cardId)) {
                    ViewHub.showShortToast(activity, "请输入正确身份证号码");
                } else {
                    checkIDCard();
                }
            }
        });
        return view;
    }

    private void checkIDCard() {
        addSubscribe(HttpManager.getInstance().getPinHuoNoCacheApi(TAG)
                .checkIDCard(cardId).compose(RxUtil.<PinHuoResponse<Object>>rxSchedulerHelper())
                .compose(RxUtil.handleResult()).subscribeWith(new CommonSubscriber<Object>(activity,true,R.string.loading) {
                    @Override
                    public void onNext(Object o) {
                        super.onNext(o);
                        mListener.onFinish(AuthenticationFragment.class);
                    }
                }));
    }

}
