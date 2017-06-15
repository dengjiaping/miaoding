package cn.cloudworkshop.miaoding.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.umeng.analytics.MobclickAgent;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.cloudworkshop.miaoding.R;
import cn.cloudworkshop.miaoding.base.BaseActivity;
import cn.cloudworkshop.miaoding.bean.TailorInfoBean;
import cn.cloudworkshop.miaoding.bean.TailorItemBean;
import cn.cloudworkshop.miaoding.constant.Constant;
import cn.cloudworkshop.miaoding.utils.MyLinearLayoutManager;
import cn.cloudworkshop.miaoding.utils.SharedPreferencesUtils;
import okhttp3.Call;

/**
 * Author：Libin on 2016/8/31 10:23
 * Email：1993911441@qq.com
 * Describe：订制商品结果
 */
public class CustomResultActivity extends BaseActivity {

    @BindView(R.id.img_header_back)
    ImageView imgHeaderBack;
    @BindView(R.id.tv_header_title)
    TextView tvHeaderTitle;
    @BindView(R.id.tv_buy_now)
    TextView tvBuyNow;
    @BindView(R.id.rl_tailor_positive)
    RelativeLayout rlTailorPositive;
    @BindView(R.id.tv_tailor_name)
    TextView tvTailorName;
    @BindView(R.id.rv_tailor_name)
    RecyclerView rvTailorName;
    @BindView(R.id.tv_tailor_price)
    TextView tvTailorPrice;
    @BindView(R.id.tv_add_bag)
    TextView tvAddBag;
    @BindView(R.id.img_header_share)
    ImageView imgShoppingBag;
    @BindView(R.id.rl_tailor_back)
    RelativeLayout rlTailorBack;
    @BindView(R.id.rl_tailor_inside)
    RelativeLayout rlTailorInside;
    @BindView(R.id.rgs_tailor_position)
    RadioGroup rgsTailorPosition;
    @BindView(R.id.img_default_item)
    ImageView imgDefaultItem;

    //1:直接购买 2：加入购物袋
    private int type = 0;
    private String cartId;
    public static Activity instance;

    private float x1 = 0;
    private float x2 = 0;

