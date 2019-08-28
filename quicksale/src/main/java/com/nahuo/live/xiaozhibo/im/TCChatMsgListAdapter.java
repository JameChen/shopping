package com.nahuo.live.xiaozhibo.im;


import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.nahuo.library.helper.FunctionHelper;
import com.nahuo.live.xiaozhibo.common.utils.TCConstants;
import com.nahuo.quicksale.R;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


/**
 * 消息列表的Adapter
 */
public class TCChatMsgListAdapter extends BaseAdapter implements AbsListView.OnScrollListener {

    private static String TAG = TCChatMsgListAdapter.class.getSimpleName();
    private static final int ITEMCOUNT = 7;
    private List<TCChatEntity> listMessage = null;
    private LayoutInflater inflater;
    private LinearLayout layout;
    private int mTotalHeight;
    public static final int TYPE_TEXT_SEND = 0;
    public static final int TYPE_TEXT_RECV = 1;
    private Context context;
    private ListView mListView;
    private ArrayList<TCChatEntity> myArray = new ArrayList<>();

    public void setmPusherId(String mPusherId) {
        this.mPusherId = mPusherId;
    }

    private String mPusherId="";
//    private boolean  mBLiveAnimator = false;

    class AnimatorInfo {
        long createTime;

        public AnimatorInfo(long uTime) {
            createTime = uTime;
        }

        public long getCreateTime() {
            return createTime;
        }

        public void setCreateTime(long createTime) {
            this.createTime = createTime;
        }
    }

    private static final int MAXANIMATORCOUNT = 8;
    private static final int MAXLISTVIEWHEIGHT = 750;
    private static final int ANIMATORDURING = 8000;
    private static final int MAXITEMCOUNT = 50;
    private LinkedList<AnimatorSet> mAnimatorSetList;
    private LinkedList<AnimatorInfo> mAnimatorInfoList;
    private boolean mScrolling = false;
//    private boolean mCreateAnimator = false;

    public TCChatMsgListAdapter(Context context, ListView listview, List<TCChatEntity> objects) {
        this.context = context;
        mListView = listview;
        inflater = LayoutInflater.from(context);
        this.listMessage = objects;

        mAnimatorSetList = new LinkedList<>();
        mAnimatorInfoList = new LinkedList<>();

        mListView.setOnScrollListener(this);
    }


    @Override
    public int getCount() {
        return listMessage.size();
    }

