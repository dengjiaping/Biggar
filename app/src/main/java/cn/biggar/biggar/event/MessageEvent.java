package cn.biggar.biggar.event;

/**
 * Created by mx on 2017/1/4.
 * 消息event
 */

public class MessageEvent {
    /**
     * 先这俩字段 后续根据业务再改动
     * @param type
     * @param count
     */
    public int type;  //类型
    public int count;//数量


    public MessageEvent(int type, int count) {
        this.type = type;
        this.count = count;
    }
}
