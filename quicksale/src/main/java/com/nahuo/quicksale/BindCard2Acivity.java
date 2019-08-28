package com.nahuo.quicksale;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.baidu.mobstat.StatService;
import com.google.gson.reflect.TypeToken;
import com.nahuo.library.controls.LoadingDialog;
import com.nahuo.library.helper.FunctionHelper;
import com.nahuo.library.helper.GsonHelper;
import com.nahuo.quicksale.PayPswDlgFragment.Listener;
import com.nahuo.quicksale.api.AccountAPI;
import com.nahuo.quicksale.api.BankAPI;
import com.nahuo.quicksale.common.Const;
import com.nahuo.quicksale.common.Const.PasswordExtra;
import com.nahuo.quicksale.common.Const.PasswordType;
import com.nahuo.quicksale.common.ListUtils;
import com.nahuo.quicksale.common.SpManager;
import com.nahuo.quicksale.eventbus.BusEvent;
import com.nahuo.quicksale.eventbus.EventBusId;
import com.nahuo.quicksale.oldermodel.Bank;
import com.nahuo.quicksale.oldermodel.BankModel;
import com.nahuo.quicksale.oldermodel.json.JAuthInfo;
import com.nahuo.quicksale.oldermodel.json.JBankInfo;
import com.nahuo.quicksale.provider.UserInfoProvider;

import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * @author ZZB
 * @description 绑定银行卡
 * @created 2014-9-15 下午4:27:01
 */
public class BindCard2Acivity extends BaseSlideBackActivity implements OnClickListener {

    public static final String EXTRA_BANK_INFO = "EXTRA_BANK_INFO";
    private static final int REQUEST_SELECT_SUB_BANK = 1;
    private Context mContext = this;
    private EditText mEtCardNo;
    private TextView mTvParentBank, mTvCardNo, mTvState;
    private TextView mTvUserName, mTvTips;
    private Button mSubmitBtn;
    // 银行和支行缓存
    private Bank mParentBank;
    private List<Bank> mBanks;
    /**
     * 是否绑定银行
     */
    // private boolean mBinded;
    private JBankInfo mBankInfo;
    private TextView tvBank;

    private LoadingDialog loadingDialog;
    AlertDialog dialog;
    private BindCard2Acivity vthis = this;

    private int mProvinveSelPs;
    private BankModel mBank;

    private static enum Step {
        INIT_BANK, BIND_BANK
    }

    private static enum QUEST_TYPE {
        BANKLIST
    }
    private EventBus mEventBus = EventBus.getDefault();
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mEventBus!=null)
            mEventBus.unregister(this);
    }
    public void onEventMainThread(BusEvent event) {

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);// 设置自定义标题栏
        setContentView(R.layout.activity_bindcard);
