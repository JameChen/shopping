package com.nahuo.quicksale;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.Html;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.baidu.mobstat.StatService;
import com.nahuo.quicksale.oldermodel.ReFundModel;
import com.nahuo.quicksale.util.AKUtil;

/**
 * Created by ALAN on 2017/4/20 0020.
 */
public class TradeDialogFragment extends DialogFragment {

    private View mContentView;
    private ReFundModel item;
    private View.OnClickListener mDefaultListener;
    private Button mBtnNegative;
    private TextView left1,left2,leftTime,left3,left4,left5,mType,mMoney,mTime,mPerson,mState,mContent;

    public static final String ARG_CONTENT = "ARG_CONTENT";
    public static TradeDialogFragment newInstance(ReFundModel item) {
        TradeDialogFragment f = new TradeDialogFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_CONTENT,item);
        f.setArguments(args);
        return f;
    }

    public static TradeDialogFragment newInstance(Bundle args) {
        TradeDialogFragment f = new TradeDialogFragment();
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.MyTryUseDialogFragment);
    }

    public void onResume() {
        super.onResume();
        setWidth();
        StatService.onResume(this);
    }
    private void setWidth(){
        DisplayMetrics dm = new DisplayMetrics();
        //获取屏幕信息
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenWidth = dm.widthPixels;
        getDialog().getWindow().setLayout(screenWidth- AKUtil.convertDIP2PX(getActivity(),110), ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    public void onPause() {
        super.onPause();
        StatService.onPause(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContentView = inflater.inflate(R.layout.layout_dlg_trade_log, container, false);

        Bundle args = getArguments();
        if(args!=null){
            item = (ReFundModel)args.getSerializable(ARG_CONTENT);
        }
        initView();
        return mContentView;
    }

    private void initView() {
        initInclude();
        mBtnNegative = (Button) mContentView.findViewById(R.id.btn_negative);
        mDefaultListener = new Listener(null);
        initNegativeBtn();
    }
    private void initInclude(){
        View include1=mContentView.findViewById(R.id.include_id1);
        View include2=mContentView.findViewById(R.id.include_id2);
        View includeTime=mContentView.findViewById(R.id.include_id_time);
        View include3=mContentView.findViewById(R.id.include_id3);
        View include4=mContentView.findViewById(R.id.include_id4);
        View include5=mContentView.findViewById(R.id.include_id5);
        left1 = (TextView) include1.findViewById(android.R.id.title);
        mType = (TextView) include1.findViewById(android.R.id.content);
        left2 = (TextView) include2.findViewById(android.R.id.title);
        mMoney = (TextView) include2.findViewById(android.R.id.content);
        leftTime = (TextView) includeTime.findViewById(android.R.id.title);
        mTime = (TextView) includeTime.findViewById(android.R.id.content);
        left3 = (TextView) include3.findViewById(android.R.id.title);
        mPerson = (TextView) include3.findViewById(android.R.id.content);
        left4 = (TextView) include4.findViewById(android.R.id.title);
        mState = (TextView) include4.findViewById(android.R.id.content);
        left5 = (TextView) include5.findViewById(android.R.id.title);
        mContent = (TextView) include5.findViewById(android.R.id.content);
        left1.setText("订单类型:");
        left2.setText("退款金额:");
        leftTime.setText("退款时间:");
        left3.setText("退款方:  ");
        left4.setText("退款状态:");
        left5.setText("退款说明:");
        setData(item);
    }

    private void initNegativeBtn(){
        mBtnNegative.setOnClickListener(mDefaultListener);
    }

    private void setData(ReFundModel item) {
            mType.setText(item.getType());
            mMoney.setText(item.getMoney());
            mTime.setText(item.getTime());
            mPerson.setText(item.getPerson());
            mState.setText(item.getState());
            mContent.setText(Html.fromHtml("<font color='red' size='2'>不含运费，运费单独结算退款</font><br><font color='gray'>"+item.getContent()+"</font><br>"));
    }

    public void updateText(ReFundModel item) {
        setData(item);
    }

    private class Listener implements View.OnClickListener{
        private View.OnClickListener mListener;

        public Listener(View.OnClickListener listener) {
            super();
            mListener = listener;
        }

        @Override
        public void onClick(View v) {
            if(mListener != null){
                mListener.onClick(v);
            }
            dismiss();
        }

    }
}
