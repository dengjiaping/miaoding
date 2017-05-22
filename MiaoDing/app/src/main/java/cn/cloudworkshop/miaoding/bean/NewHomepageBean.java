package cn.cloudworkshop.miaoding.bean;

import java.util.List;

/**
 * Author：binge on 2017-04-28 18:14
 * Email：1993911441@qq.com
 * Describe：
 */
public class NewHomepageBean {

    /**
     * code : 1
     * data : [[{"title":"没有吴亦凡的高颜值 就学他的好衣品吧","sub_title":null,"img":"/uploads/img/2016123018170099505252.png","id":11,"link":null,"img_list":["/uploads/img/2016122718552398555156.png","/uploads/img/2016122718552848100995.png","/uploads/img/2016122718553252975651.png"],"p_time":"- DEC.27 -","view_nums":0,"name":"推荐","uname":"云小编"}]]
     * designer_list : [{"id":79,"name":"Eric","phone":"13916905124","pwd":null,"nickname":"","avatar":"/uploads/img/2017041314512157481021.jpg","sex":1,"reg_time":1482836170,"status":1,"type":2,"remark":"杜嘉班纳公司创立于1985年，总部位于意大利米兰。今天已成为在奢侈品领域中最主要的国际集团之一。","age":0,"c_time":0,"a_id":0,"b_id":0,"group_id":1,"invite_ewm":null,"award_money":"0.00","tx_money":"0.00","reg_ip":null,"xf_money":"0.00","credit":"44.00","birthday":"2017-02-28","tag":"爸爸","introduce":"杜嘉班纳公司创立于1985年，"},{"id":257,"name":"黄梦炜","phone":"17012348909","pwd":null,"nickname":"","avatar":"/uploads/img/2017042809461151981015.jpg","sex":1,"reg_time":null,"status":1,"type":2,"remark":"热特瑞","age":0,"c_time":1493372778,"a_id":0,"b_id":0,"group_id":1,"invite_ewm":null,"award_money":"0.00","tx_money":"0.00","reg_ip":null,"xf_money":"0.00","credit":"0.00","birthday":null,"tag":"失恋阵线联盟","introduce":"罚单还感觉很尴尬"}]
     * lunbo : [{"img":"/uploads/img/2016101715541897515149","link":"","title":"1","share_link":""},{"img":"/uploads/img/2016101716465850555498","link":"","title":"设计师入住","share_link":""},{"img":"/uploads/img/2016101716475054525010","link":"","title":"设计师入住","share_link":""},{"img":"/uploads/img/2016101716485050545249","link":"","title":"设计师入住","share_link":""}]
     * msg : 成功
     */

    private int code;
    private String msg;
    private List<List<DataBean>> data;
    private List<DesignerListBean> designer_list;
    private List<LunboBean> lunbo;

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

    public List<DesignerListBean> getDesigner_list() {
        return designer_list;
    }

    public void setDesigner_list(List<DesignerListBean> designer_list) {
        this.designer_list = designer_list;
    }

    public List<LunboBean> getLunbo() {
        return lunbo;
    }

    public void setLunbo(List<LunboBean> lunbo) {
        this.lunbo = lunbo;
    }

    public static class DataBean {
        /**
         * title : 没有吴亦凡的高颜值 就学他的好衣品吧
         * sub_title : null
         * img : /uploads/img/2016123018170099505252.png
         * id : 11
         * link : null
         * img_list : ["/uploads/img/2016122718552398555156.png","/uploads/img/2016122718552848100995.png","/uploads/img/2016122718553252975651.png"]
         * p_time : - DEC.27 -
         * view_nums : 0
         * name : 推荐
         * uname : 云小编
         */

        private String title;
        private String sub_title;
        private String img;
        private int id;
        private String link;
        private String p_time;
        private int view_nums;

        public String getTag_name() {
            return tag_name;
        }

        public void setTag_name(String tag_name) {
            this.tag_name = tag_name;
        }

        private String name;
        private String uname;
        private String tag_name;
        private List<String> img_list;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getSub_title() {
            return sub_title;
        }

        public void setSub_title(String sub_title) {
            this.sub_title = sub_title;
        }

