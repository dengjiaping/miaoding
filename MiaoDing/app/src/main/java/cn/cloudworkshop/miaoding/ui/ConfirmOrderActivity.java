package cn.cloudworkshop.miaoding.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.umeng.analytics.MobclickAgent;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.cloudworkshop.miaoding.R;
import cn.cloudworkshop.miaoding.base.BaseActivity;
import cn.cloudworkshop.miaoding.bean.ConfirmOrderBean;
import cn.cloudworkshop.miaoding.constant.Constant;
import cn.cloudworkshop.miaoding.utils.ActivityManagerUtils;
import cn.cloudworkshop.miaoding.utils.DateUtils;
import cn.cloudworkshop.miaoding.utils.GsonUtils;
import cn.cloudworkshop.miaoding.utils.MyLinearLayoutManager;
import cn.cloudworkshop.miaoding.utils.PayOrderUtils;
import cn.cloudworkshop.miaoding.utils.SharedPreferencesUtils;
import cn.cloudworkshop.miaoding.utils.ToastUtils;
import okhttp3.Call;

/**
 * Author：Libin on 2016/10/7 14:54
 * Email：1993911441@qq.com
 * Describe：确认订单
 */
public class ConfirmOrderActivity extends BaseActivity {
    @BindView(R.id.img_header_back)
    ImageView imgHeaderBack;
    @BindView(R.id.tv_header_title)
    TextView tvHeaderTitle;
    @BindView(R.id.tv_user_name)
    TextView tvUserName;
    @BindView(R.id.tv_user_phone)
    TextView tvUserPhone;
    @BindView(R.id.tv_user_address)
    TextView tvProvinceAddress;
    @BindView(R.id.tv_need_pay)
    TextView tvNeedPay;
    @BindView(R.id.tv_confirm_order)
    TextView tvConfirmOrder;
    @BindView(R.id.rv_confirm_order)
    RecyclerView rvConfirmOrder;
    @BindView(R.id.tv_no_address)
    TextView tvNoAddress;
    @BindView(R.id.tv_goods_total)
    TextView tvGoodsTotal;
    @BindView(R.id.tv_freight)
    TextView tvFreight;
    @BindView(R.id.tv_discount_money)
    TextView tvCouponDiscount;
    @BindView(R.id.ll_first_order)
    LinearLayout llFirstOrder;
    @BindView(R.id.view_first_order)
    View viewFirstOrder;
    @BindView(R.id.tv_coupon_count)
    TextView tvCouponCount;
    @BindView(R.id.ll_select_coupon)
    LinearLayout llSelectCoupon;
    @BindView(R.id.tv_coupon_content)
    TextView tvCouponContent;
    @BindView(R.id.ll_user_address)
    LinearLayout llUserAddress;
    @BindView(R.id.tv_default_address)
    TextView tvDefaultAddress;
    @BindView(R.id.rl_select_address)
    RelativeLayout rlSelectAddress;
    @BindView(R.id.rl_add_address)
    RelativeLayout rlAddAddress;

    //购物车id
    private String cartIds;
    private ConfirmOrderBean confirmOrderBean;
    //地址id
    private String addressId;
    //省
    private String provinceAddress;
    //市
    private String cityAddress;
    //区
    private String areaAddress;
    //详细地址
    private String detailAddress;
    //是否默认地址
    private int defaultAddress;
    //用户名
    private String userName;
    //手机号
    private String phoneNumber;
    //优惠券id
    private String couponId;
    //优惠券金额
    private String couponMoney;
    //优惠券详情
    private String couponContent;
    //优惠券最低使用金额
    private String couponMinMoney;
    //优惠券适用的商品
    private String goodsIds;
    //优惠券数量
    private int couponNum;
    //显示优惠金额
    private float discountCoupon = 0.00f;
    //实际优惠金额
    private float discountMoney = 0.00f;
    //商品adapter
    private CommonAdapter<ConfirmOrderBean.DataBean.CarListBean> adapter;

