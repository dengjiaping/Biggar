package cn.biggar.biggar.event;


import cn.biggar.biggar.bean.MusicBean;

/**
 * Created by SUE on 2016/6/24 0024.
 */
public class MusicSelectEvent {

    public MusicBean musicBean;

    public MusicSelectEvent(MusicBean musicBean) {
        this.musicBean = musicBean;
    }
}
