package com.nahuo.quicksale.adapter;


import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.nahuo.library.helper.ImageUrlExtends;
import com.nahuo.quicksale.R;
import com.nahuo.quicksale.Topic.PostDetailActivity;
import com.nahuo.quicksale.common.Const;
import com.nahuo.quicksale.common.ListUtils;
import com.nahuo.quicksale.customview.PinHuoTextView;
import com.nahuo.quicksale.oldermodel.TopicInfoModel;
import com.nahuo.quicksale.util.GlideUtls;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class TopicPageAdapter extends BaseAdapter {

    public Context mContext;
    public List<TopicInfoModel> mList;
    private int mGridViewWidth;

    // 构造函数
    public TopicPageAdapter(Context Context, List<TopicInfoModel> dataList) {
        mContext = Context;
        mList = dataList;
    }

    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public TopicInfoModel getItem(int arg0) {
        return mList.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        return arg0;
    }

    private static int touchDownY = 0;
    private static int touchUpY = 0;

    @Override
    public View getView(final int arg0, View arg1, ViewGroup arg2) {

        final ViewHolder holder;
        View view = arg1;
        if (mList.size() > 0) {
            String cover = Const.getShopLogo(mList.get(arg0).getUserID());
            int postCount = mList.get(arg0).getPostCount();
            int viewedCount = mList.get(arg0).getViewCount();
            String title = mList.get(arg0).getTitle();
            if (view == null) {
                view = LayoutInflater.from(mContext).inflate(
                        R.layout.item_topic_page, arg2, false);
                holder = new ViewHolder();

                holder.cover = (ImageView) view
                        .findViewById(R.id.topic_page_item_cover);
                holder.title = (TextView) view
                        .findViewById(R.id.topic_page_item_name);
                holder.summary = (PinHuoTextView) view
                        .findViewById(R.id.topic_page_item_summary);
                holder.summary.setLetterSpacing(2);
                holder.username = (TextView) view
                        .findViewById(R.id.tv_username);
                holder.viewed = (TextView) view.findViewById(R.id.txt_topic_page_item_viewed);
                holder.postCount = (TextView) view
                        .findViewById(R.id.topic_page_item_postcount);
                holder.createTime = (TextView) view
                        .findViewById(R.id.topic_page_item_create);
                holder.gridview = (GridView) view.findViewById(R.id.gv_pics);
                holder.imageView = (ImageView) view.findViewById(R.id.iv_pic);
//                holder.gridview.setOnTouchListener(new View.OnTouchListener() {
//
//                    @Override
//                    public boolean onTouch(View arg0, MotionEvent arg1) {
//                        switch (arg1.getActionMasked()) {
//                            case MotionEvent.ACTION_UP:
//                                touchUpY = (int) arg1.getY();
//                                if (Math.abs(touchUpY - touchDownY) <= 10) {
//                                    gotoDetail(Integer.valueOf(arg0.getTag()
//                                            .toString()));
//                                }
//                                break;
//                            case MotionEvent.ACTION_DOWN:
//                                touchDownY = (int) arg1.getY();
//                                break;
//
//                            default:
//                                break;
//                        }
//                        return true;
//                    }
//                });
                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }
            holder.position = arg0;
            holder.title.setTag(arg0);
            if (TextUtils.isEmpty(title)) {
                holder.title.setVisibility(View.GONE);
            } else {
                holder.title.setVisibility(View.VISIBLE);
            }
            holder.title.setText(title);
            holder.postCount.setText(String.valueOf(postCount));
            holder.viewed.setText(String.valueOf(viewedCount));
            holder.username.setText(mList.get(arg0).getUserName());
            //String createTime = mList.get(arg0).getLastPostTime();
            String timeTips = mList.get(arg0).getTimeTips();
//            if (createTime == null) {
//                createTime = mList.get(arg0).getCreateTime();
//            }
//            if (mList.get(arg0).getType() == 0) {
//                holder.gridview.setVisibility(View.GONE);
//                holder.summary.setText(mList.get(arg0).getSummary());
//                holder.summary.setVisibility(View.VISIBLE);
//            } else
            if (true) {
                holder.gridview.setTag(arg0);
                // holder.summary.setVisibility(View.GONE);
                if (TextUtils.isEmpty(mList.get(arg0).getSummary())) {
                    holder.summary.setVisibility(View.GONE);
                } else {
                    holder.summary.setVisibility(View.VISIBLE);
                    holder.summary.setText(mList.get(arg0).getSummary());
                }
                holder.gridview.setVisibility(View.VISIBLE);
//                String[] showImgs = new String[4];
//                int j = 0;
//                for (int i = mList.get(arg0).getImages().size()-1; i >= 0 ; i--) {
//                    if(j >= 4)
//                    {
//                        break;
//                    }
//                    showImgs[j] = mList.get(arg0).getImages().get(i);
//                    j++;
//                }
//                populateGridView(holder.gridview, showImgs);
                List<String> urls = mList.get(arg0).getImages();
                String tag = (String) holder.imageView.getTag();
                //GlideUtls.loadIntoUseFitWidth(mContext, "http://nahuo-img-server.b0.upaiyun.com//0/180403/aab.jpg", holder.imageView,R.drawable.empty_photo);
                if (ListUtils.isEmpty(urls)) {
                    holder.imageView.setVisibility(View.GONE);
                } else {
                    holder.imageView.setVisibility(View.VISIBLE);
                    String tmpUrl = urls.get(0);
                    String url = ImageUrlExtends.getImageUrl(tmpUrl);
                    holder.imageView.setTag(url);
                    if (!TextUtils.equals(url, tag)) {
                        holder.imageView.setImageResource(R.drawable.empty_photo);
                    }
//                    File f = new File(tmpUrl);
//                    if (f.exists()) {
//                        Picasso.with(mContext).load(f).placeholder(R.drawable.empty_photo).into(holder.imageView);
//                    } else {
//                        String url = ImageUrlExtends.getImageUrl(tmpUrl);
//                        if (!TextUtils.isEmpty(url)) {
//                            Picasso.with(mContext).load(url).placeholder(R.drawable.empty_photo).into(holder.imageView);
//                        }
//                    }
                    GlideUtls.loadIntoUseFitWidth(mContext, url, holder.imageView, R.drawable.empty_photo);
                }
//                if (urls != null && urls.size() > 3) {
//                    holder.gridview.setVisibility(View.VISIBLE);
//                    List<String> list = new ArrayList<String>();
//                    for (int i = 0; i < urls.size() - 1; i++) {
//                        holder.gridview.setNumColumns(3);
//                        list.add(urls.get(i));
//                        if (i == 2) {
//                            break;
//                        }
//                    }
//                    populateGridView(holder.gridview, list, 3);
//                } else if (urls != null && urls.size() == 3) {
//                    holder.gridview.setVisibility(View.VISIBLE);
//                    List<String> list = new ArrayList<String>();
//                    for (int i = 0; i < urls.size(); i++) {
//                        holder.gridview.setNumColumns(3);
//                        list.add(urls.get(i));
//                    }
//                    populateGridView(holder.gridview, list, 3);
//                } else if (urls != null && urls.size() == 2) {
//                    holder.gridview.setVisibility(View.VISIBLE);
//                    holder.gridview.setNumColumns(2);
//                    populateGridView(holder.gridview, urls, 2);
//                } else if (urls != null && urls.size() == 1) {
//                    holder.gridview.setVisibility(View.VISIBLE);
//                    holder.gridview.setNumColumns(2);
//                    populateGridView(holder.gridview, urls, 1);
//                } else {
//                    holder.gridview.setVisibility(View.GONE);
//                }
            }
//            holder.createTime.setText(FunctionHelper
//                    .getFriendlyTime(createTime));
            holder.createTime.setText(timeTips);
            String imageurl = ImageUrlExtends.getImageUrl(cover, 3);
            if (imageurl.length() > 0) {
                Picasso.with(mContext).load(imageurl)
                        .placeholder(R.drawable.shop_logo_normal1).into(holder.cover);
            }
//            holder.cover.setTag(arg0);
//            holder.cover.setOnClickListener(new View.OnClickListener() {
//
//                @Override
//                public void onClick(View v) {
//                    gotoUserCardInfo((Integer) v.getTag());
//                }
//            });
//            holder.username.setTag(arg0);
//            holder.username.setOnClickListener(new View.OnClickListener() {
//
//                @Override
//                public void onClick(View v) {
//                    gotoUserCardInfo((Integer) v.getTag());
//                }
//            });
            holder.title.setTag(arg0);
//            holder.title.setOnClickListener(new View.OnClickListener() {
//
//                @Override
//                public void onClick(View arg0) {
////                  gotoUserCardInfo((Integer) arg0.getTag());
//                    gotoDetail((Integer) arg0.getTag());
//                }
//            });
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    gotoDetail(arg0);
                }
            });
            holder.gridview.setClickable(false);
            holder.gridview.setEnabled(false);
        }


        return view;
    }

    private Date lastShareTime;

    private void gotoDetail(int position) {
//        if (!SpManager.getIs_Login(mContext)) {
//            Utils.gotoLoginActivity(mContext);
//        } else {
        if (lastShareTime != null) {
            Calendar cal = Calendar.getInstance();

            // 2秒内重复弹出就忽略
            long second = Math.abs((cal.getTimeInMillis() - lastShareTime
                    .getTime()) / 1000);
            if (second < 2) {
                return;
            }
        }
        lastShareTime = new Date();

        TopicInfoModel model = mList.get(position);
        // 进入活动详情
        Intent intent = new Intent(mContext, PostDetailActivity.class);
        intent.putExtra(PostDetailActivity.EXTRA_TID, model.getID());
        intent.putExtra(PostDetailActivity.EXTRA_LOGO_URL,
                Const.getShopLogo(model.getUserID()));
        intent.putExtra(PostDetailActivity.EXTRA_POST_TITLE, model.getTitle());
        intent.putExtra(PostDetailActivity.EXTRA_POST_TYPE,
                model.getType() == 0 ? Const.PostType.TOPIC
                        : Const.PostType.ACTIVITY);
        mContext.startActivity(intent);
//        SpManager.setQuickActivityMaxID(mContext, MainNewActivity.ActivityId);
//        SpManager.setQuickTopicMaxID(mContext, MainNewActivity.TopicId);
//        MsgRed msgRed = new MsgRed();
//        msgRed.setCount(0);
//        msgRed.setIs_Show(false);
//        EventBus.getDefault().post(BusEvent.getEvent(EventBusId.PINHUO_ME_RED_IS_SHOW, msgRed));
        //}
    }

    private void populateGridView(GridView gridView, final List<String> urls, int num) {
        PicGridViewAdapter gridAdapter = new PicGridViewAdapter(mContext, urls);
        gridAdapter.setNumColumns(num);
        gridView.setAdapter(gridAdapter);
//        int size = urls.size();
//        if (size == 1) {
//            gridAdapter.setNumColumns(1);
//        } else if (size == 2) {
//            gridAdapter.setNumColumns(2);
//        } else if (size > 2) {
//            gridAdapter.setNumColumns(3);
//        }
//        gridAdapter.notifyDataSetChanged();
//        if (mGridViewWidth == 0) {
//            gridView.post(new Runnable() {
//                @Override
//                public void run() {
//                    mGridViewWidth = gridView.getMeasuredWidth();
//                    gridAdapter.setGridViewWidth(gridView.getMeasuredWidth());
//                    gridAdapter.setData(urls);
//                    gridAdapter.notifyDataSetChanged();
//                }
//            });
//        } else {
//            gridAdapter.setGridViewWidth(mGridViewWidth);
//            gridAdapter.setData(urls);
//            gridAdapter.notifyDataSetChanged();
//        }

    }

    /**
     * @description 进入名片页
     */
    private void gotoUserCardInfo(int position) {
        TopicInfoModel model = mList.get(position);

        // 进入活动详情
//        Intent intent = new Intent(mContext, UserInfoActivity.class);
//        intent.putExtra(UserInfoActivity.EXTRA_USER_ID, model.getUserID());
//        mContext.startActivity(intent);
    }

    public class ViewHolder {
        public int position;
        private ImageView cover;
        private TextView title;
        private TextView postCount;
        private TextView createTime;
        private PinHuoTextView summary;
        private TextView username;
        private TextView viewed;
        private GridView gridview;
        private ImageView imageView;
    }
}
