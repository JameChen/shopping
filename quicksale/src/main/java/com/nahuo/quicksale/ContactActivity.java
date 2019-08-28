package com.nahuo.quicksale;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.mobstat.StatService;
import com.nahuo.quicksale.ContactPhoneEdtDialogFragment.OnSuccessListener;
import com.nahuo.quicksale.api.ShopSetAPI;
import com.nahuo.quicksale.oldermodel.ContactModel;
import com.nahuo.quicksale.oldermodel.PublicData;
import com.nahuo.quicksale.provider.ContactInfoProvider;
import com.nahuo.quicksale.util.ActivityUtil;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @description 联系方式页面
 * @created 2014-8-18 上午10:10:00
 */
public class ContactActivity extends BaseSlideBackActivity implements OnClickListener {

    private ContactActivity vThis = this;
    private static final String TAG = "ContactActivity";
    private List<ContactModel> datas = new ArrayList<ContactModel>();
    private ContactModel phoneModel;
    private ContactModel emailModel;
    private List<ContactModel> qqDatas;
    private List<ContactModel> wxDatas;
    private String address;
    private boolean phoneSet, qqSet, wxSet, emailSet, addressSet;
    public static boolean backToMain = false;
    private TextView mTvMobileSet, mTvQQSet, mTvWxSet, mTvMailSet, mTvAddressSet, mTvWeiXunSet;

    public static final int CONTACT_RELOAD = 9898;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);// 设置自定义标题栏
        setContentView(R.layout.activity_contact);
