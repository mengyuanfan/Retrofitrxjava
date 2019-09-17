package com.retrofitrxjava.main.model;

import com.retrofitrxjava.main.MainBean;

/**
 * Created by Administrator on 2019/9/17.
 */
public interface MainModel {
    void onSuccess(MainBean bean);
    void onFail();
}
