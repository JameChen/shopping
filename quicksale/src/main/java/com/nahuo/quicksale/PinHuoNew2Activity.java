//package com.nahuo.quicksale;
//
//import android.content.Context;
//import android.hardware.Sensor;
//import android.hardware.SensorManager;
//import android.os.AsyncTask;
//import android.os.Bundle;
//import android.view.View;
//import android.view.Window;
//import android.widget.TextView;
//
//import com.nahuo.library.controls.LoadingDialog;
//import com.nahuo.quicksale.adapter.PinHuoNewAdapter2;
//import com.nahuo.quicksale.api.HttpRequestHelper;
//import com.nahuo.quicksale.api.OrderAPI;
//import com.nahuo.quicksale.api.QuickSaleApi;
//import com.nahuo.quicksale.api.SimpleHttpRequestListener;
//import com.nahuo.quicksale.base.BaseAppCompatActivity;
//import com.nahuo.quicksale.common.SpManager;
//import com.nahuo.quicksale.eventbus.BusEvent;
//import com.nahuo.quicksale.eventbus.EventBusId;
//import com.nahuo.quicksale.jcvideoplayer_lib.JCVideoPlayer;
//import com.nahuo.quicksale.model.PinHuoModel;
//import com.nahuo.quicksale.model.RequestEntity;
//import com.nahuo.quicksale.model.ResultData;
//import com.nahuo.quicksale.model.ScoreModel;
//import com.nahuo.quicksale.model.quicksale.PinHuoNewResultModel;
//import com.nahuo.quicksale.mvp.view.PinHuoView;
//import com.nahuo.quicksale.provider.UserInfoProvider;
//
//import java.util.ArrayList;
//
//import de.greenrobot.event.EventBus;
//
///**
// * 拼货
// *
// * @author James Chen
// * @create time in 2017/3/24 16:20
// */
//public class PinHuoNew2Activity extends BaseAppCompatActivity implements PinHuoView {
//
//    private LoadingDialog mLoadingDialog;
//
//    private Context mContent = this;
//    private boolean mIsLoading;
//    private int curCategoryID = 0;
//    private PinHuoNewResultModel result1;
//    public static View empty_view;
//    private TextView tv_empty;
//    SensorManager sensorManager;
//    JCVideoPlayer.JCAutoFullscreenListener sensorEventListener;
//    public boolean isStartFragment;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        setContentView(R.layout.activity_pin_huo_new2);
//        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
//        sensorEventListener = new JCVideoPlayer.JCAutoFullscreenListener();
//        EventBus.getDefault().registerSticky(this);
//        initViews();
//        loadData(true);
//
//    }
//
//    @Override
//    public void onBackPressed() {
//        if (JCVideoPlayer.backPress()) {
//            return;
//        }
//        super.onBackPressed();
//    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
//        JCVideoPlayer.JC_TYPE = JCVideoPlayer.IS_PINHUO_MAIN_LIST_TYPE;
//        new Task().execute();
//        PinHuoNewAdapter2.isPause = false;
//        Sensor accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
//        sensorManager.registerListener(sensorEventListener, accelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);
//    }
//
//    private class Task extends AsyncTask<Object, Void, Object> {
//
//        @Override
//        protected void onPreExecute() {
//
//        }
//
//        @Override
//        protected Object doInBackground(Object... params) {
//            try {
//                return OrderAPI.getScore(PinHuoNew2Activity.this);
//            } catch (Exception e) {
//                return null;
//            }
//        }
//
//        @Override
//        protected void onPostExecute(Object result) {
//            if (result != null) {
//                ScoreModel data = (ScoreModel) result;
//                int statuId = data.getAuthInfo().getStatuID();
//                int score = data.getPoint();
//                SpManager.setScore(mContent, score);
//                SpManager.setStatuId(mContent, statuId);
//            }
//        }
//    }
//
//
//    private void replaceFragment(PinHuoNewResultModel result) {
//        isStartFragment=true;
//        getSupportFragmentManager().beginTransaction().replace(R.id.container, PinHuoNew2MainFragment.getInstance(result)).commitAllowingStateLoss();
//    }
//
//    @Override
//    public void onPause() {
//        super.onPause();
//        PinHuoNewAdapter2.isPause = true;
//        sensorManager.unregisterListener(sensorEventListener);
//        JCVideoPlayer.releaseAllVideos();
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        EventBus.getDefault().unregister(this);
//    }
//
//    private void initViews() {
//        mLoadingDialog = new LoadingDialog(this);
//        empty_view = findViewById(R.id.empty_view);
//        tv_empty = (TextView) findViewById(R.id.tv_empty);
//        tv_empty.setText(R.string.pin_huo_net_error);
//        empty_view.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                loadData(true);
//            }
//        });
//    }
//
//    protected <T extends View> T $(int id) {
//        return (T) findViewById(id);
//    }
//
//    private void loadData(boolean showLoading) {
//        if (mIsLoading) {
//            return;
//        }
////        loadCount ++;
//        mIsLoading = true;
//        if (showLoading) {
//            showLoading();
//        }
//        QuickSaleApi.getPinHuoNewList(new RequestEntity(this, new HttpRequestHelper(), new SimpleHttpRequestListener() {
//            @Override
//            public void onRequestSuccess(String method, Object object) {
//                try {
//                    loadFinished();
//                    PinHuoNewResultModel newResult = (PinHuoNewResultModel) object;
//                    if (result1 != null) {
//
//                        EventBus.getDefault().post(BusEvent.getEvent(EventBusId.LOAD_PIN_HUO_FINISHED, newResult));
//                    } else {
//                        result1 = newResult;
//                        replaceFragment(newResult);
//
//                    }
//                    setEmpty(newResult);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onRequestFail(String method, int statusCode, String msg) {
//                loadFinished();
//                onLoadPinAndForecastFailed();
//                try {
//                    setEmpty(result1);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onRequestExp(String method, String msg, ResultData data) {
//                loadFinished();
//                onLoadPinAndForecastFailed();
//                try {
//                   setEmpty(result1);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                if (data.getCode().equals("AccountError")) {
//                    ViewHub.showLongToast(mContent, "禁止登录");
//                    UserInfoProvider.exitApp(mContent);
//                }
//            }
//        }), curCategoryID, 1, 100);
//    }
//
//    private void setEmpty(PinHuoNewResultModel newResult) {
//        if (newResult != null) {
//
//            if (newResult.ActivityList == null || newResult.ActivityList.size() == 0) {
//                if (!isStartFragment)
//                empty_view.setVisibility(View.VISIBLE);
//            } else {
//                empty_view.setVisibility(View.GONE);
//
//            }
//        } else {
//            if (!isStartFragment)
//                empty_view.setVisibility(View.VISIBLE);
//
//        }
//    }
//
//
//    @Override
//    public void showLoading() {
//        mLoadingDialog.start();
//    }
//
//    @Override
//    public void hideLoading() {
//        mLoadingDialog.stop();
//    }
//
//    private void loadFinished() {
//        hideLoading();
//        mIsLoading = false;
////        EventBus.getDefault().post(BusEvent.getEvent(EventBusId.LOAD_PIN_HUO_FINISHED));
//    }
//
//    @Override
//    public void onLoadPinAndForecastFailed() {
//        loadFinished();
//        ViewHub.showShortToast(this, "加载失败，请稍候再试");
//    }
//
//    @Override
//    public void onAllDataLoaded(ArrayList<PinHuoModel> pinList,
//                                ArrayList<PinHuoModel> forecastList,
//                                ArrayList<PinHuoModel> historyList) {
//
//    }
//
//
//    public void onEventMainThread(BusEvent event) {
//        switch (event.id) {
//            case EventBusId.REFRESH_ALL_ITEMS:// 刷新
//                int catID = (int) event.data;
//                if (catID > 0) {
//                    curCategoryID = catID;
//                }
//                loadData(true);
//                break;
//            case EventBusId.REFRESH_PIN_HUO:
//                loadData(true);
//                break;
//        }
//    }
//}
