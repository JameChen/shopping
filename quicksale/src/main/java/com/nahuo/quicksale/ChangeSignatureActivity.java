package com.nahuo.quicksale;

import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mobstat.StatService;
import com.nahuo.library.controls.LoadingDialog;
import com.nahuo.quicksale.api.AccountAPI;
import com.nahuo.quicksale.api.ApiHelper;
import com.nahuo.quicksale.common.SpManager;
import com.nahuo.quicksale.oldermodel.PublicData;

/**
 * Description：修改签名页
 */
public class ChangeSignatureActivity extends BaseSlideBackActivity implements OnClickListener {

    private static final String TAG = "ChangeSignatureActivity";
    private ChangeSignatureActivity vThis = this;
    private EditText mEtSignature;
    private LoadingDialog loadingDialog = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);// 设置自定义标题栏
        setContentView(R.layout.activity_change_signature);
//        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
//                R.layout.layout_titlebar_default);// 更换自定义标题栏布局

        loadingDialog = new LoadingDialog(vThis);
        initView();
    }

    private void initView() {
        initTitleBar();

        mEtSignature = (EditText) findViewById(R.id.change_signature_text);
        String signature = SpManager.getSignature(this);
        mEtSignature.setText(signature);
        mEtSignature.setSelection(signature.length());
    }

    private void initTitleBar() {
        TextView tvTitle = (TextView) findViewById(R.id.titlebar_tvTitle);
        tvTitle.setText("修改签名");

        Button btnLeft = (Button) findViewById(R.id.titlebar_btnLeft);
        btnLeft.setText(R.string.titlebar_btnBack);
        btnLeft.setVisibility(View.VISIBLE);
        btnLeft.setOnClickListener(this);
        Button btnRight = (Button) findViewById(R.id.titlebar_btnRight);
        btnRight.setText("保存");
        btnRight.setVisibility(View.VISIBLE);
        btnRight.setOnClickListener(this);

    }

    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.titlebar_btnLeft:
            finish();
            break;
        case R.id.titlebar_btnRight:
            submitSignature();
            break;
        }
    }

    /**
     * Description:提交签名
     */
    private void submitSignature() {
        String result = validateInput();
        if ("ok".equals(result)) {

            SaveDataTask sdt = new SaveDataTask();
            sdt.execute((Void) null);

        } else {
            Toast.makeText(this, result, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Description:校验输入
     */
    private String validateInput() {
        String result = "";
        if (TextUtils.isEmpty(mEtSignature.getText().toString())) {
            result = "请输入签名";
        } else {
            result = "ok";
        }
        return result;
    }

    /**
     * 保存个性设置
     * */
    private class SaveDataTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            try {
                boolean success = AccountAPI.getInstance().setSignature(
                        mEtSignature.getText().toString(), PublicData.getCookie(vThis));
                if (success)
                {
                    return "OK";
                }
                else
                {
                    return "保存失败";
                }
            } catch (Exception ex) {
                Log.e(TAG, "无法更新签名");
                ex.printStackTrace();
                return ex.getMessage() == null ? "未知异常" : ex.getMessage();
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loadingDialog.start(getString(R.string.me_loading));
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            loadingDialog.stop();

            if (result.equals("OK")) {

                PublicData.mUserInfo.setSignature(mEtSignature.getText().toString());

                SpManager.setSignature(vThis, PublicData.mUserInfo.getSignature());
                setResult(RESULT_OK);
                Toast.makeText(vThis, "签名保存成功", Toast.LENGTH_LONG).show();
            } else {
                // 验证result
                if (result.toString().startsWith("401")
                        || result.toString().startsWith("not_registered")) {
                    Toast.makeText(vThis, result.toString(), Toast.LENGTH_LONG)
                            .show();
                    ApiHelper.checkResult(result, vThis);
                } else {
                    Toast.makeText(vThis, result, Toast.LENGTH_LONG).show();
                }
            }
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

    @Override
    public void finish() {
        super.finish();
        ViewHub.hideKeyboard(this);
    }
}
