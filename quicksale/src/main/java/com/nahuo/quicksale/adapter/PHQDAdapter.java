package com.nahuo.quicksale.adapter;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nahuo.library.controls.LoadingDialog;
import com.nahuo.library.helper.ImageUrlExtends;
import com.nahuo.quicksale.R;
import com.nahuo.quicksale.ViewHub;
import com.nahuo.quicksale.api.QuickSaleApi;
import com.nahuo.quicksale.common.Const;
import com.nahuo.quicksale.controls.NFHSZDialog;
import com.nahuo.quicksale.oldermodel.ApplyListModel;
import com.nahuo.quicksale.oldermodel.PHQDDetailModel;
import com.nahuo.quicksale.oldermodel.PHQDModel;
import com.nahuo.quicksale.orderdetail.BaseOrderDetailActivity;
import com.nahuo.quicksale.orderdetail.GetBuyOrderActivity;
import com.squareup.picasso.Picasso;

import java.util.List;

public class PHQDAdapter extends MyBaseAdapter<PHQDModel> {

    private PHQDAdapter vThis = this;
    private int workWareHouseID = 0;
    private LoadingDialog mLoadingDialog;
    private List<PHQDDetailModel> data;
    private ApplyListModel workApplyData;
    PHQDDetailAdapter adapter = new PHQDDetailAdapter();
    private Activity activity;

