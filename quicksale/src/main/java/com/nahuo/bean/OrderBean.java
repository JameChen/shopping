package com.nahuo.bean;

import com.google.gson.annotations.SerializedName;
import com.nahuo.quicksale.oldermodel.OrderButton;

import java.io.Serializable;
import java.util.List;

/**
 * Created by jame on 2018/3/5.
 */

public class OrderBean implements Serializable {
    private static final long serialVersionUID = 3021968471780710821L;

    /**
     * Notice :
     * OrderList : [{"ID":1746,"PayableAmount":"0.00","Code":"180302174805062232","Title":"Lilifeiyang 内网测试 0809 第1款","OrderIDS":"","Images":["upyun:nahuo-img-server://3636/item/1502246402.jpg"],"RefundSummary":"","Summary":"共1款，3件，合计¥ 48.50，运费¥11.00","Statu":"已取消","Buttons":[{"title":"一键加入拿货车","action":"一键加入购物车","isPoint":false,"isEnable":true,"type":"button","data":0}]},{"ID":1745,"PayableAmount":"0.00","Code":"180302174722468309","Title":"Lilifeiyang 20180208内网测试第2 不排单","OrderIDS":"","Images":["upyun:nahuo-img-server://3636/item/d5c75029-e62a-4c6f-997e-53cc125aebc4-3555.jpg"],"RefundSummary":"","Summary":"共1款，3件，合计¥ 40.20，运费¥6.00","Statu":"已取消","Buttons":[{"title":"一键加入拿货车","action":"一键加入购物车","isPoint":false,"isEnable":true,"type":"button","data":0}]},{"ID":1738,"PayableAmount":"0.00","Code":"180301170925999305","Title":"","OrderIDS":"","Images":["upyun:nahuo-img-server://3636/item/75d24430-4c5a-465e-9c53-88fadfc05353-21008","upyun:nahuo-img-server://3636/item/1506564473.jpg","upyun:nahuo-img-server://3636/item/f21caab2-abab-496b-ae10-8685e66aea2d-89972.jpg"],"RefundSummary":"","Summary":"共4款，35件，合计¥ 1410.81，运费¥129.00","Statu":"已取消","Buttons":[{"title":"一键加入拿货车","action":"一键加入购物车","isPoint":false,"isEnable":true,"type":"button","data":0}]},{"ID":1733,"PayableAmount":"0.00","Code":"180228165620877768","Title":"","OrderIDS":"","Images":["upyun:nahuo-img-server://3636/item/d5c75029-e62a-4c6f-997e-53cc125aebc4-3555.jpg","upyun:nahuo-img-server://3636/item/1506564473.jpg","upyun:nahuo-img-server://3636/item/8ec6afa4-ce4d-41be-8abf-89ac98f9169e-57037"],"RefundSummary":"","Summary":"共3款，25件，合计¥ 337.90，运费¥116.00","Statu":"已取消","Buttons":[{"title":"一键加入拿货车","action":"一键加入购物车","isPoint":false,"isEnable":true,"type":"button","data":0}]},{"ID":1732,"PayableAmount":"0.00","Code":"180227161650980103","Title":"lilifeiyang 0927 内网测试 第1","OrderIDS":"","Images":["upyun:nahuo-img-server://3636/item/1506477408.jpg"],"RefundSummary":"","Summary":"共1款，17件，合计¥ 182.25，运费¥62.00","Statu":"已取消","Buttons":[{"title":"一键加入拿货车","action":"一键加入购物车","isPoint":false,"isEnable":true,"type":"button","data":0}]},{"ID":1728,"PayableAmount":"0.00","Code":"180226184548830151","Title":"","OrderIDS":"","Images":["upyun:banwo-img-server://102897/item/1502692886886.jpg","upyun:nahuo-img-server://3636/item/2a23fb00-10cd-4271-869a-15c1bd362ca2-15574","upyun:nahuo-img-server://3636/item/436b9001-4572-4d1c-818c-ea4534ffb5c5-701"],"RefundSummary":"","Summary":"共4款，4件，合计¥ 63.15，运费¥30.00","Statu":"已取消","Buttons":[{"title":"一键加入拿货车","action":"一键加入购物车","isPoint":false,"isEnable":true,"type":"button","data":0}]},{"ID":1727,"PayableAmount":"0.00","Code":"180226184548830196","Title":"","OrderIDS":"","Images":["upyun:nahuo-img-server://61056/item/1510813980.jpg","upyun:nahuo-img-server://61056/item/1510804611.jpg"],"RefundSummary":"","Summary":"共2款，2件，合计¥ 19.60，运费¥0.00","Statu":"已取消","Buttons":[{"title":"一键加入拿货车","action":"一键加入购物车","isPoint":false,"isEnable":true,"type":"button","data":0}]},{"ID":1726,"PayableAmount":"0.00","Code":"180226184249413135","Title":"","OrderIDS":"","Images":["upyun:nahuo-img-server://3636/item/75d24430-4c5a-465e-9c53-88fadfc05353-21008","upyun:nahuo-img-server://3636/item/36b9c4a5-4ebc-4c61-8729-28e5a8fba539-66346"],"RefundSummary":"","Summary":"共2款，5件，合计¥ 111.95，运费¥54.00","Statu":"已取消","Buttons":[{"title":"一键加入拿货车","action":"一键加入购物车","isPoint":false,"isEnable":true,"type":"button","data":0}]},{"ID":1725,"PayableAmount":"0.00","Code":"180226143549667161","Title":"","OrderIDS":"","Images":["upyun:nahuo-img-server://61056/item/b5f06ccb-d495-44b7-b8e2-6a0003c5f611-66077.jpg","upyun:nahuo-img-server://61056/item/140f5707-8652-44f2-8a22-afb6ab731aa6-72881.jpg"],"RefundSummary":"","Summary":"共2款，2件，合计¥ 20.60，运费¥0.00","Statu":"已取消","Buttons":[{"title":"一键加入拿货车","action":"一键加入购物车","isPoint":false,"isEnable":true,"type":"button","data":0}]},{"ID":1654,"PayableAmount":"0.00","Code":"180126144553730144","Title":"Lilifeiyang 内网测试 1012 有档口 第5","OrderIDS":"","Images":["upyun:nahuo-img-server://3636/item/1507788833.jpg"],"RefundSummary":"退款：共1款，10件，合计¥ 111.50","Summary":"共1款，10件，合计¥ 145.50，运费¥34.00","Statu":"已完成","Buttons":[]},{"ID":1641,"PayableAmount":"0.00","Code":"180120194057287178","Title":"","OrderIDS":"","Images":["upyun:nahuo-img-server://3636/item/1511748784.jpg","upyun:nahuo-img-server://3636/item/bebced22-3b18-48fd-8e2a-ca96ab3c5a61-92871.jpg"],"RefundSummary":"退款：共2款，2件，合计¥ 103.25","Summary":"共2款，2件，合计¥ 103.25，运费¥8.00","Statu":"已退款","Buttons":[{"title":"一键加入拿货车","action":"一键加入购物车","isPoint":false,"isEnable":true,"type":"button","data":0}]},{"ID":1640,"PayableAmount":"0.00","Code":"180120194057287169","Title":"lilifeiyang xiaoluo一起复制","OrderIDS":"","Images":["upyun:nahuo-img-server://3636/item/1506068601.jpg"],"RefundSummary":"退款：共1款，1件，合计¥ 6.05","Summary":"共1款，1件，合计¥ 6.05，运费¥4.00","Statu":"已退款","Buttons":[{"title":"一键加入拿货车","action":"一键加入购物车","isPoint":false,"isEnable":true,"type":"button","data":0}]},{"ID":1614,"PayableAmount":"0.00","Code":"180110163234460128","Title":"","OrderIDS":"","Images":["upyun:nahuo-img-server://3636/item/4e300e83-a46d-46c5-98f5-9c89fe285663-23475.jpg","upyun:nahuo-img-server://3636/item/093bdc97-3d7b-4886-9fac-0dd36629e980-41509"],"RefundSummary":"","Summary":"共2款，11件，合计¥ 139.75，运费¥41.00","Statu":"已取消","Buttons":[{"title":"一键加入拿货车","action":"一键加入购物车","isPoint":false,"isEnable":true,"type":"button","data":0}]},{"ID":1606,"PayableAmount":"0.00","Code":"180109170900630125","Title":"","OrderIDS":"","Images":["upyun:nahuo-img-server://3636/item/ab625697-b943-40c1-b691-6ea7bc53037a-73165.jpg","upyun:nahuo-img-server://3636/item/1507789795.jpg"],"RefundSummary":"","Summary":"共2款，43件，合计¥ 594.90，运费¥153.00","Statu":"已取消","Buttons":[{"title":"一键加入拿货车","action":"一键加入购物车","isPoint":false,"isEnable":true,"type":"button","data":0}]},{"ID":1603,"PayableAmount":"0.00","Code":"180109142909937139","Title":"","OrderIDS":"","Images":["upyun:nahuo-img-server://3636/item/ab625697-b943-40c1-b691-6ea7bc53037a-73165.jpg","upyun:nahuo-img-server://3636/item/1507789795.jpg"],"RefundSummary":"","Summary":"共2款，43件，合计¥ 573.90，运费¥132.00","Statu":"已取消","Buttons":[{"title":"一键加入拿货车","action":"一键加入购物车","isPoint":false,"isEnable":true,"type":"button","data":0}]},{"ID":1600,"PayableAmount":"0.00","Code":"180109101713353118","Title":"","OrderIDS":"","Images":["upyun:nahuo-img-server://3636/item/b99bf880-2129-493f-a23c-c0dc3781e163-78397","upyun:nahuo-img-server://3636/item/0d88fc68-bf33-4ae0-bb0a-8565ec2dc090-25546"],"RefundSummary":"","Summary":"共2款，2件，合计¥ 30.15，运费¥6.00","Statu":"已取消","Buttons":[{"title":"一键加入拿货车","action":"一键加入购物车","isPoint":false,"isEnable":true,"type":"button","data":0}]},{"ID":1558,"PayableAmount":"0.00","Code":"180104161328570159","Title":"Lilifeiyang内网测试 0103第1款zzb复制","OrderIDS":"","Images":["upyun:nahuo-img-server://3636/item/15fa2f38-c3d1-48e3-8300-299200d60349-34549"],"RefundSummary":"","Summary":"共1款，1件，合计¥ 10.50，运费¥1.00","Statu":"已取消","Buttons":[{"title":"一键加入拿货车","action":"一键加入购物车","isPoint":false,"isEnable":true,"type":"button","data":0}]},{"ID":1550,"PayableAmount":"0.00","Code":"171229105844050140","Title":"lilifeiyang内网测试2224第3","OrderIDS":"","Images":["upyun:nahuo-img-server://3636/item/1510652977.jpg"],"RefundSummary":"","Summary":"共1款，7件，合计¥ 147.55，运费¥24.00","Statu":"已取消","Buttons":[{"title":"一键加入拿货车","action":"一键加入购物车","isPoint":false,"isEnable":true,"type":"button","data":0}]},{"ID":1547,"PayableAmount":"0.00","Code":"171225172212093158","Title":"PP现款，机绣黑色打钉系列，各种","OrderIDS":"","Images":["/u40263/item/m1411869286572.jpg"],"RefundSummary":"","Summary":"共1款，1件，合计¥ 493.80，运费¥0.00","Statu":"已取消","Buttons":[{"title":"一键加入拿货车","action":"一键加入购物车","isPoint":false,"isEnable":true,"type":"button","data":0}]},{"ID":1546,"PayableAmount":"0.00","Code":"171225143750187113","Title":"PP现款，机绣黑色打钉系列，各种","OrderIDS":"","Images":["/u40263/item/m1411869286572.jpg"],"RefundSummary":"","Summary":"共1款，1件，合计¥ 493.80，运费¥0.00","Statu":"已取消","Buttons":[{"title":"一键加入拿货车","action":"一键加入购物车","isPoint":false,"isEnable":true,"type":"button","data":0}]}]
     */

