package com.nahuo.quicksale;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.nahuo.library.helper.FunctionHelper;
import com.nahuo.library.utils.TimeUtils;
import com.nahuo.quicksale.base.BaseAppCompatActivity;
import com.nahuo.quicksale.common.Const;
import com.nahuo.quicksale.common.SpManager;
import com.nahuo.quicksale.common.StringUtils;
import com.nahuo.quicksale.oldermodel.PublicData;
import com.nahuo.quicksale.util.ActivityUtil;
import com.tencent.bugly.crashreport.CrashReport;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class StartActivity extends BaseAppCompatActivity {

    private static final String TAG = "StartActivity";
    private StartActivity vThis = this;

    private static final int STATE_GUIDE = 1; // 首次使用
    private static final int STATE_LOGIN = 2; // 进入登录界面
    private static final int STATE_MAIN = 3; // 进入主界面
    private static final int Wait_Time = 1000 * 3;
    private static final int Animator_Time = 1000 * 2;
    private ImageView ivBackground;
    private ProgressBar mProgressBar;
    private Intent intent;
    private String typeid = "", content = "";
    String cookie = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //BWApplication.getInstance().addStartActivity(this);
        if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
            finish();
            return;
        }
        setContentView(R.layout.activity_start);
        cookie = SpManager.getCookie(vThis);
        if (TextUtils.isEmpty(cookie))
            SpManager.setIs_Login(vThis, false);
        else
            SpManager.setIs_Login(vThis, true);
        intent = getIntent();
        Uri uri = intent.getData();
        if (uri != null) {
            typeid = uri.getQueryParameter("typeid");
            content = uri.getQueryParameter("content");
        }
        ivBackground = (ImageView) findViewById(android.R.id.background);
        mProgressBar = (ProgressBar) findViewById(R.id.main_progressbar);
        try {
            ivBackground.setImageResource(R.drawable.start);
           // startPropertyAnim(ivBackground);
        } catch (OutOfMemoryError error) {
            CrashReport.postCatchedException(error);
        }

        judeGoto();

