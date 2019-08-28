package com.nahuo.quicksale;

import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.nahuo.library.controls.LightAlertDialog;
import com.nahuo.quicksale.common.Const.PasswordExtra;
import com.nahuo.quicksale.common.Const.PasswordType;
import com.nahuo.quicksale.common.SpManager;
import com.nahuo.quicksale.provider.UserInfoProvider;

/**
 * @description 手机已经绑定页面
 * @created 2014-8-20 下午4:02:42
 * @author ZZB
 */
public class PhoneBindedFragment extends Fragment{

    private Context mContext;
    private TextView mTvPhone;
    private Button mBtnChangePhone;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.frgm_phone_binded, container, false);
        initView(v);
        return v;
    }

    private void initView(View contentView) {
        mTvPhone = (TextView) contentView.findViewById(R.id.tv_phone);
        
        String phone = UserInfoProvider.getBindPhone(mContext, SpManager.getUserId(mContext));
        mTvPhone.setText(phone);
        
        mBtnChangePhone = (Button) contentView.findViewById(R.id.btn_change_phone);
        mBtnChangePhone.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean set = UserInfoProvider.hasSetSafeQuestion(mContext, SpManager.getUserId(mContext));
                if(set){
                    Bundle args = new Bundle();
                    args.putString(TextDlgFragment.ARG_TEXT, "<br><font color='black'>修改后原来的手机号将不能再登录</font><br>");
                    args.putFloat(TextDlgFragment.ARG_TEXT_SIZE, 18f);
                    TextDlgFragment f = TextDlgFragment.newInstance(args);
                    f.setNegativeListener("关闭", null).setPositiveListener("确认换号", new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(getActivity(), AnswerSafeQuestionsActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY); 
                            startActivity(intent);
                        }
                    });
                    f.show(getActivity().getSupportFragmentManager(), "TextDlgFragment");
                }else{
                    boolean setPayPsw = UserInfoProvider.hasSetPayPsw(mContext, SpManager.getUserId(mContext));
                    if(setPayPsw){
                        setSafeQ();
                    }else{
                        setPayPsw();
                    }
                }
                
            }
        });
        
        
        
    }

    /**
     * @description 提示未设置密码问题
     * @created 2014-9-18 下午2:28:34
     * @author ZZB
     */
    private void setSafeQ(){
        Builder builder = LightAlertDialog.Builder.create(mContext);
        builder.setTitle("提示").setMessage("您还没有设置密码问题").setNegativeButton("取消", null)
                .setPositiveButton("去设置", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(mContext, SetSafeQuestionsActivity.class);
                        startActivity(intent);
                    }
                });
        builder.show();
    }
    /**
     * @description 提示未设置支付密码
     * @created 2014-9-18 下午2:28:18
     * @author ZZB
     */
    private void setPayPsw(){
        Builder builder = LightAlertDialog.Builder.create(mContext);
        builder.setTitle("提示").setMessage("您还未设置支付密码").setPositiveButton("去设置", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(mContext, SetPswActivity1.class);
                intent.putExtra(PasswordExtra.EXTRA_PSW_TYPE, PasswordType.PAYMENT);
                startActivity(intent);
            }}).setNegativeButton("取消", null);
        builder.show();
    }
    
    
}
