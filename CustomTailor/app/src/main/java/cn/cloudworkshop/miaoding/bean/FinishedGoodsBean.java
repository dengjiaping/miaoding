package cn.cloudworkshop.miaoding.bean;

import java.util.List;

/**
 * Author：Libin on 2016/10/26 10:50
 * Email：1993911441@qq.com
 * Describe：
 */
public class FinishedGoodsBean {

    /**
     * code : 1
     * data : {"name":"【云工场&杜嘉班纳】 2017春夏新款","sub_name":"东西方文化的碰撞","img_list":[""],"view_num":0,"like_num":0,"price":[],"content":"杜嘉班纳公司创立于1985年，总部位于意大利米兰。今天已成为在奢侈品领域中最主要的国际集团之一。","type":2,"heat":88,"thumb":"/uploads/img/2017042411432510057545.jpg","classify_id":1,"size_list":[{"id":1,"name":"S","sku_num":117,"sale_num":6,"price":"110.00","size_name":"S","size_list":[{"id":1,"gid":49,"name":"S","sku_num":117,"sale_num":6,"price":"110.00","c_time":1493101758,"size_list_id":0,"size_id":0,"color_img":"/uploads/img/2016102811045757495157.png","size":"S","sort":1,"status":1}]},{"id":5,"name":"L","sku_num":213,"sale_num":0,"price":"123.00","size_name":"L","size_list":[{"id":2,"gid":49,"name":"L","sku_num":213,"sale_num":0,"price":"123.00","c_time":1493101776,"size_list_id":0,"size_id":0,"color_img":"/uploads/img/2016102811051054101101.png","size":"L","sort":1,"status":1},{"id":5,"gid":49,"name":"L","sku_num":213,"sale_num":0,"price":"123.00","c_time":1493112525,"size_list_id":0,"size_id":0,"color_img":"/uploads/img/2016102811053510254495.png","size":"L","sort":1,"status":1}]},{"id":3,"name":"XL","sku_num":123,"sale_num":0,"price":"112.00","size_name":"XL","size_list":[{"id":3,"gid":49,"name":"XL","sku_num":123,"sale_num":0,"price":"112.00","c_time":1493101796,"size_list_id":0,"size_id":0,"color_img":"/uploads/img/2016102811051810197511.png","size":"XL","sort":1,"status":1}]},{"id":4,"name":"XXL","sku_num":344,"sale_num":0,"price":"113.00","size_name":"XXL","size_list":[{"id":4,"gid":49,"name":"XXL","sku_num":344,"sale_num":0,"price":"113.00","c_time":1493101889,"size_list_id":0,"size_id":0,"color_img":"/uploads/img/2016102811053510254495.png","size":"XXL","sort":1,"status":1}]},{"id":6,"name":"XL","sku_num":242,"sale_num":0,"price":"123.00","size_name":"XL","size_list":[{"id":6,"gid":49,"name":"XL","sku_num":242,"sale_num":0,"price":"123.00","c_time":1493112559,"size_list_id":0,"size_id":0,"color_img":"/uploads/img/2016102811053510254495.png","size":"XL","sort":1,"status":1}]}],"is_collect":1,"is_yuyue":1}
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
         * img_list : [""]
         * view_num : 0
         * like_num : 0
         * price : []
         * content : 杜嘉班纳公司创立于1985年，总部位于意大利米兰。今天已成为在奢侈品领域中最主要的国际集团之一。
         * type : 2
         * heat : 88
         * thumb : /uploads/img/2017042411432510057545.jpg
         * classify_id : 1
         * size_list : [{"id":1,"name":"S","sku_num":117,"sale_num":6,"price":"110.00","size_name":"S","size_list":[{"id":1,"gid":49,"name":"S","sku_num":117,"sale_num":6,"price":"110.00","c_time":1493101758,"size_list_id":0,"size_id":0,"color_img":"/uploads/img/2016102811045757495157.png","size":"S","sort":1,"status":1}]},{"id":5,"name":"L","sku_num":213,"sale_num":0,"price":"123.00","size_name":"L","size_list":[{"id":2,"gid":49,"name":"L","sku_num":213,"sale_num":0,"price":"123.00","c_time":1493101776,"size_list_id":0,"size_id":0,"color_img":"/uploads/img/2016102811051054101101.png","size":"L","sort":1,"status":1},{"id":5,"gid":49,"name":"L","sku_num":213,"sale_num":0,"price":"123.00","c_time":1493112525,"size_list_id":0,"size_id":0,"color_img":"/uploads/img/2016102811053510254495.png","size":"L","sort":1,"status":1}]},{"id":3,"name":"XL","sku_num":123,"sale_num":0,"price":"112.00","size_name":"XL","size_list":[{"id":3,"gid":49,"name":"XL","sku_num":123,"sale_num":0,"price":"112.00","c_time":1493101796,"size_list_id":0,"size_id":0,"color_img":"/uploads/img/2016102811051810197511.png","size":"XL","sort":1,"status":1}]},{"id":4,"name":"XXL","sku_num":344,"sale_num":0,"price":"113.00","size_name":"XXL","size_list":[{"id":4,"gid":49,"name":"XXL","sku_num":344,"sale_num":0,"price":"113.00","c_time":1493101889,"size_list_id":0,"size_id":0,"color_img":"/uploads/img/2016102811053510254495.png","size":"XXL","sort":1,"status":1}]},{"id":6,"name":"XL","sku_num":242,"sale_num":0,"price":"123.00","size_name":"XL","size_list":[{"id":6,"gid":49,"name":"XL","sku_num":242,"sale_num":0,"price":"123.00","c_time":1493112559,"size_list_id":0,"size_id":0,"color_img":"/uploads/img/2016102811053510254495.png","size":"XL","sort":1,"status":1}]}]
         * is_collect : 1
         * is_yuyue : 1
         */

