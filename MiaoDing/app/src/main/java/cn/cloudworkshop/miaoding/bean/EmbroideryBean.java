package cn.cloudworkshop.miaoding.bean;

import java.util.List;

/**
 * Author：Libin on 2016/10/25 12:55
 * Email：1993911441@qq.com
 * Describe：
 */
public class EmbroideryBean {


    /**
     * code : 1
     * data : {"currentPos":[{"id":1,"name":"右袖口","img":"/uploads/img/2016102809545510299575.png","a_name":"位置"},{"id":2,"name":"左袖口","img":"/uploads/img/2016102809552610150501.png","a_name":"位置"},{"id":4,"name":"三角布","img":"/uploads/img/2016102809554110051102.png","a_name":"位置"},{"id":5,"name":"口袋","img":"/uploads/img/2016102809560755974951.png","a_name":"位置"}],"font":[{"name":"英文","img":"/uploads/img/2016102810582953545157.png","id":1,"a_name":"字体"},{"name":"中文","img":"/uploads/img/2016102810584048100100.png","id":2,"a_name":"字体"}],"color":[{"name":"蓝色","img":"/uploads/img/2016102811045757495157.png","id":1,"a_name":"颜色"},{"name":"红色","img":"/uploads/img/2016102811051054101101.png","id":2,"a_name":"颜色"},{"name":"墨绿色","img":"/uploads/img/2016102811053510254495.png","id":4,"a_name":"颜色"},{"name":"淡色","img":"/uploads/img/2016102811051810197511.png","id":3,"a_name":"颜色"}],"spec_templets_recommend":[{"name":"","spec_ids":"345,348,350,359,407,423","content":1,"list":[{"id":345,"name":"暗门襟","img_a":"/uploads/img/2016111515552499535698.png","img_b":"/uploads/img/2016111515563450995249.png","img_c":"/uploads/img/2016111515574697529852.png","position_id":1,"spec_id":9,"spec_name":"门襟"},{"id":348,"name":"圆摆","img_a":"/uploads/img/2016122916491810149515.png","img_b":"/uploads/img/2016122916495248495356.png","img_c":"/uploads/img/2017010216234953979799.png","position_id":1,"spec_id":2,"spec_name":"下摆"},{"id":350,"name":" 带扣尖领","img_a":"/uploads/img/2016111516144452974910.png","img_b":"/uploads/img/2016111516150910055485.png","img_c":"/uploads/img/2016111516152949100559.png","position_id":1,"spec_id":1,"spec_name":"领子"},{"id":359,"name":"腰省","img_a":"/uploads/img/2016111517013850565097.png","img_b":"/uploads/img/2016111517015450525256.png","img_c":"/uploads/img/2017010216283854481025.png","position_id":2,"spec_id":7,"spec_name":"后背"},{"id":407,"name":"三角口袋","img_a":"/uploads/img/2016111609285998521025.png","img_b":"/uploads/img/2016111609312157995054.png","img_c":"/uploads/img/2016111609324610151545.png","position_id":1,"spec_id":4,"spec_name":"口袋"},{"id":423,"name":"单袖头六角两粒扣","img_a":"/uploads/img/2016111609421998100100.png","img_b":"/uploads/img/2016111609423598555097.png","img_c":"/uploads/img/2016123010065749575749.png","position_id":2,"spec_id":5,"spec_name":"袖口"}]}]}
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
        private List<PositionBean> position;
        private List<FontBean> font;
        private List<ColorBean> color;
        private List<SpecTempletsRecommendBean> spec_templets_recommend;

        public List<PositionBean> getPosition() {
            return position;
        }

        public void setPosition(List<PositionBean> position) {
            this.position = position;
        }

        public List<FontBean> getFont() {
            return font;
        }

        public void setFont(List<FontBean> font) {
            this.font = font;
        }

        public List<ColorBean> getColor() {
            return color;
        }

        public void setColor(List<ColorBean> color) {
            this.color = color;
        }


        public static class PositionBean {
            /**
             * id : 1
             * name : 右袖口
             * img : /uploads/img/2016102809545510299575.png
             * a_name : 位置
             */

            private int id;
            private String name;
            private String img;
            private String a_name;

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

            public String getImg() {
                return img;
            }

            public void setImg(String img) {
                this.img = img;
            }

            public String getA_name() {
                return a_name;
            }

