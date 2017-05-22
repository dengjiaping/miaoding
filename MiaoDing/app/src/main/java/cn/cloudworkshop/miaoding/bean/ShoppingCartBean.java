package cn.cloudworkshop.miaoding.bean;

import java.util.List;

/**
 * Author：Libin on 2016/10/27 09:17
 * Email：1993911441@qq.com
 * Describe：
 */
public class ShoppingCartBean {


    /**
     * code : 1
     * data : [{"id":18,"goods_name":"1","goods_thumb":"1","num":10,"price":"1.00","c_time":1477384385,"goods_id":1,"goods_type":1},{"id":16,"goods_name":"1","goods_thumb":"1","num":1,"price":"1.00","c_time":1477384299,"goods_id":4,"goods_type":1},{"id":15,"goods_name":"1","goods_thumb":"1","num":1,"price":"1.00","c_time":1477384297,"goods_id":4,"goods_type":1},{"id":14,"goods_name":"1","goods_thumb":"1","num":1,"price":"1.00","c_time":1477384282,"goods_id":4,"goods_type":1},{"id":13,"goods_name":"1","goods_thumb":"1","num":10,"price":"1.00","c_time":1477381402,"goods_id":4,"goods_type":1}]
     * msg : 成功
     */

    private int code;
    private String msg;
    /**
     * id : 18
     * goods_name : 1
     * goods_thumb : 1
     * num : 10
     * price : 1.00
     * c_time : 1477384385
     * goods_id : 1
     * goods_type : 1
     */

    private List<DataBean> data;

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

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        private int id;
        private String goods_name;
        private String goods_thumb;
        private int num;
        private double price;
        private int c_time;
        private int goods_id;
        private int goods_type;
        private String size_content;
        private boolean is_select = true;

        public boolean getIs_select() {
            return is_select;
        }

        public void setIs_select(boolean is_select) {
            this.is_select = is_select;
        }

        public int getId() {
            return id;
        }

        public String getSize_content() {
            return size_content;
        }

        public void setSize_content(String size_content) {
            this.size_content = size_content;
        }

        public void setId(int id) {
            this.id = id;
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

        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
        }

        public int getC_time() {
            return c_time;
        }

        public void setC_time(int c_time) {
            this.c_time = c_time;
        }

        public int getGoods_id() {
            return goods_id;
        }

        public void setGoods_id(int goods_id) {
            this.goods_id = goods_id;
        }

        public int getGoods_type() {
            return goods_type;
        }

        public void setGoods_type(int goods_type) {
            this.goods_type = goods_type;
        }
    }
}
