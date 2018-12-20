package com.jct.bd.gettexidriver.model.datasource;

public interface Action<T> {
    void onSuccess(String obj);

    void onFailure(Exception exception);

    void onProgress(String status, double percent);
}