        public String getLink() {
            return link;
        }

        public void setLink(String link) {
            this.link = link;
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



        public String getP_time() {
            return p_time;
        }

        public void setP_time(String p_time) {
            this.p_time = p_time;
        }

        public int getView_nums() {
            return view_nums;
        }

        public void setView_nums(int view_nums) {
            this.view_nums = view_nums;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getUname() {
            return uname;
        }

        public void setUname(String uname) {
            this.uname = uname;
        }

        public List<String> getImg_list() {
            return img_list;
        }

        public void setImg_list(List<String> img_list) {
            this.img_list = img_list;
        }
    }

    public static class DesignerListBean {
        /**
         * id : 79
         * name : Eric
         * phone : 13916905124
         * pwd : null
         * nickname :
         * avatar : /uploads/img/2017041314512157481021.jpg
         * sex : 1
         * reg_time : 1482836170
         * status : 1
         * type : 2
         * remark : 杜嘉班纳公司创立于1985年，总部位于意大利米兰。今天已成为在奢侈品领域中最主要的国际集团之一。
         * age : 0
         * c_time : 0
         * a_id : 0
         * b_id : 0
         * group_id : 1
         * invite_ewm : null
         * award_money : 0.00
         * tx_money : 0.00
         * reg_ip : null
         * xf_money : 0.00
         * credit : 44.00
         * birthday : 2017-02-28
         * tag : 爸爸
         * introduce : 杜嘉班纳公司创立于1985年，
         */

        private int id;
        private String name;
        private String phone;
        private Object pwd;
        private String nickname;
        private String avatar;
        private int sex;
        private int reg_time;
        private int status;
        private int type;
        private String remark;
        private int age;
        private int c_time;
        private int a_id;
        private int b_id;
        private int group_id;
        private Object invite_ewm;
        private String award_money;
        private String tx_money;
        private Object reg_ip;
        private String xf_money;
        private String credit;
        private String birthday;
        private String tag;
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

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public Object getPwd() {
            return pwd;
        }

        public void setPwd(Object pwd) {
            this.pwd = pwd;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public int getSex() {
            return sex;
        }

        public void setSex(int sex) {
            this.sex = sex;
        }

        public int getReg_time() {
            return reg_time;
        }

        public void setReg_time(int reg_time) {
            this.reg_time = reg_time;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public int getC_time() {
            return c_time;
        }

        public void setC_time(int c_time) {
            this.c_time = c_time;
        }

        public int getA_id() {
            return a_id;
        }

        public void setA_id(int a_id) {
            this.a_id = a_id;
        }

        public int getB_id() {
            return b_id;
        }

        public void setB_id(int b_id) {
            this.b_id = b_id;
        }

        public int getGroup_id() {
            return group_id;
        }

        public void setGroup_id(int group_id) {
            this.group_id = group_id;
        }

        public Object getInvite_ewm() {
            return invite_ewm;
        }

        public void setInvite_ewm(Object invite_ewm) {
            this.invite_ewm = invite_ewm;
        }

        public String getAward_money() {
            return award_money;
        }

        public void setAward_money(String award_money) {
            this.award_money = award_money;
        }

        public String getTx_money() {
            return tx_money;
        }

        public void setTx_money(String tx_money) {
            this.tx_money = tx_money;
        }

        public Object getReg_ip() {
            return reg_ip;
        }

        public void setReg_ip(Object reg_ip) {
            this.reg_ip = reg_ip;
        }

        public String getXf_money() {
            return xf_money;
        }

        public void setXf_money(String xf_money) {
            this.xf_money = xf_money;
        }

        public String getCredit() {
            return credit;
        }

        public void setCredit(String credit) {
            this.credit = credit;
        }

        public String getBirthday() {
            return birthday;
        }

        public void setBirthday(String birthday) {
            this.birthday = birthday;
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

    public static class LunboBean {
        /**
         * img : /uploads/img/2016101715541897515149
         * link :
         * title : 1
         * share_link :
         */

        private String img;
        private String link;
        private String title;
        private String share_link;

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

        public String getShare_link() {
            return share_link;
        }

        public void setShare_link(String share_link) {
            this.share_link = share_link;
        }
    }
}
