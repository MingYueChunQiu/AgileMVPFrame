package com.mingyuechunqiu.agilemvpframe.base.presenter;

import android.text.TextUtils;

import com.mingyuechunqiu.agilemvpframe.R;
import com.mingyuechunqiu.agilemvpframe.agile.AgileMVPFrame;
import com.mingyuechunqiu.agilemvpframe.base.model.BaseNetModel;
import com.mingyuechunqiu.agilemvpframe.base.view.BaseNetView;
import com.mingyuechunqiu.agilemvpframe.base.view.BaseView;
import com.mingyuechunqiu.agilemvpframe.data.bean.BaseInfo;
import com.mingyuechunqiu.agilemvpframe.util.NetworkUtils;
import com.mingyuechunqiu.agilemvpframe.util.SharedPreferencesUtils;

import static com.mingyuechunqiu.agilemvpframe.constants.UserConstants.PREF_USER_INFO;
import static com.mingyuechunqiu.agilemvpframe.constants.UserConstants.TOKEN;

/**
 * <pre>
 *     author : xyj
 *     e-mail : yujie.xi@ehailuo.com
 *     time   : 2018/11/15
 *     desc   : 所有带网络功能的P层的基类
 *              继承自BaseAbstractPresenter
 *     version: 1.0
 * </pre>
 */
public abstract class BaseNetPresenter<V extends BaseNetView, M extends BaseNetModel> extends BaseAbstractPresenter<V, M> {

    @Override
    public void detachView() {
        if (mModel != null) {
            mModel.release();
            mModel = null;
        }
        releaseOnDetach();
    }

    /**
     * 检测网络请求
     *
     * @return 当界面被回收和网络中断时返回false，否则返回true
     */
    protected boolean judgeNetwork() {
        if (mViewRef.get() != null) {
            if (!NetworkUtils.checkNetState(mViewRef.get().getCurrentContext())) {
                mViewRef.get().showToast(R.string.network_disconnected);
                return false;
            }
            return true;
        }
        return false;
    }

    /**
     * 进行网络请求
     */
    protected void getRequest() {
        getRequest(null);
    }

    /**
     * 带参数的网络请求
     *
     * @param info 网络请求参数对象
     * @param <B>  网络请求参数类型
     */
    protected <B extends BaseInfo> void getRequest(B info) {
        //判断当前网络状况，是否继续进行网络业务操作
        if (judgeNetwork()) {
            requestModel(info);
        } else {
            if (mViewRef.get() != null) {
                disconnectNet();
            }
        }
    }

    /**
     * 获取token
     *
     * @return 存储的token值
     */
    protected String getToken() {
        if (mViewRef.get() == null) {
            return null;
        }
        String token = SharedPreferencesUtils.getString(
                AgileMVPFrame.getAppContext(), PREF_USER_INFO, TOKEN, null);
        if (TextUtils.isEmpty(token)) {
            showToast(R.string.error_set_net_params);
            return null;
        } else {
            return token;
        }
    }

    /**
     * 由子类重写，调用model进行网络请求操作
     *
     * @param info 网络请求参数对象
     * @param <B>  网络请求参数类型
     */
    protected abstract <B extends BaseInfo> void requestModel(B info);

    /**
     * 当网络连接断开时回调
     */
    protected abstract void disconnectNet();
}