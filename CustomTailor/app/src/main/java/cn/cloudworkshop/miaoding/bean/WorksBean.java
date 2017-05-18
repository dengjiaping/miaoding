package cn.cloudworkshop.miaoding.bean;

import java.util.List;

/**
 * Author：binge on 2017-05-09 16:10
 * Email：1993911441@qq.com
 * Describe：
 */
public class WorksBean {

    /**
     * code : 1
     * data : {"name":"【云工场&杜嘉班纳】 2017春夏新款","sub_name":"东西方文化的碰撞","img_list":["/uploads/img/2017050208401097979951.jpg","/uploads/img/2017050208401310054101.png"],"view_num":0,"like_num":0,"price":[],"content":"杜嘉班纳公司创立于1985年，总部位于意大利米兰。今天已成为在奢侈品领域中最主要的国际集团之一。","type":2,"heat":149,"thumb":"/uploads/img/2017042411432510057545.jpg","classify_id":1,"uid":79,"designer":{"id":47,"uid":79,"name":"胡","phone":"136161635979","weixin":"13916905124","email":"13916905124@qq","img_list1":"/uploads/apply/2017011394696.jpg,","img_list2":"/uploads/apply/2017011319413.jpg,","c_time":1484294467,"status":2,"pop_num":0,"tag":"标签123123","introduce":null},"size_list":[{"id":7,"name":"X","sku_num":107,"sale_num":16,"price":"123.00","size_name":"2","size_list":[{"id":7,"gid":49,"name":"X","sku_num":107,"sale_num":16,"price":"123.00","c_time":1493690270,"size_list_id":0,"size_id":0,"color_img":"1","size":"2","sort":null,"status":1}]},{"id":8,"name":"XL","sku_num":111,"sale_num":5,"price":"44.00","size_name":"3","size_list":[{"id":8,"gid":49,"name":"XL","sku_num":111,"sale_num":5,"price":"44.00","c_time":1493690293,"size_list_id":0,"size_id":0,"color_img":"2","size":"3","sort":null,"status":1}]},{"id":9,"name":"XXL","sku_num":111,"sale_num":6,"price":"55.00","size_name":"4","size_list":[{"id":9,"gid":49,"name":"XXL","sku_num":111,"sale_num":6,"price":"55.00","c_time":1493690355,"size_list_id":0,"size_id":0,"color_img":"3","size":"4","sort":null,"status":1}]}],"is_collect":-1,"is_yuyue":0}
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
         * name : 【云工场&杜嘉班纳】 2017春夏新款
         * sub_name : 东西方文化的碰撞
         * img_list : ["/uploads/img/2017050208401097979951.jpg","/uploads/img/2017050208401310054101.png"]
         * view_num : 0
         * like_num : 0
         * price : []
         * content : 杜嘉班纳公司创立于1985年，总部位于意大利米兰。今天已成为在奢侈品领域中最主要的国际集团之一。
         * type : 2
         * heat : 149
         * thumb : /uploads/img/2017042411432510057545.jpg
         * classify_id : 1
         * uid : 79
         * designer : {"id":47,"uid":79,"name":"胡","phone":"136161635979","weixin":"13916905124","email":"13916905124@qq","img_list1":"/uploads/apply/2017011394696.jpg,","img_list2":"/uploads/apply/2017011319413.jpg,","c_time":1484294467,"status":2,"pop_num":0,"tag":"标签123123","introduce":null}
         * size_list : [{"id":7,"name":"X","sku_num":107,"sale_num":16,"price":"123.00","size_name":"2","size_list":[{"id":7,"gid":49,"name":"X","sku_num":107,"sale_num":16,"price":"123.00","c_time":1493690270,"size_list_id":0,"size_id":0,"color_img":"1","size":"2","sort":null,"status":1}]},{"id":8,"name":"XL","sku_num":111,"sale_num":5,"price":"44.00","size_name":"3","size_list":[{"id":8,"gid":49,"name":"XL","sku_num":111,"sale_num":5,"price":"44.00","c_time":1493690293,"size_list_id":0,"size_id":0,"color_img":"2","size":"3","sort":null,"status":1}]},{"id":9,"name":"XXL","sku_num":111,"sale_num":6,"price":"55.00","size_name":"4","size_list":[{"id":9,"gid":49,"name":"XXL","sku_num":111,"sale_num":6,"price":"55.00","c_time":1493690355,"size_list_id":0,"size_id":0,"color_img":"3","size":"4","sort":null,"status":1}]}]
         * is_collect : -1
         * is_yuyue : 0
         */

        private String name;
        private String sub_name;
        private int view_num;
        private int like_num;
        private String content;
        private String content2;

        public String getContent2() {
            return content2;
        }

        public void setContent2(String content2) {
            this.content2 = content2;
        }

        private int type;
        private int heat;
        private String thumb;
        private int classify_id;
        private int uid;
        private DesignerBean designer;
        private int is_collect;
        private int is_yuyue;
        private List<String> img_list;

        private List<SizeListBeanX> size_list;

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

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getHeat() {
            return heat;
        }

        public void setHeat(int heat) {
            this.heat = heat;
        }

        public String getThumb() {
            return thumb;
        }

        public void setThumb(String thumb) {
            this.thumb = thumb;
        }

        public int getClassify_id() {
            return classify_id;
        }

        public void setClassify_id(int classify_id) {
            this.classify_id = classify_id;
        }

        public int getUid() {
            return uid;
        }

        public void setUid(int uid) {
            this.uid = uid;
        }

        public DesignerBean getDesigner() {
            return designer;
        }

        public void setDesigner(DesignerBean designer) {
            this.designer = designer;
        }

        public int getIs_collect() {
            return is_collect;
        }

        public void setIs_collect(int is_collect) {
            this.is_collect = is_collect;
        }

