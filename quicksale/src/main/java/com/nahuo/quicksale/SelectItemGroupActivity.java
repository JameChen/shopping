package com.nahuo.quicksale;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mobstat.StatService;
import com.nahuo.library.controls.LoadingDialog;
import com.nahuo.quicksale.ViewHub.EditDialogListener;
import com.nahuo.quicksale.adapter.ItemVisibleRangeAdapter;
import com.nahuo.quicksale.api.AgentAPI;
import com.nahuo.quicksale.common.Const.SystemGroupId;
import com.nahuo.quicksale.common.SpManager;
import com.nahuo.quicksale.common.StringUtils;
import com.nahuo.quicksale.oldermodel.AgentGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Description:选择商品可视范围界面
 * 2014-7-7上午11:15:11
 */
public class SelectItemGroupActivity extends BaseSlideBackActivity implements View.OnClickListener {

    private Context mContext = this;
    private ExpandableListView mListView;
//    private SelectItemGroupAdapter mAdapter;
    private ItemVisibleRangeAdapter mAdapter;
//    private int mItemId;
    private LoadingDialog mLoadingDlg;
    public static final String KEY_RESULT_CODE = "KEY_RESULT_CODE";
    /**默认选中的分组ID, 用逗号隔开*/
    public static final String EXTRA_SELECTED_ITEM_IDS = "KEY_SELECTED_ITEMS";
    private String mSelectedItemIds = "";

    private static enum Step {
        LOAD_GROUPS, CREATE_GROUP;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      //  requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_select_visible_range);
        //getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.layout_titlebar_default);
//        mItemId = getIntent().getIntExtra("ITEM_ID", -1);
        initParams();
        initView();
    }

    private void initParams() {
        Intent intent = getIntent();
        mSelectedItemIds = intent.getStringExtra(EXTRA_SELECTED_ITEM_IDS);
    }

    private void initView() {
        mLoadingDlg = new LoadingDialog(this);
        initTitleBar();
        initListView();
    }

    private void initTitleBar() {
        TextView tvTitle = (TextView) findViewById(R.id.titlebar_tvTitle);

        Button btnLeft = (Button) findViewById(R.id.titlebar_btnLeft);
        tvTitle.setText(R.string.title_visible_range);
        btnLeft.setText(R.string.titlebar_btnBack);
        btnLeft.setVisibility(View.VISIBLE);
        btnLeft.setOnClickListener(this);

        Button btnRight = (Button) findViewById(R.id.titlebar_btnRight);
        btnRight.setVisibility(View.VISIBLE);
        btnRight.setText(R.string.btn_save);
        btnRight.setOnClickListener(this);
    }

    private void initListView() {
        mListView = (ExpandableListView) findViewById(R.id.lv_item_visible_range);
        mListView.setGroupIndicator(null);
//        mAdapter = new SelectItemGroupAdapter(this, mSelectedItemIds);
        mAdapter = new ItemVisibleRangeAdapter(mContext, mSelectedItemIds);
        
        mListView.setAdapter(mAdapter);
        asyncLoadListView();
    }

