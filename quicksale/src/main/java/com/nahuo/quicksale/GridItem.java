//package com.nahuo.quicksale;
//
//import com.nahuo.quicksale.model.ImageViewModel;
//import com.nahuo.quicksale.model.ImageViewModel.UploadStatus;
//import com.nostra13.universalimageloader.core.DisplayImageOptions;
//import com.nostra13.universalimageloader.core.ImageLoader;
//
//import android.content.Context;
//import android.graphics.Bitmap;
//import android.util.AttributeSet;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.widget.Checkable;
//import android.widget.ImageView;
//import android.widget.ProgressBar;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//
//public class GridItem extends RelativeLayout implements Checkable {
//
//    private Context mContext;
//    private ImageLoader mImageLoader;
//    private DisplayImageOptions mOptions;
//
//    private boolean mChecked;
//    private boolean mIsLoading;
//    private UploadStatus mUploadStatus;
//    private ImageViewModel mImageViewModel;
//
//    private ImageView mImgView, mSelectView, mIcon;
//    private TextView mState;
//    private ProgressBar mLoadingBar;
//    private View mBottomLayout;
//
//    public GridItem(Context context) {
//	super(context);
//	init(context, null);
//    }
//
//    public GridItem(Context context, AttributeSet attrs) {
//	super(context, attrs);
//	init(context, attrs);
//    }
//
//    public GridItem(Context context, AttributeSet attrs, int defStyle) {
//	super(context, attrs, defStyle);
//	init(context, attrs);
//    }
//
//    /**
//     * 初始化布局
//     * */
//    private void init(Context context, AttributeSet attrs) {
//	mContext = context;
//	this.mImageLoader = ImageLoader.getInstance();
//	this.mOptions = new DisplayImageOptions.Builder()
//	// .showImageOnLoading(R.drawable.preview_photo)
//	// .showImageForEmptyUri(R.drawable.not_found)
//	// .showImageOnFail(R.drawable.not_found)
//		.resetViewBeforeLoading(false) // default
//		.delayBeforeLoading(1000).cacheInMemory(true) // default
//		.cacheOnDisc(true).bitmapConfig(Bitmap.Config.RGB_565).build();
//
//
//	LayoutInflater.from(mContext).inflate(R.layout.layout_image, this);
//	mImgView = (ImageView) findViewById(R.id.imageview);
//	mSelectView = (ImageView) findViewById(R.id.image_check);
//	mIcon = (ImageView) findViewById(R.id.image_icon);
//	mState = (TextView) findViewById(R.id.image_tvState);
//	mLoadingBar = (ProgressBar) findViewById(R.id.image_loadingbar);
//	mBottomLayout = findViewById(R.id.image_layout_bottom);
//	// 设置默认值
//	setChecked(false);
//	setLoading(false);
//	setUploadStatus(UploadStatus.NONE);
//    }
//
//    @Override
//    public void setChecked(boolean checked) {
//	mChecked = checked;
//	mSelectView.setVisibility(mChecked ? View.VISIBLE : View.GONE);
//    }
//
//    @Override
//    public boolean isChecked() {
//	return mChecked;
//    }
//
//    @Override
//    public void toggle() {
//	setChecked(!mChecked);
//    }
//
//    /**
//     * 设置图片
//     * */
//    public void setImage(String url) {
//    	mImageLoader.displayImage(url, mImgView, mOptions);
////        Picasso.with(mContext).load(url).placeholder(R.drawable.empty_photo).into(mImgView);
//    }
//
//    public boolean isLoading() {
//	return mIsLoading;
//    }
//
//    /**
//     * 设置转圈圈显示与隐藏
//     * */
//    public void setLoading(boolean isLoading) {
//	mIsLoading = isLoading;
//	mLoadingBar.setVisibility(mIsLoading ? View.VISIBLE : View.GONE);
//    }
//
//    public UploadStatus getUploadStatus() {
//	return mUploadStatus;
//    }
//
//    public void setImageViewModel(ImageViewModel imageViewModel) {
//	mImageViewModel = imageViewModel;
//	if (mImageViewModel != null) {
//	    setLoading(mImageViewModel.isLoading());
//	    setChecked(mImageViewModel.isCanRemove());
//	    setUploadStatus(mImageViewModel.getUploadStatus());
//	    setImage(mImageViewModel.getUrl());
//	}
//    }
//
//    public ImageViewModel getImageViewModel() {
//	if (mImageViewModel != null) {
//	    mImageViewModel.setLoading(mIsLoading);
//	    mImageViewModel.setCanRemove(mChecked);
//	    mImageViewModel.setUploadStatus(mUploadStatus);
//	}
//	return mImageViewModel;
//    }
//
//    /**
//     * 设置图片上传状态：SUCCESS-上传成功，FAIL-上传失败
//     * */
//    public void setUploadStatus(UploadStatus uploadStatus) {
//	mUploadStatus = uploadStatus;
//	switch (mUploadStatus) {
//	case NONE:
//	    mBottomLayout.setVisibility(View.GONE);
//	    break;
//	case SUCCESS:
//	    mIcon.setImageResource(R.drawable.ic_success);
//	    mState.setText(R.string.uploaditem_image_upload_success);
//	    mBottomLayout.setVisibility(View.VISIBLE);
//	    break;
//	case FAIL:
//	    mIcon.setImageResource(R.drawable.ic_error);
//	    mState.setText(R.string.uploaditem_image_upload_fail);
//	    mBottomLayout.setVisibility(View.VISIBLE);
//	    break;
//	}
//    }
//
//}
