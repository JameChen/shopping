package com.nahuo.quicksale.adapter;

import java.util.List;

import com.nahuo.quicksale.R;
import com.nahuo.quicksale.oldermodel.ContactModel;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

public class ContactItemAdapter extends BaseAdapter {
	
	public Context mContext;
	public List<ContactModel> mList;
	public UploadImageItemAdapter mImageAdapter;

	// 构造函数
	public ContactItemAdapter(Context Context, List<ContactModel> dataList) {
		mContext = Context;

		mList = dataList;
	}

	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public Object getItem(int arg0) {
		return mList.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {

		final ViewHolder holder;
		View view = arg1;
		if (mList.size() > 0) {
			String ID = mList.get(arg0).getID();
			String name = mList.get(arg0).getName();
			String content = mList.get(arg0).getContent();
			if (view == null) {
				view = LayoutInflater.from(mContext).inflate(
						R.layout.layout_contact_item, arg2, false);
				holder = new ViewHolder();

				holder.name = (TextView) view
						.findViewById(R.id.contact_item_name);
				holder.content = (TextView) view
						.findViewById(R.id.contact_item_content);
				holder.edt = (Button) view
						.findViewById(R.id.contact_item_edit);
				holder.delete = (Button) view
						.findViewById(R.id.contact_item_delete);
				holder.edt.setTag(holder);
				holder.delete.setTag(holder);
				view.setTag(holder);
			} else {
				holder = (ViewHolder) view.getTag();
			}
			holder.position = arg0;
			holder.name.setText(name);
			holder.content.setText(content);
			holder.edt.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					ViewHolder clickHolder = (ViewHolder) v.getTag();
					ContactModel data = mList.get(clickHolder.position);
					 
	                if (mListener != null) {
	                    mListener.onEdit(data.getID(),data.getName(),data.getContent());
	                }
				}
			});
			holder.delete.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					ViewHolder clickHolder = (ViewHolder) v.getTag();
					ContactModel data = mList.get(clickHolder.position);
					 
	                if (mListener != null) {
	                    mListener.onDelete(data.getID(),data.getName(),data.getContent());
	                }
				}
			});
		}

		return view;
	}

	class ViewHolder {
		int position;
		TextView name;
		TextView content;
		Button edt;
		Button delete;
	}
	
	/**
	 * 单击事件监听器
	 */
	private OnContactClickListener mListener = null;

	public void setOnContactClickListener(OnContactClickListener listener) {
		mListener = listener;
	}

    public interface OnContactClickListener {

        public void onEdit(String ID, String name, String content);
        public void onDelete(String ID, String name, String content);
    }
}


