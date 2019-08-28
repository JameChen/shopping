package com.nahuo.live.xiaozhibo.mainui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.luck.picture.lib.tools.ScreenUtils;
import com.nahuo.live.xiaozhibo.adpater.GoodsAdapter;
import com.nahuo.live.xiaozhibo.model.GoodsBean;
import com.nahuo.live.xiaozhibo.permission.FloatWindowManager;
import com.nahuo.quicksale.R;
import com.nahuo.quicksale.activity.ShopCartNewActivity;
import com.nahuo.quicksale.common.ListUtils;
import com.nahuo.quicksale.common.SpManager;
import com.nahuo.quicksale.common.Utils;

/**
 * Created by jame on 2019/5/10.
 */

public class GoodsPopupWindow extends PopupWindow {
    private View conentView;
    private RecyclerView recyclerView;
    private GoodsBean goodsBean;
    private TextView tv_goods_count;
    private int count;
    private GoodsAdapter goodsAdapter;
    private int Type;
    private GoodsTryOnOnClick goodsTryOnOnClick;
    private GoodsOnItemClick onItemClick;
    private ImageView iv_shop_cart;
    private TextView tv_goods_red_count;

    public void setOnItemClick(GoodsOnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }

    public void setGoodsTryOnOnClick(GoodsTryOnOnClick goodsTryOnOnClick) {
        this.goodsTryOnOnClick = goodsTryOnOnClick;
    }

    public GoodsPopupWindow(final Context context, GoodsBean goodsBean, final int Type) {
        this.goodsBean = goodsBean;
        this.Type = Type;
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        conentView = inflater.inflate(R.layout.goods_pop_menu, null);
        iv_shop_cart = (ImageView) conentView.findViewById(R.id.iv_shop_cart);
        tv_goods_red_count = (TextView) conentView.findViewById(R.id.tv_goods_red_count);
        if (this.goodsBean != null) {
            if (this.goodsBean.getGoodsRedCount() > 0) {
                tv_goods_red_count.setVisibility(View.VISIBLE);
                tv_goods_red_count.setText(goodsBean.getGoodsRedCount()+"");
            } else
                tv_goods_red_count.setVisibility(View.INVISIBLE);
        } else {
            tv_goods_red_count.setVisibility(View.INVISIBLE);
        }
        iv_shop_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FloatWindowManager.getInstance().applyOrShowFloatWindow(context, new FloatWindowManager.GotoPlay() {
                    @Override
                    public void onCancel() {
                        goToShopCarts(Type, context);
                    }

                    @Override
                    public void onGotoPlay() {
                        //goodsPopupWindow.dismiss();
                        if (goodsTryOnOnClick != null)
                            goodsTryOnOnClick.OnFloatVideo();
                        goToShopCarts(Type, context);
                    }
                });


            }
        });
        recyclerView = (RecyclerView) conentView.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        goodsAdapter = new GoodsAdapter(context);
        goodsAdapter.setType(this.Type);
        goodsAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (onItemClick != null) {
                    onItemClick.OnItemClick((GoodsBean.GoodsListBean) adapter.getData().get(position), position);
                }
            }
        });
        goodsAdapter.setTryOnItemOnClick(new GoodsAdapter.TryOnItemOnClick() {
            @Override
            public void OnBuyClick(GoodsBean.GoodsListBean item, int position, boolean isTryOn) {
                if (goodsTryOnOnClick != null)
                    goodsTryOnOnClick.OnBuyClick(item, position, isTryOn);
            }

            @Override
            public void OnClick(GoodsBean.GoodsListBean item, int position, boolean isTryOn) {
                if (goodsTryOnOnClick != null)
                    goodsTryOnOnClick.OnClick(item, position, isTryOn);
            }
        });
        recyclerView.setAdapter(goodsAdapter);
        int h = ScreenUtils.getScreenHeight(context);
        int w = ScreenUtils.getScreenWidth(context);
// 设置SelectPicPopupWindow的View
        this.setContentView(conentView);
// 设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(w - 50);
// 设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(h * 5 / 7);
// 设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        this.setOutsideTouchable(true);
// 刷新状态
        this.update();
// 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0000000000);
// 点back键和其他地方使其消失,设置了这个才能触发OnDismisslistener ，设置其他控件变化等操作
        this.setBackgroundDrawable(dw);
// mPopupWindow.setAnimationStyle(android.R.style.Animation_Dialog);
// 设置SelectPicPopupWindow弹出窗体动画效果
        this.setAnimationStyle(R.style.popwin_anim_style);
        conentView.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {

                int height = conentView.findViewById(R.id.recyclerView).getTop();
                int y = (int) event.getY();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (y < height) {
                        dismiss();
                    }
                }
                return true;
            }
        });
        if (this.goodsBean != null) {
            count = this.goodsBean.getGoodsCount();
            if (ListUtils.isEmpty(this.goodsBean.getGoodsList())) {
                goodsAdapter.setNewData(null);
            } else {
                goodsAdapter.setNewData(this.goodsBean.getGoodsList());
            }
        }
        tv_goods_count = (TextView) conentView.findViewById(R.id.tv_goods_count);
        tv_goods_count.setText(count + "");
    }

    void goToShopCarts(int Type, Context context) {
        if (Type == GoodsAdapter.Type_Live) {
            if (SpManager.getIs_Login(context)) {
                Intent it = new Intent(context, ShopCartNewActivity.class);
                context.startActivity(it);
            } else {
                Utils.gotoLoginActivityForResult((Activity) context);
            }
        } else {
            Utils.gotoShopcart(context);
        }
    }

    public void noTifyList(int pos, boolean tryON) {
        if (goodsBean != null) {
            if (!ListUtils.isEmpty(goodsBean.getGoodsList())) {
                for (int i = 0; i < goodsBean.getGoodsList().size(); i++) {
                    GoodsBean.GoodsListBean bean = goodsBean.getGoodsList().get(i);
                    if (tryON) {
                        if (i == pos) {
                            bean.setTryOn(true);
                        } else {
                            bean.setTryOn(false);
                        }
                    } else {
                        bean.setTryOn(false);
                    }
                }
            }
            if (goodsAdapter != null)
                goodsAdapter.notifyDataSetChanged();
        }
    }

    public interface GoodsTryOnOnClick {
        void OnClick(GoodsBean.GoodsListBean item, int position, boolean isTryOn);

        void OnBuyClick(GoodsBean.GoodsListBean item, int position, boolean isTryOn);
        void OnFloatVideo();
    }

    public interface GoodsOnItemClick {

        void OnItemClick(GoodsBean.GoodsListBean item, int position);
    }
//    /**
//     * 显示popupWindow
//     *
//     * @param parent
//     */
//    public void showPopupWindow(View parent) {
//        if (!this.isShowing()) {
//// 以下拉方式显示popupwindow
//            this.showAsDropDown(parent, parent.getLayoutParams().width / 2, 18);
//        } else {
//            this.dismiss();
//        }
//    }
}