    @SerializedName("Notice")
    private String Notice="";
    @SerializedName("OrderList")
    private List<OrderListBean> OrderList;

    public String getNotice() {
        return Notice;
    }

    public void setNotice(String Notice) {
        this.Notice = Notice;
    }

    public List<OrderListBean> getOrderList() {
        return OrderList;
    }

    public void setOrderList(List<OrderListBean> OrderList) {
        this.OrderList = OrderList;
    }

    public static class OrderListBean implements Serializable {
        /**
         * ID : 1746
         * PayableAmount : 0.00
         * Code : 180302174805062232
         * Title : Lilifeiyang 内网测试 0809 第1款
         * OrderIDS :
         * Images : ["upyun:nahuo-img-server://3636/item/1502246402.jpg"]
         * RefundSummary :
         * Summary : 共1款，3件，合计¥ 48.50，运费¥11.00
         * Statu : 已取消
         * Buttons : [{"title":"一键加入拿货车","action":"一键加入购物车","isPoint":false,"isEnable":true,"type":"button","data":0}]
         */

        @SerializedName("ID")
        private int ID;
        @SerializedName("PayableAmount")
        private String PayableAmount="";
        @SerializedName("Code")
        private String Code="";
        @SerializedName("Title")
        private String Title="";
        @SerializedName("OrderIDS")
        private String OrderIDS;
        @SerializedName("RefundSummary")
        private String RefundSummary="";
        @SerializedName("Summary")
        private String Summary="";
        @SerializedName("Statu")
        private String Statu="";
        @SerializedName("Images")
        private List<String> Images;
        @SerializedName("Buttons")
        private List<OrderButton> Buttons;

