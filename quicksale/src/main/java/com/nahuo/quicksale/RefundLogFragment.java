package com.nahuo.quicksale;

import com.nahuo.quicksale.adapter.RefundLogAdapter;
import com.nahuo.quicksale.oldermodel.RefundPickingBillModel;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

/**
 * @author 退款日志
 * 
 */
public class RefundLogFragment extends Fragment {

    private static String          SER_KEY = "-2";
    private View                   mView;
    private ListView               lv;
    private RefundLogAdapter       adapter;
    private RefundPickingBillModel item;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

    }

    public static RefundLogFragment newInstance(RefundPickingBillModel model) {
        RefundLogFragment fragment = new RefundLogFragment();

        Bundle args = new Bundle();

        args.putSerializable(SER_KEY, model);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO Auto-generated method stub

        mView = inflater.inflate(R.layout.frgm_refund_log, container, false);
        initview(mView);
        return mView;

    }

    private void initview(View view) {

        item = (RefundPickingBillModel)getArguments().getSerializable(SER_KEY);
        lv = (ListView)view.findViewById(R.id.lv_logs);
        adapter = new RefundLogAdapter(getActivity());
        adapter.setData(item.Logs);
        lv.setAdapter(adapter);
    }
}
