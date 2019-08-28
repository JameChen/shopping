package com.nahuo.quicksale.Topic;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mobstat.StatService;
import com.google.gson.reflect.TypeToken;
import com.nahuo.library.controls.FlowLayout;
import com.nahuo.library.controls.LoadingDialog;
import com.nahuo.library.controls.NoScrollGridView;
import com.nahuo.library.controls.pulltorefresh.PullToRefreshListView;
import com.nahuo.library.helper.DisplayUtil;
import com.nahuo.library.helper.FunctionHelper;
import com.nahuo.library.helper.GsonHelper;
import com.nahuo.library.helper.ImageUrlExtends;
import com.nahuo.library.utils.TimeUtils;
import com.nahuo.quicksale.ItemDetailsActivity;
import com.nahuo.quicksale.PicGalleryActivity;
import com.nahuo.quicksale.R;
import com.nahuo.quicksale.ShareEntity;
import com.nahuo.quicksale.ViewHub;
import com.nahuo.quicksale.activity.ItemPreview1Activity;
import com.nahuo.quicksale.activity.PinHuoDetailListActivity;
import com.nahuo.quicksale.activity.SortReasonActivity;
import com.nahuo.quicksale.adapter.CommentAdapter;
import com.nahuo.quicksale.adapter.ImgViewAdapter;
import com.nahuo.quicksale.api.AccountAPI;
import com.nahuo.quicksale.api.ActivityAPI;
import com.nahuo.quicksale.api.ReplyAPI;
import com.nahuo.quicksale.api.TopicAPI;
import com.nahuo.quicksale.app.BWApplication;
import com.nahuo.quicksale.base.BaseAppCompatActivity;
import com.nahuo.quicksale.common.CacheDirUtil;
import com.nahuo.quicksale.common.Const;
import com.nahuo.quicksale.common.ListUtils;
import com.nahuo.quicksale.common.NahuoShare;
import com.nahuo.quicksale.common.SpManager;
import com.nahuo.quicksale.common.UpToTopTool;
import com.nahuo.quicksale.common.Utils;
import com.nahuo.quicksale.common.WSRuleHelper;
import com.nahuo.quicksale.di.module.HttpManager;
import com.nahuo.quicksale.model.http.CommonSubscriber;
import com.nahuo.quicksale.model.http.api.PinHuoApi;
import com.nahuo.quicksale.model.http.response.PinHuoResponse;
import com.nahuo.quicksale.oldermodel.PinHuoModel;
import com.nahuo.quicksale.oldermodel.PostsListModel;
import com.nahuo.quicksale.oldermodel.ReturnData;
import com.nahuo.quicksale.oldermodel.TopicDetailModel;
import com.nahuo.quicksale.oldermodel.UserCardModel;
import com.nahuo.quicksale.util.ActivityUtil;
import com.nahuo.quicksale.util.RxUtil;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by 诚 on 2015/9/21.
 */

/**
 * 帖子
 *
 * @author James Chen
 * @create time in 2017/3/23 17:59
 */
