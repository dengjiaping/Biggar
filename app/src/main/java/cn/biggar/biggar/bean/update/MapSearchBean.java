package cn.biggar.biggar.bean.update;

import com.baidu.mapapi.model.LatLng;

import java.io.Serializable;

/**
 * Created by Chenwy on 2017/11/17.
 */

public class MapSearchBean implements Serializable {
    private static final long serialVersionUID = -3650116734988695395L;
    public String key;
    public String city;
    public String district;
    public LatLng pt;
    public String uid;
}
