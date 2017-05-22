package cn.cloudworkshop.miaoding.bean;

import java.util.List;

/**
 * Author：binge on 2017-05-08 10:17
 * Email：1993911441@qq.com
 * Describe：
 */
public class NewMemberBean {

    /**
     * code : 1
     * data : {"user_info":{"avatar":"/uploads/img/2016122211012553521024.png","name":"云工场8908","phone":"17812348908","sex":0,"age":0,"credit":"6.00","birthday":null,"user_grade":{"name":"青铜会员","img":"/uploads/img/2017050801332351485410.png","img2":"/uploads/img/2017050801245252519956.png","id":1,"min_credit":"0.00","max_credit":"999.00"}},"user_privilege":[{"id":2,"name":"专属时尚顾问","img":"/uploads/img/2017021614513897551019.png","desc":"每个等级的会员都可享受云工场提供的1对1专属时尚顾问","is_get":1},{"id":3,"name":"邀请好友返利","img":"/uploads/img/2017021614582099525610.png","desc":"邀请好友，可获得相应的现金返利，详情可在云工场APP-我的-邀请有礼页面查看","is_get":1},{"id":4,"name":"会员升级礼包","img":"/uploads/img/2017021614540056521014.png","desc":"· 50点成长值\r\n· 1张全场通用券，满2000-500\r\n· 1张衬衫优惠券，满1000-200\r\n· 礼包领取有效期：\r\n  会员升级后7天内有效","is_get":3},{"id":5,"name":"急速售后服务","img":"/uploads/img/2017021614541757549799.png","desc":"享受云工场提供的7*24小时全年无休的售后服务","is_get":1},{"id":6,"name":"多重生日惊喜","img":"/uploads/img/2017021614543357100511.png","desc":"· 100点成长值 \r\n· 1张全场通用券，满1500-500 \r\n· 1张衬衫优惠券，满1000-200 \r\n· 生日周期内购物享双倍积分\r\n· 礼包领取有效期：\r\n  生日前三天及后四天内有效","is_get":2}]}
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
         * user_info : {"avatar":"/uploads/img/2016122211012553521024.png","name":"云工场8908","phone":"17812348908","sex":0,"age":0,"credit":"6.00","birthday":null,"user_grade":{"name":"青铜会员","img":"/uploads/img/2017050801332351485410.png","img2":"/uploads/img/2017050801245252519956.png","id":1,"min_credit":"0.00","max_credit":"999.00"}}
         * user_privilege : [{"id":2,"name":"专属时尚顾问","img":"/uploads/img/2017021614513897551019.png","desc":"每个等级的会员都可享受云工场提供的1对1专属时尚顾问","is_get":1},{"id":3,"name":"邀请好友返利","img":"/uploads/img/2017021614582099525610.png","desc":"邀请好友，可获得相应的现金返利，详情可在云工场APP-我的-邀请有礼页面查看","is_get":1},{"id":4,"name":"会员升级礼包","img":"/uploads/img/2017021614540056521014.png","desc":"· 50点成长值\r\n· 1张全场通用券，满2000-500\r\n· 1张衬衫优惠券，满1000-200\r\n· 礼包领取有效期：\r\n  会员升级后7天内有效","is_get":3},{"id":5,"name":"急速售后服务","img":"/uploads/img/2017021614541757549799.png","desc":"享受云工场提供的7*24小时全年无休的售后服务","is_get":1},{"id":6,"name":"多重生日惊喜","img":"/uploads/img/2017021614543357100511.png","desc":"· 100点成长值 \r\n· 1张全场通用券，满1500-500 \r\n· 1张衬衫优惠券，满1000-200 \r\n· 生日周期内购物享双倍积分\r\n· 礼包领取有效期：\r\n  生日前三天及后四天内有效","is_get":2}]
         */

        private UserInfoBean user_info;
        private List<UserPrivilegeBean> user_privilege;

        public UserInfoBean getUser_info() {
            return user_info;
        }

        public void setUser_info(UserInfoBean user_info) {
            this.user_info = user_info;
        }

        public List<UserPrivilegeBean> getUser_privilege() {
            return user_privilege;
        }

        public void setUser_privilege(List<UserPrivilegeBean> user_privilege) {
            this.user_privilege = user_privilege;
        }

        public static class UserInfoBean {
            /**
             * avatar : /uploads/img/2016122211012553521024.png
             * name : 云工场8908
             * phone : 17812348908
             * sex : 0
             * age : 0
             * credit : 6.00
             * birthday : null
             * user_grade : {"name":"青铜会员","img":"/uploads/img/2017050801332351485410.png","img2":"/uploads/img/2017050801245252519956.png","id":1,"min_credit":"0.00","max_credit":"999.00"}
             */

            private String avatar;
            private String name;
            private String phone;
            private int sex;
            private int age;
            private String credit;
            private String birthday;
            private UserGradeBean user_grade;

            public String getAvatar() {
                return avatar;
            }

            public void setAvatar(String avatar) {
                this.avatar = avatar;
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

            public int getSex() {
                return sex;
            }

            public void setSex(int sex) {
                this.sex = sex;
            }

            public int getAge() {
                return age;
            }

            public void setAge(int age) {
                this.age = age;
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

            public UserGradeBean getUser_grade() {
                return user_grade;
            }

            public void setUser_grade(UserGradeBean user_grade) {
                this.user_grade = user_grade;
            }

            public static class UserGradeBean {
                /**
                 * name : 青铜会员
                 * img : /uploads/img/2017050801332351485410.png
                 * img2 : /uploads/img/2017050801245252519956.png
                 * id : 1
                 * min_credit : 0.00
                 * max_credit : 999.00
                 */

                private String name;
                private String img;
                private String img2;
                private int id;
                private String min_credit;
                private String max_credit;

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

                public String getImg2() {
                    return img2;
                }

                public void setImg2(String img2) {
                    this.img2 = img2;
                }

                public int getId() {
                    return id;
                }

                public void setId(int id) {
                    this.id = id;
                }

                public String getMin_credit() {
                    return min_credit;
                }

                public void setMin_credit(String min_credit) {
                    this.min_credit = min_credit;
                }

                public String getMax_credit() {
                    return max_credit;
                }

                public void setMax_credit(String max_credit) {
                    this.max_credit = max_credit;
                }
            }
        }

        public static class UserPrivilegeBean {
            /**
             * id : 2
             * name : 专属时尚顾问
             * img : /uploads/img/2017021614513897551019.png
             * desc : 每个等级的会员都可享受云工场提供的1对1专属时尚顾问
             * is_get : 1
             */

            private int id;
            private String name;
            private String img;
            private String desc;
            private int is_get;

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

            public String getDesc() {
                return desc;
            }

            public void setDesc(String desc) {
                this.desc = desc;
            }

            public int getIs_get() {
                return is_get;
            }

            public void setIs_get(int is_get) {
                this.is_get = is_get;
            }
        }
    }
}
