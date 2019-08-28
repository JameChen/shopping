package com.nahuo.quicksale.provider;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nahuo.quicksale.common.SpManager;
import com.nahuo.quicksale.oldermodel.ContactModel;

import java.util.List;

/**
 * @author ZZB
 * @description 联系方式
 * @created 2014-9-2 下午2:42:47
 */
public class ContactInfoProvider {

    public static void saveContactInfo(Context context, List<ContactModel> contactInfo, String addressInfo) {
        List<ContactModel> infos = contactInfo;
        Gson gson = new Gson();
        String json = gson.toJson(infos);
        SpManager.setContactInfos(context, json);
        SpManager.setContactAddressInfos(context, addressInfo);
    }

    public static void saveAddressInfo(Context context, String addressInfo) {
        SpManager.setContactAddressInfos(context, addressInfo);
    }

    public static void saveContactInfo(Context context, List<ContactModel> contactInfo) {
        List<ContactModel> infos = contactInfo;
        Gson gson = new Gson();
        String json = gson.toJson(infos);
        SpManager.setContactInfos(context, json);
    }

    public static List<ContactModel> getContactInfo(Context context) {
        String json = SpManager.getContactInfos(context);
        if (TextUtils.isEmpty(json)) {
            return null;
        } else {
            Gson gson = new Gson();
            List<ContactModel> infos = gson.fromJson(json, new TypeToken<List<ContactModel>>() {
            }.getType());
            return infos;
        }
    }

    public static String getContactAddressInfo(Context context) {
        return SpManager.getContactAddressInfos(context);
    }

    /**
     * @description 是否设置手机
     * @created 2014-11-4 下午5:25:42
     * @author ZZB
     */
    public static boolean isPhoneSet(Context context) {
        String json = SpManager.getContactInfos(context);
        if (TextUtils.isEmpty(json)) {
            return false;
        } else {
            Gson gson = new Gson();
            List<ContactModel> infos = gson.fromJson(json, new TypeToken<List<ContactModel>>() {
            }.getType());
            for (ContactModel cm : infos) {
                if (cm.getTypeID() == ContactModel.TYPE_MOBILE) {
                    return cm.getIsEnabled() == 1;
                }
            }
            return false;
        }
    }
}
