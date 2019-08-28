package com.nahuo.quicksale;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.baidu.mobstat.StatService;
import com.nahuo.library.controls.LoadingDialog;
import com.nahuo.library.helper.FunctionHelper;
import com.nahuo.quicksale.api.ShopSetAPI;
import com.nahuo.quicksale.oldermodel.Address;

import java.util.Locale;

/**
 * @description 添加或者编辑收件地址
 * @created 2014-8-28 上午10:24:56
 * @author ZZB
 */
public class HandleAddressActivity extends BaseSlideBackActivity implements OnClickListener,
        SelectAreaDialogFragment.DailogResultListener {

    private LoadingDialog mDialog;
    public static final String EXTRA_HANDLE_TYPE = "EXTRA_TYPE";
    public static final String EXTRA_ADDRESS = "EXTRA_ADDRESS";
    /**标志是否没有地址，如果没有地址，则添加的地址为默认地址*/
    public static final String EXTRA_EMPTY_ADDRESS = "EXTRA_EMPTY_ADDRESS";
    public static final int TYPE_ADD_ADDRESS = 1;
    public static final int TYPE_EDIT_ADDRESS = 2;
    private static final int TYPE_DELETE_ADDRESS = 3;
    private static final int TYPE_DEFAULT_ADDRESS = 4;

    // result code: 保存，删除，默认地址
    public static final int RESULT_SAVE = 1;
    public static final int RESULT_DEFAULT = 2;
    public static final int RESULT_DELETE = 3;

    private int mType;
    private Address mAddress;

    private EditText mEtConsignee, mEtDetailAddress, mEtPhone;
//    , mEtPostcode;
    private TextView mTvArea;
    private View mBtnDelete;
    public Context mContext = this;
    public boolean mEmptyAddress;
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_handle_address);
       // getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.layout_titlebar_default);
        initParams();
        initView();
    }

    private void initParams() {
        Intent intent = getIntent();
        mType = intent.getIntExtra(EXTRA_HANDLE_TYPE, -1);
        mAddress = (Address) intent.getSerializableExtra(EXTRA_ADDRESS);
        mEmptyAddress = intent.getBooleanExtra(EXTRA_EMPTY_ADDRESS, false);
    }

    private void initView() {
        mDialog = new LoadingDialog(this);
        initTitleBar();
        mEtConsignee = (EditText) findViewById(R.id.et_consignee);
        mTvArea = (TextView) findViewById(R.id.tv_area);
        mEtDetailAddress = (EditText) findViewById(R.id.et_detail_address);
        mEtPhone = (EditText) findViewById(R.id.et_phone);
//        mEtPostcode = (EditText) findViewById(R.id.et_post_code);
        mBtnDelete = findViewById(R.id.btn_delete);

        if (mAddress != null) {// 编辑
            String userName = mAddress.getUserName();
            mEtConsignee.setText(userName);
            mEtConsignee.setSelection(userName.length());
            updateArea();
            mEtDetailAddress.setText(mAddress.getDetailAddress());
            mEtPhone.setText(mAddress.getPhone());
//            mEtPostcode.setText(mAddress.getPostCode());
        } else {// 添加
            mBtnDelete.setVisibility(View.GONE);
        }

    }

    private void updateArea() {
        mTvArea.setText(String.format(Locale.CHINA, "%s %s %s", mAddress.getProvince()
                .getName(), mAddress.getCity().getName(), mAddress.getArea().getName()));
    }

    private void initTitleBar() {
        TextView tvTitle = (TextView) findViewById(R.id.titlebar_tvTitle);
        String title = "";
        switch (mType) {
        case TYPE_ADD_ADDRESS:
            title = "添加收件地址";
            break;
        case TYPE_EDIT_ADDRESS:
            title = "编辑收件地址";
            break;
        default:
            throw new RuntimeException("没有设置标题");
        }
        tvTitle.setText(title);
        Button btnLeft = (Button) findViewById(R.id.titlebar_btnLeft);
        btnLeft.setText(R.string.titlebar_btnBack);
        btnLeft.setVisibility(View.VISIBLE);
        btnLeft.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.titlebar_btnLeft:
            finish();
            break;
        case R.id.tv_area:
            SelectAreaDialogFragment f = SelectAreaDialogFragment.newInstance(mAddress);
            f.setListener(this);
            f.show(getSupportFragmentManager(), SelectAreaDialogFragment.TAG);
            break;
        case R.id.btn_save:
            onSaveClick();
            break;
        case R.id.btn_delete:
            onDeleteClick();
            break;
        case R.id.btn_default:
            onDefaultClick();
            break;
        }
    }

    private void submit(int type, int resultCode, String text) {
        if (validateAddress()) {
            new Task(type, resultCode, text).execute();
        }
    }

    /**
     * @description 设置默认地址
     * @created 2014-8-29 下午2:42:14
     * @author ZZB
     */
    private void onDefaultClick() {
        submit(TYPE_DEFAULT_ADDRESS, RESULT_DEFAULT, "设置默认地址成功");
    }

    /**
     * @description 删除地址
     * @created 2014-8-29 下午2:30:37
     * @author ZZB
     */
    private void onDeleteClick() {
        submit(TYPE_DELETE_ADDRESS, RESULT_DELETE, "删除成功");

    }

    /**
     * @description 保存地址
     * @created 2014-8-29 下午2:28:38
     * @author ZZB
     */
    private void onSaveClick() {
        submit(mType, RESULT_SAVE, "保存成功");
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
    public void onResult(Address address) {
        if (mAddress == null) {
            mAddress = new Address();
        }
        mAddress.setProvince(address.getProvince());
        mAddress.setArea(address.getArea());
        mAddress.setCity(address.getCity());
        updateArea();
    }

    private boolean validateAddress() {
        String userName = mEtConsignee.getText().toString();
        String area = mTvArea.getText().toString();
        String detailAdd = mEtDetailAddress.getText().toString();
        String phone = mEtPhone.getText().toString();
//        String postcode = mEtPostcode.getText().toString();

        if (TextUtils.isEmpty(userName)) {
            ViewHub.setEditError(mEtConsignee, "收件人不能为空");
            return false;
        } else if (TextUtils.isEmpty(area)) {
            ViewHub.showLongToast(this, "地区信息不能为空");
            return false;
        } else if (TextUtils.isEmpty(detailAdd)) {
            ViewHub.setEditError(mEtDetailAddress, "详细地址不能为空");
            return false;
        } else if (!FunctionHelper.isPhoneNo(phone)) {
            ViewHub.setEditError(mEtPhone, "请输入正确的手机号码");
            return false;
        }
//        else if (!FunctionHelper.isPostcode(postcode)) {
//            ViewHub.setEditError(mEtPostcode, "请输入正确的邮政编码");
//            return false;
//        }

        if (mAddress == null) {
            mAddress = new Address();
        }
        mAddress.setUserName(userName);
        mAddress.setDetailAddress(detailAdd);
        mAddress.setPhone(phone);
//        mAddress.setPostCode(postcode);
        return true;
    }

    private class Task extends AsyncTask<Void, Void, Object> {

        private int type;
        private int resultCode;
        private String dlgText;

        public Task(int type, int resultCode, String dlgText) {
            this.type = type;
            this.resultCode = resultCode;
            this.dlgText = dlgText;
        }

        @Override
        protected void onPreExecute() {
            switch (type) {
            case TYPE_ADD_ADDRESS:
                mDialog.start("添加地址中...");
                break;
            case TYPE_EDIT_ADDRESS:
                mDialog.start("更新地址中...");
                break;
            case TYPE_DELETE_ADDRESS:
                mDialog.start("删除地址中...");
                break;
            case TYPE_DEFAULT_ADDRESS:
                mDialog.start("设置默认地址中");
                break;
            }
        }

        @Override
        protected Object doInBackground(Void... params) {
            try {
                switch (type) {
                case TYPE_ADD_ADDRESS:
                    if(mEmptyAddress){
                        mAddress.setDefault(true);
                    }
                    int addressId = ShopSetAPI.addAddress(mContext, mAddress);
                    return addressId;
                case TYPE_EDIT_ADDRESS:
                    ShopSetAPI.updateAddress(mContext, mAddress);
                    break;
                case TYPE_DELETE_ADDRESS:
                    ShopSetAPI.deleteAddress(mContext, mAddress);
                    break;
                case TYPE_DEFAULT_ADDRESS:
                    boolean update = mType == TYPE_EDIT_ADDRESS ? true : false;
                    mAddress.setDefault(true);
                    int defaultAddressId = mAddress.getId();
                    if(update){
                        ShopSetAPI.updateAddress(mContext, mAddress);
                    }else{
                        defaultAddressId = ShopSetAPI.addAddress(mContext, mAddress);
                    }
                    return defaultAddressId;
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
            } else {
                switch (type) {
                case TYPE_ADD_ADDRESS:
                    mAddress.setId((Integer) result);
                    break;
                case TYPE_EDIT_ADDRESS:
                    break;
                case TYPE_DELETE_ADDRESS:
                    break;
                case TYPE_DEFAULT_ADDRESS:
                    mAddress.setId((Integer) result);
                    break;
                }
                Intent intent = new Intent();
                intent.putExtra(EXTRA_ADDRESS, mAddress);
                setResult(resultCode, intent);
                ViewHub.showLongToast(mContext, dlgText);
                finish();
            }
        }
    }
}
