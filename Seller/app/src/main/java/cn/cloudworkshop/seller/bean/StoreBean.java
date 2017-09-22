package cn.cloudworkshop.seller.bean;

/**
 * Author：binge on 2017-09-19 13:30
 * Email：1993911441@qq.com
 * Describe：
 */
public class StoreBean {

    /**
     * code : 1
     * data : {"shop_name":"广州迪敖贸易","order_count":0,"order_sum":0}
     * msg : 获取成功
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
         * shop_name : 广州迪敖贸易
         * order_count : 0
         * order_sum : 0
         */

        private String shop_name;
        private int order_count;
        private int order_sum;

        public String getShop_name() {
            return shop_name;
        }

        public void setShop_name(String shop_name) {
            this.shop_name = shop_name;
        }

        public int getOrder_count() {
            return order_count;
        }

        public void setOrder_count(int order_count) {
            this.order_count = order_count;
        }

        public int getOrder_sum() {
            return order_sum;
        }

        public void setOrder_sum(int order_sum) {
            this.order_sum = order_sum;
        }
    }
}
