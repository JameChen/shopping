package com.nahuo.quicksale;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;

import com.baidu.mobstat.StatService;
import com.nahuo.library.controls.LoadingDialog;
import com.nahuo.quicksale.activity.MainNewActivity;
import com.nahuo.quicksale.api.XiaoZuAPI;
import com.nahuo.quicksale.oldermodel.NewsModel;

/**
 * @description 帖子详细
 * @created 2014-12-17 下午4:51:44
 * @author ZZB
 */
public class NewsDetailActivity extends BaseSlideBackActivity implements View.OnClickListener {

    private Context mContext = this;
    private LoadingDialog mLoadingDialog;
    private TextView tvUser,tvTime;
    private WebView wvContent;
    private TextView tvTitle;
    private NewsModel itemModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_news_detail);
        //getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.layout_titlebar_default);
        initView();

        Intent intent = getIntent();
        int id = intent.getIntExtra("ID", 0);
        LoadDetailTask ldt = new LoadDetailTask(id);
        ldt.execute((Void)null);
    }
    
    private void initView() {
        initTitleBar();
        mLoadingDialog = new LoadingDialog(this);
        tvUser = (TextView) findViewById(R.id.news_detail_user);
        tvTime = (TextView) findViewById(R.id.news_detail_time);
        wvContent = (WebView) findViewById(R.id.news_detail_content);
    }

    private void initTitleBar() {
        tvTitle = (TextView) findViewById(R.id.titlebar_tvTitle);

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

    private void initData()
    {
    	if (itemModel!=null)
    	{
    	tvTitle.setText(itemModel.getTitle());
    	tvUser.setText(itemModel.getTitle());
    	tvTime.setText(itemModel.getCreateTime());
        wvContent.getSettings().setDefaultTextEncodingName("UTF -8");
        StringBuilder html = new StringBuilder();
        html.append("<html>"
                + "<head>"
                + "<style type=\"text/css\">"
                + "*{margin:0px; padding:0px;}" 
                + ".wp-item-detail-format { max-width:100%;    }"
                + ".wp-item-detail-format img{ height:auto; text-align:center; } " 
                + "</style>" 
                + "</head>" 
                + "<body>"
                + "<div class=wp-item-detail-format>");
        html.append(itemModel.getContent());
        html.append("</div>" + "</body>" + "</html>");
        wvContent.loadData(html.toString(), "text/html; charset=UTF-8", null);
        wvContent.setVerticalScrollBarEnabled(false);
    	}
    }
    
    private class LoadDetailTask extends AsyncTask<Object, Void, Object> {
    	int mId;
    	
        public LoadDetailTask(int id) {
        	mId = id;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
                mLoadingDialog.start("正在加载...");
        }

        @Override
        protected Object doInBackground(Object... params) {
            try {
            	return XiaoZuAPI.getNewsDetail(mContext, mId);
            } catch (Exception e) {
                e.printStackTrace();
                return "error:" + e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(Object result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            if (mLoadingDialog.isShowing()) {
                mLoadingDialog.stop();
            }
            if (result instanceof String && ((String) result).startsWith("error:")) {
                ViewHub.showLongToast(mContext, ((String) result).replace("error:", ""));
            } else {
            	itemModel = (NewsModel)result;
            	initData();

                Intent reloadIntent = new Intent();
                reloadIntent
						.setAction(MainNewActivity.RELOAD_NEWS_LOADED);
				sendBroadcast(reloadIntent);

                Intent reload1Intent = new Intent();
                reload1Intent
						.setAction(NewsActivity.RELOAD_NEWS_LOADED);
				sendBroadcast(reload1Intent);
            }
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

    @Override
    public void finish() {
        super.finish();
        ViewHub.hideKeyboard(this);
    }
}