public class PostDetailActivity extends BaseAppCompatActivity implements
        View.OnClickListener {
    public static String TAG = PostDetailActivity.class.getSimpleName();

    private Context mContext = this;
    private LoadingDialog mLoadingDialog;
    /**
     * 用户头像
     */
    public static final String EXTRA_LOGO_URL = "EXTRA_LOGO_URL";
    /**
     * 帖子类型 {@link Const.PostType}
     */
    public static final String EXTRA_POST_TYPE = "EXTRA_POST_TYPE";
    /**
     * 帖子ID
     */
    public static final String EXTRA_TID = "EXTRA_TID";
    /**
     * 帖子标题
     */
    public static final String EXTRA_POST_TITLE = "EXTRA_POST_TITLE";
    // public static final String EXTRA_TUAN = "tuan";
    private int mPostId;
    private String mNormailTitle = "";
    private Const.PostType mPostType;
    private String mLogoUrl;
    private ImageView mIvIcon, mIvLike;
    private TextView mTvUserName, mTvGoods, mTvPostTime, mTvCommentTitle;
    // private GridView mGvGoods;
    // private LikeGridViewAdapter mLikeGridViewAdapter;
    private PullToRefreshListView mLvComments;
    private CommentAdapter mCommentAdapter;
    private boolean mIsRefreshComment = true, mIsLoadingComments;
    private WebView mWebView;
    private int mPageIndex = 1;
    private int mPageSize = 20;
    private View mLayoutFakeComment, mLayoutWriteComment, mLayoutLikeUser;
    private RelativeLayout.LayoutParams commentParams;
    private EditText mEtRealComment;
    private String mCommentContent;
    private PostsListModel mCurReplyUser; // 当前被回复的用户
    private TopicDetailModel mTopicDetailModel;
    private FlowLayout mLayoutLikes;
    private TextView tvTitle, tvPostTitle, tvShopSignture;
    private NoScrollGridView mActivityImgs;
    private String[] mTopicFirstImg;
    public static String PostDetailActivityReloadBroadcaseName = "com.nahuo.wp.PostDetailActivity.reload";

    private MyBroadcast mybroadcast;

    private Button mBtnvoice;

    private TextView joinPersonView, activityLeaveView, activityLeaveLabel, activityTitleView;

    //  private VoiceHelper voicehelper;

    private PopupWindow mPopupWindow;

    private Button moreBtn;

    private ImageView iv, ivexproe;
    private Task task;

    private static enum Step {
        LOAD_POST_DETAIL, LOAD_POST_COMMENT, LIKE, COMMENT, LOAD_SHOP_DATA, LOAD_COLLETION
    }

    private ViewGroup mainView;
    private View nameActivityView, mMainView, popview;
    private TextView popCollectTxt;
    private boolean isCollected;
    private UpToTopTool mUpToTop;
    private boolean collectJob;
    private boolean showEditComment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainView = (ViewGroup) getLayoutInflater().inflate(
                R.layout.activity_post_detail, null);
        setContentView(mainView);
        initExtras();
        initView();
        loadData();
        mybroadcast = new MyBroadcast();
        IntentFilter filter = new IntentFilter();
        filter.addAction(PostDetailActivityReloadBroadcaseName);
        registerReceiver(mybroadcast, filter); // 注册Broadcast Receiver
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (task != null)
            task.cancel(true);
        mUpToTop.dismissUpHead();
        unregisterReceiver(mybroadcast);// 取消注册
        mWebView.removeAllViews();
        mWebView.destroyDrawingCache();
        mWebView.destroy();
        if (mTopicDetailModel != null && mTopicDetailModel.getImages() != null)
            mTopicDetailModel.getImages().clear();
        if (mTopicDetailModel != null
                && mTopicDetailModel.getLikeUsers() != null)
            mTopicDetailModel.getLikeUsers().clear();
        mTopicDetailModel = null;
        mCurReplyUser = null;
        mLvComments = null;
        mEtRealComment = null;
        mTvCommentTitle = null;
        mLayoutWriteComment = null;
        commentParams = null;
        // VoiceRecognizer.shareInstance().destroy();
        System.gc();
    }

    public class MyBroadcast extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String actionStr = intent.getAction();
            if (actionStr.equals(PostDetailActivityReloadBroadcaseName)) {// 重新刷新
                loadData();
            }
        }
    }

    private void loadData() {
//        task = new Task(Step.LOAD_POST_DETAIL);
//        task.execute();
        switch (mPostType) {
            case ACTIVITY:
                getActivityDetail();
                break;
            case TOPIC:
                getTopDetail();
                break;
            default:
                break;
        }
    }

    private void getActivityDetail() {
        PinHuoApi pinHuoApi = HttpManager.getInstance().getPinHuoNoCacheApi(TAG);
        addSubscribe(pinHuoApi.getActivityDetailInfo(mPostId)
                .compose(RxUtil.<PinHuoResponse<TopicDetailModel>>rxSchedulerHelper())
                .compose(RxUtil.<TopicDetailModel>handleResult())
                .subscribeWith(new CommonSubscriber<TopicDetailModel>(this, true, R.string.loading) {
                    @Override
                    public void onNext(TopicDetailModel topicDetailModel) {
                        super.onNext(topicDetailModel);
                        if (topicDetailModel != null)
                            onPostDetailLoaded(topicDetailModel);
                    }
                }));

    }

    private void getTopDetail() {
        PinHuoApi pinHuoApi = HttpManager.getInstance().getPinHuoNoCacheApi(TAG);
        addSubscribe(pinHuoApi.getTopicDetailInfo(mPostId)
                .compose(RxUtil.<PinHuoResponse<TopicDetailModel>>rxSchedulerHelper())
                .compose(RxUtil.<TopicDetailModel>handleResult())
                .subscribeWith(new CommonSubscriber<TopicDetailModel>(this, true, R.string.loading) {
                    @Override
                    public void onNext(TopicDetailModel topicDetailModel) {
                        super.onNext(topicDetailModel);
                        if (topicDetailModel != null)
                            onPostDetailLoaded(topicDetailModel);
                    }
                }));
    }

    private void load_post_comment(final Step mStep) {

        PinHuoApi pinHuoApi = HttpManager.getInstance().getPinHuoNoCacheApi(TAG);
        addSubscribe(((mPostType == Const.PostType.TOPIC) ? pinHuoApi.getTopicPostsListInfo(mPostId, mPageIndex, mPageSize) :
                pinHuoApi.getActivityPostsListInfo(mPostId, mPageIndex, mPageSize))
                .compose(RxUtil.<PinHuoResponse<List<PostsListModel>>>rxSchedulerHelper())
                .compose(RxUtil.<List<PostsListModel>>handleResult())
                .subscribeWith(new CommonSubscriber<List<PostsListModel>>(this, true, R.string.loading_topic) {
                    @Override
                    public void onNext(List<PostsListModel> data) {
                        super.onNext(data);
                        if (!ListUtils.isEmpty(data)) {
                            File cCacheDir = null;
                            if (mPageIndex == 1 && mCommentAdapter.getCount() == 0) {
                                String cName = mStep.toString() + "_"
                                        + mPostType.toString() + "_" + mPostId + "_"
                                        + mPageIndex;
                                cCacheDir = CacheDirUtil.getCache(
                                        getApplicationContext(), cName);
                            }
                            if (cCacheDir != null) {
                                String json = GsonHelper.objectToJson(data);
                                CacheDirUtil.saveString(cCacheDir, json);
                            }
                            onCommentsLoaded(data);
                        } else {
//                            List<PostsListModel> list=new ArrayList<PostsListModel>();
//                            PostsListModel model=new PostsListModel();
//                            model.setUserName("dsad");
//                            model.setContent("dddddddddddddddddddd");
//                            model.setReplyUserName("aaa");
//                            list.add(model);
//                            mCommentAdapter.setData(list);
                            mLvComments.setCanLoadMore(false);
                            mLvComments.onLoadMoreComplete();
                        }
                    }
                }));
    }
    private void collect() {

        PinHuoApi pinHuoApi = HttpManager.getInstance().getPinHuoNoCacheApi(TAG);
        addSubscribe(((mPostType == Const.PostType.TOPIC) ? pinHuoApi.collecXiaoZutTopic(mPostId) :
                pinHuoApi.collecXiaoZutActivity(mPostId))
                .compose(RxUtil.<PinHuoResponse<Object>>rxSchedulerHelper())
                .compose(RxUtil.<Object>handleResult())
                .subscribeWith(new CommonSubscriber<Object>(this, true, R.string.loading) {
                    @Override
                    public void onNext(Object object) {
                        super.onNext(object);
                        if (object instanceof  Double){
                            double id= (double) object;
                            ShowColletion(id);
                        }
                    }
                }));
    }
    private void initExtras() {
        Intent intent = getIntent();
        mPostId = intent.getIntExtra(EXTRA_TID, 0);
        mNormailTitle = intent.getStringExtra(EXTRA_POST_TITLE);
        mPostType = (Const.PostType) intent.getSerializableExtra(EXTRA_POST_TYPE);
        if (mPostType == null) {
            throw new IllegalArgumentException("Const.PostType为空");
        }
        mLogoUrl = intent.getStringExtra(EXTRA_LOGO_URL);
        if (mLogoUrl == null) {
            mLogoUrl = "";
        }
    }

    @SuppressLint({"SetJavaScriptEnabled", "JavascriptInterface"})
    private void initView() {

        tvTitle = (TextView) findViewById(R.id.tv_title);
        switch (mPostType) {
            case ACTIVITY:
                tvTitle.setText("活动");
                break;
            case TOPIC:
                tvTitle.setText("帖子");
                break;
            default:
                break;
        }
        mUpToTop = new UpToTopTool();
        mLvComments = (PullToRefreshListView) findViewById(R.id.lv_comments);
        mUpToTop.setListViewTouchListener(mLvComments);
        mUpToTop.setTitleTap(findViewById(R.id.title));
        mUpToTop.setMarginBottom(FunctionHelper.dip2px(getResources(), 80));
        mLvComments.setCanLoadMore(true);
        mLvComments.setCanRefresh(false);
        mLvComments.setAutoLoadMore(true);
        mLvComments.setOnLoadListener(new PullToRefreshListView.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                loadComments(false);
            }
        });
        mIvLike = (ImageView) findViewById(R.id.iv_like);
        initListView();
        // tvTitle.setText(mNormailTitle);
        Button backBtn = (Button) findViewById(R.id.titlebar_btnLeft);
        backBtn.setVisibility(View.VISIBLE);
        ImageView my_share = (ImageView) findViewById(R.id.my_share);
        my_share.setVisibility(View.VISIBLE);
        my_share.setOnClickListener(this);
        backBtn.setOnClickListener(this);
        Button shareBtn = (Button) findViewById(R.id.titlebar_btnRight);
        moreBtn = (Button) findViewById(R.id.titlebar_btnmore);

        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) shareBtn
                .getLayoutParams();
        params.width = params.height = FunctionHelper
                .dip2px(getResources(), 20);
        //  params.width = params.height=getResources().getDimensionPixelSize(R.dimen.titlebar_height);
        shareBtn.setLayoutParams(params);
        shareBtn.setBackgroundResource(R.drawable.pn_share_left_white);
        shareBtn.setVisibility(View.GONE);
        shareBtn.setOnClickListener(this);

        moreBtn.setBackgroundResource(R.drawable.liulan);
        moreBtn.setOnClickListener(this);
        mLoadingDialog = new LoadingDialog(this);
        mMainView = this.findViewById(R.id.ll_postdetail_voice);
        // mGvGoods = (GridView)findViewById(R.id.gv_goods);
        // mLikeGridViewAdapter = new LikeGridViewAdapter(mContext);
        // mGvGoods.setAdapter(mLikeGridViewAdapster);

        //   Logger.e("post detail type = " + mPostType+"  "+mPostId);
        mLayoutFakeComment = findViewById(R.id.layout_fake_comment);
        mLayoutWriteComment = findViewById(R.id.layout_write_comment);
        findViewById(R.id.et_fake_comment).setOnClickListener(this);

    }

    public static final int REQUEST_RECHARGE = 1;
