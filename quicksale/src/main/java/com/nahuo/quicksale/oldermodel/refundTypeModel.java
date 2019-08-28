package com.nahuo.quicksale.oldermodel;

import com.google.gson.annotations.Expose;

public class refundTypeModel {

    @Expose
    public int    id;
    @Expose
    public String name;

    public String value = name;

    @Override
    public String toString() { // 为什么要重写toString()呢？因为适配器在显示数据的时候，如果传入适配器的对象不是字符串的情况下，直接就使用对象.toString()
        // TODO Auto-generated method stub
        return id + "";
    }

}
