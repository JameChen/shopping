package com.nahuo.quicksale.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.SystemClock;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.google.gson.reflect.TypeToken;
import com.luck.picture.lib.tools.ScreenUtils;
import com.nahuo.constant.UmengClick;
import com.nahuo.library.controls.LoadingDialog;
import com.nahuo.library.helper.GsonHelper;
import com.nahuo.library.helper.ImageUrlExtends;
import com.nahuo.library.utils.TimeUtils;
import com.nahuo.quicksale.PinHuoNewOveredActivity;
import com.nahuo.quicksale.R;
import com.nahuo.quicksale.Topic.PostDetailActivity;
import com.nahuo.quicksale.ViewHub;
import com.nahuo.quicksale.activity.ItemPreview1Activity;
import com.nahuo.quicksale.api.HttpRequestHelper;
import com.nahuo.quicksale.api.HttpRequestListener;
import com.nahuo.quicksale.api.QuickSaleApi;
import com.nahuo.quicksale.app.BWApplication;
import com.nahuo.quicksale.common.Const;
import com.nahuo.quicksale.common.ViewUtil;
import com.nahuo.quicksale.countdownutils.CountDownTask;
import com.nahuo.quicksale.countdownutils.CountDownTimers;
import com.nahuo.quicksale.customview.ItemJCVideoPlayerStandard;
import com.nahuo.quicksale.customview.PinHuoTextView;
import com.nahuo.quicksale.eventbus.BusEvent;
import com.nahuo.quicksale.eventbus.EventBusId;
import com.nahuo.quicksale.jcvideoplayer_lib.JCVideoPlayer;
import com.nahuo.quicksale.oldermodel.PinHuoModel;
import com.nahuo.quicksale.oldermodel.ResultData;
import com.nahuo.quicksale.util.Objects;
import com.nahuo.quicksale.util.UMengTestUtls;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by jame on 2018/4/24.
 */

public class PinHuoMainAdapter extends BaseQuickAdapter<PinHuoModel,BaseViewHolder> implements View.OnClickListener{
    private PinHuoMainAdapter vThis = this;
    private Context mContext;
    private LoadingDialog mLoadingDialog;
    private CountDownTask mCountDownTask;
    public static boolean isPause = false;
    private RequestOptions options;
    private RequestOptions options1;
    private List<PinHuoModel> mdata;

    public void setData(List<PinHuoModel> mdata) {
        this.mdata = mdata;
        super.setNewData(mdata);
    }

    public PinHuoMainAdapter(Context context, @LayoutRes int layoutResId, @Nullable List<PinHuoModel> data) {
        super(layoutResId, data);
        mContext = context;
        mLoadingDialog = new LoadingDialog(mContext);
        options = new RequestOptions()
                //.centerCrop()
                .placeholder(R.color.transparent)
                .error(R.color.transparent)
                .fallback(R.color.transparent)
                //.skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.ALL);
        options1 = new RequestOptions()
                //.centerCrop()
                .placeholder(R.drawable.empty_photo)
                .error(R.drawable.empty_photo)
                .fallback(R.drawable.empty_photo)
                //.skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.ALL);
    }

    public void setCountDownTask(CountDownTask countDownTask) {
        if (!Objects.equals(mCountDownTask, countDownTask)) {
            mCountDownTask = countDownTask;
            notifyDataSetChanged();
        }
    }
    /**
     * 取消定时
     *
     * @author James Chen
     * @create time in 2017/3/27 16:36
     */
    private void cancelCountDown(View convertView) {
        if (mCountDownTask != null) {
            mCountDownTask.cancel(convertView);
        }
    }
    public void getFocusStat() {
        List<String> s = new ArrayList<>();
        for (PinHuoModel p : mdata) {
            s.add(p.getShopID() + "");
        }
        QuickSaleApi.getFocusStatList(mContext, mRequestHelper, new HttpRequestListener() {
            @Override
            public void onRequestStart(String method) {
//                mLoadingDialog.start("读取关注数据中");
            }

            @Override
            public void onRequestSuccess(String method, Object object) {
//                mLoadingDialog.stop();

                try {
                    List<Long> shopids = GsonHelper.jsonToObject(object.toString(),
                            new TypeToken<List<Long>>() {
                            });
                    for (PinHuoModel p : mdata) {
                        if (shopids.indexOf(p.getShopID()) > -1) {
                            p.isFocus = true;
                        }
                    }
                    notifyDataSetChanged();
                } catch (Exception ex) {
                }
            }

            @Override
            public void onRequestFail(String method, int statusCode, String msg) {
//                mLoadingDialog.stop();
                ViewHub.showShortToast(mContext, msg);
            }

            @Override
            public void onRequestExp(String method, String msg, ResultData data) {
//                mLoadingDialog.stop();
                ViewHub.showShortToast(mContext, msg);
            }
        }, s);
    }

