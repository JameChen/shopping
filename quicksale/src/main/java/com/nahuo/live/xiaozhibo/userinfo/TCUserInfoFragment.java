package com.nahuo.live.xiaozhibo.userinfo;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nahuo.live.xiaozhibo.common.utils.TCUtils;
import com.nahuo.live.xiaozhibo.common.widget.TCLineControllerView;
import com.nahuo.live.xiaozhibo.login.TCUserMgr;
import com.nahuo.quicksale.R;
import com.tencent.imsdk.TIMManager;
import com.tencent.rtmp.TXLiveBase;

import org.json.JSONObject;

/**
 * 用户资料展示页面
 */
public class TCUserInfoFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "TCUserInfoFragment";
    private ImageView mHeadPic;
    private TextView mNickName;
    private TextView mUserId;
    private TCLineControllerView mBtnLogout;
    private TCLineControllerView mBtnSet;
    private TCLineControllerView mVersion;
    private TCLineControllerView mGetTechnicalSupport;
    private TextView mTvDownloadSDK;
    private TextView mTvProductIntroduction;

    public TCUserInfoFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_info, container, false);
        mHeadPic = (ImageView) view.findViewById(R.id.iv_ui_head);
        mNickName = (TextView) view.findViewById(R.id.tv_ui_nickname);
        mUserId = (TextView) view.findViewById(R.id.tv_ui_user_id);
        mBtnSet = (TCLineControllerView) view.findViewById(R.id.lcv_ui_set);
        mBtnLogout = (TCLineControllerView) view.findViewById(R.id.lcv_ui_logout);
        mVersion = (TCLineControllerView) view.findViewById(R.id.lcv_ui_version);
        mGetTechnicalSupport = (TCLineControllerView) view.findViewById(R.id.lcv_get_technical_support);
        mBtnSet.setOnClickListener(this);
        mBtnLogout.setOnClickListener(this);
        mVersion.setOnClickListener(this);
        mGetTechnicalSupport.setOnClickListener(this);
        mNickName.setText(TCUserMgr.getInstance().getNickname());
        mUserId.setText("ID:" + TCUserMgr.getInstance().getUserId());

        mTvDownloadSDK = (TextView) view.findViewById(R.id.tv_download_sdk);
        mTvProductIntroduction = (TextView) view.findViewById(R.id.tv_product_introduction);

        mTvDownloadSDK.setMovementMethod(LinkMovementMethod.getInstance());
        mTvProductIntroduction.setMovementMethod(LinkMovementMethod.getInstance());
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();

        //页面展示之前，更新一下用户信息
        TCUserMgr.getInstance().fetchUserInfo(new TCUserMgr.Callback() {
            @Override
            public void onSuccess(JSONObject data) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mNickName.setText(TCUserMgr.getInstance().getNickname());
                        mUserId.setText("ID:" + TCUserMgr.getInstance().getUserId());
                        TCUtils.showPicWithUrl(getActivity(), mHeadPic, TCUserMgr.getInstance().getHeadPic(), R.drawable.face);
                    }
                });
            }

            @Override
            public void onFailure(int code, final String msg) {

            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    private void enterEditUserInfo() {
        try {
            Intent intent = new Intent(getContext(), TCEditUseInfoActivity.class);
            startActivity(intent);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.lcv_ui_set: //设置用户信息
                enterEditUserInfo();
                break;
            case R.id.lcv_ui_logout: //注销APP
//                TCUserMgr.getInstance().logout();
//                Intent intent = new Intent(getContext(), TCLoginActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(intent);
//                getActivity().finish();
                break;
            case R.id.lcv_ui_version: //显示 APP SDK 的版本信息
                showSDKVersion();
                break;
            case R.id.lcv_get_technical_support:
                showTechnicalSupport();
                break;
        }
    }

    private void showTechnicalSupport(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        AlertDialog alertDialog = builder.setCancelable(false).setMessage(R.string.follow_and_send_msg_to_public_number)
                .setPositiveButton(R.string.btn_sure, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create();
        alertDialog.show();
    }

    /**
     * 显示 APP SDK 的版本信息
     */
    private void showSDKVersion() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(),R.style.ConfirmDialogStyle);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setMessage("APP : " + getAppVersion() + "\r\n"
                + "RTMP SDK: " + TXLiveBase.getSDKVersionStr() + "\r\n"
                + "IM SDK: " + TIMManager.getInstance().getVersion()
                );
        builder.show();
    }

    /**
     * 获取APP版本
     *
     * @return APP版本
     */
    private String getAppVersion() {
        PackageManager packageManager = getActivity().getPackageManager();
        PackageInfo packInfo;
        String version = "";
        try {
            packInfo = packageManager.getPackageInfo(getActivity().getPackageName(), 0);
            version = packInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return version;
    }
}
