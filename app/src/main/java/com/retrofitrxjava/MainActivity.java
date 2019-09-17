package com.retrofitrxjava;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.retrofitrxjava.main.MainBean;
import com.retrofitrxjava.main.model.MainModel;
import com.retrofitrxjava.main.presenter.MainPresenter;

public class MainActivity extends AppCompatActivity
        implements MainModel {
    private MainPresenter mainPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //MVP 网络获取数据---------------
        mainPresenter = new MainPresenter(this);
        mainPresenter.getMain();
    }

    @Override
    public void onSuccess(MainBean bean) {
    }

    @Override
    public void onFail() {
    }
}
