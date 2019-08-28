package com.nahuo.quicksale;

import android.app.Activity;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.EdgeEffectCompat;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.gson.internal.LinkedTreeMap;
import com.luck.picture.lib.tools.ScreenUtils;
import com.nahuo.Dialog.BuPinDomDialog;
import com.nahuo.Dialog.GDialog;
import com.nahuo.Dialog.PdMenu;
import com.nahuo.bean.ColorPicsBean;
import com.nahuo.bean.WenXinPics;
import com.nahuo.constant.UpYunIcon;
import com.nahuo.library.controls.BottomMenu;
import com.nahuo.library.controls.CircleTextView;
import com.nahuo.library.controls.FlowIndicator;
import com.nahuo.library.controls.FlowLayout;
import com.nahuo.library.controls.LoadingDialog;
import com.nahuo.library.controls.PagerScrollView;
import com.nahuo.library.helper.DisplayUtil;
import com.nahuo.library.helper.FunctionHelper;
import com.nahuo.library.helper.GsonHelper;
import com.nahuo.library.helper.ImageUrlExtends;
import com.nahuo.library.utils.TimeUtils;
import com.nahuo.quicksale.Topic.PostDetailActivity;
import com.nahuo.quicksale.activity.StallReasonListActivity;
import com.nahuo.quicksale.activity.VideoActivity1;
import com.nahuo.quicksale.adapter.ImageAdapter;
import com.nahuo.quicksale.adapter.RelatedGoodsAdapter;
import com.nahuo.quicksale.adapter.VideoAdapter;
import com.nahuo.quicksale.api.AgentAPI;
import com.nahuo.quicksale.api.BuyOnlineAPI;
import com.nahuo.quicksale.api.GoodsCountApi;
import com.nahuo.quicksale.api.QuickSaleApi;
import com.nahuo.quicksale.app.BWApplication;
import com.nahuo.quicksale.base.BaseAppCompatActivity;
import com.nahuo.quicksale.common.BaiduStats;
import com.nahuo.quicksale.common.ChatHelper;
import com.nahuo.quicksale.common.Const;
import com.nahuo.quicksale.common.Const.ApplyAgentStatu;
import com.nahuo.quicksale.common.ListUtils;
import com.nahuo.quicksale.common.MediaStoreUtils;
import com.nahuo.quicksale.common.NahuoDetailsShare;
import com.nahuo.quicksale.common.SpManager;
import com.nahuo.quicksale.common.TuanPiUtil;
import com.nahuo.quicksale.common.Utils;
import com.nahuo.quicksale.controls.RectangleTextSpan;
import com.nahuo.quicksale.controls.SelectSizeColorMenu;
import com.nahuo.quicksale.controls.SelectSizeColorMenu.SelectMenuDismissListener;
import com.nahuo.quicksale.di.module.HttpManager;
import com.nahuo.quicksale.eventbus.BusEvent;
import com.nahuo.quicksale.eventbus.EventBusId;
import com.nahuo.quicksale.model.http.CommonSubscriber;
import com.nahuo.quicksale.model.http.response.PinHuoResponse;
import com.nahuo.quicksale.oldermodel.GoodBaseInfo;
import com.nahuo.quicksale.oldermodel.ItemDetailShopInfo;
import com.nahuo.quicksale.oldermodel.ItemShopCategory;
import com.nahuo.quicksale.oldermodel.OrderButton;
import com.nahuo.quicksale.oldermodel.ProductModel;
import com.nahuo.quicksale.oldermodel.PublicData;
import com.nahuo.quicksale.oldermodel.Share2WPItem;
import com.nahuo.quicksale.oldermodel.ShopItemListModel;
import com.nahuo.quicksale.oldermodel.ShopItemModel;
import com.nahuo.quicksale.oldermodel.UpdateItem;
import com.nahuo.quicksale.upyun.api.utils.TimeCounter;
import com.nahuo.quicksale.util.ChatUtl;
import com.nahuo.quicksale.util.CircleCarTxtUtl;
import com.nahuo.quicksale.util.DownloadService;
import com.nahuo.quicksale.util.FileUriUtil;
import com.nahuo.quicksale.util.GlideUtls;
import com.nahuo.quicksale.util.GuidePreference;
import com.nahuo.quicksale.util.JsonKit;
import com.nahuo.quicksale.util.RxUtil;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.greenrobot.event.EventBus;

import static com.nahuo.quicksale.tabfragment.sort.SortTabFragment.Tag;


/**
 * @author ZZB
 * @description 商品详情
 * @created 2014-10-17 上午10:38:19
 */
public class ItemDetailsActivity extends BaseAppCompatActivity implements View.OnClickListener {

    private static final int REQUEST_SHARE_TO_WP = 1;
    private static final int REQUEST_UPDATE_MY_ITEM = 2;
    private Context mContext = this;
    private LoadingDialog mDialog;
    private ItemDetailsActivity vThis = this;
    /** 判断是否转发 */
    // public static final String EXTRA_SHARE_STATU = "EXTRA_SHARE_STATU";
    /**
     * 判断是否是自己上传的款
     */
    // public static final String EXTRA_IS_SOURCE = "EXTRA_IS_SOURCE";
    /**
     * 必须传int类型，否则会找不到商品！
     */
    public static final String EXTRA_ID = "EXTRA_ID";
    public static final String EXTRA_PIN_HUO_ID = "EXTRA_PIN_HUO_ID";
    public static final String EXTRA_UPDATE_ITEM = "EXTRA_UPDATE_ITEM";
    /**
     * 成功转发到微铺的item id
     */
    public static final String EXTRA_SHARED_ITEM_ID = "EXTRA_SHARED_ITEM_ID";
    private DecimalFormat df = new DecimalFormat("#0.00");
    private ViewPager mPager;
    //private SlidePicPagerAdapter1 mPagerAdapter;
    private FlowIndicator mLayoutIndicator;
    private TextView mTvDesc, mTvRetailPrice, tv_pin, mTvAgentPrice, mTvShopName, mTvSignature, mTvShare2Wp,
            mTvFavorite, mTvEnterWp, mTvTitle, mTvViewOrgItem, tv_rigt_bg, tv_left_bg, tv_rigt_bg_chat, tv_re, tv_ori_price;
    private ImageView tvTRight_chat;
    private View layout_stall, expand_stall, layout_buyer_shop;
    private View mBtnApplyAgent;
    private ImageView mIvShopLogo, iv_coin_pay_icon;
    private FlowLayout mLayoutCreditBages;
    private FlowLayout mGvTags;
    private int mId;
    private int chengtuanCount;
    private int dealCount;
    private boolean tuanpiOver;
    // 自己上传和转发的商品
    private boolean mIsSelf;
    private ItemDetailShopInfo mShopInfo;
    // private ItemDetailUserInfo mUserInfo;
    private boolean mLoadingShopInfo = true;
    private boolean mLoadingUserInfo = true;
    // 颜色对应的尺码
    private Map<String, String> mColorSizeMap = new HashMap<String, String>();
    // 尺码对应的颜色
    private Map<String, String> mSizeColorMap = new HashMap<String, String>();

    private Set<String> mColors = new LinkedHashSet<String>();
    private Set<String> mSizes = new LinkedHashSet<String>();
    private WebView mWebView, wv_item_detail_des;
    private ShopItemModel mShopItem;
    // 分享到微铺之后的item
    private UpdateItem mUpdateItem;
    private ArrayList<String> mBasePicUrls = new ArrayList<String>();
    private Html.ImageGetter imageGetter;
    protected String mSelectedColor, mSelectedSize;
    protected int mSelectedBuyCount;
    private List<ItemShopCategory> mItemShopCategories;
    private View mRootView;
    private ShopItemModel mShopItemModel;
    private TextView mTvWeixun;
    private TextView tv_title_top;
    private View mTvShare;
    private TextView tvChengTuanTips, tvChengTuanTipsDetail;
    private ProgressBar progress;
    private EventBus mEventBus = EventBus.getDefault();
    private RelativeLayout mBtnBuy, rl_scend;
    private View rlTContent;
    private FlowLayout buttons;
    private int mCurrentPs = 0;
    private int mPinHuoId;
    private CircleTextView circle_car_text, circle_car_text_chat;
    TextView circle_car_text2, tv_collect, tv_btn;
    private ColorDrawable mDrawable;
    private LinearLayout ll_time;
    private TextView mTvHH, mTvH, mTvMM, mTvM, mTvSS, mTvS, mTvF, mTvDay, mTvDayDesc;
    private long mEndMillis = 0;
    private int sc_y = 0;
    private ArrayList<String> urls = new ArrayList<String>();
    private int totalSaleCount = 0;
    private boolean isOnPause = false;
    private ListView lv_videos;
    private VideoAdapter adapter;
    private LinearLayout ll_propertys_tags;
    private View layout_explain, layout_related_goods, layout_shop_cart;
    private GridView lv_related_goods;
    private RelatedGoodsAdapter relatedGoodsAdapter;
    private int StallID;

    private enum Step {
        SHOP_RECORD, PINHUOITEM_DETAIL, LOAD_SHOP_ITEM, APPLY_AGENT, LOAD_SHOP_INFO, BUY, FAVORITE_ITEM, CHECKMYITEMFAVORITE, REMOVE_FAVORITE_ITEM, TUANPI, SaveReplenishmentRecord
    }

