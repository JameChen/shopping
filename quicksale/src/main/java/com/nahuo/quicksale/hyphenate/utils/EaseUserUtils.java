package com.nahuo.quicksale.hyphenate.utils;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.hyphenate.easeui.R;
import com.nahuo.library.helper.ImageUrlExtends;
import com.nahuo.quicksale.app.BWApplication;
import com.nahuo.quicksale.common.Const;
import com.nahuo.quicksale.common.SpManager;
import com.nahuo.quicksale.hyphenate.EaseUI;
import com.nahuo.quicksale.hyphenate.EaseUI.EaseUserProfileProvider;
import com.nahuo.quicksale.hyphenate.domain.EaseUser;
import com.nahuo.quicksale.util.ChatUtl;

import static com.nahuo.quicksale.common.SpManager.getUserId;

public class EaseUserUtils {
    
    static EaseUserProfileProvider userProvider;
    
    static {
        userProvider = EaseUI.getInstance().getUserProfileProvider();
    }
    
    /**
     * get EaseUser according username
     * @param username
     * @return
     */
    public static EaseUser getUserInfo(String username){
        if(userProvider != null)
            return userProvider.getUser(username);
        
        return null;
    }
    
    /**
     * set user avatar
     * @param username
     */
    public static void setUserAvatar(Context context, String username, ImageView imageView){
    	EaseUser user = getUserInfo(username);
        if(user != null && user.getAvatar() != null){
            try {
                int avatarResId = Integer.parseInt(user.getAvatar());
                Glide.with(context).load(avatarResId).into(imageView);
            } catch (Exception e) {
                //use default avatar
                RequestOptions options=new RequestOptions();
                options.diskCacheStrategy(DiskCacheStrategy.ALL)
                        .placeholder(R.drawable.ease_default_avatar);
                Glide.with(context).load(user.getAvatar()).apply(options).into(imageView);
            }
        }else{
            Glide.with(context).load(R.drawable.ease_default_avatar).into(imageView);
        }
    }

    /**
     * set user avatar
     * @param username
     */
    public static void setPinHuoUserAvatar(Context context, String username, ImageView imageView,int type){
        String d_imgcover = Const.getShopLogo(username);
        String imageurl = "";
        RequestOptions options=new RequestOptions();
        options.diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.ease_default_avatar);
        if (type==ChatUtl.TYPE_TO) {
            if (username.equals(ChatUtl.SYSTEM_HX_ID + "")) {
                imageurl = ImageUrlExtends.getImageUrl(d_imgcover, Const.LIST_COVER_SIZE);
            } else {
                imageurl = SpManager.getECC_HEADIMAGE(context);
            }
            Glide.with(context).load(imageurl).apply(options).into( imageView);
        }else  if (type==ChatUtl.TYPE_ME){
            if (username.equals(ChatUtl.SYSTEM_HX_ID + "")) {
                imageView.setVisibility(View.GONE);
            } else {
                String logo = SpManager.getShopLogo(context).trim();
                if (TextUtils.isEmpty(logo)) {
                    imageurl = Const.getShopLogo(SpManager.getUserId(BWApplication.getInstance()));
                } else {
                    imageurl = ImageUrlExtends.getImageUrl(logo, 8);
                }
                Glide.with(context).load(imageurl).apply(options).into( imageView);
            }
        }

        //EaseUser user = getUserInfo(username);
//        if(user != null && user.getAvatar() != null){
//            try {
//                int avatarResId = Integer.parseInt(user.getAvatar());
//                Glide.with(context).load(avatarResId).into(imageView);
//            } catch (Exception e) {
//                //use default avatar
//                RequestOptions options=new RequestOptions();
//                options.diskCacheStrategy(DiskCacheStrategy.ALL)
//                        .placeholder(R.drawable.ease_default_avatar);
//                Glide.with(context).load(user.getAvatar()).apply(options).into(imageView);
//            }
//        }else{
//            Glide.with(context).load(R.drawable.ease_default_avatar).into(imageView);
//        }
    }
    /**
     * set user's nickname
     */
    public static void setUserNick(String username,TextView textView){
        if(textView != null){
        	EaseUser user = getUserInfo(username);
        	if(user != null && user.getNick() != null){
        		textView.setText(user.getNick());
        	}else{
        		textView.setText(username);
        	}
        }
    }
    
}