        public int getIs_yuyue() {
            return is_yuyue;
        }

        public void setIs_yuyue(int is_yuyue) {
            this.is_yuyue = is_yuyue;
        }

        public List<String> getImg_list() {
            return img_list;
        }

        public void setImg_list(List<String> img_list) {
            this.img_list = img_list;
        }


        public List<SizeListBeanX> getSize_list() {
            return size_list;
        }

        public void setSize_list(List<SizeListBeanX> size_list) {
            this.size_list = size_list;
        }

        public static class DesignerBean {
            /**
             * id : 47
             * uid : 79
             * name : 胡
             * phone : 136161635979
             * weixin : 13916905124
             * email : 13916905124@qq
             * img_list1 : /uploads/apply/2017011394696.jpg,
             * img_list2 : /uploads/apply/2017011319413.jpg,
             * c_time : 1484294467
             * status : 2
             * pop_num : 0
             * tag : 标签123123
             * introduce : null
             */

            private int id;
            private int uid;
            private String name;
            private String avatar;
            private String phone;
            private String weixin;
            private String email;

            public String getAvatar() {
                return avatar;
            }

            public void setAvatar(String avatar) {
                this.avatar = avatar;
            }

            private String img_list1;
            private String img_list2;
            private int c_time;
            private int status;
            private int pop_num;
            private String tag;
            private String introduce;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public int getUid() {
                return uid;
            }

            public void setUid(int uid) {
                this.uid = uid;
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

            public String getWeixin() {
                return weixin;
            }

            public void setWeixin(String weixin) {
                this.weixin = weixin;
            }

            public String getEmail() {
                return email;
            }

            public void setEmail(String email) {
                this.email = email;
            }

            public String getImg_list1() {
                return img_list1;
            }

            public void setImg_list1(String img_list1) {
                this.img_list1 = img_list1;
            }

            public String getImg_list2() {
                return img_list2;
            }

            public void setImg_list2(String img_list2) {
                this.img_list2 = img_list2;
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

            public int getPop_num() {
                return pop_num;
            }

            public void setPop_num(int pop_num) {
                this.pop_num = pop_num;
            }

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
        }

        public static class SizeListBeanX {
            /**
             * id : 7
             * name : X
             * sku_num : 107
             * sale_num : 16
             * price : 123.00
             * size_name : 2
             * size_list : [{"id":7,"gid":49,"name":"X","sku_num":107,"sale_num":16,"price":"123.00","c_time":1493690270,"size_list_id":0,"size_id":0,"color_img":"1","size":"2","sort":null,"status":1}]
             */

            private int id;
            private String name;
            private int sku_num;
            private int sale_num;
            private String price;
            private String size_name;
            private List<SizeListBean> size_list;

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

            public int getSku_num() {
                return sku_num;
            }

            public void setSku_num(int sku_num) {
                this.sku_num = sku_num;
            }

            public int getSale_num() {
                return sale_num;
            }

            public void setSale_num(int sale_num) {
                this.sale_num = sale_num;
            }

            public String getPrice() {
                return price;
            }

            public void setPrice(String price) {
                this.price = price;
            }

            public String getSize_name() {
                return size_name;
            }

            public void setSize_name(String size_name) {
                this.size_name = size_name;
            }

            public List<SizeListBean> getSize_list() {
                return size_list;
            }

            public void setSize_list(List<SizeListBean> size_list) {
                this.size_list = size_list;
            }

            public static class SizeListBean {
                /**
                 * id : 7
                 * gid : 49
                 * name : X
                 * sku_num : 107
                 * sale_num : 16
                 * price : 123.00
                 * c_time : 1493690270
                 * size_list_id : 0
                 * size_id : 0
                 * color_img : 1
                 * size : 2
                 * sort : null
                 * status : 1
                 */

                private int id;
                private int gid;
                private String name;
                private int sku_num;
                private int sale_num;
                private String price;
                private int c_time;
                private int size_list_id;
                private int size_id;
                private String color_img;
                private String color_name;
                private String size;
                private Object sort;
                private int status;

                public String getColor_name() {
                    return color_name;
                }

                public void setColor_name(String color_name) {
                    this.color_name = color_name;
                }

                public int getId() {
                    return id;
                }

                public void setId(int id) {
                    this.id = id;
                }

                public int getGid() {
                    return gid;
                }

                public void setGid(int gid) {
                    this.gid = gid;
                }

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public int getSku_num() {
                    return sku_num;
                }

                public void setSku_num(int sku_num) {
                    this.sku_num = sku_num;
                }

                public int getSale_num() {
                    return sale_num;
                }

                public void setSale_num(int sale_num) {
                    this.sale_num = sale_num;
                }

                public String getPrice() {
                    return price;
                }

                public void setPrice(String price) {
                    this.price = price;
                }

                public int getC_time() {
                    return c_time;
                }

                public void setC_time(int c_time) {
                    this.c_time = c_time;
                }

                public int getSize_list_id() {
                    return size_list_id;
                }

                public void setSize_list_id(int size_list_id) {
                    this.size_list_id = size_list_id;
                }

                public int getSize_id() {
                    return size_id;
                }

                public void setSize_id(int size_id) {
                    this.size_id = size_id;
                }

                public String getColor_img() {
                    return color_img;
                }

                public void setColor_img(String color_img) {
                    this.color_img = color_img;
                }

                public String getSize() {
                    return size;
                }

                public void setSize(String size) {
                    this.size = size;
                }

                public Object getSort() {
                    return sort;
                }

                public void setSort(Object sort) {
                    this.sort = sort;
                }

                public int getStatus() {
                    return status;
                }

                public void setStatus(int status) {
                    this.status = status;
                }
            }
        }
    }
}