    int useId;
    private boolean current_flag = false;
    boolean CanDownLoadPicAndVideo;
    EdgeEffectCompat leftEdge, rightEdge;
    LinearLayout layout_bottom_new;
    private ImageAdapter imageAdapter;
    private List<View> views = new ArrayList<>();
    private ImageView iv_buyer_shop;
    private TextView tv_buyer_shop_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);
        getWindow().setFormat(PixelFormat.TRANSLUCENT);//设置窗口格式为半透明
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
//                WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);//开启硬件加速
        if (mEventBus != null) {
            if (!mEventBus.isRegistered(this)) {//加上判断
                mEventBus.register(this);
            }
        }
        BWApplication.addActivity(this);
        BWApplication.getInstance().registerActivity(this);
        imageGetter = new Html.ImageGetter() {
            @Override
            public Drawable getDrawable(String source) {
                Drawable drawable = null;
                int rId = Integer.parseInt(source);
                drawable = mContext.getResources().getDrawable(rId);
                drawable.setBounds(0, 0, Const.getQQFaceWidth(mContext), Const.getQQFaceWidth(mContext));
                return drawable;
            }

        };
        try {
            initExtras();
            initView();
            loadData();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadCountDown() {
        //long endMillis = SpManager.getQuickSellEndTime(mContext) - System.currentTimeMillis();
        long endMillis = mEndMillis - System.currentTimeMillis();
        if (endMillis > 0) {
            MyCountDownTimer mOnlineCountDownTimer = new MyCountDownTimer(endMillis);
            mOnlineCountDownTimer.start();
            ll_time.setVisibility(View.VISIBLE);
            // tv_title_top.setVisibility(View.GONE);
        } else {
            ll_time.setVisibility(View.GONE);
            //tv_title_top.setVisibility(View.VISIBLE);
        }


    }

    PdMenu pdMenu = null;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
                int size = msg.arg1;
                int count = msg.arg2;
                pdMenu = PdMenu.getInstance(ItemDetailsActivity.this);
                pdMenu.dShow("正在下载" + size + "/" + count);
                if (size >= count) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (pdMenu != null) {
                                pdMenu.dismiss();
                                pdMenu = null;
                            }
                        }
                    }, 1000);
                    copyTittle();
                }
            }

        }
    };

    private int share_type = -1;
    private List<String> dList = new ArrayList<>();
    private ArrayList<Uri> uList = new ArrayList<>();
    private ArrayList<Uri> allList = new ArrayList<>();
    private String mShareTittle = "";

    public void onEventMainThread(BusEvent event) {

        switch (event.id) {
            case EventBusId.ITEM_DETAIL_REFESH:
                if (vThis != null) {
                    loadData();
                }
                break;
            case EventBusId.WEIXUN_NEW_MSG:
                if (circle_car_text_chat != null)
                    ChatUtl.judeChatNums(circle_car_text_chat, event);
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
                break;
            case EventBusId.ON_SHARE_2_WP_SUCCESS:// 分享完成

                Share2WPItem wpItem = (Share2WPItem) event.data;// (Share2WPItem)data.getSerializableExtra(Share2WPActivity.EXTRA_SHARED_ITEM);
                int itemCopyType = wpItem.isCloneable() ? Const.ShopItemCopyType.ALREADY_COPIED : Const.ShopItemCopyType.ALREADY_SHARED;
                mShopItem.setItemCopyType(itemCopyType);
                updateShare2WP(itemCopyType);
                mUpdateItem = newUpdateItem(wpItem);
                Intent intent = new Intent();
                intent.putExtra(EXTRA_SHARED_ITEM_ID, mId);
                setResult(RESULT_OK, intent);
                break;
            case EventBusId.SHARE_WEIXIN_PICS:
                //微信多图片分享
                WenXinPics bean = (WenXinPics) event.data;
                final String title = bean.getTitle();
                mShareTittle = bean.getTitle();
                List<String> pic_imag = new ArrayList<>();
                share_type = bean.getType();
                if(share_type>1){
                    for (int k=0;k<bean.getImages().size();k++) {
                        pic_imag.add(bean.getImages().get(k));
                        if (k>=8){
                            break;
                        }
                    }
                }else {
                    pic_imag.addAll(bean.getImages());
                }
                dList.clear();
                uList.clear();
                File downloadDirectory = new File(DownloadService.PINHUP_ROOT_DIRECTORY);
                if (downloadDirectory.exists()) {
                    for (String fileName : pic_imag) {
                        File cacheFile = new File(DownloadService.createFilePath(downloadDirectory, fileName));
                        if (cacheFile.exists()) {
                            uList.add(FileUriUtil.getWxFileProviderUri(vThis, cacheFile));
                        } else {
                            dList.add(fileName);
                        }
                    }
                    if (ListUtils.isEmpty(dList)) {
                        final Intent weChatIntent = new Intent();
                        if (uList.size() == 0)
                            return;
//                        try {
//                            if (!ListUtils.isEmpty(uList)) {
//                                for (Uri uri : uList) {
//                                    MediaStoreUtils.scanImageFile(mContext, uri);
//                                }
//                            }
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                            ViewHub.showLongToast(mContext, "更新图片到系统相册失败，请在Picture目录查找");
//                        } finally
                        final ArrayList<Uri> shareList = new ArrayList<>();
                        for (int i = 0; i < uList.size(); i++) {
                            if (i < 9) {
                                shareList.add(uList.get(i));
                            }
                        }
                        {
                            if (share_type == 1) {
                                weChatIntent.setComponent(new ComponentName("com.tencent.mm", "com.tencent.mm.ui.tools.ShareImgUI"));
                                weChatIntent.setAction(Intent.ACTION_SEND_MULTIPLE);
                                weChatIntent.setType("image/*");
                                weChatIntent.putExtra(Intent.EXTRA_STREAM, shareList);
                                startActivity(weChatIntent);
                            } else if (share_type == 2) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        GDialog commDialog = GDialog.getInstance(vThis)
                                                .setContent("由于微信6.7.3及以上版本做了多图分享的限制，我们已经将图片保存到您的相册，您需要分享时手动选择其他的照片。").setLeftStr("立即分享").setRightStr("取消")
                                                .setPositive(new GDialog.PopDialogListener() {
                                                    @Override
                                                    public void onPopDialogButtonClick(int ok_cancel, GDialog.DialogType type) {
                                                        if (ok_cancel == GDialog.BUTTON_POSITIVIE) {
                                                            ArrayList<Uri> oneList = new ArrayList<>();
                                                            oneList.add(shareList.get(0));
                                                            weChatIntent.setComponent(new ComponentName("com.tencent.mm", "com.tencent.mm.ui.tools.ShareToTimeLineUI"));
                                                            weChatIntent.setAction(Intent.ACTION_SEND_MULTIPLE);
                                                            weChatIntent.setType("image/*");
                                                            weChatIntent.putExtra(Intent.EXTRA_STREAM, oneList);
                                                            weChatIntent.putExtra("Kdescription", title); //分享描述
                                                            startActivity(weChatIntent);
                                                        }
                                                    }
                                                });
                                        commDialog.showDialog();
                                    }
                                });
                            } else if (share_type == 4) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        GDialog commDialog = GDialog.getInstance(vThis)
                                                .setContent("由于微信7.0.3及以上版本做了多图分享的限制，我们已经将图片保存到您的相册，您需要手动到朋友圈选择照片分享。").setLeftStr("立即分享").setRightStr("取消")
                                                .setPositive(new GDialog.PopDialogListener() {
                                                    @Override
                                                    public void onPopDialogButtonClick(int ok_cancel, GDialog.DialogType type) {
                                                        if (ok_cancel == GDialog.BUTTON_POSITIVIE) {
                                                            try {
                                                                Intent intent = new Intent();
                                                                ComponentName cmp = new ComponentName("com.tencent.mm", "com.tencent.mm.ui.LauncherUI");
                                                                intent.setAction(Intent.ACTION_MAIN);
                                                                intent.addCategory(Intent.CATEGORY_LAUNCHER);
                                                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                                intent.setComponent(cmp);
                                                                startActivity(intent);
                                                            } catch (Exception e) {
                                                                e.printStackTrace();
                                                                ViewHub.showShortToast(vThis, "检查到您手机有可能没有安装微信，请安装后使用该功能");
                                                            }

                                                        }
                                                    }
                                                });
                                        commDialog.showDialog();
                                    }
                                });
                            } else if (share_type == 3) {
                                weChatIntent.setComponent(new ComponentName("com.tencent.mm", "com.tencent.mm.ui.tools.ShareToTimeLineUI"));
                                weChatIntent.setAction(Intent.ACTION_SEND_MULTIPLE);
                                weChatIntent.setType("image/*");
                                weChatIntent.putExtra(Intent.EXTRA_STREAM, shareList);
                                weChatIntent.putExtra("Kdescription", title); //分享描述
                                startActivity(weChatIntent);
                            }
                            copyTittle();
                        }
                    } else {
                        gotoDownloadPics(title, dList, -2);
                    }
                } else {
                    gotoDownloadPics(title, pic_imag, -1);
                }

                break;

        }
    }

    private void copyTittle() {
        Utils.addToClipboard(this, mShareTittle);
    }

    private void gotoDownloadPics(final String title, List<String> pic_imag, final int d_methor) {
        DownloadService.getInstace(ItemDetailsActivity.this, DownloadService.PINHUP_ROOT_DIRECTORY, pic_imag, new DownloadService.DownloadStateListener() {

            @Override
            public void onFinish(ArrayList<Uri> imageList) {
                //图片下载成功后，实现您的代码
                allList.clear();
                final Intent weChatIntent = new Intent();
                if (d_methor == -1) {
                    //下载全部
                    if (!ListUtils.isEmpty(imageList))
                        allList.addAll(imageList);
                } else if (d_methor == -2) {
                    if (!ListUtils.isEmpty(imageList)) {
                        allList.addAll(imageList);
                    }
                    if (!ListUtils.isEmpty(uList)) {
                        allList.addAll(uList);
                    }
                }
                if (allList.size() == 0)
                    return;
                try {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (!ListUtils.isEmpty(allList)) {
                                for (Uri uri : allList) {
                                    MediaStoreUtils.scanImageFile(mContext, uri);
                                }
                            }
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ViewHub.showLongToast(mContext, "更新图片到系统相册失败，请在pinhuo/pinhuo_save目录查找");
                        }
                    });
                } finally {
                    final ArrayList<Uri> shareList = new ArrayList<>();
                    for (int i = 0; i < allList.size(); i++) {
                        if (i < 9) {
                            shareList.add(allList.get(i));
                        }
                    }
                    if (share_type == 1) {
                        weChatIntent.setComponent(new ComponentName("com.tencent.mm", "com.tencent.mm.ui.tools.ShareImgUI"));
                        weChatIntent.setAction(Intent.ACTION_SEND_MULTIPLE);
                        weChatIntent.setType("image/*");
                        weChatIntent.putExtra(Intent.EXTRA_STREAM, shareList);
                        startActivity(weChatIntent);
                    } else if (share_type == 2) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                GDialog commDialog = GDialog.getInstance(vThis)
                                        .setContent("由于微信6.7.3及以上版本做了多图分享的限制，我们已经将图片保存到您的相册，您需要分享时手动选择其他的照片。").setLeftStr("立即分享").setRightStr("取消")
                                        .setPositive(new GDialog.PopDialogListener() {
                                            @Override
                                            public void onPopDialogButtonClick(int ok_cancel, GDialog.DialogType type) {
                                                if (ok_cancel == GDialog.BUTTON_POSITIVIE) {
                                                    ArrayList<Uri> oneList = new ArrayList<>();
                                                    oneList.add(shareList.get(0));
                                                    weChatIntent.setComponent(new ComponentName("com.tencent.mm", "com.tencent.mm.ui.tools.ShareToTimeLineUI"));
                                                    weChatIntent.setAction(Intent.ACTION_SEND_MULTIPLE);
                                                    weChatIntent.setType("image/*");
                                                    weChatIntent.putExtra(Intent.EXTRA_STREAM, oneList);
                                                    weChatIntent.putExtra("Kdescription", title); //分享描述
                                                    startActivity(weChatIntent);
                                                }
                                            }
                                        });
                                commDialog.showDialog();
                            }
                        });

                    } else if (share_type == 4) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                GDialog commDialog = GDialog.getInstance(vThis)
                                        .setContent("由于微信7.0.3及以上版本做了多图分享的限制，我们已经将图片保存到您的相册，您需要手动到朋友圈选择照片分享。").setLeftStr("立即分享").setRightStr("取消")
                                        .setPositive(new GDialog.PopDialogListener() {
                                            @Override
                                            public void onPopDialogButtonClick(int ok_cancel, GDialog.DialogType type) {
                                                if (ok_cancel == GDialog.BUTTON_POSITIVIE) {
                                                    try {
                                                        Intent intent = new Intent();
                                                        ComponentName cmp = new ComponentName("com.tencent.mm", "com.tencent.mm.ui.LauncherUI");
                                                        intent.setAction(Intent.ACTION_MAIN);
                                                        intent.addCategory(Intent.CATEGORY_LAUNCHER);
                                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                        intent.setComponent(cmp);
                                                        startActivity(intent);
                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                        ViewHub.showShortToast(vThis, "检查到您手机有可能没有安装微信，请安装后使用该功能");
                                                    }

                                                }
                                            }
                                        });
                                commDialog.showDialog();
                            }
                        });
                    } else if (share_type == 3) {
                        weChatIntent.setComponent(new ComponentName("com.tencent.mm", "com.tencent.mm.ui.tools.ShareToTimeLineUI"));
                        weChatIntent.setAction(Intent.ACTION_SEND_MULTIPLE);
                        weChatIntent.setType("image/*");
                        weChatIntent.putExtra(Intent.EXTRA_STREAM, shareList);
                        weChatIntent.putExtra("Kdescription", title); //分享描述
                        startActivity(weChatIntent);
                    }
                }
            }

            @Override
            public void onFailed() {
                //图片下载成功后，实现您的代码
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ViewHub.showShortToast(ItemDetailsActivity.this, "图片下载失败");
                    }
                });

            }

            @Override
            public void onUpdate(int size, int count) {
                Message message = Message.obtain();
                message.arg1 = size;
                message.arg2 = count;
                message.what = 0;
                handler.sendMessage(message);
            }
        }).startDownload();
    }

    private class MyCountDownTimer extends CountDownTimer {
        public MyCountDownTimer(long millisInFuture) {
            super(millisInFuture, 100);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            // long millisUntilFinished_now = SpManager.getQuickSellEndTime(mContext) - System.currentTimeMillis();
            long millisUntilFinished_now = mEndMillis - System.currentTimeMillis();
            int hour = (int) (millisUntilFinished_now / TimeUtils.HOUR_MILLIS);
            int min = (int) ((millisUntilFinished_now - hour * TimeUtils.HOUR_MILLIS) / TimeUtils.MINUTE_MILLIS);
            int sec = (int) ((millisUntilFinished_now - hour * TimeUtils.HOUR_MILLIS - min * TimeUtils.MINUTE_MILLIS) / TimeUtils.SECOND_MILLIS);
            int milli = (int) (millisUntilFinished_now - hour * TimeUtils.HOUR_MILLIS - min * TimeUtils.MINUTE_MILLIS - sec * TimeUtils.SECOND_MILLIS);
            updateCountDownTime(hour, min, sec, milli);
        }

        @Override
        public void onFinish() {
            try {
                ll_time.setVisibility(View.GONE);
                // tv_title_top.setVisibility(View.VISIBLE);
                //  EventBus.getDefault().post(BusEvent.getEvent(EventBusId.REFRESH_PIN_HUO));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {
            case REQUEST_SHARE_TO_WP:// 分享到微铺回调，改用EventBus ON_SHARE_2_WP_SUCCESS

                break;
            case REQUEST_UPDATE_MY_ITEM:// 修改我自己上传商品的回调
                if (data != null) {
//                    ShopItemModel item = (ShopItemModel) data
//                            .getSerializableExtra(UploadItemActivity.EXTRA_UPLOADED_ITEM);
//                    mTvDesc.setText(ShopItemListModel.getTextHtml(item.getIntroOrName(), mContext, imageGetter));
//                    mTvDesc.setTag(item.getIntroOrName());
//                    mTvTitle.setText(ShopItemListModel.getTextHtml(item.getName(), mContext, imageGetter));
//                    mTvTitle.setTag(item.getName());
//                    formatServicePrice(item.getRetailPrice());
//                    formatAgentPrice(item.getPrice());
                }
                break;
//            case UpdateSharedItemActivity.REQUEST_CODE_UPDATE_WP_ITEM:// 修改我转发的商品的回调
//                if (data != null) {
//                    UpdateItem updateItem = (UpdateItem) data
//                            .getSerializableExtra(UpdateSharedItemActivity.EXTRA_UPDATED_ITEM);
//                    updateContent(updateItem);
//                }
//
//                break;
        }

    }

//    /**
//     * @description 更新显示内容
//     * @created 2015-4-1 下午2:06:58
//     * @author ZZB
//     */
//    private void updateContent(UpdateItem updateItem) {
//        mTvDesc.setText(ShopItemListModel.getTextHtml(updateItem.getIntro(), mContext, imageGetter));
//        mTvDesc.setTag(updateItem.getIntro());
//        mTvTitle.setText(ShopItemListModel.getTextHtml(updateItem.title, mContext, imageGetter));
//        mTvTitle.setTag(updateItem.title);
//        formatServicePrice(Double.valueOf(updateItem.retailPrice));
//        formatAgentPrice(Double.valueOf(updateItem.agentPrice));
//    }

    private void loadData() {
        //new Task(Step.PINHUOITEM_DETAIL).execute();
        // 加载商品信息
        // new Task(Step.LOAD_SHOP_ITEM).execute();
        // 加载店铺信息
        // new Task(Step.LOAD_SHOP_INFO).execute();
        // 加载团批信息
        // new Task(Step.TUANPI).execute();
        // 加载是否收藏
        // new Task(Step.CHECKMYITEMFAVORITE).execute();
        addSubscribe(HttpManager.getInstance().getPinHuoNoCacheApi(Tag).getPinhuoDetail(mId, mPinHuoId)
                .compose(RxUtil.<PinHuoResponse<Object>>rxSchedulerHelper())
                .compose(RxUtil.<Object>handleResult())
                .subscribeWith(new CommonSubscriber<Object>(vThis, true, R.string.loading) {
                    @Override
                    public void onNext(Object object) {
                        super.onNext(object);
                        try {
                            LinkedTreeMap map = (LinkedTreeMap) object;
                            String json = JsonKit.mapToJson(map, null).toString();
                            mShopItem = getDetailResult(json);
                            if (mUpdateItem == null) {
                                setUpUpdateItem();
                            }
                            if (mShopItem != null) {
                                mShopItem.setMyUserId(useId);
                            }
                            ShopItemModel bean = mShopItem;
                            onShopItemLoaded(bean);
                            boolean isMyFavorite1 = false;
                            isMyFavorite1 = bean.isFavorite();
                            CanDownLoadPicAndVideo = bean.CanDownLoadPicAndVideo;
                            //Log.e("Itemyu", "===>" + isMyFavorite1 + "");
                            itemFavoriteChange(isMyFavorite1);
                            ShopItemModel.ActivityBean mAbean = bean.getActivity();
                            String endTime = "";
                            if (mAbean != null) {
                                chengtuanCount = mAbean.getChengTuanCount();
                                dealCount = mAbean.getTransCount();
                                totalSaleCount = mAbean.getTotalSaleCount();
                                tuanpiOver = !mAbean.isIsStart();
                                endTime = mAbean.getEndTime();
                                if (mAbean.isShowCoinPayIcon()) {
                                    iv_coin_pay_icon.setVisibility(View.VISIBLE);
                                    GlideUtls.glidePic(vThis, UpYunIcon.ICON_DETAIL, iv_coin_pay_icon);
                                } else {
                                    iv_coin_pay_icon.setVisibility(View.GONE);
                                }
                            }
                            onTuanPiDataLoaded();
                            mEndMillis = getMillis(endTime);
                            loadCountDown();
                            // new Task(Step.SHOP_RECORD).execute();
                            setItemVisitRecord(mId);
                            if (useId > 0 && !mShopItem.getItemStatu().equals("已下架")) {
                                int detail_first = GuidePreference.getInstance().getSaveShopCartDeatail(useId);
                                if (detail_first == 1) {
                                    layout_explain.setVisibility(View.GONE);
                                } else {
                                    //layout_explain.setVisibility(View.VISIBLE);
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }));
    }

    private void setItemVisitRecord(int id) {
        if (id > 0) {
            addSubscribe(HttpManager.getInstance().getPinHuoNoCacheApi(Tag).setItemVisitRecord(id)
                    .compose(RxUtil.<PinHuoResponse<Object>>rxSchedulerHelper())
                    .compose(RxUtil.handleResult())
                    .subscribeWith(new CommonSubscriber<Object>(vThis) {
                        @Override
                        public void onNext(Object o) {
                            super.onNext(o);
                        }
                    }));
        }
    }

    private ShopItemModel getDetailResult(String json) throws JSONException {
        ShopItemModel result = null;
        List<ProductModel> list = new ArrayList<>();
        JSONObject jsonObject = new JSONObject(json);
        List<ColorPicsBean> colorPicsList = new ArrayList<>();
        if (list != null)
            list.clear();
        JSONArray jsonArray = jsonObject.optJSONArray("Products");
        double price = 0.0;
        String sPrice = jsonObject.optString("Price");
        String MainColorPic = "";
        MainColorPic = jsonObject.optString("MainColorPic");
        if (!TextUtils.isEmpty(MainColorPic)) {
            ColorPicsBean colorPicsBean = new ColorPicsBean();
            colorPicsBean.setUrl(MainColorPic);
            colorPicsBean.setColor("");
            colorPicsList.add(colorPicsBean);
        }
        if (!TextUtils.isEmpty(sPrice))
            price = Double.parseDouble(sPrice);
        for (int i = 0; i < jsonArray.length(); i++) {

            JSONObject jsonObject1 = new JSONObject(jsonArray.get(i).toString());
            String color = jsonObject1.optString("Color");
            String colorPic = jsonObject1.optString("ColorPic");
            if (!TextUtils.isEmpty(colorPic)) {
                ColorPicsBean colorPicsBean = new ColorPicsBean();
                colorPicsBean.setColor(color);
                colorPicsBean.setUrl(colorPic);
                colorPicsList.add(colorPicsBean);
            }
            JSONObject jsonObject2 = new JSONObject(jsonObject1.optString("Sizes").toString());
            Iterator it = jsonObject2.keys();
            while (it.hasNext()) {
                ProductModel productModel = new ProductModel();
                productModel.setColor(color);
                productModel.setColorPic(colorPic);
                String key = (String) it.next();
                if (key != null) {
                    productModel.setSize(key.toString());
                    int value = jsonObject2.optInt(key.toString());
                    productModel.setStock(value);
                }
                list.add(productModel);
            }

        }
        result = GsonHelper.jsonToObject(json, ShopItemModel.class);
        result.setProducts(list);
        result.setRetailPrice(price);
        result.setColorPicsBeanList(colorPicsList);
        return result;
    }

    private void initExtras() {
        Intent intent = getIntent();
        mId = intent.getIntExtra(EXTRA_ID, -1);
        mPinHuoId = intent.getIntExtra(EXTRA_PIN_HUO_ID, -1);
        mUpdateItem = (UpdateItem) intent.getSerializableExtra(EXTRA_UPDATE_ITEM);
    }


    private void initView() {
        // initTitleBar();
        // setContentHide(true);
        //initVideoView();
        mRootView = findViewById(R.id.rootView);
        iv_buyer_shop = (ImageView) findViewById(R.id.iv_buyer_shop);
        tv_buyer_shop_name = (TextView) findViewById(R.id.tv_buyer_shop_name);
        layout_bottom_new = (LinearLayout) findViewById(R.id.layout_bottom_new);
        lv_related_goods = (GridView) findViewById(R.id.lv_related_goods);
        relatedGoodsAdapter = new RelatedGoodsAdapter(this);
        lv_related_goods.setAdapter(relatedGoodsAdapter);
        ll_propertys_tags = (LinearLayout) findViewById(R.id.propertys_tags);
        lv_videos = (ListView) findViewById(R.id.lv_videos);
        tv_title_top = (TextView) findViewById(R.id.tv_title_top);
        tv_left_bg = (TextView) findViewById(R.id.tv_left_bg);
        tv_rigt_bg = (TextView) findViewById(R.id.tv_rigt_bg);
        tv_rigt_bg_chat = (TextView) findViewById(R.id.tv_rigt_bg_chat);
        tvTRight_chat = (ImageView) findViewById(R.id.tvTRight_chat);
        layout_stall = findViewById(R.id.layout_stall);
        layout_buyer_shop = findViewById(R.id.layout_buyer_shop);
        expand_stall = findViewById(R.id.expand_stall);
        layout_stall.setOnClickListener(this);
        mTvHH = (TextView) findViewById(R.id.tv_hh);
        mTvH = (TextView) findViewById(R.id.tv_h);
        mTvMM = (TextView) findViewById(R.id.tv_mm);
        mTvM = (TextView) findViewById(R.id.tv_m);
        mTvSS = (TextView) findViewById(R.id.tv_ss);
        mTvS = (TextView) findViewById(R.id.tv_s);
        mTvF = (TextView) findViewById(R.id.tv_f);
        mTvDay = (TextView) findViewById(R.id.tv_day);
        mTvDayDesc = (TextView) findViewById(R.id.tv_day_desc);
        circle_car_text = (CircleTextView) findViewById(R.id.circle_car_text);
        circle_car_text_chat = (CircleTextView) findViewById(R.id.circle_car_text_chat);
        CircleCarTxtUtl.setColor(circle_car_text_chat);
        mLayoutCreditBages = (FlowLayout) findViewById(R.id.layout_credit_bage);
        mTvEnterWp = (TextView) findViewById(R.id.tv_enter_wp);
        mIvShopLogo = (ImageView) findViewById(R.id.iv_shop_logo);
        mTvDesc = (TextView) findViewById(R.id.tv_item_desc);
        mTvDesc.setOnLongClickListener(new OnLongClickCopyListener());
        iv_coin_pay_icon = (ImageView) findViewById(R.id.iv_coin_pay_icon);
        iv_coin_pay_icon.setOnClickListener(this);
        tvChengTuanTips = (TextView) findViewById(R.id.chengtuan_tips);
        tvChengTuanTipsDetail = (TextView) findViewById(R.id.chengtuan_tips_detail);
        progress = (ProgressBar) findViewById(R.id.chengtuan_progress);

        mTvTitle = (TextView) findViewById(R.id.tv_item_title);
        mTvTitle.setOnLongClickListener(new OnLongClickCopyListener());
        mGvTags = (FlowLayout) findViewById(R.id.gv_tags);
        mTvRetailPrice = (TextView) findViewById(R.id.tv_retail_price);
        tv_pin = (TextView) findViewById(R.id.tv_pin);
        tv_ori_price = (TextView) findViewById(R.id.tv_ori_price);
        tv_ori_price.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        tv_re = (TextView) findViewById(R.id.tv_re);
//        mTvAgentPrice = (TextView) findViewById(R.id.tv_agent_price);
//        mTvAgentPrice.setVisibility(View.INVISIBLE);
        mTvShopName = (TextView) findViewById(R.id.tv_shop_name);
        mTvShare2Wp = (TextView) findViewById(R.id.tv_share_to_wp);
        mTvFavorite = (TextView) findViewById(R.id.tv_favorite);
        mTvViewOrgItem = (TextView) findViewById(R.id.tv_view_org_item);
        mWebView = (WebView) findViewById(R.id.wv_item_detail);
        wv_item_detail_des = (WebView) findViewById(R.id.wv_item_detail_des);
        mBtnApplyAgent = findViewById(R.id.btn_apply_agent);
        mTvSignature = (TextView) findViewById(R.id.tv_signature);
        buttons = (FlowLayout) findViewById(R.id.ll_order_info_btn_parent);
        mPager = (ViewPager) findViewById(R.id.pager);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ScreenUtils.getScreenWidth(this), ScreenUtils.getProportionHeight((ScreenUtils.getScreenWidth(this))));
        mPager.setLayoutParams(params);
        mLayoutIndicator = (FlowIndicator) findViewById(R.id.layout_indicator);
//        try {
//            Field leftEdgeField = mPager.getClass().getDeclaredField("mLeftEdge");
//            Field rightEdgeField = mPager.getClass().getDeclaredField("mRightEdge");
//            if (leftEdgeField != null && rightEdgeField != null) {
//                leftEdgeField.setAccessible(true);
//                rightEdgeField.setAccessible(true);
//                leftEdge = (EdgeEffectCompat) leftEdgeField.get(mPager);
//                rightEdge = (EdgeEffectCompat) rightEdgeField.get(mPager);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        mPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                if (position < urls.size())
                    mLayoutIndicator.setSelectedPos(position);
                if (position == urls.size()) {
                    mPager.setCurrentItem(position - 1);
                    new Handler().post(new Runnable() {
                        @Override
                        public void run() {
                            // ((PagerScrollView) mRootView).scrollTo(0, FunctionHelper.getScreenHeight((Activity) mContext));
                            ((PagerScrollView) mRootView).smoothScrollToSlow(0, FunctionHelper.getScreenHeight((Activity) mContext), 1000);
                        }
                    });
                }
            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                //if (position == mLayoutIndicator.getMaxNum() - 1) {

//                if (rightEdge != null && !rightEdge.isFinished()) {
//                    //最后一个项继续拉的话,下拉到详情页
////                    new Handler().post(new Runnable() {
////                        @Override
////                        public void run() {
////                            // ((PagerScrollView) mRootView).scrollTo(0, FunctionHelper.getScreenHeight((Activity) mContext));
////                            ((PagerScrollView) mRootView).smoothScrollToSlow(0, FunctionHelper.getScreenHeight((Activity) mContext), 1000);
////                        }
////                    });
//
//                }
            }
        });
//        mPagerAdapter = new SlidePicPagerAdapter1(this.getSupportFragmentManager());
//        mPagerAdapter.setPicZoomable(false);
//        mPager.setAdapter(mPagerAdapter);
//        mPagerAdapter.setOnItemClickLitener(new OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//
//                String url = (String) v.getTag();
//                if (url.contains(".mp4")) {
//                    Intent intent = new Intent(ItemDetailsActivity.this, VideoActivity1.class);
//                    intent.putExtra(VideoActivity1.VIEDEO_URL_EXTR, url);
//                    intent.putExtra(VideoActivity1.VIEDEO_ISHOW_DOWN_EXTR, CanDownLoadPicAndVideo);
//                    startActivity(intent);
//
//                } else {
//                    int pos = 0;
//                    if (current_flag) {
//                        if (mPager.getCurrentItem() > 0)
//                            pos = mPager.getCurrentItem() - 1;
//                    } else {
//                        pos = mPager.getCurrentItem();
//                    }
//                    Intent intent = new Intent(mContext, PicGalleryActivity.class);
//                    intent.putExtra(PicGalleryActivity.EXTRA_CUR_POS, pos);
//                    intent.putStringArrayListExtra(PicGalleryActivity.EXTRA_URLS, mBasePicUrls);
//                    startActivity(intent);
//                }
//            }
//        });

        mTvWeixun = (TextView) findViewById(R.id.tv_weixun);
        mTvShare = findViewById(R.id.ll_share);
        mTvShare.setOnClickListener(this);
        mDialog = new LoadingDialog(this);

        //默认是灰色
        findViewById(R.id.btn_buynow).setClickable(false);
        TextView textView = (TextView) findViewById(R.id.btn_buynow_txt);
        textView.setText("正在加载");
        findViewById(R.id.btn_buynow).setBackgroundColor(getResources().getColor(R.color.lightgray));

        TextView textView1 = (TextView) findViewById(R.id.btn_buynow_txt1);
        textView1.setText("正在加载");
        findViewById(R.id.btn_buynow1).setBackgroundColor(getResources().getColor(R.color.lightgray));

        findViewById(R.id.tvTLeft).setOnClickListener(this);
        //tvRight.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.toolbar_cart, 0);
        findViewById(R.id.tl_right).setOnClickListener(this);
        findViewById(R.id.tl_right_chat).setOnClickListener(this);
        layout_shop_cart = findViewById(R.id.layout_shop_cart);
        layout_shop_cart.setOnClickListener(this);
        rlTContent = findViewById(R.id.rlTContent);
        ll_time = (LinearLayout) findViewById(R.id.ll_time);
        initScollview();
        layout_explain = findViewById(R.id.layout_explain);
        layout_related_goods = findViewById(R.id.layout_related_goods);
        useId = SpManager.getUserId(BWApplication.getInstance());
        GuidePreference.init(this);
        layout_explain.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                GuidePreference.getInstance().setSaveShopCartDetail(useId, 1);
                layout_explain.setVisibility(View.GONE);
            }
        });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //rlTContent.setPadding(0, ScreenUtils.getStatusBarHeight(vThis), 0, 0);
            LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ScreenUtils.getStatusBarHeight(vThis));
            this.findViewById(R.id.layout_tittle_status).setLayoutParams(params1);
            this.findViewById(R.id.layout_tittle_status).setVisibility(View.VISIBLE);
