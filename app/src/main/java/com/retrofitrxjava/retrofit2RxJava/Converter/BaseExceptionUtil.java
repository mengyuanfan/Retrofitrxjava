package com.retrofitrxjava.retrofit2RxJava.Converter;

import android.util.Log;


import com.retrofitrxjava.constants.Constants;
import com.retrofitrxjava.retrofit2RxJava.exception.ApiException;
import com.retrofitrxjava.util.ToastUtil;

import java.io.IOException;

import retrofit2.adapter.rxjava.HttpException;

/**
 * Created by Admin on 2019/6/29.
 * 报错信息封装--------
 */
public class BaseExceptionUtil {

    //与BaseSubscriber 中的onError一样 ,为了去除加载提示框设置
    public static void onError(final Throwable e) {

        Log.w("Subscriber onError", e);
        if (e instanceof HttpException) {
            // We had non-2XX http error
//            Toast.makeText(mContext, mContext.getString(R.string.server_internal_error), Toast.LENGTH_SHORT).show();
            ToastUtil.showShortToast(Constants.serverInternalError);
        } else if (e instanceof IOException) {
            // A network or conversion error happened
//            Toast.makeText(mContext, mContext.getString(R.string.cannot_connected_server), Toast.LENGTH_SHORT).show();
            ToastUtil.showShortToast(Constants.cannotConnectedServer);
        } else if (e instanceof ApiException) {
            ApiException exception = (ApiException) e;
            if (exception.isTokenExpried()) {
                //处理token失效对应的逻辑
            } else {
                ToastUtil.showShortToast(e.getMessage());
            }
        }
    }
}