    private TailorItemBean tailorBean;
    private Map<Integer, Integer> posMap = new HashMap<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_result);
        ButterKnife.bind(this);
        instance = this;
        tvHeaderTitle.setText("订制详情");
        imgShoppingBag.setVisibility(View.VISIBLE);
        imgShoppingBag.setImageResource(R.mipmap.icon_shopping_bag);
        getData();
    }

    /**
     * 商品详情
     */
    private void getData() {
        Bundle bundle = getIntent().getExtras();
        tailorBean = (TailorItemBean) bundle.getSerializable("tailor");

        initView();
    }


    /**
     * 加载视图
     */
    private void initView() {

        if (tailorBean.getItemBean() != null && tailorBean.getItemBean().size() > 0) {
            for (int i = 0; i < tailorBean.getItemBean().size(); i++) {
                posMap.put(tailorBean.getItemBean().get(i).getPosition_id(), i);
                ImageView img = new ImageView(this);
                Glide.with(getApplicationContext())
                        .load(Constant.HOST + tailorBean.getItemBean().get(i).getImg())
                        .fitCenter()
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .into(img);
                switch (tailorBean.getItemBean().get(i).getPosition_id()) {
                    case 1:
                        rgsTailorPosition.getChildAt(0).setVisibility(View.VISIBLE);
                        rlTailorPositive.addView(img);
                        break;
                    case 2:
                        rgsTailorPosition.getChildAt(1).setVisibility(View.VISIBLE);
                        rlTailorBack.addView(img);
                        break;
                    case 3:
                        rgsTailorPosition.getChildAt(2).setVisibility(View.VISIBLE);
                        rlTailorInside.addView(img);
                        break;
                }

            }

            if (posMap.size() > 1) {
                ((RadioButton) rgsTailorPosition.getChildAt(0)).setChecked(true);
                rgsTailorPosition.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup radioGroup, int i) {

                        for (int m = 0; m < rgsTailorPosition.getChildCount(); m++) {
                            RadioButton rBtn = (RadioButton) radioGroup.getChildAt(m);
                            if (rBtn.getId() == i) {
                                switch (m) {
                                    case 0:
                                        rlTailorPositive.setVisibility(View.VISIBLE);
                                        rlTailorBack.setVisibility(View.GONE);
                                        rlTailorInside.setVisibility(View.GONE);
                                        break;
                                    case 1:
                                        rlTailorBack.setVisibility(View.VISIBLE);
                                        rlTailorPositive.setVisibility(View.GONE);
                                        rlTailorInside.setVisibility(View.GONE);
                                        break;
                                    case 2:
                                        rlTailorInside.setVisibility(View.VISIBLE);
                                        rlTailorPositive.setVisibility(View.GONE);
                                        rlTailorBack.setVisibility(View.GONE);
                                        break;
                                }
                            }
                        }
                    }
                });

                rlTailorPositive.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {
                        switch (motionEvent.getAction()) {
                            case MotionEvent.ACTION_DOWN:
                                x1 = motionEvent.getRawX();
                                break;
                            case MotionEvent.ACTION_MOVE:
                                x2 = motionEvent.getRawX();
                            case MotionEvent.ACTION_UP:
                                if (x1 < x2) {
                                    if (rgsTailorPosition.getChildAt(1).getVisibility() == View.VISIBLE) {
                                        ((RadioButton) rgsTailorPosition.getChildAt(1)).setChecked(true);
                                    }
                                }
                                break;
                        }
                        return true;
                    }
                });

                rlTailorBack.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {
                        switch (motionEvent.getAction()) {
                            case MotionEvent.ACTION_DOWN:
                                x1 = motionEvent.getX();
                                break;
                            case MotionEvent.ACTION_MOVE:
                                x2 = motionEvent.getX();
                            case MotionEvent.ACTION_UP:
                                if (x1 > x2) {
                                    ((RadioButton) rgsTailorPosition.getChildAt(0)).setChecked(true);
                                } else if (x1 < x2) {
                                    if (rgsTailorPosition.getChildAt(2).getVisibility() == View.VISIBLE) {
                                        ((RadioButton) rgsTailorPosition.getChildAt(2)).setChecked(true);
                                    }
                                }
                                break;
                        }
                        return true;
                    }
                });

                rlTailorInside.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {
                        switch (motionEvent.getAction()) {
                            case MotionEvent.ACTION_DOWN:
                                x1 = motionEvent.getX();
                                break;
                            case MotionEvent.ACTION_MOVE:
                                x2 = motionEvent.getX();
                            case MotionEvent.ACTION_UP:
                                if (x1 > x2) {
                                    ((RadioButton) rgsTailorPosition.getChildAt(1)).setChecked(true);
                                }
                                break;
                        }
                        return true;
                    }
                });
            } else {
                rgsTailorPosition.setVisibility(View.GONE);
            }

        } else {
            rgsTailorPosition.setVisibility(View.GONE);
            Glide.with(getApplicationContext())
                    .load(Constant.HOST + tailorBean.getDefault_img())
                    .fitCenter()
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(imgDefaultItem);
        }


        tvTailorName.setText(tailorBean.getGoods_name());
        tvTailorPrice.setText("¥" + tailorBean.getPrice());
        rvTailorName.setFocusable(false);

        List<TailorInfoBean> itemList = new ArrayList<>();

        if (!TextUtils.isEmpty(tailorBean.getSpec_content())) {
            String[] typeStr = tailorBean.getSpec_content().split(";");
            for (int i = 0; i < typeStr.length; i++) {
                String[] nameStr = typeStr[i].split(":");
                TailorInfoBean tailorInfoBean = new TailorInfoBean();
                tailorInfoBean.setType(nameStr[0]);
                tailorInfoBean.setName(nameStr[1]);
                itemList.add(tailorInfoBean);
            }

            MyLinearLayoutManager linearLayoutManager = new MyLinearLayoutManager(this);
            linearLayoutManager.setScrollEnabled(false);
            rvTailorName.setLayoutManager(linearLayoutManager);
            CommonAdapter<TailorInfoBean> adapter = new CommonAdapter<TailorInfoBean>(this,
                    R.layout.listitem_custom_result, itemList) {
                @Override
                protected void convert(ViewHolder holder, TailorInfoBean tailorInfoBean, int position) {
                    holder.setText(R.id.tv_tailor_type, tailorInfoBean.getType() + "：");
                    holder.setText(R.id.tv_tailor_item, tailorInfoBean.getName());
                }


            };
            rvTailorName.setAdapter(adapter);
        }


    }


    @OnClick({R.id.img_header_back, R.id.tv_buy_now, R.id.tv_add_bag, R.id.img_header_share})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_header_back:
                finish();
                break;
            case R.id.tv_buy_now:
                type = 1;
                addToCart();
                break;
            case R.id.tv_add_bag:
                type = 2;
                addToCart();

                break;
            case R.id.img_header_share:
                startActivity(new Intent(this, ShoppingCartActivity.class));
                break;
        }
    }


    /**
     * 加入购物车
     */
    private void addToCart() {
        OkHttpUtils.post()
                .url(Constant.ADD_CART)
                .addParams("token", SharedPreferencesUtils.getString(this, "token"))
                .addParams("type", type + "")
                .addParams("price_id", tailorBean.getPrice_type())
                .addParams("goods_id", tailorBean.getId())
                .addParams("goods_type", "1")
                .addParams("price", tailorBean.getPrice())
                .addParams("goods_name", tailorBean.getGoods_name())
                .addParams("goods_thumb", tailorBean.getImg_url())
                .addParams("spec_ids", tailorBean.getSpec_ids())
                .addParams("spec_content", tailorBean.getSpec_content())
                .addParams("mianliao_id", tailorBean.getFabric_id())
                .addParams("banxing_id", tailorBean.getBanxing_id())
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                            cartId = jsonObject1.getString("car_id");
                            if (cartId != null) {
                                MobclickAgent.onEvent(CustomResultActivity.this, "add_cart");
                                if (type == 2) {
                                    Toast.makeText(CustomResultActivity.this, "加入购物袋成功",
                                            Toast.LENGTH_SHORT).show();
                                } else if (type == 1) {
                                    Intent intent = new Intent(CustomResultActivity.this,
                                            ConfirmOrderActivity.class);
                                    Bundle bundle = new Bundle();
                                    bundle.putString("cart_id", cartId);
                                    bundle.putString("log_id", tailorBean.getLog_id());
                                    bundle.putLong("goods_time", tailorBean.getGoods_time());
                                    bundle.putLong("dingzhi_time", tailorBean.getDingzhi_time());
                                    bundle.putString("goods_id", tailorBean.getId());
                                    bundle.putString("goods_name", tailorBean.getGoods_name());
                                    intent.putExtras(bundle);
                                    startActivity(intent);
                                }

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });

    }

}
