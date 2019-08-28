package com.nahuo.bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by jame on 2017/9/1.
 */

public class ApplyDetailBean {

    /**
     * BaseInfo : {"Show":true,"ID":3,"OrderID":202528,"OrderCode":"170119-3A6D9BDC","Status":"已登记","Msg":"提示文字提示文字"}
     * ProductInfo : {"Show":true,"Cover":"upyun:nahuo-img-server://33306/item/1484807228.jpg","Name":"0119测试款","Price":1,"OrderCode":"170119-3A6D9BDC","List":[{"Color":"黄色","Size":"42码","Qty":1,"ApplyQty":1},{"Color":"紫色","Size":"42码","Qty":1,"ApplyQty":1},{"Color":"紫色","Size":"43码","Qty":2,"ApplyQty":2}]}
     * ProblemInfo : {"Show":true,"ProblemList":[{"ID":1,"Name":"次品","Remark":"次品的图片提示"},{"ID":2,"Name":"配错款式","Remark":"配错款式的图片提示"},{"ID":3,"Name":"配错颜色","Remark":"配错颜色的图片提示"},{"ID":4,"Name":"配错码数","Remark":"配错码数的图片提示"}],"SelectedID":1,"ProblemDetail":"配错码数配错码数配错码数配错码数配错码数","Images":["http://item-img.b0.upaiyun.com/com/cs/2806473bcc17b1cb73473ab1a0140d5c.jpg","http://item-img.b0.upaiyun.com/com/cs/bd9dc47dba7b7f8618a9d3e2210c8380.jpg"]}
     * HistoryInfo : {"Show":true,"List":[{"CreateUserName":"客服","CreateTime":"2017-02-08 11:57:01","Status":"等待买手取货"}]}
     * ReturnExpressInfo : {"Show":false,"Company":"","Code":"","Fee":"0.00","CreateDate":"","Image":""}
     * ExchangeExpressInfo : {"Show":false,"Company":"","Code":"","CreateDate":""}
     * RefundInfo : {"Show":false,"RefundType":"","MoneyDetail":"","CustomerMsg":""}
     * RejectInfo : {"Show":false,"RejectReason":""}
     * ReturnCompanyInfo : {"Show":false,"Address":"广东省广州市荔湾区桥中南路109号二楼天天拼货团","UserName":"小格调","Phone":"13800138000"}
     */
    @Expose
    @SerializedName("BaseInfo")
    private BaseInfoBean BaseInfo;
    @Expose
    @SerializedName("ProductInfo")
    private ProductInfoBean ProductInfo;
    @Expose
    @SerializedName("ProblemInfo")
    private ProblemInfoBean ProblemInfo;
    @Expose
    @SerializedName("HistoryInfo")
    private HistoryInfoBean HistoryInfo;
    @Expose
    @SerializedName("ReturnExpressInfo")
    private ReturnExpressInfoBean ReturnExpressInfo;
    @Expose
    @SerializedName("ExchangeExpressInfo")
    private ExchangeExpressInfoBean ExchangeExpressInfo;
    @Expose
    @SerializedName("RefundInfo")
    private RefundInfoBean RefundInfo;
    @Expose
    @SerializedName("RejectInfo")
    private RejectInfoBean RejectInfo;
    @Expose
    @SerializedName("ReturnCompanyInfo")
    private ReturnCompanyInfoBean ReturnCompanyInfo;

    public BaseInfoBean getBaseInfo() {
        return BaseInfo;
    }

    public void setBaseInfo(BaseInfoBean BaseInfo) {
        this.BaseInfo = BaseInfo;
    }

    public ProductInfoBean getProductInfo() {
        return ProductInfo;
    }

    public void setProductInfo(ProductInfoBean ProductInfo) {
        this.ProductInfo = ProductInfo;
    }

    public ProblemInfoBean getProblemInfo() {
        return ProblemInfo;
    }

    public void setProblemInfo(ProblemInfoBean ProblemInfo) {
        this.ProblemInfo = ProblemInfo;
    }

    public HistoryInfoBean getHistoryInfo() {
        return HistoryInfo;
    }

    public void setHistoryInfo(HistoryInfoBean HistoryInfo) {
        this.HistoryInfo = HistoryInfo;
    }

    public ReturnExpressInfoBean getReturnExpressInfo() {
        return ReturnExpressInfo;
    }

    public void setReturnExpressInfo(ReturnExpressInfoBean ReturnExpressInfo) {
        this.ReturnExpressInfo = ReturnExpressInfo;
    }

    public ExchangeExpressInfoBean getExchangeExpressInfo() {
        return ExchangeExpressInfo;
    }

