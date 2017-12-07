package cn.biggar.biggar.helper;


import com.duanqu.qupai.engine.session.UISettings;

/*
* 文件名：
* 描 述：
* 作 者：苏昭强
* 时 间：2016/5/5
* 版 权：比格科技有限公司
*/
public class QPUISettings extends UISettings {

    //是否需要导入功能
    public boolean hasImporter() {
        return true;
    }

    //是否需要编辑功能
    public boolean hasEditor() {
        return true;
    }

    //是否启动引导功能，建议用户第一次使用时设置为true
    public boolean hasGuide() {
        return true;
    }

    public boolean hasFlashLight() {
        return false;
    }

    //是否显示美颜图标
    public boolean hasSkinBeautifer() {
        return true;
    }
}
