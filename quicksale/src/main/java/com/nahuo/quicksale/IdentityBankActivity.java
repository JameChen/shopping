package com.nahuo.quicksale;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.nahuo.library.controls.LoadingDialog;
import com.nahuo.library.helper.GsonHelper;
import com.nahuo.quicksale.api.BankAPI;
import com.nahuo.quicksale.common.Const;
import com.nahuo.quicksale.common.ListUtils;
import com.nahuo.quicksale.common.SpManager;
import com.nahuo.quicksale.common.StringUtils;
import com.nahuo.quicksale.oldermodel.BankModel;
import com.nahuo.quicksale.oldermodel.json.JAuthInfo;
import com.nahuo.quicksale.provider.UserInfoProvider;

import java.util.List;

/**
 * Created by ALAN on 2017/5/22 0022.
 * 绑定银行卡
 */
public class IdentityBankActivity extends BaseSlideBackActivity implements View.OnClickListener {
    private IdentityBankActivity vthis = this;
    private EditText edname, edcardId, edbankCardID;
    private TextView edbankName;
    private TextView tvname, tvcardId, tvbankName, tvbankCardID, bottomTv, bottomTvState;
    private TextView mTVCardState;//验证状态
    private Button save;
    AlertDialog dialog;
    private int mProvinveSelPs;
    private LoadingDialog loadingDialog;
    private BankModel mBank;

    private View mLayoutCardState;
    private JAuthInfo mAuthInfo;

