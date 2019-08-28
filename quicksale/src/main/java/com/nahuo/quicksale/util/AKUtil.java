package com.nahuo.quicksale.util;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.nahuo.library.controls.CircleTextView;
import com.nahuo.library.utils.TimeUtils;
import com.nahuo.quicksale.R;
import com.nahuo.quicksale.common.SpManager;
import com.nahuo.quicksale.oldermodel.PublicData;

import org.apache.http.Header;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2017/3/30 0030.
 */
public class AKUtil {

    /**
     * 判断对象是否为空<br>
     * 1,字符串(null或者"")都返回true<br>
     * 2,数字类型(null或者0)都返回true<br>
     * 3,集合类型(null或者不包含元素都返回true)<br>
     * 4,数组类型不包含元素返回true(包含null元素返回false)<br>
     * 5,其他对象仅null返回true
     *
     * @param obj
     * @return
     */
    public static boolean isEmpty(Object obj) {
        if (obj == null) { return true; }
        if (obj instanceof Number) {
            Number num = (Number) obj;
            if (num.intValue() == 0) {
                return true;
            } else {
                return false;
            }
        } else if (obj instanceof String) {
            String str = (String) obj;
            if ((str == null) || str.equals("")) {
                return true;
            } else {
                return false;
            }
        } else if (obj instanceof Collection<?>) {
            Collection<?> c = (Collection<?>) obj;
            return c.isEmpty();
        } else if (obj instanceof Map<?, ?>) {
            Map<?, ?> m = (Map<?, ?>) obj;
            return m.isEmpty();
        } else if (obj.getClass().isArray()) {
            int length = Array.getLength(obj);
            return length == 0 ? true : false;
        } else {
            return false;
        }
    }
    public static SpannableStringBuilder handler(final Context context,final TextView gifTextView, String content) {
        SpannableStringBuilder sb = new SpannableStringBuilder(content);
        String regex = "(\\#\\[face/png/f_static_)\\d{3}(.png\\]\\#)";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(content);
        while (m.find()) {
            String tempText = m.group();
            try {
                String num = tempText.substring("#[face/png/f_static_".length(), tempText.length()- ".png]#".length());
                String gif = "face/gif/f" + num + ".gif";
                /**
                 * 如果open这里不抛异常说明存在gif，则显示对应的gif
                 * 否则说明gif找不到，则显示png
                 * */
                InputStream is = context.getAssets().open(gif);
                sb.setSpan(new AnimatedImageSpan(new AnimatedGifDrawable(is,new AnimatedGifDrawable.UpdateListener() {
                            @Override
                            public void update() {
                                gifTextView.postInvalidate();
                            }
                        })), m.start(), m.end(),
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                is.close();
            } catch (Exception e) {
                String png = tempText.substring("#[".length(),tempText.length() - "]#".length());
                try {
                    sb.setSpan(new ImageSpan(context, BitmapFactory.decodeStream(context.getAssets().open(png))), m.start(), m.end(),Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                } catch (IOException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
                e.printStackTrace();
            }
        }
        return sb;
    }

    public static void setTypeResource(String typeString,String tradeName,ImageView image){
        int type=-1;
        if(typeString.contains("充值")){
            type=0;
        }else if(typeString.contains("交易")){
            type=1;
        }else if(typeString.contains("提现")){
            type=2;
        }else if(typeString.contains("退款")){
            type=3;
        }
        switch (type){
            case 0:
                image.setBackgroundResource(R.drawable.ic_recharge);
                break;
            case 1:
                image.setBackgroundResource(R.drawable.ic_transaction);
                break;
            case 2:
                image.setBackgroundResource(R.drawable.ic_withdraw);
                break;
            case 3:
                image.setBackgroundResource(R.drawable.ic_refund);
                break;
            case -1:
                if(tradeName.contains("Out")){
                    image.setBackgroundResource(R.drawable.ic_withdraw);
                }else{
                    image.setBackgroundResource(R.drawable.ic_refund);
                }
                break;
        }
    }

    public static void changeTextColor(Context mContext,TextView tv,String code){
        if(!code.contains("-")){
            if(Double.parseDouble(getStringToDouble(code))==0){
                tv.setTextColor(mContext.getResources().getColor(R.color.black));
            }else {
                tv.setTextColor(mContext.getResources().getColor(R.color.freight_green));
            }
        }else{
            tv.setTextColor(mContext.getResources().getColor(R.color.freight_red));
        }
    }

    public static int returnchangeTextColor(String code){

        try{
            if(!code.contains("-")){
                if(Double.parseDouble(getStringToDouble(code))==0){
                    return R.color.black;
                }else {
                    return R.color.freight_green;
                }
            }else{
                return R.color.freight_red;
            }
        }catch (Exception ex){
            ex.printStackTrace();
            return R.color.black;
        }
    }
    //正则表达式取出字符串中的double
    private static String getStringToDouble(String s){
        String returnStr="";
        Pattern p=Pattern.compile("(\\d+\\.\\d+)");
        Matcher m=p.matcher(s);
        if(m.find()){
            returnStr=m.group();
        }
        return returnStr;
    }

    /**
     * 转换px为dip
     */
    public static int convertPX2DIP(Context context, int px) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (px / scale + 0.5f * (px >= 0 ? 1 : -1));
    }

    /**
     * 转换dip为px
     */
    public static int convertDIP2PX(Context context, int dip) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dip * scale + 0.5f * (dip >= 0 ? 1 : -1));
    }

