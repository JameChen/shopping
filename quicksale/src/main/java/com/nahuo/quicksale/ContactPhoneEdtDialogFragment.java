package com.nahuo.quicksale;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.nahuo.library.controls.LoadingDialog;
import com.nahuo.library.controls.OkDialog;
import com.nahuo.library.helper.FunctionHelper;
import com.nahuo.quicksale.api.ShopSetAPI;
import com.nahuo.quicksale.oldermodel.PublicData;

/**
 * Description:修改手机号
 */
public class ContactPhoneEdtDialogFragment extends DialogFragment implements View.OnClickListener {

    private Context mContext;
    private LoadingDialog mLoading;
    private OkDialog mOklading;
    private OnSuccessListener mListener;

    private String ID;
    private String name;
    private String phoneNo;
    private EditText mEdtName;
    private EditText mEdtPhoneNo;

    public static ContactPhoneEdtDialogFragment newInstance(String _ID,String _name,String _phoneNo) {
        ContactPhoneEdtDialogFragment f = new ContactPhoneEdtDialogFragment();
f.ID = _ID;
f.name = _name;
f.phoneNo = _phoneNo;
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Dialog);
        mContext = getActivity();// .getBaseContext();
        mLoading = new LoadingDialog(mContext);
        mOklading = new OkDialog(mContext);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.dlg_contact_phone_edt, container, false);
        initView(contentView);
        return contentView;
    }

    private void initView(View contentView) {
    	mEdtName = (EditText) contentView.findViewById(R.id.layout_contact_phone_name);
    	mEdtName.setText(name);
    	mEdtPhoneNo = (EditText) contentView.findViewById(R.id.layout_contact_phone_no);
    	mEdtPhoneNo.setText(phoneNo);
        Button giveupBtn = (Button) contentView.findViewById(R.id.contact_phone_edt_cancel);
        Button submitBtn = (Button) contentView.findViewById(R.id.contact_phone_edt_save);
        giveupBtn.setOnClickListener(this);
        submitBtn.setOnClickListener(this);
    }

    public class SaveTask extends AsyncTask<Void, Void, String> {

        public SaveTask() {

            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll()
                    .build();

            StrictMode.setThreadPolicy(policy);
        }

        @Override
        protected void onPreExecute() {
        	name = mEdtName.getText().toString();
        	name = name.replaceAll("\r", "").replaceAll("\n", "") ; 
        	phoneNo = mEdtPhoneNo.getText().toString();
            mLoading.start();
        }

        @Override
        protected String doInBackground(Void... args) {

            try {
            	if (ID!=null && ID.length()>0)
            	{//修改
            		ShopSetAPI.getInstance().UpdateContact(ID, name, phoneNo, PublicData.getCookie(mContext));
            	}
            	else
            	{//新增
            		ShopSetAPI.getInstance().AddContact(1, name, phoneNo, PublicData.getCookie(mContext));
            	}
            	
                return "OK";
            } catch (Exception e) {
                e.printStackTrace();
                return e.toString();
            }
        }

        @Override
        protected void onPostExecute(String result) {
            mLoading.stop();
            if (result == "OK") {
                mOklading.start("设置成功");

                mOklading.stop(2500);
                dismiss();
                if (mListener != null) {
                    mListener.onSuccess(ID,name,phoneNo);
                }

                return;
            } else {
                showToast(result);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.contact_phone_edt_cancel:
            dismiss();
            break;
        case R.id.contact_phone_edt_save:
            onSubmitClick();
            
            break;
        default:
            showToast("on click missed");
            break;
        }
    }

    private void onSubmitClick() {
        String phoneNo = mEdtPhoneNo.getText().toString();
        if (TextUtils.isEmpty(phoneNo)) {
            ViewHub.setEditError(mEdtPhoneNo, "请输入手机号");
            return;
        }else if(!FunctionHelper.isPhoneNo(phoneNo)){
            ViewHub.setEditError(mEdtPhoneNo, "请输入正确的手机号码");
            return;
        }

        SaveTask result = new SaveTask();
        result.execute();
        dismiss();
    }

    private void showToast(String msg){
        Toast.makeText(mContext, msg, Toast.LENGTH_LONG).show();
    }
    
    public void setOnSuccessListener(OnSuccessListener listener) {
        this.mListener = listener;
    }

    public interface OnSuccessListener {

        /** 分享保存 */
        public void onSuccess(String ID, String name, String content);
    }
}
