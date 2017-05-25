package cn.cloudworkshop.miaoding.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseIntArray;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.cloudworkshop.miaoding.R;
import cn.cloudworkshop.miaoding.base.BaseActivity;
import cn.cloudworkshop.miaoding.bean.NewTailorBean;
import cn.cloudworkshop.miaoding.bean.TailorItemBean;
import cn.cloudworkshop.miaoding.constant.Constant;
import cn.cloudworkshop.miaoding.utils.GsonUtils;
import cn.cloudworkshop.miaoding.view.CircleImageView;
import okhttp3.Call;

/**
 * Author：binge on 2017-04-25 10:52
 * Email：1993911441@qq.com
 * Describe：定制界面
 */
public class CustomDiyActivity extends BaseActivity {
    @BindView(R.id.img_header_back)
    ImageView imgHeaderBack;
    @BindView(R.id.tv_header_title)
    TextView tvHeaderTitle;
    @BindView(R.id.tv_header_next)
    TextView tvHeaderNext;
    @BindView(R.id.tv_cloth_fabric)
    TextView tvClothFabric;
    @BindView(R.id.tv_cloth_type)
    TextView tvClothType;
    @BindView(R.id.tv_cloth_item)
    TextView tvClothItem;
    @BindView(R.id.ll_select_type)
    LinearLayout llSelectType;
    @BindView(R.id.rv_select_type)
    RecyclerView rvSelectType;
    @BindView(R.id.rv_select_item)
    RecyclerView rvSelectItem;
    @BindView(R.id.rl_cloth_positive)
    RelativeLayout rlClothPositive;
    @BindView(R.id.rl_cloth_back)
    RelativeLayout rlClothBack;
    @BindView(R.id.rl_cloth_inside)
    RelativeLayout rlClothInside;
    @BindView(R.id.rgs_select_cloth)
    RadioGroup rgsSelect;
    @BindView(R.id.img_cloth_large)
    ImageView imgClothLarge;
    @BindView(R.id.img_reset_tailor)
    ImageView imgResetTailor;

    private String id;
    private String goodsName;
    private String imgUrl;
    private String price;
    private String priceType;
    private int classifyId;
    private NewTailorBean tailorBean;

    //当前面料
    private int currentFabric = 0;
    //当前版型
    private int currentType = 0;
    //当前部件
    private int index;
    private float x1 = 0;
    private float x2 = 0;

    //稀疏数组
    //选择部件
    private SparseIntArray itemArray = new SparseIntArray();

