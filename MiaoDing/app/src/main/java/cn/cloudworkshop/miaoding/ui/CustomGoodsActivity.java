package cn.cloudworkshop.miaoding.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.umeng.analytics.MobclickAgent;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.cloudworkshop.miaoding.R;
import cn.cloudworkshop.miaoding.application.MyApplication;
import cn.cloudworkshop.miaoding.base.BaseActivity;
import cn.cloudworkshop.miaoding.bean.TailorGoodsBean;
import cn.cloudworkshop.miaoding.constant.Constant;
import cn.cloudworkshop.miaoding.utils.ContactService;
import cn.cloudworkshop.miaoding.utils.DateUtils;
import cn.cloudworkshop.miaoding.utils.DisplayUtils;
import cn.cloudworkshop.miaoding.utils.GsonUtils;
import cn.cloudworkshop.miaoding.utils.LogUtils;
import cn.cloudworkshop.miaoding.utils.NetworkImageHolderView;
import cn.cloudworkshop.miaoding.utils.ShareUtils;
import cn.cloudworkshop.miaoding.utils.SharedPreferencesUtils;
import cn.cloudworkshop.miaoding.view.ScrollViewContainer;
import okhttp3.Call;

import static android.view.KeyEvent.KEYCODE_BACK;


/**
 * Author：binge on 2017-04-18 10:59
 * Email：1993911441@qq.com
 * Describe：定制商品详情界面
 */
public class CustomGoodsActivity extends BaseActivity {
    @BindView(R.id.tv_goods_sort)
    TextView tvGoodsName;
    @BindView(R.id.tv_goods_introduce)
    TextView tvGoodsContent;
    @BindView(R.id.img_tailor_back)
    ImageView imgBack;
    @BindView(R.id.tv_goods_tailor)
    TextView tvTailor;
    @BindView(R.id.banner_goods)
    ConvenientBanner bannerGoods;
    @BindView(R.id.scroll_container)
    ScrollViewContainer scrollContainer;
    @BindView(R.id.img_add_like)
    ImageView imgAddLike;
    @BindView(R.id.img_tailor_consult)
    ImageView imgConsult;
    @BindView(R.id.ll_goods_tailor)
    LinearLayout llGoodsTailor;
    @BindView(R.id.img_tailor_share)
    ImageView imgShare;
    @BindView(R.id.img_tailor_details)
    ImageView imgDetails;
    private String id;
    private TailorGoodsBean.DataBean tailorBean;

    private long enterTime;
    private int isBuy;
    private String logId;
    private String customTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_goods);
        ButterKnife.bind(this);
        getData();
        initData();
    }

    /**
     * 商品详情
     */
    private void getData() {
        enterTime = DateUtils.getCurrentTime();
        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        logId = intent.getStringExtra("log_id");
        customTime = intent.getStringExtra("custom_time");
    }

    /**
     * 加载数据
     */
    private void initData() {

        OkHttpUtils.get()
                .url(Constant.NEW_GOODS_DETAILS)
                .addParams("token", SharedPreferencesUtils.getString(this, "token"))
                .addParams("goods_id", id)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        tailorBean = GsonUtils.jsonToBean(response, TailorGoodsBean.class).getData();
                        if (tailorBean != null) {
                            initView();
                        }

                    }
                });

    }


    /**
     * 加载视图
     */
    private void initView() {
        tvGoodsName.setText(tailorBean.getName());
        tvGoodsContent.setText(tailorBean.getSub_name());
        if (tailorBean.getIs_collect() == 1) {
            imgAddLike.setImageResource(R.mipmap.icon_add_like);
        } else {
            imgAddLike.setImageResource(R.mipmap.icon_cancel_like);
        }
        bannerGoods.startTurning(4000);
        bannerGoods.setPages(
                new CBViewHolderCreator<NetworkImageHolderView>() {
                    @Override
                    public NetworkImageHolderView createHolder() {
                        return new NetworkImageHolderView();
                    }
                }, tailorBean.getImg_list())
                //设置两个点图片作为翻页指示器
                .setPageIndicator(new int[]{R.drawable.dot_black, R.drawable.dot_white})
                //设置指示器的方向
                .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.CENTER_HORIZONTAL);
