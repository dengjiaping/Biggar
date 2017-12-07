package per.sue.gear2.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * Created by SUE on 2016/7/7 0007.
 */
public abstract class UIBaseFragment extends Fragment implements IBaseView {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutResId(), null);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        onInitialize(savedInstanceState);
    }

    public void onError(int code, String message) {
    }

    public void onCompleted() {
    }

    public void showLoading() {
    }

    public void onLoadFailed() {
    }
}
