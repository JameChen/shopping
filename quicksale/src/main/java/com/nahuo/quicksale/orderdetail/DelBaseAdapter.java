package com.nahuo.quicksale.orderdetail;

import android.content.DialogInterface;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;

import com.nahuo.library.controls.LoadingDialog;
import com.nahuo.quicksale.DialogEditNumber;
import com.nahuo.quicksale.ViewHub;
import com.nahuo.quicksale.api.HttpRequestHelper;
import com.nahuo.quicksale.api.HttpRequestHelper.HttpRequest;
import com.nahuo.quicksale.api.HttpRequestListener;
import com.nahuo.quicksale.oldermodel.ResultData;
import com.nahuo.quicksale.orderdetail.model.OrderItemModel;
import com.nahuo.quicksale.orderdetail.model.Product;

import java.util.ArrayList;
import java.util.List;

public abstract class DelBaseAdapter extends BaseAdapter{

    protected OnClickListener delProductItemListener = new OnClickListener(){
        @Override
        public void onClick(final View v) {
            final Product p = (Product)v.getTag() ; 
            ViewHub.showOkDialog(v.getContext(), "提示" , "你确定要删除该商品吗？", "确定" ,"取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface d, int which) {
                    final LoadingDialog dialog = new LoadingDialog(v.getContext()) ; 
                    dialog.start("商品删除中...") ;
                    HttpRequestHelper  mRequestHelper      = new HttpRequestHelper();
                    HttpRequest request = mRequestHelper.getRequest(v.getContext()
                            , "shop/agent/order/DeleteOrderItem", new HttpRequestListener() {
                                @Override
                                public void onRequestSuccess(String method, Object object) {
                                    dialog.stop() ; 
                                    ViewHub.showLongToast(v.getContext(), "商品删除成功!") ; 
                                    p.IsDeleted = true ; 
                                    notifyDataSetChanged() ;
                                }
                                @Override
                                public void onRequestStart(String method) {
                                }
                                @Override
                                public void onRequestFail(String method, int statusCode, String msg) {
                                    dialog.stop() ; 
                                    ViewHub.showLongToast(v.getContext(), "商品删除失败!"+msg) ;
                                }
                                @Override
                                public void onRequestExp(String method, String msg, ResultData data) {
                                    dialog.stop() ; 
                                    ViewHub.showLongToast(v.getContext(), "商品删除失败!"+msg) ;
                                }
                            } );
                    request.addParam("id", String.valueOf(p.ID));
                    request.doPost() ;
                }
            });
        }
    } ;
    protected OnClickListener editNumberL = new OnClickListener() {
        @Override
        public void onClick(View v) {
            Product p = (Product)v.getTag() ;
            new DialogEditNumber(v.getContext(), p.ID, p.Qty).show() ;
        }
    };
//    protected OnEditorActionListener eal = new OnEditorActionListener() {
//        @Override
//        public boolean onEditorAction(final TextView v, int actionId, KeyEvent event) {
//            Product p = (Product)v.getTag() ; 
//            HttpRequestHelper requestHelper = new HttpRequestHelper() ; 
//            HttpRequest request = requestHelper.getRequest(v.getContext()
//                    , "shop/agent/order/UpdateOrderItemQty", new HttpRequestListener() {
//                        LoadingDialog dialog = new LoadingDialog(v.getContext());
//                        @Override
//                        public void onRequestSuccess(String method, Object object) {
//                            ViewHub.showLongToast(BWApplication.getInstance(), "商品数量修改成功！") ; 
//                            dialog.dismiss() ;
//                        }
//                        @Override
//                        public void onRequestStart(String method) {
//                            dialog.start("修改中...") ; 
//                        }
//                        @Override
//                        public void onRequestFail(String method, int statusCode, String msg) {
//                            ViewHub.showLongToast(BWApplication.getInstance(), "商品数量修改失败！") ; 
//                            dialog.dismiss() ; 
//                        }
//                        @Override
//                        public void onRequestExp(String method, String msg) {
//                            dialog.dismiss() ; 
//                            ViewHub.showLongToast(BWApplication.getInstance(), "商品数量修改失败！") ; 
//                        }
//                    });
//            
//            request.addParam("qty", v.getText().toString()) ;
//            request.addParam("id", p.ID) ;
//            request.doPost() ;
//            return false;
//        }
//    } ;
    public static List<OrderItemModel> getModels(List<OrderItemModel> models){
        int size = models.size() ; 
        for(int j = size - 1 ; j>=0 ; j--){
            OrderItemModel model = models.get(j) ;
            List<Product> products = model.getProducts() ; 
            if(products.size()>1){
                for(int i = 1 ; i < products.size() ; i++){
                    List<Product> nps = new ArrayList<Product>() ; 
                    nps.add(products.get(i)) ;
                    OrderItemModel itemMode=  new OrderItemModel(model.getAgentItemID()
                            , model.getName() , model.getPrice() , model.getCover() 
                            , model.isIsDeleted() , nps , model.getParent()) ;
                    models.add( j , itemMode) ; 
                }
            }
        }
        return models ; 
    }
}
