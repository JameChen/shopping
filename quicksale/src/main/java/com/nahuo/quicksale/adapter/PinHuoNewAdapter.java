package com.nahuo.quicksale.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.nahuo.library.controls.LoadingDialog;
import com.nahuo.library.helper.GsonHelper;
import com.nahuo.library.helper.ImageUrlExtends;
import com.nahuo.library.utils.TimeUtils;
import com.nahuo.quicksale.ItemPreviewActivity;
import com.nahuo.quicksale.PinHuoNewOveredActivity;
import com.nahuo.quicksale.R;
import com.nahuo.quicksale.Topic.PostDetailActivity;
import com.nahuo.quicksale.ViewHub;
import com.nahuo.quicksale.api.HttpRequestHelper;
import com.nahuo.quicksale.api.HttpRequestListener;
import com.nahuo.quicksale.api.QuickSaleApi;
import com.nahuo.quicksale.common.Const;
import com.nahuo.quicksale.common.ViewUtil;
import com.nahuo.quicksale.eventbus.BusEvent;
import com.nahuo.quicksale.eventbus.EventBusId;
import com.nahuo.quicksale.oldermodel.PinHuoModel;
import com.nahuo.quicksale.oldermodel.ResultData;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

public class PinHuoNewAdapter extends MyBaseAdapter<PinHuoModel> implements OnClickListener {

    private PinHuoNewAdapter vThis = this;
    private Context mContext;
    private LoadingDialog mLoadingDialog;

