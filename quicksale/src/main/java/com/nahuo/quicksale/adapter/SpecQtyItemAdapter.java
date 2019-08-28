package com.nahuo.quicksale.adapter;

import java.util.List;

import com.nahuo.quicksale.R;
import com.nahuo.quicksale.oldermodel.ColorSizeModel;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

public class SpecQtyItemAdapter extends BaseAdapter {

    private Context mContext;
    public List<ColorSizeModel> mColorSizeList;
    private OnSpecQtyItemClickListener mListener;

    public void setOnSpecQtyItemClickListener(
	    OnSpecQtyItemClickListener listener) {
	mListener = listener;
    }

    public SpecQtyItemAdapter(Context context,
	    List<ColorSizeModel> colorSizeList) {
	mContext = context;
	mColorSizeList = colorSizeList;
    }

    @Override
    public int getCount() {
	return mColorSizeList.size();
    }

    @Override
    public Object getItem(int position) {
	return mColorSizeList.get(position);
    }

    @Override
    public long getItemId(int position) {
	return position;
    }

    private Integer index = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
	final ViewHolder holder;
	View view = convertView;
	if (mColorSizeList.size() > 0) {
	    if (view == null) {
		view = LayoutInflater.from(mContext).inflate(
			R.layout.layout_specqty_items, parent, false);
		holder = new ViewHolder();
		holder.tvColor = (TextView) view
			.findViewById(R.id.specqty_items_tvColor);
		holder.tvSize = (TextView) view
			.findViewById(R.id.specqty_items_tvSize);
		holder.lkbDelete = (TextView) view
			.findViewById(R.id.specqty_items_lkbDelete);
		holder.edtQty = (EditText) view
			.findViewById(R.id.specqty_items_edtQty);
		holder.edtQty.setTag(position);
		holder.edtQty.setOnTouchListener(new OnTouchListener() {

		    @Override
		    public boolean onTouch(View v, MotionEvent event) {

			if (event.getAction() == MotionEvent.ACTION_UP) {
			    index = (Integer) v.getTag();
			}
			return false;
		    }
		});
		holder.edtQty.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus)
				{
					EditText edt = (EditText)v;
					if (edt.getText().toString().length()>0)
					{
					edt.setSelection(edt.getText().toString().length() - 1);
					}
				}
			}
		});

		// 修改数量
		class MyTextWatcher implements TextWatcher {

		    private ViewHolder mHolder;

		    public MyTextWatcher(ViewHolder holder) {
			mHolder = holder;
		    }

		    @Override
		    public void beforeTextChanged(CharSequence s, int start,
			    int count, int after) {

		    }

		    @Override
		    public void onTextChanged(CharSequence s, int start,
			    int before, int count) {

		    }

		    @Override
		    public void afterTextChanged(Editable s) {
			if (s != null && !"".equals(s.toString())) {
			    int position = (Integer) mHolder.edtQty.getTag();
			    ColorSizeModel entity = mColorSizeList
				    .get(position);
			    if (TextUtils.isEmpty(s.toString())) {
				mHolder.edtQty.setText("0");
				mHolder.edtQty.selectAll();
				entity.setQty(0);
				return;
			    }

			    int qty = Integer.parseInt(s.toString());
			    if (qty < 0) {
				mHolder.edtQty.setText("0");
				mHolder.edtQty.selectAll();
				entity.setQty(0);
			    } else {
				entity.setQty(qty);
			    }
			}
		    }

		}
		holder.edtQty.addTextChangedListener(new MyTextWatcher(holder));
		view.setTag(holder);
	    } else {
		holder = (ViewHolder) view.getTag();
		holder.edtQty.setTag(position);
	    }

	    final ColorSizeModel colorSize = mColorSizeList.get(position);
	    String colorName = colorSize.getColor().getName();
	    String sizeName = colorSize.getSize().getName();
	    int qty = colorSize.getQty();
	    holder.tvColor.setText(colorName);
	    holder.tvSize.setText(sizeName);
	    holder.edtQty.setText(String.valueOf(qty));
	    holder.edtQty.clearFocus();
	    if (index != -1 && index == position) {
		holder.edtQty.requestFocus();
	    }
	    // 删除
	    holder.lkbDelete.setTag(position);
	    holder.lkbDelete.setOnClickListener(new OnClickListener() {

		@Override
		public void onClick(View v) {
		    if (mListener != null) {
			int position = Integer.valueOf(v.getTag().toString());
			mListener.onItemRemove(position);
		    }
		}
	    });
	}
	return view;
    }

    private class ViewHolder {
	TextView tvColor, tvSize, lkbDelete;
	EditText edtQty;
    }

    public interface OnSpecQtyItemClickListener {
	void onItemRemove(int position);
    }
}
