package com.guohe.corecenter.core;

/**
 * Created by SouKou on 2017/8/15.
 */

public interface CoreObject {
    void initialize();

    boolean isInitialized();

    void dispose();
}
