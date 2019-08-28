package com.nahuo.quicksale;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.mobstat.StatService;
import com.nahuo.library.controls.BottomMenuList;
import com.nahuo.library.controls.LoadingDialog;
import com.nahuo.quicksale.api.AccountAPI;
import com.nahuo.quicksale.common.Const;
import com.nahuo.quicksale.common.SpManager;
import com.nahuo.quicksale.common.StringUtils;
import com.nahuo.quicksale.common.SystemCameraHelper;
import com.nahuo.quicksale.oldermodel.ImageViewModel;
import com.nahuo.quicksale.oldermodel.json.JAuthInfo;
import com.nahuo.quicksale.provider.UserInfoProvider;

/**
 * Description:身份验证 2014-7-25 上午10:20:21
 * 
 * @author ZZB
 */
public class IdentityAuthActivity extends BaseSlideBackActivity implements OnClickListener {

    private Context             mContext              = this;
    private static final int    POPTYPE_ADD_PHOTO     = 1;
    private static final int    POPTYPE_PHOTO_MENU    = 2;
    private static final int    REQUESTCODE_FROMALBUM = IdentityAuthActivity.class.hashCode() % 10000 - 1;
    // public static final String EXTRA_AUTH_INFO = "EXTRA_AUTH_INFO";
    /** 照片位置 */
    private static final int    PIC_POS_FRONT         = 0;
    private static final int    PIC_POS_BACK          = 1;
    private static final int    PIC_POS_HAND          = 2;
    // 图片地址
    private SparseArray<String> mPicPaths             = new SparseArray<String>();

    private TextView            mTVUserName;
    private EditText            mETUserName;
    private TextView            mTVCardNo;
    private EditText            mETCardNo;
    private ImageView           mFrontImg;
    private ImageView           mBackImg;
    private ImageView           mHandImg;
    private ImageView           mCurImageView;
    /** 当前点击照片的位置 */
    private int                 mCurPicPos            = -1;
    private View                mSaveBtn;
    private View                mLayoutCardState;

    private TextView            mTVCardState;
    private TextView            mTVBottomInfo;
    private SystemCameraHelper  mCameraHelper;

    // private PopupWindowEx pwAddPhoto, pwPhotoMenu;
    private LoadingDialog       mDlg;
    private JAuthInfo           mAuthInfo;

    public static enum Step {
        SUBMIT_DATA
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      //  requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_identity_auth);
      //  getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.layout_titlebar_default);
        try {
            initParams();
            initView();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    String authState;
    private void initParams() {
        mAuthInfo = UserInfoProvider.getAuthInfo(mContext, SpManager.getUserId(mContext));
//        if (mAuthInfo == null) {
//            mAuthInfo = JAuthInfo.getNotCommitInstance();
//        }
        int userId = SpManager.getUserId(this);
        authState= UserInfoProvider.getIdentityAuthState(this, userId);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SystemCameraHelper.ACTION_TAKE_PHOTO_B && resultCode == RESULT_OK) {
            // 显示拍完的照片
            String picPath = mCameraHelper.handleBigCameraPhoto();
            mPicPaths.put(mCurPicPos, picPath);
        } else if (requestCode == REQUESTCODE_FROMALBUM && data != null) {
            // 从相册中选择照片后显示
            @SuppressWarnings("unchecked")
            ArrayList<ImageViewModel> imgs = (ArrayList<ImageViewModel>)data
                    .getSerializableExtra(AlbumActivity.EXTRA_SELECTED_PIC_MODEL);
            if (imgs.size() == 0) {
                return;
            } else {
                String picPath = imgs.get(0).getOriginalUrl();
                mPicPaths.put(mCurPicPos, picPath);
                SystemCameraHelper.displayImageView(mCurImageView, imgs.get(0).getOriginalUrl());
            }
        }

    }