    private static enum QUEST_TYPE {
        BANKLIST, SAVE
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_identity_auth_bank);
        //getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.layout_titlebar_default);
        initParams();
        initView();
    }

    private void initParams() {
        mAuthInfo = UserInfoProvider.getAuthInfo(vthis, SpManager.getUserId(vthis));
        if (mAuthInfo == null) {
            mAuthInfo = JAuthInfo.getNotCommitInstance();
        }
    }

    private void initView() {
        initTitleBar();
        save = (Button) findViewById(R.id.btn_save);
        save.setOnClickListener(this);
        edname = (EditText) findViewById(R.id.et_user_name);
        edcardId = (EditText) findViewById(R.id.et_card_number);
        edbankName = (TextView) findViewById(R.id.et_card_user_bank_name);
        edbankName.setOnClickListener(this);
        edbankCardID = (EditText) findViewById(R.id.et_bank_card_number);
        tvname = (TextView) findViewById(R.id.tv_user_name_after);
        tvcardId = (TextView) findViewById(R.id.tv_card_number_after);
        tvbankName = (TextView) findViewById(R.id.tv_card_user_bank_name_after);
        tvbankCardID = (TextView) findViewById(R.id.tv_bank_card_number_after);
        bottomTv = (TextView) findViewById(R.id.bottom_info);
        bottomTvState = (TextView) findViewById(R.id.bottom_info_state);
        mLayoutCardState = findViewById(R.id.layout_card_state);
        mTVCardState = (TextView) findViewById(R.id.tv_card_state);
        loadingDialog = new LoadingDialog(vthis);
    }

    private void initTitleBar() {
        TextView tvTitle = (TextView) findViewById(R.id.titlebar_tvTitle);
        tvTitle.setText("我的银行卡");
        Button btnLeft = (Button) findViewById(R.id.titlebar_btnLeft);
        btnLeft.setText(R.string.titlebar_btnBack);
        btnLeft.setVisibility(View.VISIBLE);
        btnLeft.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.titlebar_btnLeft:
                ViewHub.hideKeyboard(this);
                finish();
                break;
            case R.id.et_card_user_bank_name:
                new MyTask(QUEST_TYPE.BANKLIST).execute();
                break;
            case R.id.btn_save:
                //开始存入身份信息中
                if (verificationInfo(edname.getText().toString(), edcardId.getText().toString(), edbankName.getText().toString(), edbankCardID.getText().toString())) {
                    new MyTask(QUEST_TYPE.SAVE).execute();
                }
                break;
        }
    }


    /**
     * Description:在view的编辑与正确显示之前切换
     *
     * @author ALAN
     */
    private void changeView(boolean isEdit) {
        edname.setVisibility(isEdit ? View.VISIBLE : View.GONE);
        edcardId.setVisibility(isEdit ? View.VISIBLE : View.GONE);
        edbankName.setVisibility(isEdit ? View.VISIBLE : View.GONE);
        edbankCardID.setVisibility(isEdit ? View.VISIBLE : View.GONE);
        bottomTv.setVisibility(isEdit ? View.VISIBLE : View.GONE);
        bottomTvState.setVisibility(isEdit ? View.GONE : View.VISIBLE);
        save.setVisibility(isEdit ? View.VISIBLE : View.GONE);
        String authState = mAuthInfo.getStatu();
        if (authState.equals(Const.IDAuthState.AUTH_PASSED) || authState.equals(Const.IDAuthState.CHECKING)) {
            mLayoutCardState.setVisibility(View.VISIBLE);
        } else {
            mLayoutCardState.setVisibility(View.GONE);
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
        tvname.setText(edname.getText().toString());
        tvcardId.setText(edcardId.getText().toString());
        tvbankName.setText(edbankName.getText().toString());
        tvbankCardID.setText(edbankCardID.getText().toString());
        mTVCardState.setText(Const.IDAuthState.CHECKING);
        bottomTvState.setText(Html.fromHtml("<font color='#E3D7DD'>绑定银行卡审核将在1-3个工作日完成</font>"));
        UserInfoProvider.setIdentityAuthState(vthis, SpManager.getUserId(vthis), Const.IDAuthState.CHECKING);
        mAuthInfo.setStatu(Const.IDAuthState.CHECKING);
        UserInfoProvider.setAuthInfo(vthis, mAuthInfo);
        UserInfoProvider.setBankState(vthis, SpManager.getUserId(vthis), Const.IDAuthState.CHECKING);
    }

    /**
     * Description:切换到"审核完成的界面 2014-7-28 下午5:57:55
     *
     * @author ZZB
     */
    private void switchToFinishingState() {
        mAuthInfo.setStatu(Const.IDAuthState.CHECKING);
        changeView(false);
        tvname.setText(edname.getText().toString());
        tvcardId.setText(edcardId.getText().toString());
        tvbankName.setText(edbankName.getText().toString());
        tvbankCardID.setText(edbankCardID.getText().toString());
        mTVCardState.setText(Const.IDAuthState.AUTH_PASSED);
        bottomTvState.setText(Html.fromHtml("<font color='#E3D7DD'>绑定银行卡审核通过</font>"));
        UserInfoProvider.setIdentityAuthState(vthis, SpManager.getUserId(vthis), Const.IDAuthState.CHECKING);
        mAuthInfo.setStatu(Const.IDAuthState.AUTH_PASSED);
        UserInfoProvider.setAuthInfo(vthis, mAuthInfo);
        UserInfoProvider.setBankState(vthis, SpManager.getUserId(vthis), Const.IDAuthState.AUTH_PASSED);
    }

    private class MyTask extends AsyncTask<Void, Void, String> {
        private String json;
        private QUEST_TYPE type;
        private String name, cardID, bankName, bankCardId;

        public MyTask(QUEST_TYPE type) {
            this.type = type;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (type.equals(QUEST_TYPE.SAVE)) {
                name = edname.getText().toString();
                cardID = edcardId.getText().toString();
                bankName = edbankName.getText().toString();
                bankCardId = edbankCardID.getText().toString();
            }
            switch (type) {
                case BANKLIST:
                    loadingDialog.start(getString(R.string.me_bink_list));
                    break;
                case SAVE:
                    loadingDialog.start(getString(R.string.save_bink_info));
                    break;
            }
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                switch (type) {
                    case BANKLIST:
                        json = BankAPI.getInstance().getBankList(vthis);
                        break;
                    case SAVE:
                        json = BankAPI.getInstance().submitBindBank(vthis, name, cardID, bankName, bankCardId);
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
                return "error:" + e.getMessage();
            }
            return json;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (loadingDialog.isShowing()) {
                loadingDialog.stop();
            }
            if (result instanceof String && ((String) result).startsWith("error:")) {
                ViewHub.showShortToast(vthis, ((String) result).replace("error:", ""));
            } else {
                switch (type) {
                    case BANKLIST:
                        final List<BankModel> list;

                        list = GsonHelper
                                .jsonToObject(json,
                                        new TypeToken<List<BankModel>>() {
                                        });
                        if (!ListUtils.isEmpty(list)) {
                            if (mBank == null) {
                                mBank = list.get(0);
                            }
                            String[] provincs = new String[list.size()];
                            int selectIndex = 0;
                            for (int i = 0; i < provincs.length; i++) {
                                provincs[i] = list.get(i).getName();
                                if (mBank != null && provincs[i].equals(mBank.getName())) {
                                    selectIndex = i;
                                }
                            }
                            mProvinveSelPs = selectIndex;
                            dialog = new AlertDialog.Builder(vthis, AlertDialog.THEME_HOLO_LIGHT)
                                    .setTitle(R.string.pls_select_bankname)
                                    .setSingleChoiceItems(provincs, selectIndex, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            mProvinveSelPs = which;
                                        }
                                    }).setNegativeButton(android.R.string.cancel, null)
                                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            mBank = list.get(mProvinveSelPs);
                                            edbankName.setText(mBank.getName());
                                        }
                                    }).create();
                            dialog.show();
                        }
                        break;
                    case SAVE:
                        ViewHub.showLongToast(vthis, "审核成功");
                        switchToFinishingState();
                        finish();
                        //switchToCheckingState();
                        // finish();
                        break;
                }
            }
        }
    }

    private boolean verificationInfo(String name, String cardId, String bankName, String bankCardID) {
        //edname.getText().toString(),edcardId.getText().toString(),edbankName.getText().toString(),edbankCardID.getText().toString()
        if (StringUtils.isEmptyWithTrim(name)) {
            ViewHub.setEditError(edname, "持证姓名不能为空");
            edname.requestFocus();
            return false;
        } else if
            // (!FunctionHelper.isIDCard(cardId))
                (TextUtils.isEmpty(cardId)) {
            ViewHub.setEditError(edcardId, "请输入身份证号码");
            edcardId.requestFocus();
            return false;
        } else if (StringUtils.isEmptyWithTrim(bankName)) {
            ViewHub.showLongToast(vthis, "银行名称不能为空");
            return false;
        } else if (StringUtils.isEmptyWithTrim(bankCardID)) {
            ViewHub.setEditError(edbankCardID, "银行卡号不能为空");
            edbankCardID.requestFocus();
            return false;
        }
        return true;
    }
}
