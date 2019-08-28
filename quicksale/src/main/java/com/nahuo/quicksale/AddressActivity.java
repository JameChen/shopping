package com.nahuo.quicksale;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.baidu.mobstat.StatService;
import com.nahuo.library.controls.LoadingDialog;
import com.nahuo.quicksale.adapter.AddressAdapter;
import com.nahuo.quicksale.api.ShopSetAPI;
import com.nahuo.quicksale.oldermodel.Address;

import java.util.List;

/*
 * 收件人页面
 */
@Deprecated
public class AddressActivity extends BaseSlideBackActivity implements OnClickListener {

    public static final String  EXTRA_ADDRESSES      = "EXTRA_ADDRESSES";
    // public static final String EXTRA_ADDRESS = "EXTRA_ADDRESS";
    private static final String EMPTY_TEXT           = "您还未设置收件地址 :)";
    private static final int    REQUEST_ADD_ADDRESS  = 1;
    private static final int    REQUEST_EDIT_ADDRESS = 2;
    private Context             mContext             = this;
    private ListView            mListView;
    private AddressAdapter      mAdapter;
    private LoadingDialog       mDialog;
    /** 标志是否没有地址，如果没有地址，则添加的地址为默认地址 */
    private boolean             mEmpty;
    private int                 clickPosition        = -1;

