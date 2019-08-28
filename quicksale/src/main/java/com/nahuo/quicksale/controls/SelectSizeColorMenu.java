package com.nahuo.quicksale.controls;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Handler;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.luck.picture.lib.tools.ScreenUtils;
import com.nahuo.bean.ColorPicsBean;
import com.nahuo.library.controls.LightPopDialog;
import com.nahuo.library.controls.LoadingDialog;
import com.nahuo.library.controls.NoScrollGridView;
import com.nahuo.library.helper.ImageUrlExtends;
import com.nahuo.quicksale.R;
import com.nahuo.quicksale.ViewHub;
import com.nahuo.quicksale.activity.ColorPicGalleryActivity;
import com.nahuo.quicksale.api.HttpUtils;
import com.nahuo.quicksale.api.RequestMethod.ShopCartMethod;
import com.nahuo.quicksale.common.ListUtils;
import com.nahuo.quicksale.common.StringUtils;
import com.nahuo.quicksale.common.Utils;
import com.nahuo.quicksale.di.module.HttpManager;
import com.nahuo.quicksale.model.http.CommonSubscriber;
import com.nahuo.quicksale.model.http.response.PinHuoResponse;
import com.nahuo.quicksale.oldermodel.GoodBaseInfo;
import com.nahuo.quicksale.oldermodel.ProductModel;
import com.nahuo.quicksale.oldermodel.PublicData;
import com.nahuo.quicksale.util.RxUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

import static com.nahuo.quicksale.tabfragment.sort.SortTabFragment.Tag;

/**
 * @author JorsonWong
 * @description 购买时弹出菜单
 * @created 2015年4月10日 上午10:54:35
 */
public class SelectSizeColorMenu extends PopupWindow implements View.OnClickListener {

    private Activity mActivity;
    private View mRootView;
    private LinearLayout mContentViewBg, mProductView;

    private NoScrollGridView mGvColor;

    private ColorSizeAdapter mColorAdapter;
    private SelectProductAdapter mProductAdapter;
    private ImageView mIcon;
    private TextView mTvName, mTvPrice, mTvProxy, mTvSelected;
    private ListView mListView;
    private String mImageUrl;
    private String mName;
    private double mPrice;
    private String mProxy = "";
    private SelectMenuDismissListener mSelectMenuDismissListener;
    private String[] mColorTexts;
    private GoodBaseInfo mGoodBaseInfo;
    private LoadingDialog mLoadingDialog;
    public String mSelectColor;
    public List<ProductModel> mSelectList = new ArrayList<ProductModel>();
    private final static String TAG = "SelectSizeColorMenu";
    private String first_color_pic = "";
    private ArrayList<ColorPicsBean> colorPicsBeanList = new ArrayList<>();
    private RequestOptions options;
    private int pic_type = 14;
    private Button btn_ok;
    protected CompositeDisposable mCompositeDisposable;
    private boolean isLive;
    private  LiveBuyLister mLiveBuyLister;

    public void setmLiveBuyLister(LiveBuyLister mLiveBuyLister) {
        this.mLiveBuyLister = mLiveBuyLister;
    }

    public void setLive(boolean live) {
        isLive = live;
    }

    public boolean isLive() {
        return isLive;
    }

    protected void unSubscribe() {
        if (mCompositeDisposable != null) {
            mCompositeDisposable.clear();
        }
    }

    protected void addSubscribe(Disposable subscription) {
        if (mCompositeDisposable == null) {
            mCompositeDisposable = new CompositeDisposable();
        }
        mCompositeDisposable.add(subscription);
    }

    public SelectSizeColorMenu(Activity activity, GoodBaseInfo info) {
        super();
        this.mActivity = activity;
        RoundedCorners roundedCorners = new RoundedCorners(ScreenUtils.px2dip(activity,5));
        //通过RequestOptions扩展功能
        options = new RequestOptions()
                .centerCrop()
                .transform(roundedCorners)
                // .override(300,300)
                .placeholder(R.drawable.empty_photo)
                .error(R.drawable.empty_photo)
                .fallback(R.drawable.empty_photo)
                //.skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.ALL);
//        options = new RequestOptions()
//                .centerCrop()
//                // .override(300,300)
//                .placeholder(R.drawable.empty_photo)
//                .error(R.drawable.empty_photo)
//                .fallback(R.drawable.empty_photo)
//                //.skipMemoryCache(true)
//                .diskCacheStrategy(DiskCacheStrategy.ALL);
        initViews();
        setGoodBaseInfo(info);
    }