        private String name;
        private String sub_name;
        private int view_num;
        private int like_num;
        private String content;
        private int type;
        private int heat;
        private String thumb;
        private int classify_id;
        private int is_collect;
        private int is_yuyue;
        private List<String> img_list;
        private List<?> price;
        private List<SizeListBean> size_list;

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

        public List<?> getPrice() {
            return price;
        }

        public void setPrice(List<?> price) {
            this.price = price;
        }

        public List<SizeListBean> getSize_list() {
            return size_list;
        }

        public void setSize_list(List<SizeListBean> size_list) {
            this.size_list = size_list;
        }

        public static class SizeListBean {
            /**
             * id : 1
             * name : S
             * sku_num : 117
             * sale_num : 6
             * price : 110.00
             * size_name : S
             * size_list : [{"id":1,"gid":49,"name":"S","sku_num":117,"sale_num":6,"price":"110.00","c_time":1493101758,"size_list_id":0,"size_id":0,"color_img":"/uploads/img/2016102811045757495157.png","size":"S","sort":1,"status":1}]
             */

            private int id;
            private String name;
            private int sku_num;
            private int sale_num;
            private String price;
            private String size_name;
            private List<ItemListBean> size_list;

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

            public List<ItemListBean> getSize_list() {
                return size_list;
            }

            public void setSize_list(List<ItemListBean> size_list) {
                this.size_list = size_list;
            }

            public static class ItemListBean {
                /**
                 * id : 1
                 * gid : 49
                 * name : S
                 * sku_num : 117
                 * sale_num : 6
                 * price : 110.00
                 * c_time : 1493101758
                 * size_list_id : 0
                 * size_id : 0
                 * color_img : /uploads/img/2016102811045757495157.png
                 * size : S
                 * sort : 1
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
                private String size;
                private int sort;
                private int status;

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

                public int getSort() {
                    return sort;
                }

                public void setSort(int sort) {
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