//            RelativeLayout.LayoutParams params= new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, ScreenUtils.dip2px(Vthis,50)+ScreenUtils.getStatusBarHeight(Vthis));
//            iv_top_logobg.setLayoutParams(params);
        }
    }

    private void initScollview() {
        mDrawable = new ColorDrawable(Color.argb(255, 252, 62, 57));
        mDrawable.setAlpha(0);
        ll_time.setAlpha(0);
        //tv_title_top.setAlpha(0);
        rlTContent.setBackgroundDrawable(mDrawable);
        ((PagerScrollView) mRootView).setOnScrollChangedListener(new PagerScrollView.OnScrollChangedListener() {
            @Override
            public void onScrollChanged(ScrollView who, int x, int y, int oldx,
                                        int oldy) {
                // TODO Auto-generated method stub
                sc_y = oldy;
                if (y >= rlTContent.getHeight()
                        && y < 2 * rlTContent.getHeight()) {
                    y = y - rlTContent.getHeight();
                } else if (y >= 2 * rlTContent.getHeight()) {
                    y = rlTContent.getHeight();
                } else if (y < rlTContent.getHeight()) {
                    y = 0;
                }
                ll_time.setAlpha(y * 255
                        / rlTContent.getHeight());
//                tv_title_top.setAlpha(y * 255
//                        / rlTContent.getHeight());
                tv_rigt_bg.setAlpha(rlTContent.getHeight() / (y + 1)
                );
                tv_rigt_bg_chat.setAlpha(rlTContent.getHeight() / (y + 1)
                );
                tv_left_bg.setAlpha(rlTContent.getHeight() / (y + 1));
                if (Integer.parseInt(android.os.Build.VERSION.SDK) >= 16) {
                    mDrawable.setAlpha(y * 255 / rlTContent.getHeight());
                }
            }

        });
    }

    private void updateCountDownTime(int hour, int min, int sec, int milli) {
        setHourTv(mTvHH, mTvH, mTvDay, mTvDayDesc, hour);
        setTv(mTvMM, mTvM, min);
        setTv(mTvSS, mTvS, sec);
        mTvF.setText(milli / 100 + "");
    }

    private void setHourTv(TextView tvHH, TextView tvH, TextView tvDay, TextView tvDayDesc, int time) {
        int aa = time / 10;
        if (time < 24) {
            int a = time % 10;
            tvHH.setText(aa + "");
            tvH.setText(a + "");
            tvDay.setVisibility(View.GONE);
            tvDayDesc.setVisibility(View.GONE);
        } else {
            tvDay.setVisibility(View.VISIBLE);
            tvDayDesc.setVisibility(View.VISIBLE);
            int hours = time % 24;
            setTv(tvHH, tvH, hours);
            tvDay.setText((time / 24) + "");
        }

    }

    private void setTv(TextView tvAA, TextView tvA, int time) {
        int aa = time / 10;
        int a = time % 10;
        tvAA.setText(aa + "");
        tvA.setText(a + "");
    }

