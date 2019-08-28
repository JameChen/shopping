package com.nahuo.quicksale;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.baidu.mobstat.StatService;

/**
 * @description 编辑添加微询等
 * @created 2014-12-16 上午10:00:55
 * @author ZZB
 */
public class WeiXunActivity extends BaseSlideBackActivity implements View.OnClickListener {

    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_weixun);
        //getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.layout_titlebar_default);

        initView();
    }

    private void initView() {
        initTitleBar();
        mListView = (ListView) findViewById(R.id.list_view);
    }

    private void initTitleBar() {
        TextView tvTitle = (TextView) findViewById(R.id.titlebar_tvTitle);
        tvTitle.setText("微询");

        Button btnLeft = (Button) findViewById(R.id.titlebar_btnLeft);
        btnLeft.setText(R.string.titlebar_btnBack);
        btnLeft.setVisibility(View.VISIBLE);
        btnLeft.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.titlebar_btnLeft:
            finish();
            break;
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
}
