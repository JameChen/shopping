package com.nahuo.quicksale.orderdetail;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.nahuo.Dialog.PdMenu;
import com.nahuo.bean.DefectiveBean;
import com.nahuo.library.controls.LoadingDialog;
import com.nahuo.library.controls.NoScrollListView;
import com.nahuo.library.helper.ImageUrlExtends;
import com.nahuo.quicksale.DialogSureGetGoods;
import com.nahuo.quicksale.ItemDetailsActivity;
import com.nahuo.quicksale.OrderPayActivity;
import com.nahuo.quicksale.R;
import com.nahuo.quicksale.RefundByBuyerActivity;
import com.nahuo.quicksale.ViewHub;
import com.nahuo.quicksale.activity.CustomerServiceActivity;
import com.nahuo.quicksale.activity.PackageListActivity;
import com.nahuo.quicksale.activity.VideoActivity1;
import com.nahuo.quicksale.adapter.OrderDetailItemAdapter;
import com.nahuo.quicksale.adapter.SaleAfterDetailAdapter;
import com.nahuo.quicksale.api.BuyOnlineAPI;
import com.nahuo.quicksale.api.HttpRequestHelper.HttpRequest;
import com.nahuo.quicksale.api.HttpRequestHelper1;
import com.nahuo.quicksale.api.HttpRequestListener1;
import com.nahuo.quicksale.api.OrderAPI;
import com.nahuo.quicksale.api.QuickSaleApi;
import com.nahuo.quicksale.api.RequestMethod;
import com.nahuo.quicksale.app.BWApplication;
import com.nahuo.quicksale.base.BaseFragmentActivity;
import com.nahuo.quicksale.common.Const;
import com.nahuo.quicksale.common.ListUtils;
import com.nahuo.quicksale.common.ViewUtil;
import com.nahuo.quicksale.eventbus.BusEvent;
import com.nahuo.quicksale.eventbus.EventBusId;
import com.nahuo.quicksale.oldermodel.OrderButton;
import com.nahuo.quicksale.oldermodel.PinHuoModel;
import com.nahuo.quicksale.oldermodel.PublicData;
import com.nahuo.quicksale.oldermodel.ResultData;
import com.nahuo.quicksale.oldermodel.ShopItemModel;
import com.nahuo.quicksale.oldermodel.SubmitOrderResult;
import com.nahuo.quicksale.oldermodel.quicksale.RecommendModel;
import com.nahuo.quicksale.orderdetail.model.Consignee;
import com.nahuo.quicksale.orderdetail.model.GetBuyOrderModel;
import com.nahuo.quicksale.orderdetail.model.OrderItemModel;
import com.nahuo.quicksale.orderdetail.model.OrderShopModel;
import com.nahuo.quicksale.util.PicOrVideoDownloadService;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/****
 * 拿货单详细 created by 陈智勇 2015-4-23 下午4:24:00
 */
