package com.nahuo.quicksale;

import com.baidu.mobstat.StatService;
import com.nahuo.library.controls.UnderLineEditText;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

/**
 * Description:创建分组弹窗
 * 2014-7-20 下午3:39:58
 * 
 * @author ZZB
 */
public class CreateGroupDlgFragment extends DialogFragment implements View.OnClickListener {
    
    private Context mContext;
    private Listener mListener;
    private UnderLineEditText mETGroupName;
    private String mText;
    
    public static interface Listener{
        public void onSubmitClick(String groupName);
    }

    public static CreateGroupDlgFragment newInstance(String editText) {
        CreateGroupDlgFragment f = new CreateGroupDlgFragment();
        Bundle args = new Bundle();
        args.putString("edit_text", editText);
        f.setArguments(args);
        return f;
    }
    
    public void setListener(Listener listener){
        this.mListener = listener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setStyle(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Dialog);
        mContext = getActivity();
        mText = getArguments().getString("edit_text");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View contentView = LayoutInflater.from(mContext).inflate(R.layout.dlg_create_group,
                container, false);
        initContentView(contentView);
        return contentView;
    }

    private void initContentView(View contentView) {
        View submitBtn = contentView.findViewById(R.id.btn_submit);
        View cancleBtn = contentView.findViewById(R.id.btn_cancle);
        mETGroupName = (UnderLineEditText) contentView.findViewById(R.id.et_group_name);
        if(!TextUtils.isEmpty(mText)){
            mETGroupName.setText(mText);
        }
        submitBtn.setOnClickListener(this);
        cancleBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.btn_submit:
            if(mListener != null){
                mListener.onSubmitClick(mETGroupName.getText().toString());
            }
            break;
        case R.id.btn_cancle:
            dismiss();
            break;
        }

    }
    @Override
    public void onPause() {
        super.onPause();
        StatService.onPause(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        StatService.onResume(this);
    }
}
