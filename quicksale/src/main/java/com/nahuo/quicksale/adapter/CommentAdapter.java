package com.nahuo.quicksale.adapter;

/**
 * Created by 诚 on 2015/9/21.
 */

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nahuo.library.helper.FunctionHelper;
import com.nahuo.library.helper.ImageUrlExtends;
import com.nahuo.quicksale.R;
import com.nahuo.quicksale.common.Const;
import com.nahuo.quicksale.common.URLImageGetter;
import com.nahuo.quicksale.common.URLTagHandler;
import com.nahuo.quicksale.oldermodel.PostsListModel;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * @author ZZB
 * @description 回复adapter
 * @created 2015-2-10 上午11:10:25
 */
public class CommentAdapter extends MyBaseAdapter<PostsListModel> implements
        View.OnClickListener {
    private int headSize;
    private Listener mListener;

    public CommentAdapter(Context context) {
        super(context);
        headSize = context.getResources().getDimensionPixelSize(R.dimen.msg_head_size);
    }

    public static interface Listener {
        public void onReplyClick(PostsListModel model);
    }

    /**
     * @description 添加子评论
     * @created 2015-2-10 下午7:08:16
     * @author ZZB
     */
    public void addComment(PostsListModel comment) {
        if (comment.getRootId() == 0) {// 回复楼主
            addDataToOne(comment);
        } else {// 子回复
            for (PostsListModel tmpComment : mdata) {
                if (tmpComment.getID() == comment.getRootId()) {
                    List<PostsListModel> subComments = tmpComment.getChilds();
                    subComments.add(comment);
                    break;
                }
            }
        }
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.lvitem_comment, parent,
                    false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final PostsListModel comment = mdata.get(position);
        String logoUrl = Const.getShopLogo(comment.getUserID());
        logoUrl = ImageUrlExtends.getImageUrl(logoUrl, 3);
        Picasso.with(parent.getContext()).load(logoUrl).resize(headSize, headSize)
                .placeholder(R.drawable.shop_logo_normal1).into(holder.ivIcon);
        holder.tvUserName.setText(comment.getUserName());
        holder.ivIcon.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                gotoUserInfo(Integer.valueOf(arg0.getTag().toString()));
            }
        });
        holder.tvUserName.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                gotoUserInfo(Integer.valueOf(arg0.getTag().toString()));
            }
        });
        holder.ivIcon.setTag(position);
        holder.tvUserName.setTag(position);
        if (comment.getFloor() > 0) {
            holder.tvFloor.setText("第"+comment.getFloor() + "楼 | ");
        } else {
            holder.tvFloor.setText("新回复");
        }
//		if (comment.getContent().contains("http://"))
//		{
//			holder.tvReplyContent.setVisibility(View.GONE);
//			holder.tvReplyContentHtml.setVisibility(View.VISIBLE);
//			holder.tvReplyContentHtml.loadDataWithBaseURL(null, comment.getContent(), "text/html", "UTF-8", null);
//			}
//		else
//		{
        holder.tvReplyContent.setOnClickListener(null);
        URLImageGetter imgGetter = new URLImageGetter(mContext, holder.tvReplyContent);
        String htmlStr = comment.getContent();
        int index = htmlStr.indexOf("<img");
        int end = 0;
        while (index >= 0) {
            end = htmlStr.indexOf(">", index);

            if (end >= 0) {
                String suffix = htmlStr.substring(index, end);
                if (!suffix.contains("gif'")) {
                    htmlStr = htmlStr.replace("<img", "<br/><img");
                }
            }
            index = htmlStr.indexOf("<img", end);
        }
        holder.tvReplyContent.setText(Html.fromHtml(htmlStr, imgGetter, new URLTagHandler(mContext)));
