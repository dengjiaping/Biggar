package cn.biggar.biggar.contract;

import java.util.List;

import cn.biggar.biggar.base.BasePresenter;
import cn.biggar.biggar.base.BaseView;
import cn.biggar.biggar.bean.update.SearchGoodsBean;

/**
 * Created by Chenwy on 2017/9/19.
 */

public class SearchGoodsContract {
    public interface View extends BaseView {
        void showGoodses(List<SearchGoodsBean> searchGoodsBeen);
        void showMoreGoodses(List<SearchGoodsBean> searchGoodsBeen);
    }

    public abstract static class Presenter extends BasePresenter<View> {
        public abstract void requestGoods(String typeId,String uid,int pages,int p,String name);
        public abstract void loadMoreGoods(String typeId,String uid,int pages,int p,String name);
    }
}
