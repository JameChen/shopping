//package com.nahuo.quicksale;
//
//import com.nahuo.quicksale.exceptions.CatchedException;
//import com.nahuo.quicksale.oldermodel.Share2WPItem;
//import com.tencent.bugly.crashreport.CrashReport;
//
//import android.app.Activity;
//import android.content.Intent;
//import android.view.Gravity;
//import android.view.LayoutInflater;
//import android.view.MotionEvent;
//import android.view.View;
//import android.view.View.OnTouchListener;
//import android.view.ViewGroup.LayoutParams;
//import android.view.Window;
//import android.view.WindowManager;
//import android.view.WindowManager.BadTokenException;
//import android.widget.PopupWindow;
//
///**
// * @description 商品分享或者上传完成的弹窗
// * @created 2015-5-6 上午11:04:30
// * @author ZZB
// */
//public class UploadItemFinishedPopup extends PopupWindow implements View.OnClickListener {
//
//    private Activity mActivity;
//    private View     mRootView;
//    private Window   mWindow;
//    private Listener mListener;
//    private boolean mIsOtherClick = true;
//    private Share2WPItem mShare2wpItem;
//
//    public static interface Listener{
//        public void goOnUpload();
//        public void onOtherClickDismiss();//其他地方点击消失的
//    }
//
//    public UploadItemFinishedPopup(Activity activity) {
//        mActivity = activity;
//        mWindow = mActivity.getWindow();
//        initView();
//
//    }
//
//    private void initView() {
//        mRootView = LayoutInflater.from(mActivity).inflate(R.layout.pop_uploaded_item, null);
//        mRootView.setOnTouchListener(new OnTouchListener() {
//
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                closePopUp();
//                return false;
//            }
//        });
//        mRootView.findViewById(R.id.layout_root).setOnTouchListener(new OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                return true;
//            }
//        });
//        setContentView(mRootView);
//        mRootView.findViewById(R.id.btn_go_on_upload).setOnClickListener(this);
//        mRootView.findViewById(R.id.btn_share).setOnClickListener(this);
//        WindowManager.LayoutParams params = mWindow.getAttributes();
//        params.alpha = 0.5f;
//        setWidth(LayoutParams.MATCH_PARENT);
//        setHeight(LayoutParams.MATCH_PARENT);
//        setAnimationStyle(R.style.PopupBottomAnimation);
//        mWindow.setAttributes(params);
//        setOnDismissListener(new OnDismissListener() {
//
//            @Override
//            public void onDismiss() {
//                if(mListener != null && mIsOtherClick) {
//                    mListener.onOtherClickDismiss();
//                }
//            }
//        });
//    }
//
//    public void show() {
//        try {
//            View view = mWindow.getDecorView();
//            showAtLocation(view, Gravity.BOTTOM, 0, 0);
//        } catch (BadTokenException e) {
//            e.printStackTrace();
//            CrashReport.postCatchedException(new CatchedException(e));
//        }
//
//    }
//
//    private void closePopUp() {
//        dismiss();
//        WindowManager.LayoutParams params = mWindow.getAttributes();
//        params.alpha = 1f;
//        mWindow.setAttributes(params);
//    }
//
//
//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.btn_go_on_upload:// 继续上架
//                mIsOtherClick = false;
//                if(mListener != null) {
//                    mListener.goOnUpload();
//                }
//                closePopUp();
//                break;
//            case R.id.btn_share:// 分享赚钱
//                mIsOtherClick = false;
//                Intent intent = new Intent(mActivity, ShareItemActivity.class);
//                intent.putExtra(ShareItemActivity.EXTRA_SHARED_ITEM, mShare2wpItem);
//                mActivity.startActivity(intent);
//                closePopUp();
//                break;
//        }
//
//    }
//
//
//    public void setListener(Listener listener) {
//        mListener = listener;
//    }
//
//
//    public void setShare2wpItem(Share2WPItem share2wpItem) {
//        mShare2wpItem = share2wpItem;
//    }
//}