//		holder.tvReplyContent.setMovementMethod(LinkMovementMethod.getInstance());//超链接生效
//		holder.tvReplyContent.setVisibility(View.VISIBLE);
//		holder.tvReplyContentHtml.setVisibility(View.GONE);
//		}
        String time="";
        if (!TextUtils.isEmpty(comment
                .getCreateTime())) {
           time= FunctionHelper.getFriendlyTime(comment
                    .getCreateTime());
        }else {
            time="";
        }
        holder.tvReplyTime.setText(time);
        holder.tvReply.setOnClickListener(this);
        holder.tvReply.setTag(R.id.Tag_Position, position);
        holder.mLvSubComments.removeAllViews();
        List<PostsListModel> subComments = comment.getChilds();
        if (subComments != null && subComments.size() > 0) {
            if (holder.mLvSubComments.getVisibility() != View.VISIBLE)
                holder.mLvSubComments.setVisibility(View.VISIBLE);
            boolean first = true;
            for (PostsListModel plm : subComments) {
                plm.setRootId(comment.getID());
                View v = getSubCommentView(plm, first);
                holder.mLvSubComments.addView(v);
                first = false;
            }
        } else {
            if (holder.mLvSubComments.getVisibility() != View.GONE)
                holder.mLvSubComments.setVisibility(View.GONE);
        }
        return convertView;
    }

    private void gotoUserInfo(int position) {
//        Intent intent = new Intent(mContext, UserInfoActivity.class);
//        intent.putExtra(UserInfoActivity.EXTRA_USER_ID, mdata.get(position).getUserID());
//        mContext.startActivity(intent);
    }

    public void setListener(Listener listener) {
        mListener = listener;
    }

    private static class ViewHolder {
        private ImageView ivIcon;
        private TextView tvUserName, tvReplyContent, tvReplyTime,
                tvFloor;
        private View tvReply;
        private LinearLayout mLvSubComments;
//		private WebView tvReplyContentHtml;

        public ViewHolder(View v) {
            ivIcon = (ImageView) v.findViewById(R.id.iv_icon);
            tvUserName = (TextView) v.findViewById(R.id.tv_user_name);
            tvReplyContent = (TextView) v.findViewById(R.id.tv_reply_content);
//			tvReplyContentHtml = (WebView) v.findViewById(R.id.tv_reply_content_html);
            tvReplyTime = (TextView) v.findViewById(R.id.tv_reply_time);
            tvReply = v.findViewById(R.id.tv_reply);
            tvFloor = (TextView) v.findViewById(R.id.tv_floor);
            mLvSubComments = (LinearLayout) v
                    .findViewById(R.id.lv_sub_comments);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_reply:
                int pos = (Integer) v.getTag(R.id.Tag_Position);
                PostsListModel comment = mdata.get(pos);
                comment.setRootId(comment.getID());
                mListener.onReplyClick(comment);
                break;
        }
    }

    private TextView getSubCommentView(final PostsListModel item, boolean first) {
        int colorResId = mContext.getResources().getColor(R.color.gray_3);

        //边距
        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT, 1);
        if (first) {
            param.setMargins(10, 10, 0, 0);
        } else {
            param.setMargins(10, 0, 0, 0);
        }

        TextView tv = new TextView(mContext);
        tv.setTextColor(ContextCompat.getColor(mContext, R.color.gray_80));
        tv.setLineSpacing(FunctionHelper.dip2px(mContext.getResources(), 5), 1);
        tv.setLayoutParams(param);
        String userName = item.getUserName();
        int userNameLen = userName.length();
        String replyUserName = item.getReplyUserName();
        int replyUserNameLen = replyUserName.length();

//        imgGetter = new URLImageGetter(mContext, tv);
//        String htmlStr = item.getContent();
//        if (htmlStr.contains("<img"))
//        {
//            htmlStr = htmlStr.replace("<img", "<br/><img");
//            }
//		Spanned content = Html.fromHtml(htmlStr, imgGetter, new URLTagHandler(mContext));
        Spanned content = Html.fromHtml(item.getContent());
        String fullContent = userName + "回复" + replyUserName + ":" + content;
        SpannableString msp = new SpannableString(fullContent);
        msp.setSpan(new ForegroundColorSpan(colorResId), 0, userNameLen,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        msp.setSpan(new ForegroundColorSpan(colorResId), userNameLen + 2,
                userNameLen + 2 + replyUserNameLen,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv.setText(msp);
        tv.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mListener.onReplyClick(item);
            }
        });
        tv.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View arg0, MotionEvent arg1) {
                switch (arg1.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        arg0.setBackgroundColor(mContext.getResources().getColor(R.color.lightgray));
                        break;
                    default:
                        arg0.setBackgroundColor(mContext.getResources().getColor(R.color.transparent));
                        break;
                }
                return false;
            }
        });
        return tv;
    }
}
