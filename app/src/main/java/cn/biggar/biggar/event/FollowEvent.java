package cn.biggar.biggar.event;

/**
 * Created by Chenwy on 2017/8/8.
 */

public class FollowEvent {
    public FollowEvent(boolean isFollow) {
        this.isFollow = isFollow;
    }

    public boolean isFollow;
}
