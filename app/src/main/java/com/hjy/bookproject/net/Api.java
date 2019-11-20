package com.hjy.bookproject.net;

/**
 * Created by hjy on 2019/11/20
 */
public class Api {

    private static String ROOT_URL = "http://www.imooc.com/";

    //获取书架列表数据
    public static String getBookLists() {
        return ROOT_URL + "api/teacher?type=10";
    }
}
