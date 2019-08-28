package com.nahuo.constant;

import com.nahuo.quicksale.app.BWApplication;

import java.io.File;

/**
 * Created by jame on 2018/4/26.
 */

public class Constant {
    public static final int TYPE_LEVEL_0 = 0;
    public static final int TYPE_LEVEL_1 = 1;
    public static final String PATH_DATA = BWApplication.getInstance().getCacheDir().getAbsolutePath() + File.separator + "data";
    public static final String PATH_CACHE = PATH_DATA + "/NetCache";
    public class EccId{
        public static final int ECC_SYSTEM = 862418;
    }
    public class TtrdeAction {
        public static final String TRADING_FLOW = "交易流水";
        public static final String RECHARGE_RECORD = "充值记录";
        public static final String PRESENT_RECORD = "提现记录";
        public static final String ORDER_TRANSACTION = "订单交易";
        public static final String ORDER_REFUND = "订单退款";
        public static final String FREIGHT_SETTLEMENT = "运费结算";
        public static final String  AFTER_SALE_SUBSIDY  = "售后补贴";
        public static final String SECURITY_SETTING = "安全设置";
    }
    public class CionType {
        public static final int CIONTYPE_DEFAUT = -1;
        public static final int CIONTYPE_All = 0;
    }
    public class AreaTypeID {
        public static final int AREATYPEID_AD = 1;
        public static final int AREATYPEID_POP = 2;
    }

    public class ShopCartVersion {
        public static final int Version_2 = 2;
        public static final int Version_3 = 3;
    }

    public class NotifyAreaID {
        public static final int NotifyAreaID_OrderManager = 2;
        public static final int NotifyAreaID_ShopCart = 1;
        public static final int NotifyAreaID_Me = 0;
        public static final int NotifyAreaID_OrderManager_All = 3;
        public static final int NotifyAreaID_OrderManager_Wait_Pay = 4;
        public static final int NotifyAreaID_OrderManager_Wait_Send= 5;
        public static final int NotifyAreaID_OrderManager_Wait_Receive = 6;
        public static final int NotifyAreaID_OrderManager_Done = 7;
        public static final int NotifyAreaID_OrderManager_Close= 8;
    }

    public class LoginRegisterFrom {
        public static final int LOGIN_REGISTER_FROM_ANDROID = 8;
        public static final int LOGIN_FROM_TTPH = 1;
    }
}
