package com.nahuo.quicksale;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.baidu.mobstat.StatService;

public class WPAgreeUseActivity extends BaseActivity {

	private WPAgreeUseActivity vThis = this;

	private TextView tvTitle;
	private Button btnLeft;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);// 设置自定义标题栏
		setContentView(R.layout.activity_wp_agree_use);
//		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
//				R.layout.layout_titlebar_default);// 更换自定义标题栏布局

		// 标题栏
		tvTitle = (TextView) findViewById(R.id.titlebar_tvTitle);
		btnLeft = (Button) findViewById(R.id.titlebar_btnLeft);
		btnLeft.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
		tvTitle.setText("使用协议");
		btnLeft.setVisibility(View.VISIBLE);
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
