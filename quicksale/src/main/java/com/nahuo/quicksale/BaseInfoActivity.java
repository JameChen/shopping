package com.nahuo.quicksale;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nahuo.library.controls.LoadingDialog;
import com.nahuo.library.helper.FunctionHelper;
import com.nahuo.library.helper.ImageUrlExtends;
import com.nahuo.quicksale.api.ShopSetAPI;
import com.nahuo.quicksale.base.BaseAppCompatActivity;
import com.nahuo.quicksale.eventbus.BusEvent;
import com.nahuo.quicksale.eventbus.EventBusId;
import com.nahuo.quicksale.oldermodel.AuthInfoModel;
import com.nahuo.quicksale.oldermodel.PublicData;
import com.squareup.picasso.Picasso;

import de.greenrobot.event.EventBus;

public class BaseInfoActivity extends BaseAppCompatActivity implements OnClickListener {

    private Context mContext = this;
    private EditText et_boss_name, et_phone, et_store_name, et_store_num,et_style;
    private TextView tv_address, tv_title, tvMsg, tvRight;
    private LinearLayout llPanel1, llPanel2;
    private RelativeLayout rlLocation;
    private ImageView ivLicense, ivStore;
    private EventBus mEventBus = EventBus.getDefault();
    private LoadingDialog loadingDialog;

