//package com.nahuo.quicksale;
//
//import android.content.Intent;
//import android.os.AsyncTask;
//import android.os.Bundle;
//import android.text.Spanned;
//import android.text.TextUtils;
//import android.view.View;
//import android.view.animation.Animation;
//import android.view.animation.AnimationUtils;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.RelativeLayout;
//import android.widget.ScrollView;
//import android.widget.TextView;
//
//import com.luck.picture.lib.tools.ScreenUtils;
//import com.nahuo.bean.MsgRed;
//import com.nahuo.bean.NoticeBean;
//import com.nahuo.constant.UmengClick;
//import com.nahuo.library.controls.CircleTextView;
//import com.nahuo.library.controls.LoadingDialog;
//import com.nahuo.library.helper.DisplayUtil;
//import com.nahuo.library.helper.ImageUrlExtends;
//import com.nahuo.quicksale.Topic.PostDetailActivity;
//import com.nahuo.quicksale.activity.AfterSaleListActivity;
//import com.nahuo.quicksale.activity.ItemPreview1Activity;
//import com.nahuo.quicksale.activity.SoonLeagueActivity;
//import com.nahuo.quicksale.api.HttpUtils;
//import com.nahuo.quicksale.api.OrderAPI;
//import com.nahuo.quicksale.app.BWApplication;
//import com.nahuo.quicksale.base.BaseAppCompatActivity;
//import com.nahuo.quicksale.common.BlurTransform;
//import com.nahuo.quicksale.common.Const;
//import com.nahuo.quicksale.common.NahuoNewShare;
//import com.nahuo.quicksale.common.SpManager;
//import com.nahuo.quicksale.common.Utils;
//import com.nahuo.quicksale.customview.MarqueeTextView;
//import com.nahuo.quicksale.customview.TopScrollView;
//import com.nahuo.quicksale.eventbus.BusEvent;
//import com.nahuo.quicksale.eventbus.EventBusId;
//import com.nahuo.quicksale.oldermodel.AuthInfoModel;
//import com.nahuo.quicksale.oldermodel.ScoreModel;
//import com.nahuo.quicksale.util.ChatUtl;
//import com.nahuo.quicksale.util.CircleCarTxtUtl;
//import com.nahuo.quicksale.util.UMengTestUtls;
//import com.squareup.picasso.Picasso;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//
//import de.greenrobot.event.EventBus;
//import de.hdodenhof.circleimageview.CircleImageView;
//
//import static com.nahuo.quicksale.common.SpManager.getUserId;
//
///**
// * Created by 诚 on 2015/9/21.
// */
//public class Sq_meAcivity extends BaseAppCompatActivity implements View.OnClickListener {//, OnTitleBarClickListener
//    private Sq_meAcivity Vthis = this;
//    private EventBus mEventBus = EventBus.getDefault();
//    private CircleImageView mIvAvatar;
//    private CircleTextView circle_car_text_chat;
//    private LoadingDialog loadingDialog;
//    private TextView tvScore;//积分
//    private String url = "";
//    // private MarqueeView tv_notifi_auto;
//    private LinearLayout ll_notifi;
//    //private MarqueeFactory<TextView, NoticeBean> marqueeFactory;
//    private List<NoticeBean> list = new ArrayList<>();
//    private MarqueeTextView tv_notifi_marquee;
//    private List<String> slist = new ArrayList<>();
//    private CircleTextView carCountTv;
//    private View layout_explain;
//    private int useId;
//    private TopScrollView scroll_view;
//    private ImageView iv_top_logobg;
//    private View rlTContent;
//    private String pointName = "";
//    private String Summary = "", Content = "", ShareTitle = "",
//            ShareContent = "",
//            ShareUrl = "";
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_qs_me);
////        com.nahuo.library.controls.TitleBar titleBar = (com.nahuo.library.controls.TitleBar) findViewById(R.id.ablum_titlebar);
////        titleBar.setOnTitleBarClickListener(this);
//        initView();
//    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
//        //tv_notifi_auto.startFlipping();
//        initdata();
//        ChatUtl.setChatBroad(this);
//        // new LoadGoodsTask(this, carCountTv).execute();
//        if (SpManager.getIs_Login(this)) {
//            new GetWaitPayCountTask(Step.NOTIFI).execute();
//            new GetWaitPayCountTask(Step.NUMS).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
//            new GetWaitPayCountTask(Step.RENZHENG).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
//            new GetWaitPayCountTask(Step.SCORE).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
//        }
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        mEventBus.unregister(this);
//        try {
//            if (loadingDialog != null) {
//                if (loadingDialog.isShowing()) {
//                    loadingDialog.stop();
//                }
//            }
//        } catch (Exception e) {
//            loadingDialog = null;
//        }
//    }
//
//    public void onEventMainThread(BusEvent event) {
//        switch (event.id) {
//            case EventBusId.WEIXUN_NEW_MSG:
//                if (circle_car_text_chat == null) {
//                    return;
//                }
//                String num = event.data.toString();
//                if (TextUtils.isEmpty(num)) {
//                    circle_car_text_chat.setVisibility(View.GONE);
//                } else {
//                    circle_car_text_chat.setVisibility(View.VISIBLE);
//                    circle_car_text_chat.setText(event.data.toString());
//                }
//                break;
//            case EventBusId.SHOP_LOGO_UPDATED:// 店铺logo有修改
//                String logo = SpManager.getShopLogo(Vthis);
//                if (!TextUtils.isEmpty(logo)) {
//                    String url = ImageUrlExtends.getImageUrl(logo, Const.LIST_HEADER_COVER_SIZE);
//                    Picasso.with(Vthis).load(url).skipMemoryCache().placeholder(R.drawable.empty_photo).into(mIvAvatar);
//                    initLogoBgView(url);
//                }
//                break;
//        }
//    }
//
//    private void initdata() {
//        String shopName = SpManager.getShopName(BWApplication.getInstance());
//        String userName = SpManager.getUserName(BWApplication.getInstance());
//        String nickName = SpManager.getNICKNAME(BWApplication.getInstance());
//        String logo = SpManager.getShopLogo(BWApplication.getInstance()).trim();// SpManager.getUserLogo(NHApplication.getInstance());
//        TextView name = (TextView) Vthis.findViewById(R.id.txt_name);
//        mIvAvatar = (CircleImageView) Vthis.findViewById(R.id.iv_userhead);
//        mIvAvatar.setOnClickListener(this);
//        mIvAvatar.setBorderWidth(DisplayUtil.dip2px(this, 2));
//        mIvAvatar.setBorderColor(getResources().getColor(R.color.white));
//        if (HttpUtils.IS_LOCAL) {
//            name.setText(nickName + "(内网版)");
//        } else {
//            name.setText(nickName);
//        }
//
//        if (TextUtils.isEmpty(logo)) {
//            url = Const.getShopLogo(getUserId(BWApplication.getInstance()));
//        } else {
//            url = ImageUrlExtends.getImageUrl(logo, 8);
//        }
//        Picasso.with(BWApplication.getInstance()).load(url)
//                .placeholder(R.drawable.empty_photo1).into(mIvAvatar);
//        initLogoBgView(url);
////        new GetWaitPayCountTask(Step.NUMS).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
////        new GetWaitPayCountTask(Step.RENZHENG).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
////        new GetWaitPayCountTask(Step.SCORE).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
//    }
//
//    private void initLogoBgView(String url) {
////        ((ImageView)Vthis.findViewById(R.id.iv_logobg)).setImageBitmap(ImageTools.blurBitmap(Vthis,mIvAvatar.get));
////        RequestOptions options = new RequestOptions()
////                .centerCrop()
////                .placeholder(R.drawable.sq_me_bg)
////                .diskCacheStrategy(DiskCacheStrategy.ALL);
////        Glide.with(this)
////                .load(url)
////                .apply(options)
////                .into((ImageView)findViewById(R.id.iv_logobg));
////        Glide.with(this)
////                .load(url)
////                .apply(options)
////                .into((ImageView)findViewById(R.id.iv_logobg1));
//        Picasso.with(BWApplication.getInstance())
//                .load(url)
//                .transform(new BlurTransform(80))
//                .skipMemoryCache()
//                .placeholder(R.drawable.sq_me_bg)
//                .error(R.drawable.sq_me_bg).into((ImageView) Vthis.findViewById(R.id.iv_logobg));
////        Picasso.with(BWApplication.getInstance())
////                .load(url)
////                .transform(new BlurTransform(80))
////                .error(R.drawable.sq_me_bg).skipMemoryCache()
////                .placeholder(R.drawable.sq_me_bg).into((ImageView) Vthis.findViewById(R.id.iv_logobg1));
//        iv_top_logobg.setImageResource(R.drawable.sq_me_bg);
//    }
//
////    public class NoticeMF extends MarqueeFactory<TextView, NoticeBean> {
////        private LayoutInflater inflater;
////
////        public NoticeMF(Context mContext) {
////            super(mContext);
////            inflater = LayoutInflater.from(mContext);
////        }
////
////        @Override
////        public TextView generateMarqueeItemView(NoticeBean data) {
////            TextView mView = (TextView) inflater.inflate(R.layout.notice_item, null);
//////            String mess = data.getTitle();
//////            String num = data.getNums() + "";
//////            if (!TextUtils.isEmpty(mess))
//////                mView.setText(num + ": " + mess);
////            return mView;
////        }
////    }
////
//
//    @Override
//    public void onPause() {
//        super.onPause();
////        tv_notifi_auto.stopFlipping();
//
//    }
//
//    int sq_me_col;
//    String msg_guanzhu="关注足迹",msg_my_goto_tuan="即将成团"
//            ,msg_my_yft="我的钱包",msg_mybill="商品账单"
//            ,msg_mypostfee="运费账单",msg_item_phqd="配货清单",msg_item_bgqd="包裹清单",msg_item_me_invite_register="邀请店主注册"
//            ,msg_item_myrenzheng="店铺认证",msg_item_myhelp="帮助中心";
//    private void initView() {
//        mEventBus.register(this);
//        loadingDialog = new LoadingDialog(this);
//        iv_top_logobg = (ImageView) findViewById(R.id.iv_logobg1);
//        iv_top_logobg.setAlpha(0);
//        rlTContent = findViewById(R.id.rl_top);
//        findViewById(R.id.iv_shopping_cart).setOnClickListener(this);
//        findViewById(R.id.iv_all_search).setOnClickListener(this);
//        carCountTv = (CircleTextView) findViewById(R.id.circle_car_text);
//        circle_car_text_chat = (CircleTextView) findViewById(R.id.circle_car_text_chat);
//        CircleCarTxtUtl.setColor(circle_car_text_chat);
//        scroll_view = (TopScrollView) findViewById(R.id.scroll_view);
////        findViewById(R.id.my_share).setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View view) {
////                Intent mes = new Intent(Vthis, ShareActivity.class);
////                startActivity(mes);
////            }
////        });
//        findViewById(R.id.my_share).setOnClickListener(this);
//        findViewById(R.id.my_setting).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent mes = new Intent(Vthis, MeSettingActivity.class);
//                startActivity(mes);
//            }
//        });
//        findViewById(R.id.iv_chat_txt).setOnClickListener(this);
//        scroll_view.setOnScrollChangedListener(new TopScrollView.OnScrollChangedListener() {
//            @Override
//            public void onScrollChanged(ScrollView who, int x, int y, int oldx, int oldy) {
//                if (y >= rlTContent.getHeight()
//                        && y < 2 * rlTContent.getHeight()) {
//                    y = y - rlTContent.getHeight();
//                } else if (y >= 2 * rlTContent.getHeight()) {
//                    y = rlTContent.getHeight();
//                } else if (y < rlTContent.getHeight()) {
//                    y = 0;
//                }
//                iv_top_logobg.setAlpha(y * 255
//                        / rlTContent.getHeight());
////                tv_title_top.setAlpha(y * 255
////                        / rlTContent.getHeight());
//             /*   tv_rigt_bg.setAlpha(rlTContent.getHeight() / (y + 1)
//                );
//                tv_left_bg.setAlpha(rlTContent.getHeight() / (y + 1));
//                if (Integer.parseInt(android.os.Build.VERSION.SDK) >= 16) {
//                    mDrawable.setAlpha(y * 255 / rlTContent.getHeight());
//                }*/
//            }
//        });
//        layout_explain = findViewById(R.id.layout_explain);
////        useId = SpManager.getUserId(BWApplication.getInstance());
////        if (useId>0) {
////            GuidePreference.init(this);
////            sq_me_col = GuidePreference.getInstance().geSqMe_Cole(useId + "");
////            if (sq_me_col == 1) {
////                layout_explain.setVisibility(View.GONE);
////            } else {
////                layout_explain.setVisibility(View.VISIBLE);
////            }
////        }
////            layout_explain.setOnClickListener(new View.OnClickListener() {
////                @Override
////                public void onClick(View v) {
////                    layout_explain.setVisibility(View.GONE);
////                    GuidePreference.getInstance().setSqMe_Cole(useId + "", 1);
////                    mEventBus.post(BusEvent.getEvent(EventBusId.PINHUO_TAB_BOTTOM_LAYOUT));
////                }
////            });
//
//        initMarquee();
//        ll_notifi = (LinearLayout) findViewById(R.id.ll_notifi);
//        initItem(R.id.item_myorder, "我的订单", false);
//        setItemRightText(R.id.item_myorder, "查看全部订单");
//        setItemLeftImageView(R.id.item_myorder, R.drawable.icon_my_orders);
//        initItem(R.id.item_myyft, msg_my_yft, false);
//        setItemLeftImageView(R.id.item_myyft, R.drawable.icon_my_yue);
//        setItemRightText(R.id.item_myyft, "充值、提现等");
//        initItem(R.id.item_mycoupon, "优惠券", false);
//        setItemLeftImageView(R.id.item_mycoupon, R.drawable.icon_my_coupon);
//        initItem(R.id.item_mybill, msg_mybill, false);
//        setItemLeftImageView(R.id.item_mybill, R.drawable.icon_my_bill);
//        setItemRightText(R.id.item_mybill, "每日进货退款明细");
//
//        initItem(R.id.item_mypostfee, msg_mypostfee, false);
//        setItemLeftImageView(R.id.item_mypostfee, R.drawable.icon_my_yufei);
//        setItemRightText(R.id.item_mypostfee, "运费每周结算一次");
//        initItem(R.id.item_phqd, msg_item_phqd, false);
//        setItemLeftImageView(R.id.item_phqd, R.drawable.icon_my_peihuo);
//        setItemRightText(R.id.item_phqd, "已配商品、设置发货时间");
//        initItem(R.id.item_bgqd, msg_item_bgqd, false);
//        setItemLeftImageView(R.id.item_bgqd, R.drawable.icon_my_baoguo);
//        setItemRightText(R.id.item_bgqd, "查看快递包裹明细");
//        initItem(R.id.item_mytuan, msg_guanzhu, false);
//        setItemLeftImageView(R.id.item_mytuan, R.drawable.iconjjct);
//        setItemRightText(R.id.item_mytuan, "关注的团和商品、足迹");
//        initItem(R.id.item_my_goto_tuan, msg_my_goto_tuan, false);
//        setItemLeftImageView(R.id.item_my_goto_tuan, R.drawable.icon_my_team);
//        setItemRightText(R.id.item_my_goto_tuan, "及时了解拼单中的商品");
//        //邀请店主注册
//        initItem(R.id.item_me_invite_register,msg_item_me_invite_register, false);
//        setItemLeftImageView(R.id.item_me_invite_register, R.drawable.invite_register);
//        initItem(R.id.item_myrenzheng, msg_item_myrenzheng, false);
//        setItemLeftImageView(R.id.item_myrenzheng, R.drawable.icon_my_store);
//        //initItem(R.id.item_setting_security, "安全设置", true);
//        initItem(R.id.item_myhelp, msg_item_myhelp, false);
//        setItemLeftImageView(R.id.item_myhelp, R.drawable.icon_my_help);
//        // initItem(R.id.item_about, "关于我们", true);
//        // setItemLeftImageView(R.id.item_about, R.drawable.icon_my_about);
//
//        findViewById(R.id.my_order_pay).setOnClickListener(this);
//        findViewById(R.id.my_order_ship).setOnClickListener(this);
//        findViewById(R.id.my_order_get).setOnClickListener(this);
//        findViewById(R.id.my_order_overed).setOnClickListener(this);
//        findViewById(R.id.my_order_cancel).setOnClickListener(this);
//        findViewById(R.id.my_order_refund).setOnClickListener(this);
//        tvScore = (TextView) this.findViewById(R.id.tvScore);
//        if (SpManager.getScore(Vthis) < 0) {
//            SpManager.setScore(Vthis, 0);
//        }
//        tvScore.setText("活跃分: " + SpManager.getScore(this));
//        tvScore.setOnClickListener(this);
//    }
//
//    private void initMarquee() {
//        tv_notifi_marquee = (MarqueeTextView) findViewById(R.id.tv_notifi_marquee);
//        tv_notifi_marquee.setVerticalSwitchSpeed(1000);
//        tv_notifi_marquee.setHorizontalScrollSpeed(200);
//        tv_notifi_marquee.setOnItemOnClickListener(new MarqueeTextView.OnItemClickListener() {
//            @Override
//            public void onItemclick(int index) {
//                NoticeBean bean = list.get(index);
//                String target = bean.getTarget();
//                if (!TextUtils.isEmpty(target)) {
//                    Intent intent = new Intent(Vthis, PostDetailActivity.class);
//                    intent.putExtra(PostDetailActivity.EXTRA_TID, bean.getTargetID());
//                    intent.putExtra(PostDetailActivity.EXTRA_LOGO_URL,
//                            url);
//                    intent.putExtra(PostDetailActivity.EXTRA_POST_TITLE, bean.getTitle());
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
////        tv_notifi_auto = (MarqueeView) findViewById(R.id.tv_notifi);
////        marqueeFactory = new NoticeMF(this);
////        tv_notifi_auto.setMarqueeFactory(marqueeFactory);
////        marqueeFactory.setOnItemClickListener(new MarqueeFactory.OnItemClickListener<TextView, NoticeBean>() {
////            @Override
////            public void onItemClickListener(MarqueeFactory.ViewHolder<TextView, NoticeBean> holder) {
////                // Toast.makeText(Sq_meAcivity.this, holder.data.getID() + "", Toast.LENGTH_SHORT).show();
////                String target = holder.data.getTarget();
////                if (!TextUtils.isEmpty(target)) {
////                    Intent intent = new Intent(Vthis, PostDetailActivity.class);
////                    intent.putExtra(PostDetailActivity.EXTRA_TID, holder.data.getTargetID());
////                    intent.putExtra(PostDetailActivity.EXTRA_LOGO_URL,
////                            url);
////                    intent.putExtra(PostDetailActivity.EXTRA_POST_TITLE, holder.data.getTitle());
////                    if (target.equals("activity")) {
////                        intent.putExtra(PostDetailActivity.EXTRA_POST_TYPE,
////                                Const.PostType.ACTIVITY);
////                    }
////                    if (target.equals("topic")) {
////                        intent.putExtra(PostDetailActivity.EXTRA_POST_TYPE,
////                                Const.PostType.TOPIC);
////                    }
////                    Vthis.startActivity(intent);
////                }
////            }
////        });
////        tv_notifi_auto.startFlipping();
////        marqueeFactory.setData(list);
//    }
//
//    private void initItem(int viewId, String text, boolean b) {
//        View v = findViewById(viewId);
//        v.setOnClickListener(Vthis);
//        TextView tv = (TextView) v.findViewById(R.id.tv_left_text);
//        ImageView ivLeftIcon = (ImageView) v.findViewById(R.id.iv_left_icon);
//        View bBtmLine = v.findViewById(R.id.view_btm_line);
//        if (!b) {
//            bBtmLine.setVisibility(View.VISIBLE);
//        } else {
//            bBtmLine.setVisibility(View.GONE);
//        }
//
//        tv.setText(text);
//        ivLeftIcon.setVisibility(View.GONE);
//
//    }
//
//    private void setItemRightText(int viewId, String text) {
//        View v = findViewById(viewId);
//        TextView tv = (TextView) v.findViewById(R.id.tv_right_text);
//        tv.setText(text);
//    }
//
//    private void setItemRightRegent(int viewId, String text, boolean Visable, int width, int height) {
//        View v = findViewById(viewId);
//        TextView tv = (TextView) v.findViewById(R.id.regent_order_text);
//        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ScreenUtils.dip2px(this, width), ScreenUtils.dip2px(this, height));
//        layoutParams.addRule(RelativeLayout.RIGHT_OF, R.id.tv_left_text);
//        layoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
//        layoutParams.leftMargin = ScreenUtils.dip2px(this, 10);
//        tv.setLayoutParams(layoutParams);
//        if (Visable) {
//            tv.setText(text);
//            tv.setVisibility(View.VISIBLE);
//        } else {
//            tv.setVisibility(View.GONE);
//        }
//    }
//
//    private void setItemRightCircle(int viewId, String text, boolean Visable) {
//        View v = findViewById(viewId);
//        CircleTextView tv = (CircleTextView) v.findViewById(R.id.circle_order_text);
//        if (Visable) {
//            tv.setText(text);
//            tv.setVisibility(View.VISIBLE);
//        } else {
//            tv.setVisibility(View.GONE);
//        }
//    }
//
//    private void setItemSaleAfterRightCircle(int viewId, String text, boolean Visable) {
//        View v = findViewById(viewId);
//        if (Visable) {
//            CircleTextView tv = (CircleTextView) v.findViewById(R.id.circle_order_sale_after);
//            tv.setText(text);
//            tv.setVisibility(View.VISIBLE);
//        } else {
//            CircleTextView tv = (CircleTextView) v.findViewById(R.id.circle_order_sale_after);
//            tv.setVisibility(View.GONE);
//        }
//    }
//
//    private void setItemLeftImageView(int viewId, int resId) {
//        View v = findViewById(viewId);
//        ImageView ivLeftIcon = (ImageView) v.findViewById(R.id.iv_left_icon);
//        ivLeftIcon.setImageResource(resId);
//        ivLeftIcon.setVisibility(View.VISIBLE);
//    }
//
//    private void setItemRightText(int viewId, Spanned spanned) {
//        View v = findViewById(viewId);
//        TextView tv = (TextView) v.findViewById(R.id.tv_right_text);
//        tv.setText(spanned);
//    }
//
//    @Override
//    public void onClick(View v) {
//        HashMap<String, String> hashMap = new HashMap<>();
//        switch (v.getId()) {
//            case R.id.my_share:
//            case R.id.item_me_invite_register:
//                //邀请店主注册
//                hashMap.put("type", msg_item_me_invite_register);
//                UMengTestUtls.UmengOnClickEvent(this, UmengClick.Click36, hashMap);
//                share();
//                break;
//            case R.id.iv_all_search:
//                CommonSearchActivity.launch(this, CommonSearchActivity.SearchType.ALL_ITEM_SEARCH);
//                break;
//            case R.id.iv_chat_txt:
//                UMengTestUtls.UmengOnClickEvent(this, UmengClick.Click12);
//                ChatUtl.goToChatMainActivity(this,true);
////                Intent xintent = new Intent(this, ChatMainActivity.class);
////                xintent.putExtra(ChatMainActivity.ETRA_LEFT_BTN_ISHOW, true);
////                startActivity(xintent);
//                break;
//            case R.id.iv_shopping_cart:
//                Utils.gotoShopcart(this);
//                //PicMenu menu = PicMenu.getInstance(this);
//                // menu.dShow("正在上传...", 10);
//                break;
//            case R.id.my_order_pay:
//                gotoOrderManageWithType(Const.OrderStatus.WAIT_PAY);
//                break;
//            case R.id.my_order_ship:
//                gotoOrderManageWithType(Const.OrderStatus.WAIT_SHIP);
//                break;
//            case R.id.my_order_get:
//                gotoOrderManageWithType(Const.OrderStatus.SHIPED);
//                break;
//            case R.id.my_order_overed:
//                gotoOrderManageWithType(Const.OrderStatus.DONE);
//                break;
//            case R.id.my_order_cancel:
//                gotoOrderManageWithType(Const.OrderStatus.CANCEL);
//                break;
//            case R.id.my_order_refund:
//                startActivity(new Intent(this, AfterSaleListActivity.class));
//                //gotoOrderManageWithType(Const.OrderStatus.REFUND);
//                break;
//            case R.id.item_myorder:
//                Intent intent = new Intent(Vthis, OrderManageActivity.class);
//                intent.putExtra(OrderManageActivity.EXTRA_QSID, 0);
//                intent.putExtra(OrderManageActivity.EXTRA_TITLE, "订单管理");
//                startActivity(intent);
//                break;
//            case R.id.item_myyft:
//                UMengTestUtls.UmengOnClickEvent(this, UmengClick.Click33);
//                Intent it = new Intent(Vthis, MyIncomeActivity.class);
//                startActivity(it);
//                break;
//            case R.id.item_mybill:
//                //商品账单
//
//                hashMap.put("type", msg_mybill);
//                UMengTestUtls.UmengOnClickEvent(this, UmengClick.Click36, hashMap);
//                Intent billintent = new Intent(this, ItemPreview1Activity.class);
//                billintent.putExtra("name", "商品账单");
//                billintent.putExtra("url", "http://pinhuobuyer.nahuo.com/order/productbill");
//                startActivity(billintent);
//                break;
//            case R.id.item_mypostfee:
//               /* Intent itemIntent = new Intent(Vthis, ItemPreviewActivity.class);
//                itemIntent.putExtra("url", "http://pinhuobuyer.nahuo.com/PostfeeBillList");
//                itemIntent.putExtra("name", "运费账单");
//                startActivity(itemIntent);*/
//                hashMap.put("type", msg_mypostfee);
//                UMengTestUtls.UmengOnClickEvent(this, UmengClick.Click36, hashMap);
//                Intent itemIntent = new Intent(Vthis, FreightBillActivity.class);
//                startActivity(itemIntent);
//                break;
//            case R.id.item_bgqd:
//                hashMap.put("type", msg_item_bgqd);
//                UMengTestUtls.UmengOnClickEvent(this, UmengClick.Click36, hashMap);
//                Intent bgqd = new Intent(Vthis, BGQDActivity.class);
//                startActivity(bgqd);
//                break;
//            case R.id.item_mytuan:
//                //关注足迹
//                hashMap.put("type", msg_guanzhu);
//                UMengTestUtls.UmengOnClickEvent(this, UmengClick.Click36, hashMap);
//                Intent tuan = new Intent(Vthis, MyMainCollectionActivity.class);
//                startActivity(tuan);
//                break;
//            case R.id.item_my_goto_tuan:
//                //即将成团
//                UMengTestUtls.UmengOnClickEvent(Sq_meAcivity.this, UmengClick.Click16);
//                startActivity(new Intent(Vthis, SoonLeagueActivity.class));
//                break;
//            case R.id.item_phqd:
//                hashMap.put("type", msg_item_phqd);
//                UMengTestUtls.UmengOnClickEvent(this, UmengClick.Click36, hashMap);
//                Intent phqd = new Intent(Vthis, PHQDActivity.class);
//                startActivity(phqd);
//                break;
//            case R.id.item_myhelp:
//                hashMap.put("type", msg_item_myhelp);
//                UMengTestUtls.UmengOnClickEvent(this, UmengClick.Click36, hashMap);
//                Intent help = new Intent(Vthis, NewsActivity.class);
//                startActivity(help);
//                break;
//            case R.id.item_myrenzheng://认证
//                hashMap.put("type", msg_item_myrenzheng);
//                UMengTestUtls.UmengOnClickEvent(this, UmengClick.Click36, hashMap);
//                Intent baseInfo = new Intent(Vthis, BaseInfoActivity.class);
//                startActivity(baseInfo);
//                break;
//            case R.id.item_setting_security:
//                //安全设置
//                startActivity(new Intent(Vthis, SecuritySettingsActivity.class));
//                break;
//            case R.id.item_about:
//                Intent knowIntent = new Intent(Vthis, PostDetailActivity.class);
//                knowIntent.putExtra(PostDetailActivity.EXTRA_TID, 102139);
//                knowIntent.putExtra(PostDetailActivity.EXTRA_POST_TYPE, Const.PostType.TOPIC);
//                startActivity(knowIntent);
//                break;
//            case R.id.tvScore:
//                Intent sl = new Intent(Vthis, ScoreListActivity.class);
//                sl.putExtra(ScoreListActivity.ETRA_POINT_NAME, pointName);
//                startActivity(sl);
//                break;
//            case R.id.iv_userhead:
//                Intent mes = new Intent(Vthis, MeSettingActivity.class);
//                startActivity(mes);
//                break;
//            case R.id.item_mycoupon:
//                //我的优惠券
//                UMengTestUtls.UmengOnClickEvent(this,UmengClick.Click13);
//                if (Avail_MaxID > 0) {
//                    SpManager.setQuickMeCouponMaxID(this, Avail_MaxID);
//                    // setItemRightText(R.id.item_mycoupon, "");
//                    setItemRightRegent(R.id.item_mycoupon, "new", false, 40, 18);
//                    MsgRed msgRed = new MsgRed();
//                    msgRed.setCount(0);
//                    msgRed.setIs_Show(false);
//                    mEventBus.post(BusEvent.getEvent(EventBusId.PINHUO_ME_RED_IS_SHOW, msgRed));
//                }
//                Intent couponIntent = new Intent(Vthis, CouponActivity.class);
//                startActivity(couponIntent);
//                break;
//
//            default:
//                break;
//        }
//    }
//
//    private void share() {
//        ShareEntity shareData = new ShareEntity();
//        shareData.setTitle(ShareTitle);
//        shareData.setSummary(ShareContent);
//        shareData.setTitle_des(Content);
////                String imgUrl = "http://baidu.com";
////                shareData.setImgUrl(imgUrl);
//        shareData.setTargetUrl(ShareUrl);
//        NahuoNewShare share = new NahuoNewShare(this, shareData);
//        // share.addPlatforms(NahuoShare.PLATFORM_WX_CIRCLE, NahuoShare.PLATFORM_WX_FRIEND);
//        share.show();
//    }
//
//    private void gotoOrderManageWithType(int type) {
//        Intent intent = new Intent(Vthis, OrderManageActivity.class);
//        intent.putExtra(OrderManageActivity.EXTRA_QSID, 0);
//        intent.putExtra(OrderManageActivity.EXTRA_TITLE, "订单管理");
//        intent.putExtra(OrderManageActivity.EXTRA_ORDER_TYPE, type);
//        startActivity(intent);
//    }
//
//    private static enum Step {
//        NUMS, RENZHENG, SCORE, NOTIFI
//    }
//
//    private class GetWaitPayCountTask extends AsyncTask<Object, Void, Object> {
//
//        private Step mStep;
//
//        public GetWaitPayCountTask(Step step) {
//            mStep = step;
//        }
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            loadingDialog.start();
//        }
//
//        @Override
//        protected Object doInBackground(Object... params) {
//            try {
//                switch (mStep) {
//                    case NUMS: {
//                        return OrderAPI.getPendingOrderCount(Vthis);
//                    }
//                    case RENZHENG: {
//                        return OrderAPI.getAuthInfoStatu(Vthis);
//                    }
//                    case SCORE: {
//                        return OrderAPI.getScore(Vthis);
//                    }
//                    case NOTIFI:
//                        return OrderAPI.getcurrentnotice(Vthis, 0);
//
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//                return "error:" + e.getMessage();
//            }
//            return null;
//
//        }
//
//        @Override
//        protected void onPostExecute(Object result) {
//            if (loadingDialog.isShowing()) {
//                loadingDialog.stop();
//            }
//            if (result.getClass() == String.class && result.toString().startsWith("error:")) {
//                ViewHub.showLongToast(Vthis, result.toString());
//                return;
//            }
//            switch (mStep) {
//                case NUMS: {
//                    setNums(result);
//                    break;
//                }
//                case RENZHENG: {// 认证状态
//                    AuthInfoModel data = (AuthInfoModel) result;
//                    if (data.getAuthInfo() != null) {
//                        setItemRightText(R.id.item_myrenzheng, data.getAuthInfo().getStatu());
//                    } else {
//                        ViewHub.showLongToast(Vthis, "未找到认证状态");
//                    }
//                    break;
//                }
//                case SCORE: {// 获取积分
//                    ScoreModel data = (ScoreModel) result;
//                    int statuId = data.getAuthInfo().getStatuID();
//                    pointName = data.getPointName();
//                    int score = data.getPoint();
//                    SpManager.setScore(Vthis, score);
//                    SpManager.setStatuId(Vthis, statuId);
//                    if (SpManager.getScore(Vthis) < 0) {
//                        SpManager.setScore(Vthis, 0);
//                    }
//                    tvScore.setText(pointName + ": " + SpManager.getScore(Vthis));
//                    break;
//                }
//                case NOTIFI:
//                    Animation animation = null;
//                    list.clear();
//                    slist.clear();
//                    List<NoticeBean> data = (List<NoticeBean>) result;
//                    if (data != null && data.size() > 0) {
//
//                        animation = AnimationUtils.loadAnimation(Vthis, R.anim.slide_bottom_in);
//                        ll_notifi.setVisibility(View.VISIBLE);
//                        EventBus.getDefault().post(BusEvent.getEvent(EventBusId.PINHUO_TAB_BOTTOM_SQME_LAYOUT_HEIGHT));
//                        // ll_notifi.startAnimation(animation);
//                        for (int i = 0; i < data.size(); i++) {
//                            NoticeBean bean = data.get(i);
//                            bean.setNums_content(i + 1 + ": " + bean.getTitle());
//                            list.add(bean);
//                            slist.add(i + 1 + ": " + bean.getTitle());
//                        }
//                        tv_notifi_marquee.setContentList(slist);
//                        //tv_notifi_auto.startFlipping();
//                        // marqueeFactory.resetData(list);
//                    } else {
//                        animation = AnimationUtils.loadAnimation(Vthis, R.anim.slide_bottom_out);
//                        // ll_notifi.startAnimation(animation);
//                        ll_notifi.setVisibility(View.GONE);
//                    }
//                    break;
//            }
//        }
//    }
//
//    long Avail_MaxID, Avail_Total;
//
//    private void setNums(Object result) {
//        if (result instanceof String && ((String) result).startsWith("error:")) {
//            ViewHub.showLongToast(Vthis, ((String) result).replace("error:", ""));
//        } else {
//            int waitPayCount = 0;
//            int waitShipCount = 0;
//            int shipedCount = 0;
//            int overedCount = 0;
//            int cancelCount = 0;
//            int readyCount = 0;
//            int saleAfterCount = 0;
//            JSONObject jsonObject;
//            JSONArray ja;
//            JSONObject acJsonObject;
//            JSONObject pJsonObject;
//            try {
//                jsonObject = new JSONObject(result.toString());
//                readyCount = jsonObject.optInt("ReadyCount", 0);
//                String OrderStatuList = jsonObject.optString("OrderStatuList");
//                String AvailableCouponInfo = jsonObject.optString("AvailableCouponInfo");
//                String Promote = jsonObject.optString("Promote");
//                if (!TextUtils.isEmpty(Promote)) {
//                    pJsonObject = new JSONObject(Promote);
//                    Summary = pJsonObject.optString("Summary");
//                    Content = pJsonObject.optString("Content");
//                    ShareTitle = pJsonObject.optString("ShareTitle");
//                    ShareContent = pJsonObject.optString("ShareContent");
//                    ShareUrl = pJsonObject.optString("ShareUrl");
//                    if (!TextUtils.isEmpty(Summary)) {
//                        setItemRightText(R.id.item_me_invite_register, Summary);
//                    }
//                }
//                if (!TextUtils.isEmpty(AvailableCouponInfo)) {
//                    acJsonObject = new JSONObject(AvailableCouponInfo);
//                    Avail_MaxID = acJsonObject.optLong("MaxID", 0);
//                    Avail_Total = acJsonObject.optLong("Total", 0);
//                    long id = SpManager.getQuickMeCouponMaxID(this);
//                    if (Avail_Total > 0) {
//                        setItemRightText(R.id.item_mycoupon, Avail_Total + "张可用");
//                    }
//                    if (id < Avail_MaxID) {
//                        // SpManager.setQuickMeCouponMaxID(this, Avail_MaxID);
//                        if (Avail_Total > 0) {
//                            MsgRed msgRed = new MsgRed();
//                            msgRed.setIs_Show(true);
//                            msgRed.setCount(Avail_Total);
//                            mEventBus.post(BusEvent.getEvent(EventBusId.PINHUO_ME_RED_IS_SHOW, msgRed));
//                            // setItemRightText(R.id.item_mycoupon, Avail_Total + "张可用");
//                            setItemRightRegent(R.id.item_mycoupon, "new", true, 40, 18);
//                        }
//
//                    }
//
//                }
//                ja = new JSONArray(OrderStatuList + "");
//                for (int i = 0; i < ja.length(); i++) {
//                    JSONObject jo = ja.getJSONObject(i);
//                    switch (jo.getInt("StatuID")) {
//                        case Const.OrderStatus.WAIT_PAY:
//                            waitPayCount = jo.getInt("Amount");
//                            break;
//                        case Const.OrderStatus.SHIPED:
//                            shipedCount = jo.getInt("Amount");
//                            break;
//                        case Const.OrderStatus.DONE:
//                            overedCount = jo.getInt("Amount");
//                            break;
//                        case Const.OrderStatus.CANCEL:
//                            cancelCount = jo.getInt("Amount");
//                            break;
//                        case Const.OrderStatus.WAIT_SHIP:
//                            waitShipCount = jo.getInt("Amount");
//                            break;
//                        case Const.OrderStatus.REFUND:
//                            saleAfterCount = jo.getInt("Amount");
//                            break;
//                    }
//                }
//
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//            if (saleAfterCount > 0) {
//                setItemSaleAfterRightCircle(R.id.circle_order_sale_after, saleAfterCount + "", true);
//            } else {
//                setItemSaleAfterRightCircle(R.id.circle_order_sale_after, saleAfterCount + "", false);
//            }
//            if (readyCount > 0) {
//                setItemRightCircle(R.id.item_my_goto_tuan, readyCount + "", true);
//            } else {
//                setItemRightCircle(R.id.item_my_goto_tuan, readyCount + "", false);
//            }
//            if (waitPayCount > 0) {
//                ((CircleTextView) findViewById(R.id.circle_order_text0)).setText(waitPayCount + "");
//                findViewById(R.id.circle_order_text0).setVisibility(View.VISIBLE);
//            } else {
//                findViewById(R.id.circle_order_text0).setVisibility(View.GONE);
//            }
//            if (waitShipCount > 0) {
//                ((CircleTextView) findViewById(R.id.circle_order_text1)).setText(waitShipCount + "");
//                findViewById(R.id.circle_order_text1).setVisibility(View.VISIBLE);
//            } else {
//                findViewById(R.id.circle_order_text1).setVisibility(View.GONE);
//            }
//            if (shipedCount > 0) {
//                ((CircleTextView) findViewById(R.id.circle_order_text2)).setText(shipedCount + "");
//                findViewById(R.id.circle_order_text2).setVisibility(View.VISIBLE);
//            } else {
//                findViewById(R.id.circle_order_text2).setVisibility(View.GONE);
//            }
//        }
//    }
//}
