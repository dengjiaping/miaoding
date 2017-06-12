package cn.cloudworkshop.miaoding.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
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
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.cloudworkshop.miaoding.R;
import cn.cloudworkshop.miaoding.base.BaseActivity;
import cn.cloudworkshop.miaoding.bean.CustomGoodsBean;
import cn.cloudworkshop.miaoding.bean.TailorItemBean;
import cn.cloudworkshop.miaoding.constant.Constant;
import cn.cloudworkshop.miaoding.utils.ContactService;
import cn.cloudworkshop.miaoding.utils.DateUtils;
import cn.cloudworkshop.miaoding.utils.DisplayUtils;
import cn.cloudworkshop.miaoding.utils.GsonUtils;
import cn.cloudworkshop.miaoding.utils.LogUtils;
import cn.cloudworkshop.miaoding.utils.NetworkImageHolderView;
import cn.cloudworkshop.miaoding.utils.ShareUtils;
import cn.cloudworkshop.miaoding.utils.SharedPreferencesUtils;
import cn.cloudworkshop.miaoding.view.CircleImageView;
import cn.cloudworkshop.miaoding.view.ScrollViewContainer;
import okhttp3.Call;


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
    @BindView(R.id.tv_custom_goods)
    TextView tvCustomGoods;
    @BindView(R.id.tv_collect_count)
    TextView tvCollectCount;
    @BindView(R.id.rv_collect_user)
    RecyclerView rvCollectUser;
    @BindView(R.id.tv_comment_count)
    TextView tvCommentCount;
    @BindView(R.id.tv_all_evaluate)
    TextView tvAllEvaluate;
    @BindView(R.id.img_user_avatar)
    CircleImageView imgUser;
    @BindView(R.id.tv_name_user)
    TextView tvUserName;
    @BindView(R.id.tv_comment_time)
    TextView tvCommentTime;
    @BindView(R.id.tv_evaluate_content)
    TextView tvEvaluateContent;
    @BindView(R.id.rv_evaluate_picture)
    RecyclerView rvEvaluate;
    @BindView(R.id.tv_type_goods)
    TextView tvTypeGoods;
    @BindView(R.id.scroll_container)
    ScrollViewContainer scrollContainer;
    @BindView(R.id.img_user_grade)
    ImageView imgUserGrade;
    @BindView(R.id.tv_none_love)
    TextView tvNoneLove;
    @BindView(R.id.ll_none_evaluate)
    LinearLayout llNoneEvaluate;
    @BindView(R.id.tv_none_evaluate)
    TextView tvNoneEvaluate;
    private String id;
    private CustomGoodsBean customBean;
    private long enterTime;


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
        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        enterTime = DateUtils.getCurrentTime();
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
                        customBean = GsonUtils.jsonToBean(response, CustomGoodsBean.class);
                        if (customBean.getData() != null) {
                            initView();
                        }
                    }
                });

    }


    /**
     * 加载视图
     */
    private void initView() {

        tvGoodsName.setText(customBean.getData().getName());
        tvGoodsContent.setText(customBean.getData().getSub_name());
        if (customBean.getData().getIs_collect() == 1) {
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
                }, customBean.getData().getImg_list())
                //设置两个点图片作为翻页指示器
                .setPageIndicator(new int[]{R.drawable.dot_black, R.drawable.dot_white})
                //设置指示器的方向
                .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.CENTER_HORIZONTAL);
