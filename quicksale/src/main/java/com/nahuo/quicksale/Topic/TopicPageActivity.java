package com.nahuo.quicksale.Topic;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.luck.picture.lib.tools.ScreenUtils;
import com.nahuo.constant.UmengClick;
import com.nahuo.library.controls.LightAlertDialog;
import com.nahuo.library.controls.LoadingDialog;
import com.nahuo.library.controls.pulltorefresh.PullToRefreshListView;
import com.nahuo.library.helper.FunctionHelper;
import com.nahuo.library.helper.GsonHelper;
import com.nahuo.library.helper.ImageUrlExtends;
import com.nahuo.quicksale.BaseXActivity;
import com.nahuo.quicksale.R;
import com.nahuo.quicksale.ViewHub;
import com.nahuo.quicksale.activity.MainNewActivity;
import com.nahuo.quicksale.adapter.YePinAdapter;
import com.nahuo.quicksale.api.GroupAPI;
import com.nahuo.quicksale.api.HttpUtils;
import com.nahuo.quicksale.api.TopicAPI;
import com.nahuo.quicksale.app.BWApplication;
import com.nahuo.quicksale.common.CacheDirUtil;
import com.nahuo.quicksale.common.Const;
import com.nahuo.quicksale.common.DialogUtils;
import com.nahuo.quicksale.common.ListUtils;
import com.nahuo.quicksale.common.SpManager;
import com.nahuo.quicksale.common.Utils;
import com.nahuo.quicksale.common.WSRuleHelper;
import com.nahuo.quicksale.di.module.HttpManager;
import com.nahuo.quicksale.eventbus.BusEvent;
import com.nahuo.quicksale.eventbus.EventBusId;
import com.nahuo.quicksale.model.http.CommonSubscriber;
import com.nahuo.quicksale.model.http.api.PinHuoApi;
import com.nahuo.quicksale.model.http.response.PinHuoResponse;
import com.nahuo.quicksale.oldermodel.ActivityInfoModel;
import com.nahuo.quicksale.oldermodel.AgentGroup;
import com.nahuo.quicksale.oldermodel.PublicData;
import com.nahuo.quicksale.oldermodel.TopicInfoModel;
import com.nahuo.quicksale.tab.Constant;
import com.nahuo.quicksale.util.RxUtil;
import com.nahuo.quicksale.util.UMengTestUtls;
import com.scwang.smartrefresh.header.BezierCircleHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.greenrobot.event.EventBus;

import static com.nahuo.quicksale.R.id.id_add_topicBtn;

/**
 * 小组页面
 *
 * @author nahuo9
 */
public class TopicPageActivity extends BaseXActivity implements View.OnClickListener, PullToRefreshListView.OnLoadMoreListener, PullToRefreshListView.OnRefreshListener {

    private static final String TAG = TopicPageActivity.class.getSimpleName();
    private Activity vThis;
    private LoadingDialog mloadingDialog;
    private int mPageIndex = 1;
    private int mPageSize = 20;
    private Button mAddTopicBtn;                                  // 添加话题和活动按钮
    private int groupID;
    private TextView tvTitle, tvTopicName, tvDetailCount;
    private ImageView imgLogo;
    private YePinAdapter adapter;
    private List<TopicInfoModel> itemList = new ArrayList<TopicInfoModel>();
    private AgentGroup groupModel;
    private TextView btnAddMember;
    private ImageView addTopicBtn;
    private Button btnCancel, btnAddActivity, btnAddTopic;
    private ListView mToptopiclist;
    private List<TopicInfoModel> topgrouplist = new ArrayList<TopicInfoModel>();
    private Boolean isInTeam = null;                           // 是否加入小组
    private boolean isLastInTeam;
    private RecyclerView mRefreshListView;
    private Dialog writeTopicDialog;
    private EventBus mEventBus = EventBus.getDefault();
    private View mContentView, iv_scroll_to_top;
    private SmartRefreshLayout refresh_layout;
    private BezierCircleHeader mRefreshHeader;
    private View empty_view;
    private boolean isReFresh = true;

    @Override
    public void onLoadMore() {
        bindItemData(false);
    }

    @Override
    public void onRefresh() {
        bindItemData(true);
    }