    private void initView() {
        initTitleBar();
        mDlg = new LoadingDialog(this);

        mETUserName = (EditText)findViewById(R.id.et_user_name);
        mTVUserName = (TextView)findViewById(R.id.tv_user_name);

        mETCardNo = (EditText)findViewById(R.id.et_card_number);
        // mETCardNo.setRawInputType(InputType.TYPE_CLASS_NUMBER);
        mTVCardNo = (TextView)findViewById(R.id.tv_card_number);

        mLayoutCardState = findViewById(R.id.layout_card_state);
        //mLayoutPics = findViewById(R.id.layout_pics);

        mFrontImg = (ImageView)findViewById(R.id.img_front);
        mBackImg = (ImageView)findViewById(R.id.img_back);
        mHandImg = (ImageView)findViewById(R.id.img_hand);

        mTVBottomInfo = (TextView)findViewById(R.id.bottom_info);
        mTVCardState = (TextView)findViewById(R.id.tv_card_state);

        mSaveBtn = findViewById(R.id.btn_save);
        // new Task(Step.LOAD_DATA).execute();
            new Task2().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
       // updateUI();
    }
    private class Task2 extends AsyncTask<Object, Void, Object> {


        @Override
        protected void onPreExecute() {

            //mDialog.start("加载数据中...");
        }

