package com.nahuo.quicksale.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.luck.picture.lib.tools.ScreenUtils;
import com.nahuo.bean.FollowsBean;
import com.nahuo.bean.SortMenusBean;
import com.nahuo.constant.UpYunIcon;
import com.nahuo.library.helper.DisplayUtil;
import com.nahuo.library.helper.FunctionHelper;
import com.nahuo.library.helper.ImageUrlExtends;
import com.nahuo.quicksale.R;
import com.nahuo.quicksale.ViewHub;
import com.nahuo.quicksale.app.BWApplication;
import com.nahuo.quicksale.common.Const;
import com.nahuo.quicksale.common.ListUtils;
import com.nahuo.quicksale.common.TuanPiUtil;
import com.nahuo.quicksale.common.ViewUtil;
import com.nahuo.quicksale.controls.RectangleTextSpan;
import com.nahuo.quicksale.oldermodel.PinHuoModel;
import com.nahuo.quicksale.oldermodel.ShopItemListModel;
import com.nahuo.quicksale.oldermodel.quicksale.RecommendModel;
import com.nahuo.quicksale.util.GlideUtls;
import com.nahuo.quicksale.util.ReasonUtls;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jame on 2018/1/29.
 */

public class PinHuoShowAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private RecommendModel mRecommendModel;
    private static int IMAGE_WIDTH, IMAGE_HEIGHT;
    private PinHuoListener mListener;
    private int mChengTuanCount; //场次的成团数
    private int mQsChengTuanCount;
    private static final DecimalFormat PRICE_FORMAT = new DecimalFormat("#0.00");
    public boolean ShowCoinPayIcon;
    int currentMenuID;
    private TitleSortMenusLister menusLister;
    public boolean isCheckPassTitle = false;
    private EmptyLister emptyLister;
    public boolean isChangShi = false;
    private int passSortPos = -1;

    public int getPassSortPos() {
        return passSortPos;
    }

    public void setPassSortPos(int passSortPos) {
        this.passSortPos = passSortPos;
    }

    public void setChengTuanCount(int chengTuanCount) {
        mQsChengTuanCount = chengTuanCount;
    }

    public void setChangShi(boolean changShi) {
        isChangShi = changShi;
    }

    public void setEmptyLister(EmptyLister emptyLister) {
        this.emptyLister = emptyLister;
    }

    public void setMenusLister(TitleSortMenusLister menusLister) {
        this.menusLister = menusLister;
    }

    public int getCurrentMenuID() {
        return currentMenuID;
    }

    public void setCurrentMenuID(int currentMenuID) {
        this.currentMenuID = currentMenuID;
    }


    public void setShowCoinPayIcon(boolean showCoinPayIcon) {
        ShowCoinPayIcon = showCoinPayIcon;
    }

    public void setmListener(PinHuoListener mListener) {
        this.mListener = mListener;
    }

    public enum Type {TYPE_SHOP, TYPE_TITLE, TYPE_NEW_OLDER_ITEM, TYPE_SORT_MENUS_ITEM, TYPE_EMPTY_MID_ITEM, TYPE_VIEW_MARGIN, TYPE_HEAD, TYPE_FOOT}

    List<ShopItemListModel> mData = new ArrayList<>();

    public void setmData(List<ShopItemListModel> mData) {
        this.mData = mData;
    }

    public List<ShopItemListModel> getmData() {
        return mData;
    }

    private SparseArray<View> mHeaderViews;
    private SparseArray<View> mFooterViews;

    // 基本的头部类型开始位置  用于viewType
    public static int BASE_ITEM_TYPE_HEADER = 10000000;
    // 基本的底部类型开始位置  用于viewType
    private static int BASE_ITEM_TYPE_FOOTER = 20000000;

    public PinHuoShowAdapter(Context mContext) {
        this.mContext = mContext;
        mHeaderViews = new SparseArray<>();
        mFooterViews = new SparseArray<>();
        IMAGE_WIDTH = (DisplayUtil.getScreenWidth() - ScreenUtils.dip2px(BWApplication.getInstance(), DisplayUtil.getResDimen(BWApplication.getInstance(), R.dimen.rv_horizontal_spacing))) / 2;
        IMAGE_HEIGHT = ScreenUtils.getProportionHeight(IMAGE_WIDTH);
    }

    /**
     * 是不是头部类型
     */
    private boolean isHeaderViewType(int viewType) {
        int position = mHeaderViews.indexOfKey(viewType);
        return position >= 0;
    }

    /**
     * 是不是底部类型
     */
    private boolean isFooterViewType(int viewType) {
        int position = mFooterViews.indexOfKey(viewType);
        return position >= 0;
    }

    public int getSortItemIndex(View view) {
        int position = -1;
        if (mHeaderViews != null && view != null) {
            position = mHeaderViews.indexOfValue(view);
        }
        return position;
    }

    /**
     * 是不是底部位置
     */
    private boolean isFooterPosition(int position) {
        return position >= (mHeaderViews.size() + this.mData.size());
    }

    /**
     * 是不是头部位置
     */
    private boolean isHeaderPosition(int position) {
        return position < mHeaderViews.size();
    }

    // 添加头部
    public void addHeaderView(View view) {
        int position = mHeaderViews.indexOfValue(view);
        if (position < 0) {
            mHeaderViews.put(BASE_ITEM_TYPE_HEADER++, view);
        }
    }

    // 添加底部
    public void addFooterView(View view) {
        int position = mFooterViews.indexOfValue(view);
        if (position < 0) {
            mFooterViews.put(BASE_ITEM_TYPE_FOOTER++, view);
        }
    }

    // 移除头部
    public void removeHeaderView(View view) {
        int index = mHeaderViews.indexOfValue(view);
        if (index < 0) return;
        mHeaderViews.removeAt(index);
        notifyDataSetChanged();
    }

    // 移除底部
    public void removeFooterView(View view) {
        int index = mFooterViews.indexOfValue(view);
        if (index < 0) return;
        mFooterViews.removeAt(index);
        notifyDataSetChanged();
    }

    public void removeAllHeadsView() {
        if (mHeaderViews != null) {
            mHeaderViews.clear();
        }
        notifyDataSetChanged();
    }

    int headPos, footPos, headCount;

    public int getHeadCount() {
        if (mHeaderViews != null) {
            headCount = mHeaderViews.size();
        } else {
            headCount = 0;
        }
        return headCount;
    }

    public int getHeadPos() {
        return headPos;
    }

    public void setHeadPos(int headPos) {
        this.headPos = headPos;
    }

    public int getFootPos() {
        return footPos;
    }

    public void setFootPos(int footPos) {
        this.footPos = footPos;
    }

    /**
     * 创建头部或者底部的ViewHolder
     */
    private RecyclerView.ViewHolder createHeaderFooterViewHolder(View view) {
        return new RecyclerView.ViewHolder(view) {
        };
    }

    public void setmRecommendModel(RecommendModel mRecommendModel) {
        this.mRecommendModel = mRecommendModel;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == Type.TYPE_SHOP.ordinal()) {
            return new HolderShop(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_market, parent, false));
        } else if (viewType == Type.TYPE_TITLE.ordinal()) {
            return new HolderTitle(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_title, parent, false));
        } else if (viewType == Type.TYPE_NEW_OLDER_ITEM.ordinal()) {
            return new HolderNewOlder(LayoutInflater.from(parent.getContext()).inflate(R.layout.lvitem_pin_huo_detail1, parent, false));
        } else if (viewType == Type.TYPE_SORT_MENUS_ITEM.ordinal()) {
            return new HolderSortMenus(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sort_menus, parent, false));
        } else if (viewType == Type.TYPE_EMPTY_MID_ITEM.ordinal()) {
            return new HolderEmptyMid(LayoutInflater.from(parent.getContext()).inflate(R.layout.stall_empty, parent, false));
        } else if (viewType == Type.TYPE_VIEW_MARGIN.ordinal()) {
            return new HolderMaginView(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_margin_view, parent, false));
        } else if (viewType >= BASE_ITEM_TYPE_HEADER) {
            int pos = viewType - BASE_ITEM_TYPE_HEADER;
            View headerView = mHeaderViews.get(pos);
            return createHeaderFooterViewHolder(headerView);
        } else if (viewType == Type.TYPE_FOOT.ordinal()) {
            View footerView = mFooterViews.get(getFootPos());
            return createHeaderFooterViewHolder(footerView);
        }
        return null;
    }

    public class HolderShop extends RecyclerView.ViewHolder {
        ImageView iv_pic;
        TextView tv_title, tv_content, tv_right;

        public HolderShop(View convertView) {
            super(convertView);
            iv_pic = (ImageView) convertView.findViewById(R.id.iv_pic);
            tv_title = (TextView) convertView.findViewById(R.id.tv_title);
            tv_content = (TextView) convertView.findViewById(R.id.tv_content);
            tv_right = (TextView) convertView.findViewById(R.id.tv_right);
        }
    }

    public class HolderTitle extends RecyclerView.ViewHolder {
        RadioButton rb_new, rb_older;

        public HolderTitle(View itemView) {
            super(itemView);
            rb_new = (RadioButton) itemView.findViewById(R.id.rb_new);
            rb_older = (RadioButton) itemView.findViewById(R.id.rb_older);
        }
    }

    public void clear() {
        if (!ListUtils.isEmpty(mData)) {
            setPassSortPos(-1);
            mData.clear();
            notifyDataSetChanged();
        }
    }

    public class HolderSortMenus extends RecyclerView.ViewHolder {
        RecyclerView recyclerView;

        public HolderSortMenus(View itemView) {
            super(itemView);
            recyclerView = (RecyclerView) itemView.findViewById(R.id.recyclerView_sort_menu);
            recyclerView.setNestedScrollingEnabled(false);
        }
    }

    public class HolderMaginView extends RecyclerView.ViewHolder {

        public HolderMaginView(View itemView) {
            super(itemView);

        }
    }

    public class HolderEmptyMid extends RecyclerView.ViewHolder {
        TextView empty_txt;

        public HolderEmptyMid(View itemView) {
            super(itemView);
            empty_txt = (TextView) itemView.findViewById(R.id.empty_txt);
            empty_txt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (emptyLister != null)
                        emptyLister.onClickEmpty();
                }
            });
        }
    }

    public class HolderNewOlder extends RecyclerView.ViewHolder {
        FrameLayout layout;
        TextView tvPrice;
        TextView tvSaleCount;
        ImageView ivCover;
        TextView tvContent, tv_ori_price;
        RelativeLayout rl_content;
        RelativeLayout.LayoutParams lp;
        LinearLayout.LayoutParams llp;
        TextView tvPinStatus;
        ProgressBar progressBar;
        ImageView pinStatusIcon;
        ImageView video_icon;
        ImageView iv_down_over;
        View iv_sale_out;
        ImageView iv_coin_pay_icon;

        public HolderNewOlder(View itemView) {
            super(itemView);
            iv_coin_pay_icon = (ImageView) itemView.findViewById(R.id.iv_coin_pay_icon);
            layout = (FrameLayout) itemView.findViewById(R.id.layout);
            tvPrice = (TextView) itemView.findViewById(R.id.tv_price);
            tvSaleCount = (TextView) itemView.findViewById(R.id.tv_saleCount);
            ivCover = (ImageView) itemView.findViewById(R.id.iv_cover);
            tvContent = (TextView) itemView.findViewById(R.id.tv_content);
            tv_ori_price = (TextView) itemView.findViewById(R.id.tv_ori_price);
            tv_ori_price.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            rl_content = (RelativeLayout) itemView.findViewById(R.id.rl_content);
            lp = new RelativeLayout.LayoutParams(IMAGE_WIDTH, IMAGE_HEIGHT);
            llp = new LinearLayout.LayoutParams(IMAGE_WIDTH, IMAGE_HEIGHT);
            ivCover.setLayoutParams(lp);
            rl_content.setLayoutParams(llp);
            tvPinStatus = (TextView) itemView.findViewById(R.id.tv_pin_status);
            progressBar = (ProgressBar) itemView.findViewById(R.id.progress);
            pinStatusIcon = (ImageView) itemView.findViewById(R.id.tv_pin_status_icon);
            video_icon = (ImageView) itemView.findViewById(R.id.video_icon);
            iv_down_over = (ImageView) itemView.findViewById(R.id.iv_down_over);
            iv_sale_out = itemView.findViewById(R.id.iv_sale_out);
        }
    }

