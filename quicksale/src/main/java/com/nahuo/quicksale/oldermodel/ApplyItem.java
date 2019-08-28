package com.nahuo.quicksale.oldermodel;

import java.io.Serializable;

/**
 * Description:代理商申请的list view item
 * 2014-7-8下午2:36:49
 */
public class ApplyItem implements Serializable {

    // icon
    private int userId;
    private String name;
    /** 是否处理过 */
    private boolean handled;
    /** 处理过后的状态：已同意、已忽略@see {@link HandleStateCode} */
    private int handleStateCode;
    /** 处理过后的状态：已同意、已忽略 */
    private String handleState;
    /** 申请日期 */
    private String createData;
    /** 申请说明 */
    private String message;

    /** 处理的状态 */
    public static interface HandleStateCode {
        public static final int APPLY = 1;
        public static final int REJECT = 2;
        public static final int AGREE = 3;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isHandled() {
        return handled;
    }

    public void setHandled(boolean handled) {
        this.handled = handled;
    }

    /**
     * Description:获取处理状态
     * 
     * @return @see {@link HandleState} 2014-7-12 下午6:18:51
     */
    public int getHandleStateCode() {
        return handleStateCode;
    }

    /**
     * Description:设置处理状态
     * 
     * @param @see {@link HandleState} 2014-7-12 下午6:17:33
     */
    public void setHandleStateCode(int handleState) {
        this.handleStateCode = handleState;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getCreateData() {
        return createData;
    }

    public void setCreateData(String createData) {
        this.createData = createData;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getHandleState() {
        return handleState;
    }

    public void setHandleState(String handleState) {
        this.handleState = handleState;
        if("申请".equals(handleState)){
            this.handleStateCode = HandleStateCode.APPLY;
        }else if("拒绝".equals(handleState)){
            this.handleStateCode = HandleStateCode.REJECT;
        }else if("接受".equals(handleState)){
            this.handleStateCode = HandleStateCode.AGREE;
        }
    }

    

}
