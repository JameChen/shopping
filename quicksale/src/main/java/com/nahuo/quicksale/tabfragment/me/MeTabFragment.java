package com.nahuo.quicksale.tabfragment.me;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.luck.picture.lib.tools.ScreenUtils;
import com.nahuo.bean.BalanceBean;
import com.nahuo.bean.MenuRedPointBean;
import com.nahuo.bean.MsgRed;
import com.nahuo.bean.NoticeBean;
import com.nahuo.bean.OrderMeBean;
import com.nahuo.constant.UmengClick;
import com.nahuo.library.controls.CircleTextView;
import com.nahuo.library.controls.LoadingDialog;
import com.nahuo.library.helper.DisplayUtil;
import com.nahuo.library.helper.ImageUrlExtends;
import com.nahuo.quicksale.BGQDActivity;
import com.nahuo.quicksale.BaseInfoActivity;
import com.nahuo.quicksale.CommonSearchActivity;
import com.nahuo.quicksale.CouponActivity;
import com.nahuo.quicksale.MeSettingActivity;
import com.nahuo.quicksale.MyIncomeActivity;
import com.nahuo.quicksale.MyMainCollectionActivity;
import com.nahuo.quicksale.NewsActivity;
import com.nahuo.quicksale.OrderManageActivity;
import com.nahuo.quicksale.PHQDActivity;
import com.nahuo.quicksale.R;
import com.nahuo.quicksale.ScoreListActivity;
import com.nahuo.quicksale.SecuritySettingsActivity;
import com.nahuo.quicksale.ShareEntity;
import com.nahuo.quicksale.Topic.PostDetailActivity;
import com.nahuo.quicksale.ViewHub;
import com.nahuo.quicksale.activity.AfterSaleListActivity;
import com.nahuo.quicksale.activity.ItemPreview1Activity;
import com.nahuo.quicksale.activity.MainNewActivity;
import com.nahuo.quicksale.activity.SoonLeagueActivity;
import com.nahuo.quicksale.api.HttpUtils;
import com.nahuo.quicksale.api.OrderAPI;
import com.nahuo.quicksale.app.BWApplication;
import com.nahuo.quicksale.base.TabFragment;
import com.nahuo.quicksale.common.Const;
import com.nahuo.quicksale.common.ListUtils;
import com.nahuo.quicksale.common.NahuoNewShare;
import com.nahuo.quicksale.common.SpManager;
import com.nahuo.quicksale.common.Utils;
import com.nahuo.quicksale.customview.MarqueeTextView;
import com.nahuo.quicksale.customview.TopScrollView;
import com.nahuo.quicksale.di.module.HttpManager;
import com.nahuo.quicksale.eventbus.BusEvent;
import com.nahuo.quicksale.eventbus.EventBusId;
import com.nahuo.quicksale.model.http.CommonSubscriber;
import com.nahuo.quicksale.model.http.response.PinHuoResponse;
import com.nahuo.quicksale.oldermodel.AuthInfoModel;
import com.nahuo.quicksale.oldermodel.ScoreModel;
import com.nahuo.quicksale.tab.Constant;
import com.nahuo.quicksale.util.ActivityUtil;
import com.nahuo.quicksale.util.ChatUtl;
import com.nahuo.quicksale.util.CircleCarTxtUtl;
import com.nahuo.quicksale.util.GlideUtls;
import com.nahuo.quicksale.util.RxUtil;
import com.nahuo.quicksale.util.UMengTestUtls;
import com.squareup.picasso.Picasso;
import com.tencent.mm.opensdk.modelbiz.WXLaunchMiniProgram;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

import de.greenrobot.event.EventBus;
import de.hdodenhof.circleimageview.CircleImageView;

import static com.nahuo.quicksale.common.SpManager.getUserId;

/**
 * Created by 诚 on 2015/9/21.
 */
