package com.guohe.corecenter.utils;

import java.util.UUID;

/**
 * Created by kousou on 2018/12/17.
 */

public class UUIDUtil {

    public static String createRandomUUID(){
        return UUID.randomUUID().toString().replace("-", "");
    }

    public static void main(String args[]) {
        System.out.println(createRandomUUID());
    }
}
