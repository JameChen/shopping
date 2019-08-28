package com.nahuo.quicksale;

import com.nahuo.library.controls.LoadingDialog;
import com.nahuo.quicksale.api.HttpRequestHelper;
import com.nahuo.quicksale.api.HttpRequestListener;
import com.nahuo.quicksale.api.HttpRequestHelper.HttpRequest;
import com.nahuo.quicksale.eventbus.BusEvent;
import com.nahuo.quicksale.eventbus.EventBusId;
import com.nahuo.quicksale.oldermodel.ResultData;
import com.nahuo.quicksale.orderdetail.model.SellerOrderModel;
import com.nahuo.quicksale.orderdetail.model.SendGoodsModel;

import de.greenrobot.event.EventBus;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

/****
 * 售货单修改价格对话框 created by 陈智勇 2015-4-28 上午9:15:54
 */
public class ChangePriceAgentDialog extends ChangePriceDialog1 {

    private float    upPrice, upPost;
    private TextView txtGainMethod;
    private TextView txtGain;
    private TextView txtProduct;
    private TextView txtTotal;
    private boolean  mIsSellerOnlyShipper; // 自己是供货商，中间没有代理
    private float    total, upTotal;
    private View     hide;
    private float    mProductAmount;

    /***
     * 调用show显示对话框
     * 
     * @param orderID
     * @param orderPrice 订单商品金额
     * @param orderPost 订单商品邮费
     * @param upPrice 给供货商的报价
     * @param upPost 给供货商的邮费
     * @param total 商品总计
     * @param upTotal 给上家总计
     * 
     */
    private ChangePriceAgentDialog(Context context, int shipID, float orderPrice, float orderPost, boolean isFreePost,
            float total, float upPrice, float upPost, float upTotal) {
        super(context, shipID, orderPrice, orderPost, isFreePost);
        this.upPost = upPost;
        this.upPrice = upPrice;
        this.total = total;
        this.upTotal = upTotal;
    }

    public ChangePriceAgentDialog(Context context, SellerOrderModel order) {
        this(context, order.getOrderID(), order.getOrderPrice(), order.getPostFee(), order.isIsFreePost(), order
                .getPayableAmount(), order.AgentsProductAmount, order.AgentsShipAmount, order.AgentExpense);

        this.mProductAmount = order.getProductAmount();
        mIsSellerOnlyShipper = order.SellerIsOnlyShipper;

    }

    @Override
    protected void setContent() {
        setContentView(R.layout.dlg_change_price);
    }

    @Override
    protected void initView() {
        super.initView();
        hide = (TextView)findViewById(R.id.txt_hide);
        TextView txtProduct1 = (TextView)findViewById(R.id.txt_dlg_change_price_product2);
        TextView txtPost1 = (TextView)findViewById(R.id.txt_dlg_change_price_post2);
        TextView txtTotal1 = (TextView)findViewById(R.id.txt_dlg_change_price_total2);
        txtProduct1.setText("商品：¥" + df.format(upPrice));
        txtPost1.setText("运费：¥" + df.format(upPost));
        txtTotal1.setText(df.format(upTotal));
        txtGainMethod = (TextView)findViewById(R.id.txt_dlg_change_price_gain);
        txtGain = (TextView)findViewById(R.id.txt_dlg_change_price_gain2);
        setMethodView(total);
        txtProduct = (TextView)findViewById(R.id.et_dlg_change_price_product);
        txtTotal = (TextView)findViewById(R.id.txt_dlg_change_price_total);
        String pstr = df.format(orderPrice);
        txtProduct.setText(pstr);
        ((EditText)txtProduct).setSelection(pstr.length());
        txtTotal.setText(df.format(total));

        txtPost.addTextChangedListener(postWatcher);
        txtProduct.addTextChangedListener(productWatcher);
        if (mIsSellerOnlyShipper) {
            findViewById(R.id.layout_below).setVisibility(View.GONE);
        }
    }

