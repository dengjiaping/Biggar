package cn.biggar.biggar.preference;

import android.content.Context;
import android.text.TextUtils;

import cn.biggar.biggar.bean.UserBean;
import cn.biggar.biggar.utils.SerializableUtil;
import cn.biggar.biggar.utils.SharedPreferencesHelper;

import java.io.IOException;

public class AbsPreferences {
	
	private static final String TAG = "GearPreferences";
    private static final boolean DEBUG = false;
    static UserBean mUserBeans;
    
    
    protected static Object getBaseObject(Context context,String str)
    {

//    	  ObjectInputStream objectIn = null;
//          Object object = null;
//          try {
//              FileInputStream fileIn = context.getApplicationContext().openFileInput(str);
//              objectIn = new ObjectInputStream(fileIn);
//              object = objectIn.readObject();
//          } catch (FileNotFoundException e) {
//              // Do nothing
//          } catch (IOException e) {
//              e.printStackTrace();
//          } catch (ClassNotFoundException e) {
//              e.printStackTrace();
//          } finally {
//              if (objectIn != null) {
//                  try {
//                      objectIn.close();
//                  } catch (IOException e) {
//                  }
//              }
//          }
        String ret = "";
        SharedPreferencesHelper share = SharedPreferencesHelper.getInstance(context,str);
        ret = share.getValue("userStr");
        if (!TextUtils.isEmpty(ret)) {
            Object object = null;
            try {
                object = SerializableUtil.str2Obj(ret);
                if (null != object)
                    mUserBeans = (UserBean) object;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return mUserBeans;

    }
    
    
    protected static void storeBaseObject(Context ctx,Object object,String str)
    {
//    	 ObjectOutputStream objectOut = null;
//         try {
//             FileOutputStream fileOut = ctx.openFileOutput(str,0);
//             objectOut = new ObjectOutputStream(fileOut);
//             objectOut.writeObject(object);
//             fileOut.getFD().sync();
//             if (DEBUG)
//                 Log.d(AbsPreferences.TAG, "storeObject"+str+" sessus!");
//         } catch (IOException e) {
//             e.printStackTrace();
//             if (DEBUG) Log.d(TAG, "store"+str+" error!"+e.toString());
//         } finally {
//             if (objectOut != null) {
//                 try {
//                     objectOut.close();
//                 } catch (IOException e) {
//                 }
//             }
//         }
        mUserBeans = (UserBean) object;
        String userStr = "";
        try {
            userStr = SerializableUtil.obj2Str(mUserBeans);
        } catch (IOException e) {
            e.printStackTrace();
        }
        SharedPreferencesHelper helper = SharedPreferencesHelper.getInstance(ctx, str);
        helper.putValue("userStr", userStr);
    }

}