        @Override
        protected Object doInBackground(Object... params) {
            try {
                    mAuthInfo = AccountAPI.getIDAuthInfo(mContext);

                return mAuthInfo;
            } catch (Exception e) {
                e.printStackTrace();
                return "error:" + e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(Object result) {
//            if (mDialog.isShowing()) {
//                mDialog.stop();
//            }
            if (result instanceof String && ((String) result).startsWith("error:")) {
               // ViewHub.showLongToast(mContext, ((String) result).replace("error:", ""));
            } else {
                updateUI();
            }

        }

    }
    private void updateUI() {
        String authState = mAuthInfo.getStatu();
        mTVUserName.setText(mAuthInfo.getRealName());
        if (Const.IDAuthState.AUTH_PASSED.equals(authState)) {// 验证通过
            changeView(false);
            mTVCardNo.setText(mAuthInfo.getCardNum());
            mTVCardState.setText(Const.IDAuthState.AUTH_PASSED);
            updateBottomInfo("身份验证不能修改，如果确实需要修改，请联系酷有小二");
        } else if (Const.IDAuthState.NOT_COMMIT.equals(authState)) {// 未提交
            changeView(true);
            updateBottomInfo("亲，必须是本人的证件，证件姓名与提现银行卡姓名必须一致，否则无法提现\n\n审核时间为1-3个工作日");
        } else if (Const.IDAuthState.CHECKING.equals(authState)) {// 审核中
            changeView(false);
            mTVCardNo.setText(mAuthInfo.getCardNum());
            mTVCardState.setText(Const.IDAuthState.CHECKING);
            updateBottomInfo("身份验证审核将在1-3个工作日完成");
        } else if (Const.IDAuthState.REJECT.equals(authState)) {// 未通过
            changeView(true);
            mETCardNo.setText(mAuthInfo.getCardNum());
            mETUserName.setText(mAuthInfo.getRealName());
            mTVCardState.setText(Const.IDAuthState.REJECT);
            updateBottomInfo(Html.fromHtml("驳回原因：<font color='red'>" + mAuthInfo.getMessage() + "</font>"));
        } else if (Const.IDAuthState.RECHECK.equals(authState)) {// 重审
            changeView(true);
            mETCardNo.setText(mAuthInfo.getCardNum());
            mETUserName.setText(mAuthInfo.getRealName());
            mTVCardState.setText(Const.IDAuthState.RECHECK);
            updateBottomInfo(Html.fromHtml("重审原因：<font color='red'>" + mAuthInfo.getMessage() + "</font>"));
        }
        if (mETUserName!=null)
        mETUserName.setSelection(mETUserName.getText().toString().length());
    }

    private void updateBottomInfo(Spanned spanned) {
        mTVBottomInfo.setText(spanned);
    }

    private void updateBottomInfo(String info) {

        mTVBottomInfo.setText(info);
        mTVBottomInfo.setTextColor(getResources().getColor(R.color.hint));
    }

    private void initTitleBar() {
        TextView tvTitle = (TextView)findViewById(R.id.titlebar_tvTitle);
        tvTitle.setText("身份验证");

        Button btnLeft = (Button)findViewById(R.id.titlebar_btnLeft);
        btnLeft.setText(R.string.titlebar_btnBack);
        btnLeft.setVisibility(View.VISIBLE);
        btnLeft.setOnClickListener(this);

    }

    /**
     * Description:在view的编辑与正确显示之前切换 2014-7-25 下午2:12:09
     * 
     * @author ZZB
     */
    private void changeView(boolean isEdit) {
        if (isEdit) {
            mCameraHelper = new SystemCameraHelper(this, "weipu");

            // 添加照片选择方式的弹出框
            // View addPhotoPwView = LayoutInflater.from(this).inflate(R.layout.layout_pw_addphoto,
            // null);
            // pwAddPhoto = new PopupWindowEx(addPhotoPwView, R.id.uploaditem_pw_addphoto,
            // RelativeLayout.LayoutParams.MATCH_PARENT,
            // RelativeLayout.LayoutParams.WRAP_CONTENT,
            // true);
            // pwAddPhoto.setAnimationStyle(R.style.PopupBottomAnimation);
            // 点击照片，弹出操作菜单
            // View photoMenuPwView = LayoutInflater.from(this).inflate(R.layout.pop_handle_img, null);
            // pwPhotoMenu = new PopupWindowEx(photoMenuPwView, R.id.pw_handle_photo_menu, LayoutParams.MATCH_PARENT,
            // LayoutParams.WRAP_CONTENT, true);
            // pwPhotoMenu.setAnimationStyle(R.style.PopupBottomAnimation);
        }
        mETUserName.setVisibility(isEdit ? View.VISIBLE : View.GONE);
        mETCardNo.setVisibility(isEdit ? View.VISIBLE : View.GONE);
        //mLayoutPics.setVisibility(isEdit ? View.VISIBLE : View.GONE);//(暂时先去掉)
        String authState = mAuthInfo.getStatu();
        if (authState.equals(Const.IDAuthState.AUTH_PASSED) || authState.equals(Const.IDAuthState.CHECKING)) {
            mLayoutCardState.setVisibility(View.VISIBLE);
        } else {
            mLayoutCardState.setVisibility(View.GONE);
        }

        mSaveBtn.setVisibility(isEdit ? View.VISIBLE : View.GONE);

        mFrontImg.setVisibility(isEdit ? View.VISIBLE : View.GONE);
        mBackImg.setVisibility(isEdit ? View.VISIBLE : View.GONE);
        mHandImg.setVisibility(isEdit ? View.VISIBLE : View.GONE);

        mTVUserName.setVisibility(isEdit ? View.GONE : View.VISIBLE);
        mTVCardNo.setVisibility(isEdit ? View.GONE : View.VISIBLE);

    }

    /**
     * 弹出或关闭PopupWindow
     * 
     * @param view 当前被点击的控件
     * @param type 弹出框类型：1.选择照片菜单 2.操作照片菜单
     * */
    private void togglePopupWindow(int type) {
        // 隐藏软键盘
        // FunctionHelper.hideSoftInput(getWindowToken(), this);
        // PopupWindowEx pw = null;
        BottomMenuList menu;
        switch (type) {
            case POPTYPE_ADD_PHOTO:
                // pw = pwAddPhoto;
                menu = new BottomMenuList(this);
                menu.setItems(getResources().getStringArray(R.array.menu_upload_image_texts))
                        .setOnMenuItemClickListener(new OnItemClickListener() {

                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                switch (position) {
                                    case 0:
                                        mCameraHelper.takeBigPic(mCurImageView);
                                        break;
                                    case 1:
                                        fromAblum();
                                        break;
                                    default:
                                        break;
                                }
                            }
                        }).show();

                break;
            case POPTYPE_PHOTO_MENU:
                // pw = pwPhotoMenu;
                menu = new BottomMenuList(this);
                menu.setItems(getResources().getStringArray(R.array.handle_image_texts))
                        .setOnMenuItemClickListener(new OnItemClickListener() {

                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                switch (position) {
                                    case 0:
                                        viewImage();
                                        break;
                                    case 1:
                                        deleteImage();
                                        break;
                                    default:
                                        break;
                                }
                            }
                        }).show();

                break;
        }
        // if (pw.isShowing()) {
        // pw.dismiss();
        // } else {
        // pw.showAtLocation(findViewById(R.id.base_layout), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        // }
    }

