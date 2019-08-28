package com.nahuo.live.xiaozhibo.adpater;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.nahuo.library.helper.ImageUrlExtends;
import com.nahuo.live.xiaozhibo.common.utils.GlideUtls;
import com.nahuo.live.xiaozhibo.model.LiveListBean;
import com.nahuo.live.xiaozhibo.model.ParLiveListBean;
import com.nahuo.quicksale.R;
import com.nahuo.quicksale.common.ListUtils;

import static com.nahuo.constant.Constant.TYPE_LEVEL_0;
import static com.nahuo.constant.Constant.TYPE_LEVEL_1;

/**
 * Created by jame on 2019/5/7.
 */

public class LiveListAdapter extends BaseMultiItemQuickAdapter<MultiItemEntity, BaseViewHolder> {
    private Context context;
    private LiveOnClickListener listener;

    public void setListener(LiveOnClickListener listener) {
        this.listener = listener;
    }

    public LiveListAdapter(Context context) {
        super(null);
        this.context = context;
        addItemType(TYPE_LEVEL_0, R.layout.item_live_list_lv0);
        addItemType(TYPE_LEVEL_1, R.layout.item_live_list_lv1);
    }

    @Override
    protected void convert(BaseViewHolder helper, MultiItemEntity item) {
        switch (helper.getItemViewType()) {
            case TYPE_LEVEL_0:
                final ParLiveListBean parent = (ParLiveListBean) item;
                helper.setText(R.id.textView, parent.getTitle());
                break;
            case TYPE_LEVEL_1:
                final LiveListBean.SubLiveListBean sub = (LiveListBean.SubLiveListBean) item;
                if (item != null) {
                    ImageView big_iv = helper.getView(R.id.iv_icon);
                    ImageView iv_01 = helper.getView(R.id.iv_01);
                    ImageView iv_02 = helper.getView(R.id.iv_02);
                    ImageView iv_03 = helper.getView(R.id.iv_03);
                    TextView tv_watch_count = helper.getView(R.id.tv_watch_count);
                    TextView tv_look_start = helper.getView(R.id.tv_look_start);
                    TextView tv_time_title = helper.getView(R.id.tv_time_title);
                    if (sub.getGoodsCount() > 0) {
                        helper.setVisible(R.id.tv_goods_count, true);
                    } else {
                        helper.setVisible(R.id.tv_goods_count, false);
                    }
                    switch (sub.getType()) {
                        case LiveListBean.Type_ProcessLiveList:
                            tv_watch_count.setVisibility(View.VISIBLE);
                            tv_watch_count.setTextColor(ContextCompat.getColor(context, R.color.txt_black));
                            tv_watch_count.setTextSize(16);
                            tv_watch_count.setText(sub.getWatchCount() + "人在线");
                            tv_look_start.setVisibility(View.VISIBLE);
                            tv_look_start.setText("进入直播");
                            tv_look_start.setTextColor(ContextCompat.getColor(context, R.color.txt_black));
                            tv_look_start.setBackground(ContextCompat.getDrawable(context, R.drawable.btn_live_rectangle_black));
                            tv_time_title.setText("正在开播中");
                            tv_time_title.setTextColor(ContextCompat.getColor(context, R.color.txt_black));
                            tv_time_title.setTextSize(16);
                            break;
                        case LiveListBean.Type_PreviewLiveList:
                            tv_watch_count.setVisibility(View.GONE);
                            if (sub.isCanWatch()) {
                                tv_look_start.setVisibility(View.VISIBLE);
                            } else {
                                tv_look_start.setVisibility(View.GONE);
                            }
                            tv_look_start.setText("进入直播");
                            tv_look_start.setTextColor(ContextCompat.getColor(context, R.color.txt_black));
                            tv_look_start.setBackground(ContextCompat.getDrawable(context, R.drawable.btn_live_rectangle_black));
                            tv_time_title.setText(sub.getTimeTitle());
                            tv_time_title.setTextColor(ContextCompat.getColor(context, R.color.b_red));
                            tv_time_title.setTextSize(13);
                            break;
                        case LiveListBean.Type_OverLiveList:
                            tv_watch_count.setVisibility(View.VISIBLE);
                            tv_watch_count.setTextColor(ContextCompat.getColor(context, R.color.txt_gray));
                            tv_watch_count.setTextSize(13);
                            tv_watch_count.setText(sub.getWatchCount() + "人已看");
                            tv_look_start.setVisibility(View.VISIBLE);
                            tv_look_start.setText("查看回放");
                            tv_look_start.setTextColor(ContextCompat.getColor(context, R.color.white));
                            tv_look_start.setBackground(ContextCompat.getDrawable(context, R.drawable.btn_live_shape_red));
                            tv_time_title.setText(sub.getTimeTitle());
                            tv_time_title.setTextColor(ContextCompat.getColor(context, R.color.txt_gray));
                            tv_time_title.setTextSize(13);
                            break;
                    }
                    helper.setText(R.id.tv_title, sub.getTitle());
                    helper.setText(R.id.tv_goods_count, sub.getGoodsCount() + "件\n直播款");
                    String cover01 = "", cover02 = "", cover03 = "";
                    if (ListUtils.isEmpty(sub.getGoodsList())) {
                        helper.setVisible(R.id.iv_01, false);
                        helper.setVisible(R.id.iv_02, false);
                        helper.setVisible(R.id.iv_03, false);
                        cover01 = "";
                        cover02 = "";
                        cover03 = "";
                    } else {
                        if (sub.getGoodsList().size() == 1) {
                            cover01 = sub.getGoodsList().get(0).getCover();
                            cover02 = "";
                            cover03 = "";

                        } else if (sub.getGoodsList().size() == 2) {
                            cover01 = sub.getGoodsList().get(0).getCover();
                            cover02 = sub.getGoodsList().get(1).getCover();
                            cover03 = "";
                        } else if (sub.getGoodsList().size() >= 3) {
                            cover01 = sub.getGoodsList().get(0).getCover();
                            cover02 = sub.getGoodsList().get(1).getCover();
                            cover03 = sub.getGoodsList().get(2).getCover();
                        }
                    }
                    GlideUtls.loadRoundedCorners(context, R.drawable.empty_square_photo, ImageUrlExtends.getImageUrl(sub.getCover()), big_iv);
                    GlideUtls.loadRoundedCorners(context, R.drawable.empty_square_photo, ImageUrlExtends.getImageUrl(cover01), iv_01);
                    GlideUtls.loadRoundedCorners(context, R.drawable.empty_square_photo, ImageUrlExtends.getImageUrl(cover02), iv_02);
                    GlideUtls.loadRoundedCorners(context, R.drawable.empty_square_photo, ImageUrlExtends.getImageUrl(cover03), iv_03);
                    helper.getConvertView().setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                                if (listener!=null)
                                    listener.onClick(sub);
                        }
                    });
                }
                break;
        }
    }
    public interface LiveOnClickListener{
        void  onClick(LiveListBean.SubLiveListBean bean);
    }
}
