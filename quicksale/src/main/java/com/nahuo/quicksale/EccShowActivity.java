//package com.nahuo.quicksale;
//
//import android.os.Bundle;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.widget.Button;
//import android.widget.CheckBox;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import com.baidu.mobstat.StatService;
//import com.nahuo.quicksale.common.ChatHelper;
//import com.nahuo.quicksale.common.Const;
//import com.nahuo.quicksale.common.SpManager;
//import com.squareup.picasso.Picasso;
//
//public class EccShowActivity extends BaseActivity2 implements OnClickListener {
//
//	private EccShowActivity vThis = this;
//
//	private ImageView ivLogo,ivChat;
//	private TextView tvName,tvSignature;
//	private Button btnOK;
//	private CheckBox btnNoTips;
//
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_ecc_show);
//
//		initView();
//	}
//
//	private void initView() {
//		ivLogo = (ImageView)findViewById(R.id.ecc_logo);
//		Picasso.with(vThis).load(Const.getShopLogo(SpManager.getECC_USER_ID(vThis)))
//				.placeholder(R.drawable.empty_photo).into(ivLogo);
//		ivChat = (ImageView)findViewById(R.id.ecc_chat);
//		ivChat.setOnClickListener(this);
//		tvName = (TextView)findViewById(R.id.ecc_name);
//		tvName.setText(SpManager.getECC_USER_NAME(vThis));
//		tvSignature = (TextView)findViewById(R.id.ecc_signature);
//		tvSignature.setText(SpManager.getECC_USER_SIGNATURE(vThis));
//		btnOK = (Button)findViewById(R.id.ecc_confirm);
//		btnOK.setOnClickListener(this);
//		btnNoTips = (CheckBox)findViewById(R.id.ecc_no_tips);
//
//		setTitle("您的专属客服");
//		setLeftClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View view) {
//					SpManager.setECC_NO_SHOWED(vThis,btnNoTips.isChecked());
//				finish();
//			}
//		});
//	}
//
//	@Override
//	public void onClick(View v) {
//		switch (v.getId()) {
//			case R.id.ecc_chat:
//				ChatHelper.chat(vThis,
//						Integer.valueOf(SpManager.getECC_USER_ID(vThis)),
//						SpManager.getECC_USER_NAME(vThis), null,
//						0);
//				break;
//			case R.id.ecc_confirm:
//				SpManager.setECC_NO_SHOWED(vThis,btnNoTips.isChecked());
//				finish();
//				break;
//		}
//	}
//
//	@Override
//	public void onPause() {
//		super.onPause();
//		StatService.onPause(this);
//	}
//
//	@Override
//	public void onResume() {
//		super.onResume();
//		StatService.onResume(this);
//	}
//}