            public void setA_name(String a_name) {
                this.a_name = a_name;
            }
        }

        public static class FontBean {
            /**
             * name : 英文
             * img : /uploads/img/2016102810582953545157.png
             * id : 1
             * a_name : 字体
             */

            private String name;
            private String img;
            private int id;
            private String a_name;
            private int is_english;

            public int getIs_english() {
                return is_english;
            }

            public void setIs_english(int is_english) {
                this.is_english = is_english;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getImg() {
                return img;
            }

            public void setImg(String img) {
                this.img = img;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getA_name() {
                return a_name;
            }

            public void setA_name(String a_name) {
                this.a_name = a_name;
            }
        }

        public static class ColorBean {
            /**
             * name : 蓝色
             * img : /uploads/img/2016102811045757495157.png
             * id : 1
             * a_name : 颜色
             */

            private String name;
            private String img;
            private int id;
            private String a_name;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getImg() {
                return img;
            }

            public void setImg(String img) {
                this.img = img;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getA_name() {
                return a_name;
            }

            public void setA_name(String a_name) {
                this.a_name = a_name;
            }
        }

        public static class SpecTempletsRecommendBean {
            /**
             * name :
             * spec_ids : 345,348,350,359,407,423
             * content : 1
             * list : [{"id":345,"name":"暗门襟","img_a":"/uploads/img/2016111515552499535698.png","img_b":"/uploads/img/2016111515563450995249.png","img_c":"/uploads/img/2016111515574697529852.png","position_id":1,"spec_id":9,"spec_name":"门襟"},{"id":348,"name":"圆摆","img_a":"/uploads/img/2016122916491810149515.png","img_b":"/uploads/img/2016122916495248495356.png","img_c":"/uploads/img/2017010216234953979799.png","position_id":1,"spec_id":2,"spec_name":"下摆"},{"id":350,"name":" 带扣尖领","img_a":"/uploads/img/2016111516144452974910.png","img_b":"/uploads/img/2016111516150910055485.png","img_c":"/uploads/img/2016111516152949100559.png","position_id":1,"spec_id":1,"spec_name":"领子"},{"id":359,"name":"腰省","img_a":"/uploads/img/2016111517013850565097.png","img_b":"/uploads/img/2016111517015450525256.png","img_c":"/uploads/img/2017010216283854481025.png","position_id":2,"spec_id":7,"spec_name":"后背"},{"id":407,"name":"三角口袋","img_a":"/uploads/img/2016111609285998521025.png","img_b":"/uploads/img/2016111609312157995054.png","img_c":"/uploads/img/2016111609324610151545.png","position_id":1,"spec_id":4,"spec_name":"口袋"},{"id":423,"name":"单袖头六角两粒扣","img_a":"/uploads/img/2016111609421998100100.png","img_b":"/uploads/img/2016111609423598555097.png","img_c":"/uploads/img/2016123010065749575749.png","position_id":2,"spec_id":5,"spec_name":"袖口"}]
             */

            private String name;
            private String spec_ids;
            private int type;
            private List<ListBean> list;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getSpec_ids() {
                return spec_ids;
            }

            public void setSpec_ids(String spec_ids) {
                this.spec_ids = spec_ids;
            }

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }

            public List<ListBean> getList() {
                return list;
            }

            public void setList(List<ListBean> list) {
                this.list = list;
            }

            public static class ListBean {
                /**
                 * id : 345
                 * name : 暗门襟
                 * img_a : /uploads/img/2016111515552499535698.png
                 * img_b : /uploads/img/2016111515563450995249.png
                 * img_c : /uploads/img/2016111515574697529852.png
                 * position_id : 1
                 * spec_id : 9
                 * spec_name : 门襟
                 */

                private int id;
                private String name;
                private String img_a;
                private String img_b;
                private String img_c;
                private int position_id;
                private int spec_id;
                private String spec_name;

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

                public int getPosition_id() {
                    return position_id;
                }

                public void setPosition_id(int position_id) {
                    this.position_id = position_id;
                }

                public int getSpec_id() {
                    return spec_id;
                }

                public void setSpec_id(int spec_id) {
                    this.spec_id = spec_id;
                }

                public String getSpec_name() {
                    return spec_name;
                }

                public void setSpec_name(String spec_name) {
                    this.spec_name = spec_name;
                }
            }
        }
    }
}
