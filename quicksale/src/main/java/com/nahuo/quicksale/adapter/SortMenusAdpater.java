package com.nahuo.quicksale.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.nahuo.bean.SortMenusBean;
import com.nahuo.quicksale.R;

/**
 * Created by jame on 2019/3/22.
 */

public class SortMenusAdpater extends BaseQuickAdapter<SortMenusBean, BaseViewHolder> {
    private Context context;
    int currentMenuID;

    public int getCurrentMenuID() {
        return currentMenuID;
    }

    public void setCurrentMenuID(int currentMenuID) {
        this.currentMenuID = currentMenuID;
    }

    public SortMenusAdpater( Context context) {
        super(R.layout.item_sort_menus_child);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, SortMenusBean item) {
        if (item!=null) {
            ImageView imageView = helper.getView(R.id.image_view);
            TextView textView=helper.getView(R.id.tv_name);
            if (helper.getAdapterPosition() >= getData().size() - 1) {
                helper.setGone(R.id.line, false);
            } else {
                helper.setGone(R.id.line, true);
            }
            int value=item.getValue();
            String name=item.getTitle();
            if (value == 4 || value == 5 || value == 20) {
               imageView.setVisibility(View.VISIBLE);

            }else {
                imageView.setVisibility(View.GONE);
            }
            textView.setText(name);
            if (item.isCheck){
                if (value == 5) {
                    imageView.setImageResource(R.drawable.toup);
                } else if (value == 4) {
                    imageView.setImageResource(R.drawable.todown);
                } else if (value == 20) {
                    imageView.setImageResource(R.drawable.screen_choose_click);
                }
                textView.setTextColor(context.getResources().getColor(R.color.bottom_item_txt_press));

            }else {
                if (value == 5 || value == 4) {
                    imageView.setImageResource(R.drawable.todefaut);
                } else if (value == 20) {
                    imageView.setImageResource(R.drawable.screen_choose);
                }
                textView.setTextColor(context.getResources().getColor(R.color.bottom_item_txt_normal));
            }
//            if (value == currentMenuID) {
//                if (value == 5) {
//                    imageView.setImageResource(R.drawable.toup);
//                } else if (value == 4) {
//                    imageView.setImageResource(R.drawable.todown);
//                } else if (value == 20) {
//                    imageView.setImageResource(R.drawable.screen_choose_click);
//                }
//                textView.setTextColor(context.getResources().getColor(R.color.bottom_item_txt_press));
//            } else {
//                if (value == 5 || value == 4) {
//                    imageView.setImageResource(R.drawable.todefaut);
//                } else if (value == 20) {
//                    imageView.setImageResource(R.drawable.screen_choose);
//                }
//                textView.setTextColor(context.getResources().getColor(R.color.bottom_item_txt_normal));
//            }
        }
    }
    public interface  SortMenusLister{
        void OnClickSortMenus (SortMenusBean item);
    }
}
