package cn.cloudworkshop.seller.bean;

import java.util.List;

/**
 * Author：binge on 2017-09-18 15:48
 * Email：1993911441@qq.com
 * Describe：
 */
public class OrderBean {

    /**
     * code : 1
     * msg : 获取成功
     * list : [[{"money":"0.01","c_time":1505729311,"id":2019,"car_ids":"5211","order_no":"1167757","goods_name":"始终如\u201c衣\u201d","goods_thumb":"/uploads/img/2017052716461454515299.jpg","num":1,"date":"昨日订单"}],[{"money":"100","c_time":1505584800,"id":2018,"car_ids":"5210","order_no":"1175201","goods_name":"始终如\u201c衣\u201d","goods_thumb":"/uploads/img/2017052716461454515299.jpg","num":1,"date":"20170917"}]]
     */

    private int code;
    private String msg;
    private List<List<ListBean>> list;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<List<ListBean>> getList() {
        return list;
    }

    public void setList(List<List<ListBean>> list) {
        this.list = list;
    }

    public static class ListBean {
        /**
         * money : 0.01
         * c_time : 1505729311
         * id : 2019
         * car_ids : 5211
         * order_no : 1167757
         * goods_name : 始终如“衣”
         * goods_thumb : /uploads/img/2017052716461454515299.jpg
         * num : 1
         * date : 昨日订单
         */

        private String money;
        private int c_time;
        private int id;
        private String car_ids;
        private String order_no;
        private String goods_name;
        private String goods_thumb;
        private int num;
        private String date;
        private int goods_type;
        private String size_content;

        public int getGoods_type() {
            return goods_type;
        }

        public void setGoods_type(int goods_type) {
            this.goods_type = goods_type;
        }

        public String getSize_content() {
            return size_content;
        }

        public void setSize_content(String size_content) {
            this.size_content = size_content;
        }

        public String getMoney() {
            return money;
        }

        public void setMoney(String money) {
            this.money = money;
        }

        public int getC_time() {
            return c_time;
        }

        public void setC_time(int c_time) {
            this.c_time = c_time;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getCar_ids() {
            return car_ids;
        }

        public void setCar_ids(String car_ids) {
            this.car_ids = car_ids;
        }

        public String getOrder_no() {
            return order_no;
        }

        public void setOrder_no(String order_no) {
            this.order_no = order_no;
        }

        public String getGoods_name() {
            return goods_name;
        }

        public void setGoods_name(String goods_name) {
            this.goods_name = goods_name;
        }

        public String getGoods_thumb() {
            return goods_thumb;
        }

        public void setGoods_thumb(String goods_thumb) {
            this.goods_thumb = goods_thumb;
        }

        public int getNum() {
            return num;
        }

        public void setNum(int num) {
            this.num = num;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }
    }
}
