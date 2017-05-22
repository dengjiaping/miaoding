package cn.cloudworkshop.miaoding.ui;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.cloudworkshop.miaoding.R;
import cn.cloudworkshop.miaoding.base.BaseActivity;
import cn.cloudworkshop.miaoding.bean.FinishedGoodsBean;
import cn.cloudworkshop.miaoding.bean.GuideBean;
import cn.cloudworkshop.miaoding.bean.TailorGoodsBean;
import cn.cloudworkshop.miaoding.constant.Constant;
import cn.cloudworkshop.miaoding.utils.ContactService;
import cn.cloudworkshop.miaoding.utils.DisplayUtils;
import cn.cloudworkshop.miaoding.utils.VerVPTransformer;
import cn.cloudworkshop.miaoding.utils.GsonUtils;
import cn.cloudworkshop.miaoding.utils.ShareUtils;
import cn.cloudworkshop.miaoding.utils.SharedPreferencesUtils;
import cn.cloudworkshop.miaoding.view.VerticalViewPager;
import okhttp3.Call;


/**
 * Author：Libin on 2016/8/30 17:36
 * Email：1993911441@qq.com
 * Describe：商品详情（定制、成品）
 */
public class GoodsDetailsActivity extends BaseActivity {
    @BindView(R.id.vvp_goods)
    VerticalViewPager vvpGoods;
    @BindView(R.id.img_goods_details)
    ImageView imgGoodsDetails;
    @BindView(R.id.ll_tailor_details)
    LinearLayout llTailorDetails;
    @BindView(R.id.rl_goods_details)
    RelativeLayout rlGoodsDetails;
    @BindView(R.id.rgs_indicator)
    RadioGroup rgsIndicator;
    @BindView(R.id.img_goods_close)
    ImageView imgGoodsClose;
    @BindView(R.id.ll_goods_details)
    LinearLayout llGoodsDetails;
    @BindView(R.id.tv_add_cart)
    TextView tvAddCart;
    @BindView(R.id.img_header_back)
    ImageView imgHeaderBack;
    @BindView(R.id.tv_header_title)
    TextView tvHeaderTitle;
    @BindView(R.id.rv_goods_price)
    RecyclerView rvGoodsPrice;
    @BindView(R.id.rl_goods_price)
    RelativeLayout rlGoodsPrice;
    @BindView(R.id.view_header_line)
    View viewHeaderLine;
    @BindView(R.id.img_header_share)
    ImageView imgHeaderShare;
    @BindView(R.id.ll_works_details)
    LinearLayout llWorksDetails;
    @BindView(R.id.tv_buy_goods)
    TextView tvBuyGoods;
    @BindView(R.id.tv_tailor_goods)
    TextView tvTailorGoods;
    @BindView(R.id.rl_add_cart)
    RelativeLayout rlAddCart;
    @BindView(R.id.tv_confirm_buy)
    TextView tvConfirmBuy;
    @BindView(R.id.view_select_type)
    View viewSelectType;
    @BindView(R.id.tv_goods_like_count)
    TextView tvGoodsLike;
    @BindView(R.id.img_goods_icon)
    ImageView imgGoodsIcon;
    @BindView(R.id.tv_product_price)
    TextView tvGoodsPrice;
    @BindView(R.id.tv_goods_stock)
    TextView tvGoodsStock;
    @BindView(R.id.rv_goods_size)
    RecyclerView rvGoodsSize;
    @BindView(R.id.rv_goods_color)
    RecyclerView rvGoodsColor;
    @BindView(R.id.tv_reduce_count)
    TextView tvReduceCount;
    @BindView(R.id.tv_select_count)
    TextView tvSelectCount;
    @BindView(R.id.tv_add_count)
    TextView tvAddCount;
    @BindView(R.id.img_add_collection)
    ImageView imgAddCollection;
    @BindView(R.id.img_consult_server_works)
    ImageView imgConsultWorks;
    @BindView(R.id.img_consult_server)
    ImageView imgConsultTailor;
    @BindView(R.id.tv_goods_info)
    TextView tvGoodsInfo;
    @BindView(R.id.img_goods_guide)
    ImageView imgGoodsGuide;
//    @BindView(R.id.web_goods_comment)
//    WebView webView;
//    @BindView(R.id.scroll_container)
//    ScrollViewContainer scrollContainer;

