package com.nahuo.quicksale.adapter;

import android.app.Activity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.nahuo.bean.Price;
import com.nahuo.bean.SearchPanelBean;
import com.nahuo.constant.IDsConstant;
import com.nahuo.quicksale.R;
import com.nahuo.quicksale.base.ViewHolder;
import com.nahuo.quicksale.common.ListUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jame on 2017/12/25.
 */

public class SortFilterAdpater extends RecyclerView.Adapter<ViewHolder> {
    public enum SortType {
        TYPEID_FEI_LEI, TYPEID_DA_SHA, TYPEID_PRICE, TYPEID_HUO_DAY
    }

    private long MinPrice = -1;
    private long MaxPrice = -1;
    List<SearchPanelBean.PanelsBeanX> Panels = new ArrayList<>();
    Activity context;
    FliterSortItemAdpter itemAdpter;

    public SearchPanelBean getSearchPanelBean() {
        return searchPanelBean;
    }

    SearchPanelBean searchPanelBean;
    RecyclerView drawRecyclerView;

    public SortFilterAdpater(Activity context) {
        this.context = context;
        //itemAdpter = new FliterSortItemAdpter(context);
    }

    public void setPanels(SearchPanelBean searchPanelBean) {
        this.searchPanelBean = searchPanelBean;
        if (this.searchPanelBean != null) {
            if (!ListUtils.isEmpty(this.searchPanelBean.getPanels()))
                Panels = searchPanelBean.getPanels();
        }

    }


