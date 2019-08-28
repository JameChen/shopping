package com.nahuo.quicksale;

import com.nahuo.quicksale.common.SpManager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

/**
 * @description 为什么招代理页面
 * @created 2015-4-21 下午3:32:51
 * @author ZZB
 */
public class WhyRecruitAgentActivity extends BaseActivity2 implements OnClickListener {

    private Context  mContext = this;
    private CheckBox mCbShowWhyRecruit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_why_recruit_agent);
        initView();
    }

    private void initView() {
        setTitle("招代理");
        mCbShowWhyRecruit = (CheckBox)findViewById(R.id.cb_show_why_recruit_agent);
        mCbShowWhyRecruit.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SpManager.setShowWhyRecruitAgent(mContext, !isChecked);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_know_more://了解更多
                Intent knowIntent = new Intent(mContext, NewsDetailActivity.class);
                knowIntent.putExtra("ID", 82776);
                mContext.startActivity(knowIntent);
                break;
            case R.id.btn_recruit_agent:// 招代理
                Intent intent = new Intent(mContext, RecruitAgentActivity.class);
                startActivity(intent);
                break;
        }
    }
}
