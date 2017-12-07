package cn.biggar.biggar.event;

/**
 * Created by Chenwy on 2017/8/10.
 */

public class GetTaskEvent {
    public GetTaskEvent(boolean isJumpToLoading) {
        this.isJumpToLoading = isJumpToLoading;
    }

    public boolean isJumpToLoading;
}
