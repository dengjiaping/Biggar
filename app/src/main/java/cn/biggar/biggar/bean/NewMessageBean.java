package cn.biggar.biggar.bean;

/**
 * Created by mx on 2016/11/3.
 * 最新消息
 */
public class NewMessageBean {
    /**
     * E_CreateDate : 1470473633
     * E_Type : 1
     * E_Message :  我发送了一个通告
     * ID : 55
     */

    private String E_CreateDate;
    private String E_RelaType;      //      E_RelaType  ：4通告  5评论  9礼物   10账户   12聊天  14官方   11公会  13悬赏
    private String E_Message;
    private String ID;

    public String getE_Number() {
        return E_Number;
    }

    public void setE_Number(String e_Number) {
        E_Number = e_Number;
    }

    private String E_Number;

    public String getE_CreateDate() {
        return E_CreateDate;
    }

    public void setE_CreateDate(String E_CreateDate) {
        this.E_CreateDate = E_CreateDate;
    }

    public String getE_RelaType() {
        return E_RelaType;
    }

    public void setE_RelaType(String E_RelaType) {
        this.E_RelaType = E_RelaType;
    }

    public String getE_Message() {
        return E_Message;
    }

    public void setE_Message(String E_Message) {
        this.E_Message = E_Message;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }
}
