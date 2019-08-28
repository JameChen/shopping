package com.nahuo.quicksale;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.google.gson.reflect.TypeToken;
import com.nahuo.library.controls.LightPopDialog.PopDialogListener;
import com.nahuo.library.controls.LoadingDialog;
import com.nahuo.library.controls.pulltorefresh.PullToRefreshListView.OnRefreshListener;
import com.nahuo.library.controls.pulltorefresh.PullToRefreshListViewEx;
import com.nahuo.quicksale.adapter.AddressAdapter2;
import com.nahuo.quicksale.adapter.AddressAdapter2.IAddressEdtDelListener;
import com.nahuo.quicksale.api.HttpRequestHelper;
import com.nahuo.quicksale.api.HttpRequestHelper.HttpRequest;
import com.nahuo.quicksale.api.RequestMethod.ShopMethod;
import com.nahuo.quicksale.app.BWApplication;
import com.nahuo.quicksale.common.SpManager;
import com.nahuo.quicksale.oldermodel.Address;
import com.nahuo.quicksale.oldermodel.Area;
import com.nahuo.quicksale.oldermodel.ResultData;
import com.nahuo.quicksale.oldermodel.json.JAddress;
import com.nahuo.quicksale.provider.UserInfoProvider;

import java.util.ArrayList;
import java.util.List;