    private AuthInfoModel data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_base_info);
        if (loadingDialog == null)
            loadingDialog = new LoadingDialog(this);
        mEventBus.register(this);
        initViews();
        initData();

    }

    private void initViews() {
        loadingDialog.start("加载中...");
        llPanel1 = (LinearLayout) findViewById(R.id.llPanel1);
        llPanel2 = (LinearLayout) findViewById(R.id.llPanel2);
        tv_title = (TextView) findViewById(R.id.title).findViewById(R.id.tv_title);
        tvRight = (TextView) findViewById(R.id.title).findViewById(R.id.tv_right);
        ivStore = (ImageView) this.findViewById(R.id.ivStore);
        ivLicense = (ImageView) this.findViewById(R.id.ivLicense);
        tv_title.setText("1/2填写基本信息");
        this.findViewById(R.id.title).findViewById(R.id.iv_left).setOnClickListener(this);
        tvRight.setText("下一步");
        tvRight.setOnClickListener(this);
        et_boss_name = (EditText) this.findViewById(R.id.et_boss_name);
        et_phone = (EditText) this.findViewById(R.id.et_phone);
        et_store_name = (EditText) this.findViewById(R.id.et_store_name);
        et_store_num = (EditText) this.findViewById(R.id.et_store_num);
        et_style = (EditText) this.findViewById(R.id.et_style);
        tv_address = (TextView) this.findViewById(R.id.tv_address);
        tvMsg = (TextView) this.findViewById(R.id.tvMsg);
        rlLocation = (RelativeLayout) this.findViewById(R.id.rlLocation);
        this.findViewById(R.id.rlTip).setVisibility(View.GONE);
        rlLocation.setOnClickListener(this);
    }

    private void setDataView() {
        et_boss_name.setText(data.getAuthInfo().getRealName());
        et_phone.setText(data.getAuthInfo().getMobile());
        et_store_name.setText(data.getAuthInfo().getShopName());
        String address = data.getAuthInfo().getProvince() +
                data.getAuthInfo().getCity() +
                data.getAuthInfo().getArea()
                + data.getAuthInfo().getAddress()
                + data.getAuthInfo().getStreet();
        tv_address.setText(address);
        et_store_num.setText(data.getAuthInfo().getHouseNumber());
        et_style.setText(data.getAuthInfo().getMjStyle());
        if (data.getAuthInfo().getImages() != null && data.getAuthInfo().getImages().size() > 0) {
            for (AuthInfoModel.Images item : data.getAuthInfo().getImages()) {
                int typeID = item.getTypeID();
                String url = item.getUrl();
                url = ImageUrlExtends.getImageUrl(url);
                if (typeID == 1) {// 店铺
                    Picasso.with(mContext).load(url).placeholder(R.drawable.empty_photo).into(ivStore);
                } else {//执照
                    Picasso.with(mContext).load(url).placeholder(R.drawable.empty_photo).into(ivLicense);
                }
            }
        }
    }

    public void initData() {
        new Task().execute((Void)null);
    }

    public void onDateLoaded() {
        if (data != null) {
            if (data.getAuthInfo().getStatu().equals("未认证")) {// 可编辑
                llPanel1.setVisibility(View.VISIBLE);
                llPanel2.setVisibility(View.GONE);
                tvMsg.setVisibility(View.GONE);
                tvRight.setVisibility(View.VISIBLE);
                setClick(true);
            } else if (data.getAuthInfo().getStatu().equals("审核中")) {// 不可编辑
                tvRight.setVisibility(View.GONE);
                llPanel1.setVisibility(View.VISIBLE);
                llPanel2.setVisibility(View.VISIBLE);
                tvMsg.setVisibility(View.VISIBLE);
                setClick(false);
                tv_title.setText("店铺认证");
                setDataView();
                tvMsg.setText("您的店铺认证正在审核中!");
            } else if (data.getAuthInfo().getStatu().equals("认证失败")) {// 可编辑
                tvRight.setVisibility(View.VISIBLE);
                llPanel1.setVisibility(View.VISIBLE);
                llPanel2.setVisibility(View.GONE);
                tvMsg.setVisibility(View.VISIBLE);
                setClick(true);
                setDataView();
                if (data.getAuthInfo().getAuthResult().length()>0) {
                    tvMsg.setText(data.getAuthInfo().getAuthResult());
                } else {
                    tvMsg.setText(data.getAuthInfo().getStatu());
                }
            } else if (data.getAuthInfo().getStatu().equals("已认证")) {// 不可编辑
                tvRight.setVisibility(View.GONE);
                llPanel1.setVisibility(View.VISIBLE);
                llPanel2.setVisibility(View.VISIBLE);
                tvMsg.setVisibility(View.VISIBLE);
                setClick(false);
                tv_title.setText("店铺认证");
                setDataView();
                tvMsg.setText("您的店铺已认证通过!");
            } else {// 认证失败，不可编辑
                tvRight.setVisibility(View.GONE);
                llPanel1.setVisibility(View.VISIBLE);
                llPanel2.setVisibility(View.VISIBLE);
                tvMsg.setVisibility(View.VISIBLE);
                tv_title.setText("店铺认证");
                setClick(false);
                setDataView();
                if (data.getAuthInfo().getAuthResult().length()>0) {
                    tvMsg.setText(data.getAuthInfo().getAuthResult());
                } else {
                    tvMsg.setText(data.getAuthInfo().getStatu());
                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_left: {
                finish();
                break;
            }
            case R.id.tv_right: {// 下一步
                if (isGetEmpty()) {
                    return;
                }
                if(data==null)
                {
                 data = new AuthInfoModel();
                    AuthInfoModel.AIModel ai = data.new AIModel();
                    data.setAuthInfo(ai);
                }
                data.getAuthInfo().setRealName(et_boss_name.getText().toString().trim());//店主名字
                data.getAuthInfo().setMobile(et_phone.getText().toString().trim());
                data.getAuthInfo().setShopName(et_store_name.getText().toString().trim());// 店铺名
                data.getAuthInfo().setHouseNumber(et_store_num.getText().toString().trim());// 门牌号
                data.getAuthInfo().setMjStyle(et_style.getText().toString().trim());// 主营风格

                Intent intent = new Intent(mContext, BaseInfoSecondActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable(BaseInfoSecondActivity.EXTRA_DATA, data);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            }
            case R.id.rlLocation: {// 地图
                Intent intent = new Intent(mContext, MapActivity.class);
                startActivityForResult(intent,100);
                break;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent dataIntent) {
        super.onActivityResult(requestCode, resultCode, dataIntent);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 100: {
                    AuthInfoModel.AIModel aiData = (AuthInfoModel.AIModel) dataIntent.getSerializableExtra(MapActivity.resultKey);
                    data.setAuthInfo(aiData);
                    String address = data.getAuthInfo().getProvince() +
                            data.getAuthInfo().getCity() +
                            data.getAuthInfo().getArea()
                            + data.getAuthInfo().getAddress()
                            + data.getAuthInfo().getStreet();
                    tv_address.setText(address);
                }
                break;
            }
        }
    }

    public void onEventMainThread(BusEvent event) {
        switch (event.id) {
            case EventBusId.RENZHENG_SELECT_RB:// 提交成功后
                finish();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mEventBus.unregister(this);
    }

    private void getFocus(EditText et) {
        et.setFocusable(true);
        et.setFocusableInTouchMode(true);
        et.requestFocus();
    }

    private boolean isGetEmpty() {
        boolean b = false;
        String etBossName = et_boss_name.getText().toString().trim();//  店主姓名
        String etPhone = et_phone.getText().toString().trim();
        String etStoreName = et_store_name.getText().toString().trim();//店铺名称
        String etAddress = tv_address.getText().toString().trim();
        if (TextUtils.isEmpty(etBossName)) {
            b = true;
            getFocus(et_boss_name);
            Toast.makeText(this, "店主姓名不能为空", Toast.LENGTH_SHORT).show();
            return b;
        } else if (etBossName.length() > 50) {
            b = true;
            Toast.makeText(this, "请填写正确的姓名", Toast.LENGTH_SHORT).show();
            return b;
        }

        if (TextUtils.isEmpty(etPhone)) {
            b = true;
            getFocus(et_phone);
            Toast.makeText(this, "联系电话不能为空", Toast.LENGTH_SHORT).show();
            return b;
        } else if (!FunctionHelper.isPhoneNo(etPhone)) {
            if (FunctionHelper.isTelepone(etPhone) && etPhone.length() > 10) {
                b = false;
            } else {
                b = true;
                Toast.makeText(this, "请填写正确的联系电话号码", Toast.LENGTH_SHORT).show();
                return b;
            }
        }

        if (TextUtils.isEmpty(etStoreName)) {
            b = true;
            getFocus(et_store_name);
            Toast.makeText(this, "店铺名称不能为空", Toast.LENGTH_SHORT).show();
            return b;
        } else if (etStoreName.length() > 50) {
            b = true;
            Toast.makeText(this, "请填写正确的名称", Toast.LENGTH_SHORT).show();
            return b;
        }

        if (TextUtils.isEmpty(etAddress)) {
            Toast.makeText(this, "店铺地址不能为空", Toast.LENGTH_SHORT).show();
            b = true;
            return b;
        }
        return b;
    }

    private void setClick(boolean b) {
        if (b) {
            et_boss_name.setEnabled(true);
            et_boss_name.setClickable(true);
            et_phone.setEnabled(true);
            et_phone.setClickable(true);
            et_store_name.setEnabled(true);
            et_store_name.setClickable(true);
            et_store_num.setEnabled(true);
            et_store_num.setClickable(true);
            et_style.setEnabled(true);
            et_style.setClickable(true);
            tv_address.setEnabled(false);
            tv_address.setClickable(false);
            rlLocation.setClickable(true);
        } else {
            et_boss_name.setEnabled(false);
            et_boss_name.setClickable(false);
            et_phone.setEnabled(false);
            et_phone.setClickable(false);
            et_store_name.setEnabled(false);
            et_store_name.setClickable(false);
            tv_address.setEnabled(false);
            tv_address.setClickable(false);
            rlLocation.setClickable(false);
            et_store_num.setEnabled(false);
            et_store_num.setClickable(false);
            et_style.setEnabled(false);
            et_style.setClickable(false);
        }
    }

    public class Task extends AsyncTask<Void, Void, String> {
        public Task() {
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loadingDialog.start("加载数据中,请稍后...");
        }

        @Override
        protected void onPostExecute(String result) {
            if (result.equals("ok")) {
                onDateLoaded();
            }
            else
            {
                ViewHub.showLongToast(mContext,result);
            }
            if (loadingDialog.isShowing()) {
                loadingDialog.stop();
            }
        }

        @Override
        protected String doInBackground(Void... params) {

            try {
                data = ShopSetAPI.getInstance().GetAuthInfo(PublicData.getCookie(mContext));
                return "ok";
            } catch (Exception ex) {
                ex.printStackTrace();
                return "error:"+ex.getMessage();
            }
        }

    }

}