    public PinHuoNewAdapter(Context context) {
        super(context);
        mContext = context;
        mLoadingDialog = new LoadingDialog(mContext);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.lvitem_pin_huo_pining_new, parent, false);
            holder = new ViewHolder();
            holder.ivCover = (ImageView) convertView.findViewById(R.id.iv_cover);
            holder.ivCoverPining = (ImageView) convertView.findViewById(R.id.iv_cover_pining);// 正在拼
            holder.ivCoverNewItem = (ImageView) convertView.findViewById(R.id.iv_new_item_icon);
            holder.ivCoverEnded = (ImageView) convertView.findViewById(R.id.iv_cover_ended);//已结束
            holder.iv_cover_yg = (ImageView) convertView.findViewById(R.id.iv_cover_yg);//已结束
            holder.cover_end_view = convertView.findViewById(R.id.iv_cover_ended_view);
            holder.tvTitle = (TextView) convertView.findViewById(R.id.tv_title);// 内容标题
            holder.txt_next_yg = (TextView) convertView.findViewById(R.id.txt_next_yg);// 内容标题
            holder.tvContent = (TextView) convertView.findViewById(R.id.tv_content);// 内容说明
            holder.mTvOvered = convertView.findViewById(R.id.tv_overed);
            holder.rl_bottom = (RelativeLayout) convertView.findViewById(R.id.rl_bottom);// 底部栏
            holder.btnPin = (Button) convertView.findViewById(R.id.btn_pin);
            holder.btnOvered = (Button) convertView.findViewById(R.id.btn_overed);
            holder.mTvHH = (TextView) convertView.findViewById(R.id.tv_hh);
            holder.mTvH = (TextView) convertView.findViewById(R.id.tv_h);
            holder.mTvMM = (TextView) convertView.findViewById(R.id.tv_mm);
            holder.mTvM = (TextView) convertView.findViewById(R.id.tv_m);
            holder.mTvSS = (TextView) convertView.findViewById(R.id.tv_ss);
            holder.mTvS = (TextView) convertView.findViewById(R.id.tv_s);
//            holder.mTvF = (TextView) convertView.findViewById(R.id.tv_f);
            holder.mTvDay = (TextView) convertView.findViewById(R.id.tv_day);
            holder.mTvDayDesc = (TextView) convertView.findViewById(R.id.tv_day_desc);
            holder.tvTime = (TextView) convertView.findViewById(R.id.overed_text);
            holder.open_view = convertView.findViewById(R.id.open_textview);
            holder.tips_view = convertView.findViewById(R.id.tips_view);// 倒计时框
            holder.btnPin.setOnClickListener(this);
//            holder.ivCover.setOnClickListener(this);
            holder.btnOvered.setOnClickListener(this);
            holder.mTvOvered.setOnClickListener(this);
            convertView.setOnClickListener(this);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        PinHuoModel item = mdata.get(position);
        String url = ImageUrlExtends.getImageUrl(item.AppCover, 20);
        Picasso.with(mContext).load(url).placeholder(R.drawable.empty_photo).into(holder.ivCover);
        holder.tvTitle.setText(item.Name);
        holder.btnPin.setTag(item);
        holder.btnOvered.setTag(item);
        holder.btnOvered.setVisibility(View.GONE);
        holder.mTvOvered.setTag(item);
        convertView.setTag(R.id.item, item);
//        holder.ivCover.setTag(R.id.item, item);
        holder.tvContent.setText(item.Description);
        if (item.isPicAd()) {
            holder.ivCoverEnded.setVisibility(View.GONE);
            holder.ivCoverPining.setVisibility(View.GONE);
            holder.tvTitle.setVisibility(View.GONE);
            holder.tvContent.setVisibility(View.GONE);
            holder.rl_bottom.setVisibility(View.GONE);
            holder.tips_view.setVisibility(View.GONE);
        } else {
            holder.tvTitle.setVisibility(View.VISIBLE);
            holder.tvContent.setVisibility(View.VISIBLE);
            holder.rl_bottom.setVisibility(View.VISIBLE);

            holder.btnOvered.setVisibility(View.GONE);
            holder.txt_next_yg.setVisibility(View.GONE);
            holder.tips_view.setVisibility(View.GONE);
            holder.ivCoverNewItem.setVisibility(View.GONE);
            holder.btnPin.setVisibility(View.GONE);
            holder.btnOvered.setVisibility(View.GONE);
            holder.cover_end_view.setVisibility(View.GONE);
            holder.tvTime.setVisibility(View.GONE);
            holder.ivCoverEnded.setVisibility(View.GONE);
            holder.iv_cover_yg.setVisibility(View.GONE);
            holder.open_view.setVisibility(View.GONE);

//            if (item.isStart() && !item.isHasNewItems()) {//预告状态
//                holder.txt_next_yg.setText("下场明天"+item.getTimes()+"点开拼");
//                holder.txt_next_yg.setVisibility(View.VISIBLE);
//                //右上角绿色图标   没这玩意
//                //底部按钮
//                holder.btnOvered.setVisibility(View.VISIBLE);
//            } else
            if (item.isStart()) {//开始状态
                if (item.getActivityType().equals("拼货")) {
                    //普通场
                    if (item.getOpenStatu().getStatu().equals("预告")) {
                        //预计文案
                        holder.open_view.setVisibility(View.GONE);
                        holder.tips_view.setVisibility(View.VISIBLE);
                        holder.tips_view.setBackgroundResource(R.drawable.bg_rectangle_green);
                        holder.tvTime.setVisibility(View.VISIBLE);
                        holder.tvTime.setText(item.getOpenStatu().getContent());
                        //右上角绿色图标
                        holder.iv_cover_yg.setVisibility(View.VISIBLE);
                        //底部按钮 本团介绍帖子
                        holder.btnOvered.setVisibility(View.VISIBLE);
                        holder.btnOvered.setBackgroundResource(R.drawable.bg_rectangle_green);

                    } else if (item.getOpenStatu().getStatu().equals("开拼中")) {
                        //倒计时
                        holder.open_view.setVisibility(View.VISIBLE);
                        holder.tips_view.setVisibility(View.VISIBLE);
                        updateTime(holder, item);
                        //右上角新款图标
                        holder.ivCoverNewItem.setVisibility(View.VISIBLE);
                        //进入本场\
                        holder.btnPin.setVisibility(View.VISIBLE);
                        holder.tips_view.setBackgroundResource(R.drawable.bg_rectangle_red2);

                    } else if (item.getOpenStatu().getStatu().equals("已结束")) {
                        //本场已结束
                        holder.cover_end_view.setVisibility(View.VISIBLE);
                        holder.open_view.setVisibility(View.GONE);
                        holder.tips_view.setVisibility(View.VISIBLE);
                        holder.tips_view.setBackgroundResource(R.drawable.bg_rectangle_gray1);
                        holder.tvTime.setVisibility(View.VISIBLE);
                        holder.tvTime.setText(item.getOpenStatu().getContent());
                        //右上角结束图标  大邮戳没有， 只有小图
                        holder.ivCoverEnded.setVisibility(View.VISIBLE);
                        //底部按钮，本团介绍帖子
                        holder.btnOvered.setVisibility(View.VISIBLE);
                        holder.btnOvered.setBackgroundResource(R.drawable.bg_rectangle_gray1);
                    }
                } else {//聚合场，按照目前逻辑不变
                    holder.btnPin.setVisibility(View.VISIBLE);
                    holder.ivCoverNewItem.setVisibility(View.GONE);
                    holder.tips_view.setVisibility(View.GONE);
                }
            }
//            else {//结束状态
//                //本场已结束
//                holder.cover_end_view.setVisibility(View.VISIBLE);
//                holder.tips_view.setBackgroundResource(R.drawable.bg_rectangle_gray1);
//                holder.tvTime.setVisibility(View.VISIBLE);
//                holder.tvTime.setText("本场已结束");
//                //右上角结束大邮戳  没这玩意
//                //底部按钮本团介绍帖子
//                holder.btnOvered.setVisibility(View.VISIBLE);
//            }

            holder.txt_next_yg.setVisibility(item.getTimes()>0?View.VISIBLE:View.GONE);
            holder.txt_next_yg.setText("第"+item.getTimes()+"期");
            holder.mTvOvered.setVisibility(item.isShowHistory()?View.VISIBLE:View.GONE);

//            holder.btnPin.setTag(item);
//            if (item.isFocus) {
//                holder.btnPin.setBackgroundResource(R.drawable.btn_black_stroke_white_solid);
//                holder.btnPin.setTextColor(mContext.getResources().getColor(R.color.gray_92));
//                holder.btnPin.setText("取消关注");
//            } else {
//                holder.btnPin.setBackgroundResource(R.drawable.btn_red_stroke_white_solid);
//                holder.btnPin.setTextColor(mContext.getResources().getColor(R.color.red));
//                holder.btnPin.setText("关注本团");
//            }
        }