    private enum step {
        loaddata, loadtopic
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yepin);
        mContentView = findViewById(R.id.content);
        vThis = this;
        groupID = getIntent().getIntExtra("gid", 0);
        mloadingDialog = new LoadingDialog(vThis);
        mEventBus.registerSticky(vThis);
        initView();
        loadData();
        loadDataList();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //mEventBus.unregister(this);
        adapter = null;
        if (itemList != null) {
            itemList.clear();
            itemList = null;
        }
        groupModel = null;
        if (writeTopicDialog != null) {
            if (writeTopicDialog.isShowing())
                writeTopicDialog.dismiss();
            writeTopicDialog = null;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        MainNewActivity.currFragTag = Constant.FRAGMENT_FLAG_YUE_PIN;
    }

    private void initView() {
        LayoutInflater lif = (LayoutInflater) vThis.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        empty_view = lif.inflate(R.layout.public_empty_view, null);
        TextView btn_reload = (TextView) empty_view.findViewById(R.id.btn_reload);
        btn_reload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // EventBus.getDefault().post(BusEvent.getEvent(EventBusId.REFRESH_PIN_HUO));
                if (refresh_layout != null)
                    refresh_layout.autoRefresh();
            }
        });
        // 标题
        tvTitle = (TextView) mContentView.findViewById(R.id.tv_title);
        tvTitle.setText("天天拼货团");
        //  setTitleTap(findViewById(R.id.title));
        // setListViewTouchListener();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ScreenUtils.getStatusBarHeight(vThis));
            mContentView.findViewById(R.id.layout_tittle_status).setLayoutParams(params);
            mContentView.findViewById(R.id.layout_tittle_status).setVisibility(View.VISIBLE);
        }
        Button backBtn = (Button) mContentView.findViewById(R.id.titlebar_btnLeft);
        backBtn.setVisibility(View.VISIBLE);
//        if (groupID == 60033) {
//            backBtn.setVisibility(View.GONE);
//        } else {
//            backBtn.setOnClickListener(this);
//        }
        backBtn.setOnClickListener(this);
        addTopicBtn = (ImageView) mContentView.findViewById(R.id.titlebar_icon_right);
        iv_scroll_to_top = mContentView.findViewById(R.id.iv_scroll_to_top);
        iv_scroll_to_top.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mRefreshListView != null) {
                    mRefreshListView.scrollToPosition(0);
//                    mRefreshListView.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_CANCEL, 0, 0, 0));
//                    mRefreshListView.setSelection(0);
                }
            }
        });
//        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) addTopicBtn.getLayoutParams();
//        params.width = params.height = FunctionHelper.dip2px(getResources(), 30);
//        addTopicBtn.setLayoutParams(params);
        addTopicBtn.setImageResource(R.drawable.yepin_edit);
        addTopicBtn.setOnClickListener(this);
        addTopicBtn.setVisibility(View.VISIBLE);

        View headview = LayoutInflater.from(vThis).inflate(R.layout.layout_topic_page_head, null);
        mToptopiclist = (ListView) headview.findViewById(R.id.topic_top_list);
        btnAddMember = (TextView) headview.findViewById(R.id.id_add_topicBtn);
        btnAddMember.setOnClickListener(this);
        tvTopicName = (TextView) headview.findViewById(R.id.tv_groupname);
        tvDetailCount = (TextView) headview.findViewById(R.id.tv_groupCount);
        imgLogo = (ImageView) headview.findViewById(R.id.iv_group_logo);
        refresh_layout = (SmartRefreshLayout) mContentView.findViewById(R.id.refresh_layout);
        mRefreshHeader = (BezierCircleHeader) mContentView.findViewById(R.id.header);
        refresh_layout.setPrimaryColorsId(R.color.my_colorPrimary, android.R.color.white);
        //mRefreshHeader.setEnableHorizontalDrag(true);
        refresh_layout.setEnableHeaderTranslationContent(true);
        refresh_layout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                isReFresh = true;
                refresh_layout.setEnableLoadMore(true);
                loadData();
                bindItemData(isReFresh);
            }
        });
        refresh_layout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                isReFresh = false;
                bindItemData(isReFresh);
            }
        });
        refresh_layout.setEnableLoadMore(true);
        mRefreshListView = (RecyclerView) mContentView.findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(vThis);
        mRefreshListView.setLayoutManager(layoutManager);
        adapter = new YePinAdapter(vThis, R.layout.item_topic_page, itemList);
        mRefreshListView.setAdapter(adapter);
        // mRefreshListView.addHeaderView(headview);
        mRefreshListView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (mRefreshListView != null) {
                    int scollPos = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition();
                    if (scollPos > 4) {
                        if (iv_scroll_to_top != null)
                            iv_scroll_to_top.setVisibility(View.VISIBLE);
                    } else {
                        if (iv_scroll_to_top != null)
                            iv_scroll_to_top.setVisibility(View.GONE);
                    }
                }
            }
        });
