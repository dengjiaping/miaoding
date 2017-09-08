package cn.cloudworkshop.miaoding.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

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
import cn.cloudworkshop.miaoding.jazzyviewpager.JazzyViewPager;
import cn.cloudworkshop.miaoding.utils.DisplayUtils;
import cn.cloudworkshop.miaoding.utils.GsonUtils;
import cn.cloudworkshop.miaoding.utils.LoadErrorUtils;
import cn.cloudworkshop.miaoding.utils.LogUtils;
import cn.cloudworkshop.miaoding.utils.ShareUtils;
import cn.cloudworkshop.miaoding.utils.SharedPreferencesUtils;
import cn.cloudworkshop.miaoding.utils.ToastUtils;
import cn.cloudworkshop.miaoding.view.CircleImageView;
import cn.cloudworkshop.miaoding.view.TyperTextView;
import okhttp3.Call;


/**
 * Author：binge on 2017-09-08 13:34
 * Email：1993911441@qq.com
 * Describe：
 */
public class NewWorksActivity extends BaseActivity {
    @BindView(R.id.vp_works_detail)
    JazzyViewPager vpWorks;
    @BindView(R.id.img_add_bag)
    ImageView imgAddBag;
    @BindView(R.id.img_buy_works)
    ImageView imgBuyWorks;
    @BindView(R.id.rgs_indicator_works)
    RadioGroup rgsIndicator;
    @BindView(R.id.img_goods_back)
    ImageView imgBack;
    @BindView(R.id.img_goods_share)
    ImageView imgShare;
    @BindView(R.id.tv_content_goods)
    TyperTextView tvContent;

    //商品id
    private String id;