//    @Override
//    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
//        super.onAttachedToRecyclerView(recyclerView);
//        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
//        if (manager instanceof GridLayoutManager) {
//            final GridLayoutManager gridManager = ((GridLayoutManager) manager);
//            gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
//                @Override
//                public int getSpanSize(int position) {
//                    int type = getItemViewType(position);
//                    if (type == PinHuoShowAdapter.Type.TYPE_SHOP.ordinal()) {
//                        return gridManager.getSpanCount();
//                    } else if (type == PinHuoShowAdapter.Type.TYPE_TITLE.ordinal()) {
//                        return gridManager.getSpanCount();
//                    } else if (type == PinHuoShowAdapter.Type.TYPE_NEW_OLDER_ITEM.ordinal()) {
//                        return 1;
//                    }
//                    return gridManager.getSpanCount();
//                }
//            });
//        }
//    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (isHeaderPosition(position) || isFooterPosition(position)) {
            return;
        }
        position = position - mHeaderViews.size();
        if (holder instanceof HolderShop) {
            bindTypeShop((HolderShop) holder, position);
        } else if (holder instanceof HolderTitle) {
            bindTypeTitle((HolderTitle) holder, position);
        } else if (holder instanceof HolderNewOlder) {
            bindTypeNewOlder((HolderNewOlder) holder, position);
        } else if (holder instanceof HolderSortMenus) {
            bindTypeSortMenus((HolderSortMenus) holder, position);
        } else if (holder instanceof HolderEmptyMid) {
            bindTypeEmpty((HolderEmptyMid) holder, position);
        } else if (holder instanceof HolderMaginView) {
            bindTypeMargin((HolderMaginView) holder, position);
        }
    }

    private void bindTypeSortMenus(HolderSortMenus holder, int position) {
        final List<SortMenusBean> SortMenus = mData.get(position).getSortMenus();
        final boolean sortType_isNew = mData.get(position).sortType_isNew;
        if (!sortType_isNew) {
            setPassSortPos(position);
        }
        SortMenusAdpater adpater = new SortMenusAdpater(mContext);
        adpater.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (menusLister != null)
                    menusLister.OnClickSortMenus(SortMenus.get(position), position, sortType_isNew);
            }
        });
        adpater.setCurrentMenuID(currentMenuID);
        adpater.setNewData(SortMenus);
        GridLayoutManager manager = new GridLayoutManager(mContext, SortMenus.size());
        manager.setAutoMeasureEnabled(true);
        holder.recyclerView.setLayoutManager(manager);
        holder.recyclerView.setAdapter(adpater);
    }

    private void bindTypeShop(HolderShop holder, int position) {
        FollowsBean bean = mData.get(position).getFollowsBean();
        if (bean == null)
            return;
        String shopLogo = Const.getStallLogo(bean.getShopID());
        if (TextUtils.isEmpty(shopLogo)) {
            holder.iv_pic.setImageResource(R.drawable.empty_photo);
        } else {
            Picasso.with(mContext).load(shopLogo).placeholder(R.drawable.empty_photo).into(holder.iv_pic);
        }
        String shopName = bean.getShopName();
        String stallName = bean.getStallName();
        String content = bean.getContent();
        String statu = bean.getStatu();
        if (!TextUtils.isEmpty(shopName)) {
            holder.tv_title.setText(shopName);
        }
        if (!TextUtils.isEmpty(stallName)) {
            holder.tv_content.setText(stallName);
        }
        if (statu.equals("预告")) {
            holder.tv_right.setBackgroundResource(R.drawable.bg_rectangle_green_m);
        } else if (statu.equals("开拼中")) {
            holder.tv_right.setBackgroundResource(R.drawable.bg_rectangle_red_m);

        } else if (statu.equals("已结束")) {
            holder.tv_right.setBackgroundResource(R.drawable.bg_rectangle_gray_m);
        }
        if (!TextUtils.isEmpty(content)) {
            holder.tv_right.setVisibility(View.VISIBLE);
            holder.tv_right.setText(content);
        } else {
            holder.tv_right.setVisibility(View.GONE);

        }
        holder.itemView.setTag(R.id.item, bean);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    FollowsBean bean = (FollowsBean) v.getTag(R.id.item);
                    PinHuoModel model1 = new PinHuoModel();
                    model1.setShopID(bean.getShopID());
                    model1.setQsID(bean.getQsID());
                    model1.setUrl(bean.getUrl());
                    model1.setName(bean.getShopName());
                    model1.setActivityType("");
                    FollowsBean.VisitResultBean visitResultBean = bean.getVisitResult();
                    if (visitResultBean != null) {
                        PinHuoModel.VisitResultModel visitResultModel = new PinHuoModel.VisitResultModel();
                        visitResultModel.setCanVisit(visitResultBean.isCanVisit());
                        visitResultModel.setMessage(visitResultBean.getMessage());
                        visitResultModel.setResultType(visitResultBean.getResultType());
                        model1.setVisitResult(visitResultModel);
                    }
                    PinHuoModel.OpenStatuBean openStatuBean = new PinHuoModel.OpenStatuBean();
                    openStatuBean.setStatu(bean.getStatu());
                    if (bean.getStatu().equals("开拼中") || bean.getStatu().equals("预告")) {
                        model1.IsStart = true;
                        model1.setActivityType("拼货");
                    } else {
                        model1.IsStart = false;
                    }
                    model1.setOpenStatu(openStatuBean);
                    if (model1.QsID != 0) {
                        ViewUtil.gotoMarketChangci(mContext, model1);
                    } else {
                        ViewHub.showShortToast(mContext, "没有拼货场次");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
//        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(final View v) {
//                ViewHub.showLightPopDialog((Activity) mContext, "温馨提示", "您确定要取消关注吗？","取消",
//                        "确定", new LightPopDialog.PopDialogListener() {
//                            @Override
//                            public void onPopDialogButtonClick(int which) {
//                                FollowsBean bean = (FollowsBean) v.getTag(R.id.item);
//                                if (mListener!=null)
//                                    mListener.onRemoveFollowLongClick(bean);
//                            }
//                        });
//                return false;
//
//            }
        //       });

    }

    private void bindTypeMargin(HolderMaginView holder, int position) {

    }

    private void bindTypeEmpty(HolderEmptyMid holder, int position) {
        String txt = mData.get(position).getEmpty_txt();
        if (!TextUtils.isEmpty(txt)) {
            holder.empty_txt.setText(txt);
        }
    }

    private void bindTypeTitle(HolderTitle holder, int position) {
        String tittle2 = mData.get(position).getPart2title();
        String tittle1 = mData.get(position).getPart1title();
        final int type = mData.get(position).getShowTitleType();
        if (type == ShopItemListModel.Show_Older_Title) {
            holder.rb_new.setVisibility(View.GONE);
            holder.rb_older.setVisibility(View.VISIBLE);
            holder.rb_older.setChecked(true);
            holder.rb_new.setChecked(false);
        } else {
            if (isCheckPassTitle) {
                holder.rb_older.setChecked(true);
                holder.rb_new.setChecked(false);
            } else {
                holder.rb_older.setChecked(false);
                holder.rb_new.setChecked(true);
            }
            holder.rb_new.setVisibility(View.VISIBLE);
            holder.rb_older.setVisibility(View.VISIBLE);
        }
        holder.rb_new.setText(tittle1);
        holder.rb_older.setText(tittle2);
        holder.rb_new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (menusLister != null && type == ShopItemListModel.Show_All_Title) {
                    menusLister.onClickTitleMenus(false);
                }
            }
        });
        holder.rb_older.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (menusLister != null && type == ShopItemListModel.Show_All_Title) {
                    menusLister.onClickTitleMenus(true);
                }
            }
        });
    }

    private void bindTypeNewOlder(HolderNewOlder holder, int position) {
        ShopItemListModel item = mData.get(position);
        View itemView = holder.itemView;
        if (item != null) {
            ReasonUtls.setPinHuoItemMargin(itemView, item.getItem_layout_direction());
            holder.ivCover.setVisibility(View.VISIBLE);
            if (item.HasVideo) {
                holder.video_icon.setVisibility(View.VISIBLE);
            } else {
                holder.video_icon.setVisibility(View.GONE);
            }
            if (!TextUtils.isEmpty(item.getStatu())) {
                if (item.getStatu().equals("已下架")) {
                    holder.iv_down_over.setVisibility(View.VISIBLE);
                } else {
                    if (item.isSaleOut()) {
                        holder.iv_sale_out.setVisibility(View.VISIBLE);
                    } else {
                        holder.iv_sale_out.setVisibility(View.GONE);
                    }
                    holder.iv_down_over.setVisibility(View.GONE);
                }
            }
        }
        if (item.getID() == -1) {
            holder.tvPrice.setText("");
            holder.tvSaleCount.setText("");
            holder.tvContent.setText("");
            holder.tvPinStatus.setText("");
            holder.ivCover.setVisibility(View.INVISIBLE);
            holder.progressBar.setVisibility(View.INVISIBLE);
            holder.pinStatusIcon.setVisibility(View.INVISIBLE);
            holder.iv_coin_pay_icon.setVisibility(View.INVISIBLE);
            itemView.setEnabled(false);
            return;
        } else {
            itemView.setEnabled(true);
        }
        if (isChangShi) {
            if (ShowCoinPayIcon) {
                holder.iv_coin_pay_icon.setVisibility(View.VISIBLE);
                GlideUtls.glidePic(mContext, UpYunIcon.ICON_LIST, holder.iv_coin_pay_icon);
            } else {
                holder.iv_coin_pay_icon.setVisibility(View.GONE);
            }
        } else {
            if (item.isShowCoinPayIcon()) {
                holder.iv_coin_pay_icon.setVisibility(View.VISIBLE);
                GlideUtls.glidePic(mContext, UpYunIcon.ICON_LIST, holder.iv_coin_pay_icon);
            } else {
                holder.iv_coin_pay_icon.setVisibility(View.GONE);
            }
        }
        holder.progressBar.setVisibility(View.VISIBLE);
        holder.pinStatusIcon.setVisibility(View.VISIBLE);
        String picsUrl = item.getCover();
        if (picsUrl != null && picsUrl.length() > 0) {
            String url = ImageUrlExtends.getImageUrl(picsUrl, 15);
            GlideUtls.glideChang(mContext, url, holder.ivCover);
        }
        holder.ivCover.setVisibility(View.VISIBLE);
        holder.progressBar.setVisibility(View.VISIBLE);
        holder.pinStatusIcon.setVisibility(View.VISIBLE);
//        if (item.isPassItem) {
//            holder.pinStatusIcon.setVisibility(View.GONE);
//            holder.progressBar.setVisibility(View.GONE);
//            holder.tvPinStatus.setVisibility(View.GONE);
//        } else {
//            holder.pinStatusIcon.setVisibility(View.VISIBLE);
//            holder.progressBar.setVisibility(View.VISIBLE);
//            holder.tvPinStatus.setVisibility(View.VISIBLE);
//            int dealCount = item.getDealCount();
//            //判断到底是读场次还是商品的成团数量
//            mChengTuanCount = item.getChengTuanCount() > 0 ? item.getChengTuanCount() : mQsChengTuanCount;
//            holder.progressBar.setMax(mChengTuanCount);
//            holder.progressBar.setProgress(dealCount);
//            holder.tvPinStatus.setText(TuanPiUtil.getChengTuanTips(false, mChengTuanCount, dealCount));
//        }
//        if (item.getDisplayStatuID() == 0) {
//            holder.pinStatusIcon.setImageResource(R.drawable.new_icon);
//        } else {
//            holder.pinStatusIcon.setImageResource(R.drawable.bu_icom);
//        }
//        holder.tvPrice.setText("¥" + PRICE_FORMAT.format(item.getPrice()));
//        if (item.getTotalQty() > 0) {
//            holder.tvSaleCount.setText("总销量" + item.getTotalQty() + "件");
//        } else {
//            holder.tvSaleCount.setText("");
//        }
//        holder.tvContent.setText(item.Title);

        int dealCount = item.getDealCount();
        //判断到底是读场次还是商品的成团数量
        mChengTuanCount = item.getChengTuanCount() > 0 ? item.getChengTuanCount() : mQsChengTuanCount;
        holder.progressBar.setMax(mChengTuanCount);
        holder.progressBar.setProgress(dealCount);
        holder.ivCover.setVisibility(View.VISIBLE);
        holder.progressBar.setVisibility(View.VISIBLE);
        holder.pinStatusIcon.setVisibility(View.VISIBLE);
        if (item.getDisplayStatuID() == 2) {
            holder.pinStatusIcon.setVisibility(View.GONE);
            holder.tvPinStatus.setText("等待发起补拼");
        } else {
            if (item.getDisplayStatuID() == 0) {
                holder.pinStatusIcon.setImageResource(R.drawable.new_icon);
            } else if (item.getDisplayStatuID() == 1) {
                holder.pinStatusIcon.setImageResource(R.drawable.bu_icom);
            }
            holder.pinStatusIcon.setVisibility(View.VISIBLE);
            holder.tvPinStatus.setText(TuanPiUtil.getChengTuanTips(false, mChengTuanCount, dealCount));
        }
        if (item != null) {
            if (item.IsShowStatuIcon) {
                if (item.getDisplayStatuID() == 2) {
                    holder.pinStatusIcon.setVisibility(View.GONE);
                } else {
                    holder.pinStatusIcon.setVisibility(View.VISIBLE);
                }
            } else {
                holder.pinStatusIcon.setVisibility(View.GONE);
            }
            if (!TextUtils.isEmpty(item.getStatu())) {
                if (item.getStatu().equals("已下架")) {
                    holder.iv_down_over.setVisibility(View.VISIBLE);
                    holder.tvPinStatus.setText("已下架");
                } else {
                    if (item.isSaleOut()) {
                        holder.iv_sale_out.setVisibility(View.VISIBLE);
                    } else {
                        holder.iv_sale_out.setVisibility(View.GONE);
                    }
                    holder.iv_down_over.setVisibility(View.GONE);
                }
            }
        } else {
            holder.pinStatusIcon.setVisibility(View.GONE);
        }
        FunctionHelper.formatAgentPrice(mContext, holder.tvPrice, item.getPrice());
        if (TextUtils.isEmpty(item.getDiscount())) {
            holder.tv_ori_price.setVisibility(View.GONE);
            holder.tvSaleCount.setVisibility(View.VISIBLE);
        } else {
            holder.tv_ori_price.setVisibility(View.VISIBLE);
            holder.tvSaleCount.setVisibility(View.GONE);
        }
        FunctionHelper.formatRightPrice(mContext, holder.tv_ori_price, item.getOriPrice());
        if (TextUtils.isEmpty(item.getDiscount())) {
            holder.tvContent.setText(item.Title);
        } else {
            formatTitle(mContext, holder.tvContent, item.Title, item.getDiscount());
        }
        if (item.getTotalQty() > 0) {
            holder.tvSaleCount.setText("总销量" + item.getTotalQty() + "件");
        } else {
            holder.tvSaleCount.setText("");
        }


        itemView.setTag(item);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShopItemListModel item = (ShopItemListModel) v.getTag();
                mListener.onNewOlderItemClick(item);
            }
        });
    }

    public interface PinHuoListener {
        void onRemoveFollowLongClick(FollowsBean item);

        void onNewOlderItemClick(ShopItemListModel item);
    }

    private void formatAgentPrice(Context context, TextView tvPrice, double retailPrice) {
        DecimalFormat df = new DecimalFormat("#0.00");
        String retailPriceStr = "¥" + df.format(retailPrice);
        //int dotPos = retailPriceStr.indexOf(".");
        SpannableString spRetailPrice = new SpannableString(retailPriceStr);
        // 设置字体大小（相对值,单位：像素） 参数表示为默认字体大小的多少倍 //0.5f表示默认字体大小的一半
        spRetailPrice.setSpan(new RelativeSizeSpan(1.7f), 1, retailPriceStr.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        // 设置字体前景色
        spRetailPrice.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.pin_huo_red)), 0,
                retailPriceStr.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        tvPrice.setText(spRetailPrice);
    }

    private void formatTitle(Context context, TextView tvTittle, String name, String discount) {
        SpannableString spannableString = new SpannableString(" " + name);
        RectangleTextSpan topSpan = new RectangleTextSpan(context, R.color.pin_huo_red, R.color.pin_huo_red, discount);
        spannableString.setSpan(topSpan, 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvTittle.setText(spannableString);
    }

    @Override
    public int getItemCount() {
        return ListUtils.isEmpty(mData) ? 0 : mData.size() + mHeaderViews.size() + mFooterViews.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (isHeaderPosition(position)) {
            // 直接返回position位置的key
            return mHeaderViews.keyAt(position) + BASE_ITEM_TYPE_HEADER;
            //setHeadPos(mHeaderViews.keyAt(position));
            //return Type.TYPE_HEAD.ordinal();
        } else if (isFooterPosition(position)) {
            // 直接返回position位置的key
            position = position - mHeaderViews.size() - mData.size();
//           int p= mFooterViews.keyAt(position);
//            mFooterViews.get(p);
//            Log.d("dd",mFooterViews.keyAt(position)+"");
            setFootPos(mFooterViews.keyAt(position));
            return Type.TYPE_FOOT.ordinal();
        } else {
            position = position - mHeaderViews.size();
            if (!ListUtils.isEmpty(mData)) {
                int layout_type = mData.get(position).getItem_layout_type();
                switch (layout_type) {
                    case ShopItemListModel.TYPE_SHOP:
                        return Type.TYPE_SHOP.ordinal();
                    case ShopItemListModel.TYPE_TITLE:
                        return Type.TYPE_TITLE.ordinal();
                    case ShopItemListModel.TYPE_NEW_OLDER_ITEM:
                        return Type.TYPE_NEW_OLDER_ITEM.ordinal();
                    case ShopItemListModel.TYPE_SORT_MENUS:
                        return Type.TYPE_SORT_MENUS_ITEM.ordinal();
                    case ShopItemListModel.TYPE_EMPTY_MID:
                        return Type.TYPE_EMPTY_MID_ITEM.ordinal();
                    case ShopItemListModel.TYPE_MARIN_VIEW:
                        return Type.TYPE_VIEW_MARGIN.ordinal();
                    default:
                        return Type.TYPE_NEW_OLDER_ITEM.ordinal();
                }
            } else
                return Type.TYPE_NEW_OLDER_ITEM.ordinal();
        }
    }

    public interface TitleSortMenusLister {
        void OnClickSortMenus(SortMenusBean item, int pos, boolean sortType_isNew);

        void onClickTitleMenus(boolean isCheckPassTitle);
    }

    public interface EmptyLister {
        void onClickEmpty();
    }
}