    public void setExchangeExpressInfo(ExchangeExpressInfoBean ExchangeExpressInfo) {
        this.ExchangeExpressInfo = ExchangeExpressInfo;
    }

    public RefundInfoBean getRefundInfo() {
        return RefundInfo;
    }

    public void setRefundInfo(RefundInfoBean RefundInfo) {
        this.RefundInfo = RefundInfo;
    }

    public RejectInfoBean getRejectInfo() {
        return RejectInfo;
    }

    public void setRejectInfo(RejectInfoBean RejectInfo) {
        this.RejectInfo = RejectInfo;
    }

    public ReturnCompanyInfoBean getReturnCompanyInfo() {
        return ReturnCompanyInfo;
    }

    public void setReturnCompanyInfo(ReturnCompanyInfoBean ReturnCompanyInfo) {
        this.ReturnCompanyInfo = ReturnCompanyInfo;
    }

    public static class BaseInfoBean {
        /**
         * Show : true
         * ID : 3
         * OrderID : 202528
         * OrderCode : 170119-3A6D9BDC
         * Status : 已登记
         * Msg : 提示文字提示文字
         */
        @Expose
        @SerializedName("StatusID")
        private int StatusID;

        public int getStatusID() {
            return StatusID;
        }

        public void setStatusID(int statusID) {
            StatusID = statusID;
        }

        @Expose
        @SerializedName("Show")
        private boolean Show;
        @Expose
        @SerializedName("ID")
        private int ID;
        @Expose
        @SerializedName("OrderID")
        private int OrderID;
        @Expose
        @SerializedName("OrderCode")
        private String OrderCode;
        @Expose
        @SerializedName("Status")
        private String Status;
        @Expose
        @SerializedName("Msg")
        private String Msg;
        @Expose
        @SerializedName("Date")
        private String Date;

        public String getDate() {
            return Date;
        }

        public void setDate(String date) {
            Date = date;
        }

        public boolean isShow() {
            return Show;
        }

        public void setShow(boolean Show) {
            this.Show = Show;
        }

        public int getID() {
            return ID;
        }

        public void setID(int ID) {
            this.ID = ID;
        }

        public int getOrderID() {
            return OrderID;
        }

        public void setOrderID(int OrderID) {
            this.OrderID = OrderID;
        }

        public String getOrderCode() {
            return OrderCode;
        }

        public void setOrderCode(String OrderCode) {
            this.OrderCode = OrderCode;
        }

        public String getStatus() {
            return Status;
        }

        public void setStatus(String Status) {
            this.Status = Status;
        }

        public String getMsg() {
            return Msg;
        }

        public void setMsg(String Msg) {
            this.Msg = Msg;
        }
    }

    public static class ProductInfoBean {
        /**
         * Show : true
         * Cover : upyun:nahuo-img-server://33306/item/1484807228.jpg
         * Name : 0119测试款
         * Price : 1
         * OrderCode : 170119-3A6D9BDC
         * List : [{"Color":"黄色","Size":"42码","Qty":1,"ApplyQty":1},{"Color":"紫色","Size":"42码","Qty":1,"ApplyQty":1},{"Color":"紫色","Size":"43码","Qty":2,"ApplyQty":2}]
         */
        @Expose
        @SerializedName("Show")
        private boolean Show;
        @Expose
        @SerializedName("Cover")
        private String Cover;
        @Expose
        @SerializedName("Name")
        private String Name;
        @Expose
        @SerializedName("Price")
        private double Price;
        @Expose
        @SerializedName("OrderCode")
        private String OrderCode;
        @Expose
        @SerializedName("List")
        private java.util.List<ListBean> List;

        public boolean isShow() {
            return Show;
        }

        public void setShow(boolean Show) {
            this.Show = Show;
        }

        public String getCover() {
            return Cover;
        }

        public void setCover(String Cover) {
            this.Cover = Cover;
        }

        public String getName() {
            return Name;
        }

        public void setName(String Name) {
            this.Name = Name;
        }

        public double getPrice() {
            return Price;
        }

        public void setPrice(double Price) {
            this.Price = Price;
        }

        public String getOrderCode() {
            return OrderCode;
        }

        public void setOrderCode(String OrderCode) {
            this.OrderCode = OrderCode;
        }

        public List<ListBean> getList() {
            return List;
        }

        public void setList(List<ListBean> List) {
            this.List = List;
        }

        public static class ListBean {
            /**
             * Color : 黄色
             * Size : 42码
             * Qty : 1
             * ApplyQty : 1
             */
            @Expose
            @SerializedName("ProductID")
            private  String ProductID;

