package com.nahuo.quicksale;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.baidu.mobstat.StatService;
import com.nahuo.library.controls.LoadingDialog;
import com.nahuo.library.helper.FunctionHelper;
import com.nahuo.quicksale.PayPswDlgFragment.Listener;
import com.nahuo.quicksale.api.AccountAPI;
import com.nahuo.quicksale.common.Const.PasswordExtra;
import com.nahuo.quicksale.common.Const.PasswordType;
import com.nahuo.quicksale.common.Const;
import com.nahuo.quicksale.common.SpManager;
import com.nahuo.quicksale.db.BankDao;
import com.nahuo.quicksale.oldermodel.Bank;
import com.nahuo.quicksale.oldermodel.json.JAuthInfo;
import com.nahuo.quicksale.oldermodel.json.JBankInfo;
import com.nahuo.quicksale.provider.UserInfoProvider;

/**
 * @description 绑定银行卡
 * @created 2014-9-15 下午4:27:01
 * @author ZZB
 */
public class BindCardAcivity extends BaseSlideBackActivity implements OnClickListener {

    public static final String EXTRA_BANK_INFO = "EXTRA_BANK_INFO";
    private static final int REQUEST_SELECT_SUB_BANK = 1;
    private Context mContext = this;
    private EditText mEtCardNo;
    private TextView mEtSubBank, mTvSubBank, mTvParentBank, mTvCardNo, mTvState;
    private TextView mTvUserName, mTvTips;
    private Button mSubmitBtn;
    // 银行和支行缓存
    private Bank mParentBank, mSubBank;
    private Spinner mSpBank;
    private BankDao mDao;
    private List<Bank> mBanks;
    /** 是否绑定银行 */
    // private boolean mBinded;
    private JBankInfo mBankInfo;

    private static enum Step {
        INIT_BANK, BIND_BANK
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      //  requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);// 设置自定义标题栏
        setContentView(R.layout.activity_bindcard);
//        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
//                R.layout.layout_titlebar_default);// 更换自定义标题栏布局
        try {
            initView();
            initData();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
        case REQUEST_SELECT_SUB_BANK:
            if (data == null) {
                return;
            }
            mSubBank = (Bank) data.getSerializableExtra(SelectBankActivity.EXTRA_BANK);
            mEtSubBank.setText(mSubBank.getName());
            break;
        default:
            super.onActivityResult(requestCode, resultCode, data);
            break;
        }

    }

    private void initData() {
        mDao = new BankDao(this);
        mBankInfo = (JBankInfo) getIntent().getSerializableExtra(EXTRA_BANK_INFO);
        int userId = SpManager.getUserId(mContext);
        if (mBankInfo == null) {
//            mBankInfo = UserInfoProvider.getBankInfo(mContext, userId);
//            if (mBankInfo == null) {
//
//                mBankInfo = new JBankInfo();
//                mBankInfo.setStatu(Const.BankState.NOT_COMMIT);
//            }
        }

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
        // 标题栏
        initTitleBar();
        mSpBank = (Spinner) findViewById(R.id.sp_bank);
        mEtCardNo = (EditText) findViewById(R.id.et_bank_card_no);
        mEtSubBank = (TextView) findViewById(R.id.et_sub_bank);
        mTvUserName = (TextView) findViewById(R.id.tv_username);
        mSubmitBtn = (Button) findViewById(R.id.bingcard_save_btn);
        JAuthInfo authInfo = UserInfoProvider.getAuthInfo(mContext, SpManager.getUserId(mContext));
        if(authInfo != null) {
            mTvUserName.setText(authInfo.getRealName());
        }
        
        mTvParentBank = (TextView) findViewById(R.id.tv_parent_bank);
        mTvSubBank = (TextView) findViewById(R.id.tv_sub_bank);
        mTvCardNo = (TextView) findViewById(R.id.tv_card_no);
        
        mTvState = (TextView) findViewById(R.id.tv_state);
        mTvTips = (TextView) findViewById(R.id.tv_tips);
    }

