package com.nahuo.quicksale.exceptions;

/**
 * @description 手动捕捉异常
 * @created 2015-6-29 下午2:19:54
 * @author ZZB
 */
public class CatchedException extends RuntimeException{

    
    
    public CatchedException(String errorMsg) {
        super(errorMsg);
    }
    public CatchedException(Throwable e) {
        super(e);
    }

    private static final long serialVersionUID = 5371272532058112449L;

}
