package cn.biggar.biggar.api.brand;

import cn.biggar.biggar.bean.BrandBean;
import cn.biggar.biggar.bean.BrandDetails;
import cn.biggar.biggar.bean.CardWishBrandBean;
import cn.biggar.biggar.bean.GoodsBean;
import cn.biggar.biggar.bean.BrandListBean;
import cn.biggar.biggar.bean.RedPacketBean;
import cn.biggar.biggar.bean.VideoImageBean;
import cn.biggar.biggar.bean.WishCardBean;

import java.util.ArrayList;

import rx.Observable;

/**
 * Created by SUE on 2016/7/20 0020.
 */
public interface IBrandAPI {

    /**
     * 推荐品牌列表
     * @param type 分类地址(频道接口中的：E_Path值)
     * @param p
     * @param pages
     * @return
     */
     Observable<ArrayList<BrandBean>> queryRecommendBrandList(String type, String searchName, int p, int pages);

     /**
      * 关注品牌列表
      * @param userId 关注的会员ID
      * @param type 分类地址(频道接口中的：E_Path值)
      * @param flag 会员和品牌已相互关注（1）、会员关注的所有品牌（0）、默认为：0
      * @param p
      * @param pages
     * @return
     */
     Observable<ArrayList<BrandBean>> queryConcerBrandList(String userId, String type, String searchName, int flag, int p, int pages);

    /**
     * 品牌详情
     * @param Id
     * @return
     */
     Observable<BrandDetails> queryBrandDetails(String Id);

    /**
     * 品牌 粉丝秀
     * @param brandId 品牌ID
     * @param p
     * @param pages
     * @return
     */
     Observable<ArrayList<VideoImageBean>> queryFansShowList(String brandId, int p, int pages);


    /**
     * 商品列表
     * @param brandId 品牌ID
     * @param type  销量(sales)、新品（news）、价格（price）、综合（）
     * @param p
     * @param pages
     * @return
     */
    Observable<ArrayList<GoodsBean>> queryBrandGoodsList(String brandId, String type, int p, int pages);



    /**
     * 卡卷愿望  热门品牌列表
     * @param uid
     * @return
     */
    Observable<ArrayList<CardWishBrandBean>> queryCardWishHotBrandList(String uid);


    /**
     * 卡卷愿望  品牌列表
     * @param uid
     * @return
     */
    Observable<ArrayList<CardWishBrandBean>> queryCardWishBrandList(String uid);

    /**
     * 品牌卡卷列表
     * @param uid
     * @return
     */
    Observable<ArrayList<WishCardBean>> queryBrandCardList(String uid, String brandId);


    /**
     * 我的品牌 列表
     * @param uid
     * @param p
     * @param pages
     * @return
     */
    Observable<BrandListBean> queryBrandList(String uid, int p, int pages);

    /**
     *  品牌 查询红包
     * @param bid
     * @return
     */
    Observable<RedPacketBean> queryBrandRedPacket(String bid);

    /**
     *  品牌 红包 获取
     * @param bid
     * @return
     */
    Observable<String> getBrandRedPacket(String uid, String bid, String cid, String type);

}