    //判断circleTextView是否显示
    public static void isShowCircleTextView(boolean isShow, CircleTextView ctv){
        if(!isShow){
            ctv.setVisibility(View.VISIBLE);
            ctv.setText("失效");
        }else{
            ctv.setVisibility(View.GONE);
        }
    }

    //日期转换格式
    public static String format(String oldStr){
        Date date=TimeUtils.timeStampToDate(oldStr,"yyyy-MM-dd HH:mm:ss");
        return TimeUtils.dateToTimeStamp(date,"MM-dd");
    }
    //日期转换格式
    public static String xformat(String oldStr){
        Date date=TimeUtils.timeStampToDate(oldStr,"yyyy-MM-dd HH:mm:ss");
        return TimeUtils.dateToTimeStamp(date,"yyyy.MM.dd");
    }
    //
    // 判断一个字符串是否含有数字（分现金券和其它券）

    public static boolean hasDigit(String content) {

        boolean flag = false;

        Pattern p = Pattern.compile(".*\\d+.*");

        Matcher m = p.matcher(content);

        if (m.matches()&&(content.contains("¥")||content.contains("￥")))

            flag = true;

        return flag;

    }

    //（判断）设置cookie
    public static void setCookies(Context contexts,Header[] headers,String method,String targetMethod) {
        if (method.equals(targetMethod)) {
            // 获取cookie值
            if (headers != null && headers.length > 0) {
                for (Header header : headers) {
                    String nhStrPrefix = "NaHuo.UserLogin";
                    if (header.toString().contains(nhStrPrefix)) {
                        // 保存cookie
                        String cookieFulllStr = header.toString();
                        // 保存cookie
                        String cookie = cookieFulllStr.substring(cookieFulllStr.indexOf(nhStrPrefix),
                                cookieFulllStr.length());
                        PublicData.setCookieOnlyAtInit(cookie);
                        SpManager.setCookie(contexts, cookie);
                        break;
                    }
                }
            }
        }
    }

        //返回html（SpannableString）
        public static SpannableString getTextToSpan(String text) {
            if(text==null){
                text="";
            }
        Spanned contentStr = Html.fromHtml(text);
        SpannableString msp = new SpannableString(contentStr);
        return msp;
        }

    // 两次点击按钮之间的点击间隔不能少于1000毫秒
    private static final int MIN_CLICK_DELAY_TIME = 1000;
    private static long lastClickTime;

    public static boolean isFastClick() {
        boolean flag = false;
        long curClickTime = System.currentTimeMillis();
        if ((curClickTime - lastClickTime) >= MIN_CLICK_DELAY_TIME) {
            flag = true;
        }
        lastClickTime = curClickTime;
        return flag;
    }
    public static void  setListViewHeightBasedOnChildren(ListView listView) {

        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

}
