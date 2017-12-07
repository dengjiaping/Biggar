package cn.biggar.biggar.presenter;

import android.content.Context;

import cn.biggar.biggar.api.DataApiFactory;
import cn.biggar.biggar.api.brand.IBrandAPI;
import cn.biggar.biggar.app.Constants;
import cn.biggar.biggar.bean.GoodsBean;

import java.util.ArrayList;

import per.sue.gear2.presenter.ListPresenter;
import rx.Observable;

/**
 * Created by mx on 2016/8/15.
 * 商品
 */
public class GoodsListPresenter extends ListPresenter<ArrayList<GoodsBean>> {
    private IBrandAPI brandAPI;
    private String mType;//销量(sales)、新品（news）、价格升序（price）、综合（）、价格降序（priceDesc）
    private String mBrandID;// 品牌ID



    public GoodsListPresenter(Context context ,ListResultView<ArrayList<GoodsBean>> listResultView) {
        super(context,listResultView);
        brandAPI= DataApiFactory.getInstance().createIBrandAPI(context);
    }

    public void setBrandID(String brandID){
        mBrandID=brandID;
        setObservable(getObservable());
    }

    public void setType(String type){
        mType=type;
        setObservable(getObservable());
    }

    public Observable getObservable() {
        return this.brandAPI.queryBrandGoodsList(mBrandID,mType,pageNum, Constants.PAGE_SIZE);
    }
}