//        bannerGoods.setOnItemClickListener(new OnItemClickListener() {
//            @Override
//            public void onItemClick(int position) {
//                Intent intent = new Intent(CustomGoodsActivity.this, ImagePreviewActivity.class);
//                intent.putExtra("currentPos", position);
//                intent.putStringArrayListExtra("img_list", customBean.getImg_list());
//                startActivity(intent);
//            }
//        });

        //喜爱人数
        if (customBean.getData().getCollect_user() != null && customBean.getData().getCollect_user().size() > 0) {
            tvCollectCount.setText("喜爱  (" + customBean.getData().getCollect_user().size() + " 人)");
            rvCollectUser.setVisibility(View.VISIBLE);
            rvCollectUser.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
            List<String> imgList = new ArrayList<>();
            for (int i = 0; i < Math.min(customBean.getData().getCollect_user().size(), 6); i++) {
                imgList.add(customBean.getData().getCollect_user().get(i).getAvatar());
            }
            CommonAdapter<String> collectAdapter = new CommonAdapter<String>(this,
                    R.layout.listitem_user_collect, imgList) {
                @Override
                protected void convert(ViewHolder holder, String s, int position) {
                    Glide.with(CustomGoodsActivity.this)
                            .load(Constant.HOST + s)
                            .centerCrop()
                            .into((ImageView) holder.getView(R.id.img_avatar_collect));
                }
            };
            rvCollectUser.setAdapter(collectAdapter);
        } else {
            tvCollectCount.setText("喜爱  (0人)");
            tvNoneLove.setVisibility(View.VISIBLE);
        }

        //评价人数
        if (customBean.getData().getComment_num() > 0) {
            tvCommentCount.setText("评价  (" + customBean.getData().getComment_num() + "人)");
            Glide.with(getApplicationContext())
                    .load(Constant.HOST + customBean.getData().getNew_comment().getAvatar())
                    .centerCrop()
                    .into(imgUser);
            tvUserName.setText(customBean.getData().getNew_comment().getUser_name());
            Glide.with(getApplicationContext())
                    .load(Constant.HOST + customBean.getData().getNew_comment().getUser_grade().getImg2())
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(imgUserGrade);
            tvCommentTime.setText(DateUtils.getDate("yyyy-MM-dd", customBean.getData().getNew_comment().getC_time()));
            tvEvaluateContent.setText(customBean.getData().getNew_comment().getContent());
            if (customBean.getData().getNew_comment().getImg_list() != null && customBean.getData()
                    .getNew_comment().getImg_list().size() > 0) {
                rvEvaluate.setLayoutManager(new GridLayoutManager(CustomGoodsActivity.this, 3));
                CommonAdapter<String> evaluateAdapter = new CommonAdapter<String>(CustomGoodsActivity
                        .this, R.layout.listitem_user_comment, customBean.getData().getNew_comment().getImg_list()) {
                    @Override
                    protected void convert(ViewHolder holder, String s, int position) {
                        Glide.with(CustomGoodsActivity.this)
                                .load(Constant.HOST + s)
                                .centerCrop()
                                .into((ImageView) holder.getView(R.id.img_user_comment));
                    }
                };
                rvEvaluate.setAdapter(evaluateAdapter);

            }

        } else {
            tvCommentCount.setText("评价  (0人)");
            tvNoneEvaluate.setVisibility(View.VISIBLE);
            llNoneEvaluate.setVisibility(View.GONE);
        }

        tvTypeGoods.setText(customBean.getData().getNew_comment().getGoods_intro());

        //详情页
        Glide.with(getApplicationContext())
                .load(Constant.HOST + customBean.getData().getContent2())
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
        DisplayUtils.setBackgroundAlpha(this, true);
        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                DisplayUtils.setBackgroundAlpha(CustomGoodsActivity.this, false);
            }
        });

        TextView tvTitle = (TextView) contentView.findViewById(R.id.tv_select_type);
        RecyclerView rvTailor = (RecyclerView) contentView.findViewById(R.id.rv_tailor_price);


        tvTitle.setText("选择价格定制区间");
        rvTailor.setLayoutManager(new LinearLayoutManager(CustomGoodsActivity.this));

        CommonAdapter<CustomGoodsBean.DataBean.PriceBean> priceAdapter = new CommonAdapter
                <CustomGoodsBean.DataBean.PriceBean>(CustomGoodsActivity.this,
                R.layout.listitem_select_price, customBean.getData().getPrice()) {
            @Override
            protected void convert(ViewHolder holder, CustomGoodsBean.DataBean.PriceBean priceBean, int position) {
                TextView tvPrice = holder.getView(R.id.tv_type_item);
                tvPrice.setTypeface(DisplayUtils.setTextType(CustomGoodsActivity.this));
                tvPrice.setText("¥" + new DecimalFormat("#0.00").format(priceBean.getPrice()));
                holder.setText(R.id.tv_type_content, priceBean.getIntroduce());
            }

        };


        priceAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                mPopupWindow.dismiss();
                Intent intent = new Intent(CustomGoodsActivity.this, CustomDiyActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("id", id);
                bundle.putString("goods_name", customBean.getData().getName());
                bundle.putString("img_url", customBean.getData().getThumb());
                bundle.putString("price", new DecimalFormat("#0.00").format(customBean.getData().
                        getPrice().get(position).getPrice()));
                bundle.putString("price_type", customBean.getData().getPrice().get(position).getId() + "");
                bundle.putInt("classify_id", customBean.getData().getClassify_id());
                bundle.putString("log_id", customBean.getId());
                bundle.putLong("goods_time", enterTime);
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
            R.id.img_tailor_share, R.id.tv_custom_goods, R.id.tv_all_evaluate})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_goods_tailor:
                if (TextUtils.isEmpty(SharedPreferencesUtils.getString(this, "token"))) {
                    Intent login = new Intent(this, LoginActivity.class);
                    login.putExtra("page_name", "定制");
                    startActivity(login);
                } else {
                    selectGoodsPrice();
//                    if (customBean.getIs_yuyue() == 1) {
//                        selectGoodsPrice();
//                    } else {
//                        Intent intent = new Intent(this, ApplyMeasureActivity.class);
//                        intent.putExtra("goods_name", customBean.getName());
//                        startActivityForResult(intent, 1);
//                    }
                }
                break;
            case R.id.tv_custom_goods:
                if (TextUtils.isEmpty(SharedPreferencesUtils.getString(this, "token"))) {
                    Intent login = new Intent(this, LoginActivity.class);
                    login.putExtra("page_name", "定制");
                    startActivity(login);
                } else {
                    selectGoodsType();
                }
                break;
            case R.id.img_tailor_back:
                customGoodsLog();
                finish();
                break;
            case R.id.img_add_like:
                if (!TextUtils.isEmpty(SharedPreferencesUtils.getString(this, "token"))) {
                    addCollection();
                } else {
                    Intent login = new Intent(this, LoginActivity.class);
                    login.putExtra("page_name", "收藏");
                    startActivity(login);
                }
                break;
            case R.id.img_tailor_consult:
                ContactService.contactService(this);
                break;
            case R.id.img_tailor_share:
                ShareUtils.showShare(this, Constant.HOST + customBean.getData().getThumb(),
                        customBean.getData().getName(),
                        customBean.getData().getContent(),
                        Constant.CUSTOM_SHARE + "?goods_id=" + id);
                break;
            case R.id.tv_all_evaluate:
                if (customBean.getData().getComment_num() > 0) {
                    Intent intent = new Intent(this, AllEvaluationActivity.class);
                    intent.putExtra("goods_id", id);
                    startActivity(intent);
                }

                break;
        }
    }

    /**
     * 定制同款选择版型
     */
    private void selectGoodsType() {

        View contentView = LayoutInflater.from(this).inflate(R.layout.ppw_select_price, null);
        final PopupWindow mPopupWindow = new PopupWindow(contentView,
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
                DisplayUtils.setBackgroundAlpha(CustomGoodsActivity.this, false);
            }
        });

        TextView tvTitle = (TextView) contentView.findViewById(R.id.tv_select_type);
        RecyclerView rvTailor = (RecyclerView) contentView.findViewById(R.id.rv_tailor_price);


        tvTitle.setText("选择版型");
        rvTailor.setLayoutManager(new LinearLayoutManager(CustomGoodsActivity.this));

        CommonAdapter<CustomGoodsBean.DataBean.BanxingListBean> typeAdapter = new CommonAdapter
                <CustomGoodsBean.DataBean.BanxingListBean>(CustomGoodsActivity.this,
                R.layout.listitem_select_type, customBean.getData().getBanxing_list()) {
            @Override
            protected void convert(ViewHolder holder, CustomGoodsBean.DataBean.BanxingListBean
                    banxingListBean, int position) {
                holder.setText(R.id.tv_item_type, banxingListBean.getName());
            }
        };

        rvTailor.setAdapter(typeAdapter);


        typeAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                mPopupWindow.dismiss();
                Intent intent;
                Bundle bundle = new Bundle();
                int classifyId = customBean.getData().getClassify_id();
                if (classifyId == 1 || classifyId == 2) {
                    intent = new Intent(CustomGoodsActivity.this, EmbroideryActivity.class);
                    bundle.putInt("classify_id", classifyId);
                } else {
                    intent = new Intent(CustomGoodsActivity.this, CustomResultActivity.class);
                }


                TailorItemBean tailorItemBean = new TailorItemBean();
                tailorItemBean.setId(id);
                tailorItemBean.setGoods_name(customBean.getData().getName());
                tailorItemBean.setPrice(new DecimalFormat("#0.00").format(customBean.getData().getDefault_price()));
                tailorItemBean.setImg_url(customBean.getData().getThumb());
                tailorItemBean.setPrice_type(customBean.getData().getPrice_id() + "");
                tailorItemBean.setLog_id(customBean.getId());
                tailorItemBean.setGoods_time(enterTime);
                tailorItemBean.setDingzhi_time(0);
                //面料
                tailorItemBean.setFabric_id(customBean.getData().getDefault_mianliao() + "");
                tailorItemBean.setBanxing_id(customBean.getData().getBanxing_list().get(position).getId() + "");

                //部件
                List<TailorItemBean.ItemBean> itemList = new ArrayList<>();

                for (int i = 0; i < customBean.getData().getDefault_spec_list().size(); i++) {
                    TailorItemBean.ItemBean itemBean = new TailorItemBean.ItemBean();
                    itemBean.setImg(customBean.getData().getDefault_spec_list().get(i).getImg_c());
                    itemBean.setPosition_id(customBean.getData().getDefault_spec_list().get(i).getPosition_id());
                    itemList.add(itemBean);
                }


                tailorItemBean.setSpec_ids(customBean.getData().getDefault_spec_ids());
                tailorItemBean.setSpec_content(customBean.getData().getDefault_spec_content());

                tailorItemBean.setItemBean(itemList);
                bundle.putSerializable("tailor", tailorItemBean);
                intent.putExtras(bundle);
                startActivity(intent);
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });

    }

    /**
     * 商品订制跟踪
     */
    private void customGoodsLog() {
        if (customBean != null) {
            OkHttpUtils.post()
                    .url(Constant.GOODS_LOG)
                    .addParams("token", SharedPreferencesUtils.getString(this, "token"))
                    .addParams("id", customBean.getId())
                    .addParams("goods_time", (DateUtils.getCurrentTime() - enterTime) + "")
                    .addParams("goods_id", id)
                    .addParams("goods_name", customBean.getData().getName())
                    .addParams("click_dingzhi", "0")
                    .addParams("click_pay", "0")
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {

                        }

                        @Override
                        public void onResponse(String response, int id) {
                            LogUtils.log("goods:" + response);
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
                .addParams("type", "2")
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
            customBean.getData().setIs_yuyue(1);
            selectGoodsPrice();
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