public class MeTabFragment extends TabFragment implements View.OnClickListener {//, OnTitleBarClickListener
    public static String TAG = MeTabFragment.class.getSimpleName();
    private Activity Vthis;
    private EventBus mEventBus = EventBus.getDefault();
    private CircleImageView mIvAvatar;
    private CircleTextView circle_car_text_chat;
    private LoadingDialog loadingDialog;
    private TextView tvScore;//积分
    private String url = "";
    // private MarqueeView tv_notifi_auto;
    private LinearLayout ll_notifi;
    //private MarqueeFactory<TextView, NoticeBean> marqueeFactory;
    private List<NoticeBean> list = new ArrayList<>();
    private MarqueeTextView tv_notifi_marquee;
    private List<String> slist = new ArrayList<>();
    private CircleTextView carCountTv;
    private View layout_explain;
    private View item_myorder;
    private int useId;
    private TopScrollView scroll_view;
    private ImageView iv_top_logobg;
    private RelativeLayout rlTContent, titleContent;
    private String pointName = "";
    private String Summary = "", Content = "", ShareTitle = "",
            ShareContent = "",
            ShareUrl = "";
    private RelativeLayout layout_head;
    private View mContentView, mViewCoin, layout_progress;
    private TextView tv_plan_tittle, tv_pd, tv_plane_content, tv_plane_finish;
    private ProgressBar me_pd;
    private boolean isIniit = false;
    private long orderRedID;
    private String taskUrl = "", taskTitle = "";
    private ProgressAsyncTask progressAsyncTask;
    private View rl_vip;
    private TextView tv_vip;
    private ImageView iv_vip;
    private String vip_url="";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Vthis = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContentView = inflater.inflate(R.layout.activity_qs_me, container, false);
        isIniit = true;
        initView();
        return mContentView;
    }

    @Override
    public void onResume() {
        super.onResume();
        //tv_notifi_auto.startFlipping();
        MainNewActivity.currFragTag = Constant.FRAGMENT_FLAG_ME;
        init();
    }

    boolean is_ShowDialog;

    public void init() {
        initdata();
        ChatUtl.setChatBroad(Vthis);
        // new LoadGoodsTask(this, carCountTv).execute();
        if (SpManager.getIs_Login(Vthis)) {
            if (!isHidden())
                is_ShowDialog = true;
            else
                is_ShowDialog = false;
            //new GetWaitPayCountTask(Step.NOTIFI).execute();
            //new GetWaitPayCountTask(Step.NUMS).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            // new GetWaitPayCountTask(Step.RENZHENG).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            //  new GetWaitPayCountTask(Step.SCORE).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            //getBalance();
            initScoreData();
            initAuthInfoStatuData();
            initNotifyData();
            initOrderCountData();
            getMenuRedPoint();
        }
    }

    /**
     * 获取菜单栏红点提醒
     *
     * @author James Chen
     * @create time in 2018/5/11 16:34
     */
    private void getMenuRedPoint() {
        addSubscribe(HttpManager.getInstance().getPinHuoNoCacheApi(TAG
        ).getMenuRedPoint()
                .compose(RxUtil.<PinHuoResponse<MenuRedPointBean>>rxSchedulerHelper())
                .compose(RxUtil.<MenuRedPointBean>handleResult())
                .subscribeWith(new CommonSubscriber<MenuRedPointBean>(Vthis) {
                    @Override
                    public void onNext(MenuRedPointBean menuRedPointBean) {
                        super.onNext(menuRedPointBean);
                        if (menuRedPointBean != null) {
                            orderRedID = menuRedPointBean.getOrderID();
                            long orderMaxID = SpManager.getQuickMeOrderMaxID(Vthis);
                            MsgRed msgRed = new MsgRed();
                            if (orderRedID > orderMaxID) {
                                msgRed.setIs_Show(true);
                                msgRed.setCount(0);
                            } else {
                                msgRed.setIs_Show(false);
                                msgRed.setCount(0);
                            }
                            mEventBus.post(BusEvent.getEvent(EventBusId.PINHUO_ME_RED_IS_SHOW, msgRed));
                        }
                    }

                }));
    }

    /**
     * 获取换货币
     *
     * @author James Chen
     * @create time in 2018/5/11 16:34
     */
    private void getBalance() {
        TreeMap<String, String> params = new TreeMap<String, String>();
        addSubscribe(HttpManager.getInstance().getPinHuoNoCacheApi(TAG
        ).getBalance()
                .compose(RxUtil.<PinHuoResponse<BalanceBean>>rxSchedulerHelper())
                .compose(RxUtil.<BalanceBean>handleResult())
                .subscribeWith(new CommonSubscriber<BalanceBean>(Vthis) {
                    @Override
                    public void onNext(BalanceBean balanceData) {
                        super.onNext(balanceData);
                        if (balanceData != null) {
                            SpManager.setBALANCE_COIN(Vthis, balanceData.getCoinBalance());
                            if (mViewCoin != null)
                                mViewCoin.setVisibility(View.VISIBLE);
                            if (balanceData.getCoinBalance() > 0)
                                setItemRightText(R.id.item_mycoin, balanceData.getCoinBalance() + "个");
                            else
                                setItemRightText(R.id.item_mycoin, "");
                        } else {
                            SpManager.setBALANCE_COIN(Vthis, 0);
                            if (mViewCoin != null)
                                mViewCoin.setVisibility(View.GONE);
                            setItemRightText(R.id.item_mycoin, "");
                        }
                    }

                }));
    }

    private void initNotifyData() {
        addSubscribe(HttpManager.getInstance().getPinHuoNoCacheApi(TAG).getCurrentNotice(com.nahuo.constant.Constant.NotifyAreaID.NotifyAreaID_Me)
                .compose(RxUtil.<PinHuoResponse<List<NoticeBean>>>rxSchedulerHelper())
                .compose(RxUtil.<List<NoticeBean>>handleResult())
                .subscribeWith(new CommonSubscriber<List<NoticeBean>>(Vthis, is_ShowDialog, R.string.loading) {
                    @Override
                    public void onNext(List<NoticeBean> noticeBeen) {
                        super.onNext(noticeBeen);
                        list.clear();
                        slist.clear();
                        List<NoticeBean> data = noticeBeen;
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, Vthis.getResources().getDimensionPixelOffset(R.dimen.me_item_new_height));
                        params.topMargin = -Vthis.getResources().getDimensionPixelOffset(R.dimen.me_item_new_height) / 2;
                        params.leftMargin = Vthis.getResources().getDimensionPixelOffset(R.dimen.activity_horizontal_new_margin);
                        params.rightMargin = Vthis.getResources().getDimensionPixelOffset(R.dimen.activity_horizontal_new_margin);
                        LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, Vthis.getResources().getDimensionPixelOffset(R.dimen.me_item_new_height));
                        params1.topMargin = 0;
                        params1.leftMargin = Vthis.getResources().getDimensionPixelOffset(R.dimen.activity_horizontal_new_margin);
                        params1.rightMargin = Vthis.getResources().getDimensionPixelOffset(R.dimen.activity_horizontal_new_margin);

                        if (data != null && data.size() > 0) {
                            ll_notifi.setVisibility(View.VISIBLE);
                            EventBus.getDefault().post(BusEvent.getEvent(EventBusId.PINHUO_TAB_BOTTOM_SQME_LAYOUT_HEIGHT));
                            for (int i = 0; i < data.size(); i++) {
                                NoticeBean bean = data.get(i);
                                bean.setNums_content(i + 1 + ": " + bean.getTitle());
                                list.add(bean);
                                slist.add(i + 1 + ": " + bean.getTitle());
                            }
                            tv_notifi_marquee.setContentList(slist);
                            item_myorder.setLayoutParams(params1);
                        } else {
                            ll_notifi.setVisibility(View.GONE);
                            item_myorder.setLayoutParams(params);
                        }
                    }
                }));
    }

    private void initScoreData() {
        addSubscribe(HttpManager.getInstance().getPinHuoNoCacheApi(TAG).getMyUserInfo()
                .compose(RxUtil.<PinHuoResponse<ScoreModel>>rxSchedulerHelper())
                .compose(RxUtil.<ScoreModel>handleResult())
                .subscribeWith(new CommonSubscriber<ScoreModel>(Vthis, is_ShowDialog, R.string.loading) {
                    @Override
                    public void onNext(ScoreModel scoreModel) {
                        super.onNext(scoreModel);
                        ScoreModel data = scoreModel;
                        if (data != null) {
                            if (data.getVip()!=null){
                                vip_url=data.getVip().getUrl();
                                if (data.getVip().isIsShow()){
                                    if (rl_vip!=null)
                                    rl_vip.setVisibility(View.VISIBLE);
                                    if (tv_vip!=null){
                                        tv_vip.setText(data.getVip().getTipText());
                                    }
                                    if (iv_vip!=null){
                                        GlideUtls.glidePic(Vthis,data.getVip().getPic(),iv_vip,R.drawable.vip1);
                                    }

                                }else {
                                    if (rl_vip!=null)
                                        rl_vip.setVisibility(View.GONE);
                                }
                            }else {
                                if (rl_vip!=null)
                                    rl_vip.setVisibility(View.GONE);
                            }
                            if (data.getAuthInfo() != null) {
                                int statuId = data.getAuthInfo().getStatuID();
                                SpManager.setStatuId(Vthis, statuId);
                            }
                            pointName = data.getPointName();
                            int score = data.getPoint();
                            if (score < 0) {
                                SpManager.setScore(Vthis, 0);
                                score = 0;
                            } else {
                                SpManager.setScore(Vthis, score);
                            }
                            tvScore.setText(pointName + ": " + score);
                        }
                    }
                }));
    }

    private void initAuthInfoStatuData() {
        addSubscribe(HttpManager.getInstance().getPinHuoNoCacheApi(TAG).getAuthInfoStatu()
                .compose(RxUtil.<PinHuoResponse<AuthInfoModel>>rxSchedulerHelper())
                .compose(RxUtil.<AuthInfoModel>handleResult())
                .subscribeWith(new CommonSubscriber<AuthInfoModel>(Vthis, is_ShowDialog, R.string.loading) {
                    @Override
                    public void onNext(AuthInfoModel authInfoModel) {
                        super.onNext(authInfoModel);
                        AuthInfoModel data = authInfoModel;
                        if (data.getAuthInfo() != null) {
                            setItemRightText(R.id.item_myrenzheng, data.getAuthInfo().getStatu());
                        } else {
                            ViewHub.showLongToast(Vthis, "未找到认证状态");
                        }
                    }
                }));
    }

    private void initOrderCountData() {
        addSubscribe(HttpManager.getInstance().getPinHuoNoCacheApi(TAG).getOrderStatuCount()
                .compose(RxUtil.<PinHuoResponse<OrderMeBean>>rxSchedulerHelper())
                .compose(RxUtil.<OrderMeBean>handleResult())
                .subscribeWith(new CommonSubscriber<OrderMeBean>(Vthis, is_ShowDialog, R.string.loading) {
                    @Override
                    public void onNext(OrderMeBean orderMeBean) {
                        super.onNext(orderMeBean);
                        setOrdeNums(orderMeBean);
                    }
                }));
    }

    private class ProgressAsyncTask extends AsyncTask<Integer, Integer, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }


        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            try {
                if (isCancelled())
                    return;
                if (me_pd != null)
                    me_pd.setProgress(values[0]);
                if (values[2] == 1) {
                    if (tv_pd != null) {
                        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                        params.addRule(RelativeLayout.CENTER_VERTICAL);
                        params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                        params.leftMargin = 0;
                        tv_pd.setLayoutParams(params);
                    }
                } else {
                    if (tv_pd != null) {
                        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                        params.addRule(RelativeLayout.CENTER_VERTICAL);
                        params.leftMargin = values[1];
                        tv_pd.setLayoutParams(params);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(Integer... params) {
            if (isCancelled())
                return null;
            int pro = params[0];
            int p = params[3];
            if (pro >= p) {
                for (int i = p; i <= pro; i++) {
                    double f = (double) i / 100;
                    int pW = params[2];
                    int tW = params[1];
                    int marigin = (int) (f * pW);
                    int left = 0;
                    int full = 0;
                    if (pW - marigin <= tW) {
                        left = pW - tW;
                        full = 1;
                    } else {
                        left = marigin;
                        full = 0;
                    }
                    if (left <= 0)
                        left = 0;
                    publishProgress(i, left, full);
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                for (int i = p; i >= pro; i--) {
                    double f = (double) i / 100;
                    int pW = params[2];
                    int tW = params[1];
                    int marigin = (int) (f * pW);
                    int left = 0;
                    int full = 0;
                    if (pW - marigin <= tW) {
                        left = pW - tW;
                        full = 1;
                    } else {
                        left = marigin;
                        full = 0;
                    }
                    if (left <= 0)
                        left = 0;
                    publishProgress(i, left, full);
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

            return null;
        }
    }

    private void setOrdeNums(OrderMeBean orderMeBean) {
        int waitPayCount = 0;
        int waitShipCount = 0;
        int shipedCount = 0;
        int overedCount = 0;
        int cancelCount = 0;
        int readyCount = 0;
        int saleAfterCount = 0;
        try {
            if (orderMeBean != null) {
                final OrderMeBean.TaskBean taskBean = orderMeBean.getTask();
                if (taskBean != null) {
                    taskUrl = taskBean.getUrl();
                    taskTitle = taskBean.getTitle();

                    if (tv_plan_tittle != null)
                        tv_plan_tittle.setText(taskBean.getTitle());
                    if (tv_plane_content != null)
                        tv_plane_content.setText(taskBean.getSummary());
                    if (tv_pd != null)
                        tv_pd.setText(taskBean.getFinishText());
                    if (tv_plane_finish != null)
                        tv_plane_finish.setText(taskBean.getPlanText());
                    if (taskBean.isIsShow()) {
                        if (layout_progress != null)
                            layout_progress.setVisibility(View.VISIBLE);
                        if (tv_pd != null && me_pd != null) {
                            tv_pd.post(new Runnable() {
                                @Override
                                public void run() {
                                    int tw = tv_pd.getWidth();
                                    int pw = me_pd.getWidth();
                                    // taskBean.setRate(Math.random() + "");
                                    int p = (new Double(Double.parseDouble(taskBean.getRate()) * 100)).intValue();
                                    cancekTask();
                                    progressAsyncTask = new ProgressAsyncTask();
                                    progressAsyncTask.execute(p, tw, pw, me_pd.getProgress());
                                }
                            });
                        }
                    } else {
                        if (layout_progress != null)
                            layout_progress.setVisibility(View.GONE);
                        if (tv_pd != null) {
                            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                            params.addRule(RelativeLayout.CENTER_VERTICAL);
                            params.leftMargin = 0;
                            tv_pd.setLayoutParams(params);
                        }
                        if (me_pd != null)
                            me_pd.setProgress(0);
                    }

                } else {
                    if (layout_progress != null)
                        layout_progress.setVisibility(View.GONE);
                    if (tv_pd != null) {
                        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                        params.addRule(RelativeLayout.CENTER_VERTICAL);
                        params.leftMargin = 0;
                        tv_pd.setLayoutParams(params);
                    }
                    if (me_pd != null)
                        me_pd.setProgress(0);
                }
                readyCount = orderMeBean.getReadyCount();
                String coin = orderMeBean.getCoins();
                if (orderMeBean.isOpenLeague()) {
                    if (mViewCoin != null)
                        mViewCoin.setVisibility(View.VISIBLE);
                } else {
                    if (mViewCoin != null)
                        mViewCoin.setVisibility(View.GONE);
                }
//                if (TextUtils.isEmpty(coin) || coin.equals("0") || coin.equals("0.0") || coin.equals("0.00")) {
//                    setItemRightText(R.id.item_mycoin, "");
//                } else {
//                    setItemRightText(R.id.item_mycoin, coin + "个");
//                }
                setItemRightText(R.id.item_mycoin, coin + "个");
            }
            if (!ListUtils.isEmpty(orderMeBean.getOrderStatuList())) {
                for (int i = 0; i < orderMeBean.getOrderStatuList().size(); i++) {
                    OrderMeBean.OrderStatuListBean orderStatuListBean = orderMeBean.getOrderStatuList().get(i);
                    switch (orderStatuListBean.getStatuID()) {
                        case Const.OrderStatus.WAIT_PAY:
                            waitPayCount = orderStatuListBean.getAmount();
                            break;
                        case Const.OrderStatus.SHIPED:
                            shipedCount = orderStatuListBean.getAmount();
                            break;
                        case Const.OrderStatus.DONE:
                            overedCount = orderStatuListBean.getAmount();
                            break;
                        case Const.OrderStatus.CANCEL:
                            cancelCount = orderStatuListBean.getAmount();
                            break;
                        case Const.OrderStatus.WAIT_SHIP:
                            waitShipCount = orderStatuListBean.getAmount();
                            break;
                        case Const.OrderStatus.REFUND:
                            saleAfterCount = orderStatuListBean.getAmount();
                            break;
                    }
                }
            }
            OrderMeBean.AvailableCouponInfoBean availableCouponInfoBean = orderMeBean.getAvailableCouponInfo();
            if (availableCouponInfoBean != null) {
                Avail_MaxID = availableCouponInfoBean.getMaxID();
                Avail_Total = availableCouponInfoBean.getTotal();
                long id = SpManager.getQuickMeCouponMaxID(Vthis);
                if (Avail_Total > 0) {
                    setItemRightText(R.id.item_mycoupon, Avail_Total + "张可用");
                } else {
                    setItemRightText(R.id.item_mycoupon, "");
                    setItemRightRegent(R.id.item_mycoupon, "", false, 40, 18);
                }
                if (id < Avail_MaxID) {
                    // SpManager.setQuickMeCouponMaxID(this, Avail_MaxID);
                    if (Avail_Total > 0) {
//                        MsgRed msgRed = new MsgRed();
//                        msgRed.setIs_Show(true);
//                        msgRed.setCount(Avail_Total);
//                        mEventBus.post(BusEvent.getEvent(EventBusId.PINHUO_ME_RED_IS_SHOW, msgRed));
                        // setItemRightText(R.id.item_mycoupon, Avail_Total + "张可用");
                        setItemRightRegent(R.id.item_mycoupon, "new", true, 40, 18);
                    }
                }
            }
            OrderMeBean.PromoteBean promoteBean = orderMeBean.getPromote();
            if (promoteBean != null) {
                Summary = promoteBean.getSummary();
                Content = promoteBean.getContent();
                ShareTitle = promoteBean.getShareTitle();
                ShareContent = promoteBean.getShareContent();
                ShareUrl = promoteBean.getShareUrl();
                if (!TextUtils.isEmpty(Summary)) {
                    setItemRightText(R.id.item_me_invite_register, Summary);
                }
            }
            if (saleAfterCount > 0) {
                setItemSaleAfterRightCircle(R.id.circle_order_sale_after, saleAfterCount + "", true);
            } else {
                setItemSaleAfterRightCircle(R.id.circle_order_sale_after, saleAfterCount + "", false);
            }
            if (readyCount > 0) {
                setItemRightCircle(R.id.item_my_goto_tuan, readyCount + "", true);
            } else {
                setItemRightCircle(R.id.item_my_goto_tuan, readyCount + "", false);
            }
            if (waitPayCount > 0) {
                ((CircleTextView) mContentView.findViewById(R.id.circle_order_text0)).setText(waitPayCount + "");
                mContentView.findViewById(R.id.circle_order_text0).setVisibility(View.VISIBLE);
            } else {
                mContentView.findViewById(R.id.circle_order_text0).setVisibility(View.GONE);
            }
            if (waitShipCount > 0) {
                ((CircleTextView) mContentView.findViewById(R.id.circle_order_text1)).setText(waitShipCount + "");
                mContentView.findViewById(R.id.circle_order_text1).setVisibility(View.VISIBLE);
            } else {
                mContentView.findViewById(R.id.circle_order_text1).setVisibility(View.GONE);
            }
            if (shipedCount > 0) {
                ((CircleTextView) mContentView.findViewById(R.id.circle_order_text2)).setText(shipedCount + "");
                mContentView.findViewById(R.id.circle_order_text2).setVisibility(View.VISIBLE);
            } else {
                mContentView.findViewById(R.id.circle_order_text2).setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void cancekTask() {
        if(progressAsyncTask !=null && progressAsyncTask.getStatus() == AsyncTask.Status.RUNNING){
            progressAsyncTask.cancel(true);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //  mEventBus.unregister(this);
        try {
            if (loadingDialog != null) {
                if (loadingDialog.isShowing()) {
                    loadingDialog.stop();
                }
            }
        } catch (Exception e) {
            loadingDialog = null;
        }
    }

    public void onEventMainThread(BusEvent event) {
        switch (event.id) {
            case EventBusId.WEIXUN_NEW_MSG:
                if (circle_car_text_chat == null) {
                    return;
                }
                String num = event.data.toString();
                if (TextUtils.isEmpty(num)) {
                    circle_car_text_chat.setVisibility(View.GONE);
                } else {
                    circle_car_text_chat.setVisibility(View.VISIBLE);
                    circle_car_text_chat.setText(event.data.toString());
                }
                break;
            case EventBusId.SHOP_LOGO_UPDATED:// 店铺logo有修改
                if (this == null && Vthis == null && isIniit == false)
                    return;
                String logo = SpManager.getShopLogo(Vthis);
                if (!TextUtils.isEmpty(logo)) {
                    String url = ImageUrlExtends.getImageUrl(logo, Const.LIST_HEADER_COVER_SIZE);
                    Picasso.with(Vthis).load(url).skipMemoryCache().placeholder(R.drawable.empty_photo).into(mIvAvatar);
                    initLogoBgView(url);
                }
            case EventBusId.REFRESH_MEFRAGMENT:
                if (this != null && Vthis != null && isIniit == true)
                    init();
                break;
        }
    }

    TextView name;

    private void initdata() {
        String shopName = SpManager.getShopName(BWApplication.getInstance());
        String userName = SpManager.getUserName(BWApplication.getInstance());
        String nickName = SpManager.getNICKNAME(BWApplication.getInstance());
        String logo = SpManager.getShopLogo(BWApplication.getInstance()).trim();// SpManager.getUserLogo(NHApplication.getInstance());
        if (mIvAvatar != null) {
            mIvAvatar.setBorderWidth(DisplayUtil.dip2px(Vthis, 2));
            mIvAvatar.setBorderColor(getResources().getColor(R.color.white));
        }
        if (TextUtils.isEmpty(logo)) {
            url = Const.getShopLogo(getUserId(BWApplication.getInstance()));
        } else {
            url = ImageUrlExtends.getImageUrl(logo, 8);
        }
        if (mIvAvatar != null)
            Picasso.with(BWApplication.getInstance()).load(url)
                    .placeholder(R.drawable.empty_photo1).into(mIvAvatar);
        initLogoBgView(url);
        if (HttpUtils.IS_LOCAL) {
            if (name != null)
                name.setText(nickName + "(内网版)");
        } else {
            if (name != null)
                name.setText(nickName);
        }

//        new GetWaitPayCountTask(Step.NUMS).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
//        new GetWaitPayCountTask(Step.RENZHENG).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
//        new GetWaitPayCountTask(Step.SCORE).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private void initLogoBgView(String url) {
//        ((ImageView)Vthis.findViewById(R.id.iv_logobg)).setImageBitmap(ImageTools.blurBitmap(Vthis,mIvAvatar.get));
//        RequestOptions options = new RequestOptions()
//                .centerCrop()
//                .placeholder(R.drawable.sq_me_bg)
//                .diskCacheStrategy(DiskCacheStrategy.ALL);
//        Glide.with(this)
//                .load(url)
//                .apply(options)
//                .into((ImageView)findViewById(R.id.iv_logobg));
//        Glide.with(this)
//                .load(url)
//                .apply(options)
//                .into((ImageView)findViewById(R.id.iv_logobg1));
        if (Vthis.findViewById(R.id.iv_logobg) != null)
//            Picasso.with(BWApplication.getInstance())
//                    .load(url)
//                    .transform(new BlurTransform(80))
//                    .skipMemoryCache()
//                    .placeholder(R.drawable.sq_me_bg)
//                    .error(R.drawable.sq_me_bg).into((ImageView) Vthis.findViewById(R.id.iv_logobg));
            ((ImageView) Vthis.findViewById(R.id.iv_logobg)).setImageResource(R.drawable.sq_me_bg);
//        Picasso.with(BWApplication.getInstance())
//                .load(url)
//                .transform(new BlurTransform(80))
//                .error(R.drawable.sq_me_bg).skipMemoryCache()
//                .placeholder(R.drawable.sq_me_bg).into((ImageView) Vthis.findViewById(R.id.iv_logobg1));
        if (iv_top_logobg != null)
            iv_top_logobg.setImageResource(R.drawable.sq_me_bg);
    }

//    public class NoticeMF extends MarqueeFactory<TextView, NoticeBean> {
//        private LayoutInflater inflater;
//
//        public NoticeMF(Context mContext) {
//            super(mContext);
//            inflater = LayoutInflater.from(mContext);
//        }
//
//        @Override
//        public TextView generateMarqueeItemView(NoticeBean data) {
//            TextView mView = (TextView) inflater.inflate(R.layout.notice_item, null);
////            String mess = data.getTitle();
////            String num = data.getNums() + "";
////            if (!TextUtils.isEmpty(mess))
////                mView.setText(num + ": " + mess);
//            return mView;
//        }
//    }
//

    @Override
    public void onPause() {
        super.onPause();
        cancekTask();
//        tv_notifi_auto.stopFlipping();

    }

    int sq_me_col;
    String msg_guanzhu = "关注足迹", msg_my_goto_tuan = "即将成团", msg_my_yft = "我的钱包", msg_mybill = "商品账单", msg_mypostfee = "天天账单", msg_item_phqd = "配货清单", msg_item_bgqd = "包裹清单", msg_item_me_invite_register = "邀请店主注册", msg_item_myrenzheng = "店铺认证", msg_item_myhelp = "帮助中心";

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // mEventBus.register(this);
    }

    private void initView() {
        rl_vip=mContentView.findViewById(R.id.rl_vip);
        rl_vip.setOnClickListener(this);
        iv_vip=(ImageView) mContentView.findViewById(R.id.iv_vip);
        tv_vip=(TextView) mContentView.findViewById(R.id.tv_vip);
        name = (TextView) mContentView.findViewById(R.id.txt_name);
        mIvAvatar = (CircleImageView) mContentView.findViewById(R.id.iv_userhead);
        mIvAvatar.setOnClickListener(this);
        loadingDialog = new LoadingDialog(Vthis);
        iv_top_logobg = (ImageView) mContentView.findViewById(R.id.iv_logobg1);
        iv_top_logobg.setAlpha(0);
        rlTContent = (RelativeLayout) mContentView.findViewById(R.id.rl_top);
        titleContent = (RelativeLayout) mContentView.findViewById(R.id.rl_top1);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            titleContent.setPadding(0, ScreenUtils.getStatusBarHeight(Vthis), 0, 0);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, ScreenUtils.dip2px(Vthis, DisplayUtil.getResDimen(Vthis, R.dimen.titlebar_height)) + ScreenUtils.getStatusBarHeight(Vthis));
            iv_top_logobg.setLayoutParams(params);
        } else {
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, ScreenUtils.dip2px(Vthis, DisplayUtil.getResDimen(Vthis, R.dimen.titlebar_height)));
            iv_top_logobg.setLayoutParams(params);
        }
        mContentView.findViewById(R.id.iv_shopping_cart).setOnClickListener(this);
        mContentView.findViewById(R.id.iv_all_search).setOnClickListener(this);
        carCountTv = (CircleTextView) mContentView.findViewById(R.id.circle_car_text);
        circle_car_text_chat = (CircleTextView) mContentView.findViewById(R.id.circle_car_text_chat);
        layout_head = (RelativeLayout) mContentView.findViewById(R.id.layout_head);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ScreenUtils.getScreenHeight(getActivity()) * 1 / 3);
        layout_head.setLayoutParams(params);
        CircleCarTxtUtl.setColor(circle_car_text_chat);
        scroll_view = (TopScrollView) mContentView.findViewById(R.id.scroll_view);
//        findViewById(R.id.my_share).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent mes = new Intent(Vthis, ShareActivity.class);
//                startActivity(mes);
//            }
//        });
        mContentView.findViewById(R.id.my_share).setOnClickListener(this);
        mContentView.findViewById(R.id.my_setting).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mes = new Intent(Vthis, MeSettingActivity.class);
                startActivity(mes);
            }
        });
        mContentView.findViewById(R.id.iv_chat_txt).setOnClickListener(this);
        scroll_view.setOnScrollChangedListener(new TopScrollView.OnScrollChangedListener() {
            @Override
            public void onScrollChanged(ScrollView who, int x, int y, int oldx, int oldy) {
                if (y >= rlTContent.getHeight()
                        && y < 2 * rlTContent.getHeight()) {
                    y = y - rlTContent.getHeight();
                } else if (y >= 2 * rlTContent.getHeight()) {
                    y = rlTContent.getHeight();
                } else if (y < rlTContent.getHeight()) {
                    y = 0;
                }
                iv_top_logobg.setAlpha(y * 255
                        / rlTContent.getHeight());

//                tv_title_top.setAlpha(y * 255
//                        / rlTContent.getHeight());
             /*   tv_rigt_bg.setAlpha(rlTContent.getHeight() / (y + 1)
                );
                tv_left_bg.setAlpha(rlTContent.getHeight() / (y + 1));
                if (Integer.parseInt(android.os.Build.VERSION.SDK) >= 16) {
                    mDrawable.setAlpha(y * 255 / rlTContent.getHeight());
                }*/
            }
        });
        layout_explain = mContentView.findViewById(R.id.layout_explain);
//        useId = SpManager.getUserId(BWApplication.getInstance());
//        if (useId>0) {
//            GuidePreference.init(this);
//            sq_me_col = GuidePreference.getInstance().geSqMe_Cole(useId + "");
//            if (sq_me_col == 1) {
//                layout_explain.setVisibility(View.GONE);
//            } else {
//                layout_explain.setVisibility(View.VISIBLE);
//            }
//        }
//            layout_explain.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    layout_explain.setVisibility(View.GONE);
//                    GuidePreference.getInstance().setSqMe_Cole(useId + "", 1);
//                    mEventBus.post(BusEvent.getEvent(EventBusId.PINHUO_TAB_BOTTOM_LAYOUT));
//                }
//            });
        layout_progress = mContentView.findViewById(R.id.layout_progress);
        layout_progress.setOnClickListener(this);
        tv_plan_tittle = (TextView) mContentView.findViewById(R.id.tv_plan_tittle);
        tv_pd = (TextView) mContentView.findViewById(R.id.tv_pd);
        tv_plane_content = (TextView) mContentView.findViewById(R.id.tv_plane_content);
        tv_plane_finish = (TextView) mContentView.findViewById(R.id.tv_plane_finish);
        me_pd = (ProgressBar) mContentView.findViewById(R.id.me_pd);

        initMarquee();
        ll_notifi = (LinearLayout) mContentView.findViewById(R.id.ll_notifi);
        initItem(R.id.item_myorder, "我的订单", false);
        item_myorder = mContentView.findViewById(R.id.item_myorder);
        setItemRightText(R.id.item_myorder, "查看全部订单");
        setItemLeftImageView(R.id.item_myorder, false);
        initItem(R.id.item_myyft, msg_my_yft, false);
        setItemLeftImageView(R.id.item_myyft, R.drawable.sq_me_item_03);
        setItemRightText(R.id.item_myyft, "充值、提现等");
        initItem(R.id.item_mycoupon, "优惠券", false);
        setItemLeftImageView(R.id.item_mycoupon, R.drawable.sq_me_item_04);
        mViewCoin = mContentView.findViewById(R.id.item_mycoin);
        initItem(R.id.item_mycoin, "换货币", false);
        setItemLeftImageView(R.id.item_mycoin, R.drawable.sq_me_item_12);
        initItem(R.id.item_mybill, msg_mybill, false);
        setItemLeftImageView(R.id.item_mybill, R.drawable.sq_me_item_05);
        setItemRightText(R.id.item_mybill, "每日进货退款明细");

        initItem(R.id.item_mypostfee, msg_mypostfee, true);
        setItemLeftImageView(R.id.item_mypostfee, R.drawable.sq_me_item_06);
        setItemRightText(R.id.item_mypostfee, "");
        initItem(R.id.item_phqd, msg_item_phqd, false);
        setItemLeftImageView(R.id.item_phqd, R.drawable.sq_me_item_07);
        setItemRightText(R.id.item_phqd, "已配商品、设置发货时间");
        initItem(R.id.item_bgqd, msg_item_bgqd, true);
        setItemLeftImageView(R.id.item_bgqd, R.drawable.sq_me_item_08);
        setItemRightText(R.id.item_bgqd, "查看快递包裹明细");
        initItem(R.id.item_mytuan, msg_guanzhu, false);
        setItemLeftImageView(R.id.item_mytuan, R.drawable.sq_me_item_01);
        setItemRightText(R.id.item_mytuan, "关注的团和商品、足迹");
        initItem(R.id.item_my_goto_tuan, msg_my_goto_tuan, true);
        setItemLeftImageView(R.id.item_my_goto_tuan, R.drawable.sq_me_item_02);
        setItemRightText(R.id.item_my_goto_tuan, "及时了解拼单中的商品");
        //邀请店主注册
        initItem(R.id.item_me_invite_register, msg_item_me_invite_register, false);
        setItemLeftImageView(R.id.item_me_invite_register, R.drawable.sq_me_item_09);
        initItem(R.id.item_myrenzheng, msg_item_myrenzheng, false);
        setItemLeftImageView(R.id.item_myrenzheng, R.drawable.sq_me_item_10);
        //initItem(R.id.item_setting_security, "安全设置", true);
        initItem(R.id.item_myhelp, msg_item_myhelp, true);
        setItemLeftImageView(R.id.item_myhelp, R.drawable.sq_me_item_11);
        // initItem(R.id.item_about, "关于我们", true);
        // setItemLeftImageView(R.id.item_about, R.drawable.icon_my_about);

        mContentView.findViewById(R.id.my_order_pay).setOnClickListener(this);
        mContentView.findViewById(R.id.my_order_ship).setOnClickListener(this);
        mContentView.findViewById(R.id.my_order_get).setOnClickListener(this);
        mContentView.findViewById(R.id.my_order_overed).setOnClickListener(this);
        mContentView.findViewById(R.id.my_order_cancel).setOnClickListener(this);
        mContentView.findViewById(R.id.my_order_refund).setOnClickListener(this);
        tvScore = (TextView) mContentView.findViewById(R.id.tvScore);
        if (SpManager.getScore(Vthis) < 0) {
            SpManager.setScore(Vthis, 0);
        }
        tvScore.setText("活跃分: " + SpManager.getScore(Vthis));
        tvScore.setOnClickListener(this);
    }

    private void initMarquee() {
        tv_notifi_marquee = (MarqueeTextView) mContentView.findViewById(R.id.tv_notifi_marquee);
        tv_notifi_marquee.setVerticalSwitchSpeed(1000);
        tv_notifi_marquee.setHorizontalScrollSpeed(200);
        tv_notifi_marquee.setOnItemOnClickListener(new MarqueeTextView.OnItemClickListener() {
            @Override
            public void onItemclick(int index) {
                NoticeBean bean = list.get(index);
                String target = bean.getTarget();
                if (!TextUtils.isEmpty(target)) {

                    switch (target){
                        case "activity":
                            Intent intent = new Intent(Vthis, PostDetailActivity.class);
                            intent.putExtra(PostDetailActivity.EXTRA_TID, bean.getTargetID());
                            intent.putExtra(PostDetailActivity.EXTRA_LOGO_URL,
                                    url);
                            intent.putExtra(PostDetailActivity.EXTRA_POST_TITLE, bean.getTitle());
                            intent.putExtra(PostDetailActivity.EXTRA_POST_TYPE,
                                    Const.PostType.ACTIVITY);
                            Vthis.startActivity(intent);
                            break;
                        case "topic":
                            Intent intent1 = new Intent(Vthis, PostDetailActivity.class);
                            intent1.putExtra(PostDetailActivity.EXTRA_TID, bean.getTargetID());
                            intent1.putExtra(PostDetailActivity.EXTRA_LOGO_URL,
                                    url);
                            intent1.putExtra(PostDetailActivity.EXTRA_POST_TITLE, bean.getTitle());
                            intent1.putExtra(PostDetailActivity.EXTRA_POST_TYPE,
                                    Const.PostType.TOPIC);
                            Vthis.startActivity(intent1);
                            break;
                        case "schedule":
                            ActivityUtil.goToChangCiActivity(Vthis,bean.getTargetID());
                            break;
                        case "goods":
                            ActivityUtil.goToItemDtailActivity(Vthis,bean.getTargetID());
                            break;
                    }

                }
            }
        });
//        tv_notifi_auto = (MarqueeView) findViewById(R.id.tv_notifi);
//        marqueeFactory = new NoticeMF(this);
//        tv_notifi_auto.setMarqueeFactory(marqueeFactory);
//        marqueeFactory.setOnItemClickListener(new MarqueeFactory.OnItemClickListener<TextView, NoticeBean>() {
//            @Override
//            public void onItemClickListener(MarqueeFactory.ViewHolder<TextView, NoticeBean> holder) {
//                // Toast.makeText(Sq_meAcivity.this, holder.data.getID() + "", Toast.LENGTH_SHORT).show();
//                String target = holder.data.getTarget();
//                if (!TextUtils.isEmpty(target)) {
//                    Intent intent = new Intent(Vthis, PostDetailActivity.class);
//                    intent.putExtra(PostDetailActivity.EXTRA_TID, holder.data.getTargetID());
//                    intent.putExtra(PostDetailActivity.EXTRA_LOGO_URL,
//                            url);
//                    intent.putExtra(PostDetailActivity.EXTRA_POST_TITLE, holder.data.getTitle());
//                    if (target.equals("activity")) {
//                        intent.putExtra(PostDetailActivity.EXTRA_POST_TYPE,
//                                Const.PostType.ACTIVITY);
//                    }
//                    if (target.equals("topic")) {
//                        intent.putExtra(PostDetailActivity.EXTRA_POST_TYPE,
//                                Const.PostType.TOPIC);
//                    }
//                    Vthis.startActivity(intent);
//                }
//            }
//        });
//        tv_notifi_auto.startFlipping();
//        marqueeFactory.setData(list);
    }

    private void initItem(int viewId, String text, boolean b) {
        View v = mContentView.findViewById(viewId);
        v.setOnClickListener(this);
        TextView tv = (TextView) v.findViewById(R.id.tv_left_text);
        ImageView ivLeftIcon = (ImageView) v.findViewById(R.id.iv_left_icon);
        View bBtmLine = v.findViewById(R.id.view_btm_line);
        if (!b) {
            bBtmLine.setVisibility(View.VISIBLE);
        } else {
            bBtmLine.setVisibility(View.GONE);
        }

        tv.setText(text);
        ivLeftIcon.setVisibility(View.GONE);

    }

    private void setItemRightText(int viewId, String text) {
        if (mContentView != null) {
            View v = mContentView.findViewById(viewId);
            if (v != null) {
                TextView tv = (TextView) v.findViewById(R.id.tv_right_text);
                if (tv != null)
                    tv.setText(text);
            }
        }
    }

    private void setItemRightRegent(int viewId, String text, boolean Visable, int width, int height) {
        View v = mContentView.findViewById(viewId);
        TextView tv = (TextView) v.findViewById(R.id.regent_order_text);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ScreenUtils.dip2px(Vthis, width), ScreenUtils.dip2px(Vthis, height));
        layoutParams.addRule(RelativeLayout.RIGHT_OF, R.id.tv_left_text);
        layoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
        layoutParams.leftMargin = ScreenUtils.dip2px(Vthis, 10);
        tv.setLayoutParams(layoutParams);
        if (Visable) {
            tv.setText(text);
            tv.setVisibility(View.VISIBLE);
        } else {
            tv.setVisibility(View.GONE);
        }
    }

    private void setItemRightCircle(int viewId, String text, boolean Visable) {
        View v = mContentView.findViewById(viewId);
        CircleTextView tv = (CircleTextView) v.findViewById(R.id.circle_order_text);
        if (Visable) {
            tv.setText(text);
            tv.setVisibility(View.VISIBLE);
        } else {
            tv.setVisibility(View.GONE);
        }
    }

    private void setItemSaleAfterRightCircle(int viewId, String text, boolean Visable) {
        View v = mContentView.findViewById(viewId);
        if (Visable) {
            CircleTextView tv = (CircleTextView) v.findViewById(R.id.circle_order_sale_after);
            tv.setText(text);
            tv.setVisibility(View.VISIBLE);
        } else {
            CircleTextView tv = (CircleTextView) v.findViewById(R.id.circle_order_sale_after);
            tv.setVisibility(View.GONE);
        }
    }

    private void setItemLeftImageView(int viewId, int resId) {
        View v = mContentView.findViewById(viewId);
        ImageView ivLeftIcon = (ImageView) v.findViewById(R.id.iv_left_icon);
        ivLeftIcon.setImageResource(resId);
        ivLeftIcon.setVisibility(View.VISIBLE);
    }

    private void setItemLeftImageView(int viewId, boolean is_Show) {
        View v = mContentView.findViewById(viewId);
        ImageView ivLeftIcon = (ImageView) v.findViewById(R.id.iv_left_icon);
        if (is_Show) {
            ivLeftIcon.setVisibility(View.VISIBLE);
        } else {
            ivLeftIcon.setVisibility(View.GONE);
        }
    }

    private void setItemRightText(int viewId, Spanned spanned) {
        View v = mContentView.findViewById(viewId);
        TextView tv = (TextView) v.findViewById(R.id.tv_right_text);
        tv.setText(spanned);
    }

    @Override
    public void onClick(View v) {
        HashMap<String, String> hashMap = new HashMap<>();
        switch (v.getId()) {
            case R.id.rl_vip:
                if (!TextUtils.isEmpty(vip_url)) {
                    Intent pintent = new Intent(Vthis, ItemPreview1Activity.class);
                    pintent.putExtra("name", "会员详情");
                    pintent.putExtra("url", vip_url);
                    startActivity(pintent);
                }
                break;
            case R.id.layout_progress:
                if (!TextUtils.isEmpty(taskUrl)) {
                    Intent pintent = new Intent(Vthis, ItemPreview1Activity.class);
                    pintent.putExtra("name", taskTitle);
                    pintent.putExtra("url", taskUrl);
                    startActivity(pintent);
                }
                break;
            case R.id.item_mycoin:
                IWXAPI api = WXAPIFactory.createWXAPI(Vthis, BWApplication.getShareWXAPPId());
                api.registerApp(BWApplication.getShareWXAPPId());
                String path = "/pages/tab/manage";
                WXLaunchMiniProgram.Req req = new WXLaunchMiniProgram.Req();
                req.userName = BWApplication.getWXKDBMiniAppID(); // 填小程序原始id
                req.path = path;                  //拉起小程序页面的可带参路径，不填默认拉起小程序首页
                req.miniprogramType = WXLaunchMiniProgram.Req.MINIPTOGRAM_TYPE_RELEASE;// 可选打开 开发版，体验版和正式版
                api.sendReq(req);
                break;
            case R.id.my_share:
            case R.id.item_me_invite_register:
                //邀请店主注册
                hashMap.put("type", msg_item_me_invite_register);
                UMengTestUtls.UmengOnClickEvent(Vthis, UmengClick.Click36, hashMap);
                share();
                break;
            case R.id.iv_all_search:
                CommonSearchActivity.launch(Vthis, CommonSearchActivity.SearchType.ALL_ITEM_SEARCH);
                break;
            case R.id.iv_chat_txt:
                UMengTestUtls.UmengOnClickEvent(Vthis, UmengClick.Click12);
                ChatUtl.goToChatMainActivity(Vthis, true);
//                Intent xintent = new Intent(this, ChatMainActivity.class);
//                xintent.putExtra(ChatMainActivity.ETRA_LEFT_BTN_ISHOW, true);
//                startActivity(xintent);
                break;
            case R.id.iv_shopping_cart:
                Utils.gotoShopcart(Vthis);
                //PicMenu menu = PicMenu.getInstance(this);
                // menu.dShow("正在上传...", 10);
                break;
            case R.id.my_order_pay:
                gotoOrderManageWithType(Const.OrderStatus.WAIT_PAY);
//                if (orderRedID > 0) {
//                    SpManager.setQuickMeOrderMaxID(Vthis, orderRedID);
//                    MsgRed msgRed = new MsgRed();
//                    msgRed.setCount(0);
//                    msgRed.setIs_Show(false);
//                    mEventBus.post(BusEvent.getEvent(EventBusId.PINHUO_ME_RED_IS_SHOW, msgRed));
//                }
                break;
            case R.id.my_order_ship:
                gotoOrderManageWithType(Const.OrderStatus.WAIT_SHIP);
                break;
            case R.id.my_order_get:
                gotoOrderManageWithType(Const.OrderStatus.SHIPED);
                break;
            case R.id.my_order_overed:
                gotoOrderManageWithType(Const.OrderStatus.DONE);
                break;
            case R.id.my_order_cancel:
                gotoOrderManageWithType(Const.OrderStatus.CANCEL);
                break;
            case R.id.my_order_refund:
                startActivity(new Intent(Vthis, AfterSaleListActivity.class));
                //gotoOrderManageWithType(Const.OrderStatus.REFUND);
                break;
            case R.id.item_myorder:
                Intent intent = new Intent(Vthis, OrderManageActivity.class);
                intent.putExtra(OrderManageActivity.EXTRA_QSID, 0);
                intent.putExtra(OrderManageActivity.EXTRA_TITLE, "订单管理");
                startActivity(intent);
                break;
            case R.id.item_myyft:
                UMengTestUtls.UmengOnClickEvent(Vthis, UmengClick.Click33);
                Intent it = new Intent(Vthis, MyIncomeActivity.class);
                startActivity(it);
                break;
            case R.id.item_mybill:
                //商品账单

                hashMap.put("type", msg_mybill);
                UMengTestUtls.UmengOnClickEvent(Vthis, UmengClick.Click36, hashMap);
                Intent billintent = new Intent(Vthis, ItemPreview1Activity.class);
                billintent.putExtra("name", "商品账单");
                billintent.putExtra("url", "http://pinhuobuyer.nahuo.com/order/productbill");
                startActivity(billintent);
                break;
            case R.id.item_mypostfee:
               /* Intent itemIntent = new Intent(Vthis, ItemPreviewActivity.class);
                itemIntent.putExtra("url", "http://pinhuobuyer.nahuo.com/PostfeeBillList");
                itemIntent.putExtra("name", "运费账单");
                startActivity(itemIntent);*/
                hashMap.put("type", msg_mypostfee);
                UMengTestUtls.UmengOnClickEvent(Vthis, UmengClick.Click36, hashMap);
                Intent itemIntent = new Intent(Vthis, ItemPreview1Activity.class);
                itemIntent.putExtra("name", "运费账单");
                itemIntent.putExtra("url", "http://pinhuobuyer.nahuo.com/order/St4Buyer");
                startActivity(itemIntent);
//                Intent itemIntent = new Intent(Vthis, FreightBillActivity.class);
//                startActivity(itemIntent);
                break;
            case R.id.item_bgqd:
                hashMap.put("type", msg_item_bgqd);
                UMengTestUtls.UmengOnClickEvent(Vthis, UmengClick.Click36, hashMap);
                Intent bgqd = new Intent(Vthis, BGQDActivity.class);
                startActivity(bgqd);
                break;
            case R.id.item_mytuan:
                //关注足迹
                hashMap.put("type", msg_guanzhu);
                UMengTestUtls.UmengOnClickEvent(Vthis, UmengClick.Click36, hashMap);
                Intent tuan = new Intent(Vthis, MyMainCollectionActivity.class);
                startActivity(tuan);
                break;
            case R.id.item_my_goto_tuan:
                //即将成团
                UMengTestUtls.UmengOnClickEvent(Vthis, UmengClick.Click16);
                startActivity(new Intent(Vthis, SoonLeagueActivity.class));
                break;
            case R.id.item_phqd:
                hashMap.put("type", msg_item_phqd);
                UMengTestUtls.UmengOnClickEvent(Vthis, UmengClick.Click36, hashMap);
                Intent phqd = new Intent(Vthis, PHQDActivity.class);
                startActivity(phqd);
                break;
            case R.id.item_myhelp:
                hashMap.put("type", msg_item_myhelp);
                UMengTestUtls.UmengOnClickEvent(Vthis, UmengClick.Click36, hashMap);
                Intent help = new Intent(Vthis, NewsActivity.class);
                startActivity(help);
                break;
            case R.id.item_myrenzheng://认证
                hashMap.put("type", msg_item_myrenzheng);
                UMengTestUtls.UmengOnClickEvent(Vthis, UmengClick.Click36, hashMap);
                Intent baseInfo = new Intent(Vthis, BaseInfoActivity.class);
                startActivity(baseInfo);
                break;
            case R.id.item_setting_security:
                //安全设置
                startActivity(new Intent(Vthis, SecuritySettingsActivity.class));
                break;
            case R.id.item_about:
                Intent knowIntent = new Intent(Vthis, PostDetailActivity.class);
                knowIntent.putExtra(PostDetailActivity.EXTRA_TID, 102139);
                knowIntent.putExtra(PostDetailActivity.EXTRA_POST_TYPE, Const.PostType.TOPIC);
                startActivity(knowIntent);
                break;
            case R.id.tvScore:
                Intent sl = new Intent(Vthis, ScoreListActivity.class);
                sl.putExtra(ScoreListActivity.ETRA_POINT_NAME, pointName);
                startActivity(sl);
                break;
            case R.id.iv_userhead:
                Intent mes = new Intent(Vthis, MeSettingActivity.class);
                startActivity(mes);
                break;
            case R.id.item_mycoupon:
                //我的优惠券
                UMengTestUtls.UmengOnClickEvent(Vthis, UmengClick.Click13);
                if (Avail_MaxID > 0) {
                    SpManager.setQuickMeCouponMaxID(Vthis, Avail_MaxID);
                    // setItemRightText(R.id.item_mycoupon, "");
                    setItemRightRegent(R.id.item_mycoupon, "new", false, 40, 18);
//                    MsgRed msgRed = new MsgRed();
//                    msgRed.setCount(0);
//                    msgRed.setIs_Show(false);
//                    mEventBus.post(BusEvent.getEvent(EventBusId.PINHUO_ME_RED_IS_SHOW, msgRed));
                }
                Intent couponIntent = new Intent(Vthis, CouponActivity.class);
                startActivity(couponIntent);
                break;

            default:
                break;
        }
    }

    private void share() {
        ShareEntity shareData = new ShareEntity();
        shareData.setTitle(ShareTitle);
        shareData.setSummary(ShareContent);
        shareData.setTitle_des(Content);
//                String imgUrl = "http://baidu.com";
//                shareData.setImgUrl(imgUrl);
        shareData.setTargetUrl(ShareUrl);
        NahuoNewShare share = new NahuoNewShare(Vthis, shareData);
        // share.addPlatforms(NahuoShare.PLATFORM_WX_CIRCLE, NahuoShare.PLATFORM_WX_FRIEND);
        share.show();
    }

    private void gotoOrderManageWithType(int type) {
        Intent intent = new Intent(Vthis, OrderManageActivity.class);
        intent.putExtra(OrderManageActivity.EXTRA_QSID, 0);
        intent.putExtra(OrderManageActivity.EXTRA_TITLE, "订单管理");
        intent.putExtra(OrderManageActivity.EXTRA_ORDER_TYPE, type);
        startActivity(intent);
    }

    private enum Step {
        NUMS, RENZHENG, SCORE, NOTIFI
    }

    private class GetWaitPayCountTask extends AsyncTask<Object, Void, Object> {

        private Step mStep;

        public GetWaitPayCountTask(Step step) {
            mStep = step;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (!isHidden() && loadingDialog != null)
                loadingDialog.start("获取个人数据...");
        }

        @Override
        protected Object doInBackground(Object... params) {
            try {
                switch (mStep) {
                    case NUMS: {
                        return OrderAPI.getPendingOrderCount(Vthis);
                    }
                    case RENZHENG: {
                        return OrderAPI.getAuthInfoStatu(Vthis);
                    }
                    case SCORE: {
                        return OrderAPI.getScore(Vthis);
                    }
                    case NOTIFI:
                        return OrderAPI.getcurrentnotice(Vthis, 0);

                }
            } catch (Exception e) {
                e.printStackTrace();
                return "error:" + e.getMessage();
            }
            return null;

        }

        @Override
        protected void onPostExecute(Object result) {
            if (loadingDialog != null) {
                if (loadingDialog.isShowing()) {
                    loadingDialog.stop();
                }
            }
            if (result.getClass() == String.class && result.toString().startsWith("error:")) {
                ViewHub.showLongToast(Vthis, result.toString());
                return;
            }
            switch (mStep) {
                case NUMS: {
                    setNums(result);
                    break;
                }
                case RENZHENG: {// 认证状态
                    AuthInfoModel data = (AuthInfoModel) result;
                    if (data.getAuthInfo() != null) {
                        setItemRightText(R.id.item_myrenzheng, data.getAuthInfo().getStatu());
                    } else {
                        ViewHub.showLongToast(Vthis, "未找到认证状态");
                    }
                    break;
                }
                case SCORE: {// 获取积分
                    ScoreModel data = (ScoreModel) result;
                    int statuId = data.getAuthInfo().getStatuID();
                    pointName = data.getPointName();
                    int score = data.getPoint();
                    SpManager.setScore(Vthis, score);
                    SpManager.setStatuId(Vthis, statuId);
                    if (SpManager.getScore(Vthis) < 0) {
                        SpManager.setScore(Vthis, 0);
                    }
                    tvScore.setText(pointName + ": " + SpManager.getScore(Vthis));
                    break;
                }
                case NOTIFI:
                    Animation animation = null;
                    list.clear();
                    slist.clear();
                    List<NoticeBean> data = (List<NoticeBean>) result;

                    if (data != null && data.size() > 0) {

                        animation = AnimationUtils.loadAnimation(Vthis, R.anim.slide_bottom_in);
                        ll_notifi.setVisibility(View.VISIBLE);

                        EventBus.getDefault().post(BusEvent.getEvent(EventBusId.PINHUO_TAB_BOTTOM_SQME_LAYOUT_HEIGHT));
                        // ll_notifi.startAnimation(animation);
                        for (int i = 0; i < data.size(); i++) {
                            NoticeBean bean = data.get(i);
                            bean.setNums_content(i + 1 + ": " + bean.getTitle());
                            list.add(bean);
                            slist.add(i + 1 + ": " + bean.getTitle());
                        }
                        tv_notifi_marquee.setContentList(slist);
                        //tv_notifi_auto.startFlipping();
                        // marqueeFactory.resetData(list);
                    } else {

                        animation = AnimationUtils.loadAnimation(Vthis, R.anim.slide_bottom_out);
                        // ll_notifi.startAnimation(animation);
                        ll_notifi.setVisibility(View.GONE);
                    }
                    break;
            }
        }
    }

    long Avail_MaxID, Avail_Total;

    private void setNums(Object result) {
        if (result instanceof String && ((String) result).startsWith("error:")) {
            ViewHub.showLongToast(Vthis, ((String) result).replace("error:", ""));
        } else {
            int waitPayCount = 0;
            int waitShipCount = 0;
            int shipedCount = 0;
            int overedCount = 0;
            int cancelCount = 0;
            int readyCount = 0;
            int saleAfterCount = 0;
            JSONObject jsonObject;
            JSONArray ja;
            JSONObject acJsonObject;
            JSONObject pJsonObject;
            try {
                jsonObject = new JSONObject(result.toString());
                readyCount = jsonObject.optInt("ReadyCount", 0);
                String OrderStatuList = jsonObject.optString("OrderStatuList");
                String AvailableCouponInfo = jsonObject.optString("AvailableCouponInfo");
                String Promote = jsonObject.optString("Promote");
                if (!TextUtils.isEmpty(Promote)) {
                    pJsonObject = new JSONObject(Promote);
                    Summary = pJsonObject.optString("Summary");
                    Content = pJsonObject.optString("Content");
                    ShareTitle = pJsonObject.optString("ShareTitle");
                    ShareContent = pJsonObject.optString("ShareContent");
                    ShareUrl = pJsonObject.optString("ShareUrl");
                    if (!TextUtils.isEmpty(Summary)) {
                        setItemRightText(R.id.item_me_invite_register, Summary);
                    }
                }
                if (!TextUtils.isEmpty(AvailableCouponInfo)) {
                    acJsonObject = new JSONObject(AvailableCouponInfo);
                    Avail_MaxID = acJsonObject.optLong("MaxID", 0);
                    Avail_Total = acJsonObject.optLong("Total", 0);
                    long id = SpManager.getQuickMeCouponMaxID(Vthis);
                    if (Avail_Total > 0) {
                        setItemRightText(R.id.item_mycoupon, Avail_Total + "张可用");
                    }
                    if (id < Avail_MaxID) {
                        // SpManager.setQuickMeCouponMaxID(this, Avail_MaxID);
                        if (Avail_Total > 0) {
                            MsgRed msgRed = new MsgRed();
                            msgRed.setIs_Show(true);
                            msgRed.setCount(Avail_Total);
                            mEventBus.post(BusEvent.getEvent(EventBusId.PINHUO_ME_RED_IS_SHOW, msgRed));
                            // setItemRightText(R.id.item_mycoupon, Avail_Total + "张可用");
                            setItemRightRegent(R.id.item_mycoupon, "new", true, 40, 18);
                        }

                    }

                }
                ja = new JSONArray(OrderStatuList + "");
                for (int i = 0; i < ja.length(); i++) {
                    JSONObject jo = ja.getJSONObject(i);
                    switch (jo.getInt("StatuID")) {
                        case Const.OrderStatus.WAIT_PAY:
                            waitPayCount = jo.getInt("Amount");
                            break;
                        case Const.OrderStatus.SHIPED:
                            shipedCount = jo.getInt("Amount");
                            break;
                        case Const.OrderStatus.DONE:
                            overedCount = jo.getInt("Amount");
                            break;
                        case Const.OrderStatus.CANCEL:
                            cancelCount = jo.getInt("Amount");
                            break;
                        case Const.OrderStatus.WAIT_SHIP:
                            waitShipCount = jo.getInt("Amount");
                            break;
                        case Const.OrderStatus.REFUND:
                            saleAfterCount = jo.getInt("Amount");
                            break;
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (saleAfterCount > 0) {
                setItemSaleAfterRightCircle(R.id.circle_order_sale_after, saleAfterCount + "", true);
            } else {
                setItemSaleAfterRightCircle(R.id.circle_order_sale_after, saleAfterCount + "", false);
            }
            if (readyCount > 0) {
                setItemRightCircle(R.id.item_my_goto_tuan, readyCount + "", true);
            } else {
                setItemRightCircle(R.id.item_my_goto_tuan, readyCount + "", false);
            }
            if (waitPayCount > 0) {
                ((CircleTextView) mContentView.findViewById(R.id.circle_order_text0)).setText(waitPayCount + "");
                mContentView.findViewById(R.id.circle_order_text0).setVisibility(View.VISIBLE);
            } else {
                mContentView.findViewById(R.id.circle_order_text0).setVisibility(View.GONE);
            }
            if (waitShipCount > 0) {
                ((CircleTextView) mContentView.findViewById(R.id.circle_order_text1)).setText(waitShipCount + "");
                mContentView.findViewById(R.id.circle_order_text1).setVisibility(View.VISIBLE);
            } else {
                mContentView.findViewById(R.id.circle_order_text1).setVisibility(View.GONE);
            }
            if (shipedCount > 0) {
                ((CircleTextView) mContentView.findViewById(R.id.circle_order_text2)).setText(shipedCount + "");
                mContentView.findViewById(R.id.circle_order_text2).setVisibility(View.VISIBLE);
            } else {
                mContentView.findViewById(R.id.circle_order_text2).setVisibility(View.GONE);
            }
        }
    }
}
