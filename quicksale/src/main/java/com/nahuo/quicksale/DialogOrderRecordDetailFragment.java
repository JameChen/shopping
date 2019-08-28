package com.nahuo.quicksale;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.nahuo.quicksale.adapter.OrderRecordDialogAdapter;
import com.nahuo.quicksale.oldermodel.OrderItemRecordDetailModel;

import java.util.List;

public class DialogOrderRecordDetailFragment extends DialogFragment implements View.OnClickListener {

    private Context mContext;
    private ListView lv;
    private OrderRecordDialogAdapter adapter;
    public List<OrderItemRecordDetailModel> datas;

    public static DialogOrderRecordDetailFragment newInstance(List<OrderItemRecordDetailModel> _datas) {
        DialogOrderRecordDetailFragment f = new DialogOrderRecordDetailFragment();
        f.datas = _datas;
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.theme_dialog_fragment);
        mContext = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.dlg_order_record_detail, container, false);
        initView(contentView);
        return contentView;
    }

    private void initView(View contentView) {
    	lv = (ListView) contentView.findViewById(R.id.id_listview);
        adapter = new OrderRecordDialogAdapter(mContext,datas);
        if (datas.size()>3)
        {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 400);


            lv.setLayoutParams(params);
        }
        lv.setAdapter(adapter);
        Button closeBtn = (Button) contentView.findViewById(R.id.contact_msg_cancel);
        closeBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.contact_msg_cancel:
                dismiss();
            break;
        default:
            break;
        }
    }
}
