package com.mingyuechunqiu.agile.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentActivity;

import com.mingyuechunqiu.agile.base.presenter.BaseNetPresenter;
import com.mingyuechunqiu.agile.base.view.IBaseNetView;
import com.mingyuechunqiu.agile.util.ToolbarUtils;

import static com.mingyuechunqiu.agile.constants.CommonConstants.NO_RESOURCE_ID;

/**
 * <pre>
 *     author : xyj
 *     e-mail : yujie.xi@ehailuo.com
 *     time   : 2018/10/19
 *     desc   : 所有MVP层工具条界面的基类
 *              继承自BasePresenterFragment
 *     version: 1.0
 * </pre>
 */
public abstract class BaseToolbarPresenterFragment<V extends IBaseNetView<P>, P extends BaseNetPresenter> extends BaseNetPresenterFragment<V, P> {

    protected Toolbar tbBar;
    protected ToolbarUtils.ToolbarBean mToolbarBean;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        ActionBar actionBar = null;
        FragmentActivity activity = getActivity();
        if (activity instanceof AppCompatActivity) {
            setHasOptionsMenu(true);
            ((AppCompatActivity) activity).setSupportActionBar(tbBar);
            actionBar = ((AppCompatActivity) activity).getSupportActionBar();
        }
        mToolbarBean = setToolbarBean();
        ToolbarUtils.initToolbar(tbBar, actionBar, mToolbarBean);
        return view;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        if (mToolbarBean == null) {
            return;
        }
        if (mToolbarBean.isClearActivityMenu()) {
            menu.clear();
        }
        if (mToolbarBean.getMenuResId() != NO_RESOURCE_ID) {
            inflater.inflate(mToolbarBean.getMenuResId(), menu);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mToolbarBean = null;
    }

    /**
     * 供子类覆写的创建ToolbarBean方法，并放回创建好的ToolbarBean
     *
     * @return 返回创建好的ToolbarBean
     */
    protected abstract ToolbarUtils.ToolbarBean setToolbarBean();
}
