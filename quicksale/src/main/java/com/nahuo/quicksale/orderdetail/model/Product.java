package com.nahuo.quicksale.orderdetail.model;

import com.google.gson.annotations.Expose;

public class Product {

    @Expose
    public String ID ; 
    @Expose
    public String Color ; 
    @Expose
    public String Size ; 
    @Expose
    public int Qty ; 
    @Expose
    public boolean IsDeleted ; 
    @Expose
    public boolean EnableModify ;
    @Expose
    public String Summary ;

}
