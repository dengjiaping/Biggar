package cn.biggar.biggar.event;

import cn.biggar.biggar.bean.VideoBean;
import com.umeng.socialize.bean.SHARE_MEDIA;


/*
* 文件名：
* 描 述：
* 作 者：苏昭强
* 时 间：2016/4/28
* 版 权：比格科技有限公司
*/
public class PublishVideoSucessEvent {

    public SHARE_MEDIA shareMedia;
    public VideoBean videoBean;

    public PublishVideoSucessEvent(VideoBean videoBean) {
        this.videoBean = videoBean;
    }

    public PublishVideoSucessEvent(SHARE_MEDIA shareMedia, VideoBean videoBean) {
        this.shareMedia = shareMedia;
        this.videoBean = videoBean;
    }
}