    private HttpRequestHelper mRequestHelper = new HttpRequestHelper();

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_overed:// 往前回顾
                PinHuoModel item1 = (PinHuoModel) v.getTag();
                Intent it = new Intent(mContext, PinHuoNewOveredActivity.class);
                it.putExtra(PinHuoNewOveredActivity.EXTRA_ID, item1.CategoryID);
                it.putExtra(PinHuoNewOveredActivity.EXTRA_Name, item1.CategoryName);
                it.putExtra(PinHuoNewOveredActivity.EXTRA_QSID, item1.QsID);
                mContext.startActivity(it);

               /* PinHuoModel item1 = (PinHuoModel) v.getTag();
                ViewUtil.gotoChangci(mContext, item1);*/
                break;
            case R.id.btn_pin:
                PinHuoModel model = (PinHuoModel) v.getTag();
                ViewUtil.gotoChangci(mContext, model);
//                final PinHuoModel item = (PinHuoModel)v.getTag();
//                QuickSaleApi.saveFocus(mContext, mRequestHelper, new HttpRequestListener() {
//                    @Override
//                    public void onRequestStart(String method) {
//                        if (item.isFocus) {
//                            mLoadingDialog.start("取消关注中...");
//                        } else {
//                            mLoadingDialog.start("关注中...");
//                        }
//                    }
//                    @Override
//                    public void onRequestSuccess(String method, Object object) {
//                        mLoadingDialog.stop();
//                        item.isFocus = !item.isFocus;
//                        notifyDataSetChanged();
//                        if (item.isFocus) {
//                            ViewHub.showLongToast(mContext,"已成功关注");
//                        } else {
//                            ViewHub.showLongToast(mContext,"已取消关注");
//                            if (vThis.listener!=null) {
//                                vThis.listener.focusStatChanged();
//                            }
//                        }
//                    }
//
//                    @Override
//                    public void onRequestFail(String method, int statusCode, String msg) {
//                        mLoadingDialog.stop();
//                        ViewHub.showShortToast(mContext, msg);
//                    }
//
//                    @Override
//                    public void onRequestExp(String method, String msg, ResultData data) {
//                        mLoadingDialog.stop();
//                        ViewHub.showShortToast(mContext, msg);
//                    }
//                }, item.getShopID());
                break;
            case R.id.btn_focus:
                //关注，取消关注
                final PinHuoModel item = (PinHuoModel) v.getTag();
                QuickSaleApi.saveFocus(mContext, mRequestHelper, new HttpRequestListener() {
                    @Override
                    public void onRequestStart(String method) {
                        if (item.isFocus) {
                            mLoadingDialog.start("取消关注中...");
                        } else {
                            mLoadingDialog.start("关注中...");
                        }
                    }

                    @Override
                    public void onRequestSuccess(String method, Object object) {
                        mLoadingDialog.stop();
                        item.isFocus = !item.isFocus;
                        notifyDataSetChanged();
                        if (item.isFocus) {
                            ViewHub.showLongToast(mContext, "已成功关注");
                        } else {
                            ViewHub.showLongToast(mContext, "已取消关注");
                            if (vThis.listener != null) {
                                vThis.listener.focusStatChanged();
                            }
                        }
                    }

                    @Override
                    public void onRequestFail(String method, int statusCode, String msg) {
                        mLoadingDialog.stop();
                        ViewHub.showShortToast(mContext, msg);
                    }

                    @Override
                    public void onRequestExp(String method, String msg, ResultData data) {
                        mLoadingDialog.stop();
                        ViewHub.showShortToast(mContext, msg);
                    }
                }, item.getShopID());


