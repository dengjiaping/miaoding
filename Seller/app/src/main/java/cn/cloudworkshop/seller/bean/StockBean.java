package cn.cloudworkshop.seller.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Author：binge on 2017-09-19 15:14
 * Email：1993911441@qq.com
 * Describe：
 */
public class StockBean implements Serializable{

    /**
     * code : 1
     * msg : 获取成功
     * list : {"total":1,"per_page":10,"current_page":1,"data":[{"id":4,"storage":"1231","use_up":"11","name":"测试","pinhao":"121"}]}
     */

    private int code;
    private String msg;
    private ListBean list;
    private int type;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

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

    public ListBean getList() {
        return list;
    }

    public void setList(ListBean list) {
        this.list = list;
    }

    public static class ListBean {
        /**
         * total : 1
         * per_page : 10
         * current_page : 1
         * data : [{"id":4,"storage":"1231","use_up":"11","name":"测试","pinhao":"121"}]
         */

        private int total;
        private int per_page;
        private int current_page;
        private List<DataBean> data;

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public int getPer_page() {
            return per_page;
        }

        public void setPer_page(int per_page) {
            this.per_page = per_page;
        }

        public int getCurrent_page() {
            return current_page;
        }

        public void setCurrent_page(int current_page) {
            this.current_page = current_page;
        }

        public List<DataBean> getData() {
            return data;
        }

        public void setData(List<DataBean> data) {
            this.data = data;
        }

        public static class DataBean implements Serializable{
            /**
             * id : 4
             * storage : 1231
             * use_up : 11
             * name : 测试
             * pinhao : 121
             */

            private int id;
            private String storage;
            private String use_up;
            private String name;
            private String pinhao;
            private String introduce;

            public String getIntroduce() {
                return introduce;
            }

            public void setIntroduce(String introduce) {
                this.introduce = introduce;
            }

            public String getImg_max() {
                return img_max;
            }

            public void setImg_max(String img_max) {
                this.img_max = img_max;
            }

            public String getImg_min() {
                return img_min;
            }

            public void setImg_min(String img_min) {
                this.img_min = img_min;
            }

            private String img_max;
            private String img_min;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getStorage() {
                return storage;
            }

            public void setStorage(String storage) {
                this.storage = storage;
            }

            public String getUse_up() {
                return use_up;
            }

            public void setUse_up(String use_up) {
                this.use_up = use_up;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getPinhao() {
                return pinhao;
            }

            public void setPinhao(String pinhao) {
                this.pinhao = pinhao;
            }
        }
    }
}
