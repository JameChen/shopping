package com.nahuo.quicksale;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;
import android.widget.TextView;

import com.nahuo.library.controls.LoadingDialog;
import com.nahuo.library.utils.TimeUtils;
import com.nahuo.quicksale.adapter.LeaveMsgAdapter;
import com.nahuo.quicksale.api.HttpRequestHelper;
import com.nahuo.quicksale.api.HttpRequestHelper.HttpRequest;
import com.nahuo.quicksale.api.HttpRequestListener;
import com.nahuo.quicksale.oldermodel.LeaveMsgModel;
import com.nahuo.quicksale.oldermodel.LeaveResultModel;
import com.nahuo.quicksale.oldermodel.ResultData;
import com.nahuo.quicksale.orderdetail.BaseOrderDetailActivity;

import java.util.List;


/***
 * 留言界面
 * created by 陈智勇   2015-4-29  上午9:34:18
 */
public class LeaveMsgActivity extends BaseActivity2{

    private ListView mListView ; 
    private TextView txtEditMsg ; 
    private int targetUserId ; 
    private int orderID ; 
    public static final String EXTRA_USER_ID = "targetUserId"; 
    public static final String EXTRA_USER_NAME = "targetUserName"; 
    private int pageIndex = 1 ; 
    private LeaveMsgAdapter mAdapter ; 
    private boolean loadMoreing ; 
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("留言") ;
        setContentView(R.layout.activity_leave_msg) ; 
        initExtras() ; 
        mLoadingDialog = new LoadingDialog(this) ; 
        initView() ; 
        initData()  ;
    }

    private void initExtras() {
        Intent data = getIntent() ; 
        orderID = data.getIntExtra(BaseOrderDetailActivity.EXTRA_ORDER_ID, 0) ; 
        targetUserId = data.getIntExtra(EXTRA_USER_ID, 0) ; 
    }

    private void initView() {
        mListView = (ListView)findViewById(R.id.list) ; 
        mAdapter = new LeaveMsgAdapter(getIntent().getStringExtra(EXTRA_USER_NAME) , targetUserId) ; 
        mListView.setAdapter(mAdapter) ; 
        mListView.setOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                
            }
            
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if(firstVisibleItem == 0&&!loadMoreing){
                    if(mAdapter.getCount()>0&&mAdapter.getCount()%40 == 0){
                        loadMoreing = true ; 
                        pageIndex ++ ; 
                        initData() ;
                    }
                }
            }
        }) ; 
        txtEditMsg = (TextView)findViewById(R.id.et_sendmessage);
    }

    private void initData() {
        if(mLoadingDialog == null)
            mLoadingDialog = new LoadingDialog(this) ; 
        mLoadingDialog.start() ; 
        HttpRequest request = mRequestHelper.getRequest(getApplicationContext(), "shop/agent/order/GetTalkingList",this);
        request.setConvert2Class(LeaveResultModel.class) ;
        request.addParam(BaseOrderDetailActivity.EXTRA_ORDER_ID, String.valueOf(orderID));
        request.addParam(EXTRA_USER_ID, String.valueOf(targetUserId));
        request.addParam("pageIndex" , String.valueOf(pageIndex)) ; 
        request.addParam("pageSize", "40") ;
        request.doPost() ;
        
        request = mRequestHelper.getRequest(getApplicationContext(), "shop/agent/order/SetTalkingHadRead",null);
        request.setConvert2Class(LeaveResultModel.class) ;
        request.addParam(BaseOrderDetailActivity.EXTRA_ORDER_ID, String.valueOf(orderID));
        request.doPost() ;
        
    }

    @Override
    public void onRequestSuccess(String method, Object object) {
        super.onRequestSuccess(method, object);
        Log.e("red_count_bg" , object.toString());
        LeaveResultModel data = (LeaveResultModel)object ;
        List<LeaveMsgModel> models = data.Datas ; 
        if(models.size()>0&&models.size()%40==0)
            loadMoreing = false ; 
        mAdapter.loadMore(models) ;
        if(pageIndex == 1){
            if(mListView.getCount()>1){
                mListView.setSelection(mListView.getCount() - 1) ; 
            }
        }
        else{
            if(models.size()>0)
                mListView.setSelection(models.size() -1) ;
        }
    }

    @Override
    public void onRequestFail(String method, int statusCode, String msg) {
        super.onRequestFail(method, statusCode, msg);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
    public void commitMsg(final View v){
        String msg = txtEditMsg .getText().toString() ; 
        if(TextUtils.isEmpty(msg)){
            ViewHub.showLongToast(v.getContext(), "你还没输入信息！") ;
            return ; 
        }
        final LeaveMsgModel model = new LeaveMsgModel()  ; 
        model.CreateDate = TimeUtils.getDefaultTimeStamp(System.currentTimeMillis()) ;
        model.FromUserName = ""; 
        model.Message = msg ; 
        model.success = true ; 
        mAdapter.addMsg(model);
        mListView.setSelection(mListView.getCount() -1) ;
        HttpRequestHelper  mRequestHelper      = new HttpRequestHelper();
        HttpRequest request = mRequestHelper.getRequest(v.getContext()
                , "shop/agent/order/SaveTalk", new HttpRequestListener() {
                    @Override
                    public void onRequestSuccess(String method, Object object) {
                    }
                    @Override
                    public void onRequestStart(String method) {
                    }
                    @Override
                    public void onRequestFail(String method, int statusCode, String msg) {
                        model.success = false ; 
//                        mAdapter.notifyDataSetChanged() ; 
                    }
                    @Override
                    public void onRequestExp(String method, String msg, ResultData data) {
                        model.success = false ;
//                        mAdapter.notifyDataSetChanged() ; 
                    }
                } );
        request.addParam("orderId", String.valueOf(orderID));
        request.addParam("message", msg);
        request.addParam("targetUserId", String.valueOf(targetUserId));
        request.doPost() ;
        txtEditMsg.setText(null) ;
    }


}
