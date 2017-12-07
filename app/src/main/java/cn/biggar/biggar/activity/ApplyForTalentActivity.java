package cn.biggar.biggar.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.orhanobut.logger.Logger;

import cn.biggar.biggar.R;
import cn.biggar.biggar.base.BiggarActivity;
import cn.biggar.biggar.base.BiggarFragment;
import cn.biggar.biggar.fragment.ApplyForTalentFragment;
import cn.biggar.biggar.fragment.ApplyForTalentFragment2;

/**
 * Created by mx on 2016/12/12.
 * 申请认证比格达人
 */

public class ApplyForTalentActivity extends BiggarActivity{
    public  static final int MODE_AFT_1=1;//申请认证(个人资料进)
    public  static final int MODE_AFT_2=2;//上传图片
    public  static final int MODE_AFT_3=3;//成功
    public  static final int MODE_AFT_4=4;//申请认证(非个人资料进)
    private String mUserId;
    private String mLabel;//认证的标签  MODE_AFT_2 时用到
    private BiggarFragment mFragment;
    public static Intent getInstance(Context context,int mode,String userId) {
        return getInstance(context,mode,userId,null);
    }

    public static Intent getInstance(Context context,int mode,String userId,String label) {
        Intent intent = new Intent(context, ApplyForTalentActivity.class);
        intent.putExtra("mode",mode);
        intent.putExtra("userId",userId);
        if (label!=null){
            intent.putExtra("label",label);
        }
        return intent;
    }

    @Override
    public int getLayoutResId() {
        return R.layout.activity_apply_for_talent;
    }

    @Override
    public void onInitialize(@Nullable Bundle savedInstanceState) {
        int mode=getIntent().getIntExtra("mode",MODE_AFT_1);
        mUserId=getIntent().getStringExtra("userId");
        mLabel=getIntent().getStringExtra("label");
        switch (mode){
            case MODE_AFT_1:
                mFragment=ApplyForTalentFragment.getInstance(0,mUserId);
                break;
            case MODE_AFT_2:
                mFragment=ApplyForTalentFragment2.getInstance(mUserId,mLabel);
                break;
            case MODE_AFT_3:
                mFragment=ApplyForTalentFragment.getInstance(1,mUserId);
                break;
            case MODE_AFT_4:
                mFragment=ApplyForTalentFragment.getInstance(2,mUserId);
                break;
        }
        addFragment(R.id.fragment_content,mFragment);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mFragment.onActivityResult(requestCode,resultCode,data);
    }
}
