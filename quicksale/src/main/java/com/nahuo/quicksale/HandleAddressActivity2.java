package com.nahuo.quicksale;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

import com.nahuo.library.controls.LoadingDialog;
import com.nahuo.quicksale.api.AreaAPI;
import com.nahuo.quicksale.api.HttpRequestHelper;
import com.nahuo.quicksale.api.HttpRequestHelper.HttpRequest;
import com.nahuo.quicksale.api.RequestMethod.ShopMethod;
import com.nahuo.quicksale.common.ListUtils;
import com.nahuo.quicksale.common.SpManager;
import com.nahuo.quicksale.oldermodel.Address;
import com.nahuo.quicksale.oldermodel.Area;
import com.nahuo.quicksale.oldermodel.ResultData;
import com.nahuo.quicksale.oldermodel.json.JAddress;

import java.util.List;

/**
 * @author JorsonWong
 * @description 收货地址添加编辑
 * @created 2015年4月7日 上午9:34:56
 */
public class HandleAddressActivity2 extends BaseActivity2 implements OnClickListener {

    public static final String EXTRA_ADDRESS = "EXTRA_ADDRESS";
    public static final String RESULT_ADDRESS = "EXTRA_RESULT_ADDRESS";

    private Address mAddress;
    private EditText mEtConsignee, mEtPhone, mEtProvince, mEtCity, mEtArea, mEtAddrDetail;

    private boolean mIsEdit;
   // private AreaDao mAreaDao;
    private List<Area> mProvincies, mAreas, mCities;
    private Area mProvinve, mCity, mArea;
    private int mProvinveSelPs, mCitySelPs, mAreaSelPs;
    private LoadingDialog loadingDialog;
    HandleAddressActivity2 Vthis;
    private int pid;

    private enum Step {
        PROVINCE, CITY, REGION
    }

