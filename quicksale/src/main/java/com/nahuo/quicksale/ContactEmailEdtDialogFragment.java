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
public class ContactEmailEdtDialogFragment extends DialogFragment implements View.OnClickListener {

    private Context mContext;
    private LoadingDialog mLoading;
    private OkDialog mOklading;
    private OnSuccessListener mListener;

    private String ID;
    private String email;
    private EditText mEdtEmail;

    public static ContactEmailEdtDialogFragment newInstance(String _ID,String _name,String _email) {
        ContactEmailEdtDialogFragment f = new ContactEmailEdtDialogFragment();
        f.ID = _ID;
f.email = _email;
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
        View contentView = inflater.inflate(R.layout.dlg_contact_email_edt, container, false);
        initView(contentView);
        return contentView;
    }

    private void initView(View contentView) {
    	mEdtEmail = (EditText) contentView.findViewById(R.id.layout_contact_email_name);
    	mEdtEmail.setText(email);
        Button giveupBtn = (Button) contentView.findViewById(R.id.contact_email_edt_cancel);
        Button submitBtn = (Button) contentView.findViewById(R.id.contact_email_edt_save);
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
        	email = mEdtEmail.getText().toString();
            mLoading.start();
        }

        @Override
        protected String doInBackground(Void... args) {

            try {
            	if (ID!=null && ID.length()>0)
            	{//修改
            		ShopSetAPI.getInstance().UpdateContact(ID, "", email, PublicData.getCookie(mContext));
            	}
            	else
            	{//新增
            		ShopSetAPI.getInstance().AddContact(4, "", email, PublicData.getCookie(mContext));
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
                    mListener.onSuccess(ID,"",email);
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
        case R.id.contact_email_edt_cancel:
            dismiss();
            break;
        case R.id.contact_email_edt_save:
            onSubmitClick();
            dismiss();
            break;
        default:
            showToast("on click missed");
            break;
        }
    }

    private void onSubmitClick() {
        if (TextUtils.isEmpty(mEdtEmail.getText().toString())) {
            showToast("请输入邮箱");
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