    private SelectSizeColorMenu(Activity activity, AttributeSet atrr) {
        super();
        this.mActivity = activity;
        initViews();
    }


    private void initViews() {
        // imageGetter = new Html.ImageGetter() {
        // @Override
        // public Drawable getDrawable(String source) {
        // try {
        // Drawable drawable = null;
        // int rId = Integer.parseInt(source);
        // drawable = mActivity.getResources().getDrawable(rId);
        // drawable.setBounds(0, 0, Const.getQQFaceWidth(mActivity), Const.getQQFaceWidth(mActivity));
        // return drawable;
        // } catch (Exception e) {
        // e.printStackTrace();
        // return null;
        // }
        // }
        // };

        mRootView = mActivity.getLayoutInflater().inflate(R.layout.select_size_color_menu, null);
        btn_ok = (Button) mRootView.findViewById(android.R.id.button1);
        mRootView.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int height = mContentViewBg.getTop();
                int y = (int) event.getY();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (y < height) {
                        dismiss();
                    }
                }
                return true;
            }
        });
        mRootView.findViewById(R.id.layout_pic).setOnClickListener(this);
        mIcon = (ImageView) mRootView.findViewById(android.R.id.icon);
        mRootView.findViewById(android.R.id.icon1).setOnClickListener(this);
        mTvName = (TextView) mRootView.findViewById(android.R.id.title);
        mTvPrice = (TextView) mRootView.findViewById(android.R.id.text1);
        mTvProxy = (TextView) mRootView.findViewById(android.R.id.text2);
        mProductView = (LinearLayout) mRootView.findViewById(R.id.lv_size_view);
        mListView = (ListView) mRootView.findViewById(R.id.lv_size);