//    Handler handler=new Handler(){
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            if (msg.what==1){
//                int webViewHeight=msg.arg1;
//                ViewHub.showLongToast(PostDetailActivity.this,webViewHeight+"");
//                LinearLayout.LayoutParams linearParams2 =(LinearLayout.LayoutParams)mWebView.getLayoutParams(); //取控件textView当前的布局参数
//                linearParams2.height = webViewHeight;// 控件的高强制设成20
//
//            }
//        }
//    };
    // js通信接口
    public class JavascriptInterface {
        //        @android.webkit.JavascriptInterface
//        public void GoToRecharge() {
//            //充值
//            Intent intent = new Intent(PostDetailActivity.this, WXPayEntryActivity.class);
//            intent.putExtra(WXPayEntryActivity.EXTRA_TYPE, WXPayEntryActivity.Type.CHARGE);
//            startActivityForResult(intent, REQUEST_RECHARGE);
//        }
//
//        @android.webkit.JavascriptInterface
//        public void GoToItemDetail(String str, String str1) {
//            //商品详情
//            try {
//                Intent intent = new Intent(PostDetailActivity.this, ItemDetailsActivity.class);
//                intent.putExtra(ItemDetailsActivity.EXTRA_ID, Integer.parseInt(str));
//               // intent.putExtra(ItemDetailsActivity.EXTRA_PIN_HUO_ID, Integer.parseInt(str1));
//                startActivity(intent);
//            } catch (NumberFormatException e) {
//                e.printStackTrace();
//            }
//        }
//
//        @android.webkit.JavascriptInterface
//        public void GoToQsList(String str) {
//            try {
//                PinHuoModel model1 = new PinHuoModel();
//                model1.ID=Integer.parseInt(str);
//                PinHuoDetailListActivity.launch(PostDetailActivity.this, model1, false);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
////        }
//        @android.webkit.JavascriptInterface
//        public void getBodyHeight(String number) {
//           int webViewHeight =  Integer.parseInt(number.split("[.]")[0]);
//            Message msg = new Message();
//            msg.what = 1;
//            msg.arg1=webViewHeight;
//            handler.sendMessage(msg);//用activity中的handler发送消息
//        }

        @android.webkit.JavascriptInterface
        public void GoToRecharge() {
            //充值
//            Intent intent = new Intent(mContext, WXPayEntryActivity.class);
//            intent.putExtra(WXPayEntryActivity.EXTRA_TYPE, WXPayEntryActivity.Type.CHARGE);
//            startActivityForResult(intent, REQUEST_RECHARGE);
            Utils.gotoPayEntryActivity(PostDetailActivity.this);
        }

        @android.webkit.JavascriptInterface
        public void GoToItemDetail(String str, String str1) {
            //商品详情
            try {
                Intent intent = new Intent(mContext, ItemDetailsActivity.class);
                intent.putExtra(ItemDetailsActivity.EXTRA_ID, Integer.parseInt(str));
                //intent.putExtra(ItemDetailsActivity.EXTRA_PIN_HUO_ID, Integer.parseInt(str1));
                startActivity(intent);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }

        @android.webkit.JavascriptInterface
        public void GoToQsList(String str) {
            try {
                PinHuoModel model1 = new PinHuoModel();
                model1.ID = Integer.parseInt(str);
                PinHuoDetailListActivity.launch(mContext, model1, false);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @android.webkit.JavascriptInterface
        public void GoToGroupDetail(String str, String str1) {
            try {
                Intent intent = new Intent(mContext, PostDetailActivity.class);
                intent.putExtra(PostDetailActivity.EXTRA_TID, Integer.parseInt(str));

                //帖子
                if (str1.equals("activity")) {
                    intent.putExtra(PostDetailActivity.EXTRA_POST_TYPE, Const.PostType.ACTIVITY);
                } else {
                    intent.putExtra(PostDetailActivity.EXTRA_POST_TYPE, Const.PostType.TOPIC);
                }
                mContext.startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @android.webkit.JavascriptInterface
        public void GoToUrl(String str, String str1) {
            //如果为0表示App打开，1表示由系统浏览器打开
            try {
                if (!TextUtils.isEmpty(str)) {
                    if (str1.equals("0")) {
                        Intent intent = new Intent();
                        intent.setAction("android.intent.action.VIEW");
                        Uri content_url = Uri.parse(str);
                        intent.setData(content_url);
                        mContext.startActivity(intent);
                    } else {
                        Intent intent = new Intent(mContext, ItemPreview1Activity.class);
                        intent.putExtra("name", "天天拼货");
                        intent.putExtra("url", str);
                        mContext.startActivity(intent);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        @android.webkit.JavascriptInterface
        public void GoToItemCat(String str, String str1, String str2) {
            //分类
            try {
                Intent intent = new Intent(mContext, SortReasonActivity.class);
                intent.putExtra(SortReasonActivity.EXTRA_RID, Integer.parseInt(str));
                intent.putExtra(SortReasonActivity.EXTRA_VALUEIDS, str1);
                intent.putExtra(SortReasonActivity.EXTRA_TITLE, str2);
                mContext.startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        @android.webkit.JavascriptInterface
        public void GoToLiveList() {
            //分类
            try {
                ActivityUtil.goToLiveListActivity(mContext);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        @android.webkit.JavascriptInterface
        public void GoToLiveItem(String str) {
            //分类
            try {
                goLiveItem(str);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        @android.webkit.JavascriptInterface
        public void openImage(String[] imgs, String indexSrc) {
            if (imgs == null) {
                return;
            }
            int index = 0;
            boolean find = false;
            ArrayList<String> urls = new ArrayList<String>();
            for (int i = 0; i < imgs.length; i++) {
                String url = imgs[i];
                if (url.equals(indexSrc)) {
                    find = true;
                    urls.add(url);
                } else if (!url.endsWith("gif")) {
                    urls.add(url);
                    if (!find)
                        index++;
                }
            }
            // 查看大图
            toPicGallery(urls, index);
        }

        @android.webkit.JavascriptInterface
        public void firstImage(String[] imgs) {
            mTopicFirstImg = imgs;
        }
    }

    /****
     * created by 陈智勇   2015-4-13  下午6:13:39
     *
     * @param urls
     * @param indexSrc 当urls==null时，此值小于0从第一张查看，大于等于0，从帖子图片后的图片开始查看，相当于直接查看活动图片
     */
    public void toPicGallery(ArrayList<String> urls, int indexSrc) {
        if (urls == null) {
            urls = new ArrayList<String>();
            if (mTopicFirstImg != null) {
                for (String url : mTopicFirstImg) {
                    if (!url.contains(".gif")) {
                        if (url.startsWith("/bbs/upload")) {
                            url = "http://www.nahuo.com" + url;
                        }
                        urls.add(url);
                    }
                }
            }
            if (indexSrc < 0) {
                indexSrc = 0;
            } else
                indexSrc = indexSrc + urls.size();
        }
        if (mTopicDetailModel.getImages() != null) {
            urls.addAll(mTopicDetailModel.getImages());
        }
        if (urls.size() > 0) {
            Intent intent = new Intent(mContext, PicGalleryActivity.class);
            intent.putExtra("post_detail", mTopicDetailModel);
            intent.putExtra(EXTRA_POST_TYPE, mPostType);
            intent.putExtra(PicGalleryActivity.EXTRA_URLS, urls);
            intent.putExtra(PicGalleryActivity.EXTRA_CUR_POS, indexSrc);
            startActivityForResult(intent, 1);
        } else {
            ViewHub.showLongToast(mContext, "帖子中未找到图片");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            boolean liked = data.getBooleanExtra("like",
                    mTopicDetailModel.isIsLike());
            int postCount = data.getIntExtra("post",
                    mTopicDetailModel.getPostCount());
            boolean needLoadData = false;
            if (liked != mTopicDetailModel.isIsLike()) {
                mTopicDetailModel.setIsLike(true);
                mTopicDetailModel.setLike(mTopicDetailModel.getLike() + 1);
                int id = SpManager.getUserId(mContext);
                mTopicDetailModel.getLikeUsers().add((long) id);
                populateLikes(id);
                // updateLikeBtn(true);
                needLoadData = true;
            }
            if (postCount != mTopicDetailModel.getPostCount()) {
                mTopicDetailModel.setPostCount(postCount);
                needLoadData = true;
            }
            // if(needLoadData){
            // loadData() ;
            // }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void addImageClickListner() {
        // 这段js函数的功能就是，遍历所有的img几点，并添加onclick函数，函数的功能是在图片点击的时候调用本地java接口并传递url过去
//        mWebView.loadUrl("<script>" +
//                "    function GoToRecharge() {" +
//                "        window.WebViewJavascriptBridge.GoToRecharge()" +
//                "    }" +
//                "    function GoToItemDetail(str, str1) {" +
//                "        window.WebViewJavascriptBridge.GoToItemDetail(str, str1)" +
//                "    }" +
//                "    function GoToQsList(str) {" +
//                "        window.WebViewJavascriptBridge.GoToQsList(str)" +
//                "    }" +
//                "</script>");
        mWebView.loadUrl("javascript:(function(){"
                + "     var objs = document.getElementsByTagName(\"img\"); "
                + "     var array = new Array(objs.length);"
                + "     for(var i=0;i<objs.length;i++)  "
                + "     {"
                + "         array[i] = objs[i].src ; "
                + "         objs[i].onclick=function()  "
                + "                         {  "
                + "                             window.wst.openImage(array , this.src);  "
                + "                         } "
                + "     } "
                + "     window.wst.firstImage(array);"
                + "})()");
        // mWebView.loadUrl("javascript:red_count_bg()");
        //mCommentAdapter.notifyDataSetChanged();
    }

    //监听
    private class
    MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Intent intent = new Intent();
            intent.setAction("android.intent.action.VIEW");
            Uri content_url = Uri.parse(url);
            intent.setData(content_url);
            startActivity(intent);
            return true;
            // return super.shouldOverrideUrlLoading(view, url);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
             view.getSettings().setJavaScriptEnabled(true);
            //mWebView.loadUrl("javascript:(function(){window.wst.getBodyHeight($(document.body).height());})()");//注入自定义方法——获取webview高度的方法
            super.onPageFinished(view, url);
            // html加载完成之后，添加监听图片的点击js函数
            addImageClickListner();
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            // view.getSettings().setJavaScriptEnabled(true);
            super.onPageStarted(view, url, favicon);

        }

        @Override
        public void onReceivedError(WebView view, int errorCode,
                                    String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
        }
    }


    private void initListViewHeader() {
        LinearLayout topHeader = (LinearLayout) getLayoutInflater().inflate(R.layout.header_post_detail_top, null);
        //int margin = FunctionHelper.dip2px(getResources(), 8);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        params.topMargin = params.leftMargin = params.rightMargin = margin;
//        params.addRule(RelativeLayout.BELOW, R.id.tv_post_title);
        mWebView = new WebView(getApplicationContext());
        topHeader.addView(mWebView, params);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.setOverScrollMode(WebView.OVER_SCROLL_NEVER);
        mWebView.setWebViewClient(new MyWebViewClient());
        mWebView.addJavascriptInterface(new JavascriptInterface(), "wst");
        View header = getLayoutInflater().inflate(
                R.layout.activity_post_detail_header, null);
        mIvIcon = (ImageView) topHeader.findViewById(R.id.iv_icon);
        mIvIcon.setOnClickListener(this);
        mTvUserName = (TextView) topHeader.findViewById(R.id.tv_user_name);
        mTvUserName.setOnClickListener(this);
        tvShopSignture = (TextView) topHeader.findViewById(R.id.tv_shop_signture);
        tvPostTitle = (TextView) topHeader.findViewById(R.id.tv_post_title);
        mTvPostTime = (TextView) topHeader.findViewById(R.id.tv_post_time);

        mLayoutLikeUser = header.findViewById(R.id.layout_goods);
        mActivityImgs = (NoScrollGridView) header
                .findViewById(R.id.wv_activity_imgs);
        switch (mPostType) {
            case ACTIVITY:
                activityTitleView = (TextView) header.findViewById(R.id.wv_activity_imgs_text);
                //    mIvLike.setVisibility(View.GONE);
                //    mLayoutLikeUser.setVisibility(View.GONE);
                header.findViewById(R.id.post_detail_activity_parent)
                        .setVisibility(View.GONE);
                ( header.findViewById(R.id.wv_activity_imgs_text))
                        .setVisibility(View.VISIBLE);
                nameActivityView = header.findViewById(R.id.btn_activity_name);
                joinPersonView = (TextView) header
                        .findViewById(R.id.txt_activity_person);
                activityLeaveView = (TextView) header
                        .findViewById(R.id.txt_activity_leave_time);
                activityLeaveLabel = (TextView) header
                        .findViewById(R.id.txt_activity_leave_label);

            break;
            default:
                break;
        }
        mLayoutLikes = (FlowLayout) header.findViewById(R.id.flowlayout_likes);
        mTvGoods = (TextView) header.findViewById(R.id.tv_goods);
        mLvComments.addHeaderView(topHeader);
        mLvComments.addHeaderView(header, null, false);


    }

    private void initListView() {
        initListViewHeader();
        mCommentAdapter = new CommentAdapter(mContext);
        mLvComments.setAdapter(mCommentAdapter);
        mCommentAdapter.setListener(new CommentAdapter.Listener() {
            @Override
            public void onReplyClick(PostsListModel model) {
                mCurReplyUser = model;
                handleCommentLayout(true);
                if (mTvCommentTitle != null)
                    mTvCommentTitle.setText("回复" + mCurReplyUser.getUserName());
            }
        });
    }

    // //释放webview导致的内存泄漏
    // private void setConfigCallback(WindowManager windowManager) {
    // try {
    // Field field = WebView.class.getDeclaredField("mWebViewCore");
    // field = field.getType().getDeclaredField("mBrowserFrame");
    // field = field.getType().getDeclaredField("sConfigCallback");
    // field.setAccessible(true);
    // Object configCallback = field.get(null);
    //
    // if (null == configCallback) {
    // return;
    // }
    //
    // field = field.getType().getDeclaredField("mWindowManager");
    // field.setAccessible(true);
    // field.set(configCallback, windowManager);
    // } catch(Exception e) {
    // }
    // }
    private void exit() {
        if (collectJob) {
            Intent data = new Intent();
            data.putExtra(EXTRA_TID, mPostId);
            setResult(Activity.RESULT_OK, data);
        }
        finish();
    }

    @Override
    public void onBackPressed() {
        exit();
    }

    /***
     * 显示报名对话框 create by 陈智勇Mar 25, 2015-10:36:27 AM
     */
    private void showNameDialog() {
        ClipboardManager cm = (ClipboardManager) BWApplication.getInstance()
                .getSystemService(Context.CLIPBOARD_SERVICE);
        cm.setPrimaryClip(ClipData.newPlainText("wx",
                mTopicDetailModel.getWXCode()));
        AlertDialog.Builder builder = new AlertDialog.Builder(this,
                AlertDialog.THEME_HOLO_LIGHT);
        builder.setMessage(mTopicDetailModel.getTuanContent())
                .setTitle("参加团购")
                .setPositiveButton("去微信",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                Intent intent = new Intent();
                                intent.setClassName("com.tencent.mm",
                                        "com.tencent.mm.ui.LauncherUI");
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                try {
                                    startActivity(intent);
                                } catch (ActivityNotFoundException e) {
                                    e.printStackTrace();
                                    Toast.makeText(getApplicationContext(),
                                            "打开微信失败！", Toast.LENGTH_LONG)
                                            .show();
                                }
                            }
                        }).setNegativeButton(R.string.camera_close, null)
                .create().show();
    }

    // 右上角弹出
    private void showScanPopUp(View v) {
        int[] location = new int[2];
        v.getLocationOnScreen(location);
        int width = FunctionHelper.dip2px(mContext.getResources(), 115);
        int hight = FunctionHelper.dip2px(mContext.getResources(), 80);
        // int hight15 = FunctionHelper.dip2px(vThis.getResources(), 15);
        int popY = location[1] + v.getHeight() + 10;
        if (popview == null) {
            popview = LayoutInflater.from(mContext).inflate(
                    R.layout.layout_collection_menu_popview, null);
            iv = (ImageView) popview.findViewById(R.id.Iv_Collection);
            ivexproe = (ImageView) popview.findViewById(R.id.Iv_expore);

            popview.findViewById(R.id.ll_pop_collection).setOnClickListener(this);
            popview.findViewById(R.id.ll_pop_expore).setOnClickListener(this);
            popCollectTxt = (TextView) popview.findViewById(R.id.txt_pop_collect);
            if (isCollected) {
                iv.setBackgroundResource(R.drawable.ic_collected);
                popCollectTxt.setText("取消收藏");
                // ViewHub.showShortToast(mContext, "取消收藏");
            } else {
                iv.setBackgroundResource(R.drawable.ic_collect);
                popCollectTxt.setText("收藏帖子");
                // ViewHub.showShortToast(mContext, "收藏成功");
            }
        }
        if (mPopupWindow == null) {
            mPopupWindow = new PopupWindow(popview, width, hight);

            mPopupWindow.setFocusable(true);

            mPopupWindow.setOutsideTouchable(true);
            mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        }
        mPopupWindow.showAtLocation(moreBtn, Gravity.NO_GRAVITY, location[0]
                + v.getWidth(), popY);

    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.titlebar_btnmore:
                //    showScanPopUp(v);
                toPicGallery(null, -1);

                break;

            case R.id.ll_pop_collection:
                //new Task(Step.LOAD_COLLETION).execute();
                if (SpManager.getIs_Login(this))
                collect();
                break;
            case R.id.ll_pop_expore:
                toPicGallery(null, -1);
                break;
            case R.id.btn_activity_name:
                showNameDialog();
                break;
            case R.id.titlebar_btnLeft:
                exit();
                break;
            case R.id.my_share:
            case R.id.titlebar_btnRight:// 分享
                // Intent intent11 = new Intent(mContext, WXEntryActivity.class);
                // mContext.startActivity(intent11);
                if (mTopicDetailModel == null) {
                    return;
                }
                ShareEntity shareData = new ShareEntity();
                shareData.setTitle(mTopicDetailModel.getTitle());
                if (mTopicDetailModel.getSummary() != null) {
                    shareData.setSummary(mTopicDetailModel.getSummary());
                } else {
                    shareData.setSummary(mTopicDetailModel.getContent());
                }
                // shareData.setSummary("123123123");
                // shareData.setTitle("13213123123123");
                String imgUrl = "";
                switch (mPostType) {
                    case ACTIVITY:
                        shareData.setTargetUrl(mTopicDetailModel.getWebUrl("activity"));
                        if (mTopicDetailModel.getImages().size() > 0) {
                            shareData.setImgUrl(mTopicDetailModel.getImages().get(0));
                        } else {
                            if (mTopicFirstImg != null && mTopicFirstImg.length > 0) {
                                String url = ImageUrlExtends.getImageUrl(
                                        mTopicFirstImg[0], 1);
                                shareData.setImgUrl(url);
                            } else {
                                shareData.setImgUrl(Const.getShopLogo(mTopicDetailModel
                                        .getUserID()));
                            }
                        }
                        break;
                    case TOPIC:
                        shareData.setTargetUrl(mTopicDetailModel.getWebUrl("topic"));
                        if (mTopicFirstImg != null && mTopicFirstImg.length > 0) {
                            String url = ImageUrlExtends.getImageUrl(mTopicFirstImg[0],
                                    1);
                            shareData.setImgUrl(url);
                        } else {
                            shareData.setImgUrl(Const.getShopLogo(mTopicDetailModel
                                    .getUserID()));
                        }
                        break;
                    default:
                        break;
                }
                // shareData.setImgUrl("http://img4.nahuo.com/u33306/item/m1418961008019.jpg!thum.112");
                // shareData.setTargetUrl("http://item.weipushop.com/wap/wpitem/790509");
                NahuoShare share = new NahuoShare(mContext, shareData);
                share.show();
                break;
            case R.id.iv_like:// 点赞
                //   onLikeClick(v);
                if (SpManager.getIs_Login(this)) {
                   // new Task(Step.LOAD_COLLETION).execute();
                    collect();
                }
                else
                    Utils.gotoLoginActivity(this);
                break;
            case R.id.iv_cancel_comment:// 取消评论
                handleCommentLayout(false);
                break;
            case R.id.iv_submit_comment:
                onComment();
                break;
            case R.id.et_fake_comment:// 回复楼主
                if (SpManager.getIs_Login(this)) {
                    if (mTopicDetailModel == null) {
                        ViewHub.showLongToast(mContext, "页面加载异常，请重新进入");
                        return;
                    }
                    if (!WSRuleHelper.doRule(mContext, mTopicDetailModel.getCanReply(),
                            mTopicDetailModel.getGroupID(), "",
                            new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    doJoinDialogClick(v);
                                }
                            })) {
                        return;
                    }
                    mCurReplyUser = new PostsListModel();
                    handleCommentLayout(true);
                    if (mTvCommentTitle != null)
                        mTvCommentTitle.setText("回复楼主");
                } else {
                    Utils.gotoLoginActivity(this);
                }
                break;
            case R.id.iv_icon:
            case R.id.tv_user_name:
//                Intent intent = new Intent(mContext, UserInfoActivity.class);
//                intent.putExtra(UserInfoActivity.EXTRA_USER_ID,
//                        mTopicDetailModel.getUserID());
//                mContext.startActivity(intent);
                break;
        }

    }

    private void doJoinDialogClick(View v) {
        Dialog dialog = (Dialog) v.getTag();
        dialog.dismiss();
        if (v.getId() == R.id.dialog_sure) {
            // if(getCallingActivity()!=null){
            // setResult(Activity.RESULT_OK) ;
            // Logger.e("set result ok ...") ;
            // finish() ;
            // }
            // else
            {
                Intent intent = new Intent(mContext,
                        TopicPageActivity.class);
                intent.putExtra("gid", mTopicDetailModel.getGroupID());
                // intent.putExtra("join", true) ;
                startActivity(intent);
            }
        }
    }

    /**
     * @description 评论
     * @created 2015-2-10 下午4:53:27
     * @author ZZB
     */
    private void onComment() {
        mCommentContent = mEtRealComment.getText().toString();
        mCommentContent = mCommentContent.replace("<", "&lt;");
        mCommentContent = mCommentContent.replace(">", "&gt;");
        mCommentContent = mCommentContent.replace("\"", "&quot;");
        if (TextUtils.isEmpty(mCommentContent)) {
            ViewHub.setEditError(mEtRealComment, "请输入评论内容");
            return;
        }
        handleCommentLayout(false);
        new Task(Step.COMMENT).execute();
    }

    /**
     * @description 写评论的显示控制
     * @created 2015-2-10 下午3:03:17
     * @author ZZB
     */
    private void handleCommentLayout(boolean show) {
        if (!SpManager.getIs_Login(this)) {
            Utils.gotoLoginActivity(this);
        } else {
            if (show && !showEditComment) {
                showEditComment = show;
                mLayoutFakeComment.setVisibility(View.GONE);
                if (mLayoutWriteComment == null) {
                    mLayoutWriteComment = getLayoutInflater().inflate(
                            R.layout.common_comment, null);
                    mLayoutWriteComment.findViewById(R.id.iv_cancel_comment)
                            .setOnClickListener(this);
                    mLayoutWriteComment.findViewById(R.id.iv_submit_comment)
                            .setOnClickListener(this);
                }
                if (commentParams == null) {
                    commentParams = new RelativeLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT);
                    commentParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM,
                            RelativeLayout.TRUE);
                }
                if (mEtRealComment == null)
                    mEtRealComment = (EditText) mLayoutWriteComment
                            .findViewById(R.id.et_real_comment);
                if (mTvCommentTitle == null) {
                    mTvCommentTitle = (TextView) mLayoutWriteComment
                            .findViewById(R.id.tv_comment_title);
                }
                mainView.addView(mLayoutWriteComment, commentParams);
                mLayoutWriteComment.requestFocus();
                ViewHub.showKeyboard(PostDetailActivity.this, mEtRealComment);
                mBtnvoice = (Button) mLayoutWriteComment
                        .findViewById(R.id.btn_set_mode_voice);
                // 弹出麦克风
                mBtnvoice.setOnTouchListener(new View.OnTouchListener() {

                    @Override
                    public boolean onTouch(View arg0, MotionEvent arg1) {
                        switch (arg1.getAction()) {
                            case MotionEvent.ACTION_DOWN:
                                // voicehelper = new VoiceHelper(mContext, mEtRealComment);
                                //     voicehelper.togglePopupWindow(mMainView);
                                break;
                            case MotionEvent.ACTION_UP:
                                //   voicehelper.OnStop();
                                break;
                            default:
                                break;
                        }
                        return false;
                    }
                });
            } else {
                showEditComment = false;
                mEtRealComment.setText("");
                mLayoutFakeComment.setVisibility(View.VISIBLE);
                if (mLayoutWriteComment != null)
                    mainView.removeView(mLayoutWriteComment);
                ViewHub.hideKeyboard(PostDetailActivity.this);
            }
        }
    }

    /**
     * @description 点赞
     * @created 2015-2-10 下午2:39:31
     * @author ZZB
     */
    private void onLikeClick(View v) {
        if (!WSRuleHelper.doRule(mContext, mTopicDetailModel.getCanLike(),
                mTopicDetailModel.getGroupID(), "去加入", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        doJoinDialogClick(v);
                    }
                })) {
            return;
        }
        boolean isLike = (v.getTag() == null ? false : (Boolean) v.getTag());
        if (isLike) {
            ViewHub.showShortToast(mContext, "已点赞");
        } else {
            new Task(Step.LIKE).execute();
        }
    }

    private class Task extends AsyncTask<Object, Object, Object> {

        private Step mStep;

        public Task(Step step) {
            mStep = step;
        }

        @Override
        protected void onPreExecute() {
            switch (mStep) {
                case LOAD_SHOP_DATA:
                    tvShopSignture.setText("签名加载中...");
                    break;
                case LOAD_POST_COMMENT:
                    // if(!mIsRefreshComment)
                    // {
                    // mLoadingDialog.start("正在加载评论...");
                    // }
                    break;
                case LOAD_POST_DETAIL:
                    mLoadingDialog.start("加载中...");
                    break;
                case LIKE:
                    mLoadingDialog.start("加载中...");
                    break;
                case COMMENT:
                    // mLoadingDialog.start("回复中...");
                    break;
                case LOAD_COLLETION:
                    mLoadingDialog.start("操作中...");
                    break;
            }

        }

        @Override
        protected Object doInBackground(Object... params) {
            try {
                switch (mStep) {
                    case LOAD_COLLETION:
                        return TopicAPI.collection(mContext, mPostId, mPostType);
                    case LOAD_SHOP_DATA:
                        if (mTopicDetailModel == null) {
                            return "error:";
                        }
                        String userID = String.valueOf(mTopicDetailModel
                                .getUserID());
                        File userCache = CacheDirUtil.getCache(
                                getApplicationContext(), "userInfo_" + userID);
                        if (userCache.exists()) {
                            UserCardModel userInfo = GsonHelper.jsonToObject(
                                    CacheDirUtil.readString(userCache),
                                    UserCardModel.class);
                            if (userInfo != null) {
                                publishProgress(userInfo, mStep);
                            }
                        }
                        UserCardModel UserCardModel = AccountAPI.getUserCardInfo(
                                mContext, userID, userCache);
                        return UserCardModel;
                    case LOAD_POST_COMMENT:// 加载评论
                        List<PostsListModel> comments;
                        File cCacheDir = null;
                        if (mPageIndex == 1 && mCommentAdapter.getCount() == 0) {
                            String cName = mStep.toString() + "_"
                                    + mPostType.toString() + "_" + mPostId + "_"
                                    + mPageIndex;
                            cCacheDir = CacheDirUtil.getCache(
                                    getApplicationContext(), cName);
                            // if(cCacheDir.exists()){
                            // List<PostsListModel> mPostsListModels =
                            // GsonHelper.jsonToObject(CacheDirUtil.readString(cCacheDir)
                            // ,new TypeToken<List<PostsListModel>>(){});
                            // if(mPostsListModels!=null&&mPostsListModels.size()>0){
                            // publishProgress(mPostsListModels);
                            // }
                            // }
                        }
                        switch (mPostType) {
                            case ACTIVITY:
                                comments = ActivityAPI.getPostsListInfo(mContext,
                                        mPageIndex, mPageSize, mPostId, cCacheDir);
                                return comments;
                            case TOPIC:
                                comments = TopicAPI.getPostsListInfo(mContext,
                                        mPageIndex, mPageSize, mPostId, cCacheDir);
                                return comments;
                        }
                    case LOAD_POST_DETAIL:// 加载帖子内容
                        TopicDetailModel topic;
                        String name = mStep.toString() + "_" + mPostType.toString()
                                + "_" + mPostId;
                        File cacheDir = CacheDirUtil.getCache(
                                getApplicationContext(), name);
                        if (cacheDir.exists()) {
                            String json = CacheDirUtil.readString(cacheDir);
                            TopicDetailModel mTopicDetailModel = GsonHelper
                                    .jsonToObject(json, TopicDetailModel.class);
                            publishProgress(mTopicDetailModel, mStep);
                            // 加载评论缓存
                            String cName = Step.LOAD_POST_COMMENT.toString() + "_"
                                    + mPostType.toString() + "_" + mPostId + "_"
                                    + mPageIndex;
                            cCacheDir = CacheDirUtil.getCache(
                                    getApplicationContext(), cName);
                            if (cCacheDir.exists()) {
                                List<PostsListModel> mPostsListModels = GsonHelper
                                        .jsonToObject(
                                                CacheDirUtil.readString(cCacheDir),
                                                new TypeToken<List<PostsListModel>>() {
                                                });
                                if (mPostsListModels != null
                                        && mPostsListModels.size() > 0) {
                                    publishProgress(mPostsListModels,
                                            Step.LOAD_POST_COMMENT);
                                }
                            }

                        }
                        switch (mPostType) {
                            case ACTIVITY:
                                topic = ActivityAPI.getActivityDetailInfo(mContext,
                                        mPostId, cacheDir);
                                return topic;
                            case TOPIC:
                                topic = TopicAPI.getTopicDetailInfo(mContext, mPostId,
                                        cacheDir);
                                return topic;
                        }
                    case LIKE:// 点赞
                        //  TopicAPI.like(mContext, mPostId);
                        break;
                    case COMMENT:
                        String msg = null;
                        switch (mPostType) {
                            case ACTIVITY:
                                msg = ReplyAPI.saveActivityPost(mContext, mPostId,
                                        mCommentContent, mCurReplyUser.getRootId(),
                                        mCurReplyUser.getID());
                                break;
                            case TOPIC:
                                msg = ReplyAPI.saveTopicPost(mContext, mPostId,
                                        mCommentContent, mCurReplyUser.getRootId(),
                                        mCurReplyUser.getID());
                                break;
                        }
                        return msg;
                }
            } catch (Exception e) {
                e.printStackTrace();
                return "error:" + e.getMessage();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Object... values) {
            if (mLoadingDialog.isShowing()) {
                mLoadingDialog.stop();
            }
            Step step;
            if (values.length == 1) {
                step = mStep;
            } else {
                step = (Step) values[1];
            }
            doData(values[0], step);
        }

        @Override
        protected void onPostExecute(Object result) {
            super.onPostExecute(result);
            if (mLoadingDialog.isShowing()) {
                mLoadingDialog.stop();
            }
            doData(result, mStep);

        }

        private void doData(Object result, Step mStep) {
            if (isFinishing())
                return;
            if (result instanceof String
                    && ((String) result).startsWith("error:")) {
                String errorText = ((String) result).replace("error:", "");
                if (errorText.length() == 0)
                    return;
                ReturnData resultData;

                try {
                    resultData = GsonHelper.jsonToObject(errorText,
                            ReturnData.class);
                    if (resultData.getCode().equals("topic_not_exist")
                            || resultData.getCode()
                            .equals("activity_not_exist")
                            || resultData.getCode().equals("limit_member")) {
                        ViewHub.showLongToast(mContext, resultData.getMessage());
                        collectJob = true;
                        exit();
                    } else {
                        WSRuleHelper.doError(mContext, resultData);
                    }
                } catch (Exception e) {
                    ViewHub.showLongToast(mContext, errorText);
                }
            } else {
                switch (mStep) {
                    case LOAD_SHOP_DATA:
                        UserCardModel UserCardModel = (UserCardModel) result;
                        ShopInfoLoaded(UserCardModel);
                        break;
                    case LOAD_POST_COMMENT:
                        onCommentsLoaded(result);
                        break;
                    case LOAD_POST_DETAIL:
                        onPostDetailLoaded(result);
                        break;
                    case LIKE:
                        //    populateLikes(SpManager.getUserId(mContext));
                        //   mTopicDetailModel.setLike(mTopicDetailModel.getLike() + 1);
                        //   updateLikeBtn(true);
                        break;
                    case COMMENT:
                        if (!(result.equals("true") || result.equals("发表成功"))) {
                            if (result == null || result.equals("false")) {
                                result = "回复失败！";
                            }
                            ViewHub.showLongToast(mContext, (String) result);
                        } else
                            onCommentFinished();
                        break;
                    case LOAD_COLLETION:
                      //  ShowColletion(String.valueOf(result));
                        break;
                }
            }
        }

    }

    // /***
    // * 修复用asynctask导致线程阻塞的问题
    // * @author 陈智勇2015-3-17上午9:55:57
    // */
    // private class Task extends Thread {
    //
    // private Step mStep;
    // public Task(Step step) {
    // mStep = step;
    // Logger.e("task()"+step) ;
    // switch (mStep) {
    // case LOAD_POST_COMMENT:
    // if(!mIsRefreshComment)
    // {
    // mLoadingDialog.start("正在加载评论...");
    // }
    // break;
    // case LOAD_POST_DETAIL:
    // mLoadingDialog.start("加载中...");
    // break;
    // case LIKE:
    // mLoadingDialog.start("加载中...");
    // break;
    // case COMMENT:
    // mLoadingDialog.start("回复中...");
    // break;
    // }
    // }
    //
    // @Override
    // public void run() {
    // Message msg = null ;
    // Bundle data = new Bundle() ;
    // data.putSerializable("step", mStep) ;
    // try {
    // switch (mStep) {
    // case LOAD_POST_COMMENT:// 加载评论
    // List<PostsListModel> comments;
    // String cName = mStep.toString()+ "_"+mPostType.toString()+ "_"+mPostId +
    // "_" + mPageIndex;
    // File cCacheDir = CacheDirUtil.getCache(getApplicationContext(), cName) ;
    // if(cCacheDir.exists()){
    // List<PostsListModel> mPostsListModels =
    // GsonHelper.jsonToObject(CacheDirUtil.readString(cCacheDir)
    // ,new TypeToken<List<PostsListModel>>(){});
    // if(mPostsListModels!=null&&mPostsListModels.size()>0){
    // msg = handler.obtainMessage(DO_DATA, mPostsListModels) ;
    // msg.setData(data) ;
    // msg.sendToTarget() ;
    // }
    // }
    // switch (mPostType) {
    // case ACTIVITY:
    // comments = ActivityAPI.getPostsListInfo(mContext, mPageIndex, mPageSize,
    // mPostId , cCacheDir);
    // msg = handler.obtainMessage(DO_DATA, comments);
    // msg.setData(data) ;
    // case TOPIC:
    // comments = TopicAPI.getPostsListInfo(mContext, mPageIndex, mPageSize,
    // mPostId ,cCacheDir);
    // msg = handler.obtainMessage(DO_DATA, comments) ;
    // msg.setData(data) ;
    // }
    // break ;
    // case LOAD_POST_DETAIL:// 加载帖子内容
    // TopicDetailModel topic;
    // String name = mStep.toString()+ "_"+mPostType.toString()+ "_"+mPostId ;
    // File cacheDir = CacheDirUtil.getCache(getApplicationContext(), name) ;
    // if(cacheDir.exists()){
    // String json = CacheDirUtil.readString(cacheDir) ;
    // TopicDetailModel mTopicDetailModel = GsonHelper.jsonToObject(json,
    // TopicDetailModel.class);
    // msg = handler.obtainMessage(DO_DATA, mTopicDetailModel);
    // msg.setData(data) ;
    // msg.sendToTarget() ;
    // }
    // switch (mPostType) {
    // case ACTIVITY:
    // topic = ActivityAPI.getActivityDetailInfo(mContext, mPostId , cacheDir);
    // Logger.e("post activity detail "+topic.getImages()) ;
    // msg = handler.obtainMessage(DO_DATA, topic) ;
    // msg.setData(data) ;
    // case TOPIC:
    // topic = TopicAPI.getTopicDetailInfo(mContext, mPostId , cacheDir);
    // msg = handler.obtainMessage(DO_DATA, topic) ;
    // msg.setData(data) ;
    // }
    // // 加载评论
    // loadComments(true);
    // break ;
    // case LIKE:// 点赞
    // TopicAPI.like(mContext, mPostId);
    // break;
    // case COMMENT:
    // switch (mPostType) {
    // case ACTIVITY:
    // ReplyAPI.saveActivityPost(mContext, mPostId, mCommentContent,
    // mCurReplyUser.getRootId(), mCurReplyUser.getID());
    // break;
    // case TOPIC:
    // ReplyAPI.saveTopicPost(mContext, mPostId, mCommentContent,
    // mCurReplyUser.getRootId(),
    // mCurReplyUser.getID());
    // break;
    // }
    // break;
    // }
    // } catch (Exception e) {
    // e.printStackTrace();
    // msg = handler.obtainMessage(DO_DATA, "error:" + e.getMessage());
    // msg.setData(data) ;
    // }
    // if(msg!=null){
    // msg.sendToTarget() ;
    // }
    // }
    //
    // }
    // /***
    // * 处理loadData获取到的数据
    // * create by 陈智勇2015-3-17上午9:23:55
    // * @param result
    // */
    // private void doData(Object result , Step mStep) {
    // if (result instanceof String && ((String)result).startsWith("error:")) {
    // String errorText = ((String)result).replace("error:", "");
    // ReturnData resultData;
    //
    // try {
    // resultData = GsonHelper.jsonToObject(errorText, ReturnData.class);
    // if (resultData.getCode().equals("topic_not_exist") ||
    // resultData.getCode().equals("activity_not_exist"))
    // {
    // ViewHub.showLongToast(mContext, resultData.getMessage());
    // finish();
    // }
    // else
    // {
    // WSRuleHelper.doError(mContext, resultData);
    // }
    // } catch (Exception e) {
    // ViewHub.showLongToast(mContext, errorText);
    // }
    // } else {
    // switch (mStep) {
    // case LOAD_POST_COMMENT:
    // onCommentsLoaded(result);
    // break;
    // case LOAD_POST_DETAIL:
    // onPostDetailLoaded(result);
    // break;
    // case LIKE:
    // populateLikes(SpManager.getUserName(mContext));
    // updateLikeBtn(true);
    // break;
    // case COMMENT:
    // onCommentFinished();
    // break;
    // }
    // }
    // }
    // private static final int DO_DATA = 1 ;
    // private Handler handler = new Handler(){
    // public void handleMessage(Message msg){
    // switch(msg.what){
    // case DO_DATA:
    // if (mLoadingDialog.isShowing()) {
    // mLoadingDialog.stop();
    // }
    // Step step = (Step) msg.getData().getSerializable("step") ;
    // doData(msg.obj , step) ;
    // break ;
    // }
    // }
    // };

    /**
     * @description 用户信息加载完毕
     */
    public void ShopInfoLoaded(UserCardModel userCardModel) {
        if (userCardModel != null && userCardModel.getShop() != null
                && userCardModel.getShop().getSignature().length() > 0) {
            tvShopSignture.setText("\""
                    + userCardModel.getShop().getSignature() + "\"");
        } else {
            tvShopSignture.setText("");
        }
    }

    /**
     * @description 评论加载完成
     * @created 2015-2-10 上午11:00:54
     * @author ZZB
     */
    private void onCommentsLoaded(Object result) {
        if (isFinishing()) {
            return;
        }
        mLvComments.onLoadMoreComplete();
        mIsLoadingComments = false;
        if (!(result instanceof List)) {
            // mLvComments.showView(State.ERROR);
        } else {
            @SuppressWarnings("unchecked")
            List<PostsListModel> comments = (List<PostsListModel>) result;
            if (mCommentAdapter.getCount() > 0 && ListUtils.isEmpty(comments)) {
                Toast.makeText(getApplicationContext(), "没有更多的评论啦！",
                        Toast.LENGTH_LONG).show();
                return;
            }
            if (mIsRefreshComment) {
                mCommentAdapter.setData(comments);
            } else {
                List<PostsListModel> oldComments = mCommentAdapter.getData();
                if (oldComments == null) {
                    oldComments = new ArrayList<PostsListModel>();
                }
                oldComments.addAll(comments);
                mCommentAdapter.setData(oldComments);
            }
            if (comments.size() != mPageSize) {
                mLvComments.setCanLoadMore(false);
            }
            mCommentAdapter.notifyDataSetChanged();

        }
    }

    /**
     * @description 评论完成
     * @created 2015-2-10 下午4:46:56
     * @author ZZB
     */
    public void onCommentFinished() {
        PostsListModel comment = new PostsListModel();
        comment.setUserName(SpManager.getUserName(this));
        comment.setReplyUserName(mCurReplyUser.getUserName());
        comment.setContent(mCommentContent);
        comment.setFloor(-1);
        comment.setUserID(SpManager.getUserId(getApplicationContext()));
        comment.setCreateTime(TimeUtils.millisToTimestamp(
                System.currentTimeMillis(), "yyyy-MM-dd HH:mm:ss"));
        mCommentContent = "";
        comment.setRootId(mCurReplyUser.getRootId());
        mCommentAdapter.addComment(comment);
    }

    /***
     * @description 回复给活动
     * @created 2015-2-10 下午4:42:21
     * @author ZZB
     */
    public void onCommentToActivity() throws Exception {

    }

    // 收藏是否成功显示
    private void ShowColletion(double f) {


        if (f<=0) {


            switch (mPostType) {
                case ACTIVITY:
                    tvTitle.setText("活动");


                    break;
                case TOPIC:
                    File cacheFile = CacheDirUtil.getCache(BWApplication.getInstance(),
                            "notetab_cache");

                    if (cacheFile != null) cacheFile.delete();
                    break;
                default:
                    break;
            }
            collectJob = true;
            if (iv != null)
                iv.setBackgroundResource(R.drawable.ic_collect);
            mIvLike.setBackgroundResource(R.drawable.ic_collect);

            ViewHub.showShortToast(mContext, "取消收藏");
            if (popCollectTxt != null)
                popCollectTxt.setText("收藏帖子");
        } else {
            collectJob = false;
            mIvLike.setBackgroundResource(R.drawable.ic_collected);
            if (iv != null)
                iv.setBackgroundResource(R.drawable.ic_collected);
            ViewHub.showShortToast(mContext, "收藏成功");
            if (popCollectTxt != null)
                popCollectTxt.setText("取消收藏");
        }
    }


    /**
     * @description 帖子详情加载完成
     * @created 2015-2-10 上午11:00:45
     * @author ZZB
     */
    private void onPostDetailLoaded(Object result) {
        if (!(result instanceof TopicDetailModel)) {

            return;
        }
        TopicDetailModel localDetail = (TopicDetailModel) result;
        if (localDetail.getCreateTime() == null) {
            return;
        }
        isCollected = localDetail.getIsCollect();


        if (mTopicDetailModel != null
                && TextUtils.equals(localDetail.getContent(),
                mTopicDetailModel.getContent())) {
            // 两次的帖子内容一样，不再加载webview
            mTopicDetailModel = localDetail;
        } else {
            mTopicDetailModel = localDetail;
//            StringBuffer html = new StringBuffer();
//            html.append("<html>"
//                    + "<meta http-equiv=\"Content-Type\" content=\"text/html;charset=utf-8\">"
//                    + "<head>"
//                    + "<style type=\"text/css\">"
//                    + ".wp-item-detail-format img,.wp-item-detail-format table{ max-width:100%;width:auto!important;height:auto!important; }"
//                    + "*{margin:0px; padding:0px;word-break:break-all;}"
//                    + "</style>" + "</head>" + "<body>"
//                    + "<div class=wp-item-detail-format>");
//            html.append(mTopicDetailModel.getContent());
//            html.append("</div>");
//            html.append("<script>" +
//                    "    function GoToRecharge() {" +
//                    "        window.wst.GoToRecharge()" +
//                    "    }" +
//                    "    function GoToItemDetail(str, str1) {" +
//                    "        window.wst.GoToItemDetail(str, str1)" +
//                    "    }" +
//                    "    function GoToQsList(str) {" +
//                    "        window.wst.GoToQsList(str)" +
//                    "    }" +
//                    "</script>");
//            html.append("</body></html>");
//            mWebView.loadDataWithBaseURL(null, html.toString(), "text/html",
//                    "UTF-8", null);
            StringBuffer html = new StringBuffer();
            html.append("<html>"
                    + "<meta http-equiv=\"Content-Type\" content=\"text/html;charset=utf-8\">"
                    + "<head>"
                    + "<style type=\"text/css\">"
                    + ".wp-item-detail-format.wp-item-detail-format table{ max-width:100%;width:auto!important;height:auto!important; }"
                    + "*{margin:0px; padding:0px;word-break:break-all;}"
                    + "</style>" + "</head>" + "<body>"
                    + "<div class=wp-item-detail-format>");
            html.append("<hr/>");
            html.append(mTopicDetailModel.getContent());
            html.append("</div>");
            html.append("<script>" +
                    "    function GoToRecharge() {" +
                    "        window.wst.GoToRecharge()" +
                    "    }" +
                    "    function GoToItemDetail(str, str1) {" +
                    "        window.wst.GoToItemDetail(str, str1)" +
                    "    }" +
                    "    function GoToQsList(str) {" +
                    "        window.wst.GoToQsList(str)" +
                    "    }"
                    +
                    "    function GoToGroupDetail(str, str1) {" +
                    "        window.wst.GoToGroupDetail(str, str1)" +
                    "    }"
                    +
                    "    function GoToUrl(str, str1) {" +
                    "        window.wst.GoToUrl(str, str1)" +
                    "    }"
                    +
                    "    function GoToItemCat(str, str1,str2) {" +
                    "        window.wst.GoToItemCat(str, str1,str2)" +
                    "    }"
                    +
                    "</script>");
            html.append("</body></html>");
            mWebView.loadDataWithBaseURL(null, html.toString(), "text/html",
                    "UTF-8", null);
        }
        tvPostTitle.setText(mTopicDetailModel.getTitle());
        int headSize = getResources().getDimensionPixelSize(
                R.dimen.msg_head_size);
        if (mLogoUrl.length() > 0) {
            Picasso.with(mContext).load(mLogoUrl).resize(headSize, headSize)
                    .into(mIvIcon);
        } else {
            String username = Const.getShopLogo(mTopicDetailModel.getUserID());
            username = ImageUrlExtends.getImageUrl(username, 3);
            Picasso.with(mContext).load(username).placeholder(R.drawable.shop_logo_normal1)
                    .into(mIvIcon);
        }
        mTvUserName.setText(mTopicDetailModel.getUserName());

     /*   mTvPostTime.setText(FunctionHelper.getFriendlyTime(mTopicDetailModel
                .getCreateTime()));*/
        mTvPostTime.setText(mTopicDetailModel.getTimeTips());
        //  updateLikeBtn(mTopicDetailModel.isIsLike());

        if (isCollected) {
            mIvLike.setBackgroundResource(R.drawable.ic_collected);
        } else {
            mIvLike.setBackgroundResource(R.drawable.ic_collect);
        }
        int likeCount = mTopicDetailModel.getLike();
        mTvGoods.setText(likeCount + "人赞过");
        if (likeCount > 0) {
            mLayoutLikes.removeAllViews();
            List<Long> likes = mTopicDetailModel.getLikeUsers();
            int i = 0;
            for (long like : likes) {
                if (i > 10) {
                    break;
                }
                populateLikes((int) like);
                i++;
            }
            mLayoutLikeUser.setVisibility(View.VISIBLE);
        } else {
            mLayoutLikeUser.setVisibility(View.GONE);
        }
        String activityTitleStr = null;
        if (mTopicDetailModel.getImages() != null) {
            activityTitleStr = "(" + mTopicDetailModel.getImages().size() + ")";
            List<String> images = mTopicDetailModel.getImages();
            Collections.reverse(images);
            ImgViewAdapter imgAdapter = new ImgViewAdapter(mContext, images);
            mActivityImgs.setAdapter(imgAdapter);
        }
        if (activityTitleView != null) {
            activityTitleStr = "活动图片" + activityTitleStr;
            activityTitleView.setText(activityTitleStr);
        }
        loadComments(true);
        new Task(Step.LOAD_SHOP_DATA).execute();
        if (!TextUtils.isEmpty(mTopicDetailModel.getWXCode())) {
            initTuanView();
            joinPersonView.setText(String.valueOf(mTopicDetailModel
                    .getJoinCount()));
            long endTime = TimeUtils.timeStampToMillis(
                    mTopicDetailModel.getToTime(), "yyyy-MM-dd kk:ss");
            if (endTime - System.currentTimeMillis() < 0) {
                nameActivityView.setEnabled(false);
                nameActivityView.setClickable(false);
                nameActivityView.setAlpha(0.2f);
                ((TextView) nameActivityView).setText("已结束");
                activityLeaveView.setText(null);
                activityLeaveLabel.setText(getLeaveTime(endTime));
                ((View) nameActivityView.getParent()).findViewById(
                        R.id.img_post_detail_time_icon)
                        .setVisibility(View.GONE);
            } else {
                activityLeaveView.setText(getLeaveTime(endTime));
            }
        }
        // zc 先屏蔽
        moreBtn.setVisibility(View.GONE);
    }

    /**
     * 初始化团购操作 create by 陈智勇Mar 26, 2015-11:03:19 AM
     */
    private void initTuanView() {
        nameActivityView.setOnClickListener(this);
        nameActivityView.setVisibility(View.VISIBLE);
        ((View) joinPersonView.getParent()).setVisibility(View.VISIBLE);
    }

    /***
     * 计算给定时间距离现在还有多久 create by 陈智勇Mar 25, 2015-11:03:22 AM
     *
     * @return
     */
    private String getLeaveTime(long endTime) {
        long leave = endTime - System.currentTimeMillis();
        if (leave < 0) {
            return TimeUtils.millisToTimestamp(endTime, "MM-dd");
        }
        leave = leave / 1000 / 60; // 小分钟
        if (leave < 60) {
            return leave + "分钟";
        }
        int hour = (int) (leave / 60);
        if (hour < 24) {
            return hour + "小时" + leave % 60 + "分钟";
        }
        return hour / 24 + "天" + hour % 24 + "小时";
    }

    /**
     * @description 显示赞
     * @created 2015-2-11 下午3:01:39
     * @author ZZB
     */
    private void populateLikes(int like) {
        int imgSize = DisplayUtil.dip2px(mContext, 35);
        FlowLayout.LayoutParams specLayoutParams = new FlowLayout.LayoutParams(
                DisplayUtil.dip2px(this, 4), DisplayUtil.dip2px(this, 4));
        ImageView img = new ImageView(this);
        img.setMinimumWidth(imgSize);
        img.setMinimumHeight(imgSize);
        img.setMaxWidth(imgSize);
        img.setMaxHeight(imgSize);
        img.setScaleType(ImageView.ScaleType.CENTER_CROP);
        String tmpUrl = Const.getShopLogo(like);
        String url = ImageUrlExtends.getImageUrl(tmpUrl, 3);
        if (!TextUtils.isEmpty(url)) {
            Picasso.with(mContext).load(url).resize(imgSize, imgSize).into(img);
        }
        mLayoutLikes.addView(img, specLayoutParams);
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

    private void loadComments(boolean isRefresh) {
//        if (mIsLoadingComments) {
//            mLvComments.onLoadMoreComplete();
//            return;
//        }
        mIsLoadingComments = true;
        mIsRefreshComment = isRefresh;
        if (isRefresh) {
            mLvComments.setCanLoadMore(true);
            mPageIndex = 1;
            load_post_comment(Step.LOAD_POST_COMMENT);
            //new Task(Step.LOAD_POST_COMMENT).execute();
        } else {
            mPageIndex++;
            load_post_comment(Step.LOAD_POST_COMMENT);
            //new Task(Step.LOAD_POST_COMMENT).execute();
        }
    }
}