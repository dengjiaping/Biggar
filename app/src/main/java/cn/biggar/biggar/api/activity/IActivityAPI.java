package cn.biggar.biggar.api.activity;

import cn.biggar.biggar.bean.ActivityBean;

import java.util.ArrayList;

import rx.Observable;

/**
 * Created by mx on 2016/8/11 .
 * 活动
 */
public interface IActivityAPI {

    /**
     * 活动悬赏列表
     * @param brandId 品牌商ID、可省略、对应品牌商的任务
     * @param p
     * @param pages
     * @return
     */
     Observable<ArrayList<ActivityBean>> queryActivityRewardList(String brandId, int p, int pages);



    /**
     * 我参加的活动
     * @param brandId 品牌商ID、可省略、对应品牌商的任务
     * @param userID 用户ID
     * @param p
     * @param pages
     * @return
     */
    Observable<ArrayList<ActivityBean>> queryActivityMyJoinList(String brandId, String userID, int p, int pages);
}