public class GetBuyOrderActivity extends BaseOrderDetailActivity implements View.OnClickListener, HttpRequestListener1 {
    private TextView txtShopName, txtShopContact;
    private TextView txtPostName, txtPostPhone, txtPostAddr, tv_defective_msg;
    private OrderDetailItemAdapter mAdapter;
    private EventBus mEventBus = EventBus.getDefault();
    private TextView tvWeipuAccount;
    private OrderShopModel shopModel;
    private GetBuyOrderModel data;
    private Context mContext = this;
    private HttpRequestHelper1 mHttpRequestHelper1 = new HttpRequestHelper1();
    private View layout_sale_after;
    private DefectiveBean defectiveBean;
    private NoScrollListView lv_sale_after;
    private SaleAfterDetailAdapter saleAfterDetailAdapter;
    private Button btn_apply_after_sale;
    private ShopItemModel mShopItem;
    private int mDetailId, mQId;
    private String Dcim_Path;
    private List<String> nPicsList = new ArrayList<>();
    private List<String> nVideosList = new ArrayList<>();
    private TextView tvTitleCenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setTitle("订单详细");
        setContentView(R.layout.activity_get_order_buy);
        tvTitleCenter = (TextView) findViewById(R.id.tvTitleCenter);
        tvTitleCenter.setText("订单详细");
        Dcim_Path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath();
        mDialog = new LoadingDialog(this);
        orderID = getIntent().getIntExtra(BaseOrderDetailActivity.EXTRA_ORDER_ID, 0);
        BWApplication.getInstance().registerActivity(this);
        initView();
        initData();
        mEventBus.register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mEventBus.unregister(this);
    }

    public void onEventMainThread(BusEvent event) {
        switch (event.id) {
            case EventBusId.REFUND_BUYER_AGRESS:
            case EventBusId.SURE_GET_GOOD:
            case EventBusId.ADD_MEMO:
            case EventBusId.CHANGE_NUMBER:
                initData();
                break;
        }
    }

    @Override
    void initData() {
        super.initData();
        HttpRequest request = mRequestHelper.getRequest(getApplicationContext(),
                RequestMethod.OrderDetailMethod.GET_BUY_ORDER, GetBuyOrderActivity.this);
        request.setConvert2Class(GetBuyOrderModel.class);
        request.addParam("orderId", String.valueOf(orderID));
//        request.addParam("type", "2");
        request.doPost();
    }

    private LoadingDialog mDialog;
    private String fileName = "", vfileName = "";

    //获取详情图片和视频
    private class Task extends AsyncTask<Object, Void, Object> {

        @Override
        protected Object doInBackground(Object... params) {
            //拼货商品信息
            try {
                mShopItem = BuyOnlineAPI.getInstance().getPiHuoItemDetailNew(mDetailId, mQId, PublicData.getCookie(mContext));
                return mShopItem;
            } catch (Exception e) {
                e.printStackTrace();
                return "error:" + e.getMessage();
            }
        }

        @Override
        protected void onPreExecute() {
            mDialog.start("正在获取数据中");

        }

        @Override
        protected void onPostExecute(Object result) {
            if (mDialog.isShowing()) {
                mDialog.stop();
            }
            if (result instanceof String && ((String) result).startsWith("error:")) {
                ViewHub.showLongToast(mContext, ((String) result).replace("error:", ""));
            } else {
                try {
                    ShopItemModel bean = (ShopItemModel) result;
                    if (bean != null) {
                        List<String> picList = new ArrayList<>();
                        List<String> videoList = new ArrayList<>();
                        List<String> videos = bean.getVideos();
                        String[] picArray = bean.getImages();
                        if (videos != null) {
                            if (videos.size() > 0) {
                                videoList.clear();
                                videoList.addAll(videos);
                            }
                        }
                        if (picArray != null) {
                            if (picArray.length > 0) {
                                for (String url : picArray) {
                                    if (!TextUtils.isEmpty(url)) {
                                        picList.add(ImageUrlExtends.getImageUrl(url, 21));
                                    }
                                }
                            }
                        }
                        if (ListUtils.isEmpty(videoList) && ListUtils.isEmpty(picList)) {
                            ViewHub.showShortToast(GetBuyOrderActivity.this, "没有图片和视频资源");
                        } else {
                            if (!ListUtils.isEmpty(picList)) {
                                File downloadDirectory = new File(Dcim_Path + "/" + Const.IMAGES_CASH_PATH);
                                if (!downloadDirectory.exists()) {
                                    downloadDirectory.mkdirs();
                                }
                                nPicsList.clear();
                                for (String url : picList) {
                                    if (!TextUtils.isEmpty(url)) {
                                        try {
                                            fileName = url.substring(url.lastIndexOf("/"), url.lastIndexOf("!"));
                                        } catch (Exception e) {
                                            fileName = "/" + System.currentTimeMillis() + ".jpg";
                                        }
                                        File cacheFile = new File(Dcim_Path + "/" + Const.IMAGES_CASH_PATH + fileName);
                                        if (!cacheFile.exists()) {
                                            nPicsList.add(url);
                                        }
                                    }
                                }

                            }
                            if (!ListUtils.isEmpty(videoList)) {
                                File downloadDirectory = new File(Dcim_Path + VideoActivity1.PINHUP_ROOT_DIRECTORY);
                                if (!downloadDirectory.exists()) {
                                    downloadDirectory.mkdirs();
                                }
                                nVideosList.clear();
                                for (String url : videoList) {
                                    if (!TextUtils.isEmpty(url)) {
                                        try {
                                            vfileName = url.substring(url.lastIndexOf("/"), url.length());
                                        } catch (Exception e) {
                                            vfileName = "/" + System.currentTimeMillis() + ".mp4";
                                        }
                                        File cacheFile = new File(Dcim_Path + VideoActivity1.PINHUP_ROOT_DIRECTORY + vfileName);
                                        if (!cacheFile.exists()) {
                                            nVideosList.add(url);
                                        }
                                    }
                                }
                            }
                            if (ListUtils.isEmpty(nPicsList) && ListUtils.isEmpty(nVideosList)) {
                                ViewHub.showLongToast(GetBuyOrderActivity.this, "图片保存在：DCIM/weipu/weipu_save" + "\n" + "视频保存在：DCIM/pinhuo/pinhuo_video_save");
                            } else {
                                isFinish = false;
                                PicOrVideoDownloadService.getInstace(GetBuyOrderActivity.this, Dcim_Path, nPicsList, nVideosList, new PicOrVideoDownloadService.DownloadStateListener() {
                                    @Override
                                    public void onFinish() {
                                        isFinish = true;


                                    }

                                    @Override
                                    public void onFailed() {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                ViewHub.showShortToast(GetBuyOrderActivity.this, "下载失败");
                                            }
                                        });
                                    }

                                    @Override
                                    public void onUpdate(int pSize, int pTotalCount, int vSize, int vTotalCount, int type) {
                                        Message msg = handler.obtainMessage();
                                        /**
                                         * 将Message对象发送到目标对象
                                         * 所谓的目标对象，就是生成该msg对象的handler对象
                                         */
                                        Bundle b = new Bundle();
                                        b.putInt("type", type);
                                        b.putInt("pSize", pSize);
                                        b.putInt("pTotalCount", pTotalCount);
                                        b.putInt("vSize", vSize);
                                        b.putInt("vTotalCount", vTotalCount);
                                        msg.setData(b);
                                        msg.sendToTarget();
                                    }
                                }).startDownload();
                            }
                        }

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

        }
    }

    boolean isFinish = false;
    PdMenu pdMenu = null;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
                Bundle b = msg.getData();
                int type = b.getInt("type");
                int pSize = b.getInt("pSize");
                int pTotalCount = b.getInt("pTotalCount");
                int vSize = b.getInt("vSize");
                int vTotalCount = b.getInt("vTotalCount");
                pdMenu = PdMenu.getInstance(GetBuyOrderActivity.this);
                pdMenu.dShow("正在下载图片" + pSize + "/" + pTotalCount + "\n" + "正在下载视频" + vSize + "/" + vTotalCount);
                if (type == PicOrVideoDownloadService.TYPE_PIC_VIDEO || type == PicOrVideoDownloadService.TYPE_VIDEO) {
                    if (vSize >= vTotalCount) {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                ViewHub.showShortToast(GetBuyOrderActivity.this, "下载完成");
                                if (pdMenu != null) {
                                    pdMenu.dismiss();
                                    pdMenu = null;
                                }
                            }
                        }, 1000);

                    }
                } else if (type == PicOrVideoDownloadService.TYPE_PIC) {
                    if (pSize >= pTotalCount) {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                ViewHub.showShortToast(GetBuyOrderActivity.this, "下载完成");
                                if (pdMenu != null) {
                                    pdMenu.dismiss();
                                    pdMenu = null;
                                }
                            }
                        }, 1000);

                    }
                }
            }

        }
    };

    @Override
    public void onResume() {
        super.onResume();
        initData();
    }

    @Override
    void initView() {
        super.initView();
        btn_apply_after_sale = (Button) findViewById(R.id.btn_apply_after_sale);
        txtShopName = (TextView) findViewById(R.id.txt_order_shop_shop);
        tvWeipuAccount = (TextView) findViewById(R.id.txt_order_shop_weipu_account);
        txtShopContact = (TextView) findViewById(R.id.txt_order_shop_contact);
        layout_sale_after = findViewById(R.id.layout_sale_after);
        tv_defective_msg = (TextView) findViewById(R.id.tv_defective_msg);
        lv_sale_after = (NoScrollListView) findViewById(R.id.lv_sale_after);
        txtPostName = (TextView) findViewById(R.id.txt_order_post_name);
        txtPostPhone = (TextView) findViewById(R.id.txt_order_post_phone);
        txtPostAddr = (TextView) findViewById(R.id.txt_order_post_address);

        mAdapter = new OrderDetailItemAdapter(mContext);
        itemListView.setAdapter(mAdapter);
        saleAfterDetailAdapter = new SaleAfterDetailAdapter(this);
        lv_sale_after.setAdapter(saleAfterDetailAdapter);
        findViewById(R.id.txt_order_shop_shop).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                OrderShopModel shop = ((GetBuyOrderModel) mOrderInfoMode).getShop();
                if (shop != null) {
//                    Intent userIntent = new Intent(v.getContext(), UserInfoActivity.class);
//                    userIntent.putExtra(UserInfoActivity.EXTRA_USER_ID, shop.getUserID());
//                    v.getContext().startActivity(userIntent);
                }
            }
        });

    }

    void toWeiXun(int userId, String nickName) {
        if (shopModel != null) {
            super.toWeiXun(shopModel.getUserID(), shopModel.getUserName());
        }
    }

    void viewBindData() {
        super.viewBindData();
        GetBuyOrderModel model = (GetBuyOrderModel) mOrderInfoMode;
        data = model;
        shopModel = model.getShop();
        defectiveBean = model.getDefective();
        mQId = model.getQsID();
        if (!ListUtils.isEmpty(model.getItems()))
            mDetailId = model.getItems().get(0).getAgentItemID();
        if (defectiveBean != null) {
            if (defectiveBean.isShowDefectiveInfo()) {
                layout_sale_after.setVisibility(View.VISIBLE);
                if (defectiveBean.isShowBtn()) {
                    if (mOrderInfoMode != null) {
//                        if (mOrderInfoMode.getStatu().equals("已发货")||mOrderInfoMode.getStatu().equals("已完成")){
//                            btnWeiXun.setVisibility(View.GONE);
//                        }
                    }
                    btn_apply_after_sale.setVisibility(View.VISIBLE);
                    btn_apply_after_sale.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (defectiveBean.isHasMoreShipping()) {
                                Intent vendorIntent = new Intent(GetBuyOrderActivity.this, PackageListActivity.class);
                                vendorIntent.putExtra(PackageListActivity.EXTRA_ORDERID, mOrderInfoMode.getOrderID());
                                startActivity(vendorIntent);

                            } else {
                                Intent vendorIntent = new Intent(GetBuyOrderActivity.this, CustomerServiceActivity.class);
                                vendorIntent.putExtra(CustomerServiceActivity.EXTRA_TYPE, CustomerServiceActivity.TYPE_AFTER_SALES_APPLY);
                                vendorIntent.putExtra(CustomerServiceActivity.EXTRA_SHIPPINGID, defectiveBean.getShippingID());
                                vendorIntent.putExtra(CustomerServiceActivity.EXTRA_ORDERID, mOrderInfoMode.getOrderID());
                                startActivity(vendorIntent);
                            }
                        }
                    });
                    if (!TextUtils.isEmpty(defectiveBean.getBtnText()))
                        btn_apply_after_sale.setText(defectiveBean.getBtnText());
                } else {
                    btn_apply_after_sale.setVisibility(View.GONE);
                }
                List<DefectiveBean.DefectiveListBean> defectiveListBeanList = new ArrayList<>();
                defectiveListBeanList = defectiveBean.getDefectiveList();
                if (ListUtils.isEmpty(defectiveListBeanList)) {

                    if (!TextUtils.isEmpty(defectiveBean.getDefectiveMsg())) {
                        tv_defective_msg.setText(defectiveBean.getDefectiveMsg());
                        tv_defective_msg.setVisibility(View.VISIBLE);
                    } else {
                        tv_defective_msg.setVisibility(View.GONE);
                    }
                } else {
                    saleAfterDetailAdapter.setData(defectiveListBeanList);
                    saleAfterDetailAdapter.notifyDataSetChanged();
                    tv_defective_msg.setVisibility(View.GONE);
                }

            } else {
                layout_sale_after.setVisibility(View.GONE);
            }

        }
        if (shopModel != null) {
//            findViewById(R.id.get_buy_shop_parent).setVisibility(View.VISIBLE);
//            String shopName = getString(R.string.shop_name, shopModel.getName());
//            ViewHub.highlightTextView(getApplicationContext(), txtShopName, shopName, R.color.light_blue, 3, shopName.length());
//            tvWeipuAccount.setText(getString(R.string.weipu_account, shopModel.getUserName()));
//            StringBuffer sb = new StringBuffer();
//            sb.append("手机:").append(shopModel.getMobile());
//            if (!TextUtils.isEmpty(shopModel.getQQ())) {
//                sb.append("       QQ:");
//                sb.append(shopModel.getQQ());
//            }
//            txtShopContact.setText(sb);
        }
        Consignee consignee = model.getConsignee();
        if (consignee != null) {
            findViewById(R.id.get_buy_consignee_parent).setVisibility(View.VISIBLE);
            txtPostName.setText(getString(R.string.post_name1, consignee.getRealName()));
            txtPostPhone.setText(getString(R.string.contact, consignee.getMobile()));
            txtPostAddr.setText(getString(R.string.post_address, consignee.getArea() + consignee.getStreet()));
        } else {
            findViewById(R.id.get_buy_consignee_parent).setVisibility(View.GONE);
        }
        for (OrderItemModel oim : mOrderInfoMode.getItems()) {
            oim.setSummary(mOrderInfoMode.getSummary());
        }
        mAdapter.qsid = mOrderInfoMode.getQsID();
        mAdapter.refresh(mOrderInfoMode.getItems());

        addOrderDetailButton1(operateBtnParent, mOrderInfoMode.Buttons, ol, model.getMemo(), model.UnreadTalkingCount);
    }

    private OnClickListener ol = new OnClickListener() {
        @Override
        public void onClick(View v) {
            // String action = (String) v.getTag();
            OrderButton orderButton = (OrderButton) v.getTag();
            String action = orderButton.getAction();
            GetBuyOrderModel model = (GetBuyOrderModel) mOrderInfoMode;
            OrderShopModel shop = model.getShop();
            BaseOrderDetailActivity.removeCommentRedBall(v.getContext(), v, action);
            if (isBaseButtonClick(v.getContext(), action, mOrderInfoMode.getOrderID(),
                    shop == null ? 0 : shop.getUserID(),
                    shop == null ? "" : shop.getUserName(), mOrderInfoMode.getMemo())) {
                return;
            }
            do {
                if (Const.OrderAction.SALE_AFTER.equals(action)) {
                    try {
                        // boolean redirectToList=FunctionHelper.getOrderButtonRedirectToList(btn.getData());
                        if (orderButton.getData() instanceof Double) {
                            int defectiveID = new Double(Double.parseDouble(orderButton.getData().toString())).intValue();
                            if (defectiveID < 0) {
                                Intent vendorIntent = new Intent(mContext, PackageListActivity.class);
                                vendorIntent.putExtra(PackageListActivity.EXTRA_ORDERID, model.getOrderID());
                                mContext.startActivity(vendorIntent);
                            } else {

                                Intent intent = new Intent(mContext, CustomerServiceActivity.class);
                                intent.putExtra(CustomerServiceActivity.EXTRA_TYPE, CustomerServiceActivity.TYPE_AFTER_SALES_APPLY_DETAIL);
                                intent.putExtra(CustomerServiceActivity.EXTRA_APPLYID, defectiveID);
                                mContext.startActivity(intent);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (Const.OrderAction.GOTO_CHANGCI.equals(action)) {
                    int qid = -1;
                    try {
                        if (orderButton.getData() instanceof Double) {
                            int defectiveID = new Double(Double.parseDouble(orderButton.getData().toString())).intValue();
                            //  qid = (int) Double.parseDouble(defectiveID);
                            qid = defectiveID;
                        }
                        } catch(Exception e){
                            e.printStackTrace();
                        }
                        QuickSaleApi.getRecommendShopItems1(mContext,
                                mHttpRequestHelper1,
                                GetBuyOrderActivity.this,
                                qid,
                                0,
                                20,
                                "",
                                Const.SortIndex.DefaultDesc,
                                -1,
                                0);

                    break;
                }
                if (Const.OrderAction.BUYER_CANCEL.equals(action)) {
                    ViewHub.showOkDialog(v.getContext(), getString(R.string.prompt),
                            getString(R.string.cancel_order),
                            getString(R.string.titlebar_btnOK),
                            getString(R.string.titlebar_btnCancel),
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    mLoadingDialog.start("订单取消中...");
                                    OrderAPI.cancelOrder(getApplicationContext(),
                                            mRequestHelper, GetBuyOrderActivity.this,
                                            orderID);
                                }
                            });
                    break;
                }
                if (Const.OrderAction.BUYER_PAY.equals(action)) {
                    // 去支付界面
                    Intent payIntent = new Intent(v.getContext(), OrderPayActivity.class);
                    SubmitOrderResult l;
                    SubmitOrderResult.OrderPay pay = new SubmitOrderResult.OrderPay();
                    pay.OrderID = orderID;
                    pay.Code = mOrderInfoMode.getCode();
                    pay.PayableAmount = Double.parseDouble(mOrderInfoMode.getPayableAmount() + "");

                    pay.SellerUserID = shop.getUserID();
                    pay.Domain = shop.getDomain();
                    pay.ShopID = shop.getShopID();
                    pay.ShopName = shop.getName();
                    pay.SellerUserName = shop.getUserName();
                    payIntent.putExtra(OrderPayActivity.INTENT_PAY_MONEY, pay.PayableAmount);
                    payIntent.putExtra(OrderPayActivity.INTENT_PAY_ORDER_ID, pay.OrderID + "");
                    // payIntent.putExtra(OrderPayActivity.INTENT_SHOW_SUCCESS, true);
                    v.getContext().startActivity(payIntent);
                    break;
                }
                if (Const.OrderAction.BUYER_RETURN.equals(action)) {
                    // ViewHub.showOkDialog(v.getContext(), getString(R.string.prompt)
                    // , "你确定要申请退款吗?", getString(R.string.titlebar_btnOK)
                    // , getString(R.string.titlebar_btnCancel), new
                    // DialogInterface.OnClickListener() {
                    // @Override
                    // public void onClick(DialogInterface dialog, int which) {
                    // mLoadingDialog.start("退款申请中...") ;
                    // HttpRequest request =
                    // mRequestHelper.getRequest(getApplicationContext()
                    // , "shop/agent/refund/BuyerApplyRefund", GetBuyOrderActivity.this );
                    // request.addParam("orderId", String.valueOf(orderID));
                    // request.doPost() ;
                    // }
                    // });
                  //  RefundApplyDialog.getInstance(v.getContext(), action, 0, orderID).show();
                    if (mContext!=null)
                        ((BaseFragmentActivity)mContext).getOrderItemForRefund(mContext,orderID);
                    break;
                }
                if (Const.OrderAction.BUYER_CONFIRM_RECEIPT.equals(action)) {
                    new DialogSureGetGoods(v.getContext(), orderID).show();
                    break;
                }
                if (Const.OrderAction.BUYER_EXPRESS.equals(action)) {
                    // 进入买家物流详细页
                    Intent shipIntent = new Intent(v.getContext(), ShipActivity.class);
                    shipIntent.putExtra(BaseOrderDetailActivity.EXTRA_ORDER_ID, orderID);
                    v.getContext().startActivity(shipIntent);
                    break;
                }
                if (Const.OrderAction.BUYER_RETURN_BILL.equals(action) || Const.OrderAction.BUYER_FOUND_BILL.equals(action)) {
                    // 进入买家退款详细页
                    if (model.getRefund() != null) {
                        Intent retIntent = new Intent(v.getContext(),
                                RefundByBuyerActivity.class);
                        retIntent.putExtra("ID", model.getRefund().getRefundID());
                        v.getContext().startActivity(retIntent);
                    }
                    break;
                }
                if (Const.OrderAction.PICS_ONE_DOWNLOAD.equals(action)) {
                    //一键下图
                    new Task().execute();
                    break;
                }

                if (Const.OrderAction.BUHUO.equals(action)) {// 补货
//                                                                                              String msg = model.getMemo();
//                                               new DialogReplenishment(v.getContext(), msg, data.getItems().get(0).getAgentItemID() + "").show();
//修改去掉qid
                    Intent intent = new Intent(v.getContext(), ItemDetailsActivity.class);
                    intent.putExtra(ItemDetailsActivity.EXTRA_ID, model.getItems().get(0).getAgentItemID());
                    //   intent.putExtra(ItemDetailsActivity.EXTRA_PIN_HUO_ID, model.getQsID());
                    v.getContext().startActivity(intent);
//                                               ViewHub.showTextDialog(mContext, "", msg,
//                                                       "确认", "取消",
//                                                       new ViewHub.EditDialogListener() {
//                                                           @Override
//                                                           public void onOkClick(DialogInterface dialog, EditText editText) {
//
//                                                           }
//
//                                                           @Override
//                                                           public void onOkClick(EditText editText) {// 确认
//                                                               commitBuHuo();
//                                                           }
//
//                                                           @Override
//                                                           public void onNegativecClick() {
//
//                                                           }
//                                                       });
                    break;
                }
            } while (false);
        }
    };


    //    private void commitBuHuo(){
//        OrderAPI.SaveReplenishmentRecord(getApplicationContext(),
//                mRequestHelper, GetBuyOrderActivity.this,
//                data.getItems().get(0).getAgentItemID()+"");
//    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txt_order_shop_shop:
//                UserInfoActivity.toUserInfoActivity(this, shopModel.getUserID());
                break;

        }

    }

    @Override
    public void onRequestStart1(String method) {
        if (RequestMethod.QuickSaleMethod.RECOMMEND_SHOP_ITEMS.equals(method)) {
            mLoadingDialog.start("获取场次信息...");
        }
    }

    @Override
    public void onRequestSuccess1(String method, Object object) {
        mLoadingDialog.stop();
        if (RequestMethod.QuickSaleMethod.RECOMMEND_SHOP_ITEMS.equals(method)) {
            RecommendModel mRecommendModel = (RecommendModel) object;
            RecommendModel.InfoEntity ie = mRecommendModel.getInfo();
//            mDetailId=mRecommendModel.get
//            mDetailId=ie.
            PinHuoModel item = new PinHuoModel();
            item.setID(ie.getID());
            item.IsStart = ie.isIsStart();
            item.setAppCover(ie.getAppCover());
            item.setPicAd(false);
            item.setDescription(ie.getDescription());
            item.setGroupDealCount(ie.getChengTuanCount());
            item.setName(ie.getName());
            item.setPCCover(ie.getPCCover());
            long l = ie.getToTime();
            item.setStartMillis(ie.getStartTime());
            long ll = ie.getEndMillis();
            item.setEndMillis(ie.getToTime());
            item.setLimitPoint(ie.getLimitPoint());
            item.setLimitShopAuth(ie.isLimitShopAuth());
            item.setVisitResult(ie.getVisitResult());
            item.setActivityType(ie.getActivityType());
            item.setHasNewItems(mRecommendModel.NewItems.size() > 0 ? true : false);
            ViewUtil.gotoChangci(mContext, item);
        }
    }

    @Override
    public void onRequestFail1(String method, int statusCode, String msg) {
        mLoadingDialog.stop();
    }

    @Override
    public void onRequestExp1(String method, String msg, ResultData data) {
        mLoadingDialog.stop();
    }


}