public class AddressActivity2 extends BaseActivity2 implements OnItemClickListener, IAddressEdtDelListener,
        AdapterView.OnItemLongClickListener, OnRefreshListener {

    private PullToRefreshListViewEx mRefreshListViewEx;
    private AddressAdapter2 mAdapter;
    private Address mDelAddress;

    public static final String INTENT_SELECTED_ADDRESS = "intent_selected_address";
    // public static final String INTENT_DELETED_ADDRESS = "intent_deleted_address";

    private static final int CODE_ADD_ADDRESS = 1300;
    private static final int CODE_EDIT_ADDRESS = 1301;
    private HttpRequestHelper mHttpRequestHelper = new HttpRequestHelper();
    private Address mDefaultAddress;
    private boolean mIsSelectDefault;
    /**
     * 收货地址管理
     */
    public static final String INTENT_IS_SELECT_DEFAULT = "intent_is_select_default";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address2);
        mIsSelectDefault = getIntent().getBooleanExtra(INTENT_IS_SELECT_DEFAULT, false);
        initView();
        init();
        loadAddress();
    }

    private void init() {
        mLoadingDialog = new LoadingDialog(this);
        mAdapter = new AddressAdapter2(this);
        mRefreshListViewEx.setAdapter(mAdapter);
        mAdapter.setAddressEdtDelListener(this);
    }

    private void initView() {
        setTitle(R.string.receive_address);
        setRightText("添加");
        mRefreshListViewEx = (PullToRefreshListViewEx) findViewById(R.id.lv_refresh);
        mRefreshListViewEx.setOnRefreshListener(this);
        mRefreshListViewEx.setCanLoadMore(false);
        mRefreshListViewEx.setCanRefresh(true);
        mRefreshListViewEx.setOnItemClickListener(this);
        mRefreshListViewEx.setOnItemLongClickListener(this);
    }

    @Override
    public void onRightClick(View v) {
        Intent it = new Intent(getApplicationContext(), HandleAddressActivity2.class);
        startActivityForResult(it, CODE_ADD_ADDRESS);
        super.onRightClick(v);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        Address address = (Address) parent.getAdapter().getItem(position);
        final Address addr = address;
        AlertDialog.Builder builder = new AlertDialog.Builder(this, AlertDialog.THEME_HOLO_LIGHT);
        AlertDialog dialog = builder.setItems(R.array.address_click_items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        setDefault(addr);
                        break;
                    case 1:
                        editAddress(addr);
                        break;
                    case 2:
                        delAddress(addr);
                        break;
                    default:
                        break;
                }
            }
        }).create();
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
        return true;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Address address = (Address) parent.getAdapter().getItem(position);
        if (!mIsSelectDefault) {
            BWApplication.addisEdit = true;
            SpManager.setLastSelectAddress(getApplicationContext(), SpManager.getUserId(getApplicationContext()),
                    address);
            Intent it = new Intent();
            it.putExtra(INTENT_SELECTED_ADDRESS, address);
            setResult(RESULT_OK, it);
            finish();
        } else {

        }
    }

    private void loadAddress() {
        HttpRequest r = mHttpRequestHelper.getRequest(getApplicationContext(), ShopMethod.SHOP_ADDRESS_GET_ADDRESSES,
                this);
        r.setConvert2Token(new TypeToken<List<JAddress>>() {
        }).doPost();
    }

    private void setDefault(Address address) {
        mDefaultAddress = address;
        HttpRequest r = mHttpRequestHelper.getRequest(getApplicationContext(), ShopMethod.SHOP_ADDRESS_UPDATE, this);
        r.addParam("id", address.getId() + "");
        r.addParam("isDefault", "true");
        r.doPost();
    }

    @Override
    public void onRequestStart(String method) {
        if (ShopMethod.SHOP_ADDRESS_GET_ADDRESSES.equals(method)) {
            mLoadingDialog.setMessage(getString(R.string.loading));
        } else if (ShopMethod.SHOP_ADDRESS_DELETE.equals(method)) {
            mLoadingDialog.setMessage(getString(R.string.deleting));
        } else if (ShopMethod.SHOP_ADDRESS_UPDATE.equals(method)) {
            mLoadingDialog.setMessage("正在设置默认地址...");
        }
        mLoadingDialog.show();
        super.onRequestStart(method);
    }

    @Override
    public void onRequestSuccess(String method, Object object) {
        hideDialog();
        mRefreshListViewEx.onRefreshComplete();
        if (ShopMethod.SHOP_ADDRESS_GET_ADDRESSES.equals(method)) {
            List<JAddress> jList = (List<JAddress>) object;
            if (jList != null && !jList.isEmpty()) {
                ArrayList<Address> addresses = new ArrayList<Address>();
                //  AreaDao dao = new AreaDao(getApplicationContext());
                for (JAddress ja : jList) {
                    Address add = new Address();
                    add.setId(ja.getID());
                    add.setUserName(ja.getRealName());
                    add.setPostCode(ja.getPostCode());
                    add.setPhone(ja.getMobile());
                    add.setDetailAddress(ja.getAddress());
                    add.setDefault(ja.isIsDefault());
                    String area_name = "", city_name = "", province_name = "";
                    if (!TextUtils.isEmpty(ja.getArea())) {
                        String[] mRegions = ja.getArea().split("\\s+");
                        if (mRegions.length >= 3) {
                            province_name = mRegions[0];
                            city_name = mRegions[1];
                            area_name = mRegions[2];
                        } else if (mRegions.length == 2) {
                            province_name = mRegions[0];
                            city_name = mRegions[1];
                        } else {
                            province_name = mRegions[0];
                        }
                    }
//                    Area area = dao.getArea(ja.getAreaID());
//                    Area city = dao.getArea(area.getParentId());
                    Area area = new Area();
                    area.setName(area_name);
                    area.setId(ja.getAreaID());
                    area.setParentId(ja.getCityID());
                    Area city = new Area();
                    city.setId(ja.getCityID());
                    city.setParentId(ja.getProvinceID());
                    city.setName(city_name);
                    Area province = new Area();
                    province.setName(province_name);
                    province.setId(ja.getProvinceID());
//                    if (city==null)
//                    {
//                        province = dao.getArea(area.getId());
//                    }
//                    else {
//                        province = dao.getArea(city.getParentId());
//                    }
                    add.setArea(area);
                    add.setCity(city);
                    add.setProvince(province);

                    if (ja.isIsDefault()) {
                        UserInfoProvider.setDefaultAddress(getApplicationContext(),
                                SpManager.getUserId(getApplicationContext()), ja.getAddress());
                        SpManager.setLastSelectAddress(getApplicationContext(),
                                SpManager.getUserId(getApplicationContext()), add);
                    }

                    addresses.add(add);
                }
                mAdapter.setData(addresses);
                mAdapter.notifyDataSetChanged();
            } else {
                Intent it = new Intent(getApplicationContext(), HandleAddressActivity2.class);
                startActivityForResult(it, CODE_ADD_ADDRESS);
            }
        } else if (ShopMethod.SHOP_ADDRESS_DELETE.equals(method)) {
            if (mDelAddress != null) {
                Address defaultAddr = SpManager.getLastSelectAddress(getApplicationContext(),
                        SpManager.getUserId(getApplicationContext()));
                mAdapter.remove(mDelAddress);
                if (defaultAddr != null && mDelAddress.getId() == defaultAddr.getId()) {
                    Log.i(getClass().getSimpleName(), "SHOP_ADDRESS_DELETE:");
                    List<Address> ls = mAdapter.getData();
                    Address latestAddr = null;
                    if (ls != null && !ls.isEmpty()) {
                        latestAddr = ls.get(0);
                        for (Address ad : ls) {
                            if (ad.isDefault()) {
                                latestAddr = ad;
                            }
                        }
                    }
                    SpManager.setLastSelectAddress(getApplicationContext(),
                            SpManager.getUserId(getApplicationContext()), latestAddr);
                    // Intent data = new Intent();
                    // data.putExtra(INTENT_DELETED_ADDRESS, mDelAddress);
                    // setResult(RESULT_OK, data);
                }
                mAdapter.notifyDataSetChanged();

            }
        } else if (ShopMethod.SHOP_ADDRESS_UPDATE.equals(method)) {
            if (mDefaultAddress != null) {
                UserInfoProvider.setDefaultAddress(getApplicationContext(),
                        SpManager.getUserId(getApplicationContext()), mDefaultAddress.getDetailAddress());
                ViewHub.showShortToast(getApplicationContext(), "设置成功");
                setReplaceElement(mAdapter.getList(), mDefaultAddress, mAdapter.getDefault());
            }
        }
        super.onRequestSuccess(method, object);
    }

    @Override
    public void onRequestFail(String method, int statusCode, String msg) {
        hideDialog();
        mRefreshListViewEx.onRefreshComplete();

        super.onRequestFail(method, statusCode, msg);
    }

    @Override
    public void onRequestExp(String method, String msg, ResultData data) {
        hideDialog();
        mRefreshListViewEx.onRefreshComplete();
        super.onRequestExp(method, msg, data);
    }

    @Override
    public void edit(int position, Address address) {
        editAddress(address);
    }

    private void editAddress(Address address) {
        Intent it = new Intent(getApplicationContext(), HandleAddressActivity2.class);
        it.putExtra(HandleAddressActivity2.EXTRA_ADDRESS, address);
        startActivityForResult(it, CODE_EDIT_ADDRESS);
    }

    private void delAddress(final Address address) {
        ViewHub.showLightPopDialog(this, "温馨提示", "您确定要删除该收货地址吗？", getString(android.R.string.cancel),
                getString(android.R.string.ok), new PopDialogListener() {
                    @Override
                    public void onPopDialogButtonClick(int which) {
                        deleteAddress(address);
                    }
                });
    }

    @Override
    public void delete(final int position, final Address address) {
        delAddress(address);
    }

    private void deleteAddress(Address address) {
        mDelAddress = address;
        HttpRequestHelper r = new HttpRequestHelper();
        r.getRequest(this, ShopMethod.SHOP_ADDRESS_DELETE, this).addParam("id", address.getId() + "").doPost();
        if (address.isDefault()) {// 把本地缓存的默认地址设为空
            UserInfoProvider.setDefaultAddress(getApplicationContext(), SpManager.getUserId(getApplicationContext()),
                    "");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case CODE_ADD_ADDRESS:
                    Address address = (Address) data.getSerializableExtra(HandleAddressActivity2.RESULT_ADDRESS);
                    if (mAdapter != null) {
//                        if (ListUtils.isEmpty(mAdapter.getData())) {
//                            setDefault(address);
//                        }
                        setDefault(address);
                        mAdapter.add(address);
                    }
                    SpManager.setLastSelectAddress(getApplicationContext(),
                            SpManager.getUserId(getApplicationContext()), address);
                    if (!mIsSelectDefault) {
                        // 新添加地址后直接返回使用
                        BWApplication.addisEdit = true;
                        Intent it = new Intent();
                        it.putExtra(INTENT_SELECTED_ADDRESS, address);
                        setResult(RESULT_OK, it);
                        finish();
                    }
                    break;
                case CODE_EDIT_ADDRESS:
                    address = (Address) data.getSerializableExtra(HandleAddressActivity2.RESULT_ADDRESS);
//                    Log.d(getClass().getSimpleName(), "name:" + address.getUserName() + " phone:" + address.getPhone()
//                            + " p:" + address.getProvince().getName() + " c:" + address.getCity().getName() + " a:"
//                            + address.getArea().getName());
                    List<Address> list = mAdapter.getList();
                    for (int i = 0; i < list.size(); i++) {
                        if (list.get(i).getId() == address.getId()) {
                            list.add(i, address);
                            list.remove(i + 1);
                            mAdapter.notifyDataSetChanged();
                            break;
                        }
                    }

                    break;
                default:
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    //替换adapter的list的一个元素并通知adapter刷新
    private void setReplaceElement(List<Address> list, Address address, Address oldAddress) {
        if (address != oldAddress) {
            boolean isCanBreak1 = false, isCanBreak2 = false;
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getId() == address.getId()) {
                    list.add(i, address);
                    list.remove(i + 1);
                    list.get(i).setDefault(true);
                    isCanBreak1 = true;
                }
                if (oldAddress != null) {
                    if (list.get(i).getId() == oldAddress.getId()) {
                        list.get(i).setDefault(false);
                        isCanBreak2 = true;
                    }
                }
                if ((isCanBreak1 && isCanBreak2) || (isCanBreak1 && oldAddress == null)) {
                    mAdapter.notifyDataSetChanged();
                    break;
                }
            }
        }
    }

    @Override
    public void onRefresh() {
        loadAddress();
    }


}
