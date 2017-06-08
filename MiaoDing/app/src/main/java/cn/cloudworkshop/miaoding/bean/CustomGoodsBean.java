package cn.cloudworkshop.miaoding.bean;

import java.util.List;

/**
 * Author：Libin on 2016-10-18 13:56
 * Email：1993911441@qq.com
 * Describe：
 */

public class CustomGoodsBean {


    /**
     * code : 1
     * data : {"name":"始终如\u201c衣\u201d","sub_name":"三百七十一个小时的匠心大作","img_list":["/uploads/img/2017052508025648569752.png","/uploads/img/2017052508025648995210.jpg","/uploads/img/2017052508025749509948.jpg","/uploads/img/2017052508025749974998.jpg"],"view_num":0,"like_num":0,"price":[{"id":2,"price":799,"introduce":"白银"},{"id":3,"price":999,"introduce":"黄金"},{"id":4,"price":1888,"introduce":"钻石"}],"content":"选择你的衬衫\n选择你的生活态度\n是将就一份不适合\n还是去定制一份专属?\n你会如何选择?\n\n自带经纬度编织扎染效果的面料，赋予衬衫简约中\n的繁复感，清新怡人的色调，适合年轻时尚男士穿\n着，柔软亲肤面料，穿着更舒适，文艺风圆领设计，\n更时尚。\n\n面料成份:100%亚麻。","content2":"/uploads/img/2017052508053310049102.jpg","type":1,"heat":3862,"thumb":"/uploads/img/2017060517293951515557.jpg","classify_id":1,"uid":0,"default_spec_content":"领子: 带扣尖领;领子: 带扣尖领","default_spec_ids":"348,350","default_mianliao":1,"designer":null,"default_spec_list":[{"id":348,"gid":null,"spec_id":2,"mianliao_id":1,"name":"圆摆","type":0,"img_a":"/uploads/img/2016122916520957534954.png","img_b":"/uploads/img/2016122916523110297529.png","img_c":"/uploads/img/2017010216243248971019.png","introduce":null,"spec_name":"下摆","position_id":1},{"id":350,"gid":null,"spec_id":1,"mianliao_id":1,"name":" 带扣尖领","type":0,"img_a":"/uploads/img/2016111516165998519952.png","img_b":"/uploads/img/2016111516170751531001.png","img_c":"/uploads/img/2016111516152610149535.png","introduce":null,"spec_name":"领子","position_id":1}],"banxing_list":[{"id":1,"name":"修身"},{"id":2,"name":"宽松"},{"id":3,"name":"合适"}],"default_price":399,"is_collect":1,"is_yuyue":0}
     * msg : 成功
     * id : 397
     */