            public String getProductID() {
                return ProductID;
            }

            public void setProductID(String productID) {
                ProductID = productID;
            }

            @Expose
            @SerializedName("Color")
            private String Color;
            @Expose
            @SerializedName("Size")
            private String Size;
            @Expose
            @SerializedName("Qty")
            private int Qty;
            @Expose
            @SerializedName("ApplyQty")
            private int ApplyQty;

            public String getColor() {
                return Color;
            }

            public void setColor(String Color) {
                this.Color = Color;
            }

            public String getSize() {
                return Size;
            }

            public void setSize(String Size) {
                this.Size = Size;
            }

            public int getQty() {
                return Qty;
            }

            public void setQty(int Qty) {
                this.Qty = Qty;
            }

            public int getApplyQty() {
                return ApplyQty;
            }

            public void setApplyQty(int ApplyQty) {
                this.ApplyQty = ApplyQty;
            }
        }
    }

    public static class ProblemInfoBean {
        /**
         * Show : true
         * ProblemList : [{"ID":1,"Name":"次品","Remark":"次品的图片提示"},{"ID":2,"Name":"配错款式","Remark":"配错款式的图片提示"},{"ID":3,"Name":"配错颜色","Remark":"配错颜色的图片提示"},{"ID":4,"Name":"配错码数","Remark":"配错码数的图片提示"}]
         * SelectedID : 1
         * ProblemDetail : 配错码数配错码数配错码数配错码数配错码数
         * Images : ["http://item-img.b0.upaiyun.com/com/cs/2806473bcc17b1cb73473ab1a0140d5c.jpg","http://item-img.b0.upaiyun.com/com/cs/bd9dc47dba7b7f8618a9d3e2210c8380.jpg"]
         */
        @Expose
        @SerializedName("Show")
        private boolean Show;
        @Expose
        @SerializedName("SelectedID")
        private int SelectedID;
        @Expose
        @SerializedName("ProblemDetail")
        private String ProblemDetail;
        @Expose
        @SerializedName("ProblemList")
        private List<ProblemListBean> ProblemList;
        @Expose
        @SerializedName("Images")
        private List<String> Images;

        public boolean isShow() {
            return Show;
        }

        public void setShow(boolean Show) {
            this.Show = Show;
        }

        public int getSelectedID() {
            return SelectedID;
        }

        public void setSelectedID(int SelectedID) {
            this.SelectedID = SelectedID;
        }

        public String getProblemDetail() {
            return ProblemDetail;
        }

        public void setProblemDetail(String ProblemDetail) {
            this.ProblemDetail = ProblemDetail;
        }

        public List<ProblemListBean> getProblemList() {
            return ProblemList;
        }

        public void setProblemList(List<ProblemListBean> ProblemList) {
            this.ProblemList = ProblemList;
        }

        public List<String> getImages() {
            return Images;
        }

        public void setImages(List<String> Images) {
            this.Images = Images;
        }

    }

    public static class HistoryInfoBean {
        /**
         * Show : true
         * List : [{"CreateUserName":"客服","CreateTime":"2017-02-08 11:57:01","Status":"等待买手取货"}]
         */
        @Expose
        @SerializedName("Show")
        private boolean Show;
        @Expose
        @SerializedName("List")
        private java.util.List<ListBeanX> List;

        public boolean isShow() {
            return Show;
        }

        public void setShow(boolean Show) {
            this.Show = Show;
        }

        public List<ListBeanX> getList() {
            return List;
        }

        public void setList(List<ListBeanX> List) {
            this.List = List;
        }

        public static class ListBeanX {
            /**
             * CreateUserName : 客服
             * CreateTime : 2017-02-08 11:57:01
             * Status : 等待买手取货
             */
            @Expose
            @SerializedName("CreateUserName")
            private String CreateUserName;
            @Expose
            @SerializedName("CreateTime")
            private String CreateTime;
            @Expose
            @SerializedName("Status")
            private String Status;

            public String getCreateUserName() {
                return CreateUserName;
            }

            public void setCreateUserName(String CreateUserName) {
                this.CreateUserName = CreateUserName;
            }

            public String getCreateTime() {
                return CreateTime;
            }

            public void setCreateTime(String CreateTime) {
                this.CreateTime = CreateTime;
            }

            public String getStatus() {
                return Status;
            }

            public void setStatus(String Status) {
                this.Status = Status;
            }
        }
    }