                break;
            case R.id.btn_overed: {// 本团介绍
                PinHuoModel item2 = (PinHuoModel) v.getTag();

                if (item2.Url.indexOf("/xiaozu/topic/") > 1) {
                    String temp = "/xiaozu/topic/";
                    int topicID = Integer.parseInt(item2.Url.substring(item2.Url.indexOf(temp) + temp.length()));
                    Intent intent = new Intent(mContext, PostDetailActivity.class);
                    intent.putExtra(PostDetailActivity.EXTRA_TID, topicID);
                    intent.putExtra(PostDetailActivity.EXTRA_POST_TYPE, Const.PostType.TOPIC);
                    mContext.startActivity(intent);
                } else if (item2.Url.indexOf("/xiaozu/act/") > 1) {
                    String temp = "/xiaozu/act/";
                    int actID = Integer.parseInt(item2.Url.substring(item2.Url.indexOf(temp) + temp.length()));

                    Intent intent = new Intent(mContext, PostDetailActivity.class);
                    intent.putExtra(PostDetailActivity.EXTRA_TID, actID);
                    intent.putExtra(PostDetailActivity.EXTRA_POST_TYPE, Const.PostType.ACTIVITY);
                    mContext.startActivity(intent);
                } else {
                    Intent intent = new Intent(mContext, ItemPreview1Activity.class);
                    intent.putExtra("name", "拼货预告");
                    intent.putExtra("url", item2.Url);
                    mContext.startActivity(intent);
                }
                break;
            }
            default:
                PinHuoModel model1 = (PinHuoModel) v.getTag(R.id.item);
                HashMap<String,String> hashMap=new HashMap<>();
                hashMap.put("type", BWApplication.PINHUPTITLE+"-"+model1.Name);
                UMengTestUtls.UmengOnClickEvent(mContext, UmengClick.Click37,hashMap);
                long isStartDuration = model1.getStartMillis() - System.currentTimeMillis();
                if (model1.isStart) {
                    ViewUtil.gotoChangci(mContext, model1);
                } else {
                    if (isStartDuration > 0) {//预告
                        if (model1.QsID != 0) {
                            ViewUtil.gotoChangci(mContext, model1);
                        } else {
                            if (model1.Url.indexOf("/xiaozu/topic/") > 1) {
                                String temp = "/xiaozu/topic/";
                                int topicID = Integer.parseInt(model1.Url.substring(model1.Url.indexOf(temp) + temp.length()));

                                Intent intent = new Intent(mContext, PostDetailActivity.class);
                                intent.putExtra(PostDetailActivity.EXTRA_TID, topicID);
                                intent.putExtra(PostDetailActivity.EXTRA_POST_TYPE, Const.PostType.TOPIC);
                                mContext.startActivity(intent);
                            } else if (model1.Url.indexOf("/xiaozu/act/") > 1) {
                                String temp = "/xiaozu/act/";
                                int actID = Integer.parseInt(model1.Url.substring(model1.Url.indexOf(temp) + temp.length()));

                                Intent intent = new Intent(mContext, PostDetailActivity.class);
                                intent.putExtra(PostDetailActivity.EXTRA_TID, actID);
                                intent.putExtra(PostDetailActivity.EXTRA_POST_TYPE, Const.PostType.ACTIVITY);
                                mContext.startActivity(intent);
                            } else {
                                Intent intent = new Intent(mContext, ItemPreview1Activity.class);
                                intent.putExtra("name", "拼货预告");
                                intent.putExtra("url", model1.Url);
                                mContext.startActivity(intent);
                            }
                        }

                    } else {
                        ViewUtil.gotoChangci(mContext, model1);
                    }
                }
                break;
        }
    }

    @Override
    protected void convert(BaseViewHolder helper,final PinHuoModel item) {
        View  convertView=helper.getConvertView();
        View layout_need_hide = helper.getView(R.id.layout_need_hide);
        ItemJCVideoPlayerStandard jcVideoPlayer = (ItemJCVideoPlayerStandard) helper.getView(R.id.video_player);
        ImageView ivCover = (ImageView) helper.getView(R.id.iv_cover);
        final ImageView ivCoverPining = (ImageView) helper.getView(R.id.iv_cover_pining);// 正在拼
        ImageView ivCoverNewItem = (ImageView) helper.getView(R.id.iv_new_item_icon);
        ImageView video_icon = (ImageView) helper.getView(R.id.video_icon);
        ImageView video_hide = (ImageView) helper.getView(R.id.video_hide);
        final ImageView ivCoverEnded = (ImageView) helper.getView(R.id.iv_cover_ended);//已结束
        ImageView iv_cover_yg = (ImageView) helper.getView(R.id.iv_cover_yg);//已结束
        final View cover_end_view = helper.getView(R.id.iv_cover_ended_view);
        TextView tvTitle = (TextView) helper.getView(R.id.tv_title);// 内容标题
        TextView txt_next_yg = (TextView) helper.getView(R.id.txt_next_yg);// 内容标题
        PinHuoTextView tvContent = (PinHuoTextView) helper.getView(R.id.tv_content);// 内容说明
        View mTvOvered = helper.getView(R.id.tv_overed);
        RelativeLayout rl_bottom = (RelativeLayout) helper.getView(R.id.rl_bottom);// 底部栏
        RelativeLayout rl_wai = (RelativeLayout) helper.getView(R.id.rl_wai);
        RelativeLayout rl_li = (RelativeLayout) helper.getView(R.id.rl_li);
        final Button btnPin = (Button) helper.getView(R.id.btn_pin);
        final Button btnOvered = (Button) helper.getView(R.id.btn_overed);
        final  TextView mTvHH = (TextView) helper.getView(R.id.tv_hh);
        final TextView mTvH = (TextView) helper.getView(R.id.tv_h);
        final TextView mTvMM = (TextView) helper.getView(R.id.tv_mm);
        final TextView mTvM = (TextView) helper.getView(R.id.tv_m);
        final TextView mTvSS = (TextView) helper.getView(R.id.tv_ss);
        final  TextView mTvS = (TextView) helper.getView(R.id.tv_s);
        final TextView mTvF = (TextView) helper.getView(R.id.tv_f);
        final TextView mTvDay = (TextView) helper.getView(R.id.tv_day);
        final TextView mTvDayDesc = (TextView) helper.getView(R.id.tv_day_desc);
        final  TextView tvTime = (TextView) helper.getView(R.id.overed_text);
        final View open_view = helper.getView(R.id.open_textview);
        final View tips_view = helper.getView(R.id.tips_view);// 倒计时框
        Button btn_focus = (Button) helper.getView(R.id.btn_focus);
        btnPin.setOnClickListener(this);
//             ivCover.setOnClickListener(this);
        btnOvered.setOnClickListener(this);
        mTvOvered.setOnClickListener(this);
        btn_focus.setOnClickListener(this);
        convertView.setOnClickListener(this);
        tvContent.setLetterSpacing(2);
        Glide.with(mContext)
                .load(ImageUrlExtends.HTTP_BANWO_FILES+"/img/xinkuan.png")
                .apply(options)
                .into( ivCoverNewItem);
        Glide.with(mContext)
                .load(ImageUrlExtends.HTTP_BANWO_FILES+"/img/yugao.png")
                .apply(options)
                .into( iv_cover_yg);
        String url = ImageUrlExtends.getImageUrl(item.AppCover, 20);
        // Picasso.with(mContext).load(url).placeholder(R.drawable.empty_photo).into( ivCover);
        int width= ScreenUtils.getScreenWidth(mContext);
        int bottom_margin=ScreenUtils.dip2px(mContext,15);
        LinearLayout.LayoutParams  wai_layoutParams=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ScreenUtils.getPinHuoProportionHeight(width)+bottom_margin);
        RelativeLayout.LayoutParams  li_layoutParams=new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ScreenUtils.getPinHuoProportionHeight(width));
        rl_wai.setLayoutParams(wai_layoutParams);
        rl_li.setLayoutParams(li_layoutParams);
        Glide.with(mContext)
                .load(url)
                .apply(options1)
                .into( ivCover);

        tvTitle.setText(item.Name);
        btnPin.setTag(item);
        btnOvered.setTag(item);
        btnOvered.setVisibility(View.GONE);
        mTvOvered.setTag(item);
        convertView.setTag(R.id.item, item);