//        Thread loadThread = new Thread(myRunnable);
//        loadThread.start();
        // handleOuterStart();
    }

    private void startPropertyAnim(final View view) {
        // 将直接把TextView这个view对象的透明度渐变。
        // 注意第二个参数："alpha"，指明了是透明度渐变属性动画
        // 透明度变化从1—>0.1—>1—>0.5—>1，TextView对象经历4次透明度渐变
        PropertyValuesHolder pv1 = PropertyValuesHolder.ofFloat("alpha", 0.1f, 0.25f, 0.5f, 0.75f, 1f);
//        PropertyValuesHolder pv2 = PropertyValuesHolder.ofFloat("scaleX", 0.1f, 0.5f, 1f);
//        PropertyValuesHolder pv3 = PropertyValuesHolder.ofFloat("scaleY", 0.1f, 0.5f, 1f);
       // PropertyValuesHolder pv4 = PropertyValuesHolder.ofFloat("rotation", 0f, 360f);
        ObjectAnimator.ofPropertyValuesHolder(view, pv1).setDuration(Animator_Time).start();
//        ObjectAnimator anim = ObjectAnimator.ofFloat(view, "alpha", 0.1f, 0.25f, 0.5f, 0.75f, 1f);
//        ObjectAnimator animatorx = ObjectAnimator.ofFloat(view, "scaleX", 0.1f, 0.5f, 1f);
//        ObjectAnimator animatory = ObjectAnimator.ofFloat(view, "scaleY", 0.1f, 0.5f, 1f);
//        ObjectAnimator rotate = ObjectAnimator.ofFloat(view, "rotation", 0f, 360f);
//        anim.setDuration(Wait_Time);// 动画持续时间
//        // 这里是一个回调监听，获取属性动画在执行期间的具体值
//        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//
//            @Override
//            public void onAnimationUpdate(ValueAnimator animation) {
//                float cVal = (Float) animation.getAnimatedValue();
//                view.setAlpha(cVal);
//                view.setScaleX(cVal);
//                view.setScaleY(cVal);
//                view.setRotation(cVal);
//            }
//        });
//
//        anim.start();
    }

    public void  judeGoto() {
        int state = 3;
        // 读取位置文件，判断程序是否首次使用
        boolean isFirst = SpManager.isFirstUseApp(vThis, FunctionHelper.GetAppVersionCode(this));
        if (isFirst) {
            Log.i(TAG, "进入引导界面");
            state = 1;
        } else {
            // 读取当前登录用户配置文件，存在Cookie信息时不在执行登录操作直接进入主界面，不存在则进入登录界面
            // String cookie = SpManager.getCookie(vThis);
            if (!TextUtils.isEmpty(cookie.trim())) {
                PublicData.setCookie(vThis, cookie.trim());
                PublicData.mUserInfo.setUserID(SpManager.getUserId(vThis));
                PublicData.mUserInfo.setUserName(SpManager.getUserName(vThis));
                PublicData.mUserInfo.setSignature(SpManager.getSignature(vThis));
                PublicData.mUserInfo.setAllAgentCount(SpManager.getAllagentcount(vThis));
                PublicData.mUserInfo.setAllItemCount(SpManager.getAllitemcount(vThis));
                Log.i(TAG, "Cookie值：" + PublicData.getCookie(vThis));
                state = 3;
            } else {
                Log.i(TAG, "进入登录界面");
                state = 2;
            }
        }
        mProgressBar.setVisibility(state == 3 ? ProgressBar.VISIBLE : ProgressBar.GONE);
        switch (state) {
            case STATE_GUIDE: // 首次使用，进入引导界面
                // startPropertyAnim(ivBackground);
                TimerTask timerTask = new TimerTask() {
                    @Override
                    public void run() {
                        toGuide();
                    }
                };
                Timer timer = new Timer();
                timer.schedule(timerTask, Wait_Time);

                break;
            case STATE_LOGIN: // 首次登录、用户已注销、自动登录失败，进入登录界面
                // startPropertyAnim(ivBackground);

                TimerTask timerTask2 = new TimerTask() {
                    @Override
                    public void run() {
                        toLogin();
                    }
                };
                Timer timer2 = new Timer();
                timer2.schedule(timerTask2, Wait_Time);
                break;
            case STATE_MAIN: // 自动登录进入主界面
                new MyTask().execute();
                break;
        }
    }

    //处理外部的启动
    private void handleOuterStart() {
        Uri uri = getIntent().getData();
        if (uri != null) {
            try {
                String urlString = uri.toString();
                String hostStr =
                        urlString.substring(urlString.indexOf(Const.getStartNormalHost())
                                + Const.getStartNormalHost().length());
                if (TextUtils.isEmpty(hostStr)) {
                    return;
                }
                hostStr = hostStr.substring(1);
                //扫码登录：weipu://main/login?uid=XXX
                if (hostStr.contains("?")) {
                    Map<String, String> params = StringUtils.getUrlParams(hostStr);
                    Const.setStartParam(params);
                } else {
                    Const.setStartModel(hostStr);
                }
            } catch (Exception e) {
                ViewHub.showLongToast(vThis, "非法的外部启动参数");
            }
        }
    }


    private Runnable myRunnable = new Runnable() {

        @Override
        public void run() {
            try {

                // 休眠500毫秒，避免程序出现白屏
                Thread.sleep(500);
                forward();
                // 复制bank and area数据库
                // new BankAndAreaDBHelper(vThis).createDataBase();
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    };

    @SuppressLint("HandlerLeak")
    private Handler myHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            mProgressBar.setVisibility(msg.what == 3 ? ProgressBar.VISIBLE : ProgressBar.GONE);
            switch (msg.what) {
                case STATE_GUIDE: // 进入引导界面
                    toGuide();
                    break;
                case STATE_LOGIN: // 进入登录界面
                    toLogin();
                    break;
                case STATE_MAIN: // 自动登录进入主界面
                    //延迟1.4s进入
                    //autoLogin();
                    Log.e("yu", "======");
                    new MyTask().execute();
                    break;
            }
        }
    };

    private void forward() {
        int state = 0;
        // 读取位置文件，判断程序是否首次使用
        boolean isFirst = SpManager.isFirstUseApp(vThis, FunctionHelper.GetAppVersionCode(this));
        if (isFirst) {
            Log.i(TAG, "进入引导界面");
            state = 1;
        } else {
            // 读取当前登录用户配置文件，存在Cookie信息时不在执行登录操作直接进入主界面，不存在则进入登录界面
            // String cookie = SpManager.getCookie(vThis);
            if (!TextUtils.isEmpty(cookie.trim())) {
                PublicData.setCookie(vThis, cookie.trim());
                PublicData.mUserInfo.setUserID(SpManager.getUserId(vThis));
                PublicData.mUserInfo.setUserName(SpManager.getUserName(vThis));
                PublicData.mUserInfo.setSignature(SpManager.getSignature(vThis));
                PublicData.mUserInfo.setAllAgentCount(SpManager.getAllagentcount(vThis));
                PublicData.mUserInfo.setAllItemCount(SpManager.getAllitemcount(vThis));
                Log.i(TAG, "Cookie值：" + PublicData.getCookie(vThis));
                state = 3;
            } else {
                Log.i(TAG, "进入登录界面");
                state = 2;
            }
        }
        switch (state) {
            case STATE_GUIDE: // 首次使用，进入引导界面
                myHandler.obtainMessage(STATE_GUIDE).sendToTarget();
                break;
            case STATE_LOGIN: // 首次登录、用户已注销、自动登录失败，进入登录界面
                myHandler.obtainMessage(STATE_LOGIN).sendToTarget();
                break;
            case STATE_MAIN: // 自动登录进入主界面
                myHandler.obtainMessage(STATE_MAIN).sendToTarget();
                break;
        }
    }

    /**
     * 进入引导界面
     */
    private void toGuide() {
        // 首次使用，记录首次使用时间等信息
        SpManager.setIsFirstUseApp(vThis, FunctionHelper.GetAppVersionCode(vThis), false);
        SpManager.setFirstUseTime(vThis, TimeUtils.dateToTimeStamp(new Date(), "yyyy-MM-dd HH:mm:ss"));
        // 进入引导界面
        Intent intent = new Intent(vThis, GuideActivity.class);
        startActivity(intent);
        //vThis.finish();
        //toOtherActivity(GuideActivity.class);
    }

    /**
     * 进入登录界面
     */
    private void toLogin() {
        //  toGuide();
        autoLogin();
//        Intent intent = new Intent(this, WXEntryActivity.class);
//        intent.putExtra(WXEntryActivity.EXTRA_TYPE, WXEntryActivity.Type.LOGIN);
//        // intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
//        startActivity(intent);
//        finish();// 需要finish这个页面，否则无法退出应用
    }


    /**
     * 自动登录，登录成功进入主界面
     */
    private void autoLogin() {
        Log.e("yu", "autoLogin");
        GotoMainActivty();

    }

    public void GotoMainActivty() {
        Map<String,Object> map=new HashMap<>();
        map.put("typeid", typeid);
        map.put("content", content);
        ActivityUtil.goToMainActivity(vThis,map);
        finish();
    }

    private void toOtherActivity(Class<?> clazz) {
        Intent intent = new Intent(vThis, clazz);
        startActivity(intent);
        finish();
    }

    //AsyncTask(异步进度条)
    private class MyTask extends AsyncTask<Void, Integer, Void> {
        int current;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            current = 0;
            mProgressBar.setMax(100);
            // startPropertyAnim(ivBackground);
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {

                while (current < 100) {
                    current += 2;
                    publishProgress(current); //这里的参数类型是 AsyncTask<Void, Integer, Void>中的Integer决定的，在onProgressUpdate中可以得到这个值去更新UI主线程，这里是异步线程
                    Thread.sleep(30);

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            mProgressBar.setProgress(values[0]);

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            autoLogin();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
//        StatService.onPause(this);
//        JPushInterface.onPause(vThis);
    }

    @Override
    public void onResume() {
        super.onResume();
//        StatService.onResume(this);
//        JPushInterface.onResume(vThis);
    }
}