//        bannerGoods.setOnItemClickListener(new OnItemClickListener() {
//            @Override
//            public void onItemClick(int position) {
//                Intent intent = new Intent(CustomGoodsActivity.this, ImagePreviewActivity.class);
//                intent.putExtra("currentPos", position);
//                intent.putStringArrayListExtra("img_list", tailorBean.getImg_list());
//                startActivity(intent);
//            }
//        });
        Glide.with(getApplicationContext())
                .load(Constant.HOST + tailorBean.getContent2())
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(imgDetails);

        scrollContainer.getCurrentView(new ScrollViewContainer.CurrentPageListener() {
            @Override
            public void getCurrentPage(int page) {
                if (page == 0) {
                    llGoodsTailor.setVisibility(View.GONE);
                } else {
                    llGoodsTailor.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    /**
     * 选择价格
     */
    private void selectGoodsPrice() {

        View contentView = LayoutInflater.from(this).inflate(R.layout.ppw_select_price, null);
        final PopupWindow mPopupWindow = new PopupWindow(contentView,
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mPopupWindow.setContentView(contentView);
        mPopupWindow.setTouchable(true);
        mPopupWindow.setFocusable(true);
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable(getResources(), (Bitmap) null));
        mPopupWindow.showAtLocation(getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
        DisplayUtils.setBackgroundAlpha(this, 0.5f);
        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                DisplayUtils.setBackgroundAlpha(CustomGoodsActivity.this, 1.0f);
            }
        });
        RecyclerView rvTailor = (RecyclerView) contentView.findViewById(R.id.rv_tailor_price);

        rvTailor.setLayoutManager(new LinearLayoutManager(CustomGoodsActivity.this));

        CommonAdapter<TailorGoodsBean.DataBean.PriceBean> priceAdapter = new CommonAdapter
                <TailorGoodsBean.DataBean.PriceBean>(CustomGoodsActivity.this,
                R.layout.listitem_price_type, tailorBean.getPrice()) {
            @Override
            protected void convert(ViewHolder holder, TailorGoodsBean.DataBean.PriceBean priceBean, int position) {
                TextView tvPrice = holder.getView(R.id.tv_type_item);
                tvPrice.setTypeface(DisplayUtils.setTextType(CustomGoodsActivity.this));
                tvPrice.setText("¥" + new DecimalFormat("#0.00").format(priceBean.getPrice()));
                holder.setText(R.id.tv_type_content, priceBean.getIntroduce());
            }

        };


        priceAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                isBuy = 1;
                customGoodsLog();
                mPopupWindow.dismiss();
                Intent intent = new Intent(CustomGoodsActivity.this, CustomDiyActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("id", id);
                bundle.putString("goods_name", tailorBean.getName());
                bundle.putString("img_url", tailorBean.getThumb());
                bundle.putString("price", new DecimalFormat("#0.00").format(tailorBean.getPrice()
                        .get(position).getPrice()));
                bundle.putString("price_type", tailorBean.getPrice().get(position).getId() + "");
                bundle.putInt("classify_id", tailorBean.getClassify_id());
                intent.putExtras(bundle);
                startActivity(intent);
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });

        rvTailor.setAdapter(priceAdapter);
    }


    @OnClick({R.id.tv_goods_tailor, R.id.img_tailor_back, R.id.img_add_like, R.id.img_tailor_consult,
            R.id.img_tailor_share})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_goods_tailor:
                if (TextUtils.isEmpty(SharedPreferencesUtils.getString(this, "token"))) {
                    Intent login = new Intent(this, LoginActivity.class);
                    login.putExtra("page_name", "定制商品详情");
                    startActivity(login);
                } else {
                    selectGoodsPrice();
//                    if (tailorBean.getIs_yuyue() == 1) {
//                        selectGoodsPrice();
//                    } else {
//                        Intent intent = new Intent(this, ApplyMeasureActivity.class);
//                        intent.putExtra("goods_name", tailorBean.getName());
//                        startActivityForResult(intent, 1);
//                    }
                }
                break;
            case R.id.img_tailor_back:
                isBuy = 0;
                customGoodsLog();
                finish();
                break;
            case R.id.img_add_like:
                if (!TextUtils.isEmpty(SharedPreferencesUtils.getString(this, "token"))) {
                    addCollection();
                } else {
                    Intent login = new Intent(this, LoginActivity.class);
                    login.putExtra("page_name", "定制商品详情");
                    startActivity(login);
                }
                break;
            case R.id.img_tailor_consult:
                ContactService.contactService(this);
                break;
            case R.id.img_tailor_share:
                ShareUtils.showShare(this, Constant.HOST + tailorBean.getThumb(),
                        tailorBean.getName(),
                        tailorBean.getContent(),
                        Constant.GOODS_SHARE + "?type=2&id=" + id + "&token=" +
                                SharedPreferencesUtils.getString(this, "token"));
                break;
        }
    }

    /**
     * 商品订制跟踪
     */
    private void customGoodsLog() {
        if (tailorBean != null && logId != null && customTime != null) {
            long time = DateUtils.getCurrentTime() - enterTime;
            OkHttpUtils.post()
                    .url(Constant.GOODS_LOG)
                    .addParams("id", logId)
                    .addParams("dingzhi_time",customTime)
                    .addParams("goods_time", time + "")
                    .addParams("goods_id", id)
                    .addParams("goods_name", tailorBean.getName())
                    .addParams("click_dingzhi", isBuy + "")
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {

                        }

                        @Override
                        public void onResponse(String response, int id) {
                            LogUtils.log("goods:"+response);
                        }
                    });
        }


    }

    /**
     * 添加收藏
     */
    private void addCollection() {
        OkHttpUtils.get()
                .url(Constant.ADD_COLLECTION)
                .addParams("token", SharedPreferencesUtils.getString(this, "token"))
                .addParams("type", String.valueOf(2))
                .addParams("cid", id)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String msg = jsonObject.getString("msg");
                            switch (msg) {
                                case "成功":
                                    MobclickAgent.onEvent(CustomGoodsActivity.this, "collection");
                                    imgAddLike.setImageResource(R.mipmap.icon_add_like);
                                    Toast.makeText(CustomGoodsActivity.this,
                                            "收藏成功", Toast.LENGTH_SHORT).show();
                                    break;
                                case "取消成功":
                                    imgAddLike.setImageResource(R.mipmap.icon_cancel_like);
                                    Toast.makeText(CustomGoodsActivity.this,
                                            "已取消收藏", Toast.LENGTH_SHORT).show();

                                    break;

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
        if (requestCode == 1 && resultCode == 1) {
            tailorBean.setIs_yuyue(1);
            selectGoodsPrice();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            isBuy = 0;
            customGoodsLog();
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