//    /**
//     * @description 如果是自己的商品，隐藏部分组件
//     * @created 2014-10-24 下午1:54:23
//     * @author ZZB
//     */
//    private void handleSelfWidgets() {
////        if (mIsSelf) {
////            mBtnApplyAgent.setVisibility(View.GONE);
////            mTvWeixun.setVisibility(View.GONE);
////            mTvShare.setVisibility(View.VISIBLE);
////        } else {
////            mTvWeixun.setVisibility(View.VISIBLE);
////            mTvShare.setVisibility(View.GONE);
////        }
//    }

//    private void initTitleBar() {
//        setTitle("商品详情");
//        setRightIcon(R.drawable.toolbar_cart);
//        showRight(true);
////        setSearchIcon(R.drawable.share);
////        showSearch(true);
//
//
//    }

    @Override
    public void onResume() {
        super.onResume();
        ChatUtl.setChatBroad(vThis);
//        CircleTextView ctv=getCircleTextView();
//        if (circle_car_text != null) {
//            new LoadGoodsTask(ItemDetailsActivity.this, circle_car_text).execute();
//        }
        if (circle_car_text2 != null)
            new DetailLoadGoodsTask(ItemDetailsActivity.this, circle_car_text2).execute();
//        try {
//            if (isOnPause) {
//                if (mWebView != null) {
//                    mWebView.getClass().getMethod("onResume").invoke(mWebView, (Object[]) null);
//                }
//                isOnPause = false;
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    class DetailLoadGoodsTask extends AsyncTask<Void, Void, String> {
        private Context mContext;
        private CircleTextView carCountTv;
        private TextView red;

        public DetailLoadGoodsTask(Context mContext, CircleTextView carCountTv) {
            this.mContext = mContext;
            this.carCountTv = carCountTv;
        }

        public DetailLoadGoodsTask(Context mContext, TextView red) {
            this.mContext = mContext;
            this.red = red;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... params) {
            String result = "";

            try {
                result = GoodsCountApi.getInstance().getCarGoodsCount(mContext);
            } catch (Exception e) {
                result = "数量获取失败";
                e.printStackTrace();
            }

            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (!result.startsWith("数量获取失败")) {
                int count = Integer.parseInt(result);
                if (count > 0) {
                    if (carCountTv != null) {
                        carCountTv.setText(count + "");
                        carCountTv.setVisibility(View.VISIBLE);
                    }
                    if (red != null) {
                        red.setText(count + "");
                        red.setVisibility(View.VISIBLE);
                    }
                } else {
                    if (carCountTv != null) {
                        carCountTv.setVisibility(View.GONE);
                    }
                    if (red != null) {
                        red.setVisibility(View.GONE);
                    }
                }
                if (tv_btn != null) {
                    RelativeLayout.LayoutParams rp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                    rp.addRule(RelativeLayout.CENTER_IN_PARENT);
                    tv_btn.setLayoutParams(rp);
                }

            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
//        try {
//            if (mWebView != null) {
//                mWebView.getClass().getMethod("onPause").invoke(mWebView, (Object[]) null);
//                isOnPause = true;
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mEventBus != null) {
            if (mEventBus.isRegistered(this))//加上判断
                mEventBus.unregister(this);
        }
//        if (mWebView != null) {
//            //mWebView.getSettings().setBuiltInZoomControls(true);
//            mWebView=null;
//        }
        //isOnPause = false;
    }

    private void updateShare2WP(int itemCopyType) {
        switch (itemCopyType) {
            case Const.ShopItemCopyType.SELF_UPLOAD://自己创建的商品
            case Const.ShopItemCopyType.SELF_SHARE://自己转发的商品
            case Const.ShopItemCopyType.SELF_COPY://自己复制的商品
                mTvShare2Wp.setText("修改商品");
                mTvShare2Wp.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.edit_item, 0, 0);
                break;
            case Const.ShopItemCopyType.NOT_SHARE://        未转发商品
            case Const.ShopItemCopyType.ALREADY_COPIED://        已复制的商品
                mTvShare2Wp.setText("上架");
                mTvShare2Wp.setCompoundDrawablesWithIntrinsicBounds(R.drawable.up_item, 0, 0, 0);
                break;
            case Const.ShopItemCopyType.ALREADY_SHARED://        已转发的商品
                mTvShare2Wp.setText("已上架");
                mTvShare2Wp.setCompoundDrawablesWithIntrinsicBounds(R.drawable.up_item, 0, 0, 0);
                break;

        }
    }

//    /**
//     * @description 进入店主微铺
//     * @created 2014-10-27 上午9:39:10
//     * @author ZZB
//     */
//    private void enterWp() {
//        if (mLoadingShopInfo) {
//            ViewHub.showShortToast(mContext, "信息加载中，请稍后再试");
//            return;
//        }
//        // 主页面选择微铺菜单
//        if (mShopInfo.getUserId() == SpManager.getUserId(mContext)) {
//            // Intent serviceIntent = new Intent();
//            // serviceIntent.setAction(MainActivity.SELECT_MY_ITEM);
//            // sendBroadcast(serviceIntent);
//            // finish();
//
//            Intent detail = new Intent(mContext, ItemPreviewActivity.class);
//            detail.putExtra("user_id", mShopInfo.getUserId() + "");
//            detail.putExtra("name", mShopInfo.getShopName());
//            mContext.startActivity(detail);
//        } else {
////            Intent intent = new Intent(mContext, ShopItemsActivity.class);
////            intent.putExtra("Userid", mShopInfo.getUserId() + "");
////            intent.putExtra("Username", mShopInfo.getShopName());
////            mContext.startActivity(intent);
//        }
//
//    }

    private void onShopInfoLoaded() {
        mLoadingShopInfo = false;
        // handleSelfWidgets();

        ViewHub.drawItemDetailCreditBage(mContext, mLayoutCreditBages, "担保交易");
        if (mShopInfo.getShopCreditItem().isJoinSevenDaysDelivery()) {
            findViewById(R.id.layout_7days_credit).setVisibility(View.VISIBLE);
            ViewHub.drawItemDetailCreditBage(mContext, mLayoutCreditBages, "7天包退");
        }
        LinearLayout layoutCredit = (LinearLayout) findViewById(R.id.layout_credit);
        mItemShopCategories = mShopInfo.getShopCats();
        ViewHub.drawSellerCreditLevel(mContext, layoutCredit, mShopInfo.getShopCredit().getId());
        mTvSignature.setText(mShopInfo.getSignature());
        mTvShopName.setText(mShopInfo.getShopName());
        mTvEnterWp.setText("进入天天拼货团");

        String logoUrl = Const.getShopLogo(mShopInfo.getUserId());
        Picasso.with(mContext).load(logoUrl).placeholder(R.drawable.shop_logo_normal1).into(mIvShopLogo);

        // 如果已经是代理，隐藏申请代理
        if (mShopInfo.getCurrentUserApplyStatuID() != 0) {
            mBtnApplyAgent.setVisibility(View.GONE);
        } else if (mShopInfo.getUserId() != SpManager.getUserId(mContext)) {
            mBtnApplyAgent.setVisibility(View.VISIBLE);
        }
        // boolean isAgentUser = mUserInfo == null ? mUserInfo.isAgentUser() :
        // false;
        // mBtnApplyAgent.setVisibility(isAgentUser ? View.GONE
        // : View.VISIBLE);// || mIsSelfUpload

//        mTvAgentPrice.setVisibility(View.INVISIBLE);
//        if (mShopInfo.getCurrentUserApplyStatuID() == 3 || mIsSelf) {
//            mTvAgentPrice.setVisibility(View.VISIBLE);
//        }
    }

    /**
     * @description 团批数据加载完成
     * @author pj
     */
    private void onTuanPiDataLoaded() {
        progress.setMax(chengtuanCount);
        progress.setProgress(dealCount);
        tvChengTuanTips.setText(TuanPiUtil.getChengTuanTipsx(tuanpiOver, chengtuanCount, dealCount, totalSaleCount));
        tvChengTuanTipsDetail.setText(TuanPiUtil.getChengTuanDetailTips(tuanpiOver, chengtuanCount, dealCount));


        if (tuanpiOver) {

            if (chengtuanCount >= dealCount) {
                progress.setProgressDrawable(mContext.getResources().getDrawable(R.drawable.style_progressbar_nostatus));
            } else {
                progress.setProgressDrawable(mContext.getResources().getDrawable(R.drawable.style_progressbar));
            }
            int invCount = 0;
            List<ProductModel> modelList = mShopItemModel.getProducts();
            if (modelList != null) {
                for (ProductModel pm : modelList) {
                    invCount += pm.getStock();
                }
            }
            if (invCount == 0) {
                findViewById(R.id.btn_buynow).setClickable(false);
                TextView textView1 = (TextView) findViewById(R.id.btn_buynow_txt);
                textView1.setText("已抢完");
                findViewById(R.id.btn_buynow).setBackgroundColor(getResources().getColor(R.color.lightgray));

                TextView textView11 = (TextView) findViewById(R.id.btn_buynow_txt1);
                textView11.setText("已抢完");
                findViewById(R.id.btn_buynow1).setBackgroundColor(getResources().getColor(R.color.lightgray));

            } else {
                findViewById(R.id.btn_buynow).setClickable(true);
                TextView textView = (TextView) findViewById(R.id.btn_buynow_txt);
                textView.setText("发起补拼");

                TextView textView1 = (TextView) findViewById(R.id.btn_buynow_txt1);
                textView1.setText("发起补拼");
//            mTvShare.setText("我要补货");
//            mTvShare.setCompoundDrawables(null, null, null, null);
//            mTvShare.setVisibility(View.GONE);
//            buttons.setVisibility(View.VISIBLE);
                mTvShare.setVisibility(View.VISIBLE);
                //  mTvShare.setText("约店主一起拼");
//            findViewById(R.id.btn_buynow).setBackgroundColor(getResources().getColor(R.color.lightgray));
                findViewById(R.id.btn_buynow).setBackgroundResource(R.drawable.btn_red);
                findViewById(R.id.btn_buynow1).setBackgroundResource(R.drawable.btn_red);
            }

//            if (mShopItem.getButtons() != null && mShopItem.getButtons().size() > 0) {
//                for (OrderButton b : mShopItem.getButtons()) {
//                    if (b.getAction().equals("补货")) {
//                        buttons.setVisibility(View.VISIBLE);
//                    } else {
//                        buttons.setVisibility(View.GONE);
//                    }
//                }
//            }

        } else {
            //判断无库存时，灰掉购买按钮
            int invCount = 0;
            for (ProductModel pm : mShopItemModel.getProducts()) {
                invCount += pm.getStock();
            }
            if (invCount == 0) {
                findViewById(R.id.btn_buynow).setClickable(false);
                TextView textView1 = (TextView) findViewById(R.id.btn_buynow_txt);
                textView1.setText("已抢完");
                findViewById(R.id.btn_buynow).setBackgroundColor(getResources().getColor(R.color.lightgray));

                TextView textView11 = (TextView) findViewById(R.id.btn_buynow_txt1);
                textView11.setText("已抢完");
                findViewById(R.id.btn_buynow1).setBackgroundColor(getResources().getColor(R.color.lightgray));
            } else {
                findViewById(R.id.btn_buynow).setClickable(true);
                TextView textView = (TextView) findViewById(R.id.btn_buynow_txt);
                findViewById(R.id.btn_buynow).setBackgroundResource(R.drawable.btn_red);

                TextView textView1 = (TextView) findViewById(R.id.btn_buynow_txt1);
                findViewById(R.id.btn_buynow1).setBackgroundResource(R.drawable.btn_red);

                if (mShopItemModel.getDisplayStatu().equals("新款") ||
                        mShopItemModel.getDisplayStatu().equals("补货中")) {
                    textView.setText("我要拼单");
                    textView1.setText("我要拼单");
                } else {
                    textView.setText("发起补拼");
                    textView1.setText("发起补拼");
                }
            }
            mTvShare.setVisibility(View.VISIBLE);
            //mTvShare.setText("约店主一起拼");
            buttons.setVisibility(View.GONE);
            progress.setProgressDrawable(mContext.getResources().getDrawable(R.drawable.style_progressbar));
        }

        if (mShopItem.getItemStatu().equals("已下架")) {
            findViewById(R.id.btn_buynow).setClickable(false);
            TextView textView1 = (TextView) findViewById(R.id.btn_buynow_txt);
            textView1.setText("已下架");
            findViewById(R.id.btn_buynow).setBackgroundColor(getResources().getColor(R.color.lightgray));
            TextView textView11 = (TextView) findViewById(R.id.btn_buynow_txt1);
            textView11.setText("已下架");
            findViewById(R.id.btn_buynow1).setBackgroundColor(getResources().getColor(R.color.lightgray));
        }
    }

    public static void removeCommentRedBall(Context context, View v, String action) {
        if (Const.OrderAction.LEAVE_MSG.equals(action)) {
            Button btn = (Button) v;
            btn.setCompoundDrawables(null, null, null, null);
        }
    }

    private OnClickListener ol = new OnClickListener() {
        @Override
        public void onClick(View v) {
            String action = (String) v.getTag();
            ItemDetailsActivity.removeCommentRedBall(v.getContext(), v, action);
            do {
                if (Const.OrderAction.BUHUO.equals(action)) {// 补货
                    String msg = mShopItem.getReplenishmentRemark();
                    ViewHub.showTextDialog(mContext, "", msg,
                            "确认", "取消",
                            new ViewHub.EditDialogListener() {
                                @Override
                                public void onOkClick(DialogInterface dialog, EditText editText) {

                                }

                                @Override
                                public void onOkClick(EditText editText) {
                                    new Task(Step.SaveReplenishmentRecord).execute();
                                }

                                @Override
                                public void onNegativecClick() {

                                }
                            });
                    break;
                } else {
                    wxShare();
                }


            } while (false);
        }
    };


    public void addOrderDetailButton(FlowLayout parent, List<OrderButton> buttons, OnClickListener l) {
        parent.removeAllViews();
        if (buttons != null) {
            int margin = parent.getResources().getDimensionPixelSize(R.dimen.activity_margin);
            FlowLayout.LayoutParams params = new FlowLayout.LayoutParams(margin, margin);
            for (OrderButton model : buttons) {
                if (!model.isEnable())
                    continue;
                Button child = new Button(parent.getContext());
                child.setEllipsize(TextUtils.TruncateAt.END);
                child.setSingleLine(true);
                child.setText(model.getTitle());
                child.setGravity(Gravity.CENTER_VERTICAL);
                highlightButton(child, model.isPoint());
                if (model.getType().equals("button")) {
                    child.setTag(model.getAction());
                    child.setOnClickListener(l);
                } else {
                    child.setClickable(false);
                    child.setEnabled(false);
                }
                parent.addView(child, params);
            }
        }
    }

    private static void highlightButton(Button btn, boolean highlight) {
        btn.setBackgroundResource(highlight ? R.drawable.btn_blue : R.drawable.bg_rect_gray_corner);
        btn.setTextColor(highlight ? btn.getResources().getColor(R.color.white) : btn.getResources().getColor(
                R.color.lightblack));
    }


    /**
     * @description 数据加载完成
     * @created 2014-10-23 下午2:41:15
     * @author ZZB
     */
    List<String> vList = new ArrayList<>();
    List<String> result = new ArrayList<String>();
    int width = ScreenUtils.px2dip(BWApplication.getInstance(), 25), height = ScreenUtils.px2dip(BWApplication.getInstance(), 25);

    private void formatTitle(Context context, TextView tvTittle, String name, String discount) {
        SpannableString spannableString = new SpannableString(" " + name);
        RectangleTextSpan topSpan = new RectangleTextSpan(context, R.color.pin_huo_red, R.color.pin_huo_red, discount);
        spannableString.setSpan(topSpan, 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvTittle.setText(spannableString);
    }

    private void onShopItemLoaded(ShopItemModel item) {

        try {
            mShopItemModel = item;
            mIsSelf = item.getUserId() == SpManager.getUserId(mContext);
            if (mShopItemModel != null) {
                progress.setVisibility(View.VISIBLE);
                tv_buyer_shop_name.setText(mShopItemModel.getBuyerShopName());
                if (mShopItem.getBuyerShopID() > 0) {
                    layout_buyer_shop.setVisibility(View.VISIBLE);
                } else {
                    layout_buyer_shop.setVisibility(View.GONE);
                }
                String shopLogo = Const.getShopLogo(mShopItemModel.getBuyerShopID());
                GlideUtls.glideCirclePic(vThis, shopLogo, iv_buyer_shop);
                tv_re.setText(mShopItemModel.getRelatedGoodsText());
                if (mShopItemModel.isIsShowShareBtn()) {
                    mTvShare.setVisibility(View.VISIBLE);
                } else {
                    mTvShare.setVisibility(View.INVISIBLE);
                }
                layout_bottom_new.removeAllViews();
                final Drawable nav_nomal = getResources().getDrawable(R.drawable.icon_detail_shop);
                nav_nomal.setBounds(0, 0, width, height);
                final Drawable snav_nomal = getResources().getDrawable(R.drawable.icon_detail_share);
                snav_nomal.setBounds(0, 0, width, height);
                final Drawable cnav_nomal = getResources().getDrawable(R.drawable.icon_detail_focusno);
                cnav_nomal.setBounds(0, 0, width, height);
                final Drawable mnav_nomal = getResources().getDrawable(R.drawable.icon_detail_market);
                mnav_nomal.setBounds(0, 0, width, height);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1);
                RelativeLayout.LayoutParams rp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                rp.addRule(RelativeLayout.CENTER_IN_PARENT);
                RelativeLayout.LayoutParams lineRp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, 1);
                // rp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                LinearLayout.LayoutParams bigParams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, (float) 3);
//                if (ListUtils.isEmpty(mShopItemModel.getButtomSmallButtons()) && ListUtils.isEmpty(mShopItemModel.getButtomBigButtons())) {
//                    layout_bottom_new.setVisibility(View.GONE);
//                } else {
//                    layout_bottom_new.setVisibility(View.VISIBLE);
//                }
                if (!ListUtils.isEmpty(mShopItemModel.getButtomSmallButtons())) {
                    for (OrderButton button : mShopItemModel.getButtomSmallButtons()) {
                        RelativeLayout relativeLayout = new RelativeLayout(this);
                        relativeLayout.setLayoutParams(params);
                        if (button != null) {
                            TextView line = new TextView(this);
                            line.setLayoutParams(lineRp);
                            line.setBackgroundColor(getResources().getColor(R.color.line_gray));
                            TextView tv = new TextView(this);
                            tv.setLayoutParams(rp);
                            tv.setText(button.getTitle());
                            tv.setGravity(Gravity.CENTER);
                            tv.setTextSize(12);
                            relativeLayout.addView(line);
                            relativeLayout.addView(tv);
                            if (button.getAction().equals(Const.OrderAction.ACTION_NAHUO)) {
                                tv.setCompoundDrawables(null, nav_nomal, null, null);
                                tv.setId(R.id.tv_shop_cart_bot);
                                TextView circleTextView = new TextView(this);
                                circleTextView.setTextSize(11);
                                int pad = 3;
                                circleTextView.setPadding(pad, pad, pad, pad);
                                circleTextView.setGravity(Gravity.CENTER);
                                circleTextView.setTextColor(getResources().getColor(R.color.white));
                                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                                layoutParams.addRule(RelativeLayout.RIGHT_OF, R.id.tv_shop_cart_bot);
                                layoutParams.leftMargin = (int) getResources().getDimension(R.dimen.cirview_margin);
                                circleTextView.setBackgroundResource(R.drawable.re_circle2);
                                circleTextView.setLayoutParams(layoutParams);
                                circle_car_text2 = circleTextView;
                                tv_btn = tv;
                                circle_car_text2.setVisibility(View.GONE);
                                relativeLayout.addView(circleTextView);
                            } else if (button.getAction().equals(Const.OrderAction.ACTION_SHARE)) {
                                tv.setCompoundDrawables(null, snav_nomal, null, null);
                            } else if (button.getAction().equals(Const.OrderAction.ACTION_COLLECT)) {
                                tv.setCompoundDrawables(null, cnav_nomal, null, null);
                                tv_collect = tv;
                            } else if (button.getAction().equals(Const.OrderAction.ACTION_STALL)) {
                                tv.setCompoundDrawables(null, mnav_nomal, null, null);
                            }

                            relativeLayout.setTag(button);
                            relativeLayout.setOnClickListener(new OrderButtonLister());
                            layout_bottom_new.addView(relativeLayout);
                        }
                    }
                    if (circle_car_text2 != null)
                        new DetailLoadGoodsTask(ItemDetailsActivity.this, circle_car_text2).execute();
                }
                if (!ListUtils.isEmpty(mShopItemModel.getButtomBigButtons())) {
                    for (OrderButton button : mShopItemModel.getButtomBigButtons()) {
                        if (button != null) {
                            TextView tv = new TextView(this);
                            tv.setLayoutParams(bigParams);
                            tv.setText(button.getTitle());
                            tv.setTextSize(16);
                            tv.setGravity(Gravity.CENTER);
                            if (button.isPoint()) {
                                tv.setBackgroundColor(getResources().getColor(R.color.base_red));
                                tv.setTextColor(getResources().getColor(R.color.white));
                            } else {
                                tv.setBackgroundColor(getResources().getColor(R.color.lightgray));
                                tv.setTextColor(getResources().getColor(R.color.white));
                            }
                            tv.setTag(button);
                            tv.setOnClickListener(new OrderButtonLister());
                            layout_bottom_new.addView(tv);
                        }
                    }
                }

            }
            if (mShopItem != null) {
                StallID = mShopItem.getStallID();
                if (StallID > 0) {
                    layout_stall.setVisibility(View.VISIBLE);
                    expand_stall.setVisibility(View.VISIBLE);
                } else {
                    layout_stall.setVisibility(View.GONE);
                    expand_stall.setVisibility(View.GONE);
                }
            }
            //  int itemCopyType = item.getItemCopyType();
            //handleViewOrgItem(item);
            //   updateShare2WP(itemCopyType);
            initColorSizeMap(item.getProducts());

            if (item.getTags() != null) {
                mGvTags.setVisibility(View.VISIBLE);
                int padding = DisplayUtil.dip2px(this, 8);
                int paddingTB = DisplayUtil.dip2px(this, 2);

                FlowLayout.LayoutParams params2 = new FlowLayout.LayoutParams(padding, padding);
                for (ShopItemModel.ItemTagModel tagModel : item.getTags()) {
                    TextView textView = new TextView(this);
                    textView.setSingleLine(true);
                    textView.setTextColor(getResources().getColor(R.color.pin_huo_list_item_price_txt));
                    textView.setText(tagModel.getName());
                    textView.setGravity(Gravity.CENTER);
                    textView.setPadding(padding, paddingTB, padding, paddingTB);
                    textView.setTextSize(12);
                    textView.setBackgroundResource(R.drawable.bg_rect_item_red_corner);
                    mGvTags.addView(textView, params2);
                }
            } else {
                mGvTags.setVisibility(View.GONE);
            }

            mTvDesc.setText(ShopItemListModel.getTextHtml(item.getIntroOrName(), mContext, imageGetter));
            mTvDesc.setTag(item.getIntroOrName());
            if (TextUtils.isEmpty(item.getDiscount())) {
                mTvTitle.setText(ShopItemListModel.getTextHtml(item.getName(), mContext, imageGetter));
            } else {
                formatTitle(mContext, mTvTitle, item.getName(), item.getDiscount());
            }
            mTvTitle.setTag(item.getName());

            FunctionHelper.formatAgentPrice(vThis, mTvRetailPrice, item.getPrice());
            tv_pin.setVisibility(View.VISIBLE);
            if (!TextUtils.isEmpty(item.getDiscount())) {
                FunctionHelper.formatRightPrice(vThis, tv_ori_price, item.getOriPrice());
            }
            //属性 if (item.getPropertys() != null)
            if (false) {
                List<ShopItemModel.PropertysBeanX> parePropertys = item.getPropertys();
                if (parePropertys != null && parePropertys.size() > 0) {
                    ll_propertys_tags.setVisibility(View.VISIBLE);
                    for (ShopItemModel.PropertysBeanX beanX : parePropertys) {
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                        LinearLayout pLayout = new LinearLayout(mContext);
                        pLayout.setPadding(0, 10, 0, 10);
                        pLayout.setOrientation(LinearLayout.HORIZONTAL);
                        int sel_id = beanX.getSelectedID();
                        String first_name = beanX.getName();
                        TextView tv = setLeftTextview(first_name);
                        pLayout.addView(tv);
                        if (beanX.getPropertys() != null && beanX.getPropertys().size() > 0) {
                            if (beanX.getPropertys().size() > 3) {
                                if (beanX.getPropertys().subList(0, 3).contains(sel_id)) {
                                    for (int i = 0; i < 3; i++) {
                                        TextView textView = null;
                                        if (beanX.getPropertys().get(i).getID() == sel_id) {
                                            textView = setRightTextview(beanX.getPropertys().get(i).getName(), true);
                                        } else {
                                            textView = setRightTextview(beanX.getPropertys().get(i).getName(), false);
                                        }
                                        if (textView != null)
                                            pLayout.addView(textView);
                                    }
                                } else {
                                    int index = 0;
                                    for (int i = 0; i < beanX.getPropertys().size(); i++) {
                                        if (beanX.getPropertys().get(i).getID() == sel_id) {
                                            index = i;
                                        }
                                    }
                                    List<ShopItemModel.PropertysBeanX.PropertysBean> list = null;
                                    if (index < beanX.getPropertys().size() - 1) {
                                        list = beanX.getPropertys().subList(index - 1, index + 2);
                                    } else {
                                        list = beanX.getPropertys().subList(index - 2, index + 1);
                                    }
                                    for (ShopItemModel.PropertysBeanX.PropertysBean bean : list) {
                                        TextView textView = null;
                                        if (bean.getID() == sel_id) {
                                            textView = setRightTextview(bean.getName(), true);
                                        } else {
                                            textView = setRightTextview(bean.getName(), false);
                                        }
                                        if (textView != null)
                                            pLayout.addView(textView);
                                    }
                                }
                            } else {

                                for (ShopItemModel.PropertysBeanX.PropertysBean bean : beanX.getPropertys()) {
                                    TextView textView = null;
                                    if (bean.getID() == sel_id) {
                                        textView = setRightTextview(bean.getName(), true);
                                    } else {
                                        textView = setRightTextview(bean.getName(), false);
                                    }
                                    if (textView != null)
                                        pLayout.addView(textView);
                                }
                                if (beanX.getPropertys().size() == 2) {
                                    pLayout.addView(setEmptyView());
                                }
                                if (beanX.getPropertys().size() == 1) {
                                    pLayout.addView(setEmptyView());
                                    pLayout.addView(setEmptyView());
                                }
                            }
                        }

                        ll_propertys_tags.addView(pLayout);

                    }
                }
            }

            //   formatServicePrice(item.getRetailPrice());
            //商品详情
            //populateWebView(item.getDescriptionFull());
            String viewosString = "";
            String firtVideo = "";
            if (item.getVideos() != null) {
                if (item.getVideos().size() > 0) {
                    current_flag = true;
                    vList.clear();
                    result.clear();
                    lv_videos.setVisibility(View.VISIBLE);
                    for (String s : item.getVideos()) {
                        if (Collections.frequency(result, s) < 1)
                            result.add(s);
                    }
                    if (result.size() > 4) {
                        for (int i = 0; i < 4; i++) {
                            vList.add(result.get(i));
                        }
                    } else {
                        vList.addAll(result);
                    }
                    firtVideo = vList.get(0);
                    adapter = new VideoAdapter(this, vList, lv_videos);
                    lv_videos.setAdapter(adapter);
                    setListViewHeightBasedOnChildren(lv_videos);
                    ((PagerScrollView) mRootView).post(new Runnable() {

                        @Override
                        public void run() {
                            // TODO Auto-generated method stub
                            ((PagerScrollView) mRootView).scrollTo(0, 0);
                        }
                    });
                    lv_videos.setOnItemClickListener(new OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            // Toast.makeText(ItemDetailsActivity.this, i + "", Toast.LENGTH_SHORT).show();
                            String url = vList.get(i);
                            Intent intent = new Intent(ItemDetailsActivity.this, VideoActivity1.class);
                            intent.putExtra(VideoActivity1.VIEDEO_URL_EXTR, url);
                            intent.putExtra(VideoActivity1.VIEDEO_ISHOW_DOWN_EXTR, CanDownLoadPicAndVideo);
                            startActivity(intent);
                        }
                    });
                } else {
                    lv_videos.setVisibility(View.GONE);
                    current_flag = false;
                }


                //            StringBuilder sb = new StringBuilder();
                //            for (int i = 0; i < vList.size(); i++) {
                //
                //                if (i == 0) {
                //                    firtVideo = vList.get(i);
                //                }
                //                sb.append("<video width=\"100%\" height=\"211\" controls=\"controls\" preload=\"preload\"style=\"background-color: black;\"  > <source src=\" ");
                //                sb.append(vList.get(i));
                //                sb.append("\"type=\"video/mp4\"></video>");
                //                sb.append("<div style=\"height:20px;\"></div>");
                //            }
                //            viewosString = sb.toString();
            } else {
                lv_videos.setVisibility(View.GONE);
                current_flag = false;
            }
