package cn.cloudworkshop.miaoding.bean;

import java.util.List;

/**
 * Author：Libin on 2016/10/12 10:36
 * Email：1993911441@qq.com
 * Describe：
 */

public class HomepageListBean {

    /**
     * code : 1
     * data : [[{"title":"测试","img":"/uploads/img/2016101013030456545656a6.jpg","id":6,"link":"http://www.baidu.com","img_list":["/uploads/img/2016101014361749989910a7.jpg","/uploads/img/2016101014361749989910a7.jpg","/uploads/img/2016101014361749989910a7.jpg"],"p_time":"2016-10-26","name":"分类1"}],[{"title":"17","img":"/uploads/img/2016101014363450535299a8.jpg","id":2,"link":"http://www.baidu.com","img_list":["/uploads/img/2016101109594310298102a1.jpg","/uploads/img/2016101109594310210050a2.jpg","/uploads/img/2016101109594310210150a3.jpg"],"p_time":"2016-10-09","name":"分类1"},{"title":"12","img":"/uploads/img/2016101210331054509798a7.jpg","id":4,"link":"http://www.baidu.com","img_list":["/uploads/img/2016101014361749989910a7.jpg"],"p_time":"2016-10-09","name":"分类2"}],[{"title":"199","img":"/uploads/img/2016101014371054101989a5.jpg","id":1,"link":"http://www.baidu.com","img_list":["/uploads/img/2016101109594310298102a1.jpg","/uploads/img/2016101109594310210050a2.jpg","/uploads/img/2016101109594310210150a3.jpg"],"p_time":"2016-10-07","name":"分类1"}]]
     * msg : 成功
     */

    private int code;
    private String msg;
    /**
     * title : 测试
     * img : /uploads/img/2016101013030456545656a6.jpg
     * id : 6
     * link : http://www.baidu.com
     * img_list : ["/uploads/img/2016101014361749989910a7.jpg","/uploads/img/2016101014361749989910a7.jpg","/uploads/img/2016101014361749989910a7.jpg"]
     * p_time : 2016-10-26
     * name : 分类1
     */

    private List<List<DataBean>> data;

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

    public List<List<DataBean>> getData() {
        return data;
    }

    public void setData(List<List<DataBean>> data) {
        this.data = data;
    }

    public static class DataBean {
        private String title;
        private String sub_title;
        private String img;
        private int id;
        private String link;
        private String p_time;
        private String name;
        private String tag_name;
        private String uname;
        private List<String> img_list;

        public String getSub_title() {
            return sub_title;
        }

        public void setSub_title(String sub_title) {
            this.sub_title = sub_title;
        }

        public String getTag_name() {
            return tag_name;
        }

        public void setTag_name(String tag_name) {
            this.tag_name = tag_name;
        }

        public String getUname() {
            return uname;
        }

        public void setUname(String uname) {
            this.uname = uname;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
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

        public String getLink() {
            return link;
        }

        public void setLink(String link) {
            this.link = link;
        }

        public String getP_time() {
            return p_time;
        }

        public void setP_time(String p_time) {
            this.p_time = p_time;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<String> getImg_list() {
            return img_list;
        }

        public void setImg_list(List<String> img_list) {
            this.img_list = img_list;
        }
    }
}
