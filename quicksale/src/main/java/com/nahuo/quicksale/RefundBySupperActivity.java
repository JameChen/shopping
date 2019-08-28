package com.nahuo.quicksale;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.nahuo.library.controls.LoadingDialog;
import com.nahuo.quicksale.api.OrderAPI;
import com.nahuo.quicksale.eventbus.BusEvent;
import com.nahuo.quicksale.eventbus.EventBusId;
import com.nahuo.quicksale.oldermodel.RefundPickingBillModel;

import de.greenrobot.event.EventBus;

/**
 * 卖家退款详细
 */
public class RefundBySupperActivity extends BaseSlideBackActivity implements OnClickListener {

    private static enum Step {
        LOAD_INFO
    }

    private LoadingDialog          mDialog;
    private RefundBySupperActivity vThis     = this;
    private RefundPickingBillModel mBill     = null;
    private long                   mId;
    // private FragmentTransaction transaction = null;
    private EventBus               mEventBus = EventBus.getDefault();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);// 设置自定义标题栏

       // requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_refund_seller);
       // getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.layout_titlebar_default);
        // getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.layout_titlebar_default);// 更换自定义标题栏布局
        initView();
        mEventBus.register(this);
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();

        mEventBus.unregister(this);
    }

    private void initTitlebar() {
        // 标题栏
        TextView tvTitle = (TextView)findViewById(R.id.titlebar_tvTitle);
        tvTitle.setText("供货商退款");
        Button btnLeft = (Button)findViewById(R.id.titlebar_btnLeft);
        btnLeft.setText(R.string.titlebar_btnBack);
        btnLeft.setVisibility(View.VISIBLE);
        btnLeft.setOnClickListener(this);
    }

    // 初始化view

    private void initView() {

        mId = getIntent().getIntExtra("ID", 0);
        initTitlebar();
        mDialog = new LoadingDialog(vThis);

        new Task(Step.LOAD_INFO).execute();

    }

    @SuppressWarnings("unused")
    private class Task extends AsyncTask<Object, Void, Object> {
        private Step mStep;

        public Task(Step step) {
            mStep = step;
        }

        @Override
        protected void onPreExecute() {
            switch (mStep) {
                case LOAD_INFO:
                    mDialog.start("加载供货商退款订单详情");
                    break;
            }
        }

        @Override
        protected Object doInBackground(Object... arg0) {
            // TODO Auto-generated method stub
            try {
                switch (mStep) {
                    case LOAD_INFO:
                        mBill = OrderAPI.getrundbysupper(vThis, mId, 2);
                        return mBill;

                }
            } catch (Exception e) {
                e.printStackTrace();
                return "error:" + e.getMessage();
            }
            return null;
        }

        protected void onPostExecute(Object result) {

            if (mDialog.isShowing()) {
                mDialog.dismiss();
            }
            if (result instanceof String && ((String)result).startsWith("error:")) {
                ViewHub.showLongToast(vThis, ((String)result).replace("error:", ""));
            } else {
                switch (mStep) {
                    case LOAD_INFO:
                        RefundinfoBySupperFragment frag = RefundinfoBySupperFragment
                                .newInstance((RefundPickingBillModel)result);
                        FragmentManager fm = getFragmentManager();
                        FragmentTransaction transaction = fm.beginTransaction();
                        transaction.replace(R.id.frame_refund_Seller, frag);
                        transaction.commit();
                        break;
                }
            }

        }

    }

    public void onEventMainThread(BusEvent event) {
        switch (event.id) {
            case EventBusId.REFUND_SUPP_AGRESS:
                try {
                    // 走一发
                    new Task(Step.LOAD_INFO).execute();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

        }
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.titlebar_btnLeft:
                finish();
                break;

            default:
                break;
        }
    }

}
