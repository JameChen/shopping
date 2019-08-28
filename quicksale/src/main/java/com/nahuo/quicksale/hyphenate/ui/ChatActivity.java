package com.nahuo.quicksale.hyphenate.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;

import com.google.gson.internal.LinkedTreeMap;
import com.hyphenate.util.EasyUtils;
import com.nahuo.quicksale.R;
import com.nahuo.quicksale.activity.MainNewActivity;
import com.nahuo.quicksale.app.BWApplication;
import com.nahuo.quicksale.common.SpManager;
import com.nahuo.quicksale.di.module.HttpManager;
import com.nahuo.quicksale.hyphenate.runtimepermissions.PermissionsManager;
import com.nahuo.quicksale.model.http.CommonSubscriber;
import com.nahuo.quicksale.model.http.response.PinHuoResponse;
import com.nahuo.quicksale.util.JsonKit;
import com.nahuo.quicksale.util.RxUtil;

import org.json.JSONObject;

/**
 * chat activity，EaseChatFragment was used {@link #}
 */
public class ChatActivity extends BaseAppCompatActicity {
    public static ChatActivity activityInstance;
    public static String TAG = ChatActivity.class.getSimpleName();
    private com.nahuo.quicksale.hyphenate.ui.EaseChatFragment chatFragment;
    String toChatUsername;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.em_activity_chat);
        activityInstance = this;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(BWApplication.getInstance(), Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                this.requestPermissions(new String[]{Manifest.permission.RECORD_AUDIO},
                        1111);
            }
        }
        //get user id or group id
        toChatUsername = getIntent().getExtras().getString("userId");
        getSendext();
        //use EaseChatFratFragment
        chatFragment = new ChatFragment();
        //pass parameters to chat fragment
        chatFragment.setArguments(getIntent().getExtras());
        getSupportFragmentManager().beginTransaction().add(R.id.container, chatFragment).commit();

    }

    protected void getSendext() {

        addSubscribe(HttpManager.getInstance().getPinHuoNoCacheApi(TAG).getSendext()
                .compose(RxUtil.<PinHuoResponse<Object>>rxSchedulerHelper())
                .compose(RxUtil.<Object>handleResult())
                .subscribeWith(new CommonSubscriber<Object>(this) {
                    @Override
                    public void onNext(Object object) {
                        super.onNext(object);
                        try {
                            LinkedTreeMap map = (LinkedTreeMap) object;
                            String json = JsonKit.mapToJson(map, null).toString();
                            JSONObject jsonObject = new JSONObject(json);
                            String AgentUserName = jsonObject.optString("AgentUserName");
                            String QueueName = jsonObject.optString("QueueName");
                            SpManager.setECC_AGENT_USER_NAME(activityInstance, AgentUserName);
                            SpManager.setECC_QUEUE_NAME(activityInstance, QueueName);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        super.onComplete();
                    }
                }));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        activityInstance = null;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        // make sure only one chat activity is opened
        String username = intent.getStringExtra("userId");
        if (toChatUsername.equals(username))
            super.onNewIntent(intent);
        else {
            finish();
            startActivity(intent);
        }

    }

    @Override
    public void onBackPressed() {
        chatFragment.onBackPressed();
        if (EasyUtils.isSingleActivity(this)) {
            Intent intent = new Intent(this, MainNewActivity.class);
            startActivity(intent);
        }
    }

    public String getToChatUsername() {
        return toChatUsername;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        PermissionsManager.getInstance().notifyPermissionsChange(permissions, grantResults);
    }
}
