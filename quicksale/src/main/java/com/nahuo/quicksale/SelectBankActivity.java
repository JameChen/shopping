package com.nahuo.quicksale;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;

import com.baidu.mobstat.StatService;
import com.nahuo.library.controls.LoadingDialog;
import com.nahuo.quicksale.ViewHub.EditDialogListener;
import com.nahuo.quicksale.adapter.SelectBankAdapter;
import com.nahuo.quicksale.adapter.SelectBankAdapter.SearchCallBack;
import com.nahuo.quicksale.db.BankDao;
import com.nahuo.quicksale.oldermodel.Bank;

import java.util.List;

/**
 * @description 选择银行
 * @created 2014-9-1 上午10:00:05
 * @author ZZB
 */
public class SelectBankActivity extends BaseActivity2 implements OnClickListener {

    public static final String EXTRA_BANK_PARENT_ID = "EXTRA_BANK_PARENT_ID";
    public static final String EXTRA_BANK = "EXTRA_BANK";
    private int mBankParentId;
    private ListView mListView;
    private EditText mEtSearch;
    private SelectBankAdapter mAdapter;
    private BankDao mDao;
    protected Context mContext = this;
    private LoadingDialog mDialog;
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_bank);
        mDao = new BankDao(this);
        initParams();
        initView();
    }

    private void initParams() {
        mBankParentId = getIntent().getIntExtra(EXTRA_BANK_PARENT_ID, -1);
    }

    private void initView() {
        mDialog = new LoadingDialog(this);
        mEtSearch = (EditText) findViewById(R.id.et_search);
        mEtSearch.requestFocus();
        mEtSearch.addTextChangedListener(new TextWatcher() {
            
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            
            @Override
            public void afterTextChanged(Editable s) {
                mAdapter.search(s.toString());
            }
        });
        initTitleBar();
        initListView();
    }

    private void initListView() {
        mListView = (ListView) findViewById(R.id.listview);
        mAdapter = new SelectBankAdapter(this);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bank bank = (Bank) mAdapter.getItem(position);
                onBankSelected(bank);
            }
        });
        mAdapter.setCallBack(new SearchCallBack() {
            
            @Override
            public void onFinish(int resultCount) {
                if(resultCount == 0){
                    ViewHub.setEmptyView(mContext, mListView, "没有搜索结果");
                }
            }
        });
        initData();
    }
    

    private void initData() {
        new AsyncTask<Void, Void, Object>() {

            
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                mDialog.start("加载数据中...");
            }

            @Override
            protected Object doInBackground(Void... params) {
                List<Bank> banks = mDao.getBanks(mBankParentId);
                return banks;
            }

            @Override
            protected void onPostExecute(Object result) {
                super.onPostExecute(result);
                List<Bank> banks = (List<Bank>) result;
                if(banks.size() == 0){
                    ViewHub.setEmptyView(mContext , mListView, "该银行没有支行");
                }
                mAdapter.setData(banks);
                mAdapter.notifyDataSetChanged();
                if(mDialog.isShowing()){
                    mDialog.stop();
                }
            }
            
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private void initTitleBar() {
        setTitle("选择支行");
        setRightText("添加");
        setRightClickListener(this);
    }

    @Override
    public void finish() {
        super.finish();
        ViewHub.hideKeyboard(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.tvTRight:
            ViewHub.showEditDialog(mContext, "请输入支行名称", "", new EditDialogListener() {
                @Override
                public void onOkClick(DialogInterface dialog, EditText editText) {
                    String bankName = editText.getText().toString();
                    if(TextUtils.isEmpty(bankName)) {
                        ViewHub.setDialogDismissable(dialog, false);
                        ViewHub.setEditError(editText, "请输入支行名称");
                    }else {
                        ViewHub.setDialogDismissable(dialog, true);
                        Bank bank = new Bank();
                        bank.setName(bankName);
                        onBankSelected(bank);
                    }
                }

                @Override
                public void onOkClick(EditText editText) {

                }

                @Override
                public void onNegativecClick() {
                }
            });
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

    private void onBankSelected(Bank bank) {
        Intent intent = new Intent();
        intent.putExtra(EXTRA_BANK, bank);
        setResult(RESULT_OK, intent);
        finish();
    }
}