//        final List<String> sList = new ArrayList<>();
//        sList.add("http://nahuo-img-server.b0.upaiyun.com//33306/item/1493014819.mp4");
//        sList.add("http://nahuo-img-server.b0.upaiyun.com//33306/item/1493014819.mp4");
//        sList.add("http://nahuo-img-server.b0.upaiyun.com//33306/item/1493018386.mp4");
//        sList.add("http://nahuo-img-server.b0.upaiyun.com//33306/item/1493014819.mp4");
            String Description = "";
            if (item.getImages() != null) {
                if (item.getImages().length > 0) {
                    StringBuilder imges = new StringBuilder();
                    for (int i = 0; i < item.getImages().length; i++) {
                        if (!TextUtils.isEmpty(item.getImages()[i])) {
                            if (i == 0) {
                                SpManager.setVIDEO_SHOP_FIRST_ICON(this, ImageUrlExtends.getImageUrl(item.getImages()[i], 21));
                            }
                            imges.append("<img src=\"" + ImageUrlExtends.getImageUrl(item.getImages()[i], 21) + "\"/><br/>");
                            if (i < item.getImages().length - 1)
                                imges.append("<div style=\"height:5px;\"></div>");
                        }

                    }
                    Description = imges.toString();
                } else {
                    SpManager.setVIDEO_SHOP_FIRST_ICON(this, "");
                }
            } else {
                SpManager.setVIDEO_SHOP_FIRST_ICON(this, "");
            }

            populateWebView(item.getDescriptionHead(), Description, item.getDescriptionFoot(), viewosString);
         //   int url_index = 0;
            if (item.getHeadImages() != null && item.getHeadImages().length > 0) {
                View view = LayoutInflater.from(vThis).inflate(R.layout.detail_item, null, false);
                urls.clear();
                if (!TextUtils.isEmpty(firtVideo)) {
                    urls.add(firtVideo);
                    views.add(view);
                }
                for (String url : item.getHeadImages()) {
                    Log.i("ItemDetail", "url:" + url);
                    if (!TextUtils.isEmpty(url)) {
                        urls.add(ImageUrlExtends.getImageUrl(url, 21));
                        mBasePicUrls.add(ImageUrlExtends.getImageUrl(url, 21));
                        views.add(view);
                      //  url_index++;
//                        if (url_index > 4) {
//                            break;
//                        }// 只读取前五张
                    }
                }
                imageAdapter = new ImageAdapter(views, this, urls);
                imageAdapter.setOnClickListener(new ImageAdapter.ImageOnClickListener() {
                    @Override
                    public void onClick(String ss) {
                        String url = ss;
                        if (url.contains(".mp4")) {
                            Intent intent = new Intent(ItemDetailsActivity.this, VideoActivity1.class);
                            intent.putExtra(VideoActivity1.VIEDEO_URL_EXTR, url);
                            intent.putExtra(VideoActivity1.VIEDEO_ISHOW_DOWN_EXTR, CanDownLoadPicAndVideo);
                            startActivity(intent);

                        } else {
                            int pos = 0;
                            if (current_flag) {
                                if (mPager.getCurrentItem() > 0)
                                    pos = mPager.getCurrentItem() - 1;
                            } else {
                                pos = mPager.getCurrentItem();
                            }
                            Intent intent = new Intent(mContext, PicGalleryActivity.class);
                            intent.putExtra(PicGalleryActivity.EXTRA_CUR_POS, pos);
                            intent.putStringArrayListExtra(PicGalleryActivity.EXTRA_URLS, mBasePicUrls);
                            startActivity(intent);
                        }
                    }
                });
                View footerView = LayoutInflater.from(vThis).inflate(R.layout.right_refesh_head, null, false);
                mPager.setAdapter(imageAdapter);
                imageAdapter.addFooterView(footerView);
                if (urls.size() > 0) {
//                    mPagerAdapter.setData(urls);
//                    mPagerAdapter.notifyDataSetChanged();
                    mLayoutIndicator.setMaxNum(urls.size());

                }
            }
            if (item != null) {
                if (ListUtils.isEmpty(item.getRelatedGoods())) {
                    layout_related_goods.setVisibility(View.GONE);
                } else {
                    //设置水平横向滑动的参数
                    int size = item.getRelatedGoods().size();
                    int length = 100;
                    DisplayMetrics dm = new DisplayMetrics();
                    this.getWindowManager().getDefaultDisplay().getMetrics(dm);
                    float density = dm.density;
                    int gridviewWidth = (int) (size * (length + 4) * density);
                    int itemWidth = (int) (length * density);

                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(gridviewWidth, LinearLayout.LayoutParams.FILL_PARENT);
                    lv_related_goods.setLayoutParams(params); //设置GirdView布局参数,横向布局的关键
                    lv_related_goods.setColumnWidth(itemWidth);
                    lv_related_goods.setHorizontalSpacing(15);
                    lv_related_goods.setStretchMode(GridView.NO_STRETCH);
                    lv_related_goods.setNumColumns(size);
                    layout_related_goods.setVisibility(View.VISIBLE);
                    relatedGoodsAdapter.setData(item.getRelatedGoods());
                    relatedGoodsAdapter.notifyDataSetChanged();
                }
            }
