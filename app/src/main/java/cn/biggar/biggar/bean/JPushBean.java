package cn.biggar.biggar.bean;

/**
 * Created by Administrator on 2016/9/29.
 */
public class JPushBean {
    public String c_id;//消息ID
    public String c_title;//消息标题
    public String c_content;//消息内容
    public int c_tag;//消息进入页面
    public String c_tagvalue;//消息可带参数值
    public String c_weburl;//消息网链接
    public String c_type;//消息类型

    public String getC_id() {
        return c_id;
    }

    public void setC_id(String c_id) {
        this.c_id = c_id;
    }

    public String getC_title() {
        return c_title;
    }

    public void setC_title(String c_title) {
        this.c_title = c_title;
    }

    public String getC_content() {
        return c_content;
    }

    public void setC_content(String c_content) {
        this.c_content = c_content;
    }

    public int getC_tag() {
        return c_tag;
    }

    public void setC_tag(int c_tag) {
        this.c_tag = c_tag;
    }

    public String getC_tagvalue() {
        return c_tagvalue;
    }

    public void setC_tagvalue(String c_tagvalue) {
        this.c_tagvalue = c_tagvalue;
    }

    public String getC_weburl() {
        return c_weburl;
    }

    public void setC_weburl(String c_weburl) {
        this.c_weburl = c_weburl;
    }

    public String getC_type() {
        return c_type;
    }

    public void setC_type(String c_type) {
        this.c_type = c_type;
    }
}