    private ArrayList<String> imgList = new ArrayList<>();
    private String id;
    private int type;
    private String imgUrl;
    private TailorGoodsBean tailorEntity;
    private FinishedGoodsBean finishedGoodsBean;
    //加入购物车或直接购买
    private int index = 0;
    //尺码
    private int currentSize = -1;
    //颜色
    private int currentColor = -1;
    //购买数量
    private int count = 1;
    //购物车id
    private String cartId;
    //库存
    private int stock;


    private CommonAdapter<FinishedGoodsBean.DataBean.SizeListBean.ItemListBean> colorAdapter;
    private List<FinishedGoodsBean.DataBean.SizeListBean.ItemListBean> colorList = new ArrayList<>();
    //首张引导图
    private boolean isFirstGuide = true;
    private GuideBean guideBean;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_details);
        ButterKnife.bind(this);
        getData();
        initData();
    }

    /**
     * 商品详情
     */
    private void getData() {
        Bundle bundle = getIntent().getExtras();
        id = bundle.getString("id");
        imgUrl = bundle.getString("img_url");
    }

    /**
     * @param name
     * @param num
     * @param content 加载视图
     */
    private void initView(String name, String num, String content) {
        tvHeaderTitle.setText(name);
        imgHeaderShare.setVisibility(View.VISIBLE);
        tvGoodsLike.setText(num + "℃");
        tvGoodsInfo.setText(content);
        vvpGoods.setOffscreenPageLimit(imgList.size());
        vvpGoods.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return imgList.size();
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public Object instantiateItem(ViewGroup container, final int position) {
                View view = LayoutInflater.from(GoodsDetailsActivity.this)
                        .inflate(R.layout.viewpager_goods_details, null);
                final ImageView img = (ImageView) view.findViewById(R.id.img_goods_picture);
                Glide.with(GoodsDetailsActivity.this)
                        .load(Constant.HOST + imgList.get(position))
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .into(img);
                img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(GoodsDetailsActivity.this, ImagePreviewActivity.class);
                        intent.putExtra("currentPos", position);
                        intent.putStringArrayListExtra("img_list", imgList);
                        startActivity(intent);
                    }
                });
                container.addView(view);
                return view;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView((View) object);
            }
        });
        vvpGoods.setPageTransformer(true, new VerVPTransformer());
        initRgsIndicator();

