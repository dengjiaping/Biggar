package cn.biggar.biggar.bean;

/**
 * Created by mx on 2016/9/19.
 * 愿望卡卷
 */
public class WishCardBean extends CardBean{

    /**
     * ID : 1025
     * E_Language : cn
     * E_CreateUser : sinomax
     * E_CreateDate : 2016-07-29 15:15:36
     * E_UpdateUser : null
     * E_UpdateDate : 2016-07-29 15:15:36
     * E_CreateIP : 116.23.249.161
     * E_State : 0
     * E_MemID : 1136
     * E_CardNo : 5863972545
     * E_Money : 10.00
     * E_CardType : 1
     * E_ExDate : 2016
     * E_Title : 爆款现金券满100使用
     * E_ProID : 0
     * E_ConsumeNO : null
     * E_BrandID : 6
     * E_Past : 1
     * E_Path :
     * E_WebHotID : 14
     * E_Role : N;
     * E_Rate : 0.00
     * E_RelationData : null
     * E_FromeUser : 0
     * E_RateKeys : 915963260691367
     */

    private boolean type;//类型  true null     false 有卡卷

    public boolean isType() {
        return type;
    }

    public void setType(boolean type) {
        this.type = type;
    }

    public WishCardBean(boolean type) {
        this.type = type;
    }
    public WishCardBean() {
    }

}