    @Override
    public Object getItem(int position) {
        return listMessage.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * 通过名称计算颜色
     */
    private int calcNameColor(String strName) {
        if (strName == null) return 0;
        if (strName.equals("主播"))
            return context.getResources().getColor(R.color.pin_huo_red);
        byte idx = 0;
        byte[] byteArr = strName.getBytes();
        for (int i = 0; i < byteArr.length; i++) {
            idx ^= byteArr[i];
        }

        switch (idx & 0x7) {
            case 1:
                return context.getResources().getColor(R.color.colorSendName1);
            case 2:
                return context.getResources().getColor(R.color.colorSendName2);
            case 3:
                return context.getResources().getColor(R.color.colorSendName3);
            case 4:
                return context.getResources().getColor(R.color.colorSendName4);
            case 5:
                return context.getResources().getColor(R.color.colorSendName5);
            case 6:
                return context.getResources().getColor(R.color.colorSendName6);
            case 7:
                return context.getResources().getColor(R.color.colorSendName7);
            case 0:
            default:
                return context.getResources().getColor(R.color.colorSendName);
        }
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        SpannableString spanString;

        if (convertView == null) {
            holder = new ViewHolder();
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            convertView = layoutInflater.inflate(R.layout.listview_msg_item, null);
            holder.sendContext = (TextView) convertView.findViewById(R.id.sendcontext);
            convertView.setTag(R.id.tag_first, holder);
        } else {
            holder = (ViewHolder) convertView.getTag(R.id.tag_first);
        }

        TCChatEntity item = listMessage.get(position);

//        if (mCreateAnimator && mBLiveAnimator) {
//            playViewAnimator(convertView, position, item);
//        }
        String name=isAnchor(item.getReceiveId())?"主播": FunctionHelper.getStarString(item.getSenderName(),5);
        spanString = new SpannableString(name + "  " + item.getContext());
        if (item.getType() != TCConstants.TEXT_TYPE) {
            // 设置名称为粗体
            StyleSpan boldStyle = new StyleSpan(Typeface.BOLD_ITALIC);
            spanString.setSpan(boldStyle, 0, name.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            holder.sendContext.setTextColor(context.getResources().getColor(R.color.colorSendName1));
        } else {
            // 根据名称计算颜色
            spanString.setSpan(new ForegroundColorSpan(calcNameColor(name)),
                    0, name.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
            holder.sendContext.setTextColor(context.getResources().getColor(R.color.colorTextWhite));
        }
        holder.sendContext.setText(spanString);
        // 设置控件实际宽度以便计算列表项实际高度
        //holder.sendContext.fixViewWidth(mListView.getWidth());
        return convertView;
    }
 private  boolean isAnchor(String rId){
     if (!TextUtils.isEmpty(rId)&&!TextUtils.isEmpty(mPusherId)){
         if (rId.equals(mPusherId))
             return  true;
     }
     return  false;
 }

    static class ViewHolder {
        public TextView sendContext;

    }

    /**
     * 停止View属性动画
     *
     * @param itemView 当前执行动画View
     */
    private void stopViewAnimator(View itemView) {
        AnimatorSet aniSet = (AnimatorSet) itemView.getTag(R.id.tag_second);
        if (null != aniSet) {
            aniSet.cancel();
            mAnimatorSetList.remove(aniSet);
        }
    }

    /**
     * 播放View属性动画
     *
     * @param itemView   动画对应View
     * @param startAlpha 初始透明度
     * @param duringTime 动画时长
     */
    private void playViewAnimator(View itemView, float startAlpha, long duringTime) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(itemView, "alpha", startAlpha, 0f);
        AnimatorSet aniSet = new AnimatorSet();
        aniSet.setDuration(duringTime);
        aniSet.play(animator);
        aniSet.start();
        mAnimatorSetList.add(aniSet);
        itemView.setTag(R.id.tag_second, aniSet);
    }

    /**
     * 播放渐消动画
     *
     * @param pos 位置
     * @param view 执行动画View
     */
    public void playDisappearAnimator(int pos, View view) {
        int firstVisable = mListView.getFirstVisiblePosition();
        if (firstVisable <= pos) {
            playViewAnimator(view, 1f, ANIMATORDURING);
        } else {
            Log.d(TAG, "playDisappearAnimator->unexpect pos: " + pos + "/" + firstVisable);
        }
    }

    /**
     * 继续播放渐消动画
     *
     * @param itemView 执行动画View
     * @param position 位置
     * @param item
     */
    private void continueAnimator(View itemView, int position, final TCChatEntity item) {
        int animatorIdx = listMessage.size() - 1 - position;

        if (animatorIdx < MAXANIMATORCOUNT) {
            float startAlpha = 1f;
            long during = ANIMATORDURING;

            stopViewAnimator(itemView);

            // 播放动画
            if (position < mAnimatorInfoList.size()) {
                AnimatorInfo info = mAnimatorInfoList.get(position);
                long time = info.getCreateTime();  //  获取列表项加载的初始时间
                during = during - (System.currentTimeMillis() - time);     // 计算动画剩余时长
                startAlpha = 1f * during / ANIMATORDURING;                    // 计算动画初始透明度
                if (during < 0) {   // 剩余时长小于0直接设置透明度为0并返回
                    itemView.setAlpha(0f);
                    Log.v(TAG, "continueAnimator->already end animator:" + position + "/" + item.getContext() + "-" + during);
                    return;
                }
            }

            // 创建属性动画并播放
            Log.v(TAG, "continueAnimator->pos: " + position + "/" + listMessage.size() + ", alpha:" + startAlpha + ", dur:" + during);
            playViewAnimator(itemView, startAlpha, during);
        } else {
            Log.v(TAG, "continueAnimator->ignore pos: " + position + "/" + listMessage.size());
        }
    }

    /**
     * 播放消失动画
     */
    private void playDisappearAnimator() {
        for (int i = 0; i < mListView.getChildCount(); i++) {
            View itemView = mListView.getChildAt(i);
            if (null == itemView) {
                Log.w(TAG, "playDisappearAnimator->view not found: " + i + "/" + mListView.getCount());
                break;
            }

            // 更新动画创建时间
            int position = mListView.getFirstVisiblePosition() + i;
            if (position < mAnimatorInfoList.size()) {
                mAnimatorInfoList.get(position).setCreateTime(System.currentTimeMillis());
            } else {
                Log.e(TAG, "playDisappearAnimator->error: " + position + "/" + mAnimatorInfoList.size());
            }

            playViewAnimator(itemView, 1f, ANIMATORDURING);
        }
    }

    /**
     * 播放列表项动画
     *
     * @param itemView 要播放动画的列表项
     * @param position 列表项的位置
     * @param item     列表数据
     */
    private void playViewAnimator(View itemView, int position, final TCChatEntity item) {
        if (!myArray.contains(item)) {  // 首次加载的列表项动画
            myArray.add(item);
            mAnimatorInfoList.add(new AnimatorInfo(System.currentTimeMillis()));
        }

        if (mScrolling) {  // 滚动时不播放动画，设置透明度为1
            itemView.setAlpha(1f);
        } else {
            continueAnimator(itemView, position, item);
        }
    }

    /**
     * 删除超过上限(MAXITEMCOUNT)的列表项
     */
    private void clearFinishItem() {
        // 删除超过的列表项
        while (listMessage.size() > MAXITEMCOUNT) {
            listMessage.remove(0);
            if (mAnimatorInfoList.size() > 0) {
                mAnimatorInfoList.remove(0);
            }
        }

        // 缓存列表延迟删除
        while (myArray.size() > (MAXITEMCOUNT << 1)) {
            myArray.remove(0);
        }

        while (mAnimatorInfoList.size() >= listMessage.size()) {
            Log.e(TAG, "clearFinishItem->error size: " + mAnimatorInfoList.size() + "/" + listMessage.size());
            if (mAnimatorInfoList.size() > 0) {
                mAnimatorInfoList.remove(0);
            } else {
                break;
            }
        }
    }

    /**
     * 重新计算ITEMCOUNT条记录的高度，并动态调整ListView的高度
     */
    private void redrawListViewHeight() {

        int totalHeight = 0;
        int start = 0, lineCount = 0;

        if (listMessage.size() <= 0) {
            return;
        }

        if (mTotalHeight >= MAXLISTVIEWHEIGHT) {
            return;
        }

        // 计算底部ITEMCOUNT条记录的高度
//        mCreateAnimator = false;    // 计算高度时不播放属性动画
        for (int i = listMessage.size() - 1; i >= start && lineCount < ITEMCOUNT; i--, lineCount++) {
            View listItem = getView(i, null, mListView);

            listItem.measure(View.MeasureSpec.makeMeasureSpec(MAXLISTVIEWHEIGHT, View.MeasureSpec.AT_MOST)
                    , View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
            // add item height
            totalHeight += listItem.getMeasuredHeight();
            if(totalHeight > MAXLISTVIEWHEIGHT) {
                totalHeight = MAXLISTVIEWHEIGHT;
                break;
            }
        }
//        mCreateAnimator = true;



        mTotalHeight = totalHeight;
        // 调整ListView高度
        ViewGroup.LayoutParams params = mListView.getLayoutParams();
        params.height = totalHeight + (mListView.getDividerHeight() * (lineCount - 1));
        mListView.setLayoutParams(params);
    }

    /**
     * 停止当前所有属性动画并重置透明度
     */
    private void stopAnimator() {
        // 停止动画
        for (AnimatorSet anSet : mAnimatorSetList) {
            anSet.cancel();
        }
        mAnimatorSetList.clear();
    }

    /**
     * 重置透明度
     */
    private void resetAlpha() {
        for (int i = 0; i < mListView.getChildCount(); i++) {
            View view = mListView.getChildAt(i);
            view.setAlpha(1f);
        }
    }

    /**
     * 继续可视范围内所有动画
     */
    private void continueAllAnimator() {
//        int startPos = mListView.getFirstVisiblePosition();
//
//        for (int i = 0; i < mListView.getChildCount(); i++) {
//            View view = mListView.getChildAt(i);
//            if (null != view && startPos + i < listMessage.size()) {
//                continueAnimator(view, startPos + i, listMessage.get(startPos + i));
//            }
//        }
    }

    /**
     * 重载notifyDataSetChanged方法实现渐消动画并动态调整ListView高度
     */
    @Override
    public void notifyDataSetChanged() {
        Log.v(TAG, "notifyDataSetChanged->scroll: " + mScrolling);
//        if (mScrolling) {
//            // 滑动过程中不刷新
//            super.notifyDataSetChanged();
//            return;
//        }
//
//        // 删除多余项
//        clearFinishItem();
//
//        if (mBLiveAnimator) {
//            // 停止之前动画
//            stopAnimator();
//
//            // 清除动画
//            mAnimatorSetList.clear();
//        }

        super.notifyDataSetChanged();

        // 重置ListView高度
        redrawListViewHeight();

//        if (mBLiveAnimator && listMessage.size() >= MAXITEMCOUNT) {
//            continueAllAnimator();
//        }

        // 自动滚动到底部
        mListView.post(new Runnable() {
            @Override
            public void run() {
                mListView.setSelection(mListView.getCount() - 1);
            }
        });
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        switch (scrollState) {
            case SCROLL_STATE_FLING:
                break;
            case SCROLL_STATE_TOUCH_SCROLL:
//                if (mBLiveAnimator) {
//                    // 开始滚动时停止所有属性动画
//                    stopAnimator();
//                    resetAlpha();
//                }
                mScrolling = true;
                break;
            case SCROLL_STATE_IDLE:
                mScrolling = false;
//                if (mBLiveAnimator) {
//                    // 停止滚动时播放渐消动画
//                    playDisappearAnimator();
//                }
                break;
            default:
                break;
        }

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

    }
}
