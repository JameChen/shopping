package com.nahuo.quicksale.controls;

import com.nahuo.library.helper.FunctionHelper;
import com.nahuo.quicksale.R;
import com.nahuo.quicksale.adapter.ColorGridItemAdapter;
import com.nahuo.quicksale.adapter.SizeGridItemAdapter;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.PopupWindow;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ProgressBar;
import android.widget.TextView;

public class ColorSizeSelectMenu extends PopupWindow implements OnItemClickListener, View.OnClickListener {

    private Activity                 mActivity;
    private View                     mRootView;
    private GridView                 mGridView;
    private TextView                 mTvTitle;
    private EditText                 mEtInput;
    private ProgressBar              mPbInput;
    private Button                   mBtnAdd, mBtnOk, mBtnDel;
    // private List<String> mItems = new ArrayList<String>();
    // private List<Boolean> mSelect = new ArrayList<Boolean>();
    // private OnClickListener mAddListener;
    private ColorSizeOperateCallback mSelectedCallback;
    // private MAdapter mAdapter;
    private String                   mTitle = "";
    private View                     mMenuContent;
    private ColorGridItemAdapter     mColorAdatper;
    private SizeGridItemAdapter      mSizeAdapter;

    public ColorSizeSelectMenu(Activity activity) {
        super();
        this.mActivity = activity;
        initViews();
    }

    public ColorSizeSelectMenu(Activity activity, AttributeSet atrr) {
        super();
        this.mActivity = activity;
        initViews();
    }

    private void initViews() {

        mRootView = mActivity.getLayoutInflater().inflate(R.layout.color_size_select_menu, null);
        mMenuContent = mRootView.findViewById(R.id.menu_content);
        mTvTitle = (TextView)mRootView.findViewById(android.R.id.text1);
        mGridView = (GridView)mRootView.findViewById(android.R.id.list);
        mEtInput = (EditText)mRootView.findViewById(android.R.id.edit);
        mPbInput = (ProgressBar)mRootView.findViewById(android.R.id.progress);
        mBtnAdd = (Button)mRootView.findViewById(android.R.id.button1);
        mBtnDel = (Button)mRootView.findViewById(android.R.id.button2);
        mBtnOk = (Button)mRootView.findViewById(android.R.id.button3);

        mGridView.setOnItemClickListener(this);
        mBtnAdd.setOnClickListener(this);
        mBtnDel.setOnClickListener(this);
        mBtnOk.setOnClickListener(this);

    }

    public ColorSizeSelectMenu setTitle(String title) {
        this.mTitle = title;
        return this;
    }

    public ColorSizeSelectMenu setInputHint(String hint) {
        mEtInput.setHint(hint);
        return this;
    }

