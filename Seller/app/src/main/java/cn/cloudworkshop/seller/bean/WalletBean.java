package cn.cloudworkshop.seller.bean;

/**
 * Author：binge on 2017-09-18 17:51
 * Email：1993911441@qq.com
 * Describe：
 */
public class WalletBean {

    /**
     * code : 1
     * msg : 获取成功
     * data : {"id":14,"name":"广州迪敖贸易","phone":"13711772175","pwd":"13711772175","c_time":1500953360,"status":1,"uname":"庄佳冰","account":"13711772175","email":"","link":"","login_ip":"","type":2,"pid":0,"factory_id":0,"money":0,"sell_money":0,"fenxiao":0,"cash":0}
     */

    private int code;
    private String msg;
    private DataBean data;

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

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 14
         * name : 广州迪敖贸易
         * phone : 13711772175
         * pwd : 13711772175
         * c_time : 1500953360
         * status : 1
         * uname : 庄佳冰
         * account : 13711772175
         * email :
         * link :
         * login_ip :
         * type : 2
         * pid : 0
         * factory_id : 0
         * money : 0
         * sell_money : 0
         * fenxiao : 0
         * cash : 0
         */

        private int id;
        private String name;
        private String phone;
        private String pwd;
        private int c_time;
        private int status;
        private String uname;
        private String account;
        private String email;
        private String link;
        private String login_ip;
        private int type;
        private int pid;
        private int factory_id;
        private int money;
        private int sell_money;
        private int fenxiao;
        private int cash;

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

        public String getPwd() {
            return pwd;
        }

        public void setPwd(String pwd) {
            this.pwd = pwd;
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

        public String getUname() {
            return uname;
        }

        public void setUname(String uname) {
            this.uname = uname;
        }

        public String getAccount() {
            return account;
        }

        public void setAccount(String account) {
            this.account = account;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getLink() {
            return link;
        }

        public void setLink(String link) {
            this.link = link;
        }

        public String getLogin_ip() {
            return login_ip;
        }

        public void setLogin_ip(String login_ip) {
            this.login_ip = login_ip;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getPid() {
            return pid;
        }

        public void setPid(int pid) {
            this.pid = pid;
        }

        public int getFactory_id() {
            return factory_id;
        }

        public void setFactory_id(int factory_id) {
            this.factory_id = factory_id;
        }

        public int getMoney() {
            return money;
        }

        public void setMoney(int money) {
            this.money = money;
        }

        public int getSell_money() {
            return sell_money;
        }

        public void setSell_money(int sell_money) {
            this.sell_money = sell_money;
        }

        public int getFenxiao() {
            return fenxiao;
        }

        public void setFenxiao(int fenxiao) {
            this.fenxiao = fenxiao;
        }

        public int getCash() {
            return cash;
        }

        public void setCash(int cash) {
            this.cash = cash;
        }
    }
}