    /**
     * Description:从专辑先照片 2014-7-28 下午3:53:41
     * 
     * @author ZZB
     */
    private void fromAblum() {
        Intent intent = new Intent(this, AlbumActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt(AlbumActivity.EXTRA_MAX_PIC_COUNT, 1);
        intent.putExtras(bundle);
        startActivityForResult(intent, REQUESTCODE_FROMALBUM);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.titlebar_btnLeft:
                ViewHub.hideKeyboard(this);
                finish();
                break;
            case R.id.img_front:// 正面照
                onImageClick(v);
                break;
            case R.id.img_back:// 背面照
                onImageClick(v);
                break;
            case R.id.img_hand:// 手持照
                onImageClick(v);
                break;
//            case R.id.uploaditem_pw_addphoto_btnTakePhoto:// 拍照
//                togglePopupWindow(v, POPTYPE_ADD_PHOTO);
//                mCameraHelper.takeBigPic(mCurImageView);
//                break;
//            case R.id.uploaditem_pw_addphoto_btnFromAlbum:// 从相册选照片
//                togglePopupWindow(v, POPTYPE_ADD_PHOTO);
//                fromAblum();
//                break;
//            case R.id.uploaditem_pw_addphoto_btnCancel:// 拍照弹窗： 取消
//                togglePopupWindow(v, POPTYPE_ADD_PHOTO);
//                break;
//            case R.id.btn_cancle:// 图片处理弹窗：取消
//                togglePopupWindow(v, POPTYPE_PHOTO_MENU);
//                break;
//            case R.id.btn_delete_img:// 删除照片
//                togglePopupWindow(v, POPTYPE_PHOTO_MENU);
//                deleteImage();
//                break;
//            case R.id.btn_view_img:// 查看大图
//                togglePopupWindow(v, POPTYPE_PHOTO_MENU);
//                viewImage();
//                break;
            case R.id.btn_save:// 提交验证信息
                onSaveClick();
                break;
        }
    }

    /**
     * @description 删除图片
     * @time 2014-8-1 下午2:49:38
     * @author ZZB
     */
    private void deleteImage() {
        mCurImageView.setImageBitmap(null);
        mPicPaths.put(mCurPicPos, null);
    }

    /**
     * @description 查看大图
     * @time 2014-8-1 下午2:30:08
     * @author ZZB
     */
    private void viewImage() {
        String picPath = "file://" + mPicPaths.get(mCurPicPos);
        Intent intent = new Intent(this, ItemImageViewActivity.class);
        intent.putExtra(ItemImageViewActivity.IMAGE_URL, picPath);
        startActivity(intent);
    }

    /**
     * @description 点击图片
     * @time 2014-8-1 下午2:31:21
     * @author ZZB
     */
    private void onImageClick(View v) {
        switch (v.getId()) {
            case R.id.img_front:
                mCurPicPos = PIC_POS_FRONT;
                break;
            case R.id.img_back:
                mCurPicPos = PIC_POS_BACK;
                break;
            case R.id.img_hand:
                mCurPicPos = PIC_POS_HAND;
                break;
        }
        if (TextUtils.isEmpty(mPicPaths.get(mCurPicPos))) {
            // 没选择图片：拍照或者从相册选
            togglePopupWindow(POPTYPE_ADD_PHOTO);
        } else {
            // 选择了图片:查看图片
            togglePopupWindow(POPTYPE_PHOTO_MENU);
        }
        mCurImageView = (ImageView)v;
    }

