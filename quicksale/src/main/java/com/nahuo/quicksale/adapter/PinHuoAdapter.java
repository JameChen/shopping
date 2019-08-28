package com.nahuo.quicksale.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.reflect.TypeToken;
import com.nahuo.library.controls.LoadingDialog;
import com.nahuo.library.helper.GsonHelper;
import com.nahuo.library.helper.ImageUrlExtends;
import com.nahuo.quicksale.R;
import com.nahuo.quicksale.ViewHub;
import com.nahuo.quicksale.api.HttpRequestHelper;
import com.nahuo.quicksale.api.HttpRequestListener;
import com.nahuo.quicksale.api.QuickSaleApi;
import com.nahuo.quicksale.common.ViewUtil;
import com.nahuo.quicksale.oldermodel.PinHuoModel;
import com.nahuo.quicksale.oldermodel.ResultData;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * 正在拼货
 * Created by ZZB on 2015/10/14.
 */
public class PinHuoAdapter extends MyBaseAdapter<PinHuoModel> implements OnClickListener{
    private RequestOptions options;
    private PinHuoAdapter vThis = this;
    public boolean overed = false;
    private Context mContext;
    private LoadingDialog mLoadingDialog;

    public PinHuoAdapter(Context context) {
        super(context);
        mContext = context;
        mLoadingDialog = new LoadingDialog(mContext);
        options = new RequestOptions()
                //.centerCrop()
                .placeholder(R.color.transparent)
                .error(R.color.transparent)
                .fallback(R.color.transparent)
                //.skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.ALL);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.lvitem_pin_huo_pining, parent, false);
            holder = new ViewHolder();
            holder.ivCoverNewItem = (ImageView) convertView.findViewById(R.id.iv_new_item_icon);
            holder.ivCover = (ImageView) convertView.findViewById(R.id.iv_cover);
            holder.tvTitle = (TextView) convertView.findViewById(R.id.tv_title);
            holder.tvContent = (TextView) convertView.findViewById(R.id.tv_content);
//            holder.tvTime = (TextView) convertView.findViewById(R.id.tv_time);
            holder.btnPin = (Button) convertView.findViewById(R.id.btn_pin);
            holder.iv_cover_yg = (ImageView) convertView.findViewById(R.id.iv_cover_yg);//已结束
            holder.ivCoverEnded = (ImageView) convertView.findViewById(R.id.iv_cover_ended);//已结束
            holder.txt_next_yg = (TextView) convertView.findViewById(R.id.txt_next_yg);// 内容标题
            holder.btn_focus=(Button)convertView.findViewById(R.id.btn_focus);
            holder.btn_focus.setOnClickListener(this);
            holder.btnPin.setOnClickListener(this);
            convertView.setOnClickListener(this);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        Glide.with(mContext)
                .load(ImageUrlExtends.HTTP_BANWO_FILES+"/img/xinkuan.png")
                .apply(options)
                .into(holder.ivCoverNewItem);
        Glide.with(mContext)
                .load(ImageUrlExtends.HTTP_BANWO_FILES+"/img/yugao.png")
                .apply(options)
                .into(holder.iv_cover_yg);
        PinHuoModel item = mdata.get(position);
        String url = ImageUrlExtends.getImageUrl(item.AppCover, 20);
        Picasso.with(mContext).load(url).placeholder(R.drawable.empty_photo).into(holder.ivCover);
        holder.tvTitle.setText(item.Name);
        holder.btnPin.setTag(item);
        convertView.setTag(R.id.item, item);
        holder.tvContent.setText(item.Description);

        holder.txt_next_yg.setVisibility(View.GONE);
        holder.ivCoverNewItem.setVisibility(View.GONE);
        holder.btnPin.setVisibility(View.GONE);
//        holder.tvTime.setVisibility(View.GONE);
        holder.ivCoverEnded.setVisibility(View.GONE);
        holder.iv_cover_yg.setVisibility(View.GONE);

        if (item.isStart()) {//开始状态
            if (item.getActivityType().equals("拼货")) {
                //普通场
                if (item.getOpenStatu().getStatu().equals("预告")) {
//                    holder.tvTime.setVisibility(View.VISIBLE);
//                    holder.tvTime.setText(item.getOpenStatu().getContent());
                    //右上角绿色图标
                    holder.iv_cover_yg.setVisibility(View.VISIBLE);

                } else if (item.getOpenStatu().getStatu().equals("开拼中")) {
                    //右上角新款图标
                    holder.ivCoverNewItem.setVisibility(View.VISIBLE);
                    //进入本场\
                    holder.btnPin.setVisibility(View.VISIBLE);
                } else if (item.getOpenStatu().getStatu().equals("已结束")) {
//                    holder.tvTime.setVisibility(View.VISIBLE);
//                    holder.tvTime.setText(item.getOpenStatu().getContent());
                    //右上角结束图标  大邮戳没有， 只有小图
                    holder.ivCoverEnded.setVisibility(View.VISIBLE);
                }
            } else {//聚合场，按照目前逻辑不变
                holder.btnPin.setVisibility(View.VISIBLE);
                holder.ivCoverNewItem.setVisibility(View.GONE);
            }
        }

        holder.txt_next_yg.setVisibility(item.getTimes()>0?View.VISIBLE:View.GONE);
        holder.txt_next_yg.setText("第"+item.getTimes()+"期");
        //我的关注
        holder.btn_focus.setVisibility(View.VISIBLE);
        holder.btn_focus.setTag(item);
        if (item.isFocus) {
            holder.btn_focus.setBackgroundResource(R.drawable.bg_rectangle_grayx);
            holder.btn_focus.setTextColor(mContext.getResources().getColor(R.color.gray_92));
            holder.btn_focus.setText("取消关注");
        } else {
            holder.btn_focus.setBackgroundResource(R.drawable.bg_rectangle_red1);
            holder.btn_focus.setTextColor(Color.parseColor("#e03e3f"));
            holder.btn_focus.setText("关注本团");
        }
        return convertView;
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
        switch (v.getId()){
            case R.id.btn_pin:
                PinHuoModel model1 = (PinHuoModel) v.getTag();
                ViewUtil.gotoChangci(mContext,model1);
//                final PinHuoModel item = (PinHuoModel)v.getTag();
//                QuickSaleApi.saveFocus(mContext, mRequestHelper, new HttpRequestListener() {
//                    @Override
//                    public void onRequestStart(String method) {
//if (item.isFocus) {
//    mLoadingDialog.start("取消关注中...");
//} else {
//    mLoadingDialog.start("关注中...");
//}
//                    }
//                    @Override
//                    public void onRequestSuccess(String method, Object object) {
//                        mLoadingDialog.stop();
//                        if (item.isFocus) {
//                            ViewHub.showLongToast(mContext,"已取消关注");
//                        } else {
//                            ViewHub.showLongToast(mContext,"已成功关注");
//                        }
//                        item.isFocus = !item.isFocus;
//                        notifyDataSetChanged();
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
            default:
//                PinHuoModel model = (PinHuoModel) v.getTag(R.id.item);
//                PinHuoDetailListActivity.launch(mContext, model,overed);

                PinHuoModel model = (PinHuoModel) v.getTag(R.id.item);
                ViewUtil.gotoChangci(mContext,model);
                break;
        }
    }
    public interface PinHuoNewListener {
        public void focusStatChanged();
    }

    private PinHuoNewListener listener;

    public void setListener(PinHuoNewListener listener) {
        this.listener = listener;
    }
    private static class ViewHolder{
        private ImageView ivCover,ivCoverNewItem,ivCoverEnded,iv_cover_yg;
        private TextView tvTitle, tvContent, tvTime,txt_next_yg;
        private Button btnPin,btn_focus;
    }
}