        public int getID() {
            return ID;
        }

        public void setID(int ID) {
            this.ID = ID;
        }

        public String getPayableAmount() {
            return PayableAmount;
        }

        public void setPayableAmount(String PayableAmount) {
            this.PayableAmount = PayableAmount;
        }

        public String getCode() {
            return Code;
        }

        public void setCode(String Code) {
            this.Code = Code;
        }

        public String getTitle() {
            return Title;
        }

        public void setTitle(String Title) {
            this.Title = Title;
        }

        public String getOrderIDS() {
            return OrderIDS;
        }

        public void setOrderIDS(String OrderIDS) {
            this.OrderIDS = OrderIDS;
        }

        public String getRefundSummary() {
            return RefundSummary;
        }

        public void setRefundSummary(String RefundSummary) {
            this.RefundSummary = RefundSummary;
        }

        public String getSummary() {
            return Summary;
        }

        public void setSummary(String Summary) {
            this.Summary = Summary;
        }

        public String getStatu() {
            return Statu;
        }

        public void setStatu(String Statu) {
            this.Statu = Statu;
        }

        public List<String> getImages() {
            return Images;
        }

        public void setImages(List<String> Images) {
            this.Images = Images;
        }

        public List<OrderButton> getButtons() {
            return Buttons;
        }

