package com.nahuo.quicksale.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nahuo.library.helper.FunctionHelper;
import com.nahuo.library.helper.ImageUrlExtends;
import com.nahuo.quicksale.R;
import com.nahuo.quicksale.common.Const;
import com.nahuo.quicksale.oldermodel.TopicInfoModel;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by 诚 on 2015/9/21.
 */
public abstract class BaseHotAdapter extends BaseAdapter {

    public Context mContext;
    private List<TopicInfoModel> models ;
    @Override
    public int getCount() {
        return models == null ? 0 : models .size();
    }

    @Override
    public TopicInfoModel getItem(int position) {
        return models.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null ;
        if(convertView == null){
            convertView =  inflaterView(parent.getContext());
            holder = new ViewHolder() ;
            holder.commentView = (TextView) convertView.findViewById(R.id.txt_hotpost_comment) ;
            holder.headView = (ImageView) convertView.findViewById(R.id.img_hotpst_head) ;
            holder.lookView = (TextView) convertView.findViewById(R.id.txt_hotpost_like) ;  //点赞数
            holder.commonView = convertView.findViewById(R.id.view_hot_common) ;
            holder.timeView = (TextView) convertView.findViewById(R.id.txt_hotpost_time) ;
            holder.titleView = (TextView) convertView.findViewById(R.id.txt_hotpost_title) ;
            holder.userNameView = (TextView) convertView.findViewById(R.id.txt_hotpost_username) ;
//			holder.groupView = (TextView) convertView.findViewById(R.id.btn_hotpost_group) ;
//			holder.groupView.setOnClickListener(new OnClickListener(){
//				@Override
//				public void onClick(View v) {
//					int gid = (Integer) v.getTag() ;
//					Intent intent = new Intent(v.getContext(), TopicPageActivity.class);
//					intent.putExtra("gid", gid);
//					v.getContext().startActivity(intent);
//				}
//			}) ;
            convertView.setTag(holder) ;
        }
        else{
            holder = (ViewHolder) convertView.getTag()  ;
        }
        TopicInfoModel model = getItem(position) ;
        holder.commentView.setText(String.valueOf(model.getPostCount())) ;
//		holder.groupView.setText(model.getGroupName()) ;
//		holder.groupView.setTag(model.getGroupID()) ;
        String cover = Const.getShopLogo(model.getUserID());
        String imageurl = ImageUrlExtends.getImageUrl(cover, 3);
        if (imageurl.length() > 0) {
            Picasso.with(parent.getContext()).load(imageurl)
                    .placeholder(R.drawable.empty_photo).into(holder.headView);
        }
        holder.lookView.setText(String.valueOf(model.getLikeCount()));
        if (model.getLastPostTime() != null) {
            holder.timeView.setText(FunctionHelper.getFriendlyTime(model
                    .getLastPostTime()));
        } else {
            holder.timeView.setText(FunctionHelper.getFriendlyTime(model
                    .getCreateTime()));
        }
        holder.titleView.setText(model.getTitle()) ;
        holder.userNameView.setText(model.getUserName()) ;
        doChildView(holder.commonView ,model) ;
        holder.userNameView.setTag(position);
        holder.userNameView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                gotoUserCardInfo(Integer.valueOf(v.getTag().toString()));
            }
        });
        holder.headView.setTag(position);
        holder.headView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                gotoUserCardInfo(Integer.valueOf(v.getTag().toString()));
            }
        });
        return convertView;
    }
    static class ViewHolder{
        ImageView headView ;
        TextView userNameView ;
        TextView titleView ;
        TextView timeView ;
        //		TextView groupView ;
        TextView lookView ;  //查看帖子的人数
        TextView commentView ;
        View commonView ;
    }
    public void setData(List<TopicInfoModel> mCachePosts) {
//		if(models!=null)
//			models.clear() ;
        this.models = mCachePosts ;
    }
    public void loadMore(List<TopicInfoModel> more)
    {
        if(models ==null){
            models = more ;
        }
        else{
            models.addAll(more) ;
        }
    }
    protected abstract void doChildView(View commonView , TopicInfoModel model);
    protected abstract View inflaterView(Context context);

    /**
     * @description 进入名片页
     */
    private void gotoUserCardInfo(int position) {
        TopicInfoModel model = getItem(position) ;

        // 进入活动详情
//        Intent intent = new Intent(mContext, UserInfoActivity.class);
//        intent.putExtra(UserInfoActivity.EXTRA_USER_ID, model.getUserID());
//        mContext.startActivity(intent);
    }

}
