package cn.cloudworkshop.miaoding.bean;

import java.util.List;

/**
 * Author：Libin on 2016-10-17 15:54
 * Email：1993911441@qq.com
 * Describe：
 */

public class NewDesignWorksBean {


    /**
     * code : 1
     * data : {"total":2,"per_page":10,"current_page":1,"data":[{"id":15,"name":"博物馆","des_uid":282,"title":null,"content":"/uploads/img/2017090708315155555253.png","c_time":1504773151,"status":2,"recommend_goods_ids":"49","img":"/uploads/img/2017090708533310057565.png","p_time":"1504773156","sort":1},{"id":8,"name":"","des_uid":282,"title":"从你的全世界路过","content":"/uploads/img/2017073117561510210154.png","c_time":1494899535,"status":2,"recommend_goods_ids":"","img":"/uploads/img/2017073117561951102505.png","p_time":"1495868320","sort":1}]}
     * msg : 成功
     */

    private int code;
    private DataBeanX data;
    private String msg;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public DataBeanX getData() {
        return data;
    }

    public void setData(DataBeanX data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public static class DataBeanX {
        /**
         * total : 2
         * per_page : 10
         * current_page : 1
         * data : [{"id":15,"name":"博物馆","des_uid":282,"title":null,"content":"/uploads/img/2017090708315155555253.png","c_time":1504773151,"status":2,"recommend_goods_ids":"49","img":"/uploads/img/2017090708533310057565.png","p_time":"1504773156","sort":1},{"id":8,"name":"","des_uid":282,"title":"从你的全世界路过","content":"/uploads/img/2017073117561510210154.png","c_time":1494899535,"status":2,"recommend_goods_ids":"","img":"/uploads/img/2017073117561951102505.png","p_time":"1495868320","sort":1}]
         */

        private int total;
        private int per_page;
        private int current_page;
        private List<DataBean> data;

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public int getPer_page() {
            return per_page;
        }

        public void setPer_page(int per_page) {
            this.per_page = per_page;
        }

        public int getCurrent_page() {
            return current_page;
        }

        public void setCurrent_page(int current_page) {
            this.current_page = current_page;
        }

        public List<DataBean> getData() {
            return data;
        }

        public void setData(List<DataBean> data) {
            this.data = data;
        }

        public static class DataBean {
            /**
             * id : 15
             * name : 博物馆
             * des_uid : 282
             * title : null
             * content : /uploads/img/2017090708315155555253.png
             * c_time : 1504773151
             * status : 2
             * recommend_goods_ids : 49
             * img : /uploads/img/2017090708533310057565.png
             * p_time : 1504773156
             * sort : 1
             */

            private int id;
            private String name;
            private int des_uid;
            private Object title;
            private String content;
            private int c_time;
            private int status;
            private String recommend_goods_ids;
            private String img;
            private String p_time;
            private int sort;
            private String c_time_format;

            public String getC_time_format() {
                return c_time_format;
            }

            public void setC_time_format(String c_time_format) {
                this.c_time_format = c_time_format;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public int getDes_uid() {
                return des_uid;
            }

            public void setDes_uid(int des_uid) {
                this.des_uid = des_uid;
            }

            public Object getTitle() {
                return title;
            }

            public void setTitle(Object title) {
                this.title = title;
            }

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public int getC_time() {
                return c_time;
            }

            public void setC_time(int c_time) {
                this.c_time = c_time;
            }

            public int getStatus() {
                return status;
            }

            public void setStatus(int status) {
                this.status = status;
            }

            public String getRecommend_goods_ids() {
                return recommend_goods_ids;
            }

            public void setRecommend_goods_ids(String recommend_goods_ids) {
                this.recommend_goods_ids = recommend_goods_ids;
            }

            public String getImg() {
                return img;
            }

            public void setImg(String img) {
                this.img = img;
            }

            public String getP_time() {
                return p_time;
            }

            public void setP_time(String p_time) {
                this.p_time = p_time;
            }

            public int getSort() {
                return sort;
            }

            public void setSort(int sort) {
                this.sort = sort;
            }
        }
    }
}
