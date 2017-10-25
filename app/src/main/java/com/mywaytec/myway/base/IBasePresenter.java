package com.mywaytec.myway.base;

/**
 * 基础 Presenter
 */
public interface IBasePresenter<T extends IBaseView> {

    void attachView(T view);

    void detachView();

}
