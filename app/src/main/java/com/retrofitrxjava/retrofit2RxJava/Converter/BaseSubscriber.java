package com.retrofitrxjava.retrofit2RxJava.Converter;

import android.content.Context;
import android.util.Log;


import com.retrofitrxjava.constants.Constants;
import com.retrofitrxjava.retrofit2RxJava.exception.ApiException;
import com.retrofitrxjava.util.ToastUtil;

import java.io.IOException;

import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;

/**
 * Created by Admin on 2019/6/22.
 * 统一报错信息处理-------------------------
 * https://www.jianshu.com/p/5b8b1062866b
 */
public class BaseSubscriber <T> extends Subscriber<T> {
    protected Context mContext;

//    public BaseSubscriber(Context context) {
//        this.mContext = context;
//    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(final Throwable e) {
        Log.w("Subscriber onError", e);
        if (e instanceof HttpException) {
            // We had non-2XX http error
//            Toast.makeText(mContext, mContext.getString(R.string.server_internal_error), Toast.LENGTH_SHORT).show();
            ToastUtil.showShortToast(Constants.serverInternalError);
            System.out.println("BaseSubscriber----"+e.getMessage());
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

    @Override
    public void onNext(T t) {

    }
}
