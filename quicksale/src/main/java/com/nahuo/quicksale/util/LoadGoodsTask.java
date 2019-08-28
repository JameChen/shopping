package com.nahuo.quicksale.util;

import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.TextView;

import com.nahuo.library.controls.CircleTextView;
import com.nahuo.quicksale.api.GoodsCountApi;

/**
 * Created by Administrator on 2017/3/31 0031.
 */
public class LoadGoodsTask extends AsyncTask<Void, Void, String> {
    private Context mContext;
    private CircleTextView carCountTv;
    private TextView red;

    public LoadGoodsTask(Context mContext, CircleTextView carCountTv) {
        this.mContext = mContext;
        this.carCountTv = carCountTv;
    }

    public LoadGoodsTask(Context mContext, TextView red) {
        this.mContext = mContext;
        this.red = red;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(Void... params) {
        String result = "";

        try {
            result = GoodsCountApi.getInstance().getCarGoodsCount(mContext);
        } catch (Exception e) {
            result = "数量获取失败";
            e.printStackTrace();
        }

        return result;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        if (!result.startsWith("数量获取失败")) {
            int count = Integer.parseInt(result);
            if (count > 0) {
                if (carCountTv != null) {
                    carCountTv.setText(count + "");
                    carCountTv.setVisibility(View.VISIBLE);
                }
                if (red != null) {
                    red.setText(count + "");
                    red.setVisibility(View.VISIBLE);
                }
            } else {
                if (carCountTv != null) {
                    carCountTv.setVisibility(View.GONE);
                }
                if (red != null) {
                    red.setVisibility(View.GONE);
                }
            }
        }
    }
}
