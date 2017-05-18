package cn.cloudworkshop.miaoding.bean;

import java.util.List;

/**
 * Author：Libin on 2016/10/10 14:58
 * Email：1993911441@qq.com
 * Describe：
 */

public class BannerBean {

    /**
     * code : 1
     * data : [{"img":"/uploads/img/2016110216175955495597.png","link":"http://www.cloudworkshop.cn/web/jquery-obj/static/web/html/designer.html?type=1&id=2","title":"图片01"},{"img":"/uploads/img/2016111921414052101545.jpg","link":"http://www.cloudworkshop.cn/web/jquery-obj/static/web/html/designer.html?type=1&id=2","title":""},{"img":"/uploads/img/2016110216183699515051.png","link":"http://www.cloudworkshop.cn/web/jquery-obj/static/web/html/designer.html?type=1&id=2","title":""}]
     * msg : 成功
     */

    private int code;
    private String msg;
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
        /**
         * img : /uploads/img/2016110216175955495597.png
         * link : http://www.cloudworkshop.cn/web/jquery-obj/static/web/html/designer.html?type=1&id=2
         * title : 图片01
         */

        private String img;
        private String link;
        private String title;
        private String share_link;

        public String getShare_link() {
            return share_link;
        }

        public void setShare_link(String share_link) {
            this.share_link = share_link;
        }

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }

        public String getLink() {
            return link;
        }

        public void setLink(String link) {
            this.link = link;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }
}
