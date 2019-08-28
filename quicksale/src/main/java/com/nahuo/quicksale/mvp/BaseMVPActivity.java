package com.nahuo.quicksale.mvp;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.baidu.mobstat.StatService;
import com.nahuo.library.helper.DisplayUtil;
import com.nahuo.quicksale.R;
import com.nahuo.quicksale.base.BaseAppCompatActivity;
import com.nahuo.quicksale.common.ListUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ZZB on 2015/6/3.
 */
public abstract class BaseMVPActivity extends BaseAppCompatActivity implements MvpView {
    protected List<MvpPresenter> mPresenters = new ArrayList<MvpPresenter>();
    protected void initPresenters() {
    }

    ;

    protected <T extends View> T $(int id) {
        return (T) findViewById(id);
    }
    protected <T extends View> T $(View v, int id) {
        return (T) v.findViewById(id);
    }

    protected Toolbar mToolbar;
    TextView titleView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initPresenters();
        if (!ListUtils.isEmpty(mPresenters)) {
            for (MvpPresenter p : mPresenters) {
                p.attachView(this);
            }
        }
//        ViewServer.get(this).addWindow(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (!ListUtils.isEmpty(mPresenters)) {
            for (MvpPresenter p : mPresenters) {
                p.detachView(false);
            }
        }
//        ViewServer.get(this).removeWindow(this);
    }

    /**
     * 将toolbar设置一个左边的返回按钮
     */
    protected void enableNavigationIcon() {
        if (mToolbar == null)
            mToolbar = $(R.id.tool_bar);
        mToolbar.setTitle(null);
        mToolbar.setSubtitle(null);

        mToolbar.setNavigationIcon(R.drawable.back);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    View setBarRightView(int rightView) {
        View view = getLayoutInflater().inflate(rightView, null);
        setBarRightView(view);
        return view;
    }

    /**
     * 为toolbar右侧添加一个自定义view菜单
     */
    void setBarRightView(View rightView) {
        if (mToolbar == null) {
            mToolbar = $(R.id.tool_bar);
        }
        if (mToolbar != null) {
            ActionBar.LayoutParams params = new ActionBar.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT
                    , ActionBar.LayoutParams.WRAP_CONTENT, Gravity.RIGHT);
            int right = DisplayUtil.dip2px(this, 16);
            params.rightMargin = right;
//                    params.leftMargin = right;
            mToolbar.addView(rightView, params);
        }
    }

    protected void setTitle(String title) {
        if (mToolbar == null) {
            mToolbar = $(R.id.tool_bar);
        }
        if (mToolbar != null) {
            if (titleView == null) {
                ActionBar.LayoutParams params = new ActionBar.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT
                        , ActionBar.LayoutParams.WRAP_CONTENT, Gravity.CENTER);
                int right = getResources().getDimensionPixelSize(R.dimen.activity_horizontal_margin);
                params.rightMargin = params.leftMargin = right;
                titleView = new TextView(this);
                titleView.setSingleLine(true);
                titleView.setTextSize(20);
                titleView.setTextColor(getResources().getColor(R.color.black));
                titleView.setText(title);
                titleView.setEllipsize(TextUtils.TruncateAt.END);
                mToolbar.addView(titleView, params);
            }
        }
        if (titleView != null)
            titleView.setText(title);
    }

    @Override
    protected void onResume() {
        super.onResume();
        StatService.onResume(this);
//        ViewServer.get(this).setFocusedWindow(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        StatService.onPause(this);
    }
}
