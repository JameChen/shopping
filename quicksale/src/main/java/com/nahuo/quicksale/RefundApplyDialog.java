package com.nahuo.quicksale;

import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.text.Html;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Spinner;
import android.widget.TextView;

import com.nahuo.library.controls.LoadingDialog;
import com.nahuo.quicksale.api.OrderAPI;
import com.nahuo.quicksale.eventbus.BusEvent;
import com.nahuo.quicksale.eventbus.EventBusId;
import com.nahuo.quicksale.oldermodel.RefundOrderItemModel;
import com.nahuo.quicksale.oldermodel.RefundPickingBillModel;
import com.nahuo.quicksale.oldermodel.refundTypeModel;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

public class RefundApplyDialog extends Dialog implements View.OnClickListener {

    private String                 mAction;
    private Context                mContext;
    private RefundApplyDialog      Vthis = this;
    private TextView               mtitle, mContents;
    private LoadingDialog          mDialog;
    private long                   mRefundID, moid;
    private Spinner                msp;
    private TextView               mtxtapplytitle, metdlgdes;
    private RefundOrderItemModel   item;
    // 作为参数
    private RefundPickingBillModel mreturnitem;

    private TextView               mforgatpwd;
    private TextView               metdlgmoney;

    private Boolean                f     = true;

    private RadioButton            rbtn1, rbtn2;
    private TextView               mrightbuyer;
    private TextView               msellertips;

    private TextView               mexpressname;
    private TextView               mexpresscode;
    private TextView               mexpressdesc;

    private static enum Step {
        SELLER_AGREE, SELLER_REFUND, BUYER_CANCLE, LOAD_BUYER_MOD_INFO, BUYER_MOD, SUPPER_REFUND, SUPPER_REFUND_NO,
        BUYER_APPLY, BUYER_RIGHT, SUPPER_RIGHT, BUYER_FAHUO, SUPPER_AGRESS, SUPPER_NOAGRESS
    }
    private static RefundApplyDialog dialog = null;

    public static RefundApplyDialog getInstance(Context context, String action, long refundID, long oid) {
        if (dialog == null) {
            synchronized (RefundApplyDialog.class) {
                if (dialog == null) {
                    dialog = new RefundApplyDialog(context,action,refundID,oid);
                }
            }
        }
        return dialog;
    }

    @Override
    public void dismiss() {
        super.dismiss();
        if (dialog != null)
            dialog = null;
    }

    public RefundApplyDialog(Context context, String action, long refundID, long oid) {
        super(context, R.style.dialog);
        mAction = action;
        mContext = context;
        mDialog = new LoadingDialog(mContext);
        mRefundID = refundID;
        moid = oid;
        // TODO Auto-generated constructor stub
    }

    public RefundApplyDialog(Context context, String action, long refundID, long oid, RefundPickingBillModel returnitem) {
        super(context, R.style.dialog);
        mAction = action;
        mContext = context;
        mDialog = new LoadingDialog(mContext);
        mRefundID = refundID;
        moid = oid;
        mreturnitem = returnitem;
        // TODO Auto-generated constructor stub
    }