    //清空筛选
    public void setClear() {
        if (searchPanelBean != null) {
            searchPanelBean.setMaxPrice(-1);
            searchPanelBean.setMinPrice(-1);
            if (!ListUtils.isEmpty(Panels)) {
                for (SearchPanelBean.PanelsBeanX panelsBeanX : Panels) {
                    if (!ListUtils.isEmpty(panelsBeanX.getPanels())) {
                        for (SearchPanelBean.PanelsBeanX.PanelsBean bean : panelsBeanX.getPanels()) {
                            bean.isSelect = false;
                        }
                    }
                }
            }
            notifyDataSetChanged();
        }

    }
  int index,index2;
    @Override
    public int getItemViewType(int position) {
        switch (Panels.get(position).getTypeID()) {
            case IDsConstant.TYPEID_FEI_LEI:
                return SortType.TYPEID_FEI_LEI.ordinal();
            case IDsConstant.TYPEID_DA_SHA:
                return SortType.TYPEID_DA_SHA.ordinal();
            case IDsConstant.TYPEID_PRICE:
                return SortType.TYPEID_PRICE.ordinal();
            case IDsConstant.TYPEID_HUO_DAY:
                return SortType.TYPEID_HUO_DAY.ordinal();
        }
        return 0;
    }
    boolean is_low_touh=false;
    boolean is_hight_touh=false;
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder holder = ViewHolder.createViewHolder(context, parent, R.layout.item_sort_filter);
        return holder;
    }

    public void setDrawRecyclerView(RecyclerView drawRecyclerView) {
        this.drawRecyclerView = drawRecyclerView;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        View itemView = holder.getConvertView();
        TextView tv_title = (TextView) itemView.findViewById(R.id.tv_title);
        ImageView iv_top_icon = (ImageView) itemView.findViewById(R.id.iv_top_icon);
       final EditText et_low_price = (EditText) itemView.findViewById(R.id.et_low_price);
       final EditText et_high_price = (EditText) itemView.findViewById(R.id.et_high_price);
        View layout_price = itemView.findViewById(R.id.layout_price);
        View layout_head = itemView.findViewById(R.id.layout_head);
        RecyclerView recycler_item = (RecyclerView) itemView.findViewById(R.id.recycler_item);
        GridLayoutManager manager = new GridLayoutManager(context, 3);
        recycler_item.setLayoutManager(manager);
        if (searchPanelBean != null) {
            MaxPrice = searchPanelBean.getMaxPrice();
            MinPrice = searchPanelBean.getMinPrice();
        }
        final SearchPanelBean.PanelsBeanX mPanelBean = Panels.get(position);
        if (mPanelBean.is_expand) {
            iv_top_icon.setImageResource(R.drawable.top_up);
        } else {
            iv_top_icon.setImageResource(R.drawable.top_down);
        }
        final List<SearchPanelBean.PanelsBeanX.PanelsBean> panelsBeanList = mPanelBean.getPanels();
        tv_title.setText(mPanelBean.getName() + "");
        if (MaxPrice > -1) {
            et_high_price.setText(MaxPrice + "");
        } else {
            et_high_price.setText("");
        }
        if (MinPrice > -1) {
            et_low_price.setText(MinPrice + "");
        } else {
            et_low_price.setText("");
        }
        if (getItemViewType(position) == SortType.TYPEID_FEI_LEI.ordinal() || getItemViewType(position) == SortType.TYPEID_DA_SHA.ordinal()) {
            // iv_top_icon.setVisibility(View.VISIBLE);
            if (!ListUtils.isEmpty(panelsBeanList)) {
                layout_head.setVisibility(View.VISIBLE);
            } else {
                layout_head.setVisibility(View.GONE);
            }
            layout_price.setVisibility(View.GONE);
            itemAdpter = new FliterSortItemAdpter(context);
            itemAdpter.setType_choose(FliterSortItemAdpter.MULT_CHOOSE);

        } else if (getItemViewType(position) == SortType.TYPEID_PRICE.ordinal()) {
            //价格
            // iv_top_icon.setVisibility(View.GONE);
            layout_head.setVisibility(View.VISIBLE);
            layout_price.setVisibility(View.VISIBLE);

            if (!ListUtils.isEmpty(panelsBeanList)) {
                for (SearchPanelBean.PanelsBeanX.PanelsBean bean : panelsBeanList) {
                    Price price = getPrice(bean.getName());
                    if (price.getMaxPrice() == MaxPrice && price.getMinPrice() == MinPrice) {
                        bean.isSelect = true;
                    } else {
                        bean.isSelect = false;
                    }
                }
            }
            itemAdpter = new FliterSortItemAdpter(context);
            itemAdpter.setType_choose(FliterSortItemAdpter.SINGLE_CHOOSE);
            itemAdpter.setListener(new FliterSortItemAdpter.Listener() {
                @Override
                public void onItemClick(SearchPanelBean.PanelsBeanX.PanelsBean item) {
                    if (mPanelBean.getTypeID() == IDsConstant.TYPEID_PRICE) {
                        if (item.isSelect) {
                            String price = item.getName();
                            Price mPrice = getPrice(price);
                            searchPanelBean.setMaxPrice(mPrice.getMaxPrice());
                            searchPanelBean.setMinPrice(mPrice.getMinPrice());

                        } else {
                            searchPanelBean.setMaxPrice(-1);
                            searchPanelBean.setMinPrice(-1);
                        }
                        notifyDataSetChanged();

                    }
                }
            });
            et_low_price.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                    String text = s.toString();
                    if (TextUtils.isEmpty(text)) {
                        if (searchPanelBean != null)
                            searchPanelBean.setMinPrice(-1);
                    } else {
                        et_low_price.setSelection(text.length());
                        searchPanelBean.setMinPrice(Long.parseLong(text));
                    }
                    if (drawRecyclerView.getScrollState() == RecyclerView.SCROLL_STATE_IDLE && !drawRecyclerView.isComputingLayout()) {
                        notifyDataSetChanged();
                    }

                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

            et_low_price.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    is_low_touh=true;
                    is_hight_touh=false;
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        index = position;
                    }
                    return false;
                }
            });
            et_high_price.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    is_low_touh=false;
                    is_hight_touh=true;
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        index2 = position;
                    }
                    return false;
                }
            });
            et_high_price.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                    String text = s.toString();
                    if (TextUtils.isEmpty(text)) {
                        if (searchPanelBean != null)
                            searchPanelBean.setMaxPrice(-1);
                    } else {
                        et_high_price.setSelection(text.length());
                        searchPanelBean.setMaxPrice(Long.parseLong(text));
                    }
                    if (drawRecyclerView.getScrollState() == RecyclerView.SCROLL_STATE_IDLE && !drawRecyclerView.isComputingLayout()) {
                        notifyDataSetChanged();
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
            if (index != -1 && index == position&&is_low_touh) {
                // 如果当前的行下标和点击事件中保存的index一致，手动为EditText设置焦点。
                et_low_price.requestFocus();
                et_low_price.setFocusable(true);
                et_low_price.setFocusableInTouchMode(true);
            }
            if (index2 != -1 && index2 == position&&is_hight_touh) {
                // 如果当前的行下标和点击事件中保存的index一致，手动为EditText设置焦点。
                et_high_price.requestFocus();
                et_high_price.setFocusable(true);
                et_high_price.setFocusableInTouchMode(true);
            }
        } else if (getItemViewType(position) == SortType.TYPEID_HUO_DAY.ordinal()) {
            // iv_top_icon.setVisibility(View.GONE);
            if (!ListUtils.isEmpty(panelsBeanList)) {
                layout_head.setVisibility(View.VISIBLE);
            } else {
                layout_head.setVisibility(View.GONE);
            }
            layout_price.setVisibility(View.GONE);
            itemAdpter = new FliterSortItemAdpter(context);
            itemAdpter.setType_choose(FliterSortItemAdpter.SINGLE_CHOOSE);
        }
        recycler_item.setAdapter(itemAdpter);
        if (!ListUtils.isEmpty(panelsBeanList)) {
            if (panelsBeanList.size() > 6) {
                iv_top_icon.setVisibility(View.VISIBLE);
                if (mPanelBean.is_expand) {
                    itemAdpter.setData(panelsBeanList);
                } else {
                    itemAdpter.setData(panelsBeanList.subList(0, 6));
                }
            } else {
                itemAdpter.setData(panelsBeanList);
                iv_top_icon.setVisibility(View.GONE);
            }

        } else {
            iv_top_icon.setVisibility(View.GONE);
        }

        iv_top_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SearchPanelBean.PanelsBeanX mPanelBean = Panels.get(position);
                mPanelBean.is_expand = !mPanelBean.is_expand;
                itemAdpter.notifyDataSetChanged();
                notifyDataSetChanged();
            }
        });


    }

    public Price getPrice(String strPrice) {
        Price price = new Price();
        price.setMaxPrice(0);
        price.setMinPrice(0);
        try {
            if (!TextUtils.isEmpty(strPrice)) {
                if (strPrice.contains("-")) {
                    String[] ss = strPrice.split("-");
                    price.setMinPrice(Long.parseLong(ss[0]));
                    price.setMaxPrice(Long.parseLong(ss[1]));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return price;
        }
        return price;
    }

    @Override
    public int getItemCount() {
        return Panels.size();
    }
}
