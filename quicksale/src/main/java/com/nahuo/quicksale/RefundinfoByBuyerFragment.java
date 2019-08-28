package com.nahuo.quicksale;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nahuo.library.controls.FlowLayout;
import com.nahuo.quicksale.oldermodel.OrderButton;
import com.nahuo.quicksale.oldermodel.RefundPickingBillModel;
import com.nahuo.quicksale.orderdetail.BaseOrderDetailActivity;

import java.text.DecimalFormat;
import java.util.List;

/**
 * 专门用来处理
 */
public class RefundinfoByBuyerFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

    }

    private View                   mView;
    private static String          SER_KEY = "-1";
    private RefundPickingBillModel item;

    private TextView               mHandleRejectOtDate;                 // 回复超时时间
    private TextView               mShipOtDate;                         // 发货超时时间
    private TextView               mFinishDate;                         // 关闭时间
    private TextView               mFinishType;                         // 退款结果
    private TextView               mRefundCode;                         // 退款单号
    private TextView               mStatu;                              // 退款状态
    private TextView               mPayableAmount;                      // 退款状态
    private TextView               mAmount;                             // 退款总额：
    private TextView               mCreateDate;                         // 申请时间
    private TextView               mRefundReason;                       // 退款原因：
    private TextView               mDesc;                               // 退款说明

    private TextView               mIsApplyOt;                          // 回复结果
    private TextView               mReplyDate;                          // 回复时间
    private TextView               mReplyDesc;                          // 回复说明

    private TextView               mCode;                               // 订单单号
    private TextView               mOrderStatu;                         // 订单单号
    private TextView               mSellerUserName;                     // 卖家账号
    private TextView               mProductAmount;                      // 订单金额

    private TextView               mDeliveryDate;                       // 发货时间
    private TextView               mExpressCompany;                     // 快递公司
    private TextView               mExpressCode;                        // 快递单号
    private TextView               mExpressDesc;                        // 快递单号

    private TextView               mRightCreateDate;                    // 维权信息
    private TextView               mRightFinishDate;                    // 仲裁时间
    private TextView               mRightStatu;                         // 维权结果

    private LinearLayout           mllHandleRejectOtDate, mllShipOtDate, mllFinishDate, mllFinishType, mllIsApplyOt,
            llExpressCode, lllog, ll_right;
    private FlowLayout             mrlbtns;
    private DecimalFormat          df      = new DecimalFormat("#0.00");

    public static RefundinfoByBuyerFragment newInstance(RefundPickingBillModel model) {
        RefundinfoByBuyerFragment fragment = new RefundinfoByBuyerFragment();

        Bundle args = new Bundle();

        args.putSerializable(SER_KEY, model);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO Auto-generated method stub

        mView = inflater.inflate(R.layout.frgm_refund_info_buyer, container, false);
        initview(mView);
        return mView;

    }

    // 初始化
    private void initview(View view) {
        mrlbtns = (FlowLayout)view.findViewById(R.id.rl_buttons);

        mHandleRejectOtDate = (TextView)view.findViewById(R.id.txt_HandleRejectOtDate);
        mllHandleRejectOtDate = (LinearLayout)view.findViewById(R.id.ll_HandleRejectOtDate);

        mShipOtDate = (TextView)view.findViewById(R.id.txt_ShipOtDate);
        mllShipOtDate = (LinearLayout)view.findViewById(R.id.ll_ShipOtDate);

        mFinishDate = (TextView)view.findViewById(R.id.txt_FinishDate);
        mllFinishDate = (LinearLayout)view.findViewById(R.id.ll_FinishDate);

        mFinishType = (TextView)view.findViewById(R.id.txt_FinishType);
        mllFinishType = (LinearLayout)view.findViewById(R.id.ll_FinishType);

        mllIsApplyOt = (LinearLayout)view.findViewById(R.id.ll_IsApplyOt);
        llExpressCode = (LinearLayout)view.findViewById(R.id.ll_DeliveryDate);
        mRefundCode = (TextView)view.findViewById(R.id.txt_RefundCode);
        mStatu = (TextView)view.findViewById(R.id.txt_Statu);
        mPayableAmount = (TextView)view.findViewById(R.id.txt_PayableAmount);
        mAmount = (TextView)view.findViewById(R.id.txt_Amount);
        mCreateDate = (TextView)view.findViewById(R.id.txt_CreateDate);
        mRefundReason = (TextView)view.findViewById(R.id.txt_RefundReason);
        mDesc = (TextView)view.findViewById(R.id.txt_Desc);

        mIsApplyOt = (TextView)view.findViewById(R.id.txt_IsApplyOt);
        mReplyDate = (TextView)view.findViewById(R.id.txt_ReplyDate);
        mReplyDesc = (TextView)view.findViewById(R.id.txt_ReplyDesc);

        mCode = (TextView)view.findViewById(R.id.txt_Code);
        mOrderStatu = (TextView)view.findViewById(R.id.txt_OrderStatu);
        mSellerUserName = (TextView)view.findViewById(R.id.txt_SellerUserName);
        mProductAmount = (TextView)view.findViewById(R.id.txt_ProductAmount);

        mDeliveryDate = (TextView)view.findViewById(R.id.txt_DeliveryDate);
        mExpressCompany = (TextView)view.findViewById(R.id.txt_ExpressCompany);
        mExpressCode = (TextView)view.findViewById(R.id.txt_ExpressCode);
        mExpressDesc = (TextView)view.findViewById(R.id.txt_ExpressDesc);

        lllog = (LinearLayout)view.findViewById(R.id.ll_log);
        ll_right = (LinearLayout)view.findViewById(R.id.ll_right);

        mRightCreateDate = (TextView)view.findViewById(R.id.txt_Right_CreateDate);
        mRightFinishDate = (TextView)view.findViewById(R.id.txt_Right_FinishDate);
        mRightStatu = (TextView)view.findViewById(R.id.txt_Right_Statu);
        luoji();
    }

    // 逻辑转用
    private void luoji() {
        item = (RefundPickingBillModel)getArguments().getSerializable(SER_KEY);
        

        // 维权
        if (item.Right != null) {
            ll_right.setVisibility(View.VISIBLE);
            mRightCreateDate.setText(item.Right.getCreateDate());
            mRightFinishDate.setText(item.Right.getFinishDate());
            mRightStatu.setText(item.Right.getStatu());
        }

        if (item.Statu.equals("卖家拒绝退款") && item.Right == null) {
            mllHandleRejectOtDate.setVisibility(View.VISIBLE);
            mHandleRejectOtDate.setText(item.Ot.HandleRejectOtDate);
        }

        if (item.RefundShipping != null) {
            if (item.RefundShipping.getStatu().equals("等待买家发货")) {
                mllShipOtDate.setVisibility(View.VISIBLE);
                mShipOtDate.setText(item.Ot.ShipOtDate);
            }
        }

        if (!TextUtils.isEmpty(item.FinishType)) {
            mllFinishDate.setVisibility(View.VISIBLE);
            mllFinishType.setVisibility(View.VISIBLE);
            mFinishDate.setText(item.FinishDate);
            mFinishType.setText(item.FinishType);
        }
        mRefundCode.setText(item.RefundCode);
        mStatu.setText(item.Statu);

        if (item.IsNeedReturnGoods) {
            if (item.Order.PayableAmount == item.Amount) {
                mPayableAmount.setText("全额退款，且全部退货");
            } else {
                mPayableAmount.setText("部分退款，且全部退货");
            }
        } else {
            if (item.Order.PayableAmount == item.Amount) {
                mPayableAmount.setText("全额退款，但不退货");
            } else {
                mPayableAmount.setText("部分退款，但不退货");
            }
        }

        mAmount.setText("¥" + df.format(item.Amount));
        mCreateDate.setText(item.CreateDate);
        mRefundReason.setText(item.RefundReason);
        mDesc.setText(item.Desc);
        if (item.ReplyResult != null) {
            if (!item.ReplyResult) {

                if (!item.ReplyDate.equals("")) {
                    if (item.Ot.IsApplyOt) {
                        mIsApplyOt.setText("回复超时，系统自动同意退款");
                    } else {
                        if (item.ReplyResult) {
                            mIsApplyOt.setText("同意退款");
                        } else {
                            mIsApplyOt.setText("拒绝退款");
                        }

                    }
                } else {
                    mIsApplyOt.setText("等待卖家回复");

                }
                mReplyDate.setText(item.ReplyDate);
                mReplyDesc.setText(item.ReplyDesc);

            } else {
                mllIsApplyOt.setVisibility(View.GONE);
            }
        } else {
            mllIsApplyOt.setVisibility(View.GONE);
        }

        mCode.setText(item.Order.Code);
        mOrderStatu.setText(item.Order.Statu);
        mSellerUserName.setText(item.Order.SellerUserName);

        mProductAmount.setText("货款(¥" + df.format(item.Order.ProductAmount) + ")+折扣(¥" + df.format(item.Order.Discount)
                + ")+邮费(¥" + df.format(item.Order.PostFee) + ")＝最终支付(¥" + df.format(item.Order.PayableAmount) + ")");

        if (item.RefundShipping != null) {
            if (!TextUtils.isEmpty(item.RefundShipping.getDeliveryDate())) {
                mDeliveryDate.setText(item.RefundShipping.getDeliveryDate());
                mExpressCompany.setText(item.RefundShipping.getExpressCompany());
                mExpressCode.setText(item.RefundShipping.getExpressCode());
                mExpressDesc.setText(item.RefundShipping.getExpressDesc());
            } else {
                llExpressCode.setVisibility(View.GONE);
            }
        } else {
            llExpressCode.setVisibility(View.GONE);
        }

        List<OrderButton> btnlist = item.Buttons;

        BaseOrderDetailActivity.addOrderDetailButton(mrlbtns, btnlist, new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                String tag = v.getTag().toString();

                if (tag.equals("买家取消退款")) {

                    // 弹出同意退款
                    new RefundApplyDialog(getActivity(), tag, item.RefundID, item.OrderID).show();

                } else {
                    // 弹出 修改退款
                    new RefundApplyDialog(getActivity(), tag, item.RefundID, item.OrderID).show();
                }
            }
        });

        if (item.Logs.size() > 0) {
            // refundinfoFragment frag = refundinfoFragment.newInstance((RefundPickingBillModel)result);
            RefundLogFragment frag = RefundLogFragment.newInstance(item);

            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.ll_log, frag);
            transaction.commit();
        } else {
            lllog.setVisibility(View.GONE);
        }

    }
}
