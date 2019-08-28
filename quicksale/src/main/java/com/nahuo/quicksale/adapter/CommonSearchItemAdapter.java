package com.nahuo.quicksale.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nahuo.library.helper.DisplayUtil;
import com.nahuo.library.helper.ImageUrlExtends;
import com.nahuo.quicksale.app.BWApplication;
import com.nahuo.quicksale.R;
import com.nahuo.quicksale.common.TuanPiUtil;
import com.nahuo.quicksale.oldermodel.SearchItemModel;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class CommonSearchItemAdapter extends BaseRecyclerAdapter<SearchItemModel> implements View.OnClickListener{
    private static final DecimalFormat PRICE_FORMAT = new DecimalFormat("#0.00");
    private static int IMAGE_WIDTH;
    private Context mContext;
    private Listener mListener;

    public interface Listener{
        void onItemClick(SearchItemModel item);
    }
    public CommonSearchItemAdapter() {
        IMAGE_WIDTH = (DisplayUtil.getScreenWidth() - 4 * DisplayUtil.getResDimen(BWApplication.getInstance(), R.dimen.rv_horizontal_spacing)) / 2;
    }
    public void setListener(Listener listener){
        mListener = listener;
    }
    @Override
    public void onBindViewHolder(BaseRecyclerAdapter.ViewHolder holder, int position) {
        View itemView = holder.itemView;

        mContext = itemView.getContext();
        TextView tvPrice = (TextView) itemView.findViewById(R.id.tv_price);
        TextView tvSaleCount = (TextView) itemView.findViewById(R.id.tv_saleCount);
        ImageView ivCover = (ImageView) itemView.findViewById(R.id.iv_cover);
        TextView tvContent = (TextView) itemView.findViewById(R.id.tv_content);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(IMAGE_WIDTH, (int)(IMAGE_WIDTH*1.5));
        ivCover.setLayoutParams(lp);
        TextView tvPinStatus = (TextView) itemView.findViewById(R.id.tv_pin_status);
        ProgressBar progressBar = (ProgressBar) itemView.findViewById(R.id.progress);
        ImageView pinStatusIcon = (ImageView) itemView.findViewById(R.id.tv_pin_status_icon);
        SearchItemModel item = mData.get(position);

        if (item.getID()==-1) {
            tvPrice.setText("");
            tvSaleCount.setText("");
            tvContent.setText("");
            tvPinStatus.setText("");
            ivCover.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.INVISIBLE);
            pinStatusIcon.setVisibility(View.INVISIBLE);
            return;
        }
        ivCover.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        pinStatusIcon.setVisibility(View.VISIBLE);

//        String[] picsUrl = item.getImages();
        String picsUrl = item.getCover();
        if (picsUrl != null && picsUrl.length() > 0) {
            String url = ImageUrlExtends.getImageUrl(picsUrl, 13);
            if(!TextUtils.isEmpty(url)){

                Picasso.with(mContext).load(url).placeholder(R.drawable.empty_photo).into(ivCover);
            }
        }
        int dealCount = item.getDealCount();

        itemView.findViewById(R.id.recycler_view1_txt).setVisibility(View.GONE);
        if (passItemMinIndex == position) {
//            if (passItemMinIndex%2 == 0) {
//                tvPrice.setText("");
//                tvSaleCount.setText("");
//                tvContent.setText("");
//                tvPinStatus.setText("");
//                ivCover.setVisibility(View.GONE);
//                progressBar.setVisibility(View.GONE);
//                pinStatusIcon.setVisibility(View.GONE);
//
//                passItemMinIndex++;
//            } else {
                itemView.findViewById(R.id.recycler_view1_txt).setVisibility(View.VISIBLE);
                ((TextView) itemView.findViewById(R.id.recycler_view1_txt)).setText("往期好货：");
//            }
        }
        if (passItemMinIndex + 1 == position) {
            itemView.findViewById(R.id.recycler_view1_txt).setVisibility(View.VISIBLE);
            ((TextView) itemView.findViewById(R.id.recycler_view1_txt)).setText("");
        }

        if (item.isPassItem) {
            pinStatusIcon.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);
            tvPinStatus.setVisibility(View.GONE);
        } else {
            pinStatusIcon.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.VISIBLE);
            tvPinStatus.setVisibility(View.VISIBLE);

            progressBar.setMax(item.getChengTuanCount());
            progressBar.setProgress(dealCount);
            tvPinStatus.setText(TuanPiUtil.getChengTuanTips(false, item.getChengTuanCount(), dealCount));
        }
        if (item.getDisplayStatuID() == 0) {
            pinStatusIcon.setImageResource(R.drawable.new_icon);
        } else {
            pinStatusIcon.setImageResource(R.drawable.bu_icom);
        }

        tvPrice.setText("¥" + PRICE_FORMAT.format(item.getPrice()));
        if (item.getTotalQty()>0) {
            tvSaleCount.setText("总销量"+item.getTotalQty()+"件");
        }
        else
        {
            tvSaleCount.setText("");
        }
        tvContent.setText(item.getTitle());
        itemView.setOnClickListener(this);
        itemView.setTag(item);
    }

    int passItemMinIndex = 10000;
    public void setPassItemPosition() {
        if (this.getData().size()%2==1) {
            SearchItemModel emptyModel = new SearchItemModel();
            emptyModel.setID(-1);
            List<SearchItemModel> l = new ArrayList<>();
            l.add(emptyModel);
            this.addDataToTail(l);
        }
        passItemMinIndex = this.getData().size();
    }
    public void addPassItem(List<SearchItemModel> data) {
        for (SearchItemModel d : data) {
            d.isPassItem = true;
        }
        if (this.getData().size()==0) {
            this.setData(data);
        } else {
            this.addDataToTail(data);
        }
        this.notifyDataSetChanged();
    }

    @Override
    protected int getItemViewLayoutId() {
        return R.layout.lvitem_pin_huo_detail;
    }


    @Override
    public void onClick(View v) {
        SearchItemModel item = (SearchItemModel) v.getTag();
        mListener.onItemClick(item);
    }

}
