package com.dog.manage.app;


public interface Callback {

    public void onError();

    public void onResponse(boolean success,int id);
}