    protected void setMethodView(float oTotal) {
        txtGainMethod.setText(txtGainMethod.getResources().getString(R.string.change_price_method, oTotal, upTotal));
        float total = oTotal - upTotal;
        if (total < 0) {
            txtGain.setTextColor(txtGain.getResources().getColor(R.color.red));
            hide.setVisibility(View.VISIBLE);
        } else {
            hide.setVisibility(View.GONE);
            txtGain.setTextColor(txtGain.getResources().getColor(R.color.green));
        }
        txtGain.setText("¥" + df.format(total));
    }

    TextWatcher productWatcher = new TextWatcher() {
                                   @Override
                                   public void onTextChanged(CharSequence s, int start, int before, int count) {
                                       float orderPrice = 0;
                                       float oPost = 0;
                                       try{
                                           oPost = Float.valueOf(txtPost.getText().toString());
                                       }catch (NumberFormatException e) {
                                           e.printStackTrace();
                                       }
                                       try {
                                           orderPrice = Float.valueOf(s.toString());
                                       } catch (NumberFormatException e) {
                                           e.printStackTrace();
                                       }
                                       float oTotal = orderPrice + oPost;
                                       txtTotal.setText(df.format(oTotal));
                                       setMethodView(oTotal);
                                   }

                                   @Override
                                   public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                                   @Override
                                   public void afterTextChanged(Editable s) {}
                               };
    TextWatcher postWatcher    = new TextWatcher() {
                                   @Override
                                   public void onTextChanged(CharSequence s, int start, int before, int count) {
                                       float orderPost = 0;
                                       float oPrice = 0;
                                       try{
                                           oPrice = Float.valueOf(txtProduct.getText().toString());
                                       } catch (NumberFormatException e) {
                                           e.printStackTrace();
                                       }
                                       try {
                                           orderPost = Float.valueOf(s.toString());
                                       } catch (NumberFormatException e) {
                                           e.printStackTrace();
                                       }
                                       float oTotal = oPrice + orderPost;
                                       txtTotal.setText(df.format(oTotal));
                                       setMethodView(oTotal);
                                   }

                                   @Override
                                   public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                                   @Override
                                   public void afterTextChanged(Editable s) {}
                               };

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.contact_msg_set) {
            if (hide.getVisibility() == View.VISIBLE) {
                ViewHub.showLongToast(v.getContext(), "利润不能小于0");
                return;
            }
            float oPost = 0;
            float oPrice = 0;
            try{
                oPost = Float.valueOf(txtPost.getText().toString());
            }catch (NumberFormatException e) {
                e.printStackTrace();
            }
            try {
                oPrice = Float.valueOf(txtProduct.getText().toString());
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
            final Context context = v.getContext();
            HttpRequestHelper mRequestHelper = new HttpRequestHelper();
            HttpRequest request = mRequestHelper.getRequest(context, "shop/agent/order/UpdateOrderDiscount",
                    new HttpRequestListener() {
                        LoadingDialog dialog = new LoadingDialog(context);

                        @Override
                        public void onRequestSuccess(String method, Object object) {
                            dialog.stop();
                            dismiss();
                            ViewHub.showLongToast(context, "价格修改成功！");
                            {
                                EventBus.getDefault().postSticky(BusEvent.getEvent(EventBusId.CHANGE_PRICE));
                            }
                        }

                        @Override
                        public void onRequestStart(String method) {
                            dialog.start("改价中...");
                        }

                        @Override
                        public void onRequestFail(String method, int statusCode, String msg) {
                            dialog.stop();
                            ViewHub.showLongToast(context, "价格修改失败！");
                        }

                        @Override
                        public void onRequestExp(String method, String msg, ResultData data) {
                            dialog.stop();
                            ViewHub.showLongToast(context, "价格修改失败！");
                        }
                    });
            request.setConvert2Class(SendGoodsModel.class);
            request.addParam("orderId", String.valueOf(shipID));
            request.addParam("postFee", String.valueOf(oPost));
            request.addParam("discount", String.valueOf(oPrice - mProductAmount));
            request.doPost();
        } else {
            dismiss();
        }
    }
}
