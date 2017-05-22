package cn.cloudworkshop.miaoding.bean;

import java.util.List;

/**
 * Author：Libin on 2016-10-17 15:54
 * Email：1993911441@qq.com
 * Describe：
 */

public class DesignWorksBean {

    /**
     * code : 1
     * data : {"total":3,"per_page":10,"current_page":1,"data":[{"name":"第一期","p_time":"2016-10-13","img":"/uploads/img/2016101716164056481005","goods_id":"8","uid":0,"thumb":"/uploads/img/2016102612153353975052.jpg","username":null,"avatar":null,"remark":null},{"name":"第二期","p_time":"2016-10-16","img":"/uploads/img/2016101716213951519999","goods_id":"5","uid":0,"thumb":"/uploads/img/2016102612151652485551.jpg","username":null,"avatar":null,"remark":null},{"name":"下期预告","p_time":"2016-10-18","img":"/uploads/img/2016101716240452555549","goods_id":"","uid":null,"thumb":null,"username":null,"avatar":null,"remark":null}]}
     * msg : 成功
     */

    private int code;
    /**
     * total : 3
     * per_page : 10
     * current_page : 1
     * data : [{"name":"第一期","p_time":"2016-10-13","img":"/uploads/img/2016101716164056481005","goods_id":"8","uid":0,"thumb":"/uploads/img/2016102612153353975052.jpg","username":null,"avatar":null,"remark":null},{"name":"第二期","p_time":"2016-10-16","img":"/uploads/img/2016101716213951519999","goods_id":"5","uid":0,"thumb":"/uploads/img/2016102612151652485551.jpg","username":null,"avatar":null,"remark":null},{"name":"下期预告","p_time":"2016-10-18","img":"/uploads/img/2016101716240452555549","goods_id":"","uid":null,"thumb":null,"username":null,"avatar":null,"remark":null}]
     */

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
        private int total;
        private int per_page;
        private int current_page;
        /**
         * name : 第一期
         * p_time : 2016-10-13
         * img : /uploads/img/2016101716164056481005
         * goods_id : 8
         * uid : 0
         * thumb : /uploads/img/2016102612153353975052.jpg
         * username : null
         * avatar : null
         * remark : null
         */

        private List<ItemBean> data;

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

        public List<ItemBean> getData() {
            return data;
        }

        public void setData(List<ItemBean> data) {
            this.data = data;
        }

        public static class ItemBean {
            private String name;
            private String p_time;
            private String img;
            private String goods_id;
            private String goods_name;
            private int uid;
            private String thumb;
            private String username;
            private String avatar;
            private String remark;
            private String tag;
            private String introduce;

            public String getTag() {
                return tag;
            }

            public void setTag(String tag) {
                this.tag = tag;
            }

            public String getIntroduce() {
                return introduce;
            }

            public void setIntroduce(String introduce) {
                this.introduce = introduce;
            }

            public String getGoods_name() {
                return goods_name;
            }

            public void setGoods_name(String goods_name) {
                this.goods_name = goods_name;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getP_time() {
                return p_time;
            }

            public void setP_time(String p_time) {
                this.p_time = p_time;
            }

            public String getImg() {
                return img;
            }

            public void setImg(String img) {
                this.img = img;
            }

            public String getGoods_id() {
                return goods_id;
            }

            public void setGoods_id(String goods_id) {
                this.goods_id = goods_id;
            }

            public int getUid() {
                return uid;
            }

            public void setUid(int uid) {
                this.uid = uid;
            }

            public String getThumb() {
                return thumb;
            }

            public void setThumb(String thumb) {
                this.thumb = thumb;
            }

            public String getUsername() {
                return username;
            }

            public void setUsername(String username) {
                this.username = username;
            }

            public String getAvatar() {
                return avatar;
            }

            public void setAvatar(String avatar) {
                this.avatar = avatar;
            }

            public String getRemark() {
                return remark;
            }

            public void setRemark(String remark) {
                this.remark = remark;
            }
        }
    }
}
