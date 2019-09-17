package com.retrofitrxjava.main.presenter;

import com.retrofitrxjava.main.MainBean;
import com.retrofitrxjava.main.model.MainModel;
import com.retrofitrxjava.retrofit2RxJava.Converter.BaseSubscriber;
import com.retrofitrxjava.retrofit2RxJava.http.HttpUtils;

/**
 * Created by Administrator on 2019/9/17.
 */
public class MainPresenter {

    private MainModel mView;

    public MainPresenter(MainModel mView){
        this.mView = mView;
    }

    public void getMain() {
        HttpUtils.requestGeMainDataByPost(new BaseSubscriber<MainBean>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                if (mView!=null)mView.onFail();
            }

            @Override
            public void onNext(MainBean bean) {
//                System.out.println("信息==="+bean.toString());
                if (mView!=null)mView.onSuccess(bean);
            }
        });
    }

}
