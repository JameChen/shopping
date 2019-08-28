package com.nahuo.quicksale;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore.Images.ImageColumns;
import android.provider.MediaStore.Images.Media;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.baidu.mobstat.StatService;
import com.nahuo.quicksale.adapter.AlbumGridViewAdapter;
import com.nahuo.quicksale.adapter.ImgDirListAdapter;
import com.nahuo.quicksale.adapter.ImgDirListAdapter.OnImgDirClickListener;
import com.nahuo.quicksale.oldermodel.ImageDirModel;
import com.nahuo.quicksale.oldermodel.ImageViewModel;
import com.nahuo.quicksale.oldermodel.ImageViewModel.UploadStatus;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

/*
 * 从相册选择图片
 */
public class AlbumActivity extends BaseActivity {

    private AlbumActivity              vThis                    = this;
    private static final String        TAG                      = "AlbumActivity";
  //  public static final int            RESULTCODE_OK            = 109;
    // 已经选择的图片的model
    public static final String         EXTRA_SELECTED_PIC_MODEL = "EXTRA_SELECTED_PIC_MODEL";
    public static final String         EXTRA_MAX_PIC_COUNT      = "EXTRA_MAX_PIC_COUNT";
    private GridView                   gridView;
    private ArrayList<String>          dataList                 = new ArrayList<String>();
    private HashMap<String, ImageView> hashMap                  = new HashMap<String, ImageView>();
    private ArrayList<ImageViewModel>  selectedDataList         = new ArrayList<ImageViewModel>();
    private ProgressBar                progressBar;
    private AlbumGridViewAdapter       gridImageAdapter;
    private LinearLayout               selectedImageLayout;
    private Button                     okButton;
    private HorizontalScrollView       scrollview;
    private TextView                   tvTitle;
    private Button                     btnLeft;
    private Button                     btnRight;
    private ListView                   lvPicDir;
    private ImgDirListAdapter          imgDirAdapter;
    private RelativeLayout             rlPics;

    private int                        photoMax                 = 9;
    private ArrayList<ImageDirModel>   deviceImgDic             = new ArrayList<ImageDirModel>();

    @SuppressWarnings("unchecked")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
      //  requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);// 设置自定义标题栏
        setContentView(R.layout.activity_album);
       // getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.layout_titlebar_default);// 更换自定义标题栏布局

        // 取出已选择照片，在加载时，默认选择
        Intent intent = getIntent();

        selectedDataList = (ArrayList<ImageViewModel>)intent.getSerializableExtra(EXTRA_SELECTED_PIC_MODEL);
        if (selectedDataList == null) {
            selectedDataList = new ArrayList<ImageViewModel>();
        }
        photoMax = intent.getIntExtra(EXTRA_MAX_PIC_COUNT, 9);