//        addOrderDetailButton(buttons, mShopItem.getButtons(), ol);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class OrderButtonLister implements OnClickListener {

        @Override
        public void onClick(final View v) {
            OrderButton button = (OrderButton) v.getTag();
            if (button != null) {
                switch (button.getAction()) {
                    case Const.OrderAction.ACTION_BUDAN:
                        if (!SpManager.getIs_Login(mContext)) {
                            Utils.gotoLoginActivity(mContext);
                            return;
                        }
                        String msg = mShopItem.getReplenishmentRemark();
                        if (TextUtils.isEmpty(msg)) {
                            buy(v);
                        } else {
                            BuPinDomDialog dialog = new BuPinDomDialog(vThis);
                            dialog.setTitle("补货须知！").setMessage(msg).setPositive("知道了", new BuPinDomDialog.PopDialogListener() {
                                @Override
                                public void onPopDialogButtonClick(int which) {
                                    buy(v);
                                }
                            }).show();
                        }
                        break;
                    case Const.OrderAction.ACTION_PINDAN:
                        if (SpManager.getIs_Login(mContext)) {
                            buy(v);
                        } else {
                            Utils.gotoLoginActivity(mContext);
                        }
                        break;
                    case Const.OrderAction.ACTION_XIAJIA:
                        break;
                    case Const.OrderAction.ACTION_COLLECT:
                        if (SpManager.getIs_Login(mContext)) {
                            favoriteItem();
                        } else {
                            Utils.gotoLoginActivity(mContext);
                        }
                        break;
                    case Const.OrderAction.ACTION_NAHUO:
//                        Intent it = new Intent(vThis, ShopCartNewActivity.class);
//                        it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                        vThis.startActivity(it);
                        Utils.gotoShopcart(getApplicationContext());
                        break;
                    case Const.OrderAction.ACTION_STALL:
                        Intent sIntent = new Intent(vThis, StallReasonListActivity.class);
                        sIntent.putExtra(StallReasonListActivity.EXTRA_STALL_RID, StallID);
                        startActivity(sIntent);
                        break;
                    case Const.OrderAction.ACTION_SHARE:
                        Share();
                        break;
                }
            }
        }
    }

    private TextView setLeftTextview(String name) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(DisplayUtil.dip2px(mContext, 80), LinearLayout.LayoutParams.MATCH_PARENT);
        TextView tv = new TextView(mContext);
        if (!TextUtils.isEmpty(name))
            tv.setText(name + "：");
        tv.setGravity(Gravity.CENTER);
        tv.setTextSize(15);
        //tv.setPadding(30, 20, 30, 20);
        tv.setLayoutParams(params);
        tv.setTextColor(Color.parseColor("#717171"));
        return tv;
    }

    private View setEmptyView() {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1);
        params.leftMargin = 10;
        params.rightMargin = 10;
        TextView tv = new TextView(mContext);
        tv.setLayoutParams(params);
        return tv;
    }

    private TextView setRightTextview(String name, boolean is_sel) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1);
        params.leftMargin = 10;
        params.rightMargin = 10;
        TextView tv = new TextView(mContext);
        if (!TextUtils.isEmpty(name))
            tv.setText(name);
        tv.setGravity(Gravity.CENTER);
        tv.setTextSize(14);
        //tv.setPadding(30, 20, 30, 20);
        tv.setLayoutParams(params);
        if (is_sel) {
            tv.setTextColor(Color.parseColor("#ffffff"));
            tv.setBackgroundColor(Color.parseColor("#e48e8f"));
        } else {
            tv.setTextColor(Color.parseColor("#979696"));
            tv.setBackgroundColor(Color.parseColor("#b3b1b1"));
        }
        return tv;
    }

    private void setListViewHeightBasedOnChildren(ListView listView) {

        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }


//    /**
//     * @description 控制是否显示源商品
//     * @created 2015-1-29 下午3:11:59
//     * @author ZZB
//     */
//    private void handleViewOrgItem(ShopItemModel item) {
////        int itemCopyType = item.getItemCopyType();
////        switch(itemCopyType){
////            case Const.ShopItemCopyType.SELF_SHARE:
////                mTvViewOrgItem.setTag(item.getParentID());
////                mTvViewOrgItem.setVisibility(View.VISIBLE);
////                break;
////            case Const.ShopItemCopyType.SELF_COPY:
////                mTvViewOrgItem.setVisibility(View.VISIBLE);
////                mTvViewOrgItem.setTag(item.getSourceID());
////                break;
////
////        }
//
//
//    }

    /**
     * 视频全屏参数
     */
    protected static final FrameLayout.LayoutParams COVER_SCREEN_PARAMS = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    private View customView;
    private FrameLayout fullscreenContainer;
    // private WebChromeClient.CustomViewCallback customViewCallback;

    /**
     * @description 商品详情webview
     * @created 2014-10-23 下午6:49:44
     * @author ZZB
     */
    private void populateWebView(String descriptionhead, String description, String descriptionfoot, String sViedos) {
        StringBuilder html = new StringBuilder();
        html.append("<html>"
                + "<head>"
                + "<style type=\"text/css\">"
                + ".wp-item-detail-format img,.wp-item-detail-format table{ max-width:100%;width:100%!important;height:auto!important; }"
                + "*{margin:0px; padding:0px;}" + "</style>" + "</head>" + "<body>"
                + "<div class=wp-item-detail-format>");
        // html.append(descriptionhead);
        //html.append(sViedos);
        html.append(description);
        // html.append("<div style=\"height:5px;\"></div>");
        html.append(descriptionfoot);
        html.append("</div>" + "</body>" + "</html>");
        wv_item_detail_des.loadData("<html>"
                + "<head>"
                + "<style type=\"text/css\">"
                + ".wp-item-detail-format img,.wp-item-detail-format table{ max-width:100%;width:100%!important;height:auto!important; }"
                + "*{margin:0px; padding:0px;}" + "</style>" + "</head>" + "<body>"
                + "<div class=wp-item-detail-format>" + descriptionhead + "</div>" + "</body>" + "</html>", "text/html; charset=UTF-8", null);
        // mWebView.requestFocus();
//        mWebView.setScrollBarStyle(0);

//        WebChromeClient wvcc = new WebChromeClient();
        WebSettings webSettings = mWebView.getSettings();
////        webSettings.setBuiltInZoomControls(true);
////        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
////        webSettings.setUseWideViewPort(true);
////        webSettings.setLoadWithOverviewMode(true);
//        // webSettings.setJavaScriptEnabled(true);
////        webSettings.setUseWideViewPort(true); // 关键点
////        webSettings.setAllowFileAccess(true); // 允许访问文件
////        webSettings.setSupportZoom(true); // 支持缩放
////        webSettings.setLoadWithOverviewMode(true);
//        webSettings.setLoadsImagesAutomatically(true);
//        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
//        webSettings.setPluginState(WebSettings.PluginState.ON);
//        // webSettings.setDefaultFontSize(50);
//        // webSettings.setTextZoom(200);
//        //webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
        webSettings.setDefaultTextEncodingName("utf-8");
//        mWebView.setWebChromeClient(wvcc);
//        mWebView.setFocusable(false);
        WebViewClient wvc = new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if (!mWebView.getSettings().getLoadsImagesAutomatically()) {
                    mWebView.getSettings().setLoadsImagesAutomatically(true);
                }
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                mWebView.loadUrl(url);
                return true;
            }
        };
        mWebView.setWebViewClient(wvc);
//        mWebView.setWebChromeClient(new WebChromeClient() {
//
//            /*** 视频播放相关的方法 **/
//
//            @Override
//            public View getVideoLoadingProgressView() {
//                FrameLayout frameLayout = new FrameLayout(ItemDetailsActivity.this);
//                frameLayout.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
//                return frameLayout;
//            }
//
//            @Override
//            public void onShowCustomView(View view, CustomViewCallback callback) {
//                showCustomView(view, callback);
//
//            }
//
//            @Override
//            public void onHideCustomView() {
//                hideCustomView();
//            }
//        });

        // 加载Web地址
        // mWebView.loadUrl("http://look.appjx.cn/mobile_api.php?mod=news&id=12603");
        mWebView.loadData(html.toString(), "text/html; charset=UTF-8", null);
        //mWebView.setVerticalScrollBarEnabled(false);
    }


