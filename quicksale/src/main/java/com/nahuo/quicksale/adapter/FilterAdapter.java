package com.nahuo.quicksale.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.nahuo.quicksale.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jame on 2017/8/21.
 */

public class FilterAdapter<T>extends BaseAdapter implements Filterable {
    private List<T> fData;//过路器（MyFilter）数据源
    LayoutInflater con;
    private List<T> data;//作为参数的数据源
    private final Object mLock = new Object();
    private MyFilter mFilter = null;

    public FilterAdapter(Context con, List<T> data) {
        this.con = LayoutInflater.from(con);
        mFilter = new MyFilter();
        this.data = new ArrayList<T>(data);
    }

    public int getCount() {
        return data.size();
    }

    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    public View getView(final int peisition, View conview, ViewGroup arg2) {
        View view = null;
        ViewHoder vh = null;
        if (conview == null) {
            vh = new ViewHoder();

            view = con.inflate(R.layout.search_auto_item, null);
            vh.te = (TextView) view.findViewById(R.id.textView);
            view.setTag(vh);

        } else {

            view = conview;
            vh = (ViewHoder) view.getTag();
        }
        vh.te.setText((CharSequence) data.get(peisition));
        return view;
    }

    class ViewHoder {
        TextView te;
    }

    @Override
    public android.widget.Filter getFilter() {

        return mFilter;
    }

    private class MyFilter extends Filter {

        /**
         * 过滤autoCompleteEditext中的字 改成包含
         */
        protected FilterResults performFiltering(CharSequence constraint) {

            FilterResults results = new FilterResults();
            if (fData == null) {
                synchronized (mLock) {
                    fData = new ArrayList<T>(data);
                }
            }
            int count = fData.size();
            ArrayList<T> values = new ArrayList<T>();

            for (int i = 0; i < count; i++) {
                T value = fData.get(i);
                String valueText = value.toString();
                if (null != valueText && null != constraint
                        && valueText.toUpperCase().indexOf(constraint.toString().toUpperCase())!=-1) {
                    // Log.d("--==tag", "--==tag--" + value);
                    values.add(value);
                }
            }
            results.values = values;
            results.count = values.size();
            return results;
        }

        /**
         * 在FilterResults方法中把过滤好的数据传入此方法中 results过滤好的数据源重新给data赋值显示新的适配内容
         * 并刷新适配器
         */
        @Override
        protected void publishResults(CharSequence arg0, FilterResults results) {

            data = (List<T>) results.values;
            if (results.count > 0) {
                notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated();
            }

        }

    }
}
