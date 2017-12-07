package cn.biggar.biggar.receiver;
import android.content.Context;
import io.rong.push.notification.PushMessageReceiver;
import io.rong.push.notification.PushNotificationMessage;

/**
 * Created by Chenwy on 2017/6/22.
 */

public class BGNotificationReceiver extends PushMessageReceiver {
    @Override
    public boolean onNotificationMessageArrived(Context context, PushNotificationMessage pushNotificationMessage) {
        return false;
    }

    @Override
    public boolean onNotificationMessageClicked(Context context, PushNotificationMessage message) {
        return false;
    }

//    private void handJump(Context context){
//        Intent intent = new Intent();
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        intent.putExtra("isFromNotification", true);
//        intent.putExtra("tagetId", message.getTargetId());
//        intent.putExtra("tagetName", message.getTargetUserName());
//        intent.setClass(context, MainActivity.class);
//        context.startActivity(intent);
//    }
}
