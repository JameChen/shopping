package com.nahuo.quicksale.orderdetail;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextUtils.TruncateAt;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;
import com.nahuo.library.controls.FlowLayout;
import com.nahuo.library.controls.LoadingDialog;
import com.nahuo.library.helper.GsonHelper;
import com.nahuo.library.utils.TimeUtils;
import com.nahuo.quicksale.DialogMemo;
import com.nahuo.quicksale.ItemDetailsActivity;
import com.nahuo.quicksale.LeaveMsgActivity;
import com.nahuo.quicksale.R;
import com.nahuo.quicksale.Topic.PostDetailActivity;
import com.nahuo.quicksale.TradeDialogFragment;
import com.nahuo.quicksale.TradingDetailsActivity;
import com.nahuo.quicksale.ViewHub;
import com.nahuo.quicksale.adapter.OrderRecordDialogAdapter;
import com.nahuo.quicksale.api.HttpRequestHelper;
import com.nahuo.quicksale.api.OrderAPI;
import com.nahuo.quicksale.api.RequestMethod;
import com.nahuo.quicksale.common.Const;
import com.nahuo.quicksale.oldermodel.OrderButton;
import com.nahuo.quicksale.oldermodel.OrderItemRecordDetailModel;
import com.nahuo.quicksale.oldermodel.ReFundModel;
import com.nahuo.quicksale.oldermodel.ResultData;
import com.nahuo.quicksale.orderdetail.model.BaseOrdeInfoModel;
import com.nahuo.quicksale.orderdetail.model.OrderItemModel;
import com.nahuo.quicksale.orderdetail.model.TransferModel;

import java.util.List;

/***
 * 订单详细界面 created by 陈智勇 2015-4-23 下午3:26:27
 */