//    /**
//     * 视频播放全屏
//     **/
//    private void showCustomView(View view, WebChromeClient.CustomViewCallback callback) {
//        // if a view already exists then immediately terminate the new one
//        if (customView != null) {
//            callback.onCustomViewHidden();
//            return;
//        }
//        ItemDetailsActivity.this.getWindow().getDecorView();
//        FrameLayout decor = (FrameLayout) getWindow().getDecorView();
//        fullscreenContainer = new FullscreenHolder(ItemDetailsActivity.this);
//        fullscreenContainer.addView(view, COVER_SCREEN_PARAMS);
//        decor.addView(fullscreenContainer, COVER_SCREEN_PARAMS);
//        customView = view;
//        setStatusBarVisibility(false);
//        //customViewCallback = callback;
//        //mWebView.setVisibility(View.INVISIBLE);
//    }
//
//    /**
//     * 隐藏视频全屏
//     */
//    private void hideCustomView() {
//        if (customView == null) {
//            return;
//        }
//
//        setStatusBarVisibility(true);
//        FrameLayout decor = (FrameLayout) getWindow().getDecorView();
//        decor.removeView(fullscreenContainer);
//        fullscreenContainer = null;
//        customView = null;
//       // customViewCallback.onCustomViewHidden();
//        mWebView.setVisibility(View.VISIBLE);
//        ((PagerScrollView) mRootView).post(new Runnable() {
//
//            @Override
//            public void run() {
//                // TODO Auto-generated method stub
//                // Toast.makeText(ItemDetailsActivity.this,sc_y+"",Toast.LENGTH_LONG).show();
//                ((PagerScrollView) mRootView).scrollTo(0, sc_y);
//            }
//        });
//    }

    /**
     * 全屏容器界面
     */
    static class FullscreenHolder extends FrameLayout {

        public FullscreenHolder(Context ctx) {
            super(ctx);
            setBackgroundColor(ctx.getResources().getColor(android.R.color.black));
        }

        @Override
        public boolean onTouchEvent(MotionEvent evt) {
            return true;
        }
    }

    private void setStatusBarVisibility(boolean visible) {
        int flag = visible ? 0 : WindowManager.LayoutParams.FLAG_FULLSCREEN;
        getWindow().setFlags(flag, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

//    @Override
//    public boolean onKeyUp(int keyCode, KeyEvent event) {
//        switch (keyCode) {
//            case KeyEvent.KEYCODE_BACK:
//                /** 回退键 事件处理 优先级:视频播放全屏-网页回退-关闭页面 */
//                if (mWebView.canGoBack()) {
//                    mWebView.goBack();
//                } else {
//                    finish();
//                }
//                return true;
//            default:
//                return super.onKeyUp(keyCode, event);
//        }
//    }

//    private void formatServicePrice(double retailPrice) {
////        mTvAgentPrice.setText("服务费  ￥" + df.format(retailPrice * 0.05));
//    }

    /**
     * @description 零售价格式化
     * @created 2014-10-23 下午6:06:40
     * @author ZZB
     */
    private void formatAgentPrice(double retailPrice) {
        String retailPriceStr = "¥" + df.format(retailPrice);
        //int dotPos = retailPriceStr.indexOf(".");
        SpannableString spRetailPrice = new SpannableString(retailPriceStr);
        // 设置字体大小（相对值,单位：像素） 参数表示为默认字体大小的多少倍 //0.5f表示默认字体大小的一半
        spRetailPrice.setSpan(new RelativeSizeSpan(2.0f), 1, retailPriceStr.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        // 设置字体前景色
        spRetailPrice.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.pin_huo_red)), 0,
                retailPriceStr.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        mTvRetailPrice.setText(spRetailPrice);
        tv_pin.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_coin_pay_icon:
                Intent knowIntent = new Intent(vThis, PostDetailActivity.class);
                knowIntent.putExtra(PostDetailActivity.EXTRA_TID, 104444);
                knowIntent.putExtra(PostDetailActivity.EXTRA_POST_TYPE, Const.PostType.TOPIC);
                startActivity(knowIntent);
                break;
            case R.id.tl_right_chat:
                try {
                    if (SpManager.getIs_Login(mContext)) {
                        if (mShopItem != null) {
                            mShopItem.ID = mId;
                            //                ChatHelper.chat(mContext, mShopInfo.getUserId(), mShopInfo.getUserName(), mShopItem,
                            //                        mShopItem.getApplyStatuID());
                            ChatHelper.chat(mContext,
                                    Integer.valueOf(SpManager.getECC_USER_ID(mContext)),
                                    SpManager.getECC_USER_NICK_NAME(mContext), mShopItem,
                                    mShopItem.getApplyStatuID());
                        }
                    } else {
                        Utils.gotoLoginActivity(mContext);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.layout_stall:
                Intent sIntent = new Intent(vThis, StallReasonListActivity.class);
                sIntent.putExtra(StallReasonListActivity.EXTRA_STALL_RID, StallID);
                startActivity(sIntent);
                break;
            case R.id.tl_right:
//                if (mLoadingShopInfo) {
//                    ViewHub.showShortToast(mContext, "数据加载中，请稍候再试");
//                    return;
//                }
                CommonSearchActivity.launch(this, CommonSearchActivity.SearchType.ALL_ITEM_SEARCH);
                // Utils.gotoShopcart(getApplicationContext());
                break;
            case R.id.layout_shop_cart:
                Utils.gotoShopcart(getApplicationContext());
                break;
            case R.id.tvTLeft:
                finish();
                break;
            case R.id.layout_7days_credit:// 查看7天退货
                Intent credit7Intent = new Intent(this, SetPayPswFragment.class);
                credit7Intent.putExtra(SetPayPswFragment.EXTRA_READ_ONLY, true);
                startActivity(credit7Intent);
                break;
            case R.id.layout_credit:// 查看信誉
                Intent creditIntent = new Intent(this, ShopCreditActivity.class);
                creditIntent.putExtra(ShopCreditActivity.EXTRA_USER_ID, mShopInfo.getUserId());
                startActivity(creditIntent);
                break;
            case R.id.tv_view_org_item:// 查看源商品
                Intent intent = new Intent(this, ItemDetailsActivity.class);
                int parentId = (Integer) v.getTag();
                intent.putExtra(ItemDetailsActivity.EXTRA_ID, parentId);
                startActivity(intent);
                break;
            case R.id.tv_shopping_cart:
                break;
            case R.id.btn_apply_agent:// 申请代理
                applyAgent();
                break;
            case R.id.btn_buynow:
                if (SpManager.getIs_Login(mContext)) {
                    goToBuyer(v);
                } else {
                    Utils.gotoLoginActivity(mContext);
                }
                break;
            case R.id.tv_enter_wp:// 进入微铺
            case R.id.iv_shop_logo:
            case R.id.tv_shop_name:
//                enterWp();
                break;
            case R.id.tv_share_to_wp:// 转发到微铺或者修改
                shareToWp();
                break;
            case R.id.tv_favorite:// 收藏、取消收藏商品
            case R.id.iv_collect:// 商品收藏
                if (SpManager.getIs_Login(mContext)) {
                    favoriteItem();
                } else {
                    Utils.gotoLoginActivity(mContext);
                }

                break;
            case R.id.tv_category:// 店铺分类
                if (mLoadingShopInfo) {
                    ViewHub.showShortToast(mContext, "信息加载中，请稍后再试");
                    return;
                }
                String[] items = new String[mItemShopCategories.size()];
                for (int i = 0; i < items.length; i++) {
                    items[i] = mItemShopCategories.get(i).getName();
                }
                BottomMenu bottomMenu = new BottomMenu(this);
//                bottomMenu.setSelectedItemPosition(mCurrentPs);
                bottomMenu.setItems(items).setOnMenuItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        mCurrentPs = position;
                        int userId = mShopInfo.getUserId();
                        ItemShopCategory cat = mItemShopCategories.get(position);
                        if (userId != SpManager.getUserId(mContext)) {
//                            int catId = cat.getId();
//                            Intent intent = new Intent(mContext, ShopItemsActivity.class);
//                            intent.putExtra("Userid", userId + "");
//                            intent.putExtra("Username", mShopInfo.getShopName());
//                            intent.putExtra(ShopItemsActivity.EXTRA_SHOP_CAT_ID, catId);
//                            startActivity(intent);
                        } else {
//                            Intent intent = new Intent(mContext, ItemCategoryDetailActivity.class);
//                            intent.putExtra(ItemCategoryDetailActivity.EXTRA_CATEGORY, cat);
//                            startActivity(intent);
                        }

                    }
                });
                bottomMenu.show((View) v.getParent());
                break;
            case R.id.tv_weixun:
                mShopItem.ID = mId;
//                ChatHelper.chat(mContext, mShopInfo.getUserId(), mShopInfo.getUserName(), mShopItem,
//                        mShopItem.getApplyStatuID());
                ChatHelper.chat(mContext,
                        Integer.valueOf(SpManager.getECC_USER_ID(mContext)),
                        SpManager.getECC_USER_NICK_NAME(mContext), mShopItem,
                        mShopItem.getApplyStatuID());
                break;
            case R.id.ll_share:
//                if (mTvShare.getText().toString().equals("我要补货")) {
//                    String content = "点击确认后您的补货需求将提交到买手，如商品有货将直接上架到<店主补货专场>。每天15点后的补货将上架到第二天的补货专场。如有疑问，可联系专属客服！";
//                    ViewHub.showTextDialog(mContext, "", content,
//                            "确认", "取消",
//                            new ViewHub.EditDialogListener() {
//                                @Override
//                                public void onOkClick(DialogInterface dialog, EditText editText) {
//
//                                }
//
//                                @Override
//                                public void onOkClick(EditText editText) {// 确认
//                                    buhuo();
//                                }
//
//                                @Override
//                                public void onNegativecClick() {
//
//                                }
//                            });
//
//                } else {
                wxShare();
//                }
                break;
        }
    }

    private void goToBuyer(View v) {
        final View vvv = (View) v.getParent();
        if (!SpManager.getReplenishmentRemark_TIPS(mContext)) {
            if (mShopItemModel.getDisplayStatu().equals("新款") ||
                    mShopItemModel.getDisplayStatu().equals("补货中")) {
                buy(vvv);
                return;
            }
            String msg = mShopItem.getReplenishmentRemark();
//                    ViewHub.showOkDialog(mContext, "补拼提示", msg,"知道了",new DialogInterface.OnClickListener(){
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            buy(vvv);
//                        }
//                    });
            BuPinDomDialog dialog = new BuPinDomDialog(this);
            dialog.setTitle("补货须知！").setMessage(msg).setPositive("知道了", new BuPinDomDialog.PopDialogListener() {
                @Override
                public void onPopDialogButtonClick(int which) {
                    buy(vvv);
                }
            }).show();
//                    ViewHub.showTextDialog(mContext, "补拼提示", msg,
//                            "不再提示", "知道了",
//                            new ViewHub.EditDialogListener() {
//                                @Override
//                                public void onOkClick(DialogInterface dialog, EditText editText) {
//                                }
//
//                                @Override
//                                public void onOkClick(EditText editText) {
//                                    SpManager.setReplenishmentRemark_TIPS(mContext, true);
//                                    buy(vvv);
//                                }
//
//                                @Override
//                                public void onNegativecClick() {
//                                    buy(vvv);
//                                }
//                            });
            return;
        }
        buy(vvv);
    }

    /**
     * @description 微信分享
     * @created 2014-10-27 下午8:48:10
     * @author ZZB
     */
    private void wxShare() {
        if (mShopItem == null) {
            ViewHub.showShortToast(this, "商品信息加载失败，请重新进入后再分享");
            return;
        }
        NahuoDetailsShare share = new NahuoDetailsShare(mContext, mShopItem);
        share.show();
    }

    /**
     * @description 微信分享
     * @created 2014-10-27 下午8:48:10
     * @author ZZB
     */
    private void Share() {
        if (mShopItem == null) {
            ViewHub.showShortToast(this, "商品信息加载失败，请重新进入后再分享");
            return;
        }
        NahuoDetailsShare share = new NahuoDetailsShare(mContext, mShopItem);
        share.setType(NahuoDetailsShare.TYPE_SHARE);
        share.show();
    }

    /**
     * @description 收藏、取消收藏商品
     * @created 2014-10-27 上午9:57:12
     * @author ZZB
     */
    private void favoriteItem() {
        boolean isFavorite = mShopItem.isFavorite();
        if (isFavorite) {
            new Task(Step.REMOVE_FAVORITE_ITEM).execute();
        } else {
            new Task(Step.FAVORITE_ITEM).execute();
        }
    }

    /**
     * @description 转发到微铺或者修改
     * @created 2014-10-24 下午6:14:32
     * @author ZZB
     */
    private void shareToWp() {
        if (mLoadingShopInfo) {
            ViewHub.showShortToast(this, "加载数据中...");
            return;
        }
        switch (mShopItem.getItemCopyType()) {
            case Const.ShopItemCopyType.SELF_UPLOAD:// 创建
            case Const.ShopItemCopyType.SELF_COPY:// 转发
//                Intent intent = new Intent(mContext, UploadItemActivity.class);
//
//                intent.putExtra("itemID", mUpdateItem.itemId);// TODO 回调修改
//                Bundle bundle = new Bundle();
//                bundle.putSerializable("itemData", mShopItem);
//                if (mUpdateItem.shopUserId > 0) {
//                    mShopItem.setUserId(mUpdateItem.shopUserId);
//                    mShopItem.setUserName(mUpdateItem.shopUserName);
//                }
//                intent.putExtras(bundle);
//                startActivityForResult(intent, REQUEST_UPDATE_MY_ITEM);
                break;
            case Const.ShopItemCopyType.SELF_SHARE:// 已转发
//                Intent intent2 = new Intent(mContext, UpdateSharedItemActivity.class);
//                mUpdateItem.myItemId = mUpdateItem.itemId;
//                intent2.putExtra(UpdateSharedItemActivity.EXTRA_UPDATE_ITEM, mUpdateItem);
//                startActivityForResult(intent2, UpdateSharedItemActivity.REQUEST_CODE_UPDATE_WP_ITEM);
                break;
            case Const.ShopItemCopyType.NOT_SHARE:// 没转发过
            case Const.ShopItemCopyType.ALREADY_COPIED:// 已复制的商品
                final int applyId = mShopInfo.getCurrentUserApplyStatuID();
                switch (applyId) {

                    case ApplyAgentStatu.NOT_APPLY:
                    case ApplyAgentStatu.REJECT:
//                        ViewHub.showOkDialog(mContext, "提示", "您必须先申请成为该供货商的代理，才能转发商品到我的店铺，是否立即申请？", "申请代理", "放弃",
//                                new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialog, int which) {
//                                        AgentShopTask task = new AgentShopTask(mContext, mShopInfo.getUserId(), applyId);
//                                        task.setCallback(new AgentShopTask.Callback() {
//                                            @Override
//                                            public void onAgentFinished() {
//                                                mShopInfo.setCurrentUserApplyStatuID(ApplyAgentStatu.APPLYING);
//                                            }
//                                        });
//                                        task.execute();
//                                    }
//                                });
                        break;
                    case ApplyAgentStatu.APPLYING:
                        ViewHub.showShortToast(mContext, "供货商还未审核通过您的代理申请，请耐心等候");
                        break;
                    case ApplyAgentStatu.ACCEPT:
//                        Intent shareIntent = new Intent(this, Share2WPActivity.class);
//                        Share2WPItem item = new Share2WPItem(mId + "", mShopInfo.getUserId() + "", mShopItem.getName(),
//                                mShopItem.getPrice() + "", mShopItem.getRetailPrice() + "");
//                        item.setIntro(mShopItem.getIntroOrName());
//                        item.imgUrls = mShopItem.getImages();
//                        shareIntent.putExtra(Share2WPActivity.EXTRA_SHARE_ITEM, item);
//                        startActivityForResult(shareIntent, REQUEST_SHARE_TO_WP);
                        break;
                }
                break;
            case Const.ShopItemCopyType.ALREADY_SHARED:
                break;
        }
    }

    private UpdateItem newUpdateItem(Share2WPItem sItem) {
        UpdateItem item = new UpdateItem(sItem.myItemId, mShopItem.getName(), sItem.agentPrice);
        item.mGroupIds = sItem.mGroupIds;
        item.mGroupNames = sItem.mGroupNames;
        item.supplyPrice = sItem.supplyPrice;
        item.isOnly4Agent = sItem.isOnly4Agent;
        item.addRate = sItem.retailAddRate;
        return item;
    }

    /**
     * @description 购买
     * @created 2014-10-24 下午6:13:34
     * @author ZZB
     */
    private void buy(View v) {
//        if (mLoadingShopInfo) {
//            ViewHub.showShortToast(mContext, "数据加载中，请稍候再试");
//            return;
//        }
        showBuyPopup(v);
    }

    /**
     * @description 检查商品规格
     * @created 2014-10-23 下午9:14:19
     * @author ZZB
     */
    private void showBuyPopup(View v) {
        Log.i("ItemDetail", "ApplyId:" + mShopItemModel.getApplyStatuID());
        GoodBaseInfo baseInfo = new GoodBaseInfo(mShopItemModel.getItemID(), mShopItemModel.getName(),
                mShopItemModel.getIntro(), mShopItemModel.getMainColorPic(), mShopItemModel.getRetailPrice(),
                mShopItemModel.getPrice(), mShopItemModel.getApplyStatuID());
        Iterator<String> it1 = mSizes.iterator();
        while (it1.hasNext()) {
            String str = it1.next();
            Log.i(getClass().getSimpleName(), "size:" + str);
            baseInfo.addSize(str);
        }
        Iterator<String> it = mColors.iterator();
        while (it.hasNext()) {
            String str = it.next();
            Log.i(getClass().getSimpleName(), "color:" + str);
            baseInfo.addColor(str);
        }

        for (ProductModel pm : mShopItemModel.getProducts()) {
            if (pm.getStock() > 0) {
                baseInfo.addProduct(pm);
            }
        }
        baseInfo.setColorPicsBeanList(mShopItemModel.getColorPicsBeanList());

        SelectSizeColorMenu menu = new SelectSizeColorMenu(this, baseInfo);
        menu.setSelectMenuDismissListener(new SelectMenuDismissListener() {
            @Override
            public void dismissStart(long duration) {
                ScaleAnimation animation = new ScaleAnimation(0.8f, 1.0f, 0.8f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f,
                        Animation.RELATIVE_TO_SELF, 0.5f);//ScaleAnimation缩放动画
                animation.setFillAfter(true);
                animation.setDuration(duration);
                mRootView.startAnimation(animation);
            }

            @Override
            public void dismissEnd() {
            }
        });
        menu.show();

        ScaleAnimation animation = new ScaleAnimation(1.0f, 0.8f, 1.0f, 0.8f, Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setFillAfter(true);
        animation.setDuration(300);
        mRootView.startAnimation(animation);

        // ProductSpecSelDialog dialog = ProductSpecSelDialog.newInstance(colors, sizes);
        // dialog.setProductSpecSelCallback(new ProductSpecSelCallback() {
        // @Override
        // public void productSpecSel(int colorPosition, int sizePosition, int count) {
        // mSelectedColor = colors[colorPosition];
        // mSelectedSize = sizes[sizePosition];
        // mSelectedBuyCount = count;
        // new Task(Step.ADD_TO_CART).execute();
        // }
        // });
        // dialog.show(getFragmentManager(), "ProductDialog");

    }

    /**
     * @description 添加到购物车
     * @created 2014-10-23 下午8:39:30
     * @author ZZB
     */
    // private void addToShoppingCart() throws Exception {
    // ShoppingCartItem item = new ShoppingCartItem(mId, mSelectedColor, mSelectedSize, mSelectedBuyCount);
    // BuyOnlineAPI.addToShoppingCart(mContext, item);
    //
    // }

    /**
     * @description 申请代理
     * @created 2014-10-23 下午7:10:39
     * @author ZZB
     */
    private void applyAgent() {
        new Task(Step.APPLY_AGENT).execute();
    }

    private class Task extends AsyncTask<Object, Void, Object> {
        private Step mStep;
        private TimeCounter mLoadItemTc;

        public Task(Step step) {
            mStep = step;
        }

        @Override
        protected void onPreExecute() {
            switch (mStep) {
                case SHOP_RECORD:
                    break;
                case PINHUOITEM_DETAIL:
                    mDialog.start("加载数据中");
                    mLoadItemTc = new TimeCounter();
                    break;
                case LOAD_SHOP_ITEM:
                    mDialog.start("加载数据中");
                    mLoadItemTc = new TimeCounter();
                    break;
                case LOAD_SHOP_INFO:// 加载签名
                    break;
                case APPLY_AGENT:// 申请代理
                    mDialog.start("申请代理中...");
                    break;
                // case ADD_TO_CART:
                // mDialog.start("加入购物车中...");
                // break;
                case BUY:
                    mDialog.start("加载数据中...");
                    break;
                case SaveReplenishmentRecord:
                    mDialog.start("补货中...");
                    break;
                case FAVORITE_ITEM:
                    mDialog.start("收藏中...");
                    break;
                case REMOVE_FAVORITE_ITEM:
                    mDialog.start("取消收藏中...");
                    break;
                case CHECKMYITEMFAVORITE:
                    break;

            }
        }

        @Override
        protected Object doInBackground(Object... params) {
            try {
                switch (mStep) {
                    case SHOP_RECORD:
                        BuyOnlineAPI.setRecord(vThis, mId);
                        break;
                    case PINHUOITEM_DETAIL:
                        //拼货商品信息
                        mShopItem = BuyOnlineAPI.getInstance().getPiHuoItemDetailNew(mId, mPinHuoId, PublicData.getCookie(mContext));
                        // Log.e("Itemyu", "===>" + mShopItem.isFavorite() + "加载商品信息");
                        if (mUpdateItem == null) {
                            setUpUpdateItem();
                        }
                        if (mShopItem != null) {
                            mShopItem.setMyUserId(useId);
                        }
                        return mShopItem;
                    case LOAD_SHOP_ITEM:// 加载商品信息
                        mShopItem = BuyOnlineAPI.getInstance().getItemDetail(mId, PublicData.getCookie(mContext));
                        if (mUpdateItem == null) {
                            setUpUpdateItem();
                        }
                        return mShopItem;
                    case APPLY_AGENT:// 申请代理
                        int agentUserId = mShopInfo.getUserId();
                        AgentAPI.applyAgent(mContext, agentUserId, "");
                        break;
                    case SaveReplenishmentRecord://补货
                        BuyOnlineAPI.SaveReplenishmentRecord(mContext, mId);
                        break;
                    case LOAD_SHOP_INFO:// 加载店铺信息
                        mShopInfo = BuyOnlineAPI.getShopInfoForItemDetail(mContext, mId);
                        break;
                    // case ADD_TO_CART:// 添加到购物车
                    // addToShoppingCart();
                    // break;
                    case BUY:// 购买
                        // addToShoppingCart();
                        Utils.gotoShopcart(getApplicationContext());

                        break;
                    case FAVORITE_ITEM:
                        BuyOnlineAPI.addFavorite(mContext, mId, mPinHuoId);
                        break;
                    case REMOVE_FAVORITE_ITEM:
                        BuyOnlineAPI.removeFavorite(mContext, mId);
                        break;
                    case TUANPI:
                        return QuickSaleApi.getItemTuanPiData(mContext, mId, mPinHuoId);
                    case CHECKMYITEMFAVORITE:
                        return BuyOnlineAPI.checkMyItemFavorite(mContext, mId);
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
                if (((String) result).replace("error:", "").equals("401")) {
                    Utils.gotoLoginActivity(vThis);
                    finish();
                } else {
                    ViewHub.showLongToast(mContext, ((String) result).replace("error:", ""));
                }
            } else {
                switch (mStep) {
                    case PINHUOITEM_DETAIL:
                        try {
                            ShopItemModel bean = (ShopItemModel) result;
                            onShopItemLoaded(bean);
                            boolean isMyFavorite1 = false;
                            isMyFavorite1 = bean.isFavorite();
                            CanDownLoadPicAndVideo = bean.CanDownLoadPicAndVideo;
                            Log.e("Itemyu", "===>" + isMyFavorite1 + "");
                            itemFavoriteChange(isMyFavorite1);
                            ShopItemModel.ActivityBean mAbean = bean.getActivity();
                            String endTime = "";
                            if (mAbean != null) {
                                chengtuanCount = mAbean.getChengTuanCount();
                                dealCount = mAbean.getTransCount();
                                totalSaleCount = mAbean.getTotalSaleCount();
                                tuanpiOver = !mAbean.isIsStart();
                                endTime = mAbean.getEndTime();
                            }
                            onTuanPiDataLoaded();
                            long costs1 = mLoadItemTc.end();
                            mEndMillis = getMillis(endTime);
                            loadCountDown();
                            new Task(Step.SHOP_RECORD).execute();
                            if (useId > 0 && !mShopItem.getItemStatu().equals("已下架")) {
                                int detail_first = GuidePreference.getInstance().getSaveShopCartDeatail(useId);
                                if (detail_first == 1) {
                                    layout_explain.setVisibility(View.GONE);
                                } else {
                                    //layout_explain.setVisibility(View.VISIBLE);
                                }
                            }
                            BaiduStats.log(mContext, BaiduStats.EventId.ENTER_ITEM_DETAIL_TIME, costs1 + "-10倍", costs1 * 10);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                    case LOAD_SHOP_ITEM:
                        onShopItemLoaded((ShopItemModel) result);
                        long costs = mLoadItemTc.end();
                        BaiduStats.log(mContext, BaiduStats.EventId.ENTER_ITEM_DETAIL_TIME, costs + "-10倍", costs * 10);
                        break;
                    case APPLY_AGENT:
                        ViewHub.showOkDialog(mContext, "提示", "申请代理发送成功", "OK");
                        break;
                    case SaveReplenishmentRecord:
                        ViewHub.showShortToast(ItemDetailsActivity.this, "已成功提交");
                        break;
                    case LOAD_SHOP_INFO:
                        onShopInfoLoaded();
                        break;
                    case SHOP_RECORD:
                        break;
                    // case ADD_TO_CART:
                    // ViewHub.showLightPopDialog(ItemDetailsActivity.this, "提示", "添加到购物车成功，是否去结算？", "继续逛逛", "马上结算",
                    // new PopDialogListener() {
                    // @Override
                    // public void onPopDialogButtonClick(int which) {
                    // // new Task(Step.BUY).execute();
                    // Utils.gotoShopcart(getApplicationContext());
                    //
                    // }
                    // });
                    // ViewHub.showOkDialog(mContext, "提示", "添加到购物车成功，是否去结算？", "马上结算", "继续逛逛",
                    // new DialogInterface.OnClickListener() {
                    // @Override
                    // public void onClick(DialogInterface dialog, int which) {
                    // new Task(Step.BUY).execute();
                    // }
                    // });
                    // break;
                    case BUY:

                        break;
                    case FAVORITE_ITEM:
                        itemFavoriteChange(true);
                        break;
                    case REMOVE_FAVORITE_ITEM:
                        itemFavoriteChange(false);
                        break;
                    case CHECKMYITEMFAVORITE:
                        boolean isMyFavorite = false;
                        try {
                            JSONObject jo = new JSONObject(result.toString());
                            isMyFavorite = jo.getBoolean("IsMyFavorite");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        itemFavoriteChange(isMyFavorite);
                        break;
                    case TUANPI:
                        try {
                            JSONObject jo = new JSONObject(result.toString());
                            chengtuanCount = jo.getInt("ChengTuanCount");
                            dealCount = jo.getInt("DealCount");
                            tuanpiOver = !jo.getBoolean("IsStart");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        onTuanPiDataLoaded();
                        break;

                }

            }
        }

    }

    private long getMillis(String time) {
        try {
            return TimeUtils.timeStampToMillis(time);
        } catch (Exception e) {
            return 0;
        }
    }

    //收藏状态变更
    private void itemFavoriteChange(boolean isFavorite) {
        if (mShopItem == null || mTvFavorite == null) {
            return;
        }
        RelativeLayout.LayoutParams rp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        rp.addRule(RelativeLayout.CENTER_IN_PARENT);
        final Drawable cnav_nomal_sel = getResources().getDrawable(R.drawable.icon_detail_focused);
        cnav_nomal_sel.setBounds(0, 0, width, height);
        final Drawable cnav_nomal_unsel = getResources().getDrawable(R.drawable.icon_detail_focusno);
        cnav_nomal_unsel.setBounds(0, 0, width, height);
        if (isFavorite) {
            mTvFavorite.setText("已收藏");
            mTvFavorite.setCompoundDrawables(null, cnav_nomal_sel, null, null);
            // mTvFavorite.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.detail_is_collected, 0, 0);
            if (tv_collect != null) {
                tv_collect.setLayoutParams(rp);
                tv_collect.setText("已收藏");
                tv_collect.setCompoundDrawables(null, cnav_nomal_sel, null, null);
                //tv_collect.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.detail_is_collected, 0, 0);
            }
            mShopItem.setIsFavorite(true);
        } else {
            mTvFavorite.setText("收藏");
            mTvFavorite.setCompoundDrawables(null, cnav_nomal_unsel, null, null);
            //mTvFavorite.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.detail_is_collect, 0, 0);
            if (tv_collect != null) {
                tv_collect.setLayoutParams(rp);
                tv_collect.setText("收藏");
                tv_collect.setCompoundDrawables(null, cnav_nomal_unsel, null, null);
                // tv_collect.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.detail_is_collect, 0, 0);
            }
            mShopItem.setIsFavorite(false);
        }
    }


    private void setUpUpdateItem() {
//        mShopItem
        try {
            mUpdateItem = new UpdateItem(mShopItem.getName(), mShopItem.getItemID(), mShopItem.getOrgPrice() + "",
                    mShopItem.getRetailPrice() + "");
            mUpdateItem.setIntro(mShopItem.getIntroOrName());
            mUpdateItem.mGroupIds = mShopItem.getGroupIdsFromGroups();
            mUpdateItem.mGroupNames = mShopItem.getGroupNamesFromGroups();
            mUpdateItem.agentPrice = mShopItem.getPrice() + "";
            mUpdateItem.isOnly4Agent = mShopItem.getIsOnly4Agent();
            mUpdateItem.myItemId = mShopItem.getItemID();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initColorSizeMap(List<ProductModel> specs) {

        for (ProductModel pm : specs) {
            if (pm.getStock() > 0) {
                String color = pm.getColor();
                String size = pm.getSize();
                mColors.add(color);
                mSizes.add(size);

                // color对应的尺码
                String sizes = mColorSizeMap.get(color);
                sizes += size + ",";
                mColorSizeMap.put(color, sizes);

                // 尺码对应的颜色
                String colors = mSizeColorMap.get(size);
                colors += color + ",";
                mSizeColorMap.put(size, colors);
            }
        }
    }

//    @Override
//    public void onRightClick(View v) {
//        if (mLoadingShopInfo) {
//            ViewHub.showShortToast(mContext, "数据加载中，请稍候再试");
//            return;
//        }
//        Utils.gotoShopcart(getApplicationContext());
//        super.onRightClick(v);
//    }
//
//    @Override
//    public void onSearchClick(View v) {
//        if (mLoadingShopInfo) {
//            ViewHub.showShortToast(mContext, "数据加载中，请稍候再试");
//            return;
//        }
//        wxShare();
//        super.onSearchClick(v);
//    }
//
//    @Override
//    public void onRequestStart(String method) {
//        if (mLoadingDialog == null)
//            mLoadingDialog = new LoadingDialog(this);
//
//        mLoadingDialog.show();
//        super.onRequestStart(method);
//    }
//
//    @Override
//    public void onRequestSuccess(String method, Object object) {
//
//        super.onRequestSuccess(method, object);
//    }

    private class OnLongClickCopyListener implements OnLongClickListener {
        @Override
        public boolean onLongClick(View v) {
            Vibrator vib = (Vibrator) mContext.getSystemService(Service.VIBRATOR_SERVICE);
            vib.vibrate(50);
            v.setBackgroundResource(R.color.lightgray);
            ViewHub.showCopyView(mContext, v, v.getTag().toString(), true);
            return true;
        }

    }

}
