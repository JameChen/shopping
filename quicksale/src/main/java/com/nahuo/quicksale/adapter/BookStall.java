//package com.nahuo.quicksale.adapter;
//
//import android.support.annotation.NonNull;
//import android.support.v7.widget.GridLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.TextView;
//
//import com.nahuo.quicksale.R;
//
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * A RecyclerView.Adapter that allows for headers and footers as well.
// * <p/>
// * This class wraps a base adapter that's passed into the constructor. It works by creating extra view items types
// * that are returned in {@link #getItemViewType(int)}, and mapping these to the header and footer views provided via
// * {@link #addHeader(View)} and {@link #addFooter(View)}.
// * <p/>
// * There are two restrictions when using this class:
// * <p/>
// * 1) The base adapter can't use negative view types, since this class uses negative view types to keep track
// * of header and footer views.
// * <p/>
// * 2) You can't add more than 1000 headers or footers.
// * <p/>
// * Created by mlapadula on 12/15/14.
// */
//public class BookStall<T extends RecyclerView.Adapter> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
//
//    private final T mBase;
//
//    /**
//     * Defines available view type integers for headers and footers.
//     * <p/>
//     * How this works:
//     * - Regular views use view types starting from 0, counting upwards
//     * - Header views use view types starting from -1000, counting upwards
//     * - Footer views use view types starting from -2000, counting upwards
//     * <p/>
//     * This means that you're safe as long as the base adapter doesn't use negative view types,
//     * and as long as you have fewer than 1000 headers and footers
//     */
//    private static final int HEADER_VIEW_TYPE = -1000;
//    private static final int FOOTER_VIEW_TYPE = -2000;
//    public static final int TYPE_TYPE_PASS = 0xff07;
//    public static final int TYPE_TYPE_NEW = 0xff08;
//    private final List<View> mHeaders = new ArrayList<View>();
//    private final List<View> mFooters = new ArrayList<View>();
//    private boolean mShowFooter;
//    static public  int passPosindex=0;
//    static  public  String part2title="往期好货";
//    /**
//     * Constructor.
//     *
//     * @param base the adapter to wrap
//     */
//    public BookStall(T base) {
//        super();
//        mBase = base;
//    }
//
//    public BookStall(T base, final GridLayoutManager gridLayoutManager) {
//        this(base);
//        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
//            @Override
//            public int getSpanSize(int position) {
//                //  Log.e("yu","getSpanSize position="+position+"===");
////                switch (getItemViewType(position)){
////                    case  TYPE_TYPE_PASS
////                        : return  1;
////
////                }
//                if (position < getHeaderCount() || position >= getItemCount() - getVisibleFooterSize()) {
//                    return gridLayoutManager.getSpanCount();
//                } else {
//                    if (getItemViewType(position) == TYPE_TYPE_PASS) {
//                        return gridLayoutManager.getSpanCount();
//                    } else {
//                        return 1;
//                    }
//                }
//                //return (position < getHeaderCount() || position >= getItemCount() - getVisibleFooterSize()) ? gridLayoutManager.getSpanCount() : 1;
//            }
//        });
//    }
//
//    /**
//     * Gets the base adapter that this is wrapping.
//     */
//    public T getWrappedAdapter() {
//        return mBase;
//    }
//
//    /**
//     * Adds a header view.
//     */
//    public void addHeader(@NonNull View view) {
//        if (view == null) {
//            throw new IllegalArgumentException("You can't have a null header!");
//        }
//        mHeaders.add(view);
//    }
//
//    /**
//     * Adds a footer view.
//     */
//    public void addFooter(@NonNull View view) {
//        if (view == null) {
//            throw new IllegalArgumentException("You can't have a null footer!");
//        }
//        mFooters.add(view);
//    }
//
//    public void clearFooter() {
//        mFooters.clear();
//    }
//
//    /**
//     * Toggles the visibility of the header views.
//     */
//    public void setHeaderVisibility(boolean shouldShow) {
//        for (View header : mHeaders) {
//            header.setVisibility(shouldShow ? View.VISIBLE : View.GONE);
//        }
//    }
//
//    /**
//     * Toggles the visibility of the footer views.
//     */
//    public void setFooterVisibility(boolean shouldShow) {
//        for (View footer : mFooters) {
//            footer.setVisibility(shouldShow ? View.VISIBLE : View.GONE);
//        }
//    }
//
//    /**
//     * @return the number of headers.
//     */
//    public int getHeaderCount() {
//        return mHeaders.size();
//    }
//
//    /**
//     * @return the number of footers.
//     */
//    public int getFooterCount() {
//        return mFooters.size();
//    }
//
//    /**
//     * Gets the indicated header, or null if it doesn't exist.
//     */
//    public View getHeader(int i) {
//        return i < mHeaders.size() ? mHeaders.get(i) : null;
//    }
//
//    /**
//     * Gets the indicated footer, or null if it doesn't exist.
//     */
//    public View getFooter(int i) {
//        return i < mFooters.size() ? mFooters.get(i) : null;
//    }
//
//    private boolean isHeader(int viewType) {
//        return viewType >= HEADER_VIEW_TYPE && viewType < (HEADER_VIEW_TYPE + mHeaders.size());
//    }
//
//    private boolean isFooter(int viewType) {
//        return viewType >= FOOTER_VIEW_TYPE && viewType < (FOOTER_VIEW_TYPE + getVisibleFooterSize());
//    }
//
//    @Override
//    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
//       // Log.e("yu", "viewType=" + viewType + "===");
//
//        if (isHeader(viewType)) {
//            int whichHeader = Math.abs(viewType - HEADER_VIEW_TYPE);
//            View headerView = mHeaders.get(whichHeader);
//            return new RecyclerView.ViewHolder(headerView) {
//            };
//        } else if (isFooter(viewType)) {
//            int whichFooter = Math.abs(viewType - FOOTER_VIEW_TYPE);
//            View footerView = mFooters.get(whichFooter);
//            return new RecyclerView.ViewHolder(footerView) {
//            };
//
//        } else if (viewType == TYPE_TYPE_PASS) {
//            //Log.e("yu", "==xxxxxxxxxxxxxxxxxxxxxxxxxxx");
//            return new HolderType(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_pass_pinhuo_tx, viewGroup, false));
//        } else {
//            return mBase.onCreateViewHolder(viewGroup, viewType);
//        }
//    }
//
//    public class HolderType extends RecyclerView.ViewHolder {
//        public TextView item_img_type2;
//
//        public HolderType(View itemView) {
//            super(itemView);
//            item_img_type2 = (TextView) itemView.findViewById(R.id.item_pass_txt);
//        }
//    }
//
//    @Override
//    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
//       // Log.e("yu", "pos===" + position + "mHeaders.size()" + mHeaders.size() + "mBase.getItemCount()" + mBase.getItemCount());
//        if (viewHolder instanceof BookStall.HolderType) {
//           // Log.e("yu", "==aaaaaaaaa");
//            ((HolderType) viewHolder).item_img_type2.setText(part2title);
//        }
//        if (position < mHeaders.size()) {
//            // Headers don't need anything special
//
//        } else if (!(viewHolder instanceof BookStall.HolderType) && (position < mHeaders.size() + mBase.getItemCount())) {
//            // This is a real position, not a header or footer. Bind it.
//            mBase.onBindViewHolder(viewHolder, position - mHeaders.size());
//
//        } else {
//            // Footers don't need anything special
//            viewHolder.itemView.setVisibility(mBase.getItemCount() == 0 ? View.GONE : View.VISIBLE);
//        }
//    }
//
//    @Override
//    public int getItemCount() {
//        return mHeaders.size() + mBase.getItemCount() + getVisibleFooterSize() ;
//    }
//
//
//    private int getVisibleFooterSize() {
//        return mShowFooter ? mFooters.size() : 0;
//    }
//
//    public void setShowFooter(boolean show) {
//        mShowFooter = show;
//        notifyDataSetChanged();
//    }
//
//    @Override
//    public int getItemViewType(int position) {
//        if (position == passPosindex+1) {
//            return TYPE_TYPE_PASS;
//        }
//        if (position < mHeaders.size()) {
//            return HEADER_VIEW_TYPE + position;
//
//        } else if (position < (mHeaders.size() + mBase.getItemCount())) {
//            return mBase.getItemViewType(position - mHeaders.size());
//
//        } else {
//            return FOOTER_VIEW_TYPE + position - mHeaders.size() - mBase.getItemCount();
//        }
//
//    }
//}