//        WebSettings ws = webView.getSettings();
//        ws.setJavaScriptEnabled(true);
//        ws.setAllowFileAccess(true);
//        ws.setBuiltInZoomControls(false);
//        ws.setSupportZoom(false);
//        ws.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
//        webView.setWebViewClient(new WebViewClient() {
//            @Override
//            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                view.loadUrl(url);
//                return true;
//            }
//        });
//        webView.loadUrl(Constant.GOODS_COMMENT + "?token=" + SharedPreferencesUtils.getString(this, "token")
//                + "&type=" + 2 + "&id=" + id + "&price=" + sb);

        //viewpager监听
        vvpGoods.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position < rgsIndicator.getChildCount()) {
                    ((RadioButton) rgsIndicator.getChildAt(position)).setChecked(true);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


//        scrollContainer.getCurrentView(new ScrollViewContainer.CurrentPageListener() {
//            @Override
//            public void getCurrentPage(int page) {
//                if (page == 0) {
//                    rgsIndicator.setVisibility(View.VISIBLE);
//                    imgGoodsDetails.setVisibility(View.VISIBLE);
//                } else if (page == 1) {
//                    rgsIndicator.setVisibility(View.GONE);
//                    imgGoodsDetails.setVisibility(View.GONE);
//                    llGoodsDetails.setVisibility(View.GONE);
//                }
//            }
//        });

//        if (isSelect) {
//            selectGoodsPrice();
//        }
    }

    /**
     * 加载指示器
     */
    private void initRgsIndicator() {

        for (int i = 0; i < imgList.size(); i++) {
            RadioButton radioButton = new RadioButton(this);
            radioButton.setBackgroundResource(R.drawable.viewpager_indicator);
            radioButton.setButtonDrawable(null);
            RadioGroup.LayoutParams layoutParams = new RadioGroup.LayoutParams(15, 15);
            layoutParams.setMargins(0, 0, 0, 15);
            radioButton.setLayoutParams(layoutParams);
            rgsIndicator.addView(radioButton);
        }
        ((RadioButton) rgsIndicator.getChildAt(0)).setChecked(true);
    }

    /**
     * 初始化数据
     */
    private void initData() {
        viewHeaderLine.setVisibility(View.GONE);
        //首次进入向导页
        Boolean isFirst = SharedPreferencesUtils.getBoolean(this, "goods_guide", true);
        if (isFirst) {
            OkHttpUtils.get()
                    .url(Constant.GUIDE_IMG)
                    .addParams("id", "1")
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {

                        }

                        @Override
                        public void onResponse(String response, int id) {
                            guideBean = GsonUtils.jsonToBean(response, GuideBean.class);
                            if (guideBean.getData().getImg_urls() != null &&
                                    guideBean.getData().getImg_urls().size() > 0) {
                                imgGoodsGuide.setVisibility(View.VISIBLE);
                                Glide.with(GoodsDetailsActivity.this)
                                        .load(Constant.HOST + guideBean.getData().getImg_urls().get(0))
                                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                                        .into(imgGoodsGuide);
                            }
                        }
                    });
        }
        OkHttpUtils.get()
                .url(Constant.GOODS_DETAILS)
                .addParams("token", SharedPreferencesUtils.getString(this, "token"))
                .addParams("goods_id", id)
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
                            type = jsonObject1.getInt("type");
                            int isCollection = jsonObject1.getInt("is_collect");
                            switch (isCollection) {
                                case -1:
                                    imgAddCollection.setImageResource(R.mipmap.icon_cancel_like);
                                    break;
                                case 0:
                                    imgAddCollection.setImageResource(R.mipmap.icon_cancel_like);
                                    break;
                                case 1:
                                    imgAddCollection.setImageResource(R.mipmap.icon_add_like);
                                    break;
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (type == 1) {
                            llTailorDetails.setVisibility(View.VISIBLE);
                            llWorksDetails.setVisibility(View.GONE);
                            tailorEntity = GsonUtils.jsonToBean(response, TailorGoodsBean.class);
                            for (int i = 0; i < tailorEntity.getData().getImg_list().size(); i++) {
                                imgList.add(tailorEntity.getData().getImg_list().get(i));
                            }


                            initView(tailorEntity.getData().getName(), String.valueOf(
                                    tailorEntity.getData().getHeat()), tailorEntity.getData().getContent());
                        } else if (type == 2) {

                            llWorksDetails.setVisibility(View.VISIBLE);
                            llTailorDetails.setVisibility(View.GONE);

                            finishedGoodsBean = GsonUtils.jsonToBean(response, FinishedGoodsBean.class);
                            for (int i = 0; i < finishedGoodsBean.getData().getImg_list().size(); i++) {
                                imgList.add(finishedGoodsBean.getData().getImg_list().get(i));
                            }

                            initView(finishedGoodsBean.getData().getName(), String.valueOf(
                                    finishedGoodsBean.getData().getHeat()), finishedGoodsBean.getData().getContent());
                        }
                    }
                });


    }

    /**
     * 选择价格
     */
    private void selectGoodsPrice() {

        rlGoodsPrice.setVisibility(View.VISIBLE);
        rvGoodsPrice.setLayoutManager(new LinearLayoutManager(GoodsDetailsActivity.this));

        CommonAdapter<TailorGoodsBean.DataBean.PriceBean> priceAdapter = new CommonAdapter
                <TailorGoodsBean.DataBean.PriceBean>(GoodsDetailsActivity.this,
                R.layout.listitem_price_type, tailorEntity.getData().getPrice()) {
            @Override
            protected void convert(ViewHolder holder, TailorGoodsBean.DataBean.PriceBean priceBean, int position) {
                TextView tvPrice = holder.getView(R.id.tv_type_item);
                tvPrice.setTypeface(DisplayUtils.setTextType(GoodsDetailsActivity.this));
                tvPrice.setText("¥" + new DecimalFormat("#0.00").format(priceBean.getPrice()));
                holder.setText(R.id.tv_type_content, "(" + priceBean.getIntroduce() + ")");
            }
        };


        priceAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                rlGoodsPrice.setVisibility(View.GONE);
                Intent intent = new Intent(GoodsDetailsActivity.this, EmbroideryActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("id", id);
                bundle.putString("goods_name", tailorEntity.getData().getName());
                bundle.putString("img_url", imgUrl);
                bundle.putString("price", tailorEntity.getData().getPrice().get(position).getId() + "");
                bundle.putString("price_type", (position + 1) + "");
                bundle.putInt("classify_id", tailorEntity.getData().getClassify_id());
                intent.putExtras(bundle);
                startActivity(intent);
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });

        rvGoodsPrice.setAdapter(priceAdapter);
    }

    @OnClick({R.id.img_header_back, R.id.img_consult_server, R.id.tv_add_cart, R.id.tv_tailor_goods,
            R.id.img_goods_details, R.id.img_goods_close, R.id.rl_goods_price, R.id.img_consult_server_works,
            R.id.tv_buy_goods, R.id.tv_confirm_buy, R.id.view_select_type, R.id.tv_reduce_count,
            R.id.tv_add_count, R.id.img_header_share, R.id.img_add_collection, R.id.img_goods_guide})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_header_back:
                finish();
                break;
            case R.id.img_consult_server:
                ContactService.contactService(this);
                break;
            case R.id.tv_add_cart:
                index = 2;
                selectGoodsType();
                break;
            case R.id.tv_tailor_goods:
                if (TextUtils.isEmpty(SharedPreferencesUtils.getString(this, "token"))) {
                    startActivity(new Intent(this, LoginActivity.class));
                } else {
                    if (tailorEntity.getData().getIs_yuyue() == 1) {
                        selectGoodsPrice();
                    } else {
                        Intent intent = new Intent(this, MapViewActivity.class);
                        intent.putExtra("goods_name", tailorEntity.getData().getName());
                        startActivityForResult(intent, 1);
                    }
                }
                break;
            case R.id.img_goods_details:
                llGoodsDetails.setVisibility(View.VISIBLE);
                imgGoodsDetails.setVisibility(View.GONE);
                rgsIndicator.setVisibility(View.GONE);
                break;
            case R.id.img_goods_close:
                llGoodsDetails.setVisibility(View.GONE);
                imgGoodsDetails.setVisibility(View.VISIBLE);
                rgsIndicator.setVisibility(View.VISIBLE);
                break;
            case R.id.rl_goods_price:
                rlGoodsPrice.setVisibility(View.GONE);
                break;
            case R.id.img_consult_server_works:
                ContactService.contactService(this);
                break;
            case R.id.tv_buy_goods:
                index = 1;
                selectGoodsType();
                break;
            case R.id.tv_confirm_buy:
                if (!TextUtils.isEmpty(SharedPreferencesUtils.getString(this, "token"))) {
                    addToCart();
                } else {
                    startActivity(new Intent(this, LoginActivity.class));
                }
                break;
            case R.id.view_select_type:
                rlAddCart.setVisibility(View.GONE);
                count = 1;
                break;
            case R.id.tv_reduce_count:
                if (count > 1) {
                    count--;
                    tvSelectCount.setText(String.valueOf(count));
                }
                break;
            case R.id.tv_add_count:
                if (count < stock) {
                    count++;
                    tvSelectCount.setText(String.valueOf(count));
                }
                break;
            case R.id.img_header_share:
                switch (type) {
                    case 1:
                        ShareUtils.showShare(this, Constant.HOST + imgUrl,
                                tailorEntity.getData().getName(),
                                tailorEntity.getData().getContent(),
                                Constant.GOODS_SHARE + "?type=2&" + "id=" + id + "&token=" +
                                        SharedPreferencesUtils.getString(this, "token"));
                        break;
                    case 2:
                        ShareUtils.showShare(this, Constant.HOST + imgUrl,
                                finishedGoodsBean.getData().getName(),
                                finishedGoodsBean.getData().getContent(),
                                Constant.GOODS_SHARE + "?type=2&" + "id=" + id + "&token=" +
                                        SharedPreferencesUtils.getString(this, "token"));
                        break;
                }

                break;

            case R.id.img_add_collection:
                if (!TextUtils.isEmpty(SharedPreferencesUtils.getString(this, "token"))) {
                    addCollection();
                } else {
                    startActivity(new Intent(this, LoginActivity.class));
                }
                break;
            case R.id.img_goods_guide:
                if (isFirstGuide && guideBean.getData().getImg_urls().get(1) != null) {
                    Glide.with(GoodsDetailsActivity.this)
                            .load(Constant.HOST + guideBean.getData().getImg_urls().get(1))
                            .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                            .into(imgGoodsGuide);
                    isFirstGuide = false;
                } else {
                    imgGoodsGuide.setVisibility(View.GONE);
                    SharedPreferencesUtils.saveBoolean(this, "goods_guide", false);
                }
                break;
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
                                    MobclickAgent.onEvent(GoodsDetailsActivity.this, "collection");
                                    imgAddCollection.setImageResource(R.mipmap.icon_add_like);
                                    Toast.makeText(GoodsDetailsActivity.this,
                                            "收藏成功", Toast.LENGTH_SHORT).show();
