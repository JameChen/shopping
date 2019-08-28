package com.nahuo.quicksale.event;

import java.text.DecimalFormat;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;

/**
 * @description 价格改变加价率的级联TextWatcher
 * @created 2014-9-29 上午9:55:55
 * @author ZZB
 */
public class PriceToRateTextWatcher implements TextWatcher{
    
    private EditText mEtRate, mEtPrice;
    private String mBasePrice;
    private DecimalFormat mDecimalFormat = new DecimalFormat("#0.00");
    public PriceToRateTextWatcher(String basePrice, EditText etPrice, EditText etRate){
        mEtRate = etRate;
        mBasePrice = basePrice;
        mEtPrice = etPrice;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
       
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        
    }

    @Override
    public void afterTextChanged(Editable s) {
        String priceStr = s.toString();
        
        int posDot = priceStr.indexOf(".");
        
        if (posDot >= 0 && priceStr.length() - posDot - 1 > 2) {
            s.delete(posDot + 3, posDot + 4);
        }
        
        
        String tag = (String) mEtPrice.getTag();
        if(tag != null){//tag用来避免级联死循环
            if(tag.equals("end")){
                mEtPrice.setTag("start");
                return;
            }else if(tag.equals("start")){
                mEtPrice.setTag("end");
            }else{
                return;
            } 
        }else{
            mEtPrice.setTag("end");
        }
        if(TextUtils.isEmpty(priceStr) || TextUtils.isEmpty(mBasePrice)){
            mEtRate.setText("");
            return;
        }
        double price = 0.00;
        double basePrice = 0.00;
        try {
             price = Double.valueOf(priceStr);
             basePrice = Double.valueOf(mBasePrice);
            
		} catch (Exception e) {
			Log.e("PriceToRateTextWatcher", e.getMessage());
		}
        if(price > basePrice){
            double addRate = (price/basePrice-1) * 100;
            mEtRate.setText(mDecimalFormat.format(addRate) + "");
        }else{
            mEtRate.setText("");
        }
    }

}
