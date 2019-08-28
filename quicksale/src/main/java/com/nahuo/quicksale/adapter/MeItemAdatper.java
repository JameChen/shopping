package com.nahuo.quicksale.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nahuo.quicksale.R;
import com.nahuo.quicksale.oldermodel.MeItemModel;

import java.util.List;

/**
 * Created by 诚 on 2015/9/21.
 */
public class MeItemAdatper extends BaseAdapter {

    public Context mContext;
    public List<MeItemModel> mList;

    public  MeItemAdatper(Context Context, List<MeItemModel> List){
        mContext=Context;
        mList=List;
    }

    private OnMeItemListener mListener;

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.layout_item_me, parent, false);
            holder = new ViewHolder();
            holder.title = (TextView) view.findViewById(R.id.ItemText);
            holder.image = (ImageView) view.findViewById(R.id.ItemImage);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        final MeItemModel model = mList.get(position);



        if (model != null) {
            holder.title.setText(model.getName());
            holder.image.setBackgroundResource(model.getSourceId());
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.OnMeItemClick(model);
                }
            });
        }
        view.setBackgroundColor(Color.WHITE); //设置背景颜色

        return view;
    }


    public class ViewHolder {


        TextView title;
        ImageView image;

    }

    public static interface OnMeItemListener {
        public void OnMeItemClick(MeItemModel item);
    }

    public void setStyleListener(OnMeItemListener listener) {
        mListener = listener;
    }

}
