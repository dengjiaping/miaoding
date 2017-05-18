package cn.cloudworkshop.miaoding.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Author：Libin on 2016/10/27 16:10
 * Email：1993911441@qq.com
 * Describe：
 */
public class OrderInfoBean {


    /**
     * code : 1
     * data : [{"id":26,"car_ids":"10","order_no":"YGC2016102716144250545253","status":1,"ems_com_id":0,"ems_no":null,"money":"0.00","ems_com_name":null,"ems_com":null,"list":[{"goods_name":"","goods_thumb":"","num":1,"price":"0.00"}]},{"id":13,"car_ids":"5","order_no":null,"status":1,"ems_com_id":0,"ems_no":null,"money":"0.00","ems_com_name":null,"ems_com":null,"list":[{"goods_name":"","goods_thumb":"","num":1,"price":"0.00"}]},{"id":12,"car_ids":"1","order_no":null,"status":1,"ems_com_id":0,"ems_no":null,"money":"0.00","ems_com_name":null,"ems_com":null,"list":[{"goods_name":"1","goods_thumb":"1","num":1,"price":"1.00"}]},{"id":11,"car_ids":"0","order_no":null,"status":1,"ems_com_id":0,"ems_no":null,"money":"0.00","ems_com_name":null,"ems_com":null,"list":[]},{"id":9,"car_ids":"0","order_no":null,"status":1,"ems_com_id":0,"ems_no":null,"money":"0.00","ems_com_name":null,"ems_com":null,"list":[]},{"id":8,"car_ids":"2,3","order_no":null,"status":1,"ems_com_id":0,"ems_no":null,"money":"0.00","ems_com_name":null,"ems_com":null,"list":[{"goods_name":"山沟02","goods_thumb":"1","num":1,"price":"1.00"},{"goods_name":"商品01","goods_thumb":"http://139.196.113.61/uploads/img/20161014172840565749515897242250_2_3_1.jpg","num":1,"price":"1.00"}]},{"id":7,"car_ids":"2","order_no":null,"status":1,"ems_com_id":0,"ems_no":null,"money":"0.00","ems_com_name":null,"ems_com":null,"list":[{"goods_name":"商品01","goods_thumb":"http://139.196.113.61/uploads/img/20161014172840565749515897242250_2_3_1.jpg","num":1,"price":"1.00"}]},{"id":6,"car_ids":"2","order_no":null,"status":1,"ems_com_id":0,"ems_no":null,"money":"0.00","ems_com_name":null,"ems_com":null,"list":[{"goods_name":"商品01","goods_thumb":"http://139.196.113.61/uploads/img/20161014172840565749515897242250_2_3_1.jpg","num":1,"price":"1.00"}]},{"id":5,"car_ids":"2","order_no":null,"status":1,"ems_com_id":0,"ems_no":null,"money":"0.00","ems_com_name":null,"ems_com":null,"list":[{"goods_name":"商品01","goods_thumb":"http://139.196.113.61/uploads/img/20161014172840565749515897242250_2_3_1.jpg","num":1,"price":"1.00"}]},{"id":4,"car_ids":"2","order_no":null,"status":1,"ems_com_id":0,"ems_no":null,"money":"0.00","ems_com_name":null,"ems_com":null,"list":[{"goods_name":"商品01","goods_thumb":"http://139.196.113.61/uploads/img/20161014172840565749515897242250_2_3_1.jpg","num":1,"price":"1.00"}]},{"id":3,"car_ids":"0","order_no":null,"status":1,"ems_com_id":0,"ems_no":null,"money":"0.00","ems_com_name":null,"ems_com":null,"list":[]},{"id":2,"car_ids":"2","order_no":null,"status":1,"ems_com_id":0,"ems_no":null,"money":"0.00","ems_com_name":null,"ems_com":null,"list":[{"goods_name":"商品01","goods_thumb":"http://139.196.113.61/uploads/img/20161014172840565749515897242250_2_3_1.jpg","num":1,"price":"1.00"}]},{"id":1,"car_ids":"0","order_no":null,"status":1,"ems_com_id":0,"ems_no":null,"money":"0.00","ems_com_name":null,"ems_com":null,"list":[]}]
     * msg : 成功
     */

    private int code;
    private String msg;
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
        /**
         * id : 26
         * car_ids : 10
         * order_no : YGC2016102716144250545253
         * status : 1
         * ems_com_id : 0
         * ems_no : null
         * money : 0.00
         * ems_com_name : null
         * ems_com : null
         * list : [{"goods_name":"","goods_thumb":"","num":1,"price":"0.00"}]
         */

        private int id;
        private String car_ids;
        private String order_no;
        private int status;
        private int ems_com_id;
        private String ems_no;
        private String money;
        private String ems_com_name;
        private String ems_com;
        private List<ListBean> list;

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

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public int getEms_com_id() {
            return ems_com_id;
        }

        public void setEms_com_id(int ems_com_id) {
            this.ems_com_id = ems_com_id;
        }

        public String getEms_no() {
            return ems_no;
        }

        public void setEms_no(String ems_no) {
            this.ems_no = ems_no;
        }

        public String getMoney() {
            return money;
        }

        public void setMoney(String money) {
            this.money = money;
        }

        public String getEms_com_name() {
            return ems_com_name;
        }

        public void setEms_com_name(String ems_com_name) {
            this.ems_com_name = ems_com_name;
        }

        public String getEms_com() {
            return ems_com;
        }

        public void setEms_com(String ems_com) {
            this.ems_com = ems_com;
        }

        public List<ListBean> getList() {
            return list;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }

        public static class ListBean implements Serializable{
            /**
             * goods_name :
             * goods_thumb :
             * num : 1
             * price : 0.00
             */

            private int id;
            private String goods_name;
            private String goods_thumb;
            private int num;
            private String price;
            private int goods_type;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            private String size_content;

            public String getSize_content() {
                return size_content;
            }

            public int getGoods_type() {
                return goods_type;
            }

            public void setGoods_type(int goods_type) {
                this.goods_type = goods_type;
            }

            public void setSize_content(String size_content) {
                this.size_content = size_content;
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

            public String getPrice() {
                return price;
            }

            public void setPrice(String price) {
                this.price = price;
            }
        }
    }


}