    public static Activity tailorActivity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_diy);
        ButterKnife.bind(this);
        tailorActivity = this;
        getData();
        initData();

    }

    /**
     * 商品信息
     */
    private void getData() {
        Bundle bundle = getIntent().getExtras();
        id = bundle.getString("id");
        goodsName = bundle.getString("goods_name");
        imgUrl = bundle.getString("img_url");
        price = bundle.getString("price");
        priceType = bundle.getString("price_type");
        classifyId = bundle.getInt("classify_id");
    }

    private void initData() {
        OkHttpUtils
                .get()
                .url(Constant.NEW_TAILOR)
                .addParams("goods_id", id)
                .addParams("phone_type", String.valueOf("3"))
                .addParams("price_type", priceType)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        tailorBean = GsonUtils.jsonToBean(response, NewTailorBean.class);

                        if (tailorBean.getData() != null) {
                            initView();
                        }
                    }
                });
    }

    /**
     * 加载视图
     */
    private void initView() {
        tvHeaderTitle.setText("定制");
        tvHeaderNext.setVisibility(View.VISIBLE);
        tvHeaderNext.setText("下一步");

        ((RadioButton) rgsSelect.getChildAt(0)).setChecked(true);

        for (int i = 0; i < tailorBean.getData().getBanxin().get(currentType).getPeijian().size(); i++) {
            ImageView img1 = new ImageView(this);
            img1.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT));

            ImageView img2 = new ImageView(this);
            img2.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT));

            ImageView img3 = new ImageView(this);
            img3.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT));

            int currentItem = 0;
            if (tailorBean.getData().getBanxin().get(currentType).getPeijian().get(i).getSpec_list() != null
                    && tailorBean.getData().getBanxin().get(currentType).getPeijian().get(i).getSpec_list().size() > 0) {

                for (int j = 0; j < tailorBean.getData().getBanxin().get(currentType).getPeijian().get(i).getSpec_list().size(); j++) {
                    currentItem = j;
                    if (tailorBean.getData().getMianliao().get(currentFabric).getId() ==
                            tailorBean.getData().getBanxin().get(currentType).getPeijian().get(i).getSpec_list().get(j).getMianliao_id()) {
                        switch (tailorBean.getData().getBanxin().get(currentType).getPeijian().get(i).getPosition_id()) {
                            case 1:
                                Glide.with(getApplicationContext())
                                        .load(Constant.HOST + tailorBean.getData().getBanxin().get(currentType)
                                                .getPeijian().get(i).getSpec_list().get(j).getImg_c())
                                        .fitCenter()
                                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                                        .into(img1);
                                break;
                            case 2:
                                Glide.with(getApplicationContext())
                                        .load(Constant.HOST + tailorBean.getData().getBanxin().get(currentType)
                                                .getPeijian().get(i).getSpec_list().get(j).getImg_c())
                                        .fitCenter()
                                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                                        .into(img2);
                                break;
                            case 3:
                                rgsSelect.getChildAt(2).setVisibility(View.VISIBLE);
                                Glide.with(getApplicationContext())
                                        .load(Constant.HOST + tailorBean.getData().getBanxin().get(currentType)
                                                .getPeijian().get(i).getSpec_list().get(j).getImg_c())
                                        .fitCenter()
                                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                                        .into(img3);
                                break;
                        }

                        break;
                    }


                }


            }

            rlClothPositive.addView(img1);
            rlClothBack.addView(img2);
            rlClothInside.addView(img3);

            itemArray.put(i, currentItem);

        }


        rgsSelect.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {

                for (int m = 0; m < rgsSelect.getChildCount(); m++) {
                    RadioButton rBtn = (RadioButton) radioGroup.getChildAt(m);
                    if (rBtn.getId() == i) {
                        switch (m) {
                            case 0:
                                rlClothPositive.setVisibility(View.VISIBLE);
                                rlClothBack.setVisibility(View.GONE);
                                rlClothInside.setVisibility(View.GONE);
                                break;
                            case 1:
                                rlClothBack.setVisibility(View.VISIBLE);
                                rlClothPositive.setVisibility(View.GONE);
                                rlClothInside.setVisibility(View.GONE);
                                break;
                            case 2:
                                rlClothInside.setVisibility(View.VISIBLE);
                                rlClothPositive.setVisibility(View.GONE);
                                rlClothPositive.setVisibility(View.GONE);
                                break;
                        }


                    }
                }
            }
        });

        rlClothPositive.setOnTouchListener(new View.OnTouchListener() {
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
                            ((RadioButton) rgsSelect.getChildAt(1)).setChecked(true);
                        }
                        break;
                }
                return true;
            }
        });

        rlClothBack.setOnTouchListener(new View.OnTouchListener() {
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
                            ((RadioButton) rgsSelect.getChildAt(0)).setChecked(true);
                        } else if (x1 < x2) {
                            if (rgsSelect.getChildAt(2).getVisibility() == View.VISIBLE) {
                                ((RadioButton) rgsSelect.getChildAt(2)).setChecked(true);
                            }
                        }
                        break;
                }
                return true;
            }
        });

        rlClothInside.setOnTouchListener(new View.OnTouchListener() {
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
                            ((RadioButton) rgsSelect.getChildAt(1)).setChecked(true);
                        }
                        break;
                }
                return true;
            }
        });

        rvSelectItem.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    imgClothLarge.setVisibility(View.GONE);
                }
                return false;
            }
        });

    }


    @OnClick({R.id.img_header_back, R.id.tv_header_next, R.id.img_reset_tailor, R.id.tv_cloth_fabric
            , R.id.tv_cloth_type, R.id.tv_cloth_item})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_header_back:
                finish();
                break;
            case R.id.tv_header_next:
                nextStep();
                break;
            case R.id.img_reset_tailor:
                resetTailor();
                break;
            case R.id.tv_cloth_fabric:
                selectFabric();
                break;
            case R.id.tv_cloth_type:
                selectType();
                break;
            case R.id.tv_cloth_item:
                selectItem();
                break;
        }
    }

    /**
     * 选择面料
     */
    private void selectFabric() {
        if (tailorBean.getData().getMianliao() != null) {
            rvSelectItem.setVisibility(View.VISIBLE);
            tvHeaderTitle.setText("面料");
            tvClothFabric.setTextColor(Color.WHITE);
            tvClothFabric.setBackgroundResource(R.drawable.circle_black);
            tvClothType.setTextColor(ContextCompat.getColor(this, R.color.dark_gray_22));
            tvClothType.setBackgroundResource(R.drawable.ring_gray);
            final CommonAdapter<NewTailorBean.DataBean.MianliaoBean> fabricAdapter = new
                    CommonAdapter<NewTailorBean.DataBean.MianliaoBean>(CustomDiyActivity.this, R.layout.listitem_custom_parts,
                            tailorBean.getData().getMianliao()) {
                        @Override
                        protected void convert(ViewHolder holder, NewTailorBean.DataBean.MianliaoBean
                                mianliaoBean, int position) {
                            Glide.with(CustomDiyActivity.this)
                                    .load(Constant.HOST + mianliaoBean.getImg_a())
                                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                                    .into((ImageView) holder.getView(R.id.img_tailor_item));
                            if (currentFabric == position) {
                                holder.setVisible(R.id.img_tailor_bg, true);
                            } else {
                                holder.setVisible(R.id.img_tailor_bg, false);
                            }
                        }
                    };
            rvSelectItem.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
            rvSelectItem.setAdapter(fabricAdapter);


            fabricAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                    currentFabric = position;
                    fabricAdapter.notifyDataSetChanged();
                    tvHeaderTitle.setText(tailorBean.getData().getMianliao().get(position).getName());
                    if (imgClothLarge.getVisibility() == View.VISIBLE) {
                        imgClothLarge.setVisibility(View.GONE);
                    }



                    for (int i = 0; i < tailorBean.getData().getBanxin().get(currentType)
                            .getPeijian().size(); i++) {
                        if (tailorBean.getData().getBanxin().get(currentType).getPeijian().get(i)
                                .getSpec_list() != null && tailorBean.getData().getBanxin()
                                .get(currentType).getPeijian().get(i).getSpec_list().size() > 0) {

                            for (int j = 0; j < tailorBean.getData().getBanxin().get(currentType)
                                    .getPeijian().get(i).getSpec_list().size(); j++) {

                                if (tailorBean.getData().getMianliao().get(currentFabric).getId() ==
                                        tailorBean.getData().getBanxin().get(currentType).getPeijian()
                                                .get(i).getSpec_list().get(j).getMianliao_id()) {
                                    //选择正反面
                                    int positionId = tailorBean.getData().getBanxin().get(currentType)
                                            .getPeijian().get(i).getPosition_id();
                                    switch (positionId) {
                                        case 1:
                                            ImageView positiveImg = (ImageView) rlClothPositive.getChildAt(i);
                                            Glide.with(CustomDiyActivity.this)
                                                    .load(Constant.HOST + tailorBean.getData().getBanxin()
                                                            .get(currentType).getPeijian().get(i)
                                                            .getSpec_list().get(0).getImg_c())
                                                    .fitCenter()
                                                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                                                    .into(positiveImg);

                                            break;
                                        case 2:
                                            ImageView backImg = (ImageView) rlClothBack.getChildAt(i);
                                            Glide.with(CustomDiyActivity.this)
                                                    .load(Constant.HOST + tailorBean.getData().getBanxin()
                                                            .get(currentType).getPeijian().get(i)
                                                            .getSpec_list().get(0).getImg_c())
                                                    .fitCenter()
                                                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                                                    .into(backImg);

                                            break;
                                        case 3:
                                            ImageView inSideImg = (ImageView) rlClothInside.getChildAt(i);
                                            Glide.with(CustomDiyActivity.this)
                                                    .load(Constant.HOST + tailorBean.getData().getBanxin()
                                                            .get(currentType).getPeijian().get(i)
                                                            .getSpec_list().get(0).getImg_c())
                                                    .fitCenter()
                                                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                                                    .into(inSideImg);
                                            break;
                                    }
                                    break;
                                }

                            }

                        }
                    }


                    imgResetTailor.setVisibility(View.VISIBLE);
                }

                @Override
                public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {

                    Glide.with(CustomDiyActivity.this)
                            .load(Constant.HOST + tailorBean.getData().getMianliao().get(position).getImg_b())
                            .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                            .into(imgClothLarge);
                    if (imgClothLarge.getVisibility() == View.GONE) {
                        imgClothLarge.setVisibility(View.VISIBLE);
                    }
                    return false;
                }
            });
        }

    }

    /**
     * 重置
     */
    private void resetTailor() {
        imgResetTailor.setVisibility(View.GONE);
        itemArray.clear();
        llSelectType.setVisibility(View.VISIBLE);
        rvSelectType.setVisibility(View.INVISIBLE);
        rvSelectItem.setVisibility(View.INVISIBLE);
        currentFabric = 0;
        currentType = 0;
        tvClothFabric.setTextColor(ContextCompat.getColor(this, R.color.dark_gray_22));
        tvClothFabric.setBackgroundResource(R.drawable.ring_gray);
        tvClothType.setTextColor(ContextCompat.getColor(this, R.color.dark_gray_22));
        tvClothType.setBackgroundResource(R.drawable.ring_gray);
        rlClothPositive.removeAllViews();
        rlClothBack.removeAllViews();
        rlClothInside.removeAllViews();
        initView();
    }


    /**
     * 选择版型
     */
    private void selectType() {
        if (tailorBean.getData().getBanxin() != null) {
            rvSelectItem.setVisibility(View.VISIBLE);
            tvHeaderTitle.setText("版型");
            tvClothType.setTextColor(Color.WHITE);
            tvClothType.setBackgroundResource(R.drawable.circle_black);
            tvClothFabric.setTextColor(ContextCompat.getColor(this, R.color.dark_gray_22));
            tvClothFabric.setBackgroundResource(R.drawable.ring_gray);
            final CommonAdapter<NewTailorBean.DataBean.BanxinBean> typeAdapter = new
                    CommonAdapter<NewTailorBean.DataBean.BanxinBean>(CustomDiyActivity.this,
                            R.layout.listitem_custom_parts, tailorBean.getData().getBanxin()) {
                        @Override
                        protected void convert(ViewHolder holder, NewTailorBean.DataBean.BanxinBean
                                banxinBean, int position) {
                            Glide.with(CustomDiyActivity.this)
                                    .load(Constant.HOST + banxinBean.getImg_a())
                                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                                    .into((ImageView) holder.getView(R.id.img_tailor_item));
                            if (currentType == position) {
                                holder.setVisible(R.id.img_tailor_bg, true);
                            } else {
                                holder.setVisible(R.id.img_tailor_bg, false);
                            }
                        }
                    };
            rvSelectItem.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
            rvSelectItem.setAdapter(typeAdapter);
            typeAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                    currentType = position;
                    typeAdapter.notifyDataSetChanged();
                    tvHeaderTitle.setText(tailorBean.getData().getBanxin().get(position).getName());
                    if (imgClothLarge.getVisibility() == View.VISIBLE) {
                        imgClothLarge.setVisibility(View.GONE);
                    }


                    for (int i = 0; i < tailorBean.getData().getBanxin().get(currentType)
                            .getPeijian().size(); i++) {
                        if (tailorBean.getData().getBanxin().get(currentType).getPeijian().get(i)
                                .getSpec_list() != null && tailorBean.getData().getBanxin()
                                .get(currentType).getPeijian().get(i).getSpec_list().size() > 0) {

                            for (int j = 0; j < tailorBean.getData().getBanxin().get(currentType)
                                    .getPeijian().get(i).getSpec_list().size(); j++) {

                                if (tailorBean.getData().getMianliao().get(currentFabric).getId() ==
                                        tailorBean.getData().getBanxin().get(currentType).getPeijian()
                                                .get(i).getSpec_list().get(j).getMianliao_id()) {
                                    //选择正反面
                                    int positionId = tailorBean.getData().getBanxin().get(currentType)
                                            .getPeijian().get(i).getPosition_id();
                                    switch (positionId) {
                                        case 1:
                                            ImageView positiveImg = (ImageView) rlClothPositive.getChildAt(i);
                                            Glide.with(CustomDiyActivity.this)
                                                    .load(Constant.HOST + tailorBean.getData().getBanxin()
                                                            .get(currentType).getPeijian().get(i)
                                                            .getSpec_list().get(0).getImg_c())
                                                    .fitCenter()
                                                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                                                    .into(positiveImg);

                                            break;
                                        case 2:
                                            ImageView backImg = (ImageView) rlClothBack.getChildAt(i);
                                            Glide.with(CustomDiyActivity.this)
                                                    .load(Constant.HOST + tailorBean.getData().getBanxin()
                                                            .get(currentType).getPeijian().get(i)
                                                            .getSpec_list().get(0).getImg_c())
                                                    .fitCenter()
                                                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                                                    .into(backImg);

                                            break;
                                        case 3:
                                            ImageView inSideImg = (ImageView) rlClothInside.getChildAt(i);
                                            Glide.with(CustomDiyActivity.this)
                                                    .load(Constant.HOST + tailorBean.getData().getBanxin()
                                                            .get(currentType).getPeijian().get(i)
                                                            .getSpec_list().get(0).getImg_c())
                                                    .fitCenter()
                                                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                                                    .into(inSideImg);
                                            break;
                                    }
                                    break;
                                }

                            }

                        }
                    }

                    imgResetTailor.setVisibility(View.VISIBLE);
                }

                @Override
                public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {

                    Glide.with(CustomDiyActivity.this)
                            .load(Constant.HOST + tailorBean.getData().getBanxin().get(position).getImg_b())
                            .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                            .into(imgClothLarge);
                    if (imgClothLarge.getVisibility() == View.GONE) {
                        imgClothLarge.setVisibility(View.VISIBLE);
                    }
                    return false;
                }
            });
        }

    }

    /**
     * 选择细节
     */
    private void selectItem() {
        tvHeaderTitle.setText("细节");
        llSelectType.setVisibility(View.INVISIBLE);
        rvSelectType.setVisibility(View.VISIBLE);
        rvSelectItem.setVisibility(View.INVISIBLE);
        imgResetTailor.setVisibility(View.VISIBLE);
        CommonAdapter<NewTailorBean.DataBean.BanxinBean.PeijianBean> partsAdapter = new
                CommonAdapter<NewTailorBean.DataBean.BanxinBean.PeijianBean>
                        (CustomDiyActivity.this, R.layout.listitem_custom_parts, tailorBean.getData()
                                .getBanxin().get(currentType).getPeijian()) {
                    @Override
                    protected void convert(ViewHolder holder, NewTailorBean.DataBean.BanxinBean
                            .PeijianBean peijianBean, int position) {

                        Glide.with(CustomDiyActivity.this)
                                .load(Constant.HOST + peijianBean.getImg_a())
                                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                                .into((ImageView) holder.getView(R.id.img_tailor_item));

                    }
                };

        rvSelectType.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL
                , false));
        rvSelectType.setAdapter(partsAdapter);
        partsAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                index = position;
                rvSelectItem.setVisibility(View.VISIBLE);
                tvHeaderTitle.setText(tailorBean.getData().getBanxin().get(currentType)
                        .getPeijian().get(index).getName());

                ((RadioButton) rgsSelect.getChildAt(tailorBean.getData().getBanxin().get(currentType)
                        .getPeijian().get(index).getPosition_id() - 1)).setChecked(true);

                final CommonAdapter<NewTailorBean.DataBean.BanxinBean.PeijianBean.SpecListBean>
                        itemAdapter = new CommonAdapter<NewTailorBean.DataBean.BanxinBean.PeijianBean
                        .SpecListBean>(CustomDiyActivity.this,
                        R.layout.listitem_custom_parts, tailorBean.getData().getBanxin()
                        .get(currentType).getPeijian().get(position).getSpec_list()) {
                    @Override
                    protected void convert(ViewHolder holder, NewTailorBean.DataBean
                            .BanxinBean.PeijianBean.SpecListBean specListBean, int position) {
                        if (tailorBean.getData().getMianliao().get(currentFabric).getId() ==
                                specListBean.getMianliao_id()) {
                            Glide.with(CustomDiyActivity.this)
                                    .load(Constant.HOST + specListBean.getImg_a())
                                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                                    .into((ImageView) holder.getView(R.id.img_tailor_item));
                            if (itemArray.get(index) == position) {
                                holder.setVisible(R.id.img_tailor_bg, true);
                            } else {
                                holder.setVisible(R.id.img_tailor_bg, false);
                            }
                        }

                    }

                };
                rvSelectItem.setLayoutManager(new LinearLayoutManager(CustomDiyActivity.this,
                        LinearLayoutManager.HORIZONTAL, false));
                rvSelectItem.setAdapter(itemAdapter);


                itemAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {

                        if (tailorBean.getData().getBanxin().get(currentType).getPeijian()
                                .get(index).getSpec_list().get(position) != null) {
                            if (imgClothLarge.getVisibility() == View.VISIBLE) {
                                imgClothLarge.setVisibility(View.GONE);
                            }


                            CircleImageView imgItem = (CircleImageView) rvSelectType
                                    .findViewHolderForAdapterPosition(index)
                                    .itemView.findViewById(R.id.img_tailor_item);


                            Glide.with(CustomDiyActivity.this)
                                    .load(Constant.HOST + tailorBean.getData().getBanxin()
                                            .get(currentType).getPeijian().get(index)
                                            .getSpec_list().get(position).getImg_a())
                                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                                    .into(imgItem);

                            View itemBg = rvSelectType.findViewHolderForAdapterPosition(index)
                                    .itemView.findViewById(R.id.view_tailor_item);
                            itemBg.setVisibility(View.VISIBLE);


                            //选择正反面
                            int positionId = tailorBean.getData().getBanxin().get(currentType)
                                    .getPeijian().get(index).getPosition_id();
                            ((RadioButton) rgsSelect.getChildAt(positionId - 1)).setChecked(true);

                            switch (positionId) {
                                case 1:
                                    ImageView positiveImg = (ImageView) rlClothPositive.getChildAt(index);
                                    Glide.with(CustomDiyActivity.this)
                                            .load(Constant.HOST + tailorBean.getData().getBanxin()
                                                    .get(currentType).getPeijian().get(index)
                                                    .getSpec_list().get(position).getImg_c())
                                            .fitCenter()
                                            .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                                            .into(positiveImg);

                                    break;
                                case 2:
                                    ImageView backImg = (ImageView) rlClothBack.getChildAt(index);
                                    Glide.with(CustomDiyActivity.this)
                                            .load(Constant.HOST + tailorBean.getData().getBanxin()
                                                    .get(currentType).getPeijian().get(index)
                                                    .getSpec_list().get(position).getImg_c())
                                            .fitCenter()
                                            .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                                            .into(backImg);

                                    break;
                                case 3:
                                    ImageView inSideImg = (ImageView) rlClothInside.getChildAt(index);
                                    Glide.with(CustomDiyActivity.this)
                                            .load(Constant.HOST + tailorBean.getData().getBanxin()
                                                    .get(currentType).getPeijian().get(index)
                                                    .getSpec_list().get(position).getImg_c())
                                            .fitCenter()
                                            .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                                            .into(inSideImg);
                                    break;

                            }

                            tvHeaderTitle.setText(tailorBean.getData().getBanxin()
                                    .get(currentType).getPeijian().get(index)
                                    .getSpec_list().get(position).getName());
                            itemArray.put(index, position);
                            itemAdapter.notifyDataSetChanged();

                        }

                    }

                    @Override
                    public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {

                        Glide.with(CustomDiyActivity.this)
                                .load(Constant.HOST + tailorBean.getData().getBanxin()
                                        .get(currentType).getPeijian().get(index).getSpec_list()
                                        .get(position).getImg_b())
                                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                                .into(imgClothLarge);
                        if (imgClothLarge.getVisibility() == View.GONE) {
                            imgClothLarge.setVisibility(View.VISIBLE);
                        }
                        return false;
                    }
                });


            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });


    }

    /**
     * 下一步
     */
    private void nextStep() {
        Intent intent;
        Bundle bundle = new Bundle();

        if (classifyId == 1 || classifyId == 2) {
            intent = new Intent(this, EmbroideryActivity.class);
            bundle.putInt("classify_id", classifyId);
        } else {
            intent = new Intent(this, CustomResultActivity.class);
        }


        TailorItemBean tailorItemBean = new TailorItemBean();
        tailorItemBean.setId(id);
        tailorItemBean.setGoods_name(goodsName);
        tailorItemBean.setPrice(price);
        tailorItemBean.setImg_url(imgUrl);
        tailorItemBean.setPrice_type(priceType);
        //面料
        tailorItemBean.setFabric_id(tailorBean.getData().getMianliao().get(currentFabric).getId() + "");


        //部件
        List<TailorItemBean.ItemBean> itemList = new ArrayList<>();
        StringBuilder sbIds = new StringBuilder();
        StringBuilder sbContent = new StringBuilder();
        for (int i = 0; i < itemArray.size(); i++) {
            TailorItemBean.ItemBean itemBean = new TailorItemBean.ItemBean();
            int key = itemArray.keyAt(i);
            int value = itemArray.valueAt(i);
            if (tailorBean.getData().getBanxin().get(currentType).getPeijian()
                    .get(key).getSpec_list().get(value) != null) {

                sbIds.append(tailorBean.getData().getBanxin().get(currentType).getPeijian()
                        .get(key).getSpec_list().get(value).getId())
                        .append(",");

                sbContent.append(tailorBean.getData().getBanxin().get(currentType).getPeijian()
                        .get(key).getName())
                        .append(":")
                        .append(tailorBean.getData().getBanxin().get(currentType).getPeijian()
                                .get(key).getSpec_list().get(value).getName())
                        .append(";");

                itemBean.setImg(tailorBean.getData().getBanxin().get(currentType)
                        .getPeijian().get(key).getSpec_list().get(value).getImg_c());
                itemBean.setPosition_id(tailorBean.getData().getBanxin().get(currentType)
                        .getPeijian().get(key).getPosition_id());

                itemList.add(itemBean);

            }
        }

        sbContent.append("面料:")
                .append(tailorBean.getData().getMianliao().get(currentFabric).getName())
                .append(";");

        tailorItemBean.setSpec_ids(sbIds.deleteCharAt(sbIds.length() - 1).toString());
        tailorItemBean.setSpec_content(sbContent.toString());

        tailorItemBean.setItemBean(itemList);
        bundle.putSerializable("tailor", tailorItemBean);
        intent.putExtras(bundle);
        startActivity(intent);

    }


    /**
     * 是否选择完毕
     */

    private void isAllSelect() {
        if (currentFabric != -1 && currentType != -1 && itemArray.size() == tailorBean.getData()
                .getBanxin().get(currentType).getPeijian().size()) {
            tvHeaderNext.setVisibility(View.VISIBLE);
            tvHeaderNext.setText("下一步");
        }
    }

}