//        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
//                R.layout.layout_titlebar_default);// 更换自定义标题栏布局

        address = ContactInfoProvider.getContactAddressInfo(vThis);
        initView();
        initData();
    }

    private void initView() {
        initTitlebar();
        initItem(R.id.item_weixun, R.drawable.contact_weixun, "微询");
        initItem(R.id.item_mobile, R.drawable.contact_phone, "手机");
        initItem(R.id.item_qq, R.drawable.contact_qq, "QQ");
        initItem(R.id.item_wx, R.drawable.contact_weixin, "微信");
        initItem(R.id.item_email, R.drawable.contact_email, "邮箱");
        initItem(R.id.item_address, R.drawable.contact_address, "地址");
    }

    private void initTitlebar() {
        // 标题栏
        TextView tvTitle = (TextView) findViewById(R.id.titlebar_tvTitle);

        tvTitle.setText(R.string.me_my_link);
        Button btnLeft = (Button) findViewById(R.id.titlebar_btnLeft);
        btnLeft.setText(R.string.titlebar_btnBack);
        btnLeft.setVisibility(View.VISIBLE);
        btnLeft.setOnClickListener(this);
    }

    private void initData() {
        GetContactInfoListTask task = new GetContactInfoListTask();
        task.execute();
    }

    private void initItem(int viewId, int imgResId, String text) {
        View v = findViewById(viewId);
        v.setOnClickListener(this);
        TextView leftTv = (TextView) v.findViewById(R.id.tv_left_text);
        ImageView ivLeftIcon = (ImageView) v.findViewById(R.id.iv_left_icon);

        leftTv.setText(text);
        ivLeftIcon.setImageResource(imgResId);

        TextView rightTv = (TextView) v.findViewById(R.id.tv_right_text);
        switch (viewId) {
        case R.id.item_mobile:
            mTvMobileSet = rightTv;
            break;
        case R.id.item_qq:
            mTvQQSet = rightTv;
            break;
        case R.id.item_wx:
            mTvWxSet = rightTv;
            break;
        case R.id.item_email:
            mTvMailSet = rightTv;
            break;
        case R.id.item_address:
            mTvAddressSet = rightTv;
            break;
        case R.id.item_weixun:
            mTvWeiXunSet = rightTv;
            break;
        }
    }

    private void setRightText(TextView tv, boolean set) {
        tv.setText(set ? "已设置" : "未设置");
        tv.setTextColor(set ? getResources().getColor(R.color.lightblack) : getResources()
                .getColor(R.color.lightgray));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.titlebar_btnLeft:
            if (backToMain) {
               /* Intent gotoHomeIntent = new Intent(vThis, MainActivity.class);
                startActivity(gotoHomeIntent);*/
                ActivityUtil.goToMainActivity(vThis);
            } 
            finish();
            break;
        case R.id.item_mobile:
            ContactPhoneEdtDialogFragment showView;
            if (phoneModel != null && phoneModel.getID().length() > 0) {
                showView = ContactPhoneEdtDialogFragment.newInstance(phoneModel.getID(),
                        phoneModel.getName(), phoneModel.getContent());
            } else {
                showView = ContactPhoneEdtDialogFragment.newInstance("", "", "");
            }
            showView.setOnSuccessListener(new OnSuccessListener() {

                @Override
                public void onSuccess(String ID, String name, String content) {
                    initData();
                }
            });
            showView.show(vThis.getSupportFragmentManager(), "ContactPhoneEdtDialogFragment");
            break;
        case R.id.item_qq:
            Intent intent = new Intent(vThis, ContactQQActivity.class);
            startActivityForResult(intent,
                    ContactActivity.CONTACT_RELOAD);
            break;
        case R.id.item_wx:
            Intent intent1 = new Intent(vThis, ContactWXActivity.class);
            startActivityForResult(intent1,
                    ContactActivity.CONTACT_RELOAD);
            break;
        case R.id.item_email:
            ContactEmailEdtDialogFragment showViewEmail;
            if (emailModel != null && emailModel.getID().length() > 0) {
                showViewEmail = ContactEmailEdtDialogFragment.newInstance(emailModel.getID(),
                        emailModel.getName(), emailModel.getContent());
            }
            else {
                showViewEmail = ContactEmailEdtDialogFragment.newInstance("", "", "");
            }
            showViewEmail.setOnSuccessListener(new OnSuccessListener() {

                @Override
                public void onSuccess(String ID, String name, String content) {
                    initData();
                }
            });
            showViewEmail.show(vThis.getSupportFragmentManager(), "ContactEmailEdtDialogFragment");
            break;
        case R.id.item_address:
            ContactAddressEdtDialogFragment showViewAddress;
            showViewAddress = ContactAddressEdtDialogFragment.newInstance(address);
            showViewAddress.setOnSuccessListener(new OnSuccessListener() {

                @Override
                public void onSuccess(String ID, String name, String content) {
                    initData();
                }
            });
            showViewAddress.show(vThis.getSupportFragmentManager(),
                    "ContactAddressEdtDialogFragment");
            break;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
        case ContactActivity.CONTACT_RELOAD:
            initData();
            break;
        default:
            break;
        }
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

    /**
     * 获取联系方式数据
     * */
    private class GetContactInfoListTask extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            datas = ContactInfoProvider.getContactInfo(vThis);
            checkContactStatus();
            initContactStatus();
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                datas = ShopSetAPI.getInstance().GetContactInfoList(0, PublicData.getCookie(vThis));

                // 获取地址
                String json = ShopSetAPI.getInstance()
                        .GetAgentShopInfo(PublicData.getCookie(vThis));
                JSONObject jo = new JSONObject(json);
                address = jo.getString("Address");

                // 本地缓存起来
                ContactInfoProvider.saveContactInfo(vThis, datas, address);
                // 获取页面联系方式状态
                checkContactStatus();

                return "OK";
            } catch (Exception ex) {
                Log.e(TAG, "无法获取数据");
                ex.printStackTrace();
                return ex.getMessage() == null ? "未知异常" : ex.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if (result.equals("OK")) {
                initContactStatus();
            }
        }

    }

    private void checkContactStatus() {
        phoneSet = false;
        qqSet = false;
        wxSet = false;
        emailSet = false;
        qqDatas = new ArrayList<ContactModel>();
        wxDatas = new ArrayList<ContactModel>();
        if (datas == null) {
            return;
        }
        for (ContactModel item : datas) {
            switch (item.getTypeID()) {
            case 1:// Mobile
                phoneSet = true;
                phoneModel = item;
                break;
            case 2:// QQ
                qqSet = true;
                qqDatas.add(item);
                break;
            case 3:// WeiXin
                wxSet = true;
                wxDatas.add(item);
                break;
            case 4:// Email
                emailSet = true;
                emailModel = item;
                break;
            }
        }
        addressSet = TextUtils.isEmpty(address) ? false : true;
    }

    private void initContactStatus() {
        setRightText(mTvMobileSet, phoneSet);
        setRightText(mTvQQSet, qqSet);
        setRightText(mTvWxSet, wxSet);
        setRightText(mTvMailSet, emailSet);
        setRightText(mTvAddressSet, addressSet);
    }
}
