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
import com.nahuo.quicksale.ContactPhoneEdtDialogFragment.OnSuccessListener;
import com.nahuo.quicksale.api.ShopSetAPI;
import com.nahuo.quicksale.oldermodel.PublicData;

/**
 * Description:修改手机号
 */
public class ContactQQEdtDialogFragment extends DialogFragment implements View.OnClickListener {

    private Context mContext;
    private LoadingDialog mLoading;
    private OkDialog mOklading;
    private OnSuccessListener mListener;

    private String ID;
    private String name;
    private String no;
    private EditText mEdtName;
    private EditText mEdtNo;

    public static ContactQQEdtDialogFragment newInstance(String _ID,String _name,String _no) {
        ContactQQEdtDialogFragment f = new ContactQQEdtDialogFragment();
        f.ID = _ID;
f.name = _name;
f.no = _no;
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
        View contentView = inflater.inflate(R.layout.dlg_contact_qq_edt, container, false);
        initView(contentView);
        return contentView;
    }

    private void initView(View contentView) {
    	mEdtName = (EditText) contentView.findViewById(R.id.layout_contact_qq_name);
    	mEdtName.setText(name);
    	mEdtNo = (EditText) contentView.findViewById(R.id.layout_contact_qq_no);
    	mEdtNo.setText(no);
        Button giveupBtn = (Button) contentView.findViewById(R.id.contact_qq_edt_cancel);
        Button submitBtn = (Button) contentView.findViewById(R.id.contact_qq_edt_save);
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
        	no = mEdtNo.getText().toString();
            mLoading.start();
        }

        @Override
        protected String doInBackground(Void... args) {

            try {
            	if (ID!=null && ID.length()>0)
            	{//修改
            		ShopSetAPI.getInstance().UpdateContact(ID, name, no, PublicData.getCookie(mContext));
            	}
            	else
            	{//新增
            		ShopSetAPI.getInstance().AddContact(2, name, no, PublicData.getCookie(mContext));
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
                    mListener.onSuccess(ID,name,no);
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
        case R.id.contact_qq_edt_cancel:
            dismiss();
            break;
        case R.id.contact_qq_edt_save:
            onSubmitClick();
            dismiss();
            break;
        default:
            showToast("on click missed");
            break;
        }
    }

    private void onSubmitClick() {
        if (TextUtils.isEmpty(mEdtNo.getText().toString())) {
            showToast("请输入QQ号");
            return;
        }

        SaveTask result = new SaveTask();
        result.execute();
    }

    private void showToast(String msg){
        Toast.makeText(mContext, msg, Toast.LENGTH_LONG).show();
    }
    public void setOnSuccessListener(OnSuccessListener listener) {
        this.mListener = listener;
    }

}