        public void setButtons(List<OrderButton> Buttons) {
            this.Buttons = Buttons;
        }

//        public static class ButtonsBean implements Serializable {
//            private static final long serialVersionUID = -8080792560449898643L;
//            /**
//             * title : 一键加入拿货车
//             * action : 一键加入购物车
//             * isPoint : false
//             * isEnable : true
//             * type : button
//             * data : 0
//             */
//
//            @SerializedName("title")
//            private String title="";
//            @SerializedName("action")
//            private String action="";
//            @SerializedName("isPoint")
//            private boolean isPoint;
//            @SerializedName("isEnable")
//            private boolean isEnable;
//            @SerializedName("type")
//            private String type="";
//            @SerializedName("data")
//            private int data;
//
//            public String getTitle() {
//                return title;
//            }
//
//            public void setTitle(String title) {
//                this.title = title;
//            }
//
//            public String getAction() {
//                return action;
//            }
//
//            public void setAction(String action) {
//                this.action = action;
//            }
//
//            public boolean isIsPoint() {
//                return isPoint;
//            }
//
//            public void setIsPoint(boolean isPoint) {
//                this.isPoint = isPoint;
//            }
//
//            public boolean isIsEnable() {
//                return isEnable;
//            }
//
//            public void setIsEnable(boolean isEnable) {
//                this.isEnable = isEnable;
//            }
//
//            public String getType() {
//                return type;
//            }
//
//            public void setType(String type) {
//                this.type = type;
//            }
//
//            public int getData() {
//                return data;
//            }
//
//            public void setData(int data) {
//                this.data = data;
//            }
//        }
    }
}
