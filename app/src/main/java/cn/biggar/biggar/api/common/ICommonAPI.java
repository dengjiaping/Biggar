package cn.biggar.biggar.api.common;


import cn.biggar.biggar.bean.ApkInfoBean;
import cn.biggar.biggar.bean.BannerBean;
import cn.biggar.biggar.bean.CommentBean;
import cn.biggar.biggar.bean.ServerConfigBean;
import cn.biggar.biggar.bean.TalentLabelBean;
import cn.biggar.biggar.wxapi.WXPayInfo;


import java.io.File;
import java.util.ArrayList;


import per.sue.gear2.net.progress.ProgressResponseListener;
import rx.Observable;

/**
 * Created by mx on 16/5/19.
 */
public interface ICommonAPI {


    Observable<ApkInfoBean> checkApkVersion();

    Observable<ServerConfigBean> getServerConfig();


    Observable<File> loadFile(String url, File file, ProgressResponseListener progressResponseListener);

    Observable<WXPayInfo> loadWXPayInfo();

    /**
     * 查询广告 根据类型
     *
     * @param type
     * @return
     */
    Observable<ArrayList<BannerBean>> queryBannerByType(String type);

    /**
     * 查询评论列表
     *
     * @param id     ID
     * @param pages
     * @param vitype 类型 评论类型：如视频（VIDEO）、会员（MEMBER）、商品（SHOP）、通告（ACTIVITY）
     * @param p
     * @return
     */
    Observable<ArrayList<CommentBean>> queryCommentList(String id, String uid, String vitype, int pages, int p);


    /**
     * 评论点赞
     *
     * @param id 评论ID
     * @return
     */
    Observable<String> commentLike(String id, String uid);

    /**
     * 实现愿望
     *
     * @param modelId  宝箱ID
     * @param userId   用户ID
     * @param userName 用户名
     */
    Observable<String> achieveDesire(String modelId, String userId, String userName);


    /**
     * 分享成功后
     *
     * @param oid  分享的对像ID、如视频ID
     * @param uid  分享的会员ID
     * @param type 分享的类型、如：VIDEO、BRAND、PRODUCT、是视频（VIDEO）时、可以省略这个参数传、接口有默认为：VIDEO
     * @return
     */
    Observable<String> shareSuccess(String oid, String uid, String type);

    /**
     * 删除发布的图片或者视频
     *
     * @param uid
     * @param mid
     */
    Observable<String> deleteRelease(String uid, String mid);

    /**
     * 删除相册图片
     *
     * @param uid
     * @param url
     * @return
     */
    Observable<String> deleteImage(String uid, String url);


    /**
     * 验证手机验证码
     */
    Observable<String> verifyCode(String userId, String code);

    /**
     * 判断密码是否正确
     *
     * @param phoneNum
     * @param password
     * @return
     */
    Observable<String> getVerifyPassword(String userId, String password, String phoneNum);

    /**
     * 查询 达人认证标签
     *
     * @return
     */
    Observable<ArrayList<TalentLabelBean>> getTalentLabelList(String pages);
}