    @Override
    public void show() {

        if (mAction.equals("卖家拒绝退款")) {
            setContentView(R.layout.dlg_refund_order_giveup);
        } else if (mAction.equals("买家取消退款")) {
            setContentView(R.layout.dlg_refund_cancel);
        } else if (mAction.equals("买家修改退款")) {
            setContentView(R.layout.dlg_refund_modify);
        } else if (mAction.equals("买家申请退款")) {
            setContentView(R.layout.dlg_refund_apply);
        } else if (mAction.equals("供货商拒绝退款")) {
            setContentView(R.layout.dlg_refund_order_supper_giveup);
        } else if (mAction.equals("供货商同意退款")) {
            setContentView(R.layout.dlg_refund_supper_order);
            TextView tips = (TextView)this.findViewById(R.id.refund_tips);
            tips.setText("在您同意退款后，系统将在最后结算的时候退款给买家。");
        } else if (mAction.equals("买家申请维权")) {
            setContentView(R.layout.dlg_refund_buyer_rights);
        } else if (mAction.equals("供货商维权")) {
            setContentView(R.layout.dlg_refund_buyer_rights);
        } else if (mAction.equals("买家发货")) {
            setContentView(R.layout.dlg_refund_buyer_fahuo);
        } else if (mAction.equals("供货商确认收货")) {
            setContentView(R.layout.dlg_shipper_supper_order);
        } else if (mAction.equals("供货商拒绝收货")) {
            setContentView(R.layout.dlg_shipper_supper_giveup);
        } else {
            // 卖家同意退款
            setContentView(R.layout.dlg_refund_order);
            msellertips = (TextView)this.findViewById(R.id.refund_tips_seller);

            /*
             * if (mreturnitem.SellerIsOnlyShipper || (mreturnitem.IsNeedReturnGoods == false &&
             * mreturnitem.HasNoShipOrder == false)) { msellertips.setText("在您同意退款后，系统将立即退款给买家。"); } else {
             * msellertips.setText("在您同意退款后，还需要等待供货商的退款回复。供货商如果逾期未回复，系统将自动退款。"); }
             */

            if (mreturnitem.SellerIsOnlyShipper == true) {
                if (mreturnitem.IsNeedReturnGoods == false && mreturnitem.HasNoShipOrder == false) {
                    msellertips.setText("在您同意退款后，系统将立即退款给买家。");
                } else {
                    msellertips.setText("同意退款后，需要等待买家退货，最后您确认收货后，货款将退回买家。");
                }
            } else {

                if (mreturnitem.IsNeedReturnGoods == false) {
                    msellertips.setText("在您同意退款后，还需要等待供货商的退款回复。供货商如果逾期未回复，系统将自动退款。");
                } else {
                    msellertips.setText("在您同意退款后，还需要等待供货商的退款回复。最后需要等待买家退货，并且供货商确认收货后，货款将退回买家。");
                }

            }

        }
        WindowManager.LayoutParams params = getWindow().getAttributes();
        DisplayMetrics outMetrics = new DisplayMetrics();
        getWindow().getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
        params.width = outMetrics.widthPixels * 4 / 5;
        onWindowAttributesChanged(params);
        initView();
        super.show();
    }