//         ivCover.setTag(R.id.item, item);
        tvContent.setText(item.Description);
        video_icon.setVisibility(View.GONE);
        video_hide.setVisibility(View.GONE);
        //item.setVideo("http://nahuo-video-server.b0.upaiyun.com/0/171016/131525960265864128.mp4");
        if (!TextUtils.isEmpty(item.getVideo())) {
            jcVideoPlayer.setVisibility(View.VISIBLE);

        } else {
            jcVideoPlayer.setVisibility(View.GONE);
        }
//        if (!TextUtils.isEmpty(item.getVideo())&&item.VideoPlayer_Is_Hide) {
//             video_icon.setVisibility(View.VISIBLE);
//             jcVideoPlayer.setVisibility(View.GONE);
//             jcVideoPlayer.onCompletion();
//             video_hide.setVisibility(View.GONE);
//
//        } else if (TextUtils.isEmpty(item.getVideo())){
//             video_icon.setVisibility(View.GONE);
//             jcVideoPlayer.setVisibility(View.GONE);
//             jcVideoPlayer.onCompletion();
//             video_hide.setVisibility(View.GONE);
//        }else if (!TextUtils.isEmpty(item.getVideo())&&!item.VideoPlayer_Is_Hide){
//            if (!isPause)
//             jcVideoPlayer.startVideo();
//             jcVideoPlayer.setVisibility(View.VISIBLE);
//             video_icon.setVisibility(View.GONE);
//             video_hide.setVisibility(View.VISIBLE);
//           // ViewHub.showShortToast(mContext,"xxx");
//        }
        //Picasso.with(mContext).load(url).placeholder(R.drawable.empty_photo).into( jcVideoPlayer.thumbImageView);
        // jcVideoPlayer.thumbImageView.setScaleType(ImageView.ScaleType.FIT_XY);
        Glide.with(mContext)
                .load(url)
                .apply(options1)
                .into( jcVideoPlayer.thumbImageView);
        jcVideoPlayer.setUp(item.getVideo()
                , JCVideoPlayer.SCREEN_LAYOUT_LIST
                , item.Name, item,  layout_need_hide);
