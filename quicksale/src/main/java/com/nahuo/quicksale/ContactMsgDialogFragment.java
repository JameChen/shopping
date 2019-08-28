package com.nahuo.quicksale;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;

import com.nahuo.quicksale.common.SpManager;

/**
 * Description:修改手机号
 */
public class ContactMsgDialogFragment extends DialogFragment implements View.OnClickListener {

    private Context mContext;

    private CheckBox mCbOverlook;
    public static ContactMsgDialogFragment newInstance() {
        ContactMsgDialogFragment f = new ContactMsgDialogFragment();
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setStyle(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Dialog);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.theme_dialog_fragment);
        mContext = getActivity();// .getBaseContext();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.dlg_contact_msg, container, false);
        initView(contentView);
        return contentView;
    }

    private void initView(View contentView) {
    	mCbOverlook = (CheckBox) contentView.findViewById(R.id.layout_contact_msg_checkbox);
        Button giveupBtn = (Button) contentView.findViewById(R.id.contact_msg_cancel);
        Button submitBtn = (Button) contentView.findViewById(R.id.contact_msg_set);
        giveupBtn.setOnClickListener(this);
        submitBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.contact_msg_cancel:
        	try {

            	if (mCbOverlook.isChecked())
            	{
            		SpManager.setContactMsgTip(mContext, false);
            	}
            	
                dismiss();
			} catch (Exception e) {
				e.printStackTrace();
			}
            break;
        case R.id.contact_msg_set:

        	try {

        	if (mCbOverlook.isChecked())
        	{
        		SpManager.setContactMsgTip(mContext, false);
        	}
        	
            Intent intent = new Intent(mContext, ContactActivity.class);
			ContactActivity.backToMain = false;
            startActivity(intent);
            dismiss();
			} catch (Exception e) {
			    e.printStackTrace();
			}
            break;
        default:
            break;
        }
    }
}