    private WorksDetailBean worksBean;

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
        setContentView(R.layout.activity_works_new);
        ButterKnife.bind(this);
        getData();
        initData();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
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
                .url(Constant.GOODS_DETAILS)
                .addParams("token", SharedPreferencesUtils.getStr(this, "token"))
                .addParams("goods_id", id)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        LoadErrorUtils.showDialog(NewWorksActivity.this, new LoadErrorUtils.OnRefreshListener() {
                            @Override
                            public void onRefresh() {
                                initData();
                            }
                        });
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        worksBean = GsonUtils.jsonToBean(response, WorksDetailBean.class);
                        if (worksBean.getData() != null) {
                            initView();
                        }
                    }
                });

    }

    /**
     * 加载视图
     */
    private void initView() {
        String str = worksBean.getData().getContent();
        String sb = "<font><big><big>" +
                str.charAt(0) +
                "</big></big></font>" +
                str.substring(1);

        tvContent.animateText(Html.fromHtml(sb));

        vpWorks.setOffscreenPageLimit(worksBean.getData().getImg_list().size());
        vpWorks.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return worksBean.getData().getImg_list().size();
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public Object instantiateItem(ViewGroup container, final int position) {
                View view = LayoutInflater.from(NewWorksActivity.this)
                        .inflate(R.layout.viewpager_goods_details, null);
                final ImageView img = (ImageView) view.findViewById(R.id.img_goods_picture);
                Glide.with(NewWorksActivity.this)
                        .load(Constant.HOST + worksBean.getData().getImg_list().get(position))
                        .centerCrop()
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .into(img);
                container.addView(view);
                vpWorks.setObjectForPosition(view, position);

                return view;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView((View) object);
            }
        });
        vpWorks.setCurrentItem(0);

        for (int i = 0; i < worksBean.getData().getImg_list().size(); i++) {
            RadioButton radioButton = new RadioButton(this);

            RadioGroup.LayoutParams layoutParams = new RadioGroup.LayoutParams(18, 18);
            layoutParams.setMargins(10, 10, 10, 10);
            radioButton.setLayoutParams(layoutParams);
            radioButton.setButtonDrawable(null);
            radioButton.setClickable(false);
            radioButton.setBackgroundResource(R.drawable.viewpager_indicator);
            rgsIndicator.addView(radioButton);
        }
        ((RadioButton) rgsIndicator.getChildAt(0)).setChecked(true);

        vpWorks.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                tvContent.setAlpha(1 - (float) positionOffsetPixels / vpWorks.getWidth());
            }

            @Override
            public void onPageSelected(int position) {
                ((RadioButton) rgsIndicator.getChildAt(position)).setChecked(true);

                String str = worksBean.getData().getContent();
                String sb = "<font><big><big>" +
                        str.charAt(0) +
                        "</big></big></font>" +
                        str.substring(1);
                tvContent.setAlpha(1);
                tvContent.animateText(Html.fromHtml(sb));


            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


    }

    @OnClick({R.id.img_add_bag, R.id.img_buy_works, R.id.img_goods_back, R.id.img_goods_share})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_add_bag:
                index = 2;
                showWorksType();
                break;
            case R.id.img_buy_works:
                index = 1;
                showWorksType();
                break;
            case R.id.img_goods_back:
                finish();
                break;
            case R.id.img_goods_share:
                if (worksBean != null) {
                    ShareUtils.showShare(this, Constant.HOST + worksBean.getData().getThumb(),
                            worksBean.getData().getName(), worksBean.getData().getContent(),
                            Constant.WORKS_SHARE + "?content=2&id=" + id);
                }
                break;
        }
    }


    /**
     * 商品规格
     */
    private void showWorksType() {
        if (worksBean != null) {
            View contentView = LayoutInflater.from(this).inflate(R.layout.ppw_select_type, null);
            mPopupWindow = new PopupWindow(contentView,
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            mPopupWindow.setContentView(contentView);
            mPopupWindow.setTouchable(true);
            mPopupWindow.setFocusable(true);
            mPopupWindow.setOutsideTouchable(true);
            mPopupWindow.setBackgroundDrawable(new BitmapDrawable(getResources(), (Bitmap) null));
            if (!isFinishing()) {
                mPopupWindow.showAtLocation(getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
            }

            DisplayUtils.setBackgroundAlpha(this, true);
            mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    DisplayUtils.setBackgroundAlpha(NewWorksActivity.this, false);
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
                    .load(Constant.HOST + worksBean.getData().getThumb())
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(imageView);
            tvPrice.setTypeface(DisplayUtils.setTextType(this));
            if (worksBean.getData().getSize_list() != null) {
                tvPrice.setText("¥" + worksBean.getData().getSize_list().get(0).getSize_list().get(0).getPrice());
                tvStock.setText("库存：" + worksBean.getData().getSize_list().get(0).getSize_list()
                        .get(0).getSku_num() + "件");
                tvCount.setText("1");
                currentSize = 0;
                currentColor = 0;
                stock = worksBean.getData().getSize_list().get(0).getSize_list().get(0).getSku_num();
                remainGoodsCount(stock);
                colorList.addAll(worksBean.getData().getSize_list().get(0).getSize_list());

                //尺码
                rvSize.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
                final CommonAdapter<WorksDetailBean.DataBean.SizeListBeanX> sizeAdapter
                        = new CommonAdapter<WorksDetailBean.DataBean.SizeListBeanX>(NewWorksActivity.this,
                        R.layout.listitem_works_size, worksBean.getData().getSize_list()) {
                    @Override
                    protected void convert(ViewHolder holder, WorksDetailBean.DataBean.SizeListBeanX positionBean, int position) {
                        TextView tvSize = holder.getView(R.id.tv_works_size);
                        tvSize.setText(positionBean.getSize_name());

                        if (currentSize == position) {
                            tvSize.setTextColor(Color.WHITE);
                            tvSize.setBackgroundResource(R.drawable.circle_black);

                        } else {
                            tvSize.setTextColor(ContextCompat.getColor(NewWorksActivity.this, R.color.dark_gray_22));
                            tvSize.setBackgroundResource(R.drawable.ring_gray);
                        }
                    }
                };
                rvSize.setAdapter(sizeAdapter);

                sizeAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                        currentSize = holder.getLayoutPosition();
                        currentColor = 0;
                        colorList.clear();
                        colorList.addAll(worksBean.getData().getSize_list().get(currentSize).getSize_list());
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
                        (NewWorksActivity.this, R.layout.listitem_works_color, colorList) {
                    @Override
                    protected void convert(ViewHolder holder, WorksDetailBean.DataBean.SizeListBeanX.SizeListBean
                            positionBean, int position) {
                        CircleImageView imgColor = holder.getView(R.id.img_works_color);
                        CircleImageView imgMask = holder.getView(R.id.img_works_mask);
                        Glide.with(NewWorksActivity.this)
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
                        currentColor = holder.getLayoutPosition();
                        ToastUtils.showToast(NewWorksActivity.this, colorList.get(position).getColor_name());
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
                        if (!TextUtils.isEmpty(SharedPreferencesUtils.getStr(NewWorksActivity.this, "token"))) {
                            addToCart();
                        } else {
                            Intent login = new Intent(NewWorksActivity.this, LoginActivity.class);
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

    }

    /**
     * 重新选择规格，刷新页面
     */
    private void reSelectWorks() {
        colorAdapter.notifyDataSetChanged();
        tvPrice.setTypeface(DisplayUtils.setTextType(NewWorksActivity.this));
        tvPrice.setText("¥" + worksBean.getData().getSize_list().get(currentSize).getSize_list()
                .get(currentColor).getPrice());
        tvStock.setText("库存：" + worksBean.getData().getSize_list()
                .get(currentSize).getSize_list().get(currentColor).getSku_num() + "件");
        stock = worksBean.getData().getSize_list().get(currentSize)
                .getSize_list().get(currentColor).getSku_num();
        remainGoodsCount(worksBean.getData().getSize_list().get(currentSize)
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
                .addParams("token", SharedPreferencesUtils.getStr(this, "token"))
                .addParams("type", index + "")
                .addParams("goods_id", id)
                .addParams("goods_type", "2")
                .addParams("price", worksBean.getData().getSize_list().get(currentSize)
                        .getSize_list().get(currentColor).getPrice())
                .addParams("goods_name", worksBean.getData().getName())
                .addParams("goods_thumb", worksBean.getData().getThumb())
                .addParams("size_ids", String.valueOf(worksBean.getData().getSize_list()
                        .get(currentSize).getSize_list().get(currentColor).getId()))
                .addParams("size_content", "颜色:" + worksBean.getData().getSize_list().get(currentSize)
                        .getSize_list().get(currentColor).getColor_name() + ";尺码:" + worksBean.getData()
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
                            MobclickAgent.onEvent(NewWorksActivity.this, "add_cart");
                            if (index == 1) {
                                Intent intent = new Intent(NewWorksActivity.this,
                                        ConfirmOrderActivity.class);
                                intent.putExtra("cart_id", cartId);
                                mPopupWindow.dismiss();

                                startActivity(intent);

                            } else if (index == 2) {
                                ToastUtils.showToast(NewWorksActivity.this, "加入购物袋成功");
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

}