        return convertView;
    }

    private void updateTime(ViewHolder holder, PinHuoModel item) {
        long isStartDuration = item.getStartMillis() - System.currentTimeMillis();
        long isEndDuration = item.getEndMillis() - System.currentTimeMillis();
        if (isStartDuration == 0l || isEndDuration == 0l) {//TODO时间截止时，刷新
            EventBus.getDefault().post(BusEvent.getEvent(EventBusId.REFRESH_COMPLETEd));
        }
        if (item.IsStart) {//处于拼货期间
//            holder.ivCoverPining.setVisibility(View.GONE);
//            holder.cover_end_view.setVisibility(View.GONE);
//            holder.ivCoverEnded.setVisibility(View.GONE);
//            holder.btnOvered.setVisibility(View.GONE);
//            holder.btnPin.setVisibility(View.VISIBLE);
//            holder.tips_view.setBackgroundResource(R.drawable.bg_rectangle_red2);
//            holder.open_view.setVisibility(View.VISIBLE);
//            holder.tvTime.setVisibility(View.GONE);
            int hour = (int) (isEndDuration / TimeUtils.HOUR_MILLIS);
            int min = (int) ((isEndDuration - hour * TimeUtils.HOUR_MILLIS) / TimeUtils.MINUTE_MILLIS);
            int sec = (int) ((isEndDuration - hour * TimeUtils.HOUR_MILLIS - min * TimeUtils.MINUTE_MILLIS) / TimeUtils.SECOND_MILLIS);
//            int milli = (int) (isEndDuration - hour * TimeUtils.HOUR_MILLIS - min * TimeUtils.MINUTE_MILLIS - sec * TimeUtils.SECOND_MILLIS);
            if (hour < 24) {//小于一天
                holder.mTvDay.setVisibility(View.GONE);
                holder.mTvDayDesc.setVisibility(View.GONE);
            } else {
                holder.mTvDay.setVisibility(View.VISIBLE);
                holder.mTvDayDesc.setVisibility(View.VISIBLE);
                holder.mTvDay.setText((hour / 24) + "");
            }

            int hour_show = hour % 24;
            holder.mTvHH.setText((hour_show / 10) + "");
            holder.mTvH.setText((hour_show % 10) + "");
            holder.mTvMM.setText((min / 10) + "");
            holder.mTvM.setText((min % 10) + "");
            holder.mTvSS.setText((sec / 10) + "");
            holder.mTvS.setText((sec % 10) + "");
//            holder.mTvF.setText(milli/100+"");

        } else if (isStartDuration > 0) {//展示预告
//            holder.ivCoverPining.setVisibility(View.GONE);
//            holder.cover_end_view.setVisibility(View.VISIBLE);
//            holder.ivCoverEnded.setVisibility(View.VISIBLE);
//            holder.btnOvered.setVisibility(View.GONE);
//            holder.btnPin.setVisibility(View.GONE);
//            holder.tips_view.setBackgroundResource(R.drawable.bg_rectangle_green);
//            holder.open_view.setVisibility(View.GONE);
//            holder.tvTime.setVisibility(View.VISIBLE);

//            int hour = (int) (item.getStartMillis() / TimeUtils.HOUR_MILLIS);
//            /*
//            *、超过2天时，显示“下期XX月XX日XX开拼”
//2、超过1天少于2天时，显示“下期明天XX点开拼”
//3、少于24小时时，显示“下期今天XX点开拼”
//            * */
//            if(hour>48)
//            {
//                holder.tvTime.setText("下一场"+TimeUtils.millisToTimestamp(item.getStartMillis(), "MM月dd日HH点")+"开拼");
//            } else if(hour<48 && hour>24) {
//                holder.tvTime.setText("下一场明天"+TimeUtils.millisToTimestamp(item.getStartMillis(), "HH点")+"开拼");
//            } else if (hour>0 && hour<24)
//            {
//                holder.tvTime.setText("下一场今天"+TimeUtils.millisToTimestamp(item.getStartMillis(), "HH点")+"开拼");
//            }
//            Date startDate = TimeUtils.timeStampToDate(item.StartTime, "yyyy-MM-dd HH:mm:ss");
//            Calendar startCal = Calendar.getInstance();
//            startCal.setTime(startDate);
//            int startYear = startCal.get(Calendar.YEAR);
//            int startMonth = startCal.get(Calendar.MONTH);
//            int startDay = startCal.get(Calendar.DATE);
//            int startHour = startCal.get(Calendar.HOUR);
//
//            Calendar cal = Calendar.getInstance();
//            int nowYear = cal.get(Calendar.YEAR);
//            int nowMonth = cal.get(Calendar.MONTH);
//            int nowDay = cal.get(Calendar.DATE);
//            int nowHour = cal.get(Calendar.HOUR);
//
//            if (startYear == nowYear && startMonth == nowMonth) {
//                if (startDay > nowDay) {
//                    if (startDay - nowDay < 1) {//今天
//                        holder.tvTime.setText("下一场今天" + TimeUtils.millisToTimestamp(item.getStartMillis(), "HH点") + "开拼");
//                        return;
//                    } else if (startDay - nowDay < 2) {//明天
//                        holder.tvTime.setText("下一场明天" + TimeUtils.millisToTimestamp(item.getStartMillis(), "HH点") + "开拼");
//                        return;
//                    }
//                }
//            }
//            holder.tvTime.setText("下一场" + TimeUtils.millisToTimestamp(item.getStartMillis(), "MM月dd日HH点") + "开拼");

        } else if (isEndDuration < 0) {//结束
//            holder.ivCoverPining.setVisibility(View.GONE);
//            holder.cover_end_view.setVisibility(View.VISIBLE);
//            holder.ivCoverEnded.setVisibility(View.VISIBLE);
//            holder.btnOvered.setVisibility(View.GONE);
//            holder.btnPin.setVisibility(View.GONE);
//            holder.tips_view.setBackgroundResource(R.drawable.bg_rectangle_gray1);
//            holder.open_view.setVisibility(View.GONE);
//            holder.tvTime.setVisibility(View.VISIBLE);
//            holder.tvTime.setText(TimeUtils.millisToTimestamp(item.getEndMillis(), "MM月dd日 HH:mm:ss") + " 拼货结束");
        }

    }

    public void getFocusStat(){
        List<String> s = new ArrayList<>();
        for (PinHuoModel p : mdata) {
            s.add(p.getShopID()+"");
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
                    List<Long> shopids  = GsonHelper.jsonToObject(object.toString(),
                            new TypeToken<List<Long>>() {});
                    for (PinHuoModel p : mdata) {
                        if (shopids.indexOf(p.getShopID())>-1) {
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
                it.putExtra(PinHuoNewOveredActivity.EXTRA_QSID,item1.QsID);
                mContext.startActivity(it);
                break;
            case R.id.btn_pin:
                PinHuoModel model = (PinHuoModel) v.getTag();
                ViewUtil.gotoChangci(mContext,model);
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
                    Intent intent = new Intent(mContext, ItemPreviewActivity.class);
                    intent.putExtra("name", "拼货预告");
                    intent.putExtra("url", item2.Url);
                    mContext.startActivity(intent);
                }
                break;
            }
            default:
                PinHuoModel model1 = (PinHuoModel) v.getTag(R.id.item);
                long isStartDuration = model1.getStartMillis() - System.currentTimeMillis();
                if (model1.isStart) {
                    ViewUtil.gotoChangci(mContext,model1);
                } else {
                    if (isStartDuration > 0) {//预告
                        if (model1.QsID != 0) {
                            ViewUtil.gotoChangci(mContext,model1);
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
                                Intent intent = new Intent(mContext, ItemPreviewActivity.class);
                                intent.putExtra("name", "拼货预告");
                                intent.putExtra("url", model1.Url);
                                mContext.startActivity(intent);
                            }
                        }

                    } else {
                        ViewUtil.gotoChangci(mContext,model1);
                    }
                }
                break;
        }
    }

    private static class ViewHolder {
        private ImageView ivCover, ivCoverEnded,iv_cover_yg, ivCoverPining,ivCoverNewItem;
        private View open_view, tips_view, cover_end_view, mTvOvered;
        private TextView tvTitle, tvContent, tvTime,txt_next_yg,
                mTvDay, mTvHH, mTvH, mTvMM, mTvM, mTvSS, mTvS, mTvF, mTvDayDesc;
        private Button btnPin, btnOvered;
        private RelativeLayout rl_bottom;
    }

    public interface PinHuoNewListener {
        public void focusStatChanged();
    }
    private PinHuoNewListener listener;
    public void setListener(PinHuoNewListener listener) {
        this.listener = listener;
    }
}