//        View mListHeader = LayoutInflater.from(mActivity).inflate(R.layout.item_select_size_color_detail_header, null);
//        mListView.addHeaderView(mListHeader);
        View footList = mActivity.getLayoutInflater().inflate(R.layout.size_color_list_bottom, null);
        mTvSelected = (TextView) footList.findViewById(android.R.id.summary);
        mListView.addFooterView(footList);
        mProductAdapter = new SelectProductAdapter(mActivity, new ArrayList<ProductModel>());
        mProductAdapter.setListener(new SelectProductAdapter.OnChangeQtyListener() {
            @Override
            public void OnQtyChange(boolean reduce, int stock) {

                for (ProductModel pm : mProductAdapter.mList) {
                    for (ProductModel pmSelect : mSelectList) {
                        if (pm.getColor().equals(pmSelect.getColor()) && pm.getSize().equals(pmSelect.getSize())) {
                            pmSelect.setStock(pm.getStock());
                        }
                    }
                }
                int total = 0;
                for (ProductModel pm : mSelectList) {
                    if (pm.getStock() > 0) {
                        total += pm.getStock();
                    }
                }
                if (total > 0)
                    btn_ok.setText("添加到拿货车(" + total + "件)");
                else
                    btn_ok.setText("添加到拿货车");
                updateSelected();
            }
        });
        mListView.setAdapter(mProductAdapter);
        mGvColor = (NoScrollGridView) mRootView.findViewById(R.id.gv_color);

        mContentViewBg = (LinearLayout) mRootView.findViewById(android.R.id.content);


        btn_ok.setOnClickListener(this);
        mGvColor.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String color = (String) parent.getAdapter().getItem(position);

                if (color.equals(mSelectColor)) {
                    return;
                }

                mSelectColor = color;

                updateProductList(color);
                Map<String, Boolean> map = mColorAdapter.getSelectedMap();
                for (String key : map.keySet()) {
                    map.put(key, false);
                }

                map.put(color, true);

                mColorAdapter.notifyDataSetChanged();
                updateSelected();
            }
        });

    }

    private SelectSizeColorMenu create() {

        setItem();
        mColorAdapter = new ColorSizeAdapter(mActivity, mColorTexts);

        mGvColor.setAdapter(mColorAdapter);

        //默认选中第一个
        if (mColorTexts!=null&&mColorTexts.length>0)
        mSelectColor = mColorTexts[0];
        Map<String, Boolean> map = mColorAdapter.getSelectedMap();
        if (map.containsKey(mSelectColor)) {
            map.put(mSelectColor, !map.get(mSelectColor).booleanValue());
        } else {
            map.put(mSelectColor, true);
        }
        mColorAdapter.notifyDataSetChanged();
        updateProductList(mSelectColor);
        updateSelected();
        return this;
    }

    /**
     * @description 显示菜单栏
     * @created 2015年3月20日 上午11:22:23
     * @author JorsonWong
     */
    public void show() {

        create();

        // int[] location = new int[2];
        // view.getLocationOnScreen(location);
        // ListviewUtls.setListViewHeightBasedOnChildren(mListView);
        this.setWidth(LayoutParams.MATCH_PARENT);
        this.setHeight(LayoutParams.MATCH_PARENT);

        this.setContentView(mRootView);
        this.setFocusable(true);
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        this.setBackgroundDrawable(dw);
        // showAtLocation(view, Gravity.NO_GRAVITY, location[0], location[1] - getHeight());
        showAtLocation(mActivity.getWindow().getDecorView(), Gravity.CENTER, 0, 0);
        mContentViewBg.setVisibility(View.VISIBLE);
        mContentViewBg.startAnimation(AnimationUtils.loadAnimation(mActivity, R.anim.bottom_menu_appear));

    }

    // public int getStatusHeight() {
    // Rect frame = new Rect();
    // mActivity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
    // int statusHeight = frame.top;

    // return statusHeight;
    // }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_pic:
                int mpos = 0;
                if (!ListUtils.isEmpty(colorPicsBeanList)) {
                    for (int i = 0; i < colorPicsBeanList.size(); i++) {
                        if (mSelectColor.equals(colorPicsBeanList.get(i).getColor())) {
                            mpos = i;
                        }
                    }
                    Intent intent = new Intent(mActivity, ColorPicGalleryActivity.class);
                    intent.putExtra(ColorPicGalleryActivity.EXTRA_COLOPICS, colorPicsBeanList);
                    intent.putExtra(ColorPicGalleryActivity.EXTRA_CUR_POS, mpos);
                    mActivity.startActivity(intent);
                }
                break;
            case android.R.id.button1:

                List<String> selColors = mColorAdapter.getSelectedItems();
                List<ProductModel> products = mSelectList;

                boolean selectProducted = false;
                for (ProductModel pm : products) {
                    if (pm.getStock() > 0) {
                        selectProducted = true;
                        break;
                    }
                }

                if (selColors.size() <= 0) {
                    ViewHub.showShortToast(mActivity, "请选择颜色");
                    return;
                }
                if (!selectProducted) {
                    ViewHub.showShortToast(mActivity, "请选择一个库存");
                    return;
                }

                //  new Task().execute(getJsonParams());
                addShopCart();
                break;
            case android.R.id.icon1:
                dismiss();
                break;
            case R.id.rootView:
                dismiss();
                break;
            default:
                break;
        }
    }

    private void addShopCart() {
        if (this.mActivity == null) {
            return;
        }
        // RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json;charset=UTF-8"), getJsonParams().toString());
        addSubscribe(HttpManager.getInstance().getPinHuoNetCacheApi(Tag).addPinHuocart(getJsonParams())
                .compose(RxUtil.<PinHuoResponse<Object>>rxSchedulerHelper())
                .compose(RxUtil.<Object>handleResult()).subscribeWith(new CommonSubscriber<Object>(mActivity, true, R.string.menu_add_str) {
                    @Override
                    public void onNext(Object o) {
                        super.onNext(o);
//                        if (o.toString().equals(RxUtil.Result_OK)){
//                            ViewHub.showShortToast(mActivity, "添加成功");
//                        }else {
//                            ViewHub.showShortToast(mActivity, o.toString());
//                        }
                        if (!isLive()) {
                            ViewHub.showLightPopDialog(mActivity, "提示", "添加到拿货车成功，是否去结算？", "继续逛逛", "马上结算", new LightPopDialog.PopDialogListener() {
                                @Override
                                public void onPopDialogButtonClick(int which) {
                                    Utils.gotoShopcart(mActivity.getApplicationContext());
                                }
                            });

                        }else {
                            if (mLiveBuyLister!=null){
                                mLiveBuyLister.onBuy();
                            }
                        }
                        dismiss();
                    }
                }));
    }
    public interface LiveBuyLister{
        void  onBuy();
    }
    private String getColorsArray() {
        List<String> selColors = mColorAdapter.getSelectedItems();
        StringBuilder colors = new StringBuilder();
        colors.append("[");
        for (int i = 0; i < selColors.size(); i++) {
            colors.append("'");
            colors.append(selColors.get(i));
            colors.append("'");
            if (i < selColors.size() - 1)
                colors.append(",");
        }
        colors.append("]");
        return colors.toString();
    }

    private String getJsonParams() {
        String builder = "";
        builder += "{";
        builder += "'itemId':" + mGoodBaseInfo.ItemID + ",";
        builder += "'Products':[";
        boolean addProducted = false;
        for (ProductModel pm : mSelectList) {
            if (pm.getStock() > 0) {
                builder += "{";
                builder += "'color':'" + pm.getColor() + "',";
                builder += "'size':'" + pm.getSize() + "',";
                builder += "'qty':" + pm.getStock() + "";
                builder += "},";
                addProducted = true;
            }
        }
        if (addProducted) {
            builder = builder.substring(0, builder.length() - 1);
        }
        builder += "]";
        builder += "}";
        Log.i(TAG, "json parms:" + builder.toString());
        return builder.toString();
    }

    String colorPic = "";

    @Override
    public void dismiss() {
        if (mContentViewBg.isShown()) {
            Animation anim = AnimationUtils.loadAnimation(mActivity, R.anim.bottom_menu_disappear);
            if (mSelectMenuDismissListener != null) {
                mSelectMenuDismissListener.dismissStart(anim.getDuration());
            }
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    mContentViewBg.clearAnimation();
                    mContentViewBg.setVisibility(View.GONE);
                    dismiss();
                }
            }, anim.getDuration());
            mContentViewBg.startAnimation(anim);
        } else {
            if (mSelectMenuDismissListener != null) {
                mSelectMenuDismissListener.dismissEnd();
            }
            super.dismiss();
        }
        unSubscribe();
    }

    private void setGoodBaseInfo(GoodBaseInfo info) {
        mGoodBaseInfo = info;
        mImageUrl = info.Cover;
        mName = info.Name;
        double retailPrice = info.RetailPrice;// 零售价
        double myPrice = info.Price;// 代理价
        mPrice = info.ApplyStatuID == 3 ? myPrice : retailPrice;
        mProxy = "";//info.ApplyStatuID == 3 ? "(已代理，代理价付款)" : "(未代理，零售价付款)";
        Log.i(TAG, "ApplyStatuID:" + info.ApplyStatuID);
        mColorTexts = info.getColors();
        if (info != null) {
            colorPicsBeanList.clear();
            colorPicsBeanList.addAll(info.getColorPicsBeanList());
            if (info.getProduct() != null && info.getProduct().size() > 0) {
                for (ProductModel str : info.getProduct()) {
                    first_color_pic = str.getColorPic();
                    break;
                }
            }
        }


    }

    private void setItem() {
        if (TextUtils.isEmpty(first_color_pic)) {
//            if (TextUtils.isEmpty(mImageUrl)) {
//                mIcon.setImageResource(R.drawable.empty_photo);
//            } else {
//                String iconUrl = ImageUrlExtends.getImageUrl(mImageUrl, 21);
//                Picasso.with(mActivity).load(iconUrl).placeholder(R.drawable.empty_photo).into(mIcon);
//            }
            String iconUrl = ImageUrlExtends.getImageUrl(mImageUrl, pic_type);
            //设置图片圆角角度

            Glide.with(mActivity)
                    .load(iconUrl)
                    .apply(options)
                    .into(mIcon);
        } else {
            String iconUrl = ImageUrlExtends.getImageUrl(first_color_pic, pic_type);
            //Picasso.with(mActivity).load(iconUrl).placeholder(R.drawable.empty_photo).into(mIcon);
            Glide.with(mActivity)
                    .load(iconUrl)
                    .apply(options)
                    .into(mIcon);
        }

        mTvName.setText(mName);
        mTvPrice.setText("¥" + mPrice);
        mTvProxy.setText(mProxy);
    }

    private void updateProductList(String changeColor) {
        List<ProductModel> datas = new ArrayList<ProductModel>();
        boolean isInList = false;
        for (ProductModel pm : mSelectList) {
            if (pm.getColor().equals(changeColor)) {
                isInList = true;
            }
        }
        if (isInList) {
            for (int i = 0; i < mSelectList.size(); i++) {
                if (mSelectList.get(i).getColor().equals(changeColor)) {
                    datas.add(mSelectList.get(i));
                }
            }
        } else {
            for (ProductModel pm : mGoodBaseInfo.getProduct()) {
                if (pm.getStock() > 0 && pm.getColor().equals(changeColor)) {
                    ProductModel pm_n = new ProductModel();
                    pm_n.setColor(pm.getColor());
                    pm_n.setSize(pm.getSize());
                    pm_n.setStock(0);
                    pm_n.setPrice(pm.getPrice());
                    pm_n.setMaxStock(pm.getStock());
                    pm_n.setColorPic(pm.getColorPic());
                    datas.add(pm_n);
                    mSelectList.add(pm_n);
                }
            }
        }
        mProductAdapter.mList = datas;
        if (!ListUtils.isEmpty(datas)) {
            colorPic = datas.get(0).getColorPic();
            if (TextUtils.isEmpty(colorPic)) {
//                if (TextUtils.isEmpty(mImageUrl)) {
//                    mIcon.setImageResource(R.drawable.empty_photo);
//                } else {
//                    String iconUrl = ImageUrlExtends.getImageUrl(mImageUrl, mActivity.getResources().getInteger(R.integer.grid_pic_width_small));
//                    Picasso.with(mActivity).load(iconUrl).resize(100, 100).placeholder(R.drawable.empty_photo).into(mIcon);
//                }
                String iconUrl = ImageUrlExtends.getImageUrl(mImageUrl, pic_type);
                Glide.with(mActivity)
                        .load(iconUrl)
                        .apply(options)
                        .into(mIcon);
            } else {
                String iconUrl = ImageUrlExtends.getImageUrl(colorPic, pic_type);
                // Picasso.with(mActivity).load(iconUrl).resize(100, 100).placeholder(R.drawable.empty_photo).into(mIcon);
                Glide.with(mActivity)
                        .load(iconUrl)
                        .apply(options)
                        .into(mIcon);
            }
        }
        mProductAdapter.notifyDataSetChanged();

    }

    private void updateSelected() {

        StringBuilder builder = new StringBuilder();
        //builder.append("已选择：\n");
        builder.append("");
        Map<String, String> maps = new HashMap<String, String>();
        for (ProductModel pm : mSelectList) {
            if (pm.getStock() > 0) {
                if (maps.containsKey(pm.getColor())) {
                    maps.put(pm.getColor(), maps.get(pm.getColor()) + pm.getSize() + "/" + pm.getStock() + "件,");
                } else {
                    maps.put(pm.getColor(), pm.getSize() + "/" + pm.getStock() + "件,");
                }
//                builder.append(pm.getSize());
//                if (!pm.getSize().endsWith("码"))
//                    builder.append("码");
//                builder.append("/");
//                builder.append(pm.getColor());
//                builder.append("/");
//                builder.append(pm.getStock() + "件，");
            }
        }
        for (String color : maps.keySet()) {
            builder.append(color + "：" + maps.get(color).substring(0, maps.get(color).length() - 1));
            builder.append("\n");
        }
        if (mSelectList.size() > 0) {
            mTvSelected.setText(StringUtils.deleteEndStr(builder.toString(), "，"));
        } else {
            mTvSelected.setText("");
        }
    }

    public SelectSizeColorMenu setSelectMenuDismissListener(SelectMenuDismissListener listener) {
        this.mSelectMenuDismissListener = listener;
        return this;
    }

    public interface SelectMenuDismissListener {
        public void dismissStart(long duration);

        public void dismissEnd();

    }

    private class Task extends AsyncTask<String, Void, Object> {

        @Override
        protected void onPreExecute() {
            if (mLoadingDialog == null) {
                mLoadingDialog = new LoadingDialog(mActivity);
            }
            mLoadingDialog.start("正在添加到拿货车....");
            super.onPreExecute();
        }

        @Override
        protected Object doInBackground(String... params) {
            String ret = "";
            try {
                Log.i(TAG, "httpPost json:" + params[0]);
                Map<String, String> p = new HashMap<String, String>();
                p.put("data", params[0]);
                ret = HttpUtils.httpPostWithJson(ShopCartMethod.SHOP_AGENT_CART_ADD_ITEMS, p,
                        PublicData.getCookie(mActivity));
                Log.i(TAG, "httpPost add items:" + ret);
            } catch (Exception e) {
                e.printStackTrace();
                ret = "error:" + e.toString();
            }
            return ret;
        }

        @Override
        protected void onPostExecute(Object result) {
            if (mLoadingDialog != null)
                mLoadingDialog.stop();
            if (result instanceof String && ((String) result).startsWith("error:")) {
                ViewHub.showLongToast(mActivity.getApplicationContext(),
                        result.toString().replace("java.lang.Exception:", "").replace("error:", ""));
                // 失败时恢复数量为1
            } else {
                ViewHub.showLightPopDialog(mActivity, "提示", "添加到拿货车成功，是否去结算？", "继续逛逛", "马上结算", new LightPopDialog.PopDialogListener() {
                    @Override
                    public void onPopDialogButtonClick(int which) {
                        Utils.gotoShopcart(mActivity.getApplicationContext());
                    }
                });
                dismiss();
            }
            super.onPostExecute(result);
        }

    }
}