//        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
//                R.layout.layout_titlebar_default);// 更换自定义标题栏布局
        try {
            mEventBus.registerSticky(this);
            initView();
            initData();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
       /* case REQUEST_SELECT_SUB_BANK:
            if (data == null) {
                return;
            }
            mSubBank = (Bank) data.getSerializableExtra(SelectBankActivity.EXTRA_BANK);
            mEtSubBank.setText(mSubBank.getName());
            break;*/
            default:
                super.onActivityResult(requestCode, resultCode, data);
                break;
        }

    }

    private void initData() {
        mBankInfo = (JBankInfo) getIntent().getSerializableExtra(EXTRA_BANK_INFO);
        int userId = SpManager.getUserId(mContext);
        if (mBankInfo == null) {
//            mBankInfo = UserInfoProvider.getBankInfo(mContext, userId);
//            if (mBankInfo == null) {
//                mBankInfo = new JBankInfo();
//                mBankInfo.setStatu(Const.BankState.NOT_COMMIT);
//            }
        }
        mBank = new BankModel();
        new Task2().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private class Task2 extends AsyncTask<Object, Void, Object> {


        @Override
        protected void onPreExecute() {

        }

        @Override
        protected Object doInBackground(Object... params) {
            try {
                mBankInfo = AccountAPI.getBankInfo(mContext);

                return mBankInfo;
            } catch (Exception e) {
                e.printStackTrace();
                return "error:" + e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(Object result) {
            if (result instanceof String && ((String) result).startsWith("error:")) {
                // ViewHub.showLongToast(mContext, ((String) result).replace("error:", ""));
            } else {
                new Task(Step.INIT_BANK).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

            }

        }

    }

    private void initView() {
        try {
            loadingDialog = new LoadingDialog(vthis);
            // 标题栏
            initTitleBar();
            mEtCardNo = (EditText) findViewById(R.id.et_bank_card_no);
            mTvUserName = (TextView) findViewById(R.id.tv_username);
            mSubmitBtn = (Button) findViewById(R.id.bingcard_save_btn);
            JAuthInfo authInfo = UserInfoProvider.getAuthInfo(mContext, SpManager.getUserId(mContext));
            if (authInfo != null) {
                mTvUserName.setText(authInfo.getRealName());
            }
            mTvState = (TextView) findViewById(R.id.tv_state);
            mTvParentBank = (TextView) findViewById(R.id.tv_parent_bank);
            mTvCardNo = (TextView) findViewById(R.id.tv_card_no);
            mTvTips = (TextView) findViewById(R.id.tv_tips);

            tvBank = (TextView) findViewById(R.id.tv_bank);
            tvBank.setOnClickListener(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateUI() {
        String bankState = mBankInfo.getStatu();
        mTvState.setText(bankState);
        if (mBankInfo.getRealName() != null)
            mTvUserName.setText(mBankInfo.getRealName());
        if (Const.BankState.AUTH_PASSED.equals(bankState)) {//验证通过
            changeView(true);
            mSubmitBtn.setText("修改");
            tvBank.setText(mBankInfo.getBank());
            mEtCardNo.setText(mBankInfo.getCardNumber());
        } else if (Const.BankState.NOT_COMMIT.equals(bankState)) {//未提交
            changeView(true);
        } else if (Const.BankState.CHECKING.equals(bankState)) {//审核中
            changeView(false);
            mTvParentBank.setText(mBankInfo.getBank());
            mTvCardNo.setText(mBankInfo.getCardNumber());
        } else {//驳回或者重审
            changeView(true);
            mTvTips.setVisibility(View.VISIBLE);
            mTvTips.setText(Html.fromHtml("驳回原因：<font color='red'>" + mBankInfo.getMessage() + "</font>"));
        }
    }

    private void changeView(boolean isEdit) {
        //编辑可见
        int editVisible = isEdit ? View.VISIBLE : View.GONE;
        //编辑不可见
        int unEditVisible = isEdit ? View.GONE : View.VISIBLE;

        //编辑状态不可见
        mTvCardNo.setVisibility(unEditVisible);
        mTvCardNo.setVisibility(unEditVisible);
        //编辑状态可见
        tvBank.setVisibility(editVisible);
        mEtCardNo.setVisibility(editVisible);
        mSubmitBtn.setVisibility(editVisible);
    }

    private void initTitleBar() {
        TextView tvTitle = (TextView) findViewById(R.id.titlebar_tvTitle);
        tvTitle.setText(R.string.security_bingcard);

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
            case R.id.tv_set_pay_psw:
                Intent intent = new Intent(this, SetPswActivity1.class);
                intent.putExtra(PasswordExtra.EXTRA_PSW_TYPE, PasswordType.PAYMENT);
                startActivity(intent);
                break;
            case R.id.bingcard_save_btn:// 绑定银行卡
                //bindBank();
                newBindBank();
                break;
            case R.id.tv_bank:// 绑定银行卡(新接口)
                //弹出银行卡信息
                new MyTask(QUEST_TYPE.BANKLIST).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                break;
        }
    }

    /**
     * @description 绑定银行
     * @created 2014-9-5 下午5:05:38
     * @author ZZB
     */
    private void bindBank() {
        boolean validate = validateInput();
        if (validate) {
            if (mBankInfo.isBinded()) {// 已经绑定，修改银行绑定
                final PayPswDlgFragment f = PayPswDlgFragment.newInstance();
                f.setListener(new Listener() {
                    @Override
                    public void onPswValidated() {
                        new Task(Step.BIND_BANK).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    }
                });
                f.show(getSupportFragmentManager(), "PayPswDlgFragment");
            } else {// 未绑定，直接绑定银行
                new Task(Step.BIND_BANK).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }

        }

    }


    /**
     * @description 绑定银行
     * @created 2014-9-5 下午5:05:38
     * @author ZZB
     */
    private void newBindBank() {
        boolean validate = validateInput();
        if (validate) {
            if (mBankInfo.isBinded()) {// 已经绑定，修改银行绑定
                final PayPswDlgFragment f = PayPswDlgFragment.newInstance();
                f.setListener(new Listener() {
                    @Override
                    public void onPswValidated() {
                        new Task(Step.BIND_BANK).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    }
                });
                f.show(getSupportFragmentManager(), "PayPswDlgFragment");
            } else {// 未绑定，直接绑定银行
                new Task(Step.BIND_BANK).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }

        }

    }

    /**
     * @description 校验输入
     * @created 2014-9-5 下午5:05:46
     * @author ZZB
     */
    private boolean validateInput() {
        String cardNo = mEtCardNo.getText().toString();
        if (mBank == null) {
            ViewHub.showLongToast(mContext, "请选择银行");
            return false;
        } else if (!FunctionHelper.isBankCardNo(cardNo)) {
            ViewHub.setEditError(mEtCardNo, "请输入正确的银行卡号");
            return false;
        }
        return true;
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
    public void finish() {
        super.finish();
        ViewHub.hideKeyboard(this);
    }

    private class Task extends AsyncTask<Void, Void, Object> {

        private Step mStep;
        private LoadingDialog mDialog;

        private String bankCardId;

        public Task(Step step) {
            mStep = step;
            mDialog = new LoadingDialog(mContext);
        }

        @Override
        protected void onPreExecute() {
            switch (mStep) {
                case INIT_BANK:
                    mDialog.start("加载数据中...");
                    break;
                case BIND_BANK:
                    bankCardId = mEtCardNo.getText().toString();
                    mDialog.start("提交数据中...");
                    break;
            }
        }

        @Override
        protected Object doInBackground(Void... params) {
            try {
                switch (mStep) {
                    case INIT_BANK:
                        if (!TextUtils.isEmpty(mBankInfo.getBank())) {
                            mBank.setName(mBankInfo.getBank());
                        }
                        break;

                    case BIND_BANK:
                        Bank bindBank = new Bank();
                        bindBank.setParentName(mBank.getName());
                        bindBank.setCardNo(bankCardId);
                        AccountAPI.bindBank2(mContext, bindBank);
                        break;
                    default:
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
            if (mDialog.isShowing()) {
                mDialog.stop();
            }
            if (result instanceof String && ((String) result).startsWith("error:")) {
                ViewHub.showLongToast(mContext, ((String) result).replace("error:", ""));
            } else {
                switch (mStep) {
                    case INIT_BANK:
                        updateUI();
                        break;
                    case BIND_BANK:
                        ViewHub.showShortToast(mContext, "申请绑定银行成功");
                        mEventBus.post(BusEvent.getEvent(EventBusId.SECURITYSETTINGSACTIVITY_RESH));
                        finish();
//                        ViewHub.showOkDialog(mContext, "提示", "申请绑定银行成功", "OK",
//                                new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialog, int which) {
//                                        finish();
//                                    }
//                                });

                        break;
                }
            }

        }

    }


    private class MyTask extends AsyncTask<Void, Void, String> {
        private String json;
        private QUEST_TYPE type;

        public MyTask(QUEST_TYPE type) {
            this.type = type;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            switch (type) {
                case BANKLIST:
                    loadingDialog.start(getString(R.string.me_bink_list));
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
                                            tvBank.setText(mBank.getName());
                                        }
                                    }).create();
                            dialog.show();
                        }
                        break;
                }
            }
        }
    }
}