    public void showProgress(boolean show) {
        mPbInput.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    // public ColorSizeSelectMenu setItems(List<String> items) {
    // this.mItems = items;
    // for (int i = 0; i < mItems.size(); i++) {
    // mSelect.add(false);
    // }
    // return this;
    // }

    public ColorSizeSelectMenu setColorAdatper(ColorGridItemAdapter colorAdapter) {
        this.mColorAdatper = colorAdapter;
        return this;
    }

    public ColorGridItemAdapter getColorGridItemAdapter() {
        return mColorAdatper;
    }

    public ColorSizeSelectMenu setSizeAdatper(SizeGridItemAdapter sizeAdapter) {
        this.mSizeAdapter = sizeAdapter;
        return this;
    }

    public SizeGridItemAdapter getSizeGridItemAdapter() {
        return mSizeAdapter;
    }

    // public ColorSizeSelectMenu setAddClickListener(View.OnClickListener listener) {
    // mAddListener = listener;
    // return this;
    // }

    public void show(View v) {
        int[] location = new int[2];
        v.getLocationOnScreen(location);
        this.setWidth(LayoutParams.MATCH_PARENT);
        this.setHeight(LayoutParams.MATCH_PARENT);
        this.setContentView(mRootView);
        this.setFocusable(true);
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        this.setBackgroundDrawable(dw);
        this.setFocusable(true);

        mRootView.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                int height = mMenuContent.getTop();
                int y = (int)event.getY();
                if (event.getAction() == MotionEvent.ACTION_UP && y < height) {
                    dismiss();
                }
                return true;
            }
        });
        mTvTitle.setText(mTitle);
        if (mColorAdatper != null) {
            mGridView.setAdapter(mColorAdatper);
        } else if (mSizeAdapter != null) {
            mGridView.setAdapter(mSizeAdapter);
        }
        // else {
        // MAdapter mAdapter = new MAdapter();
        // mGridView.setAdapter(mAdapter);
        // }

        setAnimationStyle(R.style.BottomMenuAnim);
        showAtLocation(v, Gravity.NO_GRAVITY, location[0], location[1] - getHeight());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case android.R.id.button1:// add
                if (mSelectedCallback != null) {
                    mSelectedCallback.addItem(mEtInput.getText().toString());
                }
                FunctionHelper.hideSoftInput(mEtInput.getWindowToken(), mActivity);
                mEtInput.setText("");
                break;
            case android.R.id.button2:// delete
                if (mSelectedCallback != null) {
                    mSelectedCallback.deleteItems();
                    // List<Integer> ps = new ArrayList<Integer>();
                    // List<String> texts = new ArrayList<String>();
                    // for (int i = mSelect.size() - 1; i >= 0; i--) {
                    // if (mSelect.get(i).booleanValue()) {
                    // ps.add(i);
                    // texts.add(mItems.get(i));
                    // }
                    // }
                    // // int[] intArray = ArrayUtils.toPrimitive(myList.toArray(new Integer[myList.size()]));
                    // int[] positions = new int[ps.size()];
                    // for (int i = 0; i < positions.length; i++) {
                    // positions[i] = ps.get(i).intValue();
                    // }
                    // mSelectedCallback.deleteItems(positions, (String[])texts.toArray(new String[texts.size()]));
                }
                break;
            case android.R.id.button3:// ok
                if (mSelectedCallback != null) {
                    mSelectedCallback.selectedItems();
                    // List<Integer> ps = new ArrayList<Integer>();
                    // List<String> texts = new ArrayList<String>();
                    // for (int i = mSelect.size() - 1; i >= 0; i--) {
                    // if (mSelect.get(i).booleanValue()) {
                    // ps.add(i);
                    // texts.add(mItems.get(i));
                    // }
                    // }
                    // // int[] intArray = ArrayUtils.toPrimitive(myList.toArray(new Integer[myList.size()]));
                    // int[] positions = new int[ps.size()];
                    // for (int i = 0; i < positions.length; i++) {
                    // positions[i] = ps.get(i).intValue();
                    // }
                    // mSelectedCallback.selectedItems(positions, (String[])texts.toArray(new String[texts.size()]));
                }
                dismiss();
                break;
            default:
                break;
        }
    }

    public ColorSizeSelectMenu setOperateCallback(ColorSizeOperateCallback callback) {
        this.mSelectedCallback = callback;
        return this;
    }

    // public void addItem(String item) {
    // mItems.add(item);
    // mSelect.add(false);
    // // mAdapter.notifyDataSetChanged();
    // }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {}

    public interface ColorSizeOperateCallback {

        public void addItem(String text);

        public void deleteItems();

        public void selectedItems();
    }

    // private class MAdapter extends BaseAdapter {
    // @Override
    // public int getCount() {
    // return mItems.size();
    // }
    //
    // @Override
    // public Object getItem(int position) {
    // return mItems.get(position);
    // }
    //
    // @Override
    // public long getItemId(int position) {
    // return position;
    // }
    //
    // @Override
    // public View getView(final int position, View convertView, ViewGroup parent) {
    // ViewHolder holder;
    // if (null == convertView) {
    // convertView = LayoutInflater.from(mActivity).inflate(R.layout.color_size_select_menu_item, null);
    // holder = new ViewHolder();
    // holder.checkBox = (CheckBox)convertView.findViewById(android.R.id.checkbox);
    // convertView.setTag(holder);
    // } else {
    // holder = (ViewHolder)convertView.getTag();
    // }
    // holder.checkBox.setText(mItems.get(position));
    // holder.checkBox.setChecked(mSelect.get(position).booleanValue());
    //
    // holder.checkBox.setOnClickListener(new OnClickListener() {
    // @Override
    // public void onClick(View v) {
    // CheckBox cb = (CheckBox)v;
    // mSelect.set(position, cb.isChecked());
    // // cb.setChecked(!cb.isChecked());
    // }
    // });
    // return convertView;
    // }
    // }
    // private final static class ViewHolder {
    // private CheckBox checkBox;
    // }
}
