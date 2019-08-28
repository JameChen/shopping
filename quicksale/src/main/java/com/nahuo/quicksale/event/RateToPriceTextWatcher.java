package com.nahuo.quicksale.event;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.EditText;

import java.text.DecimalFormat;

/**
 * @description 加价率改变价格的级联TextWatcher
 * @created 2014-9-29 上午9:55:30
 * @author ZZB
 */
public class RateToPriceTextWatcher implements TextWatcher {

    private EditText mEtPrice;
    private String mBasePrice;
    
    private DecimalFormat mDecimalFormat = new DecimalFormat("#0.00");

    public RateToPriceTextWatcher(String basePrice, EditText etPrice) {
        mEtPrice = etPrice;
        mBasePrice = basePrice;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable s) {
        String rateStr = s.toString();
        
        int posDot = rateStr.indexOf(".");
        
        if (posDot >= 0 && rateStr.length() - posDot - 1 > 2) {
            s.delete(posDot + 3, posDot + 4);
        }
        String tag = (String) mEtPrice.getTag();
        if(tag != null){
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
            
        if(TextUtils.isEmpty(rateStr) || TextUtils.isEmpty(mBasePrice)){
            mEtPrice.setText(TextUtils.isEmpty(mBasePrice) ? "" : mBasePrice);
            return;
        }
            
        Double rate = 1.0;
        try {
            
            rate = Double.valueOf(rateStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        double basePrice = Double.valueOf(mBasePrice);
        double addPrice = rate * basePrice * 0.01;
        mEtPrice.setText(mDecimalFormat.format(addPrice + basePrice));

    }

}
