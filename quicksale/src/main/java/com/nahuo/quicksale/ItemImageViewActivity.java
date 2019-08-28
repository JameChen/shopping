package com.nahuo.quicksale;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ProgressBar;

import com.baidu.mobstat.StatService;
import com.nahuo.library.controls.imageviewzoom.ImageViewTouch;
import com.nahuo.library.controls.imageviewzoom.ImageViewTouch.OnImageViewTouchSingleTapListener;
import com.squareup.picasso.Picasso;

public class ItemImageViewActivity extends BaseActivity {
    public static final String IMAGE_URL = "com.nahuo.bw.b.UploadItemActivity.image_url";
    private ImageViewTouch imageViewTouch;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_itemimage_view);

	initView();
	initData();
    }

    private void initView() {
	imageViewTouch = (ImageViewTouch) findViewById(R.id.itemimage_image);
	progressBar = (ProgressBar) findViewById(R.id.itemimage_image_progressBar);
	// 单击关闭
	imageViewTouch
		.setSingleTapListener(new OnImageViewTouchSingleTapListener() {

		    @Override
		    public void onSingleTapConfirmed() {
			finish();
		    }
		});
    }

    private void initData() {
	Intent intent = getIntent();
	String imgUrl = intent.getStringExtra(IMAGE_URL);
	if (TextUtils.isEmpty(imgUrl))
	    return;

    Picasso.with(this).load(imgUrl).placeholder(R.drawable.empty_photo).into(imageViewTouch);
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