    /**
     * @description 提交身份验证信息
     * @time 2014-8-1 上午11:39:58
     * @author ZZB
     */
    private void onSaveClick() {
        if (StringUtils.isEmptyWithTrim(mETUserName.getText().toString())) {
            ViewHub.setEditError(mETUserName, "持证姓名不能为空");
            mETUserName.requestFocus();
            return;
        }
        else if (TextUtils.isEmpty(mETCardNo.getText().toString())) {
            ViewHub.setEditError(mETCardNo, "请输入身份证号码");
            mETCardNo.requestFocus();
            return;
        }
       /* else if (!FunctionHelper.isIDCard(mETCardNo.getText().toString())) {
            ViewHub.setEditError(mETCardNo, "请输入正确的身份证号码");
            mETCardNo.requestFocus();
            return;
        } */
        /*else if (mPicPaths.size() < 3) {
            ViewHub.showLongToast(this, "请选择证件照");
            return;
        }

        for (int pos = 0; pos < mPicPaths.size(); pos++) {
            String path = mPicPaths.get(pos);
            if (TextUtils.isEmpty(path)) {
                switch (pos) {
                    case 0:
                        ViewHub.showLongToast(this, "证件正面照片不能为空");
                        return;
                    case 1:
                        ViewHub.showLongToast(this, "证件反面照片不能为空");
                        return;
                    case 2:
                        ViewHub.showLongToast(this, "手持证件露脸照不能为空");
                        return;
                }
            }
        }*/

        new Task(Step.SUBMIT_DATA).execute();
    }

    private class Task extends AsyncTask<Void, Void, Object> {

        private Step mStep;

        public Task(Step step) {
            this.mStep = step;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            switch (mStep) {
                case SUBMIT_DATA:
                    mDlg.start("上传验证信息中...");
                    break;
            }

        }

        @Override
        protected Object doInBackground(Void... params) {
            try {
                switch (mStep) {
                    case SUBMIT_DATA:
                        String userName = mETUserName.getText().toString();
                        String cardNo = mETCardNo.getText().toString();
                        mAuthInfo.setRealName(userName.replaceAll("(?<=[\\w\\W]{1})[\\w\\W](?=[\\w\\W]{1})", "*"));
                        mAuthInfo.setCardNum(cardNo.replaceAll("(?<=\\d{1})\\d(?=\\d{1})", "*"));
                        AccountAPI.saveAuthInfo2(mContext, userName, cardNo);
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
            if (mDlg.isShowing()) {
                mDlg.stop();
            }

            if (result instanceof String && ((String)result).startsWith("error:")) {
                ViewHub.showLongToast(mContext, ((String)result).replace("error:", ""));
            } else {
                switch (mStep) {
                    case SUBMIT_DATA:
                        //switchToCheckingState();
                        ViewHub.showLongToast(IdentityAuthActivity.this, "审核成功");
                        switchToFinishingState();
                        finish();
                        break;
                }

            }

        }

    }

    /**
     * Description:切换到"审核中"的界面 2014-7-28 下午5:57:55
     * 
     * @author ZZB
     */
    private void switchToCheckingState() {
        mAuthInfo.setStatu(Const.IDAuthState.CHECKING);
        changeView(false);
        mTVUserName.setText(mETUserName.getText());
        mTVCardNo.setText(mETCardNo.getText());
        mTVCardState.setText(Const.IDAuthState.CHECKING);
        mTVBottomInfo.setText(Html.fromHtml("<font color='#E3D7DD'>身份验证审核将在1-3个工作日完成</font>"));
        UserInfoProvider.setIdentityAuthState(mContext, SpManager.getUserId(mContext), Const.IDAuthState.CHECKING);
        mAuthInfo.setStatu(Const.IDAuthState.CHECKING);
        UserInfoProvider.setAuthInfo(mContext, mAuthInfo);
       // UserInfoProvider.setAuthInfo(mContext, mAuthInfo);
    }
    /**
     * Description:切换到"审核中"的界面 2014-7-28 下午5:57:55
     *
     * @author ZZB
     */
    private void switchToFinishingState() {
        mAuthInfo.setStatu(Const.IDAuthState.AUTH_PASSED);
        changeView(false);
        mTVUserName.setText(mETUserName.getText());
        mTVCardNo.setText(mETCardNo.getText());
        mTVCardState.setText(Const.IDAuthState.AUTH_PASSED);
        mTVBottomInfo.setText(Html.fromHtml("<font color='#E3D7DD'>身份验证审核完成</font>"));
        UserInfoProvider.setIdentityAuthState(mContext, SpManager.getUserId(mContext), Const.IDAuthState.AUTH_PASSED);
        mAuthInfo.setStatu(Const.IDAuthState.AUTH_PASSED);
        UserInfoProvider.setAuthInfo(mContext, mAuthInfo);
        // UserInfoProvider.setAuthInfo(mContext, mAuthInfo);
    }
}
