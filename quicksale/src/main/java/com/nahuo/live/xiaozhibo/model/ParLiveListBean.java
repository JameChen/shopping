package com.nahuo.live.xiaozhibo.model;

import com.chad.library.adapter.base.entity.AbstractExpandableItem;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.nahuo.constant.Constant;

/**
 * Created by jame on 2019/5/7.
 */

public class ParLiveListBean extends AbstractExpandableItem<LiveListBean.SubLiveListBean> implements MultiItemEntity {
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    private String title="";
    @Override
    public int getItemType() {
        return Constant.TYPE_LEVEL_0;
    }

    @Override
    public int getLevel() {
        return 0;
    }
}
