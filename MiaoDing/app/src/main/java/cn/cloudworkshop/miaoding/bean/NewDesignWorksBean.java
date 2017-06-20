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
     * list : {"total":1,"per_page":10,"current_page":1,"data":[{"id":79,"name":"小二西服","sub_name":"123","classify_id":1,"thumb":"/uploads/img/2017051609544856491001.jpg","img_list":"/uploads/img/2017051609551349509848.jpg,/uploads/img/2017051609551349551011.jpg,/uploads/img/2017051609551450495457.png","c_time":1494899729,"status":2,"view_num":0,"like_num":0,"price":"1111.00/2222.00/3333.00","sort":1,"content":"杜嘉班纳公司创立于1985年，总部位于意大利米兰。今天已成为在奢侈品领域中最主要的国际集团之一。","content2":"/uploads/img/2017051617510798545157.jpg","img_z1":null,"img_z2":null,"img_z3":null,"img_z4":null,"img_z5":null,"img_z6":null,"img_f1":null,"img_f2":null,"img_f3":null,"img_f4":null,"img_f5":null,"img_f6":null,"goods_no":"1","recommend_goods_ids":"","type":2,"uid":79,"spec_ids":null,"heat":1,"banxin_ids":null,"mianliao_ids":null,"ziti_ids":null,"yanse_ids":null,"weizhi_ids":null,"default_spec_ids":null,"default_spec_content":null,"default_mianliao":null,"pc_introduce":null,"pc_thumb":null,"tag":"地表最强设计师","username":"青衣","avatar":"/uploads/img/2017060517380610199989.jpg","remark":"","introduce":"dvfsdsdcccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccc"}]}
     * msg : 成功
     */

    private int code;
    private ListBean list;
    private String msg;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public ListBean getList() {
        return list;
    }

    public void setList(ListBean list) {
        this.list = list;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public static class ListBean {
        /**
         * total : 1
         * per_page : 10
         * current_page : 1
         * data : [{"id":79,"name":"小二西服","sub_name":"123","classify_id":1,"thumb":"/uploads/img/2017051609544856491001.jpg","img_list":"/uploads/img/2017051609551349509848.jpg,/uploads/img/2017051609551349551011.jpg,/uploads/img/2017051609551450495457.png","c_time":1494899729,"status":2,"view_num":0,"like_num":0,"price":"1111.00/2222.00/3333.00","sort":1,"content":"杜嘉班纳公司创立于1985年，总部位于意大利米兰。今天已成为在奢侈品领域中最主要的国际集团之一。","content2":"/uploads/img/2017051617510798545157.jpg","img_z1":null,"img_z2":null,"img_z3":null,"img_z4":null,"img_z5":null,"img_z6":null,"img_f1":null,"img_f2":null,"img_f3":null,"img_f4":null,"img_f5":null,"img_f6":null,"goods_no":"1","recommend_goods_ids":"","type":2,"uid":79,"spec_ids":null,"heat":1,"banxin_ids":null,"mianliao_ids":null,"ziti_ids":null,"yanse_ids":null,"weizhi_ids":null,"default_spec_ids":null,"default_spec_content":null,"default_mianliao":null,"pc_introduce":null,"pc_thumb":null,"tag":"地表最强设计师","username":"青衣","avatar":"/uploads/img/2017060517380610199989.jpg","remark":"","introduce":"dvfsdsdcccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccc"}]
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
             * id : 79
             * name : 小二西服
             * sub_name : 123
             * classify_id : 1
             * thumb : /uploads/img/2017051609544856491001.jpg
             * img_list : /uploads/img/2017051609551349509848.jpg,/uploads/img/2017051609551349551011.jpg,/uploads/img/2017051609551450495457.png
             * c_time : 1494899729
             * status : 2
             * view_num : 0
             * like_num : 0
             * price : 1111.00/2222.00/3333.00
             * sort : 1
             * content : 杜嘉班纳公司创立于1985年，总部位于意大利米兰。今天已成为在奢侈品领域中最主要的国际集团之一。
             * content2 : /uploads/img/2017051617510798545157.jpg
             * img_z1 : null
             * img_z2 : null
             * img_z3 : null
             * img_z4 : null
             * img_z5 : null
             * img_z6 : null
             * img_f1 : null
             * img_f2 : null
             * img_f3 : null
             * img_f4 : null
             * img_f5 : null
             * img_f6 : null
             * goods_no : 1
             * recommend_goods_ids :
             * type : 2
             * uid : 79
             * spec_ids : null
             * heat : 1
             * banxin_ids : null
             * mianliao_ids : null
             * ziti_ids : null
             * yanse_ids : null
             * weizhi_ids : null
             * default_spec_ids : null
             * default_spec_content : null
             * default_mianliao : null
             * pc_introduce : null
             * pc_thumb : null
             * tag : 地表最强设计师
             * username : 青衣
             * avatar : /uploads/img/2017060517380610199989.jpg
             * remark :
             * introduce : dvfsdsdcccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccc
             */

            private int id;
            private String name;
            private String sub_name;
            private int classify_id;
            private String thumb;
            private String img_list;
            private int c_time;
            private int status;
            private int view_num;
            private int like_num;
            private String price;
            private int sort;
            private String content;
            private String content2;
            private String goods_no;
            private int type;
            private int uid;
            private int heat;
            private String tag;
            private String username;
            private String avatar;
            private String remark;
            private String introduce;

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

            public String getSub_name() {
                return sub_name;
            }

            public void setSub_name(String sub_name) {
                this.sub_name = sub_name;
            }

            public int getClassify_id() {
                return classify_id;
            }

            public void setClassify_id(int classify_id) {
                this.classify_id = classify_id;
            }

            public String getThumb() {
                return thumb;
            }

            public void setThumb(String thumb) {
                this.thumb = thumb;
            }

            public String getImg_list() {
                return img_list;
            }

            public void setImg_list(String img_list) {
                this.img_list = img_list;
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

            public int getView_num() {
                return view_num;
            }

            public void setView_num(int view_num) {
                this.view_num = view_num;
            }

            public int getLike_num() {
                return like_num;
            }

            public void setLike_num(int like_num) {
                this.like_num = like_num;
            }

            public String getPrice() {
                return price;
            }

            public void setPrice(String price) {
                this.price = price;
            }

            public int getSort() {
                return sort;
            }

            public void setSort(int sort) {
                this.sort = sort;
            }

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public String getContent2() {
                return content2;
            }

            public void setContent2(String content2) {
                this.content2 = content2;
            }

            public String getGoods_no() {
                return goods_no;
            }

            public void setGoods_no(String goods_no) {
                this.goods_no = goods_no;
            }

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }

            public int getUid() {
                return uid;
            }

            public void setUid(int uid) {
                this.uid = uid;
            }

            public int getHeat() {
                return heat;
            }

            public void setHeat(int heat) {
                this.heat = heat;
            }

            public String getTag() {
                return tag;
            }

            public void setTag(String tag) {
                this.tag = tag;
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

            public String getIntroduce() {
                return introduce;
            }

            public void setIntroduce(String introduce) {
                this.introduce = introduce;
            }
        }
    }
}
