package com.nahuo.quicksale;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import com.baidu.mobstat.StatService;
import com.hyphenate.util.DateUtils;
import com.nahuo.library.helper.FunctionHelper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TradeLogSearchActivity extends BaseActivity implements View.OnClickListener {

    private Context mContext;

    private String beginTime,endTime,type = "";
    private TextView tvBeginTime,tvEndTime;
    private String name="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_trade_log_search);
//        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.layout_titlebar_default);
        mContext = this;
        if (getIntent()!=null){
            name=getIntent().getStringExtra(TradeLogActivity.EXTRA_TRADE_NAME);
        }
        initView();
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

    private void initView() {

        Button backBtn = (Button) findViewById(R.id.titlebar_btnLeft);
        backBtn.setVisibility(View.VISIBLE);
        backBtn.setOnClickListener(this);

        TextView mIvOK = (TextView) findViewById(R.id.titlebar_btnRight);
        mIvOK.setOnClickListener(this);
        mIvOK.setVisibility(View.VISIBLE);
        mIvOK.setText("确定");
        if (TextUtils.isEmpty(name)){
            ((TextView) findViewById(R.id.titlebar_tvTitle)).setText("筛选记录" );
        }else {
            ((TextView) findViewById(R.id.titlebar_tvTitle)).setText("筛选" + name);
        }
        tvBeginTime = (TextView)findViewById(R.id.trade_log_search_begintime);
        tvEndTime = (TextView)findViewById(R.id.trade_log_search_endtime);
        tvBeginTime.setOnClickListener(this);
        tvEndTime.setOnClickListener(this);
        DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd");
        Date begin = FunctionHelper.GetDateTime(-30);
        Date end = FunctionHelper.GetDateTime(0);
        beginTime = df1.format(begin);
        endTime = df1.format(end);
        tvBeginTime.setText("开始时间:"+beginTime);
        tvEndTime.setText("结束时间:"+endTime);

        findViewById(R.id.trade_log_search_t1).setOnClickListener(this);
        findViewById(R.id.trade_log_search_t2).setOnClickListener(this);
        findViewById(R.id.trade_log_search_t3).setOnClickListener(this);
        findViewById(R.id.trade_log_search_t4).setOnClickListener(this);
        findViewById(R.id.trade_log_search_t5).setOnClickListener(this);
        findViewById(R.id.trade_log_search_t6).setOnClickListener(this);
        findViewById(R.id.trade_log_search_t7).setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.titlebar_btnLeft:
            finish();
            break;
            case R.id.titlebar_btnRight:
                Intent intent = new Intent();
                intent.putExtra("begin",beginTime);
                intent.putExtra("end",endTime);
                intent.putExtra("type",type);
                setResult(1,intent);
finish();
                break;
            case R.id.trade_log_search_begintime: {
                Calendar calendar = Calendar.getInstance();
                DatePickerDialog dialog = new DatePickerDialog(mContext, new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        String time1 = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;

                        if (checkSearchTime(time1,endTime))
                        {
                            beginTime = time1;
                            tvBeginTime.setText("开始时间:" + beginTime);
                        }
                    }
                }, calendar.get(Calendar.YEAR), calendar
                        .get(Calendar.MONTH), calendar
                        .get(Calendar.DAY_OF_MONTH));
                dialog.show();
                break;
            }
            case R.id.trade_log_search_endtime: {
                Calendar calendar = Calendar.getInstance();
                DatePickerDialog dialog = new DatePickerDialog(mContext, new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        String time2 = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;

                        if (checkSearchTime(beginTime,time2))
                        {
                            endTime = time2;
                            tvEndTime.setText("结束时间:" + endTime);
                        }
                    }
                }, calendar.get(Calendar.YEAR), calendar
                        .get(Calendar.MONTH), calendar
                        .get(Calendar.DAY_OF_MONTH));
                dialog.show();
                break;
            }
            case R.id.trade_log_search_t1:
            case R.id.trade_log_search_t2:
            case R.id.trade_log_search_t3:
            case R.id.trade_log_search_t4:
            case R.id.trade_log_search_t5:
            case R.id.trade_log_search_t6:
            case R.id.trade_log_search_t7:
                type = v.getTag().toString();

                findViewById(R.id.trade_log_search_t1).setBackgroundResource(R.drawable.bg_rect_gray_corner);
                findViewById(R.id.trade_log_search_t2).setBackgroundResource(R.drawable.bg_rect_gray_corner);
                findViewById(R.id.trade_log_search_t3).setBackgroundResource(R.drawable.bg_rect_gray_corner);
                findViewById(R.id.trade_log_search_t4).setBackgroundResource(R.drawable.bg_rect_gray_corner);
                findViewById(R.id.trade_log_search_t5).setBackgroundResource(R.drawable.bg_rect_gray_corner);
                findViewById(R.id.trade_log_search_t6).setBackgroundResource(R.drawable.bg_rect_gray_corner);
                findViewById(R.id.trade_log_search_t7).setBackgroundResource(R.drawable.bg_rect_gray_corner);

                        v.setBackgroundResource(R.drawable.bg_rect_orange_corner);
                break;
        }
    }

    private boolean checkSearchTime(String time1,String time2)
    {
        Date begin = DateUtils.StringToDate(time1,"yyyy-MM-dd");
        Date end = DateUtils.StringToDate(time2,"yyyy-MM-dd");
        long second = begin.getTime() - end.getTime();
        long day = Math.abs(second/1000/60/60/24);
        if (day>365*2)
        {
            ViewHub.showLongToast(mContext,"搜索时间区间不能超过两年");
            return false;
        }
        return true;
    }

}