public class BaseOrderDetailActivity extends BaseOrderActivity {
    private static final String TAG = BaseOrderDetailActivity.class.getSimpleName();
    /***
     * 拿货单详细
     */
    public static final int GET_BUY_ORDER = 1;
    /***
     * 售货单详细
     */
    public static final int GET_SELLER_ORDER = 2;
    /***
     * 代理单详细
     */
    public static final int GET_AGENT_ORDER = 3;
    /***
     * 发货单详细
     */
    public static final int GET_SHIP_ORDER = 4;
    /***
     * 子订单详细
     */
    public static final int GET_CHILD_ORDER = 5;
    /***
     * 拣货单详细
     */
    public static final int Get_PICK_ORDER = 6;
    public static final String EXTRA_ORDER_ID = "orderId";
    public static final String TRANSFERID = "transferID";
    BaseOrdeInfoModel mOrderInfoMode;
    BaseOrdeInfoModel.SettleInfoBean settleInfoBean;
    private TextView statusTxt, orderCreateTxt;
    ListView itemListView;
    ListView recordListView;
    private View recordView;
    private TextView orderCodeTxt, orderCastTxt, orderWayTxt, orderShouldPayTxt, orderCouponTxt;
    FlowLayout operateBtnParent;
    int orderID;
    private final BaseOrderDetailActivity vthis = BaseOrderDetailActivity.this;
    ImageView iv_order_fee;
    public static int TransferID=-1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TransferID=-1;
    }

    /****
     * 子类setcontentview必须在super.oncreate前 created by 陈智勇 2015-4-23 下午4:31:36
     */
    void initView() {
        super.initView();
        statusTxt = (TextView) findViewById(R.id.txt_order_info_state);
        orderCreateTxt = (TextView) findViewById(R.id.txt_order_info_time);
        orderCodeTxt = (TextView) findViewById(R.id.txt_order_info_bill);
        orderCastTxt = (TextView) findViewById(R.id.txt_order_info_money);
        orderWayTxt = (TextView) findViewById(R.id.txt_order_info_buyway);
        orderShouldPayTxt = (TextView) findViewById(R.id.txt_order_info_should_pay);
        orderCouponTxt = (TextView) findViewById(R.id.txt_order_info_coupon);
        itemListView = (ListView) findViewById(R.id.lst_order_info_item);
        iv_order_fee = (ImageView) findViewById(R.id.iv_order_fee);
        iv_order_fee.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BaseOrderDetailActivity.this, PostDetailActivity.class);
                intent.putExtra(PostDetailActivity.EXTRA_TID, 93595);
                intent.putExtra(PostDetailActivity.EXTRA_POST_TYPE, Const.PostType.TOPIC);
                startActivity(intent);
            }
        });
        itemListView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                OrderItemModel model = (OrderItemModel) parent.getAdapter().getItem(position);
                Intent intent = new Intent(parent.getContext(), ItemDetailsActivity.class);
                intent.putExtra(ItemDetailsActivity.EXTRA_ID, model.getAgentItemID());
                parent.getContext().startActivity(intent);
            }
        });
        recordListView = (ListView) findViewById(R.id.order_info_record_list_view);
        recordView = findViewById(R.id.order_info_record_view);
        operateBtnParent = (FlowLayout) findViewById(R.id.ll_order_info_btn_parent);
    }

    void initData() {
        if (mLoadingDialog == null) {
            mLoadingDialog = new LoadingDialog(this);
        }
        mLoadingDialog.start();
    }

    void viewBindData() {
        super.viewBindData();
        // statusTxt.setText(getString(R.string.order_state, mOrderInfoMode.getStatu()));
        if (mOrderInfoMode != null) {
            if (mOrderInfoMode.ShowCSBtn) {
                if (btnWeiXun != null)
                    btnWeiXun.setVisibility(View.VISIBLE);
            } else {
                if (btnWeiXun != null)
                    btnWeiXun.setVisibility(View.GONE);
            }
            settleInfoBean = mOrderInfoMode.getSettleInfo();
            if (settleInfoBean != null) {
                TransferID = settleInfoBean.getTransferID();
            }else {
                TransferID=-1;
            }
        }
        statusTxt.setText(Html.fromHtml("<font color='#FF161515'  >" + "订单状态：" + "</font>" + "<font color='#FF0000'>" + mOrderInfoMode.getStatu() + "</font>"));
        orderCreateTxt.setText(mOrderInfoMode.getCreateDate());
        orderCodeTxt.setText("订单编号:" + mOrderInfoMode.getCode());
        String paStr = getString(R.string.order_money1, mOrderInfoMode.getProductAmount());
        float discount = mOrderInfoMode.getDiscount();
        orderCouponTxt.setText("¥ " + discount);
        orderCastTxt.setText(paStr);
//        if (discount < 0) {
//            paStr = paStr + "  降价:";
//            int len = paStr.length();
//            paStr = paStr + Math.abs(mOrderInfoMode.getDiscount());
//            SpannableString msp = new SpannableString(paStr);
//            msp.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.green_price)), len, paStr.length(),
//                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//            orderCastTxt.setText(msp);
//        } else if (discount > 0) {
//            paStr = paStr + "  涨价:";
//            int len = paStr.length();
//            paStr = paStr + mOrderInfoMode.getDiscount();
//            SpannableString msp = new SpannableString(paStr);
//            msp.setSpan(new ForegroundColorSpan(Color.RED), len, paStr.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//            orderCastTxt.setText(msp);
//        } else {
//            orderCastTxt.setText(paStr);
//        }
        if (mOrderInfoMode.isIsFreePost()) {
            paStr = "快递费用:";
            int len = paStr.length();
            paStr = paStr + mOrderInfoMode.getPostFee() + " (包邮)";
//            SpannableString msp = new SpannableString(paStr);
//            msp.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.green_price)), len, paStr.length(),
//                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            orderWayTxt.setText(paStr);
        } else {
            orderWayTxt.setText(getString(R.string.order_post_money1, mOrderInfoMode.getPostFee()));
        }
        orderShouldPayTxt.setText(getString(R.string.rmb_2, mOrderInfoMode.getPayableAmount()));

        TextView leaveTimeTxt = (TextView) findViewById(R.id.txt_order_info_leave);
        if (mOrderInfoMode.TimeOut != null && (!TextUtils.isEmpty(mOrderInfoMode.TimeOut.PayOt)
                || !TextUtils.isEmpty(mOrderInfoMode.TimeOut.ConfirmOt))) {
            String leaveLabel = null;
            long now = System.currentTimeMillis();
            long outMills = now;
            if (!TextUtils.isEmpty(mOrderInfoMode.TimeOut.PayOt)) {
                leaveLabel = getString(R.string.order_leave_time);
                outMills = TimeUtils.timeStampToMillis(mOrderInfoMode.TimeOut.PayOt);
            } else {
                leaveLabel = "离确认收货时间还有:";
                outMills = TimeUtils.timeStampToMillis(mOrderInfoMode.TimeOut.ConfirmOt);
            }
            leaveTimeTxt.setVisibility(View.VISIBLE);
            int len = leaveLabel.length();
            outMills = (outMills - now) / 1000;
            if (outMills <= 0) {
                SpannableString msp = new SpannableString("已过期");
                msp.setSpan(new ForegroundColorSpan(Color.RED), 0, 3, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                leaveTimeTxt.setText(msp);
            } else {
                outMills = (outMills + 59) / 60;
                String end = null;
                if (outMills < 60) {
                    end = leaveLabel + outMills + "分钟";
                } else {
                    int hour = (int) (outMills / 60);
                    if (hour < 24) {
                        int minute = (int) (outMills % 60);
                        end = leaveLabel + hour + "小时" + minute + "分钟";
                    } else {
                        int day = hour / 24;
                        hour = hour % 24;
                        end = leaveLabel + day + "天" + hour + "小时";
                    }
                }
                SpannableString msp = new SpannableString(end);
                msp.setSpan(new ForegroundColorSpan(Color.RED), len, end.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                leaveTimeTxt.setText(msp);
            }
        } else if (leaveTimeTxt.getVisibility() != View.GONE) {
            leaveTimeTxt.setVisibility(View.GONE);
        }
        initRecordData();
    }

    private HttpRequestHelper mHttpRequestHelper = new HttpRequestHelper();

    private void initRecordData() {
        if (mOrderInfoMode.getItems().size() > 0) {
            OrderAPI.getRecordDetail(this, mHttpRequestHelper, this, mOrderInfoMode.getItems().get(0).getAgentItemID(), mOrderInfoMode.getQsID());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void onRequestSuccess(String method, Object object) {
        super.onRequestSuccess(method, object);
        if (method.equals(RequestMethod.ShopMethod.GET_ORDER_RECORD_DETAIL)) {
            List<OrderItemRecordDetailModel> list = (List<OrderItemRecordDetailModel>) object;
            if (list.size() > 0) {
                recordView.setVisibility(View.VISIBLE);
                OrderRecordDialogAdapter adapter = new OrderRecordDialogAdapter(this, list);
                recordListView.setAdapter(adapter);
            }
        } else if (method.equals(RequestMethod.OrderDetailMethod.SaveReplenishmentRecord)) {
            Toast.makeText(this, "已成功提交", Toast.LENGTH_SHORT).show();
        } else {
            if (object instanceof BaseOrdeInfoModel) {
                mOrderInfoMode = (BaseOrdeInfoModel) object;
                Log.e("red_count_bg", " request success..." + method + "   " + object);
                viewBindData();
            }
        }
    }

    @Override
    public void onRequestExp(String method, String msg, ResultData data) {
        super.onRequestExp(method, msg, data);
    }

    @Override
    public void onRequestFail(String method, int statusCode, String msg) {
        super.onRequestFail(method, statusCode, msg);
        Log.e("red_count_bg", " request success..." + method + "   " + msg);
    }

    /****
     *
     * created by 陈智勇 2015-4-27 上午9:58:27
     *
     * @param orderID 对于单号
     * @param type 单的类型
     */
    public static void toOrderDetail(Context context, int orderID, int type) {
        Intent intent = new Intent();
        intent.putExtra(EXTRA_ORDER_ID, orderID);
        switch (type) {
            case GET_BUY_ORDER:
                intent.setClass(context, GetBuyOrderActivity.class);
                break;
            case GET_SELLER_ORDER:
                intent.setClass(context, GetSellOrderActivity.class);
                break;
            case GET_AGENT_ORDER:
                intent.setClass(context, AgentOrderActivity.class);
                break;
            case GET_SHIP_ORDER:
                intent.setClass(context, SendGoodsActivity.class);
                break;
            case GET_CHILD_ORDER:
                intent.setClass(context, ChildOrderActivity.class);
                break;
            case Get_PICK_ORDER:
                intent.setClass(context, PickingBillActivity.class);
                break;
        }
        context.startActivity(intent);
    }

    public static void toOrderDetail2(Context context, int orderID, int type, int transferID) {
        Intent intent = new Intent();
        intent.putExtra(EXTRA_ORDER_ID, orderID);
        intent.putExtra(TRANSFERID, transferID);
        switch (type) {
            case GET_BUY_ORDER:
                intent.setClass(context, GetBuyOrderActivity.class);
                break;
            case GET_SELLER_ORDER:
                intent.setClass(context, GetSellOrderActivity.class);
                break;
            case GET_AGENT_ORDER:
                intent.setClass(context, AgentOrderActivity.class);
                break;
            case GET_SHIP_ORDER:
                intent.setClass(context, SendGoodsActivity.class);
                break;
            case GET_CHILD_ORDER:
                intent.setClass(context, ChildOrderActivity.class);
                break;
            case Get_PICK_ORDER:
                intent.setClass(context, PickingBillActivity.class);
                break;
        }
        context.startActivity(intent);
    }

    /****
     * 给linearlayout等比分添加按钮 created by 陈智勇 2015-4-27 下午2:25:43
     *
     * @param parent
     * @param buttons
     * @param l
     */
    public static void addOrderDetailButton(FlowLayout parent, List<OrderButton> buttons, OnClickListener l) {
        addOrderDetailButton(parent, buttons, l, null);
    }

    public static void addOrderDetailButton(FlowLayout parent, List<OrderButton> buttons, OnClickListener l, String memo) {
        addOrderDetailButton(parent, buttons, l, memo, 0);

    }

    public static void addOrderDetailButton(FlowLayout parent, List<OrderButton> buttons, OnClickListener l,
                                            String memo, int leveMsgCount) {

        parent.removeAllViews();

        if (buttons != null) {
            int margin = parent.getResources().getDimensionPixelSize(R.dimen.activity_margin);
            FlowLayout.LayoutParams params = new FlowLayout.LayoutParams(margin, margin);
            for (OrderButton model : buttons) {
                if (!model.isEnable())
                    continue;
                Button child = new Button(parent.getContext());
                child.setEllipsize(TruncateAt.END);
                child.setSingleLine(true);
                child.setText(model.getTitle());
                child.setGravity(Gravity.CENTER_VERTICAL);
                // child.getPaint().measureText(text, start, end)
                highlightButton(child, model.isPoint());
                if (model.getType().equals("button")) {
                    child.setTag(model.getAction());
                    child.setOnClickListener(l);
                } else {
                    child.setClickable(false);
                    child.setEnabled(false);
                }
                parent.addView(child, params);
                if (!TextUtils.isEmpty(memo) && Const.OrderAction.MEMO.equals(model.getAction())) {
                    Drawable right = parent.getResources().getDrawable(R.drawable.icon_true);
                    right.setBounds(0, 0, right.getIntrinsicWidth(), right.getIntrinsicHeight());
                    child.setCompoundDrawables(null, null, right, null);

                } else if (leveMsgCount > 0 && Const.OrderAction.LEAVE_MSG.equals(model.getAction())) {//订单留言
                    Drawable right = parent.getResources().getDrawable(R.drawable.red_ball);
                    right.setBounds(0, 0, right.getIntrinsicWidth(), right.getIntrinsicHeight());
                    child.setCompoundDrawables(null, null, right, null);
                }
            }
        }
    }

    public static void addOrderDetailButton1(FlowLayout parent, List<OrderButton> buttons, OnClickListener l,
                                             String memo, int leveMsgCount) {

        parent.removeAllViews();

        if (buttons != null) {
            int margin = parent.getResources().getDimensionPixelSize(R.dimen.activity_margin1);
            FlowLayout.LayoutParams params = new FlowLayout.LayoutParams(margin, margin);
            for (OrderButton model : buttons) {
//                if (!model.isEnable())
//                    continue;
                Button child = new Button(parent.getContext());
                child.setPadding(margin, margin, margin, margin);
                child.setEllipsize(TruncateAt.END);
                child.setSingleLine(true);
                child.setText(model.getTitle());
                child.setGravity(Gravity.CENTER_VERTICAL);
                child.setClickable(model.isEnable());
                // child.getPaint().measureText(text, start, end)
                highlightButton(child, model.isPoint());
                if (model.getType().equals("button")) {
                    child.setTag(model);
                    child.setOnClickListener(l);
                } else {
                    child.setClickable(false);
                    child.setEnabled(false);
                }
                parent.addView(child, params);
                if (!TextUtils.isEmpty(memo) && Const.OrderAction.MEMO.equals(model.getAction())) {
                    Drawable right = parent.getResources().getDrawable(R.drawable.icon_true);
                    right.setBounds(0, 0, right.getIntrinsicWidth(), right.getIntrinsicHeight());
                    child.setCompoundDrawables(null, null, right, null);
                } else if (leveMsgCount > 0 && Const.OrderAction.LEAVE_MSG.equals(model.getAction())) {//订单留言
                    Drawable right = parent.getResources().getDrawable(R.drawable.red_ball);
                    right.setBounds(0, 0, right.getIntrinsicWidth(), right.getIntrinsicHeight());
                    child.setCompoundDrawables(null, null, right, null);
                }
            }
        }
    }

    /**
     * @description 高亮按钮
     * @created 2015-4-28 下午3:05:14
     * @author ZZB
     */
    private static void highlightButton(Button btn, boolean highlight) {
        btn.setBackgroundResource(highlight ? R.drawable.btn_blue : R.drawable.bg_rect_gray_corner);
        btn.setTextColor(highlight ? btn.getResources().getColor(R.color.white) : btn.getResources().getColor(
                R.color.lightblack));
    }

    public static void removeCommentRedBall(final Context context, View v, String action) {
        if (Const.OrderAction.LEAVE_MSG.equals(action)) {
            Button btn = (Button) v;
            btn.setCompoundDrawables(null, null, null, null);
        }
    }

    public static boolean isBaseButtonClick(final Context context, String action, final int orderID, int userID, String userName,
                                            String memo) {
        if (Const.OrderAction.MEMO.equals(action)) {
            new DialogMemo(context, orderID, memo).show();
            return true;
        }
        if (Const.OrderAction.LEAVE_MSG.equals(action)) {
            Intent intent = new Intent(context, LeaveMsgActivity.class);
            intent.putExtra(EXTRA_ORDER_ID, orderID);
            intent.putExtra(LeaveMsgActivity.EXTRA_USER_ID, userID);
            intent.putExtra(LeaveMsgActivity.EXTRA_USER_NAME, userName);
            context.startActivity(intent);
            return true;
        }
        if (Const.OrderAction.MONEY_BACK.equals(action)) {
            Intent intent = new Intent(context, TradingDetailsActivity.class);
            intent.putExtra(TradingDetailsActivity.ORDERID, orderID);
            context.startActivity(intent);
            return true;
        }
        if (Const.OrderAction.SHOW_TRANSFER.equals(action)) {

            final int transferID = ((BaseOrderDetailActivity) context).getIntent().getIntExtra(TRANSFERID, -1);
           if (transferID>-1){
               TransferID=transferID;
           }
            if (TransferID > -1) {

                new AsyncTask<Void, Void, String>() {
                    TransferModel order;

                    @Override
                    protected void onPreExecute() {
                        super.onPreExecute();
                    }

                    @Override
                    protected String doInBackground(Void... params) {
                        String gson;
                        try {
                            gson = OrderAPI.getTransfer2(context, TransferID + "");
                            order = GsonHelper.jsonToObject(gson, new TypeToken<TransferModel>() {
                            });
                        } catch (Exception ex) {
                            Log.e(TAG, "获取待退款发生异常");
                            ex.printStackTrace();
                            return ex.getMessage() == null ? "未知异常" : ex.getMessage();
                        }
                        return "OK";
                    }

                    @Override
                    protected void onPostExecute(String result) {
                        super.onPostExecute(result);
                        if (!result.equals("OK")) {
                            // 验证result
                            if (result.startsWith("401")
                                    || result.startsWith("not_registered")) {
                                Toast.makeText(context, result, Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(context, result, Toast.LENGTH_LONG).show();
                            }
                        } else {
                        /*
                         订单类型：拼货订单退款
                         订单编号：109324-99404934F
                         退款金额：¥1133.00
                         退款时间：2016-03-02 00:33:22
                         退款方：拼货平台
                         退款说明：
                         * */
                            if (order == null) {
                                return;
                            }
                             /*StringBuilder content = new StringBuilder();
                             content.append("<font color='black'>订单类型： </font><font color='gray'>").append(order.TypeName).append("</font><br><br>");
                             //content.append("<font color='black'>订单编号： </font>").append(order.ID).append("<br><br>");
                             content.append("<font color='black'>退款金额： </font><font color='gray'>").append(order.RefundAmount).append("</font><br><br>");
                             content.append("<font color='black'>退款时间： </font><font color='gray'>").append(order.CreateDate).append("</font><br><br>");
                             content.append("<font color='black'>退款方： </font><font color='gray'>").append(order.RefundUserName).append("</font><br><br>");
                             content.append("<font color='black'>退款说明： </font><br><font color='red' size='2'>不含运费，运费单独结算退款</font><br><font color='gray'>").append(order.Desc).append("</font><br>");
                             final TextDlgFragment f = TextDlgFragment.newInstance(content.toString());
                             f.setNegativeListener("关闭", null);
                             f.show(((BaseOrderDetailActivity) context).getSupportFragmentManager(), "TextDlgFragment");*/
                            //更换为新界面
                            ReFundModel item = new ReFundModel();
                            item.setType(order.TypeName);
                            item.setMoney(order.RefundAmount);
                            item.setTime(order.CreateDate);
                            item.setPerson(order.RefundUserName);
                            item.setState(order.Statu);
                            item.setContent(order.Desc);
                            final TradeDialogFragment f = TradeDialogFragment.newInstance(item);
                            f.show(((BaseOrderDetailActivity) context).getSupportFragmentManager(), "TradeDialogFragment");
                        }
                    }
                }.execute();
            } else {
                ViewHub.showShortToast(context, "没有订单");
            }
            return true;
        }
        return false;
    }
}
