package com.mingyuechunqiu.agilemvpframe.base.presenter;

import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;

import com.mingyuechunqiu.agilemvpframe.base.model.BaseAbstractModel;
import com.mingyuechunqiu.agilemvpframe.base.view.IBaseDialogView;
import com.mingyuechunqiu.agilemvpframe.feature.loading.LoadingDialogFragmentOption;

/**
 * <pre>
 *     author : xyj
 *     e-mail : xiyujieit@163.com
 *     time   : 2018/05/12
 *     desc   : 所有带对话框功能的Presenter的基类
 *              继承自BaseAbstractPresenter
 *     version: 1.0
 * </pre>
 */
public abstract class BaseDialogPresenter<V extends IBaseDialogView, M extends BaseAbstractModel> extends BaseAbstractPresenter<V, M> {

    /**
     * 显示加载对话框
     *
     * @param hint       提示文本
     * @param cancelable 对话框是否可以被取消
     */
    protected void showLoadingDialog(@Nullable String hint, boolean cancelable) {
        if (mViewRef != null && mViewRef.get() != null) {
            mViewRef.get().showLoadingDialog(hint, cancelable);
        }
    }

    /**
     * 显示加载界面
     *
     * @param option 加载配置参数信息对象
     */
    protected void showLoadingDialog(@Nullable LoadingDialogFragmentOption option) {
        if (mViewRef != null && mViewRef.get() != null) {
            mViewRef.get().showLoadingDialog(option);
        }
    }

    /**
     * 关闭加载对话框
     */
    protected void dismissLoadingDialog() {
        if (mViewRef != null && mViewRef.get() != null) {
            mViewRef.get().dismissLoadingDialog();
        }
    }

    /**
     * 添加显示加载对话框
     *
     * @param containerId 对话框所属布局ID
     * @param option      加载对话框配置信息对象
     */
    protected void addOrShowLoadingDialog(@IdRes int containerId, LoadingDialogFragmentOption option) {
        if (mViewRef != null && mViewRef.get() != null) {
            mViewRef.get().addOrShowLoadingDialog(containerId, option);
        }
    }

    /**
     * 隐藏加载对话框
     */
    protected void hideLoadingDialog() {
        if (mViewRef != null && mViewRef.get() != null) {
            mViewRef.get().hideLoadingDialog();
        }
    }

    /**
     * 显示提示信息并关闭加载对话框
     *
     * @param hint 提示文本
     */
    protected void showToastAndDismissLoadingDialog(@Nullable String hint) {
        if (mViewRef != null && mViewRef.get() != null) {
            mViewRef.get().showToast(hint);
            mViewRef.get().dismissLoadingDialog();
        }
    }

    /**
     * 显示提示信息并关闭加载对话框
     *
     * @param stringResourceId 提示文本资源ID
     */
    protected void showToastAndDismissLoadingDialog(@StringRes int stringResourceId) {
        if (mViewRef != null && mViewRef.get() != null) {
            mViewRef.get().showToast(stringResourceId);
            mViewRef.get().dismissLoadingDialog();
        }
    }
}
