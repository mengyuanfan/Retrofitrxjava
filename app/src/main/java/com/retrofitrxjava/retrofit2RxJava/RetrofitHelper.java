package com.retrofitrxjava.retrofit2RxJava;

import com.google.gson.Gson;
import com.retrofitrxjava.constants.GlobalConstantUrl;
import com.retrofitrxjava.retrofit2RxJava.Converter.CustomGsonConverterFactory;
import com.retrofitrxjava.retrofit2RxJava.http.OkHttpClientHelper;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Retrofit帮助类
 */

public class RetrofitHelper {

    private static OkHttpClient mClient;
    private Retrofit mRetrofit;

    private RetrofitHelper() {
        mClient = OkHttpClientHelper.getInstance().getOkHttpClient();
    }

    private static RetrofitHelper helper;

    //单例 保证对象唯一
    public static RetrofitHelper getInstance() {
        if (helper == null) {
            synchronized (RetrofitHelper.class) {
                if (helper == null) {
                    helper = new RetrofitHelper();
                }
            }
        }
        return helper;
    }

    //获取Retrofit对象
    public Retrofit getRetrofit() {

        if (mClient == null) {

            mClient = OkHttpClientHelper.getInstance().getOkHttpClient();
        }

        if (mRetrofit == null) {
            mRetrofit = new Retrofit.Builder()
                    .baseUrl(GlobalConstantUrl.BASE_SERVER + "/")
//                    .addConverterFactory(new NullOnEmptyConverterFactory())   //NullOnEmptyConverterFactory必需在GsonConverterFactory之前addConverterFactory
//                    .addConverterFactory(GsonConverterFactory.create())  //添加Gson支持
                    //TODO 修改--java.lang.IllegalStateException: Fatal Exception thrown on Scheduler.Worker thread.
//                    .addConverterFactory(GsonConverterFactory.create(new Gson()))  //添加Gson支持
                    .addConverterFactory(ScalarsConverterFactory.create())//首先判断是否需要转换成字符串，简单类型
                    .addConverterFactory(CustomGsonConverterFactory.create(new Gson()))  //再将转换成bean 添加Gson支持(自定义)
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())  //添加RxJava支持
                    .client(mClient)                                            //关联okhttp
                    .build();
        }
        return mRetrofit;
    }


   //TODO 2017/07/10  java.io.EOFException:End of input at line 1 column 1 path $
    public class NullOnEmptyConverterFactory extends Converter.Factory {
        @Override
        public Converter<ResponseBody, ?> responseBodyConverter(Type type,
                                                                Annotation[] annotations,
                                                                Retrofit retrofit) {
            final Converter<ResponseBody, ?> delegate = retrofit.nextResponseBodyConverter
                    (this, type, annotations);
            return new Converter<ResponseBody,Object>() {
                @Override
                public Object convert(ResponseBody body) throws IOException {
                    if (body.contentLength() == 0) return null;
                    return delegate.convert(body);
                }
            };
        }
    }

}