    private static final String ERROR_PREFIX = "error:";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_handle_address2);
        loadingDialog = new LoadingDialog(this);
        Vthis = this;
        initParams();
        initView();

        if (mAddress != null) {
            setTitle(R.string.update_address);
            mIsEdit = true;
            setData(mAddress);
        } else {
            setTitle(R.string.add_address);
        }
    }

    private void initView() {
        mEtConsignee = (EditText) findViewById(R.id.et_name);
        mEtPhone = (EditText) findViewById(R.id.et_phone);
        mEtProvince = (EditText) findViewById(R.id.et_province);
        mEtCity = (EditText) findViewById(R.id.et_city);
        mEtArea = (EditText) findViewById(R.id.et_area);
        mEtAddrDetail = (EditText) findViewById(R.id.et_address_detail);

        mLoadingDialog = new LoadingDialog(this);

    }

    private void initParams() {
        Intent intent = getIntent();
        mAddress = (Address) intent.getSerializableExtra(EXTRA_ADDRESS);
        //mAreaDao = new AreaDao(getApplicationContext());
    }

    private void setData(Address address) {
        try {
            mProvinve = address.getProvince();
            mCity = address.getCity();
            mArea = address.getArea();

            mEtConsignee.setText(address.getUserName());
            mEtPhone.setText(address.getPhone());
            mEtProvince.setText(address.getProvince().getName());
            mEtCity.setText(address.getCity().getName());
            mEtArea.setText(address.getArea().getName());
            mEtAddrDetail.setText(address.getDetailAddress());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private class AreaTask extends AsyncTask<Void, Void, Object> {
        private Step mCurrentStep;

        public AreaTask(Step step) {
            mCurrentStep = step;
        }

        @Override
        protected Object doInBackground(Void... params) {
            try {
                List<Area> result = null;
                switch (mCurrentStep) {
                    case PROVINCE:
                    case CITY:
                    case REGION:
                        result = AreaAPI.getInstance().getAreaInfo(
                                Vthis, pid);
                        break;
                }
                return result;
            } catch (Exception ex) {
                ex.printStackTrace();
                return ERROR_PREFIX + ex.getMessage();
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            String msg = "";
            switch (mCurrentStep) {
                case PROVINCE:
                    msg = "正在加载省份数据";
                    loadingDialog.start(msg);
                    break;
                case CITY:
                    msg = "正在加载城市数据";
                    loadingDialog.start(msg);
                    break;
                case REGION:
                    msg = "正在加载地区数据";
                    loadingDialog.start(msg);
                    break;
                default:
                    msg = "正在加载数据";
                    loadingDialog.start(msg);
                    break;
            }
        }

        @Override
        protected void onPostExecute(Object result) {
            super.onPostExecute(result);
            if (loadingDialog.isShowing()) {
                loadingDialog.stop();
            }
            if (result instanceof String && ((String) result).startsWith(ERROR_PREFIX)) {
                String msg = ((String) result).replace(ERROR_PREFIX, "");
                ViewHub.showLongToast(Vthis, msg);
                return;
            }
            try {
                switch (mCurrentStep) {
                    case PROVINCE:
                        mProvincies = (List<Area>) result;
                        if (mProvincies != null && mProvincies.size() > 0) {
                            String[] provincs = new String[mProvincies.size()];
                            int selectIndex = 0;
                            for (int i = 0; i < provincs.length; i++) {
                                provincs[i] = mProvincies.get(i).getName();
                                if (mProvinve != null && mProvincies.get(i).getId()==mProvinve.getId()) {
                                    selectIndex = i;
                                }
                            }
                            mProvinveSelPs = selectIndex;
                            Dialog dialog = new AlertDialog.Builder(Vthis, AlertDialog.THEME_HOLO_LIGHT)
                                    .setTitle(R.string.pls_select_province)
                                    .setSingleChoiceItems(provincs, selectIndex, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            mProvinveSelPs = which;
                                        }
                                    }).setNegativeButton(android.R.string.cancel, null)
                                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            mProvinve = mProvincies.get(mProvinveSelPs);
                                            provinveChanged(mProvinve);
                                        }
                                    }).create();
                            dialog.show();
                        }
                        break;
                    case CITY:
                        mCities = (List<Area>) result;
                        if (mCities != null && mCities.size() > 0) {
                            String[] citiStrings = new String[mCities.size()];
                            int selectIndex = 0;
                            for (int i = 0; i < citiStrings.length; i++) {
                                citiStrings[i] = mCities.get(i).getName();
                                if (mCity != null && mCities.get(i).getId()==mCity.getId()) {
                                    selectIndex = i;
                                }
                            }
                            mCitySelPs = selectIndex;
                            Dialog dialog = new AlertDialog.Builder(Vthis, AlertDialog.THEME_HOLO_LIGHT)
                                    .setTitle(R.string.pls_select_city)
                                    .setSingleChoiceItems(citiStrings, selectIndex, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            mCitySelPs = which;
                                        }
                                    }).setNegativeButton(android.R.string.cancel, null)
                                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            mCity = mCities.get(mCitySelPs);
                                            // mEtCity.setText(mCity.getName());
                                            cityChanged(mCity);
                                        }
                                    }).create();
                            dialog.show();
                        }
                        break;
                    case REGION:
                        mAreas= (List<Area>)result;
                        if (mAreas != null && mAreas.size() > 0) {
                            String[] areas = new String[mAreas.size()];
                            int selectIndex = 0;
                            for (int i = 0; i < areas.length; i++) {
                                areas[i] = mAreas.get(i).getName();
                                if (mArea!=null && mAreas.get(i).getId()==mArea.getId())
                                {
                                    selectIndex = i;
                                }
                            }
                            mAreaSelPs = selectIndex;
                           Dialog dialog = new AlertDialog.Builder(Vthis, AlertDialog.THEME_HOLO_LIGHT)
                                    .setTitle(R.string.pls_select_area)
                                    .setSingleChoiceItems(areas, selectIndex, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            mAreaSelPs = which;
                                        }
                                    }).setNegativeButton(android.R.string.cancel, null)
                                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            mArea = mAreas.get(mAreaSelPs);
                                            mEtArea.setText(mArea.getName());
                                        }
                                    }).create();
                            dialog.show();
                        }
                        break;
                    default:
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onClick(View v) {
        AlertDialog dialog;
        switch (v.getId()) {
            case android.R.id.button1:
                if (validateAddress()) {
                    if (mIsEdit) {
                        editAddress(mArea != null ? mArea.getId() : (mCity != null ? mCity.getId()
                                : (mProvinve != null ? mProvinve.getId() : -1)));
                    } else {
                        addAddress(mArea != null ? mArea.getId() : (mCity != null ? mCity.getId()
                                : (mProvinve != null ? mProvinve.getId() : -1)));
                    }
                }
                break;
            case R.id.et_province: {
                pid = 0;
                new AreaTask(Step.PROVINCE).execute();
                // mProvincies = mAreaDao.getAreas(0);

//                mProvincies = mAreaDao.getAreas(0);
//                        if (mProvincies != null && mProvincies.size() > 0) {
//                            String[] provincs = new String[mProvincies.size()];
//                            int selectIndex = 0;
//                            for (int i = 0; i < provincs.length; i++) {
//                                provincs[i] = mProvincies.get(i).getName();
//                                if (mProvinve != null && provincs[i].equals(mProvinve.getName())) {
//                                    selectIndex = i;
//                                }
//                    }
//                    mProvinveSelPs = selectIndex;
//                    dialog = new AlertDialog.Builder(this, AlertDialog.THEME_HOLO_LIGHT)
//                            .setTitle(R.string.pls_select_province)
//                            .setSingleChoiceItems(provincs, selectIndex, new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//                                    mProvinveSelPs = which;
//                                }
//                            }).setNegativeButton(android.R.string.cancel, null)
//                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//                                    mProvinve = mProvincies.get(mProvinveSelPs);
//                                    provinveChanged(mProvinve);
//                                }
//                            }).create();
//                    dialog.show();
//                }
                break;
            }
            case R.id.et_city:
                if (mProvinve == null) {
                    ViewHub.showShortToast(getApplicationContext(), getString(R.string.pls_select_province));
                    return;
                }
                pid = mProvinve.getId();
                new AreaTask(Step.CITY).execute();
//                mCities = mAreaDao.getAreas(mProvinve.getId());
//                if (mCities != null && mCities.size() > 0) {
//                    String[] citiStrings = new String[mCities.size()];
//                    int selectIndex = 0;
//                    for (int i = 0; i < citiStrings.length; i++) {
//                        citiStrings[i] = mCities.get(i).getName();
//                        if (mCity!=null && citiStrings[i].equals(mCity.getName()))
//                        {
//                            selectIndex = i;
//                        }
//                    }
//                    mCitySelPs = selectIndex;
//                    dialog = new AlertDialog.Builder(this, AlertDialog.THEME_HOLO_LIGHT)
//                            .setTitle(R.string.pls_select_city)
//                            .setSingleChoiceItems(citiStrings, selectIndex, new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//                                    mCitySelPs = which;
//                                }
//                            }).setNegativeButton(android.R.string.cancel, null)
//                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//                                    mCity = mCities.get(mCitySelPs);
//                                    // mEtCity.setText(mCity.getName());
//                                    cityChanged(mCity);
//                                }
//                            }).create();
//                    dialog.show();
//                }
                break;
            case R.id.et_area:
                if (mCity == null) {
                    ViewHub.showShortToast(getApplicationContext(), getString(R.string.pls_select_city));
                    return;
                }
                pid = mCity.getId();
                new AreaTask(Step.REGION).execute();
//                mAreas = mAreaDao.getAreas(mCity.getId());
//                if (mAreas != null && mAreas.size() > 0) {
//                    String[] areas = new String[mAreas.size()];
//                    int selectIndex = 0;
//                    for (int i = 0; i < areas.length; i++) {
//                        areas[i] = mAreas.get(i).getName();
//                        if (mArea!=null && areas[i].equals(mArea.getName()))
//                        {
//                            selectIndex = i;
//                        }
//                    }
//                    mAreaSelPs = selectIndex;
//                    dialog = new AlertDialog.Builder(this, AlertDialog.THEME_HOLO_LIGHT)
//                            .setTitle(R.string.pls_select_area)
//                            .setSingleChoiceItems(areas, selectIndex, new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//                                    mAreaSelPs = which;
//                                }
//                            }).setNegativeButton(android.R.string.cancel, null)
//                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//                                    mArea = mAreas.get(mAreaSelPs);
//                                    mEtArea.setText(mArea.getName());
//                                }
//                            }).create();
//                    dialog.show();
//                }
                break;
            default:
                break;
        }
    }

    private void provinveChanged(Area provinve) {
        if (provinve != null) {
            mProvinve = provinve;
            mEtProvince.setText(mProvinve.getName());
           // mCities = mAreaDao.getAreas(mProvinve.getId());
            if (!ListUtils.isEmpty(mCities))
            mCities.clear();
            if (!ListUtils.isEmpty(mAreas))
            mAreas.clear();
            mEtCity.setText("");
            mEtArea.setText("");
            mCity = null;
            mArea = null;
//            if (mCities != null && mCities.size() > 0) {
//                mEtCity.setVisibility(View.VISIBLE);
//                mCity = mCities.get(0);
//                cityChanged(mCity);
//            } else {
//                mCity = null;
//                mArea = null;
//                mEtCity.setVisibility(View.GONE);
//                mEtArea.setVisibility(View.GONE);
//            }
        }
    }

    private void cityChanged(Area city) {
        if (city != null) {
            mCity = city;
            mEtCity.setText(mCity.getName());
            if (!ListUtils.isEmpty(mAreas))
                mAreas.clear();
            mEtArea.setText("");
            mArea = null;
          //  mAreas = mAreaDao.getAreas(mCity.getId());
//            if (mAreas != null && mAreas.size() > 0) {
//                mEtArea.setVisibility(View.VISIBLE);
//                mArea = mAreas.get(0);
//                mEtArea.setText(mArea.getName());
//            } else {
//                mArea = null;
//                mEtArea.setVisibility(View.GONE);
//            }
        }
    }

    private boolean validateAddress() {

        String userName = mEtConsignee.getText().toString();
        String province = mEtProvince.getText().toString();
        String city = mEtCity.getText().toString();
        String area = mEtArea.getText().toString();
        String addrDetail = mEtAddrDetail.getText().toString();
        String phone = mEtPhone.getText().toString();

        if (TextUtils.isEmpty(userName)) {
            ViewHub.setEditError(mEtConsignee, "收件人不能为空");
            return false;
        } else if (TextUtils.isEmpty(province)) {
            ViewHub.showLongToast(this, "省份信息不能为空");
            return false;
        } else if (TextUtils.isEmpty(city)) {
//            if (mCities == null || mCities.size() == 0) {
//            } else {
//                ViewHub.showLongToast(this, "城市信息不能为空");
//                return false;
//            }
            ViewHub.showLongToast(this, "城市信息不能为空");
            return false;
        } else if (TextUtils.isEmpty(area)) {
//            if (mAreas == null || mAreas.size() == 0) {
//            } else {
//                ViewHub.showLongToast(this, "地区信息不能为空");
//            }
            ViewHub.showLongToast(this, "地区信息不能为空");
            return false;
        } else if (TextUtils.isEmpty(addrDetail)) {
            ViewHub.setEditError(mEtAddrDetail, "详细地址不能为空");
            return false;
        } else if (TextUtils.isEmpty(phone)) {
            ViewHub.setEditError(mEtPhone, "请输入手机号码");
            return false;
        } else if (!phone.startsWith("1")) {
            ViewHub.setEditError(mEtPhone, "请输入正确的手机号码");
            return false;
        }
        // else if (!FunctionHelper.isPhoneNo(phone)) {
        // ViewHub.setEditError(mEtPhone, "请输入正确的手机号码");
        // return false;
        // }
        return true;
    }

    private void editAddress(int areaId) {
        String name = mEtConsignee.getText().toString();
        name = name.replaceAll("\r", "").replaceAll("\n", "");
        String phone = mEtPhone.getText().toString();
        String addrDetail = mEtAddrDetail.getText().toString();
        addrDetail = addrDetail.replaceAll("\r", "").replaceAll("\n", "");
        mAddress.setUserName(name);
        ;
        mAddress.setPhone(phone);
        mAddress.setDetailAddress(addrDetail);

        mAddress.setProvince(mProvinve);
        mAddress.setCity(mCity);
        mAddress.setArea(mArea);

        HttpRequestHelper r = new HttpRequestHelper();
        HttpRequest request = r.getRequest(this, ShopMethod.SHOP_ADDRESS_UPDATE, this);
        request.addParam("id", mAddress.getId() + "");
        request.addParam("realName", name);
        request.addParam("mobile", phone);
        if (areaId > -1)
            request.addParam("areaId", areaId + "");
        request.addParam("address", addrDetail);
        // r.addParam("isDefault", address.isDefault() + "");
        request.doPost();
    }

    private void addAddress(int areaId) {
        if (areaId >= 0) {
            String name = mEtConsignee.getText().toString();
            String phone = mEtPhone.getText().toString();
            String addrDetail = mEtAddrDetail.getText().toString();
            mAddress = new Address(mProvinve, mCity, mArea);

            mAddress.setUserName(name);
            ;
            mAddress.setPhone(phone);
            mAddress.setDetailAddress(addrDetail);

            HttpRequest r = new HttpRequestHelper().getRequest(this, ShopMethod.SHOP_ADDRESS_ADD, this);
            r.addParam("realName", name);
            r.addParam("mobile", phone);
            r.addParam("areaId", areaId + "");
            r.addParam("address", addrDetail);
            r.setConvert2Class(JAddress.class);
            r.doPost();
        }

    }

    @Override
    public void onRequestStart(String method) {
        if (!isDialogShowing()) {
            mLoadingDialog.setMessage(getString(R.string.saving));
            mLoadingDialog.show();
        }
        super.onRequestStart(method);

    }

    @Override
    public void onRequestSuccess(String method, Object object) {
        hideDialog();
        if (ShopMethod.SHOP_ADDRESS_ADD.equals(method)) {
            JAddress jAddr = (JAddress) object;
            if (jAddr != null) {
                mAddress.setId(jAddr.getID());
                mAddress.setUserName(jAddr.getRealName());

                Intent data = new Intent();
                data.putExtra(RESULT_ADDRESS, mAddress);
                setResult(RESULT_OK, data);
                finish();
            }
        } else if (ShopMethod.SHOP_ADDRESS_UPDATE.equals(method)) {
            Intent data = new Intent();
            data.putExtra(RESULT_ADDRESS, mAddress);
//            Log.d(getClass().getSimpleName(), "name:" + mAddress.getUserName() + " phone:" + mAddress.getPhone()
//                    + " p:" + mAddress.getProvince().getName() + " c:" + mAddress.getCity().getName() + " a:"
//                    + mAddress.getArea().getName());
            Address lastAddr = SpManager.getLastSelectAddress(getApplicationContext(),
                    SpManager.getUserId(getApplicationContext()));
            if (lastAddr != null && lastAddr.getId() == mAddress.getId()) {
                SpManager.setLastSelectAddress(getApplicationContext(), SpManager.getUserId(getApplicationContext()),
                        mAddress);
            }
            setResult(RESULT_OK, data);
            finish();
        }
        super.onRequestSuccess(method, object);
    }

    @Override
    public void onRequestFail(String method, int statusCode, String msg) {
        hideDialog();
        super.onRequestFail(method, statusCode, msg);
    }

    @Override
    public void onRequestExp(String method, String msg, ResultData data) {
        hideDialog();
        super.onRequestExp(method, msg, data);
        if ("手机号格式不正确".equals(msg)) {
            ViewHub.setEditError(mEtPhone, msg);
        }
    }

}
