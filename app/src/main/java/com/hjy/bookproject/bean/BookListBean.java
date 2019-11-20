package com.hjy.bookproject.bean;

import java.util.List;

/**
 * Created by hjy on 2019/11/21
 */
public class BookListBean {


    /**
     * status : 1
     * data : [{"bookname":"幻兽少年","bookfile":"http://www.imooc.com/data/teacher/down/幻兽少年.txt"},{"bookname":"魔界的女婿","bookfile":"http://www.imooc.com/data/teacher/down/魔界的女婿.txt"},{"bookname":"盘龙","bookfile":"http://www.imooc.com/data/teacher/down/盘龙.txt"},{"bookname":"庆余年","bookfile":"http://www.imooc.com/data/teacher/down/庆余年.txt"},{"bookname":"武神空间","bookfile":"http://www.imooc.com/data/teacher/down/武神空间.txt"}]
     * msg : 成功
     */

    private int status;
    private String msg;
    private List<DataBean> data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * bookname : 幻兽少年
         * bookfile : http://www.imooc.com/data/teacher/down/幻兽少年.txt
         */

        private String bookname;
        private String bookfile;

        public String getBookname() {
            return bookname;
        }

        public void setBookname(String bookname) {
            this.bookname = bookname;
        }

        public String getBookfile() {
            return bookfile;
        }

        public void setBookfile(String bookfile) {
            this.bookfile = bookfile;
        }
    }
}
