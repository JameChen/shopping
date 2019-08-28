package com.nahuo.quicksale.adapter;

import java.util.List;

import com.nahuo.quicksale.R;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

public class ExpressionAdapter extends ArrayAdapter<String> {

    private IMojiClickListener mIMojiClickListener;

    public ExpressionAdapter(Context context, int textViewResourceId, List<String> objects) {
        super(context, textViewResourceId, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(getContext(), R.layout.row_expression, null);
            holder = new ViewHolder();
            holder.ivMoji = (ImageView)convertView.findViewById(R.id.iv_expression);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder)convertView.getTag();
        }

        // ImageView imageView = (ImageView)convertView.findViewById(R.id.iv_expression);

        final String filename = getItem(position);
        int resId = getContext().getResources().getIdentifier(filename, "drawable", getContext().getPackageName());
        holder.ivMoji.setImageResource(resId);

//        ScaleType sc = ScaleType.CENTER_CROP;
//        if (filename.equals("delete_expression")) {
//            sc = ScaleType.FIT_START;
//            holder.ivMoji.setScaleType(ScaleType.FIT_START);
//        }
//        holder.ivMoji.setScaleType(sc);

        holder.ivMoji.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mIMojiClickListener != null) {
                    mIMojiClickListener.mojiClick(filename);
                }
                Log.i(getClass().getSimpleName(), "onClick:" + filename);
            }
        });

        return convertView;
    }

    private static final class ViewHolder {
        public ImageView ivMoji;
    }

    public void setIMojiClickListner(IMojiClickListener listner) {
        this.mIMojiClickListener = listner;
    }

    public interface IMojiClickListener {
        public void mojiClick(String filename);
    }

}
