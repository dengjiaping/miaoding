package cn.cloudworkshop.seller.constant;

/**
 * Author：binge on 2017-09-14 17:55
 * Email：1993911441@qq.com
 * Describe：
 */
public class Constant {

    public static final String HOST = "http://139.196.113.61";
//    public static final String HOST = "http://www.cloudworkshop.cn";
//    public static final String HOST = "http://192.168.1.156";

    private static final String INDEX = "/index.php/index/seller/";


    public static final String LOGIN = HOST + INDEX + "do_login";
    public static final String HOMEPAGE = HOST + INDEX + "index";
    public static final String CHECK_LOGIN = HOST + INDEX + "check_login";
    public static final String ORDER = HOST + INDEX + "order_list";
    public static final String WALLET = HOST + INDEX + "shop_wallet";
    public static final String STOCK = HOST + INDEX + "shop_storage";
    public static final String ADD_STOCK = HOST + INDEX + "add_storage";
    public static final String DISTRIBUTE = HOST + INDEX + "shop_fenxiao";
    public static final String LOG_OUT = HOST + INDEX + "login_out";

}
