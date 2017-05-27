package cn.cloudworkshop.miaoding.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
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

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.cloudworkshop.miaoding.R;
import cn.cloudworkshop.miaoding.base.BaseActivity;
import cn.cloudworkshop.miaoding.bean.WorksDetailBean;
import cn.cloudworkshop.miaoding.constant.Constant;
import cn.cloudworkshop.miaoding.utils.ContactService;
import cn.cloudworkshop.miaoding.utils.DisplayUtils;
import cn.cloudworkshop.miaoding.utils.GsonUtils;
import cn.cloudworkshop.miaoding.utils.ShareUtils;
import cn.cloudworkshop.miaoding.utils.SharedPreferencesUtils;
import cn.cloudworkshop.miaoding.view.CircleImageView;
import cn.cloudworkshop.miaoding.view.ScrollViewContainer;
import okhttp3.Call;

/**
 * Author：binge on 2017-04-21 11:58
 * Email：1993911441@qq.com
 * Describe：作品详情
 */
public class
WorksDetailActivity extends BaseActivity {
    @BindView(R.id.img_goods_like)
    ImageView imgAddLike;
    @BindView(R.id.img_goods_consult)
    ImageView imgConsult;
    @BindView(R.id.ll_buy_works)
    LinearLayout llBuyWorks;
    @BindView(R.id.img_works)
    ImageView imgWorks;
    @BindView(R.id.img_works_designer)
    CircleImageView imgDesigner;
    @BindView(R.id.tv_name_works)
    TextView tvDesignerName;
    @BindView(R.id.tv_works_feature)
    TextView tvDesignerFeature;
    @BindView(R.id.tv_content_works)
    TextView tvDesignerInfo;
    @BindView(R.id.img_works_details)
    ImageView imgDetails;
    @BindView(R.id.img_back_works)
    ImageView imgBack;
    @BindView(R.id.img_share_works)
    ImageView imgShare;
    @BindView(R.id.scroll_container_works)
    ScrollViewContainer scrollContainer;
    @BindView(R.id.tv_works_cart)
    TextView tvAddCart;
    @BindView(R.id.tv_works_buy)
    TextView tvBuyWorks;

    private String id;

    private WorksDetailBean.DataBean worksBean;

    private List<WorksDetailBean.DataBean.SizeListBeanX.SizeListBean> colorList = new ArrayList<>();
    private CommonAdapter<WorksDetailBean.DataBean.SizeListBeanX.SizeListBean> colorAdapter;
    //库存
    private int stock;
    //购买数量
    private int count = 1;
    //尺码
    private int currentSize = 0;
    //颜色
    private int currentColor = 0;
    //1、直接购买  2、加入购物车
    private int index;
    private PopupWindow mPopupWindow;
    //购物车id
    private String cartId;
    private TextView tvPrice;
    private TextView tvStock;
    private TextView tvCount;
    private TextView tvBuy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_works_detail);
        ButterKnife.bind(this);
        getData();
        initData();
    }

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
                        worksBean = GsonUtils.jsonToBean(response, WorksDetailBean.class).getData();
                        if (worksBean != null) {
                            initView();
                        }
                    }
                });

    }

    /**
     * 加载视图
     */
    private void initView() {

        Glide.with(getApplicationContext())
                .load(Constant.HOST + worksBean.getThumb())
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(imgWorks);
        Glide.with(getApplicationContext())
                .load(Constant.HOST + worksBean.getDesigner().getAvatar())
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(imgDesigner);

        if (worksBean.getIs_collect() == 1) {
            imgAddLike.setImageResource(R.mipmap.icon_add_like);
        } else {
            imgAddLike.setImageResource(R.mipmap.icon_cancel_like);
        }

        tvDesignerName.setTypeface(DisplayUtils.setTextType(this));
        tvDesignerName.setText(worksBean.getDesigner().getName());
        tvDesignerFeature.setText(worksBean.getDesigner().getTag());
        tvDesignerInfo.setText(worksBean.getDesigner().getIntroduce());

        Glide.with(getApplicationContext())
                .load(Constant.HOST + worksBean.getContent2())
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(imgDetails);


        scrollContainer.getCurrentView(new ScrollViewContainer.CurrentPageListener() {
            @Override
            public void getCurrentPage(int page) {
                if (page == 0) {
                    llBuyWorks.setVisibility(View.GONE);
                } else {
                    llBuyWorks.setVisibility(View.VISIBLE);
                }
            }
        });

    }

    @OnClick({R.id.img_goods_like, R.id.img_goods_consult, R.id.img_back_works, R.id.img_share_works,
            R.id.tv_works_cart, R.id.tv_works_buy, R.id.img_works_designer})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_goods_like:
                if (!TextUtils.isEmpty(SharedPreferencesUtils.getString(this, "token"))) {
                    addCollection();
                } else {
                    Intent login = new Intent(this, LoginActivity.class);
                    login.putExtra("page_name", "收藏");
                    startActivity(login);
                }
                break;
            case R.id.img_goods_consult:
                ContactService.contactService(this);
                break;
            case R.id.img_back_works:
                finish();
                break;
            case R.id.img_share_works:
                ShareUtils.showShare(this, Constant.HOST + worksBean.getThumb(),
                        worksBean.getName(),
                        worksBean.getContent(),
                        Constant.DESIGNER_WORKS_SHARE + "?content=2&id=" + id + "&token=" +
                                SharedPreferencesUtils.getString(this, "token"));
                break;
            case R.id.tv_works_cart:
                index = 2;
                showWorksType();
                break;
            case R.id.tv_works_buy:
                index = 1;
                showWorksType();
                break;
            case R.id.img_works_designer:
                Intent intent = new Intent(this, DesignerInfoActivity.class);
                intent.putExtra("id", worksBean.getDesigner().getId() + "");
                startActivity(intent);
                break;

        }
    }

    /**
     * 商品规格
     */
    private void showWorksType() {

        View contentView = LayoutInflater.from(this).inflate(R.layout.ppw_select_type, null);
        mPopupWindow = new PopupWindow(contentView,
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mPopupWindow.setContentView(contentView);
        mPopupWindow.setTouchable(true);
        mPopupWindow.setFocusable(true);
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable(getResources(), (Bitmap) null));
        mPopupWindow.showAtLocation(getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
        DisplayUtils.setBackgroundAlpha(this, true);
        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                DisplayUtils.setBackgroundAlpha(WorksDetailActivity.this, false);
                currentColor = 0;
                currentSize = 0;
                count = 1;
                colorList.clear();
            }
        });

        final ImageView imageView = (ImageView) contentView.findViewById(R.id.img_works_icon);
        tvPrice = (TextView) contentView.findViewById(R.id.tv_works_price);
        tvStock = (TextView) contentView.findViewById(R.id.tv_works_stock);
        ImageView imgCancel = (ImageView) contentView.findViewById(R.id.img_cancel_buy);
        RecyclerView rvSize = (RecyclerView) contentView.findViewById(R.id.rv_works_size);
        RecyclerView rvColor = (RecyclerView) contentView.findViewById(R.id.rv_works_color);
        TextView tvReduce = (TextView) contentView.findViewById(R.id.tv_reduce_works);
        tvCount = (TextView) contentView.findViewById(R.id.tv_buy_count);
        TextView tvAdd = (TextView) contentView.findViewById(R.id.tv_add_works);
        tvBuy = (TextView) contentView.findViewById(R.id.tv_buy_works);

        Glide.with(getApplicationContext())
                .load(Constant.HOST + worksBean.getThumb())
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(imageView);
        tvPrice.setTypeface(DisplayUtils.setTextType(this));
        if (worksBean.getSize_list() != null) {
            tvPrice.setText("¥" + worksBean.getSize_list().get(0).getSize_list().get(0).getPrice());
            tvStock.setText("库存：" + worksBean.getSize_list().get(0).getSize_list().get(0).getSku_num() + "件");
            tvCount.setText("1");
            currentSize = 0;
            currentColor = 0;
            stock = worksBean.getSize_list().get(0).getSize_list().get(0).getSku_num();
            remainGoodsCount(stock);
            colorList.addAll(worksBean.getSize_list().get(0).getSize_list());

            //尺码
            rvSize.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
            final CommonAdapter<WorksDetailBean.DataBean.SizeListBeanX> sizeAdapter
                    = new CommonAdapter<WorksDetailBean.DataBean.SizeListBeanX>(WorksDetailActivity.this,
                    R.layout.listitem_works_size, worksBean.getSize_list()) {
                @Override
                protected void convert(ViewHolder holder, WorksDetailBean.DataBean.SizeListBeanX positionBean, int position) {
                    TextView tvSize = holder.getView(R.id.tv_works_size);
                    tvSize.setText(positionBean.getSize_name());

                    if (currentSize == position) {
                        tvSize.setTextColor(Color.WHITE);
                        tvSize.setBackgroundResource(R.drawable.circle_black);

                    } else {
                        tvSize.setTextColor(ContextCompat.getColor(WorksDetailActivity.this, R.color.dark_gray_22));
                        tvSize.setBackgroundResource(R.drawable.ring_gray);
                    }
                }
            };
            rvSize.setAdapter(sizeAdapter);

            sizeAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                    currentSize = holder.getAdapterPosition();
                    currentColor = 0;
                    colorList.clear();
                    colorList.addAll(worksBean.getSize_list().get(currentSize).getSize_list());
                    sizeAdapter.notifyDataSetChanged();

                    reSelectWorks();
                }

                @Override
                public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                    return false;
                }
            });


            //颜色
            rvColor.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
            colorAdapter = new CommonAdapter<WorksDetailBean.DataBean.SizeListBeanX.SizeListBean>
                    (WorksDetailActivity.this, R.layout.listitem_works_color, colorList) {
                @Override
                protected void convert(ViewHolder holder, WorksDetailBean.DataBean.SizeListBeanX.SizeListBean
                        positionBean, int position) {
                    CircleImageView imgColor = holder.getView(R.id.img_works_color);
                    CircleImageView imgMask = holder.getView(R.id.img_works_mask);
                    Glide.with(WorksDetailActivity.this)
                            .load(Constant.HOST + positionBean.getColor_img())
                            .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                            .into(imgColor);
                    if (currentColor == position) {
                        imgMask.setVisibility(View.VISIBLE);
                    } else {
                        imgMask.setVisibility(View.GONE);
                    }
                }
            };
            rvColor.setAdapter(colorAdapter);
            colorAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                    currentColor = holder.getAdapterPosition();
                    Toast.makeText(WorksDetailActivity.this, colorList.get(position).getColor_name(),
                            Toast.LENGTH_SHORT).show();
                    reSelectWorks();
                }

                @Override
                public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                    return false;
                }
            });


            //增加数量
            tvAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (count < stock) {
                        count++;
                        tvCount.setText(String.valueOf(count));
                    }
                }
            });

            //减少数量
            tvReduce.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (count > 1) {
                        count--;
                        tvCount.setText(String.valueOf(count));
                    }
                }
            });

            //确定购买
            tvBuy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!TextUtils.isEmpty(SharedPreferencesUtils.getString(WorksDetailActivity.this, "token"))) {
                        addToCart();
                    } else {
                        Intent login = new Intent(WorksDetailActivity.this, LoginActivity.class);
                        login.putExtra("page_name", "立即购买");
                        startActivity(login);
                    }
                }
            });
        }

        //取消购买
        imgCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopupWindow.dismiss();
            }
        });


    }

    /**
     * 重新选择规格，刷新页面
     */
    private void reSelectWorks() {
        colorAdapter.notifyDataSetChanged();
        tvPrice.setTypeface(DisplayUtils.setTextType(WorksDetailActivity.this));
        tvPrice.setText("¥" + worksBean.getSize_list().get(currentSize).getSize_list()
                .get(currentColor).getPrice());
        tvStock.setText("库存：" + worksBean.getSize_list()
                .get(currentSize).getSize_list().get(currentColor).getSku_num() + "件");
        stock = worksBean.getSize_list().get(currentSize)
                .getSize_list().get(currentColor).getSku_num();
        remainGoodsCount(worksBean.getSize_list().get(currentSize)
                .getSize_list().get(currentColor).getSku_num());
        count = 1;
        tvCount.setText(String.valueOf(count));
    }

    /**
     * 加入购物车
     */
    private void addToCart() {

        OkHttpUtils.post()
                .url(Constant.ADD_CART)
                .addParams("token", SharedPreferencesUtils.getString(this, "token"))
                .addParams("content", index + "")
                .addParams("goods_id", id)
                .addParams("goods_type", "2")
                .addParams("price", worksBean.getSize_list().get(currentSize)
                        .getSize_list().get(currentColor).getPrice())
                .addParams("goods_name", worksBean.getName())
                .addParams("goods_thumb", worksBean.getThumb())
                .addParams("size_ids", String.valueOf(worksBean.getSize_list().get(currentSize)
                        .getSize_list().get(currentColor).getId()))
                .addParams("size_content", "颜色:" + worksBean.getSize_list().get(currentSize)
                        .getSize_list().get(currentColor).getColor_name() + ";尺码:" + worksBean
                        .getSize_list().get(currentSize).getSize_name())

                .addParams("num", tvCount.getText().toString().trim())
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
                            MobclickAgent.onEvent(WorksDetailActivity.this, "add_cart");
                            if (index == 1) {
                                Intent intent = new Intent(WorksDetailActivity.this,
                                        ConfirmOrderActivity.class);
                                intent.putExtra("cart_id", cartId);
                                mPopupWindow.dismiss();

                                startActivity(intent);

                            } else if (index == 2) {
                                Toast.makeText(WorksDetailActivity.this, "加入购物袋成功",
                                        Toast.LENGTH_SHORT).show();
                                mPopupWindow.dismiss();
                            }
                        }

                    }
                });

    }

    /**
     * 剩余库存
     *
     * @param counts
     */
    private void remainGoodsCount(int counts) {
        if (counts == 0) {
            tvBuy.setEnabled(false);
            tvBuy.setBackgroundColor(0xffbdbdbd);
        } else {
            tvBuy.setEnabled(true);
            tvBuy.setBackgroundColor(0xff2e2e2e);
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
                                    MobclickAgent.onEvent(WorksDetailActivity.this, "collection");
                                    imgAddLike.setImageResource(R.mipmap.icon_add_like);
                                    Toast.makeText(WorksDetailActivity.this,
                                            "收藏成功", Toast.LENGTH_SHORT).show();
                                    break;
                                case "取消成功":
                                    imgAddLike.setImageResource(R.mipmap.icon_cancel_like);
                                    Toast.makeText(WorksDetailActivity.this,
                                            "已取消收藏", Toast.LENGTH_SHORT).show();

                                    break;

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });
    }

}
