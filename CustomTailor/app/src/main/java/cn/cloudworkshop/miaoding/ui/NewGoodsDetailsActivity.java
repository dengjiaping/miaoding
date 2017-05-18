package cn.cloudworkshop.miaoding.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import cn.cloudworkshop.miaoding.base.BaseActivity;
import cn.cloudworkshop.miaoding.bean.TailorGoodsBean;
import cn.cloudworkshop.miaoding.constant.Constant;
import cn.cloudworkshop.miaoding.utils.ContactService;
import cn.cloudworkshop.miaoding.utils.DisplayUtils;
import cn.cloudworkshop.miaoding.utils.GsonUtils;
import cn.cloudworkshop.miaoding.utils.NetworkImageHolderView;
import cn.cloudworkshop.miaoding.utils.ShareUtils;
import cn.cloudworkshop.miaoding.utils.SharedPreferencesUtils;
import cn.cloudworkshop.miaoding.view.ScrollViewContainer;
import okhttp3.Call;


/**
 * Author：binge on 2017-04-18 10:59
 * Email：1993911441@qq.com
 * Describe：定制商品详情界面（new）
 */
public class NewGoodsDetailsActivity extends BaseActivity {
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
    @BindView(R.id.rv_tailor_price)
    RecyclerView rvTailor;
    @BindView(R.id.rl_tailor_price)
    RelativeLayout rlTailor;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_details_new);
        ButterKnife.bind(this);
        getData();
        initData();
    }

    /**
     * 商品详情
     */
    private void getData() {
        id = getIntent().getStringExtra("id");
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
//                Intent intent = new Intent(NewGoodsDetailsActivity.this, ImagePreviewActivity.class);
//                intent.putExtra("currentPos", position);
//                intent.putStringArrayListExtra("img_list", tailorBean.getImg_list());
//                startActivity(intent);
//            }
//        });
        Glide.with(getApplicationContext())
                .load(Constant.HOST + tailorBean.getContent2())
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(imgDetails);

//        imgGoodsDetail.setImageURI(Constant.HOST + tailorBean.getContent2());


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

        rlTailor.setVisibility(View.VISIBLE);
        rvTailor.setLayoutManager(new LinearLayoutManager(NewGoodsDetailsActivity.this));

        CommonAdapter<TailorGoodsBean.DataBean.PriceBean> priceAdapter = new CommonAdapter
                <TailorGoodsBean.DataBean.PriceBean>(NewGoodsDetailsActivity.this,
                R.layout.listitem_price_type, tailorBean.getPrice()) {
            @Override
            protected void convert(ViewHolder holder, TailorGoodsBean.DataBean.PriceBean priceBean, int position) {
                TextView tvPrice = holder.getView(R.id.tv_type_item);
                tvPrice.setTypeface(DisplayUtils.setTextType(NewGoodsDetailsActivity.this));
                tvPrice.setText("¥" + new DecimalFormat("#0.00").format(priceBean.getPrice()));
                holder.setText(R.id.tv_type_content, priceBean.getIntroduce());
            }

        };


        priceAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                rlTailor.setVisibility(View.GONE);
                Intent intent = new Intent(NewGoodsDetailsActivity.this, NewTailorActivity.class);
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


    @OnClick({R.id.tv_goods_tailor, R.id.img_tailor_back, R.id.rl_tailor_price, R.id.img_add_like,
            R.id.img_tailor_consult, R.id.img_tailor_share})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_goods_tailor:
                if (TextUtils.isEmpty(SharedPreferencesUtils.getString(this, "token"))) {
                    startActivity(new Intent(this, LoginActivity.class));
                } else {
                    selectGoodsPrice();
//                    if (tailorBean.getIs_yuyue() == 1) {
//                        selectGoodsPrice();
//                    } else {
//                        Intent intent = new Intent(this, MapViewActivity.class);
//                        intent.putExtra("goods_name", tailorBean.getName());
//                        startActivityForResult(intent, 1);
//                    }
                }
                break;
            case R.id.img_tailor_back:
                finish();
                break;
            case R.id.rl_tailor_price:
                rlTailor.setVisibility(View.GONE);
                break;
            case R.id.img_add_like:
                if (!TextUtils.isEmpty(SharedPreferencesUtils.getString(this, "token"))) {
                    addCollection();
                } else {
                    startActivity(new Intent(this, LoginActivity.class));
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
                                    MobclickAgent.onEvent(NewGoodsDetailsActivity.this, "collection");
                                    imgAddLike.setImageResource(R.mipmap.icon_add_like);
                                    Toast.makeText(NewGoodsDetailsActivity.this,
                                            "收藏成功", Toast.LENGTH_SHORT).show();
                                    break;
                                case "取消成功":
                                    imgAddLike.setImageResource(R.mipmap.icon_cancel_like);
                                    Toast.makeText(NewGoodsDetailsActivity.this,
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
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (rlTailor.getVisibility() == View.VISIBLE) {
                rlTailor.setVisibility(View.GONE);
                return false;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == 1) {
            tailorBean.setIs_yuyue(1);
            selectGoodsPrice();
        }
    }

}
