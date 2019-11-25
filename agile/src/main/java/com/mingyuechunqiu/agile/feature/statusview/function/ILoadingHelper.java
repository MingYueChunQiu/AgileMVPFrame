package com.mingyuechunqiu.agile.feature.statusview.function;

import androidx.fragment.app.DialogFragment;

/**
 * <pre>
 *     author : xyj
 *     Github : https://github.com/MingYueChunQiu
 *     e-mail : yujie.xi@ehailuo.com
 *     time   : 2019/1/24
 *     desc   : 加载Fragment主接口（用户实现此接口）
 *              继承LoadingDfgFunctionable, ILoadingDfgControl
 *     version: 1.0
 * </pre>
 */
public interface ILoadingHelper extends ILoadingDfgFunction, ILoadingDfgControl {

    DialogFragment getDialogFragment();
}