    public static class ReturnExpressInfoBean {
        /**
         * Show : false
         * Company :
         * Code :
         * Fee : 0.00
         * CreateDate :
         * Image :
         */
        @Expose
        @SerializedName("Show")
        private boolean Show;
        @Expose
        @SerializedName("Company")
        private String Company;
        @Expose
        @SerializedName("Code")
        private String Code;
        @Expose
        @SerializedName("Fee")
        private String Fee;
        @Expose
        @SerializedName("CreateDate")
        private String CreateDate;
        @Expose
        @SerializedName("Image")
        private String Image;

        public boolean isShow() {
            return Show;
        }

        public void setShow(boolean Show) {
            this.Show = Show;
        }

        public String getCompany() {
            return Company;
        }

        public void setCompany(String Company) {
            this.Company = Company;
        }

        public String getCode() {
            return Code;
        }

        public void setCode(String Code) {
            this.Code = Code;
        }

        public String getFee() {
            return Fee;
        }

        public void setFee(String Fee) {
            this.Fee = Fee;
        }

        public String getCreateDate() {
            return CreateDate;
        }

        public void setCreateDate(String CreateDate) {
            this.CreateDate = CreateDate;
        }

        public String getImage() {
            return Image;
        }

        public void setImage(String Image) {
            this.Image = Image;
        }
    }

    public static class ExchangeExpressInfoBean {
        /**
         * Show : false
         * Company :
         * Code :
         * CreateDate :
         */
        @Expose
        @SerializedName("Show")
        private boolean Show;
        @Expose
        @SerializedName("Company")
        private String Company;
        @Expose
        @SerializedName("Code")
        private String Code;
        @Expose
        @SerializedName("CreateDate")
        private String CreateDate;

        public boolean isShow() {
            return Show;
        }

        public void setShow(boolean Show) {
            this.Show = Show;
        }

        public String getCompany() {
            return Company;
        }

        public void setCompany(String Company) {
            this.Company = Company;
        }

        public String getCode() {
            return Code;
        }

        public void setCode(String Code) {
            this.Code = Code;
        }

        public String getCreateDate() {
            return CreateDate;
        }

        public void setCreateDate(String CreateDate) {
            this.CreateDate = CreateDate;
        }
    }

    public static class RefundInfoBean {
        /**
         * Show : false
         * RefundType :
         * MoneyDetail :
         * CustomerMsg :
         */
        @Expose
        @SerializedName("Show")
        private boolean Show;
        @Expose
        @SerializedName("RefundType")
        private String RefundType;
        @Expose
        @SerializedName("MoneyDetail")
        private String MoneyDetail;
        @Expose
        @SerializedName("CustomerMsg")
        private String CustomerMsg;

        public boolean isShow() {
            return Show;
        }

        public void setShow(boolean Show) {
            this.Show = Show;
        }

        public String getRefundType() {
            return RefundType;
        }

        public void setRefundType(String RefundType) {
            this.RefundType = RefundType;
        }

        public String getMoneyDetail() {
            return MoneyDetail;
        }

        public void setMoneyDetail(String MoneyDetail) {
            this.MoneyDetail = MoneyDetail;
        }

        public String getCustomerMsg() {
            return CustomerMsg;
        }

        public void setCustomerMsg(String CustomerMsg) {
            this.CustomerMsg = CustomerMsg;
        }
    }

    public static class RejectInfoBean {
        /**
         * Show : false
         * RejectReason :
         */
        @Expose
        @SerializedName("Show")
        private boolean Show;
        @Expose
        @SerializedName("RejectReason")
        private String RejectReason;

        public boolean isShow() {
            return Show;
        }

        public void setShow(boolean Show) {
            this.Show = Show;
        }

        public String getRejectReason() {
            return RejectReason;
        }

        public void setRejectReason(String RejectReason) {
            this.RejectReason = RejectReason;
        }
    }

    public static class ReturnCompanyInfoBean {
        /**
         * Show : false
         * Address : 广东省广州市荔湾区桥中南路109号二楼天天拼货团
         * UserName : 小格调
         * Phone : 13800138000
         */
        @Expose
        @SerializedName("Show")
        private boolean Show;
        @Expose
        @SerializedName("Address")
        private String Address;
        @Expose
        @SerializedName("UserName")
        private String UserName;
        @Expose
        @SerializedName("Phone")
        private String Phone;

        public boolean isShow() {
            return Show;
        }

        public void setShow(boolean Show) {
            this.Show = Show;
        }

        public String getAddress() {
            return Address;
        }

        public void setAddress(String Address) {
            this.Address = Address;
        }

        public String getUserName() {
            return UserName;
        }

        public void setUserName(String UserName) {
            this.UserName = UserName;
        }

        public String getPhone() {
            return Phone;
        }

        public void setPhone(String Phone) {
            this.Phone = Phone;
        }
    }
}
