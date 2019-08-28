package com.nahuo.quicksale.eventbus;

/**
 * @description event bus传递用的数据
 * @created 2015-4-24 上午9:58:03
 * @author ZZB
 */
public class BusEvent {
    
    /**判断是哪种事件的Id {@link EventBusId}*/
    public int id;
    /**事件数据*/
    public Object data;

    public static BusEvent getEvent(int id){
        return new BusEvent(id);
    }
    public static BusEvent getEvent(int id, Object data){
        return new BusEvent(id, data);
    }
    private BusEvent(){
    }
    private BusEvent(int id) {
        this.id = id;
    }
    private BusEvent(int id, Object data) {
        this.id = id;
        this.data = data;
    }

    
}
