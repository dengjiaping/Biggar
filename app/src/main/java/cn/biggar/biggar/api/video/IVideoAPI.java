package cn.biggar.biggar.api.video;

import cn.biggar.biggar.bean.ConcersBean;
import cn.biggar.biggar.bean.LiWuBean;
import cn.biggar.biggar.bean.UserBean;
import cn.biggar.biggar.bean.VideoBean;
import cn.biggar.biggar.bean.VideoImageBean;
import cn.biggar.biggar.bean.VideoImageDetailsBean;
import cn.biggar.biggar.bean.VideoTagBean;
import cn.biggar.biggar.bean.VideoTypeBean;

import java.util.ArrayList;
import java.util.Map;


import rx.Observable;

/*
* 文件名：
* 描 述：
* 作 者：苏昭强
* 时 间：2016/5/6
*/
public interface IVideoAPI  {

    /**
     * 发布视频
     * @param userId 用户ID
     * @param userName 用户昵称
     * @param content 视频描述
     * @param path 视频路径
     * @param leng 视频长度
     * @param type 视频类别
     * @param cover 封面
     * @param tag 标签
     * @param first 第一帧
     * @return
     */
    public Observable<VideoBean> publishVideo(String userId, String userName, String content, String path, String leng, String type, String cover, String tag, String first);
    public Observable<VideoBean> publishVideo(Map<String, String> params);
    public Observable<VideoBean> publishImage(Map<String, String> params);

    /**
     * 查询视频类别／视频频道
     * @param type  0紅人  1品牌
     * @return
     */
    Observable<ArrayList<VideoTypeBean>> queryVideoTypes(String type);

    /**
     * 获取视频标签
     * @return
     */
    Observable<ArrayList<VideoTagBean>> queryVideoTags(String id);

    /**
     *获取推荐列表
     * @return
     */
    Observable<ArrayList<VideoImageBean>> queryRecommendVideo(int page, int pages, String tag);

    /**
     *获取关注列表
     * @return
     */
        Observable<ArrayList<ConcersBean>> queryConcers(int page, int pages, String uid, int mTag, String name, String brand_id);
  
    /**
     *获取 视频图片 详情
     * @return
     */
    Observable<VideoImageDetailsBean> queryVideoImageDetails(String ID, String uid);

    /**
     * 礼物列表
     */
    Observable<LiWuBean> queryLiWuList(String uid, int p, int pages, int postion);
    /**
     * 我的（TA）礼物
     */
    Observable<LiWuBean> queryMyLiWuList(int p, int pages, String MembID, String OID, String TYPE);


    /**
     *活动秀场
     * @return
     */
    Observable<ArrayList<VideoImageBean>> queryActivityShowList(int page, int pages);

    /**
     * 查询 名人堂列表
     * @param p
     * @param pages
     * @return
     */
    Observable<ArrayList<UserBean>> queryHallofFameList(String searchKey, int p, int pages);




}