//        mRefreshListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                try {
////                    if (!SpManager.getIs_Login(TopicPageActivity.this)) {
////                        Utils.gotoLoginActivity(TopicPageActivity.this);
////                    } else {
//                    TopicPageAdapter.ViewHolder holder = (TopicPageAdapter.ViewHolder) view.getTag();
//                    TopicInfoModel model = adapter.getItem(holder.position);
//                    if (holder.position == adapter.getCount() - 1) {
//
//                    } else {
//                        loadNeatDetailAhead(adapter.getItem(holder.position + 1));
//                    }
//                    // 进入活动详情
//                    Intent intent = new Intent(vThis, PostDetailActivity.class);
//                    intent.putExtra(PostDetailActivity.EXTRA_TID, model.getID());
//                    intent.putExtra(PostDetailActivity.EXTRA_LOGO_URL, Const.getShopLogo(model.getUserID()));
//                    intent.putExtra(PostDetailActivity.EXTRA_POST_TITLE, model.getTitle());
//                    intent.putExtra(PostDetailActivity.EXTRA_POST_TYPE, model.getType() == 0 ? Const.PostType.TOPIC
//                            : Const.PostType.ACTIVITY);
//                    startActivity(intent);
//                    // }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        });
    }

    /**
     * 弹出或关闭PopupWindow
     * <p>
     * 当前被点击的控件
     */
    private void togglePopupWindow() {
        if (writeTopicDialog == null) {
            writeTopicDialog = new Dialog(vThis, R.style.dialog);
            View addPhotoPwView = LayoutInflater.from(vThis).inflate(R.layout.layout_add_topic_or_activity, null);
            btnCancel = (Button) addPhotoPwView.findViewById(R.id.btnCancel);
            btnCancel.setOnClickListener(this);
            btnAddActivity = (Button) addPhotoPwView.findViewById(R.id.add_activity);
            btnAddActivity.setOnClickListener(this);
            btnAddTopic = (Button) addPhotoPwView.findViewById(R.id.add_topic);
            btnAddTopic.setOnClickListener(this);
            writeTopicDialog.setContentView(addPhotoPwView);
            WindowManager.LayoutParams params = writeTopicDialog.getWindow().getAttributes();
            params.height = getResources().getDisplayMetrics().widthPixels;
            params.gravity = Gravity.BOTTOM;
            params.windowAnimations = R.style.PopupBottomAnimation;
            writeTopicDialog.onWindowAttributesChanged(params);
            boolean hasActivity = groupModel.getHasPlateWithEnum(AgentGroup.PlateType.活动);
            boolean hasTopic = groupModel.getHasPlateWithEnum(AgentGroup.PlateType.话题);
            if (!hasActivity) {
                btnAddActivity.setVisibility(View.GONE);
            }
            if (!hasTopic) {
                btnAddTopic.setVisibility(View.GONE);
            }
        }
        if (writeTopicDialog.isShowing())
            writeTopicDialog.dismiss();
        else
            writeTopicDialog.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.titlebar_btnLeft:
//                finishJob();
               vThis.finish();
                break;
            case id_add_topicBtn:
                UMengTestUtls.UmengOnClickEvent(vThis, UmengClick.Click32);
                joinGroup();
                break;
            case R.id.titlebar_icon_right:
                try {
                    if (SpManager.getIs_Login(vThis)) {
//                        if (!WSRuleHelper.doRule(vThis, groupModel.getCanPostActivity(), groupModel.getGroupID(), "加入",
//                                new View.OnClickListener() {
//                                    @Override
//                                    public void onClick(View v) {
//                                        Dialog dialog = (Dialog) v.getTag();
//                                        dialog.dismiss();
//                                        if (v.getId() == R.id.dialog_sure) {
//                                            joinGroup();
//                                        }
//                                    }
//                                })) {
//                            return;
//                        }
                        // 发布活动/话题
                        togglePopupWindow();
                    } else {
                        Utils.gotoLoginActivity(vThis);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.add_activity:
//                if (!WSRuleHelper.doRule(vThis, groupModel.getCanPostActivity(), groupModel.getGroupID())) {
//                    return;
//                }
                // 发布活动
                Intent intentActivity = new Intent(vThis, SubmitActivityAndTopicActivity.class);
                intentActivity.putExtra("groupID", groupID);
                intentActivity.putExtra("isedit", false);
                intentActivity.putExtra("activity", (Serializable) (new ActivityInfoModel()));
                startActivity(intentActivity);
                togglePopupWindow();
                break;
            case R.id.add_topic:
//                if (!WSRuleHelper.doRule(vThis, groupModel.getCanPostTopic(), groupModel.getGroupID())) {
//                    return;
//                }
                // 发布话题
                Intent intentTopic = new Intent(vThis, SubmitActivityAndTopicActivity.class);
                intentTopic.putExtra("groupID", groupID);
                intentTopic.putExtra("isedit", false);
                intentTopic.putExtra("topic", (Serializable) (new TopicInfoModel()));
                startActivity(intentTopic);
                togglePopupWindow();
                break;
            case R.id.btnCancel:
                togglePopupWindow();
                break;
        }
    }

    public void onEventMainThread(BusEvent event) {
        switch (event.id) {
            case EventBusId.REFRESH_GROUP_DETAIL:
                if (this != null && vThis != null) {
                    loadData();
                    loadDataList();
                }
                break;
            case EventBusId.REFRESH_GROUP_DETAIL_NEW:
//                loadData();
//                loadDataList();
                if (this != null && vThis != null) {
                    if (!SpManager.getIs_Login(vThis)) {
                        if (btnAddMember != null) {
                            btnAddMember.setText("加入");
                            btnAddMember.setBackgroundResource(R.drawable.ye_pin_red_corner);
                            btnAddMember.setVisibility(View.VISIBLE);
                        }
                    } else {
                        loadData();
                        loadDataList();
                    }
                }
                break;

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == Activity.RESULT_OK) {
            joinGroup();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void joinGroup() {
        if (!SpManager.getIs_Login(vThis)) {
            Utils.gotoLoginActivity(vThis);
            return;
        }
        if (groupModel == null) {
            ViewHub.showLongToast(vThis, "页面加载异常，请重新进入");
            return;
        }
        if (!WSRuleHelper.doRule(vThis, groupModel.getCanJoin(), groupModel.getGroupID())) {
            return;
        }
        if (groupModel.isIsMember()) {// 退出小组
            DialogUtils.showSureCancelDialog(vThis, null, "确认退出小组？", new View.OnClickListener() {
                public void onClick(View v) {
                    DialogUtils.dismissDiaog(v);
                    exitGroupTask egt = new exitGroupTask();
                    egt.execute((Void) null);
                }
            });
        } else {
            switch (groupModel.getJoinSetting().getType()) {
                case 4:// 拒绝加入
//                    ViewHub.showOkDialog(vThis , null , groupModel.getJoinSetting().getTips(), "好的");
                    DialogUtils.showSureCancelDialog(vThis, null, groupModel.getJoinSetting().getTips(), "好的", null);
                    break;
                case 1:// 审核加入
                case 2:// 暗号加入
                    LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1);

                    AlertDialog.Builder builder = LightAlertDialog.Builder.create(vThis);
                    final LinearLayout ll = new LinearLayout(vThis);
                    ll.setOrientation(LinearLayout.VERTICAL);
                    final TextView tv = new TextView(vThis);
                    if (groupModel.getJoinSetting().getType() == 2) {
                        tv.setText(groupModel.getJoinSetting().getQuestion());
                    } else {
                        tv.setText(groupModel.getJoinSetting().getTips());
                    }
                    int margin = getResources().getDimensionPixelSize(R.dimen.margin_10);
                    param.setMargins(margin, margin, margin, margin);
                    tv.setLayoutParams(param);
                    ll.addView(tv);
                    final EditText et = new EditText(vThis);
                    if (groupModel.getJoinSetting().getType() == 2) {
                        et.setHint("请输入暗号");
                    } else {
                        et.setHint("请输入文字");
                    }
                    et.setBackgroundResource(R.drawable.draw_input_bg);
                    ViewHub.editTextRequestKeyboard(vThis, et);
                    et.setLayoutParams(param);
                    ll.addView(et);
                    builder.setView(ll).setNegativeButton("取消", null)
                            .setPositiveButton("加入", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (groupModel.getJoinSetting().getType() == 2) {
                                        if (et.getText().toString().equals(groupModel.getJoinSetting().getAnswer())) {// 加入小组
                                            JoinGroupTask jgt = new JoinGroupTask("");
                                            jgt.execute((Void) null);
                                        } else {// 暗号错了
                                            ViewHub.showLongToast(vThis, "暗号错误");
                                        }
                                    } else {
                                        JoinGroupTask jgt = new JoinGroupTask(et.getText().toString());
                                        jgt.execute((Void) null);
                                    }
                                }
                            });
                    builder.show();
                    break;
                case 3:// 申请即加入
                    //   Logger.e("join group ..." + System.currentTimeMillis());
                    JoinGroupTask jgt = new JoinGroupTask("");
                    jgt.execute((Void) null);
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * 获取小组信息
     */
    private void loadData() {
        if (SpManager.getIs_Login(vThis)) {
            getGroup();
        }
      /*  LoadDataTask loadDataTask = new LoadDataTask(step.loaddata);
        loadDataTask.execute((Void) null);*/

//        LoadDataTask loadDataTask1 = new LoadDataTask(step.loadtopic);
//        loadDataTask1.execute((Void) null);

    }

    /**
     * 获取小组帖子信息
     */
    private void loadDataList() {
        getList_v2();
//        LoadListDataTask loadDataTask = new LoadListDataTask(true);
//        loadDataTask.execute((Void) null);
    }

    private void getGroup() {
//        boolean isShowDialog = false;
//        if (!isHidden() && mloadingDialog != null)
//            isShowDialog = true;
        PinHuoApi pinHuoApi = HttpManager.getInstance().getPinHuoNoCacheApi(TAG);
        addSubscribe(pinHuoApi.getGroup(groupID)
                .compose(RxUtil.<PinHuoResponse<AgentGroup>>rxSchedulerHelper())
                .compose(RxUtil.<AgentGroup>handleResult())
                .subscribeWith(new CommonSubscriber<AgentGroup>(vThis, false, R.string.loading) {
                    @Override
                    public void onNext(AgentGroup bean) {
                        super.onNext(bean);
                        if (bean != null) {
                            groupModel = bean;
                            isLastInTeam = groupModel.isIsMember();
                            if (tvTitle != null)
                                tvTitle.setText(groupModel.getName());
                            if (!isLastInTeam) {
                                joinYePinGroup();
                            }
                        }
                    }

                }));
    }

    private void joinYePinGroup() {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("gid", groupID);
        params.put("content", "");
        PinHuoApi pinHuoApi = HttpManager.getInstance().getPinHuoNoCacheApi(TAG);
        addSubscribe(pinHuoApi.joinGroup(params)
                .compose(RxUtil.<PinHuoResponse<Object>>rxSchedulerHelper())
                .compose(RxUtil.<Object>handleResult()).subscribeWith(new CommonSubscriber<Object>(vThis) {
                    @Override
                    public void onNext(Object object) {
                        super.onNext(object);

                    }
                })
        );
    }

    private void getList_v2() {
        boolean isShowDialog = false;
        if (vThis != null)
            isShowDialog = true;
        PinHuoApi pinHuoApi = HttpManager.getInstance().getPinHuoNetCacheApi(TAG);
        addSubscribe(pinHuoApi.getList_v2(groupID, mPageIndex, mPageSize)
                .compose(RxUtil.<PinHuoResponse<List<TopicInfoModel>>>rxSchedulerHelper())
                .compose(RxUtil.<List<TopicInfoModel>>handleResult())
                .subscribeWith(new CommonSubscriber<List<TopicInfoModel>>(vThis, isShowDialog, R.string.loading) {
                    @Override
                    public void onNext(List<TopicInfoModel> list) {
                        super.onNext(list);
                        if (isReFresh) {
                            itemList.clear();
                            if (!ListUtils.isEmpty(list)) {
                                itemList.addAll(list);
                            }

                        } else {
                            if (ListUtils.isEmpty(list)) {
                                if (refresh_layout != null)
                                    refresh_layout.setEnableLoadMore(false);
                            } else {
                                itemList.addAll(list);
                            }
                        }
                        if (adapter != null)
                            adapter.notifyDataSetChanged();
                        setRefreshFalse();
                        judeEmpyView();
                        //    closeDialog(mLoadingDialog);
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        setRefreshFalse();
                        judeEmpyView();
                    }

                    @Override
                    public void onComplete() {
                        super.onComplete();
                        setRefreshFalse();
                        judeEmpyView();
                    }
                }));
    }

    private void judeEmpyView() {
        if (isReFresh) {
            if (ListUtils.isEmpty(itemList)) {
                setEmptyView(true);
            } else {
                setEmptyView(false);
            }
        }
    }

    private void setRefreshFalse() {
        if (isReFresh) {
            if (refresh_layout != null)
                refresh_layout.finishRefresh();
        } else {
            if (refresh_layout != null)
                refresh_layout.finishLoadMore();
        }
    }

    public void setEmptyView(boolean isShow) {

        if (isShow) {
            if (adapter != null) {
                adapter.setEmptyView(empty_view);
            }
            if (empty_view != null)
                empty_view.setVisibility(View.VISIBLE);
        } else {
            if (empty_view != null)
                empty_view.setVisibility(View.GONE);
        }
    }

    /***
     * 预加载下一个帖子 create by 陈智勇Mar 23, 2015-4:43:15 PM
     *
     * @param model
     */
    private void loadNeatDetailAhead(final TopicInfoModel model) {
        new Thread() {
            public void run() {
                try {
                    Thread.sleep(4000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Const.PostType type = model.getType() == 0 ? Const.PostType.TOPIC : Const.PostType.ACTIVITY;
                String name = "LOAD_POST_DETAIL" + "_" + type.toString() + "_" + model.getID();
                File cacheDir = CacheDirUtil.getCache(vThis, name);
                if (!cacheDir.exists()) {
                    String cookie = SpManager.getCookie(BWApplication.getInstance());
                    if (model.getType() == 0) {
                        String json = null;
                        try {
                            json = HttpUtils.httpPost("xiaozu/topic/" + model.getID(),
                                    new HashMap<String, String>(), PublicData.getCookie(BWApplication.getInstance()));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        if (json != null) {
                            CacheDirUtil.saveString(cacheDir, json);
                        }
                    } else {
                        String json = null;
                        try {

                            json = HttpUtils.httpPost("xiaozu/activity/" + model.getID(),
                                    new HashMap<String, String>(), PublicData.getCookie(BWApplication.getInstance()));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        if (json != null) {
                            CacheDirUtil.saveString(cacheDir, json);
                        }

                    }
                }
            }
        }.start();
    }

    public void finishJob() {
        if (isInTeam != null && isLastInTeam != isInTeam) {
            Intent data = new Intent();
            data.putExtra("gid", groupID);
            vThis.setResult(Activity.RESULT_OK, data);
        }
    }

//    @Override
//    public void onBackPressed() {
//        finishJob();
//        super.onBackPressed();
//    }

    public class LoadDataTask extends AsyncTask<Void, Integer, String> {
        private step mstep;

        public LoadDataTask(step step) {
            mstep = step;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            switch (mstep) {
                case loaddata:
                    if (vThis!=null && mloadingDialog != null)
                        mloadingDialog.start(getString(R.string.items_loadData_loading));
                case loadtopic:
                    if (vThis!=null && mloadingDialog != null)
                        mloadingDialog.start(getString(R.string.items_loadData_loading));
                    break;
                default:
                    break;
            }

        }

        @Override
        protected void onPostExecute(String result) {
            if (mloadingDialog != null) {
                if (mloadingDialog.isShowing())
                    mloadingDialog.stop();
            }
            switch (mstep) {
                case loaddata:
                    if (result == null || !result.equals("OK")) {
                        if (result != null) {
                            ViewHub.showLongToast(vThis, result);
                            if (result.contains("小组不存在")) {
                                // finish();
                            }
                        }
//                        else if(!NetWorkHelper.isNetworkAvailable(getApplicationContext())){
//                            Toast.makeText(getApplicationContext(), R.string.net_ivaliable, Toast.LENGTH_LONG).show() ;
//                        }
                    } else {
                        doListHead();
                    }
                    break;

                case loadtopic:
                    dotophead();
                    break;
                default:
                    break;
            }

        }

        private void dotophead() {
            List<Map<String, ?>> list = new ArrayList<Map<String, ?>>();
            for (TopicInfoModel item : topgrouplist) {
                Map<String, String> m = new HashMap<String, String>();
                m.put("Text", item.getTitle());
                list.add(m);
            }

            String[] from = {"Text"};
            int[] to = {R.id.textView1};
            final SimpleAdapter adapter = new SimpleAdapter(vThis, list, R.layout.layout_toptopic, from, to);
            mToptopiclist.setAdapter(adapter);
            int height = list.size() * 33;
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT, FunctionHelper.dip2px(getResources(),
                    height));
            params.addRule(RelativeLayout.BELOW, R.id.ll_line);
            mToptopiclist.setLayoutParams(params);
            mToptopiclist.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                    // TODO Auto-generated method stub

                    TopicInfoModel model = topgrouplist.get(arg2);

                    // 进入活动详情
                    Intent intent = new Intent(vThis, PostDetailActivity.class);
                    intent.putExtra(PostDetailActivity.EXTRA_TID, model.getID());
                    intent.putExtra(PostDetailActivity.EXTRA_LOGO_URL, Const.getShopLogo(model.getUserID()));
                    intent.putExtra(PostDetailActivity.EXTRA_POST_TITLE, model.getTitle());
                    intent.putExtra(PostDetailActivity.EXTRA_POST_TYPE, model.getType() == 0 ? Const.PostType.TOPIC
                            : Const.PostType.ACTIVITY);
                    startActivity(intent);
                }
            });
        }

        private void doListHead() {
            if (groupModel != null) {
                tvTitle.setText(groupModel.getName());
                tvTopicName.setText(groupModel.getName());
                //     tvDetailCount.setText("成员 " + groupModel.getMemberCount() + "    帖子 " + groupModel.getTopicCount());
                tvDetailCount.setText("帖子 " + groupModel.getTopicCount());
                String imageurl = ImageUrlExtends.getImageUrl(groupModel.getLogoUrl(), 3);
                if (imageurl.length() > 0) {
                    Picasso.with(vThis).load(imageurl).placeholder(R.mipmap.app_logo).into(imgLogo);
                }
                if (groupModel.isIsMember()) {
                    btnAddMember.setText("退出");
                    btnAddMember.setBackgroundResource(R.drawable.btn_white);
                    btnAddMember.setVisibility(View.GONE);
                } else {
                    btnAddMember.setText("加入");
                    btnAddMember.setBackgroundResource(R.drawable.ye_pin_red_corner);
                    btnAddMember.setVisibility(View.VISIBLE);
                }
            }
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            if (mloadingDialog != null) {
                if (mloadingDialog.isShowing())
                    mloadingDialog.stop();
            }
            if (values[0] == 0)
                doListHead();
            else {
//                if (adapter != null) {
//                    adapter.mList = itemList;
//                    adapter.notifyDataSetChanged();
//                }
            }
        }

        @Override
        protected String doInBackground(Void... params) {

            switch (mstep) {

                case loadtopic:
                    File cachetop = CacheDirUtil.getCache(vThis, "topicpage_list_top" + groupID);
                    List<TopicInfoModel> topictop = GsonHelper.jsonToObject(CacheDirUtil.readString(cachetop),
                            new TypeToken<List<TopicInfoModel>>() {
                            });
                    if (topictop != null && !topictop.isEmpty()) {

                    }
                    try {
                        topgrouplist = GroupAPI.getTopGroupInfo(vThis, String.valueOf(groupID), cachetop);
                        return "OK";
                    } catch (Exception ex) {
                        Log.e(TAG, "获取小组数据发生异常");
                        ex.printStackTrace();
                        return ex.getMessage();
                    }
                case loaddata:

                    File cacheFile = CacheDirUtil.getCache(vThis, "topicpage_" + groupID);
                    if (cacheFile.exists()) {
                        AgentGroup result = GsonHelper.jsonToObject(CacheDirUtil.readString(cacheFile),
                                new TypeToken<AgentGroup>() {
                                });
                        if (result != null) {
                            groupModel = result;
                            publishProgress(0);
                        }
                        File cacheFileList = CacheDirUtil
                                .getCache(vThis, "topicpage_list_" + groupID);
                        List<TopicInfoModel> topicInfoList = GsonHelper.jsonToObject(
                                CacheDirUtil.readString(cacheFileList), new TypeToken<List<TopicInfoModel>>() {
                                });
                        if (topicInfoList != null && !topicInfoList.isEmpty()) {
                            itemList = topicInfoList;
                            publishProgress(1);
                        }
                    }
                    try {
                        groupModel = GroupAPI.getGroupInfo(vThis, String.valueOf(groupID), cacheFile);
                        if (isInTeam == null) {
                            isLastInTeam = isInTeam = groupModel.isIsMember();
                        }
                        return "OK";
                    } catch (Exception ex) {
                        Log.e(TAG, "获取小组数据发生异常");
                        ex.printStackTrace();
                        return ex.getMessage();
                    }
                default:

                    break;
            }

            return "ok";

        }

    }

    public class JoinGroupTask extends AsyncTask<Void, Void, String> {
        private String joinContent;

        public JoinGroupTask(String content) {
            joinContent = content;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (mloadingDialog != null)
                mloadingDialog.start("加入小组中");
        }

        @Override
        protected void onPostExecute(String result) {
            if (mloadingDialog != null)
                mloadingDialog.stop();

            if (!result.equals("OK")) {
                ViewHub.showLongToast(vThis, result);
            } else {
                if (groupModel != null) {
                    if (groupModel.getJoinSetting() != null) {
                        switch (groupModel.getJoinSetting().getType()) {
                            case 4:// 拒绝加入
                                break;
                            case 1:// 审核加入
                                ViewHub.showLongToast(vThis, "已提交审核，审核通过后即可加入小组");
                                break;
                            case 2:// 暗号加入
                            case 3:// 申请即加入
                                groupModel.setIsMember(true);
                                isLastInTeam = true;
                                btnAddMember.setText("退出");
                                btnAddMember.setBackgroundResource(R.drawable.btn_white);

                                LoadDataTask loadDataTask = new LoadDataTask(step.loaddata);
                                loadDataTask.execute((Void) null);

                                Intent serviceIntent = new Intent();
                                serviceIntent.setAction(PostDetailActivity.PostDetailActivityReloadBroadcaseName);
                                vThis.sendBroadcast(serviceIntent);
                                break;
                            default:
                                break;
                        }
                    }
                }
            }
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                GroupAPI.joinGroup(vThis, String.valueOf(groupID), joinContent);
                return "OK";
            } catch (Exception ex) {
                Log.e(TAG, "加入数据发生异常");
                ex.printStackTrace();
                return ex.getMessage() == null ? "未知异常" : ex.getMessage();
            }
        }

    }

    public class exitGroupTask extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (mloadingDialog != null)
                mloadingDialog.start(getString(R.string.items_loadData_loading));
        }

        @Override
        protected void onPostExecute(String result) {
            if (mloadingDialog != null)
                mloadingDialog.stop();

            if (!result.equals("OK")) {
                ViewHub.showLongToast(vThis, result);
            } else {
                File cacheFile = CacheDirUtil.getCache(vThis, "topicpage_" + groupID);
                if (cacheFile.exists())
                    cacheFile.delete();
                groupModel.setIsMember(false);
                btnAddMember.setText("加入");
                btnAddMember.setBackgroundResource(R.drawable.ye_pin_red_corner);
            }
        }

        @Override
        protected String doInBackground(Void... params) {

            try {
                GroupAPI.exitGroup(vThis, String.valueOf(groupID));
                isLastInTeam = false;
                return "OK";
            } catch (Exception ex) {
                Log.e(TAG, "退出数据发生异常");
                ex.printStackTrace();
                return ex.getMessage() == null ? "未知异常" : ex.getMessage();
            }
        }

    }

    public class LoadListDataTask extends AsyncTask<Void, Void, Object> {
        private boolean mIsRefresh = false;

        public LoadListDataTask(boolean isRefresh) {
            mIsRefresh = isRefresh;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            if (mloadingDialog != null && !isHidden())
//                mloadingDialog.start(getString(R.string.items_loadData_loading));
        }

        @Override
        protected void onPostExecute(Object result) {
//            if (isFinishing()) {
//                return;
//            }
            if (mloadingDialog != null)
                mloadingDialog.stop();
            //   mRefreshListView.onRefreshComplete();
            if (result instanceof String) {
                ViewHub.showLongToast(vThis, (String) result);
            } else {
                @SuppressWarnings("unchecked")
                List<TopicInfoModel> list = (List<TopicInfoModel>) result;
                if (mIsRefresh) {
                    if (itemList != null)
                        itemList.clear();
                    itemList = list;
                } else {
                    itemList.addAll(list);
                }
//                if (adapter != null) {
//                    if (adapter.mList != null)
//                        adapter.mList = itemList;
//                    adapter.notifyDataSetChanged();
//                }
            }
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            //  adapter.mList = itemList;
            //  adapter.notifyDataSetChanged();
        }

        @Override
        protected Object doInBackground(Void... params) {
            File cacheFile = null;
            if (mPageIndex == 1) {
                cacheFile = CacheDirUtil.getCache(vThis, "topicpage_list_" + groupID);
            }
            try {
                List<TopicInfoModel> result = TopicAPI.getTopicInfo(vThis, groupID, mPageIndex, mPageSize, cacheFile);

                // if (mIsRefresh) {
                // itemList = result;
                // } else {
                // itemList.addAll(result);
                // }
                return result;
            } catch (Exception ex) {
                Log.e(TAG, "获取小组帖子列表发生异常");
                ex.printStackTrace();
                return ex.getMessage() == null ? "未知异常" : ex.getMessage();
            }
        }
    }

    /**
     * /** 绑定款式列表
     */
    private void bindItemData(boolean isRefresh) {
        if (isRefresh) {
            mPageIndex = 1;
//            LoadListDataTask loadListDataTask = new LoadListDataTask(isRefresh);
//            loadListDataTask.execute((Void) null);
        } else {
            mPageIndex++;
//            LoadListDataTask loadListDataTask = new LoadListDataTask(isRefresh);
//            loadListDataTask.execute((Void) null);
        }
        getList_v2();

    }

}
