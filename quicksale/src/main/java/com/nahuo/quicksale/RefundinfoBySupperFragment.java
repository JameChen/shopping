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
public class RefundinfoBySupperFragment extends Fragment {

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

    private TextView               mDesc;                               // 退款说明

    private TextView               mIsApplyOt;                          // 回复结果

    private TextView               mReplyDate;                          // 回复时间
    private TextView               mReplyDesc;                          // 回复说明

    private TextView               mCode;                               // 订单单号
    private TextView               mOrderStatu;                         // 订单单号
    private TextView               mUserName;                           // 买家账号
    private TextView               mProductAmount;                      // 订单金额

    private FlowLayout             mrlbtns;
    private DecimalFormat          df      = new DecimalFormat("#0.00");

    private LinearLayout           mllHandleRejectOtDate, mllShipOtDate, mllFinishDate, mllFinishType, mllIsApplyOt,
            lllog;

    public static RefundinfoBySupperFragment newInstance(RefundPickingBillModel model) {
        RefundinfoBySupperFragment fragment = new RefundinfoBySupperFragment();

        Bundle args = new Bundle();

        args.putSerializable(SER_KEY, model);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO Auto-generated method stub

        mView = inflater.inflate(R.layout.frgm_refund_info_supper, container, false);
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

        // llExpressCode = (LinearLayout)view.findViewById(R.id.ll_DeliveryDate);
        mRefundCode = (TextView)view.findViewById(R.id.txt_RefundCode);
        mStatu = (TextView)view.findViewById(R.id.txt_Statu);
        mPayableAmount = (TextView)view.findViewById(R.id.txt_PayableAmount);
        mAmount = (TextView)view.findViewById(R.id.txt_Amount);
        mCreateDate = (TextView)view.findViewById(R.id.txt_CreateDate);

        mDesc = (TextView)view.findViewById(R.id.txt_Desc);

        mIsApplyOt = (TextView)view.findViewById(R.id.txt_IsApplyOt);
        mReplyDate = (TextView)view.findViewById(R.id.txt_ReplyDate);
        mReplyDesc = (TextView)view.findViewById(R.id.txt_ReplyDesc);

        mCode = (TextView)view.findViewById(R.id.txt_Code);
        mOrderStatu = (TextView)view.findViewById(R.id.txt_OrderStatu);
        mUserName = (TextView)view.findViewById(R.id.txt_UserName);
        mProductAmount = (TextView)view.findViewById(R.id.txt_ProductAmount);

        lllog = (LinearLayout)view.findViewById(R.id.ll_log);

        luoji();
    }

    // 逻辑转用
    private void luoji() {
        item = (RefundPickingBillModel)getArguments().getSerializable(SER_KEY);
        if (item.Statu.equals("买家申请退款")) {
            mllHandleRejectOtDate.setVisibility(View.VISIBLE);
            mHandleRejectOtDate.setText(item.Ot.HandleApplyOtDate);

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
            if (item.OrderShipping.PayableAmount == item.Amount) {
                mPayableAmount.setText("全额退款，且全部退货");
            } else {
                mPayableAmount.setText("部分退款，且全部退货");
            }
        } else {
            if (item.OrderShipping.PayableAmount == item.Amount) {
                mPayableAmount.setText("全额退款，但不退货");
            } else {
                mPayableAmount.setText("部分退款，但不退货");
            }
        }

        mAmount.setText("¥" + df.format(item.Amount));
        mCreateDate.setText(item.CreateDate);

        mDesc.setText(item.Desc);
        if (item.ReplyResult != null) {
         /*   if (!item.ReplyResult) {*/

                if (item.Ot.IsApplyOt) {
                    mIsApplyOt.setText("回复超时，系统自动同意退款");
                } else {
                    if (item.ReplyResult) {
                        mIsApplyOt.setText("同意退款");
                    } else {
                        mIsApplyOt.setText("拒绝退款");
                    }

                }
                mReplyDate.setText(item.ReplyDate);
                mReplyDesc.setText(item.ReplyDesc);

        /*    } else {
                mllIsApplyOt.setVisibility(View.GONE);
            }*/
        } else {
            mllIsApplyOt.setVisibility(View.GONE);
        }

        mCode.setText(item.OrderShipping.Code);
        mOrderStatu.setText(item.OrderShipping.Statu);
        mUserName.setText(item.OrderShipping.BuyerUserName);

        mProductAmount.setText("货款(¥" + df.format(item.OrderShipping.Amount) + ")+邮费(¥"
                + df.format(item.OrderShipping.PostFee) + ")＝全部(¥" + df.format(item.OrderShipping.PayableAmount) + ")");

        List<OrderButton> btnlist = item.Buttons;

        BaseOrderDetailActivity.addOrderDetailButton(mrlbtns, btnlist, new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                String tag = v.getTag().toString();

                if (tag.equals("供货商同意退款")) {

                    // 弹出同意退款
                    new RefundApplyDialog(getActivity(), tag, item.RefundID, item.OrderID, item).show();

                } else if (tag.equals("供货商拒绝退款")) {
                    // 弹出拒绝退款
                    new RefundApplyDialog(getActivity(), tag, item.RefundID, item.OrderID, item).show();
                } else {
                    new RefundApplyDialog(getActivity(), tag, item.RefundID, item.OrderID, item).show();
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