//         video_icon.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                item.VideoPlayer_Is_Hide=false;
//                notifyDataSetChanged();
//            }
//        });
//         video_hide.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                item.VideoPlayer_Is_Hide=true;
//                notifyDataSetChanged();
//            }
//        });

        if (item.isPicAd()) {
            ivCoverEnded.setVisibility(View.GONE);
            ivCoverPining.setVisibility(View.GONE);
            tvTitle.setVisibility(View.GONE);
            tvContent.setVisibility(View.GONE);
            rl_bottom.setVisibility(View.GONE);
            tips_view.setVisibility(View.GONE);
        } else {
            tvTitle.setVisibility(View.VISIBLE);
            tvContent.setVisibility(View.VISIBLE);
            if (item.ShowMenu) {
                rl_bottom.setVisibility(View.VISIBLE);
            } else {
                rl_bottom.setVisibility(View.GONE);
            }
            //  rl_bottom.setVisibility(View.VISIBLE);
            btnOvered.setVisibility(View.GONE);
            txt_next_yg.setVisibility(View.GONE);
            tips_view.setVisibility(View.GONE);
            ivCoverNewItem.setVisibility(View.GONE);
            btnPin.setVisibility(View.GONE);
            btnOvered.setVisibility(View.GONE);
            cover_end_view.setVisibility(View.GONE);
            tvTime.setVisibility(View.GONE);
            ivCoverEnded.setVisibility(View.GONE);
            iv_cover_yg.setVisibility(View.GONE);
            open_view.setVisibility(View.GONE);

//            if (item.isStart() && !item.isHasNewItems()) {//预告状态
//                 txt_next_yg.setText("下场明天"+item.getTimes()+"点开拼");
//                 txt_next_yg.setVisibility(View.VISIBLE);
//                //右上角绿色图标   没这玩意
//                //底部按钮
//                 btnOvered.setVisibility(View.VISIBLE);
//            } else
            if (item.isStart()) {//开始状态
                if (item.getActivityType().equals("拼货")) {
                    //普通场
                    if (item.getOpenStatu().getStatu().equals("预告")) {
                        //预计文案
                        open_view.setVisibility(View.GONE);
                        tips_view.setVisibility(View.VISIBLE);
                        tips_view.setBackgroundResource(R.drawable.bg_rectangle_green);
                        tvTime.setVisibility(View.VISIBLE);
                        tvTime.setText(item.getOpenStatu().getContent());
                        //右上角绿色图标
                        iv_cover_yg.setVisibility(View.VISIBLE);
                        //底部按钮 本团介绍帖子
                        btnOvered.setVisibility(View.VISIBLE);
                        btnOvered.setBackgroundResource(R.drawable.bg_rectangle_green);
                        cancelCountDown(convertView);
                    } else if (item.getOpenStatu().getStatu().equals("开拼中")) {
                        //倒计时
                        open_view.setVisibility(View.VISIBLE);
                        tips_view.setVisibility(View.VISIBLE);
                        //updateTime(holder, item);
                        //右上角新款图标
                        ivCoverNewItem.setVisibility(View.VISIBLE);
                        //进入本场\
                        btnPin.setVisibility(View.VISIBLE);
                        tips_view.setBackgroundResource(R.drawable.bg_rectangle_red2);
                        long isStartDuration = item.getStartMillis() - System.currentTimeMillis();
                        long isEndDuration = item.getEndMillis() - System.currentTimeMillis();
                        if (isStartDuration == 0l || isEndDuration == 0l) {//TODO时间截止时，刷新
                            EventBus.getDefault().post(BusEvent.getEvent(EventBusId.REFRESH_COMPLETEd));
                        }
                        if (item.IsStart) {//处于拼货期间
                            long pihuoTime =isEndDuration+ SystemClock.elapsedRealtime();
                            if (pihuoTime > 0) {
                                if (mCountDownTask != null) {
                                    mCountDownTask.until(convertView, pihuoTime,
                                            100, new CountDownTimers.OnCountDownListener() {
                                                @Override
                                                public void onTick(View view, long millisUntilFinished) {
                                                    //d拼货计时开始
                                                    long isEndDuration = millisUntilFinished;
                                                    int hour = (int) (isEndDuration / TimeUtils.HOUR_MILLIS);
                                                    int min = (int) ((isEndDuration - hour * TimeUtils.HOUR_MILLIS) / TimeUtils.MINUTE_MILLIS);
                                                    int sec = (int) ((isEndDuration - hour * TimeUtils.HOUR_MILLIS - min * TimeUtils.MINUTE_MILLIS) / TimeUtils.SECOND_MILLIS);
                                                    int milli = (int) (isEndDuration - hour * TimeUtils.HOUR_MILLIS - min * TimeUtils.MINUTE_MILLIS - sec * TimeUtils.SECOND_MILLIS);
                                                    if (hour < 24) {//小于一天
                                                        mTvDay.setVisibility(View.GONE);
                                                        mTvDayDesc.setVisibility(View.GONE);
                                                    } else {
                                                        mTvDay.setVisibility(View.VISIBLE);
                                                        mTvDayDesc.setVisibility(View.VISIBLE);
                                                        mTvDay.setText((hour / 24) + "");
                                                    }
                                                    int hour_show = hour % 24;
                                                    mTvHH.setText((hour_show / 10) + "");
                                                    mTvH.setText((hour_show % 10) + "");
                                                    mTvMM.setText((min / 10) + "");
                                                    mTvM.setText((min % 10) + "");
                                                    mTvSS.setText((sec / 10) + "");
                                                    mTvS.setText((sec % 10) + "");
                                                    mTvF.setText(milli / 100 + "");
                                                }

                                                @Override
                                                public void onFinish(View view) {
                                                    //拼货结束
                                                    ivCoverPining.setVisibility(View.GONE);
                                                    cover_end_view.setVisibility(View.VISIBLE);
                                                    ivCoverEnded.setVisibility(View.VISIBLE);
                                                    btnOvered.setVisibility(View.GONE);
                                                    btnPin.setVisibility(View.GONE);
                                                    tips_view.setBackgroundResource(R.drawable.bg_rectangle_gray1);
                                                    open_view.setVisibility(View.GONE);
                                                    tvTime.setVisibility(View.VISIBLE);
                                                    tvTime.setText(TimeUtils.millisToTimestamp(item.getEndMillis(), "MM月dd日 HH:mm:ss") + " 拼货结束");

                                                }
                                            });
                                }
                            }else {
                                //拼货结束
                                ivCoverPining.setVisibility(View.GONE);
                                cover_end_view.setVisibility(View.VISIBLE);
                                ivCoverEnded.setVisibility(View.VISIBLE);
                                btnOvered.setVisibility(View.GONE);
                                btnPin.setVisibility(View.GONE);
                                tips_view.setBackgroundResource(R.drawable.bg_rectangle_gray1);
                                open_view.setVisibility(View.GONE);
                                tvTime.setVisibility(View.VISIBLE);
                                tvTime.setText(TimeUtils.millisToTimestamp(item.getEndMillis(), "MM月dd日 HH:mm:ss") + " 拼货结束");
                            }
                        }else {
                            //拼货结束
                            ivCoverPining.setVisibility(View.GONE);
                            cover_end_view.setVisibility(View.VISIBLE);
                            ivCoverEnded.setVisibility(View.VISIBLE);
                            btnOvered.setVisibility(View.GONE);
                            btnPin.setVisibility(View.GONE);
                            tips_view.setBackgroundResource(R.drawable.bg_rectangle_gray1);
                            open_view.setVisibility(View.GONE);
                            tvTime.setVisibility(View.VISIBLE);
                            tvTime.setText(TimeUtils.millisToTimestamp(item.getEndMillis(), "MM月dd日 HH:mm:ss") + " 拼货结束");
                        }
                    } else if (item.getOpenStatu().getStatu().equals("已结束")) {
                        //本场已结束
                        cover_end_view.setVisibility(View.VISIBLE);
                        open_view.setVisibility(View.GONE);
                        tips_view.setVisibility(View.VISIBLE);
                        tips_view.setBackgroundResource(R.drawable.bg_rectangle_gray1);
                        tvTime.setVisibility(View.VISIBLE);
                        tvTime.setText(item.getOpenStatu().getContent());
                        //右上角结束图标  大邮戳没有， 只有小图
                        ivCoverEnded.setVisibility(View.VISIBLE);
                        //底部按钮，本团介绍帖子
                        btnOvered.setVisibility(View.VISIBLE);
                        btnOvered.setBackgroundResource(R.drawable.bg_rectangle_gray1);
                        cancelCountDown(convertView);
                    }
                } else {//聚合场，按照目前逻辑不变
                    btnPin.setVisibility(View.VISIBLE);
                    ivCoverNewItem.setVisibility(View.GONE);
                    tips_view.setVisibility(View.GONE);
                    cancelCountDown(convertView);
                }
            }
//            else {//结束状态
//                //本场已结束
//                 cover_end_view.setVisibility(View.VISIBLE);
//                 tips_view.setBackgroundResource(R.drawable.bg_rectangle_gray1);
//                 tvTime.setVisibility(View.VISIBLE);
//                 tvTime.setText("本场已结束");
//                //右上角结束大邮戳  没这玩意
//                //底部按钮本团介绍帖子
//                 btnOvered.setVisibility(View.VISIBLE);
//            }

            txt_next_yg.setVisibility(item.getTimes() > 0 ? View.VISIBLE : View.GONE);
            txt_next_yg.setText("第" + item.getTimes() + "期");
            mTvOvered.setVisibility(item.isShowHistory() ? View.VISIBLE : View.GONE);
            btn_focus.setVisibility(View.VISIBLE);
            //我的关注
            btn_focus.setTag(item);
            if (item.isFocus) {
                btn_focus.setBackgroundResource(R.drawable.bg_rectangle_grayx);
                btn_focus.setTextColor(mContext.getResources().getColor(R.color.gray_92));
                btn_focus.setText("取消关注");
            } else {
                btn_focus.setBackgroundResource(R.drawable.bg_rectangle_red1);
                btn_focus.setTextColor(Color.parseColor("#e03e3f"));
                btn_focus.setText("关注本团");
            }
        }
    }

    public interface PinHuoNewListener {
         void focusStatChanged();
    }

    private PinHuoNewListener listener;

    public void setListener(PinHuoNewListener listener) {
        this.listener = listener;
    }
}
