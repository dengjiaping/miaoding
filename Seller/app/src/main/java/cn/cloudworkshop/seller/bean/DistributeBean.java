package cn.cloudworkshop.seller.bean;

/**
 * Author：binge on 2017-09-20 13:48
 * Email：1993911441@qq.com
 * Describe：
 */
public class DistributeBean {

    /**
     * code : 1
     * msg : 获取成功
     * data : {"user_num":0,"reward_money":0}
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
         * user_num : 0
         * reward_money : 0
         */

        private int user_num;
        private int reward_money;

        public int getUser_num() {
            return user_num;
        }

        public void setUser_num(int user_num) {
            this.user_num = user_num;
        }

        public int getReward_money() {
            return reward_money;
        }

        public void setReward_money(int reward_money) {
            this.reward_money = reward_money;
        }
    }
}
