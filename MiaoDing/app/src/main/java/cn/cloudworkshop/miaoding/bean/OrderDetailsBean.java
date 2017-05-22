package cn.cloudworkshop.miaoding.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Author：Libin on 2016/10/27 18:07
 * Email：1993911441@qq.com
 * Describe：
 */
public class OrderDetailsBean {


    /**
     * code : 1
     * data : {"order_no":"YGC2016102613505198985010","c_time":"2016-10-26 13:50:51","p_time":"1970-01-01 08:00:00","s_time":"2016-10-27 15:03:38","q_time":"1970-01-01 08:00:00","name":"121","phone":"1212","province":"浙江省","city":"杭州市","area":"下城区","address":"东新路星城发展大厦","money":"1500.00","car_ids":"32","pay_type":0,"ems_com_id":0,"ems_no":"","status":3,"ems_com_name":null,"ems_com":null,"car_list":[{"goods_name":"成品01","goods_thumb":"/uploads/img/2016101116064957521025a2.jpg","price":"1500.00","num":1}]}
     * msg : 成功
     */

    private int code;
    private DataBean data;
    private String msg;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public static class DataBean {
        /**
         * order_no : YGC2016102613505198985010
         * c_time : 2016-10-26 13:50:51
         * p_time : 1970-01-01 08:00:00
         * s_time : 2016-10-27 15:03:38
         * q_time : 1970-01-01 08:00:00
         * name : 121
         * phone : 1212
         * province : 浙江省
         * city : 杭州市
         * area : 下城区
         * address : 东新路星城发展大厦
         * money : 1500.00
         * car_ids : 32
         * pay_type : 0
         * ems_com_id : 0
         * ems_no :
         * status : 3
         * ems_com_name : null
         * ems_com : null
         * car_list : [{"goods_name":"成品01","goods_thumb":"/uploads/img/2016101116064957521025a2.jpg","price":"1500.00","num":1}]
         */

        private String order_no;
        private String c_time;
        private String p_time;
        private String s_time;
        private String q_time;
        private String name;
        private String phone;
        private String province;
        private String city;
        private String area;
        private String address;
        private String money;
        private String car_ids;
        private int pay_type;
        private int ems_com_id;
        private String ems_no;
        private int status;
        private String ems_com_name;
        private String ems_com;
        private int last_time;
        private String ticket_money;

        public String getTicket_money() {
            return ticket_money;
        }

        public void setTicket_money(String ticket_money) {
            this.ticket_money = ticket_money;
        }

        public int getLast_time() {
            return last_time;
        }

        public void setLast_time(int last_time) {
            this.last_time = last_time;
        }

        private List<CarListBean> car_list;

        public String getOrder_no() {
            return order_no;
        }

        public void setOrder_no(String order_no) {
            this.order_no = order_no;
        }

        public String getC_time() {
            return c_time;
        }

        public void setC_time(String c_time) {
            this.c_time = c_time;
        }

        public String getP_time() {
            return p_time;
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

        public void setP_time(String p_time) {
            this.p_time = p_time;
        }

        public String getS_time() {
            return s_time;
        }

        public void setS_time(String s_time) {
            this.s_time = s_time;
        }

        public String getQ_time() {
            return q_time;
        }

        public void setQ_time(String q_time) {
            this.q_time = q_time;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getProvince() {
            return province;
        }

        public void setProvince(String province) {
            this.province = province;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getArea() {
            return area;
        }

        public void setArea(String area) {
            this.area = area;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getMoney() {
            return money;
        }

        public void setMoney(String money) {
            this.money = money;
        }

        public String getCar_ids() {
            return car_ids;
        }

        public void setCar_ids(String car_ids) {
            this.car_ids = car_ids;
        }

        public int getPay_type() {
            return pay_type;
        }

        public void setPay_type(int pay_type) {
            this.pay_type = pay_type;
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

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }


        public List<CarListBean> getCar_list() {
            return car_list;
        }

        public void setCar_list(List<CarListBean> car_list) {
            this.car_list = car_list;
        }

        public static class CarListBean implements Serializable{
            /**
             * goods_name : 成品01
             * goods_thumb : /uploads/img/2016101116064957521025a2.jpg
             * price : 1500.00
             * num : 1
             */

            private String goods_name;
            private String goods_thumb;
            private double price;
            private int num;
            private String size_content;
            private int goods_type;
            private int id;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

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

            public double getPrice() {
                return price;
            }

            public void setPrice(double price) {
                this.price = price;
            }

            public int getNum() {
                return num;
            }

            public void setNum(int num) {
                this.num = num;
            }
        }
    }
}