        init();
        initListener();
        initView();
    }

    private void initView() {
        // 标题栏
        tvTitle = (TextView)findViewById(R.id.titlebar_tvTitle);
        btnLeft = (Button)findViewById(R.id.titlebar_btnLeft);
        btnRight = (Button)findViewById(R.id.titlebar_btnRight);
        tvTitle.setText(R.string.title_activity_selectPhoto);
        btnLeft.setText(R.string.titlebar_btnBack);
        btnLeft.setVisibility(View.VISIBLE);
        btnLeft.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });
        // 右边按钮为显示相册按钮，点了之后从相册图片浏览状态切换到相册选择状态
        btnRight.setText(R.string.titlebar_btnImgDir);
        btnRight.setVisibility(View.INVISIBLE);
        btnRight.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // 点击相册时，返回到相册状态
                btnRight.setVisibility(View.INVISIBLE);
                lvPicDir.setVisibility(View.VISIBLE);
                rlPics.setVisibility(View.INVISIBLE);
            }
        });
    }

    private void init() {
        rlPics = (RelativeLayout)findViewById(R.id.album_selectPic);
        lvPicDir = (ListView)findViewById(R.id.album_selectPicDir);
        imgDirAdapter = new ImgDirListAdapter(this);
        imgDirAdapter.setOnItemClickListener(new OnImgDirClickListener() {

            @Override
            public void onItemClick(ImageDirModel model) {
                btnRight.setVisibility(View.VISIBLE);
                lvPicDir.setVisibility(View.INVISIBLE);
                rlPics.setVisibility(View.VISIBLE);
                refreshData(model);
            }
        });
        lvPicDir.setAdapter(imgDirAdapter);
        progressBar = (ProgressBar)findViewById(R.id.progressbar);
        progressBar.setVisibility(View.GONE);
        gridView = (GridView)findViewById(R.id.myGrid);
        gridImageAdapter = new AlbumGridViewAdapter(this, dataList, selectedDataList);
        gridView.setAdapter(gridImageAdapter);
        selectedImageLayout = (LinearLayout)findViewById(R.id.selected_image_layout);
        okButton = (Button)findViewById(R.id.ok_button);
        scrollview = (HorizontalScrollView)findViewById(R.id.scrollview);

        initSelectImage();
        initImgDir();
        // refreshData();
    }

    private void initImgDir() {
        LoadImgDicTask loadImgDicTask = new LoadImgDicTask();
        loadImgDicTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    /**
     * 加载设备相册
     * */
    private class LoadImgDicTask extends AsyncTask<Void, ArrayList<ImageDirModel>, Object> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        @SuppressWarnings("unchecked")
        @Override
        protected Object doInBackground(Void... params) {
            Cursor bucketCursor = null;

            try {

                // 设置要返回的字段
                String[] imgColumns = {ImageColumns.DATA, ImageColumns.TITLE, ImageColumns.DATE_TAKEN};
                String[] bucketColumns = {ImageColumns.BUCKET_ID, ImageColumns.BUCKET_DISPLAY_NAME, ImageColumns.DATA,
                        "max(" + ImageColumns.DATE_TAKEN + ") as folderTaken", "count(*) as count"};

                // 根据bucket获取到共有多少个相册
                bucketCursor = getContentResolver().query(Media.EXTERNAL_CONTENT_URI, bucketColumns,
                        "1=1) group by " + ImageColumns.BUCKET_DISPLAY_NAME + " ORDER BY folderTaken DESC --", null,
                        null);
                int bucketIDColumn = bucketCursor.getColumnIndexOrThrow(ImageColumns.BUCKET_ID);
                int bucketCountColumn = bucketCursor.getColumnIndexOrThrow("count");
                int bucketDisplayColumn = bucketCursor.getColumnIndexOrThrow(ImageColumns.BUCKET_DISPLAY_NAME);
                int bucketDataColumn = bucketCursor.getColumnIndexOrThrow(ImageColumns.DATA);
                String bucketIDValue = "";
                String bucketCountValue = "";
                String bucketDisplayValue = "";
                String bucketDataValue = "";
                int fileDataColumn;
                Cursor filsCursor = null;

                while (bucketCursor.moveToNext()) {
                    bucketIDValue = bucketCursor.getString(bucketIDColumn);
                    bucketCountValue = bucketCursor.getString(bucketCountColumn);
                    bucketDisplayValue = bucketCursor.getString(bucketDisplayColumn);
                    bucketDataValue = bucketCursor.getString(bucketDataColumn);
                    // 保存相册到对象中
                    ImageDirModel nowModel = new ImageDirModel();
                    nowModel.setDirName(bucketDisplayValue);
                    nowModel.setFirstImgPath(bucketDataValue);

                    ArrayList<String> fils = new ArrayList<String>();
                    try {
                        // 找出相册下所有图片，按倒序时间显示
                        filsCursor = getContentResolver().query(Media.EXTERNAL_CONTENT_URI, imgColumns,
                                ImageColumns.BUCKET_ID + "=" + bucketIDValue + "", null,
                                ImageColumns.DATE_TAKEN + " DESC");
                        fileDataColumn = filsCursor.getColumnIndexOrThrow(ImageColumns.DATA);
                        while (filsCursor.moveToNext()) {
                            fils.add(filsCursor.getString(fileDataColumn));
                        }
                    } finally {
                        if (filsCursor != null) {
                            filsCursor.close();
                        }
                    }

                    nowModel.setFiles(fils);

                    deviceImgDic.add(nowModel);
                    publishProgress(deviceImgDic);
                }

                return null;
            } catch (Exception ex) {
                Log.e(TAG, "加载相册发生异常");
                ex.printStackTrace();
                return ex.getMessage() == null ? "操作异常" : ex.getMessage();
            } finally {
                if (bucketCursor != null) {
                    bucketCursor.close();
                }

            }
        }

        @Override
        protected void onPostExecute(Object result) {
            super.onPostExecute(result);
            if (result == null) {
                imgDirAdapter.setData(deviceImgDic);
                imgDirAdapter.notifyDataSetChanged();

            } else {
                Toast.makeText(vThis, result.toString(), Toast.LENGTH_SHORT).show();
            }
        }

    }

    // 初始化已经选择的图片
    private void initSelectImage() {
        if (selectedDataList == null)
            return;
        for (final ImageViewModel item : selectedDataList) {
            ImageView imageView = (ImageView)LayoutInflater.from(AlbumActivity.this).inflate(R.layout.choose_imageview,
                    selectedImageLayout, false);
            selectedImageLayout.addView(imageView);
            String url = item.getUrl();
            File f = new File(url);
            if (f.exists()) {
                Picasso.with(vThis).load(f).resize(100, 100).placeholder(R.drawable.logo_bg).into(imageView);
            } else {
                Picasso.with(vThis).load(url).resize(100, 100).placeholder(R.drawable.logo_bg).into(imageView);
            }
            hashMap.put(item.getUrl(), imageView);

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 点击滚动
                    selectedDataList.remove(item);
                    removePath(item.getUrl());
                    gridImageAdapter.notifyDataSetChanged();
                }
            });
        }
        if (photoMax == 1) {// 只能选择一张图片的时候，就不显示还能选择多少张图片了
            okButton.setText("完成");
        } else {
            okButton.setText("完成(" + selectedDataList.size() + "/" + photoMax + ")");
        }
    }

    // 初始化是
    private void initListener() {

        gridImageAdapter.setOnItemClickListener(new AlbumGridViewAdapter.OnItemClickListener() {

            @Override
            public void onItemClick(final ToggleButton toggleButton, int position, final String path, boolean isChecked) {

                if (selectedDataList.size() >= photoMax) {
                    toggleButton.setChecked(false);
                    if (!removePath(path)) {
                        ViewHub.showShortToast(vThis, "只能选择" + photoMax + "张图片");
                        ViewHub.shakeView(okButton);
                    }
                    return;
                }

                if (isChecked) {
                    if (!hashMap.containsKey(path)) {
                        ImageView imageView = (ImageView)LayoutInflater.from(AlbumActivity.this).inflate(
                                R.layout.choose_imageview, selectedImageLayout, false);
                        selectedImageLayout.addView(imageView);
                        imageView.postDelayed(new Runnable() {

                            @Override
                            public void run() {

                                int off = selectedImageLayout.getMeasuredWidth() - scrollview.getWidth();
                                if (off > 0) {
                                    scrollview.smoothScrollTo(off, 0);
                                }

                            }
                        }, 100);

                        hashMap.put(path, imageView);

                        ImageViewModel imageViewModel = new ImageViewModel();
                        imageViewModel.setIsAlbumPhoto(true);
                        imageViewModel.setCanRemove(false);
                        imageViewModel.setLoading(false);
                        imageViewModel.setNewAdd(true);
                        imageViewModel.setUploadStatus(UploadStatus.NONE);
                        imageViewModel.setUrl(path);
                        imageViewModel.setOriginalUrl(path);
                        selectedDataList.add(imageViewModel);

                        Picasso.with(vThis).load(new File(path)).placeholder(R.drawable.empty_photo).into(imageView);

                        imageView.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                toggleButton.setChecked(false);
                                removePath(path);
                                ImageView image = (ImageView)toggleButton.getTag(R.id.tag_second);
                                image.setVisibility(View.GONE);
                            }
                        });
                        if (photoMax == 1) {// 只能选择一张图片的时候，就不显示还能选择多少张图片了
                            okButton.setText("完成");
                        } else {
                            okButton.setText("完成(" + selectedDataList.size() + "/" + photoMax + ")");
                        }
                    }
                } else {
                    removePath(path);
                }

            }
        });

        okButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent = new Intent();
                Bundle bundle = new Bundle();

                bundle.putSerializable(EXTRA_SELECTED_PIC_MODEL, selectedDataList);
                intent.putExtras(bundle);
                setResult( Activity.RESULT_OK, intent);
                finish();

            }
        });

    }

    private boolean removePath(String path) {
        if (hashMap.containsKey(path)) {
            selectedImageLayout.removeView(hashMap.get(path));
            hashMap.remove(path);
            for (ImageViewModel ivm : selectedDataList) {
                if (ivm.getUrl().equals(path)) {
                    selectedDataList.remove(ivm);
                    break;
                }
            }
            if (photoMax == 1) {// 只能选择一张图片的时候，就不显示还能选择多少张图片了
                okButton.setText("完成");
            } else {
                okButton.setText("完成(" + selectedDataList.size() + "/" + photoMax + ")");
            }
            return true;
        } else {
            return false;
        }
    }

    private void refreshData(final ImageDirModel model) {
        progressBar.setVisibility(View.GONE);
        dataList.clear();
        gridImageAdapter.dataList = dataList;
        gridImageAdapter.selectedDataList = selectedDataList;
        gridImageAdapter.notifyDataSetChanged();

        dataList.addAll(model.excludeFiles("gif"));
        gridImageAdapter.dataList = dataList;
        gridImageAdapter.selectedDataList = selectedDataList;
        gridImageAdapter.notifyDataSetChanged();
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