    private void updateUI(){
        try {
            String bankState = mBankInfo.getStatu();
            mTvUserName.setText(mBankInfo.getRealName());
            mTvState.setText(bankState);
            if(Const.BankState.AUTH_PASSED.equals(bankState)){//验证通过
                changeView(true);
                mSubmitBtn.setText("修改");
                mEtSubBank.setText(mBankInfo.getSubBank());
                mEtCardNo.setText(mBankInfo.getCardNumber());

            }else if(Const.BankState.NOT_COMMIT.equals(bankState)){//未提交
                changeView(true);
            }else if(Const.BankState.CHECKING.equals(bankState)){//审核中
                changeView(false);
                mTvParentBank.setText(mBankInfo.getBank());
                mTvSubBank.setText(mBankInfo.getSubBank());
                mTvCardNo.setText(mBankInfo.getCardNumber());
            }else{//驳回或者重审
                changeView(true);
                mTvTips.setVisibility(View.VISIBLE);
                mTvTips.setText(Html.fromHtml("驳回原因：<font color='red'>" + mBankInfo.getMessage() + "</font>"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void changeView(boolean isEdit){
        //编辑可见
        int editVisible = isEdit ? View.VISIBLE : View.GONE;
        //编辑不可见
        int unEditVisible = isEdit ? View.GONE : View.VISIBLE;
        
        //编辑状态不可见
        mTvCardNo.setVisibility(unEditVisible);
        mTvSubBank.setVisibility(unEditVisible);
        mTvCardNo.setVisibility(unEditVisible);
        //编辑状态可见
        mSpBank.setVisibility(editVisible);
        mEtSubBank.setVisibility(editVisible);
        mEtCardNo.setVisibility(editVisible);
        mSubmitBtn.setVisibility(editVisible);
        
    }
    private void initSpinner() {
        ArrayAdapter<Bank> adapter = new ArrayAdapter<Bank>(this,
                android.R.layout.simple_spinner_item, mBanks);
        adapter.setDropDownViewResource(R.layout.drop_down_item);
        mSpBank.setAdapter(adapter);
        initSpinnerSelection();
        mSpBank.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mSubBank = null;
                mEtSubBank.setText("");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        mParentBank = (Bank) mSpBank.getSelectedItem();
    }

    private void initSpinnerSelection() {
        if (mParentBank != null) {
            int counter = 0;
            for (Bank bank : mBanks) {
                if (bank.getName().equals(mParentBank.getName())) {
                    break;
                }
                counter++;
            }
            mSpBank.setSelection(counter, true);//
        }
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
        case R.id.et_sub_bank:// 选择支行
            selectSubBank();
            break;
        case R.id.bingcard_save_btn:// 绑定银行卡
            bindBank();
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
     * @description 校验输入
     * @created 2014-9-5 下午5:05:46
     * @author ZZB
     */
    private boolean validateInput() {
        String cardNo = mEtCardNo.getText().toString();
        if (mParentBank == null) {
            ViewHub.showLongToast(mContext, "请选择银行");
            return false;
        }
//        else if (mSubBank == null) {
//            ViewHub.showLongToast(mContext, "请选择支行");
//            mEtCardNo.getText().toString();
//            return false;
//        }
        else if (!FunctionHelper.isBankCardNo(cardNo)) {
            ViewHub.setEditError(mEtCardNo, "请输入正确的银行卡号");
            return false;
        }
        return true;
    }

    /**
     * @description 选择支行
     * @created 2014-9-1 上午9:59:29
     * @author ZZB
     */
    private void selectSubBank() {
        Intent intent = new Intent(this, SelectBankActivity.class);
        mParentBank = (Bank) mSpBank.getSelectedItem();
        intent.putExtra(SelectBankActivity.EXTRA_BANK_PARENT_ID, mParentBank.getId());
        startActivityForResult(intent, REQUEST_SELECT_SUB_BANK);
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

        public Task(Step step) {
            mStep = step;
            mDialog = new LoadingDialog(mContext);
        }

        @Override
        protected void onPreExecute() {
            switch (mStep) {
            case INIT_BANK:
                mDialog.start("加载银行数据中...");
                break;
            case BIND_BANK:
                mDialog.start("提交数据中...");
                break;
            }
        }

        @Override
        protected Object doInBackground(Void... params) {
            try {
                switch (mStep) {
                case INIT_BANK:
                    mBanks = mDao.getBanks(0);
                    String bank = mBankInfo.getBank();
                    String subBank = mBankInfo.getSubBank();
                    mParentBank = mDao.getBankByName(bank);
                    mSubBank = mDao.getBankByName(subBank);
                    if(mSubBank == null) {
                        mSubBank = new Bank();
                        mSubBank.setName(subBank);
                    }
                    break;
                case BIND_BANK:
                    Bank bindBank = new Bank();
                    bindBank.setParentName(mParentBank.getName());
                    bindBank.setName(mSubBank.getName());
                    bindBank.setCardNo(mEtCardNo.getText().toString());
                    AccountAPI.bindBank(mContext, bindBank);
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
                    initSpinner();
                    updateUI();
                    break;
                case BIND_BANK:
                    ViewHub.showShortToast(mContext,"申请绑定银行成功");
                    finish();
//                    ViewHub.showOkDialog(mContext, "提示", "申请绑定银行成功", "OK",
//                            new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//                                    finish();
//                                }
//                            });

                    break;
                }
            }

        }

    }
}
