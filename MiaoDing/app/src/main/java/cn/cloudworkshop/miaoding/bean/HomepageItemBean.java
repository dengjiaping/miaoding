package cn.cloudworkshop.miaoding.bean;

import java.util.List;

/**
 * Created by Administrator on 2016/10/22.
 */

public class HomepageItemBean {

    public String img;
    public String link;
    public String time;
    public List<String> imgList;
    public String title;
    public String type;
    public String content;
    public int id;

    public HomepageItemBean(String img, String link, String time, List<String> imgList, String title,
                            String type, String content, int id) {
        this.img = img;
        this.link = link;
        this.time = time;
        this.imgList = imgList;
        this.title = title;
        this.type = type;
        this.content = content;
        this.id = id;
    }
}
