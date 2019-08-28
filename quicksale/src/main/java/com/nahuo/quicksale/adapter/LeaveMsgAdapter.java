package com.nahuo.quicksale.adapter;

import java.util.ArrayList;
import java.util.List;

import com.nahuo.library.helper.FunctionHelper;
import com.nahuo.library.helper.ImageUrlExtends;
import com.nahuo.quicksale.R;
import com.nahuo.quicksale.common.Const;
import com.nahuo.quicksale.common.SpManager;
import com.nahuo.quicksale.oldermodel.LeaveMsgModel;
import com.squareup.picasso.Picasso;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class LeaveMsgAdapter extends BaseAdapter{

    String fromUserName ; 
    int userID  ;
    int myID ; 
    public LeaveMsgAdapter(String name , int userID){
        this.fromUserName = name ; 
        this.userID = userID ; 
    }
    private List<LeaveMsgModel> models ; 
    @Override
    public int getCount() {
        return models == null ? 0 : models.size();
    }

    @Override
    public LeaveMsgModel getItem(int position) {
        return models.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    /****
     * 1：对方发来的消息
     */
    @Override
    public int getItemViewType(int position) {
        int type = 0 ; 
        if(fromUserName.equals(getItem(position).FromUserName)){ //
            type = 1 ; 
        }
        return type;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null ; 
        if(convertView == null){
            if(getItemViewType(position)== 0){
                convertView = LayoutInflater.from(parent.getContext()) .inflate(R.layout.item_send_message , null) ;
            }
            else{
                convertView = LayoutInflater.from(parent.getContext()) .inflate(R.layout.row_received_message   , null) ;
            }
            holder = new ViewHolder() ; 
            holder.time = (TextView)convertView.findViewById(R.id.timestamp);
            holder.head_iv = (ImageView)convertView.findViewById(R.id.iv_userhead);
            // 这里是文字内容
            holder.tv = (TextView)convertView.findViewById(R.id.tv_chatcontent);
            convertView.setTag(holder) ; 
        }
        else{
            holder = (ViewHolder)convertView.getTag() ; 
        }
        LeaveMsgModel model = getItem(position) ; 
        Log.e("red_count_bg" , "adapter = "+model.CreateDate+"   "+model.Message+"   "+model.FromUserName) ;
        holder.time.setText(FunctionHelper.getFriendlyTime(model.CreateDate)) ; 
        holder.tv.setText(model.Message) ; 
        if(getItemViewType(position)==0){
            if(myID == 0)
                myID = SpManager.getUserId(parent.getContext()) ; 
            Picasso.with(parent.getContext()).load(ImageUrlExtends.getImageUrl(Const.getShopLogo(myID), Const.LIST_COVER_SIZE)).into(holder.head_iv) ;
        }
        else{
            Picasso.with(parent.getContext()).load(ImageUrlExtends.getImageUrl(Const.getShopLogo(userID), Const.LIST_COVER_SIZE)).into(holder.head_iv) ;
        }
        return convertView;
    }

    
    public void refresh(List<LeaveMsgModel> models){
        if(this.models!=null)
            this.models.clear() ; 
        this.models = models ; 
        notifyDataSetChanged() ; 
    }
    public void loadMore(List<LeaveMsgModel> models){
        if(this.models == null){
            this.models = models ; 
        }
        else{
            this.models.addAll(0, models) ; 
        }
        notifyDataSetChanged() ; 
    }
    public void addMsg(LeaveMsgModel model){
        if(models == null)
            models = new ArrayList<LeaveMsgModel>() ; 
        models.add(model) ;
        notifyDataSetChanged() ;
    }
    
    static class ViewHolder {

        public TextView tv;
        public ImageView head_iv;
        public TextView time;
    }
}