    private int code;
    private DataBean data;
    private String msg;
    private String id;

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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public static class DataBean {
        /**
         * name : 始终如“衣”
         * sub_name : 三百七十一个小时的匠心大作
         * img_list : ["/uploads/img/2017052508025648569752.png","/uploads/img/2017052508025648995210.jpg","/uploads/img/2017052508025749509948.jpg","/uploads/img/2017052508025749974998.jpg"]
         * view_num : 0
         * like_num : 0
         * price : [{"id":2,"price":799,"introduce":"白银"},{"id":3,"price":999,"introduce":"黄金"},{"id":4,"price":1888,"introduce":"钻石"}]
         * content : 选择你的衬衫
         选择你的生活态度
         是将就一份不适合
         还是去定制一份专属?
         你会如何选择?

         自带经纬度编织扎染效果的面料，赋予衬衫简约中
         的繁复感，清新怡人的色调，适合年轻时尚男士穿
         着，柔软亲肤面料，穿着更舒适，文艺风圆领设计，
         更时尚。

         面料成份:100%亚麻。
         * content2 : /uploads/img/2017052508053310049102.jpg
         * type : 1
         * heat : 3862
         * thumb : /uploads/img/2017060517293951515557.jpg
         * classify_id : 1
         * uid : 0
         * default_spec_content : 领子: 带扣尖领;领子: 带扣尖领
         * default_spec_ids : 348,350
         * default_mianliao : 1
         * designer : null
         * default_spec_list : [{"id":348,"gid":null,"spec_id":2,"mianliao_id":1,"name":"圆摆","type":0,"img_a":"/uploads/img/2016122916520957534954.png","img_b":"/uploads/img/2016122916523110297529.png","img_c":"/uploads/img/2017010216243248971019.png","introduce":null,"spec_name":"下摆","position_id":1},{"id":350,"gid":null,"spec_id":1,"mianliao_id":1,"name":" 带扣尖领","type":0,"img_a":"/uploads/img/2016111516165998519952.png","img_b":"/uploads/img/2016111516170751531001.png","img_c":"/uploads/img/2016111516152610149535.png","introduce":null,"spec_name":"领子","position_id":1}]
         * banxing_list : [{"id":1,"name":"修身"},{"id":2,"name":"宽松"},{"id":3,"name":"合适"}]
         * default_price : 399
         * is_collect : 1
         * is_yuyue : 0
         */

        private String name;
        private String sub_name;
        private int view_num;
        private int like_num;
        private String content;
        private String content2;
        private int type;
        private int heat;
        private String thumb;
        private int classify_id;
        private int uid;
        private String default_spec_content;
        private String default_spec_ids;
        private int default_mianliao;
        private Object designer;
        private int default_price;
        private int is_collect;
        private int is_yuyue;
        private List<String> img_list;
        private List<PriceBean> price;
        private List<DefaultSpecListBean> default_spec_list;
        private List<BanxingListBean> banxing_list;
        private  int price_type;

        public int getPrice_type() {
            return price_type;
        }

        public void setPrice_type(int price_type) {
            this.price_type = price_type;
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

        public String getContent2() {
            return content2;
        }

        public void setContent2(String content2) {
            this.content2 = content2;
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

        public String getDefault_spec_content() {
            return default_spec_content;
        }

        public void setDefault_spec_content(String default_spec_content) {
            this.default_spec_content = default_spec_content;
        }

        public String getDefault_spec_ids() {
            return default_spec_ids;
        }

        public void setDefault_spec_ids(String default_spec_ids) {
            this.default_spec_ids = default_spec_ids;
        }

        public int getDefault_mianliao() {
            return default_mianliao;
        }

        public void setDefault_mianliao(int default_mianliao) {
            this.default_mianliao = default_mianliao;
        }

        public Object getDesigner() {
            return designer;
        }

        public void setDesigner(Object designer) {
            this.designer = designer;
        }

        public int getDefault_price() {
            return default_price;
        }

        public void setDefault_price(int default_price) {
            this.default_price = default_price;
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

        public List<PriceBean> getPrice() {
            return price;
        }

        public void setPrice(List<PriceBean> price) {
            this.price = price;
        }

        public List<DefaultSpecListBean> getDefault_spec_list() {
            return default_spec_list;
        }

        public void setDefault_spec_list(List<DefaultSpecListBean> default_spec_list) {
            this.default_spec_list = default_spec_list;
        }

        public List<BanxingListBean> getBanxing_list() {
            return banxing_list;
        }

        public void setBanxing_list(List<BanxingListBean> banxing_list) {
            this.banxing_list = banxing_list;
        }

        public static class PriceBean {
            /**
             * id : 2
             * price : 799
             * introduce : 白银
             */

            private int id;
            private int price;
            private String introduce;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public int getPrice() {
                return price;
            }

            public void setPrice(int price) {
                this.price = price;
            }

            public String getIntroduce() {
                return introduce;
            }

            public void setIntroduce(String introduce) {
                this.introduce = introduce;
            }
        }

        public static class DefaultSpecListBean {
            /**
             * id : 348
             * gid : null
             * spec_id : 2
             * mianliao_id : 1
             * name : 圆摆
             * type : 0
             * img_a : /uploads/img/2016122916520957534954.png
             * img_b : /uploads/img/2016122916523110297529.png
             * img_c : /uploads/img/2017010216243248971019.png
             * introduce : null
             * spec_name : 下摆
             * position_id : 1
             */

            private int id;
            private Object gid;
            private int spec_id;
            private int mianliao_id;
            private String name;
            private int type;
            private String img_a;
            private String img_b;
            private String img_c;
            private Object introduce;
            private String spec_name;
            private int position_id;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public Object getGid() {
                return gid;
            }

            public void setGid(Object gid) {
                this.gid = gid;
            }

            public int getSpec_id() {
                return spec_id;
            }

            public void setSpec_id(int spec_id) {
                this.spec_id = spec_id;
            }

            public int getMianliao_id() {
                return mianliao_id;
            }

            public void setMianliao_id(int mianliao_id) {
                this.mianliao_id = mianliao_id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }

            public String getImg_a() {
                return img_a;
            }

            public void setImg_a(String img_a) {
                this.img_a = img_a;
            }

            public String getImg_b() {
                return img_b;
            }

            public void setImg_b(String img_b) {
                this.img_b = img_b;
            }

            public String getImg_c() {
                return img_c;
            }

            public void setImg_c(String img_c) {
                this.img_c = img_c;
            }

            public Object getIntroduce() {
                return introduce;
            }

            public void setIntroduce(Object introduce) {
                this.introduce = introduce;
            }

            public String getSpec_name() {
                return spec_name;
            }

            public void setSpec_name(String spec_name) {
                this.spec_name = spec_name;
            }

            public int getPosition_id() {
                return position_id;
            }

            public void setPosition_id(int position_id) {
                this.position_id = position_id;
            }
        }

        public static class BanxingListBean {
            /**
             * id : 1
             * name : 修身
             */

            private int id;
            private String name;

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
        }
    }
}