    public PHQDAdapter(Activity context) {
        super(context);
        mContext = context;
        activity=context;
        mLoadingDialog = new LoadingDialog(mContext);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        final PHQDModel item = mdata.get(position);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.layout_phqd_listview, parent, false);
            holder = new ViewHolder();
            holder.txt1 = (TextView) convertView.findViewById(R.id.txt1);
            holder.txt2_select = (TextView) convertView.findViewById(R.id.txt2_select);
            holder.txt2_detail = (TextView) convertView.findViewById(R.id.txt2_detail);
            holder.txt3 = (TextView) convertView.findViewById(R.id.txt3);
            holder.txt4 = (TextView) convertView.findViewById(R.id.txt4);
            holder.fhsz = (TextView) convertView.findViewById(R.id.fhsz);
            holder.btn = (TextView) convertView.findViewById(R.id.btnGoWL);
            holder.imgUp = (ImageView) convertView.findViewById(R.id.btnUp);
            holder.info = (ListView) convertView.findViewById(R.id.list);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.txt1.setText(item.getWareHouseName());
        String t = "";
        for (PHQDModel.ShipTypeListBean p : item.getShipTypeList()) {
            if (p.isIsSelected()) {
                t = p.getName();
                break;
            }
        }
        holder.txt2_select.setText(!TextUtils.isEmpty(t) ? t : item.getShipTypeList().get(0).getName());
        holder.txt2_detail.setText(item.getShipConfigDesc());
        holder.txt3.setText(item.getAllotTimeDesc());
        holder.txt4.setText(item.getAllotDesc());
        holder.txt2_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedIndex = 0;
                workWareHouseID = item.getWareHouseID();
                final String items[] = new String[item.getShipTypeList().size()];
                for (int i = 0; i < item.getShipTypeList().size(); i++) {
                    items[i] = item.getShipTypeList().get(i).getName();
                    if (item.getShipTypeList().get(i).isIsSelected()) {
                        selectedIndex = i;
                    }
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setTitle("选择配送方式");
                builder.setSingleChoiceItems(items, selectedIndex, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        selectShipTypeName(items[which]);
                    }
                });
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        if (which >= 0 && which <= items.length) {
                            selectShipTypeName(items[which]);
                        }
                    }
                });
                builder.create().show();
            }
        });
        holder.fhsz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                workWareHouseID = item.getWareHouseID();
                new AsyncTask<Void, Void, Object>() {

                    @Override
                    protected void onPreExecute() {
                        super.onPreExecute();
                        mLoadingDialog.start("");
                    }

                    @Override
                    protected Object doInBackground(Void... params) {
                        try {
                            return QuickSaleApi.getApplyList(mContext, workWareHouseID);
                        } catch (Exception e) {
                            e.printStackTrace();
                            return "error:" + e.getMessage();
                        }
                    }

                    @Override
                    protected void onPostExecute(final Object result) {
                        super.onPostExecute(result);
                        mLoadingDialog.stop();
                        if (result instanceof String && ((String) result).startsWith("error:")) {
                            ViewHub.showShortToast(mContext, ((String) result).replace("error:", ""));
                        } else {
                            workApplyData = (ApplyListModel) result;
                            final NFHSZDialog dialog = NFHSZDialog.newInstance(workApplyData);
                            dialog.mContext = activity;
                            final NFHSZDialog ddd = dialog;
                            dialog.setDialogListener(new NFHSZDialog.FHSZDialogListener() {
                                @Override
                                public void FHSZResult(final int typeid, final int value) {
                                    dialog.dismiss();
//                                    if (typeid == 0 && workApplyData.getApplyInfo().getTypeName().equals("仓库安排")) {
//                                        ViewHub.showLongToast(mContext, "默认仓库安排，无需申请");
//                                        return;
//                                    }
                                    new AsyncTask<Void, Void, Object>() {

                                        @Override
                                        protected void onPreExecute() {
                                            super.onPreExecute();
                                            mLoadingDialog.start("保存中");
                                        }

                                        @Override
                                        protected Object doInBackground(Void... params) {
                                            try {
                                                return QuickSaleApi.setApply(mContext, workWareHouseID, typeid, value);
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                                return "error:" + e.getMessage();
                                            }
                                        }

                                        @Override
                                        protected void onPostExecute(Object result) {
                                            super.onPostExecute(result);
                                            mLoadingDialog.stop();
                                            if (result instanceof String && ((String) result).startsWith("error:")) {
                                                ViewHub.showLongToast(mContext, ((String) result).replace("error:", ""));
                                                return;
                                            }
                                            ViewHub.showLongToast(mContext, result + "");
                                        }

                                    }.execute();
                                }
                            });
                            dialog.show(((Activity) mContext).getFragmentManager(), "FHSZDialog");
                        }
                    }

                }.execute();
            }
        });
        holder.imgUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!item.isShowDetail && (item.data == null || item.data.size() == 0)) {
                    workWareHouseID = item.getWareHouseID();
                    loadDetailData();
                }
                item.isShowDetail = !item.isShowDetail;
                vThis.notifyDataSetChanged();
            }
        });
        if (item.isShowDetail) {
            holder.info.setVisibility(View.VISIBLE);
            holder.imgUp.setImageResource(R.drawable.up_icon);
        } else {
            holder.info.setVisibility(View.GONE);
            holder.imgUp.setImageResource(R.drawable.down_icon);
        }
        adapter.productDatas = item.data;
        holder.info.setAdapter(adapter);

        return convertView;
    }

    private void selectShipTypeName(String name) {
        for (PHQDModel p : mdata) {
            if (p.getWareHouseID() == workWareHouseID) {
                for (PHQDModel.ShipTypeListBean m : p.getShipTypeList()) {
                    m.setIsSelected(false);
                    if (m.getName().equals(name)) {
                        final PHQDModel.ShipTypeListBean tm = m;
                        new AsyncTask<Void, Void, Object>() {

                            @Override
                            protected void onPreExecute() {
                                super.onPreExecute();
                                mLoadingDialog.start("");
                            }

                            @Override
                            protected Object doInBackground(Void... params) {
                                try {
                                    QuickSaleApi.savePHQDShipType(mContext, workWareHouseID, tm.getShipTypeID());
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    return "error:" + e.getMessage();
                                }
                                return "ok";
                            }

                            @Override
                            protected void onPostExecute(Object result) {
                                super.onPostExecute(result);
                                mLoadingDialog.stop();
                                if (result instanceof String && ((String) result).startsWith("error:")) {
                                    ViewHub.showShortToast(mContext, ((String) result).replace("error:", ""));
                                } else {
                                    ViewHub.showLongToast(mContext, "保存成功");
                                }
                            }

                        }.execute();

                        m.setIsSelected(true);
                    }
                }
                notifyDataSetChanged();
                break;
            }
        }
    }

    private static final class ViewHolder {
        TextView txt1, txt2_select, txt2_detail, txt3, txt4, fhsz, btn;
        ImageView imgUp;
        ListView info;
    }

    private void loadDetailData() {
        new AsyncTask<Void, Void, Object>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                mLoadingDialog.start("");
            }

            @Override
            protected Object doInBackground(Void... params) {
                try {
                    data = QuickSaleApi.getPHQDDetail(mContext, workWareHouseID);
                } catch (Exception e) {
                    e.printStackTrace();
                    return "error:" + e.getMessage();
                }
                return "ok";
            }

            @Override
            protected void onPostExecute(Object result) {
                super.onPostExecute(result);
                mLoadingDialog.stop();
                if (result instanceof String && ((String) result).startsWith("error:")) {
                    ViewHub.showShortToast(mContext, ((String) result).replace("error:", ""));
                } else {
                    for (PHQDModel p : mdata) {
                        if (p.getWareHouseID() == workWareHouseID) {
                            p.data = data;
                            notifyDataSetChanged();
                            break;
                        }
                    }
                }
            }

        }.execute();
    }

    static class ViewHolder1 {
        TextView name, detail;
        ImageView icon;
        RelativeLayout item;
    }

    public class PHQDDetailAdapter extends BaseAdapter {

        public List<PHQDDetailModel> productDatas;

        @Override
        public int getCount() {
            return productDatas == null ? 0 : productDatas.size();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder1 holder = null;
            if (convertView == null) {
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.lvitem_bgqd_detail, null);
                holder = new ViewHolder1();
                convertView.setTag(holder);
                holder.icon = (ImageView) convertView.findViewById(R.id.img_order_detail_icon);
                holder.name = (TextView) convertView.findViewById(R.id.txt_order_detail_name);
                holder.detail = (TextView) convertView.findViewById(R.id.txt_order_detail_detail);
                holder.item = (RelativeLayout) convertView.findViewById(R.id.phqd_item_item);
            } else {
                holder = (ViewHolder1) convertView.getTag();
            }
            final PHQDDetailModel pd = getItem(position);

            holder.name.setText(pd.getName());

            String imageUrl = pd.getCover();
            imageUrl = ImageUrlExtends.getImageUrl(imageUrl, Const.LIST_ITEM_SIZE);
            Picasso.with(parent.getContext()).load(imageUrl).placeholder(R.drawable.empty_photo).into(holder.icon);
            holder.item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent it = new Intent(mContext, GetBuyOrderActivity.class);
                    it.putExtra(BaseOrderDetailActivity.EXTRA_ORDER_ID, pd.getOrderID());
                    mContext.startActivity(it);
                }
            });

            StringBuffer info = new StringBuffer();
            for (PHQDDetailModel.ProductsBean p : pd.getProducts()) {
                info.append(p.getColor());
                info.append("/");
                info.append(p.getSize());
                info.append("/");
                info.append(p.getQty());
                info.append("\n");
            }
            holder.detail.setText(info.toString());

            return convertView;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public PHQDDetailModel getItem(int position) {
            return productDatas.get(position);
        }
    }
}
