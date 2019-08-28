package com.nahuo.quicksale.api;
import android.content.Context;
import com.nahuo.quicksale.oldermodel.PublicData;
import java.util.HashMap;
import java.util.Map;
/**
 * Created by ALAN on 2017/5/22 0022.
 */
public class BankAPI {
    private static final String TAG = "BankAPI";
    private static BankAPI instance = null;
    /**
     * 单例
     */
    public static BankAPI getInstance() {
        if (instance == null) {
            instance = new BankAPI();
        }
        return instance;
    }

    //获取银行卡列表
    public static String getBankList(Context context) throws Exception{
        String json;
        Map<String, String> params = new HashMap<String, String>();
        json=HttpUtils.httpPost("user/user/GetBankList", params, PublicData.getCookie(context));
        return json;
    }

    //绑定银行卡（保存身份信息和银行卡）
    public static String submitBindBank(Context context,String name,String cardId,String bankName,String bankId) throws Exception{
        String json;
        Map<String, String> params = new HashMap<String, String>();
        params.put("realName", name);
        params.put("idCode",cardId);
        params.put("bankName", bankName);
        params.put("cardNo", bankId);
        json=HttpUtils.httpPost("user/user/SaveCertifyWithBankInfo", params, PublicData.getCookie(context));
        return json;
    }
}