//                                    webView.reload();
                                    break;
                                case "取消成功":
                                    imgAddCollection.setImageResource(R.mipmap.icon_cancel_like);
                                    Toast.makeText(GoodsDetailsActivity.this,
                                            "已取消收藏", Toast.LENGTH_SHORT).show();
//                                    webView.reload();
                                    break;

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });
    }

    /**
     * 加入购物车
     */
    private void addToCart() {

        OkHttpUtils.post()
                .url(Constant.ADD_CART)
                .addParams("token", SharedPreferencesUtils.getString(this, "token"))
                .addParams("type", String.valueOf(index))
                .addParams("goods_id", id)
                .addParams("goods_type", String.valueOf(2))
                .addParams("price", finishedGoodsBean.getData().getSize_list().get(currentSize)
                        .getSize_list().get(currentColor).getPrice())
                .addParams("goods_name", finishedGoodsBean.getData().getName())
                .addParams("goods_thumb", imgUrl)
                .addParams("size_ids", String.valueOf(finishedGoodsBean.getData().getSize_list()
                        .get(currentSize).getSize_list().get(currentColor).getId()))
                .addParams("size_content", finishedGoodsBean.getData().getSize_list()
                        .get(currentSize).getSize_list().get(currentColor).getName()
                        + ":" + finishedGoodsBean.getData().getSize_list().get(currentSize)
                        .getSize_list().get(currentColor).getName()
                        + ";" + finishedGoodsBean.getData().getSize_list().get(currentSize).getSize_name()
                        + ":" + finishedGoodsBean.getData().getSize_list().get(currentSize).getName()
                        + ";")
                .addParams("num", tvSelectCount.getText().toString().trim())
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
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (cartId != null) {
                            MobclickAgent.onEvent(GoodsDetailsActivity.this, "add_cart");
                            if (index == 1) {
                                Intent intent = new Intent(GoodsDetailsActivity.this,
                                        ConfirmOrderActivity.class);
                                intent.putExtra("cart_id", cartId);
                                rlAddCart.setVisibility(View.GONE);

                                count = 1;
                                startActivity(intent);

                            } else if (index == 2) {
                                Toast.makeText(GoodsDetailsActivity.this,
                                        "加入购物袋成功", Toast.LENGTH_SHORT).show();
                                rlAddCart.setVisibility(View.GONE);

                                count = 1;
                            }
                        }

                    }
                });


    }

    /**
     * 成品选择商品规格
     */
    private void selectGoodsType() {
        rlAddCart.setVisibility(View.VISIBLE);
        currentColor = 0;
        currentSize = 0;
        colorList.clear();
        if (finishedGoodsBean.getData().getSize_list().size() > 0 && finishedGoodsBean.getData().getSize_list() != null) {
            Glide.with(getApplicationContext())
                    .load(Constant.HOST + imgUrl)
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(imgGoodsIcon);

            tvGoodsPrice.setText("价格：¥" + finishedGoodsBean.getData().getSize_list().get(0)
                    .getSize_list().get(0).getPrice());
            tvGoodsStock.setText("库存：" + finishedGoodsBean.getData().getSize_list().get(0)
                    .getSize_list().get(0).getSku_num() + "件");
            tvSelectCount.setText("1");
            stock = finishedGoodsBean.getData().getSize_list().get(0).getSize_list().get(0).getSku_num();
            remainGoodsCount(finishedGoodsBean.getData().getSize_list().get(0).getSize_list().get(0).getSku_num());
            colorList.addAll(finishedGoodsBean.getData().getSize_list().get(0).getSize_list());

            //尺码
            rvGoodsSize.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
            final CommonAdapter<FinishedGoodsBean.DataBean.SizeListBean> sizeAdapter
                    = new CommonAdapter<FinishedGoodsBean.DataBean.SizeListBean>(GoodsDetailsActivity.this,
                    R.layout.listitem_works_size, finishedGoodsBean.getData().getSize_list()) {
                @Override
                protected void convert(ViewHolder holder, FinishedGoodsBean.DataBean.SizeListBean positionBean, int position) {
                    holder.setText(R.id.tv_works_size, finishedGoodsBean.getData().getSize_list().get(position).getName());

                    if (position == currentSize) {
                        holder.getView(R.id.tv_works_size).setBackgroundResource(R.mipmap.icon_checked_bg);
                        ((TextView) holder.getView(R.id.tv_works_size)).setTextColor(0xff2e2e2e);
                    } else {
                        holder.getView(R.id.tv_works_size).setBackgroundResource(R.drawable.text_gray_bg);
                        ((TextView) holder.getView(R.id.tv_works_size)).setTextColor(0xffaaaaaa);
                    }
                }
            };
            rvGoodsSize.setAdapter(sizeAdapter);

            sizeAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                    currentSize = holder.getAdapterPosition();
                    currentColor = 0;
                    colorList.clear();
                    colorList.addAll(finishedGoodsBean.getData().getSize_list().get(currentSize).getSize_list());
                    sizeAdapter.notifyDataSetChanged();
                    colorAdapter.notifyDataSetChanged();
                    tvGoodsPrice.setText("价格：¥" + finishedGoodsBean.getData().getSize_list()
                            .get(currentSize).getSize_list().get(currentColor).getPrice());
                    tvGoodsStock.setText("库存：" + finishedGoodsBean.getData().getSize_list()
                            .get(currentSize).getSize_list().get(currentColor).getSku_num() + "件");
                    stock = finishedGoodsBean.getData().getSize_list().get(currentSize)
                            .getSize_list().get(currentColor).getSku_num();
                    remainGoodsCount(finishedGoodsBean.getData().getSize_list().get(currentSize)
                            .getSize_list().get(currentColor).getSku_num());
                    count = 1;
                    tvSelectCount.setText(String.valueOf(count));
                }

                @Override
                public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                    return false;
                }
            });


            //颜色
            rvGoodsColor.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
            colorAdapter = new CommonAdapter<FinishedGoodsBean.DataBean.SizeListBean.ItemListBean>
                    (GoodsDetailsActivity.this, R.layout.listitem_works_size, colorList) {
                @Override
                protected void convert(ViewHolder holder, FinishedGoodsBean.DataBean.SizeListBean
                        .ItemListBean positionBean, int position) {
                    holder.setText(R.id.tv_works_size, positionBean.getName());
                    if (position == currentColor) {
                        holder.getView(R.id.tv_works_size).setBackgroundResource(R.mipmap.icon_checked_bg);
                        ((TextView) holder.getView(R.id.tv_works_size)).setTextColor(0xff2e2e2e);
                    } else {
                        holder.getView(R.id.tv_works_size).setBackgroundResource(R.drawable.text_gray_bg);
                        ((TextView) holder.getView(R.id.tv_works_size)).setTextColor(0xffaaaaaa);
                    }
                }
            };
            rvGoodsColor.setAdapter(colorAdapter);
            colorAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                    currentColor = holder.getAdapterPosition();
                    colorAdapter.notifyDataSetChanged();
                    tvGoodsPrice.setText("价格：¥" + finishedGoodsBean.getData().getSize_list()
                            .get(currentSize).getSize_list().get(currentColor).getPrice());
                    tvGoodsStock.setText("库存：" + finishedGoodsBean.getData().getSize_list()
                            .get(currentSize).getSize_list().get(currentColor).getSku_num() + "件");
                    stock = finishedGoodsBean.getData().getSize_list().get(currentSize)
                            .getSize_list().get(currentColor).getSku_num();
                    remainGoodsCount(finishedGoodsBean.getData().getSize_list().get(currentSize)
                            .getSize_list().get(currentColor).getSku_num());
                    count = 1;
                    tvSelectCount.setText(String.valueOf(count));
                }

                @Override
                public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                    return false;
                }
            });

        }

    }

    /**
     * 剩余库存
     *
     * @param count
     */
    private void remainGoodsCount(int count) {
        if (count == 0) {
            tvConfirmBuy.setEnabled(false);
            tvConfirmBuy.setBackgroundColor(0xffbdbdbd);
        } else {
            tvConfirmBuy.setEnabled(true);
            tvConfirmBuy.setBackgroundColor(0xff2e2e2e);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (llGoodsDetails.getVisibility() == View.VISIBLE) {
                llGoodsDetails.setVisibility(View.GONE);
                imgGoodsDetails.setVisibility(View.VISIBLE);
                rgsIndicator.setVisibility(View.VISIBLE);
                return false;
            }
            if (rlGoodsPrice.getVisibility() == View.VISIBLE) {
                rlGoodsPrice.setVisibility(View.GONE);
                return false;
            }

            if (rlAddCart.getVisibility() == View.VISIBLE) {
                rlAddCart.setVisibility(View.GONE);
                count = 1;
                return false;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == 1) {
            selectGoodsPrice();
        }
    }
}
