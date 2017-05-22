package cn.cloudworkshop.miaoding.bean;

import java.util.List;

/**
 * Author：Libin on 2016-10-21 15:06
 * Email：1993911441@qq.com
 * Describe：
 */

public class CollectionBean {


    /**
     * code : 1
     * data : [{"name":"衬衣AB","thumb":"/uploads/img/20161014172840565749515897242250_2_3_1.jpg","type":1,"goods_type":1,"cid":1,"id":346,"price2":"500~2000"}]
     * msg : 成功
     */

    private int code;
    private String msg;
    /**
     * name : 衬衣AB
     * thumb : /uploads/img/20161014172840565749515897242250_2_3_1.jpg
     * type : 1
     * goods_type : 1
     * cid : 1
     * id : 346
     * price2 : 500~2000
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
        private String name;
        private String thumb;
        private int type;
        private int goods_type;
        private int cid;
        private int id;
        private String price2;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getThumb() {
            return thumb;
        }

        public void setThumb(String thumb) {
            this.thumb = thumb;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getGoods_type() {
            return goods_type;
        }

        public void setGoods_type(int goods_type) {
            this.goods_type = goods_type;
        }

        public int getCid() {
            return cid;
        }

        public void setCid(int cid) {
            this.cid = cid;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getPrice2() {
            return price2;
        }

        public void setPrice2(String price2) {
            this.price2 = price2;
        }
    }
}
