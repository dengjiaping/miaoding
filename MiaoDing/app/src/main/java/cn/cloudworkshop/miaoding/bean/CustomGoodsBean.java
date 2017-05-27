package cn.cloudworkshop.miaoding.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Author：Libin on 2016-10-18 13:56
 * Email：1993911441@qq.com
 * Describe：
 */

public class CustomGoodsBean {


    /**
     * code : 1
     * data : {"name":"衬衣01","img_list":["/uploads/img/20161014172609495252100072370400_2_1_1.jpg","/uploads/img/20161014172609495354540072370800_1_1_1.jpg","/uploads/img/20161014172609495457480927454982_1_1_1.jpg","/uploads/img/20161014172609495599560927454982_2_1_1.jpg","/uploads/img/20161014172609495699480927454982_2_2_1.jpg"],"view_num":100,"like_num":2522,"price":["1000","2000","3000"],"content":"　　在面料方面多采用羊绒、莱卡、丝、毛、羊皮等触感极佳的材质。颜色采用都市感强烈的黑、白、灰，加入人气高贵的紫色元素做点缀，增添了神秘感，以纯净的色彩演绎品牌风格。打破了传统的二维设计理念，采用立体流线型设计，使服装变得更修身、更简洁；并融合了经典元素，也强调适于现代男性的塑形性和舒适性。\r\n\r\n　　VLOV男装设计师品牌这几年在中国国际时装周连续举行作品发布。设计师吴青青凭借清新优雅，细致考究的设计风格为自己赢得了很多奖项和荣誉。去纽约时装周做发布，凭作品打开市场的大门，也就理所应当地成吴青青的下一步计划。\r\n\r\n　　今年2月吴青青作为中国男装设计师代表随中国国际时装周组委会代表团一行赴纽约时装周实地观摩。纽约时装周成熟的运作流程和人性化的后台管理等细节给吴青青和他的创意团队带来了诸多体会也让他们收获了颇多灵感和想法，大家对VLOV未来的纽约发布会开始充满了按捺不住的兴奋和期待。之后吴青青一行与美国设计师协会(CFDA)主席Diane Von Furstenberg女士、进行了深入的讨论，对于设计和市场运作给吴青青很大的启发，给VLOV品牌进入欧美市场代理很大的信心。同时与纽约时装周主办方IMG时尚集团负责人及工作团队进行了深入探讨。他们的专业意见和期许更是让VLOV纽约发布会显现出了从规划变成现实的清晰轮廓。","content":1,"heat":9999,"thumb":"/uploads/img/20161014172554505257560072370400_2_1_1.jpg","is_collect":-1}
     * msg : 成功
     */

    private int code;
    /**
     * name : 衬衣01
     * img_list : ["/uploads/img/20161014172609495252100072370400_2_1_1.jpg","/uploads/img/20161014172609495354540072370800_1_1_1.jpg","/uploads/img/20161014172609495457480927454982_1_1_1.jpg","/uploads/img/20161014172609495599560927454982_2_1_1.jpg","/uploads/img/20161014172609495699480927454982_2_2_1.jpg"]
     * view_num : 100
     * like_num : 2522
     * price : ["1000","2000","3000"]
     * content : 　　在面料方面多采用羊绒、莱卡、丝、毛、羊皮等触感极佳的材质。颜色采用都市感强烈的黑、白、灰，加入人气高贵的紫色元素做点缀，增添了神秘感，以纯净的色彩演绎品牌风格。打破了传统的二维设计理念，采用立体流线型设计，使服装变得更修身、更简洁；并融合了经典元素，也强调适于现代男性的塑形性和舒适性。

     　　VLOV男装设计师品牌这几年在中国国际时装周连续举行作品发布。设计师吴青青凭借清新优雅，细致考究的设计风格为自己赢得了很多奖项和荣誉。去纽约时装周做发布，凭作品打开市场的大门，也就理所应当地成吴青青的下一步计划。

     　　今年2月吴青青作为中国男装设计师代表随中国国际时装周组委会代表团一行赴纽约时装周实地观摩。纽约时装周成熟的运作流程和人性化的后台管理等细节给吴青青和他的创意团队带来了诸多体会也让他们收获了颇多灵感和想法，大家对VLOV未来的纽约发布会开始充满了按捺不住的兴奋和期待。之后吴青青一行与美国设计师协会(CFDA)主席Diane Von Furstenberg女士、进行了深入的讨论，对于设计和市场运作给吴青青很大的启发，给VLOV品牌进入欧美市场代理很大的信心。同时与纽约时装周主办方IMG时尚集团负责人及工作团队进行了深入探讨。他们的专业意见和期许更是让VLOV纽约发布会显现出了从规划变成现实的清晰轮廓。
     * content : 1
     * heat : 9999
     * thumb : /uploads/img/20161014172554505257560072370400_2_1_1.jpg
     * is_collect : -1
     */

    private DataBean data;
    private String msg;
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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
        private String name;
        private String sub_name;

        public String getSub_name() {
            return sub_name;
        }

        public void setSub_name(String sub_name) {
            this.sub_name = sub_name;
        }

        private int view_num;
        private int like_num;
        private String content;
        private String content2;
        private int type;

        public String getContent2() {
            return content2;
        }

        public void setContent2(String content2) {
            this.content2 = content2;
        }

        private int heat;
        private String thumb;
        private int is_yuyue;
        private int is_collect;
        private int classify_id;
        private ArrayList<String> img_list;
        private List<PriceBean> price;

        public List<PriceBean> getPrice() {
            return price;
        }

        public void setPrice(List<PriceBean> price) {
            this.price = price;
        }

        public int getClassify_id() {
            return classify_id;
        }

        public void setClassify_id(int classify_id) {
            this.classify_id = classify_id;
        }

        public int getIs_yuyue() {
            return is_yuyue;
        }

        public void setIs_yuyue(int is_yuyue) {
            this.is_yuyue = is_yuyue;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
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

        public int getIs_collect() {
            return is_collect;
        }

        public void setIs_collect(int is_collect) {
            this.is_collect = is_collect;
        }

        public ArrayList<String> getImg_list() {
            return img_list;
        }

        public void setImg_list(ArrayList<String> img_list) {
            this.img_list = img_list;
        }

        public static class PriceBean{
            private int id;
            private double price;
            private String introduce;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public double getPrice() {
                return price;
            }

            public void setPrice(double price) {
                this.price = price;
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
