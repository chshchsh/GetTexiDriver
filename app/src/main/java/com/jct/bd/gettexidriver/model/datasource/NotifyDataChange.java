package com.jct.bd.gettexidriver.model.datasource;

public interface NotifyDataChange<T> {
    void OnDataChanged(T obj);

    void onFailure(Exception exception);
}