    private void asyncLoadListView() {
        new Task(Step.LOAD_GROUPS).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);;
    }

    private class Task extends AsyncTask<Object, Void, Object> {

        private Step mStep;

        public Task(Step step) {
            this.mStep = step;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            switch (mStep) {
            case LOAD_GROUPS:
                mLoadingDlg.setMessage("加载分组中");
                mLoadingDlg.start();
                break;
            case CREATE_GROUP:
                mLoadingDlg.setMessage("创建分组中");
                mLoadingDlg.start();
                break;
            default:
                showToast("on pre switch missed");
                break;

            }

        }

        @Override
        protected Object doInBackground(Object... params) {
            Object obj = null;
            try {
                switch (mStep) {
                case LOAD_GROUPS:
                    obj = AgentAPI.getGroups(mContext);
                    break;
                case CREATE_GROUP:
                    String groupName = (String) params[0];
                    int groupId = AgentAPI.addGroup(mContext, groupName);
                    AgentGroup group = new AgentGroup(groupId, groupName);
                    obj = group;
                    break;
                default:
                    showToast("do in bg switch missed");
                    break;
                }
            } catch (Exception e) {
                e.printStackTrace();
                return "error:" + e.getMessage();
            }

            return obj;
        }

        @Override
        protected void onPostExecute(Object result) {
            if (result instanceof String && ((String) result).startsWith("error:")) {
                showToast(((String) result).replace("error:", ""));
            } else {
                switch (mStep) {
                case LOAD_GROUPS:
                    List<AgentGroup> data = new ArrayList<AgentGroup>();
                    
                    
                    data.add(0, new AgentGroup(SystemGroupId.ALL_PPL, "公开"));
                    data.add(1, new AgentGroup(SystemGroupId.ALL_AGENT, "所有代理"));
                    AgentGroup childGroup = new AgentGroup();
                    childGroup.setName("部分代理可见");
                    childGroup.setSubGroups((List<AgentGroup>)result);
                    data.add(2, childGroup);
                        
                    mAdapter.setData(data);
                    mListView.expandGroup(2);
                    mAdapter.notifyDataSetChanged();
                    break;
                case CREATE_GROUP:
                    mAdapter.addChildItem((AgentGroup) result);
                    mAdapter.notifyDataSetChanged();
                    showToast("创建分组成功");
                    break;
                default:
                    showToast("on post switch missed");
                    break;
                }
            }
            if(mLoadingDlg.isShowing()){
                mLoadingDlg.stop();
            }
        }

        
    }


    private void onSaveClick() {
        List<AgentGroup> groups = mAdapter.getSelectedGroups();
        StringBuilder groupIds = new StringBuilder();
        StringBuilder groupNames = new StringBuilder();
        
        for (AgentGroup group : groups) {
            int groupId = group.getGroupId();
            String name = group.getName();
            //所有人不需要传id到服务端， TODO 如果是所有代理，另外处理
            if(groupId != SystemGroupId.ALL_AGENT || groupId != SystemGroupId.ALL_PPL){
                groupIds.append(groupId).append(",");
            }
            
            groupNames.append(name).append(",");
        }
        
        Intent resultIntent = new Intent();
        String strGroupIds = StringUtils.deleteEndStr(groupIds.toString(), ",");
        
//        if(strGroupIds.equals(SystemGroupId.ALL_AGENT + "")){//如果只选一个“所有代理”分组，不用传分组id
//            strGroupIds = "";
//        }
        resultIntent.putExtra("GROUP_IDS", strGroupIds);
        resultIntent.putExtra("GROUP_NAMES", groupNames.toString());
        setResult(Activity.RESULT_OK, resultIntent);

        SpManager.setUploadNewItemVisibleRanageIds(this, groupIds.toString());
        SpManager.setUploadNewItemVisibleRangeNames(this, groupNames.toString());

        finish();
    }
    /**
     *Description:创建新分组
     *2014-7-14下午4:58:51
     */
    private void createNewGroup() {
        ViewHub.showEditDialog(mContext, "创建分组", "", new EditDialogListener() {
            
            @Override
            public void onOkClick(DialogInterface dialog, EditText editText) {
                String groupName = editText.getText().toString();
                if (validateGroupName(groupName)) {
                    new Task(Step.CREATE_GROUP).execute(editText.getText().toString());
                }
            }

            @Override
            public void onOkClick(EditText editText) {

            }

            @Override
            public void onNegativecClick() {
            }
        });
    }
    /**
     * Description:校验分组名称
     * 2014-7-10下午5:09:20
     */
    private boolean validateGroupName(String groupName) {
        if (TextUtils.isEmpty(groupName)) {
            showToast("分组名称不能为空");
            return false;
        } else if (groupName.length() > 50) {
            showToast("分组名称长度不得超过50个字符");
            return false;
        } else {
            return true;
        }

    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.titlebar_btnLeft:
            finish();
            break;
        case R.id.titlebar_btnRight:
            // 保存
            onSaveClick();
            break;
        case R.id.create_group:
            createNewGroup();
            break;

        }
    }

    private void showToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
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
}