    @Override
    
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);// 设置自定义标题栏
        setContentView(R.layout.activity_address);
       // getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.layout_titlebar_default);// 更换自定义标题栏布局
        mDialog = new LoadingDialog(this);
        initView();
        initData();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_ADD_ADDRESS:
                if (resultCode == HandleAddressActivity.RESULT_SAVE) {// 增加地址
                    Address add = (Address)data.getSerializableExtra(HandleAddressActivity.EXTRA_ADDRESS);
                    mAdapter.add(add);
                } else if (resultCode == HandleAddressActivity.RESULT_DEFAULT) {// 增加并设为默认地址
                    Address add = (Address)data.getSerializableExtra(HandleAddressActivity.EXTRA_ADDRESS);
                    mAdapter.add(add);
                    mAdapter.setDefault(add);
                    mListView.setSelection(mAdapter.getSelectedPos());
                }
                break;
            case REQUEST_EDIT_ADDRESS:
                if (resultCode == HandleAddressActivity.RESULT_SAVE) {// 修改地址
                    Address add = (Address)data.getSerializableExtra(HandleAddressActivity.EXTRA_ADDRESS);
                    mAdapter.update(add);
                } else if (resultCode == HandleAddressActivity.RESULT_DELETE) {// 删除地址
                    Address add = (Address)data.getSerializableExtra(HandleAddressActivity.EXTRA_ADDRESS);
                    mAdapter.remove(add.getId());
                } else if (resultCode == HandleAddressActivity.RESULT_DEFAULT) {// 设为默认
                    Address add = (Address)data.getSerializableExtra(HandleAddressActivity.EXTRA_ADDRESS);
                    mAdapter.setDefault(add);
                }
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
                break;
        }

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
        getMenuInflater().inflate(R.menu.menu_handle_address, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterContextMenuInfo info = (AdapterContextMenuInfo)item.getMenuInfo();
        int pos = -1;
        if (info == null) {
            pos = clickPosition;
        } else {
            pos = info.position;
        }
        Address add = (Address)mAdapter.getItem(pos);
        switch (item.getItemId()) {
            case R.id.menu_item_edit:// 更新地址
                Intent intent = new Intent(this, HandleAddressActivity.class);
                intent.putExtra(HandleAddressActivity.EXTRA_HANDLE_TYPE, HandleAddressActivity.TYPE_EDIT_ADDRESS);
                intent.putExtra(HandleAddressActivity.EXTRA_ADDRESS, add);
                startActivityForResult(intent, REQUEST_EDIT_ADDRESS);
                return true;
            case R.id.menu_item_delete:// 删除地址
                new Task(Step.DELETE_ADDRESS).execute(add);
                return true;
            case R.id.menu_item_default:// 设为默认地址
                new Task(Step.SET_DEFAULT).execute(add);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    public void finish() {
        setResult(RESULT_OK);
        super.finish();
    }

    private void initView() {
        initTitleBar();
        mListView = (ListView)findViewById(R.id.lv_address);
        mAdapter = new AddressAdapter(this);
        mListView.setAdapter(mAdapter);
        registerForContextMenu(mListView);
        mListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mAdapter.setSelectedPos(position);
                mAdapter.notifyDataSetChanged();

                clickPosition = position;
                mListView.showContextMenu();
            }
        });

    }

    private void initTitleBar() {

        TextView tvTitle = (TextView)findViewById(R.id.titlebar_tvTitle);
        tvTitle.setText("收件地址");

        Button btnLeft = (Button)findViewById(R.id.titlebar_btnLeft);
        btnLeft.setText(R.string.titlebar_btnBack);
        btnLeft.setVisibility(View.VISIBLE);
        btnLeft.setOnClickListener(this);

        Button btnRight = (Button)findViewById(R.id.titlebar_btnRight);
        btnRight.setText("添加");
        btnRight.setVisibility(View.VISIBLE);
        btnRight.setOnClickListener(this);

    }

    private void initData() {
        new Task(Step.LOAD_ADDRESS).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.titlebar_btnLeft:
                finish();
                break;
            case R.id.titlebar_btnRight:// 添加收件地址
                Intent intent = new Intent(this, HandleAddressActivity.class);
                intent.putExtra(HandleAddressActivity.EXTRA_HANDLE_TYPE, HandleAddressActivity.TYPE_ADD_ADDRESS);
                // 标志是否没有地址，如果没有地址，则添加的地址为默认地址
                intent.putExtra(HandleAddressActivity.EXTRA_EMPTY_ADDRESS, mEmpty);
                startActivityForResult(intent, REQUEST_ADD_ADDRESS);
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

    private enum Step {
        LOAD_ADDRESS, DELETE_ADDRESS, SET_DEFAULT
    }

    private void updateAdapter(List<Address> adds) {
        if (adds.size() == 0) {
            ViewHub.setEmptyView(mContext, mListView, EMPTY_TEXT);
            mEmpty = true;
        } else {
            mEmpty = false;
        }
        mAdapter.setData(adds);
        mAdapter.notifyDataSetChanged();
    }

    private class Task extends AsyncTask<Object, Void, Object> {
        private Step mStep;

        public Task(Step step) {
            mStep = step;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            switch (mStep) {
                case LOAD_ADDRESS:
                    mDialog.start("加载地址中...");
                    break;
                case DELETE_ADDRESS:
                    mDialog.start("删除地址中...");
                    break;
                case SET_DEFAULT:
                    mDialog.start("设置默认地址中...");
                    break;
            }

        }

        @Override
        protected Object doInBackground(Object... params) {
            try {
                switch (mStep) {
                    case LOAD_ADDRESS:
                        List<Address> adds = ShopSetAPI.getAddresses(mContext);
                        return adds;
                    case DELETE_ADDRESS:
                        Address delAdd = (Address)params[0];
                        ShopSetAPI.deleteAddress(mContext, delAdd);
                        return delAdd.getId();
                    case SET_DEFAULT:
                        Address address = (Address)params[0];
                        address.setDefault(true);
                        ShopSetAPI.updateAddress(mContext, address);
                        return address;
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
            if (result instanceof String && ((String)result).startsWith("error:")) {
                ViewHub.showLongToast(mContext, ((String)result).replace("error:", ""));
                ViewHub.setEmptyView(mContext, mListView, "不好意思, 出错鸟~~~");
            } else {
                switch (mStep) {
                    case LOAD_ADDRESS:
                        @SuppressWarnings("unchecked")
                        List<Address> adds = (List<Address>)result;
                        updateAdapter(adds);
                        break;
                    case DELETE_ADDRESS:
                        mAdapter.remove((Integer)result);
                        mAdapter.notifyDataSetChanged();
                        ViewHub.showLongToast(mContext, "删除地址成功");
                        if (mAdapter.getCount() == 0) {
                            ViewHub.setEmptyView(mContext, mListView, EMPTY_TEXT);
                        }
                        break;
                    case SET_DEFAULT:
                        mAdapter.setDefault((Address)result);
                        mAdapter.notifyDataSetChanged();
                        break;
                }

            }

        }

    }
}
