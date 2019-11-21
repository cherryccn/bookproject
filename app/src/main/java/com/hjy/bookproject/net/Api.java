package com.hjy.bookproject.net;

import android.os.Environment;

import com.hjy.bookproject.BaseApplication;

import java.io.File;

/**
 * Created by hjy on 2019/11/20
 */
public class Api {

    private static String ROOT_URL = "http://www.imooc.com/";

    private static String FILE_DOWN_LOAD_SD_URL = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "booklist" + File.separator;

    private static String FILE_DOWN_APP_URL = BaseApplication.getContext().getFilesDir().getAbsolutePath() + File.separator;


    //文件下载路径
    public static String getFileRootPath() {
        File file;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            file = new File(FILE_DOWN_LOAD_SD_URL);
            if (!file.exists()) {
                file.mkdir();
            }
            return FILE_DOWN_LOAD_SD_URL;
        } else {
            file = new File(FILE_DOWN_APP_URL);
            if (!file.exists()) {
                file.mkdir();
            }
            return FILE_DOWN_APP_URL;
        }
    }

    //获取书架列表数据
    public static String getBookLists() {
        return ROOT_URL + "api/teacher?type=10";
    }


}