    private void initView() {

        // 【卖家同意退款】确定
        Button confirm_seller_sure = (Button)findViewById(R.id.sure_confirm_seller);
        // 【卖家同意退款】取消
        Button confirm_seller_no = (Button)findViewById(R.id.sure_cancle_seller);

        // 【卖家拒绝退款】确定
        Button confirm_seller_refund_sure = (Button)findViewById(R.id.sure_confirm_seller_no);
        // 【卖拒绝退款】取消
        Button confirm_seller_refund_no = (Button)findViewById(R.id.sure_cancle_seller_no);

        // 【买家取消退款】确定
        Button confirm_buyer_sure = (Button)findViewById(R.id.sure_confirm_buyer);
        // 【买家拒绝退款】
        Button confirm_buyer_no = (Button)findViewById(R.id.sure_cancle_buyer);

        // 【买家修改退款】确定
        Button confirm_buyer_sure_mod = (Button)findViewById(R.id.sure_confirm_buyer_refund_mod);
        // 【买家修改退款】
        Button confirm_buyer_no_mod = (Button)findViewById(R.id.sure_cancle_buyer_refund_mod);

        // 【买家申请退款】确定
        Button confirm_buyer_sure_apply = (Button)findViewById(R.id.sure_confirm_buyer_refund_apply);
        // 【买家申请退款】
        Button confirm_buyer_no_apply = (Button)findViewById(R.id.sure_cancle_buyer_refund_apply);

        // 【供货商同意退款】 确定
        Button confirm_supper_sure = (Button)findViewById(R.id.sure_confirm_supper);
        // 【供货商同意退款】 取消
        Button confirm_supper_no = (Button)findViewById(R.id.sure_cancle_supper);

        // 【供货商拒绝退款】 确定
        Button confirm_supper_sure_no = (Button)findViewById(R.id.sure_confirm_supper_no);
        // 【供货商拒绝退款】 取消
        Button confirm_supper_no_no = (Button)findViewById(R.id.sure_cancle_supper_no);

        // 【买家申请维权】确定
        Button confirm_buy_right = (Button)findViewById(R.id.sure_confirm_right);
        // 【买家申请维权】取消
        Button confirm_buy_right_no = (Button)findViewById(R.id.sure_cancle_right);

        // 【买家退款发货】取消
        Button confirm_buy_fahuo_no = (Button)findViewById(R.id.sure_cancle_buyer_fahuo);
        // 【买家退款发货】确定
        Button confirm_buy_fahuo = (Button)findViewById(R.id.sure_confirm_buyer_fahuo);

        // 【供货商发货】取消
        Button confirm_supper_shipper_no = (Button)findViewById(R.id.sure_cancle_shipper_supper);
        // 【供货商发货】确定
        Button confirm_supper_shipper = (Button)findViewById(R.id.sure_confirm_shipper_supper);

        // 【供货商拒绝】取消
        Button confirm_supper_shipperrefund_no = (Button)findViewById(R.id.sure_cancle_shipper_refund_supper_no);
        // 【供货商拒绝】确定
        Button confirm_supperrefund_shipper = (Button)findViewById(R.id.sure_confirm_shipper_refund_supper);

        msp = (Spinner)findViewById(R.id.sp_categories);
        mtxtapplytitle = (TextView)findViewById(R.id.txt_apply_title);
        mtitle = (TextView)findViewById(R.id.et_dlg_change_price_product);
        mContents = (TextView)findViewById(R.id.et_dlg_contents);

        metdlgdes = (TextView)findViewById(R.id.et_dlg_des);
        mforgatpwd = (TextView)findViewById(R.id.txt_pwd);
        mrightbuyer = (TextView)findViewById(R.id.et_buyer_right_contents);

        mexpressname = (TextView)findViewById(R.id.et_buyer_express_name);
        mexpresscode = (TextView)findViewById(R.id.et_buyer_express_code);
        mexpressdesc = (TextView)findViewById(R.id.et_buyer_express_desc);

        if (confirm_supper_shipperrefund_no != null)
            confirm_supper_shipperrefund_no.setOnClickListener(this);
        if (confirm_supperrefund_shipper != null)
            confirm_supperrefund_shipper.setOnClickListener(this);

        if (confirm_supper_shipper_no != null)
            confirm_supper_shipper_no.setOnClickListener(this);
        if (confirm_supper_shipper != null)
            confirm_supper_shipper.setOnClickListener(this);

        if (confirm_buy_fahuo != null)
            confirm_buy_fahuo.setOnClickListener(this);
        if (confirm_buy_fahuo_no != null)
            confirm_buy_fahuo_no.setOnClickListener(this);
        if (mforgatpwd != null)
            mforgatpwd.setOnClickListener(this);
        if (confirm_seller_sure != null)
            confirm_seller_sure.setOnClickListener(this);
        if (confirm_seller_no != null)
            confirm_seller_no.setOnClickListener(this);
        if (confirm_seller_refund_sure != null)
            confirm_seller_refund_sure.setOnClickListener(this);
        if (confirm_seller_refund_no != null)
            confirm_seller_refund_no.setOnClickListener(this);
        if (confirm_buyer_sure != null)
            confirm_buyer_sure.setOnClickListener(this);
        if (confirm_buyer_no != null)
            confirm_buyer_no.setOnClickListener(this);
        if (confirm_buyer_sure_mod != null)
            confirm_buyer_sure_mod.setOnClickListener(this);
        if (confirm_buyer_no_mod != null)
            confirm_buyer_no_mod.setOnClickListener(this);
        if (confirm_supper_sure != null)
            confirm_supper_sure.setOnClickListener(this);
        if (confirm_supper_no != null)
            confirm_supper_no.setOnClickListener(this);
        if (confirm_supper_sure_no != null)
            confirm_supper_sure_no.setOnClickListener(this);
        if (confirm_supper_no_no != null)
            confirm_supper_no_no.setOnClickListener(this);

        if (confirm_buyer_sure_apply != null)
            confirm_buyer_sure_apply.setOnClickListener(this);
        if (confirm_buyer_no_apply != null)
            confirm_buyer_no_apply.setOnClickListener(this);
        if (confirm_buy_right != null)
            confirm_buy_right.setOnClickListener(this);
        if (confirm_buy_right_no != null)
            confirm_buy_right_no.setOnClickListener(this);

        if (mAction.equals("买家修改退款") || mAction.equals("买家申请退款")) {
            new Task(Step.LOAD_BUYER_MOD_INFO).execute();

        }

    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub

        switch (v.getId()) {
            case R.id.sure_cancle_seller_no:
            case R.id.sure_cancle_buyer:
            case R.id.sure_cancle_seller:
            case R.id.sure_cancle_buyer_refund_mod:
            case R.id.sure_cancle_supper:
            case R.id.sure_cancle_supper_no:
            case R.id.sure_cancle_buyer_refund_apply:
            case R.id.sure_cancle_right:
            case R.id.sure_cancle_buyer_fahuo:
            case R.id.sure_cancle_shipper_supper:
            case R.id.sure_cancle_shipper_refund_supper_no:
                dismiss();
                break;

            case R.id.sure_confirm_shipper_refund_supper:
                // 【供货商拒绝收货】
                if (TextUtils.isEmpty(mContents.getText())) {
                    ViewHub.showShortToast(mContext, "请填写拒绝签收原因");
                    return;
                }
                new Task(Step.SUPPER_NOAGRESS).execute();
                break;
            case R.id.sure_confirm_shipper_supper:
                // 【供货商确认收货】
                if (mtitle.getText().toString().length() == 0) {
                    ViewHub.showShortToast(mContext, "请输入支付密码！");
                    return;
                } else {
                    new Task(Step.SUPPER_AGRESS).execute();
                }
                break;
            case R.id.sure_confirm_buyer_fahuo:
                // 【买家退款发货】

                if (TextUtils.isEmpty(mexpressname.getText())) {
                    ViewHub.showShortToast(mContext, "请输入快递名称");
                    return;
                }

                if (TextUtils.isEmpty(mexpresscode.getText())) {
                    ViewHub.showShortToast(mContext, "请输入快递单号");
                    return;
                }

                new Task(Step.BUYER_FAHUO).execute();
                break;

            case R.id.sure_confirm_right:
                // 【买家申请维权】

                if (TextUtils.isEmpty(mrightbuyer.getText())) {
                    ViewHub.showShortToast(mContext, "请输入维权内容");
                    return;
                } else {
                    new Task(Step.BUYER_RIGHT).execute();
                }

                break;

            case R.id.sure_confirm_seller:
                // 【卖家同意退款】确定
                if (mtitle.getText().toString().length() == 0) {
                    ViewHub.showShortToast(mContext, "请输入支付密码！");
                    return;
                } else {
                    new Task(Step.SELLER_AGREE).execute();
                }
                break;

            case R.id.sure_confirm_buyer_refund_apply:
                String dlMoney = metdlgmoney.getText().toString();
                // 【买家申请退款】确定
                if (TextUtils.isEmpty(dlMoney)) {
                    ViewHub.showShortToast(mContext, "请输入退款金额");
                    return;
                }
                float money = 0;
                try {
                    money = Float.valueOf(dlMoney);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
                if (money > item.totalPrice) {
                    ViewHub.showShortToast(mContext, "请输入退款金额大于支付金额或格式不对!");
                    return;
                }
                /*
                 * if (Float.parseFloat(metdlgmoney.getText().toString()) > item.orderPrice) {
                 * ViewHub.showShortToast(mContext, "退款金额不能超过订单支付金额"); return; }
                 */
                new Task(Step.BUYER_APPLY).execute();
                
                break;

            case R.id.sure_confirm_seller_no:

                // 【卖家拒绝退款】确定
                new Task(Step.SELLER_REFUND).execute();

                break;

            case R.id.sure_confirm_buyer_refund_mod:
                // 【买家修改退款】确定
                
                if (TextUtils.isEmpty(metdlgmoney.getText())) {
                    ViewHub.showShortToast(mContext, "请输入退款金额");
                    return;
                }
                new Task(Step.BUYER_MOD).execute();
                break;

            case R.id.sure_confirm_buyer:

                // 【买家取消退款】确定
                new Task(Step.BUYER_CANCLE).execute();
                break;

            case R.id.sure_confirm_supper:
                // 【供货商确定退款】确定
                new Task(Step.SUPPER_REFUND).execute();
                break;
            case R.id.sure_confirm_supper_no:
                // 【供货商确定退款】确定
                new Task(Step.SUPPER_REFUND_NO).execute();
                break;

            case R.id.txt_pwd:
                // 【忘记密码】
                ForgetPswActivity.toForgetPayPsw(mContext);

                break;

            default:
                break;
        }

    }

    // 事件处理
    @SuppressWarnings("unused")
    private class Task extends AsyncTask<Object, Void, Object> {
        private Step mStep;

        public Task(Step step) {
            mStep = step;
        }

        @Override
        protected void onPreExecute() {
            switch (mStep) {
                case SELLER_AGREE:
                    mDialog.start("卖家同意退款");
                    break;
                case SELLER_REFUND:
                    mDialog.start("卖家拒绝退款");
                    break;
                case BUYER_CANCLE:
                    mDialog.start("买家取消退款");
                    break;
                case BUYER_MOD:
                    mDialog.start("买家修改退款");
                    break;
                case SUPPER_REFUND:
                    mDialog.start("供货商同意退款");
                    break;
                case SUPPER_REFUND_NO:
                    mDialog.start("供货商拒绝退款");
                    break;
                case BUYER_APPLY:
                    mDialog.start("供货商申请退款");
                case LOAD_BUYER_MOD_INFO:
                    mDialog.start("加载数据中");
                    break;
                case BUYER_RIGHT:
                    mDialog.start("买家申请维权");
                    break;
                case BUYER_FAHUO:
                    mDialog.start("买家退款发货");
                    break;
                case SUPPER_AGRESS:
                    mDialog.start("供货商确认收货");
                    break;
                case SUPPER_NOAGRESS:
                    mDialog.start("供货商拒绝签收");
                    break;
            }
        }

        @Override
        protected Object doInBackground(Object... arg0) {
            // TODO Auto-generated method stub
            try {
                switch (mStep) {
                    case SELLER_AGREE:
                        // 卖家同意退款
                        return OrderAPI.SellerAgreeRefund(mContext, mRefundID, mtitle.getText().toString().trim());

                    case SELLER_REFUND:
                        // 卖家拒绝
                        return OrderAPI.SellerRejectRefund(mContext, mRefundID, mContents.getText().toString().trim());

                    case BUYER_CANCLE:
                        return OrderAPI.BuyerCancelRefund(mContext, mRefundID);
                    case LOAD_BUYER_MOD_INFO:
                        // 加载买家退款info
                        return OrderAPI.GetOrderItemForRefund(mContext, moid);

                    case SUPPER_REFUND:
                        // 供货商同意退款
                        return OrderAPI.ShipperAgreeRefund(mContext, mreturnitem.ShipperRefundID, mtitle.getText()
                                .toString().trim());

                    case SUPPER_REFUND_NO:
                        // 供货商拒绝退款
                        return OrderAPI.ShipperRejectRefund(mContext, mreturnitem.ShipperRefundID, mContents.getText()
                                .toString().trim());

                    case BUYER_APPLY:

                        // 加载买家申请退款info
                        return OrderAPI.BuyerApplyRefund(mContext, moid, f, ((CItem)msp.getSelectedItem()).GetID()
                                .toString(), metdlgmoney.getText().toString(), metdlgdes.getText().toString());

                    case BUYER_MOD:

                        // ((CItem) msp.getSelectedItem()).GetID();
                        // 加载买家退款info
                        return OrderAPI
                                .BuyerModifyRefund(mContext, mRefundID, item.refundWithProduct, ((CItem)msp
                                        .getSelectedItem()).GetID().toString(), metdlgmoney.getText().toString(), metdlgdes.getText()
                                        .toString());

                    case BUYER_RIGHT:
                        return OrderAPI.BuyerApplyRight(mContext, mRefundID, mrightbuyer.getText().toString());

                    case BUYER_FAHUO:
                        return OrderAPI.BuyerDeliver(mContext, mRefundID, mexpressname.getText().toString(),
                                mexpresscode.getText().toString(), mexpressdesc.getText().toString());
                    case SUPPER_AGRESS:
                        // 供货商确认收货
                        return OrderAPI.ShipperConfim(mContext, mreturnitem.ShipperRefundShipping.ShipperRefundID,
                                mtitle.getText().toString().trim());

                    case SUPPER_NOAGRESS:
                        return OrderAPI.ShipperApplyRight(mContext, mreturnitem.ShipperRefundShipping.ShipperRefundID,
                                mContents.getText().toString().trim());

                }
            } catch (Exception e) {
                e.printStackTrace();
                return "error:" + e.getMessage();
            }
            return null;
        }

        protected void onPostExecute(Object result) {

            if (mDialog.isShowing()) {
                mDialog.dismiss();
            }
            if (result instanceof String && ((String)result).startsWith("error:")) {
                ViewHub.showLongToast(mContext, ((String)result).replace("error:", ""));
            } else {

                switch (mStep) {
                    case SELLER_AGREE:
                    case SELLER_REFUND:

                        dismiss();
                        EventBus.getDefault().post(BusEvent.getEvent(EventBusId.REFUND_SELLER_AGRESS, "ok"));
                        break;

                    case LOAD_BUYER_MOD_INFO:
                        // 买家修改退款的view
                        item = (RefundOrderItemModel)result;
                        initloadview(item);
                        break;
                    case BUYER_CANCLE:
                    case BUYER_APPLY:
                    case BUYER_MOD:
                    case BUYER_RIGHT:
                    case BUYER_FAHUO:
                        dismiss();
                        EventBus.getDefault().post(BusEvent.getEvent(EventBusId.REFUND_BUYER_AGRESS, "ok"));
                        break;
                    case SUPPER_REFUND_NO:
                    case SUPPER_REFUND:

                        dismiss();
                        EventBus.getDefault().post(BusEvent.getEvent(EventBusId.REFUND_SUPP_AGRESS, "ok"));
                        break;

                    case SUPPER_AGRESS:
                    case SUPPER_NOAGRESS:
                        dismiss();
                        EventBus.getDefault().post(BusEvent.getEvent(EventBusId.REFUND_SUPP_AGRESS, "ok"));
                        EventBus.getDefault().post(BusEvent.getEvent(EventBusId.REFUND_SELLER_AGRESS, "ok"));
                        break;

                }
            }

        }

        // 加载view
        private void initloadview(RefundOrderItemModel item) {
            metdlgmoney = (TextView)Vthis.findViewById(R.id.et_dlg_money);

            // 提示

            if (metdlgmoney != null)
                metdlgmoney.setText(item.totalPrice + "");
            LinearLayout llisDeliver = (LinearLayout)Vthis.findViewById(R.id.ll_isDeliver);
            LinearLayout llnoisDeliver = (LinearLayout)Vthis.findViewById(R.id.ll_notisDeliver);
            LinearLayout llmoney = (LinearLayout)Vthis.findViewById(R.id.ll_money);
            rbtn1 = (RadioButton)Vthis.findViewById(R.id.radionorefundWithProduct);
            rbtn2 = (RadioButton)Vthis.findViewById(R.id.radiorefundWithProduct);
            if (!item.isDeliver) {
                llisDeliver.setVisibility(View.VISIBLE);
                String styledText = "已付商品货款：<font color='red'>¥" + item.orderPrice + "</font>，分摊运费：<font color='red'>¥"
                        + item.expressFee + "</font>，总计可退：<font color='#09F709'>¥" + item.totalPrice + "</font>。";
                mtxtapplytitle.setText(Html.fromHtml(styledText), TextView.BufferType.SPANNABLE);

            } else {

                llnoisDeliver.setVisibility(View.VISIBLE);
                if (item.CanRefundMaxAmount <= 0) {
                    ((TextView)Vthis.findViewById(R.id.txt_canrefund)).setVisibility(View.VISIBLE);
                    rbtn1.setVisibility(View.GONE);
                } else {
                    //
                    rbtn1.setVisibility(View.VISIBLE);
                }

                // 退款金额显示
                llmoney.setVisibility(View.VISIBLE);

            }

            if (item.lastModify) {
                ((TextView)Vthis.findViewById(R.id.txt_title_tips)).setVisibility(View.VISIBLE);
            }

            List<refundTypeModel> list = item.refundType;
            List<CItem> list1 = new ArrayList<RefundApplyDialog.Task.CItem>();
            for (refundTypeModel it : list) {
                CItem ct = new CItem(it.id + "", it.name);
                list1.add(ct);
            }

            ArrayAdapter<CItem> Adapter = new ArrayAdapter<CItem>(mContext, R.layout.layout_my_items_head_spinner_item,
                    list1);
            Adapter.setDropDownViewResource(R.layout.drop_down_item);
            msp.setAdapter(Adapter);

            RadioGroup group = (RadioGroup)Vthis.findViewById(R.id.radioGroup);
            // 绑定一个匿名监听器
            group.setOnCheckedChangeListener(new OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(RadioGroup arg0, int arg1) {
                    // TODO Auto-generated method stub
                    // 获取变更后的选中项的ID
                    if (arg1 == rbtn2.getId()) {
                        // Toast.makeText(mContext, "true", 100).show();
                        f = true;
                    } else {
                        // Toast.makeText(mContext, "false", 100).show();
                        f = false;
                    }

                }
            });

        }

        public class CItem {

            private String ID    = "";
            private String Value = "";

            public CItem() {
                ID = "";
                Value = "";
            }

            public CItem(String _ID, String _Value) {
                ID = _ID;
                Value = _Value;
            }

            @Override
            public String toString() { // 为什么要重写toString()呢？因为适配器在显示数据的时候，如果传入适配器的对象不是字符串的情况下，直接就使用对象.toString()
                // TODO Auto-generated method stub
                return Value;
            }

            public String GetID() {
                return ID;
            }

            public String GetValue() {
                return Value;
            }
        }
    }
}