    private PayOrderUtils payOrderUtil;

    //收货地址是否为空，新建地址
    private boolean isNoAddress;
    //商品id
    private String goodsId;
    //商品名称
    private String goodsName;
    private String logId;
    private long goodsTime;
    private long dingzhiTime;
    //修改购物车数量，可用优惠券变化
    private boolean canCouponSelect;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_order);
        ButterKnife.bind(this);
        getData();
        initData();
    }


    /**
     * 获取网络数据
     */
    private void initData() {

        OkHttpUtils.get()
                .url(Constant.CONFIRM_ORDER)
                .addParams("token", SharedPreferencesUtils.getString(this, "token"))
                .addParams("car_ids", cartIds)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        if (canCouponSelect) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                JSONObject data = jsonObject.getJSONObject("data");
                                couponNum = data.getInt("ticket_num");
                                initCoupon();
                                canCouponSelect = false;
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            confirmOrderBean = GsonUtils.jsonToBean(response, ConfirmOrderBean.class);
                            if (confirmOrderBean.getData() != null) {
                                initView();
                            }
                        }
                    }
                });
    }


    /**
     * 购物车信息
     */
    private void getData() {
        Bundle bundle = getIntent().getExtras();
        cartIds = bundle.getString("cart_id");
        logId = bundle.getString("log_id");
        goodsTime = bundle.getLong("goods_time");
        dingzhiTime = bundle.getLong("dingzhi_time");
        goodsId = bundle.getString("goods_id");
        goodsName = bundle.getString("goods_name");

        tvHeaderTitle.setText("确认订单");
    }

    /**
     * 加载视图
     */
    private void initView() {

        if (confirmOrderBean.getData().getAddress_list() == null ||
                confirmOrderBean.getData().getAddress_list().equals("null")) {
            addressId = null;
        } else {
            addressId = confirmOrderBean.getData().getAddress_list().getId() + "";
            provinceAddress = confirmOrderBean.getData().getAddress_list().getProvince();
            cityAddress = confirmOrderBean.getData().getAddress_list().getCity();
            areaAddress = confirmOrderBean.getData().getAddress_list().getArea();
            detailAddress = confirmOrderBean.getData().getAddress_list().getAddress();
            defaultAddress = confirmOrderBean.getData().getAddress_list().getIs_default();
            userName = confirmOrderBean.getData().getAddress_list().getName();
            phoneNumber = confirmOrderBean.getData().getAddress_list().getPhone();
        }

        couponNum = confirmOrderBean.getData().getTicket_num();

        initAddress();
        initCoupon();


        MyLinearLayoutManager linearLayoutManager = new MyLinearLayoutManager(this);
        linearLayoutManager.setScrollEnabled(false);
        rvConfirmOrder.setLayoutManager(linearLayoutManager);
        adapter = new CommonAdapter<ConfirmOrderBean.DataBean.CarListBean>(this,
                R.layout.listitem_shopping_cart, confirmOrderBean.getData().getCar_list()) {
            @Override
            protected void convert(final ViewHolder holder, ConfirmOrderBean.DataBean.CarListBean
                    carListBean, final int position) {
                holder.setVisible(R.id.checkbox_goods_select, false);
                holder.setVisible(R.id.view_cart_divide, true);
                Glide.with(ConfirmOrderActivity.this)
                        .load(Constant.HOST + carListBean.getGoods_thumb())
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .into((ImageView) holder.getView(R.id.img_item_goods));
                holder.setVisible(R.id.tv_goods_count, false);
                holder.setVisible(R.id.ll_cart_edit, false);
                holder.setVisible(R.id.ll_cart_edit1, true);
                holder.setText(R.id.tv_goods_name, carListBean.getGoods_name());
                switch (carListBean.getGoods_type()) {
                    case 2:
                        holder.setText(R.id.tv_goods_content, carListBean.getSize_content());
                        break;
                    default:
                        holder.setText(R.id.tv_goods_content, "定制款");
                        break;
                }
                holder.setText(R.id.tv_goods_price, "¥" + carListBean.getPrice());
                holder.setText(R.id.tv_cart_count1, carListBean.getNum() + "");
                holder.setVisible(R.id.view_cart, true);

                holder.getView(R.id.tv_cart_add1).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int count = confirmOrderBean.getData().getCar_list().get(position).getNum();
                        changeCartCount(position, count + 1);

                    }
                });

                holder.getView(R.id.tv_cart_reduce1).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int count = confirmOrderBean.getData().getCar_list().get(position).getNum();
                        if (count > 1) {
                            changeCartCount(position, count - 1);
                        }
                    }
                });
            }
        };
        rvConfirmOrder.setAdapter(adapter);

    }

    /**
     * 收货地址
     */
    private void initAddress() {
        if (addressId == null) {
            tvNoAddress.setVisibility(View.VISIBLE);
            if (isNoAddress) {
                tvNoAddress.setText("请创建收货地址");
            } else {
                tvNoAddress.setText("请选择收货地址");
            }
            rlAddAddress.setVisibility(View.VISIBLE);
            llUserAddress.setVisibility(View.GONE);
            tvDefaultAddress.setVisibility(View.GONE);
        } else {
            rlAddAddress.setVisibility(View.GONE);
            llUserAddress.setVisibility(View.VISIBLE);
            if (defaultAddress == 1) {
                tvDefaultAddress.setVisibility(View.VISIBLE);
            } else {
                tvDefaultAddress.setVisibility(View.GONE);
            }

            tvUserName.setText(userName);
            tvUserPhone.setText(phoneNumber);
            tvProvinceAddress.setText(provinceAddress + cityAddress + areaAddress + detailAddress);
        }
    }


    /**
     * 是否选择优惠券
     */
    private void initCoupon() {
        if (couponId == null) {
            tvCouponCount.setText(couponNum + "张");
            tvCouponCount.setVisibility(View.VISIBLE);
            tvCouponContent.setText("请选择优惠券");
            tvCouponContent.setTextColor(ContextCompat.getColor(ConfirmOrderActivity.this,
                    R.color.dark_gray_22));
            tvCouponDiscount.setText("- ¥0.00");

        } else {
            tvCouponCount.setVisibility(View.GONE);
            tvCouponContent.setText(couponContent);
            tvCouponContent.setTextColor(0xffea3a37);
            tvCouponDiscount.setText("- ¥" + new DecimalFormat("#0.00").format(discountCoupon));
        }

        getTotalPrice();
    }


    /**
     * @param position 改变购物车数量
     */
    private void changeCartCount(final int position, final int currentCount) {
        OkHttpUtils.post()
                .url(Constant.CART_COUNT)
                .addParams("token", SharedPreferencesUtils.getString(this, "token"))
                .addParams("car_id", confirmOrderBean.getData().getCar_list().get(position).getId() + "")
                .addParams("num", currentCount + "")
                .addParams("type", "1")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            int code = jsonObject.getInt("code");
                            if (code == 1) {
                                confirmOrderBean.getData().getCar_list().get(position).setNum(currentCount);
                                adapter.notifyDataSetChanged();
                                if (couponId != null) {
                                    if (!isCouponAvailable()) {
                                        couponId = null;
                                    }
                                    initCoupon();
                                } else {
                                    canCouponSelect = true;
                                    initData();
                                }
                                getTotalPrice();
                            } else {
                                ToastUtils.showToast(ConfirmOrderActivity.this, "库存不足");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    /**
     * 是否达到优惠券使用要求
     * 商品数量减少达不到关联优惠券要求，会取消优惠券
     */
    private boolean isCouponAvailable() {
        float maxPrice = 0.00f;
        int count = 0;
        //该商品是否包含在优惠券中
        String[] split = goodsIds.split(",");
        for (int i = 0; i < confirmOrderBean.getData().getCar_list().size(); i++) {
            if (Arrays.asList(split).contains(confirmOrderBean.getData().getCar_list().get(i)
                    .getGoods_id() + "")) {
                float price = Float.parseFloat(confirmOrderBean.getData().getCar_list().get(i).getPrice());
                int num = confirmOrderBean.getData().getCar_list().get(i).getNum();
                maxPrice += price * num;
                count++;
            }
        }
        //单种商品最高总价格
        if (maxPrice >= Float.parseFloat(couponMinMoney)) {
            if (Float.parseFloat(couponMoney) <= (maxPrice - 0.01 * count)) {
                discountCoupon = Float.parseFloat(couponMoney);
                discountMoney = Float.parseFloat(couponMoney);
            } else {
                discountCoupon = maxPrice;
                discountMoney = (float) (maxPrice - 0.01 * count);
            }
            return true;
        } else {
            return false;
        }
    }


    /**
     * 获取总价格
     */
    private String getTotalPrice() {
        float totalPrice = 0;
        for (int i = 0; i < confirmOrderBean.getData().getCar_list().size(); i++) {
            float price = Float.parseFloat(confirmOrderBean.getData().getCar_list().get(i).getPrice());
            int num = confirmOrderBean.getData().getCar_list().get(i).getNum();
            totalPrice += price * num;
        }
        tvGoodsTotal.setText("¥" + new DecimalFormat("#0.00").format(totalPrice));
        if (couponId != null) {
            totalPrice -= discountMoney;
        }

        tvNeedPay.setText("¥" + new DecimalFormat("#0.00").format(totalPrice));
        return new DecimalFormat("#0.00").format(totalPrice);
    }

    @OnClick({R.id.img_header_back, R.id.rl_select_address, R.id.tv_confirm_order, R.id.ll_select_coupon})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_header_back:
                customGoodsLog();
                finish();
                break;
            case R.id.rl_select_address:
                if (addressId == null) {
                    if (isNoAddress) {
                        Intent intent = new Intent(ConfirmOrderActivity.this, AddAddressActivity.class);
                        intent.putExtra("type", "add");
                        startActivityForResult(intent, 2);
                    } else {
                        Intent intent1 = new Intent(ConfirmOrderActivity.this, DeliveryAddressActivity.class);
                        intent1.putExtra("type", "select");
                        startActivityForResult(intent1, 3);
                    }

                } else {
                    Intent intent = new Intent(ConfirmOrderActivity.this, DeliveryAddressActivity.class);
                    intent.putExtra("type", "select");
                    intent.putExtra("address_id", addressId);
                    startActivityForResult(intent, 3);
                }
                break;
            case R.id.tv_confirm_order:
                if (addressId == null) {
                    ToastUtils.showToast(this, "请选择地址");
                } else {
                    confirmOrder();
                }
                break;
            case R.id.ll_select_coupon:
                Intent intent = new Intent(this, SelectCouponActivity.class);
                intent.putExtra("cart_ids", cartIds);
                startActivityForResult(intent, 1);
                break;
        }
    }


    /**
     * 确认下单
     */
    private void confirmOrder() {
        Map<String, String> map = new HashMap<>();
        map.put("token", SharedPreferencesUtils.getString(this, "token"));
        map.put("car_ids", cartIds);
        if (couponId != null) {
            map.put("ticket_id", couponId);
        }
        map.put("name", userName);
        map.put("phone", phoneNumber);
        map.put("province", provinceAddress);
        map.put("city", cityAddress);
        map.put("area", areaAddress);
        map.put("address", detailAddress);
        map.put("address_id", addressId);
        if (logId != null) {
            map.put("log_id", logId);
        }

        map.put("method", "1");

        OkHttpUtils.post()
                .url(Constant.CONFIRM_BUY)
                .params(map)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            int code = jsonObject.getInt("code");
                            String msg = jsonObject.getString("msg");
                            if (code == 1) {
                                JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                                String orderId = jsonObject1.getString("order_id");
                                if (orderId != null) {
                                    HashMap<String, String> map = new HashMap<String, String>();
                                    map.put("id", cartIds);
                                    //下单事件监听
                                    MobclickAgent.onEvent(ConfirmOrderActivity.this, "place_order", map);
                                    ToastUtils.showToast(ConfirmOrderActivity.this, msg);

                                    ActivityManagerUtils.getInstance().finishActivityClass(CustomizeActivity.class);
                                    ActivityManagerUtils.getInstance().finishActivityClass(EmbroideryActivity.class);
                                    ActivityManagerUtils.getInstance().finishActivityClass(CustomResultActivity.class);
                                    ActivityManagerUtils.getInstance().finishActivityClass(ShoppingCartActivity.class);

                                    payOrderUtil = new PayOrderUtils(ConfirmOrderActivity.this,
                                            getTotalPrice(), orderId);
                                    payOrderUtil.payMoney();

                                }
                            } else {
                                ToastUtils.showToast(ConfirmOrderActivity.this, msg);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            //优惠券
            case 1:
                switch (resultCode) {
                    //返回
                    case 1:
                        couponNum = data.getIntExtra("coupon_num", couponNum);
                        initCoupon();
                        break;
                    //不使用优惠券
                    case 2:
                        couponId = null;
                        couponNum = data.getIntExtra("coupon_num", couponNum);
                        initCoupon();
                        break;
                    //使用优惠券
                    case 3:
                        couponId = data.getStringExtra("coupon_id");
                        couponMoney = data.getStringExtra("coupon_money");
                        couponContent = data.getStringExtra("coupon_content");
                        couponMinMoney = data.getStringExtra("coupon_min_money");
                        goodsIds = data.getStringExtra("goods_ids");

                        if (!isCouponAvailable()) {
                            couponId = null;
                        }
                        initCoupon();

                        break;
                }
                break;
            //添加地址
            case 2:
                if (resultCode == 1) {
                    getAddress(data);
                }
                break;
            //选择地址
            case 3:
                switch (resultCode) {
                    //已选择地址
                    case 1:
                        getAddress(data);
                        break;
                    //收货地址全部被删除，重新创建地址
                    case 2:
                        addressId = null;
                        isNoAddress = true;
                        initAddress();
                        break;
                    //收货地址不为空，但已选择地址被删除
                    case 3:
                        addressId = null;
                        isNoAddress = false;
                        initAddress();
                        break;
                    //收货地址被修改
                    case 4:
                        getAddress(data);
                        break;
                }
                break;
        }
    }

    /**
     * @param intent 地址返回值
     */
    private void getAddress(Intent intent) {
        addressId = intent.getStringExtra("address_id");
        provinceAddress = intent.getStringExtra("province");
        cityAddress = intent.getStringExtra("city");
        areaAddress = intent.getStringExtra("area");
        detailAddress = intent.getStringExtra("address");
        userName = intent.getStringExtra("name");
        phoneNumber = intent.getStringExtra("phone");
        defaultAddress = intent.getIntExtra("is_default", 0);
        initAddress();
    }


    /**
     * 商品订制跟踪
     */
    private void customGoodsLog() {
        if (logId != null) {
            OkHttpUtils.post()
                    .url(Constant.GOODS_LOG)
                    .addParams("token", SharedPreferencesUtils.getString(this, "token"))
                    .addParams("id", logId)
                    .addParams("goods_time", (DateUtils.getCurrentTime() - goodsTime) + "")
                    .addParams("dingzhi_time", dingzhiTime + "")
                    .addParams("goods_id", goodsId)
                    .addParams("goods_name", goodsName)
                    .addParams("click_dingzhi", "1")
                    .addParams("click_pay", "0")
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {

                        }

                        @Override
                        public void onResponse(String response, int id) {
                        }
                    });
        }

    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            customGoodsLog();
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


}
