package com.nahuo.quicksale.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nahuo.bean.SearchPanelBean;
import com.nahuo.library.helper.FunctionHelper;
import com.nahuo.quicksale.R;
import com.nahuo.quicksale.base.ViewHolder;
import com.nahuo.quicksale.common.ListUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jame on 2017/12/14.
 */

public class FliterSortItemAdpter extends RecyclerView.Adapter<ViewHolder> {
    private Activity context;
    public static int SINGLE_CHOOSE = 1;
    public static int MULT_CHOOSE = 2;
    private int type_choose=2;

    public int getType_choose() {
        return type_choose;
    }

    public void setType_choose(int type_choose) {
        this.type_choose = type_choose;
    }

    List<SearchPanelBean.PanelsBeanX.PanelsBean> mdata = new ArrayList<>();
    SearchPanelBean searchPanelBean;
    public FliterSortItemAdpter(Activity context) {
        this.context = context;
    }

    public void setData(List<SearchPanelBean.PanelsBeanX.PanelsBean> data) {
        this.mdata = data;
    }

    private Listener mListener;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder holder = ViewHolder.createViewHolder(context, parent, R.layout.item_fliter_sort);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        View itemView = holder.getConvertView();
        TextView textView = (TextView) itemView.findViewById(R.id.tv_title);
        SearchPanelBean.PanelsBeanX.PanelsBean bean = mdata.get(position);
        if (bean != null) {
            if (TextUtils.isEmpty(bean.getName())) {
                textView.setText("");
            } else {
                textView.setText(bean.getName());
            }
            if (bean.isSelect) {
                // textView.setTextSize(16);
                textView.setTextColor(context.getResources().getColor(R.color.white));
                textView.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.sort_bg_select));
            } else {
                // textView.setTextSize(14);
                textView.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.sort_bg_defaut));
                textView.setTextColor(context.getResources().getColor(R.color.txt_gray));
            }
        }

        itemView.setOnClickListener(new OnItemClickListener(position));
    }

    public void setListener(Listener listener) {
        mListener = listener;
    }

    public void setSelect(int pos) {
        if (!ListUtils.isEmpty(mdata)) {
            for (int i = 0; i < mdata.size(); i++) {
                if (pos == i) {
                    mdata.get(i).isSelect = !mdata.get(i).isSelect ;
                } else {
                    mdata.get(i).isSelect = false;
                }
            }
            notifyDataSetChanged();
        }
    }

    private class OnItemClickListener implements View.OnClickListener {
        private int mPos;

        public OnItemClickListener(int pos) {
            mPos = pos;
        }

        @Override
        public void onClick(View v) {
            SearchPanelBean.PanelsBeanX.PanelsBean item = mdata.get(mPos);
            FunctionHelper.hideSoftInput(context);
            if (type_choose==SINGLE_CHOOSE){
                setSelect(mPos);
                if (mListener!=null)
                    mListener.onItemClick(item);
            }else {
                item.isSelect=!item.isSelect;
                notifyDataSetChanged();
            }

        }
    }

    public interface Listener {
        void onItemClick(SearchPanelBean.PanelsBeanX.PanelsBean item);
    }

    @Override
    public int getItemCount() {
        return mdata.size();
    }
}
