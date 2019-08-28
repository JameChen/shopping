package com.nahuo.bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by jame on 2017/9/18.
 */

public class TopicBean {

    /**
     * Title : 热门问题
     * List : [{"Title":"可以退换货吗？","Detail":"例如，做日韩女装的买家，如果是以自己所做的风格为主线，点击进入\u201c女装的日韩风格\u201d页面之后，可以在此基础上点击下面的类目，比如点击\u201c外套\u201d，整个页面将只显示日韩风格的外套。"},{"Title":"尺码怎么选？","Detail":"做日韩女装的买家，如果是自己现在缺什么样的款式(类目）为主线，比如店里缺些外套，你可以先点击进入\u201c女装的外套类目\u201d，然后再点击下面的风格进行筛选，比如点击\u201c日韩\u201d，整个页面将只显示日韩风格的外套。"},{"Title":"怎么申请补货？","Detail":"在每个风格和类目下，还能够看到当前所找的服装类别下相关的推荐商家。"},{"Title":"运费怎么计算？","Detail":"进入女装、男装、童装各栏目后，买家可以根据自己的喜好，随意以服装的风格或者类目为主线进行深入查找"},{"Title":"怎么申请退款？","Detail":"随着酷有网站的不断升级，很多栏目也在不断升级，帮助中心随着栏目同步升级了。"},{"Title":"如何获取积分？","Detail":"亲爱的酷友们，还找不到下单的地方，找不到合适的商品，不知道如何购买"}]
     */
    @Expose
    @SerializedName("Title")
    private String Title;
    @Expose
    @SerializedName("List")
    private java.util.List<ListBean> List;

    public String getTitle() {
        return Title;
    }

    public void setTitle(String Title) {
        this.Title = Title;
    }

    public List<ListBean> getList() {
        return List;
    }

    public void setList(List<ListBean> List) {
        this.List = List;
    }

    public static class ListBean {
        /**
         * Title : 可以退换货吗？
         * Detail : 例如，做日韩女装的买家，如果是以自己所做的风格为主线，点击进入“女装的日韩风格”页面之后，可以在此基础上点击下面的类目，比如点击“外套”，整个页面将只显示日韩风格的外套。
         */
        public  boolean is_expand=false;
        @Expose
        @SerializedName("Title")
        private String Title;
        @Expose
        @SerializedName("Detail")
        private String Detail;

        public String getTitle() {
            return Title;
        }

        public void setTitle(String Title) {
            this.Title = Title;
        }

        public String getDetail() {
            return Detail;
        }

        public void setDetail(String Detail) {
            this.Detail = Detail;
        }
    }
}
