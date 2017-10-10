package cn.cloudworkshop.miaoding.constant;

/**
 * Author：Libin on 2016/7/7 17:40
 * Email：1993911441@qq.com
 * Describe：接口常量
 */
public class Constant {
    //        public static final String HOST = "http://139.196.113.61";
    public static final String HOST = "http://www.cloudworkshop.cn";
//    public static final String HOST = "http://192.168.1.156";

    public static final String APP_ID = "wx07c2173e7686741e";
    private static final String INDEX = "/index.php/index/index5_4/";
    private static final String HOST_INDEX = HOST + "/index.php/index/index5_4/";
    public static final String NEW_HOMEPAGE_LIST = HOST_INDEX + "new_index_news";
    public static final String HOMEPAGE_TAB = HOST_INDEX + "get_news_tags_arr";
    public static final String HOMEPAGE_TAB_LIST = HOST_INDEX + "index_tab_list";
    public static final String GOODS_TITLE = HOST_INDEX + "goods_classify";
    public static final String GOODS_LIST = HOST_INDEX + "goods_list";
    public static final String DESIGNER_WORKS = HOST_INDEX + "cobbler";
    public static final String NEW_DESIGNER_WORKS = HOST_INDEX + "cobbler_goods_list";
    public static final String GOODS_DETAILS = HOST_INDEX + "new_goods_detail";
    public static final String TAILOR_INFO = "http://139.196.113.61" + INDEX + "customize";
    public static final String NEW_CUSTOMIZE = HOST_INDEX + "new_customize";
    public static final String VERIFICATION_CODE = HOST + "/index.php/index/login/send_sms";
    public static final String LOGIN = HOST + "/index.php/index/login/do_login";
    public static final String LOGOUT = HOST_INDEX + "login_out";
    public static final String COLLECTION = HOST_INDEX + "my_collect";
    public static final String DRESSING_TEST = HOST_INDEX + "add_user_data";
    public static final String FEED_BACK = HOST_INDEX + "add_suggest";
    public static final String ADD_ADDRESS = HOST_INDEX + "add_address";
    public static final String MY_ADDRESS = HOST_INDEX + "my_address";
    public static final String DELETE_ADDRESS = HOST_INDEX + "delete_address";
    public static final String DEFAULT_ADDRESS = HOST_INDEX + "set_default_address";
    public static final String EMBROIDERY_CUSTOMIZE = HOST_INDEX + "new_goods_gxh";
    public static final String JOIN_US = HOST_INDEX + "get_img";
    public static final String APPLY_JOIN = HOST_INDEX + "apply_in";
    public static final String USER_INFO = HOST_INDEX + "user_info";
    public static final String ADD_CART = HOST_INDEX + "add_car";
    public static final String CONFIRM_INFO = HOST_INDEX + "buy";
    public static final String CONFIRM_ORDER = HOST_INDEX + "add_order_v4";
    public static final String CHANGE_INFO = HOST_INDEX + "change_user_info";
    public static final String SHOPPING_CART = HOST_INDEX + "my_car_page";
    public static final String CART_COUNT = HOST_INDEX + "change_car_num";
    public static final String DELETE_CART = HOST_INDEX + "delete_car";
    public static final String GOODS_ORDER = HOST_INDEX + "goods_order_v5_2";
    public static final String ORDER_DETAIL = HOST_INDEX + "order_detail";
    public static final String NEW_ORDER_DETAIL = HOST_INDEX + "new_order_detail_v5_2";
    public static final String CANCEL_ORDER = HOST_INDEX + "cancel_order";
    public static final String DELETE_ORDER = HOST_INDEX + "delete_order";
    public static final String CONFIRM_RECEIVE = HOST_INDEX + "confirm_order";
    public static final String ADD_COLLECTION = HOST_INDEX + "add_user_collect";
    public static final String CART_TO_CUSTOM = HOST_INDEX + "dz_car_data";
    public static final String APPOINTMENT_ORDER = HOST_INDEX + "add_order_list";
    public static final String QUESTION_CLASSIFY = HOST_INDEX + "help_classify";
    public static final String QUESTION_LIST = HOST_INDEX + "help_list";
    public static final String QUESTION_DETAIL = HOST_INDEX + "help_detail";
    public static final String APP_INDEX = HOST + "/index.php/index/sys/index";
    public static final String ALI_PAY = HOST_INDEX + "mk_pay_order_v4";
    public static final String LOGISTICS_TRACK = HOST_INDEX + "kdcx";
    public static final String WE_CHAT_PAY = HOST + "/index.php/index/wxpay/mk_pay_order_v4";
    public static final String INVITE_FRIEND = HOST_INDEX + "invite";
    public static final String MY_COUPON = HOST + "/index.php/index/ticket/my_ticket";
    public static final String EXCHANGE_COUPON = HOST + "/index.php/index/ticket/exchange_ticket";
    public static final String SELECT_COUPON = HOST + "/index.php/index/ticket/get_car_ticket";
    public static final String CHECK_LOGIN = HOST_INDEX + "check_login";
    public static final String MESSAGE_TYPE = HOST + "/index.php/index/message/message_type";
    public static final String MESSAGE_DETAIL = HOST + "/index.php/index/message/message_list";
    public static final String AFTER_SALE = HOST_INDEX + "add_sh_order";
    public static final String MEASURE_DATA = HOST_INDEX + "lt_data";
    public static final String CLIENT_ID = HOST_INDEX + "add_device";
    public static final String APPOINTMENT_STATUS = HOST_INDEX + "get_yuyue_status";
    public static final String GUIDE_IMG = HOST_INDEX + "get_guide_img";
    public static final String GOODS_RECOMMEND = HOST_INDEX + "goods_recommend";
    public static final String MEMBER_GRADE = HOST + "/index.php/index/user/user_privilege";
    public static final String MEMBER_RULE = HOST + "/index.php/index/user/user_help";
    public static final String MEMBER_GROWTH = HOST + "/index.php/index/user/user_credit_record";
    public static final String UPGRADE_GIFT = HOST + "/index.php/index/user/user_upgrade_gift";
    public static final String BIRTHDAY_GIFT = HOST + "/index.php/index/user/get_birthday_gift";
    public static final String TAKE_PHOTO = HOST + "/index.php/web/cc/cameraAndroidUpload";
    public static final String NEW_TAKE_PHOTO = "http://139.196.113.61/index.php/web/cc/cameraImgUpload";
    public static final String DESIGNER_LIST = HOST_INDEX + "get_designer_list";
    public static final String DESIGNER_DETAILS = HOST_INDEX + "user_intro";
    public static final String COUPON_RULE = HOST + "/index.php/index/ticket/get_ticket_introduce";
    public static final String EVALUATE = HOST_INDEX + "save_order_comment";
    public static final String EVALUATE_LIST = HOST_INDEX + "get_goods_comment_list";
    public static final String APP_ICON = HOST_INDEX + "get_app_icon_list";
    public static final String HOMEPAGE_LOG = HOST_INDEX + "save_index_log";
    public static final String GOODS_LOG = HOST_INDEX + "save_goods_log";
    public static final String LOGIN_LOG = HOST_INDEX + "save_user_login_log";
    public static final String GIFT_CARD = HOST_INDEX + "gift_card";
    public static final String EXCHANGE_CARD = HOST_INDEX + "exchange_gift_card";

    //WebView
    public static final String DRESSING_TEST_RESULT = HOST + "/web/jquery-obj/static/web/html/clo.html";
    public static final String HOMEPAGE_INFO = HOST + "/web/jquery-obj/static/web/html/designer.html";
    public static final String CUSTOM_SHARE = HOST + "/web/jquery-obj/static/fx/html/dingzhi.html";
    public static final String HOMEPAGE_SHARE = HOST + "/web/jquery-obj/static/fx/html/designer.html";
    public static final String WORKS_SHARE = HOST + "/web/jquery-obj/static/fx/html/detail.html";
    public static final String DRESSING_TEST_SHARE = HOST + "/web/jquery-obj/static/fx/html/clo.html";
    public static final String DESIGNER_SHARE = HOST + "/web/jquery-obj/static/fx/html/jiangxin.html";
    public static final String INVITE_SHARE = HOST + "/web/jquery-obj/static/fx/html/yaoqing.html";
    public static final String SHARE_COUPON = HOST + "/web/jquery-obj/static/fx/html/invitation_1000.html";

}
